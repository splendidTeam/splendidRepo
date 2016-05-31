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
package com.baozun.nebula.web.controller.order.form;

import java.io.Serializable;

/**
 * 和 Coupon相关.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月28日 下午6:56:18
 * @since 5.3.1
 */
public class CouponInfoSubForm implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7444454422577156141L;

    /** 如果优惠券和用户绑定了， 那么此处的id 就是绑定的id. 
     *  nebula暂无用户绑定优惠券，这个id暂时不用。 
     *  具体使用否，要看商城端需求。
     *  比如speedo，用户绑定优惠券，是为了使用优惠券时，能够有下拉选项，用户用起来方便。
     * 
     */
    private Long              id;

    /** 单独发放的那种不和用户绑定的优惠码. */
    private String            couponCode;

    /**
     * 获得 如果优惠券和用户绑定了， 那么此处的id 就是绑定的id.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 如果优惠券和用户绑定了， 那么此处的id 就是绑定的id.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 单独发放的那种不和用户绑定的优惠码.
     *
     * @return the couponCode
     */
    public String getCouponCode(){
        return couponCode;
    }

    /**
     * 设置 单独发放的那种不和用户绑定的优惠码.
     *
     * @param couponCode
     *            the couponCode to set
     */
    public void setCouponCode(String couponCode){
        this.couponCode = couponCode;
    }

}
