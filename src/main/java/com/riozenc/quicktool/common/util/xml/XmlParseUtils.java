/**
 * @Project:sh
 * @Title:XmlParseUtils.java
 * @Author:Riozenc
 * @Datetime:2016年7月6日 下午2:36:44
 */
package com.riozenc.quicktool.common.util.xml;

import java.lang.reflect.Field;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.riozenc.quicktool.common.util.ClassUtils;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;

/**
 * 待优化，目前只能转型指定对象，无法转型Map
 * 
 * @author riozenc
 *
 */
public class XmlParseUtils {

	public static Element parse(String xmlPath) throws DocumentException {

		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(ClassUtils.getDefaultClassLoader().getResourceAsStream(xmlPath));

		Element element = document.getRootElement();
		return element;
	}

	public static <T> T xmlToBean(Element element, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {

		T obj = clazz.newInstance();
		String className = clazz.getSimpleName().toLowerCase();
		Element classElement = element.element(className);

		if (classElement == null) {
			return null;
		}
		Field[] fields = ReflectUtil.getFields(clazz);
		for (Field field : fields) {
			// 设置字段可访问（必须，否则报错）
			field.setAccessible(true);
			// 得到字段的属性名
			String name = field.getName();

			if (classElement.attributeValue(name) != null && !"".equals(classElement.attributeValue(name))) {
				// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
				try {
					ReflectUtil.setFieldValue(obj, name,
							ReflectUtil.typeFormat(field.getType(), classElement.attributeValue(name)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
		}

		return obj;
	}

}
