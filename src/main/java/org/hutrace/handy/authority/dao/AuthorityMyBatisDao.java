package org.hutrace.handy.authority.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.mybatis.MyBatisDao;

public class AuthorityMyBatisDao extends MyBatisDao {
	
	// TODO 还需要解析异常，处理异常
	
	@Override
	public <T> T selectOne(String statement) throws SelectException {
		return selectOne(statement, null);
	}
	
	@Override
	public <T> T selectOne(String statement, Object parameter) throws SelectException {
		try {
			return super.selectOne(statement, parameter);
		}catch (Throwable e) {
			throw new SelectException(e);
		}
	}
	
	@Override
	public <T> List<T> selectList(String statement) throws SelectException {
		return selectList(statement, null);
	}
	
	@Override
	public <T> List<T> selectList(String statement, Object parameter) throws SelectException {
		try {
			return super.selectList(statement, parameter);
		}catch (Throwable e) {
			throw new SelectException(e);
		}
	}
	
	@Override
	public int selectInt(String statement) throws SelectException {
		return selectInt(statement, null);
	}
	
	@Override
	public int selectInt(String statement, Object parameter) throws SelectException {
		Object obj = selectOne(statement, parameter);
		if(obj == null) {
			return 0;
		}
		return Integer.parseInt(obj.toString());
	}
	
	@Override
	public long selectLong(String statement) throws SelectException {
		return selectLong(statement, null);
	}

	@Override
	public long selectLong(String statement, Object parameter) throws SelectException {
		Object obj = selectOne(statement, parameter);
		if(obj == null) {
			return 0;
		}else {
			return Long.parseLong(obj.toString());
		}
	}

	@Override
	public String selectString(String statement) throws SelectException {
		return selectString(statement, null);
	}

	@Override
	public String selectString(String statement, Object parameter) throws SelectException {
		Object obj = selectOne(statement, parameter);
		return obj == null ? null : obj.toString();
	}
	
	@Override
	public int update(String statement) throws UpdateException {
		return update(statement, null);
	}
	
	@Override
	public int update(String statement, Object parameter) throws UpdateException {
		try {
			return super.update(statement, parameter);
		}catch (Throwable e) {
			throw new UpdateException(e);
		}
	}
	
	@Override
	public int delete(String statement) throws DeleteException {
		return delete(statement, null);
	}
	
	@Override
	public int delete(String statement, Object parameter) throws DeleteException {
		try {
			return super.delete(statement, parameter);
		}catch (Throwable e) {
			throw new DeleteException(e);
		}
	}
	
	@Override
	public int insert(String statement) throws InsertException {
		return insert(statement, null);
	}
	
	@Override
	public int insert(String statement, Object parameter) throws InsertException {
		try {
			return super.insert(statement, parameter);
		}catch (Throwable e) {
			throw new InsertException(e);
		}
	}
	
	@Override
	public <T> Map<String, Object> queryPage(String statement, Object parameter) throws SelectException {
		int count = selectInt(statement + "Count", parameter);
		List<T> list = selectList(statement, parameter);
		Map<String, Object> map = new HashMap<>();
		map.put("count", count);
		map.put("list", list);
		return map;
	}
	
}
