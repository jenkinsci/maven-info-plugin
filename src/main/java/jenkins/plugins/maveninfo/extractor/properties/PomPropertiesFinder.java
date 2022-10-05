package jenkins.plugins.maveninfo.extractor.properties;

import hudson.FilePath;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;

import java.io.IOException;
import java.io.InputStream;

import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;
import jenkins.plugins.maveninfo.util.BuildUtils;
import jenkins.plugins.maveninfo.util.ModuleNamePattern;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ExtendedBaseRules;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Extracts properties from main pom.
 * 
 * @author emenaceb
 * 
 */
public class PomPropertiesFinder implements PropertiesFinder {

	public Digester createDigester(boolean secure) throws SAXException {
		final Digester digester = new Digester();
		if (secure) {
			digester.setXIncludeAware(false);
			try {
				digester.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				digester.setFeature("http://xml.org/sax/features/external-general-entities", false);
				digester.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				digester.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			} catch (ParserConfigurationException ex) {
				throw new SAXException("Failed to securely configure xml digester parser", ex);
			}
		}
		return digester;
	}

	public void findProperties(ExtractorContext ctx) throws IOException,
			InterruptedException {

		MavenModuleSetBuild mmsb = ctx.getMmsb();
		ModuleNamePattern mainPattern = ctx.getConfig()
				.getCompiledMainModulePattern();

		MavenModule main = BuildUtils.getMainModule(mmsb, mainPattern);
		if (main == null) {
			return;
		}

		MavenBuild build = mmsb.getModuleLastBuilds().get(main);
		if (build == null) {
			return;
		}

		FilePath workspace = build.getWorkspace();
		if (workspace == null) {
			return;
		}

		FilePath p = workspace.child("pom.xml");

		try (InputStream is = p.read()) {
			Digester digester = createDigester(!Boolean.getBoolean(this.getClass().getName() + ".UNSAFE"));
			digester.setRules(new ExtendedBaseRules());

			ctx.getRuleSet().addRuleInstances(digester);

			digester.parse(is);
		} catch (SAXException ex) {
			throw new IOException("Can't read POM: " + p);
		}
	}
}
