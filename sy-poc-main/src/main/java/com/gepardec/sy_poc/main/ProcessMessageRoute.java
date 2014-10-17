package com.gepardec.sy_poc.main;

import org.apache.camel.builder.RouteBuilder;

import com.gepardec.sy_poc.xml.message_request_1_0.Message;

public class ProcessMessageRoute extends RouteBuilder {
	
	private static final String SVC_CALL_INTERNET = ServiceDefinitions.EP_SWITCHYARD + "InternetOutgoing";
	private static final String SVC_CALL_MAIL = ServiceDefinitions.EP_SWITCHYARD + "MailOutgoing";
	private static final String SVC_CALL_TV = ServiceDefinitions.EP_SWITCHYARD + "TvOutgoing";
	private static final String SVC_CALL_RESULT = ServiceDefinitions.EP_SWITCHYARD + "OutgoingResult";

	private String getSimpleExpressionFor(String messageType){
		return "${body.getService.getName} == '" + messageType + "'";
	}

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		// TODO Auto-generated method stub
		from(ServiceDefinitions.EP_SWITCHYARD + ServiceDefinitions.SVC_INCOMING_MESSAGE).log(
				"Received message for 'IncomingMessage' : ${body}")
				.choice().when().simple(getSimpleExpressionFor(ServiceDefinitions.MessageType.TYPE_INTERNET))
				.to(SVC_CALL_INTERNET, SVC_CALL_RESULT)
				
				.when().simple(getSimpleExpressionFor(ServiceDefinitions.MessageType.TYPE_MAIL))
				.to(SVC_CALL_MAIL, SVC_CALL_RESULT)
				
				.when().simple(getSimpleExpressionFor(ServiceDefinitions.MessageType.TYPE_DIGITV))
				.to(SVC_CALL_TV);
	}

}
