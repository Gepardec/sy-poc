package com.gepardec.sy_poc.main;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.bean.config.model.BeanSwitchYardScanner;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

import com.gepardec.sy_poc.main.ServiceDefinitions;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = AbstractProvisioningTest.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, HornetQMixIn.class }, scanners = BeanSwitchYardScanner.class)
public class BatchSplitterTest extends AbstractProvisioningTest {

	@ServiceOperation(ServiceDefinitions.SVC_BATCH_SPLITTER)
	private Invoker service;

	@Test
	public void testSplitterSplitsMessage() throws Exception {

		mockServices().singleMessage();

		service.sendInOnly(testKit.readResourceString("batch_request.xml"));

		checkMessages().singleMessage(2);
		assertTrue("Erster Eintrag", singleMessageContent().contains("852502"));
	}

	@Test
	public void readFromBatchDirectoryAndSplit() throws Exception {
		mockServices().singleMessage();

		copyToDir("batch_request.xml", BATCH_DIR);

		Thread.sleep(3000);
		checkMessages().singleMessage(2);

	}

	@Test
	public void whenTwoMessagesInBatchThenMessagesAreRecievedInMocks()
			throws Exception {

		mockServices().mail().tv().internet().result();

		service.sendInOnly(testKit.readResourceString("batch_request.xml"));
		Thread.sleep(3000);

		checkMessages().mail(0).tv(2).internet(0).result(0);
	}

}
