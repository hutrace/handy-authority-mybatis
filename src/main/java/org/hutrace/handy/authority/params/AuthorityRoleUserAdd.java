package org.hutrace.handy.authority.params;

import java.util.List;

import org.hutrace.handy.annotation.verify.Number;

/**
 * 保存角色用户的参数接收类
 * <p>必填: rid -> 角色ID
 * <pre>
 *  选填: 
 *    adds -> 新增的用户id集合
 *    dels -> 删除的用户id集合
 * @author hu trace
 */
public class AuthorityRoleUserAdd {
	
	@Number
	private Integer rid;
	
	private List<Long> adds;
	
	private List<Long> dels;

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public List<Long> getAdds() {
		return adds;
	}

	public void setAdds(List<Long> adds) {
		this.adds = adds;
	}

	public List<Long> getDels() {
		return dels;
	}

	public void setDels(List<Long> dels) {
		this.dels = dels;
	}
	
}
