package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.io.File.separator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Model for Puppet module
 * 
 * @author ssadeghi
 *
 */
public class PuppetModule {
	public static final String DIR_TEMPLATES = "templates";

	public static final String DIR_MANIFESTS = "manifests";
	
	public static final String DIR_FILES = "files";

	private String name;
	
	// mapping of target puppet class filenames to puppet class
	// e.g. init.pp => new PuppetClass()
	private Map<String, PuppetClass> classes = newHashMap();
	
	// mapping of target template path (relative to module dir) to template
	// e.g. file/setup.cli => new FileTemplate()
	private Map<String, FileTemplate> templates = newHashMap();

	public PuppetModule() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addClass(String fileName, PuppetClass klass) {
		classes.put(fileName, klass);
	}
	
	public Map<String, PuppetClass> getClasses() {
		return classes;
	}
	
	public Map<String, FileTemplate> getTemplates() {
		return templates;
	}
	
	public void addTemplate(String destinationPath, FileTemplate template) {
		templates.put(destinationPath, template);
	}

	public void create(String baseDir) throws IOException {
		createDirectories(baseDir);

		// export puppet classes
		for (String className : classes.keySet()) {
			String path = getModulePath(baseDir, DIR_MANIFESTS + separator + className);
			try (FileWriter out = new FileWriter(path)) {
				classes.get(className).process(out);
			}
		}
		
		// create required puppet templates
		for (String name : templates.keySet()) {
			String path = getModulePath(baseDir, name);
			try (FileWriter out = new FileWriter(path)) {
				templates.get(name).process(out);
			}
		}
	}

	private void createDirectories(String baseDir) {
		final List<String> dirs = newArrayList(
					getModulePath(baseDir, DIR_MANIFESTS),
					getModulePath(baseDir, DIR_TEMPLATES),
					getModulePath(baseDir, DIR_FILES)
					);
		
		for (String dir : dirs) {
			final File file = new File(dir);

			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	private String getModulePath(String baseDir, String moduleRelativePath) {
		return baseDir + separator + name + separator + moduleRelativePath;
	}
}
