/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.freight.manager;

import java.util.List;

import com.baozun.nebula.freight.memory.DistributionCommand;
import com.baozun.nebula.freight.memory.ShippingFeeConfigMap;
import com.baozun.nebula.freight.memory.ShippingTemeplateMap;
import com.baozun.nebula.freight.memory.ShopShippingTemeplateMap;

/**
 * 运费内存管理memory
 * 
 * @author Tianlong.Zhang
 *
 */
public interface FreightMemoryManager{

    /**
     * 将数据库的 载物流列表 ，运费模板 ，店铺运费模板 ，运费配置信息加载到内存中
     * 
     */
    void loadFreightInfosFromDB();

    /**
     * 从数据库中加载物流列表
     */
    void loadDistributionList();

    /**
     * 从数据库中加载运费模板 map
     * 
     */
    void loadShippingTemeplateMap();

    /**
     * 从数据库中加载 店铺运费模板map
     * 
     */
    void loadShopShippingTemeplateMap();

    /**
     * 从数据库中加载运费配置信息Map
     */
    void loadShippingFeeConfigMap();

    /**
     * 从内存中获得物流列表
     * 
     * @return the cmdList
     */
    List<DistributionCommand> getDistributionList();

    /**
     * 从内存中获得运费模板 map
     * 
     * @return the shippingTemeplateMap
     */
    ShippingTemeplateMap getShippingTemeplateMap();

    /**
     * 从内存中获得 店铺运费模板map
     */
    ShopShippingTemeplateMap getShopShippingTemeplateMap();

    /**
     * 从内存中获得 运费配置map
     */
    ShippingFeeConfigMap getShippingFeeConfigMap();
}
