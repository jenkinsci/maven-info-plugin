package jenkins.plugins.maveninfo.columns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import hudson.Extension;
import hudson.Util;
import hudson.maven.AbstractMavenProject;
import hudson.maven.ModuleName;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.Job;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import jenkins.model.Jenkins;
import jenkins.plugins.maveninfo.l10n.Messages;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.maven.project.MavenProject;
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

	private static class MavenModuleComparator implements
			Comparator<MavenModule> {

		public int compare(MavenModule o1, MavenModule o2) {

			return o1.getModuleName().compareTo(o2.getModuleName());
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
		return Messages.MavenVersionColumn_DisplayName();
	}

	private MavenModule getMainModule(MavenModuleSet job) {
		MavenModuleSetBuild b = job.getLastBuild();
		MavenModuleSet m = b.getParent();
		MavenModule root = m.getRootModule();
		return root;

	}

	private JSONObject getModuleAsJson(MavenModule module) {
		JSONObject json = new JSONObject();
		json.accumulate("groupId", module.getModuleName().groupId);
		json.accumulate("artifactId", module.getModuleName().artifactId);
		json.accumulate("version", module.getVersion());
		return json;
	}

	private JSONObject getModuleList(MavenModuleSet job) {

		MavenModule mainModule = getMainModule(job);
		List<MavenModule> modules = getModules(job);

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

	private List<MavenModule> getModules(MavenModuleSet job) {

		Set<MavenModule> moduleSet = job.getLastBuild().getModuleLastBuilds()
				.keySet();
		List<MavenModule> modules = new ArrayList<MavenModule>(moduleSet);

		return modules;
	}

	public String getVersion(MavenModuleSet job) {
		MavenModule m = getMainModule(job);
		return m.getVersion();
	}

}
