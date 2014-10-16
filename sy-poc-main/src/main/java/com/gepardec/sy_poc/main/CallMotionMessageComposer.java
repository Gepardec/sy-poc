package com.gepardec.sy_poc.main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.camel.component.exec.ExecBinding;
import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.common.xml.QNameUtil;
import org.switchyard.component.camel.common.composer.CamelBindingData;
import org.switchyard.component.common.composer.BaseMessageComposer;

public class CallMotionMessageComposer extends
		BaseMessageComposer<CamelBindingData> {

	@Override
	public Message compose(CamelBindingData source, Exchange exchange)
			throws Exception {
		getContextMapper().mapFrom(source, exchange.getContext());

		System.out.println("CamelExchangeComposer compose()");
		org.apache.camel.Message sourceMessage = source.getMessage();

		// map content
		Message message = exchange.getMessage();
		QName msgType = getMessageType(exchange);
		Object content;
		if (msgType == null) {
			content = sourceMessage.getBody();
		} else if (QNameUtil.isJavaMessageType(msgType)) {
			content = sourceMessage.getBody(QNameUtil
					.toJavaMessageType(msgType));
		} else {
			content = sourceMessage.getBody(InputStream.class);
		}
		message.setContent(content);
		return message;
	}

	@Override
	public CamelBindingData decompose(Exchange exchange, CamelBindingData target)
			throws Exception {
		getContextMapper().mapTo(exchange.getContext(), target);

		System.out.println("CamelExchangeComposer decompose()");
		org.apache.camel.Message targetMessage = target.getMessage();

		com.gepardec.sy_poc.xml.message_request_1_0.Message message = exchange.getMessage().getContent(
				com.gepardec.sy_poc.xml.message_request_1_0.Message.class);

		List<String> args = new ArrayList<String>();
		args.add(message.getService().getAction().getData().getOrderNo());
		args.add(message.getService().getName());
		targetMessage.setHeader(ExecBinding.EXEC_COMMAND_ARGS, args);

		String cmd = System.getProperty(ServiceDefinitions.SystemProperties.MOTION_SCRIPT, null);
		if (cmd != null) {
			targetMessage.setHeader(ExecBinding.EXEC_COMMAND_EXECUTABLE,
					cmd);
		}

		targetMessage.setBody(exchange.getMessage().getContent());
		return target;
	}

}
