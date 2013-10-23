package jenkins.plugins.maveninfo.extractor.base;

import hudson.model.TaskListener;
import hudson.model.AbstractBuild;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;

import com.google.common.collect.ListMultimap;

/**
 * Token Macro for extracted properties substitution.
 * 
 * @author emenaceb
 * 
 */
public class PropertiesTokenMacro extends TokenMacro {

	private Properties properties;

	public PropertiesTokenMacro(Properties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public boolean acceptsMacroName(String macroName) {
		return properties.containsKey(macroName);
	}

	@Override
	public String evaluate(AbstractBuild<?, ?> context, TaskListener listener,
			String macroName, Map<String, String> arguments,
			ListMultimap<String, String> argumentMultimap)
			throws MacroEvaluationException, IOException, InterruptedException {
		return properties.getProperty(macroName, "");
	}

}
