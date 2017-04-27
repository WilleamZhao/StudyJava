package com.sourcod.util;

import com.sourcod.model.Dama2Model;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Dama2Util {

	private static final Logger logger = LoggerFactory.getLogger(Dama2Util.class);

	private static String key = ConfigUtil.getValueByKey("dama2Key");
	private static String username = ConfigUtil.getValueByKey("dama2Username");
	private static String password = ConfigUtil.getValueByKey("dama2Password");
	private static String serverUrl = ConfigUtil.getValueByKey("dama2ServerUrl");
	private static String resultServerUrl = ConfigUtil.getValueByKey("dama2ResultServerUrl");
	private static String reportErrorUrl = ConfigUtil.getValueByKey("dama2ReportErrorUrl");
	private static String appID = ConfigUtil.getValueByKey("dama2AppID");
	private static String timeout = ConfigUtil.getValueByKey("dama2Timeout");
	private static String type = ConfigUtil.getValueByKey("dama2Timeout");
	private static String pwd = EncryptionUtil.MD5(key + EncryptionUtil.EncoderByMd5(EncryptionUtil.EncoderByMd5(username) + EncryptionUtil.EncoderByMd5(password))).substring(0, 8);

	public Dama2Util(String imageUrl) {

	}

	private static String getSign(String url) {
		return EncryptionUtil.MD5(key + username + url).substring(0, 8);
	}

	/**
	 * 获取验证码
	 * 
	 * @author willeam
	 * @time 2016-12-07 16:25:30
	 * @param url
	 * @return randCode
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String getVaildateCode(String url) throws IOException, URISyntaxException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String sign = Dama2Util.getSign(url);
		nvps.add(new BasicNameValuePair("appID", appID));
		nvps.add(new BasicNameValuePair("user", username));
		nvps.add(new BasicNameValuePair("pwd", pwd));
		nvps.add(new BasicNameValuePair("type", type));
		nvps.add(new BasicNameValuePair("timeout", timeout));
		nvps.add(new BasicNameValuePair("sign", sign));
		nvps.add(new BasicNameValuePair("url", URLEncoder.encode(url, "utf-8")));
		HttpResponse response = HttpClientUtil.HttpsPost(serverUrl, nvps);
		if (response.getEntity().isStreaming()) {
			logger.info("isNotNull");
		}
		String test = EntityUtils.toString(response.getEntity());
		logger.info(test);
		String result = GeneralUtil.getString(response.getEntity().getContent());
		String randCode = "";
		Dama2Model dm2 = GeneralUtil.StringToJson(result, Dama2Model.class);
		// TODO 判断打码兔是否返回坐标
		if (StringUtils.isEmpty(result)) {
			nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("appID", appID));
			nvps.add(new BasicNameValuePair("user", username));
			nvps.add(new BasicNameValuePair("pwd", pwd));
			nvps.add(new BasicNameValuePair("id", dm2.getId()));
			nvps.add(new BasicNameValuePair("sign", sign));
			response = HttpClientUtil.HttpsPost(resultServerUrl, nvps);
			result = GeneralUtil.getString(response.getEntity().getContent());
			dm2 = GeneralUtil.StringToJson(result, Dama2Model.class);
		}
		String[] randCodes = dm2.getResult().split("\\|");
		for (int j = 0; j < randCodes.length; j++) {
			int q = 1;
			for (String rands : randCodes[j].split(",")) {
				if (q % 2 == 0) {
					randCode += Integer.parseInt(rands) - 30 + ",";
				} else {
					randCode += rands + ",";
				}
				q++;
			}
		}
		randCode = randCode.substring(0, randCode.length() - 1);
		return randCode;
	}

	public static String getRandCode(HttpResponse response) throws ParseException, IOException {
		String test = EntityUtils.toString(response.getEntity());
		logger.info(test);
		String result = GeneralUtil.getString(response.getEntity().getContent());

		return "";
	}

	public static Dama2Model getDama2Model(String result) throws IOException {
		Dama2Model dm2 = GeneralUtil.StringToJson(result, Dama2Model.class);
		return dm2;
	}

	public static void reportError(String url, String id) {
		String sign = Dama2Util.getSign(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("appID", appID));
		nvps.add(new BasicNameValuePair("user", username));
		nvps.add(new BasicNameValuePair("pwd", pwd));
		nvps.add(new BasicNameValuePair("id", id));
		nvps.add(new BasicNameValuePair("sign", sign));
		try {
			HttpClientUtil.HttpsPost(reportErrorUrl, nvps);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		logger.info("报告结果成功");
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		Dama2Util.getVaildateCode("");
	}

}
