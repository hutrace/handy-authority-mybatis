package org.hutrace.handy.authority.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.authority.Constants;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.impl.signer.Signer;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.HandyserveException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.ResponseCode;
import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.http.interceptor.HttpMsgsInterceptor;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.http.result.ResultHandler;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.server.HttpHandler;

import io.netty.handler.codec.http.HttpMethod;

/**
 * 权限拦截器实现
 * <p>使用该拦截器需要和{@link AuthorityLoader}加载器一起使用
 * <p>拦截成功后，拦截器会优先使用设置的{@link ResultHandler}来处理数据
 * @author hu trace
 */
public class AuthorityInterceptor implements HttpMsgsInterceptor {
	
	private Logger log = LoggerFactory.getLogger(AuthorityInterceptor.class);
	
	/**
	 * 设置接收的用户id标识符（QueryString参数名称）
	 * <p>该值只在权限拦截器中使用，拦截器会将此参数转换为clientId，以便使用
	 */
	private static String qsParamId = "clientId";
	
	/**
	 * 验证签名的签名器列表
	 */
	private List<Signer> verifySigner;
	
	public void setQsParamId(String qsParamId) {
		AuthorityInterceptor.qsParamId = qsParamId;
	}
	
	static String getQsParamId() {
		return qsParamId;
	}
	
	public void setVerifySigner(List<Signer> verifySigner) {
		this.verifySigner = verifySigner;
	}

	@Override
	public boolean request(HttpRequest request, HttpResponse response) throws Exception {
		request.ip();
		try {
			Authority authority = authority(request);
			if(authority != null) {
				request1(request, response);
			}
			return true;
		}catch(HandyserveException e) {
			if(Configuration.resultHandler() != null) {
				ResultBean rb = Configuration.resultHandler().dispose(request, response,
						new ExceptionBean(e, e.code(), e.getMessage()));
				response.setContentType(rb.contentType());
				response.write(rb);
			}else {
				response.setContentType(HttpHandler.DEFAULT_RSP_CONTENT_TYPE);
				response.write(e.getMessage());
			}
			response.flush();
			response.close();
			return false;
		}catch (Exception e) {
			if(Configuration.resultHandler() != null) {
				ResultBean rb = Configuration.resultHandler().dispose(request, response,
						new ExceptionBean(e, ResponseCode.ERROR_CUSTOM, e.getMessage()));
				response.setContentType(rb.contentType());
				response.write(rb);
			}else {
				response.setContentType(HttpHandler.DEFAULT_RSP_CONTENT_TYPE);
				response.write(e.getMessage());
			}
			response.flush();
			response.close();
			return false;
		}
	}
	
	/**
	 * 获取当前请求接口对应的方法/类上的{@link Authority}注解
	 * @param request
	 * @return
	 */
	private Authority authority(HttpRequest request) {
		Method method = request.mapping();
		Authority authority = method.getAnnotation(Authority.class);
		if(authority != null) {
			return authority;
		}
		return request.control().getAnnotation(Authority.class);
	}
	
	/**
	 * 开始执行拦截验证
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void request1(HttpRequest request, HttpResponse response) throws Exception {
		String url = url(request);
		long clientId = clientId(request);
		String token = request.header(Constants.HEADER_TOKEN);
		BufferUser buf = buffer(clientId, token);
		checkSign(request, buf);
		checkAuthority(clientId, url, request.method().toString());
		bufMergeParams(request, buf.getCustom());
	}
	
	/**
	 * 获取url地址
	 * @param request
	 * @return
	 */
	private String url(HttpRequest request) {
		String project = Configuration.name();
		String url = request.url();
		if(url.substring(0, project.length()).equals(project)) {
			url = url.substring(project.length());
		}
		return url;
	}
	
	/**
	 * 获取clientId
	 * @param request
	 * @return
	 * @throws HandyserveException
	 */
	private long clientId(HttpRequest request) throws HandyserveException {
		String cid = request.parameter(qsParamId);
		if(cid == null) {
			throw new HandyserveException(Constants.HEADER_ERROR,
					ApplicationProperty.get("authority.req.null.id", qsParamId));
		}
		request.setParameter("clientId", cid);
		try {
			return Long.parseLong(cid);
		}catch (Exception e) {
			throw new HandyserveException(Constants.HEADER_ERROR,
					ApplicationProperty.get("authority.req.error.id", qsParamId));
		}
	}
	
	/**
	 * 获取buffer并验证token
	 * @param clientId
	 * @param token
	 * @return
	 * @throws HandyserveException
	 */
	private BufferUser buffer(Long clientId, String token) throws HandyserveException {
		if(token == null || token.isEmpty()) {
			throw new HandyserveException(Constants.HEADER_ERROR,
					ApplicationProperty.get("authority.req.null.token", Constants.HEADER_TOKEN));
		}
		return Buffers.checkToken(clientId, token);
	}
	
	/**
	 * 合并buffer中的数据值当前请求{@link HttpRequest}中
	 * @param request
	 * @param buf
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void bufMergeParams(HttpRequest request, Map<String, Object> buf) {
		if(buf != null) {
			Object obj;
			for(Entry<String, Object> entry : buf.entrySet()) {
				obj = entry.getValue();
				if(obj != null) {
					if(obj instanceof Map) {
						bufMergeParams(request, (Map) obj);
					}else {
						request.setParameter(entry.getKey(), obj.toString());
					}
				}
			}
		}
	}
	
	/**
	 * 验证签名
	 * <p>验证签名根据{@link verifySigner}设置的顺序判断，如果有重复的contentType，则第一个生效。
	 * @param request
	 * @param buf
	 * @throws Exception 
	 */
	private void checkSign(HttpRequest request, BufferUser buf) throws Exception {
		// 如果是get请求，则不验证签名
		if(!request.method().name().equals(HttpMethod.GET.name())) {
			String sign = request.header(Constants.HEADER_SIGN);
			if(sign == null) {
				throw new HandyserveException(Constants.HEADER_ERROR,
						ApplicationProperty.get("authority.req.null.sign", Constants.HEADER_SIGN));
			}
			String ct = request.contentType().split(";")[0].trim();
			for(Signer signer : verifySigner) {
				if(signer.contentType().equals(ct)) {
					String sign1 = signer.sign(request, buf);
					log.debug("authority.req.sign.info", sign, signer.getClass().getName(), sign1);
					if(sign.equals(sign1)) {
						return;
					}
					throw new HandyserveException(Constants.HEADER_ERROR, ApplicationProperty.get("authority.req.fail.sign"));
				}
			}
			throw new HandyserveException(Constants.HEADER_ERROR,
					ApplicationProperty.get("authority.req.notfound.signer", ct));
		}
	}
	
	/**
	 * 验证权限
	 * @param id
	 * @param api
	 * @param method
	 * @throws Exception
	 */
	private void checkAuthority(long id, String api, String method) throws Exception {
		int[] mids = CacheApis.get(api, method);
		if(mids.length == 0) {
			throw new Exception(ApplicationProperty.get("authority.req.notfound.api"));
		}
		BufferAuthority ba = Buffers.getAuthority(id);
		if(!ba.hasId(mids)) {
			throw new Exception(ApplicationProperty.get("authority.req.notauth.api"));
		}
	}
	
}
