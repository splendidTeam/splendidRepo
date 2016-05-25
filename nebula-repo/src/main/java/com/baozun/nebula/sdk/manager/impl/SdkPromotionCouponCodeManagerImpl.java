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
package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.system.PromotionAllCodeCommand;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager;
import com.feilong.core.Validator;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * The Class SdkPromotionCouponCodeManagerImpl.
 */
@Transactional
@Service("sdkPromotionCouponCodeManager")
public class SdkPromotionCouponCodeManagerImpl implements SdkPromotionCouponCodeManager{

    /** The Constant log. */
    private static final Logger    log = LoggerFactory.getLogger(SdkPromotionCouponCodeManagerImpl.class);

    /** The promotion coupon code dao. */
    @Autowired
    private PromotionCouponCodeDao promotionCouponCodeDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#batchUseCouponCode(java.util.List)
     */
    @Override
    public void batchUseCouponCode(List<CouponCodeCommand> couponCodes){
        Validate.notEmpty(couponCodes, "couponCodes can't be null/empty!");

        for (CouponCodeCommand couponCodeCommand : couponCodes){
            List<String> list = new ArrayList<String>();
            if (!couponCodeCommand.getIsOut()){
                list.add(couponCodeCommand.getCouponCode());
            }
            int res = promotionCouponCodeDao.comsumePromotionCouponCodeByCouponCodes(list);
            if (res != list.size()){
                throw new BusinessException(res < list.size() ? Constants.COUPON_IS_USED : Constants.CREATE_ORDER_FAILURE);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#savecoupon(java.util.List)
     */
    @Override
    public void savecoupon(List<PromotionCouponCodeCommand> couponCommand){
        for (PromotionCouponCodeCommand cp : couponCommand){
            PromotionCouponCode pccode = new PromotionCouponCode();
            BeanUtils.copyProperties(cp, pccode);
            promotionCouponCodeDao.save(pccode);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#querycouponcodeListByQueryMapWithPage(loxia.dao.Page,
     * loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<PromotionCouponCodeCommand> querycouponcodeListByQueryMapWithPage(
                    Page page,
                    Sort[] sorts,
                    Map<String, Object> queryMap){
        return promotionCouponCodeDao.querycouponcodeListByQueryMapWithPage(page, sorts, queryMap);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#findAllCouponCodeList()
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionCouponCodeCommand> findAllCouponCodeList(){
        List<PromotionCouponCodeCommand> pcc = new ArrayList<PromotionCouponCodeCommand>();
        List<PromotionCouponCode> ccodeCommand = promotionCouponCodeDao.findAllEffectPromotionCouponCodeList();
        for (PromotionCouponCode cp : ccodeCommand){
            PromotionCouponCodeCommand pc = new PromotionCouponCodeCommand();
            pc = (PromotionCouponCodeCommand) ConvertUtils.convertTwoObject(pc, cp);
            pcc.add(pc);
        }
        return pcc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#enableOrDisableCouponCodeById(java.lang.Long, java.lang.Integer)
     */
    @Override
    public Integer enableOrDisableCouponCodeById(Long id,Integer activeMark){
        Integer result = null;
        PromotionCouponCode cc = promotionCouponCodeDao.getByPrimaryKey(id);
        // 启用或禁止都必须是此优惠卷未使用
        if (cc != null){
            if (cc.getIsused() == 1){
                result = promotionCouponCodeDao.enableOrDisableCouponCodeById(id, activeMark);
            }else
                result = -1;
        }else
            log.info("enableOrDisableCouponCodeById method couponcode isused ==1");

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#findPromotionCouponCodeCommandByCouponCode(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public PromotionCouponCodeCommand findPromotionCouponCodeCommandByCouponCode(String code){
        return promotionCouponCodeDao.findPromotionCouponCodeCommandByCouponCode(code);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#findPromotionCouponCodeCommandListByCouponCodeList(java.util.List,
     * long, java.util.Date, long)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionCouponCodeCommand> findPromotionCouponCodeCommandListByCouponCodeList(
                    List<String> codes,
                    long couponTypeId,
                    Date queryTime,
                    long shopId){
        return promotionCouponCodeDao.findPromotionCouponCodeCommandListByCouponCodeList(codes, couponTypeId, queryTime, shopId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#findAndCheckPromotionCouponCodeCommandListByCodes(java.util.List,
     * java.util.Date)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionCouponCodeCommand> findAndCheckPromotionCouponCodeCommandListByCodes(List<String> codes,Date queryTime){
        return promotionCouponCodeDao.findAndCheckPromotionCouponCodeCommandListByCodes(codes, queryTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#removeCouponCodeById(java.lang.Long)
     */
    @Override
    public void removeCouponCodeById(Long id){
        PromotionCouponCode coupon = promotionCouponCodeDao.getByPrimaryKey(id);
        if (null == coupon){
            throw new BusinessException(Constants.PROMOTION_COUPON_CODE_INEXISTENCE);
        }else{
            coupon.setActiveMark(PromotionCouponCode.ACTIVE_MARK_OFF);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#saveCouponCode(com.baozun.nebula.model.promotion.PromotionCouponCode)
     */
    @Override
    public void saveCouponCode(PromotionCouponCode pcc){
        promotionCouponCodeDao.save(pcc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager#findTimesUsedByCouponCode(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findTimesUsedByCouponCode(String couponCode){
        if (Validator.isNullOrEmpty(couponCode)){
            throw new BusinessException("couponCode is empty");
        }
        return promotionCouponCodeDao.findTimesUsedByCouponCode(couponCode);
    }

    /**
     * Find promotion coupon code list byidlist.
     *
     * @author GEWEI.LU
     * @param coupontype
     *            the coupontype
     * @param couponid
     *            the couponid
     * @return List<PromotionCouponCode> 返回类型
     * @Title: findPromotionCouponCodeListByid
     * @Description:(查询所有的优惠卷)
     * @date 2016年1月17日 下午5:13:56
     */
    @Override
    public List<PromotionAllCodeCommand> findPromotionCouponCodeListByidlist(Long coupontype,List<Long> couponid){
        return promotionCouponCodeDao.findPromotionCouponCodeListByidlist(coupontype, couponid);
    }
}
