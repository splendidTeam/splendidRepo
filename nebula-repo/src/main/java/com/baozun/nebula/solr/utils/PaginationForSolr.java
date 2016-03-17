/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.solr.utils;

import java.util.ArrayList;
import java.util.List;

import loxia.dao.Pagination;

/**
 * @author Tianlong.Zhang
 * @param <T>
 *
 */
public class PaginationForSolr<T> extends Pagination<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5943929908831236583L;

	public List<T> getCurrentPageItem(){
		List<T> item = new ArrayList<T>();
		if(this.getCurrentPage()>this.getTotalPages()){
			return item;
//			this.setCurrentPage(this.getTotalPages());
		}
		if(this.getCurrentPage()<1){
			this.setCurrentPage(1);
		}
		
		int currentCount = this.getCurrentPage()*this.getSize();
		if(this.getItems().size()<currentCount){
			currentCount = this.getItems().size()-(this.getCurrentPage()-1)*this.getSize();
		}
		
		int end = this.getCurrentPage()*this.getSize();
		if(this.getItems().size()<end){
			end = this.getItems().size();
		}
		for(int i=(this.getCurrentPage()-1)*this.getSize();i<end;i++){
			item.add(this.getItems().get(i));
		}
		return item;
	}
	
}
