package com.baozun.nebula.sdk.manager.impl;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.param.PurchaseLimitConditionType;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.promotion.LimitAudienceDao;
import com.baozun.nebula.dao.promotion.LimitConditionDao;
import com.baozun.nebula.dao.promotion.LimitHeadDao;
import com.baozun.nebula.dao.promotion.LimitScopeDao;
import com.baozun.nebula.dao.rule.ItemTagRuleDao;
import com.baozun.nebula.dao.rule.MemberTagRuleDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.promotion.LimitAudience;
import com.baozun.nebula.model.promotion.LimitCondition;
import com.baozun.nebula.model.promotion.LimitHead;
import com.baozun.nebula.model.promotion.LimitScope;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.rule.MemberTagRule;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkFilterManager;
import com.baozun.nebula.sdk.manager.SdkLimitManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationConditionManager;
import com.baozun.nebula.utilities.common.Validator;

/**
 * 
 * @author 项硕
 */
@Transactional
@Service("sdkLimitManager") 
public class SdkLimitManagerImpl implements SdkLimitManager {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SdkLimitManagerImpl.class);

	@Autowired
	private LimitHeadDao limitHeadDao;
	@Autowired
	private LimitAudienceDao limitAudienceDao;
	@Autowired
	private LimitScopeDao limitScopeDao;
	@Autowired
	private LimitConditionDao limitConditionDao;
	@Autowired
	private MemberTagRuleDao memberTagRuleDao;
	@Autowired
	private ItemTagRuleDao itemTagRuleDao;
	@Autowired
	private UserDao userDao; 
	@Autowired
	private SdkFilterManager sdkFilterManager;
	@Autowired
	SdkPromotionCalculationConditionManager sdkPromotionConditionManager;

	@Override
	@Transactional(readOnly=true)
	public Pagination<LimitCommand> findLimitCommandConditionallyWithPage(Page page, Sort[] sorts,
			Map<String, Object> queryMap,String type,Long shopId) {
		Pagination<LimitCommand> pagination = null;
		if (type.isEmpty()) {
			pagination = limitHeadDao.findLimitCommandConditionallyWithPage(page, sorts, queryMap,shopId);
			List<LimitCommand> items = new ArrayList<LimitCommand>();
			if(Validator.isNotNullOrEmpty(pagination.getItems())){
				for(LimitCommand lim : pagination.getItems()){
					int lifecycle = calculateLifecycle(lim.getLifecycle(),
							lim.getStartTime(),lim.getEndTime());
					lim.setLifecycle(lifecycle);
					items.add(lim);
				}
				pagination.setItems(items);
			}
		} else {
			queryMap.put("lifecycle", LimitHead.LIFECYCLE_UNACTIVE); 
			pagination = limitHeadDao.findEditLimitCommandConditionallyWithPage(page, sorts, queryMap,shopId);
		} 
		
		return pagination;
	}
	
	@Override
	public void cancelLimit(Long lid, Long userId) {
		User user = userDao.getByPrimaryKey(userId);	//当前用户
		if (null == user) throw new BusinessException(Constants.USER_USER_NOTFOUND);
		
		LimitHead head = limitHeadDao.getByPrimaryKey(lid);
		if (null == head) throw new BusinessException(Constants.LIMIT_INEXISTED);
		
		if (LimitHead.LIFECYCLE_CANCELED.equals(head.getLifecycle())
				|| LimitHead.LIFECYCLE_UNACTIVE.equals(head.getLifecycle())) //未启用
			throw new BusinessException(Constants.LIMIT_NOT_ACTIVATED);
		
		head.setCancelId(userId);
		head.setCancelTime(new Date());
		head.setLifecycle(LimitHead.LIFECYCLE_CANCELED);
	}
	
