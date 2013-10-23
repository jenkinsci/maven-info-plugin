package jenkins.plugins.maveninfo.extractor.base;

import hudson.maven.MavenModuleSetBuild;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jenkins.plugins.maveninfo.config.MavenInfoJobConfig;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;

/**
 * {@link PropertiesSaver} base class.<br>
 * <br>
 * Contains helper methods.<br>
 * 
 * @author emenaceb
 * 
 */
public abstract class AbstractPropertiesSaver implements PropertiesSaver {

	private static final Pattern PATTERN_CLEAN = Pattern.compile(
			"\\s*([^\\r\\n]*)\\s*[\\n\\r]", Pattern.MULTILINE);

	public AbstractPropertiesSaver() {
		super();
	}

	protected String expandProperty(String template, String property,
			ExtractorContext context) throws IOException, InterruptedException {
		Properties properties = context.getProperties();
		String defaultValue = properties.getProperty(property, "");
		return expandTemplate(template, context, defaultValue);
	}

	protected String expandPropertyMultiline(String template, String property,
			ExtractorContext context) throws IOException, InterruptedException {
		Properties properties = context.getProperties();
		String oldValue = properties.getProperty(property, "");
		String defaultValue = toMultilineHtml(oldValue);
		try {
			properties.setProperty(property, defaultValue);
			return expandTemplate(template, context, defaultValue);
		} finally {
			properties.setProperty(property, oldValue);
		}

	}

	protected String expandTemplate(String template, ExtractorContext context,
			String defaultValue) throws IOException, InterruptedException {

		MavenInfoJobConfig config = context.getConfig();
		MavenModuleSetBuild mmsb = context.getMmsb();
		Properties properties = context.getProperties();

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