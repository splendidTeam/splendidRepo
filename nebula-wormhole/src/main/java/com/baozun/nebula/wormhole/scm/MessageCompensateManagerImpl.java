package com.baozun.nebula.wormhole.scm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;

@Service("messageCompensateManager")
public class MessageCompensateManagerImpl implements MessageCompensateManager {

	private static final Logger log = LoggerFactory.getLogger(MessageCompensateManagerImpl.class);
	@Autowired
	private SdkMsgManager sdkMsgManager;
	
	@Autowired
	private ProcessCompensateManager processCompensateManager;
	
	@Override
	public void timeMessageCompensate() {
		// TODO Auto-generated method stub
		List<MsgSendRecord>  recordList=sdkMsgManager.findAllNoFeedbackMsgSendRecordList();
		
		for(MsgSendRecord msr:recordList){
			try{
				processCompensateManager.processCompensate(msr);
			}
			catch(Exception e){
				e.printStackTrace();
				log.error("send error --- targetId:"+msr.getTargetId()+" ifIdentify:"+msr.getIfIdentify());
			}
		}
	}
	
	
	
	

}
