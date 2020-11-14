package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.annotation.verify.Size;

public class LoginDefault {
	
	@NotBlank
	@Size(min = 2, max = 32)
	private String username;
	
	@NotBlank
	@Size(min = 6, max = 16)
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
