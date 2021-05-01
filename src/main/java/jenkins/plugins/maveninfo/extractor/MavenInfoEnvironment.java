package jenkins.plugins.maveninfo.extractor;

import hudson.maven.AbstractMavenProject;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.model.AbstractBuild;

import java.io.IOException;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import org.xml.sax.SAXException;

/**
 * Environment that invokes {@link MavenInfoExtractor}.
 * 
 * @author emenaceb
 * 
 */
public class MavenInfoEnvironment extends Environment {

	public MavenInfoJobConfig config;

	public MavenInfoEnvironment(MavenInfoJobConfig config) {
		super();
		this.config = config;
	}

	@SuppressWarnings("rawtypes")
	public boolean tearDown(AbstractBuild build, final BuildListener listener)
			throws IOException, InterruptedException {

		AbstractMavenProject<?, ?> prj = ((AbstractMavenProject<?, ?>) build
				.getProject());
		if (build instanceof MavenModuleSetBuild
				&& prj instanceof MavenModuleSet) {

			MavenModuleSet mms = (MavenModuleSet) prj;
			MavenModuleSetBuild mmsb = (MavenModuleSetBuild) build;

			MavenInfoExtractor extractor = new MavenInfoExtractor(config);
			extractor.extract(mms, mmsb);

		}

		return true;
	}

}
