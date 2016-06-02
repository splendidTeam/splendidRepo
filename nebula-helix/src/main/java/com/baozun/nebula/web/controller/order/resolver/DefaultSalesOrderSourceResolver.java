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
package com.baozun.nebula.web.controller.order.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.feilong.spring.mobile.device.DeviceUtil;

/**
 * The Class DefaultSalesOrderSourceResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.model.salesorder.SalesOrder#SO_SOURCE_NORMAL
 * @see com.baozun.nebula.model.salesorder.SalesOrder#SO_SOURCE_MOBILE_NORMAL
 * @since 5.3.1
 */
public class DefaultSalesOrderSourceResolver implements SalesOrderSourceResolver{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.resolver.SalesOrderSourceResolver#resolver(com.baozun.nebula.web.MemberDetails,
     * com.baozun.nebula.web.controller.order.form.OrderForm, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public int resolveOrderSource(MemberDetails memberDetails,OrderForm orderForm,HttpServletRequest request){
        Device device = DeviceUtil.getDevice(request);
        return device.isMobile() ? SalesOrder.SO_SOURCE_MOBILE_NORMAL : SalesOrder.SO_SOURCE_NORMAL;
    }
}
