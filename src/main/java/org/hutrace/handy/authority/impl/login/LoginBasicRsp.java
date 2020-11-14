package org.hutrace.handy.authority.impl.login;

import java.util.Map;

import org.hutrace.handy.authority.bean.AuthorityUserBean;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.impl.AuthorityLogin;
import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 包含了基本响应数据的登陆抽象类
 * @author hu trace
 *
 */
public abstract class LoginBasicRsp extends AuthorityLogin {

	@Override
	protected void buildRspOther(AuthorityUserBean user, Map<String, Object> rsp)
			throws SelectException, RollbackException {
		rsp.put("id", user.getId());
		rsp.put("username", user.getUsername());
		rsp.put("phone", user.getPhone());
		rsp.put("email", user.getEmail());
		rsp.put("nickname", user.getNickname());
		rsp.put("organization", user.getOrganization());
		rsp.put("loginTime", user.getLoginTime());
		rsp.put("loginIp", user.getLoginIp());
		rsp.put("secret", user.getSecret());
	}

}