/*********************************************************************************************************************************/


	@Override
	public Long saveOrUpdateLimitHead(LimitCommand cmd, Long userId) {
		Long id = cmd.getId();
		LimitHead head;
		if (null == id) {	// 新建
			head = new LimitHead();
			head.setCreateId(userId);
			head.setCreateTime(new Date());
			head.setLifecycle(LimitHead.LIFECYCLE_UNACTIVE);
			head.setShopId(cmd.getShopId());
		} else {	// 更新
			head = limitHeadDao.getByPrimaryKey(id);
			if (null == head) throw new BusinessException(Constants.LIMIT_INEXISTED);
			head.setUpdateId(userId);
			head.setUpdateTime(new Date());
		}
		head.setName(cmd.getName());
		head.setStartTime(cmd.getStartTime());
		head.setEndTime(cmd.getEndTime());
		if (null == id) limitHeadDao.save(head);
		return head.getId();
	}

	@Override
	public Long saveOrUpdateLimitAudience(LimitCommand cmd, Long userId) {
		Long comboId = cmd.getMemberComboId();
		MemberTagRule combo = memberTagRuleDao.getByPrimaryKey(comboId);
		if (null == combo) throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_INEXISTED);
		
		Long limitId = cmd.getId();
		Long audienceId = cmd.getAudienceId();
		LimitAudience audience;
		if (null == audienceId) {	// 新建
			audience = new LimitAudience();
			audience.setLimitId(limitId);
		} else {	// 更新
			audience = limitAudienceDao.getByPrimaryKey(audienceId);
		}
		audience.setComboExpression(combo.getExpression());
		audience.setComboId(combo.getId());
		audience.setComboName(combo.getText());
		audience.setComboType(combo.getType());
		if (null == audienceId) limitAudienceDao.save(audience);
		return audience.getId();
	}

	@Override
	public Long saveOrUpdateLimitScope(LimitCommand cmd, Long userId) {
		Long comboId = cmd.getProductComboId();
		ItemTagRule combo = itemTagRuleDao.getByPrimaryKey(comboId);
		if (null == combo) throw new BusinessException(Constants.PRODUCT_FILTER_INEXISTED);
		
		Long limitId = cmd.getId();
		Long scopeId = cmd.getScopeId();
		LimitScope scope;
		if (null == scopeId) {	// 新建
			scope = new LimitScope();
			scope.setLimitId(limitId);
		} else {	// 更新
			scope = limitScopeDao.getByPrimaryKey(scopeId);
		}
		scope.setComboExpression(combo.getExpression());
		scope.setComboId(combo.getId());
		scope.setComboName(combo.getText());
		scope.setComboType(combo.getType());
		if (null == scopeId) limitScopeDao.save(scope);
		
		/* 初始化优先级 */
		LimitHead head = limitHeadDao.getByPrimaryKey(limitId);
		initPriority(combo.getType(), head);
		
		return scope.getId();
	}

	@Override
	public Long saveOrUpdateLimitCondition(LimitCommand cmd, Long userId) {
		Long limitId = cmd.getId();
		Long conditionId = cmd.getConditionId();
		LimitCondition condition;
		if (null == conditionId) {	// 新建
			condition = new LimitCondition();
			condition.setLimitId(limitId);
		} else {	// 更新
			condition = limitConditionDao.getByPrimaryKey(conditionId);
		}
		condition.setExpression(cmd.getConditionExpression());
		condition.setText(cmd.getConditionText());
		if (null == conditionId) limitConditionDao.save(condition);
		
		return condition.getId();
	}

	@Override
	@Transactional(readOnly=true)
	public LimitCommand findLimitCommandById(Long id) {
		LimitCommand cmd = limitHeadDao.findLimitCommandById(id);
		if (null == cmd) throw new BusinessException(Constants.LIMIT_INEXISTED);
		String exp = cmd.getConditionExpression();
		String txt = cmd.getConditionText();
		if (StringUtils.isNotBlank(exp) && StringUtils.isNotBlank(txt)) {
			cmd.setConditionExpressionList(Arrays.asList(exp.split(PurchaseLimitConditionType.AND)));
			cmd.setConditionTextList(Arrays.asList(txt.split(PurchaseLimitConditionType.NEW_LINE)));
		}
		return cmd;
	}

	@Override
	public void deleteStep(Long id) {
		limitHeadDao.deleteLimitConditionByLimitId(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<LimitCommand> checkBeforeActivation(Long id, Long shopId) {
		List<LimitCommand> list = limitHeadDao.findActiveLimitListByTimeScope(id, shopId);
//		for (LimitCommand cmd : list) {
//			int lifecycle = calculateLifecycle(cmd.getLifecycle(), cmd.getStartTime(), cmd.getEndTime());
//			if (LimitHead.LIFECYCLE_ACTIVE.equals(lifecycle) || LimitHead.LIFECYCLE_EFFECTIVE.equals(lifecycle)) {
//				
//			}
//		}
		return findConflictingLimitList(id, list, shopId);
	}

	/**
	 * 根据人群与商品范围查找冲突的限购
	 * @param id
	 * @param list
	 * @return
	 */
	@Transactional(readOnly=true)
	private List<LimitCommand> findConflictingLimitList(Long id, List<LimitCommand> list, Long shopId) {
		List<LimitCommand> rs = new ArrayList<LimitCommand>();
		LimitCommand major = limitHeadDao.findLimitCommandById(id); 
		String majorMemberExp = major.getMemberComboExpression();
		String majorProductExp = major.getProductComboExpression();
		for (LimitCommand cmd : list) {
			String memberExp = cmd.getMemberComboExpression();
			String productExp = cmd.getProductComboExpression();
			if (sdkFilterManager.isMemberConfilct(majorMemberExp, memberExp)
					&& sdkFilterManager.isProductConfilct(majorProductExp, productExp, shopId)) {
				rs.add(cmd);
			}
		}
		return rs;
	}

	@Override
	public void activateLimit(Long id, Long userId) {
		LimitCommand limit = limitHeadDao.findLimitCommandById(id);
		if (null == limit) throw new BusinessException(Constants.LIMIT_INEXISTED);
		if (null == limit.getAudienceId() || null == limit.getScopeId()
				|| null == limit.getConditionId()) 
			throw new BusinessException(Constants.LIMIT_INCOMPLETE_INFO);	// 限购信息不完整（4步不全）
		
		int lifecycle = calculateLifecycle(limit.getLifecycle(), limit.getStartTime(), limit.getEndTime());
		if (! LimitHead.LIFECYCLE_UNACTIVE.equals(lifecycle)) 
			throw new BusinessException(Constants.LIMIT_LIFECYCLE_ERROR);	// 只有‘待启用’可以执行‘启用’操作
		
		limitHeadDao.activateLimit(id, userId);
		publishLimit(new Date());	// 发布到缓存
	}

	@Override
	@Transactional(readOnly=true)
	public void publishLimit(Date currentDate) {
		List<LimitCommand> list = limitHeadDao.findActiveOrEffectiveLimitCommandList(currentDate);
		
		for(LimitCommand lmt :list ){ 
			
			if (lmt.getConditionExpression() != null && !lmt.getConditionExpression().isEmpty())
			{
				List<AtomicCondition> conditionList = sdkPromotionConditionManager.parseConditionByExpression(lmt.getConditionExpression());

				lmt.setAtomicLimitationList(conditionList);
			}
		}
		
		EngineManager.getInstance().setLimitCommandList(list);
		
		EngineManager.getInstance().setCrowdScopeList(memberTagRuleDao.findAllAvailableMemberTagRuleCommandList());
		EngineManager.getInstance().setItemScopeList(itemTagRuleDao.findAllAvailableCustomProductComboList());
		EngineManager.getInstance().build();
	}
	
	/**
	 * 计算促销生命周期
	 * @param lifecycle
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private int calculateLifecycle(Integer lifecycle, Date startTime, Date endTime) {
		Date now = new Date();
		if (now.compareTo(endTime) > 0)	// 只要过期了，就为‘已取消’
			return LimitHead.LIFECYCLE_CANCELED;
		if (LimitHead.LIFECYCLE_ACTIVE.equals(lifecycle) 
				&& now.compareTo(startTime) >= 0 && now.compareTo(endTime) <= 0) 
			return LimitHead.LIFECYCLE_EFFECTIVE;
		return lifecycle;
	}
	
	/**
	 * 初始化限购默认优先级
	 * @param exp
	 * @param head
	 */
	private void initPriority(Integer type, LimitHead head) {
		int priority = LimitHead.PRIORITY_DEFAULT;
		if (ItemTagRule.TYPE_PRODUCT.equals(type)) {
			priority = LimitHead.PRIORITY_ONE;
 		} else if (ItemTagRule.TYPE_CATEGORY.equals(type)) {
 			priority = LimitHead.PRIORITY_TWO;
 		} else if (ItemTagRule.TYPE_COMBO.equals(type)) {
 			priority = LimitHead.PRIORITY_THREE;
 		} 
		head.setDefaultPriority(priority);
	}
}
