package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.BigNumber;
import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.annotation.verify.Size;

public class AuthorityUserPassword {
	
	@BigNumber
	private Long id;

	@NotBlank
	@Size(min = 6, max = 16, msg = "authority.check.user.password", mandatory = true)
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
