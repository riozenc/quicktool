/**
 *    Auth:riozenc
 *    Date:2018年6月29日 上午10:28:44
 *    Title:com.riozenc.quicktool.shiro.utils.UserUtils.java
 **/
package com.riozenc.quicktool.shiro.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.riozenc.quicktool.shiro.Principal;

public class UserUtils {
	public static String getUserId() {
		Subject subject = SecurityUtils.getSubject();
		Principal principal = (Principal) subject.getPrincipal();
		if(null==principal) {
			throw new RuntimeException("用户失效,请重新登录...");
		}
		return principal.getUserAccount();
	}
}
