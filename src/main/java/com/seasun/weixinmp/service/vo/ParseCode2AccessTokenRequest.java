/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import java.text.MessageFormat;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class ParseCode2AccessTokenRequest {
	// appid 鏄� 鍏紬鍙风殑鍞竴鏍囪瘑
	private String appid;
	// secret 鏄� 鍏紬鍙风殑appsecret
	private String secret;
	// code 鏄� 濉啓绗竴姝ヨ幏鍙栫殑code鍙傛暟
	private String code;
	// grant_type 鏄� 濉啓涓篴uthorization_code
	private String grant_type = "authorization_code";

	public static final String URL_CODE = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getUrl() {
		String url = MessageFormat.format(URL_CODE, appid, secret, code);
		return url;
	}

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @param appid
	 *            the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret
	 *            the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the grant_type
	 */
	public String getGrant_type() {
		return grant_type;
	}

	/**
	 * @param grant_type
	 *            the grant_type to set
	 */
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

}
