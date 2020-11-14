package org.hutrace.handy.authority.impl.log;

import java.io.File;
import java.util.Date;

import org.hutrace.handy.exception.ConfigurationReadException;

public interface Logger {
	
	/**
	 * 打印msg的内容，并换行
	 * @param msg
	 */
	void print(String msg);
	
	/**
	 * 打印msgs数组的内容，数组中每条信息打印时，都会做换行处理
	 * @param msgs
	 */
	void print(String... msgs);
	
	/**
	 * 获取根据日期日志文件对象
	 * @param date
	 * @return
	 */
	File logFile(Date date);
	
	/**
	 * 查询日志数据
	 * <p>根据date获取当天的日志数据
	 * @param date 获取某天的日志
	 * @param pageStart 开始查询的条数
	 * @param pageSize 查询多少条
	 * @param timestamp 按照具体时间查询，需要注意，此处的具体时间应在formatTime内，否则无法查询到，传入0则不按此条件查询
	 * @param reqId 请求id，传入null则不按此条件查询
	 * @param rspId 响应id，传入null则不按此条件查询
	 * @return 查询出的数据，它可能是字符串，也可能是JSON对象，具体看实现类的实现方式。
	 */
	Object query(Date date, int pageStart, int pageSize, long timestamp, String reqId, String rspId);
	
	/**
	 * 启动方法
	 * <p>用于初始化配置信息
	 */
	void launch() throws ConfigurationReadException;
	
}
