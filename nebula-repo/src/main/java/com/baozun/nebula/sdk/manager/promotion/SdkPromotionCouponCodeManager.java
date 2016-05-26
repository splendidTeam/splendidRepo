package com.baozun.nebula.sdk.manager.promotion;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.system.PromotionAllCodeCommand;
import com.baozun.nebula.sdk.command.CouponCodeCommand;

public interface SdkPromotionCouponCodeManager extends BaseManager{

    /**
     * 批量使用优惠券
     * 
     * <p>
     * 如果 couponCodes是null 或者 empty 抛出异常
     * </p>
     * 
     * @param couponCodes
     * @since 5.3.1
     */
    void batchUseCouponCode(List<CouponCodeCommand> couponCodes);

    public void savecoupon(List<PromotionCouponCodeCommand> couponCommand);

    /**
     * 根据条件查询优惠劵
     * 
     * @param page
     * @param sorts
     * @param queryMap
     * @return
     */
    public Pagination<PromotionCouponCodeCommand> querycouponcodeListByQueryMapWithPage(
                    Page page,
                    Sort[] sorts,
                    Map<String, Object> queryMap);

    /***
     * 查询所有的优惠卷
     * 
     * @return
     */
    public List<PromotionCouponCodeCommand> findAllCouponCodeList();

    /**
     * 通过id禁用优惠卷
     * 
     * @param ids
     * @param state
     * @return
     */
    public Integer enableOrDisableCouponCodeById(Long id,Integer activeMark);

    /**
     * 根据优惠券券码查询优惠券
     * 
     * @param code
     * @return
     */
    public PromotionCouponCodeCommand findPromotionCouponCodeCommandByCouponCode(String code);

    /**
     * 根据优惠券券码列表查询优惠券列表
     * 
     * @param codes
     * @return
     */
    public List<PromotionCouponCodeCommand> findPromotionCouponCodeCommandListByCouponCodeList(
                    List<String> codes,
                    long couponTypeId,
                    Date queryTime,
                    long shopId);

    /**
     * 根据ID删除优惠券
     * 
     * @param id
     */
    public void removeCouponCodeById(Long id);

    /**
     * 保存优惠券
     * 
     * @param pcc
     */
    public void saveCouponCode(PromotionCouponCode pcc);

    /**
     * 根据优惠券券码列表查询优惠券列表
     * 
     * @param codes
     * @return
     */
    public List<PromotionCouponCodeCommand> findAndCheckPromotionCouponCodeCommandListByCodes(List<String> codes,Date queryTime);

    /**
     * @author 何波
     * @Description: 查询优惠劵的使用次数
     * @param couponCode
     * @return
     * Integer
     * @throws
     */
    Integer findTimesUsedByCouponCode(String couponCode);

    /**
     * @Title: findPromotionCouponCodeListByid
     * @Description:(查询所有的优惠卷)
     * @param @param
     *            couponid
     * @param @return
     *            设定文件
     * @return List<PromotionCouponCode> 返回类型
     * @throws
     *             @date
     *             2016年1月17日 下午5:14:42
     * @author GEWEI.LU
     */
    public List<PromotionAllCodeCommand> findPromotionCouponCodeListByidlist(Long coupontype,List<Long> couponid);
}
