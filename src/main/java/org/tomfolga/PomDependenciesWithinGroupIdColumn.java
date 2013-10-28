package org.tomfolga;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import jenkins.plugins.maveninfo.l10n.Messages;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

public class PomDependenciesWithinGroupIdColumn extends
		AbstractPomDependenciesColumn {

	public PomDependenciesWithinGroupIdColumn(String columnName) {
		super(columnName, true);
	}

	public Descriptor<ListViewColumn> getDescriptor() {
		return DESCRIPTOR;
	}

	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public ListViewColumn newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {
			return new PomDependenciesWithinGroupIdColumn(Messages.PomDependenciesWithinGroupIdColumn_Caption());
		}

		@Override
		public String getDisplayName() {
			return Messages.PomDependenciesWithinGroupIdColumn_DisplayName();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}

	}
}
