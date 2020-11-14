package org.hutrace.handy.authority.exception;

import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 添加数据时出现错误抛出的异常
 * <p>该异常继承至{@link RollbackException}
 * @see RollbackException
 * @author hu trace
 *
 */
public class InsertException extends RollbackException {
	
	private static final long serialVersionUID = 1L;

	public InsertException(String msg) {
		super(msg);
	}
	
	public InsertException(int code, String msg) {
		super(code, msg);
	}

	public InsertException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

	public InsertException(String msg, Throwable e) {
		super(msg, e);
	}

	public InsertException(int code, Throwable e) {
		super(code, e);
	}

	public InsertException(Throwable e) {
		super(e);
	}
	
	public int code() {
		return super.code();
	}
	
}
