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
package com.baozun.nebula.tools;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.utils.FileUtils;

import loxia.utils.FileUtil;

/**
 * 处理model的文档
 * @author Justin
 *
 */
public class ModelDoc {

	private List<String> split(String content)throws Exception{
		
		List<String> list=new ArrayList<String>();
		
		String[] strs=content.split(";");
		
		for(String str:strs){
			list.add(str);
		}
		
		return list;
		
	}
	
	private void processSingle(String content)throws Exception{
		
		String[] strs=content.split("private ");
		
		if(strs.length>1){
			String desc=strs[0].replaceAll("[/* \\t\\r\\n]", "");
			
			String[] strs2=strs[1].split(" ");
			
			String name=strs2[1].trim();
			String type=strs2[0].trim();
			
			System.out.println(name+"\t"+type+"\t"+desc);
		}
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String content=FileUtils.readFile("c:/text.txt","GBK");
		ModelDoc mc=new ModelDoc();
		
		List<String> list=mc.split(content);
		
		for(String str:list){
			mc.processSingle(str);
		}
		
	}

}
