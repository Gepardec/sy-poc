package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertEquals;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;
import org.switchyard.test.mixins.PropertyMixIn;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = AbstractProvisioningTest.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, PropertyMixIn.class, HornetQMixIn.class })
public class HornetQBindingTest extends AbstractProvisioningTest {

	private SwitchYardTestKit testKit;

	@ServiceOperation(ServiceDefinitions.SVC_INCOMING_MESSAGE)
	private Invoker service;

	@Test
	public void testSendXmlToQueue() throws Exception {

		mockServices().tv().internet().mail().result();

		sendMessage("message_request_1_0.xml", ServiceDefinitions.INCOMING_QUEUE);

		waitFor().tv().andCheckMessageCount()
			.tv(1).internet(0).mail(0).result(0);
		assertEquals("Service Response",
				testKit.readResourceString("conax_request2.txt"), tvContent());
	}

	@Test
	public void testConaxResultIsSentToResponseQueue() throws Exception {
		
		copyToDir("ps852502.emm.esbDone", CONAX_OK_DIR);

		hornetQMixIn.readJMSMessageAndTestString(
				receiveMessage(ServiceDefinitions.RESULT_QUEUE), 
				testKit.readResourceString("ps852502.emm.result.xml"));
	}

	public void sendMessage(String fileName, String queueName) throws Exception {

		Session session = hornetQMixIn.createJMSSession();
		final MessageProducer producer = session.createProducer(HornetQMixIn
				.getJMSQueue(queueName));
		Message message = hornetQMixIn.createJMSMessageFromResource(fileName);
		producer.send(message);
		System.out.println("Message sent from file " + fileName + " to "
				+ queueName);
	}

}
