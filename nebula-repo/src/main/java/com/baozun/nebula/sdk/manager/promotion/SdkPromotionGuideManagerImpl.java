package com.baozun.nebula.sdk.manager.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.param.SettingType;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.promotion.PromotionDao;
import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderLineManager;

/*
 * 促销导购。PDP页面，列表页，Landing Page页
 */
@Transactional
@Service("sdkPromotionGuideManager")
public class SdkPromotionGuideManagerImpl implements SdkPromotionGuideManager{

    @SuppressWarnings("unused")
    private final static Logger           log = LoggerFactory.getLogger(SdkPromotionGuideManagerImpl.class);

    @Autowired
    private ItemDao                       itemDao;

    @Autowired
    private SdkPromotionRuleFilterManager sdkPromotionRuleFilterManager;

    @Autowired
    private SdkOrderLineManager           sdkOrderLineManager;

    @Autowired
    private SdkOrderPromotionDao          sdkOrderPromotionDao;

    @Autowired
    private PromotionDao                  promotionDao;

    @Autowired
    private ItemCategoryDao               itemCategoryDao;

    @Autowired
    private SdkEngineManager              sdkEngineManager;

    @Autowired
    private SdkPromotionManager           sdkPromotionManager;

    /**
     * 根据ItemId列表，获取有促销活动的活动列表。典型应用场景列表页，分页的一页中那些有什么活动。
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<PromotionCommand>> getPromotionsForItemList(List<Long> itemIds,UserDetails userDetails){
        Map<Long, List<PromotionCommand>> itemsPromotions = new HashMap<Long, List<PromotionCommand>>();
        List<PromotionCommand> oneItemPromotion = new ArrayList<PromotionCommand>();
        for (Long itemId : itemIds){
            oneItemPromotion = getPromotionsForItem(itemId, userDetails);
            if (oneItemPromotion != null && oneItemPromotion.size() > 0){
                itemsPromotions.put(itemId, oneItemPromotion);
            }
        }
        return itemsPromotions;
    }

    /**
     * 根据ItemId列表，获取有促销活动的活动列表和单件优惠金额。典型应用场景列表页。
     * Map<itemId, Map<"disCountAmount"/"promotionList", disCountAmount/促销活动列表 >>
     * 备注：只是估算并不准确，没走引擎，优先级折上折等逻辑，只能用作导购的时候提示
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, Map<String, Object>> getPromotionsAndDisCountForItemList(List<Long> itemIds,UserDetails userDetails){
        Map<Long, Map<String, Object>> itemPromotionAndDisCountMap = new HashMap<Long, Map<String, Object>>();
        List<PromotionCommand> oneItemPromotionList = new ArrayList<PromotionCommand>();
        Map<String, Object> disCountMap = null;

        List<ItemCommand> itemCommandList = itemDao.findItemCommandListByIds(itemIds);
        Map<Long, ItemCommand> itemCommandMap = new HashMap<Long, ItemCommand>();
        for (ItemCommand itemCommand : itemCommandList){
            itemCommandMap.put(itemCommand.getId(), itemCommand);
        }

        ItemCommand itemCommand = null;
        for (Long itemId : itemIds){
            itemCommand = itemCommandMap.get(itemId);
            if (null == itemCommand){
                continue;
            }
            BigDecimal disCountAmount = new BigDecimal(0);
            disCountMap = new HashMap<String, Object>();
            // 获取商品的促销活动
            oneItemPromotionList = getPromotionsForItem(itemId, userDetails);
            disCountMap.put("promotionList", oneItemPromotionList);

            //商品没有参加促销活动
            if (null == oneItemPromotionList || oneItemPromotionList.size() <= 0){
                disCountMap.put("disCountAmount", disCountAmount);
                itemPromotionAndDisCountMap.put(itemId, disCountMap);
                continue;
            }
            List<AtomicSetting> atmSettingList = new ArrayList<AtomicSetting>();
            for (PromotionCommand pc : oneItemPromotionList){
                atmSettingList = new ArrayList<AtomicSetting>();
                if (pc.getAtomicSettingList() != null && pc.getAtomicSettingList().size() > 0)
                    atmSettingList.addAll(pc.getAtomicSettingList());
                if (pc.getAtomicComplexSettingList() != null && pc.getAtomicComplexSettingList().size() > 0)
                    atmSettingList.addAll(pc.getAtomicComplexSettingList());
                if (atmSettingList == null || atmSettingList.size() == 0)
                    continue;
                for (AtomicSetting setting : atmSettingList){
                    //范围单品优惠 || 范围单件优惠
                    if (SettingType.EXP_SCPPRDDISC.equalsIgnoreCase(setting.getSettingTag())
                                    || SettingType.EXP_SCPPCSDISC.equalsIgnoreCase(setting.getSettingTag())){
                        disCountAmount = disCountAmount.add(setting.getSettingValue());
                    }
                    //范围单品折扣 || 范围单件折扣
                    else if (SettingType.EXP_SCPPRDRATE.equalsIgnoreCase(setting.getSettingTag())
                                    || SettingType.EXP_SCPPCSRATE.equalsIgnoreCase(setting.getSettingTag())){
                        disCountAmount = disCountAmount
                                        .add((setting.getSettingValue().divide(new BigDecimal(100))).multiply(itemCommand.getSalePrice()));
                    }
                    //范围单品优惠价
                    else if (SettingType.EXP_SCPMKDNPRICE.equalsIgnoreCase(setting.getSettingTag())
                                    || SettingType.EXP_SCPMKDNPRICE.equalsIgnoreCase(setting.getSettingTag())){
                        BigDecimal mrk = getMarkdownPriceFromListByItemId(itemId);
                        BigDecimal mrkDownDiscount = itemCommand.getSalePrice().subtract(mrk);
                        disCountAmount = disCountAmount.add(mrkDownDiscount);
                        if (itemCommand.getSalePrice().compareTo(disCountAmount) < 0){
                            disCountAmount = itemCommand.getSalePrice();
                        }
                    }
                }
            }
            if (disCountAmount.compareTo(itemCommand.getSalePrice()) >= 0)
                disCountAmount = itemCommand.getSalePrice();
            disCountMap.put("disCountAmount", disCountAmount);
            itemPromotionAndDisCountMap.put(itemId, disCountMap);
        }
        return itemPromotionAndDisCountMap;
    }

    /**
     * 根据订单行（t_so_orderpromotion）上的Promotion List，从DB中取促销活动信息（可能过期不在内存中），
     * 计算优惠设置，获取单品折扣。典型应用场景订单详情页。
     * 备注：只是估算并不准确，没走引擎，优先级折上折等逻辑，只能用作导购的时候提示
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, Map<String, Object>> getPromotionsAndDisCountForItemListByOrderId(Long orderId){
        List<OrderPromotionCommand> orderPromotions = sdkOrderPromotionDao.findOrderProInfoByOrderId(orderId, 1);
        if (null == orderPromotions || orderPromotions.size() == 0)
            return null;
        List<OrderLineCommand> orderLines = sdkOrderLineManager.findOrderLinesByOrderId(orderId);
        if (null == orderLines || orderLines.size() == 0)
            return null;
        List<Long> itemIds = new ArrayList<Long>();
        for (OrderLineCommand line : orderLines){
            if (line.getType() == ItemInfo.TYPE_GIFT)
                continue;
            if (line.getItemId() != null && !itemIds.contains(line.getItemId())){
                itemIds.add(line.getItemId());
            }
        }
        if (null == itemIds || itemIds.size() == 0)
            return null;

        Map<String, Object> disCountMap = null;

        List<ItemCommand> itemCommandList = itemDao.findItemCommandListByIds(itemIds);
        Map<Long, ItemCommand> itemCommandMap = new HashMap<Long, ItemCommand>();
        for (ItemCommand itemCommand : itemCommandList){
            itemCommandMap.put(itemCommand.getId(), itemCommand);
        }

        Map<Long, Map<String, Object>> itemPromotionAndDisCountMap = new HashMap<Long, Map<String, Object>>();
        ItemCommand itemCommand = null;
        List<PromotionCommand> oneItemPromotionList = new ArrayList<PromotionCommand>();
        for (Long itemId : itemIds){
            itemCommand = itemCommandMap.get(itemId);
            if (null == itemCommand){
                continue;
            }
            BigDecimal disCountAmount = new BigDecimal(0);
            disCountMap = new HashMap<String, Object>();
            // 获取商品的促销活动
            oneItemPromotionList = getPromotionListFromOrderInfo(orderLines, orderPromotions, itemId);
            disCountMap.put("promotionList", oneItemPromotionList);

            //商品没有参加促销活动
            if (null == oneItemPromotionList || oneItemPromotionList.size() <= 0){
                disCountMap.put("disCountAmount", disCountAmount);
                itemPromotionAndDisCountMap.put(itemId, disCountMap);
                continue;
            }

            List<AtomicSetting> atmSettingList = new ArrayList<AtomicSetting>();
            for (PromotionCommand pc : oneItemPromotionList){
                atmSettingList = new ArrayList<AtomicSetting>();
                if (pc.getAtomicSettingList() != null && pc.getAtomicSettingList().size() > 0)
                    atmSettingList.addAll(pc.getAtomicSettingList());
                if (pc.getAtomicComplexSettingList() != null && pc.getAtomicComplexSettingList().size() > 0)
                    atmSettingList.addAll(pc.getAtomicComplexSettingList());
                if (atmSettingList == null || atmSettingList.size() == 0)
                    continue;
                for (AtomicSetting setting : atmSettingList){
                    //范围单品优惠 || 范围单件优惠
                    if (SettingType.EXP_SCPPRDDISC.equalsIgnoreCase(setting.getSettingTag())
                                    || SettingType.EXP_SCPPCSDISC.equalsIgnoreCase(setting.getSettingTag())){
                        disCountAmount = disCountAmount.add(setting.getSettingValue());
                    }
                    //范围单品折扣 || 范围单件折扣
                    else if (SettingType.EXP_SCPPRDRATE.equalsIgnoreCase(setting.getSettingTag())
                                    || SettingType.EXP_SCPPCSRATE.equalsIgnoreCase(setting.getSettingTag())){
                        disCountAmount = disCountAmount
                                        .add((setting.getSettingValue().divide(new BigDecimal(100))).multiply(itemCommand.getSalePrice()));
                    }
                    //范围单品优惠价
                    else if (SettingType.EXP_SCPMKDNPRICE.equalsIgnoreCase(setting.getSettingTag())
                                    || SettingType.EXP_SCPMKDNPRICE.equalsIgnoreCase(setting.getSettingTag())){
                        BigDecimal mrk = getMarkdownPriceFromListByItemId(itemId);
                        BigDecimal mrkDownDiscount = itemCommand.getSalePrice().subtract(mrk);
                        disCountAmount = disCountAmount.add(mrkDownDiscount);
                        if (itemCommand.getSalePrice().compareTo(disCountAmount) < 0){
                            disCountAmount = itemCommand.getSalePrice();
                        }
                    }
                }
            }
            if (disCountAmount.compareTo(itemCommand.getSalePrice()) >= 0)
                disCountAmount = itemCommand.getSalePrice();
            disCountMap.put("disCountAmount", disCountAmount);
            itemPromotionAndDisCountMap.put(itemId, disCountMap);
        }
        return itemPromotionAndDisCountMap;
    }

    @Transactional(readOnly = true)
    private BigDecimal getMarkdownPriceFromListByItemId(Long itemId){
        List<PromotionMarkdownPrice> mrkdownPriceList = sdkPromotionManager.getPromotionMarkdownPriceListByItemId(itemId);
        for (PromotionMarkdownPrice cmd : mrkdownPriceList){
            if (cmd.getItemId().equals(itemId)){
                return cmd.getMarkDownPrice();
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 根据ItemId，获取有促销活动的活动列表。典型应用场景PDP页。
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionCommand> getPromotionsForItem(Long itemId,UserDetails userDetails){
        Set<String> memboSet = null;

        if (null == userDetails){
            // 游客
            memboSet = getMemboIds();
        }else{
            // 会员
            memboSet = userDetails.getMemComboList();
        }

        List<Long> shopIds = new ArrayList<Long>();
        Set<String> itemComboIds = new HashSet<String>();
        List<Long> itemIds = new ArrayList<Long>();
        itemIds.add(itemId);
        List<Item> items = itemDao.findItemListByIds(itemIds);
        if (null != items && items.size() > 0){
            for (Item item : items){
                shopIds.add(item.getShopId());
                itemId = item.getId();
                List<ItemCategory> categoryLists = itemCategoryDao.findItemCategoryListByItemId(itemId);

                itemComboIds.addAll(sdkEngineManager.getItemScopeListByItemAndCategory(String.valueOf(itemId), categoryLists));
            }
        }
        // 获取人群和商品促销的交集
        List<PromotionCommand> promotionList = sdkPromotionRuleFilterManager
                        .getIntersectActivityRuleData(shopIds, memboSet, itemComboIds, new Date());
        return promotionList;
    }

    /**
     * 游客的memboIds
     * 
     * @return
     */
    private Set<String> getMemboIds(){
        return sdkEngineManager.getCrowdScopeListByMemberAndGroup(null, null);
    }

