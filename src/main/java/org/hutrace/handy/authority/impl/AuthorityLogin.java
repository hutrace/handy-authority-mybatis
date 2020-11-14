package org.hutrace.handy.authority.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;
import org.hutrace.handy.authority.bean.AuthorityApiVO;
import org.hutrace.handy.authority.bean.AuthorityLoginHistoryBean;
import org.hutrace.handy.authority.bean.AuthorityUserBean;
import org.hutrace.handy.authority.exception.InsertException;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.authority.exception.UpdateException;
import org.hutrace.handy.authority.impl.code.PasswordEncrypt;
import org.hutrace.handy.authority.params.BufferParams;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.mybatis.DefaultSqlSeesion;
import org.hutrace.handy.mybatis.PondSqlFactory;
import org.hutrace.handy.mybatis.exception.RollbackException;
import org.hutrace.handy.utils.code.ChaoticMix;
import org.hutrace.handy.utils.code.TextCoding;

public abstract class AuthorityLogin {
	
	/**
	 * 获取超级管理员的id
	 * @return 超级管理员的id
	 */
	public static long adminUserId() {
		return AuthorityLoader.adminUserId;
	}
	
	private SqlSession sql;
	
	private boolean needClose;
	
	public AuthorityLogin() {
		sql = PondSqlFactory.getSqlSessions();
		needClose = false;
		if(sql == null) {
			sql = DefaultSqlSeesion.get();
			needClose = true;
		}
	}
	
	public SqlSession sql() {
		return sql;
	}
	
	public void rollback() {
		if(needClose) {
			sql.rollback();
		}
	}
	
	public void commit() {
		if(needClose) {
			sql.commit();
		}
	}
	
	public void close() {
		if(needClose) {
			sql.close();
		}
	}
	
	protected String mapper(String id) {
		return "mapper.authority.AuthorityLoginMapper." + id;
	}
	
	/**
	 * 创建用户
	 * @param user
	 * @param buf
	 * @return
	 * @throws InsertException
	 */
	public int create(AuthorityUserBean user, BufferParams buf) throws InsertException {
		user.setAddRid(buf.getBufRoleId());
		user.setAddUid(buf.getClientId());
		return create(user);
	}
	
	/**
	 * 创建用户
	 * @param user
	 * @return
	 * @throws InsertException
	 */
	public int create(AuthorityUserBean user) throws InsertException {
		checkAndPushUserBean(user);
		try {
			int num = sql.insert("mapper.authority.AuthorityUserMapper.insert", user);
			commit();
			return num;
		}catch (Throwable e) {
			rollback();
			String msg = getDuplicateKeyMsg(e.getCause());
			if(msg == null) {
				throw new InsertException(e.getMessage());
			}else {
				throw new InsertException(msg);
			}
		}finally {
			close();
		}
	}
	
	/**
	 * 修改密码
	 * <p>{@link #updatePassword(Long, String, String)}
	 * @param id
	 * @param password 密码
	 * @return
	 * @throws UpdateException
	 */
	public int updatePassword(Long id, String password) throws UpdateException {
		return updatePassword(id, password, null);
	}
	
	/**
	 * 修改密码，并处理异常，SqlSession
	 * @param id
	 * @param password 密码
	 * @param oldPassword 原始密码（为空则不验证原始密码）
	 * @return
	 * @throws UpdateException
	 */
	public int updatePassword(Long id, String password, String oldPassword) throws UpdateException {
		try {
			int count = updatePassword1(id, password, oldPassword);
			commit();
			return count;
		}catch (UpdateException e) {
			rollback();
			throw e;
		} catch (SelectException e) {
			rollback();
			throw new UpdateException(e.getMessage());
		}finally {
			close();
		}
	}
	
