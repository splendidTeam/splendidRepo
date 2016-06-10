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

import java.util.Date;

import com.baozun.nebula.solr.factory.SortTypeEnum;
import com.feilong.core.DatePattern;
import com.feilong.core.date.DateUtil;
import com.feilong.core.lang.EnumUtil;

/**
 * The Enum OrderTimeType.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月10日 下午2:17:42
 * @since 5.3.1
 */
public enum OrderTimeType{

    /** The TIMETYP e_3 month. */
    TIMETYPE_3MONTH("3month"){

        @Override
        public Date[] getBeginAndEndDate(){
            Date now = new Date();
            return new Date[] { DateUtil.addMonth(now, -3), now };
        }
    },

    /** The TIMETYP e_3 mont h_ ago. */
    TIMETYPE_3MONTH_AGO("3monthago"){

        @Override
        public Date[] getBeginAndEndDate(){
            Date myDate = DateUtil.toDate("2015-01-01", DatePattern.COMMON_DATE);
            return new Date[] { myDate, DateUtil.addMonth(new Date(), -3) };
        }
    };

    /** The type. */
    private String type;

    /**
     * The Constructor.
     *
     * @param type
     *            the type
     */
    private OrderTimeType(String type){
        this.type = type;
    }

    /**
     * Gets the sort.
     * 
     * 
     * @return the sort
     */
    public abstract Date[] getBeginAndEndDate();

    /**
     * 获得 type.
     *
     * @return the type
     */
    public String getType(){
        return type;
    }

    /**
     * 设置 type.
     *
     * @param type
     *            the type to set
     */
    public void setType(String type){
        this.type = type;
    }

    public static OrderTimeType getInstance(String type){
        return EnumUtil.getEnumByPropertyValue(OrderTimeType.class, "type", type);
    }

}
