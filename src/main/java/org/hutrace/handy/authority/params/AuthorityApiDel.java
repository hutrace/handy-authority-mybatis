package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.Number;

public class AuthorityApiDel {
	
	@Number
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
