package org.hutrace.handy.authority.controller;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityApiAdd;
import org.hutrace.handy.authority.params.AuthorityApiDel;
import org.hutrace.handy.authority.params.AuthorityApiPut;
import org.hutrace.handy.authority.params.AuthorityModuleAdd;
import org.hutrace.handy.authority.params.AuthorityModuleDel;
import org.hutrace.handy.authority.params.AuthorityModulePut;
import org.hutrace.handy.authority.params.AuthorityModuleSequencePatch;
import org.hutrace.handy.authority.params.BufferParams;
import org.hutrace.handy.authority.service.AuthorityModuleService;
import org.hutrace.handy.exception.CustomException;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.mybatis.exception.RollbackException;

@Authority
@Controller
@RequestMapping("authority/module")
public class AuthorityModuleController {
	
	private @Autowire AuthorityModuleService service;
	
	@RequestMapping(value = "api", method = RequestMethod.POST)
	@Validated
	public AuthorityApiAdd addApi(AuthorityApiAdd params) throws InsertException {
		service.insertApi(params);
		return params;
	}
	
	@RequestMapping(value = "api", method = RequestMethod.GET)
	public List<Map<String, Object>> selectApis() throws SelectException {
		return service.selectApiAll();
	}
	
	@RequestMapping(value = "api", method = RequestMethod.PUT)
	@Validated
	public int deleteApi(AuthorityApiPut params) throws UpdateException {
		return service.updateApi(params);
	}
	
	@RequestMapping(value = "api", method = RequestMethod.DELETE)
	@Validated
	public int deleteApi(AuthorityApiDel params) throws DeleteException, UpdateException {
		return service.deleteApi(params.getId());
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Map<String, Object>> selectModules() throws SelectException {
		return service.selectModuleAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Validated
	public AuthorityModuleAdd insertModule(AuthorityModuleAdd params) throws CustomException, RollbackException {
		int type = params.getType().intValue();
		switch (type) {
			case 1:
				service.insertDirectory(params);
				break;
			case 2:
				service.insertMenu(params);
				break;
			case 3:
				service.insertPage(params);
				break;
			case 4:
				service.insertAction(params);
				break;
			default:
				throw new CustomException(ApplicationProperty.get("authority.module.error.type"));
		}
		return params;
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@Validated
	public AuthorityModulePut updateModule(AuthorityModulePut params) throws CustomException, RollbackException {
		int type = params.getType().intValue();
		switch (type) {
			case 1:
				service.updateDirectory(params);
				break;
			case 2:
				service.updateMenu(params);
				break;
			case 3:
				service.updatePage(params);
				break;
			default:
				throw new CustomException(ApplicationProperty.get("authority.module.error.type"));
		}
		return params;
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	@Validated
	public int deleteModule(AuthorityModuleDel params) throws DeleteException, UpdateException {
		return service.deleteModules(params);
	}
	
	@RequestMapping(value = "sequence", method = RequestMethod.PATCH)
	@Validated
	public int sequence(AuthorityModuleSequencePatch params) throws UpdateException {
		return service.updateModuleSequence(params);
	}

	@RequestMapping(value = "byrid", method = RequestMethod.GET)
	@Validated
	public List<Map<String, Object>> byRid(BufferParams buf) throws SelectException {
		if(buf.isAdmin()) {
			return service.selectRoleModuleAll();
		}
		return service.selectModulesByRid(buf.getBufRoleId());
	}
	
}
