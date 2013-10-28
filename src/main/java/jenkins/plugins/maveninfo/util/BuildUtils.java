package jenkins.plugins.maveninfo.util;

import hudson.maven.ModuleDependency;
import hudson.maven.ModuleName;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;

/**
 * Build utilities.
 * 
 * @author emenaceb
 * 
 */
public class BuildUtils {

	private static final Logger LOGGER = Logger.getLogger(BuildUtils.class
			.getName());

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

	@SuppressWarnings("unchecked")
	private static void getModuleDependencies(
			Map<ModuleName, Dependency> dependencies, MavenModule module,
			ModuleNamePattern pattern) {
		if (pattern == null) {
			return;
		}
		MavenModule mavenModule = (MavenModule) module;
		Field field;
		try {
			// Hack to obtain dependencies (thanks to tomfolga)
			field = MavenModule.class.getDeclaredField("dependencies");
			field.setAccessible(true);
			Set<ModuleDependency> fieldValue = (Set<ModuleDependency>) field
					.get(mavenModule);

			for (ModuleDependency moduleDependency : fieldValue) {
				// skip displaying maven plugin dependencies

				if (moduleDependency.plugin)
					continue;

				ModuleName name = moduleDependency.getName();
				if (pattern.matches(name)) {

					Dependency d = dependencies.get(name);
					if (d == null) {
						d = new Dependency(name);
						dependencies.put(name, d);
					}
					d.addVersion(moduleDependency.version);
				}
			}
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
		}
	}

	public static List<Dependency> getModuleDependencies(
			MavenModuleSetBuild build, ModuleNamePattern pattern) {

		TreeMap<ModuleName, Dependency> dependencies = new TreeMap<ModuleName, Dependency>();
		List<MavenModule> modules = getModules(build);
		for (MavenModule module : modules) {
			getModuleDependencies(dependencies, module, pattern);
		}

		List<Dependency> dependencyList = new ArrayList<Dependency>(
				dependencies.values());
		Collections.sort(dependencyList);
		return dependencyList;
	}

	public static List<MavenModule> getModules(MavenModuleSetBuild build) {

		Set<MavenModule> moduleSet = build.getModuleLastBuilds().keySet();
		List<MavenModule> modules = new ArrayList<MavenModule>(moduleSet);

		return modules;
	}
}
