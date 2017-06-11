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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.Validate;

import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.web.controller.member.form.MemberProfileForm;
import com.feilong.core.DatePattern;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.date.DateUtil;
/**
 * 默认的MemberPersonalData包装器
 * 用于将提交的memberProfileForm包装成MemberPersonalData
 * @since 5.3.2.18
 * @author bowen.dai
 *
 */
public class DefaultMemberPersonalDataPacker implements MemberPersonalDataPacker{

    /** Static instance. */
    // the static instance works for all types
    public static final MemberPersonalDataPacker INSTANCE = new DefaultMemberPersonalDataPacker();

    @Override
    public MemberPersonalData packer(MemberPersonalData memberPersonalData,MemberProfileForm memberProfileForm){
        Validate.notNull(memberPersonalData, "memberPersonalData can not null");
        Validate.notNull(memberProfileForm, "memberProfileForm can not null");

        //***************************************************************************
        Date date = DateUtil.toDate(memberProfileForm.getBirthday(), DatePattern.COMMON_DATE);
        memberPersonalData.setBirthday(date);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        try{
//            if (Validator.isNotNullOrEmpty(memberProfileForm.getBirthday())){
//                memberPersonalData.setBirthday(sdf.parse(memberProfileForm.getBirthday()));
//            }
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
        memberPersonalData.setEmail(memberProfileForm.getLoginEmail());
        memberPersonalData.setMobile(memberProfileForm.getLoginMobile());

        memberPersonalData.setNickname(memberProfileForm.getLoginName());

        PropertyUtil.copyProperties(memberPersonalData, memberProfileForm, "sex", "city", "cityId", "province", "provinceId", "area", "areaId");

        //------------------------------------------------------------------------
        memberPersonalData.setVersion(new Date());
        return memberPersonalData;
    }

}
