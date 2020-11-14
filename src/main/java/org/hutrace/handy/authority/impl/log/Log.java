package org.hutrace.handy.authority.impl.log;

import java.io.File;
import java.util.Date;

/**
 * 权限模块日志打印类
 * <p>该类打印的日志用于记录系统日志
 * <p>包含请求数据信息与响应数据
 * @author hu trace
 *
 */
public class Log {
	
	/**
	 * @see Logger#print(String)
	 * @param msg
	 */
	public static void print(String msg) {
		CompleteLogListener.logImpl.print(msg);
	}
	
	/**
	 * @see Logger#print(String...)
	 * @param msgs
	 */
	public static void print(String... msgs) {
		CompleteLogListener.logImpl.print(msgs);
	}
	
	/**
	 * @see Logger#logFile(Date)
	 * @param date
	 * @return
	 */
	public static File logFile(Date date) {
		return CompleteLogListener.logImpl.logFile(date);
	}
	
	/**
	 * @see Logger#query(Date, int, int, long, String, String)
	 * @param date
	 * @param pageStart
	 * @param pageSize
	 * @param timestamp
	 * @param reqId
	 * @param rspId
	 * @return
	 */
	public static Object query(Date date, int pageStart, int pageSize, long timestamp, String reqId, String rspId) {
		return CompleteLogListener.logImpl.query(date, pageStart, pageSize, timestamp, reqId, rspId);
	}
	
}