	/**
	 * 修改密码
	 * @param id
	 * @param password 密码
	 * @param oldPassword 原始密码（为空则不验证原始密码）
	 * @return
	 * @throws UpdateException
	 * @throws SelectException
	 */
	private int updatePassword1(Long id, String password, String oldPassword) throws UpdateException, SelectException {
		AuthorityUserBean user = selectUser(LoginType.ID, id);
		if(oldPassword != null && !oldPassword.isEmpty()) {
			try {
				if(!user.getPassword().equals(AuthorityLoader.passwordEncrypt.encrypt(oldPassword, user.getSalt()))) {
					throw new SelectException(ApplicationProperty.get("authority.update.password"));
				}
			}catch (Exception e) {
				throw new UpdateException(e.getMessage());
			}
		}
		try {
			user.setMix(AuthorityLoader.passwordMix.mix(password));
			user.setPassword(AuthorityLoader.passwordEncrypt.encrypt(password, user.getSalt()));
		}catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
		try {
			return sql.update(mapper("updatePassword"), user);
		}catch (Throwable e) {
			throw new UpdateException(e.getMessage());
		}
	}
	
	/**
	 * 解析异常是否是因为重复导致的异常
	 * <p>如果是重复导致的异常，则解析出字段和值
	 * <p>使用语言包抛出信息
	 * @param e
	 * @return
	 */
	private String getDuplicateKeyMsg(Throwable e) {
		if(e == null) {
			return null;
		}
		String msg = e.getMessage();
		String value = null;
		String key = null;
		int index = 0;
		if(msg.indexOf("Duplicate entry") > -1) {
			Pattern p = Pattern.compile("'(.*?)'");
			Matcher m = p.matcher(msg);
			while(m.find() && index < 2) {
				if(index == 0) {
					value = m.group();
				}else if(index == 1) {
					key = m.group().replace("'", "");
				}
				index ++;
			}
		}
		if(key == null) {
			return null;
		}
		if(key.equals("username")) {
			return ApplicationProperty.get("authority.create.repeat.username", value);
		}else if(key.equals("phone")) {
			return ApplicationProperty.get("authority.create.repeat.phone", value);
		}else if(key.equals("email")) {
			return ApplicationProperty.get("authority.create.repeat.email", value);
		}else {
			return null;
		}
	}
	
	/**
	 * 校验传入的user数据是否完善
	 * <p>生成user需要的其它系统数据
	 * @param user
	 * @throws InsertException
	 */
	private void checkAndPushUserBean(AuthorityUserBean user) throws InsertException {
		if(user.getUsername() == null || user.getPassword() == null
				|| user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
			throw new InsertException(ApplicationProperty.get("authority.create.null"));
		}
		if(user.getNickname() == null || user.getNickname().isEmpty()) {
			user.setNickname(user.getUsername());
		}
		user.setCreateTime(new Date());
		user.setEnabled(1);
		user.setMix(AuthorityLoader.passwordMix.mix(user.getPassword()));
		user.setSalt(AuthorityLoader.salt.create());
		try {
			user.setPassword(AuthorityLoader.passwordEncrypt.encrypt(user.getPassword(), user.getSalt()));
		}catch (Exception e) {
			throw new InsertException(e.getMessage());
		}
		user.setInviteCode(AuthorityLoader.inviteCode.create());
		user.setSecret(TextCoding.createLowerRule32());
		user.setBelong("back");
	}
	
	/**
	 * 用户登陆方法
	 * <p>该方法通过LoginType判断根据什么值查询数据
	 * @param type 根据什么值查询数据
	 * @param arg0 (id/username/phone/email)的值
	 * @param arg1 (secret/password...)需要与arg0匹配登陆的值
	 * @return 
	 * @throws SelectException 
	 * @throws RollbackException 
	 */
	public Map<String, Object> login(HttpRequest request, LoginType type,
			String arg0, String arg1) throws SelectException, RollbackException {
		try {
			Map<String, Object> rsp = login1(request, type, arg0, arg1);
			commit();
			return rsp;
		}catch (SelectException e) {
			commit();
			throw e;
		}catch (RollbackException e) {
			rollback();
			throw e;
		}catch (Throwable e) {
			commit();
			throw e;
		}finally {
			close();
		}
	}

	/**
	 * 用户登陆方法
	 * <p>该方法通过LoginType判断根据什么值查询数据
	 * @param type 根据什么值查询数据
	 * @param arg0 (id/username/phone/email)的值
	 * @param arg1 (secret/password...)需要与arg0匹配登陆的值
	 * @return 
	 * @throws SelectException 
	 * @throws RollbackException 
	 */
	private Map<String, Object> login1(HttpRequest request, LoginType type,
			String arg0, String arg1) throws SelectException, RollbackException {
		AuthorityUserBean user = selectUser(type, arg0);
		check(request, user, arg1);
		return buildRsp(user);
	}
	
