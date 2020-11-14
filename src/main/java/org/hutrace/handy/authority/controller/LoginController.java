package org.hutrace.handy.authority.controller;

import java.util.Map;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.impl.AuthorityLogin.LoginType;
import org.hutrace.handy.authority.impl.login.LoginSimpleConsole;
import org.hutrace.handy.authority.params.LoginDefault;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.mybatis.exception.RollbackException;

@Controller
@RequestMapping("login")
public class LoginController {
	
	@RequestMapping(value = "default", method = RequestMethod.POST)
	@Validated
	public Map<String, Object> defaultLogin(HttpRequest request, LoginDefault params) throws SelectException, RollbackException {
		LoginSimpleConsole login = new LoginSimpleConsole();
		return login.login(request, LoginType.USERNAME, params.getUsername(), params.getPassword());
	}
	
}
