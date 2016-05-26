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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.param.ConditionMasterType;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.command.promotion.AudienceCommand;
import com.baozun.nebula.command.promotion.ConditionComplexCommand;
import com.baozun.nebula.command.promotion.ConditionNormalCommand;
import com.baozun.nebula.command.promotion.HeadCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionQueryCommand;
import com.baozun.nebula.command.promotion.ScopeCommand;
import com.baozun.nebula.command.promotion.SettingComplexCommand;
import com.baozun.nebula.command.promotion.SettingNormalCommand;
import com.baozun.nebula.command.promotion.SimpleExpressionCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.promotion.PromotionAudiencesDao;
import com.baozun.nebula.dao.promotion.PromotionConditionComplexDao;
import com.baozun.nebula.dao.promotion.PromotionConditionDao;
import com.baozun.nebula.dao.promotion.PromotionDao;
import com.baozun.nebula.dao.promotion.PromotionInformationDao;
import com.baozun.nebula.dao.promotion.PromotionOperationLogDao;
import com.baozun.nebula.dao.promotion.PromotionScopeDao;
import com.baozun.nebula.dao.promotion.PromotionSettingComplexDao;
import com.baozun.nebula.dao.promotion.PromotionSettingDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.promotion.PromotionAudiences;
import com.baozun.nebula.model.promotion.PromotionConditionComplex;
import com.baozun.nebula.model.promotion.PromotionConditionNormal;
import com.baozun.nebula.model.promotion.PromotionHead;
import com.baozun.nebula.model.promotion.PromotionInformation;
import com.baozun.nebula.model.promotion.PromotionOperationLog;
import com.baozun.nebula.model.promotion.PromotionScope;
import com.baozun.nebula.model.promotion.PromotionSettingComplex;
import com.baozun.nebula.model.promotion.PromotionSettingNormal;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;

/**
 * @author - 项硕
 */
@Transactional
@Service("promotionManager")
public class PromotionManagerImpl implements PromotionManager{

	private static final Logger				log	= LoggerFactory.getLogger(PromotionManagerImpl.class);

	@Autowired
	private PromotionDao					promotionDao;

	@Autowired
	private PromotionAudiencesDao			promotionAudiencesDao;

	@Autowired
	private PromotionScopeDao				promotionScopeDao;

	@Autowired
	private PromotionConditionComplexDao	promotionConditionComplexDao;

	@Autowired
	private PromotionSettingDao				promotionSettingDao;

	@Autowired
	private PromotionConditionDao			promotionConditionDao;

	@Autowired
	private PromotionSettingComplexDao		promotionSettingComplexDao;

	@Autowired
	private PromotionOperationLogDao		promotionOperationLogDao;

	@Autowired
	private SdkPromotionManager				sdkPromotionManager;

	@Autowired
	private UserDao							userDao;

	@Autowired
	private SkuDao							skuDao;

	@Autowired
	private PromotionInformationDao			promotionInformationDao;

	@Autowired
	private CacheManager					cacheManager;

	@Override
	public Pagination<PromotionQueryCommand> findInactivePromotionListConditionallyWithPage(
			Page page,
			Sort[] sorts,
			Map<String, Object> paraMap){
		Pagination<PromotionQueryCommand> pagination = promotionDao.findInactivePromotionListConditionallyWithPage(page, sorts, paraMap);

		log.debug(JsonFormatUtil.format(pagination));
		return pagination;
	}

	@Override
	public Pagination<PromotionQueryCommand> findCompletePromotionListConditionallyWithPage(
			Page page,
			Sort[] sorts,
			Map<String, Object> paraMap){
		Pagination<PromotionQueryCommand> pagination = promotionDao.findCompletePromotionListConditionallyWithPage(page, sorts, paraMap);
		/* 计算促销生命周期 */
		for (PromotionQueryCommand cmd : pagination.getItems()){
			cmd.setLifecycle(sdkPromotionManager.calculateLifecycle(cmd.getLifecycle(), cmd.getStartTime(), cmd.getEndTime()));
		}

		log.debug(JsonFormatUtil.format(pagination));
		return pagination;
	}

