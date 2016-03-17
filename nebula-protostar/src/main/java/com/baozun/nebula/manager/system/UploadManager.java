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
 */
package com.baozun.nebula.manager.system;

import java.io.File;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 上传功能
 * 
 * @author chenguang.zhou
 * @date 2014年4月1日 下午2:49:35
 */
public interface UploadManager {

	/**
	 * 图片上传
	 * @param role
			:图片尺寸
	 * @param file
			:上传的文件
	 * @return	:图片路径(key: imgFilePath)
	 */
	public Map<String, String> upload(Map<String, String> tmpImgFilePathMap, String role, File file);
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> uploadImg(String tmpPath, String path, String role, String userDefinedPath, CommonsMultipartFile mFile);
	
	
	/**
	 * 读取文件
	 * @return
	 */
	public String uploadFileToString(CommonsMultipartFile mFile);

}
