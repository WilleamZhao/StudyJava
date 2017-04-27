package com.sourcod.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcod.wechat.model.MessageModel;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

/**
 * 通用工具类
 * 
 * @author willeam
 *
 */
public class GeneralUtil {

	private static final Logger logger = LoggerFactory.getLogger(GeneralUtil.class);

	private static JsonGenerator jsonGenerator = null;
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String getKeyByValue() {
		return "";
	}

	/**
	 * 打印内容到页面
	 * 
	 * @author willeam
	 * @param response
	 * @param content
	 *            内容
	 */
	public static void write(HttpServletResponse response, String content) {
		try {
			response.getWriter().write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * json转javaBean
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static Object StringToJson(String json) throws IOException {
		return objectMapper.readValue(json, Object.class);
	}

	/**
	 * json转javaBean
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static <T> T StringToJson(String json, Class<T> c) throws IOException {
		return objectMapper.readValue(json, c);
	}

	/**
	 * javaBean转json
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String BeanToString(Object obj) throws IOException {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * TODO
	 * 
	 * @author zcj
	 * @time 2016-12-07 16:08:10
	 * @param ips
	 * @return 转换后InputStream
	 */
	public static String getString(InputStream ips) {
		// 取前两个字节
		byte[] header = new byte[2];
		try {
			BufferedInputStream bis = new BufferedInputStream(ips);
			bis.mark(2);
			int result = bis.read(header);
			// reset输入流到开始位置
			bis.reset();
			// 判断是否是GZIP格式
			int ss = (header[0] & 0xff) | ((header[1] & 0xff) << 8);
			if (result != -1 && ss == GZIPInputStream.GZIP_MAGIC) {
				logger.info("是gzip格式");
				return GeneralUtil.readInputStream(new GZIPInputStream(bis));
			}
			return GeneralUtil.readInputStream(bis);
		} catch (IOException e) {
			logger.error("判断是否是gzip异常", e);
			return null;
		}
	}

	/**
	 * 处理返回文件流
	 * 
	 * @author willeam
	 * @time 2016-12-07 16:13:26
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String readInputStream(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			buffer.append(line + "\n");
		}
		return buffer.toString();
	}

	public static Object getXml(InputStream input) {
		XStream xstream = new XStream();
		xstream.alias("xml", MessageModel.class);
		return xstream.fromXML(input, MessageModel.class);
	}

	public static Object getXml(String input) {
		XStream xstream = new XStream();
		xstream.alias("xml", MessageModel.class);
		return xstream.fromXML(input);
	}

	public static Object getXml(Reader input) {
		XStream xstream = new XStream();
		xstream.alias("xml", MessageModel.class);
		return xstream.fromXML(input, MessageModel.class);
	}

	public static String toXml(MessageModel mm) {
		XStream xstream = new XStream();
		xstream.alias("xml", MessageModel.class);
		return xstream.toXML(mm);
	}
	
	/**
	 * urf-8 转 iso-8859-1
	 * @author willeam
	 * @param str	要转换的字符串
	 * @return		转换后的字符串
	 */
	public static String utf8ToIso88591(String str){
		return StringUtils.newStringIso8859_1(StringUtils.getBytesUtf8(str));
	}

	public static void main(String[] args) throws IOException {
		MessageModel m = new MessageModel();
		m = GeneralUtil.StringToJson("{\"toUserName\":\"123\",\"fromUserName\":\"123\",\"createTime\":\"123\"}",
				MessageModel.class);
		System.out.println(m.getToUserName());
		System.out.println(GeneralUtil.BeanToString(m));
		Object o = GeneralUtil.StringToJson("{\"toUserName\":\"123\",\"fromUserName\":\"123\",\"createTime\":\"123\"}",
				Object.class);
		System.out.println(o);
		// jsonGenerator.writeString("{\"toUserName\":\"333\",\"fromUserName\":\"444\",\"createTime\":\"123\"}");
		objectMapper.writeValue(System.out, m);
		// System.out.println(jsonGenerator.writeString(""));asdaaa aa

	}
}
