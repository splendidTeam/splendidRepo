package com.baozun.nebula.wormhole.mq;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 整个消息载体
 * @author Justin Hu
 *
 */
public class MqMsgV5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1546387719527254549L;

    /**
     * 消息头
     */
    private MqMsgHeadV5 head;
    
    /**
     * 消息体
     */
    private MqMsgBodyV5 body;

    public MqMsgHeadV5 getHead() {
        return head;
    }

    public void setHead(MqMsgHeadV5 head) {
        this.head = head;
    }

    public MqMsgBodyV5 getBody() {
        return body;
    }

    public void setBody(MqMsgBodyV5 body) {
        this.body = body;
    }
    
}
