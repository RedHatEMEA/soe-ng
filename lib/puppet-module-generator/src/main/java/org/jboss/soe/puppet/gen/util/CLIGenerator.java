package org.jboss.soe.puppet.gen.util;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import org.jboss.soe.puppet.gen.parser.AppConfigData;
import org.jboss.soe.puppet.gen.util.CLI.IfBlock.IfBlockBuilder;

/**
 * Generates CLI commands based on {@link AppConfigData}
 *  
 * @author ssadeghi
 */
public class CLIGenerator {
	private static final String LOG_CATEGORY_ADD 			 = "/subsystem=logging/logger=%s:add(level=%S)";
	private static final String LOG_CATEGORY_READ 			 = "/subsystem=logging/logger=%s:read-resource";
	private static final String LOG_CATEGORY_REMOVE 		 = "/subsystem=logging/logger=%s:remove";
	
	private static final String OSGI_CAPABILITY_READ 		 = "/subsystem=osgi/capability=%s:read-resource";
	private static final String OSGI_CAPABILITY_REMOVE 		 = "/subsystem=osgi/capability=%s:remove";
	
	private AppConfigData appConfig;
	private CLI cli = new CLI();
	
	public CLIGenerator(AppConfigData appConfigData) {
		this.appConfig = appConfigData;
		
		addOsgiCapabilities();
		addLogCategories();
	}
	
	public List<String> getCommands() {
		return cli.getCommands();
	}
	
	private void addLogCategories() {
		if (appConfig.getLogCategories().isEmpty()) {
			return;
		}
		
		cli.addComment("Add log categories");

		Map<String, String> logCategories = appConfig.getLogCategories();
		for (String logCategory : logCategories.keySet()) {
			addLogCategory(logCategory, logCategories.get(logCategory));
		}
	}

	private void addLogCategory(final String logCategory, final String level) {
		final String addCommand = format(LOG_CATEGORY_ADD, logCategory, level);

		cli.addIfBlock(IfBlockBuilder.create()
							.withTestCommand(format(LOG_CATEGORY_READ, logCategory))
							.withSuccessCommands(format(LOG_CATEGORY_REMOVE, logCategory), addCommand)
							.withFailCommands(addCommand)
							.build());
	}

	private void addOsgiCapabilities() {
		if (appConfig.getOsgiCapibilities().isEmpty()) {
			return;
		}
		
		cli.addComment("Add OSGI capabilities");

		Map<String, Integer> capibilities = appConfig.getOsgiCapibilities();
		for (final String capability : capibilities.keySet()) {
			final String addOsgiCapability = addOsgiCapability(capability, capibilities.get(capability));
			cli.addIfBlock(IfBlockBuilder.create()
					.withTestCommand(format(OSGI_CAPABILITY_READ, capability))
					.withSuccessCommands(format(OSGI_CAPABILITY_REMOVE, capability), addOsgiCapability)
					.withFailCommands(addOsgiCapability)
					.build());
		}
	}

	private static String addOsgiCapability(String capability, Integer startLevel) {
		StringBuilder str = new StringBuilder();
		str.append("/subsystem=osgi/capability=");
		str.append(capability);
		str.append(":add(");

		if (startLevel != null && startLevel > 0) {
			str.append("startLevel=").append(startLevel);
		}

		str.append(")");
		return str.toString();
	}
}
