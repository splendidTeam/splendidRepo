package com.baozun.nebula.sdk.command;

import java.util.Date;

import com.baozun.nebula.command.Command;

public class ActivityCardEntityIntCommand  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 944165374038024412L;

	private Long id;
	
	private String cardNo;
	
	private String cardPwd;
	
	private Date usedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardPwd() {
		return cardPwd;
	}

	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}
	
}
