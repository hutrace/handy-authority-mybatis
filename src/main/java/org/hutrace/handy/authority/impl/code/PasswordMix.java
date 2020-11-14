package org.hutrace.handy.authority.impl.code;

/**
 * 混淆密码接口
 * <p>所有混淆密码都通过此接口调用
 * <p>你可以自定义实现它
 * @author hu trace
 */
public interface PasswordMix {
	
	/**
	 * 混淆密码
	 * <p>此种混淆方式是可以还原的
	 * @param password 密码
	 * @return 混淆后的字符串
	 */
	String mix(String password);
	
	/**
	 * 还原混淆后的字符串
	 * @param mix
	 * @return
	 */
	String restore(String mix);
	
}
