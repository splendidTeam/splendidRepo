package com.baozun.nebula.tools.svnkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.baozun.nebula.tools.NebulaSvnOperator;



/**
 * 
 * 需要先装上TortoiseSVN,并加上command line安装选项
 * 用户svn相关分支操作
 * copy from NebulaSvnOperator
 * @see NebulaSvnOperator
 * @author yimin.qiao
 *
 */
public class SVNUtil {
	
	/**
	 * 执行命令，并返回结果
	 * @param command
	 * @return
	 */
	public static String execSvn(String command){
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getAutoMakeCode(){
		return System.getProperty("user.dir")+"/target/svnbat/";
	}
	
	public static void makeSvnBat(String command,String batFile){
		try {
			File f=new File(getAutoMakeCode());
			f.mkdirs();
			
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(batFile),true)));
			bw.write(command);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 执行命令，并返回结果
	 * @param command
	 * @return
	 */
	public static String execSvnByCmd(String batFile){
		try {
			String completeFlag=getAutoMakeCode()+"complete";
			File file=new File(completeFlag);
			if(file.exists())
				file.delete();
			
			makeSvnBat("echo complete > "+completeFlag,batFile);
			makeSvnBat("exit",batFile);
			
			String command="cmd.exe /c start "+batFile;
			Process process = Runtime.getRuntime().exec(command);
			//process.waitFor();
			while(true){
				Thread.sleep(1000);
				file=new File(completeFlag);
				if(file.exists()) break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//File file=new File(batFile);
			//if(file.exists()) file.delete();
		}
		
		return null;
	}
	
	public static void sleep(Long time){
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//File file=new File(batFile);
			//if(file.exists()) file.delete();
		}
	}
	
	/**
	 * 
	 * @param source 全路径
	 * @param dest 合路径
	 */
	public static void copy(String source, String dest, String comment){
		String command="svn copy "+source+" "+dest+" -m \""+comment+"\"";
		
		String result=execSvn(command);
		System.out.println("exec--command:"+command);
		System.out.println("exec--result:"+result);
	}
	
	/**
	 * 将远程的数据meger到本地
	 * @param localPath
	 * @param remoteUrl
	 */
	public static void mergeTolocal(String localPath, String remoteUrl, String batFile){
		String command="svn merge "+remoteUrl+" "+localPath+" ";
		System.out.println("exec--command:"+command);
		makeSvnBat(command,batFile);
	}
	
	/**
	 * 发布到私服
	 * @param path
	 */
	public static void deployProject(String path, String batFile){
		String command="call mvn clean deploy";
		makeSvnBat("cd /d "+path,batFile);
		makeSvnBat(command,batFile);
		
		System.out.println("exec--command:"+command);
	}
	
	
	/**
	 * 更新到本地
	 * @param path
	 * @param batFile
	 */
	public static void updateProject(String path, String batFile){
		String command="call svn update";
		makeSvnBat("cd /d "+ path, batFile);
		makeSvnBat(command, batFile);
		
		System.out.println("exec--command:" + command);
	}
	
	/**
	 * 本地install
	 * @param path
	 */
	public static void installProject(String path, String batFile){
		String command="call mvn clean install";
		makeSvnBat("cd /d "+path,batFile);
		makeSvnBat(command,batFile);
		
		System.out.println("exec--command:"+command);
	}
	
	/**
	 * 本地提交
	 * @param path
	 */
	public static void commitProject(String path, String batFile){
		String command="call svn commit -m \"auto update project version by tools\"";
		makeSvnBat("cd /d "+path,batFile);
		
		makeSvnBat(command,batFile);
		
		System.out.println("exec--command:"+command);
	}
	
	
	/**
	 * 从服务器进行检出
	 * @param url
	 * @param path
	 * @param batFile
	 */
	public static void checkout(String url,String path,String batFile){
		
		String command="svn checkout "+url+" "+path;
		makeSvnBat(command,batFile);
		System.out.println("exec--command:"+command);
	}
	
	
	/**
	 * 
	 * @param path 相关路径 branches之后的路径
	 */
	@Deprecated
	public void copyLocalNebla(String destPath, String comment){
		String base="http://10.8.12.100/svn/jumbo/nebula/";
		
		String wcbase="E:/workspace4.3/";
		copy(wcbase+"nebula-parent",base+"nebula-parent/"+destPath+"",comment);
		copy(wcbase+"nebula-repo",base+"nebula-repo/"+destPath,comment);
		copy(wcbase+"nebula-helix",base+"nebula-helix/"+destPath,comment);
		copy(wcbase+"nebula-protostar",base+"nebula-protostar/"+destPath,comment);
		copy(wcbase+"nebula-wormhole",base+"nebula-wormhole/"+destPath,comment);
	}
	
	public void copyLocalNeblaTag(String destPath,String comment){
		
		//copyNebla("trunk/","tags/"+destPath,comment);
		//copyLocalNebla("tags/"+destPath,comment);
	}

}
