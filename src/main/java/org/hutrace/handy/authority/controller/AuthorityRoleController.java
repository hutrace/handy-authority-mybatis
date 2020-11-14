package org.hutrace.handy.authority.controller;

import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityRoleAdd;
import org.hutrace.handy.authority.params.AuthorityRoleDel;
import org.hutrace.handy.authority.params.AuthorityRoleModuleAdd;
import org.hutrace.handy.authority.params.AuthorityRolePut;
import org.hutrace.handy.authority.params.AuthorityRoleQuery;
import org.hutrace.handy.authority.params.AuthorityRoleRid;
import org.hutrace.handy.authority.params.AuthorityRoleUserAdd;
import org.hutrace.handy.authority.params.BufferParams;
import org.hutrace.handy.authority.service.AuthorityModuleService;
import org.hutrace.handy.authority.service.AuthorityRoleService;
import org.hutrace.handy.authority.service.AuthorityUserService;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 权限模块->角色模块的接口类
 * @author hu trace
 * @see AuthorityRoleService
 * @see AuthorityUserService
 * @see AuthorityModuleService
 */
@Authority
@Controller
@RequestMapping("authority/role")
public class AuthorityRoleController {
	
	/**
	 * 注入{@link AuthorityRoleService}业务实现类
	 */
	private @Autowire AuthorityRoleService service;
	
	/**
	 * 注入{@link AuthorityUserService}业务实现类
	 */
	private @Autowire AuthorityUserService userService;
	
	/**
	 * 注入{@link AuthorityModuleService}业务实现类
	 */
	private @Autowire AuthorityModuleService moduleService;
	
	/**
	 * 新增角色
	 * <p>接口地址: authority/role
	 * <p>请求方式: POST
	 * @param params {@link AuthorityRoleAdd}
	 * @param buf {@link BufferParams}
	 * @return 改变数据条数
	 * @throws InsertException
	 * @see {@link AuthorityRoleService#insert(AuthorityRoleAdd)}
	 */
	@RequestMapping(method = RequestMethod.POST)
	@Validated
	public int insert(AuthorityRoleAdd params, BufferParams buf) throws InsertException {
		params.setPid(buf.getBufRoleId());
		return service.insert(params);
	}

	/**
	 * 删除角色
	 * <p>删除角色会删除角色的子、子子、子子子、...角色，直到没有子角色为止
	 * <p>该接口是一个非常危险的操作接口，删除后是不可逆的
	 * <p>接口地址: authority/role
	 * <p>请求方式: DELETE
	 * @param params {@link AuthorityRoleDel}
	 * @return 改变数据条数
	 * @throws DeleteException
	 * @throws SelectException 
	 * @see {@link AuthorityRoleService#delete(AuthorityRoleDel)}
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@Validated
	public int delete(AuthorityRoleDel params) throws DeleteException, SelectException {
		return service.delete(params);
	}

	/**
	 * 分页查询角色列表
	 * <p>接口会根据当前请求的用户判断，如果是超级管理员，则是查询所有角色列表
	 * <p>否则，查询当前请求用户角色的下一级（不会查询多级）角色列表
	 * <p>接口地址: authority/role
	 * <p>请求方式: GET
	 * @param params {@link AuthorityRoleQuery}
	 * @param buf {@link BufferParams}
	 * @return 标准的queryPage响应，map中包含count（数据条数）和list（数据集合）
	 * @throws SelectException
	 * @see {@link AuthorityRoleService#query(AuthorityRoleQuery)}
	 */
	@RequestMapping(method = RequestMethod.GET)
	@Validated
	public Map<String, Object> query(AuthorityRoleQuery params, BufferParams buf) throws SelectException {
		if(!buf.isAdmin()) {
			params.setPid(buf.getBufRoleId());
		}
		return service.query(params);
	}

	/**
	 * 修改角色
	 * <p>接口地址: authority/role
	 * <p>请求方式: PUT
	 * @param params {@link AuthorityRolePut}
	 * @return 改变数据的条数
	 * @throws UpdateException
	 * @see {@link AuthorityRoleService#update(AuthorityRolePut)}
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@Validated
	public int update(AuthorityRolePut params) throws UpdateException {
		return service.update(params);
	}

	/**
	 * 查询角色下绑定的用户
	 * <p>系统根据当前请求的用户角色ID查询该角色下已经绑定的用户
	 * <p>接口地址: authority/role/user
	 * <p>请求方式: GET
	 * @param params {@link AuthorityRoleRid}
	 * @return 用户信息列表
	 * @throws SelectException
	 * @see {@link AuthorityUserService#selectUsersByRid(Integer)}
	 */
	@RequestMapping(value = "user", method = RequestMethod.GET)
	@Validated
	public List<Map<String, Object>> selectUsersByRid(AuthorityRoleRid params) throws SelectException {
		return userService.selectUsersByRid(params.getRid());
	}

	/**
	 * 保存角色下的用户
	 * <p>当前操作会验证是否是超管
	 * <p>如果不是超管会验证传入的rid是否正确，以及添加的用户列表是否是当前用户可以操作的用户
	 * <p>接口地址: authority/role/user
	 * <p>请求方式: POST
	 * @param params {@link AuthorityRoleUserAdd}
	 * @param buf {@link BufferParams}
	 * @return 改变数据的条数
	 * @throws RollbackException
	 * @throws SelectException
	 * @see {@link AuthorityRoleService#saveUsers(AuthorityRoleUserAdd, BufferParams)}
	 */
	@RequestMapping(value = "user", method = RequestMethod.POST)
	@Validated
	public int saveUsers(AuthorityRoleUserAdd params, BufferParams buf) throws RollbackException, SelectException {
		return service.saveUsers(params, buf);
	}

	/**
	 * 查询当前角色下绑定的权限
	 * <p>系统根据当前请求的用户角色ID查询该角色下已经绑定的权限
	 * <p>接口地址: authority/role/module
	 * <p>请求方式: GET
	 * @param params {@link AuthorityRoleRid}
	 * @return 模块信息列表
	 * @throws SelectException
	 * @see {@link AuthorityModuleService#selectModulesByRid(Integer)}
	 */
	@RequestMapping(value = "module", method = RequestMethod.GET)
	@Validated
	public List<Map<String, Object>> selectModulesByRid(AuthorityRoleRid params) throws SelectException {
		return moduleService.selectModulesByRid(params.getRid());
	}

	/**
	 * 保存角色下的权限
	 * <p>当前操作会验证是否是超管
	 * <p>如果不是超管会验证传入的rid是否正确，以及添加的权限列表是否是当前用户可以操作的权限
	 * <p>接口地址: authority/role/module
	 * <p>请求方式: POST
	 * @param params {@link AuthorityRoleModuleAdd}
	 * @param buf {@link BufferParams}
	 * @return 改变数据的条数
	 * @throws RollbackException
	 * @throws SelectException
	 * @see {@link AuthorityRoleService#saveModules(AuthorityRoleModuleAdd, BufferParams)}
	 */
	@RequestMapping(value = "module", method = RequestMethod.POST)
	@Validated
	public int saveModules(AuthorityRoleModuleAdd params, BufferParams buf) throws RollbackException, SelectException {
		return service.saveModules(params, buf);
	}
	
}
