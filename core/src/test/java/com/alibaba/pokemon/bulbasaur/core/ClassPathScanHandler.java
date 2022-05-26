package com.alibaba.pokemon.bulbasaur.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author yunche.ch@taobao.com
 * @since 2013-1-28 下午01:49:02
 */
public class ClassPathScanHandler {

	/**
	 * 无参构造器，默认是排除内部类、并搜索符合规则
	 */
	public ClassPathScanHandler() {
	}

	/**
	 * 扫描包
	 *
	 * @param basePackage
	 *            基础包
	 * @param recursive
	 *            是否递归搜索子包
	 * @return Set
	 */
	public Set<File> getPackageAllFiles(String basePackage, boolean recursive) {
		Set<File> files = new LinkedHashSet<File>();
		String packageName = basePackage;
		if (packageName.endsWith(".")) {
			packageName = packageName.substring(0, packageName.lastIndexOf('.'));
		}
		String package2Path = packageName.replace('.', '/');

		Enumeration<URL> dirs;
		try {

			dirs = Thread.currentThread().getContextClassLoader().getResources("");
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					int index = filePath.lastIndexOf("/");
					String path = filePath.substring(0, index);
					int index1 = path.lastIndexOf("/");
					path = path.substring(0, index1);
					path = path + "/classes";
					doScanPackageXsdByFile(files, packageName, path, recursive);

				}
			}
		} catch (IOException e) {
		}

		return files;
	}

	/**
	 * 以文件的方式扫描包下的所有Class文件
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private void doScanPackageXsdByFile(Set<File> files, String packageName, String packagePath, boolean recursive) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles();
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				doScanPackageXsdByFile(files, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
			} else {
				if (file.getName().endsWith(".xsd"))
					files.add(file);
			}
		}
	}

	// /**
	// * 根据过滤规则判断类名
	// *
	// * @param className
	// * @return
	// */
	// private boolean filterClassName(String className) {
	// if (!className.endsWith(".class")) {
	// return false;
	// }
	// if (null == this.classFilters || this.classFilters.isEmpty()) {
	// return true;
	// }
	// String tmpName = className.substring(0, className.length() - 6);
	// boolean flag = false;
	// for (String str : classFilters) {
	// String tmpreg = "^" + str.replace("*", ".*") + "$";
	// Pattern p = Pattern.compile(tmpreg);
	// if (p.matcher(tmpName).find()) {
	// flag = true;
	// break;
	// }
	// }
	// return (checkInOrEx && flag) || (!checkInOrEx && !flag);
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 创建一个扫描处理器， 扫描符合条件的xsd
		ClassPathScanHandler handler = new ClassPathScanHandler();

		Set<File> fileList = handler.getPackageAllFiles("", true);
		for (File cla : fileList) {
			System.out.println(cla.getName());
		}
	}
}
