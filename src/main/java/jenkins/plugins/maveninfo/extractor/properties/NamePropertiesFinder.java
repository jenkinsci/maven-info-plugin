package jenkins.plugins.maveninfo.extractor.properties;

import hudson.maven.MavenModule;
import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.extractor.MavenProperties;
import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;
import jenkins.plugins.maveninfo.util.BuildUtils;
import jenkins.plugins.maveninfo.util.ModuleNamePattern;

/**
 * Extract Maven project name from build.
 * 
 * @author emenaceb
 * 
 */
public class NamePropertiesFinder implements PropertiesFinder {

	public void findProperties(ExtractorContext ctx) {
		MavenInfoJobConfig cfg = ctx.getConfig();
		if (cfg.isAssignName()) {
			ModuleNamePattern mainPattern = cfg.getCompiledMainModulePattern();
			MavenModule main = BuildUtils.getMainModule(ctx.getMmsb(), mainPattern);
			if (main != null) {
				String projectName = main.getDisplayName();
				ctx.setPropety(MavenProperties.PROP_MAVEN_NAME, projectName);
			}
		}

	}
}
