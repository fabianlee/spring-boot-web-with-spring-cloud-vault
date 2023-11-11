package org.fabianlee.springbootwithvaultsidecar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {
	
	//@Autowired
	//private VaultTemplate vaultTemplate;
	
	@Value("${config.foo")
	String foo = "foo";
	@Value("${config.username")
	String username = "username";
	@Value("${config.password")
	String password = "password";
	
	@GetMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello, world!";
	}
	
	@GetMapping("/secret")
	@ResponseBody
	public String secret() {
		return "foo/username/password: " + getVaultSecretsFromEnv();
	}
	
	@GetMapping("/secret2")
	@ResponseBody
	public String secret2() {
		return "foo/username/password: " + getVaultSecretsFromConfig();
	}
	
	//@GetMapping("/secret3")
	//@ResponseBody
	//public String secret3() {
	//	VaultResponse response = vaultTemplate
	//	        .opsForKeyValue("secret", KeyValueBackend.KV_2).get("webapp/config");
	//	return "foo/username/password: " + response.getData().get("foo") + "," + response.getData().get("username");
	//}
	
	@Autowired Environment env;
	public String getVaultSecretsFromEnv() {
		return env.getProperty("foo") + "," + env.getProperty("username") + "," + env.getProperty("password");
	}
	public String getVaultSecretsFromConfig() {
		return foo + "," + username + "," + password;
	}

}
