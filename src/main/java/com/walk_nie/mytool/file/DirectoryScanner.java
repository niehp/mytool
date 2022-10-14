package com.walk_nie.mytool.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

public class DirectoryScanner {

	protected static final String CLASS_NAME_SEPARATOR = ".";

	protected static final String RESOURCE_SEPARATOR = "/";

	private String prefix;
	private final DirectoryScanStrategy strategy;

	public DirectoryScanner(String prefix, DirectoryScanStrategy scanStrategy) {
		this.prefix = prefix;
		this.strategy = scanStrategy;
	}

	public void scan() {

		Set<String> fileSet = Sets.newLinkedHashSet();
		if (prefix.indexOf(":") != -1) {
			// file path
			fileSet = findFilesByFilePath(prefix);
		} else if (prefix.startsWith("./") || prefix.startsWith(".\\")) {
			// file path
			fileSet = findFilesByFilePath(prefix);
		} else {
			// package path
			fileSet = findFilesByPackagePath(prefix);
		}
		for (String file : fileSet) {
			if (strategy.isTarget(file)) {
				strategy.process(file);
			}
		}
	}

	private Set<String> findFilesByFilePath(String rootFilePath) {
		Set<String> fileSet = Sets.newLinkedHashSet();
		File rootFolder = new File(rootFilePath);
		if (!rootFolder.isDirectory()) {
			return fileSet;
		}
		try {
			scanFile(rootFolder, fileSet);
		} catch (IOException e) {
		}
		return fileSet;
	}

	private void scanFile(File rootFile, Set<String> fileSet) throws IOException {
		if (!rootFile.canRead()) {
			return;
		}
		if (rootFile.isFile()) {
			fileSet.add(rootFile.getCanonicalPath());
			return;
		}
		if (rootFile.isDirectory()) {
			File[] files = rootFile.listFiles();
			if (files.length == 0) {
				//rootFile.delete();
				return;
			}
			for (int i = 0; i < files.length; i++) {
				scanFile(files[i], fileSet);
			}
		}
	}

	private Set<String> findFilesByPackagePath(String prefix) {
		Set<String> fileSet = Sets.newLinkedHashSet();

		Set<URL> urls = Sets.newLinkedHashSet();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		while (loader != null) {
			if (loader instanceof URLClassLoader) {
				Collections.addAll(urls, ((URLClassLoader) loader).getURLs());
			}
			loader = loader.getParent();
		}
		prefix = prefix.replace(CLASS_NAME_SEPARATOR, RESOURCE_SEPARATOR);
		for (URL url : urls) {
			File f = new File(url.getFile());
			if (!f.isDirectory())
				continue;

			File rootFile;
			try {
				rootFile = Paths.get(f.getCanonicalPath(), prefix).normalize().toFile();
				scanFile(rootFile, fileSet);
			} catch (IOException e) {
			}
		}

		return fileSet;
	}
}
