package org.hutrace.handy.authority.dao;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.params.SystemStressTestSimple;

@DAO
public class SystemStressTestDao {
	
	private @Autowire AuthorityMyBatisDao dao;
	
	private String mapper(String id) {
		return "mapper.authority.SystemStressTestMapper." + id;
	}
	
	public int insertSimple(SystemStressTestSimple params) throws InsertException {
		return dao.insert(mapper("insertSimple"), params);
	}
	
}
