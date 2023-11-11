package org.fabianlee.springbootwithvaultsidecar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {
	
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
	
	@Autowired Environment env;
	public String getVaultSecretsFromEnv() {
		return env.getProperty("foo") + "," + env.getProperty("username") + "," + env.getProperty("password");
	}

}
