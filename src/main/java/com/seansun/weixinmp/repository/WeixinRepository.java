/**
 * 
 */
package com.seansun.weixinmp.repository;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.seansun.weixinmp.service.vo.ParseCode2AccessTokenResponse;

/**
 *
 */
@Service
public class WeixinRepository {
	private static Logger logger = LoggerFactory.getLogger(WeixinRepository.class);

	private Map<String, ParseCode2AccessTokenResponse> redirectTokenCache = new ConcurrentHashMap<>();

	public String getAppid() {
		return "wx6a84e2929940254d";
	}

	public String getSecret() {
		return "aba44dc730e0d22f0558d76184fd3650";
	}

	public String getToken() {
		return "40810e947da32e7f4d85f9f6a386019c";
	}

	public String getEncodingAESKey() {
		return "GRORyK232FMhRpolakNXt6sEjyglj0g4qJCYIazVk4R";
	}

	public void addRedirectToken(ParseCode2AccessTokenResponse response) {
		if (response != null) {
			redirectTokenCache.put(response.getAccess_token(), response);
		}
	}

	public String getOpenIdByAccessToken(String accessToken) {
		ParseCode2AccessTokenResponse response = redirectTokenCache.get(accessToken);
		if (response == null) {
			return null;
		}
		String openid = response.getOpenid();
		return openid;
	}

	public void clearRedirectToken() {
		long current = System.currentTimeMillis();
		logger.info("Before clear token, cache={}", JSON.toJSONString(redirectTokenCache));
		for (Entry<String, ParseCode2AccessTokenResponse> entry : redirectTokenCache.entrySet()) {
			ParseCode2AccessTokenResponse response = entry.getValue();
//			if (current > response.getResponseTime() + response.getExpires_in() * 1000 / 2) {
			if (!StringUtils.equals(response.getOpenid(), "ouxKswoEUvqCKkGgRZoJO7VS2HF4") && !StringUtils.equals(response.getOpenid(), "ouxKswqnnz4CHt92zDT7TJMAZDGg") && current > response.getResponseTime() + 60 * 1000) {
				redirectTokenCache.remove(entry.getKey());
				logger.info("Remove token, openid={},token={}", response.getOpenid(), entry.getKey());
			}
		}
		logger.info("After clear token, cache={}", JSON.toJSONString(redirectTokenCache));
	}
}
