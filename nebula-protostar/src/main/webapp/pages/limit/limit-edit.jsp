<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%------------------------------------- @author - 項邵瀧の父 -------------------------------------%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/limit/limit-edit.js"></script>
<script type="text/javascript">
 var  pdp_base_url = "${pdp_base_url}";
</script>
<style type="text/css">
.white-title {
	background: #CCCCFF; 
	color: #000;
}
.input-mode{
	display: none;
	background: #FFFFCC;
}
.fuzzy{
	display: none;
}
.span-setting-double label{
    display: inline;
    float: none;
    width: auto;
    padding-right: 5px;
}
#div-double-help{
	position: absolute;
	display: none;
	background: pink;
	width: 350px;
	height: 110px;
	padding: 15px;
	opacity: 0.9;
}
</style>
</head>
	
<body>
<input type="hidden" id="limit-id" value="<c:out value='${limit.id }' default='' />" />
<input type="hidden" id="audience-id" value="<c:out value='${limit.audienceId }' default='' />" />
<input type="hidden" id="scope-id" value="<c:out value='${limit.scopeId }' default='' />" />
<input type="hidden" id="condition-id" value="<c:out value='${limit.conditionId }' default='' />" />

<input type="hidden" id="is-view" value="<c:out value='${isView }' default='false' />" />

