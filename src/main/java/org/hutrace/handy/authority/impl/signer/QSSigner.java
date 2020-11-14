package org.hutrace.handy.authority.impl.signer;

import org.hutrace.handy.authority.impl.BufferUser;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.utils.code.MD5;
import org.hutrace.handy.utils.code.TextCoding;
import org.hutrace.handy.utils.qs.ParseConfig;
import org.hutrace.handy.utils.qs.QueryString;
import org.hutrace.handy.utils.qs.QueryStringObject;

public class QSSigner implements Signer {

	private ParseConfig[] config = new ParseConfig[] {
			ParseConfig.KEY_SORT
	};
	
	public void setConfig(String configs) {
		String[] arr = configs.split(",");
		config = new ParseConfig[arr.length];
		for(int i = 0; i < arr.length; i++) {
			config[i] = ParseConfig.valueOf(arr[i].trim());
		}
	}
	
	@Override
	public String sign(HttpRequest request, BufferUser buf) {
		QueryStringObject qso = QueryString.parse(request.body(), config);
		return MD5.lowerCase(TextCoding.disrupt(buf.getSignKey()) + "." + qso.toString(), Configuration.charset());
	}

	@Override
	public String contentType() {
		return "application/x-www-form-urlencoded";
	}

}
