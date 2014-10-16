package com.gepardec.sy_poc.main;

public interface ServiceDefinitions {

	public static final String EP_SWITCHYARD = "switchyard://";

	public static final String SVC_TV_OUTGOING = "TvOutgoing";
	public static final String SVC_CALL_TV = "CallConax";
	public static final String SVC_CALL_MAIL = "CallMotion";
	public static final String SVC_CALL_INTERNET = "CallIncognito";
	public static final String SVC_CALL_NAVISION = "CallNavision";
	public static final String SVC_CONAX_RESULT = "ConaxResult";

	public static final String SVC_BATCH_SPLITTER = "BatchSplitter";
	public static final String SVC_SINGLE_MESSAGE = "SingleMessage";
	
	public static final String SVC_SEND_RESULT = "OutgoingResult";

	public static final String SVC_INCOMING_MESSAGE = "IncomingMessage";

	public static final String RESULT_QUEUE = "resultQueue";
	public static final String INCOMING_QUEUE = "incomingQueue";
	
	public static final class XmlElements{
		public static final String MESSAGE = "message";
		public static final String MESSAGES = "messages";
	}
	
	public static final class SystemProperties{
		public static final String MOTION_SCRIPT = "sy.poc.motion";
	}
	
	public static final class MessageType {
		public static String TYPE_INTERNET = "internet";
		public static String TYPE_DIGITV = "digitv";
		public static String TYPE_MAIL = "mail";
	}

}
