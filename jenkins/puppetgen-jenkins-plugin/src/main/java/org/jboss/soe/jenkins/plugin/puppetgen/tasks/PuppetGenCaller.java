package org.jboss.soe.jenkins.plugin.puppetgen.tasks;

import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;

import java.io.IOException;

import org.jboss.soe.jenkins.plugin.puppetgen.PuppetGen;
import org.kohsuke.stapler.DataBoundConstructor;


public class PuppetGenCaller extends Builder {

	private final String userPathToConfiguration;

	@DataBoundConstructor
	public PuppetGenCaller(String userPathToConfiguration) {
		this.userPathToConfiguration = userPathToConfiguration;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
			throws InterruptedException, IOException {

		if (build != null && build.getResult() != null && build.getResult().isWorseThan(Result.SUCCESS)) {
			listener.getLogger().println(PuppetGen.PLUGIN_NAME + " will not run due build not being successful.");
			throw new AbortException();
		} else {
			Boolean result = launcher.getChannel().call(
					new PuppetGenCallable(build.getWorkspace(), listener, userPathToConfiguration));
			if (!result) {
				build.setResult(Result.UNSTABLE);
			}
		}
		return true;
	}

	@Extension(ordinal = 99)
	public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

		public DescriptorImpl() {
			super(PuppetGenCaller.class);
			load();
		}

		@Override
		public String getDisplayName() {
			return PuppetGen.PLUGIN_NAME;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

	public String getUserPathToConfiguration() {
		return userPathToConfiguration;
	}

}
