/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
*
 
* This software is the confidential and proprietary information of Baozun.
 
* You shall not disclose such Confidential Information and shall use it only in
 
* accordance with the terms of the license agreement you entered into
 
* with Baozun.
 
*
 
* BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 
* SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 
* IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 
* PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 
* SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 
* THIS SOFTWARE OR ITS DERIVATIVES.
 
*
 
*/
package com.baozun.nebula.curator.watcher;

/**
 * 这只是一个Demo
 * 
 * @author yue.ch
 * @time 2016年5月23日 上午10:48:10
 */
public class DemoWatcherInvoke implements IWatcherInvoke {

	/* (non-Javadoc)
	 * @see com.baozun.nebula.curator.watcher.IWatcherInvoke#invoke(java.lang.String, byte[])
	 */
	@Override
	public void invoke(String path, byte[] data) {
		// TODO 这里处理业务逻辑
		System.out.println("I'm Watcher process: path = " + path + ", data = " + new String(data));
	}

}
