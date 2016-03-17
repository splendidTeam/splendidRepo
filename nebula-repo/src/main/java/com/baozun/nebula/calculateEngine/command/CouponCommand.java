package com.baozun.nebula.calculateEngine.command;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.baozun.nebula.command.Command;

public class CouponCommand implements Command {
  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8814846720727585626L;

	private Long id;
	
	private String couponCode;
	
    private String couponName;
	
    private Long couponType;
    
    public Long getCouponType() {
		return couponType;
	}
	
	public void setCouponType(Long couponType) {
		this.couponType = 	couponType;
	}
	
    public Long getId() {
		return id;
	}
	
	public void setId(Long idValue) {
		this.id = 	idValue;
	}
	
    public String getCouponCode() {
		return couponCode;
	}
	
	public void setCouponCode(String couponCode) {
		this.couponCode = 	couponCode;
	}
	
	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}	
}


  
    
    
    
    
