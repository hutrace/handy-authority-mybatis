package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.BigNumber;
import org.hutrace.handy.annotation.verify.Number;

public class AuthorityUserEnabled {
	
	@BigNumber
	private Long id;
	
	@Number(min = 0, max = 1)
	private Integer enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	
}
