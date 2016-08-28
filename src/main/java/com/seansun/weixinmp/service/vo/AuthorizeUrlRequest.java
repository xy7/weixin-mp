/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import java.text.MessageFormat;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class AuthorizeUrlRequest {
	public static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state={2}#wechat_redirect";
	// appid 鏄� 鍏紬鍙风殑鍞竴鏍囪瘑
	private String appid;
	// redirect_uri 鏄� 鎺堟潈鍚庨噸瀹氬悜鐨勫洖璋冮摼鎺ュ湴鍧�锛岃浣跨敤urlencode瀵归摼鎺ヨ繘琛屽鐞�
	private String redirect_uri;
	// response_type 鏄� 杩斿洖绫诲瀷锛岃濉啓code
	private String response_type = "code";
	// scope 鏄� 搴旂敤鎺堟潈浣滅敤鍩燂紝snsapi_base 锛堜笉寮瑰嚭鎺堟潈椤甸潰锛岀洿鎺ヨ烦杞紝鍙兘鑾峰彇鐢ㄦ埛openid锛夛紝snsapi_userinfo
	// 锛堝脊鍑烘巿鏉冮〉闈紝鍙�氳繃openid鎷垮埌鏄电О銆佹�у埆銆佹墍鍦ㄥ湴銆傚苟涓旓紝鍗充娇鍦ㄦ湭鍏虫敞鐨勬儏鍐典笅锛屽彧瑕佺敤鎴锋巿鏉冿紝涔熻兘鑾峰彇鍏朵俊鎭級
	private String scope = SCOPE_BASE;
	public static final String SCOPE_BASE = "snsapi_base";
	public static final String SCOPE_USERINFO = "snsapi_userinfo";
	// state 鍚� 閲嶅畾鍚戝悗浼氬甫涓妔tate鍙傛暟锛屽紑鍙戣�呭彲浠ュ～鍐檃-zA-Z0-9鐨勫弬鏁板�硷紝鏈�澶�128瀛楄妭
	private String state;

	// #wechat_redirect 鏄� 鏃犺鐩存帴鎵撳紑杩樻槸鍋氶〉闈�302閲嶅畾鍚戞椂鍊欙紝蹇呴』甯︽鍙傛暟

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getUrl() {
		String url = MessageFormat.format(AUTH_URL, appid, redirect_uri, state);
		return url;
	}

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @param appid
	 *            the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * @return the redirect_uri
	 */
	public String getRedirect_uri() {
		return redirect_uri;
	}

	/**
	 * @param redirect_uri
	 *            the redirect_uri to set
	 */
	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	/**
	 * @return the response_type
	 */
	public String getResponse_type() {
		return response_type;
	}

	/**
	 * @param response_type
	 *            the response_type to set
	 */
	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

}
