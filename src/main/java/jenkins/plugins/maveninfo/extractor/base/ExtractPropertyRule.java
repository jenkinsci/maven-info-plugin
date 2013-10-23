package jenkins.plugins.maveninfo.extractor.base;

import java.util.Properties;

import org.apache.commons.digester.Rule;

/**
 * Rule to extract a element's contents as a property.
 */
public class ExtractPropertyRule extends Rule {

	private String propertyName;

	private Properties properties;

	public ExtractPropertyRule(String propertyName, Properties properties) {
		super();
		this.propertyName = propertyName;
		this.properties = properties;
	}

	@Override
	public void body(String namespace, String name, String text)
			throws Exception {
		properties.put(propertyName, text.trim());
	}
}