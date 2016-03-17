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

import com.baozun.nebula.automake.Constants;
import com.baozun.nebula.automake.GenerateMain;
import com.baozun.nebula.automake.model.MakeModel;
import com.baozun.nebula.automake.utils.BaseFreemarkUtils;

/**
 * @author Justin
 *
 */
public class MakeCodeServiceImpl implements MakeCodeService {


	private String queryTemplatePath(String fileName){
		String path=GenerateMain.class.getClassLoader().getResource("automake/"+fileName).getPath();
		return path;
	}
	

	
	@Override
	public void makeDao(MakeModel makeModel,String generateOutDir) {
		// TODO Auto-generated method stub
		BaseFreemarkUtils.generate(queryTemplatePath(Constants.DAO_TEMPLATE), 
				makeModel, 
				generateOutDir+Constants.DAO_FILE_START+makeModel.getEntityName()+"Dao.java"
				);
	}


	@Override
	public void makeManager(MakeModel makeModel,String generateOutDir) {
		// TODO Auto-generated method stub
		BaseFreemarkUtils.generate(queryTemplatePath(Constants.MANAGER_TEMPLATE), 
				makeModel, 
				generateOutDir+Constants.MANAGER_FILE_START+"Sdk"+makeModel.getEntityName()+"Manager.java"
				);
		
		BaseFreemarkUtils.generate(queryTemplatePath(Constants.MANAGER_IMPL_TEMPLATE), 
				makeModel, 
				generateOutDir+Constants.MANAGER_IMPL_FILE_START+"Sdk"+makeModel.getEntityName()+"ManagerImpl.java"
				);
	}

	@Override
	public void makeSqlXml(MakeModel makeModel,String generateOutDir) {
		BaseFreemarkUtils.generate(queryTemplatePath(Constants.XML_TEMPLATE), 
				makeModel, 
				generateOutDir+Constants.XML_FILE_START+makeModel.getPackagName()+"-"+makeModel.getEntityName().toLowerCase()+".xml"
				);
	}
	
	@Override
	public void makeController(MakeModel makeModel, String generateOutDir) {
		BaseFreemarkUtils.generate(queryTemplatePath(Constants.CONTROLLER_TEMPLATE), 
				makeModel, 
				generateOutDir+Constants.CONTROLLER_FILE_START+makeModel.getEntityName()+"Controller.java"
				);
	}

	@Override
	public void makeJsp(MakeModel makeModel, String generateOutDir) {
		String filename = makeModel.getEntityName();
		filename = filename.replaceFirst(filename.substring(0, 1), filename.substring(0, 1).toLowerCase());
		BaseFreemarkUtils.generate(queryTemplatePath("jsp_template.fl"), 
				makeModel, 
				generateOutDir+"jsp/"+filename+".jsp"
			);
	}

	@Override
	public void makeJs(MakeModel makeModel, String generateOutDir) {
		String filename = makeModel.getEntityName();
		filename = filename.replaceFirst(filename.substring(0, 1), filename.substring(0, 1).toLowerCase());
		BaseFreemarkUtils.generate(queryTemplatePath("js_template.fl"), 
				makeModel, 
				generateOutDir+"js/"+filename+".js"
			);
	}

}
