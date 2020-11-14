package org.hutrace.handy.authority.service;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.authority.dao.AuthorityModuleDao;
import org.hutrace.handy.authority.dao.AuthorityRoleDao;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityApiAdd;
import org.hutrace.handy.authority.params.AuthorityApiPut;
import org.hutrace.handy.authority.params.AuthorityModuleAdd;
import org.hutrace.handy.authority.params.AuthorityModuleDel;
import org.hutrace.handy.authority.params.AuthorityModulePut;
import org.hutrace.handy.authority.params.AuthorityModuleSequencePatch;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.mybatis.exception.RollbackException;

@Service
public class AuthorityModuleService {
	
	private @Autowire AuthorityModuleDao dao;
	
	private @Autowire AuthorityRoleDao roleDao;
	
	public int insertApi(AuthorityApiAdd params) throws InsertException {
		return dao.insertApi(params);
	}
	
	public List<Map<String, Object>> selectApiAll() throws SelectException {
		return dao.selectApiAll();
	}
	
	public int updateApi(AuthorityApiPut params) throws UpdateException {
		return dao.updateApi(params);
	}
	
	public int deleteApi(int id) throws DeleteException, UpdateException {
		int count = roleDao.delModuleByMid(id);
		count += dao.deleteModuleByApiId(id);
		count += dao.deleteApi(id);
		return count;
	}
	
	public int insertDirectory(AuthorityModuleAdd params) throws InsertException {
		return dao.insertModule(params);
	}
	
	public int insertMenu(AuthorityModuleAdd params) throws InsertException {
		if(params.getSequence() == null) {
			throw new InsertException(ApplicationProperty.get("authority.module.error.sequence"));
		}
		return dao.insertModule(params);
	}
	
	public int insertPage(AuthorityModuleAdd params) throws InsertException {
		if(params.getSequence() == null) {
			throw new InsertException(ApplicationProperty.get("authority.module.error.sequence"));
		}
		if(params.getPage() == null) {
			throw new InsertException(ApplicationProperty.get("authority.module.error.page"));
		}
		return dao.insertModule(params);
	}
	
	public int insertAction(AuthorityModuleAdd params) throws RollbackException {
		int count = 0;
		if(params.getAid() != null && params.getAid() != 0) {
			count += dao.insertModule(params);
			count += dao.updateApiQuotePushOne(params.getAid());
		}else if(params.getApi() != null && params.getMethod() != null) {
			AuthorityApiAdd apiAdd = new AuthorityApiAdd();
			apiAdd.setApi(params.getApi());
			apiAdd.setMethod(params.getMethod());
			apiAdd.setQuote(1);
			count += dao.insertApi(apiAdd);
			params.setAid(apiAdd.getId());
			count += dao.insertModule(params);
		}
		return count;
	}
	
	public int updateDirectory(AuthorityModulePut params) throws UpdateException {
		return dao.updateModule(params);
	}
	
	public int updateMenu(AuthorityModulePut params) throws UpdateException {
		return dao.updateModule(params);
	}
	
	public int updatePage(AuthorityModulePut params) throws UpdateException {
		if(params.getPage() == null) {
			throw new UpdateException(ApplicationProperty.get("authority.module.error.page"));
		}
		return dao.updateModule(params);
	}
	
	public List<Map<String, Object>> selectModuleAll() throws SelectException {
		return dao.selectModuleAll();
	}
	
	public int deleteModules(AuthorityModuleDel params) throws DeleteException, UpdateException {
		int count = dao.deleteModules(params);
		count += roleDao.delModuleByMids(params.getIds());
		if(params.getApiQuotes() != null && params.getApiQuotes().size() > 0) {
			count += dao.updateApiQuote(params);
		}
		return count;
	}
	
	public int updateModuleSequence(AuthorityModuleSequencePatch params) throws UpdateException {
		return dao.updateModuleSequence(params);
	}
	
	public List<Map<String, Object>> selectModulesByRid(Integer rid) throws SelectException {
		return dao.selectModulesByRid(rid);
	}
	
	public List<Map<String, Object>> selectRoleModuleAll() throws SelectException {
		return dao.selectRoleModuleAll();
	}
	
}
