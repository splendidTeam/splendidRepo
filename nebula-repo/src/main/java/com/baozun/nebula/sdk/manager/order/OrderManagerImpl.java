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
package com.baozun.nebula.sdk.manager.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.dao.salesorder.SdkCancelOrderDao;
import com.baozun.nebula.dao.salesorder.SdkConsigneeDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.dao.salesorder.SdkOrderStatusLogDao;
import com.baozun.nebula.dao.salesorder.SdkReturnOrderDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.CancelOrderApp;
import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.ReturnOrderApp;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.command.ConsigneeCommand;
import com.baozun.nebula.sdk.command.ItemSkuCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkSecretManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.feilong.core.Validator;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * The Class OrderManagerImpl.
 */
@Transactional
@Service("sdkOrderService")
public class OrderManagerImpl implements OrderManager{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderManagerImpl.class);

    /** 程序返回结果 *. */
    private static final Integer SUCCESS = 1;

    /** The Constant FAILURE. */
    private static final Integer FAILURE = 0;

    //---------------------------------------------------------------

    /** The sdk order email manager. */
    @Autowired
    private SdkOrderEmailManager sdkOrderEmailManager;

    /** The sdk cancel order dao. */
    @Autowired
    private SdkCancelOrderDao sdkCancelOrderDao;

    /** The sdk order dao. */
    @Autowired
    private SdkOrderDao sdkOrderDao;

    /** The sdk order promotion dao. */
    @Autowired
    private SdkOrderPromotionDao sdkOrderPromotionDao;

    /** The sdk order line dao. */
    @Autowired
    private SdkOrderLineDao sdkOrderLineDao;

    /** The sdk pay info dao. */
    @Autowired
    private PayInfoDao sdkPayInfoDao;

    /** The sdk return order dao. */
    @Autowired
    private SdkReturnOrderDao sdkReturnOrderDao;

    /** The sdk order status log dao. */
    @Autowired
    private SdkOrderStatusLogDao sdkOrderStatusLogDao;

    /** The sku dao. */
    @Autowired
    private SkuDao skuDao;

    /** The sdk consignee dao. */
    @Autowired
    private SdkConsigneeDao sdkConsigneeDao;

    /** The distribution mode dao. */
    @Autowired
    private DistributionModeDao distributionModeDao;

    /** The promotion coupon code dao. */
    @Autowired
    private PromotionCouponCodeDao promotionCouponCodeDao;

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    /** The sdk member manager. */
    @Autowired
    private SdkMemberManager sdkMemberManager;

    /** The sdk msg manager. */
    @Autowired
    private SdkMsgManager sdkMsgManager;

    /** The sdk secret manager. */
    @Autowired
    private SdkSecretManager sdkSecretManager;

    @Autowired
    private SdkOrderLinePackInfoManager sdkOrderLinePackInfoManager;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByCode(java.lang.String, java.lang.Integer)
     */
    @Override
    @Transactional(readOnly = true)
    public SalesOrderCommand findOrderByCode(String code,Integer type){
        SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderByCode(code, type);
        return pack(salesOrderCommand, type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderById(java.lang.Long, java.lang.Integer)
     */
    @Override
    //@Transactional(readOnly = true)
    public SalesOrderCommand findOrderById(Long id,Integer type){
        // 包含订单基本信息和收货信息
        SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderById(id, type);
        return pack(salesOrderCommand, type);
    }

    /**
     * @param salesOrderCommand
     * @param type
     * @param code
     * @return
     * @since 5.3.2.18
     */
    //TODO type值的方案 很挫
    private SalesOrderCommand pack(SalesOrderCommand salesOrderCommand,Integer type){
        if (null == salesOrderCommand){
            return null;
        }
        String code = salesOrderCommand.getCode();
        //---------------------------------------------------------------
        if (Objects.equals(1, type)){//since 5.3.2.18
            LOGGER.debug("begin decrypt order:[{}] ~~", code);
            //type为1时将查处收货人信息，此时解密
            decryptSalesOrderCommand(salesOrderCommand);
            LOGGER.debug("end decrypt order:[{}]~~", code);
        }

        //---------------------------------------------------------------
        // 订单支付信息
        LOGGER.debug("begin load order:[{}] pay info~~", code);
        List<PayInfoCommand> payInfos = sdkPayInfoDao.findPayInfoCommandByOrderId(salesOrderCommand.getId());
        LOGGER.debug("end load order:[{}] pay info~~", code);

        //---------------------------------------------------------------
        // 订单行信息
        LOGGER.debug("begin load order:[{}] order lines info~~", code);
        List<OrderLineCommand> orderLineCommandList = sdkOrderLineDao.findOrderDetailListByOrderIds(toList(salesOrderCommand.getId()));
        for (OrderLineCommand orderLineCommand : orderLineCommandList){
            List<SkuProperty> propList = sdkSkuManager.getSkuPros(orderLineCommand.getSaleProperty());
            orderLineCommand.setSkuPropertys(propList);
        }

        LOGGER.debug("end load order:[{}] order lines info~~", code);

        //---------------------------------------------------------------
        // 订单行促销
        LOGGER.debug("begin load order:[{}] promotion info~~", code);
        List<OrderPromotionCommand> orderPrm = sdkOrderPromotionDao.findOrderProInfoByOrderId(salesOrderCommand.getId(), 1);
        LOGGER.debug("end load order:[{}] promotion info~~", code);
        //---------------------------------------------------------------

        salesOrderCommand.setPayInfo(payInfos);
        // since 5.3.2.18 fix http://jira.baozun.cn/browse/NB-493
        salesOrderCommand.setOrderLines(sdkOrderLinePackInfoManager.packOrderLinesPackageInfo(orderLineCommandList));
        salesOrderCommand.setOrderPromotions(orderPrm);

        return salesOrderCommand;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrdersWithOutPage(loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findOrdersWithOutPage(Sort[] sorts,Map<String, Object> searchParam){
        List<SalesOrderCommand> salesOrderPage = sdkOrderDao.findOrdersWithOutPage(sorts, searchParam);

        if (null == salesOrderPage){
            return null;
        }

        List<Long> idList = new ArrayList<Long>(salesOrderPage.size());
        boolean isDecrypt = isNotNullOrEmpty(searchParam) && isNotNullOrEmpty(searchParam.get("sdkQueryType")) && searchParam.get("sdkQueryType").equals("1");
        for (SalesOrderCommand cmd : salesOrderPage){
            if (isDecrypt){
                decryptSalesOrderCommand(cmd);
            }
            idList.add(cmd.getId());
        }

        List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
        for (SalesOrderCommand order : salesOrderPage){
            Long orderId = order.getId();
            List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
            for (OrderLineCommand line : allLineList){
                if (line.getOrderId().equals(orderId)){
                    orderLineList.add(line);
                    // 属性list
                    String properties = line.getSaleProperty();
                    List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
                    line.setSkuPropertys(propList);
                }
            }
            order.setOrderLines(orderLineList);
        }
        return salesOrderPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrders(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<SalesOrderCommand> findOrders(Page page,Sort[] sorts,Map<String, Object> searchParam){
        Pagination<SalesOrderCommand> salesOrderPage = sdkOrderDao.findOrders(page, sorts, searchParam);

        List<SalesOrderCommand> salesOrderCommandList = salesOrderPage.getItems();
        if (null != salesOrderCommandList){
            List<Long> idList = new ArrayList<Long>(salesOrderCommandList.size());
            boolean isDecrypt = Validator.isNotNullOrEmpty(searchParam) && Validator.isNotNullOrEmpty(searchParam.get("sdkQueryType")) && searchParam.get("sdkQueryType").equals("1");
            for (SalesOrderCommand cmd : salesOrderCommandList){
                if (isDecrypt){
                    decryptSalesOrderCommand(cmd);
                }
                idList.add(cmd.getId());
            }

            List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
            for (SalesOrderCommand order : salesOrderCommandList){
                Long orderId = order.getId();
                List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
                for (OrderLineCommand line : allLineList){
                    if (line.getOrderId().equals(orderId)){
                        orderLineList.add(line);
                        // 属性list
                        String properties = line.getSaleProperty();
                        List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
                        line.setSkuPropertys(propList);
                    }
                }
                order.setOrderLines(orderLineList);
            }
        }
        return salesOrderPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#savePayOrder(java.lang.String, com.baozun.nebula.sdk.command.PayInfoCommand)
     */
    @Override
    public PayInfoCommand savePayOrder(String code,PayInfoCommand payInfoCommand){
        // 先根据订单code查询订单是否存在
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);

        PayInfo payInfo = (PayInfo) ConvertUtils.convertFromTarget(new PayInfo(), payInfoCommand);
        payInfo.setOrderId(salesOrder.getId());
        payInfo.setPayInfo(payInfoCommand.getPayInfo());
        // 保存订单详细
        payInfo = sdkPayInfoDao.save(payInfo);

        if (null == payInfo){
            LOGGER.warn(" payInfoDao.save(payInfo) returns null");
            return null;
        }

        return (PayInfoCommand) ConvertUtils.convertTwoObject(new PayInfoCommand(), payInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderFinancialStatus(java.lang.String, java.lang.Integer)
     */
    @Override
    public SalesOrderCommand updateOrderFinancialStatus(String code,Integer financialStatus){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderFinancialStatus(code, financialStatus, new Date());
        if (retval > 0){
            saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getFinancialStatus(), financialStatus);
            salesOrder.setFinancialStatus(financialStatus);
        }else{
            return null;
        }
        return salesOrder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderAmount(java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal findOrderAmount(List<OrderLineCommand> orderLineCommands){
        BigDecimal amount = new BigDecimal(-1);
        try{
            if (null != orderLineCommands && orderLineCommands.size() > 0){
                amount = new BigDecimal(0);
                for (OrderLineCommand orderLine : orderLineCommands){
                    Sku sku = skuDao.findSkuByExtentionCode(orderLine.getExtentionCode());
                    amount = amount.add(sku.getSalePrice().multiply(new BigDecimal(orderLine.getCount())));
                }
                amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            return amount;
        }catch (Exception e){
            return new BigDecimal(-1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveReturnOrder(com.baozun.nebula.sdk.command.ReturnOrderCommand)
     */
    @Override
    public Integer saveReturnOrder(ReturnOrderCommand returnOrderCommand){
        // 判断订单号的有效性
        judgeOrderIfExist(returnOrderCommand.getOrderCode());
        ReturnOrderApp returnOrderApp = new ReturnOrderApp();
        // 将returnOrderCommand对象转换为returnOrderapp对象
        returnOrderApp = (ReturnOrderApp) ConvertUtils.convertFromTarget(returnOrderApp, returnOrderCommand);
        returnOrderApp = sdkReturnOrderDao.save(returnOrderApp);
        if (null != returnOrderApp)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveCancelOrder(com.baozun.nebula.sdk.command.CancelOrderCommand)
     */
    @Override
    public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand){
        if (null == cancelOrderCommand)
            return FAILURE;
        // 判断订单是否存在
        SalesOrderCommand order = judgeOrderIfExist(cancelOrderCommand.getOrderCode());
        Integer orderStatus = order.getLogisticsStatus();

        if (Objects.equals(SalesOrder.SALES_ORDER_STATUS_CANCELED, orderStatus) || Objects.equals(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED, orderStatus)){
            throw new BusinessException(Constants.ORDER_ALREADY_CANCEL);
        }
        if (Objects.equals(SalesOrder.SALES_ORDER_STATUS_FINISHED, orderStatus)){
            throw new BusinessException(Constants.ORDER_ALREADY_COMPELETE);
        }
        // 判断该订单是否已经申请了取消订单
        CancelOrderCommand cancelOrdApp = sdkCancelOrderDao.findCancelOrderAppByCode(cancelOrderCommand.getOrderCode());
        if (null != cancelOrdApp)// 该订单已经申请了取消订单，则不允许再次申请
            throw new BusinessException(Constants.ORDER_ALREADY_APPLY_CANCEL);
        CancelOrderApp cancelOrderApp = new CancelOrderApp();
        // 将 CancelOrderCommand对象转换为CancelOrderApp对象
        cancelOrderApp = (CancelOrderApp) ConvertUtils.convertFromTarget(cancelOrderApp, cancelOrderCommand);
        cancelOrderApp = sdkCancelOrderDao.save(cancelOrderApp);
        if (null != cancelOrderApp)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderLogisticsStatus(java.lang.String, java.lang.Integer)
     */
    @Override
    public Integer updateOrderLogisticsStatus(String code,Integer logisticsStatus){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
        if (retval > 0){
            saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
            return SUCCESS;
        }
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#cancelOrderLogisticsStatus(java.lang.String, java.lang.Integer)
     */
    @Override
    @Deprecated
    public Integer cancelOrderLogisticsStatus(String code,Integer logisticsStatus){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
        if (retval > 0){
            OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
            // 保存OMS消息发送记录(订单状态同步)
            sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC, orderStatusLog.getId(), null);
            return SUCCESS;
        }
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#cancelOrderLogisticsStatus(java.lang.String, java.lang.Integer, java.lang.Boolean)
     */
    @Override
    public Integer cancelOrderLogisticsStatus(String code,Integer logisticsStatus,Boolean isOms){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
        if (retval > 0){
            OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
            // 保存OMS消息发送记录(订单状态同步)
            if (!Boolean.TRUE.equals(isOms)){
                sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC, orderStatusLog.getId(), null);
            }
            return SUCCESS;
        }
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#sendEmailOfOrder(java.lang.String, java.lang.String)
     */
    @Override
    public void sendEmailOfOrder(String code,String emailTemplete){
        String isSendEmail = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
        if (isSendEmail != null && isSendEmail.equals("true")){
            sendEmail(code, emailTemplete);
        }
    }

    /**
     * Send email.
     *
     * @param code
     *            the code
     * @param emailTemplete
     *            the email templete
     */
    private void sendEmail(String code,String emailTemplete){

        SalesOrderCommand salesOrderCommand = findOrderByCode(code, 1);

        MemberPersonalData memberPersonalData = null;
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getMemberId())){
            memberPersonalData = sdkMemberManager.findMemberPersonData(salesOrderCommand.getMemberId());
        }

        String nickName = "";

        String email = salesOrderCommand.getEmail();

        if (null != memberPersonalData){
            nickName = memberPersonalData.getNickname();
            if (Validator.isNullOrEmpty(email)){
                email = memberPersonalData.getEmail();
            }
        }

        if (Validator.isNullOrEmpty(email)){
            return;
        }

        if (Validator.isNullOrEmpty(nickName))
            nickName = salesOrderCommand.getMemberName();

        // 游客用收货人
        if (Validator.isNullOrEmpty(nickName)){
            nickName = salesOrderCommand.getName();
        }

        Map<String, Object> dataMap = sdkOrderEmailManager.buildDataMap(emailTemplete, salesOrderCommand, nickName);
        sdkOrderEmailManager.sendEmail(emailTemplete, email, dataMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findReturnOrdersByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<ReturnOrderCommand> findReturnOrdersByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam){
        return sdkReturnOrderDao.findReturnOrdersByQueryMapWithPage(page, sorts, searchParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findCancelOrdersByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<CancelOrderCommand> findCancelOrdersByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam){
        return sdkCancelOrderDao.findCancelOrdersByQueryMapWithPage(page, sorts, searchParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateCancelOrders(java.lang.Long, java.lang.String, java.lang.Integer,
     * java.lang.String)
     */
    @Override
    public Integer updateCancelOrders(Long handleId,String code,Integer status,String feedback){
        Integer count = sdkCancelOrderDao.updateCancelOrders(handleId, code, status, feedback, new Date());
        if (count == 1)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateReturnOrders(java.lang.Long, java.lang.String, java.lang.Integer,
     * java.lang.String)
     */
    @Override
    public Integer updateReturnOrders(Long handleId,String code,Integer status,String feedback){
        Integer count = sdkReturnOrderDao.updateReturnOrders(handleId, code, status, feedback, new Date());
        if (count == 1)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrders(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public Integer updateOrders(SalesOrderCommand salesOrderCommand){
        if (null == salesOrderCommand)
            return FAILURE;
        String orderCode = salesOrderCommand.getCode();
        SalesOrderCommand order = judgeOrderIfExist(orderCode);
        salesOrderCommand.setId(order.getId());
        Integer orderStatus = order.getLogisticsStatus();
        if (orderStatus == com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED)
            throw new BusinessException(Constants.ORDER_ALREADY_COMPELETE);
        com.baozun.nebula.model.salesorder.SalesOrder salesOrder = sdkOrderDao.getByPrimaryKey(order.getId());
        // 将salesOrderCommand转换为SalesOrder对象
        salesOrder = (com.baozun.nebula.model.salesorder.SalesOrder) ConvertUtils.convertFromTarget(salesOrder, salesOrderCommand);
        salesOrder.setModifyTime(new Date());
        salesOrder = sdkOrderDao.save(salesOrder);
        if (null == salesOrder){
            throw new BusinessException(Constants.ORDER_UPDATE_FAILURE);
        }
        ConsigneeCommand consigneeCommand = sdkConsigneeDao.findConsigneeOrderId(salesOrder.getId());
        if (null != consigneeCommand){
            Consignee consignee = sdkConsigneeDao.getByPrimaryKey(consigneeCommand.getId());
            salesOrderCommand.setId(consignee.getId());
            // 将salesOrderCommand中的收货人信息转换为Consignee对象
            consignee = (Consignee) ConvertUtils.convertFromTarget(consignee, salesOrderCommand);
            consignee.setModifyTime(new Date());
            consignee = sdkConsigneeDao.save(consignee);
            if (null == consignee){
                throw new BusinessException(Constants.ORDER_CONSIGNEE_UPDATE_FAILURE);
            }
        }
        return SUCCESS;
    }

    /**
     * Save order status log.
     *
     * @param orderId
     *            the order id
     * @param handleId
     *            the handle id
     * @param beforeStatus
     *            the before status
     * @param afterStatus
     *            the after status
     * @return the order status log
     */
    private OrderStatusLog saveOrderStatusLog(Long orderId,Long handleId,Integer beforeStatus,Integer afterStatus){
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setCreateTime(new Date());
        orderStatusLog.setAfterStatus(afterStatus);
        orderStatusLog.setBeforeStatus(beforeStatus);
        orderStatusLog.setOperatorId(String.valueOf(handleId));
        orderStatusLog.setOrderId(orderId);
        return sdkOrderStatusLogDao.save(orderStatusLog);
    }

    /**
     * 判断订单是否存在.
     *
     * @param orderCode
     *            the order code
     * @return the sales order command
     */
    private SalesOrderCommand judgeOrderIfExist(String orderCode){
        if (null == orderCode || orderCode.length() == 0 || "".equals(orderCode))
            throw new BusinessException(Constants.ORDERCODE_NOT_NULL);
        SalesOrderCommand order = sdkOrderDao.findOrderByCode(orderCode, null);
        if (null == order)// 订单不存在
            throw new BusinessException(Constants.ORDER_NOT_EXIST);
        return order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findItemSkuListByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return sdkOrderDao.findItemSkuListByQueryMapWithPage(page, sorts, paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findNotEvaultionOrderLineQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[],
     * java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<OrderLineCommand> findNotEvaultionOrderLineQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return sdkOrderDao.findNotEvaultionOrderLineQueryMapWithPage(page, sorts, paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderLineEvaulationStatus(java.lang.Long, java.lang.Long)
     */
    @Override
    public Integer updateOrderLineEvaulationStatus(Long skuId,Long orderId){
        return sdkOrderLineDao.updateOrderLineEvaulationStatusByOrderLineid(skuId, orderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findCanceledOrderList(loxia.dao.Page, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<CancelOrderCommand> findCanceledOrderList(Page page,Long memberId){
        return sdkOrderDao.findCanceledOrderList(page, memberId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findReturnedOrderList(loxia.dao.Page, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page,Long memberId){
        return sdkOrderDao.findReturnedOrderList(page, memberId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveReturnedOrder(com.baozun.nebula.sdk.command.ReturnOrderCommand)
     */
    @Override
    public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand){
        if (null == returnOrderCommand)
            return FAILURE;
        // 判断订单是否存在
        SalesOrderCommand order = judgeOrderIfExist(returnOrderCommand.getOrderCode());
        Integer orderStatus = order.getLogisticsStatus();
        if (!orderStatus.equals(com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED)) // 非完成的订单，不能申请退换货
            throw new BusinessException(Constants.ORDER_NOT_COMPELETE);

        ReturnOrderCommand returnedOrder = sdkReturnOrderDao.findReturnOrderAppByCode(returnOrderCommand.getOrderCode());
        if (null != returnedOrder)// 该订单已经申请了退换货，则不允许再次申请
            throw new BusinessException(Constants.ORDER_ALREADY_APPLY_RETURN);
        ReturnOrderApp returnOrderApp = new ReturnOrderApp();
        // 将 CancelOrderCommand对象转换为CancelOrderApp对象
        returnOrderApp = (ReturnOrderApp) ConvertUtils.convertFromTarget(returnOrderApp, returnOrderCommand);
        returnOrderApp = sdkReturnOrderDao.save(returnOrderApp);
        if (null != returnOrderApp)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByLineId(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public SalesOrderCommand findOrderByLineId(Long orderLineId){
        SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderByLineId(orderLineId);
        //收货人信息解密
        decryptSalesOrderCommand(salesOrderCommand);
        return salesOrderCommand;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderFinancialStatusById(java.util.List, java.lang.Integer)
     */
    @Override
    public void updateOrderFinancialStatusById(List<Long> orderIds,Integer financialStatus){
        Integer count = sdkOrderDao.updateOrderFinancialStatusById(orderIds, financialStatus);
        if (count != orderIds.size()){
            throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { count });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findCountOfOrder(java.util.List, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findCountOfOrder(List<Integer> status,Long memberId){
        Integer count = sdkOrderDao.findCountOfOrder(status, memberId);
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderDetailList(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineCommand> findOrderDetailList(Long orderId){
        return sdkOrderLineDao.findOrderDetailList(orderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findNoPayOrders(loxia.dao.Sort[], java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts,Long memberId){
        List<SalesOrderCommand> salesOrderComList = sdkOrderDao.findNoPayOrders(sorts, memberId);
        List<Long> idList = new ArrayList<Long>(salesOrderComList.size());
        for (SalesOrderCommand cmd : salesOrderComList){
            idList.add(cmd.getId());
        }
        List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
        for (SalesOrderCommand order : salesOrderComList){
            Long orderId = order.getId();
            List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
            for (OrderLineCommand line : allLineList){
                if (line.getOrderId().equals(orderId)){
                    orderLineList.add(line);
                    // 属性list
                    String properties = line.getSaleProperty();
                    List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
                    line.setSkuPropertys(propList);
                }
            }
            order.setOrderLines(orderLineList);
        }
        return salesOrderComList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findToBeCancelOrders(loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findToBeCancelOrders(Sort[] sorts,Map<String, Object> searchParam){
        return sdkOrderDao.findToBeCancelOrders(sorts, searchParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderPormots(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderPromotionCommand> findOrderPormots(String orderCode){
        SalesOrderCommand so = sdkOrderDao.findOrderByCode(orderCode, null);
        return sdkOrderPromotionDao.findOrderProsInfoByOrderId(so.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#validCoupon(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public PromotionCouponCode validCoupon(String couponCode){
        if (StringUtils.isNotBlank(couponCode)){
            PromotionCouponCode promotionCouponCode = promotionCouponCodeDao.findPromotionCouponCodeBycoupon(couponCode);
            return promotionCouponCode;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderStatusLogById(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public OrderStatusLogCommand findOrderStatusLogById(Long id){
        return sdkOrderStatusLogDao.findOrderStatusLogById(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByExntentionListAndOrderCreateTime(java.util.List, java.util.Date)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findOrderByExntentionListAndOrderCreateTime(List<String> extentionList,Date startTime){
        return sdkOrderDao.findOrderByExntentionListAndOrderCreateTime(extentionList, startTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByExntentionListAndOrderStatus(java.util.List, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findOrderByExntentionListAndOrderStatus(List<String> extentionList,List<Integer> orderStatus){
        return sdkOrderDao.findOrderByExntentionListAndOrderStatus(extentionList, orderStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderDetailListByOrderIds(java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineCommand> findOrderDetailListByOrderIds(List<Long> orderIdList){
        return sdkOrderLineDao.findOrderDetailListByOrderIds(orderIdList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateLogisticsInfo(java.lang.String, java.math.BigDecimal, java.lang.String,
     * java.lang.String, java.lang.String, java.util.Date)
     */
    @Override
    public void updateLogisticsInfo(String orderCode,BigDecimal actualFreight,String logisticsProviderCode,String logisticsProviderName,String transCode,Date modifyTime){
        sdkOrderDao.updateLogisticsInfo(orderCode, actualFreight, logisticsProviderCode, logisticsProviderName, transCode, modifyTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findPromotionCouponCodeListByQueryMap(java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionCouponCode> findPromotionCouponCodeListByQueryMap(Map<String, Object> paraMap){
        return promotionCouponCodeDao.findPromotionCouponCodeListByQueryMap(paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#getAllDistributionMode()
     */
    @Override
    @Transactional(readOnly = true)
    public List<DistributionMode> getAllDistributionMode(){
        return distributionModeDao.getAllDistributionMode();
    }

    /**
     * 解密订单中的收货人信息.
     *
     * @param salesOrderCommand
     *            the sales order command
     */
    private void decryptSalesOrderCommand(SalesOrderCommand salesOrderCommand){
        sdkSecretManager.decrypt(salesOrderCommand, new String[] { "name", "buyerName", "country", "province", "city", "area", "town", "address", "postcode", "tel", "buyerTel", "mobile", "email" });
    }
}
