package jenkins.plugins.maveninfo.columns;

import hudson.Extension;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import jenkins.plugins.maveninfo.l10n.Messages;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;



public class MavenVersionColumn extends ListViewColumn {
	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public String getDisplayName() {
			return Messages.MavenVersionColumn_Description();
		}

		@Override
		public ListViewColumn newInstance(StaplerRequest arg0, JSONObject arg1) throws hudson.model.Descriptor.FormException {
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

	@Override
	public String getColumnCaption() {
		return Messages.MavenVersionColumn_Description();
	}

}
