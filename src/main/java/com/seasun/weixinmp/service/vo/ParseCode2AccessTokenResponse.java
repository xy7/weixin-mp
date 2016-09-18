/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class ParseCode2AccessTokenResponse {
	// access_token 缃戦〉鎺堟潈鎺ュ彛璋冪敤鍑瘉,娉ㄦ剰锛氭access_token涓庡熀纭�鏀寔鐨刟ccess_token涓嶅悓
	private String access_token;
	// expires_in access_token鎺ュ彛璋冪敤鍑瘉瓒呮椂鏃堕棿锛屽崟浣嶏紙绉掞級
	private int expires_in;
	private long responseTime = System.currentTimeMillis();
	// refresh_token 鐢ㄦ埛鍒锋柊access_token
	private String refresh_token;
	// openid 鐢ㄦ埛鍞竴鏍囪瘑锛岃娉ㄦ剰锛屽湪鏈叧娉ㄥ叕浼楀彿鏃讹紝鐢ㄦ埛璁块棶鍏紬鍙风殑缃戦〉锛屼篃浼氫骇鐢熶竴涓敤鎴峰拰鍏紬鍙峰敮涓�鐨凮penID
	private String openid;
	// scope 鐢ㄦ埛鎺堟潈鐨勪綔鐢ㄥ煙锛屼娇鐢ㄩ�楀彿锛�,锛夊垎闅�
	private String scope;
	// unionid 鍙湁鍦ㄧ敤鎴峰皢鍏紬鍙风粦瀹氬埌寰俊寮�鏀惧钩鍙板笎鍙峰悗锛屾墠浼氬嚭鐜拌瀛楁銆傝瑙侊細鑾峰彇鐢ㄦ埛涓汉淇℃伅锛圲nionID鏈哄埗锛�
	private String unionid;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
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
	}

	/**
	 * @return the refresh_token
	 */
	public String getRefresh_token() {
		return refresh_token;
	}

	/**
	 * @param refresh_token
	 *            the refresh_token to set
	 */
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}

	/**
	 * @param openid
	 *            the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the unionid
	 */
	public String getUnionid() {
		return unionid;
	}

	/**
	 * @param unionid
	 *            the unionid to set
	 */
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	/**
	 * @return the responseTime
	 */
	public long getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime
	 *            the responseTime to set
	 */
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

}
