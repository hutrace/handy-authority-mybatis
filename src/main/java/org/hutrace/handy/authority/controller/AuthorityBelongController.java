package org.hutrace.handy.authority.controller;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.params.AuthorityBelongAdd;
import org.hutrace.handy.authority.params.AuthorityBelongDel;
import org.hutrace.handy.authority.service.AuthorityBelongService;
import org.hutrace.handy.http.RequestMethod;

@Authority
@Controller
@RequestMapping("authority/belong")
public class AuthorityBelongController {
	
	private @Autowire AuthorityBelongService service;

	@RequestMapping(method = RequestMethod.POST, produces = "application/scpsat-json")
	public int insert(AuthorityBelongAdd params) throws InsertException {
		return service.insert(params);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public int delete(AuthorityBelongDel params) throws DeleteException {
		return service.delete(params);
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/scpsat-json")
	public List<Map<String, Object>> selectAll() throws SelectException {
		return service.selectAll();
	}
	
}