	@Override
	public PromotionCommand findPromotionById(Long id){
		PromotionCommand cmd = promotionDao.findPromotionById(id);
		if (null == cmd)
			throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

		cmd.setLifecycle(sdkPromotionManager.calculateLifecycle(cmd.getLifecycle(), cmd.getStartTime(), cmd.getEndTime())); // 设置lifecycle
		PromotionAudiences audience = promotionAudiencesDao.findByPromotionId(id);
		PromotionScope scope = promotionScopeDao.findByPromotionId(id);
		ConditionNormalCommand condition = promotionConditionDao.findByPromotionId(id);
		List<ConditionComplexCommand> conditionComplexList = promotionConditionComplexDao.findPromotionConditionComplexByPromotionId(id);
		SettingNormalCommand setting = promotionSettingDao.findByPromotionId(id);
		List<SettingComplexCommand> settingComplexList = promotionSettingComplexDao.findPromotionSettingComplexByPromotionId(id);
		cmd.setAudiences(audience);
		cmd.setScope(scope);
		cmd.setConditionNormal(condition);
		cmd.setConditionComplexList(conditionComplexList);
		cmd.setSettingNormal(setting);
		cmd.setSettingComplexList(settingComplexList);

		/* 根据条件类型封装前端表现所需数据 */
		if (null != condition){
			String type = condition.getConditionType();
			if (ConditionMasterType.EXP_NORMAL.equalsIgnoreCase(type) || ConditionMasterType.EXP_NORMALSTEP.equalsIgnoreCase(type)){ // 常规||常规阶梯
				condition.setExpressionList(generateExpressionList(condition.getConditionExpress(), condition.getConditionName(), true));
				if (null != setting){
					setting.setExpressionList(generateExpressionList(setting.getSettingExpression(), setting.getSettingName(), false));
				}
			}
			if (ConditionMasterType.EXP_STEP.equalsIgnoreCase(type) || ConditionMasterType.EXP_NORMALSTEP.equalsIgnoreCase(type)){ // 阶梯||常规阶梯
				/* 阶梯类型-条件项数值 */
				if (null != conditionComplexList){
					for (ConditionComplexCommand ccc : conditionComplexList){
						String exp = ccc.getConditionExpress();
						Matcher matcher = Pattern.compile("\\(([0-9]+)").matcher(exp);
						if (matcher.find()){
							ccc.setNumber(matcher.group(1));
						}
					}
				}
				/* 阶梯类型-优惠设置范围,范围类型及数值 */
				if (null != setting && null != settingComplexList && settingComplexList.size() > 0){
					for (int i = 0; i < PromotionSettingNormal.SETTING_TYPE_ARRAY.length; i++){
						String settingType = PromotionSettingNormal.SETTING_TYPE_ARRAY[i];
						String exp = settingComplexList.get(0).getSettingExpression();
						if (exp.startsWith(settingType)){
							setting.setStepScopeType(settingType);
							Matcher matcher = Pattern.compile(",([^)]+)\\)").matcher(exp);
							if (matcher.find()){
								setting.setStepScope(matcher.group(1));
							}
							break;
						}
					}
					for (SettingComplexCommand scc : settingComplexList){
						String exp = scc.getSettingExpression();
						Matcher matcher = Pattern.compile("\\(([0-9]+)").matcher(exp);
						// 且不是赠品 何波
						if (matcher.find() && !exp.startsWith("scpgift")){
							scc.setStepNumber(new Integer(matcher.group(1)));
						}
					}
				}
			}
			if (ConditionMasterType.EXP_CHOICE.equalsIgnoreCase(type)){ // 选购
				/* 选购类型-条件项范围表达式 */
				if (null != conditionComplexList){
					for (ConditionComplexCommand ccc : conditionComplexList){
						String exp = ccc.getConditionExpress();
						Matcher matcher = Pattern.compile(",([^)]+)\\)").matcher(exp);
						if (matcher.find()){
							ccc.setScope(matcher.group(1));
						}
					}
				}
				/* 选购类型-优惠设置范围类型及数值 */
				if (null != settingComplexList){
					for (SettingComplexCommand scc : settingComplexList){
						String exp = scc.getSettingExpression();
						Matcher matcher = Pattern.compile("\\(([0-9]+)").matcher(exp);
						if (matcher.find()){
							scc.setStepNumber(new Integer(matcher.group(1)));
						}
						for (int i = 0; i < PromotionSettingNormal.SETTING_TYPE_ARRAY.length; i++){
							String scopeType = PromotionSettingNormal.SETTING_TYPE_ARRAY[i];
							if (exp.startsWith(scopeType)){
								scc.setChoiceScope(scopeType);
								break;
							}
						}
					}
				}
			}
		}

		log.debug(JsonFormatUtil.format(cmd));
		return cmd;
	}

