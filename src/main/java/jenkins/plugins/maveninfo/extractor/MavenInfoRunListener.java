package jenkins.plugins.maveninfo.extractor;

import hudson.Extension;
import hudson.Launcher;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.io.IOException;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;

/**
 * 
 * Run Listener to extract Maven project information.
 * 
 * @author emenaceb
 *
 */
@Extension
public class MavenInfoRunListener extends RunListener<Run<?, ?>> {

	@Override
	public Environment setUpEnvironment(AbstractBuild build, Launcher launcher,
			BuildListener listener) throws IOException, InterruptedException {

		AbstractProject<?, ?> prj = build.getProject();
		if (build instanceof MavenModuleSetBuild
				&& prj instanceof MavenModuleSet) {

			MavenModuleSet mms = (MavenModuleSet) prj;
			MavenModuleSetBuild mmsb = (MavenModuleSetBuild) build;

			MavenInfoJobConfig cfg = mms.getProperty(MavenInfoJobConfig.class);
			if (cfg != null) {
				return new MavenInfoEnvironment(cfg);
			}
		}
		return super.setUpEnvironment(build, launcher, listener);
	}
}
