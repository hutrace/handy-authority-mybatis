package org.hutrace.handy.authority.controller;

import java.io.File;
import java.util.Date;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.authority.annotation.Authority;
import org.hutrace.handy.authority.impl.AuthorityDownload;
import org.hutrace.handy.authority.impl.log.Log;
import org.hutrace.handy.authority.params.SystemLogGet;
import org.hutrace.handy.exception.CustomException;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.http.file.HttpDownload;
import org.hutrace.handy.language.ApplicationProperty;

@Controller
@RequestMapping("system/log")
public class SystemLogController {
	
	@RequestMapping(method = RequestMethod.GET)
	@Validated
	@Authority
	public Object query(SystemLogGet params) {
		return Log.query(params.getDate(), params.getPageStart(), params.getPageSize(),
				params.getTimestamp(), params.getReqId(), params.getRspId());
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public AuthorityDownload download(Date date) throws CustomException {
		if(date == null) {
			throw new CustomException(ApplicationProperty.get("authority.log.download.param"));
		}
		File file = Log.logFile(date);
		if(!file.exists()) {
			throw new CustomException(ApplicationProperty.get("authority.log.download.notfount"));
		}
		return new AuthorityDownload(new HttpDownload(file));
	}
	
}
