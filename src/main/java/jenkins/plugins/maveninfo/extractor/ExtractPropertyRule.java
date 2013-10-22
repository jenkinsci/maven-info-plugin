package jenkins.plugins.maveninfo.extractor;

import java.util.Properties;

import org.apache.commons.digester.Rule;

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