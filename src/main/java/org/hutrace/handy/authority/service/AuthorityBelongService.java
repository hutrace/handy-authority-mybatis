package org.hutrace.handy.authority.service;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.authority.dao.AuthorityBelongDao;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.params.AuthorityBelongAdd;
import org.hutrace.handy.authority.params.AuthorityBelongDel;

@Service
public class AuthorityBelongService {
	
	private @Autowire AuthorityBelongDao dao;
	
	public int insert(AuthorityBelongAdd params) throws InsertException {
		return dao.insert(params);
	}
	
	public int delete(AuthorityBelongDel params) throws DeleteException {
		return dao.delete(params);
	}
	
	public List<Map<String, Object>> selectAll() throws SelectException {
		return dao.selectAll();
	}
	
}