	/**
	 * 将常规条件的表达式与文本拆分
	 * 
	 * @param expression
	 * @param text
	 * @return
	 */
	private List<SimpleExpressionCommand> generateExpressionList(String expression,String text,boolean isCondition){
		List<SimpleExpressionCommand> list = new ArrayList<SimpleExpressionCommand>();

		if (StringUtils.isBlank(expression) || StringUtils.isBlank(text))
			return list;

		String[] expArr = expression.split("\\||&");
		String[] txtArr = text.split("\n");
		for (int i = 0; i < expArr.length; i++){
			SimpleExpressionCommand cmd = new SimpleExpressionCommand();

			String exp = expArr[i];
			String txt = txtArr[i];
			String mark = "";
			String scopeType = "";
			String scope = "";

			if (i > 0){
				exp = expression.substring(expression.indexOf(exp) - 1, expression.indexOf(exp)) + exp;
				txt = txt.substring(1);
				mark = txtArr[i].substring(0, 1);
			}else{
				exp = "&" + exp;
			}

			if (isCondition){
				for (int j = 0; j < ConditionType.CONDITION_EXPRESSION_ARRAY.length; j++){
					if (exp.substring(1).startsWith(ConditionType.CONDITION_EXPRESSION_ARRAY[j])){
						scopeType = j + "";
						break;
					}
				}
			}else{
				for (int j = 0; j < PromotionSettingNormal.SETTING_TYPE_ARRAY.length; j++){
					if (exp.substring(1).startsWith(PromotionSettingNormal.SETTING_TYPE_ARRAY[j])){
						scopeType = j + "";
						break;
					}
				}
			}

			Matcher matcher = Pattern.compile(",([a-z]+:[0-9]+)").matcher(exp);
			if (matcher.find()){
				scope = matcher.group(1);
			}

			cmd.setExpression(exp);
			cmd.setText(txt);
			cmd.setMark(mark);
			cmd.setScope(scope);
			cmd.setScopeType(scopeType);

			list.add(cmd);
		}
		return list;
	}

	@Override
	public Long savePromotionHead(HeadCommand headCommand,Long userId){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionHead head = null;

		boolean isNew = null == headCommand.getId();

		if (isNew){ // 新建
			head = new PromotionHead();
			head.setEndTime(headCommand.getEndTime());
			head.setPromotionLogoType(headCommand.getPromotionLogoType());
			head.setPromotionName(headCommand.getPromotionName());
			head.setStartTime(headCommand.getStartTime());
			head.setCreateId(userId);
			Date date = new Date();
			head.setCreateTime(date);
			head.setShopId(headCommand.getShopId());
			head.setLifecycle(PromotionHead.LIFECYCLE_UNACTIVATED);
			// 何波 添加修改人和修改时间
			head.setLastUpdateId(userId);
			head.setLastUpdateTime(date);
			promotionDao.save(head);
		}else{ // 更新
			head = promotionDao.getByPrimaryKey(headCommand.getId());
			if (null == head)
				throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

			if (null != head.getCopyFrom()){ // 有效期内修改：只允许修改开始时间与结束时间
				Date startTime = headCommand.getStartTime();
				Date endTime = headCommand.getEndTime();
				if (null != startTime){
					head.setStartTime(startTime);
				}
				if (null != endTime){
					head.setEndTime(endTime);
				}
			}else{
				head.setEndTime(headCommand.getEndTime());
				head.setPromotionLogoType(headCommand.getPromotionLogoType());
				head.setPromotionName(headCommand.getPromotionName());
				head.setStartTime(headCommand.getStartTime());
			}
			head.setLastUpdateId(userId);
			head.setLastUpdateTime(new Date());
		}
		record(head, userId, PromotionOperationLog.OPERATION_TYPE_EDIT); // 日志
		return head.getId();
	}

