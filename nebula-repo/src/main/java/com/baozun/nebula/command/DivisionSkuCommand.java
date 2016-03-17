package com.baozun.nebula.command;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Administrator
 * @version 1.0 2012-9-6 下午4:10:18
 */
public class DivisionSkuCommand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant log. */
	private static final Logger	log	= LoggerFactory.getLogger(DivisionSkuCommand.class);
	/**商品ID*/
	private Long id;	
	/**商品编码  */
	private String code;
	/**商品名称   */
	private String name;
	/**商品状态  */
	private String status;
	/**商品成本  */
	private BigDecimal fob;
	/**商品售价  */
	private BigDecimal price;
	
	@Column(name = "SKU_CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "FOB")
	public BigDecimal getFob() {
		return fob;
	}
	public void setFob(BigDecimal fob) {
		this.fob = fob;
	}
	
	@Column(name = "PRICE")
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}
