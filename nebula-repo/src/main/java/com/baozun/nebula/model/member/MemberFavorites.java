package com.baozun.nebula.model.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 保存我的收藏的信息
 * 
 * @author qiang.yang
 * 
 */

@Entity
@Table(name = "T_MEMBER_FAVORITES")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberFavorites extends BaseModel {

	private static final long	serialVersionUID	= 6254509970428894341L;

	/** PK */
	private Long				id;
	
	/** 会员ID */
	private Long				memberId;
	
	/** 商品Id */
	private Long				itemId;

	/** 创建时间 */
	private Date				createDate;

	/** skuId */
	private Long				skuId;

	/** version. */
	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MEM_FAVORITES", sequenceName = "S_T_MEM_FAVORITES", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_MEM_FAVORITES")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MEMBER_ID")
    @Index(name = "IDX_MEMBER_FAVORITES_MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "ITEM_ID")
    @Index(name = "IDX_MEMBER_FAVORITES_ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "SKU_ID")
    @Index(name = "IDX_MEMBER_FAVORITES_SKU_ID")
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

}
