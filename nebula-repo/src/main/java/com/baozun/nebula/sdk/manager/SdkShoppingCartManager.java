package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.condition.ItemFactor;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 
 * @deprecated since5.3.1 这个类太巨无霸了 十分难以维护,违背了太多的原则,许出不许进,不要再加什么的方法了 --by feilong
 */
@Deprecated
public interface SdkShoppingCartManager extends BaseManager{

    public List<PromotionBrief> calcuPromoBriefs(ShoppingCartCommand shopCart);

    /**
     * 获取当前人员的选择的赠品行
     * 
     * @param memberId
     * @param callType
     * @return
     */
    public List<ShoppingCartLineCommand> findShoppingCartGiftLinesByMemberId(Long memberId,Integer callType);

    /**
     * 手工购物车
     * 
     * @param lines
     * @return
     */
    public ShoppingCartCommand findManualShoppingCart(List<ShoppingCartLineCommand> lines);

    /**
     * 根据会员id查询该会员的购物车数据
     * 
     * @param memberId
     * @param callType
     *            1代表查询选中状态的 。否则查所有的
     * @return
     */
    List<ShoppingCartLineCommand> findShoppingCartLinesByMemberId(Long memberId,Integer callType);

    /**
     * 
     * @param memberId
     * @param lineId
     *            当前购物车行Id
     * @param lines
     *            整个购物车行集合
     * @param memComboIds
     *            组合id
     * @param exist
     *            表示该跳记录是否在数据库购物车表中存在。true存在 false不存在
     * @param isReduce
     *            是否为减少 购物车行的数量。 true为减少 false增加
     * @return
     * @deprecated 不再调用 by feilong
     */
    @Deprecated
    Integer addOrUpdateShoppingCart(
                    Long memberId,
                    Long lineId,
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComboIds,
                    boolean exist,
                    boolean isReduce);

    /**
     * 
     * @param memberId
     * @param extentionCode
     *            当前操作的购物车行
     * @param lines
     *            整个购物车行集合
     * @param memComboIds
     *            组合id
     * @param exist
     *            表示该跳记录是否在数据库购物车表中存在。true存在 false不存在
     * @param isReduce
     *            是否为减少 购物车行的数量。 true为减少 false增加
     * @return
     * @deprecated
     *             当sku的extention_code修改后,无法修改该购物车行数据.
     *             请参照addOrUpdateShoppingCart(Long memberId, Long lineId, List<ShoppingCartLineCommand> lines, Set<String> memComboIds,
     *             boolean exist, boolean isReduce)
     * 
     */
    @Deprecated
    Integer addOrUpdateShoppingCart(
                    Long memberId,
                    String extentionCode,
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComboIds,
                    boolean exist,
                    boolean isReduce);

    /**
     * 
     * @param memberId
     * @param extentionCode
     *            当前操作的购物车行
     * @param lines
     *            整个购物车行集合
     * @param memComboIds
     *            组合id
     * @param exist
     *            表示该跳记录是否在数据库购物车表中存在。true存在 false不存在
     * @param isReduce
     *            是否为减少 购物车行的数量。 true为减少 false增加
     * @return
     * @deprecated 不再调用 by feilong
     */
    @Deprecated
    Integer addOrUpdateShoppingCartById(
                    Long memberId,
                    String extentionCode,
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComboIds,
                    boolean exist,
                    boolean isReduce);

    /**
     * 删除购物车中的一个商品
     * 
     * @deprecated
     *             当sku的extention_code修改后,无法删除该购物车行.
     *             请参照removeShoppingCartLineByIdAndMemberId(Long shoppingcartLineId, Long memberId)
     * @param memberId
     * @param extentionCode
     */
    @Deprecated
    Integer removeShoppingCartLine(Long memberId,String extentionCode);

    /**
     * 删除一条购物车行
     * 
     * @param memberId
     * @param shoppingCartLineId
     * @return
     */
    Integer removeShoppingCartLineById(Long memberId,Long shoppingCartLineId);

    /**
     * 清空该会员的购物车
     * 
     * @param memberId
     */
    Integer emptyShoppingCart(Long memberId);

    /**
     * 更改购物车中某些商品的的结算状态
     * 
     * @param memberId
     * @param extentionCodes
     * @param settleState
     */
    Integer updateCartLineSettlementState(Long memberId,List<String> extentionCodes,Integer settleState);

