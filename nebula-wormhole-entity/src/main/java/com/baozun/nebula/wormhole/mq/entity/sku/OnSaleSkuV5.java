package com.baozun.nebula.wormhole.mq.entity.sku;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 在售商品
 * 
 * 官方商城告知SCM目前在售的商品列表，用于作为库存信息同步的数据范围
 * @author Justin Hu
 *
 */
public class OnSaleSkuV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7818333326705201118L;

	
	/**
     * sku平台对接编码
     */
    private String extensionCode;



    public String getExtensionCode() {
        return extensionCode;
    }

    public void setExtensionCode(String extensionCode) {
        this.extensionCode = extensionCode;
    }

}
