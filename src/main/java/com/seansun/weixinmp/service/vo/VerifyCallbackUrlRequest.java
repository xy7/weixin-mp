/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class VerifyCallbackUrlRequest {
	// signature 寰俊鍔犲瘑绛惧悕锛宻ignature缁撳悎浜嗗紑鍙戣�呭～鍐欑殑token鍙傛暟鍜岃姹備腑鐨則imestamp鍙傛暟銆乶once鍙傛暟銆�
	private String signature;
	// timestamp 鏃堕棿鎴�
	private String timestamp;
	// nonce 闅忔満鏁�
	private String nonce;
	// echostr 闅忔満瀛楃涓�
	private String echostr;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public boolean checkSign(String token) {
		String oksign = this.generateSign(token);
		boolean result = StringUtils.equalsIgnoreCase(oksign, signature);
		return result;
	}
	
	public String generateSign(String token) {
		String[] params = new String[] {token, timestamp, nonce};
		Arrays.sort(params);
		StringBuilder builder = new StringBuilder();
		for (String param : params) {
			builder.append(param);
		}
		
		String sign = Hex.encodeHexString(DigestUtils.sha1(builder.toString()));
		return sign;
	}
	
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the nonce
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * @param nonce
	 *            the nonce to set
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	/**
	 * @return the echostr
	 */
	public String getEchostr() {
		return echostr;
	}

	/**
	 * @param echostr
	 *            the echostr to set
	 */
	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

}
