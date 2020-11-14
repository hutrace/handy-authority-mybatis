package org.hutrace.handy.authority.controller;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.authority.impl.AuthorityDownload;
import org.hutrace.handy.exception.CustomException;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.http.file.HttpDownload;
import org.hutrace.handy.language.ApplicationProperty;

@Controller
@RequestMapping("system/download")
public class SystemDownloadController {
	
	@RequestMapping(value = "{key}", method = RequestMethod.GET)
	public HttpDownload download(String key) throws CustomException {
		HttpDownload download = AuthorityDownload.take(key);
		if(download == null) {
			throw new CustomException(ApplicationProperty.get("authority.system.download.notfount"));
		}
		return download;
	}
	
}
