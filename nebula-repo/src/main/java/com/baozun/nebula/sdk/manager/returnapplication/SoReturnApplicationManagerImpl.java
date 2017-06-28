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
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkReturnApplicationDao;
import com.baozun.nebula.dao.salesorder.SdkSoReturnApplicationDeliveryInfoDao;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.feilong.core.Validator;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

@Service("soReturnApplicationManager")
public class SoReturnApplicationManagerImpl implements SoReturnApplicationManager{

    private static final Logger log = LoggerFactory.getLogger(SoReturnApplicationManagerImpl.class);

    @Autowired
    private SdkReturnApplicationDao soReturnApplicationDao;

    @Autowired
    private SdkOrderDao sdkOrderDao;

    @Autowired
    private SoReturnLineManager soReturnLineManager;
    
    @Autowired(required=false)
    private ReturnRefundManager  returnRefundManager;

    @Autowired
    private SdkSoReturnApplicationDeliveryInfoDao sdkSoReturnApplicationDeliveryInfoDao;


    public final static Integer[] statusArr = { new Integer(SoReturnConstants.RETURN_COMPLETE) };

    @Override
    public Integer countCompletedAppsByPrimaryLineId(Long primaryLineId){
        Integer count = null;
        count = soReturnApplicationDao.countItemByOrderLineIdAndStatus(primaryLineId, statusArr);
        if (count == null){
            count = new Integer(0);
        }
        return count;
    }

    @Override
    public SoReturnApplication findLastApplicationByOrderLineId(Long orderLineId){
        if (orderLineId == null){
            log.error("===orderLineId is null ==");
            return null;
        }
        List<SoReturnApplication> returnApplications = soReturnApplicationDao.findLastApplicationByOrderLineId(orderLineId);
        //获得的returnApplication是根据创建时间倒序排列，因此取第一个就是最新的一笔退货
        if (Validator.isNotNullOrEmpty(returnApplications)){
            return returnApplications.get(0);
        }
        return null;

    }

    @Override
    public Boolean isFinishedAndOutDayOrderById(Long orderId){
        if (orderId == null){
            log.error("==orderId is null==");
            return false;
        }
        SalesOrder salesOrder = sdkOrderDao.findFinishedOrderById(orderId, SalesOrder.SALES_ORDER_STATUS_FINISHED);
        return null != salesOrder;

    }

