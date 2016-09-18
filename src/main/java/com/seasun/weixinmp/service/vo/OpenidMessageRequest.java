/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class OpenidMessageRequest {
	// touser 鎺ユ敹娑堟伅鐢ㄦ埛瀵瑰簲璇ュ叕浼楀彿鐨刼penid锛岃瀛楁涔熷彲浠ユ敼涓簍owxname锛屼互瀹炵幇瀵瑰井淇″彿鐨勯瑙�
	private String touser;
	// towxname
	private String towxname;
	// msgtype
	// 缇ゅ彂鐨勬秷鎭被鍨嬶紝鍥炬枃娑堟伅涓簃pnews锛屾枃鏈秷鎭负text锛岃闊充负voice锛岄煶涔愪负music锛屽浘鐗囦负image锛岃棰戜负video锛屽崱鍒镐负wxcard
	private String msgtype;
	// 鍥炬枃娑堟伅
	public static final String MSGTYPE_MPNEWS = "mpnews";
	private MediaMessage mpnews;
	// 鏂囨湰
	public static final String MSGTYPE_TEXT = "text";
	private TextMessage text;
	// 璇煶锛堝叾涓璵edia_id涓庢牴鎹垎缁勭兢鍙戜腑鐨刴edia_id鐩稿悓锛夛細
	public static final String MSGTYPE_VOICE = "voice";
	private MediaMessage voice;
	// 鍥剧墖锛堝叾涓璵edia_id涓庢牴鎹垎缁勭兢鍙戜腑鐨刴edia_id鐩稿悓锛夛細
	public static final String MSGTYPE_IMAGE = "image";
	private MediaMessage image;
	// 瑙嗛锛堝叾涓璵edia_id涓庢牴鎹垎缁勭兢鍙戜腑鐨刴edia_id鐩稿悓锛夛細
	public static final String MSGTYPE_MPVIDEO = "mpvideo";
	private MediaMessage mpvideo;
	// 鍗″埜锛�
	public static final String MSGTYPE_WXCARD = "wxcard";
	private WxcardMessage wxcard;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the touser
	 */
	public String getTouser() {
		return touser;
	}

	/**
	 * @param touser
	 *            the touser to set
	 */
	public void setTouser(String touser) {
		this.touser = touser;
	}

	/**
	 * @return the towxname
	 */
	public String getTowxname() {
		return towxname;
	}

	/**
	 * @param towxname
	 *            the towxname to set
	 */
	public void setTowxname(String towxname) {
		this.towxname = towxname;
	}

	/**
	 * @return the msgtype
	 */
	public String getMsgtype() {
		return msgtype;
	}

	/**
	 * @param msgtype
	 *            the msgtype to set
	 */
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	/**
	 * @return the mpnews
	 */
	public MediaMessage getMpnews() {
		return mpnews;
	}

	/**
	 * @param mpnews
	 *            the mpnews to set
	 */
	public void setMpnews(MediaMessage mpnews) {
		this.mpnews = mpnews;
	}

	/**
	 * @return the text
	 */
	public TextMessage getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(TextMessage text) {
		this.text = text;
	}

	/**
	 * @return the voice
	 */
	public MediaMessage getVoice() {
		return voice;
	}

	/**
	 * @param voice
	 *            the voice to set
	 */
	public void setVoice(MediaMessage voice) {
		this.voice = voice;
	}

	/**
	 * @return the image
	 */
	public MediaMessage getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(MediaMessage image) {
		this.image = image;
	}

	/**
	 * @return the mpvideo
	 */
	public MediaMessage getMpvideo() {
		return mpvideo;
	}

	/**
	 * @param mpvideo
	 *            the mpvideo to set
	 */
	public void setMpvideo(MediaMessage mpvideo) {
		this.mpvideo = mpvideo;
	}

	/**
	 * @return the wxcard
	 */
	public WxcardMessage getWxcard() {
		return wxcard;
	}

	/**
	 * @param wxcard
	 *            the wxcard to set
	 */
	public void setWxcard(WxcardMessage wxcard) {
		this.wxcard = wxcard;
	}

}
