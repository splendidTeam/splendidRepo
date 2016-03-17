/**
 * 
 */
package com.baozun.nebula.command.sales;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.baozun.nebula.command.Command;

/**
 * @author xianze.zhang
 *@creattime 2013-11-27
 */
public class ShoppingCartCommand implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2271872183319143956L;
		// 活动对象，封装了所有活动信息
//		private SalesActivityBean activityBean = new SalesActivityBean();
		// SKU列表（正常商品，活动商品 ，赠品）
		private List<ShoppingCartLineCommand> skuList = new ArrayList<ShoppingCartLineCommand>();
		/**
		 * 购物车头赠品列表。
		 * 1、不需要用户挑选类的赠品。例如：满200元赠送某1个或多个赠品
		 * 那么GiftBean的maxNum需要置为-1
		 * 2、需要用户挑选类的赠品。例如：满200元赠送多个赠品，但是只能挑选1个或其中几个。
		 * 那么GiftBean的maxNum需要置为最大可挑选数
		 * 3、多种情况同时都有。
		 * 那么会对应有多个giftBean
		 * 
		 */
		private List<GiftBean> giftList = new ArrayList<GiftBean>();

		private Integer skuQuantity;

		// 会员信息（含渠道信息）
		private Long memberId;

		// 订单折扣
		private BigDecimal discountAmount = BigDecimal.ZERO;

		// 原运费
		private BigDecimal originalFreight = BigDecimal.ZERO;

		// 现运费
		private BigDecimal currentFreight = BigDecimal.ZERO;

		private String cardNo;

		public BigDecimal getOriginalAmount() {
			return calOriginalAmount();
		}

		public BigDecimal getActivityAmount() {
			return calActivityAmount();
		}

		public BigDecimal getActualAmount() {
			return calActualAmount();
		}

		// --------------------------- //
		// 原总价
		public BigDecimal calOriginalAmount() {
			BigDecimal amount = BigDecimal.ZERO;
			for (ShoppingCartLineCommand sku : skuList) {
				amount = amount.add(sku.calOriginalTotalAmount());
			}
			amount = amount.setScale(2, RoundingMode.HALF_UP);
			return amount.doubleValue() > 0 ? amount : BigDecimal.ZERO;
		}

		// 活动价
		public BigDecimal calActivityAmount() {
			BigDecimal amount = BigDecimal.ZERO;
			for (ShoppingCartLineCommand sku : skuList) {
				amount = amount.add(sku.calActivityTotalAmount());
			}
			amount = amount.setScale(2, RoundingMode.HALF_UP);
			return amount.doubleValue() > 0 ? amount : BigDecimal.ZERO;
		}

		// 实际需要支付的金额
		public BigDecimal calActualAmount() {
			BigDecimal amount = BigDecimal.ZERO;
			amount = amount.add(calActivityAmount()); // +商品价格
			// amount = amount.subtract(cardAmount); // -卡券抵扣
			amount = amount.subtract(discountAmount); // -购物车抵扣
			amount = amount.setScale(2, RoundingMode.HALF_UP);
			amount= amount.doubleValue() > 0 ? amount : BigDecimal.ZERO;
			amount = amount.add(currentFreight); // +运费
			return amount;
		}
		
		
		// 实际需要支付的金额
		public BigDecimal calActualAmountWithoutFreight() {
			return this.calActualAmount().subtract(currentFreight);
		}

		// 商品数量
		public int calQuantity() {
			int num = 0;
			for (ShoppingCartLineCommand sku : skuList) {
				num += sku.getQuantity();
			}
			return num;
		}

		// 获得某sku的数量（含活动/非活动）
		public int calSkuQuantity(String extentionCode) {
			if (null == extentionCode) {
				return 0;
			}
			int num = 0;
			for (ShoppingCartLineCommand sku : skuList) {
				if (sku.getExtentionCode().equals(extentionCode)) {
					num += sku.getQuantity();
				}
			}
			return num;
		}


		public Integer getSkuQuantity() {
			return calQuantity();
		}

		
		public List<ShoppingCartLineCommand> getSkuList(){
			return skuList;
		}

		
		public void setSkuList(List<ShoppingCartLineCommand> skuList){
			this.skuList = skuList;
		}

		
		public List<GiftBean> getGiftList(){
			return giftList;
		}

		
		public void setGiftList(List<GiftBean> giftList){
			this.giftList = giftList;
		}

		public Long getMemberId(){
			return memberId;
		}

		
		public void setMemberId(Long memberId){
			this.memberId = memberId;
		}

		
		public BigDecimal getDiscountAmount(){
			return discountAmount;
		}

		
		public void setDiscountAmount(BigDecimal discountAmount){
			this.discountAmount = discountAmount;
		}

		
		public BigDecimal getOriginalFreight(){
			return originalFreight;
		}

		
		public void setOriginalFreight(BigDecimal originalFreight){
			this.originalFreight = originalFreight;
		}

		
		public BigDecimal getCurrentFreight(){
			return currentFreight;
		}

		
		public void setCurrentFreight(BigDecimal currentFreight){
			this.currentFreight = currentFreight;
		}

		
		public String getCardNo(){
			return cardNo;
		}

		
		public void setCardNo(String cardNo){
			this.cardNo = cardNo;
		}

//		public SalesActivityBean getActivityBean() {
//			return activityBean;
//		}
//
//		public void setActivityBean(SalesActivityBean activityBean) {
//			this.activityBean = activityBean;
//		}

}
