package org.hutrace.handy.authority.impl.login;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.authority.bean.AuthorityApiVO;
import org.hutrace.handy.authority.bean.AuthorityUserBean;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.impl.code.PasswordEncrypt;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 用于控制台无特殊情况登陆
 * <pre>
 *  该类实现的功能包含: 
 *    扩展缓存，缓存登陆用户的角色ID、角色父级ID
 *    编辑响应，扩展响应数据: apis、pages、menus
 * @author hu trace
 */
public class LoginSimpleConsole extends LoginBasicRsp {

	@Override
	public void checkMatch(AuthorityUserBean user, String arg1, PasswordEncrypt passwordEncrypt)
			throws SelectException, RollbackException {
		try {
			if(!user.getPassword().equals(passwordEncrypt.encrypt(arg1, user.getSalt()))) {
				throw new SelectException(1, ApplicationProperty.get("authority.login.error.pwd"));
			}
		}catch (SelectException e) {
			throw e;
		}catch (Exception e) {
			throw new SelectException(e);
		}
	}

	@Override
	protected Map<String, Object> buildExtendBufferUser(AuthorityUserBean user) {
		// 包含bufRoleId和bufRolePid
		Map<String, Object> buffer = sql().selectOne(mapper("selectRoleInfoByClientId"), user.getId());
		return buffer;
	}
	
	@Override
	protected void buildRspModule(AuthorityUserBean user, Map<String, Object> rsp) throws SelectException, RollbackException {
		List<AuthorityApiVO> apis = buildAndBufferAuthority(user);
		rsp.put("apis", apis);
		rsp.put("pages", sql().selectList(mapper("selectPagesByUid"), user.getId()));
		rsp.put("menus", sql().selectList(mapper("selectMenusByUid"), user.getId()));
	}

}
