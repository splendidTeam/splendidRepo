package com.baozun.nebula.web.controller.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

@Controller
public class PromotionSelectorController extends BaseController{

	@Autowired
	private CategoryManager		categoryManager;
	
	@Autowired
	private ItemCategoryManager itemCategoryManager;

	
	@Autowired
	private IndustryManager industryManager;
	
	@Autowired
    private ShopManager shopManager;

	/**
	 * 显示商品关联分类页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cms/toArea111.htm")
	public String itemCategory(Model model){
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		
		List<Industry> result=industryManager.findAllIndustryList(); 
		model.addAttribute("industrylist",result);
		
//		return "/product/item/item-category";
		return "/promotion/promotion-item-selector";
	}
	
	/**
	 * 已分类商品列表
	 * 
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/item/itemPromotionSelect.json")
	@ResponseBody
	public Pagination<ItemCommand> findItemCtListJson(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		
		//查询orgId
		UserDetails userDetails = this.getUserDetails();
		
		ShopCommand shopCommand =null;
		Long shopId=0L;
		
		Long currentOrgId =	userDetails.getCurrentOrganizationId();
		//根据orgId查询shopId
		if(currentOrgId!=null){
			shopCommand=shopManager.findShopByOrgId(currentOrgId);
			if(shopCommand!=null){
				shopId = shopCommand.getShopid();
			}
		}
				
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
//		Pagination<ItemCommand> args = null;
//		Pagination<ItemCommand> temp = null;
//		Map<String, Object> paras = queryBean.getParaMap();
//		String strs = (String) paras.get("categoryId");
//		if(strs != null){
//			strs = strs.replaceAll("%", "");
//			String[] ids = strs.split(",");
//			for(int i=0;i<ids.length;i++){
//				paras.remove("categoryId");
//				paras.put("categoryId", Long.parseLong(ids[i]));
//				temp = itemCategoryManager.findItemCtListByQueryMapWithPage(queryBean.getPage(),
//						sorts, paras,shopId);
//				args = addPagination(args,temp);
//			}
//		}else{
//			args = itemCategoryManager.findItemCtListByQueryMapWithPage(queryBean.getPage(),
//					sorts, paras,shopId);
//		}
		Pagination<ItemCommand> args = null;
		Map<String, Object> paras = queryBean.getParaMap();
		String strs = (String) paras.get("categoryId");
		ArrayList<Long> cids = new ArrayList<Long>();
		if(strs != null){
			strs = strs.replaceAll("%", "");
			String[] ids = strs.split(",");
			for(int i=0;i<ids.length;i++){
				cids.add(Long.parseLong(ids[i]));
			}
			paras.remove("categoryId");
			paras.put("categoryId", cids);
			args = itemCategoryManager.findItemCtListByQueryMapWithPageByCIDS(queryBean.getPage(),
					sorts, paras,shopId);
		}else{
			args = itemCategoryManager.findItemCtListByQueryMapWithPage(queryBean.getPage(),
					sorts, paras,shopId);
		}
		if(args != null){
			System.out.println("++++++++++++++++++++++++++++++"+ cids.toString()+"----"+JsonFormatUtil.format(args));
		}
		return args;
	}
	
	
	private Pagination<ItemCommand> addPagination(Pagination<ItemCommand> p1,Pagination<ItemCommand> p2){
		Pagination<ItemCommand> newpagination = null;
		if(p1 != null && p2!= null){
			List<ItemCommand> items = new ArrayList<ItemCommand>();
			items.addAll(p1.getItems());
			items.addAll(p2.getItems());
			long count = items.size();
			int size = 1;
			if(p1.getSize()>0){
				size = p1.getSize();
			}
			int total = (int) (count%size>0 ? count/size+1: count/size);
			newpagination = new Pagination(items, items.size(), p1.getCurrentPage(),
					total, p1.getStart(), size);
		}else if(p1 == null)
			return p2;
		
		return newpagination;
	}

	
	
}