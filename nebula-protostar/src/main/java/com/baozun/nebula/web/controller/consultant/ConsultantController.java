package com.baozun.nebula.web.controller.consultant;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.support.excel.ExcelWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.manager.product.ConsultantManager;
import com.baozun.nebula.model.sns.Consultants;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author - 项硕
 */
@Controller
public class ConsultantController extends BaseController {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(ConsultantController.class);
	
	/** cookie 前缀 --待回复 */
	private static final String COOKIE_PREFIX_UNRESOLVED = "c_un_";	
	/** cookie 前缀 --已回复 */
	private static final String COOKIE_PREFIX_RESOLVED = "c_re_";	
	/** cookie 有效期（一个月） */
	private static final Integer COOKIE_EXPIRE = 60 * 60 * 24 * 30;	
	
	/** 咨询列表页 待回复咨询 查询条件参数数组 */
	private static final String[] UNRESOLVED_SEARCHE_PARAM_ARRAY = {"q_date_createDateStart", "q_date_createDateEnd", "q_sll_code", "q_sl_title"};
	/** 咨询列表页 已回复咨询 查询条件参数数组 */
	private static final String[] RESOLVED_SEARCHE_PARAM_ARRAY = {"q_date_resolvetimeStart", "q_date_resolvetimeEnd", "q_int_publishmark", "q_sll_code", "q_sl_title"};
	
	@Autowired
	private ConsultantManager consultantManager;
	
	@Autowired
	@Qualifier("consultantWriter")
	private ExcelWriter consultantWriter;

