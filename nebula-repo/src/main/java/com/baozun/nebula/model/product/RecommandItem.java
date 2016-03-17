package com.baozun.nebula.model.product;

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
 * 推荐商品
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_PD_REC_ITEM")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class RecommandItem extends BaseModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 9092638531254715288L;

	/**
	 * 类型为1时候，购物车推荐类型
	 */
	public static final Long	PARAM_SHOPPING_CART	= 1L;

	/**
	 * 类型为2时候，列表推荐类型
	 */
	public static final Long	PARAM_LIST			= 2L;

	/**
	 * 类型为3时候，详情页推荐类型
	 */
	public static final Long	PARAM_PDP			= 3L;

	/**
	 * 类型：　公共
	 */
	public static final Integer	TYPE_PUBLIC			= 1;

	/**
	 * 类型：　分类
	 */
	public static final Integer	TYPE_CATEGORY		= 2;

	/**
	 * 类型：　商品
	 */
	public static final Integer	TYPE_ITEM			= 3;

	/** PK. */
	private Long				id;

	/**
	 * 描述
	 */
	private String				description;

	/**
	 * 推荐类型 1.公共类型 param属性表示是哪一种公共推荐 类型有 ： 1 购物车相关 2 pdp 3热门 等 2.分类推荐 param属性表示是分类id 3.商品搭配 param属性表示需要搭配的商品id，
	 * 
	 * 如id为1的分类推荐的商品列表： type=2,param=1,itemId=352 type=2,param=1,itemId=236 type=2,param=1,itemId=186
	 * 表示353,236,186是推荐的商品,但通过id为1分类进行定位
	 */
	private Integer				type;

	/**
	 * 推荐参数
	 */
	private Long				param;

	/**
	 * 推荐的商品id
	 */
	private Long				itemId;

	/**
	 * 排序
	 */
	private Integer				sort;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** 最后操作者 */
	private Long				opeartorId;

	/**
	 * 生命周期 (1:有效;0:无效)
	 */
	private Integer				lifecycle;

	/** version. */
	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_REC_ITEM", sequenceName = "S_T_PD_REC_ITEM", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_REC_ITEM")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "PARAM")
	public Long getParam() {
		return param;
	}

	public void setParam(Long param) {
		this.param = param;
	}

	@Column(name = "ITEM_ID")
	@Index(name = "FIDX_ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "OPEARTOR_ID")
	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

}
