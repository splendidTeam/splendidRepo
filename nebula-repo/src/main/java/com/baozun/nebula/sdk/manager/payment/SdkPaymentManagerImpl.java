package com.baozun.nebula.sdk.manager.payment;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.payment.PayCodeDao;
import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.dao.payment.PaymentLogDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;

/**
 * 
 * @author 阳羽
 * @createtime 2014-3-7 上午10:19:36
 */
@Transactional
@Service("sdkPaymentManager")
public class SdkPaymentManagerImpl implements SdkPaymentManager{

    @Autowired
    private PaymentLogDao paymentLogDao;

    @Autowired
    private PayInfoDao payInfoDao;

    @Autowired
    private PayCodeDao payCodeDao;

    @Autowired
    private PayInfoLogDao payInfoLogDao;

    @Autowired
    private SdkPayInfoQueryManager sdkPayInfoQueryManager;

    @Override
    public void savePaymentLog(Date createTime,String message,String operator,String returnVal){
        paymentLogDao.savePaymentLog(createTime, message, operator, returnVal);
    }

    @Override
    public void upPayInfoCallCloseStaBySubOrdinate(Long orderId,Boolean callCloseStatus,Date modifyTime){
        List<PayInfoLog> payInfoList = sdkPayInfoQueryManager.findPayInfoLogListByOrderId(orderId, false);
        if (payInfoList != null && payInfoList.size() != 0){
            //根据createTime 降序排列 取第一个
            PayInfoLog payInfoLog = payInfoList.get(0);
            payInfoLog.setCallCloseStatus(true);
            payInfoLog.setModifyTime(new Date());
            payInfoLogDao.save(payInfoLog);
        }
    }

    @Override
    public void updatePayCodeBySubOrdinate(String subinateCode,Integer payType){
        Integer count = payCodeDao.updatePayCodeBySubOrdinate(subinateCode, payType);
        if (count < 1){
            throw new BusinessException(ErrorCodes.UPDATE_PAYCODE_FAILURE);
        }
    }

    @Override
    public void updatePayCodePayStatus(String subinateCode,Date modifyTime,Boolean paySuccessStatus){
        Integer count = payCodeDao.updatePayCodePayStatus(subinateCode, modifyTime, paySuccessStatus);
        if (count < 1){
            throw new BusinessException(ErrorCodes.UPDATE_PAYCODE_FAILURE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PayCode findPayCodeBySubOrdinate(String subOrdinate){
        return payCodeDao.findPayCodeBySubOrdinate(subOrdinate);
    }

    @Override
    @Transactional(readOnly = true)
    public PayCode findPayCodeByCodeAndPayTypeAndPayStatus(String code,Integer payType,boolean flag){
        return payCodeDao.findPayCodeByCodeAndPayTypeAndPayStatus(code, payType, flag);
    }

    @Override
    @Transactional(readOnly = true)
    public PayCode findPayCodeByCodeAndPayType(String code,Integer payType){
        return payCodeDao.findPayCodeByCodeAndPayType(code, payType);
    }

    //---------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<PayInfoCommand> findPayInfoCommandByOrderId(Long orderId){
        return payInfoDao.findPayInfoCommandByOrderId(orderId);
    }

    /**
     * @deprecated pls use {@link com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager#findPayInfoLogListByQueryMap(Map)} ,实现和原来完全一样
     *             <br>
     *             since 5.3.2.22
     */
    @Override
    @Transactional(readOnly = false)
    @Deprecated
    public List<PayInfoLog> findPayInfoLogListByQueryMap(Map<String, Object> paraMap){
        return sdkPayInfoQueryManager.findPayInfoLogListByQueryMap(paraMap);
    }
}