	@Override
	public Long savePromotionAudience(AudienceCommand audienceCommand,Long userId){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionAudiences audiences = null;
		PromotionHead head = promotionDao.getByPrimaryKey(audienceCommand.getPromotionId());
		if (null == head)
			throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

		forbidUpdateInEffectiveTime(head);

		boolean isNew = null == audienceCommand.getId();

		if (isNew){ // 新建
			audiences = new PromotionAudiences();
		}else{ // 更新
			audiences = promotionAudiencesDao.getByPrimaryKey(audienceCommand.getId());
			if (null == audiences)
				throw new BusinessException(ErrorCodes.PROMOTION_STEP_INEXISTENCE);
		}
		audiences.setComboExpression(audienceCommand.getComboExpression());
		audiences.setComboId(audienceCommand.getComboId());
		audiences.setComboName(audienceCommand.getComboName());
		audiences.setComboType(audienceCommand.getComboType());
		audiences.setPromotionId(head.getId());
		if (isNew){
			promotionAudiencesDao.save(audiences);
		}

		return audiences.getId();
	}

	@Override
	public Long savePromotionScope(ScopeCommand scopeCommand,Long userId){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionScope scope = null;
		PromotionHead head = promotionDao.getByPrimaryKey(scopeCommand.getPromotionId());
		if (null == head)
			throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

		forbidUpdateInEffectiveTime(head);

		boolean isNew = null == scopeCommand.getId();

		if (isNew){ // 新建
			scope = new PromotionScope();
		}else{ // 更新
			scope = promotionScopeDao.getByPrimaryKey(scopeCommand.getId());
			if (null == scope)
				throw new BusinessException(ErrorCodes.PROMOTION_STEP_INEXISTENCE);
		}
		scope.setComboExpression(scopeCommand.getComboExpression());
		scope.setComboId(scopeCommand.getComboId());
		scope.setComboName(scopeCommand.getComboName());
		scope.setComboType(scopeCommand.getComboType());
		scope.setPromotionId(head.getId());
		if (isNew){
			promotionScopeDao.save(scope);
		}

		return scope.getId();
	}

