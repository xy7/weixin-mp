package com.seasun.weixinmp.service.model;

import java.sql.Date;
import java.sql.Timestamp;

import com.alibaba.fastjson.JSON;
import com.seasun.weixinmp.service.vo.ReportRequest;
import com.seasun.weixinmp.utils.DateUtils;

public class WechatPushContent {
    private int id;
    private Date startDate;
    private Date endDate;
    private int topN;
    private String pushTitle;
    private String reportTitle;
    private String boss;
    private String producer;
    private String gameEvent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTopN() {
        return topN;
    }

    public void setTopN(int topN) {
        this.topN = topN;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    /**
	 * @return the gameEvent
	 */
	public String getGameEvent() {
		return gameEvent;
	}

	/**
	 * @param gameEvent the gameEvent to set
	 */
	public void setGameEvent(String gameEvent) {
		this.gameEvent = gameEvent;
	}

	public static class Build {
        private ReportRequest reportRequest;

        public Build(ReportRequest reportRequest) {
            this.reportRequest = reportRequest;
        };

        public WechatPushContent build() {
            WechatPushContent wechatPushContent = new WechatPushContent();
            if (reportRequest.getStartDate() != null) {
                Timestamp startTimestamp = DateUtils.convertString2TimeStamp(
                        reportRequest.getStartDate(),
                        DateUtils.DEFAULT_DATE_PATTERN);
                wechatPushContent.setStartDate(new Date(startTimestamp.getTime()));
            }
            if (reportRequest.getEndDate() != null) {
                Timestamp endTimestamp = DateUtils.convertString2TimeStamp(
                        reportRequest.getEndDate(), DateUtils.DEFAULT_DATE_PATTERN);
                wechatPushContent.setEndDate(new Date(endTimestamp.getTime()));
            }
            wechatPushContent.setReportTitle(reportRequest.getReportTitle());
            wechatPushContent.setTopN(reportRequest.getTopN());
            wechatPushContent.setPushTitle(reportRequest.getPushTitle());
            if (reportRequest.getBoss() != null) {
                wechatPushContent.setBoss(JSON.toJSONString(reportRequest
                        .getBoss())); 
            }
            if (reportRequest.getProducer() != null) {
                wechatPushContent.setProducer(JSON
                        .toJSONString(reportRequest.getProducer()));
            }
            if (reportRequest.getGameEvent() != null) {
            	wechatPushContent.setGameEvent(JSON.toJSONString(reportRequest.getGameEvent()));
            }
            return wechatPushContent;
        }
    }

}
