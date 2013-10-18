package jenkins.plugins.maveninfo.patches;

import javax.xml.parsers.SAXParserFactory;

import hudson.util.Digester2;

public class Digester3 extends Digester2 {

	@Override
	public SAXParserFactory getFactory() {

		if (factory == null) {
			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(namespaceAware);
			factory.setValidating(validating);
		}
		return (factory);
	}
}
