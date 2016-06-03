/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.param.ConditionMasterType;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.calculateEngine.param.GiftType;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.model.promotion.LinePromotionSize;
import com.baozun.nebula.model.promotion.LinePromotionSizeComparator;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionConditionSKU;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManagerImpl;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionRuleFilterManager;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviourFactory;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;

@Transactional
@Service("sdkShoppingCartGroupManager")
public class SdkShoppingCartGroupManagerImpl implements SdkShoppingCartGroupManager{

    private static final Logger                        log = LoggerFactory.getLogger(SdkPromotionCalculationManagerImpl.class);

    @Autowired
    private SdkShoppingCartLineDao                     sdkShoppingCartLineDao;

    @Autowired
    private SdkSkuManager                              sdkSkuManager;

    @Autowired
    private SdkPromotionRuleFilterManager              sdkPromotionRuleFilterManager;

    @Autowired
    private SdkShoppingCartLineCommandBehaviourFactory sdkShoppingCartLineCommandBehaviourFactory;

    /**
     * 根据促销引擎计算的结果，把原购物车行数据重新分组。 添加Caption行，按lineGroup分组排列好相应的行。 jsp负责显示。 1，调用引擎，获得SkuSettingList活动的行优惠结果
     * 2，根据行优惠，获得这些活动的条件行； 3，根据优惠行，和条件行分组，Compress压缩 添加caption行； 返回前端显示所需要shoppingcartlines a,套餐 b，赠品 c，normal的活动
     * 4，没活动的行；
     * 
     * @param oneShopLines
     *            当前店铺的所有行
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> groupShoppingCartLinesToDisplayByLinePromotionResult(
                    ShoppingCartCommand oneShoppingCart,
                    List<PromotionBrief> promotionBriefList){
        if (oneShoppingCart == null || oneShoppingCart.getShoppingCartLineCommands() == null
                        || oneShoppingCart.getShoppingCartLineCommands().size() == 0)
            return null;
        Long memberId = 0L;
        if (oneShoppingCart.getUserDetails() != null)
            memberId = oneShoppingCart.getUserDetails().getMemberId();
        // 无优惠，直接返回
        // List<PromotionBrief> promotionBriefList = sdkShoppingCartManager.calcuPromoBriefs(oneShoppingCart);
        if (promotionBriefList == null || promotionBriefList.size() == 0)
            return oneShoppingCart.getShoppingCartLineCommands();
        // 无行优惠，也直接返回
        List<PromotionCommand> allLinePromotionList = getAllLinePromotionsBySKUSettingList(promotionBriefList);
        if (allLinePromotionList == null || allLinePromotionList.size() == 0)
            return oneShoppingCart.getShoppingCartLineCommands();

        List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionConditionSKU> onePromotionConditionSKUList = new ArrayList<PromotionConditionSKU>();
        // 套餐行
        List<ShoppingCartLineCommand> suitLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> allSuitLines = new ArrayList<ShoppingCartLineCommand>();
        // 行赠品行
        List<ShoppingCartLineCommand> lineGiftLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> allLineGiftLines = new ArrayList<ShoppingCartLineCommand>();
        // 整单赠品行
        List<ShoppingCartLineCommand> orderGiftLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> allOrderGiftLines = new ArrayList<ShoppingCartLineCommand>();
        // 常规优惠行
        List<ShoppingCartLineCommand> normalAMTLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> allNormalAMTLines = new ArrayList<ShoppingCartLineCommand>();

        // 分组过的行、包括无优惠的行，所有的行
        List<ShoppingCartLineCommand> allGroupedLines = new ArrayList<ShoppingCartLineCommand>();

        for (PromotionCommand onePromotion : allLinePromotionList){
            onePromotionConditionSKUList = new ArrayList<PromotionConditionSKU>();
            onePromotionSettingSKUList = new ArrayList<PromotionSKUDiscAMTBySetting>();

            onePromotionSettingSKUList = getOnePromotionSKUSettingListByPromotionBriefs(promotionBriefList, onePromotion.getPromotionId());
            onePromotionConditionSKUList = getPromotionConditionSKUListFromShoppingCartLines(
                            oneShoppingCart.getShoppingCartLineCommands(),
                            onePromotion.getPromotionId());
            if (this.checkPromotionHasSuitKits(onePromotion)){
                suitLines = new ArrayList<ShoppingCartLineCommand>();
                // 套餐行
                suitLines = this.getLinesOfSuitKitsPromotion(
                                oneShoppingCart.getShoppingCartLineCommands(),
                                onePromotionSettingSKUList,
                                onePromotionConditionSKUList);
                if (suitLines != null && suitLines.size() > 0)
                    allSuitLines.addAll(suitLines);
            }else if (this.checkPromotionHasLineGift(onePromotionSettingSKUList, onePromotion)){
                lineGiftLines = new ArrayList<ShoppingCartLineCommand>();
                // 买赠行整合
                lineGiftLines = this.getLinesOfLineGiftPromotion(
                                oneShoppingCart.getShoppingCartLineCommands(),
                                onePromotionSettingSKUList,
                                onePromotionConditionSKUList);
                if (lineGiftLines != null && lineGiftLines.size() > 0)
                    allLineGiftLines.addAll(lineGiftLines);
            }else if (this.checkPromotionOrderGift(onePromotionSettingSKUList, onePromotion)){
                orderGiftLines = new ArrayList<ShoppingCartLineCommand>();
                // 整单赠品
                orderGiftLines = this
                                .getLinesOfOrderGiftPromotion(oneShoppingCart.getShoppingCartLineCommands(), onePromotionSettingSKUList);
                if (orderGiftLines != null && orderGiftLines.size() > 0)
                    allOrderGiftLines.addAll(orderGiftLines);
            }else{
                // 行上直接减
                // 可能多个叠加.可能是无条件行优惠onePromotionConditionSKUList
                normalAMTLines = this.getLinesOfNormalPromotion(
                                oneShoppingCart.getShoppingCartLineCommands(),
                                onePromotionSettingSKUList,
                                onePromotionConditionSKUList);
                allNormalAMTLines = appendShoppingCartLines(allNormalAMTLines, normalAMTLines);
            }
        }
        // 买赠行和直接减行，需要去重
        allNormalAMTLines = appendShoppingCartLines(allNormalAMTLines, allLineGiftLines);

        // 可以调整显示循序
        // 套餐行
        if (allSuitLines != null && allSuitLines.size() > 0){
            // 需要按照活动再分组
            allSuitLines = appendCaptionLinesToGroupedLines(allSuitLines);
            allGroupedLines.addAll(allSuitLines);
        }
        // 行减
        if (allNormalAMTLines != null && allNormalAMTLines.size() > 0){
            // 原行赠品，和行减，与套餐和整单赠品，是有区别的
            // 取行上活动最多的行，按这些活动，分在一组。循环执行以上动作，直至所有行分组完毕
            // 最终Caption Line后面，跟的lineGroup值，可能不一样
            allNormalAMTLines = appendCaptionLinesByMaxPromotions(allNormalAMTLines);
            allGroupedLines.addAll(allNormalAMTLines);
        }
        // 整单赠品
        if (allOrderGiftLines != null && allOrderGiftLines.size() > 0){
            // 需要按照活动再分组
            allOrderGiftLines = appendCaptionLinesToGroupedLines(allOrderGiftLines);
            allGroupedLines.addAll(allOrderGiftLines);
        }

        // 检查original lines，没有包含在allGroupedLines中的，就是无优惠的，常规行，添加到allGroupedLines中
        allGroupedLines = appendShoppingCartLinesOfNoPromotion(oneShoppingCart.getShoppingCartLineCommands(), allGroupedLines);
        // 设置选中赠品行
        allGroupedLines = setGiftLinesSettlement(allGroupedLines, memberId);
        // 设置行库存状态
        if ((allGroupedLines != null && allGroupedLines.size() > 0) && (allLinePromotionList != null && allLinePromotionList.size() > 0))
            allGroupedLines = setShoppingCartLineOutOfStockBySKUId(allGroupedLines, allLinePromotionList);

        logLines(allGroupedLines);
        return allGroupedLines;
    }

    /*
     * 按SKU ID设置赠品行库存，考虑整个购物车中赠品的消费数量
     */
    @Transactional(readOnly = true)
    private List<ShoppingCartLineCommand> setShoppingCartLineOutOfStockBySKUId(
                    List<ShoppingCartLineCommand> groupedLines,
                    List<PromotionCommand> linePromotions){
        Map<Long, Integer> hisSKUQuantityList = new HashMap<Long, Integer>();
        Integer curComsumeSku = 0;
        Integer prvComsumeSku = 0;
        // It be ordered by promotion priority
        //先处理条件行
        for (ShoppingCartLineCommand line : groupedLines){
            if (line.isCaptionLine())
                continue;
            //if (line.getPromotionList() == null)
            //  continue;
            curComsumeSku = 0;
            prvComsumeSku = 0;

            //for (PromotionCommand pcmd : linePromotions) {
            if (line.isGift() == false){
                if (hisSKUQuantityList.containsKey(line.getSkuId())){
                    prvComsumeSku = hisSKUQuantityList.get(line.getSkuId());
                    curComsumeSku = hisSKUQuantityList.get(line.getSkuId()) + line.getQuantity();
                }else{
                    curComsumeSku = line.getQuantity();
                }
                hisSKUQuantityList.put(line.getSkuId(), curComsumeSku);
                SkuCommand skuIvt = sdkSkuManager.findSkuQSVirtualInventoryById(line.getSkuId(), line.getExtentionCode());
                if (skuIvt == null || skuIvt.getAvailableQty() == null){
                    line.setStock(0);
                }else if (skuIvt.getAvailableQty() < curComsumeSku){
                    line.setStock(skuIvt.getAvailableQty() - prvComsumeSku);
                }

                ShoppingCartLineCommandBehaviour sdkShoppingCartLineCommandBehaviour = sdkShoppingCartLineCommandBehaviourFactory
                                .getShoppingCartLineCommandBehaviour(line);

                sdkShoppingCartLineCommandBehaviour.packShoppingCartLine(line);
            }
            //}
        }
        //再处理赠品行
        for (ShoppingCartLineCommand line : groupedLines){
            if (line.isCaptionLine())
                continue;
            if (line.getPromotionList() == null)
                continue;
            curComsumeSku = 0;
            prvComsumeSku = 0;

            for (PromotionCommand pcmd : linePromotions){
                if (line.isGift() == true && line.getPromotionList().contains(pcmd)){
                    if (hisSKUQuantityList.containsKey(line.getSkuId())){
                        prvComsumeSku = hisSKUQuantityList.get(line.getSkuId());
                        curComsumeSku = hisSKUQuantityList.get(line.getSkuId()) + line.getQuantity();
                    }else{
                        curComsumeSku = line.getQuantity();
                    }
                    hisSKUQuantityList.put(line.getSkuId(), curComsumeSku);
                    SkuCommand skuIvt = sdkSkuManager.findSkuQSVirtualInventoryById(line.getSkuId(), line.getExtentionCode());
                    if (skuIvt == null || skuIvt.getAvailableQty() == null){
                        line.setStock(0);
                    }else if (skuIvt.getAvailableQty() < curComsumeSku){
                        line.setStock(skuIvt.getAvailableQty() - prvComsumeSku);
                    }

                    ShoppingCartLineCommandBehaviour sdkShoppingCartLineCommandBehaviour = sdkShoppingCartLineCommandBehaviourFactory
                                    .getShoppingCartLineCommandBehaviour(line);

                    sdkShoppingCartLineCommandBehaviour.packShoppingCartLine(line);

                }
            }
        }
        return groupedLines;
    }

