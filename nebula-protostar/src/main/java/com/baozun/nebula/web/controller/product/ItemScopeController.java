package com.baozun.nebula.web.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.sdk.manager.SdkItemTagRuleManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品范围
 * */
@Controller
public class ItemScopeController extends BaseController{

	final Logger log = LoggerFactory.getLogger(ItemScopeController.class);

	@Autowired
	private CategoryManager		categoryManager;
	
	@Autowired
	private ItemCategoryManager itemCategoryManager;

	
	@Autowired
	private IndustryManager industryManager;
	
	@Autowired
    private ShopManager shopManager;
	
	@Autowired
	private SdkItemTagRuleManager sdkItemTagRuleManager;
	
	@Autowired
	private OrganizationManager	organizationManager;
	/**
	 * 前往商品创建页
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/combo/addItem.htm")
	public String enter(Model model,@QueryBeanParam QueryBean queryBean,
			HttpServletRequest request,
			HttpServletResponse response) {
		List<Organization> list = organizationManager.findAllOrganization();
		model.addAttribute("organizationList", list);
		return "/product/combo/add-item";
	}
	
	/**
	 * 已分类商品列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/product/scope/itemSelect.json")
	@ResponseBody
	public Pagination<ItemCommand> findItemCtListJson(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		
		Long shopId = shopManager.getShopId(getUserDetails());

		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		Pagination<ItemCommand> args = itemCategoryManager.findItemCtListByQueryMapWithPage(queryBean.getPage(),
				sorts, queryBean.getParaMap(),shopId);
		System.out.println("+++++++++++++++++++++++++"+shopId+"----+++----"+JsonFormatUtil.format(args));
		
		return args;
	}
	
	
	
	/**
	 * 前往商品分类创建页
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/combo/addCategory.htm")
	public String enterCate(Model model,@QueryBeanParam QueryBean queryBean,
			HttpServletRequest request,
			HttpServletResponse response) {
		List<Organization> list = organizationManager.findAllOrganization();
		model.addAttribute("organizationList", list);
		
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);
		return "/product/combo/add-category";
	}
	
	/**
	 * 前往自定义组合创建页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/combo/addComboItem.htm", method = RequestMethod.GET)
	public String enterCombo(Model model) {
		Long shopId = shopManager.getShopId(getUserDetails());
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);
		
		Page p = new Page();
		p.setSize(10);
		p.setStart(0);
		Sort[] ccsorts = Sort.parse("t.create_time desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ttttttttttttt", "%20%");
		Pagination<ItemTagRuleCommand> customProductComboCommand = sdkItemTagRuleManager
				.findCustomProductComboList(p,ccsorts, map,shopId);
		model.addAttribute("comboList", customProductComboCommand);
		log.debug(JsonFormatUtil.format(customProductComboCommand));
		return "/product/combo/add-combo-item";
	}
	
	/**
	 * 已分类商品列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/product/scope/itemSelectNoShopid.json")
	@ResponseBody
	public Pagination<ItemCommand> itemSelectNoShopid(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		

		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		Pagination<ItemCommand> args = itemCategoryManager.findItemListEmptyCategoryByQueryMapWithPageNoShopid(queryBean.getPage(),
				sorts, queryBean.getParaMap());
		
		return args;
	}

}
