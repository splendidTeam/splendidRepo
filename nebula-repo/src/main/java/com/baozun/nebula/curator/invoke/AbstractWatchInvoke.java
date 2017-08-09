package com.baozun.nebula.curator.invoke;

import java.util.concurrent.atomic.AtomicBoolean;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;

/**
 * 引擎抽象类
 * 目前主要用来解决项目启动时每个watch有3秒睡眠而造成项目启动过慢的问题
 * 
 * @author  D.C
 * @date 2017/8/10
 *
 */
public abstract class AbstractWatchInvoke implements IWatcherInvoke{

	/**
	 * 是否初始化
	 */
    protected AtomicBoolean initialized = new AtomicBoolean(false);
}
