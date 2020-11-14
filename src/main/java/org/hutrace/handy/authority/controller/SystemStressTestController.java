package org.hutrace.handy.authority.controller;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.params.SystemStressTestSimple;
import org.hutrace.handy.authority.service.SystemStressTestService;
import org.hutrace.handy.http.RequestMethod;

@Authority
@Controller
@RequestMapping("system/stress.test")
public class SystemStressTestController {
	
	private @Autowire SystemStressTestService service;
	
	@RequestMapping(value = "simple", method = RequestMethod.POST)
	@Validated
	public String simple(SystemStressTestSimple params) throws InsertException {
		service.insertSimple(params);
		return randomStr(params.getRspstrlen());
	}
	
	private char[] strs = ("abcdefghijklmnopqrstuvwxyzABCDEFGHI"
			+ "JKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*()_+-={};:'\"|\\,.<>/?").toCharArray();
	private int strsLen = strs.length;
	
	public String randomStr(int len) {
		StringBuilder strb = new StringBuilder();
		for(int i = 0; i < len; i++) {
			strb.append(strs[(int) Math.floor(Math.random() * strsLen)]);
		}
		return strb.toString();
	}
	
}
