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

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.web.MemberDetails;

/**
 * 收货地址 列表构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
public interface ContactCommandListBuilder{

    /**
     * 构造制定用户的收获地址列表.
     * 
     * @param memberDetails
     * @return 如果 <code>memberDetails</code> 是null,返回null<br>
     *         否则调用 {@link com.baozun.nebula.sdk.manager.SdkMemberManager#findAllContactListByMemberId(Long) SdkMemberManager.findAllContactListByMemberId(Long)}
     */
    List<ContactCommand> build(MemberDetails memberDetails);
}
