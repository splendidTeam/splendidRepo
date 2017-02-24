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
package com.baozun.nebula.model.shoppingcart;

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
 * 购物车行的包装信息.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>对于同一条订单行或者购物车,可能会有多个包装信息, 比如上丝带,上精品包装</li>
 * <li>对于同一条包装信息,可能会0个 1个 或者1+ 个购物车行/订单行, 如果固定的包装 那么就会有多条购物车行/订单行数据; 对于reebok定制球衣而言, 由于包装信息是动态生成的, 对reebok的数据业务,和购物车行/订单行的数据关系是1对1的</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.model.packageinfo.PackageInfo
 * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine
 * @since 5.3.2.11-Personalise
 */
@Entity
@Table(name = "T_SC_SHOPPING_CARTLINE_PACKAGE_INFO")
public class ShoppingCartLinePackageInfo extends BaseModel{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /** 主键. */
    private Long id;

    /** 购物车行id. */
    private Long shoppingCartLineId;

    /** 对应的包装信息. */
    private Long packageInfoId;

    /**
     * 加入时间.
     * 
     * <p>
     * 有的购物车是支持先把普通商品加入购物车, 再在购物车选择或者填写包装信息数据的, 此时就会有购物车的创建时间和此处的创建时间不一致的情况
     * </p>
     */
    private Date createTime;

    /**
     * 获得 主键.
     *
     * @return the id
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SC_SHOPPING_CARTLINE_PACKAGE_INFO",sequenceName = "S_T_SC_SHOPPING_CARTLINE_PACKAGE_INFO",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SC_SHOPPING_CARTLINE_PACKAGE_INFO")
    public Long getId(){
        return id;
    }

    /**
     * 设置 主键.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 购物车行id.
     *
     * @return the shoppingCartLineId
     */
    @Column(name = "SHOPPING_CARTLINE_ID",nullable = false)
    @Index(name = "IDX_SHOPPINGCARTLINEPACKAGEINFO_SHOPPINGCARTLINEID")
    public Long getShoppingCartLineId(){
        return shoppingCartLineId;
    }

    /**
     * 设置 购物车行id.
     *
     * @param shoppingCartLineId
     *            the shoppingCartLineId to set
     */
    public void setShoppingCartLineId(Long shoppingCartLineId){
        this.shoppingCartLineId = shoppingCartLineId;
    }

    /**
     * 获得 对应的包装信息.
     *
     * @return the packageInfoId
     */
    @Column(name = "PACKAGEINFO_ID",nullable = false)
    @Index(name = "IDX_SHOPPINGCARTLINEPACKAGEINFO_PACKAGEINFOID")
    public Long getPackageInfoId(){
        return packageInfoId;
    }

    /**
     * 设置 对应的包装信息.
     *
     * @param packageInfoId
     *            the packageInfoId to set
     */
    public void setPackageInfoId(Long packageInfoId){
        this.packageInfoId = packageInfoId;
    }

    /**
     * 获得 加入时间.
     * <p>
     * 有的购物车是支持先把普通商品加入购物车, 再在购物车选择或者填写包装信息数据的, 此时就会有购物车的创建时间和此处的创建时间不一致的情况
     * </p>
     * 
     * @return the createTime
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 加入时间.
     * <p>
     * 有的购物车是支持先把普通商品加入购物车, 再在购物车选择或者填写包装信息数据的, 此时就会有购物车的创建时间和此处的创建时间不一致的情况
     * </p>
     * 
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

}
