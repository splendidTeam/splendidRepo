package com.baozun.nebula.sdk.manager.system;


public interface SysItemOperateLogManager{

    /**
     * 判断商品当前状态是否为自动下架，如果符合条件则记录日志并将状态刷为下架状态
     * @param itemId
     * @param request
     */
    public void saveSysOperateLog(Long targetId,Long userId,Long type);
}
