package org.hutrace.handy.authority.bean;

import java.util.Date;

public class AuthorityUserBean {
	
	private Long id;
	private String username;
	private String phone;
	private String email;
	private String password;
	private String salt;
	private String mix;
	private String nickname;
	private String secret;
	private String inviteCode;
	private String inviteCodeFrom;
	private String organization;
	private Integer enabled;
	private String belong;
	private Date createTime;
	private String createIp;
	private Date loginTime;
	private String loginIp;
	private Long addUid;
	private Integer addRid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getMix() {
		return mix;
	}
	public void setMix(String mix) {
		this.mix = mix;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public String getInviteCodeFrom() {
		return inviteCodeFrom;
	}
	public void setInviteCodeFrom(String inviteCodeFrom) {
		this.inviteCodeFrom = inviteCodeFrom;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateIp() {
		return createIp;
	}
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
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
