package org.hutrace.handy.authority.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.authority.dao.AuthorityModuleDao;
import org.hutrace.handy.authority.dao.AuthorityRoleDao;
import org.hutrace.handy.authority.dao.AuthorityUserDao;
import org.hutrace.handy.authority.exception.DeleteException;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.params.AuthorityRoleAdd;
import org.hutrace.handy.authority.params.AuthorityRoleDel;
import org.hutrace.handy.authority.params.AuthorityRoleModuleAdd;
import org.hutrace.handy.authority.params.AuthorityRolePut;
import org.hutrace.handy.authority.params.AuthorityRoleQuery;
import org.hutrace.handy.authority.params.AuthorityRoleUserAdd;
import org.hutrace.handy.authority.params.BufferParams;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 角色管理业务实现类
 * @author hu trace
 */
@Service
public class AuthorityRoleService {
	
	/**
	 * 注入角色管理{@link AuthorityRoleDao}持久层
	 */
	private @Autowire AuthorityRoleDao dao;
	
	/**
	 * 注入用户管理{@link AuthorityUserDao}持久层
	 */
	private @Autowire AuthorityUserDao userDao;
	
	/**
	 * 注入模块管理{@link AuthorityModuleDao}持久层
	 */
	private @Autowire AuthorityModuleDao moduleDao;
	
	/**
	 * 新增角色
	 * @param params {@link AuthorityRoleAdd}
	 * @return 改变数据的条数
	 * @throws InsertException
	 * @see {@link AuthorityRoleDao#insert(AuthorityRoleAdd)}
	 */
	public int insert(AuthorityRoleAdd params) throws InsertException {
		return dao.insert(params);
	}
	
	/**
	 * 删除角色
	 * <p>删除角色会删除角色的子、子子、子子子、...角色，直到没有子角色为止
	 * <p>创建包含传入角色ID的Integer集合
	 * <p>调用{@link #selectDeleteAllIds(Integer, List)}来获取到所有需要删除的角色ID
	 * <p>然后依次批量删除角色信息、角色用户绑定、角色权限绑定
	 * @param params {@link AuthorityRoleDel}
	 * @return 改变数据的条数
	 * @throws DeleteException
	 * @throws SelectException
	 * @see {@link #selectDeleteAllIds(Integer, List)}
	 * @see {@link AuthorityRoleDao#deletes(AuthorityRoleDel)}
	 * @see {@link AuthorityRoleDao#delUsersByRid(AuthorityRoleDel)}
	 * @see {@link AuthorityRoleDao#delModulesByRid(AuthorityRoleDel)}
	 */
	public int delete(AuthorityRoleDel params) throws DeleteException, SelectException {
		// 先查询出所有子级
		List<Integer> ids = new ArrayList<>();
		ids.add(params.getId());
		selectDeleteAllIds(params.getId(), ids);
		params.setIds(ids);
		int count = dao.deletes(params);
		count += dao.delUsersByRid(params);
		count += dao.delModulesByRid(params);
		return count;
	}
	
