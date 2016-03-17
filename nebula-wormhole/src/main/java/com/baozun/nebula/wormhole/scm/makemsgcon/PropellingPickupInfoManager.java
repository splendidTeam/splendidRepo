package com.baozun.nebula.wormhole.scm.makemsgcon;

import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;

/**
 * 推送上门取件信息到SCM
 * @author Justin Hu
 *
 */
public interface PropellingPickupInfoManager {

	
	/**
	 * 推送上门取件信息到SCM
	 * 
	 * @param msr
	 * @return
	 */
	public MsgSendContent propellingPickupInfo(MsgSendRecord msr);
}
