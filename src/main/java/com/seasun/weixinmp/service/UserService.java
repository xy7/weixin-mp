/**
 * 
 */
package com.seasun.weixinmp.service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.seasun.weixinmp.mapper.ReportMapper;
import com.seasun.weixinmp.repository.WeixinRepository;
import com.seasun.weixinmp.service.model.WechatPushUser;
import com.seasun.weixinmp.service.vo.AccessToken;
import com.seasun.weixinmp.service.vo.Group;
import com.seasun.weixinmp.service.vo.GroupList;
import com.seasun.weixinmp.service.vo.ParseCode2AccessTokenRequest;
import com.seasun.weixinmp.service.vo.ParseCode2AccessTokenResponse;
import com.seasun.weixinmp.service.vo.User;
import com.seasun.weixinmp.service.vo.UserList;
import com.seasun.weixinmp.service.vo.WeixinUser;
import com.seasun.weixinmp.utils.HttpUtils;

/**
 *
 */
@Service
public class UserService {
	
	@Autowired
	ReportMapper reportMapper;

	@Autowired
	private WeixinRepository weixinRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	public static final String URL_GETACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
	public static final String URL_GETUSERLIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";
	public static final String URL_GETGROUPLIST = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token={0}";
	public static final String URL_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN";
	public static final String FLAG_ERROR = "{\"err";

	private AccessToken accessToken;

	public String getAccessToken() {
		// if (accessToken != null && !accessToken.isExpired()) {
		// return accessToken.getAccess_token();
		// }
		String appid = weixinRepository.getAppid();
		String secret = weixinRepository.getSecret();
		String url = MessageFormat.format(URL_GETACCESSTOKEN, appid, secret);
		String responseString = HttpUtils.doGet(url);
		if (StringUtils.isEmpty(responseString)) {
			logger.error("The accessToken data is empty from the url:{}.", url);
			return null;
		}
		if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
			logger.error("The accessToken data can not be found from the url:{}. The error is {}", url, responseString);
			return null;
		}
		this.accessToken = JSON.parseObject(responseString, AccessToken.class);
		return accessToken.getAccess_token();
	}

	/**
	 * 获取单个微信用户
	 * 
	 * @param openid
	 * @return
	 */
	public User getUser(String openid) {
		String accessToken = this.getAccessToken();
		String url = MessageFormat.format(URL_USERINFO, accessToken, openid);
		String responseString = HttpUtils.doGet(url);
		if (StringUtils.isEmpty(responseString)) {
			logger.error("The user data is empty from the url:{}.", url);
			return null;
		}
		if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
			logger.error("The user data can not be found from the url:{}. The error is {}", url, responseString);
			return null;
		}
		User user = JSON.parseObject(responseString, User.class);
		return user;
	}

	/**
	 * 获取微信用户列表
	 * 
	 * @return
	 */
	public Map<String, User> getUserMap() {
		Map<String, User> userMap = new HashMap<>();
		String accessToken = this.getAccessToken();
		String nextOpenid = "";
		int total = Integer.MAX_VALUE;
		int current = 0;

		while (total > current) {
			String url = MessageFormat.format(URL_GETUSERLIST, accessToken, nextOpenid);
			String responseString = HttpUtils.doGet(url);
			if (StringUtils.isEmpty(responseString)) {
				logger.error("The openid data is empty from the url:{}.", url);
				return userMap;
			}
			if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
				logger.error("The openid data can not be found from the url:{}. The error is {}", url, responseString);
				return userMap;
			}
			UserList userList = JSON.parseObject(responseString, UserList.class);
			nextOpenid = userList.getNext_openid();
			total = userList.getTotal();
			current += userList.getCount();
			if (userList.getCount() == 0) {
				return userMap;
			}
			List<String> openids = userList.getData().getOpenid();
			for (String openid : openids) {
				User user = this.getUser(openid);
				if (user != null) {
					userMap.put(openid, user);
				}
			}
		}
		return userMap;
	}

	/**
	 * 获取微信分组
	 * 
	 * @return
	 */
	public Map<String, Group> getGroupMap() {
		String accessToken = this.getAccessToken();
		String url = MessageFormat.format(URL_GETGROUPLIST, accessToken);
		String responseString = HttpUtils.doGet(url);
		if (StringUtils.isEmpty(responseString)) {
			logger.error("The group data is empty from the url:{}.", url);
			return new HashMap<>();
		}
		if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
			logger.error("The group data can not be found from the url:{}. The error is {}", url, responseString);
			return new HashMap<>();
		}
		GroupList groupList = JSON.parseObject(responseString, GroupList.class);
		List<Group> groups = groupList.getGroups();
		Map<String, Group> groupMap = new HashMap<>();
		for (Group group : groups) {
			groupMap.put(group.getId(), group);
		}
		return groupMap;
	}

	/**
	 * 通过code换取网页授权access_token
	 * 
	 * @param code
	 * @return
	 */
	public ParseCode2AccessTokenResponse parseCode2AccessToken(String code) {
		ParseCode2AccessTokenRequest request = new ParseCode2AccessTokenRequest();
		request.setAppid(weixinRepository.getAppid());
		request.setSecret(weixinRepository.getSecret());
		request.setCode(code);
		String url = request.getUrl();
		String responseString = HttpUtils.doGet(url);
		if (StringUtils.isEmpty(responseString)) {
			logger.error("The response is empty in the url:{}.", url);
			return null;
		}
		if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
			logger.error("The response has some error in the url:{}. The error is {}", url, responseString);
			return null;
		}
		ParseCode2AccessTokenResponse response = JSON.parseObject(responseString, ParseCode2AccessTokenResponse.class);
		return response;
	}

//	public WeixinUser getWeixinUser(String openid) {
//		WechatPushUser wechatPushUser = reportMapper.getUserByOpenId(openid);
//		if (wechatPushUser == null) {
//			User user = this.getUser(openid);
//			if (user != null) {
//				wechatPushUser = reportMapper.getUserByNickname(user.getNickname());
//				if (wechatPushUser != null) {
//					wechatPushUser.setOpenId(openid);
//					int result = reportMapper.updateUser(wechatPushUser);
//					logger.info("updateUser requestData={}, response={}", JSON.toJSONString(wechatPushUser), result);
//				}
//			}
//		}
//
//		if (wechatPushUser != null) {
//			WeixinUser weixinUser = new WeixinUser();
//			weixinUser.setOpenid(openid);
//			weixinUser.setRoleType(wechatPushUser.getType().toString());
//			weixinUser.setXgAppId(wechatPushUser.getXgAppId());
//			return weixinUser;
//		}
//		return null;
//	}
}
