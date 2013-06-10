package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

/**
 * Model for Puppet package type:
 * <code>
 * package { 'mysql':
 *   ensure          => installed,
 * }
 * </code>
 * 
 * @author ssadeghi
 *
 */
public class PuppetPackageType extends AbstractPuppetTypeTemplateProcessor {
	private static final String TEMPLATE_PATH = "templates/puppet-package-type.ftl";

	private static final String PARAM_PACKAGE_NAME = "packageName";

	private static final String PARAM_VERSION = "version";

	private String name;

	private String version;

	public PuppetPackageType(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected Map<String, Object> getAdditionalTemplateParams() {
		Map<String, Object> params = newHashMap();
		params.put(PARAM_PACKAGE_NAME, name);
		params.put(PARAM_VERSION, version);
		return params;
	}

	@Override
	public String getTypeName() {
		return "Package['" + name + "']";
	}
}
