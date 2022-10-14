package com.walk_nie.mytool.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileCopyer {

	public static void main(String[] args) throws Exception {
		String filepath = args[0];
		int repeatCnt = 10;
		if (args.length > 1) {
			repeatCnt = Integer.parseInt(args[1]);
		}
		new FileCopyer().doit(filepath, repeatCnt);
	}

	private void doit(String filepath, int repeatCnt) throws Exception {
		File srcFile = new File(filepath);
		String filename = srcFile.getName();
		int dotIdx = filename.lastIndexOf(".") ;
		String filename1 = filename.substring(0, dotIdx - 3);
		String ext = filename.substring(dotIdx + 1);
		for (int i = 1; i <= repeatCnt; i++) {
			Path topath = Paths.get(srcFile.getParentFile().getAbsolutePath(), filename1 + String.format("%03d", i) + "." + ext);
			Files.copy(srcFile.toPath(), topath, StandardCopyOption.REPLACE_EXISTING);
		}
	}

}
