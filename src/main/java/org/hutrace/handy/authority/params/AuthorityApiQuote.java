package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.Number;

public class AuthorityApiQuote {
	
	@Number
	private Integer id;
	
	@Number
	private Integer quote;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuote() {
		return quote;
	}

	public void setQuote(Integer quote) {
		this.quote = quote;
	}
}
