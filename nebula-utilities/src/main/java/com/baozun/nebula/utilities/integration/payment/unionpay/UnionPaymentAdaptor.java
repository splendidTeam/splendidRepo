package com.baozun.nebula.utilities.integration.payment.unionpay;

import java.util.Properties;

import com.unionpay.acp.sdk.SDKConfig;

public class UnionPaymentAdaptor extends AbstractUnionPaymentAdaptor{

    public UnionPaymentAdaptor(Properties  properties){
        SDKConfig.getConfig().loadProperties(properties);
    }
    
    @Override
    public String getServiceProvider(){
        return null;
    }

}
