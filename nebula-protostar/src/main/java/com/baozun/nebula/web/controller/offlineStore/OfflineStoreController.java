package com.baozun.nebula.web.controller.offlineStore;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import com.baozun.nebula.manager.offlineStore.OfflineStoreManager;
import com.baozun.nebula.model.offlineStore.OfflineStore;
import com.feilong.core.Validator;

@Controller
public class OfflineStoreController {
	
	/**
	 * 上传图片的根目录
	 */
	@Value("#{meta['upload.img.base']}")
	private  String UPLOAD_IMG_BASE="";
	
//	/**
//	 * 上传图片的目录
//	 */
//	@Value("#{image['bannerImageUploadPath']}")
//	private String				bannerImageUploadPaths			= "";
	
//	/**
//	 * 上传图片的目录
//	 */
//	@Value("#{image['banner.delete.shell']}")
//	private String				bannerDeleteShell			= "";
	
	
	private static final Logger log = LoggerFactory
			.getLogger(OfflineStoreController.class);
	
	
	@Resource
	private OfflineStoreManager offlineStoreManager;
	
	/**
	 * 店铺列表页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return /offlineStore/store.htm
	 */
	@RequestMapping("/offlineStore/store.htm")
	public String store(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return "/offlineStore/store_new";
	}
	
	/**
	 * 新增店铺页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/offlineStore/toAddStore.htm")
	public String toAddStore(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("type", "add");
		return "/offlineStore/updateStore_new";
	}
	
	/**
	 * 修改店铺页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/offlineStore/toUpdateStore.htm")
	public String toUpdateStore(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("id", Long.parseLong(request.getParameter("id")));
		OfflineStore sl = offlineStoreManager.findOfflineStoreById(params);
		model.addAttribute("store", sl);
		model.addAttribute("type", "update");
		return "/offlineStore/updateStore_new";
	}
	
	/**
	 * 修改店铺页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/offlineStore/deleteStore.json")
	public String deleteStore(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		int result = offlineStoreManager.deleteStore(Long.parseLong(request.getParameter("id")));
		response.getWriter().print(result);
		return null;
	}
	
	/**
	 * 新增店铺
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/offlineStore/addStore.json")
	public String addStore(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", request.getParameter("detail_name"));
		params.put("ename", request.getParameter("detail_ename"));
		params.put("full_address", request.getParameter("detail_full_address"));
		params.put("address", request.getParameter("detail_address"));
		params.put("ename1", request.getParameter("detail_ename1"));
		params.put("ename2", request.getParameter("detail_ename2"));
		params.put("province", request.getParameter("detail_province"));
		params.put("city", request.getParameter("detail_city"));
		params.put("district", request.getParameter("_district"));
		params.put("extension", request.getParameter("detail_extension"));
		params.put("storeImage", request.getParameter("storeImagePath"));
		params.put("mapImage", request.getParameter("mapImagePath"));
		params.put("phone", request.getParameter("detail_phone"));
		params.put("hours", request.getParameter("detail_hours"));
		params.put("postcode", request.getParameter("detail_postcode"));
		
		int result = offlineStoreManager.insertStore(params);
		if (result == 1) {
			/**
			 * 上传banner图片的目录
			 */
			String	bannerImageUploadPaths	= UPLOAD_IMG_BASE+"/resources/banner/cn/";
			deleteImageNotExists(bannerImageUploadPaths + "storeImage/");
//			execShell(bannerImageUploadPaths+"storeImage/", "store.shell", bannerDeleteShell); // 复制文件至远程另外一台服务器
		}
		response.getWriter().print(result);
		return null;
	}
	
	/**
	 * 新增店铺
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/offlineStore/updateStore.json")
	public String updateStore(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(request.getParameter("detail_id")));
		params.put("name", request.getParameter("detail_name"));
		params.put("ename", request.getParameter("detail_ename"));
		params.put("full_address", request.getParameter("detail_full_address"));
		params.put("address", request.getParameter("detail_address"));
		params.put("ename1", request.getParameter("detail_ename1"));
		params.put("ename2", request.getParameter("detail_ename2"));
		params.put("province", request.getParameter("detail_province"));
		params.put("city", request.getParameter("detail_city"));
		params.put("district", request.getParameter("_district"));
		params.put("extension", request.getParameter("detail_extension"));
		params.put("storeImage", request.getParameter("storeImagePath"));
		params.put("mapImage", request.getParameter("mapImagePath"));
		params.put("phone", request.getParameter("detail_phone"));
		params.put("hours", request.getParameter("detail_hours"));
		params.put("postcode", request.getParameter("detail_postcode"));
		
		int result = offlineStoreManager.updateStore(params);
		if (result == 1) {
			/**
			 * 上传banner图片的目录
			 */
			String	bannerImageUploadPaths	= UPLOAD_IMG_BASE+"/resources/banner/cn/";
			deleteImageNotExists(bannerImageUploadPaths + "storeImage/");
