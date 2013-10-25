package org.tomfolga;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
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
			return new PomDependenciesWithinGroupIdColumn(getDisplayName());
		}

		@Override
		public String getDisplayName() {
			return "Maven Dependencies (within groupId)";
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}

	}
}
