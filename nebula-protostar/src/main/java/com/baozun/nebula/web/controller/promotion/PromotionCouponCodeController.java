/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Baozun. You
 * shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Baozun.
 * 
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.baozun.nebula.web.controller.promotion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.promotion.PromotionCouponManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponCodeManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

 
@Controller
public class PromotionCouponCodeController extends BaseController {

	@Autowired
	private SdkPromotionCouponCodeManager sdkCouponCodeManager; 
	@Autowired
	private SdkPromotionCouponManager sdkCouponManager; 
	@Autowired
	private ShopManager shopManager; 
	@Autowired
	private PromotionCouponManager promotionCouponManager;
	
	@RequestMapping(value = "/promotion/couponimport.htm", method = RequestMethod.GET)
	public String promotionEdit(Model model) {
		List<PromotionCouponCommand> couponTypeList=sdkCouponManager.findAllcouponList();
		model.addAttribute("couponTypeList", couponTypeList);
		return "/promotion/coupon-list";
	}
	
	@RequestMapping(value = "/promotion/couponCodeList.json", method = RequestMethod.GET) 
	@ResponseBody
	public Pagination<PromotionCouponCodeCommand> promotionEditList(Model model,@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0) {
			sorts = Sort.parse("cc.create_time desc");
		}
		//查询出用户属于哪个店铺
		Long shopId = shopManager.getShopId(getUserDetails());
		Map<String, Object> queryMap = queryBean.getParaMap();
		queryMap.put("shopId", shopId);
		Pagination<PromotionCouponCodeCommand> couponCodeCommand = sdkCouponCodeManager
				.querycouponcodeListByQueryMapWithPage(queryBean.getPage(), sorts,queryMap);
		return couponCodeCommand;
	}
	
	/**
	 * 删除优惠券
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/removeCouponCode.json", method = RequestMethod.POST) 
	@ResponseBody
	public BackWarnEntity removeCouponCode(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkCouponCodeManager.removeCouponCodeById(id);
			return SUCCESS;
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
/***************************************************************** X **************************************************************/
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 前往优惠券码导入页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/couponCodeImport.htm", method = RequestMethod.GET)
	public String goCouponCodeImport(Model model, HttpSession session) {
		List<PromotionCouponCommand> couponTypeList = sdkCouponManager.findAllcouponList();
		if(couponTypeList!=null && couponTypeList.size()>0){
			for (int i = 0; i < couponTypeList.size(); i++) {
				String times = "(" +couponTypeList.get(i).getLimitTimes()+"次劵)";
				String couponName = couponTypeList.get(i).getCouponName()+times;
				couponTypeList.get(i).setCouponName(couponName);
			}
		}
		model.addAttribute("couponTypeList", couponTypeList);
		return "/promotion/coupon-code-import";
	}
	
	/**
	 * 导入优惠券
	 * 为解决uploadify的406错误，直接在response中写
	 * @param couponId
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/promotion/couponImport.json", method = RequestMethod.POST)
	public void uploadCoupon(
			@RequestParam Long couponId,
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> rs = new HashMap<String, Object>();
		UserDetails user = getUserDetails();
		PromotionCouponCodeCommand coupon = new PromotionCouponCodeCommand();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("Filedata");
			Long shopId = shopManager.getShopId(user);
			coupon.setShopId(shopId);
			coupon.setCreateId(user.getUserId());
//			coupon.setStartTime(startTime);
//			coupon.setEndTime(endTime);
			coupon.setCouponId(couponId);
			List<String> errorList = promotionCouponManager.importCouponCode(file, coupon);
			rs.put("isSuccess", true);
			rs.put("errorList", errorList);
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.write(JsonFormatUtil.format(rs));
			} finally {
				if (null != out) {
					out.close();
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.put("isSuccess", false);
			rs.put("message", getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.put("isSuccess", false);
			rs.put("message", getMessage(ErrorCodes.SYSTEM_ERROR));
		}
	}
	
	/**
	 * 下载EXCEL模版
	 * @param response
	 */
	@RequestMapping(value = "/promotion/coupon-code-import.xlsx", method = RequestMethod.GET)
	public void downloadTemplate(HttpServletResponse response) {
		String path = "excel/coupon-code-import.xlsx";
		try {
			downloadExcel(response, path);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载EXCEL
	 * @param response
	 * @param path
	 * @throws IOException
	 */
	private void downloadExcel(HttpServletResponse response, String path) {
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());

		response.setHeader("Content-type", "application/force-download");
		response.setHeader("Content-Transfer-Encoding", "Binary");
		response.setHeader("Content-length", "" + file.length());
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "inline;filename=\"" + path + "\"");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		
		try {
			OutputStream os = null;
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				os = response.getOutputStream();
				byte[] buf = new byte[1024];
				while (is.read(buf) != -1) {
					os.write(buf);
				}
			} finally {
				is.close();
				os.close();
			}
		} catch (Exception e) {
			throw new BusinessException(ErrorCodes.FILE_READ_FAIL);
		}
	}
}
