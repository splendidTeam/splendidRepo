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
package com.baozun.nebula.web.controller.order.viewcommand;

import java.io.Serializable;
import java.util.Date;

/**
 * 巴枪扫描记录.
 *
 * @author feilong
 * @version 5.3.1 2016年5月12日 下午3:54:43
 * @see com.baozun.nebula.model.salesorder.Logistics#getTrackingDescription()
 * @since 5.3.1
 */
public class LogisticsInfoBarRecordSubViewCommand implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -139449041018867669L;

    /** 扫描时间,需要将拿到的日志记录中的 时间和日期提取解析出来转换成date,以便view里面渲染成不同的格式. */
    private Date              barScanDate;

    /** 监控记录. */
    private String            remark;

    /**
     * 监控记录.
     *
     * @return the 监控记录
     */
    public String getRemark(){
        return remark;
    }

    /**
     * 监控记录.
     *
     * @param remark
     *            the new 监控记录
     */
    public void setRemark(String remark){
        this.remark = remark;
    }

    /**
     * 获得 扫描时间,需要将拿到的日志记录中的 时间和日期提取解析出来转换成date,以便view里面渲染成不同的格式.
     *
     * @return the barScanDate
     */
    public Date getBarScanDate(){
        return barScanDate;
    }

    /**
     * 设置 扫描时间,需要将拿到的日志记录中的 时间和日期提取解析出来转换成date,以便view里面渲染成不同的格式.
     *
     * @param barScanDate
     *            the barScanDate to set
     */
    public void setBarScanDate(Date barScanDate){
        this.barScanDate = barScanDate;
    }
}
