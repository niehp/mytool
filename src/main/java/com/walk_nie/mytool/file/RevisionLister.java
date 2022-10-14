package com.walk_nie.mytool.file;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class RevisionLister {
	private File workFolder = null;
	private String checkOutFolder = "export";
	private static String svnRepitoryUrl = "";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String revisionFile = args[0];// revisionFile
		svnRepitoryUrl = args[1];// svn url
		// input : Revision start~end
		// output: folder of Revision
		new RevisionLister().list(revisionFile);
	}

	public void list(String revisionFile) throws IOException {
		List<MyObject> list = parse(revisionFile);

		list.sort(new Comparator<MyObject>() {

			public int compare(MyObject o1, MyObject o2) {
				return o1.Revision.compareTo(o2.Revision);
			}
		});

		report(list);

		executeCmd(list);

	}

	private void executeCmd(List<MyObject> filterList) throws IOException {
		String fmt = "TortoiseProc /command:cat /savepath:%s /path:%s%s /revision:%s";
		for (MyObject obj : filterList) {
			System.out.println("[INFO]export for #" + obj.Revision + " To "
					+ new File(workFolder, checkOutFolder).getAbsolutePath());

			for (String str : obj.fileList) {
				// System.out.println("export for #" + obj.Revision + " To " +pathPrefix);
				String desc = checkOutFolder + "/" + obj.Revision + "_" + obj.ticketNo.replaceAll(" ", "-") + str;
				File tempFile = new File(workFolder, desc);
				// String filePath = pathPrefix + "/" + obj.Revision +"_"+ obj.ticketNo + str;
				// File tempFile = new File(filePath);
				if (!tempFile.exists()) {
					tempFile.getParentFile().mkdirs();
				}
				String cmdLine = String.format(fmt, tempFile.getCanonicalPath(), svnRepitoryUrl, str, obj.Revision);
				// System.out.println(cmdLine);
				cmd(cmdLine);
			}
		}
	}

	public void report(List<MyObject> filterList) throws IOException {
		String fmt = "共通\t\t%s\t%s\t対象無し\t%s\t開発・テスト中\t%s\t%s\t%s";//

		List<String> outputLines = Lists.newArrayList();
		for (MyObject obj : filterList) {
			StringBuffer sb = new StringBuffer();
			if (obj.fileList.size() > 1) {
				sb.append("\"");
			}
			for (int i = 0; i < obj.fileList.size(); i++) {
				String str = obj.fileList.get(i).replace("/trunk/40_AP製造/", "");
				if (i == obj.fileList.size() - 1) {
					sb.append(str);
				} else {
					sb.append(str).append("\n");
				}
			}
			if (obj.fileList.size() > 1) {
				sb.append("\"");
			}
			String aut = "";
			if (obj.Author.equals("nie.M84400")) {
				aut = "ニエ";
			}
			outputLines.add(String.format(fmt, obj.ticketNo, sb.toString(), aut, obj.DateStr, "" + obj.Revision, aut));
		}
		File outputfile = new File(workFolder, "report.txt");
		outputLines.add("\n\n");
		FileUtils.writeLines(outputfile, "UTF-8", outputLines, true);
		System.out.println("[INFO]Save Report to :" + outputfile.getAbsolutePath());

//		String str1 = "",str2="";
//		for (MyObject obj1 : filterList) {
//
//			if (obj1.Author.equals("sakioka.P04712")) {
//				for(String s:obj1.logMsg) {
//					if(s.indexOf("#") != -1) {
//						str1 += s.substring(s.indexOf("#"),s.indexOf("#")+5) +" ";
//					}
//				}
//			}
//			if (obj1.Author.equals("nie.M84400")) {
//				for(String s:obj1.logMsg) {
//					if(s.indexOf("#") != -1) {
//						str2 += "" + s.substring(s.indexOf("#"),s.indexOf("#")+5) +" ";
//					}
//				}
//			}
//
//		}
//		System.out.println(str1);
//		System.out.println(str2);

		// File outputfile = new File("c:/tmp/test/rev" + System.currentTimeMillis() +
		// ".txt");
//		FileUtils.writeLines(outputfile, "UTF-8", outputLines, false);
//		System.out.println("Saved to :" + outputfile.getAbsolutePath());
	}

	private List<MyObject> parse(String revisionFile) throws IOException {

		File fromfile = new File(revisionFile);
		System.out.println("[INFO]process file:" + fromfile.getAbsolutePath());
		workFolder = fromfile.getParentFile();
		List<String> contentList = FileUtils.readLines(fromfile, "UTF-8");
		List<MyObject> list = Lists.newArrayList();
		MyObject obj = null;
		int pos = 0;
		while (true) {
			if (pos >= contentList.size())
				break;
			String line = contentList.get(pos);
			if (StringUtils.isEmpty(line)) {
				pos++;
				continue;
			}
			String key = "Revision:";
			if (line.startsWith(key)) {
				obj = new MyObject();
				list.add(obj);
				obj.Revision = line.replace(key, "").trim();
				pos++;
				continue;
			}
			key = "Author:";
			if (line.startsWith(key)) {
				obj.Author = line.replace(key, "").trim();
				pos++;
				continue;
			}
			key = "Date:";
			if (line.startsWith(key)) {
				obj.DateStr = line.replace(key, "").trim();
				if (obj.DateStr.indexOf(" ") != -1) {
					obj.DateStr = obj.DateStr.substring(0, obj.DateStr.indexOf(" "));
				}
				pos++;
				continue;
			}

			key = "/trunk/";
			if (line.indexOf(key) != -1) {
				obj.fileList.add(line.substring(line.indexOf(key)).trim());
				pos++;
				continue;
			}
			key = "Message:";
			if (line.startsWith(key)) {
				pos++;
				while (true) {
					String newLine = contentList.get(pos);
					if (newLine.startsWith("----")) {
						break;
					}
					obj.logMsg.add(newLine.trim());
					pos++;
				}
				continue;
			}
			pos++;
		}
//		List<MyObject> filterList = Lists.newArrayList();
//		for (MyObject obj1 : list) {
//			if (obj1.Author.equals("sakioka.P04712") || obj1.Author.equals("nie.M84400")) {
//				filterList.add(obj1);
//			}
//		}
		for (MyObject obj1 : list) {
			String ticketNo = "";
			for (String s : obj1.logMsg) {
				if (s.indexOf("#") != -1) {
					ticketNo += s.substring(s.indexOf("#"), s.indexOf("#") + 5) + " ";
				}
				obj1.ticketNo = ticketNo.trim();
			}
		}
		return list;
	}

	public void cmd(String cmdLine) throws IOException {
		Runtime.getRuntime().exec(cmdLine);
	}

	public RevisionLister() {
	}

	class MyObject {
		public String Revision;
		public String Author;
		public String DateStr;
		public String ticketNo;
		public List<String> fileList = Lists.newArrayList();
		public List<String> logMsg = Lists.newArrayList();
	}
}
