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
package com.baozun.nebula.model.packageinfo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.baozun.nebula.model.BaseModel;

/**
 * 包装信息.
 * 
 * <p>
 * 对于某些特殊商城,支持买家购买的时候使用不同的包装 (对于Reebok而言 就是支持定制球鞋秋衣,上面可以刻字)
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "com.baozun.nebula.wormhole.mq.entity.order.ProductPackageV5"
 * @since 5.3.2.11-Personalise
 */
@Entity
@Table(name = "T_PI_PACKAGE_INFO")
public class PackageInfo extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2063973670756502968L;

    /** PK. */
    private Long id;

    /**
     * 包装类型：SCM定义的列表中选取.
     * 
     * <p>
     * reebok的特殊处理是要基于到商品行级别的，
     * </p>
     * 
     * <ul>
     * <li>specialType (1：需特殊处理核对 2：包裹填充物）（传1 如果reebok需要特殊处理）；</li>
     * <li>isSpecialPackaging（是否特殊包装（0：否 1：是））（传0 对于reebok）;</li>
     * <li>OrderLineGift（ type 50：特殊商品印刷 如果reebok需要特殊商品印刷 ;memo: recipeid）</li>
     * </ul>
     */
    private Integer type;

    /**
     * 包装金额(不用纠结名字,和接口统一).
     *
     * @since 5.3.2.11
     */
    private BigDecimal total;

    /**
     * 包装特征信息(可有可无).
     * 
     * <blockquote>
     * <p>
     * 和 type 字段一起起到辨识以及拆分订单行的作用,相同的 type 但是不同的featureInfo 那么会渲染成两行数据
     * </p>
     * 
     * <p>
     * 比如 reebok 有recipeid .
     * </p>
     * </blockquote>
     */
    private String featureInfo;

    /**
     * 扩展信息 (JSON字符串格式).
     * 
     * <p>
     * 通常这个字段仅仅用于解析渲染
     * </p>
     * 
     * <p>
     * 对于 reebok而言,传递的是定制商品的信息, 比如 左脚写的xin 右脚文字 jin,格式每个store 可能都不一样,只要确保可以解析即可
     * </p>
     */
    private String extendInfo;

    /** 创建时间. */
    private Date createTime;
    //******************************************************************************************

    /**
     * 获得 pK.
     *
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_PI_PACKAGE_INFO",sequenceName = "S_T_PI_PACKAGE_INFO",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PI_PACKAGE_INFO")
    public Long getId(){
        return id;
    }

    /**
     * 设置 pK.
     *
     * @param id
     *            the new pK
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 包装类型：SCM定义的列表中选取.
     * *
     * <p>
     * reebok的特殊处理是要基于到商品行级别的，
     * </p>
     * 
     * <ul>
     * <li>specialType (1：需特殊处理核对 2：包裹填充物）（传1 如果reebok需要特殊处理）；</li>
     * <li>isSpecialPackaging（是否特殊包装（0：否 1：是））（传0 对于reebok）;</li>
     * <li>OrderLineGift（ type 50：特殊商品印刷 如果reebok需要特殊商品印刷 ;memo: recipeid）</li>
     * </ul>
     * 
     * @return the type
     */
    @Column(name = "TYPE")
    @Index(name = "IDX_PACKAGEINFO_TYPE")
    public Integer getType(){
        return type;
    }

    /**
     * 设置 包装类型：SCM定义的列表中选取.
     * <p>
     * reebok的特殊处理是要基于到商品行级别的，
     * </p>
     * 
     * <ul>
     * <li>specialType (1：需特殊处理核对 2：包裹填充物）（传1 如果reebok需要特殊处理）；</li>
     * <li>isSpecialPackaging（是否特殊包装（0：否 1：是））（传0 对于reebok）;</li>
     * <li>OrderLineGift（ type 50：特殊商品印刷 如果reebok需要特殊商品印刷 ;memo: recipeid）</li>
     * </ul>
     * 
     * @param type
     *            the type to set
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * 获得 包装金额(不用纠结名字,和接口统一).
     *
     * @return the total
     */
    @Column(name = "TOTAL")
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置 包装金额(不用纠结名字,和接口统一).
     *
     * @param total
     *            the total to set
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

    /**
     * 获得 包装特征信息(可有可无).
     * 
     * <blockquote>
     * <p>
     * 和 type 字段一起起到辨识以及拆分订单行的作用,相同的 type 但是不同的featureInfo 那么会渲染成两行数据
     * </p>
     * 
     * <p>
     * 比如 reebok 有recipeid .
     * </p>
     * </blockquote>
     * 
     * @return the featureInfo
     */
    @Column(name = "FEATURE_INFO")
    @Index(name = "IDX_PACKAGEINFO_FEATUREINFO")
    public String getFeatureInfo(){
        return featureInfo;
    }

    /**
     * 设置 包装特征信息(可有可无).
     * 
     * <blockquote>
     * <p>
     * 和 type 字段一起起到辨识以及拆分订单行的作用,相同的 type 但是不同的featureInfo 那么会渲染成两行数据
     * </p>
     * 
     * <p>
     * 比如 reebok 有recipeid .
     * </p>
     * </blockquote>
     * 
     * @param featureInfo
     *            the featureInfo to set
     */
    public void setFeatureInfo(String featureInfo){
        this.featureInfo = featureInfo;
    }

    /**
     * 获得 扩展信息 (JSON字符串格式).
     * 
     * <p>
     * 通常这个字段仅仅用于解析渲染
     * </p>
     * 
     * <p>
     * 对于 reebok而言,传递的是定制商品的信息, 比如 左脚写的xin 右脚文字 jin,格式每个store 可能都不一样,只要确保可以解析即可
     * </p>
     * 
     * @return the extendInfo
     */
    @Column(name = "EXTEND_INFO",length = 1000)
    public String getExtendInfo(){
        return extendInfo;
    }

    /**
     * 设置 扩展信息 (JSON字符串格式).
     * 
     * <p>
     * 通常这个字段仅仅用于解析渲染
     * </p>
     * 
     * <p>
     * 对于 reebok而言,传递的是定制商品的信息, 比如 左脚写的xin 右脚文字 jin,格式每个store 可能都不一样,只要确保可以解析即可
     * </p>
     * 
     * @param extendInfo
     *            the extendInfo to set
     */
    public void setExtendInfo(String extendInfo){
        this.extendInfo = extendInfo;
    }

    /**
     * 获得 创建时间.
     *
     * @return the createTime
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 创建时间.
     *
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

}
