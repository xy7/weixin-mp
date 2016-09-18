package com.seasun.weixinmp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ClearCacheService {

	public static final Log log = LogFactory.getLog(ClearCacheService.class);

	@CacheEvict(value = "getStaticIndex2", allEntries = true)
	public void clearStaticIndex() {
		log.info("Clear StaticIndex2 data .");
	}
	
	@CacheEvict(value = "getStaticByIndex", allEntries = true)
	public void clearStaticByIndex() {
		log.info("Clear StaticByIndex data .");
	}
	
}
