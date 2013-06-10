package org.jboss.soe.puppet.gen;

import static org.jboss.soe.puppet.gen.util.TestUtils.assertExists;
import static org.jboss.soe.puppet.gen.util.TestUtils.assertNotExist;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ModuleGeneratorTest {
	private static final String APP_CONFIG = "src/test/resources/app-config.xml";
	private static final String APP_CONFIG_EMPTY = "src/test/resources/app-config-empty.xml";
	private static final String OUTPUT_DIR = "target/test-output/modulegentest";

	@Before
	public void init() {
		File dir = new File(OUTPUT_DIR);
		if (dir.exists()) {
			dir.delete();
		}
	}

	@Test
	public void generateModule() throws Exception {
		ModuleGenerator generator = new ModuleGenerator(APP_CONFIG, OUTPUT_DIR);
		generator.generate();
		
		// verify directories exist
		assertExists(OUTPUT_DIR + "/foo");
		assertExists(OUTPUT_DIR + "/foo/templates");
		assertExists(OUTPUT_DIR + "/foo/manifests");
		assertExists(OUTPUT_DIR + "/foo_v1_0");
		assertExists(OUTPUT_DIR + "/foo_v1_0/templates");
		assertExists(OUTPUT_DIR + "/foo_v1_0/manifests");

		// check classes exist
		assertExists(OUTPUT_DIR + "/foo/manifests/init.pp");
		assertExists(OUTPUT_DIR + "/foo_v1_0/manifests/init.pp");

		// check props file exist
		assertExists(OUTPUT_DIR + "/foo_v1_0/templates/application.properties.erb");
		
		// check if cli script exists
		assertExists(OUTPUT_DIR + "/foo_v1_0/files/setup.cli");

		// TODO: verify the content of classes
	}
	
	@Test
	public void generateEmptyModule() throws Exception {
		ModuleGenerator generator = new ModuleGenerator(APP_CONFIG_EMPTY, OUTPUT_DIR);
		generator.generate();
		
		assertNotExist(OUTPUT_DIR + "/foo_empty");
		assertExists(OUTPUT_DIR + "/foo_empty_v1_0");
		assertNotExist(OUTPUT_DIR + "/foo_empty_v1_0/templates/application.properties.erb");
		assertNotExist(OUTPUT_DIR + "/foo_empty_v1_0/files/setup.cli");
	}
}
