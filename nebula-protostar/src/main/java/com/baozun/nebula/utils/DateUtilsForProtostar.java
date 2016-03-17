package com.baozun.nebula.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.solr.utils.DatePattern;

/**
 * 
 * @author 项硕
 */
public class DateUtilsForProtostar {

	public static Date parse(String source) {
		try {
			return new SimpleDateFormat(DatePattern.commonWithTime).parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodes.DATE_FORMAT_FAIL);
		}
	}
}
