package org.hutrace.handy.authority.impl.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.hutrace.handy.exception.ConfigurationReadException;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用队列异步实现日志打印
 * <p>以及查询日志记录的相关方法
 * @author hu trace
 */
public class QueueLogger implements Logger {
	
	/**
	 * 一天的秒数
	 */
	private long oneDayTime = 60 * 60 * 24;
	
	/**
	 * 当前天的结束时间戳（秒）
	 */
	private long currentDayEndTime;
	
	/**
	 * 回车字符串
	 */
	private byte[] enter;
	
	/**
	 * 当前天记录日志的文件流
	 */
	private FileOutputStream fos;
	
	/**
	 * 输入日志的实现线程
	 */
	private WriteThread log;
	
	/**
	 * 需要记录的日志消息的队列
	 * <p>阻塞队列实现日志记录
	 */
	private BlockingQueue<String> queue;
	
	/**
	 * 当前写入的日志文件总行数，便于检索当天的数据。
	 */
	private int line = 0;
	
	/**
	 * 当前写入的日志文件的日期格式
	 * <p>如：1971.01.01
	 */
	private String date;
	
	/**
	 * 当前查询的数据总条数
	 */
	private int queryLine = 0;
	
	/**
	 * 当前查询的日志文件日期格式
	 * <p>如：1971.01.01
	 */
//	private String queryDate;
	
	/**
	 * 构造方法
	 * <p>根据设置的字符编码初始化回车字符
	 * <p>初始化当天的最后一秒的时间戳（秒）
	 * <p>初始化队列，使用{@link LinkedBlockingQueue}链表队列
	 * <p>初始化日志写入线程，并启动
	 * @throws UnsupportedEncodingException
	 */
	public QueueLogger() throws UnsupportedEncodingException {
		enter = "\r\n".getBytes(CompleteLogListener.charset);
		initCurrentDayEndTime();
		queue = new LinkedBlockingQueue<>();
		log = new WriteThread();
		log.start();
	}
	
	/**
	 * 初始化当天的最后一秒的时间戳（秒）
	 */
	private void initCurrentDayEndTime() {
		long current = currentTime();
		long currentDayStartTime = current - (current + TimeZone.getDefault().getRawOffset() / 1000) % oneDayTime;
		currentDayEndTime = currentDayStartTime + oneDayTime - 1;
	}

	@Override
	public void print(String msg) {
		queue.offer(msg);
	}

	@Override
	public void print(String... msgs) {
		for(int i = 0; i < msgs.length; i++) {
			print(msgs[i]);
		}
	}

	/**
	 * 查询数据，根据date查询日志文件
	 * <p>当前类并没有实现使用时间戳（timestamp）的查询条件
	 */
	@Override
	public Object query(Date date, int pageStart, int pageSize, long timestamp, String reqId, String rspId) {
//		if(timestamp == 0 && reqId == null && rspId == null) {
		// 时间戳查询暂未生效
		if((reqId == null || reqId.isEmpty()) && (rspId == null || rspId.isEmpty())) {
			return query(date, pageStart, pageSize);
		}else {
			String name = getLogName(date);
			File file = getLogFile(name);
			BufferedReader reader = null;
			Map<String, Object> map = new HashMap<>();
			try {
				reader = new BufferedReader(new FileReader(file));
				map.put("list", search(reader, pageStart, pageSize, timestamp, reqId, rspId));
				map.put("count", queryLine);
			}catch (FileNotFoundException e) {
				map.put("count", 0);
				map.put("list", new ArrayList<>());
			}
			if(reader != null) {
				try {
					reader.close();
				}catch (IOException e) {}
			}
			return map;
		}
	}
	
