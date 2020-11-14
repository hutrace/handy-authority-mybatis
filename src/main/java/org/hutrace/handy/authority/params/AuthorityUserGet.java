package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.Number;

public class AuthorityUserGet {
	
	@Number
	private Integer pageStart;
	
	@Number
	private Integer pageSize;
	
	private String username;
	
	private String email;
	
	private String phone;
	
	private String nickname;
	
	private String belong;
	
	private Integer enabled;
	
	private Long addUid;
	
	private Integer addRid;

	public Integer getPageStart() {
		return pageStart;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Long getAddUid() {
		return addUid;
	}

	public void setAddUid(Long addUid) {
		this.addUid = addUid;
	}

	public Integer getAddRid() {
		return addRid;
	}

	public void setAddRid(Integer addRid) {
		this.addRid = addRid;
	}
	
}
