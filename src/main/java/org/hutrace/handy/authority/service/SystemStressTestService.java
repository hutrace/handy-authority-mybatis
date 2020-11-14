package org.hutrace.handy.authority.service;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.authority.dao.SystemStressTestDao;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.params.SystemStressTestSimple;

@Service
public class SystemStressTestService {
	
	private @Autowire SystemStressTestDao dao;
	
	public int insertSimple(SystemStressTestSimple params) throws InsertException {
		return dao.insertSimple(params);
	}
	
}
