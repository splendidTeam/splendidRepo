package com.baozun.nebula.sdk.manager.returnapplication;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.dao.returnapplication.SdkReturnApplicationLineDao;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;



/**
 * 退换货单行信息管理
 *
 */
@Transactional
@Service("soReturnLineManager")
public class SdkReturnApplicationLineManagerImpl implements SdkReturnApplicationLineManager {

	private static final Logger	log	= LoggerFactory.getLogger(SdkReturnApplicationLineManagerImpl.class);
	@Autowired
	private SdkReturnApplicationLineDao soReturnLineDao;
	@Override
	public List<ReturnApplicationLine> saveReturnLine(List<ReturnApplicationLine> soReturnLine) {
		List<ReturnApplicationLine> returnLines=new ArrayList<ReturnApplicationLine>();
		for(ReturnApplicationLine so:soReturnLine){
		    ReturnApplicationLine returnLine=soReturnLineDao.save(so);
			returnLines.add(returnLine);
		}
		return returnLines;
	}

	
	@Override
	public List<ReturnLineCommand> findSoReturnLinesByReturnOrderIds(List<Long> returnOrderIds) {
		List<ReturnLineCommand> soReturnLines = soReturnLineDao.findSoReturnLinesByReturnOrderIds(returnOrderIds);
		return soReturnLines;
	}
	
}