package com.baozun.nebula.sdk.command.logistics;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

public class AreaCommand extends BaseModel implements Command {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8944334613530586960L;

	private Long areaId;
	
	private String name;
	
	private Long cityId;

	
	@RiskControl(RiskLevel.LOW)
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}


	@RiskControl(RiskLevel.LOW)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@RiskControl(RiskLevel.LOW)
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
}
