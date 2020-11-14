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
import org.hutrace.handy.utils.xml.XMLObject;

import com.alibaba.fastjson.JSONObject;

public class XMLSigner implements Signer {
	
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
	public String sign(HttpRequest request, BufferUser buf) throws Exception {
		JSONObject json = XMLObject.parse(request.body());
		Set<Entry<String, Object>> set = json.getInnerMap().entrySet();
		QueryStringObject qso = new QueryStringObject(json.size(), config);
		for(Entry<String, Object> entry : set) {
			qso.put(entry.getKey(), entry.getValue().toString());
		}
		return MD5.lowerCase(TextCoding.disrupt(buf.getSignKey()) + "." + qso.toString(), Configuration.charset());
	}
	
	@Override
	public String contentType() {
		return "text/xml";
	}
	
}
