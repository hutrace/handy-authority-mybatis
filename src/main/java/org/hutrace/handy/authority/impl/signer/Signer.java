package org.hutrace.handy.authority.impl.signer;

import org.hutrace.handy.authority.impl.BufferUser;
import org.hutrace.handy.http.HttpRequest;

/**
 * 验签签名者接口
 * @author hu trace
 */
public interface Signer {
	
	/**
	 * 开始签名
	 */
	String sign(HttpRequest request, BufferUser buf) throws Exception;
	
	/**
	 * 获取匹配当前签名器的内容类型
	 * @return
	 */
	String contentType();
	
}
