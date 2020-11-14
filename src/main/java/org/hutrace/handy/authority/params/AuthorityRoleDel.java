package org.hutrace.handy.authority.params;

import java.util.List;

import org.hutrace.handy.annotation.verify.Number;

/**
 * 权限角色管理删除接口的参数接收类
 * <p>必填: id（角色ID）
 * <p>ids是后台自动封装的，不需要传入
 * @author hu trace
 */
public class AuthorityRoleDel {
	
	@Number
	private Integer id;
	
	private List<Integer> ids;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
