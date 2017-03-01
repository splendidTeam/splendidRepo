package com.baozun.nebula.sdk.command.shoppingcart;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.command.Command;

public class CookieShoppingCartLine implements Command{

    private static final long serialVersionUID = -6854832037537271539L;

    /**
     * add by feilong 2016年4月27日21:43:44
     */
    private Long id;

    private String extentionCode;

    /** 商品id */
    private Long skuId;

    /** 商品数量 */
    private Integer quantity;

    /** 会员id */
    // private String guestIndentify;

    /** 加入时间 */
    private Date createTime;
    // ,itemId

    /** 选中状态 **/
    private Integer settlementState;

    /** 店铺id **/
    private Long shopId;

    /**
     * 是否赠品
     */
    private Boolean isGift;

    /** 促销号 */
    private Long promotionId;

    /** 行分组 **/
    private Long lineGroup;

    /**
     * 对应的包装信息.
     * 
     * @since 5.3.2.11-Personalise
     */
    private List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList;

    public String getExtentionCode(){
        return extentionCode;
    }

    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    public Long getSkuId(){
        return skuId;
    }

    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public Date getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Integer getSettlementState(){
        return settlementState;
    }

    public void setSettlementState(Integer settlementState){
        this.settlementState = settlementState;
    }

    public Long getShopId(){
        return shopId;
    }

    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    public Boolean getIsGift(){
        return isGift;
    }

    public void setIsGift(Boolean isGift){
        this.isGift = isGift;
    }

    public Long getPromotionId(){
        return promotionId;
    }

    public void setPromotionId(Long promotionId){
        this.promotionId = promotionId;
    }

    public Long getLineGroup(){
        return lineGroup;
    }

    public void setLineGroup(Long lineGroup){
        this.lineGroup = lineGroup;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 对应的包装信息.
     *
     * @return the shoppingCartLinePackageInfoCommandList
     * @since 5.3.2.11-Personalise
     */
    public List<ShoppingCartLinePackageInfoCommand> getShoppingCartLinePackageInfoCommandList(){
        return shoppingCartLinePackageInfoCommandList;
    }

    /**
     * 设置 对应的包装信息.
     *
     * @param shoppingCartLinePackageInfoCommandList
     *            the shoppingCartLinePackageInfoCommandList to set
     * 
     * @since 5.3.2.11-Personalise
     */
    public void setShoppingCartLinePackageInfoCommandList(List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList){
        this.shoppingCartLinePackageInfoCommandList = shoppingCartLinePackageInfoCommandList;
    }

}
