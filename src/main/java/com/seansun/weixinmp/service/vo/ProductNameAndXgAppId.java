package com.seansun.weixinmp.service.vo;

public class ProductNameAndXgAppId {
    private String name;
    private String xgAppId;
    private byte[] icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXgAppId() {
        return xgAppId;
    }

    public void setXgAppId(String xgAppId) {
        this.xgAppId = xgAppId;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "ProductNameAndXgAppId [name=" + name + ", xgAppId=" + xgAppId
                + "]";
    }

}
