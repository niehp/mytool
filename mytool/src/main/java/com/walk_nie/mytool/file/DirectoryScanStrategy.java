package com.walk_nie.mytool.file;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;


public class DirectoryScanStrategy {

	/**
	 * パターン
	 */
	private Pattern pattern;

	/**
	 * 探索結果
	 */
	private Set<String> results = Sets.newHashSet();

	public DirectoryScanStrategy(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isTarget(String resourceName) {
		Matcher matcher = pattern.matcher(resourceName);
		return matcher.matches();
	}

	public void process(String resourceName) {
		results.add(resourceName.replace("\\", "/"));
	}

	public Set<String> results() {
		return results;
	}

}
