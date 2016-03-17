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
package com.baozun.nebula.dao.system;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.OptionGroupCommand;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * ChooseOptionDao
 * @author  Justin
 *
 */
public interface ChooseOptionDao extends GenericEntityDao<ChooseOption,Long>{

	/**
	 * 获取所有ChooseOption列表
	 * @return
	 */
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findAllChooseOptionList();
	
	/**
	 * 通过ids获取ChooseOption列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findChooseOptionListByIds(@QueryParam("ids")List<Long> ids);
	
	

	/**
	 * 修改ChooseOption信息
	 * 
	 * @param optionLabel
	 * @param optionValue
	 * @param sortNo
	 * @return caihong.wu
	 */


	
	
	
	
	
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findChooseOptionValue(@QueryParam("groupCode")List<String> groupCode);
	
	/**
	 * 通过参数map获取ChooseOption列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findChooseOptionListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ChooseOption列表
	 * @param groupCode
	 * @param page
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(pagable = true,model = ChooseOption.class)
	Pagination<ChooseOption> findChooseOptionListByQueryMapWithPage(@QueryParam("groupCode")String groupCode,Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量禁用ChooseOption
	 * 设置lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer enableOrDisableOptionByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state") Integer state);
	
	
	
	
	/**
	 * 获取有效的ChooseOption列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findAllEffectChooseOptionList();
	
	/**
	 * 通过参数map获取有效的ChooseOption列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findEffectChooseOptionListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ChooseOption列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(pagable = true,model = ChooseOption.class)
	Pagination<ChooseOption> findEffectChooseOptionListByQueryMapWithPage(int start,int size,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	/**
	 * 获取分页通用选项command
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = OptionGroupCommand.class)
	List<OptionGroupCommand> findOptionGroupList(Sort[] sorts, @QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 根据选项编码验证通用选项是否存在
	 * @param groupName
	 * @return
	 */
	@NativeQuery(pagable = true,model = ChooseOption.class)
	ChooseOption validateOptionGroupCode(@QueryParam("groupCode")String groupCode);
	
	
	/**
	 * 根据选项编码选项值验证通用选项是否存在
	 * @param groupName
	 * @return
	 */
	@NativeQuery(pagable = true,model = ChooseOption.class)
	ChooseOption validateOptionValue(@QueryParam("groupCode")String groupCode,@QueryParam("optionValue")String optionValue);
	
	
	
	
	/**
	 * 通过id获取chooseoption
	 * @param id
	 * @return
	 */
	@NativeQuery(model = ChooseOption.class)
	ChooseOption findChooseOptionById(@QueryParam("id") Long id);
	
	/**
	 * 通过分组名查询分组信息
	 * @param groupCode
	 * @return	:List<ChooseOption>
	 * @date 2014-2-25 下午01:53:35
	 */
	@NativeQuery(model = ChooseOption.class)
	List<ChooseOption> findOptionListByGroupCode(@QueryParam("groupCode") String groupCode);
}
