package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.mixins.PropertyMixIn;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = AbstractProvisioningTest.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, PropertyMixIn.class, HornetQMixIn.class })
public class MotionBindingTest extends AbstractProvisioningTest {

	@ServiceOperation(PROVISIONING_SERVICE_NAME)
	private Invoker service;

	@Test
	public void testRouteMail() throws Exception {

		mockServices().internet().mail().tv().result();

		final String RESPONSE = "MAIL00002 OK";
		mailService.replyWithOut(RESPONSE);

		service.sendInOnly(TestMessages.generateMailMessage());

		checkMessageCount().mail(1).internet(0).tv(0).result(1);

		assertEquals("Service Response", RESPONSE, resultContent());
	}

	@Test
	public void testCallMotion() throws Exception {

		mockServices().result();

		setupMotionSkript();

		final String RESPONSE = testKit
				.readResourceString("expectedMotionResult.xml");

		service.sendInOnly(TestMessages.generateMailMessage());

		checkMessageCount().result(1);
		assertEquals("Service Response", RESPONSE, resultContent());
	}

	@Test
	public void testMailAndReadFromResultQueue() throws Exception {

		setupMotionSkript();

		service.sendInOnly(TestMessages.generateMailMessage());
		
		checkQueue(ServiceDefinitions.RESULT_QUEUE, 
				testKit.readResourceString("expectedMotionResult.xml"));
	}

	public static void setupMotionSkript() throws URISyntaxException {
		File skript = new File(ClassLoader.getSystemResource("test_motion1.sh")
				.toURI());
		skript.setExecutable(true);
		System.setProperty("sy.poc.motion", skript.getAbsolutePath());
	}

}
