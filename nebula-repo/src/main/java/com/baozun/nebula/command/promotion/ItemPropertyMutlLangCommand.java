package com.baozun.nebula.command.promotion;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.i18n.LangProperty;

public class ItemPropertyMutlLangCommand implements Command {
	
	private static final long serialVersionUID = 1736979900016754100L;
	
	private String itemId;
	
	private String id;
	
	/**property_id**/
	private Long pId;
	
	/** property_value **/
	private LangProperty value;
	
	/** property_name **/
	private String pName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the pId
	 */
	public Long getpId() {
		return pId;
	}

	/**
	 * @param pId the pId to set
	 */
	public void setpId(Long pId) {
		this.pId = pId;
	}

	/**
	 * @return the value
	 */
	public LangProperty getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(LangProperty value) {
		this.value = value;
	}

	/**
	 * @return the pName
	 */
	public String getpName() {
		return pName;
	}

	/**
	 * @param pName the pName to set
	 */
	public void setpName(String pName) {
		this.pName = pName;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}
}

