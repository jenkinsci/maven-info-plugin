package jenkins.plugins.maveninfo.extractor.properties;

import hudson.FilePath;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;

import java.io.IOException;
import java.io.InputStream;

import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;
import jenkins.plugins.maveninfo.patches.Digester3;
import jenkins.plugins.maveninfo.util.BuildUtils;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.xml.sax.SAXException;

/**
 * Extracts properties from main pom.
 * 
 * @author emenaceb
 * 
 */
public class PomPropertiesFinder implements PropertiesFinder {

	public void findProperties(ExtractorContext ctx) throws IOException,
			InterruptedException {

		MavenModuleSet mms = ctx.getMms();
		MavenModule main = BuildUtils.getMainModule(mms);
		MavenBuild build = mms.getLastBuild().getModuleLastBuilds().get(main);
		FilePath p = build.getWorkspace().child("pom.xml");
		Digester digester = new Digester3();
		digester.setRules(new ExtendedBaseRules());

		ctx.getRuleSet().addRuleInstances(digester);

		InputStream is = p.read();
		try {
			digester.parse(is);
		} catch (SAXException ex) {
			throw new IOException("Can't read POM");
		} finally {
			is.close();
		}
	}
}
