/**
 * 
 */
package com.baozun.nebula.model.salesorder;

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
 * 换货出库配送信息表<br>
 * @author yaohua.wang@baozun.cn
 *
 */
@Entity
@Table(name = "T_SO_RETURN_APPLICATION_DELIVERYINFO")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SoReturnApplicationDeliveryInfo extends BaseModel{

    /**
     * 
     */
    private static final long serialVersionUID = -4772766042029836901L;

    /**
     * PK
     */
    private Long id;

    /**
     * 退换货申请Id
     */
    private Long retrunApplicationId;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 镇.
     */
    private String town;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收件人号码
     */
    private String receiverPhone;

    /**
     * 收货人手机
     */
    private String receiverMobile;

    /**
     * 物流商编号
     */
    private String transName;

    /**
     * 物流单号
     */
    private String transCode;

    /**
     * 备注 明细信息字符串
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * @return the id
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SO_RETURN_APPLICATION_DELIVERYINFO",sequenceName = "S_T_SO_RETURN_APPLICATION_DELIVERYINFO",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_RETURN_APPLICATION_DELIVERYINFO")
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
     * @return the retrunApplicationId
     */
    @Column(name = "RETURNAPPLICATION_ID")
    public Long getRetrunApplicationId(){
        return retrunApplicationId;
    }

    /**
     * @param retrunApplicationId
     *            the retrunApplicationId to set
     */
    public void setRetrunApplicationId(Long retrunApplicationId){
        this.retrunApplicationId = retrunApplicationId;
    }

    /**
     * @return the country
     */
    @Column(name = "COUNTRY")
    public String getCountry(){
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(String country){
        this.country = country;
    }

    /**
     * @return the province
     */
    @Column(name = "PROVINCE")
    public String getProvince(){
        return province;
    }

    /**
     * @param province
     *            the province to set
     */
    public void setProvince(String province){
        this.province = province;
    }

    /**
     * @return the city
     */
    @Column(name = "CITY")
    public String getCity(){
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city){
        this.city = city;
    }

    /**
     * @return the district
     */
    @Column(name = "DISTRICT")
    public String getDistrict(){
        return district;
    }

    /**
     * @param district
     *            the district to set
     */
    public void setDistrict(String district){
        this.district = district;
    }

    /**
     * @return the town
     */
    @Column(name = "TOWN")
    public String getTown(){
        return town;
    }

    /**
     * @param town
     *            the town to set
     */
    public void setTown(String town){
        this.town = town;
    }

    /**
     * @return the address
     */
    @Column(name = "ADDRESS")
    public String getAddress(){
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * @return the zipcode
     */
    @Column(name = "ZIPCODE")
    public String getZipcode(){
        return zipcode;
    }

    /**
     * @param zipcode
     *            the zipcode to set
     */
    public void setZipcode(String zipcode){
        this.zipcode = zipcode;
    }

    /**
     * @return the receiver
     */
    @Column(name = "RECEIVER")
    public String getReceiver(){
        return receiver;
    }

    /**
     * @param receiver
     *            the receiver to set
     */
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    /**
     * @return the receiverPhone
     */
    @Column(name = "RECEIVERPHONE")
    public String getReceiverPhone(){
        return receiverPhone;
    }

    /**
     * @param receiverPhone
     *            the receiverPhone to set
     */
    public void setReceiverPhone(String receiverPhone){
        this.receiverPhone = receiverPhone;
    }

    /**
     * @return the receiverMobile
     */
    @Column(name = "RECEIVERMOBILE")
    public String getReceiverMobile(){
        return receiverMobile;
    }

    /**
     * @param receiverMobile
     *            the receiverMobile to set
     */
    public void setReceiverMobile(String receiverMobile){
        this.receiverMobile = receiverMobile;
    }

    /**
     * @return the transName
     */
    @Column(name = "TRANS_NAME")
    public String getTransName(){
        return transName;
    }

    /**
     * @param transName
     *            the transName to set
     */
    public void setTransName(String transName){
        this.transName = transName;
    }

    /**
     * @return the transCode
     */
    @Column(name = "TRANS_CODE")
    public String getTransCode(){
        return transCode;
    }

    /**
     * @param transCode
     *            the transCode to set
     */
    public void setTransCode(String transCode){
        this.transCode = transCode;
    }

    /**
     * @return the description
     */
    @Column(name = "DESCRIPTION")
    public String getDescription(){
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description){
        this.description = description;
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

}
