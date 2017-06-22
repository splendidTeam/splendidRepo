package com.baozun.nebula.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.cache.CacheItemCommand;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * redis缓存管理
 * 
 * @author yuelou.zhang
 * @version 2015年11月18日
 */
@Controller
public class CacheManageController extends BaseController{

    @Autowired
    private CacheManager cacheManager;

    /**
     * 进入缓存管理页面
     */
    @RequestMapping("/system/cacheManageView.htm")
    public String goToCacheManageView(){
        return "/system/cache/cacheManage";
    }

    /**
     * 获取缓存项列表
     */
    @ResponseBody
    @RequestMapping("/system/cacheList.json")
    public List<CacheItemCommand> getCacheList(@QueryBeanParam QueryBean queryBean){
        // 获取全部缓存项
        return cacheManager.findAllCacheItem(queryBean.getParaMap());
    }

    /**
     * 根据key 清除缓存
     */
    @ResponseBody
    @RequestMapping("/system/removeCacheItem.json")
    public Object clearCache(@RequestParam("key") String key){
        cacheManager.remove(key);
        return SUCCESS;
    }

}
