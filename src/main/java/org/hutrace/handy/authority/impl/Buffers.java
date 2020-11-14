package org.hutrace.handy.authority.impl;

import java.io.IOException;
import java.util.Objects;

import org.hutrace.handy.authority.Constants;
import org.hutrace.handy.authority.exception.SelectException;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.utils.Serialize;
import org.hutrace.handy.utils.code.AES;
import org.hutrace.handy.utils.code.ChaoticMix;
import org.hutrace.handy.utils.code.TextCoding;
import org.hutrace.handy.utils.code.Tracy62;

public class Buffers {
	
	public static final String ENCODE_SPLIT = ".";
	public static final int CHAOTIC_MIX_LEN = 1;
	
	/**
	 * 获取BufferUser
	 * @param id
	 * @return {@link BufferUser}
	 */
	public static BufferUser getUser(Long id) {
		try {
			return Serialize.deserializeByFile(AuthorityLoader.usersPath + id);
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * <p>删除系统buffer的user，用于踢掉线等操作
	 * @param id
	 */
	public static void removeUser(Long id) {
		Serialize.remove(AuthorityLoader.usersPath + id);
	}
	
	/**
	 * 缓存Buffer信息
	 * @param id
	 * @param value {@link BufferUser}
	 * @throws IOException 
	 */
	public static void setUser(Long id, BufferUser value) throws IOException {
		Serialize.toFile(value, AuthorityLoader.usersPath + id);
	}
	
	/**
	 * 获取BufferAuthority
	 * @param id
	 * @return {@link BufferAuthority}
	 */
	public static BufferAuthority getAuthority(Long id) {
		try {
			return Serialize.deserializeByFile(AuthorityLoader.authorityPath + id);
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * <p>删除系统buffer的权限
	 * @param id
	 */
	public static void removeAuthority(Long id) {
		Serialize.remove(AuthorityLoader.authorityPath + id);
	}
	
	/**
	 * 缓存BufferAuthority信息
	 * @param id
	 * @param value {@link BufferAuthority}
	 * @throws IOException 
	 */
	public static void setAuthority(Long id, BufferAuthority value) throws IOException {
		Serialize.toFile(value, AuthorityLoader.authorityPath + id);
	}
	
	/**
	 * <p>创建token
	 * <pre>
	 * 以secret作为密钥
	 * 以WyString(Tracy62(id) + "." + secret + "." + Tracy62(time))作为加密文本
	 * 进行AES对称加密
	 * @param id 用户固定唯一会话字符串
	 * @param value {@link BufferValue}
	 * @return 返回token字符串
	 * @throws Exception
	 */
	public static String buildToken(Long id, String secret, BufferUser value) throws Exception {
		long timer = System.currentTimeMillis();
		value.setTimer(timer);
		Tracy62 idT62 = Tracy62.build(id);
		Tracy62 timerT62 = Tracy62.build(timer);
		String content = TextCoding.strSplice(
				idT62.toString(), ENCODE_SPLIT,
				secret, ENCODE_SPLIT,
				timerT62.toString(), ENCODE_SPLIT);
		setUser(id, value);
		return AES.encode(secret, ChaoticMix.build(content, CHAOTIC_MIX_LEN).toString());
	}
	
	/**
	 * <p>验证Token
	 * @param id {@link Long} 会话字符串
	 * @param token {@link String} token
	 * @return secret密码
	 * @throws SelectException 
	 */
	public static BufferUser checkToken(long id, String token) throws SelectException {
		BufferUser value = getUser(id);
		if(null == value) {
			// token不存在
			throw new SelectException(Constants.TOKEN_NOTFOUND, ApplicationProperty.get("authority.login.notfount.token"));
		}
		String[] contents;
		String secret = ChaoticMix.build(value.getSignKey()).string();
		try {
			contents = ChaoticMix.build(AES.dncode(secret, token), CHAOTIC_MIX_LEN).string().split("\\.");
		}catch (Exception e) {
			throw new SelectException(Constants.TOKEN_DECODE_ERROR, ApplicationProperty.get("authority.login.error.token"), e);
		}
		if(null == contents || contents.length != 3) {
			// token错误
			throw new SelectException(Constants.TOKEN_ERROR, ApplicationProperty.get("authority.login.error.token"));
		}
		long idT62 = Tracy62.build(contents[0]).toLong();
		if(idT62 != id) {
			// token错误
			throw new SelectException(Constants.TOKEN_ERROR, ApplicationProperty.get("authority.login.error.token"));
		}
		// 根据目前的规则，下面注释的这段代码是多余的
		if(!Objects.equals(contents[1], secret)) {
			// token错误
			throw new SelectException(Constants.SECRET_ERROR, ApplicationProperty.get("authority.login.error.token"));
		}
		long timerT62 = Tracy62.build(contents[2]).toLong();
		if(System.currentTimeMillis() - timerT62 >= AuthorityLoader.tokenEffectiveTime) {
			// token已过期
			throw new SelectException(Constants.TOKEN_EXPIRED, ApplicationProperty.get("authority.login.expired.token"));
		}
		return value;
	}
	
}
