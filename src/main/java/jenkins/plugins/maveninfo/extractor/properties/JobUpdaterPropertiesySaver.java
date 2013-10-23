package jenkins.plugins.maveninfo.extractor.properties;

import hudson.maven.MavenModuleSet;

import java.io.IOException;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.extractor.MavenProperties;
import jenkins.plugins.maveninfo.extractor.base.AbstractPropertiesSaver;
import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;

/**
 * Updates Job configuration.
 * 
 * @author emenaceb
 * 
 */
public class JobUpdaterPropertiesySaver extends AbstractPropertiesSaver {

	public void saveProperties(ExtractorContext context) throws IOException,
			InterruptedException {

		MavenInfoJobConfig config = context.getConfig();
		MavenModuleSet mms = context.getMms();

		if (config.isAssignName()) {

			String value = mms.getDisplayNameOrNull();
			String newValue = expandProperty(config.getNameTemplate(),
					MavenProperties.PROP_MAVEN_NAME, context);
			if (value == null || !newValue.equals(value)) {
				mms.setDisplayNameOrNull(newValue);

			}
		}

		if (config.isAssignDescription()) {

			String value = mms.getDescription();
			String newValue = expandPropertyPre(config.getDescriptionTemplate(),
					MavenProperties.PROP_MAVEN_DESCRIPTION, context);
			if (value == null || !newValue.equals(value)) {
				mms.setDescription(newValue);

			}
		}
	}
}
