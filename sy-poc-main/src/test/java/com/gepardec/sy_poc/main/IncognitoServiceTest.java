package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

import com.gepardec.sy_poc.main.ServiceDefinitions;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = AbstractProvisioningTest.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, HornetQMixIn.class })
public class IncognitoServiceTest extends AbstractProvisioningTest {

	private SwitchYardTestKit testKit;
	@ServiceOperation(PROVISIONING_SERVICE_NAME)
	private Invoker service;

	@BeforeClass
	public static void before() throws IOException {
		setupConfig();
	}

	@Test
	public void testInternetAndReadFromResultQueue() throws Exception {

		mockServices().internet();

		String result = "INET00001 OK";
		internetService.replyWithOut(result);
		
		service.sendInOnly(TestMessages.generateInternetMessage());

		hornetQMixIn.readJMSMessageAndTestString(
				receiveMessage(ServiceDefinitions.RESULT_QUEUE), 
				result);
	}
	
	/**
	 * Funktioniert nur als Integrationstest mit laufendem JBoss.
	 */
	@Test @Ignore
	public void testInternetWithoutMock() throws Exception {

		String result = "INET00001 OK";
		
		service.sendInOnly(TestMessages.generateInternetMessage());

		hornetQMixIn.readJMSMessageAndTestString(
				receiveMessage(ServiceDefinitions.RESULT_QUEUE), 
				result);
	}
}
