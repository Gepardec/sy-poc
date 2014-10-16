package com.gepardec.sy_poc.main.transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gepardec.sy_poc.main.ServiceDefinitions;

public class ConaxTransformer {
	/**
	 * Transforms incoming message to a format that can be read by conax
	 * 
	 * @param messageType
	 * @return
	 */

	private static SimpleDateFormat sdfFrom = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss");
	private static SimpleDateFormat sdfTo = new SimpleDateFormat("yyyyMMddHHmm");

	public static String message_1_0ToConax(
			com.gepardec.sy_poc.xml.message_request_1_0.Message message) {
		if (message == null || message.getService() == null
				|| message.getService().getAction() == null
				|| message.getService().getAction().getData() == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(message.getService().getAction().getData()
				.getCountry());
		newline(builder);

		builder.append(message.getService().getAction().getData()
				.getOrderNo());
		newline(builder);

		builder.append(String.format("%08d", message.getService()
				.getAction().getData().getProductId()));
		newline(builder);

		try {
			Date subStart = sdfFrom.parse(message.getService().getAction()
					.getData().getSubscriptionStart());

			builder.append(sdfTo.format(subStart));
			newline(builder);
		} catch (ParseException e) {
			newline(builder);
			newline(builder);
		}

		try {
			Date subEnd = sdfFrom.parse(message.getService().getAction()
					.getData().getSubscriptionEnd());

			builder.append(sdfTo.format(subEnd));
			newline(builder);
		} catch (ParseException e) {
			newline(builder);
			newline(builder);
		}

		builder.append("U");
		newline(builder);

		builder.append(message.getService().getAction().getData()
				.getPriority());
		newline(builder);

		builder.append("U");
		newline(builder);

		builder.append(message.getService().getAction().getData()
				.getNofsmartcards());
		newline(builder);

		builder.append(message.getService().getAction().getData()
				.getSmartcardList());
		newline(builder);

		builder.append("ZZZ");
		newline(builder);

		return builder.toString();
	}

	private static void newline(StringBuilder builder) {
		builder.append(System.getProperty("line.separator"));
	}

	public static com.gepardec.sy_poc.xml.message_response_1_0.Message conaxTomessage_response_1_0(
			String conaxString) {
		com.gepardec.sy_poc.xml.message_response_1_0.Message message = new com.gepardec.sy_poc.xml.message_response_1_0.Message();
		message.setService(new com.gepardec.sy_poc.xml.message_response_1_0.Message.Service());
		message.getService()
				.setAction(
						new com.gepardec.sy_poc.xml.message_response_1_0.Message.Service.Action());
		message.getService()
				.getAction()
				.setData(
						new com.gepardec.sy_poc.xml.message_response_1_0.Message.Service.Action.Data());

		String[] strings = conaxString.split("\\r?\\n");
		if (strings.length < 11) {
			System.err
					.println("Error while parsing conax result file. Wrong format: number of strings is "
							+ strings.length
							+ " in "
							+ conaxString
							+ " but expected 11");
			return null;
		}

		try {
			message.setName("feedback");
			message.getService().setName(ServiceDefinitions.MessageType.TYPE_DIGITV);
			message.getService().getAction().setName("activate");
			message.getService().getAction().getData().setOrderList(strings[1]);
			message.getService().getAction().getData().setStatus("OK");
			message.getService().getAction().getData().setErrorTimestamp("OK");
			message.getService().getAction().getData().setErrorDesc("Success");
			message.getService().getAction().getData().setCountry(strings[0]);
			message.getService().getAction().getData()
					.setNofsmartcards(Byte.parseByte(strings[8]));
			message.getService().getAction().getData().setPriority(strings[6]);
			message.getService().getAction().getData().setProductId(strings[3]);
			message.getService().getAction().getData()
					.setSubscriptionStart(Long.parseLong(strings[3]));
			message.getService().getAction().getData()
					.setSubscriptionEnd(Long.parseLong(strings[4]));
			message.getService().getAction().getData().setSmartcard(strings[9]);
		} catch (Exception e) {
			System.err.println("Exception while parsing conax result file");
			e.printStackTrace();
			return null;
		}

		return message;
	}
}
