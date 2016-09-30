/**
 * 
 */
package com.seasun.weixinmp.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.seasun.weixinmp.mapper.ReportMapper;
import com.seasun.weixinmp.repository.WeixinRepository;
import com.seasun.weixinmp.service.model.CardInfo;
import com.seasun.weixinmp.service.vo.ArticleMessage;
import com.seasun.weixinmp.service.vo.AuthorizeUrlRequest;
import com.seasun.weixinmp.service.vo.FilterObject;
import com.seasun.weixinmp.service.vo.GroupMessageRequest;
import com.seasun.weixinmp.service.vo.MediaMessage;
import com.seasun.weixinmp.service.vo.OpenidListMessageRequest;
import com.seasun.weixinmp.service.vo.OpenidMessageRequest;
import com.seasun.weixinmp.service.vo.SendResponse;
import com.seasun.weixinmp.service.vo.TextMessage;
import com.seasun.weixinmp.service.vo.UploadMediaResponse;
import com.seasun.weixinmp.service.vo.UploadNewsRequest;
import com.seasun.weixinmp.service.vo.UploadNewsResponse;
import com.seasun.weixinmp.service.vo.WeixinErrorResponse;
import com.seasun.weixinmp.utils.ErrorCode;
import com.seasun.weixinmp.utils.HttpUtils;

/**
 *
 */
@Service
public class MessageService {
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	public static final String FLAG_ERROR = "{\"err";
	@Autowired
	private UserService userService;
	@Autowired
	private WeixinRepository weixinRepository;
	//预览接口:开发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版。
	//为了满足第三方平台开发者的需求，在保留对openID预览能力的同时，增加了对指定微信号发送预览的能力，但该能力每日调用次数有限制（100次），请勿滥用
	public static final String URL_SENDOPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token={0}";
	public static final String URL_SENDOPENID_CUSTOM = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={0}";
	public static final String URL_SENDGROUP = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token={0}";
	public static final String URL_UPLOADMEDIA = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token={0}&type={1}";
	public static final String URL_UPLOADNEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token={0}";
	
	//根据OpenID列表群发【订阅号不可用，服务号认证后可用】
	public static final String URL_SENDOPENIDLIST = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token={0}";
	public static final String URL_USERDEFINEDMENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={0}";
	
	@Autowired
	private ReportMapper reportMapper;
	
	public SendResponse sendText2Card(int cardId, String content) {
		
		CardInfo card = reportMapper.getCardInfoById(cardId);
		if(card == null){
			String msg = MessageFormat.format("error in sendText2Card, find null from the cardId: {0}", cardId);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_RETURN);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		
		String openid = card.getOpenid(); //查询db获取openid
		return sendText2Openid(openid, content);
	}

