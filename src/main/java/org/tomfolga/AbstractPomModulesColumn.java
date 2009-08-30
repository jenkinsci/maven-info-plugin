package org.tomfolga;

import hudson.maven.MavenModule;
import hudson.views.ListViewColumn;

/**
 * 
 * This class defines a column that shows Maven POM Modules groupId, artifactId
 * and version.
 * 
 * @author tomfolga
 * 
 */
public abstract class AbstractPomModulesColumn extends ListViewColumn {

	private String columnName;

	private boolean showGroupId;

	private boolean showArtifactId;

	public boolean isShowGroupId() {
		return showGroupId;
	}

	public boolean isShowArtifactId() {
		return showArtifactId;
	}

	public boolean isShowVersion() {
		return showVersion;
	}

	private boolean showVersion;

	public AbstractPomModulesColumn(String columnName, boolean showGroupId,
			boolean showArtifactId, boolean showVersion) {
		this.columnName = columnName;
		this.showGroupId = showGroupId;
		this.showArtifactId = showArtifactId;
		this.showVersion = showVersion;
	}

	@Override
	public String getColumnCaption() {
		return columnName;
	}

	public boolean isMavenProject(Object module) {
		return module instanceof MavenModule;
	}
}
