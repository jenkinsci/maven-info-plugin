package jenkins.plugins.maveninfo.extractor.base;

import java.io.IOException;

/**
 * Interface to find properties.
 * 
 * @author emenaceb
 * 
 */
public interface PropertiesFinder {

	void findProperties(ExtractorContext ctx) throws IOException,
			InterruptedException;
}