//			execShell(bannerImageUploadPaths+"storeImage/", "store.shell", bannerDeleteShell); // 复制文件至远程另外一台服务器
		}
		response.getWriter().print(result);
		return null;
	}
	
	/**
	 * 店铺列表页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/offlineStore/storeList.json")
	public String storeList(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
//		String province = request.getParameter("store_province");
//		String city = request.getParameter("store_city");
		String name = request.getParameter("name");
		String pageNo = request.getParameter("pageNo");
		// 查询所有店铺
		Map<Object, Object> params = new HashMap<Object, Object>();
//		params.put("province", province);
//		params.put("city", city);
		params.put("name", name);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		Page page = new Page(Integer.parseInt(pageNo),10);
		Pagination<OfflineStore> pagination = offlineStoreManager.findOfflineStoreListByPage(page, params);
		JSONObject obj = JSONObject.fromObject(pagination);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(obj);
		return null;
	}
	
	/**
	 * 上传banner图片到本地临时文件夹
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/offlineStore/uploadStoreImage.json", method = RequestMethod.POST)
	@ResponseBody
	public Object uploadStoreImage(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		response.setContentType("text/html;charset=UTF-8");
		/**
		 * 上传banner图片的目录
		 */
		String	bannerImageUploadPaths	= UPLOAD_IMG_BASE+"/resources/banner/cn/";
		String bannerImageUploadPath = bannerImageUploadPaths + "storeImage/";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("Filedata");
		String fileName = file.getOriginalFilename();
		int width = 0;
		int height = 0;
		try {
			Image image = ImageIO.read(file.getInputStream());
			width = image.getWidth(null);
			height = image.getHeight(null);
		} catch (IOException e1) {
			result.put("flag", false);
			result.put("warnMsg", "获取图片宽高出错");
		}
		result.put("fileName", fileName);
		
		if (!file.isEmpty()) {
			File excelDir = new File(bannerImageUploadPath);
			if (!excelDir.exists()) {
				excelDir.mkdirs();
			}
			try {
				//deleteImageNotExists(bannerImageUploadPath);
				UUID uuid = UUID.randomUUID();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
				String timeString = format.format(new Date()) + "_" + uuid + fileName.substring(fileName.lastIndexOf("."), fileName.length());
				String path = bannerImageUploadPath + timeString;
				
				//log.error(path + "******马君啊");
				File targetFile = new File(path);
				//log.error(targetFile.getPath() + "------马君哈");
				
				file.transferTo(targetFile);
				result.put("flag", true);
				// 上传图片到服务器上的具体路径
				result.put("complateServerPath", timeString);
				//path = path.substring(path.indexOf("ROOT/") + "ROOT".length());
				//result.put("serverPath", path);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				result.put("flag", false);
				result.put("errMessage", e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				result.put("flag", false);
				result.put("errMessage", e.getMessage());
			}
		}
		return result;
	}
	
	private void deleteImageNotExists(String bannerImageUploadPath) {
		// 删除数据库不存在的图片
		List<OfflineStore> storeList = offlineStoreManager.findOfflineStoreList();
		List<String> nameStrList = getFile(bannerImageUploadPath);
		List<String> newNameList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(storeList) && CollectionUtils.isNotEmpty(nameStrList)) {
			for (OfflineStore sl : storeList) {
				for (String name : nameStrList) {
					if ((sl.getStoreImage()!= null || sl.getMapImage()!= null)&& (sl.getStoreImage().contains(name) || sl.getMapImage().contains(name))) {
						newNameList.add(name);
					}
				}
			}
			
			if (CollectionUtils.isNotEmpty(newNameList)) {
				Iterator<String> it = nameStrList.iterator();
				boolean temp = false;
				while(it.hasNext()){
					String itStr = it.next();
					for (String n : newNameList) {
						if (n.equals(itStr)) {
							temp = true;
						}
					}
					if (temp) {
						it.remove();
						temp = false;
					}
				}
			}
			for (String na : nameStrList) {
				File file = new File(bannerImageUploadPath + na);
				if(file.exists()){
					boolean fang = deleteFile(bannerImageUploadPath + na);
					if (fang) {
						log.info("删除临时上传的图片成功");
					} else {
						log.error("删除临时上传的图片失败");
					}
//					execShell(bannerImageUploadPath + na, "banner.delete.shell", bannerDeleteShell); // 删除远程另外一台服务器上的图片
				}
			}
		}
	}
	
	/**
	 * 以流的形式向前端返回图片
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/offlineStore/getStoreImage.json")
	public void getBannerImage(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String imagePath = request.getParameter("path");
		/**
		 * 上传banner图片的目录
		 */
		String	bannerImageUploadPaths	= UPLOAD_IMG_BASE+"/resources/banner/cn/";
		String bannerImageUploadPath = bannerImageUploadPaths + "storeImage/";
		try {
			response.setContentType("image/jpeg"); // 设置返回内容格式
			File file = new File(bannerImageUploadPath + imagePath); // 括号里参数为文件图片路径
			//InputStream in = inputStream;
			if (file.exists() && StringUtils.isNotEmpty(imagePath)) { // 如果文件存在
				InputStream in = new FileInputStream(bannerImageUploadPath + imagePath); // 用该文件创建一个输入流
				OutputStream os = response.getOutputStream(); // 创建输出流
				byte[] b = new byte[1024];
				while (in.read(b) != -1) {
					os.write(b);
				}
				in.close();
				os.flush();
				os.close();
			}
		} catch (FileNotFoundException e) {
			log.error("无此文件", e);
		} catch (IOException e) {
			log.error("读取文件流异常", e);
		}
	}
	
	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		boolean flag = false;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	private List<String> getFile(String path){   
		List<String> list = new ArrayList<String>();
        // get file list where the path has   
        File file = new File(path);   
        // get the folder list   
        File[] array = file.listFiles();   
        //System.out.println("一共文件数："+array.length+"*-*-*-"+path);
        if(Validator.isNotNullOrEmpty(array)){
            for(int i=0;i<array.length;i++){   
                if(array[i].isFile()){   
                    // only take file name   
                    //System.out.println("^^^^^" + array[i].getName());
                    list.add(array[i].getName());
//                    // take file path and name   
//                    System.out.println("#####" + array[i]);   
//                    // take file path and name   
//                    System.out.println("*****" + array[i].getPath());   
                } else if(array[i].isDirectory()){   
                    getFile(array[i].getPath());   
                }   
            } 
        }
        return list;
    }
	
	public static void execShell(String fileName, String shellName, String shellPath) {
		shellPath = shellPath.replace("${filePath}", fileName);
		if ("banner.shell".equals(shellName)) {
			shellPath = shellPath.replace("${filePathCopy}", fileName.substring(0, fileName.length()-3));
		}
		try {
			Runtime.getRuntime().exec(shellPath);
			log.error("linux "+shellName+" 成功");
		} catch (Exception e) {
			log.error("linux "+shellName+" 失败");
			e.printStackTrace();
			log.error("[error]" + e.getMessage());
		}
	}
}
