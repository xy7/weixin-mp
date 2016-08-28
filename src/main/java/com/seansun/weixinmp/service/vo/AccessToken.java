/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class AccessToken {
	// access_token 鑾峰彇鍒扮殑鍑瘉
	private String access_token;
	// expires_in 鍑瘉鏈夋晥鏃堕棿锛屽崟浣嶏細绉�
	private int expires_in;

	private long expiredTime;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public boolean isExpired() {
		return System.currentTimeMillis() >= expiredTime;
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token
	 *            the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the expires_in
	 */
	public int getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in
	 *            the expires_in to set
	 */
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
		this.expiredTime = System.currentTimeMillis() + (expires_in - 10) * 1000;
	}

	/**
	 * @return the expiredTime
	 */
	public long getExpiredTime() {
		return expiredTime;
	}

	/**
	 * @param expiredTime
	 *            the expiredTime to set
	 */
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

}
