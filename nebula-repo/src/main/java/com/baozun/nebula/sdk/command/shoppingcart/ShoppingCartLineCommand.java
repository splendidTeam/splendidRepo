package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.SkuProperty;

public class ShoppingCartLineCommand extends BaseModel {

	private static final long serialVersionUID = -5071101827860973497L;

	private String extentionCode;

	/** 行分组 **/
	private Long lineGroup;

	/** 该订单行是否属于预售订单 **/
	private boolean isPresale;

	/** 是套餐行 t是标题行，f不是标题行。标题行是促销活动提示，非购物车行 **/
	private boolean isCaptionLine;
	
	/** 标题 **/
	private String lineCaption;
	
	/** 是套餐行 t是套餐行，f不是套餐行 **/
	private boolean isSuitLine;
	
	/** 商品款的id **/
	private Long skuId;

	/** 商品id **/
	private Long itemId;

	/** 购物数量 **/
	private Integer quantity;

	/** 商品名称 **/
	private String itemName;

	/** 商品图片 **/
	private String itemPic;

	/** 销售属性 **/
	private String saleProperty;

	/** 库房id **/
	private Long wareHoseId;

	/** 库房名称 **/
	private String wareHoseName;

	/** 是否可见 1可见 /0不可见 **/
	private Integer visibleMark;

	/** 是否限购 1限购 0不限购 **/
	private Integer limitMark;

	/** 限购数量 **/
	private Integer limit;

	/** 是否有库存 1有库存 0无库存 **/
	private Integer stockMark;

	/** 库存数量 **/
	private Integer stock;

	/******* 扩展engine所需要的字段begin *******/

	private String productCode;// 商品code

	private List<Long> lableIds;

	private String brandId;

	private List<Long> categoryList;

	private long storeId;

	private Long indstryId;

	private String state;

	/**
	 * 商品单价
	 */
	private BigDecimal salePrice;

	/**
	 * 行促销(此商口多个数量的促销总和)
	 * 折扣-包含整单优惠分摊
	 */
	private BigDecimal discount = BigDecimal.ZERO;
	
	/** 折扣单价-不包含整单优惠分摊 */
	private BigDecimal	discountPrice = BigDecimal.ZERO;;
	
	/** 套餐、行赠品类型，只有一个PromotionId
	 *  其他类型可能有多个活动 **/
	private List<PromotionCommand> promotionList;

	/**
	 * 是否赠品
	 */
	private boolean isGift;
	/**
	 * 1是行赠品,0是整单赠品
	 */
	private int giftType;
	
	/** 会员id */
	private Long memberId;

	/** 加入时间 */
	private Date createTime;

	/** PK **/
	private Long id;

	/** 结算状态 0未选中结算 1选中结算*/
	private Integer settlementState;

	/**
	 * 0代表非买品 1代表主卖品 
	 * 与itemInfo中的type一致
	 */
	private Integer type;

	/** 购物车中商品经有效性检查引擎检查之后是否有效的字段 **/
	private boolean isValid;

	/**
	 * 有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
	 */
	private Integer validType;

	/** 店铺id **/
	private Long shopId;

	/** 店铺名称 **/
	private String shopName;

	/** 销售属性 **/
	private List<SkuProperty> skuPropertys;

	/** 分组标签 **/
	private Set<String> comboIds;

	/** 吊牌价 **/
	private BigDecimal listPrice;

	/** 购物车行 金额小计 **/
	private BigDecimal subTotalAmt = BigDecimal.ZERO;

	/** 行上的Coupon **/
	private PromotionCouponCodeCommand couponCodeOnLine;

	/**
	 * 活动id
	 */
	private Long promotionId;

	/**
	 * 优惠记录id
	 */
	private Long settingId;

	/**
	 * 礼品显示类型，0不需要用户选择，1需要用户选择
	 */
	private Integer giftChoiceType;

	/**
	 * 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数
	 */
	private Integer giftCountLimited;
	
	/**
	 * 商城自定义参数，nebula不控制
	 */
	private Map<String, Object> customParamMap;

	public String getPromotionIds() {
		String tmp = "";
		if (this.promotionList!=null)
		{
			for(PromotionCommand cmd:this.promotionList)
			{
				if (tmp=="")
					tmp = cmd.getPromotionId().toString();
				else
					tmp = tmp + "," + cmd.getPromotionId().toString();
			}
		}
		return tmp;
	}
	public BigDecimal getSubTotalAmt() {
		return subTotalAmt;
	}

