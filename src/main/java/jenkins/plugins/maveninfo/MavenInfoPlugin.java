package jenkins.plugins.maveninfo;

import hudson.Extension;
import hudson.Plugin;
import jenkins.model.Jenkins;
import jenkins.plugins.maveninfo.config.MavenInfoGlobalConfig;

@Extension
public class MavenInfoPlugin extends Plugin {

	private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

	public MavenInfoGlobalConfig getCfg() {
		return (MavenInfoGlobalConfig) Jenkins.getInstance().getDescriptor(
				MavenInfoGlobalConfig.class);
	}

	public String getEffectiveSnapshotsStyle() {
		return getCfg().getEffectiveSnapshotsStyle();
	}

	public String getReleasesStyle() {
		return getCfg().getReleasesStyle();
	}

	public String getSnapshotsStyle() {
		return getCfg().getSnapshotsStyle();
	}

	public boolean isHideSnapshots() {
		return getCfg().isHideSnapshots();
	}

	public boolean isSnapshot(String version) {
		return version != null && version.endsWith(SNAPSHOT_SUFFIX);
	}

	public String visibleVersion(String version) {
		if (isHideSnapshots()) {
			if (version != null && version.endsWith(SNAPSHOT_SUFFIX)) {
				return version.substring(0,
						version.length() - SNAPSHOT_SUFFIX.length());
			}
		}
		return version;
	}
}
