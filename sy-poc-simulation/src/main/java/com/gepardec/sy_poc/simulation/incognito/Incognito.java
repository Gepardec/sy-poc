package com.gepardec.sy_poc.simulation.incognito;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gepardec.sy_poc.xml.message_request_1_0.Message;

@Path("/incognito")
public class Incognito {
	
	@POST
	@Path(value = "/")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response process(Message message){
		String id = message.getService().getName();
		if("internet".equals(message.getService().getName())){
			return buildResponse(id, "OK");
		} else {
			return buildResponse(id, "FAIL");
		}
	}
	
	private Response buildResponse(String id, String status){
		return Response.ok("<return>" + id + " " + status + "</return>").build();
	}
	
	@POST
	@Path(value = "/mirror")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response process(String text){
		return Response.ok(text).build();
	}
	
}