	/**
	 * 递归获取角色ID下所有子、子子、子子子...的角色ID
	 * @param pid 角色ID
	 * @param ids 储存获取到的角色ID的集合
	 * @throws SelectException
	 * @see {@link AuthorityRoleDao#selectByPid(Integer)}
	 */
	private void selectDeleteAllIds(Integer pid, List<Integer> ids) throws SelectException {
		List<Integer> list = dao.selectByPid(pid);
		if(list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				ids.add(list.get(i));
				selectDeleteAllIds(list.get(i), ids);
			}
		}
	}
	
	/**
	 * 分页查询角色列表
	 * @param params {@link AuthorityRoleQuery}
	 * @return 标准的分页查询响应map
	 * @throws SelectException
	 * @see {@link AuthorityRoleDao#query(AuthorityRoleQuery)}
	 */
	public Map<String, Object> query(AuthorityRoleQuery params) throws SelectException {
		return dao.query(params);
	}
	
	/**
	 * 修改角色信息
	 * @param params {@link AuthorityRolePut}
	 * @return 改变数据的条数
	 * @throws UpdateException
	 * @see {@link AuthorityRoleDao#update(AuthorityRolePut)}
	 */
	public int update(AuthorityRolePut params) throws UpdateException {
		return dao.update(params);
	}
	
	/**
	 * 保存角色下的用户
	 * <p>使用{@link #checkSaveUsers(AuthorityRoleUserAdd, BufferParams)}验证参数是否正确
	 * <p>根据{@link AuthorityRoleUserAdd#getAdds()}和{@link AuthorityRoleUserAdd#getDels()}新增和删除
	 * @param params {@link AuthorityRoleUserAdd}
	 * @param buf {@link BufferParams}
	 * @return 改变数据的条数
	 * @throws RollbackException
	 * @throws SelectException
	 * @see {@link #checkSaveUsers(AuthorityRoleUserAdd, BufferParams)}
	 * @see {@link AuthorityRoleDao#addUsersByRidAndUids(AuthorityRoleUserAdd)}
	 * @see {@link AuthorityRoleDao#delUsersByRidAndUids(AuthorityRoleUserAdd)}
	 */
	public int saveUsers(AuthorityRoleUserAdd params, BufferParams buf) throws RollbackException, SelectException {
		checkSaveUsers(params, buf);
		int count = 0;
		if(params.getAdds() != null && params.getAdds().size() > 0) {
			count += dao.addUsersByRidAndUids(params);
		}
		if(params.getDels() != null && params.getDels().size() > 0) {
			count += dao.delUsersByRidAndUids(params);
		}
		return count;
	}
	
	/**
	 * 保存角色下的模块（权限）
	 * <p>使用{@link #checkSaveModules(AuthorityRoleModuleAdd, BufferParams)}验证参数是否正确
	 * <p>根据{@link AuthorityRoleModuleAdd#getAdds()}和{@link AuthorityRoleModuleAdd#getDels()}新增和删除
	 * @param params {@link AuthorityRoleModuleAdd}
	 * @param buf {@link BufferParams}
	 * @return 改变数据的条数
	 * @throws RollbackException
	 * @throws SelectException
	 * @see {@link #checkSaveModules(AuthorityRoleModuleAdd, BufferParams)}
	 * @see {@link AuthorityRoleDao#addModulesByRidAndUids(AuthorityRoleModuleAdd)}
	 * @see {@link AuthorityRoleDao#delModulesByRidAndUids(AuthorityRoleModuleAdd)}
	 */
	public int saveModules(AuthorityRoleModuleAdd params, BufferParams buf) throws RollbackException, SelectException {
		checkSaveModules(params, buf);
		int count = 0;
		if(params.getAdds() != null && params.getAdds().size() > 0) {
			count += dao.addModulesByRidAndUids(params);
		}
		if(params.getDels() != null && params.getDels().size() > 0) {
			count += dao.delModulesByRidAndUids(params);
		}
		return count;
	}
	
	/**
	 * 验证保存角色用户的参数是否正确
	 * <p>如果是超管，则跳过验证
	 * <p>调用{@link AuthorityUserDao#selectNotRoleUidsByRid(Integer)}查询出当前角色可操作的用户
	 * <p>根据传入的用户列表与查询出的用户列表匹配进行验证
	 * @param params {@link AuthorityRoleUserAdd}
	 * @param buf {@link BufferParams}
	 * @throws SelectException
	 * @see {@link AuthorityUserDao#selectNotRoleUidsByRid(Integer)}
	 */
	private void checkSaveUsers(AuthorityRoleUserAdd params, BufferParams buf) throws SelectException {
		if(!buf.isAdmin()) {
			checkRid(params.getRid(), buf.getBufRoleId());
			if(params.getAdds() != null && params.getAdds().size() > 0) {
				List<Long> list = userDao.selectNotRoleUidsByRid(buf.getBufRoleId());
				boolean flag;
				long sid;
				for(Long id : params.getAdds()) {
					flag = false;
					sid = id.longValue();
					for(int i = 0; i < list.size(); i++) {
						if(sid == list.get(i).longValue()) {
							flag = true;
							break;
						}
					}
					if(!flag) {
						throw new SelectException(ApplicationProperty.get("authority.role.user.param.adds"));
					}
				}
			}
		}
	}
	
	/**
	 * 验证保存角色权限的参数是否正确
	 * <p>如果是超管，则跳过验证
	 * <p>调用{@link AuthorityModuleDao#selectModuleIdsByRid(Integer)}查询出当前角色可操作的模块
	 * <p>根据传入的模块列表与查询出的模块列表匹配进行验证
	 * @param params {@link AuthorityRoleModuleAdd}
	 * @param buf {@link BufferParams}
	 * @throws SelectException
	 * @see {@link AuthorityModuleDao#selectModuleIdsByRid(Integer)}
	 */
	private void checkSaveModules(AuthorityRoleModuleAdd params, BufferParams buf) throws SelectException {
		if(!buf.isAdmin()) {
			checkRid(params.getRid(), buf.getBufRoleId());
			if(params.getAdds() != null && params.getAdds().size() > 0) {
				List<Integer> list = moduleDao.selectModuleIdsByRid(buf.getBufRoleId());
				boolean flag;
				int sid;
				for(Integer id : params.getAdds()) {
					flag = false;
					sid = id.intValue();
					for(int i = 0; i < list.size(); i++) {
						if(sid == list.get(i).intValue()) {
							flag = true;
							break;
						}
					}
					if(!flag) {
						throw new SelectException(ApplicationProperty.get("authority.role.module.param.adds"));
					}
				}
			}
		}
	}
	
	/**
	 * 验证传入的角色ID是否正确
	 * @param rid 传入需要操作的角色ID
	 * @param prid 当前登陆用户的角色ID
	 * @throws SelectException
	 * @see {@link AuthorityRoleDao#selectCheckRid(Integer, Integer)}
	 */
	private void checkRid(Integer rid, Integer prid) throws SelectException {
		int count = dao.selectCheckRid(rid, prid);
		if(count != 1) {
			throw new SelectException(ApplicationProperty.get("authority.role.param.rid"));
		}
	}
	
}
