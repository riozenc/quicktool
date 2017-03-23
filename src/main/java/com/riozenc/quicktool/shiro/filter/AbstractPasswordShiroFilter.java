/**
 * Title:AbstractShiroFilter.java
 * Author:riozenc
 * Datetime:2017年3月23日 下午4:47:34
**/
package com.riozenc.quicktool.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 修改isRedirectDirectly和exceptionKey只能在对象创建的时候修改。 建议使用@PostConstruct
 * 
 * @author riozenc
 *
 */
public abstract class AbstractPasswordShiroFilter extends FormAuthenticationFilter {
	private boolean isRedirectDirectly = false;
	private String exceptionKey = "message";

	protected abstract AuthenticationToken getToken(ServletRequest request, ServletResponse response);

	protected abstract String getFailureMessage(AuthenticationException authenticationException);

	public void setIsRedirectDirectly(boolean isRedirectDirectly) {
		this.isRedirectDirectly = isRedirectDirectly;
	}

	public void setExceptionKey(String exceptionKey) {
		this.exceptionKey = exceptionKey;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub
		return getToken(request, response);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		if (isRedirectDirectly) {
			return isRedirectDirectly;
		} else {
			return super.onLoginSuccess(token, subject, request, response);
		}
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		// TODO Auto-generated method stub
		request.setAttribute(exceptionKey, getFailureMessage(e));
		return super.onLoginFailure(token, e, request, response);
	}

}
