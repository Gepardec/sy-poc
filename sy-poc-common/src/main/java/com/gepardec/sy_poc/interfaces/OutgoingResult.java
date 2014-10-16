package com.gepardec.sy_poc.interfaces;

import com.gepardec.sy_poc.xml.message_response_1_0.Message;

public interface OutgoingResult {
	public void send(Message message);
}
