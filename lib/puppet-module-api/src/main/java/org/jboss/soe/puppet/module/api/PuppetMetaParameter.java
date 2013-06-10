package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * Model for Puppet metaparameters to describe dependency and order. The
 * following metaparameters are supported: 
 * <ul>
 * <li>require</li>
 * <li>before</li>
 * <li>notify</li>
 * <li>subscribe<li>
 * </ul>
 * 
 * @author ssadeghi
 *
 */
public class PuppetMetaParameter extends AbstractTemplateProcessor {

	public static enum MetaParameterType {
		REQUIRE, BEFORE, NOTIFY, SUBSCRIBE
	}
	
	private static final String TEMPLATE_PATH = "templates/puppet-metaparameter.ftl";
	
	private static final String PARAM_META_PARAMETERS = "metaParameters";
	
	private Multimap<MetaParameterType, PuppetType> metaParametersMap = LinkedListMultimap.create();
	
	public PuppetMetaParameter() {
	}

	public void addDependency(MetaParameterType metaParameterType, PuppetType puppetType) {
		metaParametersMap.put(metaParameterType, puppetType);
	}
	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected Map<String, Object> getTemplateParams() {
		Map<String, Object> params = newHashMap();
		params.put(PARAM_META_PARAMETERS, metaParametersMap.asMap().entrySet());
		return params;
	}
}
