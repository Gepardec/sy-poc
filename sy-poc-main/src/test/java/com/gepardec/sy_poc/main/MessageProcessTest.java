package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

import com.gepardec.sy_poc.xml.message_request_1_0.Message;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = AbstractProvisioningTest.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, HornetQMixIn.class, HTTPMixIn.class })
public class MessageProcessTest extends AbstractProvisioningTest{

	@ServiceOperation(PROVISIONING_SERVICE_NAME)
	private Invoker service;
			
	
	@AfterClass
	public static void after(){
		new File("provisioning").delete();
	}

	@Test
	public void testProceedInternet() throws Exception {
		
		mockServices().internet().mail().tv().result();

		internetService.replyWithOut("INET00001 OK");
		
		service.sendInOnly(TestMessages.generateInternetMessage());

		checkMessages().mail(0).internet(1).tv(0).result(1);
		
		assertTrue("Incognito wird aufgerufen", internetContent().contains("INET00001"));
		assertTrue("Result Service wird aufgerufen", resultContent().contains("INET00001"));
	}
	
	public void testProceedMail() throws Exception {
		
		mockServices().internet().mail().tv().result();

		Message message = TestMessages.generateMailMessage();
		service.sendInOnly(message);

		checkMessages().mail(1).internet(0).tv(0).result(1);
		
		assertTrue("Motion wird aufgerufen", mailContent().contains("IDMail OK"));
		assertTrue("Result Service wird aufgerufen", resultContent().contains("IDMail OK"));
	}
	
	public void testProceedTV() throws Exception {
		
		mockServices().internet().mail().tv().result();

		service.sendInOnly(TestMessages.generateTvMessage());

		checkMessages().mail(0).internet(0).tv(1).result(1);
		
	}
	

}
