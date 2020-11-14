package org.hutrace.handy.authority.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityRoleAdd;
import org.hutrace.handy.authority.params.AuthorityRoleDel;
import org.hutrace.handy.authority.params.AuthorityRoleModuleAdd;
import org.hutrace.handy.authority.params.AuthorityRolePut;
import org.hutrace.handy.authority.params.AuthorityRoleQuery;
import org.hutrace.handy.authority.params.AuthorityRoleUserAdd;

@DAO
public class AuthorityRoleDao {
	
	private @Autowire AuthorityMyBatisDao dao;
	
	private String mapper(String id) {
		return "mapper.authority.AuthorityRoleMapper." + id;
	}
	
	public int insert(AuthorityRoleAdd params) throws InsertException {
		return dao.insert(mapper("insert"), params);
	}
	
	public int deletes(AuthorityRoleDel params) throws DeleteException {
		return dao.delete(mapper("deletes"), params);
	}
	
	public int delUsersByRid(AuthorityRoleDel params) throws DeleteException {
		return dao.delete(mapper("delUsersByRid"), params);
	}
	
	public int delModulesByRid(AuthorityRoleDel params) throws DeleteException {
		return dao.delete(mapper("delModulesByRid"), params);
	}
	
	public int delUserByUid(Integer uid) throws DeleteException {
		List<Integer> uids = new ArrayList<>();
		uids.add(uid);
		return delUserByUids(uids);
	}
	
	public int delUserByUids(List<Integer> uids) throws DeleteException {
		return dao.delete(mapper("delUserByUids"), uids);
	}
	
	public int delModuleByMid(Integer mid) throws DeleteException {
		List<Integer> mids = new ArrayList<>();
		mids.add(mid);
		return delModuleByMids(mids);
	}
	
	public int delModuleByMids(List<Integer> mids) throws DeleteException {
		return dao.delete(mapper("delModuleByMids"), mids);
	}
	
	public List<Integer> selectByPid(Integer pid) throws SelectException {
		return dao.selectList(mapper("selectByPid"), pid);
	}
	
	public Map<String, Object> query(AuthorityRoleQuery params) throws SelectException {
		return dao.queryPage(mapper("query"), params);
	}
	
	public int update(AuthorityRolePut params) throws UpdateException {
		return dao.update(mapper("update"), params);
	}
	
	public int delUsersByRidAndUids(AuthorityRoleUserAdd params) throws DeleteException {
		return dao.delete(mapper("delUsersByRidAndUids"), params);
	}
	
	public int addUsersByRidAndUids(AuthorityRoleUserAdd params) throws InsertException {
		return dao.insert(mapper("addUsersByRidAndUids"), params);
	}
	
	public int delModulesByRidAndUids(AuthorityRoleModuleAdd params) throws DeleteException {
		return dao.delete(mapper("delModulesByRidAndUids"), params);
	}
	
	public int addModulesByRidAndUids(AuthorityRoleModuleAdd params) throws InsertException {
		return dao.insert(mapper("addModulesByRidAndUids"), params);
	}
	
	public int selectCheckRid(Integer rid, Integer prid) throws SelectException {
		AuthorityRoleQuery params = new AuthorityRoleQuery();
		params.setId(rid);
		params.setPid(prid);
		return dao.selectInt(mapper("queryCount"), params);
	}
	
}
