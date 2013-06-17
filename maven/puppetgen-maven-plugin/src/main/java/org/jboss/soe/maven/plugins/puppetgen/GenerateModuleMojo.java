package org.jboss.soe.maven.plugins.puppetgen;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jboss.soe.puppet.gen.ModuleGenerator;

@Mojo(name = "generate-module", defaultPhase = LifecyclePhase.PACKAGE)
public class GenerateModuleMojo extends AbstractMojo {
	@Parameter(defaultValue = "${project.build.directory}/puppet", property = "outputDir", required = true)
	private File outputDirectory;

	@Parameter(defaultValue = "${basedir}/configuration/app-config.xml", property = "appConfigPath", required = true)
	private String appConfigPath;

	public void execute() throws MojoExecutionException {
		File outDir = outputDirectory;

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		File appConfig = new File(appConfigPath);
		if (!appConfig.exists()) {
			throw new MojoExecutionException("Application configuration not found at " + appConfigPath);
		}

		ModuleGenerator moduleGenerator = new ModuleGenerator(appConfigPath, outDir.getAbsolutePath());
		try {
			moduleGenerator.generate();
		} catch (Exception e) {
			throw new MojoExecutionException("Error generating puppet module.", e);
		}
	}
}
