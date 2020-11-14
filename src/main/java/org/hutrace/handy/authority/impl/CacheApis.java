package org.hutrace.handy.authority.impl;

import java.util.Iterator;
import java.util.List;

import org.hutrace.handy.authority.bean.AuthorityApiCacheVO;

/**
 * api缓存类
 * <p>缓存所有api，并提供相应的方法
 * @author hu trace
 */
public class CacheApis {
	
	private static List<AuthorityApiCacheVO> apis;
	
	/**
	 * 初始化apis，在程序启动时执行一次即可
	 * @param apis
	 */
	static void set(List<AuthorityApiCacheVO> apis) {
		CacheApis.apis = apis;
	}
	
	/**
	 * 向缓存中添加一条api记录
	 * @param id
	 * @param api
	 * @param method
	 */
	public static void add(Integer id, String api, String method) {
		AuthorityApiCacheVO apivo = new AuthorityApiCacheVO();
		apivo.setId(id);
		apivo.setApi(api);
		apivo.setMethod(method);
		add(apivo);
	}
	
	/**
	 * 向缓存中添加一条api记录
	 * @param api
	 */
	public static void add(AuthorityApiCacheVO api) {
		apis.add(api);
	}
	
	/**
	 * 根据id删除一条缓存记录
	 * @param id
	 */
	public static void remove(int id) {
		for(int i = 0; i < apis.size(); i++) {
			if(apis.get(i).getId().intValue() == id) {
				apis.remove(i);
				break;
			}
		}
	}
	
	/**
	 * 根据id删除一条缓存记录
	 * @param id
	 */
	public static void removes(List<Integer> ids) {
		for(int i = apis.size() - 1; i >= 0; i--) {
			if(ids.indexOf(apis.get(i).getId()) > -1) {
				apis.remove(i);
			}
		}
	}
	
	/**
	 * 删除多条缓存记录
	 * @param ids
	 */
	public static void remove(int... ids) {
		Iterator<AuthorityApiCacheVO> iterator = apis.iterator();
		int id;
		while(iterator.hasNext()) {
			id = iterator.next().getId().intValue();
			for(int i = 0; i < ids.length; i ++) {
				if(ids[i] == id) {
					iterator.remove();
					break;
				}
			}
		}
	}
	
	/**
	 * 根据api和method获取id
	 * @param api
	 * @param method
	 * @return
	 */
	public static int[] get(String api, String method) {
		AuthorityApiCacheVO apivo;
		int[] result = new int[1024];
		int size = 0;
		for(int i = 0; i < apis.size(); i++) {
			apivo = apis.get(i);
			if(apivo.getApi().equals(api) && apivo.getMethod().equals(method)) {
				result[size ++] = apivo.getId().intValue();
			}
		}
		int[] newResult = new int[size];
		for(int i = 0; i < newResult.length; i++) {
			newResult[i] = result[i];
		}
		return newResult;
	}
	
}
