package jenkins.plugins.maveninfo.columns;

import hudson.Extension;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;

import java.util.Collections;
import java.util.List;

import jenkins.model.Jenkins;
import jenkins.plugins.maveninfo.l10n.Messages;
import jenkins.plugins.maveninfo.util.BuildUtils;
import jenkins.plugins.maveninfo.util.MavenModuleComparator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/**
 * Prints version of the last build of a Maven Job.
 * 
 * @author emenaceb
 * 
 */
public class MavenVersionColumn extends ListViewColumn {

	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public String getDisplayName() {
			return Messages.MavenVersionColumn_DisplayName();
		}

		@Override
		public ListViewColumn newInstance(StaplerRequest req, JSONObject obj)
				throws hudson.model.Descriptor.FormException {
			return new MavenVersionColumn();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}
	}

	@DataBoundConstructor
	public MavenVersionColumn() {
		super();
	}

	@JavaScriptMethod
	public JSONObject getAjaxModuleList(String jobId) {
		List<MavenModuleSet> list = Jenkins.getInstance().getAllItems(
				MavenModuleSet.class);

		for (MavenModuleSet mms : list) {
			if (mms.getName().equals(jobId)) {
				return getModuleList(mms);
			}
		}
		return new JSONObject();
	}

	@Override
	public String getColumnCaption() {
		return Messages.MavenVersionColumn_Caption();
	}

	private JSONObject getModuleAsJson(MavenModule module) {
		JSONObject json = new JSONObject();
		json.accumulate("groupId", module.getModuleName().groupId);
		json.accumulate("artifactId", module.getModuleName().artifactId);
		json.accumulate("version", module.getVersion());
		return json;
	}

	private JSONObject getModuleList(MavenModuleSet job) {

		MavenModule mainModule = BuildUtils.getMainModule(job);
		List<MavenModule> modules = BuildUtils.getModules(job);

		Collections.sort(modules, new MavenModuleComparator());
		JSONObject json = new JSONObject();
		json.accumulate("mainModule", getModuleAsJson(mainModule));
		JSONArray jsonModules = new JSONArray();
		for (MavenModule module : modules) {
			jsonModules.add(getModuleAsJson(module));
		}
		json.accumulate("modules", jsonModules);
		return json;
	}

	public String getVersion(MavenModuleSet job) {
		MavenModule m = BuildUtils.getMainModule(job);
		return m.getVersion();
	}

	public boolean isMultipleVersions(MavenModuleSet job) {
		List<MavenModule> modules = BuildUtils.getModules(job);
		if (modules.size() <= 1) {
			return false;
		}
		String version = null;
		for (MavenModule module : modules) {
			if (!module.getVersion().equals(version)) {
				if (version == null) {
					version = module.getVersion();
				} else {
					return true;
				}
			}
		}
		return false;
	}

}