<div class="content-box width-percent100">

	<div class="ui-title1">
		<img src="../images/wmi/blacks/32x32/calc.png">限购-<c:choose><c:when test="${isView }"><spring:message code='promotion.view'/></c:when><c:otherwise><spring:message code='promotion.edit'/></c:otherwise></c:choose>
		<span>
			<input class="button black btn-return" type="button" title="<spring:message code='btn.return'/>" value="<spring:message code='btn.return'/>">
		</span>
	</div>
	<div class="ui-block">

		<%------------------------------------- 限购头部设置 开始 -------------------------------------%>
		<div id="step-1" class="ui-block">
			<div class="ui-block-title1 white-title">
				限购头部设置
				<a class="func-button btn-update" title="<spring:message code='btn.update'/>" href="javascript:void(0);"><spring:message code='btn.update'/></a>
			</div>

			<%------------------------------------- 限购头部文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line">
					<label>限购名称：</label>
					<div class="text pro-name">${limit.name }</div>
				</div>
				<div class="ui-block-line">
					<label><spring:message code='promotion.expire'/>：</label>
					<div class="text"> 
						<span class="pro-startTime"><fmt:formatDate value="${limit.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span> 
						——
						<span class="pro-endTime"><fmt:formatDate value="${limit.endTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span> 
					</div>
				</div>
			</div>
			<%------------------------------------- 限购头部文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 限购头部输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line ">
					<label>限购名称：</label>
					<div><input loxiaType="input" class="pro-name" mandatory="true" value="<c:out value="${limit.name }" default="" />" /></div>
				</div>
				<div class="ui-block-line mt5">
					<label><spring:message code='promotion.expire'/>：</label>
					<div> 
						<span><input loxiaType="date" class="pro-startTime" showtime="true" mandatory="true" readonly="readonly" 
								value='<fmt:formatDate value="${limit.startTime }" pattern="yyyy-MM-dd HH:mm:ss" />' /></span> 
						——
						<span><input loxiaType="date" class="pro-endTime" showtime="true" mandatory="true" readonly="readonly" 
								value='<fmt:formatDate value="${limit.endTime }" pattern="yyyy-MM-dd HH:mm:ss" />' /></span> 
						
					</div>
				</div>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 限购头部输入模式 结束 -------------------------------------%>
		</div>
		<div style="margin-top: 10px"></div>
		<%------------------------------------- 限购头部设置 结束 -------------------------------------%>

		<%------------------------------------- 受益人群设置 开始 -------------------------------------%>
		<div id="step-2" class="ui-block">
			<div class="ui-block-title1 white-title">
				受限人群设置
			</div>

			<%------------------------------------- 受益人群文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line ">
					<label>受限人群：</label>
					<div class="text pro-audience">
						<c:out value="${limit.memberComboName }" default="" />&nbsp;
						<c:choose>
							<c:when test="${limit.memberComboType eq 1 }">[<spring:message code='member.filter.type.member'/>]</c:when>
							<c:when test="${limit.memberComboType eq 2 }">[<spring:message code='member.filter.type.group'/>]</c:when>
							<c:when test="${limit.memberComboType eq 3 }">[自定义筛选器]</c:when>
							<c:when test="${limit.memberComboType eq 4 }">[<spring:message code='member.filter.type.combo'/>]</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			<%------------------------------------- 受益人群文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 受益人群输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line">
					<label><spring:message code='promotion.member.filter.type'/>：</label>
					<div>
						<opt:select cssClass="pro-member-type" defaultValue="${ limit.memberComboType }" loxiaType="select" nullOption="请选择" expression="chooseOption.MEMBER_FILTER_TYPE"></opt:select>
						<select loxiaType="select" class="pro-member-value" data="<c:out value='${limit.memberComboId }' default='' />">
							<option value=""><spring:message code='please.select'/></option>
						</select>
					</div>
				</div>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 受益人群输入模式 结束 -------------------------------------%>
		</div>
		<div style="margin-top: 10px"></div>
		<%------------------------------------- 受益人群设置 结束 -------------------------------------%>
		
		<%------------------------------------- 限购范围设置 开始 -------------------------------------%>
		<div id="step-3" class="ui-block">
			<div class="ui-block-title1 white-title">
				限购范围设置
			</div>
			<%------------------------------------- 限购范围文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line ">
					<label><spring:message code='promotion.product.filter.type'/>：</label>
					<div class="text pro-scope">
						<c:out value="${limit.productComboName }" default="" />&nbsp;
						<c:choose>
							<c:when test="${limit.productComboType eq 1 }">[<spring:message code='item.filter.item.list'/>]</c:when>
							<c:when test="${limit.productComboType eq 2 }">[<spring:message code='item.filter.category'/>]</c:when>
							<c:when test="${limit.productComboType eq 3 }">[自定义筛选器]</c:when>
							<c:when test="${limit.productComboType eq 4 }">[<spring:message code='member.filter.type.combo'/>]</c:when>
						</c:choose> 
						
					</div>
				</div>
			</div>
			<%------------------------------------- 限购范围文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 限购范围输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line">
					<label><spring:message code='promotion.product.filter.type'/>：</label>
					<div>
						<opt:select cssClass="pro-product-type" defaultValue="${ limit.productComboType }" loxiaType="select" nullOption="请选择" expression="chooseOption.ITEM_FILTER_TYPE"></opt:select>
						<select loxiaType="select" class="pro-product-value" data="<c:out value='${limit.productComboId }' default='' />">
							<option value=""><spring:message code='please.select'/></option>
						</select>
					</div>
				</div>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 限购范围输入模式 结束 -------------------------------------%>
			<div class="ui-block-content border-grey ui-loxia-simple-table" style="float: left;">
				<div style="float: left; width: 50%; margin-right: 20px;">
					<div class="bold p5">
						<spring:message code='member.filter.includeList' />
					</div>
					<table class="pt10 tbl-include" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 25%"><spring:message code='member.filter.name' /></th>
								<th style="width: 60%"><spring:message code='item.filter.item.name' /> / <spring:message code='item.filter.category.name' /></th>
								<th style="width: 15%"><spring:message code='item.filter.item.itemPrice' /></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="float: right; width: 45%;">
					<div class="bold p5">
						<spring:message code='member.filter.excludeList' />
					</div>
					<table class="pt10 tbl-exclude" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 25%"><spring:message code='member.filter.name' /></th>
								<th style="width: 60%"><spring:message code='item.filter.item.name' /> / <spring:message code='item.filter.category.name' /></th>
								<th style="width: 15%"><spring:message code='item.filter.item.itemPrice' /></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div style="margin-top: 10px; clear: both"></div>
		<%------------------------------------- 限购范围设置 结束 -------------------------------------%>

		<%------------------------------------- 限购条件设置 开始 -------------------------------------%>
		<div id="step-4" class="ui-block ui-loxia-simple-table">
			<div class="ui-block-title1 white-title">
				限购条件设置
			</div>
			<%------------------------------------- 限购条件文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line condition">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 100%">条件项</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${limit.conditionTextList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
								<td>${item }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<%------------------------------------- 限购条件文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 限购条件输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<%------------------------------------- 常规条件 -------------------------------------%>
				<div class="ui-block-line condition">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 85%">条件项</th>
								<th style="width: 15%"><spring:message code='member.filter.operation' /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${limit.conditionExpressionList }" var="exp" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'
									data="${exp }">
								<td>${limit.conditionTextList[status.index] }</td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title="<spring:message code='btn.delete' />"><spring:message code='btn.delete' /></a></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line condition">
					<select loxiaType="select" class="pro-condition-scope-type">
						<option value="0">订单内单款件数</option>
						<option value="1">订单内商品样数</option>
						<option value="2">订单内件数</option>
						<option value="3">历史购买件数</option>
						<option value="4">历史购买商品样数</option>
						<option value="5">历史购买订单数</option>
					</select>
					<select loxiaType="select" class="pro-condition-scope">
					</select>
					<span class="pro-condition-amount-span">
						小于等于
						<input loxiaType="number" class="pro-condition-amount" mandatory="true" />
						<span class="num_info">件数</span>
					</span>
					<a class="func-button btn-add" title="<spring:message code='btn.add2' />" href="javascript:void(0);"><spring:message code='btn.add2' /></a>
				</div>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 限购条件输入模式 结束 -------------------------------------%>
		</div>
		<div style="margin-top: 10px"></div>
		<%------------------------------------- 限购条件设置 结束 -------------------------------------%>
	</div>
	<div class="ui-block-line right">
		<input class="button black btn-return" type="button" title="<spring:message code='btn.return'/>" value="<spring:message code='btn.return'/>">
	</div>
</div>

</body>
</html>
