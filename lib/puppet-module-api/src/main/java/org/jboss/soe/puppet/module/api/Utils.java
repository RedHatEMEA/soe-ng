package org.jboss.soe.puppet.module.api;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Various utilities
 * 
 * @author ssadeghi
 *
 */
final class Utils {
	private Utils() {
	}

	static String sanitize(String str) {
		return str == null ? "" : str.replaceAll("[^a-zA-Z0-9_]", "_");
	}
	
	static String escapeSingleQuote(final String str) {
		return isBlank(str) ? str : str.replace("\\", "\\\\").replace("'", "\\'");  
	}
}
