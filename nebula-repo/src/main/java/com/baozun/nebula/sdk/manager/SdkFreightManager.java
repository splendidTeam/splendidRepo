/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.freight.DistributionMode;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * @author jumbo
 *
 */
public interface SdkFreightManager extends BaseManager{

    /** --------------- 物流方式 ----------------- */
    /**
     * 获取所有的 物流方式
     * 
     * @return
     */
    List<DistributionMode> getAllDistributionMode();

    /**
     * 分页查询SupportedAreaCommand
     * 
     * @param page
     * @param sorts
     * @param searchParam
     * @return
     */
    Pagination<SupportedAreaCommand> findSupportedAreaByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam);

    /** 通过id查找物流方式 */
    DistributionMode findDistributionModeById(Long id);
}
