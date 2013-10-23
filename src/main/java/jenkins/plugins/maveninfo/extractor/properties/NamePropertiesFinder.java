package jenkins.plugins.maveninfo.extractor.properties;

import hudson.maven.MavenModule;
import jenkins.plugins.maveninfo.extractor.MavenProperties;
import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;
import jenkins.plugins.maveninfo.util.BuildUtils;

/**
 * Extract Maven project name from build.
 * 
 * @author emenaceb
 * 
 */
public class NamePropertiesFinder implements PropertiesFinder {

	public void findProperties(ExtractorContext ctx) {
		if (ctx.getConfig().isAssignName()) {
			MavenModule main = BuildUtils.getMainModule(ctx.getMms());
			if (main != null) {
				String projectName = main.getDisplayName();
				ctx.setPropety(MavenProperties.PROP_MAVEN_NAME, projectName);
			}
		}

	}
}
