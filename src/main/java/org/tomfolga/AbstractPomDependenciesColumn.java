package org.tomfolga;

import hudson.maven.MavenModule;
import hudson.maven.ModuleDependency;
import hudson.views.ListViewColumn;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 
 * This class defines a column that shows Maven POM dependencies of each Maven
 * Module in the last build.
 * 
 * @author tomfolga
 * 
 */
public abstract class AbstractPomDependenciesColumn extends ListViewColumn {

	private boolean onlyWithinGroupId;

	public boolean isOnlyWithinGroupId() {
		return onlyWithinGroupId;
	}

	private String columnName;

	public AbstractPomDependenciesColumn(String columnName,
			boolean onlyWithinGroupId) {
		this.columnName = columnName;
		this.onlyWithinGroupId = onlyWithinGroupId;
	}

	private static final Logger LOGGER = Logger
			.getLogger(AbstractPomDependenciesColumn.class.getName());

	@Override
	public String getColumnCaption() {
		return columnName;
	}

	public boolean isMavenProject(Object module) {
		return module instanceof MavenModule;
	}

	/*
	 * This method is a bit of a hack. We try read private field dependencies in
	 * MavenModule. Hopefully, in the next version of hudson there will be a
	 * getter for that field.
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleDependency> getDependencies(Object module) {
		List<ModuleDependency> dependencyStrings = new LinkedList<ModuleDependency>();
		if (isMavenProject(module)) {
			MavenModule mavenModule = (MavenModule) module;
			Field privateStringField;
			try {
				privateStringField = MavenModule.class
						.getDeclaredField("dependencies");
				privateStringField.setAccessible(true);

				Set<ModuleDependency> fieldValue;
				fieldValue = (Set<ModuleDependency>) privateStringField
						.get(mavenModule);
				for (ModuleDependency moduleDependency : fieldValue) {
					if (onlyWithinGroupId) {
						if (mavenModule.getModuleName().groupId
								.equals(moduleDependency.getName().groupId)) {
							dependencyStrings.add(moduleDependency);
						}
					} else {
						dependencyStrings.add(moduleDependency);
					}
				}
			} catch (Exception e) {
				LOGGER.severe(e.getMessage());
			}
		}
		return dependencyStrings;
	}

}
