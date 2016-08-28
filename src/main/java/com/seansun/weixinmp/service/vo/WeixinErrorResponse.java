/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class WeixinErrorResponse {
	// errcode 閿欒鐮�
	private String errcode;
	// errmsg 閿欒淇℃伅
	private String errmsg;
	// msg_id 娑堟伅ID
	private String msg_id;
	public static final String SUCCESS = "0";
	

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the errcode
	 */
	public String getErrcode() {
		return errcode;
	}

	/**
	 * @param errcode
	 *            the errcode to set
	 */
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	/**
	 * @return the errmsg
	 */
	public String getErrmsg() {
		return errmsg;
	}

	/**
	 * @param errmsg
	 *            the errmsg to set
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	/**
	 * @return the msg_id
	 */
	public String getMsg_id() {
		return msg_id;
	}

	/**
	 * @param msg_id
	 *            the msg_id to set
	 */
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

}
