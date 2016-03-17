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
package com.baozun.nebula.sdk.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.salesorder.PickupInfoDao;
import com.baozun.nebula.model.salesorder.PickupInfo;
import com.baozun.nebula.sdk.manager.SdkPickInfoManager;

/**
 * SdkPickInfoManagerImpl
 *
 * @author: shiyang.lv
 * @date: 2014年5月15日
 **/
@Transactional
@Service("sdkPickInfoManager")
public class SdkPickInfoManagerImpl implements SdkPickInfoManager {

    @Autowired
    private PickupInfoDao pickupInfoDao;
    
    @Override
    @Transactional(readOnly=true)
    public PickupInfo findPickupInfoByOrderId(Long orderId) {
        if(orderId==null){
            return null;
        }
        
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        
        List<PickupInfo> list=pickupInfoDao.findEffectPickupInfoListByQueryMap(map);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

}

