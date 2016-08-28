/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class SendResponse {
	private String code;
	private String msg;
	private String originString;

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
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the originString
	 */
	public String getOriginString() {
		return originString;
	}

	/**
	 * @param originString
	 *            the originString to set
	 */
	public void setOriginString(String originString) {
		this.originString = originString;
	}

}
