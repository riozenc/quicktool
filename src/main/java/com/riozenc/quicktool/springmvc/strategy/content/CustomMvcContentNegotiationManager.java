/**
 *    Auth:riozenc
 *    Date:2018年6月19日 下午6:25:45
 *    Title:com.riozenc.quicktool.springmvc.strategy.cnm.MvcContentNegotiationManager.java
 **/
package com.riozenc.quicktool.springmvc.strategy.content;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;

import org.springframework.http.MediaType;

public class CustomMvcContentNegotiationManager {

	@Bean(name = "mvcContentNegotiationManager")
	public ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean() {
		ContentNegotiationManagerFactoryBean bean = new ContentNegotiationManagerFactoryBean();
		bean.setFavorPathExtension(false);
		bean.setFavorParameter(false);
		Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
		mediaTypes.put("json", MediaType.APPLICATION_JSON_UTF8);
		mediaTypes.put("xml", MediaType.APPLICATION_XML);
		bean.addMediaTypes(mediaTypes);
		return bean;
	}
}
