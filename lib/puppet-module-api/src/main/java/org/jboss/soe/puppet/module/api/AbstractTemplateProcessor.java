package org.jboss.soe.puppet.module.api;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Abstract class for template enabled models
 * 
 * @author ssadeghi
 *
 */
public abstract class AbstractTemplateProcessor implements TemplateProcessor {
	private static final String TEMPLATE_DIR = "/";
	
	private static final Configuration config = new Configuration();

	// TODO: remove the static initializer
	static {
		config.setClassForTemplateLoading(FileTemplate.class, TEMPLATE_DIR);
	}
	
	@Override
	public String process() {
		final StringWriter out = new StringWriter();
		process(out);
		return out.getBuffer().toString();
	}

	@Override
	public void process(Writer out) {
		try {
			Template template = config.getTemplate(getTemplatePath());
			template.process(getTemplateParams(), out);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected abstract String getTemplatePath();
	
	protected abstract Map<String, Object> getTemplateParams();
}