    private void logLines(List<ShoppingCartLineCommand> allGroupedLines){
        log.info("分组Log开始!");
        String lineGift = "";
        if (allGroupedLines == null || allGroupedLines.size() == 0)
            return;
        for (ShoppingCartLineCommand line : allGroupedLines){
            if (line.isCaptionLine()){
                if (line.isSuitLine())
                    log.info("店铺编号：" + line.getShopId() + ",分组编号：" + line.getLineGroup() + ",活动标题：" + line.getLineCaption() + "。需要套数调节器！");
                else
                    log.info("店铺编号：" + line.getShopId() + ",分组编号：" + line.getLineGroup() + ",活动标题：" + line.getLineCaption());
            }else{
                if (line.getLineGroup() == null || line.getLineGroup() == 0L)
                    log.info(
                                    "店铺编号：" + line.getShopId() + ",活动编号：" + line.getPromotionIds() + ",常   规   行：-------------ID："
                                                    + line.getId() + ",ItemID：" + line.getItemId() + ",SKU ID：" + line.getSkuId()
                                                    + ",ExtCode：" + line.getExtentionCode() + ",名        称：" + line.getItemName()
                                                    + ",价        格：" + line.getSalePrice() + ",优        惠：" + line.getDiscount() + ",件数："
                                                    + line.getQuantity() + ",库存：" + line.getStock());
                else{
                    lineGift = "店铺编号：" + line.getShopId() + ",分组编号：" + line.getLineGroup() + ",活动编号：" + line.getPromotionIds() + ",----赠品："
                                    + (line.isGift() == true ? "是，" : "否，") + "套装：" + (line.isSuitLine() == true ? "是，" : "否，") + "ID："
                                    + line.getId() + ",ItemID：" + line.getItemId() + ",SKU ID：" + line.getSkuId() + ",ExtCode："
                                    + line.getExtentionCode() + ",名        称：" + line.getItemName() + ",价        格：" + line.getSalePrice()
                                    + ",优        惠：" + line.getDiscount() + ",件数：" + line.getQuantity() + ",库存：" + line.getStock() + ",选中："
                                    + line.getSettlementState();
                    if (line.isGift() == true){
                        lineGift = lineGift + (line.getGiftType() == GiftType.OrderGift ? ",整赠" : ",行赠");
                        if (line.getGiftChoiceType() == GiftChoiceType.NeedChoice)
                            lineGift = lineGift + ",最多选" + line.getGiftCountLimited();
                        else
                            lineGift = lineGift + ",直推";
                        if (line.getGiftType() == GiftType.LineGift)
                            lineGift = lineGift + ",行赠品";
                        else
                            lineGift = lineGift + ",整单赠品";
                    }
                    log.info(lineGift);
                }
            }
        }
    }

