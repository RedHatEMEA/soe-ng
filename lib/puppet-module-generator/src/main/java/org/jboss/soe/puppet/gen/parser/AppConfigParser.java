package org.jboss.soe.puppet.gen.parser;

import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;


/**
 * This parse needs to be backward-compatible and be able to parse
 * all versions of app-config schema
 * 
 * @author ssadeghi
 */
public class AppConfigParser {
	private static final String VERSION_1_0 = "app-config-1.0.xsd";
	
	public static AppConfigData parse(final String appConfig) throws Exception {
		String schemaLocation = getSchemaLocation(appConfig);
		
		if (VERSION_1_0.equals(schemaLocation)) {
			return AppConfigV10Parser.parse(appConfig);
		}
		
		throw new UnsupportedOperationException("Don't know how to parse configs based on " + schemaLocation);
	}

	protected static String getSchemaLocation(final String appConfig) throws Exception {
		final File file = new File(appConfig);
		if (!file.exists()) {
			throw new IllegalStateException("App config file doesn't exist at '" + appConfig + "'");
		}
		
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		final XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(appConfig));
		
		reader.require(START_DOCUMENT, null, null);
		
		// read until the first start element
		while (reader.hasNext() && reader.next() != START_ELEMENT) {
			// NOOP
		}
		
		final int count = reader.getAttributeCount();
		if (count == 0) {
			return null;
		}
		String loc = null;
		for (int i = 0; i < count; i++) {
			if ("http://www.w3.org/2001/XMLSchema-instance".equals(reader.getAttributeNamespace(i)) && "schemaLocation".equals(reader.getAttributeLocalName(i))) {
				loc = reader.getAttributeValue(i);
				break;
			}
		}
		if (loc != null) {
			int pos = loc.indexOf(' ');
			if (pos > 0) {
				loc = loc.substring(pos + 1);
			}
		}
		return loc != null ? loc.trim() : null;
	}
}
