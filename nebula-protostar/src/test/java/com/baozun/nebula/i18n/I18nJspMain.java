/**
 * 
 */
package com.baozun.nebula.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.aspectj.util.FileUtil;
import org.junit.Test;

import com.baozun.nebula.i18n.file.handler.change.ChangeChineseToSpringI18n;
import com.baozun.nebula.i18n.file.handler.extract.ExtractJspChineseStringToPropertyFile;
import com.baozun.nebula.i18n.file.through.ThroughFile;


/**
 * @author xianze.zhang
 *@creattime 2013-6-27
 */
public class I18nJspMain{
	/**
	 * 提取的中文存放的临时文件名,一般不需要修改
	 */
	public static final String TEMP_FILE	= "temp.properties";
	
	/**
	 * 工作目录。临时的属性文件和备份文件会存在此目录
	 */
	public static final String WORK_PATH = "d:/work/";
	/**
	 * 备份的文件名,一般不需要修改
	 */
	public static final String BAK_DIR = "bak/";
	/**
	 * 需要进行国际化的目录或文件
	 */
	public static final String I18N_FILE = "src/main/webapp/pages/product/item/add-item.jsp";
	
	/**
	 * 已经有的国际化属性文件，程序会验证在国际化属性文件中已经有的中文，不进行重复提取，一般不需要修改
	 * 
	 */
	private static final String	PROPER_FILE = "i18n/message";
	/**
	 * 生成的临时属性文件的前缀
	 * 例如：
	 * HEAD_STRING = "role.";
	 * 那么生成的临时属性文件等号前面全部有  role.
	 * 
	 * 	role.=无标题文档
	 *	role.=修改
	 * 
	 */
	public static final String HEAD_STRING = "item.add.";
 
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		I18nJspMain main = new I18nJspMain();
//         main.collectChinese();
		main.pushCodeTojsp();
	}
	@Test
	public void collectChinese() throws Exception{
		File f = new File(WORK_PATH+TEMP_FILE);
		f.delete();
		
		ExtractJspChineseStringToPropertyFile extract = new ExtractJspChineseStringToPropertyFile(HEAD_STRING);
		extract.setToFile(WORK_PATH+TEMP_FILE);
		extract.setCheckMap(getCheckMap());
		ThroughFile through = new ThroughFile();
		through.setDefaultFileHandler(extract);
		through.throughFileAndHandle(new File(I18N_FILE));
	}
	@Test
	public void pushCodeTojsp() throws Exception{
		
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
		
		ChangeChineseToSpringI18n change = new ChangeChineseToSpringI18n();
		change.setChangeMap(getPropertiesMap());
		change.setResultPath(reslutPath);
		change.setBasePath(WORK_PATH+BAK_DIR);
		
		ThroughFile through = new ThroughFile();
		through.setDefaultFileHandler(change);
		through.throughFileAndHandle(new File(WORK_PATH+BAK_DIR));
	}
	private Map<String,String> getCheckMap(){
		ResourceBundle bundle = ResourceBundle.getBundle(PROPER_FILE,Locale.SIMPLIFIED_CHINESE);
		Map<String, String>	checkMap = new HashMap<String, String>();
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			checkMap.put(bundle.getString(key), key);
		}
		return checkMap;
	}
	
	private Map<String,String> getPropertiesMap() throws Exception{
		Map<String, String>	propertiesMap = getCheckMap();
		BufferedReader in = new BufferedReader(new FileReader(WORK_PATH+TEMP_FILE));
		String line = null;
		while ((line = in.readLine()) != null){
			if(line.trim().length()>0){
				String[] split = line.split("=");
				propertiesMap.put(split[1], split[0]);
				
			}
		}
		return propertiesMap;
	}
}
