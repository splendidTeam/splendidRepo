package com.baozun.nebula.sdk.manager.returnapplication;

import com.baozun.nebula.command.ReturnLineCommand;

/**
 * 退换货行退换货原因扩展处理接口
 * @author jinhui.huang
 *
 */
public interface ReturnLineReasonResolver{
    
    public void getReturnLineReason(ReturnLineCommand returnLineCommand);

}