    @Transactional
    @Override
    public ReturnApplicationCommand createReturnApplication(ReturnApplicationCommand returnCommand,SalesOrderCommand orderCommand){
        SoReturnApplication app = returnCommand.getReturnApplication();
        SoReturnApplication returnApplication = this.saveSoReturnApplication(app);

        //如果是换货请求，需要保存换货物流对象
        if (SoReturnConstants.TYPE_EXCHANGE == returnApplication.getType()){
            SoReturnApplicationDeliveryInfo deliveryInfo = returnCommand.getSoReturnApplicationDeliveryInfo();
            if (deliveryInfo != null){
                deliveryInfo.setRetrunApplicationId(returnApplication.getId());
                deliveryInfo = sdkSoReturnApplicationDeliveryInfoDao.save(deliveryInfo);
                returnCommand.setSoReturnApplicationDeliveryInfo(deliveryInfo);
            }
        }
        List<SoReturnLine> returnLines = returnCommand.getReturnLineList();
        for (SoReturnLine line : returnLines){
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
    private SoReturnApplication saveSoReturnApplication(SoReturnApplication soReturnApplication){
        soReturnApplication = soReturnApplicationDao.save(soReturnApplication);
        return soReturnApplication;
    }

    @Override
    public SoReturnApplication findLastApplicationByOrderId(Long orderId){
        List<SoReturnApplication> returnApplication = soReturnApplicationDao.findLastApplicationByOrderId(orderId);
        if(Validator.isNotNullOrEmpty(returnApplication)){
          //获得的returnApplication是根据创建时间倒序排序，因此取第一个就是最新的一笔退货
            return returnApplication.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Pagination<SoReturnApplication> findReturnByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return soReturnApplicationDao.findReturnByQueryMapWithPage(page, sorts, paraMap);
    }

    @Override
    public SoReturnApplication findByApplicationId(Long id){
        return soReturnApplicationDao.getByPrimaryKey(id);
    }

    // 退货申请
    @Override
    public SoReturnApplication auditSoReturnApplication(String returnCode,Integer status,String description,String lastModifier,String omsCode,String returnAddress) throws Exception{
        Assert.notNull(returnCode, "returnCode is null");
        Assert.notNull(lastModifier, "lastModifier is null ");
        Assert.notNull(status, "Status is null ");
        Assert.notNull(description, "审核备注不能为空");
        // 当审核通过时，允许客户退回商品，同时将退款状态改为待处理
        Date now = new Date();
        SoReturnApplication returnapp = soReturnApplicationDao.findApplicationByCode(returnCode);
        if (returnapp == null){
            throw new Exception("对应的申请单不存在");
        }
        //审核通过时必须填写退货地址
        if (status.intValue() == 2 && returnAddress == ""){
            throw new Exception("退货地址为空");
        }else{
            returnapp.setReturnAddress(returnAddress);
        }
        if (omsCode != null && omsCode != ""){
            returnapp.setOmsCode(omsCode);
        }
        returnapp.setApprovalDescription(description);
        // 当前退货状态为待审核，并且页面操作为审核
        if (status.intValue() == SoReturnConstants.TO_DELIVERY && returnapp.getStatus() == SoReturnConstants.AUDITING){// 审核通过
            // status为2时，表示已进行审核操作，需要判断当前退货单是否已审核过
            if (returnapp.getStatus() == SoReturnConstants.TO_DELIVERY || returnapp.getStatus() == SoReturnConstants.REFUS_RETURN){
                throw new Exception("对应的申请单已审核，请刷新页面 ");
            }else{
                returnapp.setStatus(SoReturnConstants.TO_DELIVERY);// 审核通过
            }
        }
        if (status == SoReturnConstants.REFUS_RETURN && returnapp.getStatus() == SoReturnConstants.AUDITING){// 审核退回
            // status为1时，表示已进行审核操作，需要判断当前退货单是否已审核过
            if (returnapp.getStatus() == SoReturnConstants.TO_DELIVERY || returnapp.getStatus() == SoReturnConstants.REFUS_RETURN){
                throw new Exception("对应的申请单已审核，请刷新页面 ");
            }else{
                returnapp.setStatus(SoReturnConstants.REFUS_RETURN);
            }
        }
        // 拒绝退款
        if (status == SoReturnConstants.REFUS_RETURN){
            returnapp.setStatus(SoReturnConstants.REFUS_RETURN);
        }
        // 同意退款
        if (status == SoReturnConstants.AGREE_REFUND){
            returnapp.setStatus(SoReturnConstants.AGREE_REFUND);
        }
        // 当前状态为同意退款并且页面操作为退款完成
        if (status == SoReturnConstants.RETURN_COMPLETE && returnapp.getStatus() == SoReturnConstants.AGREE_REFUND){
            returnapp.setStatus(SoReturnConstants.RETURN_COMPLETE);
        }
        returnapp.setOmsCode(omsCode);
        returnapp.setLastModifyUser(lastModifier);
        returnapp.setApprover(lastModifier);
        returnapp.setApproveTime(now);
        returnapp.setVersion(now);
        returnapp.setReturnReason("");
        returnapp = soReturnApplicationDao.save(returnapp);
        return returnapp;
    }

    @Override
    public List<OrderReturnCommand> findExpInfo(Sort[] sorts,Map<String, Object> paraMap){
        List<OrderReturnCommand> orderReturn = soReturnApplicationDao.findExpInfo(sorts, paraMap);
        return orderReturn;
    }

    @Override
    public List<ReturnApplicationCommand> findReturnApplicationCommandsByIds(List<Long> ids){
        List<ReturnApplicationCommand> returnApplicationCommands = soReturnApplicationDao.findReturnApplicationCommandByIds(ids);
        return returnApplicationCommands;
    }

    @Override
    public SoReturnApplication findApplicationByCode(String code){
        return soReturnApplicationDao.findApplicationByCode(code);
    }

    @Override
    public void updateRefundType(String returnCode,String lastModifier,Integer status) throws Exception{

        Date now = new Date();
        SoReturnApplication returnapp = soReturnApplicationDao.findApplicationByCode(returnCode);
        if (returnapp == null){
            throw new Exception("对应的申请单不存在");
        }
        returnapp.setLastModifyUser(lastModifier);
        returnapp.setApprover(lastModifier);
        returnapp.setApproveTime(now);
        returnapp.setVersion(now);
        returnapp.setReturnReason("");

        // 同意
        if (status == 4){
            returnapp.setStatus(SoReturnConstants.AGREE_REFUND);
            //同意退换货后扩展业务
            if(null != returnRefundManager){
                returnRefundManager.processAfterReturn(returnapp);
            }
            
        }
        // 拒绝退款
        if (status == 1){
            // 退货状态改为已拒绝
            returnapp.setStatus(SoReturnConstants.REFUS_RETURN);
        }
        if (returnapp.getStatus() == 4){
            if (status == 5){
                returnapp.setStatus(SoReturnConstants.RETURN_COMPLETE);
            }
        }else{
            throw new Exception("物流状态异常！");
        }
        soReturnApplicationDao.save(returnapp);
    }
}
