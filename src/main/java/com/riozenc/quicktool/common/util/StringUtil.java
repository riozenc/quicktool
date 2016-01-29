/**
 *
 * @author Riozen
 * @date 2015-3-18 9:30:04
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riozenc.quicktool.common.util;

/**
 *
 * @author Riozenc
 */
public class StringUtil {

    /**
     * 格式化字符串为首字母大写的形式(例:Xxx)
     *
     * @param name
     * @return
     */
    public static String fristToUpper(String name) {
        String str = null;

        if (name.length() <= 1) {
            str = name.toUpperCase();
        } else {
            str = name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        return str;
    }

    /**
     * 全部转换大写xxXxx转化为XX_XXX
     * @param input
     * @return
     */
    public static String allToUpper(String input) {
        StringBuffer result = new StringBuffer();
        char[] temp = input.toCharArray();
        int length = temp.length;
        for (int i = 0; i < length; i++) {
            if (Character.isUpperCase(temp[i])) {
                result.append("_").append(temp[i]);
            } else {
                result.append(Character.toUpperCase(temp[i]));
            }

        }
        return result.toString();
    }
}
