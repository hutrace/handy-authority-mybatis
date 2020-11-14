package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.Number;

/**
 * 角色ID参数接收类
 * @author hu trace
 */
public class AuthorityRoleRid {
	
	@Number
	private Integer rid;

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}
	
}
