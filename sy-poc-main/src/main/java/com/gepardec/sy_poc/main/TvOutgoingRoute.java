package com.gepardec.sy_poc.main;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.gepardec.sy_poc.main.transformation.ConaxProcessor;

public class TvOutgoingRoute extends RouteBuilder{
	
	private static final String REF_CALL_CONAX = "ConaxService";
	Processor processor = new ConaxProcessor();

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		from(ServiceDefinitions.EP_SWITCHYARD + ServiceDefinitions.SVC_TV_OUTGOING).process(processor)
		.to(ServiceDefinitions.EP_SWITCHYARD + REF_CALL_CONAX);
	}

}
