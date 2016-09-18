package com.seasun.weixinmp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 宸ュ叿绫汇��
 */
public class RequestUtils {
	protected static final Logger log = LoggerFactory.getLogger(RequestUtils.class);
	protected static final String MAC_NAME = "HmacSHA1";
	public static final String ENCODING = "UTF-8";
	public static final String URL_PARAM_START = "?";
	public static final String URL_PARAM_SEPARATOR = "&";
	public static final String URL_PARAM_EQUAL = "=";

	public static final String GAME_CHARSET = "UTF-8";
	public static final String REQUEST_EQUAL = "=";
	public static final String REQUEST_SEPARATOR = "&";
	public static final String REQUEST_CONTENTTYPE = "Content-Type";
	
	public static <T> T parseRequestParameter(HttpServletRequest request, Class<T> returnClass) {
		return parseRequestParameter(request, returnClass, false);
	}

	public static <T> T parseRequestParameter(HttpServletRequest request, Class<T> returnClass, boolean charsetTransfer) {
		Enumeration<String> names = request.getParameterNames();
		JSONObject jsonObject = new JSONObject();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			jsonObject.put(name, request.getParameter(name));
		}
		String jsonString = jsonObject.toJSONString();
		T obj = JSON.parseObject(jsonString, returnClass);
		return obj;
	}

	public static <T> T parseRequestParameter(String request, Class<T> returnClass) {
		JSONObject jsonObject = new JSONObject();
		String[] split = request.split("&");
		for (String string : split) {
			String[] splits = string.split("=");
			if (splits.length == 1) {
				jsonObject.put(splits[0], "");
			} else if (splits.length == 2) {
				jsonObject.put(splits[0], splits[1]);
			}
		}
		String jsonString = jsonObject.toJSONString();
		T obj = JSON.parseObject(jsonString, returnClass);
		return obj;
	}

	public static JSONObject parseRequestParameter(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		JSONObject jsonObject = new JSONObject();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			jsonObject.put(name, request.getParameter(name));
		}
		return jsonObject;
	}

	public static JSONObject parseRequestStream(HttpServletRequest request) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = inputStream.read(bytes);
			while (length > 0) {
				outputStream.write(bytes, 0, length);
				length = inputStream.read(bytes);
			}
			// String jsonString = StreamUtils.copyToString(inputStream,
			// Charset.forName(ENCODING));
			String jsonString = new String(outputStream.toByteArray(), ENCODING);
			//log.info(jsonString);
			// JSONObject jsonObject = JSON.parseObject(jsonString);
			return JSON.parseObject(jsonString);
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException t) {
					log.error(t.getMessage(), t);
				}
			}
		}
		return null;
	}

	public static <T> T parseRequestStreamByJson(HttpServletRequest request, Class<T> returnClass) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = inputStream.read(bytes);
			while (length > 0) {
				outputStream.write(bytes, 0, length);
				length = inputStream.read(bytes);
			}
			// String jsonString = StreamUtils.copyToString(inputStream,
			// Charset.forName(ENCODING));
			String jsonString = new String(outputStream.toByteArray(), ENCODING).trim();
			//log.info("[" + jsonString + "]");
			// JSONObject jsonObject = JSON.parseObject(jsonString);
			T obj = JSON.parseObject(jsonString, returnClass);
			return obj;
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException t) {
					log.error(t.getMessage(), t);
				}
			}
		}
		return (T) null;
	}

	public static <T> T parseRequestStreamByXml(HttpServletRequest request, Class<T> returnClass) {
		try {
			byte[] bytes = RequestUtils.getRequestBytes(request);
			String xmlString = new String(bytes, ENCODING).trim();
			T obj = XmlUtils.parseObject(xmlString, returnClass);
			log.info("[" + xmlString + "]");
			return obj;
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
		return (T) null;
	}

	public static <T> T parseRequestStreamByUrlParam(HttpServletRequest request, Class<T> returnClass) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = inputStream.read(bytes);
			while (length > 0) {
				outputStream.write(bytes, 0, length);
				length = inputStream.read(bytes);
			}
			// String jsonString = StreamUtils.copyToString(inputStream,
			// Charset.forName(ENCODING));
			String jsonString = new String(outputStream.toByteArray(), ENCODING);
			JSONObject jsonObject = new JSONObject();
			if (!StringUtils.isEmpty(jsonString)) {
				String[] pairs = jsonString.split(URL_PARAM_SEPARATOR);
				for (String pair : pairs) {
					String[] namevalue = pair.split(URL_PARAM_EQUAL);
					if (namevalue.length == 2) {
						jsonObject.put(namevalue[0], namevalue[1]);
					}
				}
			}
			T obj = JSON.parseObject(jsonObject.toJSONString(), returnClass);
			return obj;
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException t) {
					log.error(t.getMessage(), t);
				}
			}
		}
		return (T) null;
	}

	public static String getRequestStream(HttpServletRequest request) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = inputStream.read(bytes);
			while (length > 0) {
				outputStream.write(bytes, 0, length);
				length = inputStream.read(bytes);
			}
			// String jsonString = StreamUtils.copyToString(inputStream,
			// Charset.forName(ENCODING));
			String jsonString = new String(outputStream.toByteArray(), ENCODING);
			return jsonString;
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException t) {
					log.error(t.getMessage(), t);
				}
			}
		}
		return "";
	}

	public static byte[] getRequestBytes(HttpServletRequest request) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = inputStream.read(bytes);
			while (length > 0) {
				outputStream.write(bytes, 0, length);
				length = inputStream.read(bytes);
			}
			return outputStream.toByteArray();
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException t) {
					log.error(t.getMessage(), t);
				}
			}
		}
		return new byte[0];
	}

	public static String getSigningString(Object request, String signFieldsName) {
		return getSigningString(request, signFieldsName, false, true, false);
	}

	public static String getSigningString(Object request, String signFieldsName, boolean withEmpty) {
		return getSigningString(request, signFieldsName, withEmpty, true, false);
	}

	public static String getSigningString(Object request, String signFieldsName, boolean withEmpty, boolean urlEncodeValue, boolean lowerName) {
		return getSigningString(request, signFieldsName, withEmpty, false, urlEncodeValue, lowerName, false);
	}

	public static String getSigningString(Object request, String signFieldsName, boolean withEmpty, boolean urlEncodeName, boolean urlEncodeValue, boolean lowerName, boolean upperCaseFirstChar) {
		String jsonString = JSON.toJSONString(request);
		JSONObject jsonObject = JSON.parseObject(jsonString);
		if (jsonObject == null || jsonObject.size() == 0) {
			return "";
		}
		if (StringUtils.isNotEmpty(signFieldsName)) {
			for (String signField : signFieldsName.split(",")) {
				jsonObject.remove(signField);
			}
		}
		List<String> keyList = new ArrayList<>(jsonObject.keySet());
		if (keyList.size() == 0) {
			return "";
		}
		Collections.sort(keyList);
		StringBuilder builder = new StringBuilder();
		for (String key : keyList) {
			if (lowerName) {
				key = StringUtils.lowerCase(key);
			}
			String value = jsonObject.getString(key);
			if (!StringUtils.isEmpty(value) && urlEncodeValue) {
				try {
					value = URLEncoder.encode(value, RequestUtils.ENCODING).replace("+", "%20");
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (urlEncodeName) {
				try {
					key = URLEncoder.encode(key, RequestUtils.ENCODING).replace("+", "%20");
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (upperCaseFirstChar) {
				if (key.length() == 1) {
					key = Character.toUpperCase(key.charAt(0)) + "";
				} else if (key.length() > 1) {
					key = Character.toUpperCase(key.charAt(0)) + key.substring(1);
				}
			}
			if (!StringUtils.isEmpty(value)) {
				builder.append(key).append(REQUEST_EQUAL).append(value).append(REQUEST_SEPARATOR);
			} else if (withEmpty) {
				builder.append(key).append(REQUEST_EQUAL).append("").append(REQUEST_SEPARATOR);
			}
		}
		builder.delete(builder.length() - REQUEST_SEPARATOR.length(), builder.length());
		return builder.toString();
	}

	public static String getRequestHeaders(HttpServletRequest hRequest) {
		Enumeration<String> headerNames = hRequest.getHeaderNames();
		JSONObject headerObject = new JSONObject();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headerObject.put(headerName, hRequest.getHeader(headerName));
		}
		String headerString = JSON.toJSONString(headerObject);
		return headerString;
	}
}
