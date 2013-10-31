package jenkins.plugins.maveninfo.columns;

import hudson.views.ListViewColumn;

/**
 * Base column
 * 
 * @author emenaceb
 * 
 */
public abstract class AbstractMavenInfoColumn extends ListViewColumn {

	private String columnName;

	public AbstractMavenInfoColumn() {
		super();
	}

	public AbstractMavenInfoColumn(String columnName) {
		super();
		this.columnName = columnName;
	}

	@Override
	public String getColumnCaption() {
		return columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
