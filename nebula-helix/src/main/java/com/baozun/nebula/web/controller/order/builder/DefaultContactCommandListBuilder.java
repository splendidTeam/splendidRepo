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

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.delivery.ContactDeliveryCommand;
import com.baozun.nebula.sdk.manager.SdkDeliveryAreaManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
@Component("contactCommandListBuilder")
public class DefaultContactCommandListBuilder implements ContactCommandListBuilder{

    /** The sdk member manager. */
    @Autowired
    private SdkMemberManager sdkMemberManager;

    /** The delivery area manager. */
    @Autowired
    private SdkDeliveryAreaManager deliveryAreaManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.ContactCommandListBuilder#build(com.baozun.nebula.web.MemberDetails)
     */
    @Override
    public List<ContactCommand> build(MemberDetails memberDetails){
        if (null == memberDetails){
            return null;
        }

        List<ContactCommand> contactCommands = sdkMemberManager.findAllContactListByMemberId(memberDetails.getGroupId());
        if (CollectionUtils.isEmpty(contactCommands)){
            return null;
        }

        for (ContactCommand contactCommand : contactCommands){
            ContactDeliveryCommand deliveryCommand = deliveryAreaManager.findContactDeliveryByDeliveryAreaCode(getDeliveryAreaCode(contactCommand));
            contactCommand.setContactDeliveryCommand(deliveryCommand);
        }
        return contactCommands;
    }

    /**
     * Gets the delivery area code.
     *
     * @param contactCommand
     *            the contact command
     * @return the delivery area code
     */
    protected String getDeliveryAreaCode(ContactCommand contactCommand){
        return contactCommand.getAreaId() + "";
    }
}
