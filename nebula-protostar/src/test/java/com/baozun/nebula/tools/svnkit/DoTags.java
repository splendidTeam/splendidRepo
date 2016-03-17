package com.baozun.nebula.tools.svnkit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * 
 * @Description:进行nebula 项目 tag 建立
 * @author 何波
 * @date 2014年8月25日 下午3:28:25
 * 
 */
public class DoTags {
	// 声明SVN客户端管理类
	private static SVNClientManager ourClientManager;

	private static String username = "hbhk";
	private static String password = "123456";

	public static void main(String[] args) throws Exception {
		List<TagsInfo> tags = new ArrayList<TagsInfo>();
		TagsInfo tag = new TagsInfo();
		String msg = "打nebula项目 6.0.0 tag";
		tag.setSrcUrl("https://10.8.17.39/svn/hbhk-repo/project1/trunk/svn1/");
		tag.setDistUrl("https://10.8.17.39/svn/hbhk-repo/project1/tags");
		tag.setPomPath("D:/baocun-ws/svn1/pom.xml");
		tag.setMsg(msg);
		TagsInfo tag1 = new TagsInfo();
		tag1.setSrcUrl("https://10.8.17.39/svn/hbhk-repo/project2/trunk/svn2/");
		tag1.setDistUrl("https://10.8.17.39/svn/hbhk-repo/project2/tags");
		tag1.setPomPath("D:/baocun-ws/svn2/pom.xml");
		tag1.setMsg(msg);
		TagsInfo p = new TagsInfo();
		p.setSrcUrl("https://10.8.17.39/svn/hbhk-repo/parent/trunk/parent-test/");
		p.setDistUrl("https://10.8.17.39/svn/hbhk-repo/parent/tags");
		p.setPomPath("D:/baocun-ws/parent-test/pom.xml");
		p.setMsg(msg);
		tags.add(tag);
		tags.add(tag1);
		tags.add(p);
		String version = "6.0.2";
		doTags(tags, username, password, version);

	}

	public static void doTags(List<TagsInfo> tags, String username,
			String password, String version) throws Exception {
		// 实例化客户端管理类
		SVNRepositoryFactoryImpl.setup();
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, username, password);
		// 校验源地址和目标地址和pom.xml
		List<String> dists = new ArrayList<String>();
		for (TagsInfo tagsInfo : tags) {
			if (!isURLExist(tagsInfo.getSrcUrl())) {
				throw new RuntimeException("源地址:" + tagsInfo.getSrcUrl()
						+ "不存在");
			}
			String disturl = tagsInfo.getDistUrl();
			if (dists.contains(disturl)) {
				throw new RuntimeException("目标地址:" + tagsInfo.getDistUrl()
						+ "存在重复");
			}
			if (isURLExist(disturl)) {
				throw new RuntimeException("目标地址:" + disturl + "  已经存在");
			}
			dists.add(disturl);
			String pomPath = tagsInfo.getPomPath();
			if (pomPath == null || pomPath.equals("")) {
				throw new RuntimeException("pom.xml 路径不能为空");
			}
			File file = new File(pomPath);
			if (!file.exists()) {
				throw new RuntimeException("路径:" + pomPath + "不存在");
			}
		}
		// 修改版本号
		System.out.println("修改版本号和提交开始");
		for (TagsInfo tagsInfo : tags) {
			// 修改版本
			UpdateVersion.updateVersion(version, tagsInfo.getPomPath());
			// // 提交
			commmit(username, password, tagsInfo.getPomPath(),
					tagsInfo.getMsg());
		}
		System.out.println("修改版本号和提交完成:");
		// 执行导入操作
		System.out.println("打包tag开始");
		List<SVNCommitInfo> commitInfos = copy(tags);
		for (SVNCommitInfo svnCommitInfo : commitInfos) {
			System.out.println("返回信息:" + svnCommitInfo.toString());
		}
		System.out.println("打包tag完成 ");
		// 还原版本号
		System.out.println("还原版本号开始");
		for (TagsInfo tagsInfo : tags) {
			// 修改版本
			UpdateVersion.updateVersion(UpdateVersion.orivsersion,
					tagsInfo.getPomPath());
			// 提交
			commmit(username, password, tagsInfo.getPomPath(),
					tagsInfo.getMsg());
		}
		System.out.println("还原版本号完成");
	}

	private static List<SVNCommitInfo> copy(List<TagsInfo> tag)
			throws SVNException {

		List<SVNCommitInfo> infos = new ArrayList<SVNCommitInfo>();
		for (TagsInfo tagsInfo : tag) {
			String src = tagsInfo.getSrcUrl();
			String dist = tagsInfo.getDistUrl();
			SVNURL srcURL1 = SVNURL.parseURIEncoded(src);

			SVNCopySource[] sources = new SVNCopySource[1];
			SVNCopySource ss = new SVNCopySource(SVNRevision.HEAD,
					SVNRevision.HEAD, srcURL1);
			sources[0] = ss;
			SVNURL distURL = SVNURL.parseURIEncoded(dist);
			SVNProperties svnp = SVNProperties
					.wrap(new HashMap<String, Object>());
			SVNCommitInfo info = ourClientManager.getCopyClient().doCopy(
					sources, distURL, false, true, false, tagsInfo.getMsg(),
					svnp);
			infos.add(info);
		}

		return infos;
	}

	/**
	 * 确定一个URL在SVN上是否存在
	 */
	public static boolean isURLExist(String url) {
		try {
			SVNURL svnurl = SVNURL.parseURIEncoded(url);
			SVNRepository svnRepository = SVNRepositoryFactory.create(svnurl);
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(username, password);
			svnRepository.setAuthenticationManager(authManager);
			SVNNodeKind nodeKind = svnRepository.checkPath("", -1);
			return nodeKind == SVNNodeKind.NONE ? false : true;
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void commmit(String username, String password,
			String pomPath, String msg) throws Exception {
		// 初始化支持svn://协议的库。 必须先执行此操作。
		SVNRepositoryFactoryImpl.setup();
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, username, password);
		// 要提交的文件
		File commitFile = new File(pomPath);
		// 获取此文件的状态（是文件做了修改还是新添加的文件？）
		SVNStatus status = ourClientManager.getStatusClient().doStatus(
				commitFile, true);
		// 如果此文件是新增加的则先把此文件添加到版本库，然后提交。
		if (status == null
				|| status.getContentsStatus() == SVNStatusType.STATUS_UNVERSIONED) {
			// 把此文件增加到版本库中
			// ourClientManager.getWCClient().doAdd(commitFile, false, false,
			// false, SVNDepth.INFINITY, false, false);
			// // 提交此文件
			// ourClientManager.getCommitClient().doCommit(
			// new File[] { commitFile }, true, msg, null, null, true,
			// false, SVNDepth.INFINITY);
			// System.out.println("add");
			throw new RuntimeException("文件:" + commitFile + "不在版本控制中");
		}
		// 如果此文件不是新增加的，直接提交。
		else {
			ourClientManager.getCommitClient().doCommit(
					new File[] { commitFile }, true, msg, null, null, true,
					false, SVNDepth.INFINITY);
			System.out.println("commit");
		}
		if (status != null) {
			System.out.println(commitFile + "状态:" + status.getContentsStatus());

		}

	}
}
