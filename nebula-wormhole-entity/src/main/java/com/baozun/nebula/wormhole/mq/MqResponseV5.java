package com.baozun.nebula.wormhole.mq;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 反馈消息体
 * 将这个数据生成json,再进行aes加密后，放入mqMsgBody的content中
 * @author Justin Hu
 *
 */
public class MqResponseV5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1546387719527254549L;

    /**
     * 消息id
     */
    private String msgId;
    
    /**
     * 接口标识
     */
    private String  ifIdentify;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getIfIdentify() {
		return ifIdentify;
	}

	public void setIfIdentify(String ifIdentify) {
		this.ifIdentify = ifIdentify;
	}

    
    
}
