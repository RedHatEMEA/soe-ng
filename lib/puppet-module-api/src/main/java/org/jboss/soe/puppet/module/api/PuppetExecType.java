package org.jboss.soe.puppet.module.api;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

/**
 * Model for Puppet type 'exec'
 * 
 * <code>
 * exec { 'run some command':
 *   command => '/bin/command arguments',
 * }
 * </code>
 * 
 * @author ssadeghi
 *
 */
public class PuppetExecType extends AbstractPuppetTypeTemplateProcessor {
	private static final String TEMPLATE_PATH = "templates/puppet-exec-type.ftl";

	private static final String PARAM_COMMENT = "comment";
	private static final String PARAM_COMMAND = "command";
	private static final String PARAM_USER = "user";
	private static final String PARAM_GROUP = "group";
	private static final String PARAM_UNLESS_COMMAND = "unless";

	private String comment;
	private String command;
	private String user;
	private String group;
	private String unlessCommand;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUnlessCommand() {
		return unlessCommand;
	}

	public void setUnlessCommand(String unlessCommand) {
		this.unlessCommand = unlessCommand;
	}

	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected Map<String, Object> getAdditionalTemplateParams() {
		Map<String, Object> params = newHashMap();
		params.put(PARAM_COMMENT, comment);
		params.put(PARAM_COMMAND, command);
		params.put(PARAM_USER, user);
		params.put(PARAM_GROUP, group);
		params.put(PARAM_UNLESS_COMMAND, unlessCommand);
		return params;
	}

	@Override
	public String getTypeName() {
		return "Exec['" + comment + "']";
	}
}
