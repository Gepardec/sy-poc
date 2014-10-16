package com.gepardec.sy_poc.simulation.conax;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ConaxSimulationHeaderProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		Object headerFileName = exchange.getIn().getHeader(Exchange.FILE_NAME);
		exchange.getIn().setHeader(Exchange.FILE_NAME, headerFileName.toString() + ".esbDone");
	}

}