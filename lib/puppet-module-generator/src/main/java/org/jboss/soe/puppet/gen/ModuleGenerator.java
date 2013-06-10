package org.jboss.soe.puppet.gen;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jboss.soe.puppet.gen.util.Utils.createModuleName;
import static org.jboss.soe.puppet.gen.util.Utils.sanitize;
import static org.jboss.soe.puppet.module.api.PuppetModule.DIR_FILES;
import static org.jboss.soe.puppet.module.api.PuppetModule.DIR_TEMPLATES;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.soe.puppet.gen.parser.AppConfigData;
import org.jboss.soe.puppet.gen.parser.AppConfigParser;
import org.jboss.soe.puppet.gen.util.CLIGenerator;
import org.jboss.soe.puppet.module.api.FileTemplate;
import org.jboss.soe.puppet.module.api.PuppetClass;
import org.jboss.soe.puppet.module.api.PuppetExecType;
import org.jboss.soe.puppet.module.api.PuppetFileType;
import org.jboss.soe.puppet.module.api.PuppetMetaParameter.MetaParameterType;
import org.jboss.soe.puppet.module.api.PuppetModule;
import org.jboss.soe.puppet.module.api.PuppetPackageType;
import org.jboss.soe.puppet.module.api.PuppetTemplateType;
import org.jboss.soe.puppet.module.api.PuppetType;

/**
 * Puppet module generator
 * 
 * @author ssadeghi
 */

//TODO: too long. break into smaller classes
//TODO: extract constants to a separate class 
public class ModuleGenerator {
	private static final String JBOSS_USER = "jboss";

	private static final String ROOT_USER = "root";

	private static final String CLI_FILENAME = "setup.cli";

	private static final String CLASS_FILENAME = "init.pp";

	private static final String TEMPLATE_EXT = ".ftl";

	private static final String TEMPLATES_PATH = "templates";

	private static final String PROPS_TEMPLATE_NAME = "application.properties.erb";
	private static final String PROPS_DEST = "/etc/jbossas/application.properties";
	

	private static final String DEFAULT_OUTPUT_DIR = ".";

	private static final String DEFAULT_APP_CONFIG = "/app-config.xml";

	private AppConfigData appConfig;
	
	private String outputDir;
	
	private String appConfigPath;

	public ModuleGenerator(String appConfigPath, String outputDir) {
		this.appConfigPath = appConfigPath;
		this.outputDir = outputDir;
	}

	public ModuleGenerator() {
	}

	private String getOutputDir() {
		return isBlank(outputDir) ? DEFAULT_OUTPUT_DIR : outputDir;
	}
	public void generate() throws Exception {
		appConfig = AppConfigParser.parse(StringUtils.isBlank(appConfigPath) ? DEFAULT_APP_CONFIG : appConfigPath);

		if (requireSharedModule()) {
			createSharedModule();
		}

		createReleaseModule();
	}

	/**
	 * Shared module is only required if there are parameters to be shared
	 * between various versions
	 */
	private boolean requireSharedModule() {
		return !appConfig.getSystemProperties().isEmpty();
	}

	private void createReleaseModule() throws Exception {
		// create puppet module
		PuppetModule puppetModule = new PuppetModule();
		puppetModule.setName(createModuleName(appConfig.getName(),
				appConfig.getVersion()));

		// create puppet class
		PuppetClass releaseClass = createPuppetClass(puppetModule.getName());

		// add package type to install artifact rpm
		PuppetPackageType packageType = addPackageType(releaseClass);

		// add properties template type for system properties
		PuppetTemplateType sysPropsType = addSystemPropertiesResources(
				puppetModule, releaseClass);

		// add CLI templates and file types
		PuppetExecType cliType = addCLIResources(puppetModule, releaseClass);

		// add service restart if needed
		if (appConfig.isRestartRequiredOnCompletion()) {
			addRestartOnCompletion(releaseClass,
					asList(packageType, cliType, sysPropsType));
		}

		puppetModule.addClass(CLASS_FILENAME, releaseClass);
		puppetModule.create(getOutputDir());
	}

	private PuppetExecType addRestartOnCompletion(PuppetClass klass,
			List<? extends PuppetType> requiredTypeList) {
		PuppetExecType cliExecType = new PuppetExecType();
		cliExecType.setComment("Restart JBoss EAP");
		cliExecType.setCommand("/sbin/service jbossas restart");
		cliExecType.setUser(ROOT_USER);
		cliExecType.setGroup(ROOT_USER);

		// make sure it runs after all the others
		for (PuppetType requiredPuppetType : requiredTypeList) {
			if (requiredPuppetType != null) {
				cliExecType.addMetaParameter(MetaParameterType.REQUIRE,
						requiredPuppetType);
			}
		}

		klass.addPuppetType(cliExecType);

		return cliExecType;
	}

