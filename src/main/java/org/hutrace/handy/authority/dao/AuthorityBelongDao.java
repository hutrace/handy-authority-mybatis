package org.hutrace.handy.authority.dao;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.params.AuthorityBelongAdd;
import org.hutrace.handy.authority.params.AuthorityBelongDel;

@DAO
public class AuthorityBelongDao {
	
	private @Autowire AuthorityMyBatisDao dao;
	
	private String mapper(String id) {
		return "mapper.authority.AuthorityBelongMapper." + id;
	}
	
	public int insert(AuthorityBelongAdd params) throws InsertException {
		return dao.insert(mapper("insert"), params);
	}
	
	public int delete(AuthorityBelongDel params) throws DeleteException {
		return dao.delete(mapper("delete"), params);
	}
	
	public List<Map<String, Object>> selectAll() throws SelectException {
		return dao.selectList(mapper("selectAll"));
	}
	
}
