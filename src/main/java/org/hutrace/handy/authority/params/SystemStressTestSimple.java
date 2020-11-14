package org.hutrace.handy.authority.params;

import java.util.Date;

import org.hutrace.handy.annotation.verify.Number;

import org.hutrace.handy.annotation.verify.NotBlank;

public class SystemStressTestSimple {
	
	@NotBlank
	private String data;
	
	private Date time;
	
	@Number
	private Integer rspstrlen;
	
	public SystemStressTestSimple() {
		time = new Date();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getRspstrlen() {
		return rspstrlen;
	}

	public void setRspstrlen(Integer rspstrlen) {
		this.rspstrlen = rspstrlen;
	}
	
}
