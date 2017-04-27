package com.sourcod.util;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * MemCache工具类
 * 
 * @author willeam
 * @time 2016-12-08 13:19:51
 *
 */
public class MemCached {

	private static final Logger logger = LoggerFactory.getLogger(MemCached.class);

	private static MemcachedClient mcc = null;
	private final static String host = ConfigUtil.getValueByKey("memcachedHost");
	private final static String port = ConfigUtil.getValueByKey("memcachedProt");
	private final static String username = ConfigUtil.getValueByKey("memcachedUsername");
	private final static String password = ConfigUtil.getValueByKey("memcachedPassword");
	private final static int DEFAULT_TIMEOUT = Integer.parseInt(ConfigUtil.getValueByKey("memcachedTimeout"));
	private final static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
	static {
		try {
			AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
					new PlainCallbackHandler(username, password));
			if (mcc == null) {
				mcc = new MemcachedClient(
						new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build(),
						AddrUtil.getAddresses(host + ":" + port));
			}
		} catch (IOException e) {
			logger.error("MemCached:加载失败", e);
		}
		logger.info("MemCached:成功加载");
	}

	/**
	 * 超时时间0
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, Object value) {
		Future<Boolean> f = mcc.set(key, 0, value);
		return getBooleanValue(f);
	}

	/**
	 * memcache Set 方法
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public static boolean set(String key, Object value, int expire) {
		Future<Boolean> f = mcc.set(key, expire, value);
		return getBooleanValue(f);
	}

	/**
	 * memcache get 方法
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return mcc.get(key);
	}

	/**
	 * memcache 异步 get 方法
	 * 
	 * @param key
	 * @return
	 */
	public static Object asyncGet(String key) {
		Object obj = null;
		Future<Object> f = mcc.asyncGet(key);
		try {
			obj = f.get(MemCached.DEFAULT_TIMEOUT, MemCached.DEFAULT_TIMEUNIT);
		} catch (Exception e) {
			f.cancel(false);
		}
		return obj;
	}

	/**
	 * 超时时间0
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean add(String key, Object value) {
		Future<Boolean> f = mcc.add(key, 0, value);
		return getBooleanValue(f);
	}

	/**
	 * memcache add 方法
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public static boolean add(String key, Object value, int expire) {
		Future<Boolean> f = mcc.add(key, expire, value);
		return getBooleanValue(f);
	}

	/**
	 * memcacht 替换方法
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public static boolean replace(String key, Object value, int expire) {
		Future<Boolean> f = mcc.replace(key, expire, value);
		return getBooleanValue(f);
	}

	/**
	 * memcache 删除方法
	 * 
	 * @param key
	 * @return
	 */
	public static boolean delete(String key) {
		Future<Boolean> f = mcc.delete(key);
		return getBooleanValue(f);
	}

	/**
	 * memcache 删除所有方法
	 * 
	 * @return
	 */
	public static boolean flush() {
		Future<Boolean> f = mcc.flush();
		return getBooleanValue(f);
	}

	public static Map<String, Object> getMulti(Collection<String> keys) {
		return mcc.getBulk(keys);
	}

	public static Map<String, Object> getMulti(String[] keys) {
		return mcc.getBulk(keys);
	}

	public static Map<String, Object> asyncGetMulti(Collection<String> keys) {
		Map<String, Object> map = null;
		Future<Map<String, Object>> f = mcc.asyncGetBulk(keys);
		try {
			map = f.get(MemCached.DEFAULT_TIMEOUT, MemCached.DEFAULT_TIMEUNIT);
		} catch (Exception e) {
			f.cancel(false);
		}
		return map;
	}

	public static Map<String, Object> asyncGetMulti(String keys[]) {
		Map<String, Object> map = null;
		Future<Map<String, Object>> f = mcc.asyncGetBulk(keys);
		try {
			map = f.get(MemCached.DEFAULT_TIMEOUT, MemCached.DEFAULT_TIMEUNIT);
		} catch (Exception e) {
			f.cancel(false);
		}
		return map;
	}

	public static long increment(String key, int by, long defaultValue, int expire) {
		return mcc.incr(key, by, defaultValue, expire);
	}

	public static long increment(String key, int by) {
		return mcc.incr(key, by);
	}

	public static long decrement(String key, int by, long defaultValue, int expire) {
		return mcc.decr(key, by, defaultValue, expire);
	}

	public static long decrement(String key, int by) {
		return mcc.decr(key, by);
	}

	public static long asyncIncrement(String key, int by) {
		Future<Long> f = mcc.asyncIncr(key, by);
		return getLongValue(f);
	}

	public static long asyncDecrement(String key, int by) {
		Future<Long> f = mcc.asyncDecr(key, by);
		return getLongValue(f);
	}

	private static boolean getBooleanValue(Future<Boolean> f) {
		try {
			Boolean bool = f.get(5, MemCached.DEFAULT_TIMEUNIT);
			return bool.booleanValue();
		} catch (Exception e) {
			f.cancel(false);
			return false;
		}
	}

	private static long getLongValue(Future<Long> f) {
		try {
			Long l = f.get(MemCached.DEFAULT_TIMEOUT, MemCached.DEFAULT_TIMEUNIT);
			return l.longValue();
		} catch (Exception e) {
			f.cancel(false);
		}
		return -1;
	}

}