	/**
	 * 验证登陆用户
	 * <pre>
	 *  包含
	 *    验证账号是否被临时锁的
	 *    验证账号参数是否匹配
	 *    验证账号是否被禁用
	 * </pre>
	 * <p>并对所有未通过的做登陆历史记录
	 * <p>未通过以异常的方式抛出
	 * @param request
	 * @param user 查询出的登陆账号
	 * @param arg1 调用登陆方法时的arg1参数
	 * @throws RollbackException
	 * @throws SelectException
	 */
	public void check(HttpRequest request, AuthorityUserBean user, String arg1) throws RollbackException, SelectException {
		AuthorityLoginHistoryBean history = new AuthorityLoginHistoryBean();
		history.setIp(request.ip());
		history.setUserId(user.getId());
		try {
			checkLock(user);
			checkMatch(user, arg1, AuthorityLoader.passwordEncrypt);
			checkUserEnabled(user);
			history.setStatus(0);
			insertHostiry(history);
			updateUserLoginInfo(user.getId(), history.getIp());
		}catch (SelectException e) {
			history.setStatus(e.code());
			history.setExplain(e.getMessage());
			insertHostiry(history);
			throw e;
		}catch (RollbackException e) {
			history.setStatus(e.code());
			history.setExplain(e.getMessage());
			insertHostiry(history);
			throw e;
		}
	}
	
	/**
	 * 校验账号是否因登陆错误次数过多而被锁定
	 */
	private void checkLock(AuthorityUserBean user) throws SelectException {
		Map<String, Object> params = new HashMap<>();
		params.put("time", AuthorityLoader.loginErrorLockTime);
		params.put("userId", user.getId());
		Object obj = sql.selectOne(mapper("selectLoginErrorCount"), params);
		int count = 0;
		if(obj != null) {
			count = Integer.parseInt(obj.toString());
		}
		if(count >= AuthorityLoader.loginErrorTime) {
			throw new SelectException(3, ApplicationProperty.get("authority.login.error.lock",
					AuthorityLoader.loginErrorTime + "", (AuthorityLoader.loginErrorLockTime/60) + ""));
		}
	}
	
	/**
	 * 判断验证是否通过的方法
	 * <p>例如：通过用户名和密码登陆时，你可以使用user.getPassword和arg1加密后是否匹配做判断
	 * @param user 查询出来的用户数据，走到这一步时，user必定不为null
	 * @param arg1 调用登陆方法时的arg1参数
	 * @param passwordEncrypt 验证不通过时，根据情况抛出SelectException或者RollbackException<br/>
	 * 抛出异常时应当注意，需要指定异常的code为1(详见数据库authority_login_history表status字段)
	 */
	public abstract void checkMatch(AuthorityUserBean user, String arg1,
			PasswordEncrypt passwordEncrypt) throws SelectException, RollbackException;
	
	/**
	 * 校验用户是否被禁用
	 * @param user
	 * @throws SelectException
	 */
	private void checkUserEnabled(AuthorityUserBean user) throws SelectException {
		if(user.getEnabled() == 0) {
			throw new SelectException(2, ApplicationProperty.get("authority.login.error.disabled"));
		}
	}
	
	/**
	 * 新增登陆历史记录
	 * @param history
	 */
	private void insertHostiry(AuthorityLoginHistoryBean history) {
		history.setTime(new Date());
		sql.insert(mapper("insertLoginHistory"), history);
	}
	
