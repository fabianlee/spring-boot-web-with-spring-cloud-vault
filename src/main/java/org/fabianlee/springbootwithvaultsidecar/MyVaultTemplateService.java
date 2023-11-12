package org.fabianlee.springbootwithvaultsidecar;
/**
 * The VaultTemplate object allows us to make calls to the Vault server
 * This class uses it to load a secret, then list the keys in the secret
 */


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

@Service
public class MyVaultTemplateService {
	
	@Autowired
    private VaultTemplate vaultTemplate;

    public Properties getSecretAsProperties(String fullSecretPath) {

    	// for final return 
    	Properties props = new Properties();
    	
    	VaultResponse response = null;
    	try {
    		// read secret from kv2 Vault path
    		System.out.println("fullSecretPath: " + fullSecretPath);
    		response = vaultTemplate.read(fullSecretPath);

    		// go through response from Vault server
            Object vaultdata = response.getRequiredData().get("data"); // 'metadata' is other key that could be pulled
            LinkedHashMap <String,String>secretmap =(LinkedHashMap<String,String>)vaultdata;
	            		
    		// go through each key in secret, add to Properties
    		Iterator <String>secretmapit = secretmap.keySet().iterator();
    		while(secretmapit.hasNext()) {
    			String secretkey = secretmapit.next();
    			String secretval = secretmap.get(secretkey);
    			props.put(secretkey, secretval);
    		}
    		
    	}catch(Exception exc) {
    		exc.printStackTrace();
    	}
    	
        return props;
    }

}
