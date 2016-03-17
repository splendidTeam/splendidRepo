/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.ItemSortScoreCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemSortScoreDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemSortScore;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemSortScoreManager;
import com.baozun.nebula.solr.utils.Validator;

/**
 * ItemSortScoreManager
 * @author  Justin
 *
 */
@Transactional
@Service("sdkItemSortScoreManager") 
public class SdkItemSortScoreManagerImpl implements SdkItemSortScoreManager {

	@Autowired
	private ItemSortScoreDao itemSortScoreDao;

	@Autowired
	private  PropertyDao propertyDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ChooseOptionDao chooseOptionDao;
	/**
	 * 保存ItemSortScore
	 * 
	 */
	public ItemSortScore saveItemSortScore(ItemSortScore model){
	    Long id = model.getId();
		if (id != null) {
			ItemSortScore sortScore = findItemSortScoreById(id);
			sortScore.setCategoryId(model.getCategoryId());
			sortScore.setOperator(model.getOperator());
			sortScore.setParam(model.getParam());
			sortScore.setPropertyId(model.getPropertyId());
			sortScore.setScore(model.getScore());
			sortScore.setType(model.getType());
			return itemSortScoreDao.save(sortScore);
		}else{
			model.setLifecycle(1);
		}
		return itemSortScoreDao.save(model);
	}
	
	/**
	 * 通过id获取ItemSortScore
	 * 
	 */
	@Transactional(readOnly=true)
	public ItemSortScore findItemSortScoreById(Long id){
	
		return itemSortScoreDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有ItemSortScore列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemSortScore> findAllItemSortScoreList(){
	
		return itemSortScoreDao.findAllItemSortScoreList();
	};
	
	/**
	 * 通过ids获取ItemSortScore列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemSortScore> findItemSortScoreListByIds(List<Long> ids){
	
		return itemSortScoreDao.findItemSortScoreListByIds(ids);
	};
	
	/**
	 * 通过参数map获取ItemSortScore列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemSortScore> findItemSortScoreListByQueryMap(Map<String, Object> paraMap){
	
		return itemSortScoreDao.findItemSortScoreListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取ItemSortScore列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<ItemSortScore> findItemSortScoreListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return itemSortScoreDao.findItemSortScoreListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemSortScore
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableItemSortScoreByIds(List<Long> ids,Integer state){
		itemSortScoreDao.enableOrDisableItemSortScoreByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除ItemSortScore
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeItemSortScoreByIds(List<Long> ids){
		itemSortScoreDao.removeItemSortScoreByIds(ids);
	}
	
	
	/**
	 * 获取有效的ItemSortScore列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemSortScore> findAllEffectItemSortScoreList(){
	
		return itemSortScoreDao.findAllEffectItemSortScoreList();
	};
	
	/**
	 * 通过参数map获取有效的ItemSortScore列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemSortScore> findEffectItemSortScoreListByQueryMap(Map<String, Object> paraMap){
	
		return itemSortScoreDao.findEffectItemSortScoreListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的ItemSortScore列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<ItemSortScore> findEffectItemSortScoreListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return itemSortScoreDao.findEffectItemSortScoreListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	@Override
	@Transactional(readOnly=true)
	public Pagination<ItemSortScoreCommand> findEffectItemSortCommandScoreListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		Pagination<ItemSortScore> pagination = itemSortScoreDao
				.findEffectItemSortScoreListByQueryMapWithPage(page, sorts,
						paraMap);

		List<ItemSortScoreCommand> itemSortScoreCommands = itemSortScoreModelToCommand(pagination
				.getItems());
		Pagination<ItemSortScoreCommand> pagination2 = new Pagination<ItemSortScoreCommand>();
		pagination2.setItems(itemSortScoreCommands);
		pagination2.setCount(pagination.getCount());
		pagination2.setCurrentPage(pagination.getCurrentPage());
		pagination2.setSize(pagination.getSize());
		pagination2.setSortStr(pagination.getSortStr());
		pagination2.setStart(pagination.getStart());
		pagination2.setTotalPages(pagination.getTotalPages());
		return pagination2;
	}
	
	@Transactional(readOnly=true)
	private List<ItemSortScoreCommand> itemSortScoreModelToCommand(
			List<ItemSortScore> list) {
		if (Validator.isNullOrEmpty(list)) {
			return null;
		}
		List<ItemSortScoreCommand> items = new ArrayList<ItemSortScoreCommand>();
		for (ItemSortScore itemSortScore : list) {
			ItemSortScoreCommand item = new ItemSortScoreCommand();
			BeanUtils.copyProperties(itemSortScore, item);
			Long proId = itemSortScore.getPropertyId();
			if (proId != null) {
              Property property =propertyDao.findPropertyById(proId);
              if(property!=null){
            	  item.setPropertyName(property.getName());
              }
			}
			Long cateId = itemSortScore.getCategoryId();
			if (cateId != null) {
			 Category category = categoryDao.findCategoryById(cateId);
			 if(category!=null){
				 item.setCategoryName(category.getName()); 
			 }
			}
			Integer type = itemSortScore.getType();
			String oper = itemSortScore.getOperator();
			String param = itemSortScore.getParam();
			if (type == 1) {
				item.setOperatorText(typeToName(type)+operToName(Constants.SORT_SCORE_OPER_EQUAL)+ item.getCategoryName());
			}else if(type == 2){
				String txt = typeToName(type)+operToName(Constants.SORT_SCORE_OPER_EQUAL)+ item.getPropertyName() +" 且参数 ";
				if(Constants.SORT_SCORE_OPER_EQUAL.equals(oper)){
					txt = txt+operToName(oper)+ param;
				}
				if(Constants.SORT_SCORE_OPER_GREATER.equals(oper)){
					txt = txt+ operToName(oper)+ param;
				}
				if(Constants.SORT_SCORE_OPER_LESS.equals(oper)){
					txt = txt+operToName(oper)+ param;
				}
				item.setOperatorText(txt);
			}else{
				if(Constants.SORT_SCORE_OPER_EQUAL.equals(oper)){
					item.setOperatorText(typeToName(type)+operToName(oper)+ param);
				}
				if(Constants.SORT_SCORE_OPER_GREATER.equals(oper)){
					item.setOperatorText(typeToName(type)+operToName(oper)+ param);
				}
				if(Constants.SORT_SCORE_OPER_LESS.equals(oper)){
					item.setOperatorText(typeToName(type)+operToName(oper)+ param);
				}
			}
			items.add(item);
		}
		return items;
	}
	private String operToName(String oper) {
		ChooseOption option = chooseOptionDao.validateOptionValue(
				"ITEM_SORT_OPER", oper);
		if(option ==null){
			return  "";
		}
		return " "+option.getOptionLabel()+" ";
	}
	private String typeToName(Integer type) {
		ChooseOption option = chooseOptionDao.validateOptionValue(
				"ITEM_SORT_SCORE", String.valueOf(type));
		if(option ==null){
			return  "";
		}
		return " "+option.getOptionLabel()+" ";
	}
	
}
