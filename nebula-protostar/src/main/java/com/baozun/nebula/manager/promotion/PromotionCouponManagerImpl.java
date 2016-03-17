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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ReadStatus;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.PromotionCouponQueryCommand;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.promotion.PromotionCouponDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.promotion.PromotionCoupon;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager;

/**
 * @author - 项硕
 */
@Service("promotionCouponManager")
public class PromotionCouponManagerImpl implements PromotionCouponManager {

	private static final Logger log = LoggerFactory
			.getLogger(PromotionCouponManagerImpl.class);

	@Autowired
	private SdkPromotionCouponCodeManager sdkCouponCodeManager;
	@Autowired
	private ExcelManipulatorFactory excelFactory;

	@Autowired
	private PromotionCouponDao promotionCouponDao;

	@Autowired
	private UserDao userDao;

	@Override
	public List<String> importCouponCode(MultipartFile file,
			PromotionCouponCodeCommand coupon) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PromotionCouponCodeCommand> list = new ArrayList<PromotionCouponCodeCommand>();
		map.put("codeList", list);
		List<String> errorList = new ArrayList<String>();
		PromotionCoupon promotionCoupon = promotionCouponDao.getByPrimaryKey(coupon.getCouponId());
		try {
			ExcelReader reader = excelFactory.createExcelReader("couponImport");
			//设置时间格式
			reader.getDefinition().getExcelSheets().get(0).getExcelBlock().getCells().get(1).setPattern("yyyy-MM-dd HH:mm:ss");
			reader.getDefinition().getExcelSheets().get(0).getExcelBlock().getCells().get(2).setPattern("yyyy-MM-dd HH:mm:ss");
			ReadStatus status = reader.readSheet(file.getInputStream(), 0, map);
			if (null != status
					&& status.getStatus() == ReadStatus.STATUS_SUCCESS) {
				for (PromotionCouponCodeCommand cmd : list) {
					String code = cmd.getCouponCode();
					if (StringUtils.isNotBlank(code)) {
						PromotionCouponCode pcc = new PromotionCouponCode();
						BeanUtils.copyProperties(coupon, pcc);
						Date startTime = cmd.getStartTime();
						Date endTime = cmd.getEndTime();
						if(startTime==null){
							errorList.add("优惠劵:"+code+"开始时间为空");
							continue;
						}
						if(endTime==null){
							errorList.add("优惠劵:"+code+"结束时间为空");
							continue;
						}
						if(startTime.compareTo(endTime)>=0){
							errorList.add("优惠劵:"+code+"开始时间应早于结束时间");
							continue;
						}
						pcc.setStartTime(startTime);
						pcc.setEndTime(endTime);
						pcc.setCreateTime(new Date());
						pcc.setCouponCode(code);
						pcc.setLimitTimes(promotionCoupon.getLimitTimes());
						PromotionCouponCodeCommand codeCommand = sdkCouponCodeManager
								.findPromotionCouponCodeCommandByCouponCode(code);
						if (null != codeCommand
								&& (!PromotionCouponCode.ACTIVE_MARK_OFF
										.equals(codeCommand.getActiveMark()))) {
							// 不能有两张同code且都是有效状态的优惠券
							errorList.add("优惠劵:"+code+"已经存在");
							continue;
						}
						sdkCouponCodeManager.saveCouponCode(pcc);
						log.debug("import conpon code: " + code);
					}
				}
			} else {
				throw new BusinessException(
						Constants.PROMOTION_COUPON_EXCEL_READ_ERROR);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(
					Constants.PROMOTION_COUPON_EXCEL_READ_ERROR);
		}
		return errorList;
	}

	@Override
	public Pagination<PromotionCouponQueryCommand> queryCouponListByConditionallyWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		Pagination<PromotionCouponQueryCommand> pagination = promotionCouponDao
				.queryCouponListByConditionallyWithPage(page, sorts, paraMap);
		List<PromotionCouponQueryCommand> commands = pagination.getItems();
		List<PromotionCouponQueryCommand> coupons = new ArrayList<PromotionCouponQueryCommand>();
		if (CollectionUtils.isNotEmpty(commands)) {
			for (PromotionCouponQueryCommand coupon : commands) {
				Long userid = coupon.getCreateId();
				if (userid != null) {
					User user = userDao.findById(userid);
					if (user != null) {
						coupon.setCreateUserName(user.getRealName());
					} else {
						coupon.setCreateUserName(userid + "");
					}
				}
				coupons.add(coupon);
			}
		}
		pagination.setItems(coupons);
		return pagination;
	}

	@Override
	@Transactional
	public void delCouponsByIds(List<Long> ids) {
		if (ids.size() > 0) {
			for (Long id : ids) {
				PromotionCoupon model = promotionCouponDao.getByPrimaryKey(id);
				model.setActiveMark(2);
				promotionCouponDao.save(model);
			}
		}
	}

	@Override
	public PromotionCoupon createOrupdate(PromotionCoupon model) {
		Long id = model.getId();
		if (id != null) {
			PromotionCoupon dbmodel = promotionCouponDao.getByPrimaryKey(id);
			model.setVersion(dbmodel.getVersion());
			model.setCreateTime(dbmodel.getCreateTime());
			model.setCreateId(dbmodel.getCreateId());
		} else {
			model.setCreateTime(new Date());
			model.setVersion(new Date());
		}

		return promotionCouponDao.save(model);
	}

	@Override
	public PromotionCoupon queryByid(Long id) {
		return promotionCouponDao.getByPrimaryKey(id);
	}

	@Override
	public PromotionCoupon updateActive(Long id, int active) {
		PromotionCoupon model = queryByid(id);
		model.setActiveMark(active);
		return promotionCouponDao.save(model);
	}

}