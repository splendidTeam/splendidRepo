package com.baozun.nebula.sdk.manager.returnapplication;

import com.baozun.nebula.command.OrderReturnCommand;

/**
 * 退换货原因扩展接口，各商场根据各自的退换货原因进行实现
 * @author jinhui.huang
 *
 */
public interface ReturnReasonResolver{
    
    public void  getReasonResolver(OrderReturnCommand orderReturnCommand);

}
