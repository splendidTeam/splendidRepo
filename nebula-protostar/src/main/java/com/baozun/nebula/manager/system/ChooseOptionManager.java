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
package com.baozun.nebula.manager.system;

import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.OptionGroupCommand;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * @author Justin
 *
 */
public interface ChooseOptionManager {
	/**
	 * 根据分组code和language查询有效的option
	 * @param groupCode 分组编号
	 * @return
	 */

	List<ChooseOption> findEffectChooseOptionListByGroupCode(String groupCode,String language);
	/**
	 * 根据分组code查询有效的option,使用当前默认语言
	 * @param groupCode 分组编号
	 * @return
	 */

	List<ChooseOption> findEffectChooseOptionListByGroupCode(String groupCode);
	
    /**
     * 通用选项分组列表
     * @param page
     * @param sorts
     * @param paraMap
     * @return
     */
	List<OptionGroupCommand> findOptionGroupListByQueryMapWithPage(Sort[] sorts, Map<String, Object> paraMap);
	
	/**
	 * 根据分组编码验证是否存在
	 * @param groupCode
	 * @return
	 */
	
	Boolean validateOptionGroupCode(String groupCode);
	
	
	
	/**
	 * 根据分组编码，选项值,验证是否存在
	 * @param groupCode
	 * @param optionValue
	 * @return
	 */
	
	Boolean validateOptionValue(String groupCode,String optionValue);
	
	
	
	

	/**
	 * 根据分组编码查找状态及其信息
	 * @param groupCode
	 * @param page
     * @param sorts
     * @param paraMap
	 * @return
	 */
	Pagination<ChooseOption> findOptionListByQueryMapWithPage(String groupCode,Page page,Sort[] sorts,Map<String, Object> paraMap);
	

	
	/**
	 * 根据ids禁用启用选项名称
	 * @param ids
	 * @param state
     * @return
	 */
	Integer enableOrDisableOptionByIds(List<Long> ids,Integer state);
	
	
	
	/**
	 * 根据id获取chooseOption
	 * @param id
     * @return
	 */
	public ChooseOption findChooseOptionById(Long chooseOptionId);
	
	
	
	public void createOrUpdateOptionGroup(ChooseOption[] chooseOption);
	
	/**
	 * 创建或修改
	 * @param chooseoption
	 */
	public void createOrUpdateOption(ChooseOption chooseOption);
	
	/**
	 * 通过分组名查询分组信息
	 * @param groupCode
	 * @return	:List<ChooseOption>
	 * @date 2014-2-25 下午01:51:21
	 */
	public List<ChooseOption> findOptionListByGroupCode(String groupCode);
	
	/**
	 * 查询所有的选项
	 * @return	:List<ChooseOption>
	 * @date 2014-2-25 下午03:44:28
	 */
	public List<ChooseOption> findAllOptionList();
	

}
