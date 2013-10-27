package jenkins.plugins.maveninfo.util;

import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;

/**
 * Build utilities.
 * 
 * @author emenaceb
 * 
 */
public class BuildUtils {


	public static MavenInfoJobConfig getJobConfig(MavenModuleSet job) {
		return job.getProperty(MavenInfoJobConfig.class);
	}

	public static MavenModuleSetBuild getLastBuild(MavenModuleSet job) {
		return job.getLastBuild();
	}

	public static MavenModule getMainModule(MavenModuleSetBuild build,
			ModuleNamePattern pattern) {

		MavenModule root = null;
		if (pattern != null) {
			List<MavenModule> modules = BuildUtils.getModules(build);
			Collections.sort(modules, new MavenModuleComparator());
			for (MavenModule module : modules) {
				if (pattern.matches(module.getModuleName())) {
					root = module;
					break;
				}
			}
		}
		if (root == null) {
			MavenModuleSet m = build.getParent();
			root = m.getRootModule();
		}
		return root;

	}

	public static List<MavenModule> getModules(MavenModuleSetBuild build) {

		Set<MavenModule> moduleSet = build.getModuleLastBuilds().keySet();
		List<MavenModule> modules = new ArrayList<MavenModule>(moduleSet);

		return modules;
	}

}
