package org.hutrace.handy.authority.impl;

import java.io.Serializable;

import org.hutrace.handy.utils.code.TextCoding;

public class BufferAuthority implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String module;
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
	/**
	 * <p>根据请求的ID判断当前账号是否存在访问权限
	 * @param id
	 * @return {@link Boolean} true:有权限，false:没有权限
	 */
	public boolean hasId(int id) {
		if(null == module || TextCoding.EMPTY_STR.equals(module)) {
			return false;
		}
		String[] arr = module.split(",");
		if(arr.length == 0) {
			return false;
		}
		for(int i = 0; i < arr.length; i++) {
			if(Integer.parseInt(arr[i]) == id) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>根据请求的ID判断当前账号是否存在访问权限
	 * @param id
	 * @return {@link Boolean} true:有权限，false:没有权限
	 */
	public boolean hasId(int... ids) {
		if(ids.length == 0) {
			return hasId(ids[0]);
		}
		if(null == module || TextCoding.EMPTY_STR.equals(module)) {
			return false;
		}
		String[] arr = module.split(",");
		if(arr.length == 0) {
			return false;
		}
		int c;
		for(int i = 0; i < arr.length; i++) {
			c = Integer.parseInt(arr[i]);
			for(int j = 0; j < ids.length; j++) {
				if(c == ids[j]) {
					return true;
				}
			}
		}
		return false;
	}
	
}
