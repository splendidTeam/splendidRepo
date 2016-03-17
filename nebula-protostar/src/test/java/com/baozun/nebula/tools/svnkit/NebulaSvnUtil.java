package com.baozun.nebula.tools.svnkit;

import java.io.File;

import com.baozun.nebula.tools.NebulaSvnOperator;



/**
 * 
 * 需要先装上TortoiseSVN,并加上command line安装选项
 * 用户svn相关分支操作
 * copy from NebulaSvnOperator
 * @see NebulaSvnOperator
 * 
 * @author yimin.qiao
 *
 */
public class NebulaSvnUtil {
	
	/**
	 * 先修改本地的版本号，提交到svn
	 * 再打分支
	 * 发布到私服
	 * 最后再将本地的版本号改成trunkVersion的类型
	 * @param destPath 分支打在什么地方（分支目录名称）
	 * @param destVersion 分支的POM的版本号
	 * @param comment svn注释
	 * @param trunkVersion trunk相关代码最后改回的版本号
	 */
	@Deprecated
	public static void copyNeblaBranch(String nebulaSvnPath, String localTrunkPath, String destPath, String destVersion, String comment, String currentTrunkVersion){
		//更新本地代码到trunk最新版本
		updateNebla(localTrunkPath);
		//修改本地POM版本号到目标版本并提交
		modifyLocalVersionAndCommit(localTrunkPath, destVersion);
		//创建分支
		copyNebla(nebulaSvnPath, "trunk/","branches/" + destPath, comment);
		//发布到私服
		deployNebla(localTrunkPath);
		//将本地的版本号改回之前的版本号
		modifyLocalVersionAndCommit(localTrunkPath, currentTrunkVersion);
	}
	
	/**
	 * 从Nebula的一个资源路径copy到另外一个，一般用于从trunk生成分支或标记
	 * @param path 相关路径 branches之后的路径
	 */
	public static void copyNebla(String nebulaSvnPath, String sourcePath, String destPath, String comment){
		SVNUtil.copy(nebulaSvnPath + "nebula-parent/" + sourcePath, nebulaSvnPath + "nebula-parent/" + destPath, comment);
		SVNUtil.copy(nebulaSvnPath + "nebula-repo/" + sourcePath, nebulaSvnPath + "nebula-repo/" + destPath, comment);
		SVNUtil.copy(nebulaSvnPath + "nebula-helix/" + sourcePath, nebulaSvnPath + "nebula-helix/" + destPath, comment);
		SVNUtil.copy(nebulaSvnPath + "nebula-protostar/" + sourcePath, nebulaSvnPath + "nebula-protostar/" + destPath, comment);
		SVNUtil.copy(nebulaSvnPath + "nebula-wormhole/" + sourcePath, nebulaSvnPath + "nebula-wormhole/" + destPath, comment);
	}
	
	/**
	 * 下载trunk到本地
	 * @param localPath 本地项目的根路径
	 */
	public static void updateNebla(String localPath){
		String batFile = SVNUtil.getAutoMakeCode() + "mvn_update.bat";
		File file = new File(batFile);
		if(file.exists()) file.delete();
		SVNUtil.makeSvnBat("@echo off", batFile);
		
		SVNUtil.updateProject(localPath + "nebula-parent", batFile);
		SVNUtil.updateProject(localPath + "nebula-repo", batFile);
		SVNUtil.updateProject(localPath + "nebula-helix", batFile);
		SVNUtil.updateProject(localPath + "nebula-protostar", batFile);
		SVNUtil.updateProject(localPath + "nebula-wormhole", batFile);
		SVNUtil.makeSvnBat("pause", batFile);
		
		//makeSvnBat("exit",batFile);
		SVNUtil.execSvnByCmd(batFile);
	}
	
