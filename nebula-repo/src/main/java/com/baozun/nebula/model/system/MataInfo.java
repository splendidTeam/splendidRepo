/**
 * 
 */
package com.baozun.nebula.model.system;

import java.io.Serializable;
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

/**
 * @author xianze.zhang
 *@creattime 2013-6-17
 */
@Entity
@Table(name = "T_SYS_MATA_INFO")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MataInfo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2173055594374651709L;
	
	/**
	 * 是否有款型
	 * 如果有款型，列表页会将相同style的商品分组查询
	 */
	public static final String KEY_HAS_STYLE = "hasStyle";
	
	/**
	 * 是否有缓存
	 * 如果有缓存，则缓存才会有效
	 */
	public static final String KEY_HAS_CACHE="hasCache";
	public static final String KEY_ORDER_EMAIL = "orderEmail";
	public static final String PAY_URL_PREFIX ="payUrlPrefix";

	/**
	 * 单笔支付查询开关
	 */
	public static final String PAYMENT_INFO = "paymentInfo";

	public static final String PAYMENT_EXPIRY_TIME = "payExpiryTime";
	
	/**
	 * 返回价格，数量，是否包含未进行促销的行,TRUE以及FALSE，默认为FALSE
	 * 这个暂无效
	 */
	public static final String KEY_SC_RETURN_PRICE_NUM_SUM="SC_RETURN_PRICE_NUM_SUM";
	
	/**
	 * 哪些商品应该做为促销进行计算 ALL、CHECKED、AVAILABLE，默认为CHECKED
	 */
	public static final String KEY_SC_CALC_LEVEL="SC_CALC_LEVEL";
	
	/**
	 * 此商城是否启用个人信息加密
	 * true或false
	 * 默认为false
	 */
	public static final String KEY_NEED_ENCRYPT_PERSON="need_encrypt_person";
	
	/**
	 * 默认的商品图片上传方式, 可选值为：add和replace <br>
	 * add:添加导入<br>
	 * replace：替换导入<br>
	 * 如果不配置该选项，默认为添加导入
	 */
	public static final String KEY_DEFAULT_ITEM_IMAGE_UPLOAD_TYPE = "default_item_image_upload_type";
	
	/**
	 * PTS的商品列表页面商品分类显示方式，可选值为: code, name <br>
	 * code:显示分类编码<br>
	 * name:显示分类名称<br>
	 * 如果不配置该选项，默认为name
	 */
	public static final String KEY_PTS_ITEM_LIST_PAGE_CATEGORYNAME_MODE = "pts_item_list_page_categoryname_mode";
	
	/** 
     * 系统参数开关 <p>
     * true	   ：每次上架时更新上架时间<p>
     * 没有设置  ：每次上架时更新上架时间<p>
     * false ：只记录首次上架时间
     */
	public static final String UPDATE_ITEM_LISTTIME = "update_item_listtime";
	
	/** 
     * 系统参数开关 <p>
     * true	   ：批量新建导入模板字段内容根据id排序<p>
     * 没有设置  ：批量新建导入模板字段内容不排序<p>
     * false ：批量新建导入模板字段内容不排序
     */
	public static final String PD_UPLOAD_TEMPLATE_FIELD_SORT_BY_ID = "pd_upload_template_field_sort_by_id";
	
	/**
	 * 通过正则控制商品编号的格式<p>
	 * 如果配置了该参数那么使用验证，否则不使用验证
	 */
	public static final String PD_VALID_CODE = "pd_valid_code";
	
	
	/***
	 * qs订单开关
	 */
	public static final String KEY_QS_QUEUE_SWITCH = "qs_queue_switch";
	
	/**
	 * 用于标识pts多销售属性的情况下，允许普通商品的销售属性非必填的开关，如有需要请配置value=1
	 */
	public static final String 	SALES_OF_PROPERTY_IS_NOT_REQUIRED = "salesOfPropertyIsNotRequired";
	
	/**
	 * PK
	 */
	private Long				id;
	/**
	 * 参数code
	 */
	private String code;
	/**
	 * 参数值
	 */
	private String value;
	/**
	 * 生命周期
	 */
	private Integer 			lifecycle;
	
	/**
	 * VERSION
	 */
	private Date 				version;
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_MATA_INFO",sequenceName = "S_T_SYS_MATA_INFO",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_MATA_INFO")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "CODE")
    @Index(name = "IDX_MATA_INFO_CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "VALUE",length = 4000)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_MATA_INFO_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
}
