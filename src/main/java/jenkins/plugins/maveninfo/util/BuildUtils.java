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

	public static MavenModule getMainModule(MavenModuleSet job) {

		ModuleNamePattern pattern = null;
		MavenInfoJobConfig cfg = job.getProperty(MavenInfoJobConfig.class);
		if (cfg != null) {
			pattern = cfg.getCompiledMainModulePattern();
		}

		MavenModule root = null;
		if (pattern != null) {
			List<MavenModule> modules = BuildUtils.getModules(job);
			Collections.sort(modules, new MavenModuleComparator());
			for (MavenModule module : modules) {
				if (pattern.matches(module.getModuleName())) {
					root = module;
					break;
				}
			}
		}
		if (root == null) {
			MavenModuleSetBuild b = job.getLastBuild();
			MavenModuleSet m = b.getParent();
			root = m.getRootModule();
		}
		return root;

	}

	public static List<MavenModule> getModules(MavenModuleSet job) {

		Set<MavenModule> moduleSet = job.getLastBuild().getModuleLastBuilds()
				.keySet();
		List<MavenModule> modules = new ArrayList<MavenModule>(moduleSet);

		return modules;
	}

}