    /**
     * 立即购买
     * 
     * @param memberId
     * @param memCombos
     * @param shoppingCartLine
     * @param lines
     */
    Integer immediatelyBuy(
                    Long memberId,
                    Set<String> memCombos,
                    ShoppingCartLineCommand shoppingCartLine,
                    List<ShoppingCartLineCommand> lines);

    /**
     * 手工下单
     * 
     * @param shoppingCartLine
     * @param lines
     * @return
     */
    Integer manualBuy(BigDecimal buyPrice,ShoppingCartLineCommand shoppingCartLine,List<ShoppingCartLineCommand> lines);

    /**
     * 获取购物车该item下商品的金额
     * 
     * @param itemId
     * @param shoppingLines
     * @return
     */
    BigDecimal getProductAmount(Long itemId,List<ShoppingCartLineCommand> shoppingLines);

    BigDecimal getAllAmount(List<ShoppingCartLineCommand> shoppingLines);

    /**
     * 获取购物车该item下商品的数量
     * 
     * @param itemId
     * @param shoppingLines
     * @return
     */
    Integer getProductQuantity(Long itemId,List<ShoppingCartLineCommand> shoppingLines);

    /**
     * 获取购物车该分类下商品的金额
     * 
     * @param categoryId
     * @param shoppingLines
     * @return
     */
    BigDecimal getCategoryAmount(Long categoryId,List<ShoppingCartLineCommand> shoppingLines);

    BigDecimal getCustomAmount(List<Long> customItemIds,List<ShoppingCartLineCommand> shoppingLines);

    /**
     * 获取购物车该分类下商品的数量
     * 
     * @param categoryId
     * @param shoppingLines
     * @return
     */
    Integer getCategoryQuantity(Long categoryId,List<ShoppingCartLineCommand> shoppingLines);

    Integer getCustomQuantity(List<Long> customItemIds,List<ShoppingCartLineCommand> shoppingLines);

    /**
     * 获取购物车该组合下商品的金额
     * 
     * @param comboId
     * @param shoppingLines
     * @return
     */
    public BigDecimal getComboAmount(Long comboId,List<ShoppingCartLineCommand> shoppingLines);

    /**
     * 获取购物车该组合下商品的数量
     * 
     * @param comboId
     * @param shoppingLines
     * @return
     */
    public Integer getComboQuantity(Long comboId,List<ShoppingCartLineCommand> shoppingLines);

    // 统计各个分类下SKU、QTY*SalesPrice
    public Map<Long, BigDecimal> getSKUSalesInfoByCategory(ShoppingCartCommand shopCart,Long categoryId);

    // 按整单优惠，计算累计优惠金额
    // public BigDecimal
    // getDiscountAMTOrderDiscountAMTByAMT(List<ShoppingCartLineCommand>
    // lines,BigDecimal discountAmount);

    // 按整单折扣率，计算累计折扣金额
    public BigDecimal getDiscountAMTOrderDiscountRateByRate(
                    ShoppingCartCommand shopCart,
                    BigDecimal discountRate,
                    BigDecimal previousDiscAMTAll);

    // 统计每个item的总金额
    public BigDecimal getItemAmount(ShoppingCartCommand shopCart,Long itemId);

    // 统计每个item下的sku的金额
    public Map<Long, BigDecimal> getSKUSalesInfoByItem(ShoppingCartCommand shopCart,Long itemId);

    // 计算礼品从UI选择SKU后。要等到导购整合好后，才能起作用.
    // public List<PromotionSKUDiscAMTBySetting>
    // getDiscountAMTGiftBySKU(List<ShoppingCartLineCommand> lines, List<Long>
    // skuIds);
    // 计算礼品的累计金额,按ItemID。一个item下多个SKU，按不定顺序取足QTY返回优惠金额
    // public List<PromotionSKUDiscAMTBySetting>
    // getDiscountAMTGiftByItemID(List<ShoppingCartLineCommand> lines,long
    // itemId,Integer qty);
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByItemID(
                    long shopId,
                    AtomicSetting setting,
                    long itemId,
                    Integer qty,
                    Integer displayCountLimited);

