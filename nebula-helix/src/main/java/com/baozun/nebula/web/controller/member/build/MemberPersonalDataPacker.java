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
package com.baozun.nebula.web.controller.member.build;

import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.web.controller.member.form.MemberProfileForm;
/**
 * MemberPersonalData包装器接口
 * 用于将提交的memberProfileForm包装成MemberPersonalData
 * @since 5.3.2.18
 * @author bowen.dai
 */
public interface MemberPersonalDataPacker{
    /**
     * memberProfileForm包装为为MemberPersonalData 
     * @param memberPersonalData
     * @param memberProfileForm
     * @return MemberPersonalData
     */
    MemberPersonalData packer(MemberPersonalData memberPersonalData,MemberProfileForm memberProfileForm);
}
