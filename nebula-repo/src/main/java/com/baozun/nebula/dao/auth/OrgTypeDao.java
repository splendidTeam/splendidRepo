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
package com.baozun.nebula.dao.auth;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.option.OptionCommand;
import com.baozun.nebula.model.auth.OrgType;

/**
 * 机构类型dao
 * @author Justin
 *
 */
public interface OrgTypeDao extends GenericEntityDao<OrgType, Long> {

	/**
	 * 获取所有机构类型
	 * @return
	 */
	@NativeQuery(model=OrgType.class)
	List<OrgType> findAllList();
	/**
	 * 获取选项列表
	 * @return
	 */
	@NativeQuery(model=OptionCommand.class)
	List<OptionCommand> findOptionCommand();
}
