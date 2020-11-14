package org.hutrace.handy.authority.impl.log;

import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ConfigurationReadException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.http.listener.ChannelListener;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.utils.qs.QueryStringObject;

import com.alibaba.fastjson.JSONObject;

import io.netty.handler.codec.http.HttpMethod;

/**
 * 因考虑到日志暂时不需要太高的效率
 * <p>当前版本为了节省内存，没有使用缓存
 * <p>后面如有需要可以扩展固定缓存，这样将大大提高查询效率
 * @author hu trace
 */
public class CompleteLogListener implements ChannelListener {
	
	static Logger logImpl;
	
	/**
	 * 储存日志的路径
	 * <p>默认当前项目下/
	 * <p>最终路径是path + folder
	 */
	static String path;
	
	/**
	 * 设置日志储存的目录
	 * <p>默认log/system/
	 * <p>最终路径是path + folder
	 */
	static String folder;
	
	/**
	 * 储存日志的文件名称
	 * <p>名称格式为name.年.月.日.log，你只需要设置name即可
	 * <p>日志系统会自动添加标识
	 * <p>默认data
	 */
	static String name;
	
	/**
	 * 日志内容编码，默认是项目编码
	 */
	static String charset = Configuration.charset();
	
	/**
	 * 需要过滤掉（不写日志）的接口列表
	 */
	private String[] filterUrl;
	
	private LogLevel logLevel = LogLevel.AUTHORITY;
	
	private RequestMethod[] logMethod = null;
	
	public void setPath(String path) {
		CompleteLogListener.path = path;
	}

	public static void setFolder(String folder) {
		CompleteLogListener.folder = folder;
	}

	public void setName(String name) {
		CompleteLogListener.name = name;
	}
	
	public static void setCharset(String charset) {
		CompleteLogListener.charset = charset;
	}

	public void setLogImpl(Logger logImpl) throws ConfigurationReadException {
		CompleteLogListener.logImpl = logImpl;
		logImpl.launch();
	}
	
	public void setFilterUrl(String[] urls) {
		this.filterUrl = urls;
		for(int i = 0; i < filterUrl.length; i++) {
			filterUrl[i] = Configuration.name() + filterUrl[i].trim();
		}
	}
	
	public void setLogLevel(String logLevel) {
		this.logLevel = LogLevel.valueOf(logLevel.toUpperCase());
	}
	
	public void setLogMethod(RequestMethod[] logMethod) {
		this.logMethod = logMethod;
	}

	private boolean canPrintForLevel(HttpRequest request) {
		switch (logLevel) {
			case ALL:
				return true;
			case AUTHORITY:
				return request.control().getAnnotation(Authority.class) != null ||
						request.mapping().getAnnotation(Authority.class) != null;
			case NOT:
			default:
				return false;
		}
	}
	
	private boolean canPrintForMethod(String method) {
		if(logMethod == null) {
			return true;
		}
		RequestMethod rm = RequestMethod.valueOf(method.toUpperCase());
		for(int i = 0; i < logMethod.length; i++) {
			if(logMethod[i] == rm) {
				return true;
			}
		}
		return false;
	}
	
	private boolean filter(String url) {
		if(filterUrl == null || filterUrl.length == 0) {
			return false;
		}
		for(int i = 0; i < filterUrl.length; i++) {
			if(filterUrl[i].equals(url)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void closed(HttpRequest request, HttpResponse response, Object parameter, Object result) {
		if(canPrintForLevel(request) && canPrintForMethod(request.method().name()) && !filter(request.url())) {
			JSONObject log = new JSONObject();
			String ct = request.contentType();
			log.put("ct", ct);
			log.put("ip", request.ip());
			if(ct == null) {
				printJSON(request, null, result, log);
			}else if(ct.indexOf("application/json") > -1) {
				printJSON(request, parameter, result, log);
			}else if(ct.indexOf("text/xml") > -1) {
				printJSON(request, parameter, result, log);
			}else if(ct.indexOf("application/x-www-form-urlencoded") > -1) {
				printJSON(request, ((QueryStringObject) parameter).innerMap(), result, log);
			}else {
				printJSON(request, null, result, log);
			}
		}
	}
	
	private void printJSON(HttpRequest request, Object parameter, Object result, JSONObject log) {
		String method = request.method().name();
		log.put("req", parameter);
		log.put("url", request.url());
		log.put("method", method);
		if(result instanceof ResultBean) {
			log.put("rsp", ((ResultBean) result).inner());
		}else {
			log.put("rsp", result);
		}
		if(!method.equals(HttpMethod.GET.name())) {
			log.put("qs", request.parameters());
		}
		logImpl.print(log.toString());
	}
	
	/**
	 * 日志登记枚举
	 * @author hu trace
	 *
	 */
	private enum LogLevel {
		
		/**
		 * 全部接口
		 */
		ALL,
		
		/**
		 * 仅仅包含权限的接口
		 */
		AUTHORITY,
		
		/**
		 * 不写入日志
		 */
		NOT
		
	}
	
}
