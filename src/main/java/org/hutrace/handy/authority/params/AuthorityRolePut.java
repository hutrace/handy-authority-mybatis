package org.hutrace.handy.authority.params;

import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.annotation.verify.Number;

/**
 * 修改角色信息的参数接口类
 * <pre>
 *  必填: 
 *    id -> 修改的数据标识
 *    name -> 名称
 * @author hu trace
 */
public class AuthorityRolePut {
	
	@Number
	private Integer id;
	
	@NotBlank
	private String name;
	
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

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
