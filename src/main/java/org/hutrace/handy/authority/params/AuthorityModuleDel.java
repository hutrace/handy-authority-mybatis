package org.hutrace.handy.authority.params;

import java.util.List;

import org.hutrace.handy.annotation.verify.Number;

public class AuthorityModuleDel {
	
	@Number
	private List<Integer> ids;
	
	private List<AuthorityApiQuote> apiQuotes;
	
	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public List<AuthorityApiQuote> getApiQuotes() {
		return apiQuotes;
	}

	public void setApiQuotes(List<AuthorityApiQuote> apiQuotes) {
		this.apiQuotes = apiQuotes;
	}
	
}
