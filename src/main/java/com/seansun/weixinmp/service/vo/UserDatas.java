/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class UserDatas {
	private List<String> openid;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the openid
	 */
	public List<String> getOpenid() {
		return openid;
	}

	/**
	 * @param openid
	 *            the openid to set
	 */
	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}

}
