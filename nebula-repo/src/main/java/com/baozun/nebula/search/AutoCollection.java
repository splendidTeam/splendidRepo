/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.model.product.ItemCollection;
import com.feilong.core.Validator;

/**
 * 自动打标器
 * 
 * @author D.C
 * @since 2016年4月13日 下午6:51:24
 */
public class AutoCollection {
	
	public static final String MAP_KEY_CATEGORY 	="c";
	public static final String MAP_KEY_PROPERTY 	="p";
	public static final String MAP_KEY_TAG			="t";
	
	/**
	 * 判断某商品是否属于某collection，商品的数据需要调用者组织
	 * 
	 * @param collection
	 * @param facetParameters
	 * @return
	 */
	public static boolean fuzzyApply(ItemCollection collection, ItemCollectionContext context) {
		List<FacetParameter> facetParameters = JSON.parseArray(collection.getFacetParameters(), FacetParameter.class);
		boolean isApplied =false;
		outer : for (FacetParameter fp : facetParameters) {
			List<String> values = new ArrayList<String>();
			List<String> origValues = new ArrayList<String>();
			switch (fp.getFacetType()) {
			case CATEGORY: {
				values.addAll(context.getCategories());
				origValues.addAll(context.getCategories());
				break;
			}
			case PROPERTY: {
				values.addAll(context.getProperties());
				origValues.addAll(context.getProperties());
				break;
			}
			case TAG: {
				values.addAll(context.getTags());
				origValues.addAll(context.getTags());
				break;
			}
			default:
				break;
			}
			//取交际过后集合未发生改变即说明collection包含了商品的关联
			values.retainAll(fp.getValues());
			if (!values.isEmpty()) {
				for (String value : origValues) {
					if(!fp.getValues().contains(value)){
						continue outer;
					}
				}
				isApplied =true;
				break ;
			}
		}
		return isApplied;
	}
	
	public static boolean exactApply(ItemCollection collection, ItemCollectionContext context){
		boolean isHasCategory =Validator.isNotNullOrEmpty(context.getCategories());
		boolean isHasProperty =Validator.isNotNullOrEmpty(context.getProperties());
		boolean isHasTag =Validator.isNotNullOrEmpty(context.getTags());
		List<FacetParameter> facetParameters = JSON.parseArray(collection.getFacetParameters(), FacetParameter.class);
		//将collection里的facetParameters归为3类
		Map<String,List<String>> reCollectionMap =extractMapByFacetParameter(facetParameters);
		//开始一类一类匹配
		boolean isApplyCategory =false;
		boolean isApplyProperty =false;
		boolean isApplyTag =false;
		for (Map.Entry<String,List<String>> entry : reCollectionMap.entrySet()) {
			String key =entry.getKey();
			List<String> values = new ArrayList<String>();
			List<String> origValues = new ArrayList<String>();
			switch (key) {
			case MAP_KEY_CATEGORY: {
				values.addAll(context.getCategories());
				origValues.addAll(context.getCategories());
				break;
			}
			case MAP_KEY_PROPERTY: {
				values.addAll(context.getProperties());
				origValues.addAll(context.getProperties());
				break;
			}
			case MAP_KEY_TAG: {
				values.addAll(context.getTags());
				origValues.addAll(context.getTags());
				break;
			}
			default:
				break;
			}
			values.retainAll(entry.getValue());
			if (!values.isEmpty()) {
				boolean isContainAll =true;
				for (String value : origValues) {
					if(!entry.getValue().contains(value)){
						isContainAll =false;
					}
				}
				if(isContainAll){
					switch (key) {
					case MAP_KEY_CATEGORY: {
						isApplyCategory =true;
						break;
					}
					case MAP_KEY_PROPERTY: {
						isApplyProperty =true;
						break;
					}
					case MAP_KEY_TAG: {
						isApplyTag =true;
						break;
					}
					default:
						break;
					}
				}
			}
		}
		if(isHasCategory){
			if(!isApplyCategory){
				return false;
			}
		}
		if(isHasProperty){
			if(!isApplyProperty){
				return false;
			}
		}
		if(isHasTag){
			if(!isApplyTag){
				return false;
			}
		}
		return true;
	}

	/**
	 * 将facetParameters 归为3类
	 * @param facetParameters
	 */
	private static Map<String,List<String>> extractMapByFacetParameter(
			List<FacetParameter> facetParameters) {
		Map<String,List<String>> reCollectionMap =new HashMap<String,List<String>>();
		List<String> reCollectionList =null;
		for (FacetParameter fp : facetParameters) {
			String key ="";
			reCollectionList =new ArrayList<String>();
			switch (fp.getFacetType()) {
				case CATEGORY: {
					key =MAP_KEY_CATEGORY;
					break;
				}
				case PROPERTY: {
					key =MAP_KEY_PROPERTY;
					break;
				}
				case TAG: {
					key =MAP_KEY_TAG;
					break;
				}
				default:
					break;
			}
			reCollectionList =new ArrayList<String>();
			if(Validator.isNotNullOrEmpty(reCollectionMap)&&
					Validator.isNotNullOrEmpty(reCollectionMap.get(key))){
				reCollectionList =reCollectionMap.get(key);
			}
			reCollectionList.addAll(fp.getValues());
			reCollectionMap.put(key, reCollectionList);
		}
		return reCollectionMap;
	}
}
