package com.gepardec.sy_poc.main;

import org.apache.camel.builder.RouteBuilder;

public class ResultRoute extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	public void configure() {
		from(ServiceDefinitions.EP_SWITCHYARD + ServiceDefinitions.SVC_SEND_RESULT)
				// Bei Mail ist Body ein
				// org.apache.camel.component.exec.ExecResult, daher umwandeln
				// in String.
				.convertBodyTo(String.class)
				.log("Received message for 'OutgoingResultRoute' : ${body}")
				.to(ServiceDefinitions.EP_SWITCHYARD + ServiceDefinitions.SVC_CALL_NAVISION);
	}

}
