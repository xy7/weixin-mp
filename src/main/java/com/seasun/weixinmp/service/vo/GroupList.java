package com.seasun.weixinmp.service.vo;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class GroupList {
	private List<Group> groups;
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
}
