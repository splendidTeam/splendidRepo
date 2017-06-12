package com.baozun.nebula.sdk.manager.returnapplication;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.dao.salesorder.SoReturnLineDao;
import com.baozun.nebula.model.salesorder.SoReturnLine;



/**
 * 退换货单行信息管理
 *
 */
@Transactional
@Service("soReturnLineManager")
public class SoReturnLineManagerImpl implements SoReturnLineManager {

	private static final Logger	log	= LoggerFactory.getLogger(SoReturnLineManagerImpl.class);
	@Autowired
	private SoReturnLineDao soReturnLineDao;
	@Override
	public List<SoReturnLine> saveReturnLine(List<SoReturnLine> soReturnLine) {
		List<SoReturnLine> returnLines=new ArrayList<SoReturnLine>();
		for(SoReturnLine so:soReturnLine){
			SoReturnLine returnLine=soReturnLineDao.save(so);
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