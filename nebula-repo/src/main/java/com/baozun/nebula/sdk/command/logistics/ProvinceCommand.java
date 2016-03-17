package com.baozun.nebula.sdk.command.logistics;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

public class ProvinceCommand extends BaseModel  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5535906654221060482L;

	private Long provinceId;
	
	private String name;
	
	@RiskControl(RiskLevel.LOW)
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}	

	@RiskControl(RiskLevel.LOW)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
}
