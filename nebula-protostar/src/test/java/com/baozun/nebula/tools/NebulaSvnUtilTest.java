package com.baozun.nebula.tools;

import org.junit.Test;

import com.baozun.nebula.tools.svnkit.NebulaSvnUtil;




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
public class NebulaSvnUtilTest {
	
	/* Nebula的svn路径 */
	private String nebulaSvnPath  = "http://10.8.12.100/svn/jumbo/nebula/";
	/* 本地的nebula trunk路径 */
	private String localTrunkPath = "D:/workspaces/nebula-trunk/";
	/* 本地的nebula branch路径 */
	private String localBranchPath = "D:/workspaces/nebula-branch/feature-20151104-samsung_optimize/";
	
	/**
	 * 创建分支测试
	 */
	@Test
	public void testCreateNebulaBranch() {
		//创建一个分支
		//NebulaSvnUtil.copyNebla(nebulaSvnPath, "trunk/", "branches/feature-20150923-nebula-optimize", "[new] 创建功能分支feature-20150923-nebula-optimize，赵元开发nebula优化功能");
	}
	
	/**
	 * 创建标记测试
	 */
	@Test
	public void testCreateNebulaTag() {
		//生成一个标记
		//NebulaSvnUtil.copyNebla(nebulaSvnPath, "branches/release-5.2.0-20150520/", "tags/5.2.0.RELEASE-20150912", "[new] 从branches/release-5.2.0-20150520/生成发布版本5.2.0标记");
	}
	
	/**
	 * 更新本地trunk代码测试
	 */
	@Test
	public void testUpdateLocalNebulaTrunk() {
		//更新本地trunk代码
		//NebulaSvnUtil.updateNebla(localTrunkPath);
	}
	
	/**
	 * 更新本地branch代码测试
	 */
	@Test
	public void testUpdateLocalNebulaBranch() {
		//更新本地branch代码
		//NebulaSvnUtil.updateNebla(localBranchPath);
	}
	
	/**
	 * 本地trunk代码install测试
	 */
	@Test
	public void testInstallLocalNebulaTrunk() {
		//install trunk
		//NebulaSvnUtil.installNebula(localTrunkPath);
	}
	
	/**
	 * 本地branch代码install测试
	 */
	@Test
	public void testInstallLocalNebulaBranch() {
		//install branch
		//NebulaSvnUtil.installNebula(localBranchPath);
	}
	
	/**
	 * 修改本地trunk版本号测试
	 */
	@Test
	public void testUpdateLocalNebulaTrunkVersion() {
		//修改本地trunk版本号
		//NebulaSvnUtil.modifyLocalVersion(localTrunkPath, "5.1.13");
	}
	
	/**
	 * 修改本地branch版本号测试
	 */
	@Test
	public void testUpdateLocalNebulaBranchVersion() {
		//修改本地branch版本号
		//NebulaSvnUtil.modifyLocalVersion(localBranchPath, "5.2.1bf.samsung-SNAPSHOT");
	}
	
	/**
	 * 将本地trunk代码发布到私服测试
	 */
	@Test
	public void testDeployLocalNebulaTrunk() {
		//发布本地trunk代码到私服
		//NebulaSvnUtil.deployNebla(localTrunkPath);
	}

	/**
	 * 将本地branch代码发布到私服测试
	 */
	@Test
	public void testDeployLocalNebulaBranch() {
		//发布本地branch代码到私服
		//NebulaSvnUtil.deployNebla(localBranchPath);
	}
	
	/**
	 * 测试检出代码到本地
	 */
	@Test
	public void testCheckoutNebulaToLocal() {
		//检出代码到本地
		//String sourcePath = "/branches/feature-20150527-item_update_upload";
		//String localDestPath = "E:/workspace-nebula-branch/feature-20150527-item_update_upload/";
		//NebulaSvnUtil.checkoutNebla(nebulaSvnPath, sourcePath, localDestPath);
	}
	
	/**
	 * 测试合并某个分支到本地trunk
	 */
	@Test
	public void testMergeNebulaBranchToLocalTrunk() {
		//合并某个分支到本地
		//NebulaSvnUtil.mergeNebla(nebulaSvnPath, "/branches/release-5.1.13-20150513", localTrunkPath);
	}
	
}
