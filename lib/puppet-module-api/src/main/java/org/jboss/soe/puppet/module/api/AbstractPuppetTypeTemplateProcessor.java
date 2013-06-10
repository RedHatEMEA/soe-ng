package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.jboss.soe.puppet.module.api.PuppetMetaParameter.MetaParameterType;

/**
 * Abstract class for template enabled Puppet types
 * 
 * @author ssadeghi
 *
 */
public abstract class AbstractPuppetTypeTemplateProcessor extends AbstractTemplateProcessor implements PuppetType {
	protected static final String PARAM_META_PARAMETERS = "metaParams";
	
	protected PuppetMetaParameter metaParameter = new PuppetMetaParameter();
	
	@Override
	public void addMetaParameter(MetaParameterType dependencyType,
			PuppetType puppetType) {
		metaParameter.addDependency(dependencyType, puppetType);
	}

	@Override
	protected Map<String, Object> getTemplateParams() {
		Map<String, Object> params = newHashMap();
		params.put(PARAM_META_PARAMETERS, metaParameter.process());
		params.putAll(getAdditionalTemplateParams());
		return params;
	}
	
	protected abstract Map<String, Object> getAdditionalTemplateParams();
}
