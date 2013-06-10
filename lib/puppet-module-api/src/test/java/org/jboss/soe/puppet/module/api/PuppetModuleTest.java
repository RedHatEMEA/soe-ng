package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.jboss.soe.puppet.module.api.Utils.escapeSingleQuote;
import static org.jboss.soe.puppet.module.api.Utils.sanitize;
import static org.jboss.soe.puppet.module.test.util.TestUtils.assertExists;
import static org.jboss.soe.puppet.module.test.util.TestUtils.newPackageType;
import static org.jboss.soe.puppet.module.test.util.TestUtils.newPuppetClass;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PuppetModuleTest {
	private static final String MODULE_NAME = "demoapp";
	private static final String OUTPUT_DIR = "target/test-output/moduletest";

	@Before
	public void init() {
		File dir = new File(OUTPUT_DIR);
		if (dir.exists()) {
			dir.delete();
		}
	}

	@Test
	public void generateModule() throws Exception {

		Map<String, String> params = createParams();

		PuppetModule module1 = new PuppetModule();
		module1.setName(MODULE_NAME);

		// create puppet types
		List<PuppetType> puppetTypes = newLinkedList();
		puppetTypes.add(newPackageType(MODULE_NAME, "1.0"));

		// create puppet class
		Map<String, String> classParams = getClassParams(MODULE_NAME, params);
		module1.addClass("init.pp", newPuppetClass(MODULE_NAME, classParams, puppetTypes));

		// generate module
		module1.create(OUTPUT_DIR);

		// verify directories exist
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME);
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME + "/templates");
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME + "/manifests");
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME);
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME + "/templates");
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME + "/manifests");

		// check classes exist
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME + "/manifests/init.pp");
		assertExists(OUTPUT_DIR + "/" + MODULE_NAME + "/manifests/init.pp");

		// TODO: verify the content of classes
	}

	private Map<String, String> createParams() {
		Map<String, String> params = newHashMap();
		params.put("prop.key1", "value1");
		params.put("prop.key2", "");
		params.put("prop.key3", "");
		return params;
	}

	private Map<String, String> getClassParams(String moduleName, Map<String, String> params) {
		Map<String, String> classParams = newHashMap();
		for (Map.Entry<String, String> param : params.entrySet()) {
			final String name = sanitize(param.getKey());
			final String value;
			if (isNotBlank(param.getValue())) {
				value = "'" + escapeSingleQuote(param.getValue()) + "'";
			} else {
				value = "$" + moduleName + "_" + name;
			}

			classParams.put(name, value);
		}

		return classParams;
	}
}
