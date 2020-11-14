package org.hutrace.handy.authority.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hutrace.handy.authority.bean.AuthorityUserBean;
import org.hutrace.handy.authority.exception.FileConflictException;
import org.hutrace.handy.authority.exception.FileNotFoundException;
import org.hutrace.handy.authority.impl.code.ChaoticMixPasswordMix;
import org.hutrace.handy.authority.impl.code.InviteCode;
import org.hutrace.handy.authority.impl.code.PasswordEncrypt;
import org.hutrace.handy.authority.impl.code.PasswordMix;
import org.hutrace.handy.authority.impl.code.SHA256PasswordEncrypt;
import org.hutrace.handy.authority.impl.code.Salt;
import org.hutrace.handy.authority.impl.code.TextCodingSalt;
import org.hutrace.handy.authority.impl.code.Tracy62InviteCode;
import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.language.LanguageLoader;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.language.SystemLanguage;
import org.hutrace.handy.language.SystemProperty;
import org.hutrace.handy.loader.Loader;
import org.hutrace.handy.mybatis.DefaultSqlSeesion;
import org.hutrace.handy.utils.code.TextCoding;

/**
 * authority模块加载器
 * <p>使用该加载器需要和{@link AuthorityInterceptor}拦截器一起使用
 * @author hu trace
 *
 */
public class AuthorityLoader implements Loader {
	
//	public static final long ADMIN_USER_ID = 888888;
	
	private Logger log = LoggerFactory.getLogger(AuthorityLoader.class);
	
	private Map<SystemLanguage, String> language;
	
	private String[] appLanguages;
	
	/**
	 * 超级管理员的id
	 */
	static long adminUserId = 888888;
	
	public static boolean queryUserNeedAddUid;
	
	/**
	 * 超级管理员账号
	 */
	private String username;
	
	/**
	 * 超级管理员密码
	 */
	private String password;
	
	/**
	 * 创建盐的接口
	 */
	static Salt salt;
	
	/**
	 * 密码加密的接口
	 */
	static PasswordEncrypt passwordEncrypt;
	
	/**
	 * 密码混淆的接口
	 */
	static PasswordMix passwordMix;
	
	/**
	 * 创建邀请码的接口
	 */
	static InviteCode inviteCode;
	
	/**
	 * 登陆时允许错误次数
	 */
	static int loginErrorTime = 5;
	
	/**
	 * 登陆时达到错误次数后需要锁定账号多长时间
	 * <p>单位秒
	 */
	static int loginErrorLockTime = 600;
	
	/**
	 * 储存用户缓存信息的路径
	 */
	static String usersPath;
	
	/**
	 * 储存用户权限缓存的路径
	 */
	static String authorityPath;
	
	/**
	 * 储存缓存的根文件夹名称
	 */
	private String rootFloder = "serializer";
	
	/**
	 * 储存用户缓存的文件夹名称
	 */
	private String usersFloder = "user";
	
	/**
	 * 储存用户权限的文件夹名称
	 */
	private String authorityFloder = "authority";
	
	/**
	 * token的有效时间，单位毫秒
	 */
	static long tokenEffectiveTime = 7200000;
	
	public AuthorityLoader() {
		String className = AuthorityLoader.class.getName().replace("impl.AuthorityLoader", "").replace(".", "/");
		language = new HashMap<>();
		language.put(SystemLanguage.ZH_CN, className + "language/SYSTEM.zh-CN.txt");
		language.put(SystemLanguage.EN_US, className + "language/SYSTEM.en-US.txt");
		appLanguages = new String[] {
				className + "language/APP.zh-CN.txt",
				className + "language/APP.en-US.txt"
		};
		username = "admin";
		salt = new TextCodingSalt();
		passwordEncrypt = new SHA256PasswordEncrypt();
		passwordMix = new ChaoticMixPasswordMix();
		inviteCode = new Tracy62InviteCode();
	}
	
