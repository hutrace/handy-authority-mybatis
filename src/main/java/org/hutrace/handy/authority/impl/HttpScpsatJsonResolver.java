package org.hutrace.handy.authority.impl;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.converter.HttpJsonMsgsConverter;
import org.hutrace.handy.http.converter.HttpMsgsConverter;
import org.hutrace.handy.http.resolver.HttpMsgsResolver;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.language.SystemProperty;
import org.hutrace.handy.utils.TypeUtils;
import org.hutrace.handy.utils.code.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.netty.handler.codec.http.HttpMethod;

public class HttpScpsatJsonResolver implements HttpMsgsResolver {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String[] CONTENT_TYPE = {
		"application/scpsat-json"
	};
	
	@Override
	public Object parse(HttpRequest request) throws ResolverException {
		if(request.method().name().equals(HttpMethod.GET.name())) {
			// GET请求不支持加解密
			throw new ResolverException(ApplicationProperty.get("authority.resolver.scpsat.nonsupport"));
		}
		Long clientId = Long.parseLong(request.parameter(AuthorityInterceptor.getQsParamId()));
		BufferUser bu = Buffers.getUser(clientId);
		String str;
		try {
			str = AES.dncode(bu.getSecret(), request.body(), Configuration.charset());
		}catch (Exception e) {
			throw new ResolverException(ApplicationProperty.get("authority.resolver.scpsat.decode", e.getMessage()));
		}
		JSONObject json = JSONObject.parseObject(str);
		return json;
	}

	@Override
	public String[] getContentType() {
		return CONTENT_TYPE;
	}

	@Override
	public HttpMsgsConverter converter() {
		return HttpJsonMsgsConverter.instance;
	}
	
	@Override
	public Object write(HttpRequest request, HttpResponse response, Object msg) throws ResolverException {
		Long clientId = Long.parseLong(request.parameter("clientId"));
		BufferUser bu = Buffers.getUser(clientId);
		String secret = bu.getSecret();
		try {
			String encodeStr;
			if(TypeUtils.simpleType(msg.getClass())) {
				encodeStr = AES.encode(secret, msg.toString(), Configuration.charset());
			}else {
				encodeStr = AES.encode(secret, JSONObject.toJSONString(msg), Configuration.charset());
			}
			response.setContentType(CONTENT_TYPE[0]);
			return encodeStr;
		}catch (Exception e) {
			log.error(SystemProperty.get("authority.resolver.scpsat.encode"), e);
			return msg;
		}
	}
	
}
