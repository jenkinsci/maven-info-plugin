package jenkins.plugins.maveninfo.extractor.properties;

import jenkins.plugins.maveninfo.extractor.MavenProperties;
import jenkins.plugins.maveninfo.extractor.base.ExtractPropertyRule;
import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;

import org.apache.commons.digester3.Rule;

/**
 * Configures pom parser to extract description.
 * 
 * @author emenaceb
 * 
 */
public class DescriptionPropertiesFinder implements PropertiesFinder {

	public void findProperties(ExtractorContext ctx) {
		if (ctx.getConfig().isAssignDescription()) {
			Rule rule = new ExtractPropertyRule(
					MavenProperties.PROP_MAVEN_DESCRIPTION, ctx.getProperties());
			ctx.addRule("project/description", rule);
		}

	}

}
