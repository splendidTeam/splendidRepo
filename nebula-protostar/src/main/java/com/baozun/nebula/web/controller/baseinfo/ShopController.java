package com.baozun.nebula.web.controller.baseinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.ShopProperty;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 店铺管理
 * 
 * @author caihong.wu
 * @date 2013-7-1下午04:39:27
 */
@Controller
public class ShopController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ShopController.class);

	@Autowired
	private ShopManager			shopManager;

	@Autowired
	private IndustryManager		industryManager;

	@Autowired
	private OrganizationManager	organizationManager;

	@Autowired
	private PropertyManager		propertyManager;

	/**
	 * ajax获取店铺列表
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/shop/shopList.json")
	@ResponseBody
	public Pagination<ShopCommand> findShopListJson(
			@QueryBeanParam QueryBean queryBean,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> paraMap = queryBean.getParaMap();
		if (null == paraMap){
			paraMap = new HashMap<String, Object>();
		}
		paraMap.put("orgaTypeId", OrgType.ID_SHOP_TYPE);
		
		Sort[] paraSorts = queryBean.getSorts();
		if (null == paraSorts){
			paraSorts=Sort.parse("s.create_time desc");
		}
		//queryBean.setParaMap(paraMap);
		List<ShopCommand> shopCommandList = shopManager.findShopListByQueryMap(paraMap,paraSorts);
		
		Pagination<ShopCommand> page=new Pagination<ShopCommand>(shopCommandList,shopCommandList.size(),1,1,0,Integer.MAX_VALUE);
		page.setSortStr(paraSorts[0].getField()+" "+paraSorts[0].getType());
		return page;
	}

	/**
	 * 获取店铺列表页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/shop/shopList.htm")
	public String findShopList(Model model,HttpServletRequest request,HttpServletResponse response){
		return "product/shop/shop-list";
	}

	/**
	 * 逻辑删除店铺
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/removeShop.json")
	@ResponseBody
	public BackWarnEntity removeShopByIds(
			@RequestParam("ids") Long[] ids,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		// Map<String, Object> result = new HashMap<String, Object>();
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		Integer errorCode = null;
		if (ids.length != 0){
			Integer isresult = shopManager.removeShopByIds(ids);
			if (isresult == ids.length){
				backWarnEntity.setIsSuccess(true);
				// result.put("result", "success");
			}else{
				// result.put("result", "fail");
				backWarnEntity.setIsSuccess(false);
				errorCode = ErrorCodes.ROWCOUNT_NOTEXPECTED;
				throw new BusinessException(ErrorCodes.ROWCOUNT_NOTEXPECTED, new Integer[] { isresult, ids.length });
			}
		}else
			// result.put("result", "fail");
			backWarnEntity.setIsSuccess(false);
		if (null != errorCode){
			backWarnEntity.setDescription(getMessage(errorCode));
		}
		return backWarnEntity;
	}

	/**
	 *增加店铺的页面显示
	 */
	@RequestMapping("/shop/createShop.htm")
	public String createShop(Model model){
		Sort[] sorts = Sort.parse("id desc");
		
		List<Industry> industries=shopManager.findAllIndustryList(sorts);
		
		List<Map<String, Object>> industryList = this.processIndusgtryList(industries, null);
		model.addAttribute("industryList", industryList);
		
        String jsonStr = convertListJson(industries,null);
        model.addAttribute("propertyZNodes", jsonStr);
        
		return "/product/shop/add-shop";
	}

	/**
	 * 对页面的数据节点进行判断存储
	 * 
	 * @param industryList
	 * @return
	 */
	private List<Map<String, Object>> processIndusgtryList(List<Industry> industryList,Long shopId){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<ShopProperty> shopPropertyList = new ArrayList<ShopProperty>();
		if(shopId!=null){
			shopPropertyList = shopManager.findShopPropertyByshopId(shopId);
		}
		for (Industry indu : industryList){
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", indu.getId());
			map.put("pId", null == indu.getParentId() ? 0 : indu.getParentId());
			map.put("indu_name", indu.getName());
			map.put("open", null == indu.getParentId() ? true : false);
			for (Industry sec_indu : industryList){
				if ((sec_indu.getParentId()).equals(indu.getId())){
					map.put("noCheck", true);
					break;
				}
				
				
			}
			
			if (shopPropertyList!= null){
				for (int i = 0; i < shopPropertyList.size(); i++){
					if(indu.getId().equals(shopPropertyList.get(i).getIndustryId())){
						map.put("checked","true");
//						map.put("open", true);
						break;
					}
				}
			}
			resultList.add(map);
		}
		if (shopPropertyList!= null){
			for (int i = 0; i < shopPropertyList.size(); i++){
				for(Map<String, Object> map:resultList){
					String industryId= shopPropertyList.get(i).getIndustryId().toString();
					String mapId= map.get("id").toString();
					if(industryId.equals(mapId)){
						searchChecked(resultList,shopPropertyList.get(i).getIndustryId().toString());
					}
					
				}
				
			}
		}
		
		return resultList;
	}
	  //递归用于筛选checked
	static void searchChecked(List<Map<String, Object>> resultList,String id)               
    {   
		for(Map<String, Object> map:resultList){
			if(map.get("id").toString().equals(id)){
				map.put("isShow", true);
				if(!map.get("pId").toString().equals("0")){
					searchChecked(resultList,map.get("pId").toString());
				}
			}
			
		}	
    }    

	private String convertListJson(List<Industry> industryList,Long shopId){

            Map<Long,List<Long>> shopPropertyMap=new HashMap<Long, List<Long>>(); 
    	    if(shopId!=null){
    	        
    	      //遍历当前店铺已经被选择的属性节点，以<行业ID,属性节点Map>作为临时变量便于查询
                List<ShopProperty> shopPropertys=shopManager.findShopPropertyByshopId(shopId);
                for(ShopProperty shopProperty:shopPropertys){
                    if(shopProperty.getPropertyId()==null||shopProperty.getIndustryId()==null){
                        continue;
                    }
                    
                    if(!shopPropertyMap.containsKey(shopProperty.getIndustryId())){
                        List<Long> propertyList=new ArrayList<Long>();
                        propertyList.add(shopProperty.getPropertyId());
                        shopPropertyMap.put(shopProperty.getIndustryId(),propertyList);
                    }else{
                        List<Long> propertyList=shopPropertyMap.get(shopProperty.getIndustryId());
                        propertyList.add(shopProperty.getPropertyId());
                    }
                }
                
    	    }
	        
	        
	        //将所有property存入map，以行业进行分组
	        List<Property> allProperty=propertyManager.findAllPropertys();
	        Map<Long,List<Property>> propertyMap=new HashMap<Long, List<Property>>(); 
	        for(Property property:allProperty){
                if(property.getId()==null||property.getIndustryId()==null){
                    continue;
                }
                
                if(!propertyMap.containsKey(property.getIndustryId())){
                    List<Property> propertyList=new ArrayList<Property>();
                    propertyList.add(property);
                    propertyMap.put(property.getIndustryId(),propertyList);
                }else{
                    List<Property> propertyList=propertyMap.get(property.getIndustryId());
                    propertyList.add(property);
                }
            }

            JSONObject json=new JSONObject();
            
	        for(Industry industry:industryList){
	            //是否为父节点，如果是跳过该次操作
	            boolean isContinue=false;
	            for (Industry sec_indu : industryList){
	                if ((sec_indu.getParentId()).equals(industry.getId())){
	                    isContinue=true;
	                    break;
	                }
	            }
	            if(isContinue){
	                continue;
	            }
	            
                JSONArray jsonArray=new JSONArray();
                JSONObject indusryJson=new JSONObject();
                Integer checkType=0;

                Long industryID=industry.getId();
                
                //转换propertyList为zTreeNode
	            List<Property> propertys=propertyMap.get(industryID);
	            if(propertys!=null&&propertys.size()>0){
    	            //判断选中类型 0未选 1选中 2全选
    	            if(shopId!=null&&shopPropertyMap.containsKey(industryID)){
    	                if(propertys.size()<=shopPropertyMap.get(industryID).size()){
    	                    checkType=2;
    	                }else{
    	                    checkType=1;
    	                }
    	            }
	                
        	        for(Property property:propertys){
        	            JSONObject propJson=new JSONObject();
                        Long propertyID=property.getId();
                            
                        propJson.put("id", property.getId());
                        propJson.put("pId", 0);
                        propJson.put("prop_name", property.getName());
                        //propJson.put("open", null == property.getParentId() ? true : false);
                            
            	        if(shopId!=null&&shopPropertyMap.containsKey(industryID)&&shopPropertyMap.get(industryID).contains(propertyID)){
            	            propJson.put("checked", true);
            	        }
                            
                        jsonArray.add(propJson);
        	        }

                }
        	        
                indusryJson.put("indu_name", industry.getName());
                indusryJson.put("checkType", checkType);
                indusryJson.put("list", jsonArray);
                json.put(industry.getId(), indusryJson);
	        }
	        
	        return json.toString();
	}
	
	/**
	 * 修改店铺页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/shop/updateShop.htm")
	public String updateshop(Model model,@RequestParam("shopid") String sId){
		Sort[] sorts = Sort.parse("id desc");
		Long shopId = Long.parseLong(sId);
		model.addAttribute("shopId", shopId);
		// 获取行业信息
		List<Industry> industrys=shopManager.findAllIndustryList(sorts);
		List<Map<String, Object>> industryList = processIndusgtryList(industrys, shopId);
		model.addAttribute("industryList", industryList);
		
		String jsonStr = convertListJson(industrys,shopId);
		model.addAttribute("propertyZNodes", jsonStr);
		
		// 根据orgId获取Organization的相关信息
		Organization orgInfo = organizationManager.findOrgbyId(shopId);
		model.addAttribute("orgInfo", orgInfo);
		return "/product/shop/update-shop";
	}

	/**
	 * 增加或者修改店铺
	 * 
	 * @param shopCommand
	 * @return
	 */
	@RequestMapping("/shop/saveShop.json")
	@ResponseBody
	public Object saveShop(@ModelAttribute() ShopCommand shopCommand,@ArrayCommand() ShopProperty[] shopPropertys){

		Date date = new Date();
		Shop shop = new Shop();
		shop.setModifyTime(date);
		shop.setCreateTime(date);
		shop.setVersion(date);
		shop.setId(shopCommand.getShopid());

		Organization oranization = new Organization();
		oranization.setId(shopCommand.getOrganizationid());
		oranization.setName(shopCommand.getShopname());
		oranization.setDescription(shopCommand.getDescription());
		oranization.setOrgTypeId(2L);
		oranization.setParentId(1L);
		oranization.setLifecycle(shopCommand.getLifecycle());
		oranization.setCode(shopCommand.getShopcode());

		Shop getShop = shopManager.createOrUpdateShop(shop, oranization, shopPropertys);
		if (getShop != null){
			BackWarnEntity backWarnEntity = new BackWarnEntity(true, null);
			//此次的 errorCode为店铺的id
			backWarnEntity.setErrorCode(getShop.getId().intValue());
			
			return backWarnEntity;
		}else{
			throw new BusinessException(ErrorCodes.PRODUCT_ADD_SHOP_FAIL);
		}

	}

	/**
	 * 验证店铺编码的唯一性
	 * 
	 * @param shopcode
	 * @return
	 */
	@RequestMapping("/shop/validateShopCode.json")
	@ResponseBody
	public Object validateShopCode(@RequestParam("shopcode") String shopcode){
		if ("".equals(shopcode) || shopcode == null){
			throw new BusinessException(ErrorCodes.PRODUCT_UPDATE_SHOP_FAIL);
		}else{
			Integer count = shopManager.validateShopCode(shopcode);
			if (count > 0){
				return FAILTRUE;
			}else{
				return SUCCESS;
			}
		}
	}

	/**
	 * 启用或禁用店铺
	 * 
	 * @param ids
	 *            店铺ID字符串
	 * @param type
	 *            启用或禁用类型
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/shop/enableOrDisableShop.json" },method = RequestMethod.POST,headers = HEADER_WITH_AJAX_SPRINGMVC)
	@ResponseBody
	public BackWarnEntity enableOrDisableShopByIds(@RequestParam("ids") String ids,@RequestParam("type") Integer type){

		String[] shopIds = ids.split(",");
		int length = shopIds.length;
		Long[] shopIds2 = new Long[length];
		for (int i = 0; i < length; i++){
			shopIds2[i] = Long.parseLong(shopIds[i]);
		}
		boolean isSuccess = true;
		Integer errorCode = null;

		Integer flag = shopManager.enableOrDisableShopByIds(shopIds2, type);
		if (flag != shopIds.length){
			isSuccess = false;
			errorCode = ErrorCodes.SHOP_ENABLE_DISABLE_FAIL;
			throw new BusinessException(ErrorCodes.ROWCOUNT_NOTEXPECTED, new Integer[] { flag, shopIds.length });
		}

		// **************************************************
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		backWarnEntity.setIsSuccess(isSuccess);
		if (null != errorCode){
			backWarnEntity.setDescription(getMessage(errorCode));
		}

		return backWarnEntity;
	}

	/**
	 * 店铺管理
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/shop/shopPropertymanager.htm")
	public String shopPropertyManager(@RequestParam("shopId") Long shopId,Model model,HttpServletRequest request){

		ShopCommand shop = shopManager.findShopById(shopId);
		model.addAttribute("shop", shop);

		// Long shopId = shopManager.findShopIdbyOrgId(orgId);
		// model.addAttribute("shopId", shopId);

		//List<Industry> list = shopManager.findIndustryListByShopId(shopId);
		//model.addAttribute("industryList", list);
		
		Sort[] sorts = Sort.parse("id desc");
		// 获取行业信息
		List<Map<String, Object>> industryList = processIndusgtryList(shopManager.findAllIndustryList(sorts), shopId);
		model.addAttribute("industryList", industryList);
		

		// 根据orgId获取Organization的相关信息
		Organization orgInfo = organizationManager.findOrgbyId(shopId);
		model.addAttribute("orgInfo", orgInfo);

		request.getSession().setAttribute("shopId", shopId);
		return "/product/shop/shop-manager";
	}

	/**
	 * 通过行业id和店铺id获取系统以及店铺属性列表
	 * 
	 * @param industyId
	 *            行业id
	 * @param shopId
	 *            店铺id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/shop/propertyList.json" },method = RequestMethod.GET,headers = HEADER_WITH_AJAX_SPRINGMVC)
	@ResponseBody
	public Pagination<Property> findPropertyListByShopIdAndIndustryId(
			@RequestParam("industryId") Long industryId,
			@RequestParam("shopId") Long shopId,
			@RequestParam(value="sortStr",required=false) String sortStr,
			Model model){
		
		Sort[] sorts=null;
		if(sortStr!=null){
			sorts=Sort.parse(sortStr);
		}
		else{
			sorts=new Sort[2];
			sorts[0]=new Sort("p.is_common_industry","desc");
			sorts[1]=new Sort("p.sort_no","asc");
		}
		
		List<Property> propertyList = shopManager.findPropertyListByIndustryIdAndShopId(industryId, shopId,sorts);
		
		Pagination<Property> page=new Pagination<Property>(propertyList,propertyList.size(),1,1,0,Integer.MAX_VALUE);
		page.setSortStr(sorts[0].getField()+" "+sorts[0].getType());
		return page;
	}

	/**
	 * 禁用或启用属性
	 * 
	 * @param ids
	 *            属性ids
	 * @param type
	 *            启用或禁用类型
	 * @return
	 */
	@RequestMapping(value = { "/shop/enableOrDisableProperty.json" },method = RequestMethod.POST,headers = HEADER_WITH_AJAX_SPRINGMVC)
	@ResponseBody
	public BackWarnEntity enableOrDisablePropertyByIds(@RequestParam("id") String id,@RequestParam("type") Integer type){

		boolean isSuccess = true;
		Integer errorCode = null;

		List<Long> ids = new ArrayList<Long>();
		ids.add(Long.parseLong(id));
		boolean result = shopManager.enableOrDisablePropertyByIds(ids, type);
		if (!result){
			isSuccess = false;
			if (type == 1){
				errorCode = ErrorCodes.PRODUCT_ENABLE_PROPERTY_FAIL;
			}else{
				errorCode = ErrorCodes.PRODUCT_DISABLE_PROPERTY_FAIL;
			}
		}

		// ******************************
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		backWarnEntity.setIsSuccess(isSuccess);
		if (null != errorCode){
			backWarnEntity.setDescription(getMessage(errorCode));
		}
		return backWarnEntity;
	}

	/**
	 * 删除属性
	 * 
	 * @param id
	 *            属性id
	 * @return
	 */
	@RequestMapping(value = { "/shop/removeProperty.json" },method = RequestMethod.POST,headers = HEADER_WITH_AJAX_SPRINGMVC)
	@ResponseBody
	public BackWarnEntity removePropertyByIds(
			@RequestParam("id") Long id,
			@RequestParam("shopId") Long shopId,
			@RequestParam("industryId") Long industryId){

		boolean isSuccess = true;
		Integer errorCode = null;

		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		boolean result = shopManager.removePropertyByIds(ids, shopId, industryId);
		if (!result){
			isSuccess = false;
		}
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		backWarnEntity.setIsSuccess(isSuccess);
		return backWarnEntity;
	}

	/**
	 * 转发到创建属性页面
	 * 
	 * @param industryId
	 *            行业id （传到页面以便创建属性的时候一并提交保存）
	 * @param industryName
	 *            行业名称（用于页面显示）
	 * @param shopName
	 *            店铺名称（用于页面显示）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/shop/createProperty.htm")
	public String createProperty(
			@RequestParam("shopId") Long shopId,
			@RequestParam("industryId") Long industryId,
			@RequestParam("industryName") String industryName,
			@RequestParam("shopName") String shopName,
			Model model){

		Integer sortNo = shopManager.findCreatePropertyDefaultSortNo(shopId, industryId);
		model.addAttribute("sortNo", sortNo);
		
		ShopCommand shopCommand = shopManager.findShopById(shopId);
		model.addAttribute("shopCode", shopCommand.getShopcode());
		model.addAttribute("shopId", shopId);
		model.addAttribute("industryId", industryId);
		model.addAttribute("industryName", industryName);
		model.addAttribute("shopName", shopName);
		return "/product/shop/shop-addproperty";

	}

	/**
	 * 保存属性
	 * 
	 * @param propery
	 *            接受要保存的model数据
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shop/saveProperty.json",method = RequestMethod.POST)
	public Object saveProperty(@ModelAttribute Property property,Model model,HttpServletRequest request){

		int type = 1;
		if (null == property.getId()){
			type = 2;
		}
		Long shopId = (Long) request.getSession().getAttribute("shopId");
		request.getSession().removeAttribute("shopId");
		boolean flag = shopManager.createOrUpdateProperty(property, shopId, type);
		if(flag){
			return SUCCESS;	
		}else{
			return FAILTRUE;
		}
		
	}

	/**
	 * 修改属性页
	 * 
	 * @param id
	 *            属性id
	 * @param industryName
	 *            行业名称
	 * @param shopName
	 *            店铺名称
	 * @param shopId
	 *            店铺id
	 * @param model
	 * @return
	 */
	@RequestMapping("/shop/updateProperty.htm")
	public String updatePropery(
			@RequestParam("id") Long id,
			@RequestParam("industryName") String industryName,
			@RequestParam("shopName") String shopName,
			@RequestParam("shopId") Long shopId,
			Model model,HttpServletRequest request){

		Long shopid = (Long) request.getSession().getAttribute("shopId");
		request.getSession().removeAttribute("shopId");
		ShopCommand shopCommand = shopManager.findShopById(shopId);
		
		Property property = shopManager.findPropertyById(id);
		model.addAttribute("property", property);
		model.addAttribute("shopId", shopid);
		model.addAttribute("shopCode", shopCommand.getShopcode());
		model.addAttribute("industryName", industryName);
		model.addAttribute("shopName", shopName);
		return "/product/shop/shop-property";
	}

	/**
	 * 显示店铺属性设置页面
	 * 
	 * @param propertyId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/propertyValueList.json")
	public String findPropertyValueList(@RequestParam("propertyId") Long propertyId,Model model,HttpServletRequest request){
		request.getSession().setAttribute("propertyId", propertyId);
		Property property = propertyManager.findPropertyById(propertyId);
		model.addAttribute("propertyName", property.getName());
		Industry industry = industryManager.findIndustryById(property.getIndustryId());
		model.addAttribute("industryName", industry.getName());
		ShopProperty shopProperty = shopManager.findShopPropertyByPropertyId(propertyId);
		ShopCommand shopCommand = shopManager.findShopById(shopProperty.getShopId());
		model.addAttribute("shopName", shopCommand.getShopname());
		List<PropertyValue> propertyValue = new ArrayList<PropertyValue>();
		propertyValue = shopManager.findPropertyValueList(propertyId);
		model.addAttribute("propertyValue", propertyValue);
		return "/product/shop/shop-property-value";

	}

	/**
	 * 新增或修改店铺属性列表
	 * 
	 * @param propertyValues
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/shop/savePropertyValue.json",method = RequestMethod.POST)
	@ResponseBody
	public Object savePropertyValueByList(
			@ArrayCommand() PropertyValue[] propertyValues,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		Long propertyId = (Long) request.getSession().getAttribute("propertyId");
		model.addAttribute("propertyId", propertyId);
		try{
			shopManager.createOrUpdatePropertyValueByList(propertyValues,propertyId);
		}catch (Exception e){
			throw new BusinessException(ErrorCodes.NAME_EXISTS);
		}
		return SUCCESS;
	}
	

	/**
	 * 判断要跟新的属性名是否重复
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shop/validatePropertyName.json")
	public BackWarnEntity validatePropertyName(@RequestParam("name") String name,@RequestParam("industryId") Integer industryId){

		boolean result = shopManager.validatePropertyName(name, industryId);
		if (!result){
			return FAILTRUE;
		}

		return SUCCESS;
	}
	
	@ResponseBody
	@RequestMapping(value = "/i18n/shop/validatePropertyName.json")
	public BackWarnEntity validatePropertyNameI18n(@RequestParam("name") String name,@RequestParam("propertyId") Integer propertyId,String lang){

		boolean result = shopManager.validatePropertyName(name, propertyId,lang);
		if (!result){
			return FAILTRUE;
		}

		return SUCCESS;
	}

}
