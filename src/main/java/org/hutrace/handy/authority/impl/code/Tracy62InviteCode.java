package org.hutrace.handy.authority.impl.code;

import org.hutrace.handy.utils.code.TextCoding;
import org.hutrace.handy.utils.code.Tracy62;

/**
 * 使用{@link Tracy62}来创建邀请码
 * @author hu trace
 */
public class Tracy62InviteCode implements InviteCode {
	
	private final char fill = 'A';
	
	@Override
	public String create() {
		String inviteNum = TextCoding.createNumberString(20);
		String str = fill(Tracy62.build(Long.parseLong(inviteNum.substring(0, 10))).toString());
		str += fill(Tracy62.build(Long.parseLong(inviteNum.substring(10))).toString());
		return str;
	}
	
	private String fill(String str) {
		// 每10位数生成的Tracy62字符大部分为6位数，偶尔会有肯是5位
		if(str.length() == 6) {
			return str;
		}
		// 否则为长度为5位
		return str += fill;
	}

}
