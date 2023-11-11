package org.fabianlee.springbootwithvaultsidecar;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

@Service
public class SecretService {
	
	@Autowired
    private VaultTemplate vaultTemplate;

    public Properties getSecretAsProperties(String backend,String context,String profile) {

    	// return all keys from secret in this
    	Properties props = new Properties();
    	
    	VaultResponse response = null;
    	try {
    		// read secret from kv2 Vault path, which needs 'data' inserted
    		String fullSecretPath = backend + "/data/" + context + "/" + profile;
    		System.out.println("fullSecretPath: " + fullSecretPath);
    		response = vaultTemplate.read(fullSecretPath);

    		// go through response from Vault server
            Iterator <String>allkeysit = response.getRequiredData().keySet().iterator();
            while(allkeysit.hasNext()) {
            	
            	// going to be either 'data' or 'metadata'
            	String outerkey = allkeysit.next();
            	
            	// only process secret data (not metadata for secret)
            	if ("data".equals(outerkey)) {
	            	Object genericObj = response.getData().get(outerkey);
	            	if ( genericObj instanceof java.util.LinkedHashMap ) {
	            		LinkedHashMap <String,String>secretmap = (LinkedHashMap<String,String>)genericObj;
	            		Iterator <String>secretmapit = secretmap.keySet().iterator();
	            		while(secretmapit.hasNext()) {
	            			String skey = secretmapit.next();
	            			String sval = secretmap.get(skey);
	            			props.put(skey, sval);
	            			//System.out.println("] " + skey + "->" + sval);
	            		}
	            	}else {
	            		System.out.println("'data' had unexpected type: " + genericObj);
	            	}
            	}
            }
    	}catch(Exception exc) {
    		exc.printStackTrace();
    	}
    	
        return props;
    }

}
