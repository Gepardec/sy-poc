package com.gepardec.sy_poc.simulation.conax;

import org.switchyard.Context;
import org.switchyard.component.camel.common.composer.CamelBindingData;
import org.switchyard.component.camel.common.composer.CamelContextMapper;
import org.switchyard.component.common.composer.ContextMapper;
import org.switchyard.config.model.composer.ContextMapperModel;

public class ConaxContextMapper implements ContextMapper<CamelBindingData>{
	
	CamelContextMapper mapper;
	
	public ConaxContextMapper() {
		mapper = new CamelContextMapper();
	}

	@Override
	public ContextMapperModel getModel() {
		return mapper.getModel();
	}

	@Override
	public void mapFrom(CamelBindingData arg0, Context arg1) throws Exception {
		mapper.mapFrom(arg0, arg1);
		
	}

	@Override
	public void mapTo(Context arg0, CamelBindingData arg1) throws Exception {
		mapper.mapTo(arg0, arg1);
		
	}

	@Override
	public void setModel(ContextMapperModel arg0) {
		mapper.setModel(arg0);
		
	}

}
