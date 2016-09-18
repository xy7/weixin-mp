/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class WeixinUser {
	private String openid;
	private String roleType;
	public static final String ROLETYPE_BOSS = "BOSS";
	public static final String ROLETYPE_GAME = "PRODUCER";
	private String xgAppId;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
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
	 * @return the roleType
	 */
	public String getRoleType() {
		return roleType;
	}

	/**
	 * @param roleType
	 *            the roleType to set
	 */
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	/**
	 * @return the xgAppId
	 */
	public String getXgAppId() {
		return xgAppId;
	}

	/**
	 * @param xgAppId
	 *            the xgAppId to set
	 */
	public void setXgAppId(String xgAppId) {
		this.xgAppId = xgAppId;
	}
}
