package jenkins.plugins.maveninfo.util;

import hudson.maven.ModuleName;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This represents a Maven dependency.
 * 
 * @author emenaceb
 * 
 */
public class Dependency implements Comparable<Dependency> {

	private ModuleName name;

	private SortedSet<String> versions = new TreeSet<String>();

	public Dependency(ModuleName name) {
		super();
		this.name = name;
	}

	public void addVersion(String version) {
		versions.add(version);
	}

	public int compareTo(Dependency o) {
		return name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dependency other = (Dependency) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getFirstVersion() {
		return versions.first();
	}

	public ModuleName getName() {
		return name;
	}

	public SortedSet<String> getVersions() {
		return Collections.unmodifiableSortedSet(versions);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
}
