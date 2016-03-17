package com.baozun.nebula.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.manager.system.UploadManager;

/**
 * 用于上传
 * @author Justin Hu
 *
 */
@Controller
public class UploadController extends BaseController {

	@Autowired
	private UploadManager uploadManager;
	
	/**
	 * 上传图片的根目录
	 */
	@Value("#{meta['upload.img.base']}")
	private  String uploadImgBase="";
	
	/**
	 * 上传图片的根临时目录
	 */
	@Value("#{meta['upload.img.base.tmp']}")
	private String	uploadImgBaseTmp	= "";
	
	@RequestMapping(value = "/demo/updateInit.htm")
	public String updateInit(){
		
		return "upload2";
	}
	

	private List<FileItem> processUploadFile(HttpServletRequest request){
		
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		FileUpload fileUpload = new ServletFileUpload(fileItemFactory);
		
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			
			return fileItems;
			
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value = "/demo/upload.json")
	@ResponseBody
	public Map<String, String> update(@RequestParam(value="role", required=false)String role,@RequestParam(value="fileToUpload",required=false) CommonsMultipartFile mFile, HttpServletRequest request){
		Calendar calendar = Calendar.getInstance();
		
		String userDefinedPath =calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH) ;
		String tmpPath = request.getContextPath()+uploadImgBaseTmp+userDefinedPath;
		String path = request.getContextPath()+uploadImgBase+userDefinedPath;
		
		Map<String, String>  resultMap = uploadManager.uploadImg(tmpPath, path, role, userDefinedPath, mFile );
		if(mFile.getSize()>0){
			return resultMap;
		}else{
			return null;
		}
		
	}

	@RequestMapping(value="/demo/uploadsave.json")
	@ResponseBody
	public String saveupload(@RequestParam("file1")String file1,@RequestParam("uname")String uname){
		
		return "success(img:"+file1+" text:"+uname+")";
	}
	
	private void writeFile(HttpServletRequest request,String path) throws Exception{
		BufferedInputStream fileIn = new BufferedInputStream(request.getInputStream());
		

		byte[] buf = new byte[1024];

		// 接收文件上传并保存到 d:\
		File file = new File(path);

		BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));

		while (true) {
			// 读取数据
			int bytesIn = fileIn.read(buf, 0, 1024);

			System.out.println(bytesIn);

			if (bytesIn == -1) {
				break;
			} else {
				fileOut.write(buf, 0, bytesIn);
			}
		}

		fileOut.flush();
		fileOut.close();
	}
}
