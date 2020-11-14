package org.hutrace.handy.authority.impl.signer;

import java.util.Map.Entry;
import java.util.Set;

import org.hutrace.handy.authority.impl.BufferUser;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.utils.code.MD5;
import org.hutrace.handy.utils.code.TextCoding;
import org.hutrace.handy.utils.qs.ParseConfig;
import org.hutrace.handy.utils.qs.QueryStringObject;

import com.alibaba.fastjson.JSONObject;

public class JSONSigner implements Signer {
	
	private ParseConfig[] config = new ParseConfig[] {
			ParseConfig.KEY_SORT
	};
	
	public void setConfig(ParseConfig[] configs) {
		this.config = configs;
	}

	@Override
	public String sign(HttpRequest request, BufferUser buf) {
		JSONObject json = JSONObject.parseObject(request.body());
		Set<Entry<String, Object>> set = json.getInnerMap().entrySet();
		QueryStringObject qso = new QueryStringObject(json.size(), config);
		for(Entry<String, Object> entry : set) {
			qso.put(entry.getKey(), entry.getValue().toString());
		}
		String str = TextCoding.disrupt(buf.getSignKey()) + "." + qso.toString();
		return MD5.lowerCase(str, Configuration.charset());
	}
	
	@Override
	public String contentType() {
		return "application/json";
	}
	
}
