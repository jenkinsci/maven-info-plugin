package org.tomfolga;

import jenkins.plugins.maveninfo.l10n.Messages;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

/**
 * 
 * This class defines a column that shows Maven POM Modules groupId, artifactId
 * and version.
 * 
 * This class has multiple descriptors which instantiate this class with
 * different params. I am not sure if it's recommended way in Hudson, but it
 * allows to create multiple column definition using one class/jelly file. TODO:
 * check if/how columns can be configured to avoid multiple columns
 * 
 * @author tomfolga
 * 
 */
public class PomModulesShortColumn extends AbstractPomModulesColumn {

	public Descriptor<ListViewColumn> getDescriptor() {
		return DESCRIPTOR;
	}

	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

	public PomModulesShortColumn(String columnName) {
		super(columnName, false, true, true);
	}

	@Extension
	public static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public ListViewColumn newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {

			return new PomModulesShortColumn(Messages.PomModulesShortColumn_Caption());
		}

		@Override
		public String getDisplayName() {
			return Messages.PomModulesShortColumn_DisplayName();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}
	}
}
