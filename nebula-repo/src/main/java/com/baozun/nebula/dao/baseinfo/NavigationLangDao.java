/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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

package com.baozun.nebula.dao.baseinfo;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.baseinfo.NavigationLang;

public interface NavigationLangDao extends GenericEntityDao<NavigationLang, Long> {
	
	@NativeQuery(clazzes = Integer.class,alias={"count"})
	int findNavigationLangByNameAndNavids(@QueryParam("name") String name,@QueryParam("lang") String lang,@QueryParam("nIds")List<Long> nIds);
	
	@NativeQuery(model = NavigationLang.class)
	NavigationLang findNavigationLangByNavidAndLang(@QueryParam("navigationId")Long navigationId,@QueryParam("lang") String lang);
	
	@NativeUpdate
	int updateNavigationLangByNavigationIdAndLang(@QueryParam("name") String name,@QueryParam("lang") String lang,@QueryParam("navigationId")Long navigationId);
	
	@NativeQuery(model = NavigationLang.class)
	List<NavigationLang> findNavigationLangListByNavidAndLangs(@QueryParam("navigationId")Long navigationId,@QueryParam("langs") List<String> lang);
	
	@NativeUpdate
	Integer deleteNavigationLangByNavigationIds(@QueryParam("navigationIds")List<Long> navigationId);

}