    /**
     * 获取各行参与的活动数，从最多活动的行的活动，取各个活动包含的行为一组。 直至行用完为止。
     * 
     * @param linesNoCaptions
     * @return
     */
    public List<ShoppingCartLineCommand> appendCaptionLinesByMaxPromotions(List<ShoppingCartLineCommand> linesNoCaptions){
        List<LinePromotionSize> lineSizeList = new ArrayList<LinePromotionSize>();
        List<Long> promotionList = new ArrayList<Long>();

        boolean exist = false;
        for (ShoppingCartLineCommand line : linesNoCaptions){
            exist = false;
            if (line.isGift() == true)
                continue;
            for (LinePromotionSize size : lineSizeList){
                if (size.getLineId().equals(line.getId())){
                    exist = true;
                    // append promotion
                    promotionList = new ArrayList<Long>();
                    if (size.getPromotionList() != null)
                        promotionList = size.getPromotionList();

                    if (line.getPromotionList() != null){
                        for (PromotionCommand pm : line.getPromotionList()){
                            // BeanUtils.copyProperties(pm,tmpPromotion);

                            if (!promotionList.contains(pm.getPromotionId()))
                                promotionList.add(pm.getPromotionId());
                        }
                    }
                    if (promotionList != null)
                        size.setPromotionList(promotionList);
                    break;
                }
            }
            if (exist == false){
                // append line
                if (line.getPromotionList() != null){
                    List<Long> promotionListNew = new ArrayList<Long>();
                    LinePromotionSize sizeNew = new LinePromotionSize();
                    sizeNew.setLineId(line.getId());
                    for (PromotionCommand pm : line.getPromotionList()){
                        promotionListNew.add(pm.getPromotionId());
                    }
                    sizeNew.setPromotionList(promotionListNew);
                    lineSizeList.add(sizeNew);
                }
            }
        }
        LinePromotionSizeComparator sizeComparator = new LinePromotionSizeComparator();
        Collections.sort(lineSizeList, sizeComparator);
        List<ShoppingCartLineCommand> oneGroupLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> groupedLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> leftLines = new ArrayList<ShoppingCartLineCommand>();
        leftLines = linesNoCaptions;
        for (LinePromotionSize size : lineSizeList){
            oneGroupLines = getShoppingCartLinesByPromotions(leftLines, size.getPromotionList());
            // Add caption line
            if (oneGroupLines != null && oneGroupLines.size() > 0){
                ShoppingCartLineCommand captionLine = this.getPromotionCaptionFromActualLines(oneGroupLines);
                Long lineGroup = captionLine.getLineGroup();
                groupedLines.add(captionLine);
                groupedLines.addAll(oneGroupLines);
                for (ShoppingCartLineCommand line : oneGroupLines){
                    if (line.isCaptionLine())
                        continue;
                    else
                        line.setLineGroup(lineGroup);
                }

                leftLines = getLeftLinesAfterRemoveOneGroup(leftLines, oneGroupLines);
            }
        }
        // 取line id，参与活动数map，
        return groupedLines;
    }

    private List<ShoppingCartLineCommand> getLeftLinesAfterRemoveOneGroup(
                    List<ShoppingCartLineCommand> containsLines,
                    List<ShoppingCartLineCommand> removeLines){
        Boolean exists = true;
        List<ShoppingCartLineCommand> leftLines = new ArrayList<ShoppingCartLineCommand>();

        for (ShoppingCartLineCommand containLine : containsLines){
            exists = false;
            for (ShoppingCartLineCommand removeLine : removeLines){
                if (containLine.getLineGroup().equals(removeLine.getLineGroup()) && containLine.getSkuId().equals(removeLine.getSkuId())){
                    exists = true;
                    break;
                }
            }
            if (!exists){
                leftLines.add(containLine);
            }
        }
        return leftLines;
    }

    /**
     * 根据行上最多的活动集合，获取分组
     * 
     * @param linesNoCaptions
     * @param groupedlines
     * @param promotionList
     * @return
     */
    @Transactional(readOnly = true)
    private List<PromotionCommand> appendPromotionsByPromotionIdList(List<PromotionCommand> tmpList,List<Long> promotionIdList){
        List<PromotionCommand> fetchPromotion = new ArrayList<PromotionCommand>();
        PromotionCommand tmpPromotion = new PromotionCommand();
        // fetchPromotion = tmpList;
        Boolean exist = false;
        for (Long promotionId : promotionIdList){
            exist = false;
            for (PromotionCommand promotion : tmpList){
                if (promotion.getPromotionId().equals(promotionId)){
                    fetchPromotion.add(promotion);
                    exist = true;
                    break;
                }
            }
            if (exist == false){
                tmpPromotion = sdkPromotionRuleFilterManager.getPromotionByPromotionId(promotionId);
                fetchPromotion.add(tmpPromotion);
            }
        }
        return fetchPromotion;
    }

    public List<ShoppingCartLineCommand> getShoppingCartLinesByPromotions(
                    List<ShoppingCartLineCommand> linesNoCaptions,
                    List<Long> promotionList){
        Boolean exists = false;
        List<ShoppingCartLineCommand> fechedLines = new ArrayList<ShoppingCartLineCommand>();
        List<Long> linePromotions = new ArrayList<Long>();
        List<PromotionCommand> tmpList = new ArrayList<PromotionCommand>();

        for (ShoppingCartLineCommand line : linesNoCaptions){
            exists = false;
            if (line.isCaptionLine())
                continue;
            if (line.getPromotionList() == null)
                continue;
            linePromotions = new ArrayList<Long>();
            for (PromotionCommand pm : line.getPromotionList()){
                if (!linePromotions.contains(pm.getPromotionId()))
                    linePromotions.add(pm.getPromotionId());
            }
            linePromotions.retainAll(promotionList);
            if (linePromotions != null && linePromotions.size() > 0){
                for (ShoppingCartLineCommand existline : fechedLines){
                    if (existline.getId() != null && line.getId() != null && existline.getId().equals(line.getId())){
                        tmpList = existline.getPromotionList();
                        tmpList = appendPromotionsByPromotionIdList(tmpList, promotionList);

                        existline.setPromotionList(tmpList);

                        exists = true;
                        break;
                    }else if (line.isGift()){
                        exists = false;
                        break;
                    }
                }
                if (!exists){
                    fechedLines.add(line);
                    exists = false;
                }
            }
        }
        return fechedLines;
    }

    /**
     * 对按照条件和Setting List分组过的行，添加Caption Line
     * 
     * @param linesNoCaptions
     * @return
     */
    public List<ShoppingCartLineCommand> appendCaptionLinesToGroupedLines(List<ShoppingCartLineCommand> linesNoCaptions){
        // 取List中的LineGroup,用lineGroup循环过滤lines
        // 一个lineGroup就是一个分组
        // ShoppingCartLineCommand captionLine = this.getPromotionCaptionFromActualLines(linesNoCaptions);
        // linesNoCaptions.add(captionLine);
        // return linesNoCaptions;
        /*
         * lineGroup sku 44 caption 整单 44 12 44 13 45 caption 买减 45 25 43 caption 买赠 43 37
         */
        if (linesNoCaptions == null || linesNoCaptions.size() == 0){
            return linesNoCaptions;
        }
        List<ShoppingCartLineCommand> shoppingCartLineCommands = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> groupShoppingCartLines = new ArrayList<ShoppingCartLineCommand>();
        List<Long> groupList = new ArrayList<Long>();
        // 获取group列表
        for (ShoppingCartLineCommand line : linesNoCaptions){
            if (!groupList.contains(line.getLineGroup())){
                Long lineGroup = line.getLineGroup();
                groupList.add(lineGroup);
                groupShoppingCartLines = getShoppingCartLineByGroup(linesNoCaptions, lineGroup);
                ShoppingCartLineCommand captionLine = this.getPromotionCaptionFromActualLines(groupShoppingCartLines);
                shoppingCartLineCommands.add(captionLine);
                shoppingCartLineCommands.addAll(groupShoppingCartLines);
            }
        }

        return shoppingCartLineCommands;
    }

