package org.hutrace.handy.authority.controller;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.impl.login.LoginSimpleConsole;
import org.hutrace.handy.authority.params.CommonPwdPut;
import org.hutrace.handy.http.RequestMethod;

@Controller
@RequestMapping("common")
public class CommonController {
	
	@RequestMapping(value = "pwd", method = RequestMethod.PUT)
	@Validated
	public int updatePwd(CommonPwdPut params) throws UpdateException {
		LoginSimpleConsole login = new LoginSimpleConsole();
		return login.updatePassword(params.getId(), params.getPassword(), params.getOldPassword());
	}
	
}