	/**
	 * 修改用户登陆信息
	 * @param id
	 * @param ip
	 */
	private void updateUserLoginInfo(Long id, String ip) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("loginIp", ip);
		params.put("loginTime", new Date());
		sql.update(mapper("updateUserLoginInfo"), params);
	}
	
	/**
	 * 查询登陆用户数据
	 * @param type 登陆类型，通过什么登陆
	 * @param value 查询对应的值
	 * @return 登陆用户数据
	 * @throws SelectException 
	 */
	public AuthorityUserBean selectUser(LoginType type, Object value) throws SelectException {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type.value);
		params.put("value", value);
		AuthorityUserBean user = sql.selectOne(mapper("selectOneUser"), params);
		if(user == null) {
			throw new SelectException(ApplicationProperty.get("authority.login.notfound." + type.value));
		}
		return user;
	}
	
	/**
	 * 登陆类型枚举
	 * @author hu trace
	 */
	public static enum LoginType {
		
		USERNAME("username"),
		PHONE("phone"),
		EMAIL("email"),
		ID("id");
		
		private LoginType(String v) {
			value = v;
		}
		
		private String value;
		
	}
	
	/**
	 * 构造响应数据
	 * @param user
	 * @return 
	 * @throws SelectException 
	 * @throws RollbackException 
	 */
	private Map<String, Object> buildRsp(AuthorityUserBean user) throws SelectException, RollbackException {
		BufferUser bu = buildBufferUser(user);
		String token;
		try {
			token = Buffers.buildToken(user.getId(), user.getSecret(), bu);
		}catch (Exception e) {
			Buffers.removeUser(user.getId());
			throw new SelectException(e.getMessage());
		}
		user.setSecret(bu.getSecret());
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("signKey", bu.getSignKey());
		buildRspOther(user, map);
		buildRspModule(user, map);
		return map;
	}
	
	/**
	 * 向响应数据中写值
	 * <p>参数rsp就是向客户端响应的数据
	 * <p>当前rsp中只有一个token值，你可以设置其它你需要的值
	 * @param user
	 * @param rsp
	 */
	protected abstract void buildRspOther(AuthorityUserBean user, Map<String, Object> rsp) throws SelectException, RollbackException;
	
	/**
	 * 向响应数据中添加模块信息
	 * @param user
	 * @param rsp
	 * @throws SelectException
	 * @throws RollbackException
	 */
	protected void buildRspModule(AuthorityUserBean user, Map<String, Object> rsp) throws SelectException, RollbackException {
		buildAndBufferAuthority(user);
	}
	
	/**
	 * 缓存buffer登陆用户权限信息
	 * @param user
	 * @return
	 * @throws SelectException
	 */
	protected List<AuthorityApiVO> buildAndBufferAuthority(AuthorityUserBean user) throws SelectException {
		List<AuthorityApiVO> apis = sql.selectList(mapper("selectApisByUid"), user.getId());
		BufferAuthority buf = new BufferAuthority();
		if(apis == null || apis.size() == 0) {
			buf.setModule("");
		}else {
			StringBuilder strb = new StringBuilder();
			for(int i = apis.size() - 1; i >= 0; i--) {
				strb.append(apis.get(i).getId());
				if(i != 0) {
					strb.append(",");
				}
			}
			buf.setModule(strb.toString());
		}
		try {
			Buffers.setAuthority(user.getId(), buf);
		}catch (IOException e) {
			throw new SelectException(e);
		}
		return apis;
	}
	
	/**
	 * 构造用户登陆缓存
	 * <p>保证多用户可同时登陆，如果不需要多用户同时登陆，修改此方法判断，改为每次new BufferUser就可以了
	 * <p>当前规则，当本次登陆距离上次登陆超出了token有效期后，就会修改secret，否则每次取上次的secret写入。
	 * @param user
	 * @return
	 */
	private BufferUser buildBufferUser(AuthorityUserBean user) {
		BufferUser bv = Buffers.getUser(user.getId());
		if(bv == null) {
			bv = new BufferUser();
			bv.setSignKey(ChaoticMix.build(user.getSecret()).toString());
			bv.setSecret(TextCoding.createLowerRule32());
		}else {
			if(System.currentTimeMillis() - bv.getTimer() >= AuthorityLoader.tokenEffectiveTime) {
				bv = new BufferUser();
				bv.setSignKey(ChaoticMix.build(user.getSecret()).toString());
				bv.setSecret(TextCoding.createLowerRule32());
			}
		}
		bv.setCustom(buildExtendBufferUser(user));
		return bv;
	}
	
	/**
	 * 编辑用户缓存的扩展值，你可以把它理解为写入session
	 * @param user
	 * @return map中写入值可以直接通过mapping方法上接口而取得
	 */
	protected abstract Map<String, Object> buildExtendBufferUser(AuthorityUserBean user);
	
}
