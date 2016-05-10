package com.baozun.nebula.manager.salesorder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baozun.nebula.command.promotion.PromotionCouponInfoCommand;
import com.baozun.nebula.dao.coupon.CouponDao;
import com.baozun.nebula.dao.coupon.CouponTypeDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.ItemTagRelationDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.model.coupon.Coupon;
import com.baozun.nebula.model.coupon.CouponType;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemTagRelation;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SimpleOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.web.command.OrderQueryCommand;
import com.feilong.core.bean.BeanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Transactional
@Service("SalesOrderManager")
public class SalesOrderManagerImpl implements SalesOrderManager {

    @Autowired
    private OrderManager sdkOrderService;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemInfoDao itemInfoDao;

    @Autowired
    private ItemCategoryDao itemCategoryDao;

    @Autowired
    private ItemTagRelationDao itemTagRelationDao;

    @Autowired
    private ItemPropertiesDao itemPropertiesDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private PropertyValueDao propertyValueDao;

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private CouponTypeDao couponTypeDao;

    @Autowired
    private PromotionCouponCodeDao promotionCouponCodeDao;

    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    @Autowired
    private SdkShoppingCartManager sdkShoppingCartManager;

    @Autowired
    private SdkOrderDao sdkOrderDao;

    @Autowired
    private SdkOrderPromotionDao sdkOrderPromotionDao;

    @Override
    public String createOrder(SalesOrderCommand salesOrderCommand, Set<String> memComboIds,
            List<ShoppingCartLineCommand> lines) {

        // 订单概要表
        ShoppingCartCommand shoppingCartCommand = null;
        // if(salesOrderCommand.getIsImmediatelyBuy()){
        // shoppingCartCommand
        // =sdkShoppingCartManager.findShoppingCart(salesOrderCommand.getMemberId(),memComboIds,
        // salesOrderCommand.getCouponCodes(),salesOrderCommand.getCalcFreightCommand(),lines);
        // }else{
        // shoppingCartCommand =
        // sdkShoppingCartManager.findShoppingCart(salesOrderCommand.getMemberId(),memComboIds,
        // salesOrderCommand.getCouponCodes(),salesOrderCommand.getCalcFreightCommand(),lines);
        // }

        String orderCode = sdkOrderService.saveOrder(shoppingCartCommand, salesOrderCommand, memComboIds);

        return orderCode;
    }

    @Override
    public List<OrderPromotionCommand> findOrderPormots(String orderCode) {
        return sdkOrderService.findOrderPormots(orderCode);
    }

    @Override
    public PromotionCouponCode validCoupon(String couponCode) {
        return sdkOrderService.validCoupon(couponCode);
    }

    @Override
    public List<OrderLineCommand> findOrderLineList(String skuId, String count, String ids) {

        List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();

        if (StringUtils.isNotBlank(skuId)) {
            // 立即购买--一条清单

            OrderLineCommand lineCommand = getOrderLineFromPruductDetail(Long.valueOf(skuId),
                    Integer.valueOf(count));
            orderLineList.add(lineCommand);

        } else {
            // 购物车-去结算--多条清单
            if (StringUtils.isNotBlank(ids)) {
                String[] idArray = ids.split(",");
                List<Long> idList = new ArrayList<Long>();
                for (String sid : idArray) {
                    idList.add(Long.parseLong(sid));
                }

                orderLineList = getOrderLineListFromShoppingCart(idList);

            }
        }
        return orderLineList;
    }

    // @Override
    // public ShoppingCartCommand findShoppingCartList(Long memberId,Integer
    // callType,HttpServletRequest request,List<String> coupons) {
    // return
    // sdkShoppingCartManager.findShoppingCart(memberId,callType,request,coupons,
    // new CalcFreightCommand());
    // }
    /**
     * 
     * @param skuId
     * @param count购买数量
     * @return
     */

    public OrderLineCommand getOrderLineFromPruductDetail(Long skuId, Integer count) {
        // List<OrderLineCommand> orderLineList =new
        // ArrayList<OrderLineCommand>();

        OrderLineCommand lineCommand = new OrderLineCommand();

        // 数量
        lineCommand.setCount(count);
        // sku信息
        Sku sku = skuDao.findSkuById(skuId);
        lineCommand.setItemId(sku.getItemId());
        lineCommand.setExtentionCode(sku.getOutid());
        lineCommand.setSaleProperty(convertSaleProp(sku.getPropertiesName()));
        lineCommand.setSalePrice(sku.getSalePrice());

        /*
         * //sku-inventory信息 SkuInventory inventory
         * =inventoryDao.findSkuInventoryByExtentionCode(sku.getOutid());
         * 
         * lineCommand.set
         */

        // item
        Item item = itemDao.findItemById(sku.getItemId());
        lineCommand.setIndstryId(item.getIndustryId());
        lineCommand.setItemPic(item.getPicUrl());

        // itemInfo
        ItemInfo itemInfo = itemInfoDao.findItemInfoByItemId(sku.getItemId());
        lineCommand.setItemName(itemInfo.getTitle());

        // itemCategory

        List<ItemCategory> categoryLists = itemCategoryDao.findItemCategoryListByItemId(sku.getItemId());
        if (categoryLists != null && categoryLists.size() > 0) {
            List<Long> categoryList = new ArrayList<Long>();
            for (ItemCategory itemCategory : categoryLists) {
                categoryList.add(itemCategory.getCategoryId());
            }
            lineCommand.setCategoryList(categoryList);
        }
        // itemTag
        List<ItemTagRelation> lableTagIds = itemTagRelationDao
                .findItemTagRelationListByItemId(sku.getItemId());
        if (lableTagIds != null && lableTagIds.size() > 0) {
            List<Long> lableIds = new ArrayList<Long>();
            for (ItemTagRelation itemTagRelation : lableTagIds) {
                lableIds.add(itemTagRelation.getTagId());
            }
            lineCommand.setLableIds(lableIds);
        }

        // orderLineList.add(lineCommand);

        return lineCommand;
    }

