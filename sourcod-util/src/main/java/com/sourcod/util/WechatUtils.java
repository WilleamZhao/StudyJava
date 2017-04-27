package com.sourcod.util;

import com.sourcod.wechat.model.MessageModel;
import com.sourcod.wechat.util.aes.AesException;
import com.sourcod.wechat.util.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 微信工具类
 * @author willeam
 *
 */
public class WechatUtils {

	private static Logger logger = LoggerFactory.getLogger(WechatUtils.class);

	private static String encodingAesKey = ConfigUtil.getValueByKey("encodingAesKey");
	private static String token = ConfigUtil.getValueByKey("token");
	private static String appId = ConfigUtil.getValueByKey("appId");

	private static WXBizMsgCrypt pc = null;
	static String access_token;

	WechatUtils() {
		try {
			pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
		} catch (AesException e) {
			e.printStackTrace();
		}
	}

	// 获取消息类
	public MessageModel getMessageModel(String encryptType, String msgSignature, String timestamp, String nonce,
			BufferedReader in) {
		StringBuilder xmlMsg = new StringBuilder();
		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				xmlMsg.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("获取到xml{}", xmlMsg.toString());
		MessageModel mm = (MessageModel) GeneralUtil.getXml(xmlMsg.toString());
		// 查询用户是否在表里
		// 如果不再新增
		String result = "";
		// raw不加密
		if ("aes".equals(encryptType)) {
			logger.info("aes加密");
			try {
				result = pc.decryptMsg(msgSignature, timestamp, nonce, xmlMsg.toString());
				logger.info("解密后明文:{}", result);
				mm = (MessageModel) GeneralUtil.getXml(result);
			} catch (AesException e) {
				logger.error("错误", e);
				return null;
			}
		}
		return mm;
	}
	
	// 返回消息
	public String setMessageModel(String message, MessageModel mm, String nonce){
		MessageModel returnMM = new MessageModel();
		returnMM.setToUserName(mm.getFromUserName());
		returnMM.setFromUserName(mm.getToUserName());
		returnMM.setContent(message);
		returnMM.setCreateTime(mm.getCreateTime());
		returnMM.setMsgType("text");
		String returnMsg = GeneralUtil.toXml(returnMM);
		logger.info("返回消息{}", returnMsg);
		try {
			return pc.encryptMsg(returnMsg, mm.getCreateTime(), nonce);
		} catch (AesException e) {
			logger.error("加密失败", e);
			return "";
		}
	}
}
