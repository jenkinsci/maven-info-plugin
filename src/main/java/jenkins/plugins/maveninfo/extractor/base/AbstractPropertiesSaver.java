package jenkins.plugins.maveninfo.extractor.base;

import hudson.maven.MavenModuleSetBuild;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

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

	public AbstractPropertiesSaver() {
		super();
	}

	protected String expandProperty(String template, String property,
			ExtractorContext context) throws IOException, InterruptedException {
		Properties properties = context.getProperties();
		String defaultValue = properties.getProperty(property, "");
		return expandTemplate(template, context, defaultValue);
	}

	protected String expandPropertyPre(String template, String property,
			ExtractorContext context) throws IOException, InterruptedException {
		Properties properties = context.getProperties();
		String defaultValue = properties.getProperty(property, "");
		if (StringUtils.isNotBlank(defaultValue)) {
			defaultValue = "<pre>" + defaultValue + "</pre>";
		}
		return expandTemplate(template, context, defaultValue);

	}

	protected String expandTemplate(String template, ExtractorContext context,
			String defaultValue) throws IOException, InterruptedException {

		MavenModuleSetBuild mmsb = context.getMmsb();
		Properties properties = context.getProperties();

		String expanded = defaultValue;
		if (StringUtils.isNotBlank(template)) {
			try {
				List<TokenMacro> macros = Collections
						.singletonList((TokenMacro) new PropertiesTokenMacro(
								properties));
				expanded = TokenMacro.expandAll(mmsb, null, template, true,
						macros);

			} catch (MacroEvaluationException ex) {

			}
		}
		return expanded;
	}

}