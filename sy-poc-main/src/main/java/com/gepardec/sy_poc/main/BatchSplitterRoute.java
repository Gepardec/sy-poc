package com.gepardec.sy_poc.main;

import org.apache.camel.builder.RouteBuilder;

public class BatchSplitterRoute extends RouteBuilder {
	
	private static final String REF_SINGLE_MESSAGE = "SingleMessage";

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {

		from(ServiceDefinitions.EP_SWITCHYARD + ServiceDefinitions.SVC_BATCH_SPLITTER).log(
				"Received message for " + this.getClass().getSimpleName() + " : ${body}")
				.split().tokenizeXML(ServiceDefinitions.XmlElements.MESSAGE, ServiceDefinitions.XmlElements.MESSAGES).streaming()
				.log("After Split: ${body}")
				.to(ServiceDefinitions.EP_SWITCHYARD + REF_SINGLE_MESSAGE);
	}
}
