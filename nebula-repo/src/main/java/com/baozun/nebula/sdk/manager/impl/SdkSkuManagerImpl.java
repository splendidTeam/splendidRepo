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
package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.ShopSkuVirtualInventoryHandler;
import com.baozun.nebula.utilities.common.LangUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The Class SdkSkuManagerImpl.
 *
 * @author chenguang.zhou
 * @date 2014-2-17 下午08:16:47
 */
@Transactional
@Service("sdkSkuManager")
public class SdkSkuManagerImpl implements SdkSkuManager{

    /** The sku dao. */
    @Autowired
    private SkuDao                         skuDao;

    /** The property dao. */
    @Autowired
    private PropertyDao                    propertyDao;

    /** The sdk sku inventory dao. */
    @Autowired
    private SdkSkuInventoryDao             sdkSkuInventoryDao;

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager                 sdkItemManager;

    /** The shop sku virtual inventory handler. */
    @Autowired(required = false)
    private ShopSkuVirtualInventoryHandler shopSkuVirtualInventoryHandler;

    /* (non-Javadoc)
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#findSkuById(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Sku findSkuById(Long skuId){
        return skuDao.findSkuById(skuId);
    }

    /* (non-Javadoc)
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#getSkuPros(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SkuProperty> getSkuPros(String properties){
        if (StringUtils.isNotBlank(properties)){
            List<Long> ids = new Gson().fromJson(properties, new TypeToken<List<Long>>(){}.getType());
            if (null != ids && ids.size() > 0){
                List<SkuProperty> skuPros = new ArrayList<SkuProperty>();
                List<ItemProperties> itemProList = sdkItemManager.findItemPropertiesByIds(ids);
                for (ItemProperties itemPropertie : itemProList){
                    SkuProperty skuPro = new SkuProperty();
                    skuPro.setItemProperties(itemPropertie);
                    if (null != itemPropertie.getPropertyId()){
                        Property property = null;
                        if (LangProperty.getI18nOnOff()){
                            String lang = LangUtil.getCurrentLang();
                            property = propertyDao.findPropertyByIdI18n(itemPropertie.getPropertyId(), lang);
                        }else{
                            property = propertyDao.findPropertyById(itemPropertie.getPropertyId());
                        }
                        if (property != null){
                            skuPro.setpId(String.valueOf(property.getId()));
                            skuPro.setpName(StringUtils.isNotBlank(property.getName()) ? property.getName() : StringUtils.EMPTY);
                            skuPro.setIsColorProp(property.getIsColorProp());
                        }
                    }
                    if (null == itemPropertie.getPropertyValueId()){
                        skuPro.setValue(itemPropertie.getPropertyValue());
                    }else{
                        PropertyValue propertyValue = sdkItemManager.findPropertyValueById(itemPropertie.getPropertyValueId());
                        skuPro.setId(String.valueOf(propertyValue.getId()));
                        skuPro.setValue(StringUtils.isNotBlank(propertyValue.getValue()) ? propertyValue.getValue() : StringUtils.EMPTY);
                    }
                    skuPros.add(skuPro);
                }
                return skuPros;
            }
        }
        return null;
    }

    /**
 * 添加 sku inventory.
 *
 * @param extentionCode
 *            the extention code
 * @param count
 *            the count
 */
    @Override
    public void addSkuInventory(String extentionCode,Integer count){
        Integer updateCount = sdkSkuInventoryDao.addSkuInventory(extentionCode, count);
        if (updateCount < 1)
            throw new BusinessException(Constants.ADD_SKU_INVENTORY_FAILURE);
    }

    /* (non-Javadoc)
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#syncSkuPriceByExtentionCode(java.math.BigDecimal, java.math.BigDecimal, java.lang.String)
     */
    @Override
    public Integer syncSkuPriceByExtentionCode(BigDecimal salesPrice,BigDecimal listPrice,String extentionCode){
        return skuDao.syncSkuPriceByExtentionCode(salesPrice, listPrice, extentionCode);
    }

    /* (non-Javadoc)
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#updateSkuPriceByItemCode(java.math.BigDecimal, java.math.BigDecimal, java.lang.String)
     */
    @Override
    public Integer updateSkuPriceByItemCode(BigDecimal salesPrice,BigDecimal listPrice,String itemCode){
        return skuDao.updateSkuPriceByItemCode(salesPrice, listPrice, itemCode);
    }

    /* (non-Javadoc)
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#findSkuByOutIds(java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Sku> findSkuByOutIds(List<String> outIdList){
        return skuDao.findSkuByExtentionCodes(outIdList);
    }

    /* (non-Javadoc)
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#findSkuByExtentionCode(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Sku findSkuByExtentionCode(String extentionCode){
        return skuDao.findSkuByExtentionCode(extentionCode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#findSkuQSVirtualInventoryById(java.lang.Long, java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public SkuCommand findSkuQSVirtualInventoryById(Long skuId,String extCode){
        SkuCommand skuCommand = null;
        if (null != shopSkuVirtualInventoryHandler){
            skuCommand = shopSkuVirtualInventoryHandler.findSkuQSVirtualInventoryById(skuId, extCode);
        }
        if (skuCommand == null){
            skuCommand = skuDao.findInventoryById(skuId);
        }
        return skuCommand;
    }
}
