/**
 *    Auth:riozenc
 *    Date:2018年8月2日 上午10:06:30
 *    Title:com.riozenc.quicktool.springmvc.interceptor.AuthenticatingInterceptor.java
 **/
package com.riozenc.quicktool.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public abstract class AuthenticatingInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(AuthenticatingInterceptor.class);

	abstract protected String getLogin();

	abstract public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object, Exception exception) throws Exception;

	abstract public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object, ModelAndView modelAndView) throws Exception;

	/**
	 * 在执行handler之前来执行的 用于用户认证校验、用户权限校验
	 * 
	 * 判断是否是公开地址(实际开发中需要公开 地址配置在配置文件中)
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object) throws Exception {
		// TODO Auto-generated method stub

		// 判断用户身份在session中是否存在
		// 如果用户身份在session中存在放行
		if (SecurityUtils.getSubject().getPrincipal() != null) {
			return true;
		}
		logger.info("{} 身份失效.", httpServletRequest.getRemoteAddr());

		if (httpServletRequest.getHeader("x-requested-with") != null
				&& httpServletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			httpServletResponse.addHeader("sessionStatus", "timeOut");
		} else {
			String str = "<script language='javascript'>alert('身份认证过期,请重新登录');" + "window.top.location.href='"
					+ getLogin() + "';</script>";
			httpServletResponse.setContentType("text/html;charset=UTF-8");// 解决中文乱码
			try {
				httpServletResponse.getWriter().write(str);
				httpServletResponse.getWriter().flush();
				httpServletResponse.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 如果返回false表示拦截不继续执行handler，如果返回true表示放行
		return false;
	}
}