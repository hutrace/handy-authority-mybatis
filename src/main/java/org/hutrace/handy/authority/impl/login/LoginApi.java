package org.hutrace.handy.authority.impl.login;

import java.util.Map;

import org.hutrace.handy.authority.bean.AuthorityUserBean;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.impl.AuthorityLogin;
import org.hutrace.handy.authority.impl.code.PasswordEncrypt;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * Api接口的登陆类
 * <p>该类跳过密码验证，改用第二个参数与secret做匹配验证
 * @author hu trace
 *
 */
public class LoginApi extends AuthorityLogin {

	/**
	 * 跳过密码验证
	 * <p>改用第二个参数与secret做匹配验证
	 */
	@Override
	public void checkMatch(AuthorityUserBean user, String arg1, PasswordEncrypt passwordEncrypt)
			throws SelectException, RollbackException {
		if(!user.getSecret().equals(arg1)) {
			throw new SelectException(1, ApplicationProperty.get("authority.login.error.secret"));
		}
	}

	@Override
	protected void buildRspOther(AuthorityUserBean user, Map<String, Object> rsp)
			throws SelectException, RollbackException {
		rsp.put("username", user.getUsername());
		rsp.put("phone", user.getPhone());
		rsp.put("email", user.getEmail());
		rsp.put("nickname", user.getNickname());
		rsp.put("organization", user.getOrganization());
		rsp.put("loginTime", user.getLoginTime());
		rsp.put("loginIp", user.getLoginIp());
	}

	@Override
	protected Map<String, Object> buildExtendBufferUser(AuthorityUserBean user) {
		Map<String, Object> buffer = sql().selectOne(mapper("selectRoleInfoByClientId"), user.getId());
		return buffer;
	}

}
