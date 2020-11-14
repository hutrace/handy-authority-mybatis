package org.hutrace.handy.authority.exception;

import org.hutrace.handy.mybatis.exception.RollbackException;

/**
 * 删除数据时出现错误抛出的异常
 * <p>该异常继承至{@link RollbackException}
 * @see RollbackException
 * @author hu trace
 *
 */
public class DeleteException extends RollbackException {
	
	private static final long serialVersionUID = 1L;

	public DeleteException(String msg) {
		super(msg);
	}
	
	public DeleteException(int code, String msg) {
		super(code, msg);
	}

	public DeleteException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

	public DeleteException(String msg, Throwable e) {
		super(msg, e);
	}

	public DeleteException(int code, Throwable e) {
		super(code, e);
	}

	public DeleteException(Throwable e) {
		super(e);
	}
	
	public int code() {
		return super.code();
	}
	
}
