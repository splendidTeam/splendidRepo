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
package com.baozun.nebula.web.controller.order.builder.subview;

import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.web.controller.order.viewcommand.ConsigneeSubViewCommand;
import com.feilong.core.bean.PropertyUtil;

/**
 * 专门用来构造 {@link ConsigneeSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("consigneeSubViewCommandBuilder")
public class DefaultConsigneeSubViewCommandBuilder implements ConsigneeSubViewCommandBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.ConsigneeSubViewCommandBuilder#buildConsigneeSubViewCommand(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public ConsigneeSubViewCommand build(SalesOrderCommand salesOrderCommand){
        // 收货地址信息
        ConsigneeSubViewCommand consigneeSubViewCommand = new ConsigneeSubViewCommand();
        PropertyUtil.copyProperties(consigneeSubViewCommand, salesOrderCommand, "name", "address", "mobile", "tel", "email", "postcode", "buyerTel", "buyerName");

        Address country = AddressUtil.getAddressById(salesOrderCommand.getCountryId());
        Address province = AddressUtil.getAddressById(salesOrderCommand.getProvinceId());
        Address city = AddressUtil.getAddressById(salesOrderCommand.getCityId());
        Address area = AddressUtil.getAddressById(salesOrderCommand.getAreaId());

        consigneeSubViewCommand.setCountry(country == null ? null : country.getName());
        consigneeSubViewCommand.setProvince(province == null ? null : province.getName());
        consigneeSubViewCommand.setCity(city == null ? null : city.getName());
        consigneeSubViewCommand.setArea(area == null ? null : area.getName());
        return consigneeSubViewCommand;
    }
}