    /**
     * 
     * @Description: 从购物车中获取相同地 lineGroup行
     * @param shoppingCartLineCommands
     * @param lineGroup
     * @return List<ShoppingCartLineCommand>
     * @throws
     */
    private List<ShoppingCartLineCommand> getShoppingCartLineByGroup(List<ShoppingCartLineCommand> shoppingCartLineCommands,Long lineGroup){
        List<ShoppingCartLineCommand> sameLineGroups = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : shoppingCartLineCommands){
            if (line.getLineGroup().equals(lineGroup)){
                sameLineGroups.add(line);
            }
        }
        return sameLineGroups;
    }

    /**
     * 获取购物车行表中的赠品行，设置赠品行选中状态
     * 
     * @param lines
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> setGiftLinesSettlement(List<ShoppingCartLineCommand> lines,Long memberId){
        List<ShoppingCartLineCommand> giftLinesSeleted = sdkShoppingCartLineDao
                        .findShopCartGiftLineByMemberId(memberId, Constants.CHECKED_CHOOSE_STATE);

        for (ShoppingCartLineCommand line : lines){
            if (line.isGift() && GiftChoiceType.NeedChoice.equals(line.getGiftChoiceType())){
                if (line.getLineGroup() == null || null == giftLinesSeleted)
                    continue;
                for (ShoppingCartLineCommand gift : giftLinesSeleted){
                    if (gift.getLineGroup() == null)
                        continue;
                    //SkuCommand invtSku = skuDao.findInventoryById(gift.getSkuId());
                    SkuCommand invtSku = sdkSkuManager.findSkuQSVirtualInventoryById(gift.getSkuId(), gift.getExtentionCode());
                    if (null == invtSku || null == invtSku.getAvailableQty())
                        continue;
                    // if (invtSku.getAvailableQty() < line.getQuantity())
                    // continue;
                    if (line.getPromotionId().equals(gift.getPromotionId()) && line.getItemId().equals(gift.getItemId())
                                    && line.getSkuId().equals(gift.getSkuId())){
                        line.setId(gift.getId());
                        line.setSkuId(gift.getSkuId());
                        line.setExtentionCode(gift.getExtentionCode());
                        if (gift.getSettlementState() != null && Constants.CHECKED_CHOOSE_STATE.equals(gift.getSettlementState()))
                            line.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
                        else
                            line.setSettlementState(Constants.NOCHECKED_CHOOSE_STATE);
                        //log.debug("-------->>> {} have inventory {}", gift.getExtentionCode(), gift.getStock());
                        //sdkEngineManager.packShoppingCartLine(line);
                        continue;
                    }
                }

            }else if (line.isGift() && GiftChoiceType.NoNeedChoice.equals(line.getGiftChoiceType())){
                if (null != line.getGiftChoiceType() && GiftChoiceType.NoNeedChoice.equals(line.getGiftChoiceType())){
                    line.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
                }
            }
        }
        return lines;
    }

    /**
     * 根据分组行（行上已经有促销），获取Caption Line
     * 
     * @param linesHasPromotion
     * @return
     */
    public ShoppingCartLineCommand getPromotionCaptionFromActualLines(List<ShoppingCartLineCommand> linesHasPromotion){
        ShoppingCartLineCommand captionLine = new ShoppingCartLineCommand();
        List<PromotionCommand> linePromotions = new ArrayList<PromotionCommand>();
        List<PromotionCommand> groupLinesPromotions = new ArrayList<PromotionCommand>();
        Long lineGroup = 0L;
        String caption = "";
        Long shopId = 0L;
        for (ShoppingCartLineCommand line : linesHasPromotion){
            lineGroup = line.getLineGroup();
            linePromotions = line.getPromotionList();
            if (linePromotions != null){
                for (PromotionCommand prm : linePromotions){
                    if (!groupLinesPromotions.contains(prm)){
                        groupLinesPromotions.add(prm);
                    }
                }
            }
        }
        if (groupLinesPromotions != null){
            for (PromotionCommand prom : groupLinesPromotions){
                shopId = prom.getShopId();
                if (caption.isEmpty())
                    caption = prom.getPromotionName();
                else
                    caption = caption + "," + prom.getPromotionName();
            }
        }
        captionLine.setPromotionList(groupLinesPromotions);
        captionLine.setLineGroup(lineGroup);
        captionLine.setCaptionLine(true);
        captionLine.setLineCaption(caption);
        captionLine.setShopId(shopId);
        return captionLine;
    }

    /**
     * 对比原购物车行，和分组过的购物车行。把没包含的添加进分组行中去。 前端直接显示分组过后的购物车行。
     * 
     * @param originShoppingCartLines
     * @param groupedShoppingCartLines
     * @return
     */
    public List<ShoppingCartLineCommand> appendShoppingCartLinesOfNoPromotion(
                    List<ShoppingCartLineCommand> originShoppingCartLines,
                    List<ShoppingCartLineCommand> groupedShoppingCartLines){
        boolean exsit = false;
        for (ShoppingCartLineCommand originShoppingCartLine : originShoppingCartLines){
            Long oskuId = originShoppingCartLine.getSkuId();
            Long oId = originShoppingCartLine.getId();
            // 加载进来的赠品可以不管，但必须把有line id的行添加进来
            if (oId == null)
                continue;
            for (ShoppingCartLineCommand groupedShoppingCartLine : groupedShoppingCartLines){
                // 过滤活动行
                if (groupedShoppingCartLine.isCaptionLine()){
                    continue;
                }
                Long skuId = groupedShoppingCartLine.getSkuId();
                Long lineGroup = groupedShoppingCartLine.getLineGroup();
                Long lineId = groupedShoppingCartLine.getId();
                // 已经添加到分组列表中的，无行Id的，必定是赠品，也忽略
                if (lineId == null)
                    continue;
                if (lineId.equals(oId) && skuId.equals(oskuId) && (lineGroup != null)){
                    exsit = true;
                    // 结束当前循环
                    break;
                }
            }
            if (!exsit){
                groupedShoppingCartLines.add(originShoppingCartLine);
            }
            // 重新计算
            exsit = false;
        }
        return groupedShoppingCartLines;
    }

    /**
     * 根据所有有优惠的行，获取对应的促销活动列表
     * 
     * @param allSettingSKUList
     * @return
     */
    @Transactional(readOnly = true)
    public List<PromotionCommand> getAllLinePromotionsBySKUSettingList(List<PromotionBrief> promotionBriefList){
        List<PromotionCommand> linePromotionList = new ArrayList<PromotionCommand>();
        PromotionCommand onePromotion = new PromotionCommand();
        if (null != promotionBriefList && promotionBriefList.size() > 0){
            for (PromotionBrief promotionBrief : promotionBriefList){
                List<PromotionSettingDetail> details = promotionBrief.getDetails();
                if (null == details || details.size() == 0)
                    continue;
                for (PromotionSettingDetail detail : details){
                    List<PromotionSKUDiscAMTBySetting> affectSkuList = detail.getAffectSKUDiscountAMTList();
                    if (null != affectSkuList && affectSkuList.size() > 0){
                        onePromotion = new PromotionCommand();
                        onePromotion = sdkPromotionRuleFilterManager.getPromotionByPromotionId(detail.getPromotionId());
                        if (linePromotionList.contains(onePromotion) == false)
                            linePromotionList.add(onePromotion);
                    }
                }
            }
        }
        return linePromotionList;
    }

    /**
     * 根据优惠引擎的摘要，获取所有有优惠的行
     * 
     * @param promotionBriefList
     * @return
     */
    public List<PromotionSKUDiscAMTBySetting> getOnePromotionSKUSettingListByPromotionBriefs(
                    List<PromotionBrief> promotionBriefList,
                    Long onePromotionId){
        List<PromotionSKUDiscAMTBySetting> affectSkuList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> oneSettingSkuList;
        if (null != promotionBriefList && promotionBriefList.size() > 0){
            for (PromotionBrief promotionBrief : promotionBriefList){
                if (onePromotionId.equals(promotionBrief.getPromotionId())){
                    List<PromotionSettingDetail> details = promotionBrief.getDetails();
                    if (null == details || details.size() == 0)
                        continue;
                    for (PromotionSettingDetail detail : details){
                        oneSettingSkuList = detail.getAffectSKUDiscountAMTList();
                        if (null != oneSettingSkuList && oneSettingSkuList.size() > 0){
                            affectSkuList.addAll(oneSettingSkuList);
                        }
                    }
                    return affectSkuList;
                }
            }
        }
        return null;
    }

    /**
     * 根据活动，回溯满足活动的条件行
     * 
     * @param shoppingLines
     * @param promotionId
     * @return
     */
    @Transactional(readOnly = true)
    public List<PromotionConditionSKU> getPromotionConditionSKUListFromShoppingCartLines(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long promotionId){
        if (shoppingLines == null || shoppingLines.size() == 0)
            return null;
        List<PromotionConditionSKU> suitSKUList = new ArrayList<PromotionConditionSKU>();

        List<AtomicCondition> condList = new ArrayList<AtomicCondition>();
        List<PromotionConditionSKU> oneSKUList = new ArrayList<PromotionConditionSKU>();
        PromotionCommand onePromotion = null;

        condList = new ArrayList<AtomicCondition>();
        oneSKUList = new ArrayList<PromotionConditionSKU>();
        onePromotion = sdkPromotionRuleFilterManager.getPromotionByPromotionId(promotionId);
        if (onePromotion == null)
            return null;

        if (onePromotion.getAtomicConditionList() != null){
            condList.addAll(onePromotion.getAtomicConditionList());
        }
        if (onePromotion.getAtomicComplexConditionList() != null){
            if (onePromotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_STEP)){
                if (onePromotion.getAtomicComplexConditionList().size() > 0)
                    condList.add(onePromotion.getAtomicComplexConditionList().get(0));
            }else{
                condList.addAll(onePromotion.getAtomicComplexConditionList());
            }
        }
        if (condList == null || condList.size() == 0)
            return null;
        for (AtomicCondition condition : condList){
            // 整单其实不需要分组，直接显示在最后面即可
            if (condition.getConditionValue() != null && condition.getConditionTag() != null
                            && condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_CUSTOM)){
                oneSKUList = getCustomConditionSKUList(shoppingLines, Long.valueOf(condition.getConditionValue().toString()), condition);
            }else{
                if ((condition.getScopeTag() == null || condition.getScopeValue() <= 0L))
                    continue;
                if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                    oneSKUList = getProductConditionSKUList(shoppingLines, condition.getScopeValue(), condition);
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                    oneSKUList = getCategoryConditionSKUList(shoppingLines, condition.getScopeValue(), condition);
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                    oneSKUList = getCustomScopeConditionSKUList(shoppingLines, condition.getScopeValue(), condition);
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                    oneSKUList = getComboConditionSKUList(shoppingLines, condition.getScopeValue(), condition);
                }
            }
            if (oneSKUList == null || oneSKUList.size() == 0)
                continue;
            for (PromotionConditionSKU sku : oneSKUList){
                sku.setPromotionId(promotionId);
            }
            suitSKUList.addAll(oneSKUList);
        }

        return suitSKUList;
    }

    /**
     * 检查一个活动是否有套餐行优惠
     * 
     * @param onePromotionSettingSKUList
     * @param promotion
     * @return
     */
    @Override
    public Boolean checkPromotionHasSuitKits(PromotionCommand promotion){
        if (promotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_CHOICE))
            return true;
        return false;
    }

    /**
     * 获取有套餐活动的所有的行，包括主选商品
     * 
     * @param oneShopCartLineList
     *            ，剩下的商品行
     * @param kitPromotionConditionSKUList
     * @param kitPromotionSettingSKUList
     * @return
     */
    @Override
    public List<ShoppingCartLineCommand> getLinesOfSuitKitsPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> kitPromotionSettingSKUList,
                    List<PromotionConditionSKU> kitPromotionConditionSKUList){
        // TODO 套餐代码是个mockup，还需要验证
        if (kitPromotionSettingSKUList == null || kitPromotionSettingSKUList.size() == 0)
            return null;
        List<Long> lineGroupList = new ArrayList<Long>();
        List<ShoppingCartLineCommand> suitLines = new ArrayList<ShoppingCartLineCommand>();
        Long suitPromotionId = 0L;
        // lines =
        // getLinesOfPromotionBySettingAndConditionList(oneShopCartLineList,kitPromotionSettingSKUList,kitPromotionConditionSKUList);
        // 1,从当前Setting list中，获取Promotion Id，按当前Promotion Id获取行
        suitPromotionId = kitPromotionSettingSKUList.get(0).getPromotionId();
        if (suitPromotionId.equals(0L))
            return null;
        // 2,按line group分组，即是suitId。line group为空的为非套餐行
        for (ShoppingCartLineCommand line : oneShopCartLineList){
            if ((line.getPromotionId() != null && !line.getPromotionId().equals(suitPromotionId))
                            && (line.getLineGroup() != null && !line.getLineGroup().equals(0L))){
                lineGroupList.add(line.getLineGroup());
            }
        }
        for (Long grpId : lineGroupList){
            for (ShoppingCartLineCommand line : oneShopCartLineList){
                if (line.getPromotionId().equals(suitPromotionId) && line.getLineGroup().equals(grpId)){
                    suitLines.add(line);
                }
            }
        }
        return suitLines;
    }

    /**
     * 根据活动的条件和优惠行，获取行分组
     * 
     * @param oneShopCartLineList
     * @param promotionSettingSKUList
     * @param promotionConditionSKUList
     * @return
     */
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getLinesOfPromotionBySettingAndConditionList(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> promotionSettingSKUList,
                    List<PromotionConditionSKU> promotionConditionSKUList){
        if (promotionSettingSKUList == null)
            return oneShopCartLineList;

        List<ShoppingCartLineCommand> allPromotionLines = new ArrayList<ShoppingCartLineCommand>();
        ShoppingCartLineCommand oneTempLine = new ShoppingCartLineCommand();
        List<PromotionCommand> promotionList = new ArrayList<PromotionCommand>();
        PromotionCommand onePromotion = null;
        Long lineGroup = 0L;

        if (promotionConditionSKUList != null && promotionConditionSKUList.size() > 0){
            for (PromotionConditionSKU conditionSKU : promotionConditionSKUList){
                if (onePromotion == null)
                    onePromotion = this.sdkPromotionRuleFilterManager.getPromotionByPromotionId(conditionSKU.getPromotionId());

                if (promotionList == null)
                    promotionList = new ArrayList<PromotionCommand>();

                if (!promotionList.contains(onePromotion))
                    promotionList.add(onePromotion);

                // 一个活动有多个优惠设置
                lineGroup = onePromotion.getSettingId();

                oneTempLine = this.getShoppingCartLineBySKUId(oneShopCartLineList, lineGroup, conditionSKU.getSkuId());
                if (null == oneTempLine)
                    continue;
                if (!this.checkLineExistsByLineGroupAndSKU(allPromotionLines, lineGroup, oneTempLine.getSkuId())){
                    oneTempLine.setLineGroup(lineGroup);
                    allPromotionLines.add(oneTempLine);
                }else{
                    System.out.print(oneTempLine.getSkuId() + "：行已经存在！");
                }
            }
        }
        if (promotionSettingSKUList != null && promotionSettingSKUList.size() > 0){
            for (PromotionSKUDiscAMTBySetting settingSKU : promotionSettingSKUList){
                if (onePromotion == null)
                    onePromotion = this.sdkPromotionRuleFilterManager.getPromotionByPromotionId(settingSKU.getPromotionId());

                if (promotionList == null)
                    promotionList = new ArrayList<PromotionCommand>();

                if (!promotionList.contains(onePromotion))
                    promotionList.add(onePromotion);

                // 用优惠设置SettingId填充lineGroup
                if (lineGroup == null || lineGroup == 0L)
                    lineGroup = onePromotion.getSettingId();
                if (settingSKU.getGiftMark() == false){
                    oneTempLine = this.getShoppingCartLineBySKUId(oneShopCartLineList, lineGroup, settingSKU.getSkuId());
                    if (null == oneTempLine)
                        continue;
                    if (!this.checkLineExistsByLineGroupAndSKU(allPromotionLines, lineGroup, oneTempLine.getSkuId())){
                        // 设置优惠金额
                        oneTempLine.setLineGroup(lineGroup);
                        allPromotionLines.add(oneTempLine);
                    }
                }else{
                    oneTempLine = this.getShoppingCartGiftLineByItemId(oneShopCartLineList, lineGroup, settingSKU.getItemId());
                    if (null == oneTempLine)
                        continue;
                    oneTempLine.setLineGroup(lineGroup);
                    allPromotionLines.add(oneTempLine);
                }
            }
        }
        if (promotionList != null && allPromotionLines.size() > 0){
            for (ShoppingCartLineCommand lineSKU : allPromotionLines){
                lineSKU.setPromotionList(promotionList);
            }
        }
        return allPromotionLines;
    }

    /**
     * 检查购物车行是否已经存在
     * 
     * @param shopCartLineList
     * @param lineGroup
     * @param skuId
     * @return
     */
    public Boolean checkLineExistsByLineGroupAndSKU(List<ShoppingCartLineCommand> shopCartLineList,Long lineGroup,Long skuId){
        if (shopCartLineList == null || shopCartLineList.size() == 0)
            return false;
        for (ShoppingCartLineCommand line : shopCartLineList){
            if (line.getLineGroup() != null && line.getLineGroup().equals(lineGroup) && line.getSkuId().equals(skuId) && line.getId() > 0L)
                return true;
        }
        return false;
    }

    /**
     * 根据SKU
     * 
     * @param shopCartLineList
     * @param skuId
     * @return
     */
    public ShoppingCartLineCommand getShoppingCartLineBySKUId(List<ShoppingCartLineCommand> shopCartLineList,Long lineGroup,Long skuId){
        ShoppingCartLineCommand lineTmp = new ShoppingCartLineCommand();
        for (ShoppingCartLineCommand line : shopCartLineList){
            if (line.isCaptionLine())
                continue;
            if (line.getLineGroup() != null && line.getLineGroup().equals(lineGroup) && line.getSkuId().equals(skuId)){
                BeanUtils.copyProperties(line, lineTmp);
                return lineTmp;
            }
            if (line.getSkuId().equals(skuId)){
                BeanUtils.copyProperties(line, lineTmp);
                return lineTmp;
            }
        }
        return null;
    }

    public ShoppingCartLineCommand getShoppingCartGiftLineByItemId(
                    List<ShoppingCartLineCommand> shopCartLineList,
                    Long lineGroup,
                    Long itemId){
        ShoppingCartLineCommand lineTmp = new ShoppingCartLineCommand();
        for (ShoppingCartLineCommand line : shopCartLineList){
            if (line.isCaptionLine())
                continue;
            if (line.getLineGroup() != null && line.getLineGroup().equals(lineGroup) && line.getItemId().equals(itemId)
                            && line.isGift() == true){
                BeanUtils.copyProperties(line, lineTmp);
                return lineTmp;
            }
        }
        for (ShoppingCartLineCommand line : shopCartLineList){
            if (line.isCaptionLine())
                continue;
            if (line.getItemId().equals(itemId) && line.isGift() == true){
                BeanUtils.copyProperties(line, lineTmp);
                return lineTmp;
            }
        }
        return null;
    }

    /**
     * 检查一个活动是否有赠品行优惠
     * 
     * @param onePromotionSettingSKUList
     * @param promotion
     * @return
     */
    @Override
    public Boolean checkPromotionHasLineGift(List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList,PromotionCommand onePromotion){
        Boolean orderBase = checkOrderBaseByPromotion(onePromotion);
        Boolean hasGift = false;
        if (onePromotionSettingSKUList == null)
            return false;
        for (PromotionSKUDiscAMTBySetting skuSetting : onePromotionSettingSKUList){
            if (skuSetting.getGiftMark() == true){
                hasGift = true;
                break;
            }
        }
        return hasGift && !orderBase;
    }

    /**
     * 根据促销条件，判断活动是基于整单的，还是基于行的（范围）
     * 
     * @param onePromotion
     * @return
     */
    public Boolean checkOrderBaseByPromotion(PromotionCommand onePromotion){
        List<AtomicCondition> condList = onePromotion.getAtomicConditionList();
        Boolean condCheck = checkOrderBaseByPromotionCondition(condList);
        if (condCheck == true)
            return condCheck;

        List<AtomicCondition> complexCondList = onePromotion.getAtomicComplexConditionList();
        Boolean condComplexCheck = checkOrderBaseByPromotionCondition(complexCondList);
        return condComplexCheck;
    }

    /**
     * 根据活动条件判断是否是整单
     * 
     * @param condList
     * @return
     */
    public Boolean checkOrderBaseByPromotionCondition(List<AtomicCondition> condList){
        if (condList == null || condList.size() == 0)
            return false;
        Boolean result = false;
        Boolean resultOne = false;
        if (condList != null && condList.size() == 1){
            //有且只有一个自定义条件时，作为整单看
            if (condList.get(0).getConditionValue() != null && condList.get(0).getConditionTag() != null
                            && condList.get(0).getConditionTag().equalsIgnoreCase(ConditionType.EXP_CUSTOM)){
                return true;
            }
        }
        for (AtomicCondition condition : condList){
            //多个条件时，自定义条件不参与
            if (condition.getConditionTag() != null && condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_CUSTOM)){
                resultOne = true;
            }else
            // 解析原子表达式
            // 无限制
            if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_NOLIMIT)){
                resultOne = true;
            }
            // 整单金额
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDAMT)){
                resultOne = true;
            }
            // 整单件数
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDPCS)){
                resultOne = true;
            }
            // 整单Coupon,范围Coupon。无倍增。
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDCOUPON)){
                resultOne = true;
            }
            // 整单优惠幅度。
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDMARGINRATE)){
                resultOne = true;
            }else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPCOUPON)){
                resultOne = false;
            }
            // 范围内整单金额
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPORDAMT)){
                resultOne = false;
            }
            // 范围内整单件数
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPORDPCS)){
                resultOne = false;
            }
            // 范围内单品金额
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDAMT)){
                resultOne = false;
            }
            // 范围内单品件数
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDPCS)){
                resultOne = false;
            }
            // 范围内优惠幅度
            else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPMARGINRATE)){
                resultOne = false;
            }

            if (condition.getOperateTag().equalsIgnoreCase("&"))
                result = result && resultOne;
            else
                result = result || resultOne;
        }
        return result;
    }

    /**
     * 获取赠品行，包括参与行
     * 
     * @param oneShopCartLineList
     *            ，剩下的商品行
     * @param lineGiftPromotionSettingSKUList
     * @param lineGiftPromotionConditionSKUList
     * @return
     */
    @Override
    public List<ShoppingCartLineCommand> getLinesOfLineGiftPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> lineGiftPromotionSettingSKUList,
                    List<PromotionConditionSKU> lineGiftPromotionConditionSKUList){
        List<ShoppingCartLineCommand> lines = getLinesOfPromotionBySettingAndConditionList(
                        oneShopCartLineList,
                        lineGiftPromotionSettingSKUList,
                        lineGiftPromotionConditionSKUList);
        for (ShoppingCartLineCommand line : lines){
            // line.setLineGroup(line.getLineGroup());
            line.setGiftType(GiftType.LineGift);
        }

        return lines;
    }

    /**
     * 获取整单赠品行
     * 
     * @param oneShopCartLineList
     *            ，剩下的商品行
     * @param onePromotionSettingSKUList
     * @return
     */
    @Override
    public Boolean checkPromotionOrderGift(List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList,PromotionCommand onePromotion){
        Boolean orderBase = checkOrderBaseByPromotion(onePromotion);
        Boolean hasGift = false;
        if (orderBase == false)
            return false;
        for (PromotionSKUDiscAMTBySetting skuSetting : onePromotionSettingSKUList){
            if (skuSetting.getGiftMark() == true){
                hasGift = true;
                break;
            }
        }
        return hasGift && orderBase;
    }

    /**
     * 获取整单赠品行
     * 
     * @param oneShopCartLineList
     *            ，剩下的商品行
     * @param onePromotionSettingSKUList
     * @return
     */
    @Override
    public List<ShoppingCartLineCommand> getLinesOfOrderGiftPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList){
        List<ShoppingCartLineCommand> lines = new ArrayList<ShoppingCartLineCommand>();
        lines = getLinesOfPromotionBySettingAndConditionList(oneShopCartLineList, onePromotionSettingSKUList, null);
        for (ShoppingCartLineCommand line : lines){
            // line.setLineGroup(line.getLineGroup());
            line.setGiftType(GiftType.OrderGift);
        }
        return lines;
    }

    /**
     * 常规行上的减，可能会出现多次 把discount累加，行不累加
     * 
     * @param linesBefore
     * @param currentPromotionLines
     * @return
     */
    public List<ShoppingCartLineCommand> appendShoppingCartLines(
                    List<ShoppingCartLineCommand> linesBefore,
                    List<ShoppingCartLineCommand> currentPromotionLines){
        if (linesBefore == null || linesBefore.size() == 0){
            return currentPromotionLines;
        }
        if (currentPromotionLines == null || currentPromotionLines.size() == 0){
            return linesBefore;
        }
        List<ShoppingCartLineCommand> originalLines = new ArrayList<ShoppingCartLineCommand>();
        ShoppingCartLineCommand tmpLine = new ShoppingCartLineCommand();
        for (ShoppingCartLineCommand line : linesBefore){
            tmpLine = new ShoppingCartLineCommand();
            BeanUtils.copyProperties(line, tmpLine);
            originalLines.add(tmpLine);
        }

        Boolean existInBefore = false;
        BigDecimal lineDiscount = BigDecimal.ZERO;
        List<PromotionCommand> linePromotions = new ArrayList<PromotionCommand>();

        // 需要去压缩 ,已经存在的，discount加上去，line上的Promotion List添加当前的
        for (ShoppingCartLineCommand curLine : currentPromotionLines){
            if (curLine.isCaptionLine()){
                continue;
            }
            existInBefore = false;
            for (ShoppingCartLineCommand befLine : linesBefore){
                if (befLine.isCaptionLine()){
                    continue;
                }
                if (befLine.getId() != null && curLine.getId() != null && befLine.getId().equals(curLine.getId())){
                    existInBefore = true;
                    break;
                }else if (befLine.getLineGroup().equals(curLine.getLineGroup()) && befLine.getSkuId().equals(curLine.getSkuId())){
                    existInBefore = true;
                    break;
                }
            }
            if (existInBefore){
                // Update to original lines
                for (ShoppingCartLineCommand orgLine : originalLines){
                    if ((orgLine.getId() != null && curLine.getId() != null && orgLine.getId().equals(curLine.getId()))
                                    || (orgLine.getLineGroup().equals(curLine.getLineGroup()) && orgLine.getSkuId().equals(
                                                    curLine.getSkuId()))){
                        linePromotions = new ArrayList<PromotionCommand>();
                        linePromotions = orgLine.getPromotionList();
                        if (curLine.getPromotionList() != null){
                            for (PromotionCommand prm : curLine.getPromotionList()){
                                if (!linePromotions.contains(prm)){
                                    linePromotions.add(prm);
                                }
                            }
                        }
                        if (orgLine.isGift() == false)
                            orgLine.setPromotionList(linePromotions);
                    }
                }
            }else{
                // Add new line to original lines
                originalLines.add(curLine);
            }
        }
        return originalLines;
    }

    /**
     * 获取非套餐、非行赠品类型优惠，购物车行 直接减金额 显示后排除这些行，待无优惠行显示
     * 
     * @param oneShopCartLineList
     *            ，剩下的商品行
     * @param onePromotionConditionSKUList
     * @param onePromotionSettingSKUList
     * @return
     */
    @Override
    public List<ShoppingCartLineCommand> getLinesOfNormalPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList,
                    List<PromotionConditionSKU> onePromotionConditionSKUList){
        List<ShoppingCartLineCommand> linesNeedToCompress = new ArrayList<ShoppingCartLineCommand>();

        linesNeedToCompress = getLinesOfPromotionBySettingAndConditionList(
                        oneShopCartLineList,
                        onePromotionSettingSKUList,
                        onePromotionConditionSKUList);

        return linesNeedToCompress;
    }

    /**
     * 获取满足优惠活动条件的SKU，直接取促销活动的条件表达式。调用现有Condition检查方法。
     * 
     * @shoppingLines确定店铺下的购物车行
     * @promotionIdList确定店铺下活动号列表，从优惠结果SettingSKU List中来；
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionConditionSKU> getPromotionConditionSKUListFromShoppingCartLines(
                    List<ShoppingCartLineCommand> shoppingLines,
                    List<Long> promotionIdList){
        if (shoppingLines == null || shoppingLines.size() == 0 || promotionIdList == null || promotionIdList.size() == 0)
            return null;
        List<PromotionConditionSKU> suitSKUList = new ArrayList<PromotionConditionSKU>();
        // get each promotion condition list
        List<AtomicCondition> condList = new ArrayList<AtomicCondition>();
        List<PromotionConditionSKU> oneSKUList = new ArrayList<PromotionConditionSKU>();
        PromotionCommand onePromotion = null;
        for (Long promotionId : promotionIdList){
            condList = new ArrayList<AtomicCondition>();
            oneSKUList = new ArrayList<PromotionConditionSKU>();
            onePromotion = sdkPromotionRuleFilterManager.getPromotionByPromotionId(promotionId);
            if (onePromotion == null)
                continue;

            if (onePromotion.getAtomicConditionList() != null){
                condList.addAll(onePromotion.getAtomicConditionList());
            }
            if (onePromotion.getAtomicComplexConditionList() != null){
                if (onePromotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_STEP)){
                    if (onePromotion.getAtomicComplexConditionList().size() > 0)
                        condList.add(onePromotion.getAtomicComplexConditionList().get(0));
                }else{
                    condList.addAll(onePromotion.getAtomicComplexConditionList());
                }
            }
            if (condList == null || condList.size() == 0)
                continue;
            for (AtomicCondition condition : condList){
                // 整单其实不需要分组，直接显示在最后面即可
                /*
                 * if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) { oneSKUList =
                 * getAllSuitSKUList(shoppingLines,condition);
                 * 
                 * }else
                 */
                if (condition.getConditionValue() != null && condition.getConditionTag() != null
                                && condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_CUSTOM)){
                    oneSKUList = getCustomConditionSKUList(
                                    shoppingLines,
                                    Long.valueOf(condition.getConditionValue().toString()),
                                    condition);
                }else{
                    if (condition.getScopeTag() == null || condition.getScopeValue() <= 0L)
                        continue;
                    if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                        oneSKUList = getProductSuitSKUList(shoppingLines, condition.getScopeValue(), condition);
                    }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                        oneSKUList = getCategorySuitSKUList(shoppingLines, condition.getScopeValue(), condition);
                    }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                        oneSKUList = getCustomScopeConditionSKUList(shoppingLines, condition.getScopeValue(), condition);
                    }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                        oneSKUList = getComboSuitSKUList(shoppingLines, condition.getScopeValue(), condition);
                    }
                }
                if (oneSKUList == null || oneSKUList.size() == 0)
                    continue;
                for (PromotionConditionSKU sku : oneSKUList){
                    sku.setPromotionId(promotionId);
                }
                suitSKUList.addAll(oneSKUList);
            }
        }
        return suitSKUList;
    }

    /*
     * 按获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getAllSuitSKUList(List<ShoppingCartLineCommand> shoppingLines,AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                sku = new PromotionConditionSKU();
                sku.setShopId(shoppingLine.getShopId());
                sku.setPromotionId(condition.getPromotionId());
                sku.setNormalConditionId(condition.getNormalConditionId());
                sku.setComplexConditionId(condition.getComplexConditionId());
                sku.setComplexType(condition.getComplexType());

                sku.setSkuId(shoppingLine.getSkuId());
                sku.setItemId(shoppingLine.getItemId());

                skuList.add(sku);
            }
        }
        return skuList;
    }

    /*
     * 按Item获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getProductSuitSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long itemId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (null == shoppingLine.getItemId()){
                    continue;
                }
                if (shoppingLine.isGift() == true)
                    continue;
                if (itemId.equals(shoppingLine.getItemId())){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    /*
     * 按Category获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getCategorySuitSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long categoryId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (null == shoppingLine.getCategoryList() || shoppingLine.getCategoryList().size() == 0){
                    continue;
                }
                if (shoppingLine.isGift() == true){
                    continue;
                }
                if (shoppingLine.getCategoryList().contains(categoryId)){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    /*
     * 按Combo获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getComboSuitSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long comboId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                Set<String> comboIds = shoppingLine.getComboIds();
                if (comboIds == null || comboIds.size() == 0){
                    continue;
                }
                if (shoppingLine.isGift() == true){
                    continue;
                }
                // 计算该combo下的商品总金额
                if (comboIds.contains(String.valueOf(comboId))){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    /**
     * 获取行上参与的优惠活动
     * 
     * @param skuListSetting所有活动的
     */
    @Override
    public List<PromotionCommand> getPromotionListBySKU(List<PromotionSKUDiscAMTBySetting> skuListSetting,Long skuId){
        if (skuListSetting == null || skuListSetting.size() == 0 || skuId == null){
            return null;
        }
        List<PromotionCommand> promotionCommands = new ArrayList<PromotionCommand>();
        List<PromotionCommand> enginepromotionCommands = EngineManager.getInstance().getPromotionCommandList();
        if (enginepromotionCommands == null || enginepromotionCommands.size() == 0){
            return null;
        }
        for (PromotionSKUDiscAMTBySetting pSetting : skuListSetting){
            Long pskuid = pSetting.getSkuId();
            if (pskuid.equals(skuId)){
                Long proid = pSetting.getPromotionId();
                for (PromotionCommand promotionCommand : enginepromotionCommands){
                    Long eproid = promotionCommand.getPromotionId();
                    if (eproid.equals(proid)){
                        promotionCommands.add(promotionCommand);
                    }
                }
            }
        }
        return promotionCommands;
    }

    /**
     * 根据促销活动，获取活动的条件行
     * 
     * @param listSKUCondition所有活动的
     * @param
     */
    @Override
    public List<PromotionConditionSKU> getConditionSKUListByPromotionId(List<PromotionConditionSKU> listSKUCondition,Long promotionId){
        if (listSKUCondition == null || listSKUCondition.size() == 0 || promotionId == null){
            return null;
        }
        List<PromotionConditionSKU> promotionConditionSKUs = new ArrayList<PromotionConditionSKU>();
        for (PromotionConditionSKU pcSKU : listSKUCondition){
            Long proid = pcSKU.getPromotionId();
            if (proid.equals(promotionId)){
                promotionConditionSKUs.add(pcSKU);
            }
        }
        return promotionConditionSKUs;
    }

    /**
     * 根据促销活动，获取活动的优惠行
     * 
     * @param skuListSetting所有活动的
     *            ,行上活动
     * @param promotionId
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getSettingSKUListByPromotionId(
                    List<PromotionSKUDiscAMTBySetting> skuListSetting,
                    Long promotionId){
        if (skuListSetting == null || skuListSetting.size() == 0 || promotionId == null){
            return null;
        }
        List<PromotionSKUDiscAMTBySetting> pSKUDiscAMTBySettings = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (PromotionSKUDiscAMTBySetting pcSKU : skuListSetting){
            Long proid = pcSKU.getPromotionId();
            if (proid.equals(promotionId)){
                pSKUDiscAMTBySettings.add(pcSKU);
            }
        }
        return pSKUDiscAMTBySettings;
    }

    /*
     * 按Item获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getProductConditionSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long itemId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (null == shoppingLine.getItemId()){
                    continue;
                }
                if (shoppingLine.isGift() == true)
                    continue;
                if (itemId.equals(shoppingLine.getItemId())){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    /*
     * 按Category获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getCategoryConditionSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long categoryId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (null == shoppingLine.getCategoryList() || shoppingLine.getCategoryList().size() == 0){
                    continue;
                }
                if (shoppingLine.isGift() == true){
                    continue;
                }
                if (shoppingLine.getCategoryList().contains(categoryId)){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    private List<PromotionConditionSKU> getCustomScopeConditionSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long customId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (shoppingLine.getItemId() == null){
                    continue;
                }
                if (shoppingLine.isGift() == true){
                    continue;
                }
                // 计算该combo下的商品总金额
                if (customItemIds.contains(shoppingLine.getItemId())){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    private List<PromotionConditionSKU> getCustomConditionSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long customId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        Long promotionId = 0L;
        promotionId = condition.getPromotionId();

        if (promotionId <= 0)
            return null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (shoppingLine.getItemId() == null){
                    continue;
                }
                // 计算该combo下的商品总金额
                if (shoppingLine.getPromotionId() != null && shoppingLine.getPromotionId().equals(promotionId)){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

    /*
     * 按Combo获取条件范围内的商品行
     */
    private List<PromotionConditionSKU> getComboConditionSKUList(
                    List<ShoppingCartLineCommand> shoppingLines,
                    Long comboId,
                    AtomicCondition condition){
        List<PromotionConditionSKU> skuList = new ArrayList<PromotionConditionSKU>();
        PromotionConditionSKU sku = null;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                Set<String> comboIds = shoppingLine.getComboIds();
                if (comboIds == null || comboIds.size() == 0){
                    continue;
                }
                if (shoppingLine.isGift() == true){
                    continue;
                }
                // 计算该combo下的商品总金额
                if (comboIds.contains(String.valueOf(comboId))){
                    sku = new PromotionConditionSKU();
                    sku.setShopId(shoppingLine.getShopId());
                    sku.setPromotionId(condition.getPromotionId());
                    sku.setNormalConditionId(condition.getNormalConditionId());
                    sku.setComplexConditionId(condition.getComplexConditionId());
                    sku.setComplexType(condition.getComplexType());

                    sku.setSkuId(shoppingLine.getSkuId());
                    sku.setItemId(shoppingLine.getItemId());

                    skuList.add(sku);
                }
            }
        }
        return skuList;
    }

}
