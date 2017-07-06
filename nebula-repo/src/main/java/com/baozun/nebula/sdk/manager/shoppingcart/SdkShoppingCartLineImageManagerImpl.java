/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.feilong.core.Validator;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月26日 下午8:17:56
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartLineImageManager")
public class SdkShoppingCartLineImageManagerImpl implements SdkShoppingCartLineImageManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkShoppingCartLineImageManagerImpl.class);

    @Autowired
    private SdkItemManager      sdkItemManager;
    
    @Autowired
    private PropertyDao			propertyDao;
    
    @Autowired
    private ItemPropertiesDao   itemPropertiesDao;
    
    @Autowired
    private SkuDao				skuDao;
    
    @Autowired
    private SdkSkuManager		sdkSkuManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineImageManager#getItemPicUrl(java.lang.Long)
     */
    @Override
    public String getItemPicUrl(Long itemId){
        List<Long> itemIds = new ArrayList<Long>(1);
        itemIds.add(itemId);

        List<ItemImageCommand> itemImageCommandList = sdkItemManager.findItemImagesByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);

        if (Validator.isNotNullOrEmpty(itemImageCommandList)){
            ItemImageCommand itemImageCommand = itemImageCommandList.get(0);
            if (Validator.isNotNullOrEmpty(itemImageCommand)){
                List<ItemImage> imgList = itemImageCommand.getItemIamgeList();

                if (Validator.isNotNullOrEmpty(imgList)){
                    return imgList.get(0).getPicUrl();
                }
            }
        }
        return null;
    }
    
    @Override
	public String getSkuPicUrl(Long skuId) {
		Sku sku = skuDao.findSkuById(skuId);
		long colorItemPropertiesID = 0;
		List<SkuProperty> skuPros = sdkSkuManager.getSkuPros(sku.getProperties());
		for (SkuProperty skuPro : skuPros) {
			if (skuPro.getIsColorProp()) {
				colorItemPropertiesID = Long.valueOf(skuPro.getId());
				break;
			}
		}

		// 取该颜色的商品的图片
		List<ItemImageCommand> itemImageCommandList = sdkItemManager.findItemImagesByItemIds(Arrays.asList(sku.getItemId()),
				ItemImage.IMG_TYPE_LIST);
				
		for (ItemImageCommand itemImageCommand : itemImageCommandList) {
			for (ItemImage itemImage : itemImageCommand.getItemIamgeList()) {
				if (colorItemPropertiesID ==  itemImage.getItemProperties()) {
					return itemImage.getPicUrl();
				}
			}
		}
		return "";
	}
}
