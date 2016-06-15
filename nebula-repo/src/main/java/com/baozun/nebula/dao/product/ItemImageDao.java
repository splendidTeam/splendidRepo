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
package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemImageLang;


/**
 * Item 图片
 * @author chenguang.zhou
 * @date 2014-1-22 下午03:29:43
 */
public interface ItemImageDao extends GenericEntityDao<ItemImage, Long>{

	/**
	 * 通过Id修改itemImage信息
	 * @param itemImage
	 * @return	:Integer
	 * @date 2014-1-22 下午03:51:32
	 */
	@NativeUpdate
	Integer updateItemImageById(
			@QueryParam("type") String type,
			@QueryParam("picUrl") String picUrl,
			@QueryParam("position") Integer position,
			@QueryParam("description") String description,
			@QueryParam("id") Long id);
	
	/**
	 * 通过itemProperties和商品Id查询itemImage集合
	 * 		:(type为3的图片信息也可以查询出来)
	 * @param paramMap
	 * @return
	 */
	@NativeQuery(model = ItemImage.class)
	List<ItemImage> findItemImageByItemPropAndItemId(@QueryParam Map<String, Object> paramMap);

	/**
	 * 通过属性值Id和商品Id删除所有的ItemImage
	 * @param propertyValueId
	 * @param itemId
	 * @return	:Integer
	 * @date 2014-1-26 上午10:01:56
	 */
	@NativeUpdate
	Integer removeAllByItemPropAndItemId(@QueryParam Map<String, Object> paramMap);
	
	/**
	 * 通过ids删除商品图片
	 * @param ids
	 * @return	:Integer
	 * @date 2014-3-5 下午05:23:08
	 */
	@NativeUpdate
	Integer removeItemImageByIds(@QueryParam("ids") List<Long> ids);
	
	@NativeUpdate
	Integer removeItemImageLangByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过商品id集合删除商品图片
	 * @param ids
	 * @return	:Integer
	 * @date 2014-3-5 下午05:23:08
	 */
	@NativeUpdate
	Integer removeItemImageByItemIds(@QueryParam("itemIds") List<Long> itemIds);
	
	/**
	 * 通过商品id集合查询商品图片
	 *
	 * @param itemIds
	 * @return
	 */
	@NativeQuery(model = ItemImage.class)
	List<ItemImage> findItemImageByItemIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("type") String type);
	
	@NativeQuery(model = ItemImage.class)
	List<ItemImage> findItemImageByItemIdsI18n(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("type") String type, @QueryParam("lang") String lang);
	/**
	 * 通过itemProperties和itemId查询商品的规格图片
	 * @param paramMap
	 * @return
	 */
	@NativeQuery(model = ItemImage.class)
	List<ItemImage> findItemImgNormsByItemIdItemProp(@QueryParam Map<String, Object> paramMap);
	
	@NativeQuery(model = ItemImage.class)
	List<ItemImage> findItemImgNormsByItemIdItemPropI18n(@QueryParam Map<String, Object> paramMap);
	
	
	@NativeUpdate
	int updateItemImageLang(@QueryParam("url") String url,@QueryParam("desc") String desc,@QueryParam("lang") String lang,@QueryParam("id")Long id);

	@NativeQuery(model = ItemImageLang.class)
	List<ItemImageLang> findItemImageLangList(@QueryParam("ids")List<Long> ids,@QueryParam("langs") List<String> langs);
	
	@NativeQuery(model = ItemImageLang.class)
	ItemImageLang findItemImageLang(@QueryParam("id")Long id,@QueryParam("lang") String lang);

	@NativeQuery(model = ItemImage.class)
	List<ItemImage> findItemImageByItemId(@QueryParam("itemId") Long itemId, @QueryParam("type") String type);
}
