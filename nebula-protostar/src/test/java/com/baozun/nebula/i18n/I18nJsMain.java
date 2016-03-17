/**
 * 
 */
package com.baozun.nebula.i18n;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.i18n.file.handler.change.ChangeChineseToNpsI18n;
import com.baozun.nebula.i18n.file.handler.change.ChangeChineseToSpringI18n;
import com.baozun.nebula.i18n.file.handler.extract.ExtractJsChineseStringToJsFile;
import com.baozun.nebula.i18n.file.handler.extract.ExtractJspChineseStringToPropertyFile;
import com.baozun.nebula.i18n.file.through.ThroughFile;


/**
 * @author xianze.zhang
 *@creattime 2013-6-27
 */
public class I18nJsMain{

	/**
	 * 提取的中文存放的临时文件名，一般不需要改
	 */
	public static final String TEMP_FILE	= "temp.js";
	
	/**
	 * 工作目录。临时的属性文件和备份文件会存在此目录
	 */
	public static final String WORK_PATH = "d:/work/";
	/**
	 * 备份的文件名，一般不需要改
	 */
	public static final String BAK_DIR = "bak/";
	/**
	 * 需要进行国际化的目录或文件
	 */
	public static final String I18N_FILE = "src/main/webapp/scripts/role/";
	
	/**
	 * 已经有的国际化文件
	 */
	private static final String	PROPER_FILE = "src/main/webapp/scripts/i18n/common.locale-zh-CN.js";
	/**
	 * 生成的临时文件的前缀
	 * 例如：
	 * HEAD_STRING = "role.";
	 * 那么生成的临时属性文件等号前面全部有  role.
	 * 
	 * 	role.=无标题文档
	 *	role.=修改
	 * 
	 */
	public static final String HEAD_STRING = "";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
//		collectChinese();
		I18nJsMain js = new I18nJsMain();
//		js.extractJsChinese();
		js.pushCodeToJs();
	}
	/**
	 * 提取js中的中文
	 * @throws Exception
	 */
	@Test
	public void extractJsChinese() throws Exception{
		File f = new File(WORK_PATH+TEMP_FILE);
		f.delete();
		
		ExtractJsChineseStringToJsFile extract = new ExtractJsChineseStringToJsFile(HEAD_STRING);
		extract.setToFile(WORK_PATH+TEMP_FILE);
		extract.setCheckMap(getCheckMap());
		ThroughFile through = new ThroughFile();
		through.setDefaultFileHandler(extract);
		through.throughFileAndHandle(new File(I18N_FILE));
	}
	/**
	 * 将中文替换成nps.i18n("")
	 * @throws Exception
	 */
	@Test
	public void pushCodeToJs() throws Exception{
		
		File iFile = new File(I18N_FILE);
		//替换后文件的输出路径
		String reslutPath = I18N_FILE;
		if(iFile.isDirectory()){
			//备份
			FileUtil.copyDir(iFile, new File(WORK_PATH+BAK_DIR));
		}else{
			reslutPath = I18N_FILE.substring(0, I18N_FILE.lastIndexOf("/")+1);
			//备份
			FileUtil.copyFile(iFile, new File(WORK_PATH+BAK_DIR+iFile.getName()));
		}
		
		
		ChangeChineseToNpsI18n change = new ChangeChineseToNpsI18n();
		change.setChangeMap(getI18nMap());
		change.setResultPath(reslutPath);
		change.setBasePath(WORK_PATH+BAK_DIR);
		
		ThroughFile through = new ThroughFile();
		through.setDefaultFileHandler(change);
		through.throughFileAndHandle(new File(WORK_PATH+BAK_DIR));
	}
	private Map<String,String> getCheckMap() throws Exception{
		return putMapFromJsFile(PROPER_FILE, new HashMap<String, String>());
	}
	private Map<String,String> getI18nMap() throws Exception{
		return putMapFromJsFile(WORK_PATH+TEMP_FILE, getCheckMap());
	}
	
	private Map<String,String> putMapFromJsFile(String file,Map<String,String> map) throws Exception{
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = in.readLine()) != null){
			if(line.trim().length()>0&&line.indexOf(":")!=-1){
				line = line.replaceAll(",", "");
				String[] split = line.trim().split(":");
				map.put(split[1].replaceAll("\"", ""), split[0].replaceAll("\"", ""));
				
			}
		}
		in.close();
		return map;
	}
}
