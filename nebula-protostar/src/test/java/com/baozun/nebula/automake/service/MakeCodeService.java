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
package com.baozun.nebula.automake.service;

import com.baozun.nebula.automake.model.MakeModel;

/**
 * 生成代码
 * sql-xml
 * dao
 * manager
 * @author Justin
 *
 */
public interface MakeCodeService {

	void makeDao(MakeModel makeModel,String generateOutDir);
	
	void makeManager(MakeModel makeModel,String generateOutDir);
	
	void makeSqlXml(MakeModel makeModel,String generateOutDir);
	
	void makeController(MakeModel makeModel,String generateOutDir);
	
	void makeJsp(MakeModel makeModel,String generateOutDir);
	void makeJs(MakeModel makeModel,String generateOutDir);
}
