package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

/**
 * Model for Puppet type 'file'
 * 
 * <code>
 * file { "/etc/app.properties":
 *   source => 'puppet:///modules/modulename/app.properties',
 * }
 * </code>
 * 
 * @author ssadeghi
 *
 */
public class PuppetFileType extends AbstractPuppetTypeTemplateProcessor {
	private static final String TEMPLATE_PATH = "templates/puppet-file-type.ftl";

	private static final String PARAM_TARGET_PATH = "targetPath";
	private static final String PARAM_OWNER = "owner";
	private static final String PARAM_MODULE_NAME = "moduleName";
	private static final String PARAM_FILENAME = "filename";
	private static final String PARAM_MODE = "mode";

	private String moduleName;
	private String targetPath;
	private String fileName; // module/files/$filename
	private String owner;
	private String mode;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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
		params.put(PARAM_TARGET_PATH, targetPath);
		params.put(PARAM_OWNER, owner);
		params.put(PARAM_MODULE_NAME, moduleName);
		params.put(PARAM_FILENAME, fileName);
		params.put(PARAM_MODE, mode);
		return params;
	}
	
	@Override
	public String getTypeName() {
		return "File['" + targetPath + "']";
	}
}
