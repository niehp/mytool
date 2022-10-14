package com.walk_nie.mytool.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

public class FileScan2 {

	private List<File> scanedFiles = Lists.newArrayList();

	public static void main(String[] args) throws Exception {
		new FileScan2().check();
	}

	private void check() throws Exception {
		String rootPath = "";
		scanFile(new File(rootPath));
		rootPath = "";
		scanFile(new File(rootPath));

		String fmt = "%s\t%s\t%s\t%s";
		for (File file : scanedFiles) {
			String name = file.getName();
			// System.out.println(name);
			String[] spl = name.split("_");
			// System.out.println(spl.length);
			String cateKino1 = file.getParentFile().getName();
			String cateKino = "";
			if (cateKino1.indexOf(".") > 0) {
				cateKino = cateKino1.substring(cateKino1.indexOf(".") + 1);
			} else {
				cateKino = cateKino1;
			}
			String id = "";
			String gamenName = "";
			String gamen = "";
			if (spl.length == 1) {
				System.err.println(file.getCanonicalPath());
			} else if (spl.length == 2) {
				System.err.println(file.getCanonicalPath());
			} else if (spl.length == 3) {
				gamen = "画面";
				gamenName = spl[1];
			} else if (spl.length == 5) {
				System.err.println(file.getCanonicalPath());
			} else if (spl.length == 6) {
				gamen = spl[0].equals("画面項目定義書") ? "画面" : "バッチ";
				gamenName = spl[4];
				id = spl[1] + "_" + spl[2] + "_" + spl[3];
			} else if (spl.length == 7) {
				gamen = spl[0].equals("画面項目定義書") ? "画面" : "バッチ";
				gamenName = spl[4];
				id = spl[1] + "_" + spl[2];
				// System.err.println(file.getCanonicalPath());
			} else if (spl.length == 8) {
				// System.err.println(file.getCanonicalPath());
				gamen = spl[0].equals("画面項目定義書") ? "画面" : "バッチ";
				gamenName = spl[4] + "_" + spl[5] + "_" + spl[6];
				id = spl[1] + "_" + spl[2] + "_" + spl[3];
			} else if (spl.length == 9) {
				gamen = spl[0].equals("画面項目定義書") ? "画面" : "バッチ";
				gamenName = spl[5] + "_" + spl[6] + "_" + spl[7];
				id = spl[1] + "_" + spl[2] + "_" + spl[3] + "_" + spl[4];
			} else {
				System.err.println(file.getCanonicalPath());
			}
			System.out.println(String.format(fmt, cateKino, gamen, id,
					gamenName));
		}
	}

	private void scanFile(File rootFile) throws IOException {
		if (!rootFile.canRead()) {
			return;
		}

		if (rootFile.isFile()) {
			String name = rootFile.getName();
			if (name.endsWith(".xlsx")) {
				scanedFiles.add(rootFile);
			}
			return;
		}

		if (rootFile.isDirectory()) {
			File[] files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				scanFile(files[i]);
			}
		}
	}
}
