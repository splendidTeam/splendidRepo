package com.baozun.nebula.manager.promotion;

import javax.servlet.http.HttpServletRequest;

public interface SaveSysItemOperateLog{

    /**
     * 判断商品当前状态是否为自动下架，如果符合条件则记录日志并将状态刷为下架状态
     * @param itemId
     * @param request
     */
    void SaveSysItemOperateLog(Long itemId,Long userId,Long type);
}
