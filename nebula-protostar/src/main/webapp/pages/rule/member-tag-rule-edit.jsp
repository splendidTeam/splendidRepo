<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/rule/member-tag-rule-edit.js"></script>
</head>
<body>
<%-- 数据回填 --%>
<input id="hid-isUpdate" type="hidden" value="${isUpdate }" />
<input id="hid-type" type="hidden" value="${combo.type }" />
<input id="hid-id" type="hidden" value="${combo.id }" />
<%-- 数据回填 --%>

<div class="content-box" style="float: left; width: 100%;">
	<div class="ui-block">
		 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
		 	  <div class="ui-loxia-nav"><span class="ui-loxia-table-title">会员筛选器</span></div>
		 	  <div class="ui-block-line p5">
		 	  	<label><spring:message code='member.filter.type'/></label>
		 	  	<opt:select id="slt-type" loxiaType="select" expression="chooseOption.MEMBER_FILTER_TYPE"  />
			  </div>
		 	  <div class="ui-block-line p5">
					<label><spring:message code='member.filter.name'/></label>
					<div class="wl-right">
						<input id="txt-name" type="text" loxiaType="input" value="${combo.name }" mandatory="true" size="50" />
					</div>
				</div>	
				<%-- 会员 --%>
				<div id="member-block" class="toggle-block">
				
		 	  <table class="pt10" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code='member.filter.memberName'/></th>
							<th style="width: 20%;"><spring:message code='member.filter.operation'/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${incList }" var="item" varStatus="status">
						<tr data="${item.id }" class=<c:if test="${status.index%2==0 }">"even"</c:if><c:if test="${status.index%2!=0 }">"odd"</c:if>>
							<td>${item.name }</td>
							<td><a class="func-button btn-remove" title="<spring:message code='btn.delete'/>" href="javascript:void(0);"><spring:message code='btn.delete'/></a></td>
						</tr>
					</c:forEach>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15 right">
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			</div>
				</div>
				<%-- 会员分组 --%>
				<div id="group-block" class="toggle-block">
				<div id="group-block-include" style="float: left; width: 55%; margin-right: 20px;">
				<div class="bold p5"><spring:message code='member.filter.includeList'/></div>
				
		 	  <table class="pt10" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code='member.filter.groupName'/></th>
							<th style="width: 20%;"><spring:message code='member.filter.operation'/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${incList }" var="item" varStatus="status">
						<tr data="${item.id }" class=<c:if test="${status.index%2==0 }">"even"</c:if><c:if test="${status.index%2!=0 }">"odd"</c:if>>
							<td>${item.name }</td>
							<td><a class="func-button btn-remove" title="<spring:message code='btn.delete'/>" href="javascript:void(0);"><spring:message code='btn.delete'/></a></td>
						</tr>
					</c:forEach>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15 right">
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			</div>
				</div>
				<div id="group-block-exclude" style="float: right; width: 40%;">
				<div class="bold p5"><spring:message code='member.filter.excludeList'/></div>
		 	  <table class="pt10" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th>会员名称 / 分组名称</th>
							<th style="width: 20%;"><spring:message code='member.filter.operation'/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${excList }" var="item" varStatus="status">
						<tr data="${item.id }" class=<c:if test="${status.index%2==0 }">"even"</c:if><c:if test="${status.index%2!=0 }">"odd"</c:if>>
							<td>${item.name }</td>
							<td><a class="func-button btn-remove" title="<spring:message code='btn.delete'/>" href="javascript:void(0);"><spring:message code='btn.delete'/></a></td>
						</tr>
					</c:forEach>
					</tbody>
		 	  </table>
		 	   <div class="ui-block-line p5 pt15 right">
		 	   	<select id="slt-exclude-type" style="float: left" loxiaType="select">
		 	   		<option value="1">会员</option>
		 	   		<option value="2">分组</option>
		 	   	</select>
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			</div>
		 	  </div>
				</div>
				
			<%--自定义筛选器组合 --%>
			<div id="user-defined-block" class="toggle-block">
				<table class="pt10" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<th>自定义固定分组名称</th>
							<th style="width: 20%;"><spring:message code='member.filter.operation'/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${incList }" var="item" varStatus="status">
						<tr data="${item.id }" class=<c:if test="${status.index%2==0 }">"even"</c:if><c:if test="${status.index%2!=0 }">"odd"</c:if>>
							<td>${item.name }</td>
							<td><a class="func-button btn-remove" title="<spring:message code='btn.delete'/>" href="javascript:void(0);"><spring:message code='btn.delete'/></a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<div class="ui-block-line p5 pt15 right">
					<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
				</div>
			</div>

			<%-- 自定义组合 --%>
			<div id="custom-block" class="toggle-block">
				<table class="pt10" cellspacing="0" cellpadding="0">
					 <thead>
						<tr>
							<th><spring:message code='member.filter.comboName'/></th>
							<th style="width: 20%;"><spring:message code='member.filter.operation'/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${incList }" var="item" varStatus="status">
						<tr data="${item.id }" class=<c:if test="${status.index%2==0 }">"even"</c:if><c:if test="${status.index%2!=0 }">"odd"</c:if>>
							<td>${item.name }</td>
							<td><a class="func-button btn-remove" title="<spring:message code='btn.delete'/>" href="javascript:void(0);"><spring:message code='btn.delete'/></a></td>
						</tr>
					</c:forEach>
					</tbody>
		 		</table>
		 		<div class="ui-block-line p5 pt15 right">
					<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
				</div>
			</div>

		</div>
		<div class="p10 right clear">
			<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save"   title="<spring:message code='btn.save'/>" />
			<input type="button" value="<spring:message code='btn.return'/>" class="button black btn-return"   title="<spring:message code='btn.return'/>" />
		</div>
	</div>
