package org.fabianlee.springbootwithvaultsidecar;

import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
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
				"<a href=/secretFile>Show file created by vault injector at /vault/secrets/mysecret.properties</a>\n"; 
	}
	
	@GetMapping("/secret")
	@ResponseBody
	public String secretVaultTemplate() {
		
		StringBuilder sb = new StringBuilder("The secret is loaded using VaulTemplate from '" + getFullSecretPath() + "':\n");

		// fetch secret from Vault
		Properties props = secret.getSecretAsProperties(getFullSecretPath());

		Iterator propit = props.keySet().iterator();
		while(propit.hasNext()) {
			String k = (String)propit.next();
			String v = (String)props.getProperty(k);
			sb.append(k + "=" + v + "\n");
		}
		return sb.toString();
	}
	
	@GetMapping("/secretFile")
	@ResponseBody
	public String secretFile() {
		
		StringBuilder sb = new StringBuilder("The secret is loaded from the file: /vault/secrets/mysecret.properties:\n");

		// fetch secret from file path
		Properties props = secretFile.getAllProperties();

		Iterator propit = props.keySet().iterator();
		while(propit.hasNext()) {
			String k = (String)propit.next();
			String v = (String)props.getProperty(k);
			sb.append(k + "=" + v + "\n");
		}
		return sb.toString();
	}
	
	
	@GetMapping("/secretConfigData")
	@ResponseBody
	public String secretConfigData() {
		
		//https://docs.spring.io/spring-cloud-vault/reference/config-data.html
		StringBuilder sb = new StringBuilder("The secret is loaded by ConfigData based on application.properties:\n");
		
		sb.append("foo=" + env.getProperty("foo") + "\n");
		sb.append("username=" + env.getProperty("username") + "\n");
		sb.append("password=" + env.getProperty("password") + "\n");
		
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
