package com.seansun.weixinmp.rest;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.seansun.weixinmp.repository.WeixinRepository;
import com.seansun.weixinmp.service.vo.VerifyCallbackUrlRequest;
import com.seansun.weixinmp.utils.RequestUtils;

@RestController
public class WeixinController {
	
	@Autowired
	private WeixinRepository weixinRepository;

	private Logger logger = LoggerFactory.getLogger(WeixinController.class);
	
	@RequestMapping(value = "/verifyCallbackUrl")
	public @ResponseBody String verifyCallbackUrl(HttpServletRequest hRequest) {
		JSONObject jsonObject = RequestUtils.parseRequestParameter(hRequest);
		String body = RequestUtils.getRequestStream(hRequest);
		String headers = RequestUtils.getRequestHeaders(hRequest);
		VerifyCallbackUrlRequest request = RequestUtils.parseRequestParameter(hRequest, VerifyCallbackUrlRequest.class);
		logger.info("start to verifyCallbackUrl, requestData={}, body={}, headers={}", jsonObject, body, headers);
		//logger.info(MessageFormat.format(URL_PATTERN, hRequest.getScheme(), hRequest.getServerName(), hRequest.getServerPort(), hRequest.getServletPath()));
		if (request.checkSign(weixinRepository.getToken())) {
			String responseString = request.getEchostr();
			if (StringUtils.isEmpty(responseString)) {
				responseString = request.getNonce();
			}
			logger.info("end to verifyCallbackUrl, requestData={}, responseString={}", request, responseString);
			return responseString;
		}
		String errMsg = "The signature is not matched.";
		logger.info("error to verifyCallbackUrl, requestData={}, responseString={}", jsonObject, errMsg);
		return errMsg;
	}
}
