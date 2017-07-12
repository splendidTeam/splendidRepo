package com.baozun.nebula.command;

import java.util.List;

import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;

public class ReturnApplicationCommand {
	
	private ReturnApplication returnApplication;

	private List<ReturnApplicationLine> returnLineList;
	
	private ReturnApplicationDeliveryInfo returnApplicationDeliveryInfo;

	public ReturnApplicationDeliveryInfo getSoReturnApplicationDeliveryInfo() {
		return returnApplicationDeliveryInfo;
	}


	public void setSoReturnApplicationDeliveryInfo(
			ReturnApplicationDeliveryInfo soReturnApplicationDeliveryInfo) {
		this.returnApplicationDeliveryInfo = soReturnApplicationDeliveryInfo;
	}


	public ReturnApplication getReturnApplication() {
		return returnApplication;
	}


	public void setReturnApplication(ReturnApplication returnApplication) {
		this.returnApplication = returnApplication;
	}


	public List<ReturnApplicationLine> getReturnLineList() {
		return returnLineList;
	}


	public void setReturnLineList(List<ReturnApplicationLine> returnLineList) {
		this.returnLineList = returnLineList;
	}

	
	
}
