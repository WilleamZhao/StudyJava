package com.sourcod.service;


import com.sourcod.model.ResponseData;
import com.sourcod.util.JsonUtil;

/**
 * 订单枚举类
 * Created by sourcod on 2017/4/18.
 */
public enum OrderEnum {

    SUCCESS_ENUM("0", "00000", "操作成功", "操作成功"),

    FAIL_ENUM("0", "11111", "操作失败", "操作失败"),

    PARAM_USER_ID_NULL_ENUM("1", "10001", "用户ID为空", "用户ID为空"),

    PARAM_ORDER_NO_NULL_ENUM("1", "10002", "订单号为空", "订单号为空")
    ;

    /**
     * 调用结果
     */
    private String callStatus;
    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误名称
     */
    private String errorName;
    /**
     * 错误描述
     */
    private String errorDesc;

    private OrderEnum(String callStatus, String errorCode, String errorName, String errorDesc) {
        this.callStatus = callStatus;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.errorName = errorName;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    /**
     * 获取返回数据
     *
     * @param em
     * @return
     */
    public static String getResponseData(OrderEnum em) {
        ResponseData res = new ResponseData();
        res.setCallStatus(em.getCallStatus());
        res.setReturnCode(em.getErrorCode());
        res.setReturnName(em.getErrorName());
        res.setReturnDesc(em.getErrorDesc());
        String resStr = JsonUtil.objectToJson(res);
        return resStr;
    }

}
