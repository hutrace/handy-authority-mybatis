package org.hutrace.handy.authority.exception;

import org.hutrace.handy.exception.HandyserveException;

/**
 * 添加数据时出现错误抛出的异常
 * <p>该异常继承至{@link HandyserveException}
 * @see HandyserveException
 * @author hu trace
 *
 */
public class SelectException extends HandyserveException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>异常构造方法
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 */
	public SelectException(String msg) {
		super(msg);
	}
	
	/**
	 * <p>异常构造方法
	 * @param code 异常码，可通过{@link #code()}获取
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 */
	public SelectException(int code, String msg) {
		super(code, msg);
	}

	/**
	 * <p>异常构造方法
	 * @param code 异常码，可通过{@link #code()}获取
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 * @param e 其它异常
	 */
	public SelectException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

	/**
	 * <p>异常构造方法
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 * @param e 其它异常
	 */
	public SelectException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * <p>异常构造方法
	 * @param code 异常码，可通过{@link #code()}获取
	 * @param e 其它异常
	 */
	public SelectException(int code, Throwable e) {
		super(code, e);
	}

	/**
	 * <p>异常构造方法
	 * @param e 其它异常
	 */
	public SelectException(Throwable e) {
		super(e);
	}
	
	/**
	 * 获取异常码，可用于判断错误类型
	 * @return 异常码
	 */
	public int code() {
		return super.code();
	}

}
