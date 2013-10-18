package jenkins.plugins.maveninfo.config;

import hudson.Extension;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.Job;
import jenkins.plugins.maveninfo.l10n.Messages;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Maven info extractor configuration.
 * 
 * @author emenaceb
 * 
 */
public class MavenInfoJobConfig extends JobProperty<Job<?, ?>> {

	@Extension(ordinal = 100)
	public static class JobConfigDescriptor extends JobPropertyDescriptor {

		@Override
		public String getDisplayName() {
			return Messages.MavenInfoJobConfig_DisplayName();

		}

		@Override
		public boolean isApplicable(Class<? extends Job> prj) {
			return MavenModule.class.isAssignableFrom(prj)
					|| MavenModuleSet.class.isAssignableFrom(prj);
		}

	}

	private String mainModulePattern;

	private boolean assignName;

	private String namePattern;

	private boolean assignDescription;

	private String descriptionPattern;

	public MavenInfoJobConfig() {
		this("", true, "", true, "");
	}

	@DataBoundConstructor
	public MavenInfoJobConfig(String mainModulePattern, boolean assignName,
			String namePattern, boolean assignDescription,
			String descriptionPattern) {
		super();
		this.mainModulePattern = mainModulePattern;
		this.assignName = assignName;
		this.namePattern = namePattern;
		this.assignDescription = assignDescription;
		this.descriptionPattern = descriptionPattern;
	}

	public String getDescriptionPattern() {
		return descriptionPattern;
	}

	public String getMainModulePattern() {
		return mainModulePattern;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public boolean isAssignDescription() {
		return assignDescription;
	}

	public boolean isAssignName() {
		return assignName;
	}

	public void setAssignDescription(boolean assignDescription) {
		this.assignDescription = assignDescription;
	}

	public void setAssignName(boolean assignName) {
		this.assignName = assignName;
	}

	public void setDescriptionPattern(String descriptionPattern) {
		this.descriptionPattern = descriptionPattern;
	}

	public void setMainModulePattern(String versionPattern) {
		this.mainModulePattern = versionPattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

}
