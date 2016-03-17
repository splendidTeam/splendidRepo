package com.baozun.nebula.command;

import java.util.Date;
/***
 * 已评论的历史记录
 * @author qiang.yang
 * @createtime 2013-12-24 AM 11:04
 *
 */
public class CommentLogCommand implements Command{

	private static final long serialVersionUID = -2571374442337795125L;
	/**PK*/
	private Long id;
	/**订单id*/
	private Long orderId;
	/**评论几颗星*/
	private Long starLevel;
	/**评论内容*/
	private String content;
	/**评论标题*/
	private String title;
	/**会员Id*/
	private Long memberId;
	/**商品的款Id*/
	private Long itemId;
	/**具体到颜色尺寸的SKU id*/
	private Long skuId;
	/**评论的状态   0代表审核中   1代表 通过  2代表未通过*/
	private Long status;
	/**商品名称*/
	private String itemName;
	/**商品图片*/
	private String picUrl;	
	/**操作员Id 代表谁审核通过*/
	private Long operatorId;	
	/**创建时间*/
	private Date createTime;
	/**未审核通过的原因*/
	private String reason;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(Long starLevel) {
		this.starLevel = starLevel;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	

}
