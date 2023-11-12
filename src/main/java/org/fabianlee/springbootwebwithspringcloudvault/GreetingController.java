package org.fabianlee.springbootwebwithspringcloudvault;
/**
 * Web endpoint for retrieving the Vault secret and its keys using various methods
 */

import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {
	
	@Autowired Environment env;
	@Autowired MyVaultTemplateService secret;
	@Autowired MySecretPropertiesFile secretFile;
	
	@GetMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello, world!\n" +
				"<a href=/secret>Show all keys of secret at " + getFullSecretPath() + "</a>\n" +
				"<a href=/secretConfigData>Show foo/username/password loaded by Spring ConfigData</a>\n" +
				"<a href=/secretSidecar>Show file created by vault sidecar at /vault/secrets/mysecret.properties</a>\n"; 
	}
	
	@GetMapping("/secret")
	@ResponseBody
	public String secretVaultTemplate() {
		
		StringBuilder sb = new StringBuilder("The secret is loaded using VaulTemplate from '" + getFullSecretPath() + "':\n");

		// fetch secret from Vault
		Properties props = secret.getSecretAsProperties(getFullSecretPath());

		if (props.isEmpty()) {
			sb.append("There must have been an error fetching the secret from Vault: " + getFullSecretPath());
		}else {
			Iterator propit = props.keySet().iterator();
			while(propit.hasNext()) {
				String k = (String)propit.next();
				String v = (String)props.getProperty(k);
				sb.append(k + "=" + v + "\n");
			}
		}
		return sb.toString();
	}
	
	@GetMapping("/secretConfigData")
	@ResponseBody
	public String secretConfigData() {
		
		//https://docs.spring.io/spring-cloud-vault/reference/config-data.html
		StringBuilder sb = new StringBuilder("The secret is loaded by ConfigData based on Spring application.properties settings:\n");

		// COULD return keys of secret very simply with env.getProperty
		// BUT this would require knowing the key names of the Vault secret ahead of time
		//sb.append("foo=" + env.getProperty("foo") + "\n");
		//sb.append("username=" + env.getProperty("username") + "\n");
		//sb.append("password=" + env.getProperty("password") + "\n");

		// We will instead use the general way of listing all the keys in the secret
		String targetPropertySourceType = "org.springframework.vault.core.env.LeaseAwareVaultPropertySource";
		// notice this is intentionally without '/data/' inserted
		String targetVaultPath = env.getProperty("VAULT_BACKEND") + "/" + env.getProperty("VAULT_CONTEXT") + "/" + env.getProperty("VAULT_PROFILE");
		
		Properties props = SpringEnvironmentUtils.getAllKnownProperties(env,targetPropertySourceType,targetVaultPath);
		if (props.isEmpty()) {
			sb.append("The ConfigData secret loaded per the values in the Spring application.properties does not exist: " + targetVaultPath + "\n");
			sb.append("Maybe the values in spring.cloud.vault.kv.{backend,default,profiles} are configured wrong for the service account/role?\n");
		}else {
			Iterator propit = props.keySet().iterator();
			while(propit.hasNext()) {
				String k = (String)propit.next();
				String v = (String)props.getProperty(k);
				sb.append(k + "=" + v + "\n");
			}
		}
		
		
		return sb.toString();
	}
	
	@GetMapping("/secretSidecar")
	@ResponseBody
	public String secretFile() {
		
		StringBuilder sb = new StringBuilder("The secret is loaded from the Vault sidecar injected file: /vault/secrets/mysecret.properties:\n");

		// fetch secret from file path
		Properties props = secretFile.getAllProperties();

		if (props.isEmpty()) {
			sb.append("The file '/vault/secrets/mysecret.properties' does not exist or have any properties, maybe you are not running the vault sidecar?\n");
		}else {
			Iterator propit = props.keySet().iterator();
			while(propit.hasNext()) {
				String k = (String)propit.next();
				String v = (String)props.getProperty(k);
				sb.append(k + "=" + v + "\n");
			}
		}
		return sb.toString();
	}
	
	
	
	private String getFullSecretPath() { 
		// 'data' must be inserted for Vault kv2
		return env.getProperty("VAULT_BACKEND") + 
				"/data/" + 
				env.getProperty("VAULT_CONTEXT") + "/" + 
				env.getProperty("VAULT_PROFILE");
	}

}
