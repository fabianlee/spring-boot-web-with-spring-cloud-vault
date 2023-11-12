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
		return SpringEnvironmentUtils.getAllKnownProperties(env,targetPropertySourceType,targetPropertyFileName);
	}

}