	/**
	 * 前往页面
	 * 咨询管理列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/consultant/consultantList.htm", method = RequestMethod.GET)
	public String consultantList(HttpServletRequest request, Model model) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				String name = c.getName();
				if (name.startsWith(COOKIE_PREFIX_UNRESOLVED) || name.startsWith(COOKIE_PREFIX_RESOLVED)) {	// TODO cookie规范未定
					try {
						String value = URLDecoder.decode(c.getValue(), "UTF-8");
						if (StringUtils.isNotBlank(value)) {
							model.addAttribute(name, value);
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "consultant/consultant-list";
	}

	/**
	 * 获取咨询列表数据
	 * @param queryBean
	 * @return
	 */
	@RequestMapping(value = "/consultant/consultantList.json", method = RequestMethod.GET)
	@ResponseBody
	public Pagination<ConsultantCommand> getConsultantListData(
			@QueryBeanParam QueryBean queryBean, 
			@RequestParam("q_int_lifecycle") Integer lifeCycle,
			HttpServletRequest request, 
			HttpServletResponse response) {
		String[] paramArr = null;
		String cookiePrefix = null;
		
		if (Consultants.STATUS_UNRESPONSED.equals(lifeCycle)) {
			paramArr = UNRESOLVED_SEARCHE_PARAM_ARRAY;
			cookiePrefix = COOKIE_PREFIX_UNRESOLVED;
		} else if (Consultants.STATUS_RESPONSED.equals(lifeCycle)) {
			paramArr = RESOLVED_SEARCHE_PARAM_ARRAY;
			cookiePrefix = COOKIE_PREFIX_RESOLVED;
		} else {
			return null;	// TODO 目前只有2种情况
		}
		
		for (int i = 0; i < paramArr.length; i++) {
			String paramName = paramArr[i];
			Object paramValue = request.getParameter(paramName);
			
			if (null == paramValue) {
				paramValue = "";
			}
			
			if (paramValue instanceof Date) {
				paramValue = DateFormatUtils.format((Date)paramValue, "yyyy-MM-dd");
			}
			try {
				Cookie cookie = new Cookie(cookiePrefix + paramName, URLEncoder.encode(paramValue.toString(), "UTF-8"));
				cookie.setMaxAge(COOKIE_EXPIRE);	
				response.addCookie(cookie);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		
		Sort[] sorts = queryBean.getSorts();
		if (null == sorts) {
			sorts = Sort.parse("a.createTime asc");
		}
        return consultantManager.findConsultantListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
	}

	/**
	 * 根据ID获取咨询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/consultant/getConsultantById.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getConsultantById(Long id) {
		return consultantManager.findConsultantById(id);
	}

	/**
	 * 回复咨询
	 * @param id
	 * @param resolveNote	回复内容
	 * @param publishMark	是否公示
	 * @return
	 */
	@RequestMapping(value = "/consultant/resolveConsultant.json", method = RequestMethod.POST)
	@ResponseBody
	public Object resolveConsultant(Long id, String resolveNote, Integer publishMark) {
		ConsultantCommand cc = consultantManager.findConsultantById(id);
		if (null != cc) {
			Long curUserId = getUserDetails().getUserId();
			cc.setLifeCycle(Consultants.STATUS_RESPONSED);
			cc.setResolveNote(StringUtils.trim(resolveNote));
			cc.setResolveId(curUserId);
			cc.setResolveTime(new Date());
			if (Consultants.PUBLISH_MARK_YES.equals(publishMark)) {
				cc.setPublishId(curUserId);
				cc.setPublishTime(new Date());
				cc.setPublishMark(publishMark);
			}
			try {
				if (consultantManager.resolveConsultant(cc)) return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return FAILTRUE;
	}

	/**
	 * 更新回复
	 * @param id
	 * @param resolveNote	回复内容
	 * @param publishMark	是否公示
	 * @return
	 */
	@RequestMapping(value = "/consultant/updateConsultant.json", method = RequestMethod.POST)
	@ResponseBody
	public Object updateConsultant(Long id, String resolveNote, Integer publishMark) {
		ConsultantCommand cc = consultantManager.findConsultantById(id);
		if (null != cc) {
			Long curUserId = getUserDetails().getUserId();
			cc.setResolveNote(StringUtils.trim(resolveNote));
			cc.setLastUpdateId(curUserId);
			cc.setLastUpdateTime(new Date());
			if (Consultants.PUBLISH_MARK_YES.equals(publishMark)) {
				cc.setPublishId(curUserId);
				cc.setPublishTime(new Date());
				cc.setPublishMark(publishMark);
			} else {
				cc.setUnPublishId(curUserId);
				cc.setUnPublishTime(new Date());
				cc.setPublishMark(publishMark);
			}
			try {
				if (consultantManager.updateConsultant(cc))	return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return FAILTRUE;
	}

	/**
	 * 公示
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/consultant/publishConsultant.json", method = RequestMethod.POST)
	@ResponseBody
	public Object publishConsultant(Long id) {
		ConsultantCommand cc = consultantManager.findConsultantById(id);
		if (null != cc) {
			Long curUserId = getUserDetails().getUserId();
			cc.setPublishId(curUserId);
			cc.setPublishMark(Consultants.PUBLISH_MARK_YES);
			try {
				if (consultantManager.publishConsultant(cc)) return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return FAILTRUE;
	}

	/**
	 * 取消公示
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/consultant/unpublishConsultant.json", method = RequestMethod.POST)
	@ResponseBody
	public Object unpublishConsultant(Long id) {
		ConsultantCommand cc = consultantManager.findConsultantById(id);
		if (null != cc) {
			Long curUserId = getUserDetails().getUserId();
			cc.setUnPublishId(curUserId);
			cc.setPublishMark(Consultants.PUBLISH_MARK_NO);
			try {
				if (consultantManager.unpublishConsultant(cc)) return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return FAILTRUE;
	}
	
	/**
	 * 批量公示
	 * @param ids	ID的csv
	 * @return
	 */
	@RequestMapping(value = "/consultant/batchPublishConsultant.json", method = RequestMethod.POST)
	@ResponseBody
	public Object batchPublishConsultant(String ids) {
		List<Long> successList = new ArrayList<Long>();
		String[] idArr = ids.split(",");
		for (String s : idArr) {
			try {
				Long id = Long.parseLong(s);
				ConsultantCommand cc = consultantManager.findConsultantById(id);
				if (null != cc) {
					Long curUserId = getUserDetails().getUserId();
					cc.setPublishId(curUserId);
					cc.setPublishMark(Consultants.PUBLISH_MARK_YES);
					if (consultantManager.publishConsultant(cc)) {
						successList.add(id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (successList.size() == idArr.length) return SUCCESS;
		return FAILTRUE;
	}

	/**
	 * 批量取消公示
	 * @param ids	ID的csv
	 * @return
	 */
	@RequestMapping(value = "/consultant/batchUnpublishConsultant.json", method = RequestMethod.POST)
	@ResponseBody
	public Object batchUnpublishConsultant(String ids) {
		List<Long> successList = new ArrayList<Long>();
		String[] idArr = ids.split(",");
		for (String s : idArr) {
			try {
				Long id = Long.parseLong(s);
				ConsultantCommand cc = consultantManager.findConsultantById(id);
				if (null != cc) {
					Long curUserId = getUserDetails().getUserId();
					cc.setUnPublishId(curUserId);
					cc.setPublishMark(Consultants.PUBLISH_MARK_NO);
					if (consultantManager.unpublishConsultant(cc)) {
						successList.add(id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (successList.size() == idArr.length) return SUCCESS;
		return FAILTRUE;
	}
	
	/**
	 * 咨询列表导出
	 * @param response
	 */
	@RequestMapping(value = "/consultant/consultant-list.xlsx", method = RequestMethod.GET)
	public void export(@QueryBeanParam QueryBean queryBean, HttpServletResponse response) {
		String path = "excel/consultant-list-export.xlsx";
		Map<String, Object> beans = new HashMap<String, Object>();
		List<ConsultantCommand> list = consultantManager.findConsultantListByQueryMap(Sort.parse("a.createtime desc"), queryBean.getParaMap());
		beans.put("consultantList", list);
		try {
			response.setHeader("Content-type", "application/force-download");
			response.setHeader("Content-Transfer-Encoding", "Binary");
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ "consultant-list.xlsx\"");

			consultantWriter.write(path, response.getOutputStream(), beans);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
