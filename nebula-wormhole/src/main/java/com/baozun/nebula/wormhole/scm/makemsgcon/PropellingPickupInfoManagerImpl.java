/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
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
package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.salesorder.PickupInfo;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkPickInfoManager;
import com.baozun.nebula.wormhole.mq.entity.logistics.PickupInfoV5;

/**
 * PropellingPickupInfoManagerImpl
 *
 * @author: shiyang.lv
 * @date: 2014年5月9日
 **/
@Service("propellingPickupInfoManager")
@Transactional
public class PropellingPickupInfoManagerImpl implements PropellingPickupInfoManager {

    
    @Autowired
    private PropellingCommonManager propellingCommonManager;

    @Autowired
    private SdkMsgManager sdkMsgManager;
    
    @Autowired
    private SdkPickInfoManager sdkPickInfoManager;
    
    @Override
    public MsgSendContent propellingPickupInfo(MsgSendRecord msr) {
        
        Long orderId=msr.getTargetId();
        Long id=msr.getId();
       
        //转化为List转为JSON存入content
        List<PickupInfoV5> pickupInfos=convertToPickupInfoV5(orderId);

        return propellingCommonManager.saveMsgBody(pickupInfos, id);
        
    }
    
    /**
     * 转化为MQ对象
     * @param pickupInfoV5
     * @param orderId
     * @return
     */
    private List<PickupInfoV5> convertToPickupInfoV5(Long orderId){
        
        
        PickupInfo beforeInfo=sdkPickInfoManager.findPickupInfoByOrderId(orderId);
        
        List<PickupInfoV5> result = new ArrayList<PickupInfoV5>();
        
        if(beforeInfo!=null){
            PickupInfoV5 pickupInfoV5 = new PickupInfoV5();
            
            
            String country=beforeInfo.getCountry();
            String province=beforeInfo.getProvince();
            String city=beforeInfo.getCity();
            String district=beforeInfo.getDistrict();
            String town=beforeInfo.getTown();
            String address=beforeInfo.getAddress();
            String zipCode=beforeInfo.getZipCode();
            String contact=beforeInfo.getContact();
            String contactPhone=beforeInfo.getContactPhone();
            String contactMobile=beforeInfo.getContactMobile();
            String bsOrdercode=beforeInfo.getBsOrdercode();
            
            pickupInfoV5.setCountry(country);
            pickupInfoV5.setProvince(province);
            pickupInfoV5.setCity(city);
            pickupInfoV5.setDistrict(district);
            pickupInfoV5.setTown(town);
            pickupInfoV5.setAddress(address);
            pickupInfoV5.setZipCode(zipCode);
            pickupInfoV5.setContact(contact);
            pickupInfoV5.setContactPhone(contactPhone);
            pickupInfoV5.setContactMobile(contactMobile);
            pickupInfoV5.setBsOrdercode(bsOrdercode);
            
            result.add(pickupInfoV5);
        }
        
        return result;
    }

}

