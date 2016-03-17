package com.baozun.nebula.web.controller.product;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.mapping.Array;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.ItemCategoryResultCommand;


public class ItemCategoryControllerTest{

	private ItemCategoryController	itemCategoryController;
	
	private IMocksControl 			control;
	
	private ItemCategoryManager 	itemCategoryManager;
	
	private CategoryManager			categoryManager;
	private IndustryManager 		industryManager;
	
	private QueryBean				queryBean;
	
	private HttpServletRequest 		request;
	private HttpServletResponse		response;
	
	private Model					model;
	
	@Before
	public void init(){
		
		itemCategoryController = new ItemCategoryController();
		control = createNiceControl();
		queryBean =new QueryBean();
		
		
		itemCategoryManager = control.createMock("itemCategoryManager", ItemCategoryManager.class);
		ReflectionTestUtils.setField(itemCategoryController, "itemCategoryManager", itemCategoryManager);
		
		
		categoryManager	= control.createMock("categoryManager", CategoryManager.class);
		ReflectionTestUtils.setField(itemCategoryController, "categoryManager", categoryManager);
		
		industryManager	= control.createMock("industryManager", IndustryManager.class);
		ReflectionTestUtils.setField(itemCategoryController, "industryManager", industryManager);
		
		/*queryBean = control.createMock("QueryBean", QueryBean.class);
		ReflectionTestUtils
				.setField(itemCategoryController, "queryBean", queryBean);*/
		
		request	= control.createMock("HttpServletRequest",HttpServletRequest.class);
		response = control.createMock("HttpServletResponse",HttpServletResponse.class);
		
		model = control.createMock("model", Model.class);
	}
	
	@Test
	public void testItemCategory(){
		List<Category> categoryList = new ArrayList<Category>();
		List<Industry> industryList = new ArrayList<Industry>();
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		
		
		
		EasyMock.expect(categoryManager.findEnableCategoryList(sorts)).andReturn(categoryList);
		EasyMock.expect(model.addAttribute(EasyMock.eq("categoryList"), EasyMock.eq(categoryList))).andReturn(model);
		
		EasyMock.expect(industryManager.findAllIndustryList()).andReturn(industryList);
		EasyMock.expect(model.addAttribute(EasyMock.eq("industryList"), EasyMock.eq(industryList))).andReturn(model);
		
		control.replay();
		assertEquals("/product/item/item-category", itemCategoryController.itemCategory(model));
	}
	
	
	//?????
	
	@Test
	public void testFindItemCtListJson(){
		Map<String,Object> paraMap	= new HashMap<String, Object>();
		
		
		Sort[] sorts = new Sort[]{};
		
		Pagination<ItemCommand> mockResult = new Pagination<ItemCommand>();
		
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
		
		EasyMock.expect(itemCategoryManager.findItemCtListByQueryMapWithPage(new Page(), sorts, paraMap,shopId)).andReturn(mockResult);
		
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals(mockResult, itemCategoryController.findItemCtListJson(model, queryBean, request, response));
	}
	@Test
	public void testFindNoctItemListJson(){
		QueryBean queryBean = new QueryBean();
		Map<String,Object> paraMap	= new HashMap<String, Object>();
		paraMap.put("lifecycle", "1");
		queryBean.setParaMap(paraMap);
		
		Page page = new Page(0, 100);
		queryBean.setPage(page);
		
		Sort[] sorts = null;
		queryBean.setSorts(sorts);
		
		Pagination<ItemCommand> mockResult = new Pagination<ItemCommand>();
		
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
		
		EasyMock.expect(itemCategoryManager.findItemNoctListByQueryMapWithPage(queryBean.getPage(), queryBean.getSorts(), queryBean.getParaMap(),shopId)).andReturn(mockResult);
		
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		//assertEquals(mockResult, itemCategoryController.findNoctItemListJson(model, queryBean, request, response));
	}
	
	@Test
	public void testBindItemCategory(){
		ItemCategoryResultCommand mockResult = new ItemCategoryResultCommand();
		Map<String,List<Item>> successMap = new HashMap<String, List<Item>>();
		List<Item> itemList = new ArrayList<Item>();
		Item item = new Item();
		item.setCode("11");
		item.setId(7L);
		item.setLifecycle(1);
		itemList.add(item);
		successMap.put("冰冰冰", itemList);
		mockResult.setSuccessMap(successMap);
		mockResult.setFailMap(successMap);
		mockResult.setRepeatMap(successMap);
		
		Long[] itemIds=new Long[]{7L};
		Long[] categoryIds=new Long[]{157L};
		
		EasyMock.expect(itemCategoryManager.bindItemCategory(itemIds, categoryIds)).andReturn(mockResult);
	}
	
	@Test
	public void testUnBindItemCategory(){
		
		Long[] itemIds=new Long[]{7L};
		Long categoryId=157L;
		
		EasyMock.expect(itemCategoryManager.unBindItemCategory(itemIds, categoryId)).andReturn(true);
		
		control.replay();
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("result", "success");
		
		try{
			assertEquals(result,itemCategoryController.unBindItemCategory(itemIds, categoryId, model, request, response));
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
