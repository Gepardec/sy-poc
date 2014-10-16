package com.gepardec.sy_poc.tools;

import java.util.Hashtable;

import javax.inject.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that assist sending messages to JMS Topic or Queue
 * 
 * @author eerofeev
 * 
 */
@Singleton
public class JMSSender {

	private static Logger log = LoggerFactory.getLogger(JMSSender.class);

	public final static String JNDI_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	public final static String REMOTE_JMS = "remote";
	public final static String REMOTE_TOPIC_USER_NAME = "remote_topic_user_name";
	public final static String REMOTE_TOPIC_PASSWORD = "remote_topic_password";

	/**
	 * Properties to remote connection
	 */
	private Hashtable<String, String> properties;

	private String jndiTopic;
	private String jndiQueue;
	private String jndiConnectionFactory;
	private String messageBody;

	public JMSSender() {
		initSender();
	}

	private void initSender() {
		properties = new Hashtable<String, String>();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
	}

	public JMSSender property(String key, String value) {
		if (key != null) {
			properties.put(key, value);
		}

		return this;
	}

	/**
	 * Sets jndi topic name
	 * 
	 * @param jndiTopicName
	 * @return current instance of Agent
	 */
	public JMSSender topic(String jndiTopicName) {
		jndiTopic = jndiTopicName;
		return this;
	}
	
	/**
	 * Sets jndi queue name
	 * 
	 * @param jndiQueueName
	 * @return current instance of Agent
	 */
	public JMSSender queue(String jndiQueueName) {
		jndiQueue = jndiQueueName;
		return this;
	}

	/**
	 * Sets jndi Connection Factory name
	 * 
	 * @param jndiCFName
	 * @return current instance of Agent
	 */
	public JMSSender connectionFactory(String jndiCFName) {
		jndiConnectionFactory = jndiCFName;
		return this;
	}
	
	/**
	 * Sets message body
	 * 
	 * @param status
	 * @return current instance of Agent
	 */
	public JMSSender body(String body) {
		messageBody = body;
		return this;
	}

	/**
	 * Creates initial context for message sending
	 * 
	 * @param remote
	 *            <code>true</code> if context for remote messaging must be
	 *            created. In this case properties are used as source of
	 *            necessary data. To force Agent to perform remote connecting property with key "remote" must
	 *            not be null
	 * @return
	 */
	private InitialContext getInitialContext(boolean remote) {

		try {
			if (!remote) {
				return new InitialContext();
			} else {
				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						properties.get(Context.INITIAL_CONTEXT_FACTORY));
				env.put(Context.PROVIDER_URL,
						properties.get(Context.PROVIDER_URL));
				env.put(Context.SECURITY_PRINCIPAL,
						properties.get(Context.SECURITY_PRINCIPAL));
				env.put(Context.SECURITY_CREDENTIALS,
						properties.get(Context.SECURITY_CREDENTIALS));
				return new InitialContext(env);
			}
		} catch (NamingException e) {
			log.error("{}", e);
			return null;
		}
	}

	/**
	 * Sends message to topic
	 */
	public void send() {
		if (jndiTopic == null && jndiQueue == null) {
			log.warn("Neither JNDI topic name nor JNDI queue name is set, use topic()/queue() for this.");
			return;
		}

		if (jndiConnectionFactory == null) {
			log.warn("JNDI Connection Factory name is not set, use cf() for this.");
			return;
		}

		Connection connection = null;

		try {
			// connect to remote or local topic
			Context ic = getInitialContext(properties.get(REMOTE_JMS) != null);
			ConnectionFactory cf = (ConnectionFactory) ic.lookup(jndiConnectionFactory);
			Destination destination = connectToDestination(ic, cf);
			
			//give credentials for remote connection
			connection = createConnection(cf);
			sendMessageToDestination(messageBody, connection, destination);

		} catch (Exception e) {
			log.warn("Exception occured {}", e);
		} finally {

			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendMessageToDestination(String msg, Connection connection,
			Destination destination) throws JMSException {
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		MessageProducer publisher = session.createProducer(destination);
		connection.start();

		TextMessage message = session.createTextMessage(msg);
		System.out.println("Agent sent message: " + msg);
		publisher.send(message);
	}

	private Connection createConnection(ConnectionFactory cf) throws JMSException {
		if(properties.get(REMOTE_JMS) != null && !properties.get(REMOTE_JMS).equals("false")){
			return cf.createConnection(
					properties.get(REMOTE_TOPIC_USER_NAME),
					properties.get(REMOTE_TOPIC_PASSWORD)
			);
		} else {
			return cf.createConnection();
		}
	}

	private Destination connectToDestination(Context ic, ConnectionFactory cf) throws NamingException {
		if(jndiTopic != null){
			return (Topic) ic.lookup(jndiTopic);
		} else {
			return (Queue) ic.lookup(jndiQueue);
		}
	}
}
