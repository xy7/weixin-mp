package com.seansun.weixinmp.service.vo;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class ReportRequest {
	private String startDate;
	private String endDate;
	private String pushTitle;
	private String reportTitle;
	private int topN;
	private Summary boss;
	private Summary producer;
	private List<ReportRequestGameEvent> gameEvent;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public int getTopN() {
		return topN;
	}

	public void setTopN(int topN) {
		this.topN = topN;
	}

	public Summary getBoss() {
		return boss;
	}

	public void setBoss(Summary boss) {
		this.boss = boss;
	}

	public Summary getProducer() {
		return producer;
	}

	public void setProducer(Summary producer) {
		this.producer = producer;
	}

	/**
	 * @return the gameEvent
	 */
	public List<ReportRequestGameEvent> getGameEvent() {
		return gameEvent;
	}

	/**
	 * @param gameEvent
	 *            the gameEvent to set
	 */
	public void setGameEvent(List<ReportRequestGameEvent> gameEvent) {
		this.gameEvent = gameEvent;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