	/**
	 * 无条件查询
	 * <p>根据date构造读取流，调用{@link #read(String, BufferedReader, int, int)}实现读取
	 * @param date
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	public Object query(Date date, int pageStart, int pageSize) {
		String name = getLogName(date);
		File file = getLogFile(name);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			return read(name, reader, pageStart, pageSize);
		}catch (FileNotFoundException e) {
			Map<String, Object> map = new HashMap<>();
			map.put("count", 0);
			map.put("list", new ArrayList<>());
			return map;
		}finally {
			if(reader != null) {
				try {
					reader.close();
				}catch (IOException e) {}
			}
		}
	}
	
	/**
	 * 判断读取的文件是当天的文件还是不是当天的文件
	 * <p>分别使用{@link #read(BufferedReader, int, int)}和{@link #readNew(BufferedReader, int, int)}读取
	 * <p>读取文件流中的数据，并将需要的数据返回
	 * @param name
	 * @param reader
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	private Map<String, Object> read(String name, BufferedReader reader, int pageStart, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		List<Object> list = new ArrayList<>();
		int num = -1;
		if(name.equals(date)) {
			num = line;
//		}else if(name.equals(queryDate)) {
//			num = queryLine;
		}
		if(num == -1) {
//			map.put("list", new ArrayList<>());
//			queryDate = name;
			list = readNew(reader, pageStart, pageStart + pageSize);
			map.put("count", queryLine);
//			return map;
		}else {
			map.put("count", num);
			int end = pageStart + pageSize;
			if(end > num)
				end = num;
			if(pageStart < end) {
				list = read(reader, pageStart, end);
			}
		}
		map.put("list", list);
		return map;
	}
	
	/**
	 * 读取当前天的数据，可直接根据开始条数和结束条数直接读取
	 * @param reader
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Object> read(BufferedReader reader, int start, int end) {
		List<Object> list = new ArrayList<>();
		int i = 0;
		String str;
		try {
			while(i < end && (str = reader.readLine()) != null) {
				if(i >= start) {
					list.add(JSONObject.parseObject(str));
				}
				i++;
			}
		}catch (IOException e) {}
		return list;
	}

	/**
	 * 读取非当前天的数据，可直接根据开始条数和结束条数直接读取
	 * @param reader
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Object> readNew(BufferedReader reader, int start, int end) {
		List<Object> list = new ArrayList<>();
		int i = 0;
		String str;
		try {
			while((str = reader.readLine()) != null) {
				if(i >= start && i < end) {
					list.add(JSONObject.parseObject(str));
				}
				i++;
			}
		}catch (IOException e) {}
		queryLine = i;
		return list;
	}
	
	/**
	 * 根据时间戳（timestamp）、请求ID（reqId）、响应ID（rspId）查询数据
	 * <p>当前版本未实现timestamp查询
	 * <p>此查询非常耗时，数据量越多越耗时
	 * @param reader
	 * @param start
	 * @param end
	 * @param time
	 * @param reqId
	 * @param rspId
	 * @return
	 */
	private List<Object> search(BufferedReader reader, int start, int end, long time, String reqId, String rspId) {
		List<String> params = new ArrayList<>(5);
		if(reqId != null && !reqId.isEmpty()) {
			if(reqId.length() == 32) {
				params.add("\"reqId\":\"" + reqId + "\"");
			}else {
				params.add("\"reqId\":\"" + reqId);
			}
		}
		if(rspId != null && !rspId.isEmpty()) {
			if(rspId.length() == 32) {
				params.add("\"rspId\":\"" + rspId + "\"");
			}else {
				params.add("\"rspId\":\"" + rspId);
			}
		}
		if(params.size() == 1) {
			return search(reader, start, end, params.get(0));
		}else {
			return search(reader, start, end, params.get(0), params.get(1));
		}
	}
	
	/**
	 * 按一个条件查询数据
	 * <p>匹配规则是半模糊匹配（"var%"）
	 * @param reader
	 * @param start
	 * @param end
	 * @param s1
	 * @return
	 */
	private List<Object> search(BufferedReader reader, int start, int end, String s1) {
		List<Object> list = new ArrayList<>();
		int i = 0;
		String str;
		try {
			while((str = reader.readLine()) != null) {
				if(str.indexOf(s1) > -1) {
					if(i >= start && i < end) {
						list.add(JSONObject.parseObject(str));
					}
					i++;
				}
			}
		}catch (IOException e) {}
		queryLine = i;
		return list;
	}
	
