package org.jboss.soe.puppet.module.api;

import static junit.framework.Assert.assertTrue;

import org.jboss.soe.puppet.module.api.PuppetTemplateType;
import org.jboss.soe.puppet.module.test.util.TestUtils;
import org.junit.Test;


public class PuppetTemplateTypeTest {
	private static final String RESULT = "  file { '/var/lib/jbossas/app.conf':\n" +
			"    ensure          => file,\n" +
			"    owner           => 'jboss',\n" +
			"    content         => template('demoapp_v1_0/app.conf.erb'),\n" +
			"    mode            => '0600',\n" +
			"  }";
    

	@Test
	public void createTemplateType() {
		PuppetTemplateType type = TestUtils.newTemplateType("demoapp_v1_0/app.conf.erb",
															"/var/lib/jbossas/app.conf");
		String result = type.process();
		
		assertTrue(result.equals(RESULT));
	}
}
