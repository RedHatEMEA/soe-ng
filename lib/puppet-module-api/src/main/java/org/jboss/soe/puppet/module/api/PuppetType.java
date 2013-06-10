package org.jboss.soe.puppet.module.api;

import org.jboss.soe.puppet.module.api.PuppetMetaParameter.MetaParameterType;

/**
 * Marker for Puppet types 
 * 
 * @author ssadeghi
 *
 */
public interface PuppetType extends TemplateProcessor {
	void addMetaParameter(MetaParameterType dependencyType, PuppetType puppetType);
	
	String getTypeName();
}
