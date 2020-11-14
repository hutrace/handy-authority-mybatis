package org.hutrace.handy.authority.impl.code;

import org.hutrace.handy.utils.code.MD5;

/**
 * 使用MD5加密密码
 * <p>使用"."拼接密码与盐
 * @author hu trace
 */
public class MD5PasswordEncrypt implements PasswordEncrypt {

	@Override
	public String encrypt(String password, String salt) throws Exception {
		return MD5.lowerCase(password + "." + salt);
	}

}
