package org.jboss.soe.puppet.module.test.util;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jboss.soe.puppet.module.api.PuppetClass;
import org.jboss.soe.puppet.module.api.PuppetPackageType;
import org.jboss.soe.puppet.module.api.PuppetTemplateType;
import org.jboss.soe.puppet.module.api.PuppetType;


public class TestUtils {
	public static PuppetPackageType newPackageType(String name, String version) {
		return new PuppetPackageType(name, version);
	}
	
	public static PuppetTemplateType newTemplateType(
							String path,
							String destination) {
		PuppetTemplateType type = new PuppetTemplateType();
		type.setDestination(destination);
		type.setMode("0600");
		type.setOwner("jboss");
		type.setName(path);
		
		return type;
	}
	
	public static PuppetClass newPuppetClass(String name,
									Map<String, String> params,
									List<PuppetType> puppetTypes) {
		PuppetClass puppetClass = new PuppetClass();
		puppetClass.setName(name);
		
		for (String paramName : params.keySet()) {
			puppetClass.addClassParam(paramName, params.get(paramName));
		}

		puppetClass.getPuppetTypes().addAll(puppetTypes);
		
		return puppetClass;
	}
	
	public static void assertExists(String path) {
		assertTrue(new File(path).exists());
	}
}
