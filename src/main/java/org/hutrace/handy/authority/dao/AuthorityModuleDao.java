package org.hutrace.handy.authority.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.impl.CacheApis;
import org.hutrace.handy.authority.params.AuthorityApiAdd;
import org.hutrace.handy.authority.params.AuthorityApiPut;
import org.hutrace.handy.authority.params.AuthorityModuleAdd;
import org.hutrace.handy.authority.params.AuthorityModuleDel;
import org.hutrace.handy.authority.params.AuthorityModulePut;
import org.hutrace.handy.authority.params.AuthorityModuleSequencePatch;

@DAO
public class AuthorityModuleDao {
	
	private @Autowire AuthorityMyBatisDao dao;
	
	private String mapper(String id) {
		return "mapper.authority.AuthorityModuleMapper." + id;
	}
	
	public int insertApi(AuthorityApiAdd params) throws InsertException {
		int count = dao.insert(mapper("insertApi"), params);
		return count;
	}
	
	public List<Map<String, Object>> selectApiAll() throws SelectException {
		return dao.selectList(mapper("selectApiAll"));
	}
	
	public int updateApi(AuthorityApiPut params) throws UpdateException {
		return dao.update(mapper("updateApi"), params);
	}
	
	public int deleteApi(int id) throws DeleteException {
		return dao.delete(mapper("deleteApi"), id);
	}
	
	public int deleteModuleByApiId(int aid) throws UpdateException {
		return dao.update(mapper("deleteModuleByApiId"), aid);
	}
	
	public int updateApiQuotePushOne(int id) throws UpdateException {
		return dao.update(mapper("updateApiQuotePushOne"), id);
	}
	
	public int insertModule(AuthorityModuleAdd params) throws InsertException {
		int count = dao.insert(mapper("insertModule"), params);
		if(params.getType() == 4) {
			CacheApis.add(params.getId(), params.getApi(), params.getMethod());
		}
		return count;
	}
	
	public List<Map<String, Object>> selectModuleAll() throws SelectException {
		return dao.selectList(mapper("selectModuleAll"));
	}
	
	public int deleteModules(AuthorityModuleDel params) throws DeleteException {
		CacheApis.removes(params.getIds());
		return dao.delete(mapper("deleteModules"), params);
	}
	
	public int updateApiQuote(AuthorityModuleDel params) throws UpdateException {
		return dao.update(mapper("updateApiQuote"), params);
	}
	
	public int updateModule(AuthorityModulePut params) throws UpdateException {
		return dao.update(mapper("updateModule"), params);
	}
	
	public int updateModuleSequence(AuthorityModuleSequencePatch params) throws UpdateException {
		return dao.update(mapper("updateModuleSequence"), params);
	}
	
	public List<Map<String, Object>> selectModulesByRid(Integer rid) throws SelectException {
		Map<String, Object> map = null;
		if(rid != null && rid != 0) {
			map = new HashMap<>();
			map.put("rid", rid);
		}
		return dao.selectList(mapper("selectModulesByRid"), map);
	}
	
	public List<Map<String, Object>> selectRoleModuleAll() throws SelectException {
		return dao.selectList(mapper("selectRoleModuleAll"));
	}
	
	public List<Integer> selectModuleIdsByRid(Integer rid) throws SelectException {
		return dao.selectList(mapper("selectModuleIdsByRid"), rid);
	}
	
}
