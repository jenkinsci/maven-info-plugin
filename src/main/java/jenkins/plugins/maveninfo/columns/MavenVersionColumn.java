package jenkins.plugins.maveninfo.columns;

import hudson.Extension;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import jenkins.plugins.maveninfo.l10n.Messages;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

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

	@Override
	public String getColumnCaption() {
		return Messages.MavenVersionColumn_DisplayName();
	}

}
