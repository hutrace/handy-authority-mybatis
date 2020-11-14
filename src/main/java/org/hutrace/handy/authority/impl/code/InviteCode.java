package org.hutrace.handy.authority.impl.code;

/**
 * 创建inviteCode（邀请码）的接口
 * <p>所有创建inviteCode（邀请码）都通过此接口调用
 * <p>你可以自定义实现它
 * @author hu trace
 */
public interface InviteCode {
	
	/**
	 * 创建邀请码
	 * @return 邀请码
	 */
	String create();
	
}
