package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

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
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager;
import com.baozun.nebula.utilities.common.Validator;

@Transactional
@Service("sdkPromotionCouponCodeManager")
public class SdkPromotionCouponCodeManagerImpl implements SdkPromotionCouponCodeManager {
	private static final Logger log = LoggerFactory.getLogger(SdkPromotionCouponCodeManagerImpl.class);

	@Autowired
	private PromotionCouponCodeDao couponCodeDao;

	@Override
	public void savecoupon(List<PromotionCouponCodeCommand> couponCommand) {
		for (PromotionCouponCodeCommand cp : couponCommand) {
			PromotionCouponCode pccode = new PromotionCouponCode();
			BeanUtils.copyProperties(cp, pccode);
			couponCodeDao.save(pccode);
		}

	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<PromotionCouponCodeCommand> querycouponcodeListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> queryMap) {
		return couponCodeDao.querycouponcodeListByQueryMapWithPage(page, sorts, queryMap);

	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionCouponCodeCommand> findAllCouponCodeList() {
		List<PromotionCouponCodeCommand> pcc = new ArrayList<PromotionCouponCodeCommand>();
		List<PromotionCouponCode> ccodeCommand = couponCodeDao.findAllEffectPromotionCouponCodeList();
		for (PromotionCouponCode cp : ccodeCommand) {
			PromotionCouponCodeCommand pc = new PromotionCouponCodeCommand();
			pc = (PromotionCouponCodeCommand) ConvertUtils.convertTwoObject(pc, cp);
			pcc.add(pc);
		}
		return pcc;
	}

	@Override
	public Integer enableOrDisableCouponCodeById(Long id, Integer activeMark) {
		Integer result = null;
		PromotionCouponCode cc = couponCodeDao.getByPrimaryKey(id);
		// 启用或禁止都必须是此优惠卷未使用
		if (cc != null) {
			if (cc.getIsused() == 1) {
				result = couponCodeDao.enableOrDisableCouponCodeById(id, activeMark);
			} else
				result = -1;
		} else
			log.info("enableOrDisableCouponCodeById method couponcode isused ==1");

		return result;
	}

	@Override
	@Transactional(readOnly=true)
	public PromotionCouponCodeCommand findPromotionCouponCodeCommandByCouponCode(String code) {
		return couponCodeDao.findPromotionCouponCodeCommandByCouponCode(code);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionCouponCodeCommand> findPromotionCouponCodeCommandListByCouponCodeList(List<String> codes, long couponTypeId, Date queryTime, long shopId) {
		return couponCodeDao.findPromotionCouponCodeCommandListByCouponCodeList(codes, couponTypeId, queryTime, shopId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionCouponCodeCommand> findAndCheckPromotionCouponCodeCommandListByCodes(List<String> codes, Date queryTime) {
		return couponCodeDao.findAndCheckPromotionCouponCodeCommandListByCodes(codes, queryTime);
	}

	@Override
	public void removeCouponCodeById(Long id) {
		PromotionCouponCode coupon = couponCodeDao.getByPrimaryKey(id);
		if (null == coupon) {
			throw new BusinessException(Constants.PROMOTION_COUPON_CODE_INEXISTENCE);
		} else {
			coupon.setActiveMark(PromotionCouponCode.ACTIVE_MARK_OFF);
		}
	}

	@Override
	public void saveCouponCode(PromotionCouponCode pcc) {
		couponCodeDao.save(pcc);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer findTimesUsedByCouponCode(String couponCode) {
		if(Validator.isNullOrEmpty(couponCode)){
			throw new BusinessException("couponCode is empty");
		}
		return couponCodeDao.findTimesUsedByCouponCode(couponCode);
	}

	/** 
	* @Title: findPromotionCouponCodeListByid 
	* @Description:(查询所有的优惠卷) 
	* @param @param couponid
	* @param @return    设定文件 
	* @return List<PromotionCouponCode>    返回类型 
	* @throws 
	* @date 2016年1月17日 下午5:13:56 
	* @author GEWEI.LU   
	*/
	public  List<PromotionAllCodeCommand> findPromotionCouponCodeListByidlist( Long coupontype,List<Long> couponid){
		 return couponCodeDao.findPromotionCouponCodeListByidlist(coupontype,couponid);
	}
}
