package jenkins.plugins.maveninfo;

import hudson.Extension;
import hudson.Plugin;
import hudson.model.Descriptor.FormException;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@Extension
public class MavenInfoPlugin extends Plugin {

	private static final String KEY_HIDE_SNAPSHOTS = "hideSnapshots";

	private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

	private boolean hideSnapshots = false;

	@Override
	public void configure(StaplerRequest req, JSONObject formData)
			throws IOException, ServletException, FormException {

		if (formData.has(KEY_HIDE_SNAPSHOTS)) {
			this.hideSnapshots = formData.getBoolean(KEY_HIDE_SNAPSHOTS);
		} else {
			this.hideSnapshots = false;
		}

	}

	public boolean isHideSnapshots() {
		return hideSnapshots;
	}

	public boolean isSnapshot(String version) {
		return version != null && version.endsWith(SNAPSHOT_SUFFIX);
	}

	public String visibleVersion(String version) {
		if (hideSnapshots) {
			if (version != null && version.endsWith(SNAPSHOT_SUFFIX)) {
				return version.substring(0,
						version.length() - SNAPSHOT_SUFFIX.length());
			}
		}
		return version;
	}
}
