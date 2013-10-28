package jenkins.plugins.maveninfo.columns;

import hudson.Extension;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.l10n.Messages;
import jenkins.plugins.maveninfo.util.BuildUtils;
import jenkins.plugins.maveninfo.util.Dependency;
import jenkins.plugins.maveninfo.util.ModuleNamePattern;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Prints version selected dependencies of the last build of a Maven Job.
 * 
 * @author emenaceb
 * 
 */
public class DependenciesVersionColumn extends ListViewColumn {

	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public String getDisplayName() {
			return Messages.DependenciesVersionColumn_DisplayName();
		}

		@Override
		public ListViewColumn newInstance(StaplerRequest req, JSONObject obj)
				throws hudson.model.Descriptor.FormException {
			return new DependenciesVersionColumn();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}
	}

	@DataBoundConstructor
	public DependenciesVersionColumn() {
		super();
	}

	protected MavenModuleSetBuild getBuild(MavenModuleSet job) {
		return BuildUtils.getLastBuild(job);
	}

	@Override
	public String getColumnCaption() {
		return Messages.DependenciesVersionColumn_Caption();
	}

	protected ModuleNamePattern getDependencyFilter(MavenModuleSet job) {
		MavenInfoJobConfig cfg = BuildUtils.getJobConfig(job);
		return cfg.getCompiledDependenciesPattern();
	}

	public String getVersion(MavenModuleSet job) {
		SortedSet<String> versions = getVersions(job);
		String version = versions.first();
		if (version == null) {
			version = "";
		}
		return version;
	}

	private SortedSet<String> getVersions(MavenModuleSet job) {
		MavenModuleSetBuild build = getBuild(job);
		ModuleNamePattern pattern = getDependencyFilter(job);

		List<Dependency> dependencies = BuildUtils.getModuleDependencies(build,
				pattern);
		SortedSet<String> versions = new TreeSet<String>();
		for (Dependency d : dependencies) {
			versions.addAll(d.getVersions());
		}
		return versions;
	}

	public boolean isMultipleVersions(MavenModuleSet job) {
		SortedSet<String> versions = getVersions(job);
		return versions.size() > 1;
	}
}
