package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.Number;

public class AuthorityModuleSequence {
	
	@Number
	private Integer id;
	@Number
	private Integer sequence;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
}
