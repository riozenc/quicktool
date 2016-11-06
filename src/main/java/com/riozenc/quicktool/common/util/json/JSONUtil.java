/**
 *	
 * @Title:JSONUtil.java
 * @author Riozen
 *	@date 2013-12-25 下午2:08:06
 *	
 */
package com.riozenc.quicktool.common.util.json;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 对象输出json
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			System.out.println("write to json string error:" + object);
			return null;
		}
	}

	/**
	 * 输出成功信息
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	public static void writeSuccessMsg(String msg, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse("{\"success\":true,\"msg\":\"" + msg + "\"}", response);
	}

	/**
	 * 输出成功信息
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	public static String writeSuccessMsg(String msg) {
		return "{\"success\":true,\"msg\":\"" + msg + "\"}";
	}

	/**
	 * 输出成功
	 * 
	 * @param response
	 * @throws IOException
	 */
	public static void writeSuccess(HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse("{\"success\":true}", response);
	}

	/**
	 * 输出失败
	 * 
	 * @param response
	 * @throws IOException
	 */
	public static void writeError(HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse("{\"success\":false}", response);
	}

	/**
	 * 输出失败信息
	 * 
	 * @param response
	 * @throws IOException
	 */
	public static void writeErrorMsg(String msg, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse("{\"success\":false,\"msg\":\"" + msg + "\"}", response);
	}

	/**
	 * 输出失败信息
	 * 
	 * @param response
	 * @throws IOException
	 */
	public static String writeErrorMsg(String msg) throws IOException {
		return "{\"success\":false,\"msg\":\"" + msg + "\"}";
	}

	public static void wirteInfo(String msg, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse(msg, response);
	}

	/**
	 * 输出Grid数据
	 * 
	 * @param data
	 * @param totalCount
	 * @param response
	 * @throws IOException
	 */
	public static void writeJSONGrid(List<?> data, int totalCount, HttpServletResponse response) throws IOException {
		JSONUtil.writeJSONGrid(new JSONGrid(totalCount, data.toArray()), response);
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
	 * @param grid
	 * @param response
	 * @throws IOException
	 */
	private static void writeJSONGrid(JSONGrid grid, HttpServletResponse response) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		// 配置mapper忽略空属性
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		JSONUtil.writeResponse(objectMapper.writeValueAsString(grid), response);
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