	@Override
	public Long savePromotionCondition(ConditionNormalCommand conditionCommand,Long userId){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionHead head = promotionDao.getByPrimaryKey(conditionCommand.getPromotionId());
		if (null == head)
			throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

		forbidUpdateInEffectiveTime(head);

		PromotionConditionNormal conditionNormal = null;
		boolean isNew = null == conditionCommand.getId();
		if (isNew){ // 新建
			conditionNormal = new PromotionConditionNormal();
		}else{ // 修改
			conditionNormal = promotionConditionDao.getByPrimaryKey(conditionCommand.getId());
			if (null == conditionNormal)
				throw new BusinessException(ErrorCodes.PROMOTION_STEP_INEXISTENCE);

			List<ConditionComplexCommand> conditionComplexList = promotionConditionComplexDao
					.findPromotionConditionComplexByPromotionId(conditionNormal.getPromotionId());
			for (ConditionComplexCommand pcc : conditionComplexList){ // 使原有复杂条件失效
				promotionConditionComplexDao.deleteByPrimaryKey(pcc.getId());
			}
		}
		String type = conditionCommand.getConditionType(); // 条件类型
		conditionNormal.setConditionType(type);
		conditionNormal.setPromotionId(head.getId());
		if (ConditionMasterType.EXP_NORMAL.equals(type)){ // 常规类型
			conditionNormal.setConditionExpress(conditionCommand.getConditionExpress());
			conditionNormal.setConditionName(conditionCommand.getConditionName());
			if (isNew)
				promotionConditionDao.save(conditionNormal);
		}else if (ConditionMasterType.EXP_STEP.equals(type)){ // 阶梯类型
			conditionNormal.setConditionExpress(null);
			conditionNormal.setConditionName(null);

			String[] expArr = conditionCommand.getConditionExpress().split("\n");
			String[] expTxtArr = conditionCommand.getConditionName().split("\n");
			if (expArr.length == 0 || expArr.length != expTxtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			if (isNew)
				promotionConditionDao.save(conditionNormal);
			for (int i = 0; i < expArr.length; i++){
				PromotionConditionComplex conditionComplex = new PromotionConditionComplex();
				conditionComplex.setComplexType(ConditionMasterType.EXP_STEP);
				conditionComplex.setConditionExpress(expArr[i]);
				conditionComplex.setConditionName(expTxtArr[i]);
				conditionComplex.setNormalConditionId(conditionNormal.getId());
				conditionComplex.setPromotionId(conditionNormal.getPromotionId());
				conditionComplex.setStepPriority(i + 1);
				promotionConditionComplexDao.save(conditionComplex);
			}
		}else if (ConditionMasterType.EXP_CHOICE.equals(type)){ // 选购类型
			conditionNormal.setConditionExpress(null);
			conditionNormal.setConditionName(null);

			String[] expArr = conditionCommand.getConditionExpress().split("\n\n");
			String[] expTxtArr = conditionCommand.getConditionName().split("\n\n");
			if (expArr.length != 2 || expArr.length != expTxtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			if (isNew)
				promotionConditionDao.save(conditionNormal);

			String[] mainExpArr = expArr[0].split("\n");
			String[] mainTxtArr = expTxtArr[0].split("\n");
			if (mainExpArr.length == 0 || mainExpArr.length != mainTxtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			for (int i = 0; i < mainExpArr.length; i++){
				PromotionConditionComplex mainCondition = new PromotionConditionComplex(); // 主商品条件
				mainCondition.setComplexType(ConditionMasterType.EXP_CHOICE);
				mainCondition.setConditionExpress(mainExpArr[0]);
				mainCondition.setConditionName(mainTxtArr[0]);
				mainCondition.setNormalConditionId(conditionNormal.getId());
				mainCondition.setPromotionId(conditionNormal.getPromotionId());
				mainCondition.setChoiceMark(PromotionConditionComplex.CHOICE_MAIN);
				mainCondition.setStepPriority(0); // 主商品优先级都为0
				promotionConditionComplexDao.save(mainCondition);
			}

			String[] deputyExpArr = expArr[1].split("\n");
			String[] deputyTxtArr = expTxtArr[1].split("\n");
			if (deputyExpArr.length == 0 || deputyExpArr.length != deputyTxtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			for (int i = 0; i < deputyExpArr.length; i++){
				PromotionConditionComplex deputyCondition = new PromotionConditionComplex(); // 选购商品条件
				deputyCondition.setComplexType(ConditionMasterType.EXP_CHOICE);
				deputyCondition.setConditionExpress(deputyExpArr[i]);
				deputyCondition.setConditionName(deputyTxtArr[i]);
				deputyCondition.setNormalConditionId(conditionNormal.getId());
				deputyCondition.setPromotionId(conditionNormal.getPromotionId());
				deputyCondition.setChoiceMark(PromotionConditionComplex.CHOICE_DEPUTY);
				deputyCondition.setStepPriority(i + 1);
				promotionConditionComplexDao.save(deputyCondition);
			}
		}else if (ConditionMasterType.EXP_NORMALSTEP.equals(type)){ // 常规+阶梯
			String[] expArr = conditionCommand.getConditionExpress().split("\n\n");
			String[] txtArr = conditionCommand.getConditionName().split("\n\n");
			if (expArr.length != 2 || expArr.length != txtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			String expNormal = expArr[0];
			String txtNormal = txtArr[0];
			String[] expStepArr = expArr[1].split("\n");
			String[] txtStepArr = txtArr[1].split("\n");
			if (expStepArr.length == 0 || expStepArr.length != txtStepArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			conditionNormal.setConditionExpress(expNormal);
			conditionNormal.setConditionName(txtNormal);
			if (isNew)
				promotionConditionDao.save(conditionNormal);
			for (int i = 0; i < expStepArr.length; i++){
				PromotionConditionComplex conditionComplex = new PromotionConditionComplex();
				conditionComplex.setComplexType(ConditionMasterType.EXP_NORMALSTEP);
				conditionComplex.setConditionExpress(expStepArr[i]);
				conditionComplex.setConditionName(txtStepArr[i]);
				conditionComplex.setNormalConditionId(conditionNormal.getId());
				conditionComplex.setPromotionId(conditionNormal.getPromotionId());
				conditionComplex.setStepPriority(i + 1);
				promotionConditionComplexDao.save(conditionComplex);
			}
		}

		if (isNew){
			initPriority(conditionNormal, head); // 默认优先级处理
		}

		return conditionNormal.getId();
	}

	@Override
	public Long savePromotionSetting(SettingNormalCommand settingCommand,Long userId){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionHead head = promotionDao.getByPrimaryKey(settingCommand.getPromotionId());
		if (null == head)
			throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

		forbidUpdateInEffectiveTime(head);

		PromotionSettingNormal settingNormal = null;
		boolean isNew = null == settingCommand.getId();
		if (isNew){ // 新建
			settingNormal = new PromotionSettingNormal();
		}else{ // 修改
			settingNormal = promotionSettingDao.getByPrimaryKey(settingCommand.getId());
			if (null == settingNormal)
				throw new BusinessException(ErrorCodes.PROMOTION_STEP_INEXISTENCE);

			List<SettingComplexCommand> settingComplexList = promotionSettingComplexDao.findPromotionSettingComplexByPromotionId(head
					.getId());
			for (SettingComplexCommand psc : settingComplexList){ // 复合条件全部失效
				promotionSettingComplexDao.deleteByPrimaryKey(psc.getId());
			}
		}
		settingNormal.setPromotionId(head.getId());
		String type = settingCommand.getConditionType();
		if (ConditionMasterType.EXP_NORMAL.equals(type)){ // 常规类型
			settingNormal.setSettingExpression(settingCommand.getSettingExpression());
			settingNormal.setSettingName(settingCommand.getSettingName());
			if (isNew)
				promotionSettingDao.save(settingNormal);
		}else if (ConditionMasterType.EXP_STEP.equals(type)){ // 阶梯类型
			String[] expArr = settingCommand.getSettingExpression().split("\n");
			String[] expTxtArr = settingCommand.getSettingName().split("\n");
			if (expArr.length == 0 || expArr.length != expTxtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			if (isNew)
				promotionSettingDao.save(settingNormal);
			List<ConditionComplexCommand> conditionList = promotionConditionComplexDao
					.findPromotionConditionComplexByPromotionId(settingCommand.getPromotionId());
			int i = 0;
			for (ConditionComplexCommand pcc : conditionList){
				PromotionSettingComplex settingComplex = new PromotionSettingComplex();
				settingComplex.setComplexConditionId(pcc.getId());
				settingComplex.setPromotionId(settingCommand.getPromotionId());
				settingComplex.setSettingExpression(expArr[i]);
				settingComplex.setSettingName(expTxtArr[i]);
				promotionSettingComplexDao.save(settingComplex);
				i++;
			}
		}else if (ConditionMasterType.EXP_CHOICE.equals(type)){ // 选购类型
			String[] expArr = settingCommand.getSettingExpression().split("\n");
			String[] expTxtArr = settingCommand.getSettingName().split("\n");
			if (expArr.length == 0 || expArr.length != expTxtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			if (isNew)
				promotionSettingDao.save(settingNormal);
			List<ConditionComplexCommand> conditionList = promotionConditionComplexDao
					.findPromotionConditionComplexByPromotionId(settingCommand.getPromotionId());
			int i = 0;
			for (ConditionComplexCommand pcc : conditionList){
				// 去掉选购的限制
				// if (PromotionConditionComplex.CHOICE_DEPUTY.equals(pcc.getChoiceMark())) { //选购商品
				PromotionSettingComplex settingComplex = new PromotionSettingComplex();
				settingComplex.setComplexConditionId(pcc.getId());
				settingComplex.setPromotionId(settingCommand.getPromotionId());
				settingComplex.setSettingExpression(expArr[i]);
				settingComplex.setSettingName(expTxtArr[i]);
				promotionSettingComplexDao.save(settingComplex);
				i++;
				// }
			}
		}else if (ConditionMasterType.EXP_NORMALSTEP.equals(type)){ // 常规+阶梯
			String[] expArr = settingCommand.getSettingExpression().split("\n\n");
			String[] txtArr = settingCommand.getSettingName().split("\n\n");
			if (expArr.length != 2 || expArr.length != txtArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}
			String expNormal = expArr[0];
			String txtNormal = txtArr[0];
			String[] expStepArr = expArr[1].split("\n");
			String[] txtStepArr = txtArr[1].split("\n");
			if (expStepArr.length == 0 || expStepArr.length != txtStepArr.length){
				throw new BusinessException(ErrorCodes.PROMOTION_CONDITION_EXPRESSION_ERROR);
			}

			/* 同步促销条件的优惠券 */
			PromotionConditionNormal condition = promotionConditionDao.findPromotionConditionNormalByPromotionId(head.getId());
			String ordcoupon = ConditionType.CONDITION_EXPRESSION_ARRAY[7]; // 整单优惠券
			String scpcoupon = ConditionType.CONDITION_EXPRESSION_ARRAY[8]; // 商品范围优惠券
			Matcher matcher = Pattern.compile("((" + ordcoupon + "|" + scpcoupon + ")[^)]+\\))").matcher(condition.getConditionExpress());
			while (matcher.find()){
				expNormal += "&" + matcher.group(1);
			}

			settingNormal.setSettingExpression(expNormal);
			settingNormal.setSettingName(txtNormal);
			if (isNew)
				promotionSettingDao.save(settingNormal);
			List<ConditionComplexCommand> conditionList = promotionConditionComplexDao
					.findPromotionConditionComplexByPromotionId(settingCommand.getPromotionId());
			int i = 0;
			for (ConditionComplexCommand pcc : conditionList){
				PromotionSettingComplex settingComplex = new PromotionSettingComplex();
				settingComplex.setComplexConditionId(pcc.getId());
				settingComplex.setPromotionId(settingCommand.getPromotionId());
				settingComplex.setSettingExpression(expStepArr[i]);
				settingComplex.setSettingName(txtStepArr[i]);
				promotionSettingComplexDao.save(settingComplex);
				i++;
			}
		}

		return settingNormal.getId();
	}

	@Override
	public Long copyPromotion(Long fromPromotionId,Long userId,boolean isUpdate){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionHead head = promotionDao.getByPrimaryKey(fromPromotionId);
		if (null == head)
			throw new BusinessException(ErrorCodes.PROMOTION_INEXISTENCE);

		int lifecycle = sdkPromotionManager.calculateLifecycle(head.getLifecycle(), head.getStartTime(), head.getEndTime());

		if (PromotionHead.LIFECYCLE_UNACTIVATED.equals(lifecycle))
			throw new BusinessException(ErrorCodes.PROMOTION_ILLEAGAL_COPY);

		// 复制头部
		PromotionHead copyHead = new PromotionHead();
		BeanUtils.copyProperties(head, copyHead);
		copyHead.setId(null);
		copyHead.setLifecycle(PromotionHead.LIFECYCLE_UNACTIVATED);
		copyHead.setCancelId(null);
		copyHead.setCancelTime(null);
		copyHead.setCreateId(userId);
		copyHead.setCreateTime(new Date());
		copyHead.setLastUpdateId(null);
		copyHead.setLastUpdateTime(null);
		copyHead.setPromotionName(head.getPromotionName() + "_COPY_" + new Date().getTime());
		if (isUpdate){
			copyHead.setCopyFrom(head.getId());
		}else{
			copyHead.setCopyFrom(null);
		}
		copyHead.setPublishId(null);
		copyHead.setPublishTime(null);
		copyHead = promotionDao.save(copyHead);

		Long copyId = copyHead.getId();

		// 复制受益人群
		PromotionAudiences audience = promotionAudiencesDao.findByPromotionId(fromPromotionId);
		PromotionAudiences copyAudiences = new PromotionAudiences();
		BeanUtils.copyProperties(audience, copyAudiences);
		copyAudiences.setId(null);
		copyAudiences.setPromotionId(copyId); // 与复制的促销关联
		promotionAudiencesDao.save(copyAudiences);

		// 复制范围
		PromotionScope scope = promotionScopeDao.findByPromotionId(fromPromotionId);
		PromotionScope copyScope = new PromotionScope();
		BeanUtils.copyProperties(scope, copyScope);
		copyScope.setId(null);
		copyScope.setPromotionId(copyId); // 与复制的促销关联
		promotionScopeDao.save(copyScope);

		// 复制条件
		PromotionConditionNormal condition = promotionConditionDao.findPromotionConditionNormalByPromotionId(fromPromotionId);
		PromotionConditionNormal copyCondition = new PromotionConditionNormal();
		BeanUtils.copyProperties(condition, copyCondition);
		copyCondition.setId(null);
		copyCondition.setPromotionId(copyId); // 与复制的促销关联
		promotionConditionDao.save(copyCondition);

		// 复制设置
		PromotionSettingNormal setting = promotionSettingDao.findPromotionSettingNormalByPromotionId(fromPromotionId);
		PromotionSettingNormal copySetting = new PromotionSettingNormal();
		BeanUtils.copyProperties(setting, copySetting);
		copySetting.setId(null);
		copySetting.setPromotionId(copyId); // 与复制的促销关联
		promotionSettingDao.save(copySetting);

		// 复制复杂条件和设置
		List<PromotionConditionComplex> conditionComplexList = promotionConditionComplexDao
				.findPromotionConditionComplexListByPromotionId(fromPromotionId);
		for (PromotionConditionComplex pcc : conditionComplexList){
			PromotionConditionComplex copy = new PromotionConditionComplex();
			BeanUtils.copyProperties(pcc, copy);
			copy.setId(null);
			copy.setNormalConditionId(copyCondition.getId());
			copy.setPromotionId(copyId);
			promotionConditionComplexDao.save(copy);

			List<PromotionSettingComplex> settingComplexList = promotionSettingComplexDao
					.findPromotionSettingComplexByConditionComplexId(pcc.getId());
			for (PromotionSettingComplex psc : settingComplexList){
				PromotionSettingComplex copySC = new PromotionSettingComplex();
				BeanUtils.copyProperties(psc, copySC);
				copySC.setId(null);
				copySC.setComplexConditionId(copy.getId());
				copySC.setPromotionId(copyId);
				promotionSettingComplexDao.save(copySC);
			}
		}

		record(head, userId, PromotionOperationLog.OPERATION_TYPE_COPY); // 日志

		return copyId;
	}

	@Override
	public boolean checkPromotionName(Long id,String name){
		PromotionHead pro = promotionDao.findPromotionByName(name);
		if (null == id){
			return null == pro;
		}else{
			return null == pro || pro.getId().equals(id);
		}
	}

	@Override
	public void deleteConditionAndSettingByPromotionId(Long id){
		promotionConditionDao.deleteByPromotionId(id);
		promotionConditionComplexDao.deleteByPromotionId(id);
		promotionSettingDao.deleteByPromotionId(id);
		promotionSettingComplexDao.deleteByPromotionId(id);
	}

	/**
	 * 有效期内修改：只允许修改开始时间与结束时间
	 * 
	 * @param head
	 */
	private void forbidUpdateInEffectiveTime(PromotionHead head){
		if (null != head.getCopyFrom())
			throw new BusinessException(ErrorCodes.PROMOTION_EDIT_DURING_EFFECTIVE);
	}

	/**
	 * 记录日志
	 * 
	 * @param promotionId
	 * @param userId
	 * @param type
	 */
	private void record(PromotionHead head,Long userId,Integer type){
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);

		PromotionOperationLog l = new PromotionOperationLog();
		l.setNote("[" + head.getPromotionName() + "]" + " [" + user.getRealName() + "]" + " ["
				+ PromotionOperationLog.OPERATION_TYPE_TO_NAME_MAP.get(type) + "]");
		l.setOperateTime(new Date());
		l.setOperationType(type);
		l.setOperatorId(userId);
		l.setPromotionId(head.getId());
		promotionOperationLogDao.save(l);
	}

	/**
	 * 初始化促销优先级
	 * 
	 * @param condition
	 * @param head
	 *            持久化状态
	 */
	private void initPriority(PromotionConditionNormal condition,PromotionHead head){
		String type = condition.getConditionType();
		int priority = ConditionMasterType.PRIORITY_DEFAULT;
		if (ConditionMasterType.EXP_NORMAL.equals(type)){
			priority = ConditionMasterType.PRIORITY_NORMAL;
		}else if (ConditionMasterType.EXP_STEP.equals(type)){
			priority = ConditionMasterType.PRIORITY_STEP;
		}else if (ConditionMasterType.EXP_CHOICE.equals(type)){
			priority = ConditionMasterType.PRIORITY_CHOICE;
		}else if (ConditionMasterType.EXP_NORMALCHOICE.equals(type)){
			priority = ConditionMasterType.PRIORITY_NORMAL_CHOICE;
		}else if (ConditionMasterType.EXP_NORMALSTEP.equals(type)){
			priority = ConditionMasterType.PRIORITY_NORMAL_STEP;
		}
		head.setDefaultpriority(priority);
	}

	@Override
	@Transactional(readOnly = true)
	public PromotionInformation findByCategoryCode(String categoryCode){
		return promotionInformationDao.findByCategoryCode(categoryCode);
	}

	@Override
	public Integer createOrUpdatePromotionInformationPage(Long id,String categoryCode,String content){
		PromotionInformation prmInfo = promotionInformationDao.findByCategoryCode(categoryCode);
		if (null == prmInfo){
			prmInfo = new PromotionInformation();
			prmInfo.setCategoryCode(categoryCode);
			prmInfo.setContent(content);
			prmInfo.setCreateTime(new Date());
			promotionInformationDao.save(prmInfo);
		}else{
			prmInfo.setModifyTime(new Date());
			prmInfo.setContent(content);
			promotionInformationDao.save(prmInfo);
		}
		// remove cache
		cacheManager.remove(CacheKeyConstant.SHOPPING_CART_PRM);

		return null;
	}
}