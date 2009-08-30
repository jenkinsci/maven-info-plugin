package org.tomfolga;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ListViewColumn;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

public class PomDependenciesColumn extends AbstractPomDependenciesColumn {

	public PomDependenciesColumn(String columnName) {
		super(columnName, false);
	}

	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

	public Descriptor<ListViewColumn> getDescriptor() {
		return DESCRIPTOR;
	}

	@Extension
	public static class DescriptorImpl extends Descriptor<ListViewColumn> {
		@Override
		public ListViewColumn newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {
			return new PomDependenciesColumn(getDisplayName());
		}

		@Override
		public String getDisplayName() {
			return "Maven Dependencies";
		}

	}

}
