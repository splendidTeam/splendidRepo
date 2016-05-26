package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.param.PromotionExclusiveGroupType;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCommandPriorityComparator;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustCommand;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustDetailCommand;
import com.baozun.nebula.dao.promotion.PromotionDao;
import com.baozun.nebula.dao.promotion.PromotionPriorityAdjustDao;
import com.baozun.nebula.dao.promotion.PromotionPriorityAdjustDetailDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjust;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjustDetail;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkFilterManager;
import com.baozun.nebula.sdk.manager.SdkPriorityAdjustManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;

@Transactional
@Service("sdkPriorityAdjustManager")
public class SdkPriorityAdjustManagerImpl implements SdkPriorityAdjustManager{

    private static final Logger              log = LoggerFactory.getLogger(SdkPriorityAdjustManagerImpl.class);

    @Autowired
    private PromotionDao                     promotionDao;

    @Autowired
    private PromotionPriorityAdjustDao       promotionPriorityAdjustDao;

    @Autowired
    private PromotionPriorityAdjustDetailDao promotionPriorityAdjustDetailDao;

    @Autowired
    private SdkPromotionManager              sdkPromotionManager;

    @Autowired
    private SdkFilterManager                 sdkFilterManager;

    @Override
    @Transactional(readOnly = true)
    public List<PromotionCommand> findConflictingPromotionListByTimePoint(Date timePoint,Long shopId){
        List<PromotionCommand> result = new ArrayList<PromotionCommand>();
        List<PromotionCommand> promotionList = promotionDao.findConflictingPromotionListByTimePoint(timePoint, shopId);
        if (promotionList.size() > 1){
            result = findConflictingPromotionList(promotionList, shopId); // 会员和商品冲突
        }
        for (PromotionCommand cmd : result){
            cmd.setLifecycle(sdkPromotionManager.calculateLifecycle(cmd.getLifecycle(), cmd.getStartTime(), cmd.getEndTime()));
        }

        return result;
    }

