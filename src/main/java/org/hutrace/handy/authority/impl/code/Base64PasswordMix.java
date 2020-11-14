package org.hutrace.handy.authority.impl.code;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.hutrace.handy.config.Configuration;

public class Base64PasswordMix implements PasswordMix {

	@Override
	public String mix(String password) {
		try {
			return new String(Base64.getEncoder().encode(password.getBytes(Configuration.charset())), Configuration.charset());
		}catch (UnsupportedEncodingException e) {
			return new String(Base64.getEncoder().encode(password.getBytes()));
		}
	}

	@Override
	public String restore(String mix) {
		try {
			return new String(Base64.getDecoder().decode(mix.getBytes(Configuration.charset())), Configuration.charset());
		} catch (UnsupportedEncodingException e) {
			return new String(Base64.getDecoder().decode(mix.getBytes()));
		}
	}

}
