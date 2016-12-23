<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>页面实例管理</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/newpage/page-instance-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">	CMS页面实例管理
		<input type="button"   value="返回" class="button butch return"  title="返回"/>
		<input type="button"   value="<spring:message code='btn.all.delete'/>" class="button butch delete"  title="<spring:message code='btn.all.delete'/>"/>
		<input type="button" value="新增页面" class="button orange add" title="新增页面"/>
	</div>
	<div class="ui-block">
		<div class="ui-block-content-lb" style="padding-bottom: 10px;" >
			<table style="font-size: 14px;">
				<tr><td>所属模板:&nbsp;&nbsp;</td><td>${ cmsPageTemplate.name }</td></tr>
			</table>
		</div>
	</div>
	
	
	<form id="searchForm">
		<input type="hidden" id="templateId" name="q_long_templateId" value="${ cmsPageTemplate.id }" />
		<div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
				<table>
					<tr>
						<td><label>页面编码</label></td>
						<td>
							<span id="searchkeytext">
								<input type="text" loxiaType="input" mandatory="false" id="code" name="q_string_code" placeholder="页面编码" />
							</span>
						</td>
						<td><label>页面名称</label></td>
						<td>
							<span id="searchkeytext">
								<input type="text" loxiaType="input" mandatory="false" id="name" name="q_sl_nameForLike" placeholder="页面名称" />
							</span>
						</td>
						<td><label>页面url</label></td>
						<td>
							<span id="searchkeytext">
								<input type="text" loxiaType="input" mandatory="false" id="url" name="q_sl_urlForLike" placeholder="页面url" />
							</span>
						</td>
						<td><label>状态</label></td>
						<td>
							<span id="searchkeytext">
								<select id="lifecycle" name="q_int_ispublished" class="ui-loxia-default ui-corner-all" aria-disabled="false">
									<option value="">不限</option>
									<option value="1">已发布</option>
									<option value="0">未发布</option>
								</select>
							</span>
						</td>
					</tr>
				</table>
				<div class="button-line1">
					<a href="javascript:void(0);"  class="func-button search"><span><spring:message code="user.list.filter.btn"/></span></a>
				</div>
			</div>
		</div>
	</form>
    <div class="ui-block">
		<div id="table1" class="border-grey" caption="页面实例列表" ></div>   
    </div>
    <div class="button-line">
		<input type="button" value="新增页面" class="button orange add" title="新增页面"/>
		<input type="button"   value="<spring:message code='btn.all.delete'/>" class="button butch delete"  title="<spring:message code='btn.all.delete'/>"/>
		<input type="button"   value="返回" class="button butch return"  title="返回"/>
    </div>

</div>

<div class="proto-dialog cms-publish-dialog" wid="">
    <h5>页面实例发布</h5>
    <div class="proto-dialog-content p10">
	<div class="ui-block">
		<div class="ui-block-line mt5 ui-block-content-lb">
			<span>开始时间和结束时间为非必填项</span><br>
			<span>如果两个都没有设置:发布就生效</span><br>
			<span>如果都设置:生效时间为开始时间,失效世界为结束时间</span><br>
			<span>如果只设置开始时间,不设置结束时间:发布后到开始时间才生效,且不会失效</span><br>
			<span>如果只设置结束时间,不设置开始时间:发布后就生效,到结束时间就失效</span>
		</div>
        <div class="ui-block-line mt5"  >
			<label title="">开始时间</label>
			<input loxiaType="date" class="starttime" showtime="true"/>
		  </div>
		  <div class="ui-block-line mt5" >
			<label>结束时间</label>
			<input loxiaType="date" class="endtime" showtime="true" />
		  </div>
	  </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="发布" class="button orange mr5 confrim" />
          <input type="button" value="关闭" class="button cancel close"/>
     </div>
</div>
</body>
</html>