</div>
<%-------------------------------------------------------- 会员选择浮层 --------------------------------------------------------%>
<div id="member-selector" class="proto-dialog">
	<%@include file="/pages/module/member-selector.jsp"%>
</div>
<%-------------------------------------------------------- 会员分组选择浮层 --------------------------------------------------------%>
<div id="group-selector" class="proto-dialog">
	<h5><spring:message code='member.filter.groupSelect'/></h5>
	<div class="proto-dialog-content p10">
		<div id="allStaff_id" class="ui-block">
			<span class="children-store"><label style="font-weight: bold; color: blue;"><input id="chk-all" type="checkbox" name="chk-group" value="0" /><spring:message code='member.filter.allStaff'/></label></span>
		</div>
		<div style="clear: both;"></div>
		<div class="ui-block">
			<c:forEach items="${groupList }" var="group">
				<span class="children-store" style="width: 110px;"><label><input type="checkbox" name="chk-group" value="${group.id }" />${group.name }</label></span>
			</c:forEach>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>
<%-------------------------------------------------------- 组合选择浮层 --------------------------------------------------------%>
<div id="custom-selector" class="proto-dialog">
	<h5><spring:message code='member.filter.filterSelect'/></h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block" style="margin-bottom: 20px;">
			<label><spring:message code='member.filter.type'/>：</label>
			<opt:select loxiaType="select" expression="chooseOption.MEMBER_FILTER_BASIC_TYPE" nullOption="role.list.label.unlimit" />
		 	 <label style="margin-left: 5px;"><spring:message code='member.filter.name'/>：</label>
		 	 <input class="txt-name" loxiaType="input" />
		</div>
		<div class="ui-block right">
			<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
		</div>
		<div class="ui-block combo-list">
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" />
	</div>
</div>
<%-------------------------------------------------------- 自定义选择浮层 --------------------------------------------------------%>
<div id="user-defined-selector" class="proto-dialog">
	<h5>自定义筛选器选择</h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block" style="margin-bottom: 20px;">
			<label style="margin-left: 5px;"><spring:message code='member.filter.name'/>：</label>
			<input class="txt-name" loxiaType="input" />
		</div>
		<div class="ui-block right">
			<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
		</div>
		<div class="ui-block combo-list">
			
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>
</body>
</html>
