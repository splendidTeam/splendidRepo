package com.baozun.nebula.utilities.integration.payment.wechat;

/**
 * 微信接口返回数据中的key
 * 
 */
public class WechatResponseKeyConstants{

    /** 签名 */
    public static final String SIGN = "sign";

    /** 返回状态码 */
    public static final String RETURN_CODE = "return_code";

    /** 业务结果 */
    public static final String RESULT_CODE = "result_code";

    /** 预支付交易会话标识 */
    public static final String PREPAY_ID = "prepay_id";

    /** 错误代码 */
    public static final String ERR_CODE = "err_code";

    /** 返回信息 */
    public static final String RETURN_MSG = "return_msg";

    /**
     * 订单金额 total_fee 是 Int 100 订单总金额，单位为分.
     * 
     * @since 5.3.2.18
     */
    public static final String TOTAL_FEE = "total_fee";

    /** 错误代码描述 */
    public static final String ERR_CODE_DES = "err_code_des";

    /** 交易状态 */
    public static final String TRADE_STATE = "trade_state";

    /** 商户交易号 */
    public static final String OUT_TRADE_NO = "out_trade_no";

    /** 微信支付订单号 */
    public static final String TRANSACTION_ID = "transaction_id";

    /** 商户号 */
    public static final String MCH_ID = "mch_id";

    /** 公众账号ID **/
    public static final String APPID = "appid";

    /** 用户标识 */
    public static final String OPENID = "openid";

    /** 设备号 **/
    public static final String DEVICE_INFO = "device_info";

    /** 随机字符串 **/
    public static final String NONCE_STR = "nonce_str";

    /** 交易类型 **/
    public static final String TRADE_TYPE = "trade_type";

    /** 二维码链接 **/
    public static final String CODE_URL = "code_url";

    /** 交易类型JSAPI **/
    public static final String TRADE_TYPE_JSAPI = "JSAPI";

    /** 交易类型NATIVE **/
    public static final String TRADE_TYPE_NATIVE = "NATIVE";

    /** 交易类型APP **/
    public static final String TRADE_TYPE_APP = "APP";

    /** 交易类型WAP **/
    public static final String TRADE_TYPE_WAP = "WAP";

}