	/**
	 * 按两个条件查询数据
	 * <p>匹配规则是半模糊匹配（"var%"）
	 * @param reader
	 * @param start
	 * @param end
	 * @param s1
	 * @param s2
	 * @return
	 */
	private List<Object> search(BufferedReader reader, int start, int end, String s1, String s2) {
		List<Object> list = new ArrayList<>();
		int i = 0;
		String str;
		try {
			while((str = reader.readLine()) != null) {
				if(str.indexOf(s1) > -1 && str.indexOf(s2) > -1) {
					if(i >= start && i < end) {
						list.add(JSONObject.parseObject(str));
					}
					i++;
				}
			}
		}catch (IOException e) {}
		queryLine = i;
		return list;
	}
	
	@Override
	public void launch() throws ConfigurationReadException {
		if(CompleteLogListener.path == null) {
			CompleteLogListener.path = System.getProperty("user.dir") + File.separator;
		}else {
			if(CompleteLogListener.path.charAt(CompleteLogListener.path.length() - 1) != File.separatorChar) {
				CompleteLogListener.path += File.separator;
			}
		}
		if(CompleteLogListener.folder == null) {
			CompleteLogListener.folder = "log" + File.separator + "system" + File.separator;
		}else {
			CompleteLogListener.folder = CompleteLogListener.folder.replace("\\", File.separator).replace("/", File.separator);
			if(CompleteLogListener.folder.charAt(CompleteLogListener.folder.length() - 1) != File.separatorChar) {
				CompleteLogListener.folder += File.separator;
			}
		}
		File file = new File(CompleteLogListener.path + CompleteLogListener.folder);
		if(!file.exists()) {
			file.mkdirs();
		}
		try {
			createFile();
		}catch (IOException e) {
			throw new ConfigurationReadException(e);
		}
	}
	
	/**
	 * 创建当天需要的日志文件
	 * <p>并重新初始化当天的文件流
	 * @throws IOException
	 */
	private void createFile() throws IOException {
		date = getLogName(new Date());
		File file = getLogFile(date);
		if(!file.exists()) {
			file.createNewFile();
		}else {
			initLine(file);
		}
		if(fos != null) {
			try {
				fos.close();
			}catch (Exception e) {}
		}
		fos = new FileOutputStream(file, true);
	}
	
	/**
	 * 根据日期获取日志文件的日期格式
	 * @param date
	 * @return
	 */
	private String getLogName(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(date == null ? new Date() : date);
	}
	
	/**
	 * 根据日期格式获取日志文件的文件对象
	 * @param name
	 * @return
	 */
	private File getLogFile(String name) {
		return new File(CompleteLogListener.path + CompleteLogListener.folder +
				CompleteLogListener.name + "." + name + ".log");
	}
	
	/**
	 * 初始化当前日志文件的写入行数
	 * @param file
	 * @throws IOException
	 */
	private void initLine(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			while(reader.readLine() != null) {
				line ++;
			}
			reader.close();
		}catch (IOException e) {
			reader.close();
			throw e;
		}
	}
	
	/**
	 * 获取时间戳（秒）
	 * @return
	 */
	private long currentTime() {
		return System.currentTimeMillis() / 1000;
	}
	
	/**
	 * 写入日志的实现线程
	 * @author hu trace
	 */
	private class WriteThread extends Thread {
		
		/**
		 * 重写run方法
		 * <p>因当前使用的队列是{@link LinkedBlockingQueue}阻塞队列
		 * <p>直接while循环获取队列的消息，不需要添加sleep
		 */
		@Override
		public void run() {
			while(true) {
				try {
					print(queue.take());
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 开始向日志文件写入消息
		 * <p>写入前判断当前时间是否已经过了当天最后一刻
		 * <p>如果过了最后一刻（第二天了），则重新创建文件
		 * <p>最后写入数据
		 * @param msg
		 */
		private void print(String msg) {
			if(currentTime() > currentDayEndTime) {
				initCurrentDayEndTime();
				try {
					createFile();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(msg != null && !msg.isEmpty()) {
				try {
					fos.write(msg.getBytes(CompleteLogListener.charset));
					fos.write(enter);
					line ++;
				}catch (Exception e) {
					e.printStackTrace();
					System.out.println(msg);
				}
			}
		}
		
	}

	@Override
	public File logFile(Date date) {
		return getLogFile(getLogName(date));
	}
	
}
