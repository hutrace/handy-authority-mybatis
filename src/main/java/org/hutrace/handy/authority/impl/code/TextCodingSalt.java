package org.hutrace.handy.authority.impl.code;

import org.hutrace.handy.utils.code.TextCoding;

/**
 * 使用{@link TextCoding#createCode64()}创建盐
 * @see TextCoding
 * @author hu trace
 */
public class TextCodingSalt implements Salt {

	@Override
	public String create() {
		return TextCoding.createCode64();
	}

}
