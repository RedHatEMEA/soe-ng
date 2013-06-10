package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

/**
 * Model for Puppet class
 * 
 * @author ssadeghi
 *
 */
public class PuppetClass extends AbstractTemplateProcessor {
	private static final String TEMPLATE_PATH = "templates/puppet-class.ftl";
	
	private static final String PARAM_NAME = "name";
	private static final String PARAM_CLASS_PARAMS = "params";
	private static final String PARAM_TYPES_OUTPUTS = "typeOutputs";
	private static final String PARAM_DEPENDENCIES = "dependencies";
	
	private String name;
	private List<PuppetType> puppetTypes = newLinkedList();
	private Map<String, String> classParams = newHashMap();
	private List<String> dependencies = newLinkedList();

	public PuppetClass() {
	}
	
	public PuppetClass(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<PuppetType> getPuppetTypes() {
		return puppetTypes;
	}
	
	public <R extends PuppetType> void addPuppetType(R puppetType) {
		puppetTypes.add(puppetType);
	}
	
	public Map<String, String> getClassParams() {
		return classParams;
	}

	public void addClassParam(String name, String defaultValue) {
		classParams.put(name, defaultValue);
	}
	
	public List<String> getDependencies() {
		return dependencies;
	}

	public void addDependency(String dependency) {
		dependencies.add(dependency);
	}

	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected Map<String, Object> getTemplateParams() {
		Map<String, Object> params = newHashMap();
		params.put(PARAM_NAME, name);
		params.put(PARAM_CLASS_PARAMS, classParams);
		params.put(PARAM_DEPENDENCIES, dependencies);
		
		List<String> typeOutputs = newLinkedList();
		for (PuppetType puppetType : puppetTypes) {
			typeOutputs.add(puppetType.process());
		}
		params.put(PARAM_TYPES_OUTPUTS, typeOutputs);
		
		return params;
	}
}
