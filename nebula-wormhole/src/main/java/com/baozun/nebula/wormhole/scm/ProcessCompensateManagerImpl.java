package com.baozun.nebula.wormhole.scm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.wormhole.scm.makemsgcon.PropellingItemManager;
import com.baozun.nebula.wormhole.scm.makemsgcon.PropellingPickupInfoManager;
import com.baozun.nebula.wormhole.scm.makemsgcon.PropellingSalesOrderManager;

@Service("processCompensateManager")
@Transactional
public class ProcessCompensateManagerImpl implements ProcessCompensateManager{

    private static Logger               logger          = LoggerFactory.getLogger(ProcessCompensateManagerImpl.class);

    @Autowired
    private MessageCommonManager        messageCommonManager;

    @Autowired
    private PropellingItemManager       propellingItemManager;

    @Autowired
    private PropellingPickupInfoManager propellingPickupInfoManager;

    @Autowired
    private PropellingSalesOrderManager propellingSalesOrderManager;

    @Autowired
    private SdkMsgManager               sdkMsgManager;

    @Autowired
    private EmailTemplateManager        emailTemplateManager;

    static Properties                   emailProperties = null;

    /**
     * 处理单个内容
     * 
     * @param msr
     */
    private MsgSendContent makeSendContent(MsgSendRecord msr){
        if (msr.getIfIdentify().equalsIgnoreCase(IfIdentifyConstants.IDENTIFY_ITEM_ONSALE_SYNC)){
            return propellingItemManager.propellingOnSalesItems(msr);

        }else if (msr.getIfIdentify().equalsIgnoreCase(IfIdentifyConstants.IDENTIFY_ORDER_SEND)){
            return propellingSalesOrderManager.propellingSalesOrder(msr);
        }else if (msr.getIfIdentify().equalsIgnoreCase(IfIdentifyConstants.IDENTIFY_PAY_SEND)){
            return propellingSalesOrderManager.propellingPayment(msr);
        }else if (msr.getIfIdentify().equalsIgnoreCase(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC)){
            return propellingSalesOrderManager.propellingSoStatus(msr);
        }else if (msr.getIfIdentify().equalsIgnoreCase(IfIdentifyConstants.IDENTIFY_SF_TAKE_DATA_ONSITE)){
            return propellingPickupInfoManager.propellingPickupInfo(msr);
        }

        return null;
    }

    /**
     * 处理单个补偿
     * 
     * @param msr
     */
    @Override
    public void processCompensate(MsgSendRecord msr){

        if (msr.getSendCount().equals(0)){
            MsgSendContent msc = makeSendContent(msr);
            msr.setSendTime(new Date());
            messageCommonManager.sendToMq(msr, msc);
            msr.setSendCount(msr.getSendCount() + 1);

            sdkMsgManager.saveMsgSendRecord(msr);
        }
        //正常发送
        else if (msr.getSendCount() < 5){
            //每次重发间隔30分钟
            if (System.currentTimeMillis() - msr.getSendTime().getTime() > 1000 * 60 * 30){
                msr.setSendTime(new Date());
                messageCommonManager.sendToMq(msr);
                msr.setSendCount(msr.getSendCount() + 1);

                sdkMsgManager.saveMsgSendRecord(msr);
                logger.info("msg send again:" + new Date());
            }
        }
        //发送告警邮件
        else{

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(
                            "desc",
                            "发现超过五次未成功的SCM消息记录!<br/>msgId:" + msr.getId() + "</br>targetId:" + msr.getTargetId() + "<br/>接口标识:"
                                            + msr.getIfIdentify());
            //EmailEvent ev=new EmailEvent(this,email,"EMAIL_WARNING",map);
            //eventPublisher.publish(ev);
            //uniquecontent表示为消息发送,msr.getId()表示，某一个消息只会发送指定次数的报警
            emailTemplateManager.sendWarningEmail("EMAIL_WARNING", "MQ_MSG_SEND-" + msr.getId(), map);
        }
    }

}
