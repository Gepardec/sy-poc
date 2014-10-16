package com.gepardec.sy_poc.main;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

public final class StringTransformer {

	@Transformer(from = "{http://www.w3.org/2001/XMLSchema}string")
	public String transformStringToString(Element from) {
		return from.getTextContent();
	}

}
