package com.gepardec.sy_poc.simulation.tools;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Singleton;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(name = "MessageMDBSample", activationConfig = {
@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/outgoingQueue"),
@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")})
@Singleton
public class QueueMessageListener implements MessageListener{
	
	
	public void onMessage(Message arg0) {		
		try {
			//System.out.println("MDB Instance: " + this);
			System.out.println("QueueMessageListener received a message: " + ((TextMessage)arg0).getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}