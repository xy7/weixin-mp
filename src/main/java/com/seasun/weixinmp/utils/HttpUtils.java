/**
 *
 */
package com.seasun.weixinmp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

/**
 * @author LUCHUNLIANG
 *
 */
public class HttpUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
	public static final String REQUEST_CONTENTTYPE = "Content-Type";
	public static final int DEFAULT_CONNECT_TIMEOUT = 20000;
	public static final int DEFAULT_SOCKET_TIMEOUT = 20000;
	public static final String GAME_CHARSET = "UTF-8";

	private static final int DEFAULT_MAX_CONNECTIONS = 10000;
	private static final int DEFAULT_MAX_PER_ROUTE = 500;
	private static PoolingHttpClientConnectionManager httpClientConnectionMgr;
	private static CloseableHttpClient httpClient;
	private static boolean shutdown;

	static {
		try {
			initHttpClientNew();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("Error init http client, all of succeeding http requests CAN NOT work, MUST fix it immediately!!!.");
		}
		startConnectionEvictionTask();
	}

	private static void initHttpClientNew() throws Exception {
		SSLContextBuilder sslContextBuilder = SSLContexts.custom().useProtocol("TLS");
		File trustStoreFile = getTrustStoreFile();
		if (trustStoreFile != null) {
			sslContextBuilder.loadTrustMaterial(getTrustStoreFile(), "xgsdk.com".toCharArray());
		}
		SSLContext sslContext = sslContextBuilder.build();
		X509TrustManager tm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		};
		sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		ConnectionSocketFactory plainsf = new PlainConnectionSocketFactory();
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainsf).register("https", sslsf).build();
		httpClientConnectionMgr = new PoolingHttpClientConnectionManager(r);
		httpClientConnectionMgr.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
		httpClientConnectionMgr.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);

		RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
		httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionMgr).setDefaultRequestConfig(config).build();
	}

	private static File getTrustStoreFile() throws IOException {
		ClassPathResource resource = new ClassPathResource("xgsdk.trust");
		try {
			return resource.getFile();
		} catch (Exception ex) {
			File temp = File.createTempFile("xgsdk.trust", ".tmp");
			FileCopyUtils.copy(resource.getInputStream(), new FileOutputStream(temp));
			return temp;
		}
	}

	private static void startConnectionEvictionTask() {
		Thread cleanListener = new Thread("Connection Eviction Listener") {
			@Override
			public void run() {
				while (!shutdown) {
					try {
						sleep(5000);
						httpClientConnectionMgr.closeExpiredConnections();
						httpClientConnectionMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		};
		cleanListener.start();
	}

	@Override
	protected void finalize() throws Throwable {
		shutdown = true;

		if (httpClient != null) {
			httpClient.close();
		}
		if (httpClient != null) {
			httpClientConnectionMgr.close();
		}
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	public static String doPostForm(String url, String body) {
		return doPostForm(url, body, null);
	}

	public static String doPostJson(String url, String body) {
		return doPostJson(url, body, null);
	}

	public static String doGet(String url, Map<String, String> headers) {
		return doGet(url, headers, null);
	}

	public static String doGet(String url, Map<String, String> headers, Map<String, String> cookies) {
		long beginTime = System.currentTimeMillis();
		log.info("start to send http request to [{}]. The header parameters are [{}].", url, headers);
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader in = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
			httpget.setConfig(config);
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					httpget.setHeader(entry.getKey(), entry.getValue());
				}
			}

			if (cookies != null && cookies.size() > 0) {
				StringBuffer buffer = new StringBuffer();
				for (Entry<String, String> entry : cookies.entrySet()) {
					buffer.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
				}
				buffer.deleteCharAt(buffer.length() - 1);
				String cookie = buffer.toString();
				httpget.setHeader("Cookie", cookie);
				log.info("cookie is {}", cookie);
			}

			response = httpClient.execute(httpget);
			if (response.getStatusLine().getStatusCode() != 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					log.error("Failed : HTTP error code : {}, response is : {}", response.getStatusLine().getStatusCode(), EntityUtils.toString(entity));
				} else {
					log.error("Failed : HTTP error code : {}, response is : {}", response.getStatusLine().getStatusCode(), response);
				}
				return null;
			}
			HttpEntity entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Throwable t) {
			log.error("error in sending http request to [{}]. The header parameters are [{}]. The error is [{}].", url, headers, t.getMessage());
			log.error(t.getMessage(), t);
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			try {
				if (response != null) {
					response.close();
					response = null;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		String responseString = stringBuffer.toString();
		log.info("end to send http request to [{}]. The header parameters are [{}]. The response is [{}]. Time is {}.", url, headers, responseString, System.currentTimeMillis() - beginTime);
		return responseString;
	}

	public static String doPostForm(String url, String body, Map<String, String> headers) {
		long beginTime = System.currentTimeMillis();
		log.info("start to send http request to [{}]. The body is [{}]. The header parameters are [{}].", url, body, headers);
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
			httppost.setConfig(config);
			if (headers == null) {
				headers = new HashMap<>();
			}
			String contentType = headers.get(REQUEST_CONTENTTYPE);
			if (contentType == null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					if (StringUtils.equalsIgnoreCase(entry.getKey(), REQUEST_CONTENTTYPE)) {
						contentType = entry.getValue();
						headers.remove(entry.getKey());
						break;
					}
				}
			} else {
				headers.remove(REQUEST_CONTENTTYPE);
			}
			if (contentType == null) {
				httppost.setHeader(REQUEST_CONTENTTYPE, "application/x-www-form-urlencoded");
			} else {
				httppost.setHeader(REQUEST_CONTENTTYPE, contentType);
			}
			for (Entry<String, String> entry : headers.entrySet()) {
				if (!httppost.containsHeader(entry.getKey()) && !StringUtils.equalsIgnoreCase("content-length", entry.getKey())) {
					httppost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
			response = httpClient.execute(httppost);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed : HTTP error code : {}, {}", response.getStatusLine().getStatusCode(), response);
				return null;
			}
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Throwable t) {
			log.error("error in sending http request to [{}]. The body is [{}]. The header parameters are [{}]. The error is [{}].", url, body, headers, t.getMessage());
			log.error(t.getMessage(), t);
		}
		String responseString = stringBuffer.toString();
		log.info("end to send http request to [{}]. The body is [{}]. The header parameters are [{}]. The response is [{}]. Time is {}.", url, body, headers, responseString, System.currentTimeMillis() - beginTime);
		return responseString;
	}

	public static String doPostJson(String url, String body, Map<String, String> headers) {
		long beginTime = System.currentTimeMillis();
		log.info("start to send http request to [{}]. The body is [{}]. The header parameters are [{}].", url, body, headers);
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader in = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
			httpPost.setConfig(config);
			httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=" + GAME_CHARSET);
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					if (!httpPost.containsHeader(entry.getKey()) && !StringUtils.equalsIgnoreCase("content-length", entry.getKey())) {
						httpPost.setHeader(entry.getKey(), entry.getValue());
					}
				}
			}
			httpPost.setEntity(new StringEntity(body == null ? "" : body, Charset.forName(GAME_CHARSET)));
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					log.error("Failed : HTTP error code : {}, response is : {}", response.getStatusLine().getStatusCode(), EntityUtils.toString(entity));
				} else {
					log.error("Failed : HTTP error code : {}, response is : {}", response.getStatusLine().getStatusCode(), response);
				}
				return null;
			}
			HttpEntity entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Throwable t) {
			log.error("error in sending http request to [{}]. The body is [{}]. The header parameters are [{}]. The error is [{}].", url, body, headers, t.getMessage());
			log.error(t.getMessage(), t);
		}
		String responseString = stringBuffer.toString();
		log.info("end to send http request to [{}]. The body is [{}]. The header parameters are [{}]. The response is [{}]. Time is {}.", url, body, headers, responseString, System.currentTimeMillis() - beginTime);
		return responseString;
	}

	public static String doPostXml(String url, String body, Map<String, String> headers) {
		long beginTime = System.currentTimeMillis();
		log.info("start to send http request to [{}]. The body is [{}]. The header parameters are [{}].", url, body, headers);
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader in = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
			httpPost.setConfig(config);
			httpPost.setHeader(HTTP.CONTENT_TYPE, "application/xml; charset=" + GAME_CHARSET);
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					if (!httpPost.containsHeader(entry.getKey()) && !StringUtils.equalsIgnoreCase("content-length", entry.getKey())) {
						httpPost.setHeader(entry.getKey(), entry.getValue());
					}
				}
			}
			httpPost.setEntity(new StringEntity(body == null ? "" : body, Charset.forName(GAME_CHARSET)));
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					log.error("Failed : HTTP error code : {}, response is : {}", response.getStatusLine().getStatusCode(), EntityUtils.toString(entity));
				} else {
					log.error("Failed : HTTP error code : {}, response is : {}", response.getStatusLine().getStatusCode(), response);
				}
				return null;
			}
			HttpEntity entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Throwable t) {
			log.error("error in sending http request to [{}]. The body is [{}]. The header parameters are [{}]. The error is [{}].", url, body, headers, t.getMessage());
			log.error(t.getMessage(), t);
		}
		String responseString = stringBuffer.toString();
		log.info("end to send http request to [{}]. The body is [{}]. The header parameters are [{}]. The response is [{}]. Time is {}.", url, body, headers, responseString, System.currentTimeMillis() - beginTime);
		return responseString;
	}

	public static String doPostWeixin(String url, String fileName, Map<String, String> headers) {
		long beginTime = System.currentTimeMillis();
		log.info("start to send http request to [{}]. The fileNames is [{}]. The header parameters are [{}].", url, fileName, headers);
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
			httppost.setConfig(config);
			if (headers == null) {
				headers = new HashMap<>();
			}
			for (Entry<String, String> entry : headers.entrySet()) {
				if (!httppost.containsHeader(entry.getKey()) && !StringUtils.equalsIgnoreCase("content-length", entry.getKey())) {
					httppost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			String boundary = "---------------------------" + new Date();
			httppost.setHeader(REQUEST_CONTENTTYPE, "multipart/form-data; boundary=" + boundary);
			httppost.setEntity(new ByteArrayEntity(getBytes(fileName, boundary)));
			response = httpClient.execute(httppost);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed : HTTP error code : {}, {}", response.getStatusLine().getStatusCode(), response);
				return null;
			}
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Throwable t) {
			log.error("error in sending http request to [{}]. The fileNames is [{}]. The header parameters are [{}]. The error is [{}].", url, fileName, headers, t.getMessage());
			log.error(t.getMessage(), t);
		}
		String responseString = stringBuffer.toString();
		log.info("end to send http request to [{}]. The fileNames is [{}]. The header parameters are [{}]. The response is [{}]. Time is {}.", url, fileName, headers, responseString, System.currentTimeMillis() - beginTime);
		return responseString;
	}

	private static byte[] getBytes(String fileName, String boundary) throws IOException {
		File file = new File(fileName);
		FileInputStream fileIs = new FileInputStream(file);
		byte[] fileStream = StreamUtils.copyToByteArray(fileIs);
		fileIs.close();
		StringBuilder sb = new StringBuilder();
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"media\"; filename=\"" + fileName + "\"; filelength=\"" + fileStream.length + "\"");
		sb.append("\r\n");
		sb.append("Content-Type: image/jpeg");
		sb.append("\r\n\r\n");
		String head = sb.toString();
		byte[] form_data = head.getBytes();

		// 缁撳熬
		byte[] foot_data = ("\r\n--" + boundary + "--\r\n").getBytes();

		// post鎬婚暱搴�
		int length = form_data.length + fileStream.length + foot_data.length;
		byte[] returnValue = new byte[length];
		System.arraycopy(form_data, 0, returnValue, 0, form_data.length);
		System.arraycopy(fileStream, 0, returnValue, form_data.length, fileStream.length);
		System.arraycopy(foot_data, 0, returnValue, form_data.length + fileStream.length, foot_data.length);
		return returnValue;

		// req.ContentLength = length;
		//
		// Stream requestStream = req.GetRequestStream();
		// //杩欓噷瑕佹敞鎰忎竴涓嬪彂閫侀『搴忥紝鍏堝彂閫乫orm_data > buffer > foot_data
		// //鍙戦�佽〃鍗曞弬鏁�
		// requestStream.Write(form_data, 0, form_data.Length);
		// //鍙戦�佹枃浠跺唴瀹�
		// requestStream.Write(buffer,0,buffer.Length);
		// //缁撳熬
		// requestStream.Write(foot_data, 0, foot_data.Length);
		//
		// requestStream.Close();
		// fileStream.Close();
		// fileStream.Dispose();
		// br.Close();
		// br.Dispose();
		// //鍝嶅簲
		// WebResponse pos = req.GetResponse();
		// StreamReader sr = new StreamReader(pos.GetResponseStream(),
		// Encoding.UTF8);
		// string html = sr.ReadToEnd().Trim();
		// sr.Close();
		// sr.Dispose();
		// if (pos != null)
		// {
		// pos.Close();
		// pos = null;
		// }
		// if (req != null)
		// {
		// req = null;
		// }
		// return html;
	}
}
