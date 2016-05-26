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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageTemplateManager;
import com.baozun.nebula.utils.image.ImageOpeartion;
import com.feilong.core.Validator;

/**
 * 图片上传的实现类
 * 
 * @author chenguang.zhou
 * @date 2014年4月1日 下午2:52:54
 */
@Service
@Transactional
public class UploadManagerImpl implements UploadManager {

	@Value("#{meta['upload.img.reduce.type']}")
	private String	REDUCE_TPYE			= "";

	/**
	 * 上传图片的根目录
	 */
	@Value("#{meta['upload.img.base']}")
	private String	uploadImgBase		= "";

	/**
	 * 上传图片的域名
	 */
	@Value("#{meta['upload.img.domain.base']}")
	private String	UPLOAD_IMG_DOMAIN	= "";

	/**
	 * 上传图片的根临时目录
	 */
	@Value("#{meta['upload.img.base.tmp']}")
	private String	uploadImgBaseTmp	= "";
	
	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;

	@Override
	public Map<String, String> upload(Map<String, String> tmpImgFilePathMap, String role, File file) {
		String distStr = "";
		String path = file.getParent();
		String distFileName = "";
		String fileName = file.getName();
		// 图片后缀
		String exp = ImageOpeartion.getExp(fileName);
		// 没有后缀的图片名称
		String picName = fileName.replace(exp, "");

		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();// 创建文件夹
		}

		if (Validator.isNotNullOrEmpty(role)) {
			String[] rules = role.split("\\|");
			for (String ru : rules) {
				distFileName = picName + "_" + ru + exp;
				distStr = path + "/" + distFileName;
				// 缩略图
				String[] strs = ru.split("X");
				ImageOpeartion.reduceImg(file, distStr, Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), REDUCE_TPYE);
				tmpImgFilePathMap.put(ru+picName, distFileName);
			}
		}
		return tmpImgFilePathMap;
	}

	@Override
	public Map<String, String> uploadImg(String tmpPath, String path, String role, String userDefinedPath, CommonsMultipartFile mFile) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> tmpImgFilePathMap = new HashMap<String, String>();
		String fileName = mFile.getFileItem().getName();
		String srcStr = "";
		String distFileName = "";
		String srcFileName = "";
		String picName = ImageOpeartion.getPicName();
		try {
			File tmpF = new File(tmpPath);
			if (!tmpF.exists()) {
				tmpF.mkdirs();// 创建文件夹
			}
			
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			// 后缀
			String exp = ImageOpeartion.getExp(fileName);

			srcFileName = picName + exp;

			srcStr = tmpPath + "/" + srcFileName;
			// 原图
			File file = new File(srcStr);
			mFile.getFileItem().write(file);
			// 原图片
			tmpImgFilePathMap.put("source"+picName, srcFileName);
			
			// 生成缩略图
			if (StringUtils.isNotBlank(role)) {
				tmpImgFilePathMap = upload(tmpImgFilePathMap, role, file);
				// 图片url
				String[] roles = role.split("\\|");
				String ru = roles[roles.length-1];
				distFileName = tmpImgFilePathMap.get(ru+picName);
			}else{
				distFileName = tmpImgFilePathMap.get("source"+picName);
			}
			
			
			
			BufferedImage bufferedImage = ImageIO.read(file);
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();

			resultMap.put("width", String.valueOf(width));
			resultMap.put("height", String.valueOf(height));
			if (Validator.isNotNullOrEmpty(role)) {
				resultMap.put("url", UPLOAD_IMG_DOMAIN + userDefinedPath + "/" + distFileName);
			} else {
				resultMap.put("url", UPLOAD_IMG_DOMAIN + userDefinedPath + "/" + srcFileName);
			}
			
			// copy image file 
			InputStream input = null;
			OutputStream output = null;
			for(Map.Entry<String, String> entry : tmpImgFilePathMap.entrySet()){
				File tmpFile = new File(tmpPath+File.separator+entry.getValue());
				input = new FileInputStream(tmpFile);
				output = new FileOutputStream(path+File.separator+entry.getValue());
				try {
					IOUtils.copy(input, output);
				} finally{
					input.close();
					output.close();
				}
				// delete image file
				tmpFile.delete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.UploadManager#uploadFile(java.lang.String, org.springframework.web.multipart.commons.CommonsMultipartFile)
	 */
	@Override
	public String uploadFileToString(CommonsMultipartFile mFile) {
		
		Map<String, String> extMap = new HashMap<String, String>();
		extMap.put("html", "html");
		extMap.put("HTML", "HTML");
		extMap.put("txt", "txt");
		extMap.put("TXT", "TXT");
		//检查上传的文件是不是纯文本文件(后缀是.txt, .html)
		String fileName = mFile.getFileItem().getName();
		if(StringUtils.isBlank(fileName)){
			throw new BusinessException(ErrorCodes.FILE_SUFFIX_ERROR);
		}
		// 文件后缀名
		String ext = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		if(StringUtils.isBlank(ext)){
			throw new BusinessException(ErrorCodes.FILE_SUFFIX_ERROR);
		}
		if(!extMap.containsKey(ext)){
			throw new BusinessException(ErrorCodes.FILE_SUFFIX_ERROR);
		}
		
		BufferedReader in =null;
		StringBuffer buffer = new StringBuffer();
		try {
			InputStream input = mFile.getInputStream();
			
			in = new BufferedReader(new InputStreamReader(input,"utf-8"));
			String line = "";
			while ((line = in.readLine()) != null){
			    buffer.append(line +"\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
		//处理utf8+dom编码方式导致jsoup解析错误的原因
		String data = dealUtf8Dom(buffer.toString());
		String html = sdkCmsPageTemplateManager.addTemplateBase(data);
		return html;
	}
	
	public String dealUtf8Dom(String str){
		StringBuilder  sb = new StringBuilder();
		for(int i=0;i<str.length();i++){
			if(i==0 && (int)str.charAt(i)==65279){
				continue;
			}
			sb.append(str.charAt(i));
		}
		return  sb.toString();
	}
}
