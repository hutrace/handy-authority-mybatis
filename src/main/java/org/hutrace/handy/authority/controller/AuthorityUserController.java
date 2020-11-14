package org.hutrace.handy.authority.controller;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.bean.AuthorityUserBean;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.impl.AuthorityLoader;
import org.hutrace.handy.authority.impl.login.LoginSimpleConsole;
import org.hutrace.handy.authority.params.AuthorityUserAdd;
import org.hutrace.handy.authority.params.AuthorityUserEnabled;
import org.hutrace.handy.authority.params.AuthorityUserGet;
import org.hutrace.handy.authority.params.AuthorityUserPassword;
import org.hutrace.handy.authority.params.BufferParams;
import org.hutrace.handy.authority.service.AuthorityUserService;
import org.hutrace.handy.http.RequestMethod;

@Authority
@Controller
@RequestMapping("authority/user")
public class AuthorityUserController {
	
	private @Autowire AuthorityUserService service;
	
	@RequestMapping(method = RequestMethod.GET)
	@Validated
	public Map<String, Object> query(AuthorityUserGet params, BufferParams buf) throws SelectException {
		if(AuthorityLoader.queryUserNeedAddUid) {
			params.setAddUid(buf.getBufUid());
			if(params.getAddUid() == null || params.getAddUid() == 0) {
				params.setAddUid(buf.getClientId());
			}
		}
		params.setAddRid(buf.getBufRoleId());
		return service.query(params);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Validated
	public int insert(AuthorityUserAdd params, BufferParams buf) throws InsertException {
		LoginSimpleConsole login = new LoginSimpleConsole();
		AuthorityUserBean user = new AuthorityUserBean();
		user.setPhone(params.getPhone());
		user.setEmail(params.getEmail());
		user.setBelong(params.getBelong());
		user.setUsername(params.getUsername());
		user.setPassword(params.getPassword());
		user.setNickname(params.getNickname());
		user.setOrganization(params.getOrganization());
		return login.create(user, buf);
	}
	
	@RequestMapping(value = "enabled", method = RequestMethod.PATCH)
	@Validated
	public int enabled(AuthorityUserEnabled params) throws UpdateException {
		return service.updateEnabled(params);
	}
	
	@RequestMapping(value = "password", method = RequestMethod.PATCH)
	@Validated
	public int password(AuthorityUserPassword params) throws UpdateException {
		LoginSimpleConsole login = new LoginSimpleConsole();
		return login.updatePassword(params.getId(), params.getPassword());
	}

	@RequestMapping(value = "byrid", method = RequestMethod.GET)
	public List<Map<String, Object>> selectNotRoleAll(BufferParams buf) throws SelectException {
		if(buf.isAdmin()) {
			return service.selectNotRoleByRid(null);
		}
		return service.selectNotRoleByRid(buf.getBufRoleId());
	}
	
}
