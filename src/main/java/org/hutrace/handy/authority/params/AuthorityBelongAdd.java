package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.annotation.verify.Size;

public class AuthorityBelongAdd {
	
	@NotBlank
	@Size(max = 32)
	private String belong;
	
	@NotBlank
	@Size(max = 64)
	private String explain;

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
