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
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.model.product.ItemCollection;

/**
 * 自动打标器
 * 
 * @author D.C
 * @since 2016年4月13日 下午6:51:24
 */
public class AutoCollection {
	/**
	 * 判断某商品是否属于某collection，商品的数据需要调用者组织
	 * 
	 * @param collection
	 * @param facetParameters
	 * @return
	 */
	public static boolean apply(ItemCollection collection, ItemCollectionContext context) {
		List<FacetParameter> facetParameters = JSON.parseArray(collection.getFacetParameters(), FacetParameter.class);
		for (FacetParameter fp : facetParameters) {
			List<String> values = new ArrayList<String>();
			switch (fp.getFacetType()) {
			case CATEGORY: {
				values.addAll(context.getCategories());
				break;
			}
			case PROPERTY: {
				values.addAll(context.getProperties());
				break;
			}
			case TAG: {
				values.addAll(context.getTags());
				break;
			}
			default:
				break;
			}
			values.retainAll(fp.getValues());
			if (values.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}

/**
 * 打表器，上下文
 * 
 * @author D.C
 * @since 2016年4月14日 下午2:41:56
 */
class ItemCollectionContext {
	private Long itemId;
	/**
	 * solr中存储的结构, /也可以使用-等其他字符标识 Doc #1: /usr, /usr/local, /usr/local/apache
	 * Doc #2: /etc, /etc/apache2 Doc #3: /etc, /etc/apache2,
	 * /etc/apache2/conf.d
	 */
	private List<String> categories;
	private List<String> tags;
	private List<String> properties;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}
}