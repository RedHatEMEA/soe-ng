package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

/**
 * 
 * Model for puppet file type with template
 * 
 * <code>
 *   file { '/var/lib/jbossas/app.properties':
 *     ensure          => file,
 *     owner           => 'jboss',
 *     content         => template('modulename/app.properties.erb'),
 *     mode            => '0600',
 *   }
 * </code>
 * 
 * @author ssadeghi
 *
 */
public class PuppetTemplateType extends AbstractPuppetTypeTemplateProcessor {
	private static final String TEMPLATE_PATH = "templates/puppet-template-type.ftl";
	
	private static final String PARAM_NAME = "name";

	private static final String PARAM_DESTINATION = "destination";

	private static final String PARAM_OWNER = "owner";

	private static final String PARAM_MODE = "mode";
	
	private String name;
	
	private String destination;
	
	private String owner;
	
	private String mode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected Map<String, Object> getAdditionalTemplateParams() {
		Map<String, Object> params = newHashMap();
		params.put(PARAM_NAME, name);
		params.put(PARAM_DESTINATION, destination);
		params.put(PARAM_MODE, mode);
		params.put(PARAM_OWNER, owner);
		
		return params;
	}
	
	@Override
	public String getTypeName() {
		return "File['" + destination + "']";
	}
}
