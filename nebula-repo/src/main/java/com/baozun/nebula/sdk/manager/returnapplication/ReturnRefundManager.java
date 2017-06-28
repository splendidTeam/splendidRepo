package com.baozun.nebula.sdk.manager.returnapplication;

import com.baozun.nebula.model.salesorder.SoReturnApplication;

public interface ReturnRefundManager{
    
    /**
     * 同意退换货后扩展业务接口
     */
    public void processAfterReturn(SoReturnApplication returnapp);

}
