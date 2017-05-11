package com.sourcod.model;

import com.sourcod.util.JsonUtil;

import java.io.Serializable;

/**
 * 公共返回类
 * @author zcj
 * @time 2016-10-20 09:53:10
 *
 * @param <T>
 */
public class ResponseData<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6693762206950954399L;
	/**
	 * 
	 */
	private String returnCode;
	private String returnDesc;
	private String callStatus;
	private String returnName;

	private T obj;

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	@Override
	public String toString() {
		return JsonUtil.objectToJson(this);
	}
	
}