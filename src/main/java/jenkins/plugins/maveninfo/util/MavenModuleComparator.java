package jenkins.plugins.maveninfo.util;

import hudson.maven.MavenModule;

import java.util.Comparator;

public class MavenModuleComparator implements Comparator<MavenModule> {

	public int compare(MavenModule o1, MavenModule o2) {

		return o1.getModuleName().compareTo(o2.getModuleName());
	}
}