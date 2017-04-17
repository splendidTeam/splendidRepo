package com.baozun.nebula.model.salesorder;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 退换货商品行表<br>
 * @author yaohua.wang@baozun.cn
 * 
 */
@Entity
@Table(name = "T_SO_RETURN_APPLICATION_LINE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SoReturnApplicationLine extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3411890079966239231L;

    /**
     * PK
     */
    private Long id;

    /**
     * 退换货申请单ID
     */
    private Long returnApplicationId;

    /**
     * SoLine(平台退货订单行)ID
     * 原始订单行ID哪怕是多次换货
     */
    private Long soLineId;

    /**
     * 退换订单行类型
     */
    private Integer type;

    /**
     * 退回商品外部编码
     */
    private String rtnExtentionCode;

    /**
     * 换货商品外部编码.当[退换货申请类型]为[换货申请]时，则该信息非空
     */
    private String chgExtentionCode;

    /**
     * 退换货商品数量
     */
    private int qty;

    /**
     * 退回原因
     */
    private String returnReason;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 图片名,质量问题退换时上传的图片名称，图片名称自动生成
     */
    private String descriptionImage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退货单商品行的退款价
     */
    private BigDecimal returnPrice;

    /**
     * @return the id
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SO_RETURN_APPLICATION_LINE",sequenceName = "S_T_SO_RETURN_APPLICATION_LINE",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_RETURN_APPLICATION_LINE")
    public Long getId(){
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return the returnApplicationId
     */
    @Column(name = "RETURNAPPLICATION_ID")
    public Long getReturnApplicationId(){
        return returnApplicationId;
    }

    /**
     * @param returnApplicationId
     *            the returnApplicationId to set
     */
    public void setReturnApplicationId(Long returnApplicationId){
        this.returnApplicationId = returnApplicationId;
    }

    /**
     * @return the soLineId
     */
    @Column(name = "SOLINE_ID")
    public Long getSoLineId(){
        return soLineId;
    }

    /**
     * @param soLineId
     *            the soLineId to set
     */
    public void setSoLineId(Long soLineId){
        this.soLineId = soLineId;
    }

    /**
     * @return the type
     */
    @Column(name = "TYPE")
    public Integer getType(){
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * @return the rtnExtentionCode
     */
    @Column(name = "RTNEXTENTIONCODE")
    public String getRtnExtentionCode(){
        return rtnExtentionCode;
    }

    /**
     * @param rtnExtentionCode
     *            the rtnExtentionCode to set
     */
    public void setRtnExtentionCode(String rtnExtentionCode){
        this.rtnExtentionCode = rtnExtentionCode;
    }

    /**
     * @return the chgExtentionCode
     */
    @Column(name = "CHGEXTENTIONCODE")
    public String getChgExtentionCode(){
        return chgExtentionCode;
    }

    /**
     * @param chgExtentionCode
     *            the chgExtentionCode to set
     */
    public void setChgExtentionCode(String chgExtentionCode){
        this.chgExtentionCode = chgExtentionCode;
    }

    /**
     * @return the qty
     */
    @Column(name = "QTY")
    public int getQty(){
        return qty;
    }

    /**
     * @param qty
     *            the qty to set
     */
    public void setQty(int qty){
        this.qty = qty;
    }

    /**
     * @return the returnReason
     */
    @Column(name = "RETURN_REASON")
    public String getReturnReason(){
        return returnReason;
    }

    /**
     * @param returnReason
     *            the returnReason to set
     */
    public void setReturnReason(String returnReason){
        this.returnReason = returnReason;
    }

    /**
     * @return the createTime
     */
    @Column(name = "CREATETIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * @return the descriptionImage
     */
    @Column(name = "DESCRIPTION_IMAGE")
    public String getDescriptionImage(){
        return descriptionImage;
    }

    /**
     * @param descriptionImage
     *            the descriptionImage to set
     */
    public void setDescriptionImage(String descriptionImage){
        this.descriptionImage = descriptionImage;
    }

    /**
     * @return the remark
     */
    @Column(name = "remark")
    public String getRemark(){
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark){
        this.remark = remark;
    }

    /**
     * @return the returnPrice
     */
    @Column(name = "RETURN_PRICE")
    public BigDecimal getReturnPrice(){
        return returnPrice;
    }

    /**
     * @param returnPrice
     *            the returnPrice to set
     */
    public void setReturnPrice(BigDecimal returnPrice){
        this.returnPrice = returnPrice;
    }

}