    /**
     * 筛选出会员和商品都有冲突的促销
     * 
     * @param promotionList
     * @return
     */
    private List<PromotionCommand> findConflictingPromotionList(List<PromotionCommand> promotionList,Long shopId){
        List<PromotionCommand> result = new ArrayList<PromotionCommand>();
        for (PromotionCommand outCmd : promotionList){
            String outMemberExp = outCmd.getMemComboExpression();
            String outProductExp = outCmd.getScopeExpression();
            for (PromotionCommand inCmd : promotionList){
                if (!outCmd.equals(inCmd)){
                    String inMemberExp = inCmd.getMemComboExpression();
                    String inProductExp = inCmd.getScopeExpression();
                    if (sdkFilterManager.isMemberConfilct(inMemberExp, outMemberExp)
                                    && sdkFilterManager.isProductConfilct(inProductExp, outProductExp, shopId)){
                        result.add(outCmd);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<PromotionPriorityAdjustCommand> findPriorityAdjustList(Page page,Sort[] sorts,Map<String, Object> paraMap){
        Pagination<PromotionPriorityAdjustCommand> pagination = promotionPriorityAdjustDao.findPriorityAdjustList(page, sorts, paraMap);

        //		for (PromotionPriorityAdjustCommand cmd : pagination.getItems()) {
        //			int status = generatePriorityStatus(cmd.getActiveMark(), cmd.getEndTime());
        //			cmd.setStatus(status);
        //		}
        return pagination;
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionPriorityAdjust findPriorityAdjustById(Long id){
        return promotionPriorityAdjustDao.getByPrimaryKey(id);
    }

    @Override
    public void enablePriorityById(Long id,Long userId){
        PromotionPriorityAdjust priority = promotionPriorityAdjustDao.getByPrimaryKey(id);
        if (priority == null)
            throw new BusinessException(Constants.PROMOTION_PRIORITY_INEXISTENCE);
        int status = generatePriorityStatus(priority.getActiveMark(), priority.getEndTime());
        if (PromotionPriorityAdjustCommand.STATUS_INACTIVE == status){
            priority.setActiveMark(PromotionPriorityAdjust.ACTIVEMARK_ENABLE);
            priority.setLastUpdateId(userId);
            priority.setLastUpdateTime(new Date());
        }else{
            throw new BusinessException(Constants.PROMOTION_PRIORITY_STATUS_ERROR);
        }
    }

    @Override
    public void disablePriorityById(Long id,Long userId){
        PromotionPriorityAdjust priority = promotionPriorityAdjustDao.getByPrimaryKey(id);
        if (priority == null)
            throw new BusinessException(Constants.PROMOTION_PRIORITY_INEXISTENCE);
        int status = generatePriorityStatus(priority.getActiveMark(), priority.getEndTime());
        if (PromotionPriorityAdjustCommand.STATUS_ACTIVE == status){
            priority.setActiveMark(PromotionPriorityAdjust.ACTIVEMARK_FORBIDDEN);
            priority.setLastUpdateId(userId);
            priority.setLastUpdateTime(new Date());
        }else{
            throw new BusinessException(Constants.PROMOTION_PRIORITY_STATUS_ERROR);
        }
    }

    /**
     * 获取优先级的状态
     * 
     * @param activeMark
     * @param endTime
     * @return
     */
    private int generatePriorityStatus(Integer activeMark,Date endTime){
        if ((PromotionPriorityAdjust.ACTIVEMARK_ENABLE.equals(activeMark) || PromotionPriorityAdjust.ACTIVEMARK_DISABLE.equals(activeMark))
                        && endTime.compareTo(new Date()) < 0){
            return PromotionPriorityAdjustCommand.STATUS_EXPIRED;
        }
        return activeMark;
    }

    @Override
    public Long saveOrUpdatePriority(PromotionPriorityAdjustCommand cmd){
        JSONArray json = JSONArray.fromObject(cmd.getPriorityDetailListString());
        @SuppressWarnings("unchecked")
        List<PromotionPriorityAdjustDetail> detailList = (List<PromotionPriorityAdjustDetail>) JSONArray
                        .toCollection(json, PromotionPriorityAdjustDetail.class);

        PromotionPriorityAdjust priority = new PromotionPriorityAdjust();
        if (null == cmd.getId()){ // 新建
            BeanUtils.copyProperties(cmd, priority);
            priority.setActiveMark(PromotionPriorityAdjust.ACTIVEMARK_DISABLE);
            priority.setLastUpdateTime(new Date());
            promotionPriorityAdjustDao.save(priority);
        }else{ // 更新
            priority = promotionPriorityAdjustDao.getByPrimaryKey(cmd.getId());
            priority.setAdjustName(cmd.getAdjustName());
            priority.setEndTime(cmd.getEndTime());
            priority.setLastUpdateId(cmd.getLastUpdateId());
            priority.setLastUpdateTime(new Date());
            priority.setStartTime(cmd.getStartTime());

            /* 删除已有优先级详情 */
            List<Long> idList = new ArrayList<Long>();
            for (PromotionPriorityAdjustDetailCommand d : promotionPriorityAdjustDetailDao
                            .findPromotionPriorityAdjustDetailByAdjustid(priority.getId())){
                idList.add(d.getId());
            }
            promotionPriorityAdjustDetailDao.deleteAllByPrimaryKey(idList);
        }

        int i = 1;
        for (PromotionPriorityAdjustDetail d : detailList){
            d.setAdjustId(priority.getId());
            d.setPriority(i);
            promotionPriorityAdjustDetailDao.save(d);
            i++;
        }

        return priority.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionPriorityAdjustDetailCommand> findPriorityDetailListByPriorityId(Long id){
        // 显示时与新增一致的Lifecycle 何波
        List<PromotionPriorityAdjustDetailCommand> result = promotionPriorityAdjustDetailDao.findPriorityDetailListByPriorityId(id);
        //		for (PromotionPriorityAdjustDetailCommand cmd : result) {
        //			cmd.setPromotionLifecycle(sdkPromotionManager.calculateLifecycle(cmd.getPromotionLifecycle(), cmd.getPromotionStartTime(), cmd.getPromotionEndTime()));
        //		}
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionPriorityAdjustDetailCommand> findPriorityDetailListByShopIdCurrentTime(List<Long> shopIds,Date currentTime){
        return promotionPriorityAdjustDetailDao.findPriorityDetailListByShopIdCurrentTime(shopIds);
    }

    /**
     * 调整促销优先级. 将强制调整优先级的促销集合添加到 促销集合的最前面
     * Origin：159,166,167,168,162.Adjustment:168,167,166(不参与),169(原中不存在) 步骤：
     * 1，把调整中的不参与的，从原列表中去掉； 2，把调整中的不在原列表中的，从调整列表中去掉； 3，把原列表中的，添加都调整列表中，存在的就不添加；
     * 4，返回最后的调整列表； 以上结果：168,167,159,162
     * 
     * @param promos
     * @param shopIds
     * @param currentTime
     * @return
     */
    @Override
    public List<PromotionCommand> promotionAdjustPriority(List<PromotionCommand> originalPromosList,Long shopId,Date currentTime){
        List<PromotionCommand> originalOneShopPromosList = new ArrayList<PromotionCommand>();// 当前店铺规则集合
        for (PromotionCommand onePrm : originalPromosList){
            if (onePrm.getShopId().equals(shopId)){
                originalOneShopPromosList.add(onePrm);
            }
        }
        // 当前店铺没有活动规则
        if (null == originalOneShopPromosList || originalOneShopPromosList.size() == 0){
            return null;
        }
        log.info("内存中筛选过后的排序");
        logPromotionPriority(originalOneShopPromosList);

        List<Long> shopIds = new ArrayList<Long>();
        shopIds.add(shopId);
        // 调整的优先级列表
        List<PromotionPriorityAdjustDetailCommand> oneShopPromoAdjustment = findPriorityDetailListByShopIdCurrentTime(shopIds, currentTime);
        // 当前店铺在这个时间点没有优先级列表，返回原列表数据
        if (null == oneShopPromoAdjustment || oneShopPromoAdjustment.size() == 0){
            return originalOneShopPromosList;
        }
        log.info("优先级调整记录");
        logPromotionAdjustPriority(oneShopPromoAdjustment);

        List<PromotionCommand> newOnePromoList = promotionAdjustmentByAdjustPriority(originalOneShopPromosList, oneShopPromoAdjustment);

        log.info("内存中经过优先级调整排序之后");
        logPromotionPriority(newOnePromoList);
        return newOnePromoList;
    }

    private void logPromotionAdjustPriority(List<PromotionPriorityAdjustDetailCommand> allAdjustList){
        if (null == allAdjustList || allAdjustList.size() < 0)
            return;
        for (PromotionPriorityAdjustDetailCommand one : allAdjustList){
            log.info(
                            "活动编号：" + one.getPromotionId() + "，优先级：" + one.getPriority() + "，活动分组：" + one.getGroupName() + "，拍他标签："
                                            + one.getExclusiveMark());
        }
        log.info("结束！");
    }

    private void logPromotionPriority(List<PromotionCommand> allPromotionList){
        if (null == allPromotionList || allPromotionList.size() < 0)
            return;
        String exclusiveType = "";
        for (PromotionCommand one : allPromotionList){
            if (null == one.getGroupType()){
                exclusiveType = "null";
            }else if (one.getGroupType().equals(PromotionExclusiveGroupType.SHARE)){
                exclusiveType = "共享";
            }else if (one.getGroupType().equals(PromotionExclusiveGroupType.SINGLE)){
                exclusiveType = "单享";
            }else{
                exclusiveType = "null";
            }
            log.info(
                            "活动编号：" + one.getPromotionId() + "，优先级：" + one.getPriority() + "，缺省排他：" + one.getExclusiveMark() + "，排他组类型："
                                            + exclusiveType + "，拍他组名：" + one.getGroupName() + "，确省优先级：" + one.getDefaultPriority()
                                            + "，活动名称：" + one.getPromotionName());
        }
        log.info("结束！");
    }

    /**
     * 1，以原列表中的优先级为基础，在AD列表中找，如果不在Ad表中，优先级降到最低9999，
     * 如果存在把排他信息传给活动（分组名，排他标签）；
     * 2，按照Priority重新排序；
     * 
     * @param originalPromosList
     *            当前店铺列表集合
     * @param promotionProAdjustList
     *            优先级列表集合
     * @return
     */
    private List<PromotionCommand> promotionAdjustmentByAdjustPriority(
                    List<PromotionCommand> originalPromosList,
                    List<PromotionPriorityAdjustDetailCommand> promotionProAdjustList){
        if (null == promotionProAdjustList || promotionProAdjustList.size() <= 0)
            return originalPromosList;
        PromotionPriorityAdjustDetailCommand adOfOnePrm = null;
        for (PromotionCommand onePrm : originalPromosList){
            adOfOnePrm = null;
            for (PromotionPriorityAdjustDetailCommand oneAd : promotionProAdjustList){
                if (onePrm.getPromotionId().equals(oneAd.getPromotionId())){
                    adOfOnePrm = oneAd;
                    break;
                }
            }
            if (null != adOfOnePrm){
                onePrm.setPriority(adOfOnePrm.getPriority());
                onePrm.setGroupName(adOfOnePrm.getGroupName());
                onePrm.setGroupType(adOfOnePrm.getGroupType());
                onePrm.setExclusiveMark(adOfOnePrm.getExclusiveMark());
            }else{
                onePrm.setPriority(9999);
            }
        }
        //整理后对原活动列表按Priority排序,缺省按publish time倒序
        Collections.sort(originalPromosList, new PromotionCommandPriorityComparator());

        return originalPromosList;
    }

    /**
     * 返回存在优先级列表的原始数据
     * 
     * @param originalPromosList
     *            当前店铺集合
     * @param promotionId
     *            优先级的规则ID
     * @return
     */
    private PromotionCommand getPromotionCommandById(List<PromotionCommand> originalPromosList,long promotionId){
        for (PromotionCommand onePromo : originalPromosList){
            if (onePromo.getPromotionId().equals(promotionId)){
                return onePromo;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Integer enableOrUnablePriorityById(Long id,Long userId,Integer activeMark){
        return promotionPriorityAdjustDao.enableOrUnablePriorityById(id, userId, activeMark);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionPriorityAdjustCommand> findEffectivePriorityList(Long shopId){
        return promotionPriorityAdjustDao.findEffectivePriorityList(shopId);
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionPriorityAdjust findPriorityAdjustByName(String name){
        return promotionPriorityAdjustDao.findPriorityAdjustByName(name);
    }
}
