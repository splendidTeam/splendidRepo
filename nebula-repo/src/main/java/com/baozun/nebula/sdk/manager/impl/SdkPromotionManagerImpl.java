package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.command.promotion.ConditionComplexCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionMarkdownPriceCommand;
import com.baozun.nebula.command.promotion.PromotionQueryCommand;
import com.baozun.nebula.command.promotion.PromotionSettingQueryCommand;
import com.baozun.nebula.command.promotion.SettingComplexCommand;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.promotion.PromotionConditionComplexDao;
import com.baozun.nebula.dao.promotion.PromotionDao;
import com.baozun.nebula.dao.promotion.PromotionMarkdownPriceDao;
import com.baozun.nebula.dao.promotion.PromotionOperationLogDao;
import com.baozun.nebula.dao.promotion.PromotionSettingComplexDao;
import com.baozun.nebula.dao.promotion.PromotionSettingDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.promotion.PromotionConditionNormal;
import com.baozun.nebula.model.promotion.PromotionHead;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;
import com.baozun.nebula.model.promotion.PromotionOperationLog;
import com.baozun.nebula.model.promotion.PromotionSettingNormal;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkFilterManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkItemTagRuleManager;
import com.baozun.nebula.sdk.manager.SdkMemberTagRuleManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationConditionManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationSettingManager;
import com.baozun.nebula.sdk.manager.SdkPromotionManager;
import com.baozun.nebula.sdk.manager.SdkPromotionMarkdownPriceManager;

@Transactional
@Service("sdkPromotionManager")
public class SdkPromotionManagerImpl implements SdkPromotionManager {
	// @Autowired
	// ConditionPromotionManager conditionPromotionManager;
	@Autowired
	private PromotionDao promotionDao;
	@Autowired
	private PromotionSettingComplexDao promotionSettingComplexDao;
	@Autowired
	private PromotionConditionComplexDao promotionConditionComplexDao;

	@Autowired
	private PromotionOperationLogDao promotionOperationLogDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SdkPromotionCalculationConditionManager sdkPromotionConditionManager;
	@Autowired
	private SdkPromotionCalculationSettingManager sdkPromotionSettingManager;
	@Autowired
	private SdkFilterManager sdkFilterManager;
	@Autowired
	private SdkMemberTagRuleManager sdkMemberTagRuleManager;
	@Autowired
	private SdkItemTagRuleManager sdkItemTagRuleManager;
	@Autowired
	private SdkItemManager sdkItemManager;
	@Autowired
	private PromotionSettingDao promotionSettingDao;
	@Autowired
	SdkPromotionMarkdownPriceManager sdkPromotionMarkdownPriceManager;
	
	private static final Logger log = LoggerFactory.getLogger(SdkPromotionCalculationConditionManager.class);

	@Override
	public List<PromotionBrief> doPromotion(ShoppingCartCommand shoppingCartCommand) {
		List<PromotionBrief> briefList = new ArrayList<PromotionBrief>();
		// List<PromotionConditionNormal> conditionList =
		// conditionPromotionManager.getModelListByRefer(shoppingCartCommand);
		List<PromotionConditionNormal> conditionList = new ArrayList<PromotionConditionNormal>();
		for (PromotionConditionNormal condition : conditionList) {
			PromotionBrief brief = new PromotionBrief();
			brief.setPromotionId(condition.getPromotionId());
			briefList.add(brief);
		}
		return briefList;
	}

	@Override
	public void inactivatePromotionById(Long promotionId, Long userId) {
		PromotionHead head = promotionDao.getByPrimaryKey(promotionId);
		if (null == head)
			throw new BusinessException(Constants.PROMOTION_INEXISTENCE);

		if (PromotionHead.LIFECYCLE_CANCELED.equals(head.getLifecycle()) || PromotionHead.LIFECYCLE_UNACTIVATED.equals(head.getLifecycle())) // 未启用
			throw new BusinessException(Constants.PROMOTION_NOT_ACTIVATED);

		head.setCancelId(userId);
		head.setCancelTime(new Date());
		head.setLifecycle(PromotionHead.LIFECYCLE_CANCELED);

		record(head, userId, PromotionOperationLog.OPERATION_TYPE_CANCEL); // 日志
	}

