package com.walk_nie.mytool.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class FileScan {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		listFile();
		// rename();
		// loaderPath();
	}

	protected static void listFile() {
		String workPath = System.getProperty("scan.path");
		if (StringUtils.isEmpty(workPath)) {
			return;
		}
		String[] paths = workPath.split(",");
		for (String path : paths) {
			DirectoryScanStrategy strategy = new DirectoryScanStrategy(Pattern.compile(".*.*"));
			DirectoryScanner scanner = new DirectoryScanner(path, strategy);
			scanner.scan();
			Set<String> files = strategy.results();
			for (String file : files) {
				File f = new File(file);
				if (!f.isFile())
					continue;
				String s = f.getAbsolutePath();
				if (!(s.endsWith(".java") || s.endsWith(".sql") || s.endsWith(".xml"))) {
					continue;
				}
				if (s.indexOf("\\migration\\") != -1)
					continue;
				if (s.indexOf("\\entity\\") != -1)
					continue;
				int idx = s.lastIndexOf("\\");
				String f1 = s.substring(0, idx);
				if (f1.indexOf("biz-im\\") != -1) {
					f1 = f1.substring(f1.indexOf("biz-im\\") + "biz-im\\".length());
				}
				String f2 = s.substring(idx + 1);
				if (f2.startsWith("Abstract") && f2.endsWith(".java"))
					continue;
				System.out.println(String.format("%s\t%s", f1, f2));
			}
		}
	}

	protected static void rename() throws IOException {

		DirectoryScanStrategy strategy = new DirectoryScanStrategy(Pattern.compile(".*.st"));
		DirectoryScanner scanner = new DirectoryScanner(System.getProperty("bl.path"), strategy);
		scanner.scan();
		Set<String> files = strategy.results();
		for (String file : files) {
			File f = new File(file);
			// *.stencil -> *.st
//			if(f.getName().endsWith(".stencil")) {
//				String newFileName = f.getName().substring(0,f.getName().indexOf(".stencil")) +".st";
//				Path frompath = Paths.get(file);
//				Path topath = Paths.get(f.getParent(),newFileName);
//				//Files.move(frompath, topath, StandardCopyOption.REPLACE_EXISTING);
//			}
			// *.java -> *.java.st
//			if(f.getName().endsWith(".java")) {
//				String newFileName = f.getName() +".st";
//				Path frompath = Paths.get(file);
//				Path topath = Paths.get(f.getParent(),newFileName);
//				Files.move(frompath, topath, StandardCopyOption.REPLACE_EXISTING);
//			}
			if (f.getName().endsWith("Logic.java.st") || f.getName().endsWith("LogicImpl.java.st")) {
				String newFileName = f.getName().replace("AcceptDestroy", "_featherIdCap__methodNameCap_");
				newFileName = newFileName.replace("EntryCreateUpdate", "_featherIdCap__methodNameCap_");
				newFileName = newFileName.replace("EntryRecalculation", "_featherIdCap__methodNameCap_");
				newFileName = newFileName.replace("SearchFilter", "_featherIdCap__methodNameCap_");
				newFileName = newFileName.replace("AcceptSlip", "_featherIdCap_");
				Path frompath = Paths.get(file);
				Path topath = Paths.get(f.getParent(), newFileName);
				Files.move(frompath, topath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	protected static void loaderPath() {

		String javaClassPath = System.getProperty("java.class.path");
		if (javaClassPath != null) {
			for (String path : javaClassPath.split(File.pathSeparator)) {
				System.out.println(path);
			}
		}

//        ClassLoader loader = RadClassUtils.getContextClassLoader();
//        while (loader != null) {
//            if (loader instanceof URLClassLoader) {
//            	URL[] rs = ((URLClassLoader) loader).getURLs();
//            	for(URL r:rs) {
//                System.out.println(r);
//            	}
//            }
//            loader = loader.getParent();
//        }
	}

}
