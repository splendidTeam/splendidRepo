package com.baozun.nebula.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.baozun.nebula.tools.svnkit.UpdateVersion;
import com.baozun.nebula.utils.FileUtils;



/**
 * 
 * 需要先装上TortoiseSVN,并加上command line安装选项
 * 用户svn相关分支操作
 * @author Justin Hu
 * 
 *
 */
public class NebulaSvnOperator {
	
	
	/**
	 * 执行命令，并返回结果
	 * @param command
	 * @return
	 */
	public String execSvn(String command){
		
		Process process;
		try {
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private String getAutoMakeCode(){
		
		return System.getProperty("user.dir")+"/target/svnbat/";
	}
	
	public void makeSvnBat(String command,String batFile){
		
		try {
			File f=new File(getAutoMakeCode());
			f.mkdirs();
			
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(batFile),true)));
			bw.write(command);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 执行命令，并返回结果
	 * @param command
	 * @return
	 */
	public String execSvnByCmd(String batFile){
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//File file=new File(batFile);
			//if(file.exists()) file.delete();
		}
		
		return null;
		
	}
	
	public void sleep(Long time){
		
		try {
			Thread.sleep(time);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	public void copy(String source,String dest,String comment){
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
	public void mergeTolocal(String localPath,String remoteUrl,String batFile){
		
		String command="svn merge "+remoteUrl+" "+localPath+" ";
		System.out.println("exec--command:"+command);
		makeSvnBat(command,batFile);
		
	
	}
	
	/**
	 * 发布到私服
	 * @param path
	 */
	public void deployProject(String path,String batFile){
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
	public void updateProject(String path,String batFile){
		String command="call svn update";
		makeSvnBat("cd /d "+path,batFile);
		makeSvnBat(command,batFile);
		
		System.out.println("exec--command:"+command);
	}
	
	/**
	 * 本地install
	 * @param path
	 */
	public void installProject(String path,String batFile){
		String command="call mvn clean install";
		makeSvnBat("cd /d "+path,batFile);
		makeSvnBat(command,batFile);
		
		System.out.println("exec--command:"+command);
	}
	
	/**
	 * 本地install
	 * @param path
	 */
	public void commitProject(String path,String batFile){
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
	public void checkout(String url,String path,String batFile){
		
		String command="svn checkout "+url+" "+path;
		makeSvnBat(command,batFile);
		System.out.println("exec--command:"+command);
	}
	
	/**
	 * 
	 * @param path 相关路径 branches之后的路径
	 */
	public void copyNebla(String sourcePath,String destPath,String comment){
		String base="http://10.8.12.100/svn/jumbo/nebula/";
		
		
		copy(base+"nebula-parent/"+sourcePath,base+"nebula-parent/"+destPath,comment);
		copy(base+"nebula-repo/"+sourcePath,base+"nebula-repo/"+destPath,comment);
		copy(base+"nebula-helix/"+sourcePath,base+"nebula-helix/"+destPath,comment);
		copy(base+"nebula-protostar/"+sourcePath,base+"nebula-protostar/"+destPath,comment);
		copy(base+"nebula-wormhole/"+sourcePath,base+"nebula-wormhole/"+destPath,comment);
	}
	
	/**
	 * 先修改本地的版本号，提交到svn
	 * 再打分支
	 * 发布到私服
	 * 最后再将本地的版本号改成trunkVersion的类型
	 * @param destPath 分支打在什么地方
	 * @param comment svn注释
	 * @param trunkVersion trunk相关代码最后改回的版本号
	 */
	public void copyNeblaBranch(String destPath,String comment,String trunkVersion){
		updateNeblaTrunk("E:/workspace4.3-trunk/trunk-");
		
		modifyLocalVersionAndCommit("E:/workspace4.3-trunk/trunk-",destPath);
		
		
		copyNebla("trunk/","branches/"+destPath,comment);
		
		deployNeblaTrunk("E:/workspace4.3-trunk/trunk-");
		
		modifyLocalVersionAndCommit("E:/workspace4.3-trunk/trunk-",trunkVersion);
	}
	
	/**
	 * 打tag分支
	 * @param destVersion 目标版本号
	 * @param comment 注释
	 * @param version trunk还原的版本号
	 */
	public void copyNeblaTag(String destVersion,String comment,String version){
		
		updateNeblaTrunk("E:/workspace4.3-trunk/trunk-");
		
		modifyLocalVersionAndCommit("E:/workspace4.3-trunk/trunk-",destVersion);
		
		copyNebla("trunk/","tags/"+destVersion,comment);
		
		deployNeblaTrunk("E:/workspace4.3-trunk/trunk-");
		
		modifyLocalVersionAndCommit("E:/workspace4.3-trunk/trunk-",version);
	}
	
	/**
	 * 
	 * @param path 相关路径 branches之后的路径
	 */
	public void copyLocalNebla(String destPath,String comment){
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
	
	public void mergeNebla(String sourcePath){
		String base="http://10.8.12.100/svn/jumbo/nebula/";
		
		String batFile=getAutoMakeCode()+"merge_svn.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		makeSvnBat("@echo off",batFile);
		mergeTolocal("E:/workspace4.3/nebula-parent",base+"nebula-parent/"+sourcePath,batFile);
		mergeTolocal("E:/workspace4.3/nebula-repo",base+"nebula-repo/"+sourcePath,batFile);
		mergeTolocal("E:/workspace4.3/nebula-helix",base+"nebula-helix/"+sourcePath,batFile);
		mergeTolocal("E:/workspace4.3/nebula-protostar",base+"nebula-protostar/"+sourcePath,batFile);
		mergeTolocal("E:/workspace4.3/nebula-wormhole",base+"nebula-wormhole/"+sourcePath,batFile);

		makeSvnBat("pause",batFile);
		//makeSvnBat("exit",batFile);
		execSvnByCmd(batFile);
		
	}
	
	/**
	 * 将trunk发布到私服
	 * @param path 相关路径 branches之后的路径
	 */
	public void deployNeblaTrunk(String base){
		String batFile=getAutoMakeCode()+"mvn_deploy.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		makeSvnBat("@echo off",batFile);
		
		deployProject(base+"nebula-parent",batFile);
		deployProject(base+"nebula-repo",batFile);
		deployProject(base+"nebula-helix",batFile);
		deployProject(base+"nebula-protostar",batFile);
		deployProject(base+"nebula-wormhole",batFile);
		makeSvnBat("pause",batFile);
		
		//makeSvnBat("exit",batFile);
		execSvnByCmd(batFile);
	}
	
	/**
	 * 下载trunk到本地
	 * @param base 本地项目的根路径，以及前辍
	 */
	public void updateNeblaTrunk(String base){
		String batFile=getAutoMakeCode()+"mvn_update.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		makeSvnBat("@echo off",batFile);
		
		updateProject(base+"nebula-parent",batFile);
		updateProject(base+"nebula-repo",batFile);
		updateProject(base+"nebula-helix",batFile);
		updateProject(base+"nebula-protostar",batFile);
		updateProject(base+"nebula-wormhole",batFile);
		makeSvnBat("pause",batFile);
		
		//makeSvnBat("exit",batFile);
		execSvnByCmd(batFile);
	}
	
	
	/**
	 * 
	 * @param url svn相关路径 branches之后的路径
	 * @param localbase 本地路径前辍
	 */
	public void checkoutNebla(String localBase,String url){
		String base="http://10.8.12.100/svn/jumbo/nebula/";
		String batFile=getAutoMakeCode()+"svn_checkout.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		makeSvnBat("@echo off",batFile);
		
		checkout(base+"nebula-parent"+url,localBase+"nebula-parent",batFile);
		checkout(base+"nebula-repo"+url,localBase+"nebula-repo",batFile);
		checkout(base+"nebula-helix"+url,localBase+"nebula-helix",batFile);
		checkout(base+"nebula-protostar"+url,localBase+"nebula-protostar",batFile);
		checkout(base+"nebula-wormhole"+url,localBase+"nebula-wormhole",batFile);

		makeSvnBat("pause",batFile);
		
		//makeSvnBat("exit",batFile);
		execSvnByCmd(batFile);
	}
	
	public void mergeNeblaBranch(String sourcePath){
		
		mergeNebla("branches/"+sourcePath);
	}
	
	public void mergeNeblaTag(String sourcePath){
		
		mergeNebla("tags/"+sourcePath);
	}
	
	
	
	/**
	 * 修改本地的版本号
	 * @param startWith
	 * @param version
	 */
	public void modifyLocalVersion(String startWith,String version){
		
		
		UpdateVersion.updateVersion(version,startWith+"nebula-parent/pom.xml");
		UpdateVersion.updateVersion(version,startWith+"nebula-repo/pom.xml");
		UpdateVersion.updateVersion(version,startWith+"nebula-helix/pom.xml");
		UpdateVersion.updateVersion(version,startWith+"nebula-protostar/pom.xml");
		UpdateVersion.updateVersion(version,startWith+"nebula-wormhole/pom.xml");
		
		String batFile=getAutoMakeCode()+"mvn_install.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		makeSvnBat("@echo off",batFile);
		
		installProject(startWith+"nebula-parent",batFile);
		installProject(startWith+"nebula-repo",batFile);
		installProject(startWith+"nebula-helix",batFile);
		installProject(startWith+"nebula-protostar",batFile);
		installProject(startWith+"nebula-wormhole",batFile);
		makeSvnBat("pause",batFile);
		
		//makeSvnBat("exit",batFile);
		execSvnByCmd(batFile);
		System.out.println("run complete");
	}
	
	/**
	 * 修改本地版本号以后再发布到私服
	 * @param startWith
	 * @param version
	 */
	public void modifyLocalVersionAndDeploy(String startWith,String version){
		
		modifyLocalVersion(startWith,version);
		
		deployNeblaTrunk(startWith);
	}
	
	/**
	 * 修改本地版本号以后再提交到svn
	 * @param startWith
	 * @param version
	 */
	public void modifyLocalVersionAndCommit(String startWith,String version){
		
		modifyLocalVersion(startWith,version);
		
		String batFile=getAutoMakeCode()+"commit_svn.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		makeSvnBat("@echo off",batFile);
		commitProject(startWith+"nebula-parent",batFile);
		commitProject(startWith+"nebula-repo",batFile);
		commitProject(startWith+"nebula-helix",batFile);
		commitProject(startWith+"nebula-protostar",batFile);
		commitProject(startWith+"nebula-wormhole",batFile);

		makeSvnBat("pause",batFile);
		//makeSvnBat("exit",batFile);
		execSvnByCmd(batFile);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//打分支
		//new NebulaSvnOperator().copyNeblaBranch("5.1.19b-SNAPSHOT", "自动打分支--by justin--solr 修改","5.1.18t-SNAPSHOT");
		//new NebulaSvnOperator().copyNeblaTag("5.1.12", "自动打分支--by justin","5.1.19t-SNAPSHOT");
		//new NebulaSvnOperator().copyLocalNeblaTag("4.4.0", "自动打分支--by justin");
		
		//合并
		//new NebulaSvnOperator().mergeNeblaBranch("5.1.16b-SNAPSHOT");
		//  new NebulaSvnOperator().mergeNeblaTag("5.0.1");
		
		
		
		//update
		//new NebulaSvnOperator().updateNeblaTrunk("E:/workspace4.3-trunk/trunk-");
		
		//deploy
		//new NebulaSvnOperator().deployNeblaTrunk("E:/workspace4.3-trunk/trunk-");
		
		//checkout
		//new NebulaSvnOperator().checkoutNebla("E:/workspace4.3-branches/5.1.16-tags-", "/branches/5.1.16b-SNAPSHOT");
		
		//new NebulaSvnOperator().checkoutNebla("E:/workspace4.3-tags/5.1.10.3-tags-", "/tags/5.1.10.3");
		
		//修改本地版本号
		//new NebulaSvnOperator().modifyLocalVersion("E:/workspace4.3-trunk/trunk-","5.1.18t-SNAPSHOT");
		
		//修改本地版本号后发布到私服
		//new NebulaSvnOperator().modifyLocalVersionAndDeploy("E:/workspace4.3-trunk/trunk-","5.1.18t-SNAPSHOT");
		
		//修改本地版本号后提交到svn
		//new NebulaSvnOperator().modifyLocalVersionAndCommit("E:/workspace4.3-trunk/trunk-","5.1.18t-SNAPSHOT");
		
		//发布到私服
		//new NebulaSvnOperator().deployNebla("E:/workspace4.3-trunk/trunk-");
		

		
	}

}
