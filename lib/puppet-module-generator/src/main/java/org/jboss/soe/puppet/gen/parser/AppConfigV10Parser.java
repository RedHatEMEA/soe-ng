package org.jboss.soe.puppet.gen.parser;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

import java.io.FileReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.soe.appconfig.v1_0.AppConfig;
import org.jboss.soe.appconfig.v1_0.AppConfig.ApplicationServer.Logging;
import org.jboss.soe.appconfig.v1_0.AppConfig.ApplicationServer.Logging.Category;
import org.jboss.soe.appconfig.v1_0.AppConfig.ApplicationServer.Osgi;
import org.jboss.soe.appconfig.v1_0.AppConfig.ApplicationServer.Osgi.Capability;
import org.jboss.soe.appconfig.v1_0.AppConfig.ApplicationServer.SystemProperties;
import org.jboss.soe.appconfig.v1_0.AppConfig.ApplicationServer.SystemProperties.Property;
import org.jboss.soe.appconfig.v1_0.AppConfig.Dependencies;
import org.jboss.soe.appconfig.v1_0.AppConfig.Dependencies.Dependency;
import org.jboss.soe.puppet.gen.util.Utils;

/**
 * Parser for app-config-1.0 schema
 * 
 * @author ssadeghi
 */
public class AppConfigV10Parser {
    public static AppConfigData parse(final String appConfig) throws Exception {
        final AppConfig config = Utils.parseXml(AppConfig.class, new FileReader(appConfig));
        final AppConfigData data = new AppConfigData();

        data.setName(config.getName());
        data.setVersion(config.getVersion());

        addDependencies(data, config.getDependencies());
        
        if (config.getApplicationServer() != null) {
	        data.getSystemProperties().addAll(transformSystemProperties(config.getApplicationServer().getSystemProperties()));
	        data.getOsgiCapibilities().putAll(transformOsgiCapabilities(config.getApplicationServer().getOsgi()));
	        data.getLogCategories().putAll(transformLogCategories(config.getApplicationServer().getLogging()));
	        data.setRestartRequiredOnCompletion(Boolean.TRUE.equals(config.getApplicationServer().isRestartOnCompletion()));
        }

        return data;
    }


    private static void addDependencies(AppConfigData data, Dependencies dependencies) {
        if (dependencies != null) {
            for (Dependency dependency : dependencies.getDependency()) {
                data.addDependency(dependency.getName(), dependency.getVersion());
            }
        }
    }

    private static Map<String, String> transformLogCategories(Logging logging) {
        final Map<String, String> categories = newHashMap();

        if (logging != null && logging.getCategory() != null) {
            for (Category category : logging.getCategory()) {
                categories.put(category.getName(), category.getLevel());
            }
        }

        return categories;
    }


    private static Map<String, Integer> transformOsgiCapabilities(Osgi osgi) {
        final Map<String, Integer> capabilities = newLinkedHashMap();

        if (osgi != null && osgi.getCapability() != null) {
            for (Capability capability : osgi.getCapability()) {
                capabilities.put(capability.getName(), capability.getOrder());
            }
        }

        return capabilities;
    }


    private static Collection<String> transformSystemProperties(SystemProperties systemProperties) {
        final List<String> props = newLinkedList();

        if (systemProperties != null && systemProperties.getProperty() != null) {
            for (final Property property : systemProperties.getProperty()) {
                props.add(property.getName());
            }
        }

        return props;
    }
}
