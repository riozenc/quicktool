/**
 * Title:FileIoUtil.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.common.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.riozenc.quicktool.common.util.file.filter.ClassFileFilter;

public class FileIoUtil {
	// 是否循环搜索子包
	private static boolean recursive = true;

	// 设置是否循环搜索子包
	public static void setRecursive(boolean recursive) {
		FileIoUtil.recursive = recursive;
	}

	// 过滤器
	private static FileFilter filenameFilter = ClassFileFilter.getFilter(recursive);

	/**
	 * 获得包下面的所有的class
	 *
	 * @param pack
	 *            package完整名称
	 * @return List包含所有class的实例
	 */
	public static List<Class<?>> getClasssFromPackage(String pack) {
		List<Class<?>> clazzs = new ArrayList<Class<?>>();

		// 包名字
		String packageName = pack;
		// 包名对应的路径名称
		String packageDirName = packageName.replace('.', '/');

		Enumeration<URL> dirs;

		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				if ("file".equals(protocol)) {
					System.out.println("file类型的扫描");
					findClassInPackageByFile(packageName, filePath, recursive, clazzs);
				} else if ("jar".equals(protocol)) {
					System.out.println("jar类型的扫描");
					clazzs = getClasssFromJarFile(filePath, packageDirName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazzs;
	}

	/**
	 * 在package对应的路径下找到所有的class
	 *
	 * @param packageName
	 *            package名称
	 * @param filePath
	 *            package对应的路径
	 * @param recursive
	 *            是否查找子package
	 * @param clazzs
	 *            找到class以后存放的集合
	 */
	private static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
			List<Class<?>> clazzs) {

		System.out.println(
				"执行findClassInPackageByFile方法..参数:packageName=" + packageName + ",filePath=" + filePath + "...");
		String className;
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 在给定的目录下找到所有的文件，并且进行条件过滤
		File[] dirFiles = dir.listFiles(filenameFilter);

		for (File file : dirFiles) {
			if (file.isDirectory()) {
				findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
			} else {
				className = file.getName().substring(0, file.getName().length() - 6);
				try {
					clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
				} catch (Exception e) {

					System.err.println("异常类名:" + className);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过流将jar中的一个文件的内容输出
	 *
	 * @param jarPaht
	 *            jar文件存放的位置
	 * @param filePaht
	 *            指定的文件目录
	 */
	private void getStream(String jarPath, String filePath) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Enumeration<JarEntry> ee = jarFile.entries();

		List<JarEntry> jarEntryList = new ArrayList<JarEntry>();
		while (ee.hasMoreElements()) {
			JarEntry entry = ee.nextElement();
			// 过滤我们出满足我们需求的东西，这里的fileName是指向一个具体的文件的对象的完整包路径，比如com/mypackage/test.txt
			if (entry.getName().startsWith(filePath)) {
				jarEntryList.add(entry);
			}
		}
		try {
			InputStream in = jarFile.getInputStream(jarEntryList.get(0));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s = "";

			while ((s = br.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载jarPath目录下jar中满足filePath条件的class
	 * 
	 * @param jarPath
	 * @param filePath
	 * @return
	 */
	public static List<Class<?>> getClasssFromJarFile(String jarPath, String filePath) {
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		String path = jarPath.substring(0, jarPath.indexOf("!"));

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(path.substring(path.indexOf("/"), path.length()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<JarEntry> jarEntryList = new ArrayList<JarEntry>();

		Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry jarEntry = enumeration.nextElement();
			// 过滤我们出满足我们需求的东西
			if (jarEntry.getName().startsWith(filePath) && jarEntry.getName().endsWith(".class")) {
				jarEntryList.add(jarEntry);
			}
		}

		URL url = null;
		ClassLoader loader = null;
		try {
			url = new URL(path);
			loader = new URLClassLoader(new URL[] { url });// 自己定义的classLoader类，把外部路径也加到load路径里，使系统去该路经load对象
			for (JarEntry entry : jarEntryList) {
				String className = entry.getName().replace('/', '.');
				className = className.substring(0, className.length() - 6);

				try {
					clazzs.add(loader.loadClass(className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return clazzs;
	}

}
