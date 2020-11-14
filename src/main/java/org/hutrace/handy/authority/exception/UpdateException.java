package org.hutrace.handy.authority.exception;

import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 修改数据时出现错误抛出的异常
 * <p>该异常继承至{@link RollbackException}
 * @see RollbackException
 * @author hu trace
 *
 */
public class UpdateException extends RollbackException {
	
	private static final long serialVersionUID = 1L;

	public UpdateException(String msg) {
		super(msg);
	}
	
	public UpdateException(int code, String msg) {
		super(code, msg);
	}

	public UpdateException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

	public UpdateException(String msg, Throwable e) {
		super(msg, e);
	}

	public UpdateException(int code, Throwable e) {
		super(code, e);
	}

	public UpdateException(Throwable e) {
		super(e);
	}
	
	public int code() {
		return super.code();
	}
	
}
