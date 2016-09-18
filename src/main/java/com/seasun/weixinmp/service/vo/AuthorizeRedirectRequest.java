/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class AuthorizeRedirectRequest {
	// 濡傛灉鐢ㄦ埛鍚屾剰鎺堟潈锛岄〉闈㈠皢璺宠浆鑷�
	// redirect_uri/?code=CODE&state=STATE銆傝嫢鐢ㄦ埛绂佹鎺堟潈锛屽垯閲嶅畾鍚戝悗涓嶄細甯︿笂code鍙傛暟锛屼粎浼氬甫涓妔tate鍙傛暟redirect_uri?state=STATE
	private String code;
	private String state;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

}
