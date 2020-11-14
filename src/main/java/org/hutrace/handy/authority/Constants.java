package org.hutrace.handy.authority;

public class Constants {
	
	/**
	 * 验证token时：token不存在或sessionChar错误 {@code 6001}
	 */
	public static final int TOKEN_NOTFOUND = 6001;
	/**
	 * 验证token时：token错误 {@code 6002}
	 */
	public static final int TOKEN_ERROR = 6002;
	/**
	 * 验证token时：token错误,secret有误 {@code 6003}
	 */
	public static final int SECRET_ERROR = 6003;
	/**
	 * 验证token时：token已过期 {@code 6004}
	 */
	public static final int TOKEN_EXPIRED = 6004;
	/**
	 * 验证token时：token编码错误（解密）,此状态不建议做明确提示 {@code 6005}
	 */
	public static final int TOKEN_DECODE_ERROR = 6005;
	/**
	 * 验证token时：换取token时没有该用户(appId) {@code 6006}
	 */
	public static final int APPID_NOTFOUND = 6006;
	/**
	 * 换取token时：密码有误(appSecret) {@code 6007}
	 */
	public static final int APPSECRET_ERROR = 6007;
	/**
	 * 换取token时：用户已被禁用 {@code 6008}
	 */
	public static final int USER_FORBIDDEN = 6008;
	/**
	 * 换取token时：参数错误 {@code 6009}
	 */
	public static final int PARAMETER_ERROR = 6009;
	/**
	 * 请求时：header（消息头）错误 {@code 6010}
	 */
	public static final int HEADER_ERROR = 6010;
	/**
	 * 请求时：其它未知错误
	 */
	public static final int OTHER_ERROR = 6011;
	
	/**
	 * 消息头中token的key
	 */
	public static final String HEADER_TOKEN = "Scpsat-Token";
	
	/**
	 * 消息头中签名的key
	 */
	public static final String HEADER_SIGN = "Scpsat-Sign";
	
}
