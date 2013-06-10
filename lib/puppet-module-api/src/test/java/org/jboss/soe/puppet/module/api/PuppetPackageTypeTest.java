package org.jboss.soe.puppet.module.api;

import static junit.framework.Assert.assertTrue;
import static org.jboss.soe.puppet.module.test.util.TestUtils.newPackageType;

import org.jboss.soe.puppet.module.api.PuppetPackageType;
import org.junit.Test;


public class PuppetPackageTypeTest {
	private static final String RESULT = "  package { 'demoapp':\n" 
									   + "    ensure          => \'2.5.1\',\n" 
									   + "  }";

	@Test
	public void createPackageType() {
		PuppetPackageType type = newPackageType("demoapp", "2.5.1");
		String result = type.process();
		assertTrue(result.equals(RESULT));
	}
}
