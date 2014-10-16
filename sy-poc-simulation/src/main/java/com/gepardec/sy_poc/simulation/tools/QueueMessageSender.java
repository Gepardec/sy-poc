package com.gepardec.sy_poc.simulation.tools;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.gepardec.sy_poc.tools.JMSSender;
import com.gepardec.sy_poc.xml.message_request_1_0.Message;
import com.gepardec.sy_poc.xml.message_request_1_0.Message.Service;
import com.gepardec.sy_poc.xml.message_request_1_0.Message.Service.Action;
import com.gepardec.sy_poc.xml.message_request_1_0.Message.Service.Action.Data;

@Path("/tools/QueueMessageSender")
public class QueueMessageSender {
	
	@GET
	@Path("/send/{type}/{number}")
	@Produces(MediaType.TEXT_PLAIN)
	public String send(@PathParam("type") String type, @PathParam("number") int number){
		JMSSender sender = new JMSSender();
		sender.connectionFactory("/ConnectionFactory").queue("queue/incomingQueue");
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Message.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			for(int i = 0; i < number; i++){
				StringWriter sw = new StringWriter();
				jaxbMarshaller.marshal(generateMessage("", type, Integer.toString(i)), sw);
				sender.body(sw.toString()).send();;
			}
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "OK. Type: " + type + " sent " + number + " times";
	}
	
	private Message generateMessage(String name, String type, String id){
		Message m = new Message();
		m.setName(name);
		m.setService(new Service());
		m.getService().setName(type);
		m.getService().setAction(new Action());
		m.getService().getAction().setName("activate");
		m.getService().getAction().setData(new Data());
		m.getService().getAction().getData().setOrderNo(id);
		m.getService().getAction().getData().setCountry("AT");
		m.getService().getAction().getData().setProductId(106);
		m.getService().getAction().getData().setSubscriptionStart("01.06.2014 00:00:00");
		m.getService().getAction().getData().setSubscriptionEnd("30.06.2014 23:59:59");
		m.getService().getAction().getData().setPriority("EMM");
		m.getService().getAction().getData().setNofsmartcards((byte) 1);
		m.getService().getAction().getData().setSmartcardList("01359116803");
		return m;
	}
}
