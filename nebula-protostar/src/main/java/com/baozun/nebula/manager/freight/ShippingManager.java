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

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.freight.command.ExpShippingFeeCommand;

/**
 * @author jumbo
 *
 */
public interface ShippingManager{

    /**
     * 批量(删除)运费模板
     * 
     * @param ids
     */
    void removeShippingTemplateByIds(List<Long> ids);

    /**
     * 导入运费表
     * 
     * @param is
     * @param templateId
     * @throws BusinessException
     */
    void importShippingFeeFile(InputStream is,Long templateId);

    /**
     * 导出运费表
     * 
     * @param is
     * @param templateId
     * @throws BusinessException
     */
    List<ExpShippingFeeCommand> exportShippingFeeConfigCommandList(Long templateId);
}
