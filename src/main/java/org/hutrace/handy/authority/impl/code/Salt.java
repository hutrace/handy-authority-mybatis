package org.hutrace.handy.authority.impl.code;

/**
 * 创建salt（盐）的接口
 * <p>所有创建salt（盐）都通过此接口调用
 * <p>你可以自定义实现它
 * @author hu trace
 */
public interface Salt {
	
	/**
	 * 创建盐
	 * @return 盐
	 */
	String create();
	
}
