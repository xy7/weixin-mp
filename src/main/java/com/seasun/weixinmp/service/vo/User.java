/**
 * 
 */
package com.seasun.weixinmp.service.vo;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class User {
	// subscribe 鐢ㄦ埛鏄惁璁㈤槄璇ュ叕浼楀彿鏍囪瘑锛屽�间负0鏃讹紝浠ｈ〃姝ょ敤鎴锋病鏈夊叧娉ㄨ鍏紬鍙凤紝鎷夊彇涓嶅埌鍏朵綑淇℃伅銆�
	private String subscribe;
	// openid 鐢ㄦ埛鐨勬爣璇嗭紝瀵瑰綋鍓嶅叕浼楀彿鍞竴
	private String openid;
	// nickname 鐢ㄦ埛鐨勬樀绉�
	private String nickname;
	// sex 鐢ㄦ埛鐨勬�у埆锛屽�间负1鏃舵槸鐢锋�э紝鍊间负2鏃舵槸濂虫�э紝鍊间负0鏃舵槸鏈煡
	private String sex;
	// city 鐢ㄦ埛鎵�鍦ㄥ煄甯�
	private String city;
	// country 鐢ㄦ埛鎵�鍦ㄥ浗瀹�
	private String country;
	// province 鐢ㄦ埛鎵�鍦ㄧ渷浠�
	private String province;
	// language 鐢ㄦ埛鐨勮瑷�锛岀畝浣撲腑鏂囦负zh_CN
	private String language;
	// headimgurl
	// 鐢ㄦ埛澶村儚锛屾渶鍚庝竴涓暟鍊间唬琛ㄦ鏂瑰舰澶村儚澶у皬锛堟湁0銆�46銆�64銆�96銆�132鏁板�煎彲閫夛紝0浠ｈ〃640*640姝ｆ柟褰㈠ご鍍忥級锛岀敤鎴锋病鏈夊ご鍍忔椂璇ラ」涓虹┖銆傝嫢鐢ㄦ埛鏇存崲澶村儚锛屽師鏈夊ご鍍廢RL灏嗗け鏁堛��
	private String headimgurl;
	// subscribe_time 鐢ㄦ埛鍏虫敞鏃堕棿锛屼负鏃堕棿鎴炽�傚鏋滅敤鎴锋浘澶氭鍏虫敞锛屽垯鍙栨渶鍚庡叧娉ㄦ椂闂�
	private String subscribe_time;
	// unionid 鍙湁鍦ㄧ敤鎴峰皢鍏紬鍙风粦瀹氬埌寰俊寮�鏀惧钩鍙板笎鍙峰悗锛屾墠浼氬嚭鐜拌瀛楁銆傝瑙侊細鑾峰彇鐢ㄦ埛涓汉淇℃伅锛圲nionID鏈哄埗锛�
	private String unionid;
	// remark 鍏紬鍙疯繍钀ヨ�呭绮変笣鐨勫娉紝鍏紬鍙疯繍钀ヨ�呭彲鍦ㄥ井淇″叕浼楀钩鍙扮敤鎴风鐞嗙晫闈㈠绮変笣娣诲姞澶囨敞
	private String remark;
	// groupid 鐢ㄦ埛鎵�鍦ㄧ殑鍒嗙粍ID
	private String groupid;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the subscribe
	 */
	public String getSubscribe() {
		return subscribe;
	}

	/**
	 * @param subscribe
	 *            the subscribe to set
	 */
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
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
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the headimgurl
	 */
	public String getHeadimgurl() {
		return headimgurl;
	}

	/**
	 * @param headimgurl
	 *            the headimgurl to set
	 */
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	/**
	 * @return the subscribe_time
	 */
	public String getSubscribe_time() {
		return subscribe_time;
	}

	/**
	 * @param subscribe_time
	 *            the subscribe_time to set
	 */
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	/**
	 * @return the unionid
	 */
	public String getUnionid() {
		return unionid;
	}

	/**
	 * @param unionid
	 *            the unionid to set
	 */
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the groupid
	 */
	public String getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid
	 *            the groupid to set
	 */
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

}
