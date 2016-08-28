/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class ReportRequestGameEvent {
	// "eventDate": "2015-10-2",
	private String eventDate;
	// "appId": "1024appid",
	private String appId;
	private String appName;
	private String appIcon;
	// "description": ""
	private String description;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the eventDate
	 */
	public String getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate
	 *            the eventDate to set
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the appIcon
	 */
	public String getAppIcon() {
		return appIcon;
	}

	/**
	 * @param appIcon
	 *            the appIcon to set
	 */
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

}
