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
package com.baozun.nebula.automake;

import com.baozun.nebula.automake.model.MakeModel;
import com.baozun.nebula.automake.service.MakeCodeService;
import com.baozun.nebula.automake.service.MakeCodeServiceImpl;
import com.baozun.nebula.automake.service.MakeModelService;
import com.baozun.nebula.automake.service.MakeModelServiceImpl;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * @author Justin
 *
 */
public class GenerateMain {


	/**
	 * 生成的实体
	 */
	private final Class modelClass=ChooseOption.class;
	
	
	/**
	 * 作者名
	 */
	private final String authName="Justin";
	/**
	 * 生命周期字段,如果没有则为""空串
	 */
	private final String lifecycle="lifecycle";
	
	/**
	 * 是否拥有删除状态,拥有删除状态则会在所有的查询后加上lifecycle!=2
	 */
	private final Boolean hasDeleteLifecycle=true;
	
	private String getAutoMakeCode(){
		
		return System.getProperty("user.dir")+"/target/makecode/";
	}
	
	private void execute(String[] args) throws Exception{
		
		if(args==null||args.length==0){
			args=new String[4];
			args[0]=modelClass.getName();
			args[1]=authName;
			args[2]=lifecycle;
			args[3]=String.valueOf(hasDeleteLifecycle);
		}
		
		MakeModelService mmService=new MakeModelServiceImpl();
		
		Class clazz=Class.forName(args[0]);
		
		MakeModel mm=mmService.queryByClass(clazz);
		
		
		mm.setAuthName(args[1]);
		if(args[2]==null||"null".equals(args[2])){
			mm.setLifecycle("");
		}else{
			mm.setLifecycle(args[2]);
		}
		
		Boolean hasDeleteLifecycle=new Boolean(args[3]);
		mm.setHasDeleteLifecycle(hasDeleteLifecycle);
		
		MakeCodeService mcs=new MakeCodeServiceImpl();
		
		mcs.makeSqlXml(mm,getAutoMakeCode());
		
		mcs.makeDao(mm,getAutoMakeCode());
		
		mcs.makeManager(mm,getAutoMakeCode());
		// 导出controller文件
		mcs.makeController(mm, getAutoMakeCode());
		// 导出jsp文件
		mcs.makeJsp(mm, getAutoMakeCode());
		// 导出js文件
		mcs.makeJs(mm, getAutoMakeCode());
		System.out.println(args[0]+args[1]+args[2]+args[3]);
	}
	
	public static void main(String[] args)throws Exception{
		
		GenerateMain gm=new GenerateMain();
		args = new String[5];
		args[0]="com.baozun.nebula.model.SkuGiftType";
		args[1]="chenguang.zhou";
		args[2]="lifecycle";
		args[3]="true";
		gm.execute(args);
		
		
	}
}