    // 计算礼品的累计金额,按CategoryID。一个Category下多个SKU，按不定顺序取足QTY返回优惠金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByCategoryID(
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited);

    // public List<PromotionSKUDiscAMTBySetting>
    // getDiscountAMTGiftByCategoryIDWithoutAMT(long shopId,long
    // categoryId,Integer qty);

    // 计算礼品的累计金额,按ComboID。一个Combo下多个SKU，按不定顺序取足QTY返回优惠金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByComboID(
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited);

    // 整单按Item，计算Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal rate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    // 按Item，计算Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 按件，计算Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 整单按Category，计算Category下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal rate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    // 单品按Category，计算Category下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 单件按Category，计算Category下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 全场折扣，计算全场下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    // 全场按Item折扣，计算全场下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 全场按件折扣，计算全场下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 组合折扣，计算该组合下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    List<PromotionBrief> briefListPrevious);

    // 组合按Item折扣，计算该Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    // 按组合按件折扣，计算该Combo下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 获取购物车商品总数量
    public Integer getShopCartItemQty(Long memberId);

    // 检查整单Coupon, ordcoupon(1)
    public boolean checkCouponByCALL(long couponTypeID,List<PromotionCouponCodeCommand> couponCodes,long shopId);

    // 检查ItemCoupon,scpcoupon(1,pid:41)
    public boolean checkCouponByItemId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long itemId,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId);

    // 检查分类Coupon,scpcoupon(1,cid:41)
    public boolean checkCouponByCategoryId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long categoryId,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId);

    public boolean checkCouponByCustomItemIds(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<Long> itemIdList,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId);

    // 检查ComboCoupon,scpcoupon(1,cmbid:41)
    public boolean checkCouponByComboId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long comboId,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId);

    // 获取整单Coupon金额
    // 根据CouponCodes List，检查当前Type的优惠券，计算出该Type的优惠金额
    public Map<String, BigDecimal> getDiscountAMTByCALLCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> coupons,
                    boolean onePieceMark,
                    long shopId,
                    BigDecimal previousDiscAMTAll);

    // 获取ItemCoupon金额
    // 根据CouponCodes List，检查当前Type的优惠券，计算出该Type下itemId的优惠金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByItemIdCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long itemId,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    List<PromotionBrief> briefListPrevious);

    // 获取分类Coupon金额
    // 根据CouponCodes List，检查当前Type的优惠券，计算出该Type下categoryId的优惠金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByCategoryIdCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long categoryId,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    List<PromotionBrief> briefListPrevious);

    // 获取ComboCoupon金额
    // 根据CouponCodes List，检查当前Type的优惠券，计算出该Type下comboId的优惠金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByComboIdCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long comboId,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    List<PromotionBrief> briefListPrevious);

    // 获取购物车中的所有店铺id
    public List<Long> getShopIds(List<ShoppingCartLineCommand> lines);

    // 订单内单品件数，都有三种范围类型，PID,CID,CMBID
    public List<ShoppingCartLineCommand> getOrderSKUQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderSKUQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderSKUQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderSKUQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited);

    // 订单内商品数，都有三种范围类型，PID,CID,CMBID
    public List<ShoppingCartLineCommand> getOrderItemQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderItemQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderItemQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited);

    // 订单内件数，都有三种范围类型，PID,CID,CMBID
    public List<ShoppingCartLineCommand> getOrderQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited);

    // 历史购买件数，都有三种范围类型，PID,CID,CMBID
    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited);

    // 历史购买商品数，都有三种范围类型，PID,CID,CMBID
    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByCategoryId(
                    ShoppingCartCommand shopCart,
                    long categoryId,
                    Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited);

    // 历史购买订单数，都有三种范围类型，PID,CID,CMBID
    public List<ShoppingCartLineCommand> getHistoryOrderQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited);

    /**
     * 将购物车行区分店铺
     * 
     * @param lines
     */
    public Map<Long, ShoppingCartCommand> getShoppingCartMapByShop(List<ShoppingCartLineCommand> lines);

    /**
     * 结算时的购物车有效性和库存检查
     * 
     * @param lines
     * @param memberId
     * @param memboComIds
     * @return
     */
    public void checkShoppingCartOnCalc(Long memberId,List<ShoppingCartLineCommand> lines,Set<String> memCombos);

    /**
     * 返回该购物车sku中最小的qty
     * 
     * @param shopCart
     * @return
     */
    public Integer getMiniSKUQTYInShoppingCartByAll(ShoppingCartCommand shopCart);

    /**
     * 返回该购物车中item下sku中最小的qty.如果没有lines在itemId下，则返回0
     * 
     * @param shopCart
     * @param itemId
     * @return
     */
    public Integer getMiniSKUQTYInShoppingCartByItemId(ShoppingCartCommand shopCart,Long itemId);

    /**
     * 返回该购物车中categoryId下sku中最小的qty.如果没有lines在categoryId下，则返回0
     * 
     * @param shopCart
     * @param categoryId
     * @return
     */
    public Integer getMiniSKUQTYInShoppingCartByCategoryId(ShoppingCartCommand shopCart,Long categoryId);

    /**
     * 返回该购物车中comboId下sku中最小的qty.如果没有lines在comboId下，则返回0
     * 
     * @param shopCart
     * @param comboId
     * @return
     */
    public Integer getMiniSKUQTYInShoppingCartByComboId(ShoppingCartCommand shopCart,Long comboId);

    /**
     * 返回该购物车需要支付的金额
     * 
     * @param shopCart
     * @return
     */
    public BigDecimal getNeedToPayAmountInShoppingCartByAll(ShoppingCartCommand shopCart);

    /**
     * 返回该购物车中该item范围内需要支付的金额
     * 
     * @param shopCart
     * @param itemId
     * @return
     */
    public BigDecimal getNeedToPayAmountInShoppingCartByItemId(ShoppingCartCommand shopCart,Long itemId);

    /**
     * 返回该购物车中该categoryId范围内需要支付的金额
     * 
     * @param shopCart
     * @param categoryId
     * @return
     */
    public BigDecimal getNeedToPayAmountInShoppingCartByCategoryId(ShoppingCartCommand shopCart,Long categoryId);

    public BigDecimal getNeedToPayAmountInShoppingCartByCustomItemIds(List<ShoppingCartLineCommand> lines,List<Long> customItemIdList);

    /**
     * 返回该购物车中该comboId范围内需要支付的金额
     * 
     * @param shopCart
     * @param comboId
     * @return
     */
    public BigDecimal getNeedToPayAmountInShoppingCartByComboId(ShoppingCartCommand shopCart,Long comboId);

    /**
     * 返回该购物车的商品数量
     * 
     * @param shopCart
     * @return
     */
    public Integer getQuantityInShoppingCartByAll(ShoppingCartCommand shopCart);

    /**
     * 返回该购物车中该item范围内的商品数量
     * 
     * @param shopCart
     * @param itemId
     * @return
     */
    public Integer getQuantityInShoppingCartByItemId(ShoppingCartCommand shopCart,Long itemId);

    /**
     * 返回该购物车中该categoryId范围内的商品数量
     * 
     * @param shopCart
     * @param categoryId
     * @return
     */
    public Integer getQuantityInShoppingCartByCustomItemIds(ShoppingCartCommand shopCart,List<Long> customItemIds);

    public Integer getQuantityInShoppingCartByCategoryId(ShoppingCartCommand shopCart,Long categoryId);

    /**
     * 返回该购物车中该comboId范围内的商品数量
     * 
     * @param shopCart
     * @param comboId
     * @return
     */
    public Integer getQuantityInShoppingCartByComboId(ShoppingCartCommand shopCart,Long comboId);

    /**
     * 根据CouponTypeID，获取Coupon类型，1代表优惠券，2代表折扣券
     * 
     * @param couponTypeId
     * @return
     */

    public Integer getCouponTypeByCouponTypeID(long couponTypeId);

    /**
     * 
     * @param usedCouponList
     * @return
     */
    public BigDecimal getCouponAmtFromUsedList(Map<String, BigDecimal> usedCouponList);

    /**
     * 按照商品检查Online Coupon的有效性
     * 
     * @param shoppingCartLines
     * @param itemId
     * @param couponTypeID
     * @param shopId
     * @return
     */
    public boolean checkOnLineCouponByItemId(List<ShoppingCartLineCommand> shoppingCartLines,long itemId,long couponTypeID,long shopId);

    /**
     * 按照分类检查Online Coupon的有效性
     * 
     * @param shoppingCartLines
     * @param categoryId
     * @param couponTypeID
     * @param shopId
     * @return
     */
    public boolean checkOnLineCouponByCategoryId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long categoryId,
                    long couponTypeID,
                    long shopId);

    /**
     * 按照分组检查Online Coupon的有效性
     * 
     * @param shoppingCartLines
     * @param comboId
     * @param couponTypeID
     * @param shopId
     * @return
     */
    public boolean checkOnLineCouponByComboId(List<ShoppingCartLineCommand> shoppingCartLines,long comboId,long couponTypeID,long shopId);

    // 组合优惠，计算该组合下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    // 组合按Item优惠，计算该Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 按组合按件优惠，计算该Combo下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 整单按Category，计算Category下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    // 单品按Category，计算Category下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 单件按Category，计算Category下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 整单按Item，计算Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    // 按Item，计算Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 按件，计算Item下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 全场优惠，计算全场下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    // 全场按Item优惠，计算全场下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    // 全场按件优惠，计算全场下的累计金额
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    public Set<Long> getItemIdsFromShoppingCartByComboId(List<ShoppingCartLineCommand> lines,long comboId);

    public Set<Long> getItemIdsFromShoppingCartByCategoryId(List<ShoppingCartLineCommand> lines,long categoryId);

    public Set<Long> getItemIdsFromShoppingCartByCustomItemIds(List<ShoppingCartLineCommand> lines,List<Long> itemIdList);

    public Set<Long> getItemIdsFromShoppingCartByCall(List<ShoppingCartLineCommand> lines);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    /**
     * 修改礼品行信息
     * 
     * @param skuIds
     * @param lineGroups
     * @param memberId
     * @param promotionId
     * @return
     */
    public void updateShoppingCartlineGift(Long[] skuIds,String[] lineGroups,Long memberId,Long promotionId);

    /**
     * 通过购物车行id和用户Id删除用户选中的礼品
     * 
     * @param lineId
     * @param memeberId
     */
    public Integer removeShoppingCartGiftByIdAndMemberId(Long lineId,Long memberId);

    /**
     * 通过购物车行id和用户Id删除用户的购物车行信息
     * 
     * @param lineId
     * @param memberId
     * @return
     */
    public Integer removeShoppingCartLineByIdAndMemberId(Long lineId,Long memberId);

    /**
     * 检察商品有效性
     * 
     * @param lines
     * @param memComIds
     * @param memberId
     * @param coupons
     * @param calcFreightCommand
     * @return
     */
    public Map<Integer, List<ShoppingCartLineCommand>> checkGiftEffective(
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComIds,
                    Long memberId,
                    List<String> coupons,
                    CalcFreightCommand calcFreightCommand);

    /**
     * 检查用户选择赠品的个数是否大于活动中的赠品样数限制个数
     * 
     * @param currentSkuIds
     *            :当前选中的赠品skuId集合
     * @param lineGroups
     * @param promotionId
     * @param beforeGiftLines
     *            :以前选中的赠品集合
     * @return
     */
    public boolean checkSelectedGiftLimit(
                    Long[] currentSkuIds,
                    String[] lineGroups,
                    Long promotionId,
                    List<ShoppingCartLineCommand> beforeGiftLines);

    /**
     * 检查用户已选中的赠品
     * 
     * @param memberId
     * @return
     */
    public List<ShoppingCartLineCommand> findShopCartGiftLineByMemberId(Long memberId);

    public PromotionSKUDiscAMTBySetting getPromotionSkuAMTSetting(ShoppingCartLineCommand line,BigDecimal disAmt);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious);

    public List<ShoppingCartLineCommand> getOrderItemQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getOrderQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByCustomId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited);

    public List<ShoppingCartLineCommand> getHistoryOrderQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited);

    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByItemList(
                    List<Long> itemIdsLong,
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited);

    public Map<String, BigDecimal> getCouponTotalAmount(
                    List<PromotionCouponCodeCommand> couponCodes,
                    long couponTypeId,
                    BigDecimal totalPrice,
                    long shopID);

    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByItemID(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByCategoryID(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByComboID(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious);

    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByCustomItemIds(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIds,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious);

    public BigDecimal getNeedToPayAmountInShoppingCartByAll(List<ShoppingCartLineCommand> lines);

    public Integer doPromotionCheck(ShoppingCartCommand cart,Set<String> memboIds);

    public boolean merageShoppingCartLineById(Long userId,ShoppingCartLineCommand shoppingCartLine);
}