	/**
	 * 提交Nebula
	 * @param localPath本地项目的根路径
	 */
	public static void commitNebula(String localPath) {
		String batFile= SVNUtil.getAutoMakeCode()+"commit_svn.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		SVNUtil.makeSvnBat("@echo off", batFile);
		SVNUtil.commitProject(localPath + "nebula-parent",batFile);
		SVNUtil.commitProject(localPath + "nebula-repo",batFile);
		SVNUtil.commitProject(localPath + "nebula-helix",batFile);
		SVNUtil.commitProject(localPath + "nebula-protostar",batFile);
		SVNUtil.commitProject(localPath + "nebula-wormhole",batFile);

		SVNUtil.makeSvnBat("pause",batFile);
		//makeSvnBat("exit",batFile);
		SVNUtil.execSvnByCmd(batFile);
	}
	
	/**
	 * install 本地的Nebula
	 * @param localPath 本地nebula路径
	 */
	public static void installNebula(String localPath) {
		String batFile = SVNUtil.getAutoMakeCode()+"mvn_install.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		SVNUtil.makeSvnBat("@echo off",batFile);
		
		SVNUtil.installProject(localPath + "nebula-parent", batFile);
		SVNUtil.installProject(localPath + "nebula-repo", batFile);
		SVNUtil.installProject(localPath + "nebula-helix", batFile);
		SVNUtil.installProject(localPath + "nebula-protostar", batFile);
		SVNUtil.installProject(localPath + "nebula-wormhole", batFile);
		SVNUtil.makeSvnBat("pause", batFile);
		
		//makeSvnBat("exit",batFile);
		SVNUtil.execSvnByCmd(batFile);
		System.out.println("run complete");
	}
	
	/**
	 * 将nebula发布到私服
	 * @param localPath 本地的nebula路径
	 */
	public static void deployNebla(String localPath){
		String batFile = SVNUtil.getAutoMakeCode()+"mvn_deploy.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		SVNUtil.makeSvnBat("@echo off", batFile);
		
		SVNUtil.deployProject(localPath + "nebula-parent", batFile);
		SVNUtil.deployProject(localPath + "nebula-repo", batFile);
		SVNUtil.deployProject(localPath + "nebula-helix", batFile);
		SVNUtil.deployProject(localPath + "nebula-protostar", batFile);
		SVNUtil.deployProject(localPath + "nebula-wormhole", batFile);
		SVNUtil.makeSvnBat("pause", batFile);
		
		//makeSvnBat("exit",batFile);
		SVNUtil.execSvnByCmd(batFile);
	}
	
	/**
	 * 修改本地的版本号
	 * @param localPath 本地Nebula路径
	 * @param version
	 */
	public static void modifyLocalVersion(String localPath, String version){
		UpdateVersion.updateVersion(version, localPath + "nebula-parent/pom.xml");
		UpdateVersion.updateVersion(version, localPath + "nebula-repo/pom.xml");
		UpdateVersion.updateVersion(version, localPath + "nebula-helix/pom.xml");
		UpdateVersion.updateVersion(version, localPath + "nebula-protostar/pom.xml");
		UpdateVersion.updateVersion(version, localPath + "nebula-wormhole/pom.xml");
	}
	
	/**
	 * 修改本地版本号以后再发布到私服
	 * @param localPath
	 * @param version
	 */
	public void modifyLocalVersionAndDeploy(String localPath,String version){
		
		modifyLocalVersion(localPath, version);
		
		deployNebla(localPath);
	}
	
	/**
	 * 修改本地版本号以后再提交到svn
	 * @param localPath
	 * @param version
	 */
	public static void modifyLocalVersionAndCommit(String localPath, String version){
		modifyLocalVersion(localPath, version);
		//谨慎使用，会提交所有更改到svn
		commitNebula(localPath);
	}
	
	/**
	 * 检出到本地
	 * @param svnPath Nebula svn路径
	 * @param sourcePath svn资源路径 branches之后的路径
	 * @param localPath 本地路径前辍
	 */
	public static void checkoutNebla(String svnPath, String sourcePath, String localPath){
		String batFile = SVNUtil.getAutoMakeCode()+"svn_checkout.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		SVNUtil.makeSvnBat("@echo off",batFile);
		
		SVNUtil.checkout(svnPath + "nebula-parent" + sourcePath, localPath + "nebula-parent",batFile);
		SVNUtil.checkout(svnPath + "nebula-repo" + sourcePath, localPath + "nebula-repo",batFile);
		SVNUtil.checkout(svnPath + "nebula-helix" + sourcePath, localPath + "nebula-helix",batFile);
		SVNUtil.checkout(svnPath + "nebula-protostar" + sourcePath, localPath + "nebula-protostar",batFile);
		SVNUtil.checkout(svnPath + "nebula-wormhole" + sourcePath, localPath + "nebula-wormhole",batFile);

		SVNUtil.makeSvnBat("pause",batFile);
		
		//makeSvnBat("exit",batFile);
		SVNUtil.execSvnByCmd(batFile);
	}
	
