package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.salesorder.DefaultOrderCodeCreatorManager;
import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.dao.payment.PayCodeDao;
import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.dao.payment.PaymentLogDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPayCodeManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.Validator;

/**
 * 
 * @author 阳羽
 * @createtime 2014-3-7 上午10:19:36
 */
@Transactional
@Service("sdkPaymentManager")
public class SdkPaymentManagerImpl implements SdkPaymentManager{

    public static final Logger      log                  = LoggerFactory.getLogger(SdkPaymentManagerImpl.class);

    @Autowired
    private PaymentLogDao           paymentLogDao;

    @Autowired
    private PayInfoDao              payInfoDao;

    private OrderCodeCreatorManager creator;

    @Autowired
    private PayCodeDao              payCodeDao;

    @Autowired
    private PayInfoLogDao           payInfoLogDao;

    /** The sdk pay code manager. */
    @Autowired
    private SdkPayCodeManager       sdkPayCodeManager;

    @Value("#{meta['orderCodeCreator']}")
    private String                  orderCodeCreatorPath = "com.baozun.nebula.api.salesorder.DefaultOrderCodeCreatorManager";

    public SdkPaymentManagerImpl(){
        creator = this.getOrderCodeCreator();
    }

    @Override
    public void savePaymentLog(Date createTime,String message,String operator,String returnVal){
        paymentLogDao.savePaymentLog(createTime, message, operator, returnVal);
    }

    @Override
    public void upPayInfoCallCloseStaBySubOrdinate(Long orderId,Boolean callCloseStatus,Date modifyTime){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("orderId", orderId);
        paraMap.put("paySuccessStatusStr", 2);
        List<PayInfoLog> payInfoList = payInfoLogDao.findPayInfoLogListByQueryMap(paraMap);
        if (payInfoList != null&&payInfoList.size()!=0){
            //根据createTime 降序排列 取第一个
            PayInfoLog payInfoLog = payInfoList.get(0);
            payInfoLog.setCallCloseStatus(true);
            payInfoLog.setModifyTime(new Date());
            payInfoLogDao.save(payInfoLog);
        }
    }

    @Override
    public String savePayCodeInfo(List<SalesOrderCommand> sos){
        String subOrdinate = creator.createOrderSerialNO();
        if (StringUtils.isBlank(subOrdinate)){
            log.info("generate orderCode failure");
            throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
        }

        BigDecimal totalPayMoney = new BigDecimal(0.0);
        for (SalesOrderCommand so : sos){

            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("orderId", so.getId());
            paraMap.put("paySuccessStatusStr", 2);
            List<PayInfo> payInfoList = payInfoDao.findPayInfoListByQueryMap(paraMap);
            if (payInfoList != null){
                for (PayInfo payInfo : payInfoList){
                    PayInfoLog payInfoLog = new PayInfoLog();
                    payInfoLog.setPayMoney(payInfo.getPayMoney());
                    payInfoLog.setPayNumerical(payInfo.getPayMoney());
                    payInfoLog.setCreateTime(new Date());
                    payInfoLog.setOrderId(payInfo.getOrderId());
                    payInfoLog.setPaySuccessStatus(false);
                    payInfoLog.setSubOrdinate(subOrdinate);
                    payInfoLog.setCallCloseStatus(false);
                    payInfoLog.setPayInfoId(payInfo.getId());
                    //此处保留之前的支付方式 /*如要修改支付方式 请修改payInfo里面的payType payInfo*/
                    payInfoLog.setPayType(payInfo.getPayType());
                    payInfoLog.setPayInfo(payInfo.getPayInfo());

                    Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
                    String payInfoStr = payInfo.getPayInfo();
                    String payType = pro.getProperty(payInfoStr + ".payType").trim();

                    if (Validator.isNotNullOrEmpty(payType)){
                        payInfoLog.setThirdPayType(Integer.parseInt(payType));
                    }
                    payInfoLogDao.save(payInfoLog);

                    totalPayMoney = totalPayMoney.add(payInfo.getPayMoney());
                }
            }
        }

        totalPayMoney = totalPayMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        sdkPayCodeManager.savaPayCode(subOrdinate, totalPayMoney);
        return subOrdinate;
    }

    private OrderCodeCreatorManager getOrderCodeCreator(){
        // TODO 可以考虑使用 com.baozun.nebula.utils.spring.SpringUtil 来动态加载bean
        // 不用反射的方式
        if (creator != null){
            return creator;
        }

        creator = getDefaultCreator();

        try{

            String className = orderCodeCreatorPath;
            @SuppressWarnings("unchecked")
            Class<OrderCodeCreatorManager> cls = (Class<OrderCodeCreatorManager>) Class.forName(className);
            creator = cls.newInstance();
        }catch (Exception e){
            log.error("getDefined Creator error" + e.getMessage());
            e.printStackTrace();
        }
        return creator;
    }

    private OrderCodeCreatorManager getDefaultCreator(){
        return new DefaultOrderCodeCreatorManager();
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

    @Override
    @Transactional(readOnly = true)
    public List<PayInfoCommand> findPayInfoCommandByOrderId(Long orderId){
        return payInfoDao.findPayInfoCommandByOrderId(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayInfoLog> findPayInfoLogListByQueryMap(Map<String, Object> paraMap){
        return payInfoLogDao.findPayInfoLogListByQueryMap(paraMap);
    }
}
