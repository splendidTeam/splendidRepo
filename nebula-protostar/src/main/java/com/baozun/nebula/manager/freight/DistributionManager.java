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
package com.baozun.nebula.manager.freight;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.freight.command.ExpSupportedAreaCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * @author jumbo
 */
public interface DistributionManager extends BaseManager{

    /**
     * 保存DistributionMode
     * 
     */
    DistributionModeCommand saveOrUpdateDistributionMode(DistributionModeCommand model);

    /**
     * 分页查询SupportedAreaCommand
     * 
     * @param page
     * @param sorts
     * @param searchParam
     * @return
     */
    Pagination<SupportedAreaCommand> findSupportedAreaByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> searchParam);

    /**
     * (删除)
     * 
     * @param ids
     */
    void removeDistributionModeByIds(List<Long> ids);

    /**
     * 通过templateId 获取所有支持的 物流方式
     * 
     * @return
     */
    List<DistributionMode> getDistributionModeListByTemplateId(Long templateId);

    /**
     * 导入支持区域
     * 
     * @param is
     * @param distributionId
     * @throws BusinessException
     */
    void importSupportedAreaFile(InputStream is,Long distributionId);

    List<ExpSupportedAreaCommand> exportSupportedAreasByDistributionId(Long distributionId);

    List<SupportedAreaCommand> findSupportedAreasByDistributionId(Long distributionId);

    /**
     * 
     * @author 何波
     * @Description: 获取所有物流方式
     * @return
     * List<DistributionMode>
     * @throws
     */
    List<DistributionMode> getAllDistributionModeList();
}
