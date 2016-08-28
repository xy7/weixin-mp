/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class Group {
	// id 鍒嗙粍id锛岀敱寰俊鍒嗛厤
	private String id;
	// name 鍒嗙粍鍚嶅瓧锛孶TF8缂栫爜
	private String name;
	// count 鍒嗙粍鍐呯敤鎴锋暟閲�
	private String count;
	
	private Map<String, User> userMap = new HashMap<>();

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * @return the userMap
	 */
	public Map<String, User> getUserMap() {
		return userMap;
	}

	/**
	 * @param userMap the userMap to set
	 */
	public void setUserMap(Map<String, User> userMap) {
		this.userMap = userMap;
	}

}
