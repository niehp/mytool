package com.walk_nie.mytool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

import com.google.common.collect.Maps;

public class NieConfigUtil {
	private static Properties prop = new Properties();
	private static String configFolder = "config/";
	private static Map<File,Long> lastModifiedMap = Maps.newHashMap();

	private static void init() {
		File folder = Paths.get(configFolder).normalize().toFile();
		File[] files = folder.listFiles();
		if(files == null) {
			System.out.println("[ERROR][Config Folder is EMPTY]Folder = " + folder.getAbsolutePath());
			return;
		}
		for(File f:files) {
			if(!f.canRead())continue;
			if(!f.isFile()) continue;
			try {
				long lastModified = f.lastModified();
				Long old =  lastModifiedMap.getOrDefault(f, 0L);
				if(lastModified > old) {
					prop.load(new InputStreamReader(new FileInputStream(f),"UTF-8"));
					lastModifiedMap.put(f, lastModified);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Map<String,String> getConfigsByPrefix(final String keyPrefix){
			init();
		final Map<String, String> map = Maps.newHashMap();
		prop.forEach(new BiConsumer<Object, Object>() {

			
			public void accept(Object key, Object value) {
				String keyStr = ((String) key);
				if (keyStr.startsWith(keyPrefix)) {
					if (keyStr.equals(key)) {
						map.put(keyStr, (String) value);
					} else {
						String newKey = keyStr.substring(keyPrefix.length() + 1);
						map.put(newKey, (String) value);
					}
				}
			}
		});
		return map;
	}

	public static String getConfig(String key){
			init();
		return (String)prop.getOrDefault(key, "");
	}
}