	private PuppetClass createPuppetClass(String moduleName) {
		PuppetClass klass = new PuppetClass(moduleName);
		for (String prop : appConfig.getSystemProperties()) {
			String sanitizedParam = sanitize(prop);
			klass.addClassParam(sanitizedParam, sanitize(appConfig.getName())
					+ "_" + sanitizedParam);
		}

		Map<String, Collection<String>> dependencies = appConfig
				.getDependencies();
		for (String dependency : dependencies.keySet()) {
			// add shared app module
			klass.addDependency(createModuleName(dependency));
			for (String version : dependencies.get(dependency)) {
				// add release app modules
				klass.addDependency(createModuleName(dependency, version));
			}
		}

		return klass;
	}

	/**
	 * TODO: Not nice to return null. Refactor to be more graceful
	 * 
	 * @return it returns null if no cli commands needed
	 */
	private PuppetExecType addCLIResources(PuppetModule module,
			PuppetClass klass) {
		// create CLI script for the required CLI commands
		CLIGenerator cliGenerator = new CLIGenerator(appConfig);

		if (cliGenerator.getCommands().isEmpty()) {
			return null;
		}

		FileTemplate cliTemplate = new FileTemplate();
		cliTemplate.setTemplatePath(TEMPLATES_PATH + "/" + CLI_FILENAME
				+ TEMPLATE_EXT);
		cliTemplate.getTemplateParams().put("commands",
				cliGenerator.getCommands());

		module.addTemplate(DIR_FILES + "/" + CLI_FILENAME, cliTemplate);

		// copy the CLI script to nodes
		String cliFileName = "/tmp/" + module.getName() + "_setup.cli";
		PuppetFileType cliFileType = new PuppetFileType();
		cliFileType.setModuleName(module.getName());
		cliFileType.setTargetPath(cliFileName);
		cliFileType.setFileName(CLI_FILENAME);
		cliFileType.setOwner(JBOSS_USER);
		cliFileType.setMode("0600");
		klass.addPuppetType(cliFileType);

		// run the CLI script on nodes
		PuppetExecType cliExecType = new PuppetExecType();
		cliExecType.setComment("Run CLI commands");
		cliExecType.setCommand("/usr/share/jbossas/bin/jboss-cli.sh -c --file="
				+ cliFileName);
		cliExecType.setUser(JBOSS_USER);
		cliExecType.setGroup(JBOSS_USER);
		cliExecType.addMetaParameter(MetaParameterType.REQUIRE, cliFileType);

		klass.addPuppetType(cliExecType);

		return cliExecType;
	}

	private PuppetPackageType addPackageType(PuppetClass releaseClass) {
		PuppetPackageType packageType = new PuppetPackageType(
				appConfig.getName(), appConfig.getVersion());
		releaseClass.addPuppetType(packageType);
		return packageType;
	}

	/**
	 * TODO: Not nice to return null. Refactor to be more graceful
	 * 
	 * @return it returns null if no props needed
	 */
	private PuppetTemplateType addSystemPropertiesResources(
			PuppetModule module, PuppetClass klass) {
		if (appConfig.getSystemProperties().isEmpty()) {
			return null;
		}

		// create .erb template for system properties
		FileTemplate propsTemplate = new FileTemplate();
		propsTemplate.setTemplatePath(TEMPLATES_PATH + "/"
				+ PROPS_TEMPLATE_NAME + TEMPLATE_EXT);
		propsTemplate.getTemplateParams().put("props", getPropTemplateParams());
		propsTemplate.getTemplateParams().put("timestamp", new Date());
		propsTemplate.getTemplateParams().put("puppetClassName",
				klass.getName());

		module.addTemplate(DIR_TEMPLATES + "/" + PROPS_TEMPLATE_NAME,
				propsTemplate);

		// create template puppet type for generating properties file
		PuppetTemplateType propsTemplateType = new PuppetTemplateType();
		propsTemplateType.setDestination(PROPS_DEST);
		propsTemplateType.setMode("0600");
		propsTemplateType.setOwner(JBOSS_USER);
		propsTemplateType.setName(module.getName() + "/" + PROPS_TEMPLATE_NAME);

		klass.addPuppetType(propsTemplateType);

		return propsTemplateType;
	}

	private Map<String, String> getPropTemplateParams() {
		Map<String, String> props = newLinkedHashMap();
		for (String prop : appConfig.getSystemProperties()) {
			props.put(prop, "<%= @" + sanitize(prop) + " %>");
		}

		return props;
	}

	private void createSharedModule() throws Exception {
		PuppetModule puppetModule = new PuppetModule();
		puppetModule.setName(createModuleName(appConfig.getName()));
		puppetModule.addClass(CLASS_FILENAME,
				new PuppetClass(appConfig.getName()));
		puppetModule.create(getOutputDir());
	}
}
