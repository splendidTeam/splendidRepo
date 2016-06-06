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
package com.baozun.nebula.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utils.image.ImageOpeartion;

/**
 * @author Tianlong.Zhang
 */
@Controller
public class ImageUpLoadContorller extends BaseController{

	private final String		IMG_ROLE			= "IMG_ROLE";

	private final String		DEFAULT_ROLE		= "250X300";

	@Autowired
	private ChooseOptionManager	chooseOptionManager;

	/**
	 * 上传图片的根目录
	 */
	@Value("#{meta['upload.img.base']}")
	private String				UPLOAD_IMG_BASE		= "";

	/**
	 * 上传图片的域名
	 */
	@Value("#{meta['upload.img.domain.base']}")
	private String				UPLOAD_IMG_DOMAIN	= "";

	/**
	 * 生成随机图片名
	 * 
	 * @return
	 */
	private String getPicName(){
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		sb.append(new Date().getTime()).append(random.nextInt(10000));
		return sb.toString();
	}

	@RequestMapping(value = "/img/upload.json")
	@ResponseBody
	public String update(
			@RequestParam(value = "CKEditorFuncNum") String CKEditorFuncNum,
			@RequestParam(value = "upload",required = false) CommonsMultipartFile mFile,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		List<ChooseOption> optList = chooseOptionManager.findEffectChooseOptionListByGroupCode(IMG_ROLE);
		String role = DEFAULT_ROLE;
		if (optList.size() > 0){
			ChooseOption co = optList.get(0);
			role = co.getOptionValue();
		}
		// 设置headers参数
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		Calendar calendar = Calendar.getInstance();
		String userDefinedPath = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String path = request.getContextPath() + UPLOAD_IMG_BASE + userDefinedPath;
		String fileName = mFile.getFileItem().getName();
		String srcStr = "";
		String distStr = "";
		String distFileName = "";
		String srcFileName = "";
		String picName = getPicName();

		try{
			File f = new File(path);
			if (!f.exists()){
				f.mkdirs();// 创建文件夹
			}
			// 后缀
			String exp = ImageOpeartion.getExp(fileName);
			distFileName = picName + "_" + role + exp;
			srcFileName = picName + exp;
			// retDistUrl += distFileName;
			distStr = path + "/" + distFileName;
			srcStr = path + "/" + srcFileName;
			// 原图
			File file = new File(srcStr);
			mFile.getFileItem().write(file);

			if (Validator.isNotNullOrEmpty(role)){
				// 缩略图
				String[] strs = role.split("X");
				ImageOpeartion.reduceImg(file, distStr, Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), "BK");
			}
			// repoFileHelper.newFile(path, mFile.getInputStream());
			// System.out.println("--------"+mFile.getSize());

		}catch (Exception e){
			e.printStackTrace();
		}
		String url = "";
		if (mFile.getSize() > 0){
			if (Validator.isNotNullOrEmpty(role)){

				url = UPLOAD_IMG_DOMAIN + userDefinedPath + "/" + distFileName;
			}else{
				url = UPLOAD_IMG_DOMAIN + userDefinedPath + "/" + srcFileName;
			}
		}else{
			url = UPLOAD_IMG_DOMAIN;
		}
		String value = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ", '" + url
				+ "', '');</script>";
		out.write(value);

		return null;
	}

}