	@Override
	public int calculateLifecycle(int lifecycle, Date startTime, Date endTime) {
		Date now = new Date();
		if (now.compareTo(endTime) > 0) {
			return PromotionHead.LIFECYCLE_CANCELED;
		} else if (PromotionHead.LIFECYCLE_ACTIVATED.equals(lifecycle)) { // 已启用

			if (now.compareTo(startTime) >= 0 && now.compareTo(endTime) <= 0) { // 已生效
				return PromotionHead.LIFECYCLE_EFFECTIVE;
			} else if (now.compareTo(endTime) > 0) { // 已取消
				return PromotionHead.LIFECYCLE_CANCELED;
			}
		}
		return lifecycle;
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionQueryCommand> findConflictingPromotionListById(Long id, Long shopId) {
		List<PromotionQueryCommand> list = promotionDao.findActivePromotionListByTimeScope(id, shopId);
		list = findConflictingPromotionList(id, list, shopId);
		return list;
	}

	@Override
	public void activatePromotionById(Long id, Long userId) {
		PromotionQueryCommand promotion = promotionDao.findPromotionQueryCommandById(id);
		if (null == promotion)
			throw new BusinessException(Constants.PROMOTION_INEXISTENCE);

		if (promotion.getStartTime().compareTo(new Date()) <= 0)
			throw new BusinessException(Constants.PROMOTION_ACTIVATION_LATE); // 只有‘待启用’可以执行‘启用’操作

		int lifecycle = calculateLifecycle(promotion.getLifecycle(), promotion.getStartTime(), promotion.getEndTime());
		if (!PromotionHead.LIFECYCLE_UNACTIVATED.equals(lifecycle))
			throw new BusinessException(Constants.PROMOTION_LIFECYCLE_ERROR); // 只有‘待启用’可以执行‘启用’操作

		/*
		 * copyfrom不为空，说明该促销是一个从生效状态的促销复制过来的副本， 原促销目前可能‘已不在’或‘仍在’生效状态，
		 * 若‘仍在’生效状态，则启用副本时，取消启用原促销并将属性‘copyFrom’归null
		 * 若‘已不在’生效状态，说明此副本并非对原促销的紧急替换，则将属性‘copyFrom’归null，无须其余操作
		 */
		if (null != promotion.getCopyFrom()) {
			PromotionQueryCommand copy = promotionDao.findPromotionQueryCommandById(promotion.getCopyFrom());
			if (null != copy) {
				if (PromotionHead.LIFECYCLE_EFFECTIVE.equals(calculateLifecycle(copy.getLifecycle(), copy.getStartTime(), copy.getEndTime()))) {
					promotionDao.inactivatePromotionById(id, userId);
					record(new PromotionHead(copy.getId(), copy.getName()), userId, PromotionOperationLog.OPERATION_TYPE_CANCEL); // 日志
				}
			}
		}

		record(new PromotionHead(id, promotion.getName()), userId, PromotionOperationLog.OPERATION_TYPE_ACTIVE); // 日志

		promotionDao.activatePromotionById(id, userId);
		publishPromotion(new Date()); // 发布到缓存
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionCommand> publishPromotion(Date currentTime) {
		List<PromotionCommand> pc = promotionDao.findPromotionEnableList(currentTime);
		int defaultpriority = 1;

		for (PromotionCommand p : pc) {
			List<ConditionComplexCommand> conditionComplex = promotionConditionComplexDao.findPromotionConditionComplexByPromotionId(p.getPromotionId());
			List<SettingComplexCommand> settingComplex = promotionSettingComplexDao.findPromotionSettingComplexByPromotionId(p.getPromotionId());
			p.setConditionComplexList(conditionComplex);
			p.setSettingComplexList(settingComplex);
			p.setDefaultPriority(defaultpriority);
			defaultpriority = defaultpriority + 1;

			if (p.getConditionExpression() != null && !p.getConditionExpression().isEmpty()) {
				List<AtomicCondition> conditionList = sdkPromotionConditionManager.parseConditionByExpression(p.getConditionExpression());
				for(AtomicCondition cond:conditionList)
				{
					cond.setPromotionId(p.getPromotionId());
				}
				p.setAtomicConditionList(conditionList);
			}
			if (p.getConditionComplexList() != null && p.getConditionComplexList().size()>0) {
				List<AtomicCondition> complexConditionList = sdkPromotionConditionManager.convertComplexConditionToAtomic(p.getConditionComplexList());
				for(AtomicCondition cond:complexConditionList)
				{
					cond.setPromotionId(p.getPromotionId());
				}
				p.setAtomicConditionList(complexConditionList);
			}
			// 满足条件，获取当前优惠活动设置。要放到启用时加载
			if (p.getSettingExpression() != null && !p.getSettingExpression().isEmpty()) {
				List<AtomicSetting> settingList = sdkPromotionSettingManager.parseSettingByExpression(p.getSettingExpression());
				for(AtomicSetting set:settingList)
				{
					set.setPromotionId(p.getPromotionId());
					set.setSettingId(p.getSettingId());
				}
				p.setAtomicSettingList(settingList);
			}
			if (p.getSettingComplexList() != null && p.getSettingComplexList().size()>0) {
				List<AtomicSetting> complexSettingList =  sdkPromotionSettingManager.convertComplexSettingToAtomic(p.getSettingComplexList());
				for(AtomicSetting set:complexSettingList)
				{
					set.setPromotionId(p.getPromotionId());
				}
				p.setAtomicComplexSettingList(complexSettingList);
			}
		}
		EngineManager.getInstance().setPromotionCommandList(pc);
		EngineManager.getInstance().setCrowdScopeList(sdkMemberTagRuleManager.findAllAvailableMemberTagRuleCommandList());
		EngineManager.getInstance().setItemScopeList(sdkItemTagRuleManager.findAllAvailableCustomProductComboList());
		
		List<PromotionMarkdownPrice> mrkdownPriceList = sdkPromotionMarkdownPriceManager.getPromotionMarkdownPriceList();
		
		EngineManager.getInstance().setPromotionMarkdownPriceList(mrkdownPriceList);
		EngineManager.getInstance().build();
		log.info("活动已经启用！");
		return pc;
	}

	/**
	 * 根据人群与商品范围查找冲突的限购
	 * 
	 * @param id
	 * @param list
	 * @return
	 */
	@Transactional(readOnly=true)
	private List<PromotionQueryCommand> findConflictingPromotionList(Long id, List<PromotionQueryCommand> list, Long shopId) {
		List<PromotionQueryCommand> rs = new ArrayList<PromotionQueryCommand>();
		PromotionQueryCommand major = promotionDao.findPromotionQueryCommandById(id);
		if (null == major)
			throw new BusinessException(Constants.PROMOTION_INEXISTENCE);
		String majorMemberExp = major.getMemberComboExpression();
		String majorProductExp = major.getProductComboExpression();
		for (PromotionQueryCommand cmd : list) {
			String memberExp = cmd.getMemberComboExpression();
			String productExp = cmd.getProductComboExpression();
			if (sdkFilterManager.isMemberConfilct(majorMemberExp, memberExp) && sdkFilterManager.isProductConfilct(majorProductExp, productExp, shopId)) {
				rs.add(cmd);
			}
		}
		return rs;
	}

	/**
	 * 记录日志
	 * 
	 * @param promotionId
	 * @param userId
	 * @param type
	 */
	private void record(PromotionHead head, Long userId, Integer type) {
		User user = userDao.getByPrimaryKey(userId); // 当前用户
		if (null == user)
			throw new BusinessException(Constants.USER_USER_NOTFOUND);

		PromotionOperationLog l = new PromotionOperationLog();
		l.setNote("[" + head.getPromotionName() + "]" + " [" + user.getRealName() + "]" + " [" + PromotionOperationLog.OPERATION_TYPE_TO_NAME_MAP.get(type) + "]");
		l.setOperateTime(new Date());
		l.setOperationType(type);
		l.setOperatorId(userId);
		l.setPromotionId(head.getId());
		promotionOperationLogDao.save(l);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionQueryCommand> checkConflictNotActvieGift(Long id, Long shopId) {
		List<PromotionQueryCommand> list = promotionDao.findActivePromotionListByTimeScope(id, shopId);
		list = findConflictingPromotionList(id, list, shopId);
		// 检查冲突活动之间是否有相同的礼品
		if (list != null && list.size() > 0) {
			checkNotActiveIsSameGift(list, id);
		}
		return list;
	}
	@Transactional(readOnly=true)
	private void checkNotActiveIsSameGift(List<PromotionQueryCommand> list, Long id) {
		List<Long> proids = new ArrayList<Long>();
		proids.add(id);
		for (int i = 0; i < list.size(); i++) {
			proids.add(list.get(i).getId());
		}
		// 查询当前自己的活动信息
		list.add(promotionDao.findPromotionQueryCommandById(id));
		List<PromotionSettingQueryCommand> promotionSettings = getPromotionSetting(proids);
		PromotionSettingQueryCommand selfsetting = null;
		for (int i = 0; i < promotionSettings.size(); i++) {
			selfsetting = promotionSettings.get(i);
			if (selfsetting.getPromotionId().equals(id)) {
				break;
			}
		}
		String selfexp = selfsetting.getSettingExpression();
		// 启用的活动不是礼品类型
		String[] selfsettingArr = selfexp.split(",");
		if (!selfexp.startsWith("scpgift") || !selfsettingArr[1].startsWith("pid")) {
			return;
		}

		Map<String, List<String>> settingsPromotions = new HashMap<String, List<String>>();
		for (int i = 0; i < promotionSettings.size(); i++) {
			PromotionSettingQueryCommand settingComplexCommand = promotionSettings.get(i);
			Long proid = settingComplexCommand.getPromotionId();
			String settingexp = settingComplexCommand.getSettingExpression();
			String[] settingArr = settingexp.split(",");
			List<String> items = new ArrayList<String>();
			if (settingexp.startsWith("scpgift") && settingArr[1].startsWith("pid")) {
				String itemid = settingArr[1].split(":")[1];
				items.add(itemid);
				if (settingsPromotions.containsKey(String.valueOf(proid))) {
					settingsPromotions.get(String.valueOf(proid)).add(itemid);
				} else {
					settingsPromotions.put(String.valueOf(proid), items);
				}
			}
		}

		Set<String> proidList = settingsPromotions.keySet();
		String[] proidArr = proidList.toArray(new String[] {});
		List<String> selfitemids = settingsPromotions.get(String.valueOf(id));
		for (int i = 0; i < proidArr.length; i++) {
			String proid = proidArr[i];
			if (Long.parseLong(proid) == id) {
				continue;
			}
			List<String> itemids = settingsPromotions.get(proid);
			for (int j = 0; j < selfitemids.size(); j++) {
				String itemid = selfitemids.get(j);
				if (itemids.contains(itemid)) {
					BusinessException businessException = new BusinessException(555001);
					businessException.setMessage("当前活动[" + getPromotionName(String.valueOf(id), list) + "]与[" + getPromotionName(proid, list) + "]之间存在礼品冲突:" + getItemName(itemid));
					throw businessException;
				}
			}
		}

	}

	private String getPromotionName(String id, List<PromotionQueryCommand> list) {
		String name = "";
		for (int i = 0; i < list.size(); i++) {
			if (String.valueOf(list.get(i).getId()).equals(id)) {
				name = list.get(i).getName();
				break;
			}
		}
		return name;
	}

	private String getItemName(String itemId) {
		ItemInfo item = sdkItemManager.findItemInfoByItemId(Long.parseLong(itemId));
		return item == null ? "" : item.getTitle();
	}

	/**
	 * 
	 * @author 何波
	 * @Description: 封装常规和复杂类型设置
	 * @param proids
	 * @return List<PromotionSettingQueryCommand>
	 * @throws
	 */
	@Transactional(readOnly=true)
	private List<PromotionSettingQueryCommand> getPromotionSetting(List<Long> proids) {
		List<PromotionSettingQueryCommand> settingQueryCommands = new ArrayList<PromotionSettingQueryCommand>();
		for (int i = 0; i < proids.size(); i++) {
			long id = proids.get(i);
			PromotionSettingNormal promotionSettingNormal = promotionSettingDao.findPromotionSettingNormalByPromotionId(id);
			if ((promotionSettingNormal == null) || promotionSettingNormal.getSettingExpression() == null) {
				List<SettingComplexCommand> selfsettingComplexCommands = promotionSettingComplexDao.findPromotionSettingComplexByPromotionId(id);
				for (int j = 0; j < selfsettingComplexCommands.size(); j++) {
					PromotionSettingQueryCommand settingQueryCommand = new PromotionSettingQueryCommand();
					SettingComplexCommand settingComplexCommand = selfsettingComplexCommands.get(j);
					settingQueryCommand.setId(settingComplexCommand.getId());
					settingQueryCommand.setActiveMark(settingComplexCommand.getActiveMark());
					settingQueryCommand.setPromotionId(settingComplexCommand.getPromotionId());
					settingQueryCommand.setSettingExpression(settingComplexCommand.getSettingExpression());
					settingQueryCommand.setSettingName(settingComplexCommand.getSettingName());
					settingQueryCommands.add(settingQueryCommand);
				}
			} else {
				List<SettingComplexCommand> selfsettingComplexCommands = promotionSettingComplexDao.findPromotionSettingComplexByPromotionId(id);
				if (selfsettingComplexCommands != null && selfsettingComplexCommands.size() > 0) {
					for (int j = 0; j < selfsettingComplexCommands.size(); j++) {
						PromotionSettingQueryCommand settingQueryCommand = new PromotionSettingQueryCommand();
						SettingComplexCommand settingComplexCommand = selfsettingComplexCommands.get(j);
						settingQueryCommand.setId(settingComplexCommand.getId());
						settingQueryCommand.setActiveMark(settingComplexCommand.getActiveMark());
						settingQueryCommand.setPromotionId(settingComplexCommand.getPromotionId());
						settingQueryCommand.setSettingExpression(settingComplexCommand.getSettingExpression());
						settingQueryCommand.setSettingName(settingComplexCommand.getSettingName());
						settingQueryCommands.add(settingQueryCommand);
					}
				}
				PromotionSettingQueryCommand settingQueryCommand = new PromotionSettingQueryCommand();
				settingQueryCommand.setId(promotionSettingNormal.getId());
				settingQueryCommand.setActiveMark(promotionSettingNormal.getActiveMark());
				settingQueryCommand.setPromotionId(promotionSettingNormal.getPromotionId());
				settingQueryCommand.setSettingExpression(promotionSettingNormal.getSettingExpression());
				settingQueryCommand.setSettingName(promotionSettingNormal.getSettingName());
				settingQueryCommands.add(settingQueryCommand);
			}
		}

		return settingQueryCommands;
	}

	/**
	 * 检查2个促销之间是否存在冲突
	 */
	@Override
	@Transactional(readOnly=true)
	public Boolean checkConflictingBetweenPromotions(PromotionCommand one, PromotionCommand toCheckOne) {
		if (null == one || null == toCheckOne)
			return false;
		if (!one.getShopId().equals(toCheckOne.getShopId()))
			return false;
		Long shopId = one.getShopId();
		String majorMemberExp = one.getMemComboExpression();
		String majorProductExp = one.getScopeExpression();

		String memberExp = toCheckOne.getMemComboExpression();
		String productExp = toCheckOne.getScopeExpression();
		if (sdkFilterManager.isMemberConfilct(majorMemberExp, memberExp) && sdkFilterManager.isProductConfilct(majorProductExp, productExp, shopId)) {
			return true;
		}

		return false;
	}
	/**
	 * 获取当前时间生效的活动
	 */
	@Override
	@Transactional(readOnly=true)
	public List<PromotionCommand> getEffectPromotion(Date currentTime) {
		return promotionDao.findPromotionEnableList(currentTime);
	}
	
	
	/**
	 * 获取当前时间生效的活动
	 */
	@Override
	@Transactional(readOnly=true)
	public List<PromotionCommand> findAllPromotionEnableList() {
		
		return promotionDao.findAllPromotionEnableList();
	}
	
	/**
	 * 获取商品Id中的价格调整
	 */
	@Override
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceListByItemId(Long itemId) {
		List<PromotionMarkdownPrice> mrkdownPriceList = EngineManager.getInstance().getPromotionMarkdownPriceList();
		if (null==mrkdownPriceList || mrkdownPriceList.size()==0)
			return null;
		List<PromotionMarkdownPrice> mrkdownPriceFilteredList = new ArrayList<PromotionMarkdownPrice>();
		for(PromotionMarkdownPrice cmd:mrkdownPriceList)
		{
			if (cmd.getItemId().equals(itemId))
			{
				mrkdownPriceFilteredList.add(cmd);
			}
		}
		return mrkdownPriceFilteredList;
	}
	/**
	 * 获取商品Id列表中的价格调整
	 */
	@Override
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceListByItemIdList(List<Long> itemIdList) {
		List<PromotionMarkdownPrice> mrkdownPriceFilteredList = new ArrayList<PromotionMarkdownPrice>();
		List<PromotionMarkdownPrice> oneItems = null;
		if (null==itemIdList || itemIdList.size()==0)
			return null;
		for(Long item:itemIdList)
		{
			oneItems = getPromotionMarkdownPriceListByItemId(item);
			if (null!=oneItems)
				mrkdownPriceFilteredList.addAll(oneItems);
		}
		return mrkdownPriceFilteredList;
	}
}
