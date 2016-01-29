/**
 *
 * @author Riozen
 * @date 2015-3-25 15:13:09
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riozenc.quicktool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Riozenc
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAnnotation {

    public enum DATE_TYPE {

        DATE, DATETIME
    }

    DATE_TYPE value() default DATE_TYPE.DATETIME;
}

