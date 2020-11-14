package org.hutrace.handy.authority.service;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.authority.dao.AuthorityUserDao;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityUserEnabled;
import org.hutrace.handy.authority.params.AuthorityUserGet;

@Service
public class AuthorityUserService {
	
	private @Autowire AuthorityUserDao dao;
	
	public Map<String, Object> query(AuthorityUserGet params) throws SelectException {
		return dao.query(params);
	}
	
	public int updateEnabled(AuthorityUserEnabled params) throws UpdateException {
		return dao.updateEnabled(params);
	}
	
	public List<Map<String, Object>> selectUsersByRid(Integer rid) throws SelectException {
		return dao.selectUsersByRid(rid);
	}
	
	public List<Map<String, Object>> selectNotRoleByRid(Integer rid) throws SelectException {
		return dao.selectNotRoleByRid(rid);
	}
	
}
