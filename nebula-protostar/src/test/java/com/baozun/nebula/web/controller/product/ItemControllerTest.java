package com.baozun.nebula.web.controller.product;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.DynamicPropertyCommand;

public class ItemControllerTest {
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ItemControllerTest.class);

	private ItemController	itemController;

	private IMocksControl		control;
	
	private HttpServletRequest	request;

	private Model				model;

   //manager
	private IndustryManager		industryManager;
    
	private CategoryManager		categoryManager;
    
    private ItemManager itemManager;
    
    private ShopManager shopManager;
	
    
    private ItemCategoryManager itemCategoryManager;


	
	@Before
	public void init(){
		itemController = new ItemController();
		control = createNiceControl();
		// mock一个categoryManager对象
		industryManager = control.createMock("industryManager", IndustryManager.class);
		categoryManager = control.createMock("categoryManager", CategoryManager.class);
		itemManager = control.createMock("itemManager", ItemManager.class);
		shopManager = control.createMock("shopManager", ShopManager.class);
		itemCategoryManager = control.createMock("itemCategoryManager", ItemCategoryManager.class);
		
		ReflectionTestUtils.setField(itemController, "industryManager", industryManager);
		ReflectionTestUtils.setField(itemController, "categoryManager", categoryManager);
		ReflectionTestUtils.setField(itemController, "itemManager", itemManager);
		ReflectionTestUtils.setField(itemController, "shopManager", shopManager);
		ReflectionTestUtils.setField(itemController, "itemCategoryManager", itemCategoryManager);
	
		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		model = control.createMock("model", Model.class);
	}

//	@Test
//	public final void testCreateItem() {
//		List<Industry> industryList = new ArrayList<Industry>();
//		List<Category> categoryList = new ArrayList<Category>();
//		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
//		EasyMock.expect(industryManager.findAllIndustryList()).andReturn(industryList);
//		EasyMock.expect(categoryManager.findEnableCategoryList(sorts)).andReturn(categoryList);
//		EasyMock.expect(model.addAttribute(EasyMock.eq("industryList"), EasyMock.eq(industryList))).andReturn(model);
//		EasyMock.expect(model.addAttribute(EasyMock.eq("categoryList"), EasyMock.eq(categoryList))).andReturn(model);
//		control.replay();
//		assertEquals("/product/item/add-item", itemController.createItem(model));
//	}

	@Test
	public final void testFindDynamicPropertis() {
		List<DynamicPropertyCommand> dynamicPropertyCommandList = new ArrayList<DynamicPropertyCommand>();
		//EasyMock.expect(itemController.findDynamicPropertisByShopIdAndIndustryId(66L)).andReturn(dynamicPropertyCommandList);
	}

	@Test
	public final void testSaveItem() throws Exception {
		Item item = new Item();
		ItemCommand itemCommand = new ItemCommand();
		itemCommand.setTitle("可乐");
		itemCommand.setCode("2-33");
		itemCommand.setIndustryId("336");
		Map<String, Object> map = new HashMap<String, Object>();
		Long[] propertyValueIds= new Long[0];
		Long[] categoriesIds= new Long[1];
		categoriesIds[0]=157L;
		String[] codes=new  String[0];
		ItemProperties[] iProperties=new ItemProperties[1];
		//iProperties[0].setPropertyId(549L);
		//iProperties[0].setPropertyValueId(655L);
		Long shopid =256L;
//		EasyMock.expect(itemManager.createOrUpdateItem(itemCommand,shopid,categoriesIds,propertyValueIds,codes,iProperties)).andReturn(item);
//		control.replay();
		//assertEquals(map, itemController.saveItem(itemCommand, propertyValueIds, categoriesIds, codes, iProperties)).Object());
		
	}

	@Test
	public final void testUpdateItem() {
		  //分类列表
		List<Category> categoryList =  new ArrayList<Category>();
		Industry industry = new Industry();
		ItemInfo itemInfo = new ItemInfo();
		List<ItemProperties> itemProperties  = new 	ArrayList<ItemProperties>();
		List<ItemCategory> itemCategoryList =  new ArrayList<ItemCategory>();
		List<DynamicPropertyCommand> dynamicPropertyCommandList = new ArrayList<DynamicPropertyCommand>();
		Item item = new Item();
		Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
		EasyMock.expect(categoryManager.findEnableCategoryList(sorts)).andReturn(categoryList);
	    EasyMock.expect(model.addAttribute(EasyMock.eq("categoryList"), EasyMock.eq(categoryList))).andReturn(model);
	    
	    EasyMock.expect(itemManager.findItemById(1982L)).andReturn(item);
	    //根据行业Id查找行业
	    EasyMock.expect(industryManager.findIndustryById(336L)).andReturn(industry);
		
		//查找商品名称、商品描述
		EasyMock.expect(itemManager.findItemInfoByItemId(1982L)).andReturn(itemInfo);
			
		EasyMock.expect(itemCategoryManager.findItemCategoryListByItemId(1982L)).andReturn(itemCategoryList);
		
		EasyMock.expect(itemManager.findItemPropertiesListyByItemId(1982L)).andReturn(itemProperties);
		
		control.replay();
		//assertEquals("/product/item/update-item", itemController.updateItem(model, "1969"));
	}

	@Test
	public final void testValidateShopCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer mockResult=1;
		EasyMock.expect(itemManager.validateItemCode("d",256L)).andReturn(mockResult);
	}

	@Test
	public final void testItemList() {
		List<Industry> result=new ArrayList<Industry>();
		EasyMock.expect(industryManager.findAllIndustryList()).andReturn(result);
		control.replay();
		// 验证
        assertEquals("/product/item/item-List",itemController.itemList(model));
	}

	@Test
	public final void testFindItemListJson() {
	Map<String,Object> paraMap	= new HashMap<String, Object>();
		Sort[] sorts = new Sort[]{};
		Pagination<ItemCommand> mockResult = new Pagination<ItemCommand>();
		QueryBean queryBean = new QueryBean();
		Page page = new Page(1,5);
		queryBean.setPage(page);
		mockResult.setCount(7);
		mockResult.setSize(5);
		mockResult.setStart(0);
		List<ItemCommand> items = new ArrayList<ItemCommand>();
		ItemCommand ic = new ItemCommand();
		ic.setId(7L);
		ic.setCode("code");
		ic.setTitle("xxx");
		ic.setIndustryName("笔记本");
		items.add(ic);
		mockResult.setItems(items);
		
		Long shopId=256L;
		
		EasyMock.expect(itemManager.findItemListByQueryMap(new Page(), sorts, paraMap, shopId)).andReturn(mockResult);
		
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals(mockResult, itemController.findItemListJson(model, queryBean));
		
	}


	@Test
	public final void testEnableOrDisableMemberByIds() {
		Long itemId= 2088L;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result","success");
		List<Long> list = new ArrayList<Long>();
		list.add(itemId);
		Integer state=2;
		Integer mockResult = 1;
		EasyMock.expect(itemManager.enableOrDisableItemByIds(list, state,"xxx")).andReturn(mockResult);
		control.replay();
		try {
			assertEquals(map, itemController.enableOrDisableItemById(itemId, state));
		} catch (Exception e) {
			e.printStackTrace();
		}
		control.verify();
	}


}
