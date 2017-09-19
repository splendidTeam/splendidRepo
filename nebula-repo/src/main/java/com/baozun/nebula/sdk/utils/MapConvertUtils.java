package com.baozun.nebula.sdk.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.feilong.tools.jsonlib.JsonUtil;

public class MapConvertUtils{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MapConvertUtils.class);

    public static String transMapObjToString(Map<String, Object> result){
        StringBuffer sb = new StringBuffer();
        for (Entry<String, Object> entry : result.entrySet()){
            sb.append(entry.getKey()).append("=").append(String.valueOf(entry.getValue())).append(" ");
        }

        return sb.toString();
    }

    public static String transPaymentResultToString(PaymentResult paymentResult){

        StringBuffer sb = new StringBuffer(StringUtils.EMPTY);
        if (null != paymentResult){
            sb.append("reponseValue= " + paymentResult.getResponseValue()).append("\t");
            sb.append("message= " + paymentResult.getMessage()).append(";paymentServiceSatus=").append(paymentResult.getPaymentServiceSatus()).append("\t");

            try{
                sb.append(objectParesToString(paymentResult.getPaymentStatusInformation()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 传入对象
     * 
     * @param obj
     *            前提obj存在set get方法
     * @return返回对象存在的属性值
     * @throws Exception
     */
    public static String objectParesToString(Object obj) throws Exception{
        StringBuffer sb = new StringBuffer();

        if (null != obj){
            Class userClass = Class.forName(obj.toString().split("@")[0]);// 加载类
            Field[] fields = userClass.getDeclaredFields();// 获得对象方法集合
            String fdname = null;
            Method metd = null;
            for (Field field : fields){// 遍历该数组
                fdname = field.getName();// 得到字段名，
                sb.append(fdname).append("=");
                try{
                    metd = userClass.getMethod("get" + change(fdname), null);// 根据字段名找到对应的get方法，null表示无参数
                    if (null != metd){
                        Object name = metd.invoke(obj, null);// 调用该字段的get方法
                        if (name != null){
                            sb.append(name).append(" ");
                        }
                    }
                }catch (NoSuchMethodException e){
                    // 不输出日志
                    // e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @param src
     *            源字符串
     * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
     */
    public static String change(String src){
        if (src != null){
            StringBuffer sb = new StringBuffer(src);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
        return null;
    }

    public static String transPaymentRequestToString(PaymentRequest paymentRequest){
        Validate.notNull(paymentRequest, "paymentRequest can't be null!");

        //---------------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(paymentRequest));
        }

        //---------------------------------------------------------------------

        StringBuffer sb = new StringBuffer();
        sb.append("requestURL= ").append(paymentRequest.getRequestURL()).append("\t\t");

        for (Entry<String, String> entry : paymentRequest.getPaymentParameters().entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }

        sb.append("\t").append(" requestType= ").append(paymentRequest.getRequestType()).append("\t");
        sb.append(" requestHtml= ").append(paymentRequest.getRequestHtml());

        return sb.toString();
    }

    public static String transMapToString(Map<String, String> result){
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : result.entrySet()){
            sb.append(entry.getKey()).append(" = ").append(entry.getValue());
        }
        return sb.toString();
    }
}

class Person{

    private Integer id;

    private String name;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}