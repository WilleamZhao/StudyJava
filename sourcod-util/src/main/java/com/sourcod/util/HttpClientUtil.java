package com.sourcod.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * httpClient工具类
 * 
 * @author willeam
 *
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static HttpResponse HttpGet(String url, List<NameValuePair> nvps) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet();
		get.setURI(new URI(url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(nvps))));
		HttpResponse response = httpClient.execute(get);
		return response;
	}
	
	public static HttpResponse HttpGet(String url, List<NameValuePair> nvps, Header[] headers) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet();
		get.setURI(new URI(url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(nvps))));
		get.setHeaders(headers);
		HttpResponse response = httpClient.execute(get);
		return response;
	}

	public static HttpResponse HttpPost(String url, List<NameValuePair> nvps)
			throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost();
		post.setEntity(new UrlEncodedFormEntity(nvps));
		post.setURI(new URI(url));
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpResponse HttpsGet(String url) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpGet get = new HttpGet();
		get.setURI(new URI(url));
		HttpResponse response = httpClient.execute(get);
		return response;
	}

	public static HttpResponse HttpsGet(String url, String cookie) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpGet get = new HttpGet();
		get.setURI(new URI(url));
		get.addHeader(new BasicHeader("Cookie", cookie));
		HttpResponse response = httpClient.execute(get);
		return response;
	}

	public static HttpResponse HttpsPost(String url, List<NameValuePair> nvps, String cookie)
			throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost();
		/*
		 * String menu =
		 * "{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"},{\"type\":\"click\",\"name\":\"赞一下我们\",\"key\":\"V1001_GOOD\"}]}]}";
		 * post.setEntity(new StringEntity(menu, "UTF-8"));
		 */
		post.setEntity(new UrlEncodedFormEntity(nvps));
		post.addHeader(new BasicHeader("Cookie", cookie));
		post.setURI(new URI(url));
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpResponse HttpsPost(String url, List<NameValuePair> nvps) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost();
		post.setEntity(new UrlEncodedFormEntity(nvps));
		post.setURI(new URI(url));
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpResponse HttpsPost(String url, List<NameValuePair> nvps, StringEntity body)
			throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost();
		post.setEntity(body);
		post.setEntity(new UrlEncodedFormEntity(nvps));
		post.setURI(new URI(url));
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpResponse HttpsPost(String url, StringEntity body) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost();
		post.setEntity(body);
		post.setURI(new URI(url));
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpResponse HttpsPost(String url, String cookie) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost();
		post.addHeader(new BasicHeader("Cookie", cookie));
		post.setURI(new URI(url));
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	/**
	 * 创建ssl链接
	 * 
	 * @return
	 */
	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}

}
