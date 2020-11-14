package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.Number;

/**
 * 角色列表查询的参数接收类
 * <pre>
 *  必填: 
 *    pageStart - 开始查询的条数
 *    pageSize - 查询多少条
 * @author hu trace
 */
public class AuthorityRoleQuery {
	
	private Integer id;
	private Integer pid;
	private String name;
	private Long clientId;
	@Number
	private Integer pageStart;
	@Number
	private Integer pageSize;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
}
