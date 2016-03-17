package com.baozun.nebula.web.command;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.sdk.command.CancelOrderCommand;

public class CancelApplicationCommand extends CancelOrderCommand implements Command{

	private static final long serialVersionUID = -963193208855803974L;
	/**状态*/
	private String statusInfo;


	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	
	
	
	
	

}
