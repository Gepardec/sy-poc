package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
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
		CDIMixIn.class, PropertyMixIn.class, HornetQMixIn.class})
public class ConaxServiceTest extends AbstractProvisioningTest {

	private SwitchYardTestKit testKit;
	@ServiceOperation(PROVISIONING_SERVICE_NAME)
	private Invoker service;

	@Test
	public void whenIncomingTvMessageThenTransformedConaxFormatInTvMockService()
			throws Exception {

		mockServices().internet().mail().tv().result();

		final String CONAX_STRING = testKit
				.readResourceString("conax_request.txt");

		service.sendInOnly(TestMessages.generateTvMessage());

		checkMessageCount().mail(0).internet(0).tv(1).result(0);

		assertEquals("Service Response", CONAX_STRING, tvContent());
	}

	@Test
	public void whenFileInConaxOkDirectoryThenXmlInResultMock()
			throws Exception {
		mockServices().result();
		
		copyToDir("ps852502.emm.esbDone", CONAX_OK_DIR);

		Thread.sleep(3000);
		checkMessageCount().result(1);
		
		com.gepardec.sy_poc.xml.message_response_1_0.Message meassage = resultContentMessage();
		assertEquals("Response name", "feedback", meassage.getName());
		assertEquals("OrderId", "852502", meassage.getService().getAction().getData().getOrderList());
	}
}