    public List<OrderLineCommand> getOrderLineListFromShoppingCart(List<Long> idList) {
        List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();

        List<ShoppingCartLineCommand> shoppingCartLineCommands = sdkShoppingCartLineDao
                .findShopCartLineByIds(idList);

        OrderLineCommand orderLineCommand = null;

        for (ShoppingCartLineCommand cartLineCommand : shoppingCartLineCommands) {
            orderLineCommand = new OrderLineCommand();
            orderLineCommand = getOrderLineFromPruductDetail(cartLineCommand.getSkuId(),
                    cartLineCommand.getQuantity());

            orderLineList.add(orderLineCommand);
        }

        return orderLineList;
    }

    public String convertSaleProp(String propertiesName) {
        String result = "";
        Gson gson = new Gson();
        List<SkuProperty> pList = gson.fromJson(propertiesName, new TypeToken<List<SkuProperty>>() {
        }.getType());
        for (SkuProperty skuProperty : pList) {

            result += "<span class='salePropNameStyle'>";
            result += skuProperty.getpName() + "</span>:";
            result += "&nbsp;&nbsp;";
            result += skuProperty.getValue() + "<br/>";
            result += "<br/>";
        }
        return result;
    }

    @Override
    public CouponType findCouponTypeByCouponNo(String couponNo) {

        Coupon coupon = couponDao.findByCardNo(couponNo);
        CouponType couponType = null;
        if (coupon != null) {
            couponType = couponTypeDao.findById(coupon.getCardTypeId());
        }
        return couponType;
    }

    @Override
    public Integer immediatelyBuy(Long userId, Set<String> membComboIds,
            ShoppingCartLineCommand shoppingCartLineCommand, List<ShoppingCartLineCommand> lines) {
        Integer result = sdkShoppingCartManager.immediatelyBuy(userId, membComboIds, shoppingCartLineCommand,
                lines);
        return result;
    }

    @Override
    public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand) {
        return sdkOrderService.saveCancelOrder(cancelOrderCommand);
    }

    @Override
    public Pagination<CancelOrderCommand> findCanceledOrderList(Page page, Long memberId) {
        return sdkOrderService.findCanceledOrderList(page, memberId);
    }

    @Override
    public SalesOrderCommand findOrderByCode(String code, Integer type) {
        return sdkOrderService.findOrderByCode(code, type);
    }

    @Override
    public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page, Long memberId) {
        return sdkOrderService.findReturnedOrderList(page, memberId);
    }

    @Override
    public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand) {
        return sdkOrderService.saveReturnedOrder(returnOrderCommand);
    }

    @Override
    public SalesOrderCommand findOrderByLineId(Long orderLineId) {
        return sdkOrderService.findOrderByLineId(orderLineId);
    }

    @Override
    public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts, Long memberId) {
        List<SalesOrderCommand> salesOrderComList = sdkOrderService.findNoPayOrders(sorts, memberId);
        return salesOrderComList;
    }

    @Override
    public PromotionCouponInfoCommand findCouponInfoByCouponCode(String couponCode) {
        return promotionCouponCodeDao.findCouponInfoByCouponCode(couponCode);
    }

    /**
     * 根据orderQueryForm查询订单分页列表
     */
    @Override
    public Pagination<SimpleOrderCommand> finorderByOrderQueryForm(Page page,
            OrderQueryCommand orderQueryForm, Long memberId) {
        // 将orderqueryform转换成map
        Map<String,Object> paraMap;
        try {
            paraMap = convertBean(orderQueryForm);
            Sort[] sorts = Sort.parse("o.create_Time desc");
            paraMap.put("memberId", memberId);
            // 后台查询
             Pagination<SimpleOrderCommand> ordercommand = sdkOrderDao.findSimpleOrderByOrderQueryCommand(page,
                    sorts, paraMap);
           
             return ordercommand;
                    
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } 

    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * 
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    private Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } /*else {
                    returnMap.put(propertyName, "");
                }*/
            }
        }
        return returnMap;
    }

}