    @Override
    public List<PromotionCommand> getAllGiftPromotion(Long shopId){
        // 获取人群和商品促销的交集
        List<PromotionCommand> allPromotionList = EngineManager.getInstance().getPromotionCommandList();

        if (null == allPromotionList || allPromotionList.size() <= 0){
            return null;
        }

        List<PromotionCommand> promotionList = new ArrayList<PromotionCommand>();
        promotion: for (PromotionCommand promotionCommand : allPromotionList){
            if (shopId.equals(promotionCommand.getShopId())){
                List<AtomicSetting> settingList = promotionCommand.getAtomicSettingList();
                if (null != settingList && settingList.size() > 0){
                    for (AtomicSetting atomicSetting : settingList){
                        if (SettingType.EXP_SCPGIFT.equalsIgnoreCase(atomicSetting.getSettingTag())){
                            promotionList.add(promotionCommand);
                            continue promotion;
                        }
                    }
                }

                List<AtomicSetting> complexSettringList = promotionCommand.getAtomicComplexSettingList();
                if (null != complexSettringList && complexSettringList.size() > 0){
                    for (AtomicSetting atomicSetting : complexSettringList){
                        if (SettingType.EXP_SCPGIFT.equalsIgnoreCase(atomicSetting.getSettingTag())){
                            promotionList.add(promotionCommand);
                            continue promotion;
                        }
                    }
                }
            }
        }
        return promotionList;
    }

