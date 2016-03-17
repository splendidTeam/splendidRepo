package com.baozun.nebula.wormhole.mq;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 消息体,数据内容
 * @author Justin Hu
 *
 */
public class MqMsgBodyV5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7305836407570770703L;
    
    private String msgContent;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    
}
