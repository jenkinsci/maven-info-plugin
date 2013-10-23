package jenkins.plugins.maveninfo.extractor.base;

import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import java.util.Properties;

import org.apache.commons.digester.Rule;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;

/**
 * Context class for info extraction.
 * 
 * @author emenaceb
 *
 */
public class ExtractorContext {

	private MavenInfoJobConfig config;

	private MavenModuleSet mms;

	private MavenModuleSetBuild mmsb;

	private Properties properties = new Properties();

	private ExtractorRuleSet ruleSet = new ExtractorRuleSet();

	public ExtractorContext(MavenInfoJobConfig config, MavenModuleSet mms,
			MavenModuleSetBuild mmsb) {
		super();
		this.config = config;
		this.mms = mms;
		this.mmsb = mmsb;
	}

	public void addRule(String pattern, Rule rule) {
		ruleSet.addRule(pattern, rule);
	}

	public MavenInfoJobConfig getConfig() {
		return config;
	}

	public MavenModuleSet getMms() {
		return mms;
	}

	public MavenModuleSetBuild getMmsb() {
		return mmsb;
	}

	public Properties getProperties() {
		return properties;
	}

	public ExtractorRuleSet getRuleSet() {
		return ruleSet;
	}

	public void setPropety(String name, String value) {
		properties.setProperty(name, value);
	}
}
