package jenkins.plugins.maveninfo.extractor;

import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;
import jenkins.plugins.maveninfo.extractor.base.PropertiesSaver;
import jenkins.plugins.maveninfo.extractor.properties.DescriptionPropertiesFinder;
import jenkins.plugins.maveninfo.extractor.properties.JobUpdaterPropertiesySaver;
import jenkins.plugins.maveninfo.extractor.properties.NamePropertiesFinder;
import jenkins.plugins.maveninfo.extractor.properties.PomPropertiesFinder;
import org.xml.sax.SAXException;

/**
 * Extract Maven information from the build.
 * 
 * 
 * Assigns Job's visible name from Maven pom .<br>
 * Assigns Job's description from Maven pom.<br>
 * 
 * @author emenaceb
 * 
 */
public class MavenInfoExtractor {

	private MavenInfoJobConfig config;

	private static List<PropertiesFinder> finders = new ArrayList<PropertiesFinder>();

	private static List<PropertiesSaver> savers = new ArrayList<PropertiesSaver>();

	static {

		finders.add(new NamePropertiesFinder());
		finders.add(new DescriptionPropertiesFinder());
		finders.add(new PomPropertiesFinder());

		savers.add(new JobUpdaterPropertiesySaver());
	}

	public MavenInfoExtractor(MavenInfoJobConfig config) {
		super();
		this.config = config;

	}

	public void extract(MavenModuleSet mms, MavenModuleSetBuild mmsb)
			throws IOException, InterruptedException {
		ExtractorContext ctx = new ExtractorContext(config, mms, mmsb);

		for (PropertiesFinder finder : finders) {
			finder.findProperties(ctx);
		}

		Properties properties = ctx.getProperties();
		for (String property : MavenProperties.PROPERTIES) {
			String prop = properties.getProperty(property);
			if (prop == null) {
				properties.setProperty(property, "");
			}
		}

		for (PropertiesSaver saver : savers) {
			saver.saveProperties(ctx);
		}
	}

}
