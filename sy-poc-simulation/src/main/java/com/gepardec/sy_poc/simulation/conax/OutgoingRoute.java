package com.gepardec.sy_poc.simulation.conax;

import org.apache.camel.builder.RouteBuilder;

public class OutgoingRoute extends RouteBuilder {

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		from("switchyard://OutgoingQueue").log(
				"Simulation received message for 'OutgoingQueue' : ${body}")
				.to("switchyard://OutgoingFile");
	}

}
