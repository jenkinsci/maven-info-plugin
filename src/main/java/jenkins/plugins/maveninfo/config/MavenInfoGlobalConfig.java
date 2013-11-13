package jenkins.plugins.maveninfo.config;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Maven info global configuration.
 * 
 * @author emenaceb
 * 
 */
@Extension(ordinal = 100)
public class MavenInfoGlobalConfig extends GlobalConfiguration {

	private static final String KEY_RELEASES_STYLE = "releasesStyle";

	private static final String KEY_SNAPSHOTS_STYLE = "snapshotsStyle";

	private static final String KEY_HIDE_SNAPSHOTS = "hideSnapshots";

	private static final String DEFAULT_SNAPSHOT_STYLE = "font-style: italic; color: #aaa; font-weight: bold;";

	private boolean hideSnapshots = false;

	private String snapshotsStyle = "";

	private String releasesStyle = "";

	public MavenInfoGlobalConfig() {
		load();
	}

	@Override
	public boolean configure(StaplerRequest req, JSONObject formData)
			throws FormException {

		if (formData.has(KEY_HIDE_SNAPSHOTS)) {
			this.hideSnapshots = formData.getBoolean(KEY_HIDE_SNAPSHOTS);
		} else {
			this.hideSnapshots = false;
		}

		if (formData.has(KEY_SNAPSHOTS_STYLE)) {
			this.snapshotsStyle = formData.getString(KEY_SNAPSHOTS_STYLE);
		} else {
			this.snapshotsStyle = "";
		}

		if (formData.has(KEY_RELEASES_STYLE)) {
			this.releasesStyle = formData.getString(KEY_RELEASES_STYLE);
		} else {
			this.releasesStyle = "";
		}

		save();
		return true;
	}

	public FormValidation doCheckReleasesStyle(@QueryParameter String value) {
		return doCheckStyles(value);
	}

	public FormValidation doCheckSnapshotsStyle(@QueryParameter String value) {
		return doCheckStyles(value);
	}

	private FormValidation doCheckStyles(String parameter) {
		if (parameter.matches(".*[{}<>].*")) {
			return FormValidation
					.error("What exactly are you trying to do? Only css rules content allowed. No tags nor css rules allowed.");
		}
		return FormValidation.ok();
	}

	public String getEffectiveSnapshotsStyle() {
		if (StringUtils.isBlank(snapshotsStyle)) {
			if (hideSnapshots) {
				return DEFAULT_SNAPSHOT_STYLE;
			}
		}
		return snapshotsStyle;
	}

	public String getReleasesStyle() {
		return releasesStyle;
	}

	public String getSnapshotsStyle() {
		return snapshotsStyle;
	}

	public boolean isHideSnapshots() {
		return hideSnapshots;
	}

}
