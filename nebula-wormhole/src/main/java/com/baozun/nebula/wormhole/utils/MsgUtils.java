package com.baozun.nebula.wormhole.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 消息相关的utils 包含加密、签名、
 * 
 * @author Justin Hu
 * 
 */
public class MsgUtils {

	private static Logger logger = LoggerFactory.getLogger(MsgUtils.class);

	/**
	 * 通过参数值再加上密钥,生成hash
	 * 
	 * @param msgId
	 * @param shop
	 * @param ifVersion
	 * @param ifIdentify
	 * @param msgSendTime
	 * @param account scm帐号
	 * @return
	 */
	public static String makeSign(String msgId, String shop, String ifVersion, String ifIdentify, String msgSendTime, String account) {

		Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");// 读取配置文件
		String scmKey = pro.getProperty("scm.key");// 获取key

		StringBuffer sign = new StringBuffer(msgId);
		sign.append(account).append(ifIdentify).append(ifVersion).append(shop).append(msgSendTime).append(msgId);

		String makeSign = EncryptUtil.getInstance().hash(sign.toString(), scmKey);

		logger.info("scm生成后的Sign：" + makeSign);

		return makeSign;
	}

	/**
	 * list转成json
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String listToJson(List<T> t) {
		try {
			return JacksonUtils.getObjectMapper().writeValueAsString(t);
		} catch (IOException e) {
			logger.error("write to json string error:" + t, e);
			return null;
		}
	}

	/**
	 * json转成list
	 * 
	 * @param str
	 * @return
	 */
	public static <T> List<T> jsonToList(String str, Class<T> cls) {
		try {
			ObjectMapper mapper = JacksonUtils.getObjectMapper();
			JavaType javaType = JacksonUtils.getCollectionType(mapper, ArrayList.class, cls);
			return JacksonUtils.getObjectMapper().readValue(str, javaType);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
			return null;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 根据标识key查找配置文件里消息队列里的key
	 * 
	 * @param ifIdentify
	 *            接口标识
	 * @return 消息队列 key
	 */
	public static String msgDestination(String ifIdentify) {
		Properties pro = ProfileConfigUtil.findPro("config/MQ.properties");// 读取配置文件

		if (IfIdentifyConstants.IDENTIFY_ITEM_SYNC.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_P1_1");
		} else if (IfIdentifyConstants.IDENTIFY_ITEM_ONSALE_SYNC.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_P2_1");
		} else if (IfIdentifyConstants.IDENTIFY_ITEM_PRICE_SYNC.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_P1_2");
		} else if (IfIdentifyConstants.IDENTIFY_INVENTORY_ALL.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_I1_1");
		} else if (IfIdentifyConstants.IDENTIFY_INVENTORY_ADD.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_I1_2");
		} else if (IfIdentifyConstants.IDENTIFY_ORDER_SEND.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_O2_1");
		} else if (IfIdentifyConstants.IDENTIFY_PAY_SEND.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_O2_2");
		} else if (IfIdentifyConstants.IDENTIFY_STATUS_SCM2SHOP_SYNC.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_O1_1");
		} else if (IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_O2_3");
		} else if (IfIdentifyConstants.IDENTIFY_LOGISTICS_TRACKING.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_L1_1");
		} else if (IfIdentifyConstants.IDENTIFY_SF_TAKE_DATA_ONSITE.equals(ifIdentify)) {
			return pro.getProperty("QUEUE_L2_1");
		} else {
			return null;
		}
	}

}
