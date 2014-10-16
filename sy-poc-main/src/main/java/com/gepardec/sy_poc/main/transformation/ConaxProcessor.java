package com.gepardec.sy_poc.main.transformation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.gepardec.sy_poc.tools.ObjectXPath;



public class ConaxProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		
		String fn = ("activate".equals(ObjectXPath.get(body, "service/action/name")) ? "ps" : "q") + ObjectXPath.get(body, "service/action/data/orderNo");
		exchange.getIn().setBody(ConaxTransformer.message_1_0ToConax((com.gepardec.sy_poc.xml.message_request_1_0.Message)body));
		exchange.getIn().setHeader(Exchange.FILE_NAME, fn + ".emm");

	}

}