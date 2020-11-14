package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.annotation.verify.Number;

public class AuthorityModulePut {
	
	@Number
	private Integer id;
	
	@NotBlank
	private String name;
	
	@Number
	private Integer type;
	private String page;
	private String explain;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
