/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.model.product;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * Sku.
 *
 * @author dianchao.song
 */
@Entity
@Table(name = "t_pd_sku")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Sku extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID  = -5833258263181441457L;

    /** The Constant LIFE_CYCLE_ENABLE. */
    public static final Integer LIFE_CYCLE_ENABLE = 1;

    /** PK. */
    private Long                id;

    /** 所属商品. */
    private Long                itemId;

    /** 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：[([商品属性值id])([,商品属性值id])], 比如[19,23] 这里的商品属性值id为ItemProperties实体的id. */
    private String              properties;

    /**
     * The properties name.
     *
     * @deprecated sku所对应的销售属性的中文名字串，格式如：pid1:vid1:pid_name1:vid_name1;pid2:vid2
     *             :pid_name2: vid_name2……
     */
    private String              propertiesName;

    /** 创建时间. */
    private Date                createTime;

    /** 修改时间. */
    private Date                modifyTime;

    /** version. */
    private Date                version;

    /** 外部编码. */
    private String              outid;

    /** 生命周期. */
    private Integer             lifecycle;

    /** 销售价. */
    private BigDecimal          salePrice;

    /** 吊牌价(原单价). */
    private BigDecimal          listPrice;

    /** 1 商品已上架 0 商品未上架. */
    private String              state;

    /**
     * 商品条码
     * by D.C 2016/4/12
     */
    private String              barcode;

    /**
     * Gets the pK.
     * 
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_PD_SKU",sequenceName = "S_T_PD_SKU",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_SKU")
    public Long getId(){
        return id;
    }

    /**
     * Sets the pK.
     * 
     * @param id
     *            the new pK
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    @Version
    @Column(name = "VERSION")
    public Date getVersion(){
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(Date version){
        this.version = version;
    }

    /**
     * Gets the 创建时间.
     * 
     * @return the createTime
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * Sets the 创建时间.
     * 
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 设置 修改时间.
     *
     * @param modifyTime
     *            the new 修改时间
     */
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    /**
     * 获得 修改时间.
     *
     * @return the 修改时间
     */
    @Column(name = "MODIFY_TIME")
    public Date getModifyTime(){
        return modifyTime;
    }

    /**
     * 设置 生命周期.
     *
     * @param lifecycle
     *            the new 生命周期
     */
    public void setLifecycle(Integer lifecycle){
        this.lifecycle = lifecycle;
    }

    /**
     * 获得 生命周期.
     *
     * @return the 生命周期
     */
    @Column(name = "LIFECYCLE")
    public Integer getLifecycle(){
        return lifecycle;
    }

    /**
     * 获得 所属商品.
     *
     * @return the 所属商品
     */
    @Column(name = "ITEM_ID")
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 所属商品.
     *
     * @param itemId
     *            the new 所属商品
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：[([商品属性值id])([,商品属性值id])], 比如[19,23] 这里的商品属性值id为ItemProperties实体的id.
     *
     * @return the 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：[([商品属性值id])([,商品属性值id])], 比如[19,23] 这里的商品属性值id为ItemProperties实体的id
     */
    @Column(name = "PROPERTIES")
    public String getProperties(){
        return properties;
    }

    /**
     * 设置 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：[([商品属性值id])([,商品属性值id])], 比如[19,23] 这里的商品属性值id为ItemProperties实体的id.
     *
     * @param properties
     *            the new 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：[([商品属性值id])([,商品属性值id])], 比如[19,23] 这里的商品属性值id为ItemProperties实体的id
     */
    public void setProperties(String properties){
        this.properties = properties;
    }

    /**
     * 获得 properties name.
     *
     * @return the properties name
     */
    @Column(name = "PROPERTIES_NAME")
    public String getPropertiesName(){
        return propertiesName;
    }

    /**
     * 设置 properties name.
     *
     * @param propertiesName
     *            the properties name
     */
    public void setPropertiesName(String propertiesName){
        this.propertiesName = propertiesName;
    }

    /**
     * 获得 外部编码.
     *
     * @return the 外部编码
     */
    @Column(name = "OUT_ID")
    public String getOutid(){
        return outid;
    }

    /**
     * 设置 外部编码.
     *
     * @param outid
     *            the new 外部编码
     */
    public void setOutid(String outid){
        this.outid = outid;
    }

    /**
     * 获得 销售价.
     *
     * @return the 销售价
     */
    @Column(name = "SALE_PRICE")
    public BigDecimal getSalePrice(){
        salePrice = salePrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        return salePrice;
    }

    /**
     * 设置 销售价.
     *
     * @param salePrice
     *            the new 销售价
     */
    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    /**
     * 获得 吊牌价(原单价).
     *
     * @return the 吊牌价(原单价)
     */
    @Column(name = "LIST_PRICE")
    public BigDecimal getListPrice(){
        if (listPrice != null){
            listPrice = listPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return listPrice;
    }

    /**
     * 设置 吊牌价(原单价).
     *
     * @param listPrice
     *            the new 吊牌价(原单价)
     */
    public void setListPrice(BigDecimal listPrice){
        this.listPrice = listPrice;
    }

    /**
     * 获得 1 商品已上架 0 商品未上架.
     *
     * @return the 1 商品已上架 0 商品未上架
     */
    @Column(name = "STATE")
    public String getState(){
        return state;
    }

    /**
     * 设置 1 商品已上架 0 商品未上架.
     *
     * @param state
     *            the new 1 商品已上架 0 商品未上架
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * 获得 商品条码 by D.
     *
     * @return the 商品条码 by D
     */
    @Column(name = "BARCODE")
    public String getBarcode(){
        return barcode;
    }

    /**
     * 设置 商品条码 by D.
     *
     * @param barcode
     *            the new 商品条码 by D
     */
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

}
