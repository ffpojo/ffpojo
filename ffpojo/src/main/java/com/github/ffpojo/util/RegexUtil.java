package com.github.ffpojo.util;

public class RegexUtil {

	public static String escapeRegexMetacharacters(String s) {
		char[] charArray = s.toCharArray();
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < charArray.length; i++) {
			switch (charArray[i]) {
				case '^': case '[': case '.':  case '$': case '{':
				case '*': case '(': case '\\': case '+': case ')':
				case '|': case '?': case '<':  case '>':
					sbuf.append("\\");
					sbuf.append(charArray[i]);
					break;
				default:
					sbuf.append(charArray[i]);
			}
		}
		return sbuf.toString();
	}
	
}
