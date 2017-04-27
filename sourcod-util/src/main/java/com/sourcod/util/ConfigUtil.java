package com.sourcod.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;


/**
 * 配置文件工具类
 * 
 * @author willeam
 * @time 2016-12-07 10:10:59
 */
public class ConfigUtil {

	private static final Logger logger = Logger.getLogger(ConfigUtil.class);

	static Properties prop = new Properties();

	// 是否开启开发模式
	private static boolean developMode = true;

	static {
		try {
			logger.info("开始加载配置文件！");
			// 获取config配置文件夹路径
			File file = new File(ConfigUtil.class.getResource("/conf").getPath());
			for (File f : file.listFiles()) {
				// 只加载properties配置文件
				if (f.getName().indexOf("properties") != -1) {
					InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream("conf/" + f.getName());
					prop.load(in);
					logger.info("加载配置文件" + f.getName() + "成功！");
				}
			}
		} catch (Exception e) {
			logger.error("读取配置文件错误", e);
		}

		if ("true".equals(prop.getProperty("DEVELOP_MODE"))) {
			developMode = true;
		} else {
			developMode = false;
		}
	}

	public static String getValueByKey(String key) {
		if (key == null)
			return null;
		return prop.getProperty(key);
	}

	/**
	 * @Title: isDevelopMode
	 * @Description: 是否开启了开发模式，默认为true
	 * @return boolean
	 */
	public static boolean isDevelopMode() {
		return developMode;
	}
}
