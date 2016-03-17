package com.baozun.nebula.solr.command;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SuggestCommand  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -544691033805105552L;
	
	private List<SuggestDetailCommand> suggestDetailCommand;
	
	private Long hitNum;

	public List<SuggestDetailCommand> getSuggestDetailCommand() {
		return suggestDetailCommand;
	}

	public void setSuggestDetailCommand(
			List<SuggestDetailCommand> suggestDetailCommand) {
		this.suggestDetailCommand = suggestDetailCommand;
	}

	public Long getHitNum() {
		return hitNum;
	}

	public void setHitNum(Long hitNum) {
		this.hitNum = hitNum;
	}

}
