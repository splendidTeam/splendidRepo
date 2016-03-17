package com.baozun.nebula.wormhole.scm;

import com.baozun.nebula.model.system.MsgSendRecord;

/**
 * 单个补偿机制
 * @author Justin Hu
 *
 */
public interface ProcessCompensateManager {

	/**
	 * 处理单个补偿
	 * @param msr
	 */
	void processCompensate(MsgSendRecord msr);
}
