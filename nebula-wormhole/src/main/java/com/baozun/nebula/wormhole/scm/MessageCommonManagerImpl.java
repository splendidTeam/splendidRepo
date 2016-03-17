package com.baozun.nebula.wormhole.scm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import loxia.utils.DateUtil;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.wormhole.constants.CommonConstants;
import com.baozun.nebula.wormhole.mq.MqMsgBodyV5;
import com.baozun.nebula.wormhole.mq.MqMsgHeadV5;
import com.baozun.nebula.wormhole.mq.MqMsgV5;
import com.baozun.nebula.wormhole.mq.MqResponseV5;
import com.baozun.nebula.wormhole.utils.JacksonUtils;
import com.baozun.nebula.wormhole.utils.MsgUtils;

@Service("messageCommonManager")
public class MessageCommonManagerImpl implements MessageCommonManager {

	private static Logger logger = LoggerFactory.getLogger(MessageCommonManagerImpl.class);

	@Autowired
	private SdkMsgManager sdkMsgManager;

	@Autowired
	private JmsTemplate template;

	@Override
	public Boolean sendToMq(MsgSendRecord msgSendRecord) {
		return sendToMq(msgSendRecord, null);
	}

	@Override
	public Boolean sendToMq(MsgSendRecord msgSendRecord, MsgSendContent msgSendContent) {
		MqMsgV5 mqMsgV5 = new MqMsgV5();// 返回
		MqMsgHeadV5 mqMsgHeadV5 = new MqMsgHeadV5();// 返回头
		MqMsgBodyV5 mqMsgBodyV5 = new MqMsgBodyV5();// 返回体

		try {

			if (msgSendContent == null)
				msgSendContent = sdkMsgManager.findMsgSendContentById(msgSendRecord.getId());

			Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");// 读取配置文件
			String shop = pro.getProperty("scm.shop");// 获取shop
			String account = pro.getProperty("scm.account");// 获取shop

			/********** 头数据 **********/
			mqMsgHeadV5.setMsgId(String.valueOf(msgSendRecord.getId()));
			mqMsgHeadV5.setIfIdentify(msgSendRecord.getIfIdentify());
			mqMsgHeadV5.setShop(shop);
			mqMsgHeadV5.setAccount(account);
			mqMsgHeadV5.setIfVersion(CommonConstants.VERSION);
			mqMsgHeadV5.setMsgSendTime(DateUtil.format(msgSendRecord.getSendTime(), CommonConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));

			/*********** 体数据 *********/
			mqMsgBodyV5.setMsgContent(msgSendContent.getMsgBody());

			String sign = MsgUtils.makeSign(mqMsgHeadV5.getMsgId(), mqMsgHeadV5.getShop(), mqMsgHeadV5.getIfVersion(), mqMsgHeadV5.getIfIdentify(), mqMsgHeadV5.getMsgSendTime(), mqMsgHeadV5.getAccount());
			mqMsgHeadV5.setSign(sign);// 补充签名字段

			/************ 返回对象 *******/
			mqMsgV5.setHead(mqMsgHeadV5);
			mqMsgV5.setBody(mqMsgBodyV5);

			String jsonStr = JacksonUtils.getObjectMapper().writeValueAsString(mqMsgV5);

			String content = EncryptUtil.getInstance().base64Encode(jsonStr);
			String destination = MsgUtils.msgDestination(mqMsgHeadV5.getIfIdentify());// 接口标识
			template.convertAndSend(destination, content);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public void receiveFeedback(String content) {
		
		
		String feedBackStr = null;
		try {
			Thread.sleep(1000);
			String jsonStr = EncryptUtil.getInstance().base64Decode(content);
			feedBackStr = jsonStr;
			MqMsgV5 mqMsgV5 = JacksonUtils.getObjectMapper().readValue(jsonStr, new TypeReference<MqMsgV5>() {
			});

			MqMsgHeadV5 mqMsgHeadV5 = mqMsgV5.getHead();
			MqMsgBodyV5 mqMsgBodyV5 = mqMsgV5.getBody();

			String sign = MsgUtils.makeSign(mqMsgHeadV5.getMsgId(), mqMsgHeadV5.getShop(), mqMsgHeadV5.getIfVersion(), mqMsgHeadV5.getIfIdentify(), mqMsgHeadV5.getMsgSendTime(),mqMsgHeadV5.getAccount());

			if (!mqMsgHeadV5.getSign().equals(sign)) {
				logger.error("接收Feedback消息内容验证失败！:scmsign：[" + mqMsgHeadV5.getSign() + "], nebulasign:[" + sign +"]");
				logger.error("原始消息为：" + jsonStr);
				return ;
			}

			String msgContent = mqMsgBodyV5.getMsgContent();
			msgContent = EncryptUtil.getInstance().decrypt(msgContent);// 解密
			MqResponseV5 mqResponseV5 = JacksonUtils.getObjectMapper().readValue(msgContent, new TypeReference<MqResponseV5>() {
			});

			Long msgId = Long.parseLong(mqResponseV5.getMsgId());
			MsgSendRecord msgSendRecord = sdkMsgManager.findMsgSendRecordById(msgId);

			if (null == msgSendRecord) {
				logger.warn("返回的记录没有行ID。msgId:"+msgId+" IfIdentify:"+mqMsgHeadV5.getIfIdentify());
				return ;
			}

			//如果消息没有反馈过，才会保存反馈时间
			if(msgSendRecord.getFeedbackTime()==null){
				msgSendRecord.setFeedbackTime(new Date());
	
				sdkMsgManager.saveMsgSendRecord(msgSendRecord);
			}

			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		    logger.info(feedBackStr);
		}
	}

	@Override
	public String receiveFromScm(String content) {
		String msgInfo = "";
		try {
			String jsonStr = EncryptUtil.getInstance().base64Decode(content);
			msgInfo = jsonStr;
			MqMsgV5 mqMsgV5 = JacksonUtils.getObjectMapper().readValue(jsonStr, new TypeReference<MqMsgV5>() {
			});

			MqMsgHeadV5 mqMsgHeadV5 = mqMsgV5.getHead();
			MqMsgBodyV5 mqMsgBodyV5 = mqMsgV5.getBody();

			String sign = MsgUtils.makeSign(mqMsgHeadV5.getMsgId(), mqMsgHeadV5.getShop(), mqMsgHeadV5.getIfVersion(), mqMsgHeadV5.getIfIdentify(), mqMsgHeadV5.getMsgSendTime(), mqMsgHeadV5.getAccount());

			if (!mqMsgHeadV5.getSign().equals(sign)) {
				logger.error("接收消息内容验证失败！:scmsign：[" + mqMsgHeadV5.getSign() + "], nebulasign:[" + sign +"]");
				logger.error("原始消息为：" + jsonStr);
				return null;
			}
			
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("msgId", mqMsgHeadV5.getMsgId());
			List<MsgReceiveContent> conList=sdkMsgManager.findMsgReceiveContentListByQueryMap(paramMap);
			//如果找不到相同msgid的内容，表示没有重复，可以插入数据
			//也就是去重
			if(conList==null||conList.size()==0){
				MsgReceiveContent msgReceiveContent = new MsgReceiveContent();
				msgReceiveContent.setIfIdentify(mqMsgHeadV5.getIfIdentify());
				msgReceiveContent.setSendTime(DateUtil.parse(mqMsgHeadV5.getMsgSendTime(), CommonConstants.PATTERN_YYYY_MM_DD_HH_MM_SS));
				msgReceiveContent.setMsgId(mqMsgHeadV5.getMsgId());
				msgReceiveContent.setMsgBody(mqMsgBodyV5.getMsgContent());
				msgReceiveContent.setIsProccessed(false);
	
				MsgReceiveContent msgReceiveContentSave = sdkMsgManager.saveMsgReceiveContent(msgReceiveContent);
				if (null == msgReceiveContentSave) {
					logger.error("保存msgReceiveContentSave出错:"+content);
					return null;
				}
			}

			/******** 新的组成结构返回 *******/

			MqResponseV5 mqResponseV5 = new MqResponseV5();
			mqResponseV5.setMsgId(mqMsgHeadV5.getMsgId());
			mqResponseV5.setIfIdentify(mqMsgHeadV5.getIfIdentify());

			String josnString = JacksonUtils.getObjectMapper().writeValueAsString(mqResponseV5);
			String aesStringContent = EncryptUtil.getInstance().encrypt(josnString);// 加密后新的内容值

			mqMsgBodyV5.setMsgContent(aesStringContent);
			sign = MsgUtils.makeSign(mqMsgHeadV5.getMsgId(), mqMsgHeadV5.getShop(), mqMsgHeadV5.getIfVersion(), mqMsgHeadV5.getIfIdentify(), mqMsgHeadV5.getMsgSendTime(), mqMsgHeadV5.getAccount());

			mqMsgHeadV5.setSign(sign);

			String jsonStrMqMsg = JacksonUtils.getObjectMapper().writeValueAsString(mqMsgV5);// 新的返回对象值json格式
			String msgStr=EncryptUtil.getInstance().base64Encode(jsonStrMqMsg);
			return msgStr;
		} catch (Exception e) {
			logger.info(msgInfo);
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}
