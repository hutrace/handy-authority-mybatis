package org.hutrace.handy.authority.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.authority.controller.SystemDownloadController;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.file.HttpDownload;
import org.hutrace.handy.http.result.JSONResultBean;
import org.hutrace.handy.http.result.JSONResultHandler;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.utils.code.TextCoding;

/**
 * 权限认证的文件下载处理器
 * <p>使用此处理器你可以在前端（主要是浏览器）
 * <p>继承{@link JSONResultHandler}处理器
 * <p>此处理器只处理响应类型为{@link AuthorityDownload}
 * <p>其余的调用父类处理器处理
 * <pre>
 *  该处理器在处理的时候
 *  先生成唯一键(key)，然后与{@link AuthorityDownload}数据绑定存入{@link HashMap}
 *  (在处理HashMap缓存时都加了锁)
 *  然后提供静态方法供外部调用，简单封装在{@link AuthorityDownload}类中
 *  使用{@link AuthorityDownload#take(String)}即可获取
 *  
 *  与之配合使用的还有Controller接口{@link SystemDownloadController#download(String)}方法
 *  该方法接口地址为: /system/download
 *  如果在不使用默认封装的情况下，需要注意{@link #delay}的值，该值是{@link AuthorityDownload}可在HashMap中存在的最长时间
 *  如果超过此时间没有使用，会自动清除
 * @author hu trace
 */
public class AuthorityDownloadJSONResultHandler extends JSONResultHandler {
	
	/**
	 * {@link AuthorityDownload}可以在缓存HashMap中储存的最长时间，单位毫秒
	 * <p>此时间不建议过长，通常流程耗时不会太多
	 * <p>当网络非常慢或并发很高的时候，你可以调整此值的大小，通常10000毫秒已经足以
	 */
	private static long delay = 10000;
	
	private static ClearThread thread;
	
	private static Map<String, AuthorityDownload> cache;
	
	public AuthorityDownloadJSONResultHandler() {
		cache = new HashMap<>();
	}
	
	/**
	 * 设置有效缓存时间，超过此时间的下载文件将失效
	 * @param delay
	 */
	public static void setDelay(long delay) {
		AuthorityDownloadJSONResultHandler.delay = delay;
	}

	/**
	 * 处理类型为{@link AuthorityDownload}的响应
	 * <p>将其写入缓存，并将key响应至客户端，客户端通过此key即可下载文件
	 * <p>其它数据类型交由{@link JSONResultHandler#dispose(HttpRequest, HttpResponse, Object)}处理
	 */
	@Override
	public ResultBean dispose(HttpRequest request, HttpResponse response, Object msg) {
		if(msg instanceof AuthorityDownload) {
			String key = TextCoding.createLowerRule32();
			ResultBean rb = new JSONResultBean();
			rb.setCode(0);
			rb.setMsg("ok");
			rb.setData(key);
			synchronized (cache) {
				cache.put(key, (AuthorityDownload) msg);
				if(thread == null) {
					thread = new ClearThread();
					thread.start();
				}
			}
			return rb;
		}
		return super.dispose(request, response, msg);
	}
	
	static HttpDownload take(String key) {
		AuthorityDownload download = cache.remove(key);
		if(download == null) {
			return null;
		}else {
			return download.getDownload();
		}
	}
	
	private static class ClearThread extends Thread {
		
		private boolean flag = true;
		
		@Override
		public void run() {
			long now;
			while(flag) {
				try {
					sleep(1);
				}catch (InterruptedException e) {}
				now = System.currentTimeMillis();
				synchronized(cache) {
					Iterator<Entry<String, AuthorityDownload>> iter = cache.entrySet().iterator();
					// 防止在take方法里面删除后此处再次删除，使用try catch捕获，但不错捕获处理
					try {
						while(iter.hasNext()) {
							if(now - iter.next().getValue().getTime() >= delay) {
								iter.remove();
							}
						}
					}catch (Exception e) {}
					if(cache.size() == 0) {
						flag = false;
						thread = null;
					}
				}
			}
		}
		
	}

}
