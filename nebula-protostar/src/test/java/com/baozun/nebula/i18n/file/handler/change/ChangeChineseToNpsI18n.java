/**
 * 
 */
package com.baozun.nebula.i18n.file.handler.change;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.i18n.file.handler.AbstractChangeStringToFile;
import com.baozun.nebula.i18n.file.handler.extract.ExtractJsChineseStringToJsFile;
import com.baozun.nebula.i18n.file.handler.extract.ExtractJspChineseStringToPropertyFile;


/**
 * 将js文件中的中文替换成nps.i18n(\"" + code + "\")
 * @author xianze.zhang
 *@creattime 2013-7-5
 */
public class ChangeChineseToNpsI18n extends AbstractChangeStringToFile{
	
	
	Pattern p = Pattern.compile(ExtractJsChineseStringToJsFile.CHINESE_PATTERN_STRING);
	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.i18n.AbstractCharFileHandler#executeLine(java.lang.String)
	 */
	@Override
	protected void executeLine(String line) throws Exception{
		// TODO Auto-generated method stub
		Matcher m = p.matcher(line);
		while (m.find()){
			String value = m.group(0);
			String code = changeMap.get(value.replaceAll("\"|'", ""));
			if (code != null){
				//nps.i18n("ROLE_NAME")
				line = m.replaceFirst("nps.i18n(\"" + code + "\")");
			}else{
				break;
			}
			m = p.matcher(line);
		}
		resultFileWriter.write(line + ENTER_LINE);
	}
}
