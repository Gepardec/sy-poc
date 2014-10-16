package com.gepardec.sy_poc.main;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.gepardec.sy_poc.main.transformation.ConaxResultProcessor;

public class ConaxResultRoute extends RouteBuilder {

	private static final String REF_OUTGOING_RESULT = "OutgoingResult";
	Processor resultProcessor = new ConaxResultProcessor();

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		from(ServiceDefinitions.EP_SWITCHYARD + ServiceDefinitions.SVC_CONAX_RESULT).log(
				"Received message for 'ConaxResult' : ${body}")
				.process(resultProcessor)
				.to(ServiceDefinitions.EP_SWITCHYARD + REF_OUTGOING_RESULT);
	}

}
