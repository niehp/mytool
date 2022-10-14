package com.walk_nie.mytool.groovy;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilationFailedException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.walk_nie.mytool.NieConfigUtil;
import com.walk_nie.mytool.file.DirectoryScanStrategy;
import com.walk_nie.mytool.file.DirectoryScanner;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

/**
 * テンプレートによる リソースを生成するツール<br>
 * <p>
 * INPUT
 * </p>
 * <ul>
 * <li>work path are specified by {@code groovy.generation.work.path}.</li>
 * </ul>
 * 
 * @author Haiping.Nie
 *
 */
public class GroovyUV2 {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws CompilationFailedException
	 */
	public static void main(String[] args) throws CompilationFailedException, ClassNotFoundException, IOException {
		new GroovyUV2().generate();
	}

	public GroovyUV2() {
	}

	private void generate() throws IOException, CompilationFailedException, ClassNotFoundException {

		String workPath = NieConfigUtil.getConfig("groovy.generation.work.path");
		if (StringUtils.isEmpty(workPath)) {
			System.out.println("[INFO][groovy.generation.work.path] are NOT Specified in groovy.conf");
			return;
		}
		Map<String, String> bindAttribute = Maps.newHashMap();
		bindCommonAtribute(bindAttribute);
		List<Map<String, String>> loop = findAttributeList(workPath);
		if (CollectionUtils.isEmpty(loop)) {
			System.out.println("[INFO]NONE target!");
			return;
		}
		String stpathNew = Paths.get(workPath, "in_st").toAbsolutePath().toFile().getAbsolutePath();
		// resources : full path of files
		Set<String> resources = getResource(stpathNew);
		if (CollectionUtils.isEmpty(resources)) {
			System.out.println("[ERROR][Template Folder is EMPTY]Folder = " + stpathNew);
			return;
		}
		SimpleTemplateEngine engine = new SimpleTemplateEngine();
		for (Map<String, String> record : loop) {
			for (String res : resources) {
				System.out.println("[INFO]Generating by " + res);

				record.putAll(bindAttribute);

				String pathSuffix = res.substring(res.indexOf(stpathNew) + stpathNew.length());
				pathSuffix = buildupSTPath(record, pathSuffix);
				if (pathSuffix.endsWith(".st")) {
					pathSuffix = pathSuffix.substring(0, pathSuffix.lastIndexOf(".st"));
				}

				Template template = createTemplate(engine, res);
				String content = template.make(record).toString();
				Path outputFilePath = Paths.get(workPath, "out", pathSuffix);
				File outputFile = outputFilePath.normalize().toFile();
				System.out.println("[INFO]Save to " + outputFile.getCanonicalPath());
				FileUtils.write(outputFile, content, "UTF-8", true);
			}
		}

	}

	Map<String, Template> tempateMap = Maps.newHashMap();

	private Template createTemplate(SimpleTemplateEngine engine, String res) throws IOException {
		Template tempate = tempateMap.get(res);
		if (tempate != null) {
			return tempate;
		}
		String stData = FileUtils.readFileToString(new File(res), "UTF-8");
		Template template = engine.createTemplate(new StringReader(stData));
		tempateMap.put(res, template);
		return template;
	}

	private String buildupSTPath(Map<String, String> bindAttribute, String pathSuffix) {
		// replace the binding variable to binding value.
		Iterator<String> keys = bindAttribute.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Object valueObject = bindAttribute.get(key);
			if (valueObject instanceof String) {
				String replaceKey = "_" + key + "_";
				String value = String.class.cast(valueObject);
				pathSuffix = pathSuffix.replaceAll(replaceKey, value);
			}
		}
		return pathSuffix;
	}

	private List<Map<String, String>> findAttributeList(String workPath) throws IOException {
		List<Map<String, String>> attrList = Lists.newArrayList();

		File folder = Paths.get(workPath, "in_attr").normalize().toFile();
		File[] files = folder.listFiles();
		if (files == null) {
			System.out.println("[ERROR][Attribute Folder is EMPTY]Folder = " + folder.getAbsolutePath());
			return attrList;
		}
		for (File file : files) {
			if (!file.isFile())
				continue;
			String fileName = file.getName();
			if (fileName.indexOf(".") != -1) {
				fileName = fileName.substring(0, file.getName().indexOf("."));
			}
			List<String> lines = FileUtils.readLines(file, "UTF-8");
			if (CollectionUtils.isEmpty(lines)) {
				continue;
			}
			String firstLine = lines.get(0);
			if (firstLine.indexOf("\t") == -1) {
				// each file VS 1 key
				for (int i = 0; i < lines.size(); i++) {
					if (StringUtils.isEmpty(lines.get(i)))
						continue;
					if (attrList.size() < (i + 1)) {
						attrList.add(new HashMap<String,String>());
					}
					attrList.get(i).put(fileName, lines.get(i));
					attrList.get(i).put(fileName + "Cap", StringUtils.capitalize(lines.get(i)));
				}
			} else {
				// each file VS many key!
				// splited by tab! first line is title
				String[] keys = firstLine.split("\t");
				for (int i = 0; i < lines.size() - 1; i++) {
					if (StringUtils.isEmpty(lines.get(i + 1)))
						continue;
					if (attrList.size() < (i + 1)) {
						attrList.add(new HashMap<String,String>());
					}
					String[] items = firstLine.split("\t");
					for (int j = 0; j < keys.length; j++) {
						attrList.get(i).put(keys[j], items[j]);
						attrList.get(i).put(keys[j] + "Cap", StringUtils.capitalize(items[j]));
					}
				}
			}
		}

		return attrList;
	}

	private Set<String> getResource(String stpath) {
		DirectoryScanStrategy strategy = new DirectoryScanStrategy(Pattern.compile(".*.st"));
		DirectoryScanner scanner = new DirectoryScanner(stpath, strategy);
		scanner.scan();
		return strategy.results();
	}

	private void bindCommonAtribute(Map<String, String> bindAttribute) {
		bindAttribute.put("outputPathRoot", NieConfigUtil.getConfig("groovy.generation.outputPathRoot"));
		bindAttribute.put("packageRoot", NieConfigUtil.getConfig("groovy.generation.packageRoot"));
		bindAttribute.put("copyright", NieConfigUtil.getConfig("groovy.generation.copyright"));
		bindAttribute.put("author", NieConfigUtil.getConfig("groovy.generation.author"));
		// Map<String, String> attributes =
		// NieConfigUtil.getConfigsByPrefix("groovy.generation.attribute");
		// bindAttribute.putAll(attributes);
	}

}
