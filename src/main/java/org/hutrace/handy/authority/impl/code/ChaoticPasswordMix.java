package org.hutrace.handy.authority.impl.code;

import org.hutrace.handy.utils.code.Chaotic;
import org.hutrace.handy.utils.code.ChaoticMix;

/**
 * 使用{@link ChaoticMix}类进行密码混淆
 * @see ChaoticMix
 * @author hu trace
 */
public class ChaoticPasswordMix implements PasswordMix {
	
	@Override
	public String mix(String password) {
		return Chaotic.encode(password);
	}

	@Override
	public String restore(String mix) {
		return Chaotic.decode(mix);
	}

}
