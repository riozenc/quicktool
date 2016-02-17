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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {

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
	public static void writeJSONGrid(List data, int totalCount, HttpServletResponse response) throws IOException {
		JSONUtil.writeJSONGrid(new JSONGrid(totalCount, data.toArray()), response);
	}

	/**
	 * 输出Tree数据
	 * 
	 * @param data
	 * @param response
	 * @throws IOException
	 */
	public static void writeJSONTree(List data, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse(JSONArray.fromObject(data).toString(), response);
	}

	/**
	 * 
	 * @param grid
	 * @param response
	 * @throws IOException
	 */
	private static void writeJSONGrid(JSONGrid grid, HttpServletResponse response) throws IOException {
		JSONUtil.writeResponse(JSONObject.fromObject(grid).toString(), response);
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
		JSONUtil.writeResponse(JSONObject.fromObject(data).toString(), response);
	}
}
