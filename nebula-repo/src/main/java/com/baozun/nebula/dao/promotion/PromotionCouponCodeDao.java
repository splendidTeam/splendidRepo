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
package com.baozun.nebula.dao.promotion;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.PromotionCouponInfoCommand;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.system.PromotionAllCodeCommand;

/**
 * PromotionCouponCodeDao
 * 
 * @author Justin
 * 
 */
public interface PromotionCouponCodeDao extends GenericEntityDao<PromotionCouponCode, Long> {

	/**
	 * 获取所有PromotionCouponCode列表
	 * 
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCode.class)
	List<PromotionCouponCode> findAllPromotionCouponCodeList();

	/**
	 * 通过ids获取PromotionCouponCode列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCode.class)
	List<PromotionCouponCode> findPromotionCouponCodeListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过coupon获取PromotionCouponCode
	 * 
	 * @param coupon
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCode.class)
	PromotionCouponCode findPromotionCouponCodeBycoupon(@QueryParam("couponCode") String couponCode);

	/**
	 * 通过参数map获取PromotionCouponCode列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCode.class)
	List<PromotionCouponCode> findPromotionCouponCodeListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取PromotionCouponCode列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCodeCommand.class, pagable = true, withGroupby = true)
	public Pagination<PromotionCouponCodeCommand> querycouponcodeListByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> queryMap);

	/**
	 * 批量修改优惠券的使用状态
	 * 
	 * @param couponCodes
	 * @param isused
	 */
	@NativeUpdate
	void useOrUnusePromotionCouponCodeByCouponCodes(@QueryParam("couponCodes") List<String> couponCodes, @QueryParam("isused") Integer isused);

	/**
	 * Coupon消费一次
	 * 
	 * @param couponCodes
	 * @param isused
	 */
	@NativeUpdate
	Integer consumePromotionCouponCodeByCouponCodes(@QueryParam("couponCodes") List<String> couponCodes, @QueryParam("isused") Integer isused);

	/**
	 * 批量使用优惠券
	 * 
	 * @param couponCodes
	 * @param isused
	 */
	@NativeUpdate
	int comsumePromotionCouponCodeByCouponCodes(@QueryParam("couponCodes") List<String> couponCodes);

	/**
	 * 通过ids批量删除PromotionCouponCode 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removePromotionCouponCodeByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 获取有效的PromotionCouponCode列表 lifecycle =1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCode.class)
	List<PromotionCouponCode> findAllEffectPromotionCouponCodeList();

	/**
	 * 通过参数map获取有效的PromotionCouponCode列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCode.class)
	List<PromotionCouponCode> findEffectPromotionCouponCodeListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 通过id禁用开启couponCode 设置activeMark =0
	 * 
	 * @param id
	 * @return
	 */
	@NativeUpdate
	Integer enableOrDisableCouponCodeById(@QueryParam("id") Long id, @QueryParam("activeMark") Integer activeMark);

	/**
	 * 根据优惠券券码查询优惠券
	 * 
	 * @param code
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCodeCommand.class)
	PromotionCouponCodeCommand findPromotionCouponCodeCommandByCouponCode(@QueryParam("code") String code);

	/**
	 * 根据优惠券券码列表查询优惠券列表
	 * 
	 * @param codes
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCodeCommand.class)
	List<PromotionCouponCodeCommand> findPromotionCouponCodeCommandListByCouponCodeList(@QueryParam("codes") List<String> codes, @QueryParam("couponTypeId") long couponTypeId, @QueryParam("queryTime") Date queryTime, @QueryParam("shopId") long shopId);

	
	/**
	 * 查询有效的优惠券
	 * 
	 * @param codes
	 * @return
	 */
	@NativeQuery(model = PromotionAllCodeCommand.class)
	List<PromotionAllCodeCommand> findPromotionCouponCodeListByidlist(@QueryParam("coupontype") Long coupontype,@QueryParam("couponid") List<Long> couponid);

	
	
	/**
	 * 根据用户ID查询用户的优惠券
	 * 
	 * @param codes
	 * @return
	 */
	@NativeQuery(model = PromotionAllCodeCommand.class)
	List<PromotionAllCodeCommand> findPromotionCouponCodeListBymemberidlist(@QueryParam("memberid") Long memberid);

	
	
	/**
	 * 个人中心查询优惠券
	 * 
	 * @param codes
	 * @return
	 */
	@NativeQuery(model = PromotionAllCodeCommand.class)
	Pagination<PromotionAllCodeCommand> findPromotionCouponCodeListpersonalcenterlist(Page page,Sort[] sorts,@QueryParam("memberid") Long memberid,@QueryParam("usable") String usable,@QueryParam("alreadyuse") String alreadyuse,@QueryParam("alreaddue") String alreaddue);

	
	
	
	/**
	 * 根据优惠券券码列表查询优惠券列表
	 * 
	 * @param codes
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCodeCommand.class)
	List<PromotionCouponCodeCommand> findAndCheckPromotionCouponCodeCommandListByCodes(@QueryParam("codes") List<String> codes, @QueryParam("queryTime") Date queryTime);

	/**
	 * 根据优惠券编码查询符合要求的优惠券信息
	 * 
	 * @param couponCode
	 * @return
	 */
	@NativeQuery(model = PromotionCouponInfoCommand.class)
	PromotionCouponInfoCommand findCouponInfoByCouponCode(@QueryParam("couponCode") String couponCode);
	
	/**
	* @author 何波
	* @Description: 查询优惠劵的使用次数
	* @param couponCode
	* @return   
	* Integer   
	* @throws
	 */
	@NativeQuery(clazzes = Integer.class,alias="limitTimes")
	Integer findTimesUsedByCouponCode(@QueryParam("couponCode")String couponCode );
	

}
