package org.hutrace.handy.authority.impl.code;

/**
 * 密码加密接口
 * <p>所有密码加密都通过此接口调用
 * <p>你可以自定义实现它
 * @author hu trace
 */
public interface PasswordEncrypt {
	
	/**
	 * 通过password和salt加密
	 * @param password 密码
	 * @param salt 盐
	 * @return 加密字符串
	 */
	String encrypt(String password, String salt) throws Exception;
	
}
