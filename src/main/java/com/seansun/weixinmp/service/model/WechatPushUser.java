package com.seansun.weixinmp.service.model;

public class WechatPushUser {
    private int id;
    private Type type;
    private String openId;
    private String headImgUrl;
    private String nickname;
    private String xgAppId;

    public enum Type {
        BOSS("BOSS"), PRODUCER("PRODUCER");
        private String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getXgAppId() {
        return xgAppId;
    }

    public void setXgAppId(String xgAppId) {
        this.xgAppId = xgAppId;
    }

}
