package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.io.FileUtils;
import org.switchyard.Exchange;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.MockHandler;
import org.switchyard.test.SwitchYardTestKit;
import org.switchyard.test.mixins.PropertyMixIn;


abstract public class AbstractProvisioningTest {

	protected SwitchYardTestKit testKit;
	protected HornetQMixIn hornetQMixIn;
	
	protected MockHandler internetService;
	protected MockHandler tvService;
	protected MockHandler mailService;
	protected MockHandler resultService;
	protected MockHandler singleMessageService;
	protected MockHandler incomingService;
	
	protected static final String PROVISIONING_SERVICE_NAME = "IncomingMessage";
	public static final String SWITCHYARD_XML = "src/main/resources/META-INF/switchyard.xml";
	
	public static final String CONAX_BASE_DIR = "target/conax";
	public static final String CONAX_OK_DIR = CONAX_BASE_DIR + "/ok";
	public static final String BATCH_DIR = "target/batch";
	   
	protected PropertyMixIn propMixIn;
	   
	
	@BeforeDeploy
    public void setTestProperties() {
        propMixIn.set("sy.poc.batch.dir", BATCH_DIR);
        propMixIn.set("sy.poc.conax.dir", CONAX_BASE_DIR);
    }

	public void printMessages(MockHandler mockSvc, String txt) {
		for (Exchange exchange : mockSvc.getMessages()) {
			System.out.println( txt + ": " + exchange.getMessage().getContent(String.class) );
		}
	}

	protected void copyToDir(String file, String dir) throws IOException {
		String msg = testKit.readResourceString(file);
		FileUtils.writeStringToFile(new File(dir + '/' + file), msg);
	}
	
	protected Message receiveMessage(String queueName) throws Exception {

		Session session = hornetQMixIn.createJMSSession();
		final MessageConsumer consumer = session.createConsumer(HornetQMixIn
				.getJMSQueue(queueName));
		Message message = consumer.receive(3000);
		assertNotNull("Got message", message);
		if (message instanceof TextMessage) {
			System.out.println("TextMessage");
		} else if (message instanceof ObjectMessage) {
			System.out.println("ObjectMessage");
		} else if (message instanceof BytesMessage) {
			System.out.println("BytesMessage");
		} else {
			System.out.println("Message has type "
					+ message.getClass().getName());
		}
		return message;
	}

	public MockHandler getInOutMockHandler(String svc) {
		testKit.removeService(svc);
		return testKit.registerInOutService(svc);
	}
	public MockHandler getInOnlyMockHandler(String svc) {
		testKit.removeService(svc);
		return testKit.registerInOnlyService(svc);
	}
	
	protected String resultContent() {
		return resultService.getMessages().iterator().next().getMessage().getContent(String.class);
	}

	protected com.gepardec.sy_poc.xml.message_response_1_0.Message resultContentMessage() {
		return resultService.getMessages().iterator().next().getMessage().getContent( com.gepardec.sy_poc.xml.message_response_1_0.Message.class);
	}

	protected  com.gepardec.sy_poc.xml.message_request_1_0.Message incomingContentMessage() {
		return incomingService.getMessages().iterator().next().getMessage().getContent( com.gepardec.sy_poc.xml.message_request_1_0.Message.class);
	}

	protected String mailContent() {
		return mailService.getMessages().iterator().next().getMessage().getContent(String.class);
	}
	
	protected String internetContent() {
		return internetService.getMessages().iterator().next().getMessage().getContent(String.class);
	}
	
	protected String tvContent() {
		return tvService.getMessages().iterator().next().getMessage().getContent(String.class);
	}
	
	protected String singleMessageContent() {
		return singleMessageService.getMessages().iterator().next().getMessage().getContent(String.class);
	}
	

	protected MockCreator mockServices() {
		return new MockCreator();
	}
	
	protected class MockCreator{


		protected MockCreator result() {
			resultService = getInOnlyMockHandler(ServiceDefinitions.SVC_SEND_RESULT);
			return this;
		}

		protected MockCreator tv() {
			tvService = getInOnlyMockHandler(ServiceDefinitions.SVC_CALL_TV);
			return this;
		}

		protected MockCreator mail() {
			mailService = getInOutMockHandler(ServiceDefinitions.SVC_CALL_MAIL);
			return this;
		}

		protected MockCreator internet() {
			internetService = getInOutMockHandler(ServiceDefinitions.SVC_CALL_INTERNET);
			return this;
		}		
		protected MockCreator singleMessage() {
			singleMessageService = getInOnlyMockHandler(ServiceDefinitions.SVC_SINGLE_MESSAGE);
			return this;
		}

		public MockCreator incoming() {
			incomingService = getInOnlyMockHandler(ServiceDefinitions.SVC_INCOMING_MESSAGE);
			return this;
		}		
	}

	protected MessageCompare checkMessageCount() {
		return new MessageCompare();
	}
	
	protected class MessageCompare{

		public MessageCompare mail(int cnt) {
			assertEquals(ServiceDefinitions.SVC_CALL_MAIL, cnt, mailService.getMessages().size());
			return this;
		}

		public MessageCompare internet(int cnt) {
			assertEquals(ServiceDefinitions.SVC_CALL_INTERNET, cnt, internetService.getMessages().size());
			return this;
		}
		public MessageCompare tv(int cnt) {
			assertEquals(ServiceDefinitions.SVC_CALL_TV, cnt, tvService.getMessages().size());
			return this;
		}
		public MessageCompare singleMessage(int cnt) {
			assertEquals(ServiceDefinitions.SVC_SINGLE_MESSAGE, cnt, singleMessageService.getMessages().size());
			return this;
		}
		public MessageCompare result(int cnt) {
			assertEquals(ServiceDefinitions.SVC_SEND_RESULT, cnt, resultService.getMessages().size());
			return this;
		}
		public MessageCompare incoming(int cnt) {
			assertEquals(ServiceDefinitions.SVC_INCOMING_MESSAGE, cnt, incomingService.getMessages().size());
			return this;
		}
		
	}
	
	protected MessageWait waitFor() {
		return new MessageWait();
	}
	protected class MessageWait{

		public MessageWait mail() {
			mailService.waitForOKMessage();
			return this;
		}

		public MessageWait internet() {
			internetService.waitForOKMessage();
			return this;
		}
		public MessageWait tv() {
			tvService.waitForOKMessage();
			return this;
		}
		public MessageWait singleMessage() {
			singleMessageService.waitForOKMessage();
			return this;
		}
		public MessageWait result() {
			resultService.waitForOKMessage();
			return this;
		}
		public MessageWait incoming() {
			incomingService.waitForOKMessage();
			return this;
		}
		public MessageCompare andCheckMessageCount(){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return checkMessageCount();
		}
	}

}
