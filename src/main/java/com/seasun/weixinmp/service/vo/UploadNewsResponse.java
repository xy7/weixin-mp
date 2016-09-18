/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class UploadNewsResponse {
	// type 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛夛紝娆℃暟涓簄ews锛屽嵆鍥炬枃娑堟伅
	private String type;
	// media_id 濯掍綋鏂囦欢/鍥炬枃娑堟伅涓婁紶鍚庤幏鍙栫殑鍞竴鏍囪瘑
	private String media_id;
	// created_at 濯掍綋鏂囦欢涓婁紶鏃堕棿
	private long created_at;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the media_id
	 */
	public String getMedia_id() {
		return media_id;
	}

	/**
	 * @param media_id
	 *            the media_id to set
	 */
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	/**
	 * @return the created_at
	 */
	public long getCreated_at() {
		return created_at;
	}

	/**
	 * @param created_at
	 *            the created_at to set
	 */
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

}
