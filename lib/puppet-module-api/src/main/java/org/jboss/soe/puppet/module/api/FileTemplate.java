package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Map;

/**
 * Model for freemarker templates
 * 
 * @author ssadeghi
 */
public class FileTemplate extends AbstractTemplateProcessor {
	private String templatePath;

	private Map<String, Object> templateParams = newLinkedHashMap();

	@Override
	public String getTemplatePath() {
		return templatePath;
	}

	@Override
	public Map<String, Object> getTemplateParams() {
		return templateParams;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
}
