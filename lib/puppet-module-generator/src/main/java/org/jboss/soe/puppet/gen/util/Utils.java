package org.jboss.soe.puppet.gen.util;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.Reader;

import javax.xml.bind.JAXBContext;

import org.apache.commons.lang3.StringUtils;

/**
 * Various utilities
 * 
 * @author ssadeghi
 */
public final class Utils {
	private Utils() {
	}

	public static String sanitize(String str) {
		return str == null ? "" : str.replaceAll("[^a-zA-Z0-9_]", "_");
	}

	@SuppressWarnings("unchecked")
	public static <K> K parseXml(final Class<K> klass, final Reader xmlReader)
			throws Exception {
		JAXBContext context = JAXBContext.newInstance(klass);
		return (K) context.createUnmarshaller().unmarshal(xmlReader);
	}

	public static String escapeSingleQuote(final String str) {
		return isBlank(str) ? str : str.replace("\\", "\\\\").replace("'",
				"\\'");
	}

	public static String createModuleName(String appName) {
		return createModuleName(appName, null);
	}

	public static String createModuleName(String appName, String version) {
		StringBuilder str = new StringBuilder();
		str.append(sanitize(appName));

		if (StringUtils.isNotBlank(version)) {
			str.append("_v");
			str.append(sanitize(version));
		}

		return str.toString();
	}
}
