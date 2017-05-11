package com.sourcod.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringWriter;

public final class JsonUtil {
	@SuppressWarnings("serial")
	public static class CodecException extends RuntimeException {
		public CodecException(Throwable cause) {
			super(cause);
		}
	}

	private JsonUtil() {
	}

	public static String dump(Object obj) throws CodecException {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, obj);
		} catch (Exception e) {
			throw new CodecException(e);
		}
		return writer.toString();
	}

	public static <T> T load(String json, Class<T> type) throws CodecException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, type);
		} catch (Exception e) {
			throw new CodecException(e);
		}
	}

	/**
	 * Object对象转json字符串
	 *
	 * @author zcj
	 * @time 2016-10-20 15:30:59
	 *
	 * @param o
	 *            javabean对象
	 * @return json字符串
	 */
	public static String objectToJson(Object o) {
		JSONObject json = new JSONObject(o);
		return json.toString();
	}

	/**
	 * 对象数组转json字符串
	 *
	 * @author zcj
	 * @time 2016-11-09
	 * @param os
	 * @return
	 */
	public static String arrayToJson(Object[] os) {
		JSONArray json = new JSONArray(os);
		return json.toString();
	}


}
