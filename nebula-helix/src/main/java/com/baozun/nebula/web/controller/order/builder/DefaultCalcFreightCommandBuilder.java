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
package com.baozun.nebula.web.controller.order.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.web.controller.order.form.ShippingInfoSubForm;
import com.feilong.core.bean.PropertyUtil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toLong;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Component("calcFreightCommandBuilder")
public class DefaultCalcFreightCommandBuilder implements CalcFreightCommandBuilder{

    /**  */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCalcFreightCommandBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.CalcFreightCommandBuilder#build(com.baozun.nebula.web.controller.order.form.ShippingInfoSubForm)
     */
    @Override
    public CalcFreightCommand build(ShippingInfoSubForm shippingInfoSubForm){
        List<ContactCommand> addressList = toContactCommandList(shippingInfoSubForm);

        CalcFreightCommand calcFreightCommand = build(addressList);
        calcFreightCommand.setDistributionModeId(toLong(shippingInfoSubForm.getAppointType()));
        return calcFreightCommand;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.CalcFreightCommandBuilder#build(java.util.List)
     */
    @Override
    public CalcFreightCommand build(List<ContactCommand> contactCommandList){
        CalcFreightCommand calcFreightCommand = null;
        if (contactCommandList != null && !contactCommandList.isEmpty()){
            ContactCommand contactCommand = null;

            //默认地址
            for (ContactCommand curContactCommand : contactCommandList){
                if (curContactCommand.getIsDefault()){
                    contactCommand = curContactCommand;
                    break;
                }
            }
            //无默认，取第一条
            if (contactCommand == null){
                contactCommand = contactCommandList.get(0);
            }
            calcFreightCommand = new CalcFreightCommand();
            calcFreightCommand.setProvienceId(contactCommand.getProvinceId());
            calcFreightCommand.setCityId(contactCommand.getCityId());
            calcFreightCommand.setCountyId(contactCommand.getAreaId());
        }else{
            //TODO 为毛是上海市黄浦区?
            //在未选择地址的情况下   为了显示默认运费，初始设置一个临时地址，上海市黄浦区()
            calcFreightCommand = new CalcFreightCommand();
            calcFreightCommand.setProvienceId(310000L);
            calcFreightCommand.setCityId(310100L);
            calcFreightCommand.setCountyId(310101L);
        }
        return calcFreightCommand;
    }

    /**
     * To address list.
     *
     * @param shippingInfoSubForm
     *            the shipping info sub form
     * @return the list
     */
    private List<ContactCommand> toContactCommandList(ShippingInfoSubForm shippingInfoSubForm){
        boolean isNeedReferToCalcFreight = isNeedReferToCalcFreight(shippingInfoSubForm);

        //如果不需要(比如没有填写全,那么直接返回null)
        if (!isNeedReferToCalcFreight){
            return null;
        }

        //否则 将 shippingInfoSubForm --> contactCommand--->组装成list 返回
        ContactCommand contactCommand = toContactCommand(shippingInfoSubForm);

        return toList(contactCommand);
    }

    /**
     * 是否需要参考计算运费.
     * 
     * <p>
     * 这里当地区全部满足商城选择, 才会计算后面的运费信息<br>
     * 由于不同的商城, 省市区的级别不一样, 有的到大厦,有的需要国家等<br>
     * 有的 需要有 AreaId 才开始计算运费,有的不需要,等等
     * </p>
     * 
     * <p>
     * 该方法允许不同的商城重写
     * </p>
     *
     * @param shippingInfoSubForm
     *            the shipping info sub form
     * @return true, if is need refer to calc freight
     * @since 5.3.1.9
     */
    protected boolean isNeedReferToCalcFreight(ShippingInfoSubForm shippingInfoSubForm){
        return shippingInfoSubForm != null && shippingInfoSubForm.getProvinceId() != null && shippingInfoSubForm.getCityId() != null && shippingInfoSubForm.getAreaId() != null;
    }

    /**
     * To contact command.
     *
     * @param shippingInfoSubForm
     *            the shipping info sub form
     * @return the contact command
     * @since 5.3.1.9
     */
    private static ContactCommand toContactCommand(ShippingInfoSubForm shippingInfoSubForm){
        ContactCommand contactCommand = new ContactCommand();
        PropertyUtil.copyProperties(contactCommand, shippingInfoSubForm, "countryId", "provinceId", "cityId", "areaId", "townId");
        return contactCommand;
    }

}
