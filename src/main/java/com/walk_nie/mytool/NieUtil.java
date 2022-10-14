package com.walk_nie.mytool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NieUtil {

	public static String readLineFromSystemIn(String hint) throws IOException {
		System.out.println(hint);
		BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while ((line = rd.readLine()) == null) {
			System.out.println("Please type the valid value again!");
		}
		return line;
	}

	public static String toCamelCase(String val) {
		if (val == null || "".equals(val)) {
			return "";
		}
//        String ret = "";
//        if (val.indexOf("_") > 0) {
//            ret = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, val);
//        } else {
//        	String val1 = StringUtil.decamelize(val).toLowerCase();
//            ret = StringUtil.camelize(val1);
//        }
//
//        return StringUtil.decapitalize(ret);
		// TODO
		return "";
	}
}
