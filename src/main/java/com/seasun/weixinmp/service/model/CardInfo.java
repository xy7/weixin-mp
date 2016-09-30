package com.seasun.weixinmp.service.model;

public class CardInfo {
	private int cardId;
	private String name;
	private String company;
	private String position;
	private String tel;
	private String tel2;
	private String weixin;
	private String qq;
	private String city;
	private String origin;
	private String businessMain;
	private String businessSecondary;
	private String positionSocial;
	private String resourcesAvailable;
	private String resourcesNeed;
	private String platform;
	private String companyAddress;
	private int vip;
	private String openid;
	
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getBusinessMain() {
		return businessMain;
	}
	public void setBusinessMain(String businessMain) {
		this.businessMain = businessMain;
	}
	public String getBusinessSecondary() {
		return businessSecondary;
	}
	public void setBusinessSecondary(String businessSecondary) {
		this.businessSecondary = businessSecondary;
	}
	public String getPositionSocial() {
		return positionSocial;
	}
	public void setPositionSocial(String positionSocial) {
		this.positionSocial = positionSocial;
	}
	public String getResourcesAvailable() {
		return resourcesAvailable;
	}
	public void setResourcesAvailable(String resourcesAvailable) {
		this.resourcesAvailable = resourcesAvailable;
	}
	public String getResourcesNeed() {
		return resourcesNeed;
	}
	public void setResourcesNeed(String resourcesNeed) {
		this.resourcesNeed = resourcesNeed;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Override
	public String toString() {
		return "CardInfo [cardId=" + cardId + ", name=" + name + ", company=" + company + ", position=" + position
				+ ", tel=" + tel + ", tel2=" + tel2 + ", weixin=" + weixin + ", qq=" + qq + ", city=" + city
				+ ", origin=" + origin + ", businessMain=" + businessMain + ", businessSecondary=" + businessSecondary
				+ ", positionSocial=" + positionSocial + ", resourcesAvailable=" + resourcesAvailable
				+ ", resourcesNeed=" + resourcesNeed + ", platform=" + platform + ", companyAddress=" + companyAddress
				+ ", vip=" + vip + ", openid=" + openid + "]";
	}

}