    @Override
    public PromotionCommand getPromotionById(Long prmId){
        // TODO Auto-generated method stub
        // 获取人群和商品促销的交集
        List<PromotionCommand> allPromotionList = EngineManager.getInstance().getPromotionCommandList();

        if (null == allPromotionList || allPromotionList.size() <= 0 || prmId == null){
            return null;
        }

        for (PromotionCommand promotionCommand : allPromotionList){
            if (promotionCommand.getPromotionId().equals(prmId)){
                return promotionCommand;
            }
        }
        return null;
    }

    /**
     * 订单快照中获取促销信息
     * 
     * @param orderLines
     * @param orderPromotions
     * @param itemId
     * @return
     */
    @Transactional(readOnly = true)
    private List<PromotionCommand> getPromotionListFromOrderInfo(
                    List<OrderLineCommand> orderLines,
                    List<OrderPromotionCommand> orderPromotions,
                    Long itemId){
        List<PromotionCommand> oneItemPromotionList = new ArrayList<PromotionCommand>();
        PromotionCommand promotionCmd = null;

        if (null == orderPromotions || orderPromotions.size() == 0)
            return null;
        if (null == orderLines || orderLines.size() == 0)
            return null;
        for (OrderLineCommand line : orderLines){
            if (line.getItemId() != null && line.equals(itemId)){
                for (OrderPromotionCommand opc : orderPromotions){
                    if (line.getItemId() != null && line.equals(itemId) && opc.getOrderLineId() != null
                                    && opc.getOrderLineId().equals(line.getId())){
                        promotionCmd = promotionDao.findPromotionById(Long.valueOf(opc.getPromotionNo()));
                        oneItemPromotionList.add(promotionCmd);
                    }
                }
            }
        }
        return oneItemPromotionList;
    }
}