package com.baozun.nebula.wormhole.mq.entity.sku;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SCM同步SKU信息到官方商城用于构造商品基础信息
 * 在商品新增时才会触发
 * @author Justin Hu
 *
 */

public class SkuInfoV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2450488766376295838L;
	
	
    /**
     * 供应商商品编码(到款),也就是货号
     */
    private String supplierSkuCode;
    
    
    /**
     * oms商品编码(到款),也就是宝尊编码
     */
    private String jmCode;
	
	  /**
     * sku唯一标识(到尺码,颜色)
     */
    private String extentionCode;
	
    
    /**
     * 颜色,尺码
     * 如:红色,170 
     * 这一块各商城需要定制化
     * 其中颜色可能是中文，也可能是编码
     * 尺码也是一样
     */
    private String keyProperties;
    
    
    /**
     * 商品名
     */
    private String name;
    

    /**
     * 条码(供应商到颜色尺码)
     */
    
    private String barCode;
        
    /**
     * 商品动态属性 json格式：也是附加信息
     * {"mic_busgrp":"Hardware","mic_isphys":"TRUE","mic_revsumdiv":"Accessories","mic_mediatype"
     * :"Download","mic_issoft":"FALSE","mic_prodfam":"MsFam3","mic_ismsft":"TRUE"}
     */
    private String extProps;
    
 
    public String getJmCode() {
        return jmCode;
    }

    public void setJmCode(String jmCode) {
        this.jmCode = jmCode;
    }

    public String getSupplierSkuCode() {
        return supplierSkuCode;
    }

    public void setSupplierSkuCode(String supplierSkuCode) {
        this.supplierSkuCode = supplierSkuCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getExtentionCode() {
        return extentionCode;
    }

    public void setExtentionCode(String extentionCode) {
        this.extentionCode = extentionCode;
    }

    public String getKeyProperties() {
        return keyProperties;
    }

    public void setKeyProperties(String keyProperties) {
        this.keyProperties = keyProperties;
    }


    public String getExtProps() {
        return extProps;
    }

    public void setExtProps(String extProps) {
        this.extProps = extProps;
    }

}
