package com.seansun.weixinmp.service.vo;

public class Summary {

    private String arpuSummary;
    private String survivalSummary;
    private String wauSummary;
    private String paySummary;
    private String gameDurationSummary;
    private String playerProportionSummary;
    private String ltvSummary;

    public String getArpuSummary() {
        return arpuSummary;
    }

    public void setArpuSummary(String arpuSummary) {
        this.arpuSummary = arpuSummary;
    }

    public String getSurvivalSummary() {
        return survivalSummary;
    }

    public void setSurvivalSummary(String survivalSummary) {
        this.survivalSummary = survivalSummary;
    }

    public String getWauSummary() {
        return wauSummary;
    }

    public void setWauSummary(String wauSummary) {
        this.wauSummary = wauSummary;
    }

    public String getPaySummary() {
        return paySummary;
    }

    public void setPaySummary(String paySummary) {
        this.paySummary = paySummary;
    }

    public String getGameDurationSummary() {
        return gameDurationSummary;
    }

    public void setGameDurationSummary(String gameDurationSummary) {
        this.gameDurationSummary = gameDurationSummary;
    }

    public String getPlayerProportionSummary() {
        return playerProportionSummary;
    }

    public void setPlayerProportionSummary(String playerProportionSummary) {
        this.playerProportionSummary = playerProportionSummary;
    }

    /**
	 * @return the ltvSummary
	 */
	public String getLtvSummary() {
		return ltvSummary;
	}

	/**
	 * @param ltvSummary the ltvSummary to set
	 */
	public void setLtvSummary(String ltvSummary) {
		this.ltvSummary = ltvSummary;
	}

	@Override
    public String toString() {
        return "Summary [arpuSummary=" + arpuSummary + ", survivalSummary="
                + survivalSummary + ", wauSummary=" + wauSummary
                + ", paySummary=" + paySummary + ", gameDurationSummary="
                + gameDurationSummary + ", playerProportionSummary="
                + playerProportionSummary + "]";
    }

}
