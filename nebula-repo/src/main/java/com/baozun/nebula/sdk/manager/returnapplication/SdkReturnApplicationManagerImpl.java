package com.baozun.nebula.sdk.manager.returnapplication;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baozun.nebula.command.OrderReturnCommand;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.dao.returnapplication.SdkReturnApplicationDao;
import com.baozun.nebula.dao.returnapplication.SdkSoReturnApplicationDeliveryInfoDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.feilong.core.Validator;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

@Service("sdkReturnApplicationManager")
public class SdkReturnApplicationManagerImpl implements SdkReturnApplicationManager{

    private static final Logger log = LoggerFactory.getLogger(SdkReturnApplicationManagerImpl.class);

    @Autowired
    private SdkReturnApplicationDao returnApplicationDao;

    @Autowired
    private SdkReturnApplicationLineManager soReturnLineManager;
    
    @Autowired(required=false)
    private ReturnRefundManager  returnRefundManager;
    
    @Autowired(required=false)
    private ReturnReasonResolver    returnReasonResolver;
    

    @Autowired
    private SdkSoReturnApplicationDeliveryInfoDao sdkReturnApplicationDeliveryInfoDao;

    @Override
    public Integer countCompletedAppsByPrimaryLineId(Long primaryLineId, Integer[] status){
        Integer count = 0;
        count = returnApplicationDao.countItemByOrderLineIdAndStatus(primaryLineId, status);
        if(null==count){
            return 0;
        }
        return count;
    }

    @Override
    public ReturnApplication findLastApplicationByOrderLineId(Long orderLineId){
        if (orderLineId == null){
            log.info("===orderLineId is null ==");
            return null;
        }
        List<ReturnApplication> returnApplications = returnApplicationDao.findLastApplicationByOrderLineId(orderLineId);
        //获得的returnApplication是根据创建时间倒序排列，因此取第一个就是最新的一笔退货
        if (Validator.isNotNullOrEmpty(returnApplications)){
            return returnApplications.get(0);
        }
        return null;

    }

    @Transactional
    @Override
    public ReturnApplicationCommand createReturnApplication(ReturnApplicationCommand returnCommand,SalesOrderCommand orderCommand){
        ReturnApplication app = returnCommand.getReturnApplication();
        ReturnApplication returnApplication = this.saveReturnApplication(app);

        //如果是换货请求，需要保存换货物流对象
        if (ReturnApplication.SO_RETURN_TYPE_EXCHANGE.equals(returnApplication.getType())){
            ReturnApplicationDeliveryInfo deliveryInfo = returnCommand.getSoReturnApplicationDeliveryInfo();
            if (deliveryInfo != null){
                deliveryInfo.setRetrunApplicationId(returnApplication.getId());
                deliveryInfo = sdkReturnApplicationDeliveryInfoDao.save(deliveryInfo);
                returnCommand.setSoReturnApplicationDeliveryInfo(deliveryInfo);
            }
        }
        List<ReturnApplicationLine> returnLines = returnCommand.getReturnLineList();
        for (ReturnApplicationLine line : returnLines){
            line.setReturnOrderId(returnApplication.getId());
        }
        returnLines = soReturnLineManager.saveReturnLine(returnLines);

        returnCommand.setReturnLineList(returnLines);
        returnCommand.setReturnApplication(returnApplication);
        return returnCommand;

    }

    /**
     * 退换货申请表 对象保存
     * 
     * @author yinglong.xu
     */
    private ReturnApplication saveReturnApplication(ReturnApplication ReturnApplication){
        ReturnApplication = returnApplicationDao.save(ReturnApplication);
        return ReturnApplication;
    }

