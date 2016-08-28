/**
 * 
 */
package com.seansun.weixinmp.service.vo;

/**
 *
 */
public class WxcardMessage {
//	  "wxcard":{              
//    "card_id":"123dsdajkasd231jhksad",
//     "card_ext": "{\"code\":\"\",\"openid\":\"\",\"timestamp\":\"1402057159\",\"signature\":\"017bb17407c8e0058a66d72dcc61632b70f511ad\"}"               
//     }, 
	private String card_id;
	private String card_ext;
	/**
	 * @return the card_id
	 */
	public String getCard_id() {
		return card_id;
	}
	/**
	 * @param card_id the card_id to set
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	/**
	 * @return the card_ext
	 */
	public String getCard_ext() {
		return card_ext;
	}
	/**
	 * @param card_ext the card_ext to set
	 */
	public void setCard_ext(String card_ext) {
		this.card_ext = card_ext;
	}
	
}
