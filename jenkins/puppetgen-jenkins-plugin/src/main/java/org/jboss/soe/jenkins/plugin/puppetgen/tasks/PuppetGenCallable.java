package org.jboss.soe.jenkins.plugin.puppetgen.tasks;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.remoting.Callable;

import java.io.File;
import java.io.IOException;

import org.jboss.soe.jenkins.plugin.puppetgen.PuppetGen;
import org.jboss.soe.puppet.gen.ModuleGenerator;


public class PuppetGenCallable implements Callable<Boolean, IOException> {
	private static final long serialVersionUID = 3575035976816318336L;

	private static final String DEFAULT_APP_CONFIG = "/config/app-config.xml";
	
    private FilePath workspace;
    private BuildListener listener;
    private String pathToConfiguration;


    public PuppetGenCallable(FilePath workspace, BuildListener listener, String pathToConfiguration) {
        super();
        this.workspace = workspace;
        this.listener = listener;
        this.pathToConfiguration = pathToConfiguration;
    }


    public Boolean call() throws IOException {

        String workspacePath = this.workspace.getRemote();
        String appConfigPath = workspacePath + DEFAULT_APP_CONFIG;
        String outputDirectoryPath = workspacePath + "/target/" + PuppetGen.PLUGIN_OUTPUT_DIRECTORY;

        if (isNotBlank(pathToConfiguration)) {
        	appConfigPath = workspacePath + addStartingSlash(pathToConfiguration);
        }

        File appConfig = new File(appConfigPath);
        if (!appConfig.exists()) {
            this.listener.getLogger().println(PuppetGen.PLUGIN_NAME + " will abort. App config not found at '" + appConfigPath + "'");
            return false;
        }

        // The file is present, do the job
        // 1. Create output directory
        File directory = new File(outputDirectoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        this.listener.getLogger().println(PuppetGen.PLUGIN_NAME + " is preparing the workspace at '" + outputDirectoryPath + "'");

        // 2. Generate Puppet module
        ModuleGenerator moduleGenerator = new ModuleGenerator(appConfigPath, outputDirectoryPath);

        this.listener.getLogger().println(PuppetGen.PLUGIN_NAME + " is ready to process '" + appConfigPath + "'");

        try {
            moduleGenerator.generate();
        } catch (Exception e) {
            this.listener.getLogger().println(PuppetGen.PLUGIN_NAME + " failed to generate puppet module.");
            e.printStackTrace(this.listener.getLogger());
            return false;
        }

        return true;

    }

    private String addStartingSlash(String path) {
    	return path.startsWith("/") ? path : "/" + path;
    }
}
