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
package com.baozun.nebula.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 处理工具类
 * @author Justin
 *
 */
public class FileUtils {

	/**
	 * 读取文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String path,String encode)throws Exception{
		
		BufferedReader br=new BufferedReader(new InputStreamReader( new FileInputStream(new File(path)),encode));
		
		String result="";
		
		String str;
		
		while((str=br.readLine())!=null){
			result+=str+"\r\n";
		}
		
		return result;
		
	}
}
