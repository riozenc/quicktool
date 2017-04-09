/**
 * Title:ClassFileFilter.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.common.util.file.filter;

import java.io.File;
import java.io.FileFilter;

public class FileTypeFilter implements FileFilter {

	private boolean recursive;
	private String[] fileTypes;

	private FileTypeFilter(final boolean recursive, final String... fileTypes) {
		this.recursive = recursive;
		this.fileTypes = fileTypes;
	}

	public static FileTypeFilter getInstance(final boolean recursive, final String... fileTypes) {
		return new FileTypeFilter(recursive, fileTypes);
	}

	@Override
	public boolean accept(File file) {
		// TODO Auto-generated method stub
		if (file.isDirectory()) {
			return recursive;
		}

		for (String fileType : fileTypes) {

			return file.getName().endsWith(fileType);
		}

		return (recursive && file.isDirectory());
	}

}
