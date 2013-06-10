package org.jboss.soe.puppet.gen.parser;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * A cross-version model containing app-config.xml data
 *  
 * @author ssadeghi
 */
public class AppConfigData {
	private String name;
	private String version;
	
	private List<String> systemProperties = newLinkedList();
	private Map<String, Integer> osgiCapibilities = newLinkedHashMap();
	private Multimap<String, String> dependencies = LinkedListMultimap.create();
	private Map<String, String> logCategories = newHashMap();
	private boolean restartOnCompletion = false;
	

	public List<String> getSystemProperties() {
		return systemProperties;
	}

	public Map<String, Integer> getOsgiCapibilities() {
		return osgiCapibilities;
	}

	public Map<String, String> getLogCategories() {
		return logCategories;
	}

	public Map<String, Collection<String>> getDependencies() {
		return dependencies.asMap();
	}
	
	public void addDependency(String name, String version) {
		dependencies.put(name, version);
	}
	
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isRestartRequiredOnCompletion() {
		return restartOnCompletion;
	}

	public void setRestartRequiredOnCompletion(boolean restartOnCompletion) {
		this.restartOnCompletion = restartOnCompletion;
	}
}
