package jenkins.plugins.maveninfo.extractor.properties;

import hudson.FilePath;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import jenkins.plugins.maveninfo.extractor.base.ExtractorContext;
import jenkins.plugins.maveninfo.extractor.base.PropertiesFinder;
import jenkins.plugins.maveninfo.patches.Digester3;
import jenkins.plugins.maveninfo.util.BuildUtils;
import jenkins.plugins.maveninfo.util.ModuleNamePattern;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Extracts properties from main pom.
 * 
 * @author emenaceb
 * 
 */
public class PomPropertiesFinder implements PropertiesFinder {

	public void findProperties(ExtractorContext ctx) throws IOException,
			InterruptedException {

		MavenModuleSetBuild mmsb = ctx.getMmsb();
		ModuleNamePattern mainPattern = ctx.getConfig()
				.getCompiledMainModulePattern();

		MavenModule main = BuildUtils.getMainModule(mmsb, mainPattern);
		if (main != null) {

			MavenBuild build = mmsb.getModuleLastBuilds().get(main);
			if (build != null) {
				FilePath p;
				FilePath workspace = build.getWorkspace();
				if (workspace != null) {
					p = workspace.child("pom.xml");
				} else {
					throw new RuntimeException("Remote node has been disconnected");
				}
				if (p != null) {

					Digester digester = new Digester3();
					digester.setRules(new ExtendedBaseRules());

					ctx.getRuleSet().addRuleInstances(digester);

					InputStream is = p.read();
					try {
						digester.parse(is);
					} catch (SAXException ex) {
						throw new IOException("Can't read POM: " + p.toString());
					} finally {
						is.close();
					}
				}
			}
		}
	}
}
