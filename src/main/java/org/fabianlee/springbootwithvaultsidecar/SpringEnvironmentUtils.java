package org.fabianlee.springbootwithvaultsidecar;

import java.util.Properties;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

public class SpringEnvironmentUtils {
	
	//https://stackoverflow.com/questions/23506471/access-all-environment-properties-as-a-map-or-properties-object
	// Given an Environment object, list all the keys
	// you would think there would be a method (such as keySet) to do this, but there is not
    public static Properties getAllKnownProperties(Environment env,String targetPropertySourceType,String targetPropertyFileName) {

    	// for return
        Properties props = new Properties();
        
        if (env instanceof ConfigurableEnvironment) {
            for (org.springframework.core.env.PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {

            	// only add properties that come from certain type + name
            	if(targetPropertySourceType.equals(propertySource.getClass().getName()) && propertySource.getName().contains(targetPropertyFileName)) {
	                if (propertySource instanceof EnumerablePropertySource) {
	                    for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
	                        props.put(key, propertySource.getProperty(key));
	                    }
	                }
	            } // if type and name match
            	
            } // each property source
        }
        
        return props;
    }    
	

}
