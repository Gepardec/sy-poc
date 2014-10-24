package com.gepardec.sy_poc.main;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import com.gepardec.sy_poc.interfaces.ConaxService;
import com.gepardec.sy_poc.interfaces.OutgoingResult;
import com.gepardec.sy_poc.main.transformation.ConaxTransformer;

@Service(value = ConaxService.class, name = "ConaxResult")
public class ConaxResultBean implements ConaxService {
	
	@Inject
	@Reference
	private OutgoingResult outgoingResult;

	@Override
	public void process(String msg) {
		outgoingResult.send(ConaxTransformer.conaxTomessage_response_1_0(msg));
	}

}