    @Override
    public ReturnApplication findLastApplicationByOrderId(Long orderId){
        List<ReturnApplication> returnApplication = returnApplicationDao.findLastApplicationByOrderId(orderId);
        if(Validator.isNotNullOrEmpty(returnApplication)){
          //获得的returnApplication是根据创建时间倒序排序，因此取第一个就是最新的一笔退货
            return returnApplication.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Pagination<ReturnApplication> findReturnByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return returnApplicationDao.findReturnByQueryMapWithPage(page, sorts, paraMap);
    }

    @Override
    public ReturnApplication findByApplicationId(Long id){
        return returnApplicationDao.getByPrimaryKey(id);
    }

    // 退货申请
    @Override
    public ReturnApplication auditReturnApplication(String returnCode,Integer status,String description,String lastModifier,String omsCode,String returnAddress) throws Exception{
        Assert.notNull(returnCode, "returnCode is null");
        Assert.notNull(lastModifier, "lastModifier is null ");
        Assert.notNull(status, "Status is null ");
        Assert.notNull(description, "审核备注不能为空");
        // 当审核通过时，允许客户退回商品，同时将退款状态改为待处理
        Date now = new Date();
        ReturnApplication returnapp = returnApplicationDao.findApplicationByCode(returnCode);
        if (returnapp == null){
            throw new Exception("对应的申请单不存在");
        }
        //审核通过时必须填写退货地址
        if (status.intValue() == ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY && returnAddress == ""){
            throw new Exception("退货地址为空");
        }else{
            returnapp.setReturnAddress(returnAddress);
        }
        if (omsCode != null && omsCode != ""){
            returnapp.setOmsCode(omsCode);
        }
        returnapp.setApprovalDescription(description);
        // 当前退货状态为待审核，并且页面操作为审核
        if (ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY.equals(status.intValue()) && ReturnApplication.SO_RETURN_STATUS_AUDITING.equals(returnapp.getStatus())){// 审核通过
            // status为2时，表示已进行审核操作，需要判断当前退货单是否已审核过
            if (ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY.equals(returnapp.getStatus()) || ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(returnapp.getStatus())){
                throw new BusinessException("对应的申请单已审核，请刷新页面 ");
            }else{
                returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY);// 审核通过
            }
        }
        if (ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(status) && returnapp.getStatus() == ReturnApplication.SO_RETURN_STATUS_AUDITING){// 审核退回
            // status为1时，表示已进行审核操作，需要判断当前退货单是否已审核过
            if (ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY.equals(returnapp.getStatus()) || returnapp.getStatus() == ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN){
                throw new BusinessException("对应的申请单已审核，请刷新页面 ");
            }else{
                returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN);
            }
        }
        // 拒绝退款
        if (ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(status)){
            returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN);
        }
        // 同意退款
        if (ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND.equals(status)){
            returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND);
        }
        // 当前状态为同意退款并且页面操作为退款完成
        if (ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE.equals(status) && ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND.equals(returnapp.getStatus())){
            returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE);
        }
        returnapp.setOmsCode(omsCode);
        returnapp.setLastModifyUser(lastModifier);
        returnapp.setApprover(lastModifier);
        returnapp.setApproveTime(now);
        returnapp.setVersion(now);
        returnapp = returnApplicationDao.save(returnapp);
        return returnapp;
    }

    @Override
    public List<OrderReturnCommand> findExpInfo(Sort[] sorts,Map<String, Object> paraMap){
        List<OrderReturnCommand> orderReturn = returnApplicationDao.findExpInfo(sorts, paraMap);
        for(OrderReturnCommand returnCommand :orderReturn){
            if(ReturnApplication.SO_RETURN_TYPE_RETURN.equals(returnCommand.getType())){
                returnCommand.setBusinessType("退货");
            }else{
                returnCommand.setBusinessType("换货");
            }
            if (ReturnApplication.SO_RETURN_STATUS_AUDITING.equals(returnCommand.getStatus())){
                returnCommand.setBusinessStatus("待审核");
            }
            if (ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(returnCommand.getStatus())){
                returnCommand.setBusinessStatus("拒绝退换货");
            }
            if (ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY.equals(returnCommand.getStatus())){
                returnCommand.setBusinessStatus("退回中");
            }
            /*if (ReturnApplication.SO_RETURN_STATUS_DELIVERIED.equals(returnCommand.getStatus())){
                returnCommand.setBusinessStatus("已发货");
            }*/
            if (ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND.equals(returnCommand.getStatus())){
                returnCommand.setBusinessStatus("同意退换货");
            }
            if (ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE.equals(returnCommand.getStatus())){
                returnCommand.setBusinessStatus("已完成");
            }
            returnReasonResolver.getReasonResolver(returnCommand);
        }
        return orderReturn;
    }

    @Override
    public List<ReturnApplicationCommand> findReturnApplicationCommandsByIds(List<Long> ids){
        List<ReturnApplicationCommand> returnApplicationCommands = returnApplicationDao.findReturnApplicationCommandByIds(ids);
        return returnApplicationCommands;
    }

    @Override
    public ReturnApplication findApplicationByCode(String code){
        return returnApplicationDao.findApplicationByCode(code);
    }

    @Override
    public void updateRefundType(String returnCode,String lastModifier,Integer status) throws Exception{

        Date now = new Date();
        ReturnApplication returnapp = returnApplicationDao.findApplicationByCode(returnCode);
        if (returnapp == null){
            throw new Exception("对应的申请单不存在");
        }
        returnapp.setLastModifyUser(lastModifier);
        returnapp.setApprover(lastModifier);
        returnapp.setApproveTime(now);
        returnapp.setVersion(now);

        // 同意
        if (ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND.equals(status)){
            returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND);
            //同意退换货后扩展业务
            if(null != returnRefundManager){
                returnRefundManager.processAfterReturn(returnapp);
            }
            
        }
        // 拒绝退款
        if (ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(status)){
            // 退货状态改为已拒绝
            returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN);
        }
        if (ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND.equals(returnapp.getStatus())){
            if (ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE.equals(status)){
                returnapp.setStatus(ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE);
            }
        }else{
            throw new BusinessException("物流状态异常！");
        }
        returnApplicationDao.save(returnapp);
    }
}