	/**
	 * 合并到本地
	 * @param svnPath Nebula svn路径
	 * @param sourcePath svn资源路径 branches之后的路径
	 * @param localPath 本地路径前辍
	 */
	public static void mergeNebla(String svnPath, String sourcePath, String localPath){
		
		String batFile = SVNUtil.getAutoMakeCode() + "merge_svn.bat";
		File file=new File(batFile);
		if(file.exists()) file.delete();
		SVNUtil.makeSvnBat("@echo off",batFile);
		SVNUtil.mergeTolocal(localPath + "nebula-parent", svnPath + "nebula-parent/" + sourcePath, batFile);
		SVNUtil.mergeTolocal(localPath + "nebula-repo", svnPath + "nebula-repo/" + sourcePath, batFile);
		SVNUtil.mergeTolocal(localPath + "nebula-helix", svnPath + "nebula-helix/" + sourcePath, batFile);
		SVNUtil.mergeTolocal(localPath + "nebula-protostar", svnPath + "nebula-protostar/" + sourcePath, batFile);
		SVNUtil.mergeTolocal(localPath + "nebula-wormhole", svnPath + "nebula-wormhole/" + sourcePath, batFile);

		SVNUtil.makeSvnBat("pause", batFile);
		//makeSvnBat("exit",batFile);
		SVNUtil.execSvnByCmd(batFile);
		
	}

	public static void main(String[] args) {
		/* Nebula的svn路径 */
		//String nebulaSvnPath  = "http://10.8.12.100/svn/jumbo/nebula/";
		/* 本地的nebula trunk路径 */
		//String localTrunkPath = "E:/workspace/";
		/* 本地的nebula branch路径 */
		//String localBranchPath = "E:/workspace-nebula-branch/";
		
		//创建一个分支
		//NebulaSvnUtil.copyNebla(nebulaSvnPath, "trunk/", "branches/release-5.1.13-20150513", "[new] 创建发布分支release-5.1.13-20150513，发布nebula版本5.1.13");
		
		//生成一个标记
		//NebulaSvnUtil.copyNebla(nebulaSvnPath, "trunk/", "tags/5.1.13.RELEASE-20150513", "[new] 生成发布版本5.1.13标记");
		
		//更新本地trunk代码
		//NebulaSvnUtil.updateNebla(localTrunkPath);
		
		//更新本地branch代码
		//NebulaSvnUtil.updateNebla(localBranchPath);
		
		//install trunk
		//NebulaSvnUtil.installNebula(localTrunkPath);
		
		//install branch
		//NebulaSvnUtil.installNebula(localBranchPath);
		
		//修改本地trunk版本号
		//NebulaSvnUtil.modifyLocalVersion(localTrunkPath, "5.1.13");
		
		//修改本地branch版本号
		//NebulaSvnUtil.modifyLocalVersion(localBranchPath, "5.1.13");
		
		//发布本地trunk代码到私服
		//NebulaSvnUtil.deployNebla(localTrunkPath);
		
		//发布本地branch代码到私服
		//NebulaSvnUtil.deployNebla(localBranchPath);
		
		//检出代码到本地
		//String sourcePath = "/branches/5.1.16b-SNAPSHOT";
		//String localDestPath = "E:/workspace-nebula/branches/5.1.16b/";
		//NebulaSvnUtil.checkoutNebla(nebulaSvnPath, sourcePath, localDestPath);
		
		//合并某个分支到本地
		//NebulaSvnUtil.mergeNebla(nebulaSvnPath, "/branches/release-5.1.13-20150513", localTrunkPath);
		
	}

}
