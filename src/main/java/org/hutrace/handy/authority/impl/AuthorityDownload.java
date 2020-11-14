package org.hutrace.handy.authority.impl;

import org.hutrace.handy.http.file.HttpDownload;

/**
 * 需要权限/token验证的文件下载类
 * @author hu trace
 */
public class AuthorityDownload {
	
	private HttpDownload download;
	
	private long time;
	
	public AuthorityDownload(HttpDownload download) {
		this.download = download;
		time = System.currentTimeMillis();
	}

	public HttpDownload getDownload() {
		return download;
	}

	public void setDownload(HttpDownload download) {
		this.download = download;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * 根据key获取储存的HttpDownload对象
	 * @param key
	 * @return
	 */
	public static HttpDownload take(String key) {
		return AuthorityDownloadJSONResultHandler.take(key);
	}
	
}