	public void setSubTotalAmt(BigDecimal subTotalAmt) {
		this.subTotalAmt = subTotalAmt;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getItemPic() {
		return itemPic;
	}

	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}

	public boolean isGift() {
		return isGift;
	}

	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getSaleProperty() {
		return saleProperty;
	}

	public void setSaleProperty(String saleProperty) {
		this.saleProperty = saleProperty;
	}

	public Integer getSettlementState() {
		return settlementState;
	}

	public void setSettlementState(Integer settlementState) {
		this.settlementState = settlementState;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<Long> getLableIds() {
		return lableIds;
	}

	public void setLableIds(List<Long> lableIds) {
		this.lableIds = lableIds;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public List<Long> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Long> categoryList) {
		this.categoryList = categoryList;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public Long getIndstryId() {
		return indstryId;
	}

	public void setIndstryId(Long indstryId) {
		this.indstryId = indstryId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isValid() {
		return isValid;
	}

	public Integer getValidType() {
		return validType;
	}

	public void setValidType(Integer validType) {
		this.validType = validType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getWareHoseId() {
		return wareHoseId;
	}

	public void setWareHoseId(Long wareHoseId) {
		this.wareHoseId = wareHoseId;
	}

	public String getWareHoseName() {
		return wareHoseName;
	}

	public void setWareHoseName(String wareHoseName) {
		this.wareHoseName = wareHoseName;
	}

	public Integer getVisibleMark() {
		return visibleMark;
	}

	public void setVisibleMark(Integer visibleMark) {
		this.visibleMark = visibleMark;
	}

	public Integer getLimitMark() {
		return limitMark;
	}

	public void setLimitMark(Integer limitMark) {
		this.limitMark = limitMark;
	}

	public Integer getStockMark() {
		return stockMark;
	}

	public void setStockMark(Integer stockMark) {
		this.stockMark = stockMark;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<SkuProperty> getSkuPropertys() {
		return skuPropertys;
	}

	public void setSkuPropertys(List<SkuProperty> skuPropertys) {
		this.skuPropertys = skuPropertys;
	}

	public Set<String> getComboIds() {
		return comboIds;
	}

	public void setComboIds(Set<String> comboIds) {
		this.comboIds = comboIds;
	}

	public PromotionCouponCodeCommand getCouponCodeOnLine() {
		return couponCodeOnLine;
	}

	public void setCouponCodeOnLine(PromotionCouponCodeCommand couponCodeOnLine) {
		this.couponCodeOnLine = couponCodeOnLine;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public Long getSettingId() {
		return settingId;
	}

	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}

	public Integer getGiftChoiceType() {
		return giftChoiceType;
	}

	public void setGiftChoiceType(Integer giftChoiceType) {
		this.giftChoiceType = giftChoiceType;
	}

	public Integer getGiftCountLimited() {
		return giftCountLimited;
	}

	public void setGiftCountLimited(Integer giftCountLimited) {
		this.giftCountLimited = giftCountLimited;
	}

	public Long getLineGroup() {
		return lineGroup;
	}

	public void setLineGroup(Long lineGroup) {
		this.lineGroup = lineGroup;
	}

	public List<PromotionCommand> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<PromotionCommand> promotionList) {
		this.promotionList = promotionList;
	}

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}

	public boolean isSuitLine() {
		return isSuitLine;
	}

	public void setSuitLine(boolean isSuitLine) {
		this.isSuitLine = isSuitLine;
	}

	public boolean isCaptionLine() {
		return isCaptionLine;
	}

	public void setCaptionLine(boolean isCaptionLine) {
		this.isCaptionLine = isCaptionLine;
	}

	public String getLineCaption() {
		return lineCaption;
	}

	public void setLineCaption(String lineCaption) {
		this.lineCaption = lineCaption;
	}
	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Map<String, Object> getCustomParamMap() {
		return customParamMap;
	}
	
	public void setCustomParamMap(Map<String, Object> customParamMap) {
		this.customParamMap = customParamMap;
	}

	public boolean isPresale() {
		return isPresale;
	}

	public void setPresale(boolean isPresale) {
		this.isPresale = isPresale;
	}

}
