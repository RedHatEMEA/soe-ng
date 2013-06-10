package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static junit.framework.Assert.assertTrue;
import static org.jboss.soe.puppet.module.test.util.TestUtils.newPackageType;
import static org.jboss.soe.puppet.module.test.util.TestUtils.newPuppetClass;
import static org.jboss.soe.puppet.module.test.util.TestUtils.newTemplateType;

import java.util.List;
import java.util.Map;

import org.jboss.soe.puppet.module.api.PuppetClass;
import org.jboss.soe.puppet.module.api.PuppetType;
import org.junit.Test;


public class PuppetClassTest {
	@Test
	public void createPuppetClass() {
		List<PuppetType> puppetTypes = newLinkedList();
		puppetTypes.add(newTemplateType("demoapp_v1_0/app.conf.erb", "/var/lib/jbossas/app.conf"));
		puppetTypes.add(newPackageType("demoapp_2.5.1", "2.5.1"));
		puppetTypes.add(newPackageType("demoapp_2.7.0", "2.7.0"));

		Map<String, String> params = newHashMap();
		params.put("db_name", "db");
		params.put("db_username", "sa");
		
		PuppetClass puppetClass = newPuppetClass("demoapp", params, puppetTypes);
		String result = puppetClass.process();
		
		// TODO: find a better verification!
		assertTrue(result.contains("class demoapp (\n  $db_name = $db,\n  $db_username = $sa,\n)"));
		assertTrue(result.contains("package { 'demoapp_2.5.1':\n    ensure          => '2.5.1',\n  }"));
		assertTrue(result.contains("package { 'demoapp_2.7.0':\n    ensure          => '2.7.0',\n  }"));
		assertTrue(result.contains("  file { '/var/lib/jbossas/app.conf'"));
	}
}
