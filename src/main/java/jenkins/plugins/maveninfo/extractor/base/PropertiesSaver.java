package jenkins.plugins.maveninfo.extractor.base;

import java.io.IOException;

/**
 * Interface to save properties.
 * 
 * @author emenaceb
 * 
 */
public interface PropertiesSaver {

	void saveProperties(ExtractorContext context) throws IOException,
			InterruptedException;
}
