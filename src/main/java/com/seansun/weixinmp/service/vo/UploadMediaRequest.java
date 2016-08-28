/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class UploadMediaRequest {
	// access_token 鏄� 璋冪敤鎺ュ彛鍑瘉
	private String access_token;
	// type 鏄� 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
	private String type;
	//濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
	public static final String MEDIATYPE_IMAGE = "image";
	public static final String MEDIATYPE_VOICE = "voice";
	public static final String MEDIATYPE_VIDEO = "video";
	public static final String MEDIATYPE_THUMB = "thumb";
	// media 鏄� form-data涓獟浣撴枃浠舵爣璇嗭紝鏈塮ilename銆乫ilelength銆乧ontent-type绛変俊鎭�
	private String media;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token
	 *            the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
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
	 * @return the media
	 */
	public String getMedia() {
		return media;
	}

	/**
	 * @param media
	 *            the media to set
	 */
	public void setMedia(String media) {
		this.media = media;
	}

}
