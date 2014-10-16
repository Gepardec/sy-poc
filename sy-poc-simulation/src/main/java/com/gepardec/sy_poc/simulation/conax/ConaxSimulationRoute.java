package com.gepardec.sy_poc.simulation.conax;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;

public class ConaxSimulationRoute extends RouteBuilder {

	Predicate correct = PredicateBuilder.and(body().contains("AT"), PredicateBuilder.and(body().contains("1"), body().contains("01359116803")));
	Processor headerProcessor = new ConaxSimulationHeaderProcessor();
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		String dir = System.getProperty("sy.poc.conax.dir", "conax");
		
		// TODO Auto-generated method stub
		from("switchyard://ProcessMessage").process(headerProcessor).choice()
		.when(correct).to("file://" + dir + "/ok?autoCreate=true")
		.otherwise().to("file://" + dir + "/err?autoCreate=true");
		//.log("Received message for 'RegisterProvisionierung' : ${body}");
		
	}

}
