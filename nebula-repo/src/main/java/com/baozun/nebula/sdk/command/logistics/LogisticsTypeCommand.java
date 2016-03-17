package com.baozun.nebula.sdk.command.logistics;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

public class LogisticsTypeCommand extends BaseModel  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -9064410318740218119L;

	private String typeCode;
	
	private String typeName;

	@RiskControl(RiskLevel.LOW)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	@RiskControl(RiskLevel.LOW)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
