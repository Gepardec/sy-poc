package com.gepardec.sy_poc.main.transformation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ConaxResultProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Processing conax result "
				+ exchange.getIn().getHeader(Exchange.FILE_NAME).toString());
		Object body = exchange.getIn().getBody();
		exchange.getIn().setBody(
				ConaxTransformer.conaxTomessage_response_1_0(body
						.toString()));
		exchange.getIn().setHeader(
				Exchange.FILE_NAME,
				exchange.getIn().getHeader(Exchange.FILE_NAME).toString()
						.replace("emm.esbDone", "xml"));
	}

}