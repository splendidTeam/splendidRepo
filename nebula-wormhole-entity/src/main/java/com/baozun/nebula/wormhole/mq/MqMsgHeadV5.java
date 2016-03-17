package com.baozun.nebula.wormhole.mq;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 消息头，包含消息标识，接口标识，签名等
 * @author Justin Hu
 *
 */
public class MqMsgHeadV5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8438413423820524869L;

    /**
     * 消息唯一标识(也就是消息发送日志的id,重发时MSGID还是原来一样的)
     */
    private String msgId;
    
    /**
     * 接口标识,具体哪个接口的标识，见接口列表
     * 
     */
    private String ifIdentify;
    
    /**
     * 店铺,SCM中的店铺
     * 
     */
    private String shop;
    
    
    /**
     * 接口版本,当前为5.0
     */
    private String ifVersion;
    
    
    /**
     * 消息发起时间,当前发送消息的时间
     */
    private String msgSendTime;
    
    /**
     * SCM帐号，SCM使用帐号来找到密钥
     */
    private String account;
    
    /**
     * :消息签名,消息头除签名外+定义的密钥，再进行hash生成签名
     */
    private String sign;
    

 
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }



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

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getIfVersion() {
		return ifVersion;
	}

	public void setIfVersion(String ifVersion) {
		this.ifVersion = ifVersion;
	}

	public String getMsgSendTime() {
		return msgSendTime;
	}

	public void setMsgSendTime(String msgSendTime) {
		this.msgSendTime = msgSendTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

   
    
}
