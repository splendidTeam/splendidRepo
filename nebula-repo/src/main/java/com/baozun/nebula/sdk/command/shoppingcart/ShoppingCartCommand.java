package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.UserDetails;

public class ShoppingCartCommand extends BaseModel{

	private static final long					serialVersionUID	= 7847538393318014437L;

	private Long								id;

	/** 购物车行信息 **/
	private List<ShoppingCartLineCommand>		shoppingCartLineCommands;

	/** 应付金额 **/
	private BigDecimal							originPayAmount;

	/** 应付运费 **/
	private BigDecimal							originShoppingFee;

	/** 实付运费 **/
	private BigDecimal							currentShippingFee;

	/** 实付金额 **/
	private BigDecimal							currentPayAmount;

	/** 优惠券code **/
	private List<String>						coupons;

	/** 优惠券code **/
	private List<PromotionCouponCodeCommand>	couponCodeCommands;

	/** 当前时间 **/
	private Date								currentTime;

	/** 会员信息 **/
	private UserDetails							userDetails;

	/**
	 * 整单商品数量
	 */
	private Integer								orderQuantity;

	/**
	 * 判断购物车是否需要更新持久化到行表 当原购物车中的行，由于促销活动的变化， 只导致合并行。不存在拆行。
	 */
	private Boolean								needPersistence;

	/**
	 * 购物车促销简介信息
	 */
	private List<PromotionBrief>				cartPromotionBriefList;

	/**
	 * 根据店铺封装shopCart对象
	 */
	private Map<Long, ShoppingCartCommand>		shoppingCartByShopIdMap;

	/**
	 * 基于店铺统计
	 */
	private List<ShopCartCommandByShop>			summaryShopCartList;

	/**
	 * 会员下单时使用的积分
	 */
	private BigDecimal							points;

	/**
	 * 会员可获得的积分
	 */
	private BigDecimal							earnPoints;

	/**
	 * 来自PTS配置的展示在购物车页的促销活动描述文字
	 */
	private String								promotionMsg;

	/**
	 * 整单合计优惠金额（优惠券 + 促销活动等）
	 */
	private BigDecimal							couponPayAmount;

	public List<ShopCartCommandByShop> getSummaryShopCartList(){
		return summaryShopCartList;
	}

	public void setSummaryShopCartList(List<ShopCartCommandByShop> summaryShopCartList){
		this.summaryShopCartList = summaryShopCartList;
	}

	public Map<Long, ShoppingCartCommand> getShoppingCartByShopIdMap(){
		return shoppingCartByShopIdMap;
	}

	public void setShoppingCartByShopIdMap(Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap){
		this.shoppingCartByShopIdMap = shoppingCartByShopIdMap;
	}

	public List<ShoppingCartLineCommand> getShoppingCartLineCommands(){
		return shoppingCartLineCommands;
	}

	public void setShoppingCartLineCommands(List<ShoppingCartLineCommand> shoppingCartLineCommands){
		this.shoppingCartLineCommands = shoppingCartLineCommands;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}

	public BigDecimal getOriginPayAmount(){
		return originPayAmount;
	}

	public void setOriginPayAmount(BigDecimal originPayAmount){
		this.originPayAmount = originPayAmount;
	}

	public BigDecimal getOriginShoppingFee(){
		return originShoppingFee;
	}

	public void setOriginShoppingFee(BigDecimal originShoppingFee){
		this.originShoppingFee = originShoppingFee;
	}

	public BigDecimal getCurrentShippingFee(){
		return currentShippingFee;
	}

	public void setCurrentShippingFee(BigDecimal currentShippingFee){
		this.currentShippingFee = currentShippingFee;
	}

	public BigDecimal getCurrentPayAmount(){
		return currentPayAmount;
	}

	public void setCurrentPayAmount(BigDecimal currentPayAmount){
		this.currentPayAmount = currentPayAmount;
	}

	public List<PromotionBrief> getCartPromotionBriefList(){
		return cartPromotionBriefList;
	}

	public void setCartPromotionBriefList(List<PromotionBrief> cartPromotionBriefList){
		this.cartPromotionBriefList = cartPromotionBriefList;
	}

	public List<String> getCoupons(){
		return coupons;
	}

	public void setCoupons(List<String> coupons){
		this.coupons = coupons;
	}

	public List<PromotionCouponCodeCommand> getCouponCodeCommands(){
		return couponCodeCommands;
	}

	public void setCouponCodeCommands(List<PromotionCouponCodeCommand> couponCodeCommands){
		this.couponCodeCommands = couponCodeCommands;
	}

	public Date getCurrentTime(){
		return currentTime;
	}

	public void setCurrentTime(Date currentTime){
		this.currentTime = currentTime;
	}

	public UserDetails getUserDetails(){
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails){
		this.userDetails = userDetails;
	}

	public Integer getOrderQuantity(){
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity){
		this.orderQuantity = orderQuantity;
	}

	public Boolean getNeedPersistence(){
		return needPersistence;
	}

	public void setNeedPersistence(Boolean needPersistence){
		this.needPersistence = needPersistence;
	}

	public String getPromotionMsg(){
		return promotionMsg;
	}

	public void setPromotionMsg(String promotionMsg){
		this.promotionMsg = promotionMsg;
	}

	public BigDecimal getPoints(){
		return points;
	}

	public void setPoints(BigDecimal points){
		this.points = points;
	}

	public BigDecimal getEarnPoints(){
		return earnPoints;
	}

	public void setEarnPoints(BigDecimal earnPoints){
		this.earnPoints = earnPoints;
	}

	public BigDecimal getCouponPayAmount(){
		return couponPayAmount;
	}

	public void setCouponPayAmount(BigDecimal couponPayAmount){
		this.couponPayAmount = couponPayAmount;
	}

}
