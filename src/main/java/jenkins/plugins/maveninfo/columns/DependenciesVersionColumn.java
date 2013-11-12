package jenkins.plugins.maveninfo.columns;

import hudson.Extension;
import hudson.maven.ModuleName;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jenkins.model.Jenkins;
import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.l10n.Messages;
import jenkins.plugins.maveninfo.util.BuildUtils;
import jenkins.plugins.maveninfo.util.Dependency;
import jenkins.plugins.maveninfo.util.ModuleNamePattern;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/**
 * Prints version selected dependencies of the last build of a Maven Job.
 * 
 * @author emenaceb
 * 
 */
public class DependenciesVersionColumn extends AbstractMavenInfoColumn {

	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public String getDisplayName() {
			return Messages.DependenciesVersionColumn_DisplayName();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}
	}

	@DataBoundConstructor
	public DependenciesVersionColumn(String columnName) {
		super(columnName);
	}

	private String findVersion(SortedSet<String> versions) {

		String version = "";
		if (!versions.isEmpty()) {
			String first = versions.first();
			if (first != null) {
				version = first;
			}
		}
		return version;
	}

	private SortedSet<String> findVersions(List<Dependency> dependencies) {
		SortedSet<String> versions = new TreeSet<String>();
		for (Dependency d : dependencies) {
			versions.addAll(d.getVersions());
		}
		return versions;
	}

	@JavaScriptMethod
	public JSONObject getAjaxDependenciesList(String jobId) {
		List<MavenModuleSet> list = Jenkins.getInstance().getAllItems(
				MavenModuleSet.class);

		for (MavenModuleSet mms : list) {
			if (mms.getName().equals(jobId)) {
				return getDependenciesList(mms);
			}
		}
		return new JSONObject();
	}

	protected MavenModuleSetBuild getBuild(MavenModuleSet job) {
		return BuildUtils.getLastBuild(job);
	}

	private JSONObject getDependenciesList(MavenModuleSet job) {
		MavenModuleSetBuild build = getBuild(job);
		ModuleNamePattern pattern = getDependencyFilter(job);

		List<Dependency> dependencies = BuildUtils.getModuleDependencies(build,
				pattern);

		String version = findVersion(findVersions(dependencies));
		JSONObject response = new JSONObject();
		response.accumulate("version", version);

		JSONArray deps = new JSONArray();
		for (Dependency dependency : dependencies) {
			JSONObject dep = new JSONObject();
			ModuleName name = dependency.getName();
			dep.accumulate("groupId", name.groupId);
			dep.accumulate("artifactId", name.artifactId);
			JSONArray depVersions = new JSONArray();
			for (String depVersion : dependency.getVersions()) {
				depVersions.add(depVersion);
			}
			dep.accumulate("versions", depVersions);
			deps.add(dep);
		}
		response.accumulate("dependencies", deps);

		return response;
	}

	protected ModuleNamePattern getDependencyFilter(MavenModuleSet job) {
		MavenInfoJobConfig cfg = BuildUtils.getJobConfig(job);
		return cfg.getCompiledDependenciesPattern();
	}

	public String getVersion(MavenModuleSet job) {
		SortedSet<String> versions = getVersions(job);
		return findVersion(versions);
	}

	private SortedSet<String> getVersions(MavenModuleSet job) {
		MavenModuleSetBuild build = getBuild(job);
		ModuleNamePattern pattern = getDependencyFilter(job);

		List<Dependency> dependencies = BuildUtils.getModuleDependencies(build,
				pattern);

		return findVersions(dependencies);
	}

	public boolean isMultipleVersions(MavenModuleSet job) {
		SortedSet<String> versions = getVersions(job);
		return versions.size() > 1;
	}
}
