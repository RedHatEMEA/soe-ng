package org.jboss.soe.puppet.gen.util;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.File;

public class TestUtils {
	public static void assertExists(String path) {
		assertTrue(new File(path).exists());
	}
	
	public static void assertNotExist(String path) {
		assertFalse(new File(path).exists());
	}
}
