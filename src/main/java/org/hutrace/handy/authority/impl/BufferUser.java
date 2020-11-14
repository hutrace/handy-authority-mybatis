package org.hutrace.handy.authority.impl;

import java.io.Serializable;
import java.util.Map;

/**
 * 缓存Token验证的信息Value实体类
 * @author hu trace
 */
public class BufferUser implements Serializable {
	
	private static final long serialVersionUID = 8624037694938430435L;
	
	private String secret;
	private String signKey;
	private long timer;
	private Map<String, Object> custom;
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getSignKey() {
		return signKey;
	}
	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
	public long getTimer() {
		return timer;
	}
	public void setTimer(long timer) {
		this.timer = timer;
	}
	public Map<String, Object> getCustom() {
		return custom;
	}
	public void setCustom(Map<String, Object> custom) {
		this.custom = custom;
	}
	
}
