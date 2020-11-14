package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.annotation.verify.Number;
import org.hutrace.handy.annotation.verify.Size;

public class AuthorityApiPut {
	
	@Number
	private Integer id;
	
	@NotBlank
	@Size(max = 128, mandatory = true, msg = "authority.check.api.api")
	private String api;
	
	@NotBlank
	@Size(max = 16, mandatory = true, msg = "authority.check.api.method")
	private String method;
	
	private String explain;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
