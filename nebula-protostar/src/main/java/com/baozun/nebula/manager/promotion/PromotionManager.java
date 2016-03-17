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

package com.baozun.nebula.manager.promotion;

import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.promotion.AudienceCommand;
import com.baozun.nebula.command.promotion.ConditionNormalCommand;
import com.baozun.nebula.command.promotion.HeadCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionQueryCommand;
import com.baozun.nebula.command.promotion.ScopeCommand;
import com.baozun.nebula.command.promotion.SettingNormalCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionInformation;

/**
 * @author - 项硕
 */
public interface PromotionManager extends BaseManager{

	/**
	 * 根据条件分页查询待启用的促销列表（包含步骤不完整的）
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<PromotionQueryCommand> findInactivePromotionListConditionallyWithPage(
			Page page,
			Sort[] sorts,
			Map<String, Object> paraMap);

	/**
	 * 根据条件分页查询步骤完整的促销列表
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<PromotionQueryCommand> findCompletePromotionListConditionallyWithPage(
			Page page,
			Sort[] sorts,
			Map<String, Object> paraMap);

	/**
	 * 根据id查询促销,
	 * 
	 * @param id
	 * @return
	 */
	public PromotionCommand findPromotionById(Long id);

	/**
	 * 步骤1：保存促销活动头部信息。 判断head实体中创PromtionID值是否为空，为空建新Case，不为空编辑Case。 根据PromotionCommand.HeadCommand.CopyFrom，判断是否是有效期内修改，需要特殊处理。
	 * 
	 * @param headCommand
	 * @return promotionID
	 */
	public Long savePromotionHead(HeadCommand headCommand,Long userId);

	/**
	 * 步骤2：保存促销活动受益人群。 根据PromotionCommand.HeadCommand.CopyFrom，判断是否是有效期内修改，需要特殊处理。
	 * 
	 * @param scopeCommand
	 * @return
	 */
	public Long savePromotionAudience(AudienceCommand audienceCommand,Long userId);

	/**
	 * 步骤3：保存促销活动范围及例外。 根据PromotionCommand.HeadCommand.lifecycle，判断状态，未完成设置，设置完成未启用，已启用未生效，已生效，四种情况的不同数据处理。
	 * 根据PromotionCommand.HeadCommand.CopyFrom，判断是否是有效期内修改，需要特殊处理。
	 * 
	 * @param scopeCommand
	 * @return
	 */
	public Long savePromotionScope(ScopeCommand scopeCommand,Long userId);

	/**
	 * 步骤4：保存促销活动条件 根据PromotionCommand.HeadCommand.lifecycle，判断状态，未完成设置，设置完成未启用，已启用未生效，已生效，四种情况的不同数据处理。
	 * 根据PromotionCommand.HeadCommand.CopyFrom，判断是否是有效期内修改，需要特殊处理。
	 * 
	 * @param conditionCommand
	 * @return
	 */
	public Long savePromotionCondition(ConditionNormalCommand conditionCommand,Long userId);

	/**
	 * 步骤5：保存促销活动设置 根据PromotionCommand.HeadCommand.lifecycle，判断状态，未完成设置，设置完成未启用，已启用未生效，已生效，四种情况的不同数据处理。
	 * 根据PromotionCommand.HeadCommand.CopyFrom，判断是否是有效期内修改，需要特殊处理。
	 * 
	 * @param settingCommand
	 * @return
	 */
	public Long savePromotionSetting(SettingNormalCommand settingCommand,Long userId);

	/**
	 * Copy一个启用的促销活动草稿。5个构件都有，缺省为设置完毕
	 * 
	 * @param fromPromotionId
	 * @param userId
	 *            操作人id
	 * @param isUpdate
	 *            是否是修改（生效期修改将有copyFrom）
	 * @return
	 */
	public Long copyPromotion(Long fromPromotionId,Long userId,boolean isUpdate);

	/**
	 * 检查促销名称是否重名
	 * 
	 * @param id
	 *            可为Null,表示‘创建’
	 * @param name
	 */
	public boolean checkPromotionName(Long id,String name);

	/**
	 * 根据促销ID删除条件和设置
	 * 
	 * @param id
	 */
	public void deleteConditionAndSettingByPromotionId(Long id);

	/**
	 * 根据categoryCode查
	 * 
	 * @param categoryCode
	 * @return
	 */
	public PromotionInformation findByCategoryCode(String categoryCode);

	/**
	 * 创建or更新PromotionInformation
	 * 
	 * @param id
	 * @param categoryCode
	 * @param content
	 * @return
	 */
	public Integer createOrUpdatePromotionInformationPage(Long id,String categoryCode,String content);

}
