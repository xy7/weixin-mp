/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class UserList {
	// total 鍏虫敞璇ュ叕浼楄处鍙风殑鎬荤敤鎴锋暟
	private int total;
	// count 鎷夊彇鐨凮PENID涓暟锛屾渶澶у�间负10000
	private int count;
	// data 鍒楄〃鏁版嵁锛孫PENID鐨勫垪琛�
	private UserDatas data;
	// next_openid 鎷夊彇鍒楄〃鐨勬渶鍚庝竴涓敤鎴风殑OPENID
	private String next_openid;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the data
	 */
	public UserDatas getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(UserDatas data) {
		this.data = data;
	}

	/**
	 * @return the next_openid
	 */
	public String getNext_openid() {
		return next_openid;
	}

	/**
	 * @param next_openid
	 *            the next_openid to set
	 */
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

}
