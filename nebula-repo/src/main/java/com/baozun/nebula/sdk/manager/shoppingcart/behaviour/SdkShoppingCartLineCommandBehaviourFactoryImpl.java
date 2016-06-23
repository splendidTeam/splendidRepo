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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class SdkShoppingCartLineCommandBehaviourFactoryImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Service("sdkShoppingCartLineCommandBehaviourFactory")
public class SdkShoppingCartLineCommandBehaviourFactoryImpl implements SdkShoppingCartLineCommandBehaviourFactory{

    @Autowired
    @Qualifier("shoppingCartLineCommandBundleBehaviour")
    private ShoppingCartLineCommandBehaviour shoppingCartLineCommandBundleBehaviour;

    @Autowired
    @Qualifier("shoppingCartLineCommandCommonBehaviour")
    private ShoppingCartLineCommandBehaviour shoppingCartLineCommandCommonBehaviour;

    @Autowired
    private ItemDao                          itemDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineCommandBehaviourFactory#create(com.baozun.nebula.sdk.command.
     * shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public ShoppingCartLineCommandBehaviour getShoppingCartLineCommandBehaviour(ShoppingCartLineCommand shoppingCartLineCommand){
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        if (null == relatedItemId){
            return shoppingCartLineCommandCommonBehaviour;
        }

        Item item = itemDao.findItemById(relatedItemId);
        Validate.notNull(item, "when relatedItemId:[%s],item is null!", relatedItemId);

        //bundle item?
        Integer type = item.getType();
        if (Item.ITEM_TYPE_BUNDLE.equals(type)){
            return shoppingCartLineCommandBundleBehaviour;
        }
        //XXX feilong 将来 1 可以在这里扩展, 2可能换成默认 实现 shoppingCartLineCommandCommonBehaviour
        throw new UnsupportedOperationException(Slf4jUtil.format("item:[{}],type is :[{}],not support", relatedItemId, type));
    }
}
