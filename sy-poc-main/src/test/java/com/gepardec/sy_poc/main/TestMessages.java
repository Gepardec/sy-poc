package com.gepardec.sy_poc.main;

import com.gepardec.sy_poc.xml.message_request_1_0.Message;
import com.gepardec.sy_poc.xml.message_request_1_0.Message.Service;
import com.gepardec.sy_poc.xml.message_request_1_0.Message.Service.Action;
import com.gepardec.sy_poc.xml.message_request_1_0.Message.Service.Action.Data;

public class TestMessages {

	public static Message generateMessage(String name, String type, String id){
		
		Data data = new Data();
		data.setCountry("AT");
		data.setNofsmartcards((byte) 1);
		data.setOrderNo(id);
		data.setPriority("EMM");
		data.setProductId(106);
		data.setSmartcardList("01359116803");
		data.setSubscriptionStart("01.06.2014 00:00:00");
		data.setSubscriptionEnd("30.06.2014 23:59:59");
		
		Action action = new Action();
		action.setName("activate");
		action.setData(data);
		
		Service service = new Service();
		service.setName(type);
		service.setAction(action);
		
		Message mType = new Message();
		mType.setName(name);
		mType.setService(service);
		
		return mType;
	}

	public static Message generateMailMessage() {
		return generateMessage("order", "mail", "MAIL00001");
	}

	public static Message generateInternetMessage() {
		return generateMessage("order", "internet", "INET00001");
	}
	
	public static Message generateTvMessage() {
		return generateMessage("order", "digitv", "DIGITV00001");
	}
	
}
