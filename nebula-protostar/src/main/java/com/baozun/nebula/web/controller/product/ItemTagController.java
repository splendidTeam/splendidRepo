package com.baozun.nebula.web.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.TagCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemTagManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.ItemTag;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;


@Controller
public class ItemTagController extends BaseController{
	
	@Autowired
	private ItemTagManager itemTagManager;
	
	@Autowired
	private IndustryManager industryManager;
	
	@Autowired
    private ShopManager shopManager;
	
	/**
	 * 显示商品标签绑定页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/itemTag.htm")
	public String itemCategory(Model model){
		
		List<Industry> result=industryManager.findAllIndustryList(); 
		model.addAttribute("industrylist",result);
		
		return "/product/item/item-tag";
	}
	
	/**
	 * 标签列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/product/tagList.json")
	@ResponseBody
	public Pagination<TagCommand> tagListJson(Model model,
			@QueryBeanParam QueryBean queryBean,
			HttpServletRequest request,
			HttpServletResponse response){
		
		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			sorts=Sort.parse("name asc");
		}
		
		List<TagCommand> tagList = itemTagManager
				.findEffectItemTagListByQueryMap(queryBean.getParaMap(),sorts);
		
		Pagination<TagCommand> page=new Pagination<TagCommand>(tagList,tagList.size(),1,1,0,Integer.MAX_VALUE);
		page.setSortStr(sorts[0].getField()+" "+sorts[0].getType());
		
		return page;
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
	
	@RequestMapping(value = "/item/itemTagList.json")
	@ResponseBody
	public Pagination<ItemCommand> findItemTagListJson(Model model,
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
		Pagination<ItemCommand> args = itemTagManager
				.findItemTagListByQueryMapWithPage(queryBean.getPage()
						,sorts, queryBean.getParaMap(), shopId);
			
		return args;
	}
	
	/**
	 * 未分类商品列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/item/itemNoTagList.json")
	@ResponseBody
	public Pagination<ItemCommand> findNoTagItemListJson(Model model,
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
		
		Pagination<ItemCommand> args = itemTagManager
				.findItemNoTagListByQueryMapWithPage(queryBean.getPage()
						,sorts, queryBean.getParaMap(), shopId);
			
		return args;
	}
	
	@RequestMapping("/item/bindItemTag.json")
	@ResponseBody
	public Object bindItemTag(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("tagIds") Long[] tagIds,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		itemTagManager.bindItemTag(itemIds, tagIds);
		

		return SUCCESS;
	}
	
	@RequestMapping("/item/unBindItemTag.json")
	@ResponseBody
	public Object unBindItemTag(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("tagId") Long tagId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		
		boolean removeFlag =
				itemTagManager.unBindItemTag(itemIds, tagId);
		
		if(!removeFlag){
			throw new Exception();
		}
		return SUCCESS;
	}
	
	@RequestMapping("/item/validateUnBindByItemIdsAndTagId.json")
	@ResponseBody
	public Object validateUnBindSel(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("tagId") Long tagId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		boolean flag=
				itemTagManager.validateUnBindByItemIdsAndTagId(itemIds, tagId);

		if(!flag){
			//throw new Exception();
			return FAILTRUE;
		}
		return SUCCESS;
	}
	
	@RequestMapping("/product/saveTag.Json")
	@ResponseBody
	public Object saveGroup(
			@RequestParam("tagName") String tagName,
			@RequestParam("tagType") String tagType,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{


		if (StringUtils.isNotBlank(tagName)){

			
			boolean resultValidate = itemTagManager.validateTagName(tagName);
			
			if (resultValidate){
				//throw new BusinessException(ErrorCodes.NAME_EXISTS);
				return FAILTRUE;
			}
			
			ItemTag itemTag =new ItemTag();
			itemTag.setCreateTime(new Date());
			itemTag.setLifecycle(ItemTag.LIFECYCLE_ENABLE);
			itemTag.setName(tagName);
			itemTag.setType(Integer.valueOf(tagType));
			itemTag.setVersion(new Date());
			itemTagManager.createOrUpdateItemTag(itemTag);
			
			return SUCCESS;
		}else{
			throw new Exception();
		}
	}
	
	@RequestMapping("/product/removeTagByIds.json")
	@ResponseBody
	public Object removeGroupByIds(
			@RequestParam("ids") String ids,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		if (StringUtils.isNotBlank(ids)){

			String[] idArray = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String sid : idArray){

				idList.add(Long.parseLong(sid));
			}
			boolean removeFlag = itemTagManager.removeTagByIds(idList);

			if(!removeFlag){
				return FAILTRUE;
			}
		}
		return SUCCESS;
	}

}
