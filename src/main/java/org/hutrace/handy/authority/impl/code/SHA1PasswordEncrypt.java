package org.hutrace.handy.authority.impl.code;

import org.hutrace.handy.utils.code.SHA;

/**
 * 使用SHA1加密密码
 * <p>使用"."拼接密码与盐
 * @author hu trace
 */
public class SHA1PasswordEncrypt implements PasswordEncrypt {

	@Override
	public String encrypt(String password, String salt) throws Exception {
		return SHA.sha1(password + "." + salt);
	}

}
