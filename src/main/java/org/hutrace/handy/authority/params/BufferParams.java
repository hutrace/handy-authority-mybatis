package org.hutrace.handy.authority.params;

import org.hutrace.handy.authority.impl.AuthorityLogin;

/**
 * 后台用户缓存的实体类
 * <p>你可以自定义缓存后继承此类以便扩展
 * @author hu trace
 *
 */
public class BufferParams {
	
	protected Long clientId;
	
	protected Long bufUid;
	
	protected Integer bufRoleId;
	
	protected Integer bufRolePid;
	
	public Long getClientId() {
		return clientId;
	}
	
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getBufUid() {
		return bufUid;
	}

	public void setBufUid(Long bufUid) {
		this.bufUid = bufUid;
	}

	public Integer getBufRoleId() {
		return bufRoleId;
	}
	
	public void setBufRoleId(Integer bufRoleId) {
		this.bufRoleId = bufRoleId;
	}
	
	public Integer getBufRolePid() {
		return bufRolePid;
	}
	
	public void setBufRolePid(Integer bufRolePid) {
		this.bufRolePid = bufRolePid;
	}
	
	public boolean isAdmin() {
		return clientId.intValue() == AuthorityLogin.adminUserId() || bufRolePid == null;
	}
	
	@Override
	public String toString() {
		return "BufferParams [clientId=" + clientId + ", bufRoleId=" + bufRoleId + ", bufRolePid=" + bufRolePid + "]";
	}
	
}
