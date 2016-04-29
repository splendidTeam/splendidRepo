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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.model.product.ItemCollection;

/**
 * @author D.C
 * @since 2016年4月14日 下午4:00:48
 */
public class AutoCollectionTest {
	ItemCollection collection = new ItemCollection();
	ItemCollectionContext context = new ItemCollectionContext();

	@Before
	public void init() {
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();
		FacetParameter facetParameterCategory1 = new FacetParameter(FacetType.CATEGORY.name(),
				"/usr|/usr/local|/usr/local/apache");
		facetParameterCategory1.setFacetType(FacetType.CATEGORY);
		FacetParameter facetParameterCategory2 = new FacetParameter(FacetType.CATEGORY.name(), "/etc|/etc/apache2");
		facetParameterCategory2.setFacetType(FacetType.CATEGORY);
		FacetParameter facetParameterTags = new FacetParameter(FacetType.TAG.name(), "a|b");
		facetParameterTags.setFacetType(FacetType.TAG);
		FacetParameter facetParameterProperties = new FacetParameter(FacetType.PROPERTY.name() + "_1", "1|2");
		facetParameterProperties.setFacetType(FacetType.PROPERTY);
		//facetParameters.add(facetParameterCategory1);
		facetParameters.add(facetParameterCategory2);
		//facetParameters.add(facetParameterTags);
	//	facetParameters.add(facetParameterProperties);
		collection.setFacetParameters(JSON.toJSONString(facetParameters));

		context.setItemId(1L);
		context.setCategories(Arrays.asList(new String[] { "/etc", "/etc/apache2" }));
		context.setTags(Arrays.asList(new String[] { "hot", "new" }));

		context.setProperties(Arrays.asList(new String[] { "1", "2" }));
	}

	@Test
	public void testAutoCollection() {
		AutoCollection.apply(collection, context);
	}

}
