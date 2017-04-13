/**
 *	
 * @Title:JSONUtil.java
 * @author Riozen
 *	@date 2013-12-25 下午2:08:06
 *	
 */
package com.riozenc.quicktool.common.util.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 对象输出json
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object) {
		return toJsonString(object, false);
	}

	public static <T> T readValue(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, clazz);
	}

	public static String toJsonString(Object object, boolean isIgnoreNull) {
		try {
			if (isIgnoreNull) {
				ObjectMapper objectMapper = new ObjectMapper();
				// 配置mapper忽略空属性
				objectMapper.setSerializationInclusion(Include.NON_EMPTY);

				return objectMapper.writeValueAsString(object);
			} else {
				return objectMapper.writeValueAsString(object);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			System.err.println("write to json string error:" + object);
			return null;
		}
	}

	/**
	 * 输出信息
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	public static void writeMsg(String code, String msg, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse("{\"status\":" + code + ",\"msg\":\"" + msg + "\"}", response);
	}

	/**
	 * 输出信息
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	public static String writeMsg(String code, String msg) {
		return "{\"status\":" + code + ",\"msg\":\"" + msg + "\"}";
	}

	public static void wirteInfo(String msg, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse(msg, response);
	}

	/**
	 * 输出Tree数据
	 * 
	 * @param data
	 * @param response
	 * @throws IOException
	 */
	public static void writeJSONTree(List<?> data, HttpServletResponse response) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		// 配置mapper忽略空属性
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		JSONUtil.writeResponse(objectMapper.writeValueAsString(data), response);
	}

	/**
	 * 
	 * @param s
	 * @param response
	 * @throws IOException
	 */
	private static void writeResponse(String s, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse(s, response, null);
	}

	/**
	 * 
	 * @param s
	 * @param response
	 * @param characterEncoding
	 * @throws IOException
	 */
	private static void writeResponse(String msg, HttpServletResponse response, String characterEncoding)
			throws IOException {
		String chara = characterEncoding;
		if (chara == null) {
			chara = "UTF-8";
		}
		response.setCharacterEncoding(chara);

		response.getWriter().write(msg);
	}

	/**
	 * 输出java对象为json字符串
	 * 
	 * @param data
	 * @param response
	 * @throws IOException
	 */
	public static void writeObject2JSONString(Object data, HttpServletResponse response) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		// 配置mapper忽略空属性
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		JSONUtil.writeResponse(objectMapper.writeValueAsString(data), response);
	}
}
