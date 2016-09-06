/**
 * Title:ClassFileFilter.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.common.util.file.filter;

import java.io.File;
import java.io.FileFilter;

public class ClassFileFilter {
	public static FileFilter getFilter(final boolean recursive, final String fileType) {

		return new FileFilter() {

			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || file.getName().endsWith(fileType);
			}
		};
	}
}