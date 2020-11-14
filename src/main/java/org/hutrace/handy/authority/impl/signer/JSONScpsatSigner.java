package org.hutrace.handy.authority.impl.signer;

import org.hutrace.handy.authority.impl.BufferUser;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.utils.code.MD5;
import org.hutrace.handy.utils.code.TextCoding;

public class JSONScpsatSigner implements Signer {
	
	@Override
	public String sign(HttpRequest request, BufferUser buf) {
		String str = TextCoding.disrupt(buf.getSignKey()) + "." + request.body();
		return MD5.lowerCase(str, Configuration.charset());
	}
	
	@Override
	public String contentType() {
		return "application/scpsat-json";
	}
	
}
