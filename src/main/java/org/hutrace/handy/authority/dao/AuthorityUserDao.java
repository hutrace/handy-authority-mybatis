package org.hutrace.handy.authority.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityUserEnabled;
import org.hutrace.handy.authority.params.AuthorityUserGet;

@DAO
public class AuthorityUserDao {
	
	private @Autowire AuthorityMyBatisDao dao;
	
	private String mapper(String id) {
		return "mapper.authority.AuthorityUserMapper." + id;
	}
	
	public int updateEnabled(AuthorityUserEnabled params) throws UpdateException {
		return dao.update(mapper("updateEnabled"), params);
	}
	
	public Map<String, Object> query(AuthorityUserGet params) throws SelectException {
		return dao.queryPage(mapper("query"), params);
	}
	
	public List<Map<String, Object>> selectNotRoleByRid(Integer rid) throws SelectException {
		Map<String, Object> params = null;
		if(rid != null) {
			params = new HashMap<>();
			params.put("rid", rid);
		}
		return dao.selectList(mapper("selectNotRoleByRid"), params);
	}
	
	public List<Map<String, Object>> selectUsersByRid(Integer rid) throws SelectException {
		return dao.selectList(mapper("selectUsersByRid"), rid);
	}
	
	public List<Long> selectNotRoleUidsByRid(Integer rid) throws SelectException {
		return dao.selectList(mapper("selectNotRoleUidsByRid"), rid);
	}
	
}