	public SendResponse sendText2Openid(String openid, String content) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDOPENID, accessToken);
		OpenidMessageRequest request = new OpenidMessageRequest();
		request.setTouser(openid);
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_TEXT);
		TextMessage textMessage = new TextMessage();
		textMessage.setContent(content);
		request.setText(textMessage);

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendText2Openid to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}
	
	public SendResponse sendText2OpenidList(List<String> openids, String content) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDOPENID, accessToken); //可能有问题 应该为： URL_SENDOPENIDLIST，待测试
		OpenidListMessageRequest request = new OpenidListMessageRequest();
		request.setTouser(openids);
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_TEXT);
		TextMessage textMessage = new TextMessage();
		textMessage.setContent(content);
		request.setText(textMessage);

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendText2Openid to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}

	public SendResponse sendText2OpenidList(List<String> openids, String text, String redirectUrl, String reportId) {
		StringBuilder content = new StringBuilder();
		content.append(text);
		AuthorizeUrlRequest request = new AuthorizeUrlRequest();
		request.setAppid(weixinRepository.getAppid());
		request.setState(reportId);
		if (StringUtils.isEmpty(redirectUrl)) {
			redirectUrl = "http://wx.xgsdk.com/showReport";
		}
		request.setRedirect_uri(redirectUrl);
		content.append("\n").append(MessageFormat.format("&lt;a href=\"{0}\"&gt;点击阅读&lt;/a&gt;", request.getUrl()));
		SendResponse sendResponse = this.sendText2OpenidList(openids, content.toString());
		return sendResponse;
	}

	public SendResponse sendText2Openid(String openid, String text, String redirectUrl, String reportId) {
		StringBuilder content = new StringBuilder();
		content.append(text);
		AuthorizeUrlRequest request = new AuthorizeUrlRequest();
		request.setAppid(weixinRepository.getAppid());
		request.setState(reportId);
		if (StringUtils.isEmpty(redirectUrl)) {
			redirectUrl = "http://wx.xgsdk.com/showReport";
		}
		request.setRedirect_uri(redirectUrl);
		content.append("\n").append(MessageFormat.format("<a href=\"{0}\">点击阅读</a>", request.getUrl()));
		SendResponse sendResponse = this.sendText2Openid(openid, content.toString());
		return sendResponse;
	}

	public SendResponse sendText2Group(String groupId, String content) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDGROUP, accessToken);
		GroupMessageRequest request = new GroupMessageRequest();
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_TEXT);
		TextMessage textMessage = new TextMessage();
		textMessage.setContent(content);
		request.setText(textMessage);
		FilterObject filterObject = new FilterObject();
		if (!StringUtils.equals("0", groupId)) {
			filterObject.setIs_to_all(false);
			filterObject.setGroup_id(groupId);
			request.setFilter(filterObject);
		}

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendText2Group to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}

	public SendResponse sendText2Group(String groupId, String text, String redirectUrl, String reportId) {
		StringBuilder content = new StringBuilder();
		content.append(text);
		AuthorizeUrlRequest request = new AuthorizeUrlRequest();
		request.setAppid(weixinRepository.getAppid());
		request.setState(reportId);
		if (StringUtils.isEmpty(redirectUrl)) {
			redirectUrl = "http://wx.xgsdk.com/showReport";
		}
		request.setRedirect_uri(redirectUrl);
		content.append("\n").append(MessageFormat.format("<a href=\"{0}\">点击阅读</a>", request.getUrl()));
		SendResponse sendResponse = this.sendText2Group(groupId, content.toString());
		return sendResponse;
	}

	public SendResponse uploadImage(String fileName, String mediaType) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_UPLOADMEDIA, accessToken, mediaType);
		List<String> fileNames = new ArrayList<>();
		fileNames.add(fileName);
		String responseString = HttpUtils.doPostWeixin(url, fileName, null);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in uploadImage to the url:{0}, fileName:{1}, mediaType:{2}, response:{3}", url, fileName, mediaType, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
			String msg = MessageFormat.format("error in uploadImage to the url:{0}, fileName:{1}, mediaType:{2}, response:{3}", url, fileName, mediaType, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_RETURN);
			sendResponse.setMsg(msg);
			sendResponse.setOriginString(responseString);
			return sendResponse;
		}
		UploadMediaResponse response = JSON.parseObject(responseString, UploadMediaResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(ErrorCode.SUCCESS);
		sendResponse.setMsg(response.getMedia_id());
		sendResponse.setOriginString(responseString);
		return sendResponse;
	}

	public SendResponse uploadNews(String thumbMedia_id, String author, String title, String contentSourceUrl, String content, String digest, String showCoverPic) {
		// "thumb_media_id":"qI6_Ze_6PtV7svjolgs-rN6stStuHIjs9_DidOHaj0Q-mwvBelOXCFZiq2OsIU-p",
		// "author":"xxx",
		// "title":"Happy Day",
		// "content_source_url":"www.qq.com",
		// "content":"content",
		// "digest":"digest",
		// "show_cover_pic":"1"
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_UPLOADNEWS, accessToken);
		UploadNewsRequest request = new UploadNewsRequest();
		ArticleMessage message = new ArticleMessage();
		request.addArticle(message);
		message.setThumb_media_id(thumbMedia_id);
		message.setAuthor(author);
		message.setTitle(title);
		message.setContent_source_url(contentSourceUrl);
		message.setContent(content);
		message.setDigest(digest);
		message.setShow_cover_pic(showCoverPic);

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in uploadImage to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		if (StringUtils.startsWith(responseString, FLAG_ERROR)) {
			String msg = MessageFormat.format("error in uploadImage to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_RETURN);
			sendResponse.setMsg(msg);
			sendResponse.setOriginString(responseString);
			return sendResponse;
		}
		UploadNewsResponse response = JSON.parseObject(responseString, UploadNewsResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(ErrorCode.SUCCESS);
		sendResponse.setMsg(response.getMedia_id());
		sendResponse.setOriginString(responseString);
		return sendResponse;
	}

	public SendResponse sendImage2Openid(String openid, String mediaId) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDOPENID, accessToken);
		OpenidMessageRequest request = new OpenidMessageRequest();
		request.setTouser(openid);
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_IMAGE);
		MediaMessage message = new MediaMessage();
		message.setMedia_id(mediaId);
		request.setImage(message);

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendText2Openid to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}

	public SendResponse sendNews2Openid(String openid, String mediaId) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDOPENID, accessToken);
		OpenidMessageRequest request = new OpenidMessageRequest();
		request.setTouser(openid);
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_MPNEWS);
		MediaMessage message = new MediaMessage();
		message.setMedia_id(mediaId);
		request.setMpnews(message);

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendNews2Openid to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}

	public SendResponse sendNew2Group(String groupId, String mediaId) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDGROUP, accessToken);
		GroupMessageRequest request = new GroupMessageRequest();
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_MPNEWS);
		MediaMessage message = new MediaMessage();
		message.setMedia_id(mediaId);
		request.setMpnews(message);
		FilterObject filterObject = new FilterObject();
		if (!StringUtils.equals("0", groupId)) {
			filterObject.setIs_to_all(false);
			filterObject.setGroup_id(groupId);
			request.setFilter(filterObject);
		}

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendNew2Group to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}
	public SendResponse sendText2OpenidByCustom(String openid, String content) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_SENDOPENID_CUSTOM, accessToken);
		OpenidMessageRequest request = new OpenidMessageRequest();
		request.setTouser(openid);
		request.setMsgtype(OpenidMessageRequest.MSGTYPE_TEXT);
		TextMessage textMessage = new TextMessage();
		textMessage.setContent(content);
		request.setText(textMessage);

		String body = JSON.toJSONString(request);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in sendText2OpenidByCustom to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		return sendResponse;
	}

	public SendResponse userdefinedMenu(String body) {
		String accessToken = userService.getAccessToken();
		String url = MessageFormat.format(URL_USERDEFINEDMENU, accessToken);
		String responseString = HttpUtils.doPostJson(url, body);
		if (StringUtils.isEmpty(responseString)) {
			String msg = MessageFormat.format("error in userdefinedMenu to the url:{0}, body:{1}, response:{2}", url, body, responseString);
			logger.error(msg);
			SendResponse sendResponse = new SendResponse();
			sendResponse.setCode(ErrorCode.ERR_CONNECT);
			sendResponse.setMsg(msg);
			return sendResponse;
		}
		WeixinErrorResponse response = JSON.parseObject(responseString, WeixinErrorResponse.class);
		SendResponse sendResponse = new SendResponse();
		sendResponse.setCode(response.getErrcode());
		sendResponse.setMsg(response.getErrmsg());
		sendResponse.setOriginString(responseString);
		return sendResponse;
	}
}
