package com.baozun.nebula.wormhole.mq.entity;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 商品库存
 * 用于库存的全量以及增量更新
 * @author Justin Hu
 *
 */
public class SkuInventoryV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6898393931288433629L;

    /**
     * sku唯一标识(到尺码,颜色)
     */
    private String extentionCode;
    
    /**
     * 类型:1全量 2 增量
     */
    private int type;
    
    /**
     * 方向：1 入库,2 出库
     */
    private int direction;


    /**
     * 数量
     */
    private Long qty;

    /**
     * 库存日志生成时间
     */
    private Date opTime;




    public String getExtentionCode() {
        return extentionCode;
    }

    public void setExtentionCode(String extentionCode) {
        this.extentionCode = extentionCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }


}
