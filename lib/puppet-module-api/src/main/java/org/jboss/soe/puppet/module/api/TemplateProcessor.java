package org.jboss.soe.puppet.module.api;

import java.io.Writer;

/**
 * Interface for template based models
 * 
 * @author ssadeghi
 *
 */
public interface TemplateProcessor {
	void process(Writer out);
	
	String process();
}
