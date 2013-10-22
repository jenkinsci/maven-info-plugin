package jenkins.plugins.maveninfo.extractor;

import hudson.FilePath;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.patches.Digester3;
import jenkins.plugins.maveninfo.util.BuildUtils;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
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

	private static final String PROP_MAVEN_DESCRIPTION = "MAVEN_DESCRIPTION";

	private static final String PROP_MAVEN_NAME = "MAVEN_NAME";

	private static final Pattern PATTERN_CLEAN = Pattern.compile(
			"\\s*([^\\r\\n]*)\\s*[\\n\\r]", Pattern.MULTILINE);

	private MavenInfoJobConfig config;

	public MavenInfoExtractor(MavenInfoJobConfig config) {
		super();
		this.config = config;
	}

	private void assignAll(MavenModuleSet mms, MavenModuleSetBuild mmsb,
			Properties properties) throws IOException, InterruptedException {

		if (config.isAssignName()) {
			assignName(mms, mmsb, properties);
		}

		if (config.isAssignDescription()) {
			assignDescription(mms, mmsb, properties);
		}
	}

	private void assignDescription(MavenModuleSet mms,
			MavenModuleSetBuild mmsb, Properties properties)
			throws IOException, InterruptedException {

		String description = toMultilineHtml(properties.getProperty(
				PROP_MAVEN_DESCRIPTION, ""));
		properties.put(PROP_MAVEN_DESCRIPTION, description);
		String formatted = expandTemplate(config.getDescriptionTemplate(), mms,
				mmsb, properties, description);
		mms.setDescription(formatted);
	}

	private void assignName(MavenModuleSet mms, MavenModuleSetBuild mmsb,
			Properties properties) throws IOException, InterruptedException {

		String projectName = properties.getProperty(PROP_MAVEN_NAME, "");
		String formatted = expandTemplate(config.getNameTemplate(), mms, mmsb,
				properties, projectName);
		mms.setDisplayNameOrNull(formatted);
	}

	private String expandTemplate(String template, MavenModuleSet mms,
			MavenModuleSetBuild mmsb, Properties properties, String defaultValue)
			throws IOException, InterruptedException {
		String expanded = defaultValue;
		if (StringUtils.isNotBlank(template)) {
			try {
				List<TokenMacro> macros = Collections
						.singletonList((TokenMacro) new PropertiesTokenMacro(
								properties));
				expanded = TokenMacro.expandAll(mmsb, null,
						config.getNameTemplate(), true, macros);

			} catch (MacroEvaluationException ex) {

			}
		}
		return expanded;
	}

	public void extract(MavenModuleSet mms, MavenModuleSetBuild mmsb)
			throws IOException, InterruptedException {
		Properties properties = new Properties();
		findAll(mms, mmsb, properties);
		assignAll(mms, mmsb, properties);
	}

	private boolean findAll(MavenModuleSet mms, MavenModuleSetBuild mmsb,
			Properties properties) {

		if (config.isAssignName()) {
			findName(mms, mmsb, properties);
		}

		try {
			findFromPom(mms, mmsb, properties);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private void findConfigurePomParser(Digester digester, Properties properties) {
		if (config.isAssignDescription()) {
			digester.addRule("project/description", new ExtractPropertyRule(
					PROP_MAVEN_DESCRIPTION, properties));
		}
	}

	private void findFromPom(MavenModuleSet mms, MavenModuleSetBuild mmsb,
			Properties properties) throws IOException, InterruptedException {

		MavenModule main = BuildUtils.getMainModule(mms);
		MavenBuild build = mms.getLastBuild().getModuleLastBuilds().get(main);
		FilePath p = build.getWorkspace().child("pom.xml");
		Digester digester = new Digester3();
		digester.setRules(new ExtendedBaseRules());

		findConfigurePomParser(digester, properties);

		InputStream is = p.read();
		try {
			digester.parse(is);
		} catch (SAXException ex) {
			throw new IOException("Can't read POM");
		} finally {
			is.close();
		}
	}

	private void findName(MavenModuleSet mms, MavenModuleSetBuild mmsb,
			Properties properties) {

		MavenModule main = BuildUtils.getMainModule(mms);
		if (main != null) {
			String projectName = main.getDisplayName();
			properties.put(PROP_MAVEN_NAME, projectName);
		}

	}

	private String toMultilineHtml(String value) {
		Matcher m = PATTERN_CLEAN.matcher(value);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {

			String g = m.group(1);
			m.appendReplacement(sb, g + "<br>");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
