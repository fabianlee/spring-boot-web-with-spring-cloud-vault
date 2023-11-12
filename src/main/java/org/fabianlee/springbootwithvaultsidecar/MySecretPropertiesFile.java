package org.fabianlee.springbootwithvaultsidecar;
/**
 * The manifest yaml has an annotation that creates a properties file inside the container:
 * vault.hashicorp.com/agent-inject-template-mysecret.properties
 * 
 * This class loads that file from the container filesystem
 */

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value="file:/vault/secrets/mysecret.properties", ignoreResourceNotFound=true)
public class MySecretPropertiesFile {
	
	@Autowired
    private Environment env;

	// We have to look through global Environment
	// to find the type we are looking for, and the file name
	String targetPropertySourceType = "org.springframework.core.io.support.ResourcePropertySource";
	String targetPropertyFileName = "file:/vault/secrets/mysecret.properties";
	

	// get properties from global Environment object, filter by type+name
	public Properties getAllProperties() {
		return getAllKnownProperties(env,targetPropertySourceType,targetPropertyFileName);
	}

    
	//https://stackoverflow.com/questions/23506471/access-all-environment-properties-as-a-map-or-properties-object
	// Given an Environment object, list all the keys
	// you would think there would be a method (such as keySet) to do this, but there is not
    private static Properties getAllKnownProperties(Environment env,String targetPropertySourceType,String targetPropertyFileName) {

    	// for return
        Properties props = new Properties();
        
        if (env instanceof ConfigurableEnvironment) {
            for (org.springframework.core.env.PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {

            	// only add properties that come from certain type + name
            	if(targetPropertySourceType.equals(propertySource.getClass().getName()) && propertySource.getName().contains("")) {
	                if (propertySource instanceof EnumerablePropertySource) {
	                    for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
	                        props.put(key, propertySource.getProperty(key));
	                    }
	                }
	            }
            	
            }
        }
        
        return props;
    }    
	

}
