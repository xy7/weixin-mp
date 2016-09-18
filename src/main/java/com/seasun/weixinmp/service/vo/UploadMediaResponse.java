/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class UploadMediaResponse {
	// type 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛屼富瑕佺敤浜庤棰戜笌闊充箰鏍煎紡鐨勭缉鐣ュ浘锛�
	private String type;
	// media_id 濯掍綋鏂囦欢涓婁紶鍚庯紝鑾峰彇鏃剁殑鍞竴鏍囪瘑
	private String media_id;
	// created_at 濯掍綋鏂囦欢涓婁紶鏃堕棿鎴�
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
