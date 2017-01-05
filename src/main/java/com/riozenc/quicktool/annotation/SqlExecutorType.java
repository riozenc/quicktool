package com.riozenc.quicktool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlExecutorType {
	public enum TYPE {
		SIMPLE, REUSE, BATCH
	}
	TYPE value() default TYPE.SIMPLE;
}
