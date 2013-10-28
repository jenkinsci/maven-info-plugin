package org.tomfolga;

import jenkins.plugins.maveninfo.l10n.Messages;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import net.sf.json.JSONObject;

import org.jfree.data.statistics.MeanAndStandardDeviation;
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
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public ListViewColumn newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {
			return new PomDependenciesColumn(Messages.PomDependenciesColumn_Caption());
		}

		@Override
		public String getDisplayName() {
			return Messages.PomDependenciesColumn_DisplayName();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}

	}

}
