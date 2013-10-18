package jenkins.plugins.maveninfo.extractor;

import hudson.FilePath;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;
import jenkins.plugins.maveninfo.patches.Digester3;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.commons.digester.Rule;
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

	private static final Pattern PATTERN_CLEAN = Pattern
			.compile("\\s*([^\\r\\n]*)\\s*[\\n\\r]");

	private Map<String, String> extractedInfo = new TreeMap<String, String>();

	private MavenInfoJobConfig config;

	public MavenInfoExtractor(MavenInfoJobConfig config) {
		super();
		this.config = config;
	}

	public void extract(MavenModuleSet mms, MavenModuleSetBuild mmsb)
			throws IOException, InterruptedException {
		if (config.isAssignName()) {
			assignName(mms, mmsb);
		}
		try {
			parsePom(mms, mmsb);
			if (config.isAssignDescription()) {
				assignDescription(mms, mmsb);
			}
		} catch (Exception ex) {
			// ignore exceptions
		}
	}

	private void assignName(MavenModuleSet mms, MavenModuleSetBuild mmsb)
			throws IOException, InterruptedException {
		String projectName = null;

		MavenModule parent = mms.getLastBuild().getParent().getRootModule();
		if (parent != null) {
			projectName = parent.getDisplayName();
			mms.setDisplayNameOrNull(projectName);
		}
	}

	private class ExtractPropRule extends Rule {

		private String propertyName;

		public ExtractPropRule(String propertyName) {
			super();
			this.propertyName = propertyName;
		}

		@Override
		public void body(String namespace, String name, String text)
				throws Exception {
			extractedInfo.put(propertyName, text.trim());
		}
	}

	private Map<String, String> parsePom(MavenModuleSet mms,
			MavenModuleSetBuild mmsb) throws IOException, InterruptedException {
		MavenModuleSet root = mms.getLastBuild().getParent();
		String filePath = root.getRootPOM(mmsb.getEnvironment(null));

		FilePath p = mmsb.getWorkspace().child(filePath);

		Digester digester = new Digester3();
		digester.setRules(new ExtendedBaseRules());

		if (config.isAssignDescription()) {
			digester.addRule("project/description", new ExtractPropRule(
					"description"));
		}

		InputStream is = p.read();
		try {
			digester.parse(is);
		} catch (SAXException ex) {
			throw new IOException("Can't read POM");
		} finally {
			is.close();
		}
		return extractedInfo;
	}

	private void assignDescription(MavenModuleSet mms, MavenModuleSetBuild mmsb)
			throws IOException, InterruptedException {

		String description = extractedInfo.get("description");
		if (description == null) {
			description = "";
		}
		Matcher m = PATTERN_CLEAN.matcher(description);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {

			String g = m.group(1);
			m.appendReplacement(sb, g + "<br>");
		}
		m.appendTail(sb);
		mms.setDescription(sb.toString());
	}

}
