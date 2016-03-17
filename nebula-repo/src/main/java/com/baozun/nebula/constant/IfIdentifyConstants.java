package com.baozun.nebula.constant;

/**
 * 接口标识
 * @author Justin Hu
 *
 */
public class IfIdentifyConstants {
	/**商品信息同步 */
    public static final String IDENTIFY_ITEM_SYNC  ="P1-1" ;
    /**在售商品同步*/
    public static final String IDENTIFY_ITEM_ONSALE_SYNC  ="P2-1" ;
    /**商品价格同步*/
    public static final String IDENTIFY_ITEM_PRICE_SYNC  ="P1-2" ;
    /**库存同步（全）*/
    public static final String IDENTIFY_INVENTORY_ALL ="I1-1" ;
    /**库存同步（增）*/
    public static final String IDENTIFY_INVENTORY_ADD ="I1-2" ;
    /**订单推送*/
    public static final String IDENTIFY_ORDER_SEND ="O2-1" ;
    /**付款推送*/
    public static final String IDENTIFY_PAY_SEND ="O2-2" ;
    /**订单状态同步(scm2shop)*/
    public static final String IDENTIFY_STATUS_SCM2SHOP_SYNC ="O1-1" ;
    /**订单状态同步(shop2scm)*/
    public static final String IDENTIFY_STATUS_SHOP2SCM_SYNC ="O2-3" ;
    /**物流跟踪*/
    public static final String IDENTIFY_LOGISTICS_TRACKING  ="L1-1" ;
    /**SF上门取件*/
    public static final String IDENTIFY_SF_TAKE_DATA_ONSITE  ="L2-1" ;

}