	public void setAdminUserId(long adminUserId) {
		AuthorityLoader.adminUserId = adminUserId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSalt(Salt salt) {
		AuthorityLoader.salt = salt;
	}
	
	public void setQueryUserNeedAddUid(boolean queryUserNeedAddUid) {
		AuthorityLoader.queryUserNeedAddUid = queryUserNeedAddUid;
	}
	
	public void setPasswordEncrypt(PasswordEncrypt passwordEncrypt) {
		AuthorityLoader.passwordEncrypt = passwordEncrypt;
	}

	public void setPasswordMix(PasswordMix passwordMix) {
		AuthorityLoader.passwordMix = passwordMix;
	}

	public void setInviteCode(InviteCode inviteCode) {
		AuthorityLoader.inviteCode = inviteCode;
	}

	public void setLoginErrorTime(int loginErrorTime) {
		AuthorityLoader.loginErrorTime = loginErrorTime;
	}

	public void setLoginErrorLockTime(int loginErrorLockTime) {
		AuthorityLoader.loginErrorLockTime = loginErrorLockTime;
	}

	public void setUsersPath(String usersPath) {
		AuthorityLoader.usersPath = usersPath;
	}

	public void setAuthorityPath(String authorityPath) {
		AuthorityLoader.authorityPath = authorityPath;
	}

	public void setRootFloder(String rootFloder) {
		this.rootFloder = rootFloder;
	}

	public void setUsersFloder(String usersFloder) {
		this.usersFloder = usersFloder;
	}

	public void setAuthorityFloder(String authorityFloder) {
		this.authorityFloder = authorityFloder;
	}
	
	public void setTokenEffectiveTime(long tokenEffectiveTime) {
		AuthorityLoader.tokenEffectiveTime = tokenEffectiveTime;
	}

	@Override
	public void execute() throws AppLoaderException {
		extendSystemLanguage();
		extendAppLanguage();
		createTable();
		initUsersPath();
		initAuthorityPath();
	}
	
	/**
	 * 扩展系统语言包
	 * @throws AppLoaderException
	 */
	private void extendSystemLanguage() throws AppLoaderException {
		SystemLanguage sl = LanguageLoader.instance.getSystemLanguage();
		LanguageLoader.instance.extendSystem(language.get(sl));
	}
	
	/**
	 * 扩展程序语言包
	 * @throws AppLoaderException
	 */
	private void extendAppLanguage() throws AppLoaderException {
		for(int i = 0; i < appLanguages.length; i++) {
			LanguageLoader.instance.extendApp(appLanguages[i]);
		}
	}
	
	/**
	 * 检查并创建所有表
	 * @throws AppLoaderException 
	 */
	private void createTable() throws AppLoaderException {
		SqlSession sql = DefaultSqlSeesion.get();
		try {
			createTable(sql, "authority_user_belong", "mapper.authority.AuthorityTablesMapper.createAuthorityUserBelong");
			createTable(sql, "authority_user", "mapper.authority.AuthorityTablesMapper.createAuthorityUser");
			createTable(sql, "authority_login_history", "mapper.authority.AuthorityTablesMapper.createAuthorityLoginHistory");
			createTable(sql, "authority_api", "mapper.authority.AuthorityTablesMapper.createAuthorityApi");
			createTable(sql, "authority_module", "mapper.authority.AuthorityTablesMapper.createAuthorityModule");
			createTable(sql, "authority_role", "mapper.authority.AuthorityTablesMapper.createAuthorityRole");
			createTable(sql, "authority_role_user", "mapper.authority.AuthorityTablesMapper.createAuthorityRoleUser");
			createTable(sql, "authority_role_module", "mapper.authority.AuthorityTablesMapper.createAuthorityRoleModule");
			createTable(sql, "system_stress_test_simple", "mapper.authority.AuthorityTablesMapper.createSimpleTest");
			CacheApis.set(sql.selectList("mapper.authority.AuthorityTablesMapper.selectCacheApis"));
			sql.commit();
		}catch (Exception e) {
			sql.rollback();
//			sql.delete("mapper.authority.AuthorityTablesMapper.clearTables");
			throw new AppLoaderException(e);
		}finally {
			sql.close();
		}
	}
	
	/**
	 * 检查并创建单个表
	 * @param sql
	 * @param tableName
	 * @param mapper
	 * @throws AppLoaderException 
	 */
	private void createTable(SqlSession sql, String tableName, String mapper) throws AppLoaderException {
		Object obj = sql.selectOne("mapper.authority.AuthorityTablesMapper.showTable", tableName);
		if(obj == null || obj.toString().isEmpty() || !obj.toString().equals(tableName)) {
			log.debug("authority.loader.table.notexist", tableName);
			if(tableName.equals("authority_role") || tableName.equals("authority_role_user")) {
				Map<String, Object> map = new HashMap<>();
				map.put("adminId", adminUserId);
				sql.insert(mapper, map);
			}else {
				sql.insert(mapper);
			}
			if(tableName.equals("authority_user")) {
				insertUser(sql);
			}
		}else {
			log.debug("authority.loader.table.exist", tableName);
		}
	}
	
	/**
	 * 创建默认超管账号
	 * @param sql
	 * @throws AppLoaderException
	 */
	private void insertUser(SqlSession sql) throws AppLoaderException {
		initPassword();
		AuthorityUserBean user = createAuthorityUser();
		sql.insert("mapper.authority.AuthorityTablesMapper.insertAuthorityUser", user);
		log.debug("authority.loader.user", username, password);
	}
	
	/**
	 * 初始化密码
	 */
	private void initPassword() {
		if(password == null) {
			password = TextCoding.createRule12();
		}
	}
	
	/**
	 * 创建超管账号信息
	 * @return
	 * @throws AppLoaderException
	 */
	private AuthorityUserBean createAuthorityUser() throws AppLoaderException {
		AuthorityUserBean user = new AuthorityUserBean();
		user.setUsername(username);
		user.setCreateTime(new Date());
		user.setEnabled(1);
		user.setId(adminUserId);
		user.setSalt(salt.create());
		try {
			user.setPassword(passwordEncrypt.encrypt(password, user.getSalt()));
		}catch (Exception e) {
			throw new AppLoaderException(e);
		}
		user.setMix(passwordMix.mix(password));
		user.setInviteCode(inviteCode.create());
		user.setNickname(username);
		user.setSecret(TextCoding.createLowerRule32());
		user.setBelong("back");
		return user;
	}
	
	/**
	 * 初始化用户缓存目录
	 * @throws AppLoaderException
	 */
	private void initUsersPath() throws AppLoaderException {
		if(null == usersPath) {
			StringBuilder realPath = new StringBuilder(System.getProperty("user.dir"));
			if(realPath.charAt(realPath.length() - 1) != File.separatorChar) {
				realPath.append(File.separator);
			}
			realPath.append(rootFloder);
			File file = new File(realPath.toString());
			if(file.exists()) {
				if(!file.isDirectory()) {
					//文件冲突
					throw new FileConflictException(SystemProperty.get("authority.loader.conflict.root", realPath.toString()));
				}
			}else {
				file.mkdir();
			}
			realPath.append(File.separator);
			realPath.append(usersFloder);
			usersPath = realPath.toString();
			File bufferValue = new File(usersPath);
			if(bufferValue.exists()) {
				if(!file.isDirectory()) {
					//文件冲突
					throw new FileConflictException(SystemProperty.get("authority.loader.conflict.user", usersPath));
				}
			}else {
				bufferValue.mkdir();
			}
		}else {
			File file = new File(usersPath);
			if(!file.exists()) {
				throw new FileNotFoundException(SystemProperty.get("authority.loader.notfound.user", usersPath));
			}else {
				if(!file.isDirectory()) {
					throw new FileNotFoundException(SystemProperty.get("authority.loader.notfloder.user", usersPath));
				}
			}
		}
		if(usersPath.charAt(usersPath.length() - 1) != File.separatorChar) {
			usersPath += File.separator;
		}
		log.info("authority.loader.path.user", usersPath);
	}
	
	/**
	 * 初始化权限缓存目录
	 * @throws AppLoaderException
	 */
	private void initAuthorityPath() throws AppLoaderException {
		if(null == authorityPath) {
			StringBuilder realPath = new StringBuilder(System.getProperty("user.dir"));
			if(realPath.charAt(realPath.length() - 1) != File.separatorChar) {
				realPath.append(File.separator);
			}
			realPath.append(rootFloder);
			File file = new File(realPath.toString());
			if(file.exists()) {
				if(!file.isDirectory()) {
					//文件冲突
					throw new FileConflictException(SystemProperty.get("authority.loader.conflict.root", realPath.toString()));
				}
			}else {
				file.mkdir();
			}
			realPath.append(File.separator);
			realPath.append(authorityFloder);
			authorityPath = realPath.toString();
			File bufferValue = new File(authorityPath);
			if(bufferValue.exists()) {
				if(!file.isDirectory()) {
					//文件冲突
					throw new FileConflictException(SystemProperty.get("authority.loader.conflict.authority", authorityPath));
				}
			}else {
				bufferValue.mkdir();
			}
		}else {
			File file = new File(authorityPath);
			if(!file.exists()) {
				throw new FileNotFoundException(SystemProperty.get("authority.loader.notfound.authority", authorityPath));
			}else {
				if(!file.isDirectory()) {
					throw new FileNotFoundException(SystemProperty.get("authority.loader.notfloder.authority", authorityPath));
				}
			}
		}
		if(authorityPath.charAt(authorityPath.length() - 1) != File.separatorChar) {
			authorityPath += File.separator;
		}
		log.info("authority.loader.path.authority", authorityPath);
	}
	
}
