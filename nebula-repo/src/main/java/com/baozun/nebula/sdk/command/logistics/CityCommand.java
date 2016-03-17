package com.baozun.nebula.sdk.command.logistics;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

public class CityCommand extends BaseModel  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2617885285040243737L;

	private Long cityId;
	
	private String name;
	
	private Long provinceId;

	
	@RiskControl(RiskLevel.LOW)
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


	@RiskControl(RiskLevel.LOW)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@RiskControl(RiskLevel.LOW)
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	
	
}
