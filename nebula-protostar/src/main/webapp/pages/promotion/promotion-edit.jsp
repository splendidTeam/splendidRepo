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
<script type="text/javascript" src="${base}/scripts/promotion/promotion-constant.js"></script>
<script type="text/javascript" src="${base}/scripts/promotion/promotion-edit.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
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
.fuzzy label{
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
#div-user-select-help{
	position: absolute;
	display: none;
	background: pink;
	width: 250px;
	height: 50px;
	padding: 15px;
	opacity: 0.9;
}
</style>
</head>
	
<body>
<input type="hidden" id="pro-id" value="<c:out value='${promotion.promotionId }' default='' />" />
<input type="hidden" id="audience-id" value="<c:out value='${promotion.audiences.id }' default='' />" />
<input type="hidden" id="scope-id" value="<c:out value='${promotion.scope.id }' default='' />" />
<input type="hidden" id="condition-id" value="<c:out value='${promotion.conditionNormal.id }' default='' />" />
<input type="hidden" id="setting-id" value="<c:out value='${promotion.settingNormal.id }' default='' />" />


<input type="hidden" id="step-scope" value="<c:out value='${promotion.settingNormal.stepScope }' default='' />" />
<input type="hidden" id="isEffective" value="<c:out value='${isEffective }' default='false' />" />

<input type="hidden" id="isView" value="<c:out value='${isView }' default='false' />" />

<input id="loxia-number-template" loxiaType="input" class="pro-setting-amount validateNumber" mandatory="true" style="display: none;" />
<div class="content-box width-percent100">

	<div class="ui-title1">
		<img src="../images/wmi/blacks/32x32/calc.png"><spring:message code='promotion.title'/>-<c:choose><c:when test="${isView }"><spring:message code='promotion.view'/></c:when><c:otherwise><spring:message code='promotion.edit'/></c:otherwise></c:choose>
		<span>
			<input class="button black btn-return" type="button" title="<spring:message code='btn.return'/>" value="<spring:message code='btn.return'/>">
		</span>
	</div>
	<div class="ui-block">

		<%------------------------------------- 活动头部设置 开始 -------------------------------------%>
		<div id="step-1" class="ui-block">
			<div class="ui-block-title1 white-title">
				<spring:message code='promotion.head.set'/> 
				<c:if test="${isEffective }"><span style="color: red">(<spring:message code='promotion.head.effective'/>)</span></c:if>
				<a class="func-button btn-update" title="<spring:message code='btn.update'/>" href="javascript:void(0);"><spring:message code='btn.update'/></a>
			</div>

			<%------------------------------------- 活动头部文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line">
					<label><spring:message code='promotion.name'/>：</label>
					<div class="text pro-name">${promotion.promotionName }</div>
				</div>
				<div class="ui-block-line">
					<label><spring:message code='promotion.expire'/>：</label>
					<div class="text"> 
						<span class="pro-startTime"><fmt:formatDate value="${promotion.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span> 
						——
						<span class="pro-endTime"><fmt:formatDate value="${promotion.endTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span> 
					</div>
				</div>
				<div class="ui-block-line">
					<label><spring:message code='promotion.logo'/>：</label> 
					<div id="pro-mark-logo" class="text pro-mark"><c:out value="${promotion.logo }" default="无" /></div>
				</div>
			</div>
			<%------------------------------------- 活动头部文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 活动头部输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line ">
					<label><spring:message code='promotion.name'/>：</label>
					<div><input loxiaType="input" class="pro-name" mandatory="true" value="<c:out value="${promotion.promotionName }" default="" />" <c:if test="${isEffective }">disabled="disabled"</c:if> /></div>
				</div>
				<div class="ui-block-line mt5">
					<label><spring:message code='promotion.expire'/>：</label>
					<div> 
						<span><input loxiaType="date" class="pro-startTime" showtime="true" mandatory="true" readonly="readonly" 
								value='<fmt:formatDate value="${promotion.startTime }" pattern="yyyy-MM-dd HH:mm:ss" />' /></span> 
						——
						<span><input loxiaType="date" class="pro-endTime" showtime="true" mandatory="true" readonly="readonly" 
								value='<fmt:formatDate value="${promotion.endTime }" pattern="yyyy-MM-dd HH:mm:ss" />' /></span> 
						
					</div>
				</div>
				<div class="ui-block-line mt5">
					<label><spring:message code='promotion.logo'/>：</label> 
					<div>
						<opt:select cssClass="pro-mark" loxiaType="select" otherProperties='${isEffective?"disabled=disabled":"" }'  expression="chooseOption.PROMOTION_LOGO_MARK" defaultValue="${promotion.logo}" />
					</div>
				</div>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 活动头部输入模式 结束 -------------------------------------%>
		</div>
		<div style="margin-top: 10px"></div>
		<%------------------------------------- 活动头部设置 结束 -------------------------------------%>

		<%------------------------------------- 受益人群设置 开始 -------------------------------------%>
		<div id="step-2" class="ui-block">
			<div class="ui-block-title1 white-title">
				<spring:message code='promotion.audience.set'/>
			</div>

			<%------------------------------------- 受益人群文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line ">
					<label><spring:message code='promotion.audience'/>：</label>
					<div class="text pro-audience">
						<c:out value="${promotion.audiences.comboName }" default="" />&nbsp;
						<c:choose>
							<c:when test="${promotion.audiences.comboType eq 1 }">[<spring:message code='member.filter.type.member'/>]</c:when>
							<c:when test="${promotion.audiences.comboType eq 2 }">[<spring:message code='member.filter.type.group'/>]</c:when>
							<c:when test="${promotion.audiences.comboType eq 3 }">[自定义筛选器]</c:when>
							<c:when test="${promotion.audiences.comboType eq 4 }">[<spring:message code='member.filter.type.combo'/>]</c:when>
						</c:choose>
						<%--
						<opt:select loxiaType="select"  expression="chooseOption.MEMBER_FILTER_TYPE" defaultValue="${promotion.audiences.comboType}" />
						--%>
					</div>
				</div>
			</div>
			<%------------------------------------- 受益人群文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 受益人群输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line">
					<label><spring:message code='promotion.member.filter.type'/>：</label>
					<div>
						<opt:select cssClass="pro-member-type" loxiaType="select"  expression="chooseOption.MEMBER_FILTER_TYPE" defaultValue="${promotion.audiences.comboType}" nullOption="请选择" />
						<select loxiaType="select" class="pro-member-value" data="<c:out value='${promotion.audiences.comboId }' default='' />">
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
		
		<%------------------------------------- 活动范围设置 开始 -------------------------------------%>
		<div id="step-3" class="ui-block">
			<div class="ui-block-title1 white-title">
				<spring:message code='promotion.scope.set'/> 
			</div>

			<%------------------------------------- 活动范围文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line ">
					<label><spring:message code='promotion.product.filter.type'/>：</label>
					<div class="text pro-scope">
						<c:out value="${promotion.scope.comboName }" default="" />&nbsp;
						<c:choose>
							<c:when test="${promotion.scope.comboType eq 1 }">[<spring:message code='item.filter.item.list'/>]</c:when>
							<c:when test="${promotion.scope.comboType eq 2 }">[<spring:message code='item.filter.category'/>]</c:when>
							<c:when test="${promotion.scope.comboType eq 3 }">[自定义筛选器]</c:when>
							<c:when test="${promotion.scope.comboType eq 4 }">[<spring:message code='member.filter.type.combo'/>]</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			<%------------------------------------- 活动范围文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 活动范围输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line">
					<label><spring:message code='promotion.product.filter.type'/>：</label>
					<div>
						<opt:select cssClass="pro-product-type" loxiaType="select"  expression="chooseOption.ITEM_FILTER_TYPE" defaultValue="${promotion.scope.comboType}" nullOption="请选择" />
						<select loxiaType="select" class="pro-product-value" data="<c:out value='${promotion.scope.comboId }' default='' />">
							<option value=""><spring:message code='please.select'/></option>
						</select>
					</div>
				</div>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 活动范围输入模式 结束 -------------------------------------%>
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
		<%------------------------------------- 活动范围设置 结束 -------------------------------------%>

		<%------------------------------------- 活动条件设置 开始 -------------------------------------%>
		<div id="step-4" class="ui-block ui-loxia-simple-table">
			<div class="ui-block-title1 white-title">
				<spring:message code='promotion.condition.set' />
			</div>
			<%------------------------------------- 活动条件文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line ">
					<label><spring:message code='promotion.condition.type' />：</label>
					<div class="text pro-condition-type">
					<c:choose>
						<c:when test="${promotion.conditionNormal.conditionType eq 'Normal' }"><spring:message code='promotion.condition.type.normal' /></c:when>
						<c:when test="${promotion.conditionNormal.conditionType eq 'Step' }"><spring:message code='promotion.condition.type.step' /></c:when>
						<c:when test="${promotion.conditionNormal.conditionType eq 'Choice' }"><spring:message code='promotion.condition.type.choice' /></c:when>
						<c:when test="${promotion.conditionNormal.conditionType eq 'NormalStep' }"><spring:message code='promotion.condition.type.normalStep' /></c:when>
					</c:choose>
					</div>
				</div>
				<div class="ui-block-line condition-normal condition-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 15%"><spring:message code='promotion.condition.logic' /></th>
								<th style="width: 85%"><spring:message code='promotion.condition.normal.item' /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${promotion.conditionNormal.expressionList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
								<td>${item.mark }</td>
								<td>${item.text }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line condition-step condition-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 100%"><spring:message code='promotion.condition.step.item' /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Step' || promotion.conditionNormal.conditionType eq 'NormalStep' }">
							<c:forEach items="${promotion.conditionComplexList }" var="item" varStatus="status">
								<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
									<td>${item.conditionName }</td>
								</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line condition-choice">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 100%"><spring:message code='promotion.condition.choice.item' /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Choice' }">
							<c:forEach items="${promotion.conditionComplexList }" var="item" varStatus="status">
								<tr class='${ status.index%2==0 ? "even" : "odd" }'>
									<td>${item.conditionName }</td>
								</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
			</div>
			<%------------------------------------- 活动条件文本模式 结束 -------------------------------------%>
			
			<%------------------------------------- 活动条件输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<div class="ui-block-line">
					<label><spring:message code='promotion.condition.type' />：</label>
					<div>
						<opt:select cssClass="pro-condition-type" loxiaType="select" otherProperties='${isConditionFrozen?"disabled=disabled":"" }' expression="chooseOption.PROMOTION_CONDITION_TYPE" defaultValue="${promotion.conditionNormal.conditionType}"></opt:select>
					</div>
				</div>
				<%------------------------------------- 常规条件 -------------------------------------%>
				<div class="ui-block-line condition-normal condition-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 10%"><spring:message code='promotion.condition.logic' /></th>
								<th style="width: 60%"><spring:message code='promotion.condition.normal.item' /></th>
								<th style="width: 15%"><spring:message code='member.filter.operation' /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${promotion.conditionNormal.expressionList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'
									data="${item.expression }" data_scope_type="${item.scopeType }" data_scope="${item.scope }">
								<td>${item.mark }</td>
								<td>${item.text }</td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title="<spring:message code='btn.delete' />"><spring:message code='btn.delete' /></a></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line condition-normal condition-normalstep">
					<select loxiaType="select" class="pro-condition-mark" style="width: 60px;">
						<option value="1"><spring:message code='promotion.condition.logic.and' /></option>
						<option value="2"><spring:message code='promotion.condition.logic.or' /></option>
					</select>
					<select loxiaType="select" class="pro-condition-scope-type">
						<option value="0" data_unit="">不限</option>
						<option value="1" data_unit="元">整单金额</option>
						<option value="2" data_unit="件">整单件数</option>
						<option value="7" data_unit="">整单优惠券</option>
						<option value="9" data_unit="">整单最大优惠幅度</option>
						<option value="3" data_unit="元">商品范围整单金额</option>
						<option value="4" data_unit="件">商品范围整单件数</option>
						<option value="5" data_unit="元">商品范围单品金额</option>
						<option value="6" data_unit="件">商品范围单品件数</option>
						<option value="8" data_unit="">商品范围优惠券</option>
						<option value="10" data_unit="">商品范围最大优惠幅度</option>
						<option value="11" data_unit="">自定义条件</option>
					</select>
					<select loxiaType="select" class="pro-condition-scope fuzzy pro-normal">
						
					</select>
					<select loxiaType="select" class="pro-condition-scope fuzzy pro-custom">
						
					</select>
					<span class="pro-condition-amount-span fuzzy">
						<spring:message code='promotion.ge' />
						<input loxiaType="input" class="pro-condition-amount validateNumber" mandatory="true" />
						<span></span>
					</span>
					<span class="pro-condition-radius-max-span fuzzy">
						小于等于
						<input loxiaType="input" class="pro-condition-radius-max validateNumber" mandatory="true" />
						<span></span>
					</span>
					<span class="span-double fuzzy"><label><input type="checkbox" />倍增</label><a href="javascript:void(0)" id="double-help"><img src="../images/main/qs.png" style="width: 20px;" ></a></span>
					<select loxiaType="select" class="pro-condition-coupon fuzzy">
						<c:forEach items="${couponList }" var="coupon">
							<option value="${coupon.id }" data_type="${coupon.type }">${coupon.couponName }</option>
						</c:forEach>
					</select>
					
					<span class="span-single-piece fuzzy"><label><input type="checkbox" />单件计</label></span>
					<a class="func-button btn-add" title="<spring:message code='btn.add2' />" href="javascript:void(0);"><spring:message code='btn.add2' /></a>
					<span class="norInfo fuzzy" style="display: none;" >9折请输入90,8折请输入80,以此类推</span>
				</div>
				<%------------------------------------- 阶梯条件 -------------------------------------%>
				<div class="ui-block-line condition-step condition-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 85%"><spring:message code='promotion.condition.step.item' /></th>
								<th style="width: 15%"><spring:message code='member.filter.operation' /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Step' || promotion.conditionNormal.conditionType eq 'NormalStep'}">
						<c:forEach items="${promotion.conditionComplexList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'
									data="${item.conditionExpress }" data_number="${item.number }">
								<td>${item.conditionName }</td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title="<spring:message code='btn.delete' />"><spring:message code='btn.delete' /></a></td>
							</tr>
						</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line condition-step condition-normalstep">
					<select loxiaType="select" class="pro-condition-scope-type">
						<option value="1" data_unit="元">整单金额</option>
						<option value="2" data_unit="件">整单件数</option>
						<!--<option value="9" data_unit="折">整单最大优惠幅度</option>-->
						<option value="3" data_unit="元">商品范围整单金额</option>
						<option value="4" data_unit="件">商品范围整单件数</option>
						<option value="5" data_unit="元">商品范围单品金额</option>
						<option value="6" data_unit="件">商品范围单品件数</option>
						<!--<option value="10" data_unit="折">商品范围最大优惠幅度</option>-->
					</select>
					<select loxiaType="select" class="pro-condition-scope fuzzy">
						
					</select>
					<span class="compare-string"><spring:message code='promotion.ge' /></span>
					<input loxiaType="input" class="pro-condition-amount validateNumber" mandatory="true" />
					<span class="data-unit"></span>
					<a class="func-button btn-add" title="<spring:message code='btn.add2' />" href="javascript:void(0);"><spring:message code='btn.add2' /></a>
					<span class="norInfo fuzzy" style="display: none;" >9折请输入90,8折请输入80,以此类推</span>
				</div>
				<%------------------------------------- 选购条件 -------------------------------------%>
				<div class="ui-block-line condition-choice">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 85%"><spring:message code='promotion.condition.choice.item' /></th>
								<th style="width: 15%"><spring:message code='member.filter.operation' /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Choice' }">
						<c:forEach items="${promotion.conditionComplexList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'
									data="${item.conditionExpress }" data_type="${item.choiceMark }" data_scope="${item.scope }">
								<td>${item.conditionName }</td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title="<spring:message code='btn.delete' />"><spring:message code='btn.delete' /></a></td>
							</tr>
						</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line condition-choice">
					<select loxiaType="select" class="pro-condition-choice-type">
						<option value="prmprd">主商品</option>
						<option value="addtprd">选购商品</option>
					</select>
					<select loxiaType="select" class="pro-condition-scope">
					</select>
					<spring:message code='promotion.ge' />
					<input loxiaType="input" class="pro-condition-amount validateNumber" mandatory="true" />
					<spring:message code='promotion.piece' />
					<a class="func-button btn-add" title="<spring:message code='btn.add2' />" href="javascript:void(0);"><spring:message code='btn.add2' /></a>
				</div>
				<%------------------------------------- 常规+阶梯条件 -------------------------------------%>
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" onclick="" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消"  class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 活动条件输入模式 结束 -------------------------------------%>
		</div>
		<div style="margin-top: 10px"></div>
		<%------------------------------------- 活动条件设置 结束 -------------------------------------%>
		
		<%------------------------------------- 活动优惠设置 开始 -------------------------------------%>
		<div id="step-5" class="ui-block ui-loxia-simple-table">
			<div class="ui-block-title1 white-title">
				<spring:message code='promotion.setting.set' />
			</div>
			<%------------------------------------- 促销优惠文本模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey text-mode">
				<div class="ui-block-line setting-normal setting-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 100%"><spring:message code='promotion.setting.normal.item' /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${promotion.settingNormal.expressionList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
								<td>${item.text }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line setting-step setting-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 100%"><spring:message code='promotion.setting.step.item' /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Step' || promotion.conditionNormal.conditionType eq 'NormalStep' }">
							<c:forEach items="${promotion.settingComplexList }" var="item" varStatus="status">
								<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
									<td>${item.settingName }</td>
								</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line setting-choice">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 100%">选购优惠项</th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Choice' }">
							<c:forEach items="${promotion.settingComplexList }" var="item" varStatus="status">
								<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
									<td>${item.settingName }</td>
								</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
			</div>
			<%------------------------------------- 促销优惠文本模式 结束 -------------------------------------%>
			<%------------------------------------- 促销优惠输入模式 开始 -------------------------------------%>
			<div class="ui-block-content border-grey input-mode">
				<%------------------------------------- 常规设置 -------------------------------------%>
				<div class="ui-block-line setting-normal setting-normalstep">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 85%"><spring:message code='promotion.setting.normal.item' /></th>
								<th style="width: 15%"><spring:message code='member.filter.operation' /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${promotion.settingNormal.expressionList }" var="item" varStatus="status">
							<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'
									data="${item.expression }" <c:if test="${! fn:contains(item.expression, 'coupon') }">data_scope_type="${item.scopeType }" data_scope="${item.scope }"</c:if>>
								<td>${item.text }</td>
								<td>
								<%-- 优惠券相关设置项不能删除 --%>
								<c:if test="${! fn:contains(item.expression, 'coupon') }">
									<a href='javascript:void(0);' class='func-button btn-remove' title="<spring:message code='btn.delete' />"><spring:message code='btn.delete' /></a>
								</c:if>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="ui-block-line setting-normal setting-normalstep">
					<select loxiaType="select" class="pro-setting-scope-type dicountInfo">
						<option value="0" data_unit="rmb">整单免运费</option>
						<option value="1" data_unit="rmb">整单优惠</option>
						<option value="2" data_unit="discount">整单折扣</option>
						<option value="3" data_unit="rmb">商品范围整单优惠</option>
						<option value="4" data_unit="discount">商品范围整单折扣</option>
						<option value="5" data_unit="rmb">商品范围单品优惠</option>
						<option value="6" data_unit="discount">商品范围单品折扣</option>
						<option value="7" data_unit="rmb">商品范围单件优惠</option>
						<option value="8" data_unit="discount">商品范围单件折扣</option>
						<option value="12" data_unit="rmb">商品范围优惠价</option>
						<option value="9">赠品</option>
						<option value="13" data_unit="">自定义优惠</option>
					</select>
					<select loxiaType="select" class="pro-condition-scope fuzzy">
					</select>
					<select loxiaType="select" class="setting-custom fuzzy">						
					</select>
					<input loxiaType="input" class="pro-setting-amount fuzzy validateNumber" mandatory="true" />
					<span class="span-setting-unit-discount fuzzy"><spring:message code='promotion.discount' /></span>
					<span class="span-setting-unit-rmb fuzzy"><spring:message code='promotion.rmb' /></span>
					<span class="span-setting-single-piece fuzzy"><label><input type="checkbox" />单件计</label></span>
					<span class="span-setting-user-select fuzzy"><label><input type="checkbox"  class="user-select user-select-nor"/>用户选择</label></span>
					<span class="span-setting-user-select-info fuzzy">
					</span>
					<span class="span-setting-item-count fuzzy"><label>
						<div class="not-selected-nor" style="display:inline">最多礼品样数</div>
						<div class="user-selected-nor"  style="display:inline">推送礼品样数</div>
					<input type="text" class="gift_count gift_count_nor" loxiaType="number" mandatory="true" /></label></span>
					<a class="func-button btn-add" title="<spring:message code='btn.add2' />" href="javascript:void(0);"><spring:message code='btn.add2' /></a>
					<span  class="norInfo" style="display: none;" >9折请输入90,8折请输入80,以此类推</span>
				</div>
				<div class="ui-block-line"></div>
				<div class="ui-block-line"></div>
				
				<%------------------------------------- 阶梯设置 -------------------------------------%>
				
				  <div class="ui-block-content" style="padding-top:0">
                    <div id="ui-tag-change-id" class="ui-tag-change">
                        <ul class="tag-change-ul">
                            <li class="memberbase amount">金额/折扣</li>
                            <li class="memberbase gift">礼品</li>
                        </ul>
                        <div class="tag-change-content">
                        	<!--金额/折扣  -->
                            <div class="tag-change-in">
                              <div class="ui-block-line setting-step setting-normalstep">
								<select loxiaType="select" class="pro-setting-scope-type dicountInfo">
									<option value="3" data_unit="rmb" <c:if test="${promotion.settingNormal.stepScopeType eq 'scporddisc' }">selected="selected"</c:if>>商品范围整单优惠</option>
									<option value="4" data_unit="discount" <c:if test="${promotion.settingNormal.stepScopeType eq 'scpordrate' }">selected="selected"</c:if>>商品范围整单折扣</option>
									<option value="5" data_unit="rmb" <c:if test="${promotion.settingNormal.stepScopeType eq 'scpprddisc' }">selected="selected"</c:if>>商品范围单品优惠</option>
									<option value="6" data_unit="discount" <c:if test="${promotion.settingNormal.stepScopeType eq 'scpprdrate' }">selected="selected"</c:if>>商品范围单品折扣</option>
									<option value="7" data_unit="rmb" <c:if test="${promotion.settingNormal.stepScopeType eq 'scppcsdisc' }">selected="selected"</c:if>>商品范围单件优惠</option>
									<option value="8" data_unit="discount" <c:if test="${promotion.settingNormal.stepScopeType eq 'scppcsrate' }">selected="selected"</c:if>>商品范围单件折扣</option>
								</select>
								<select loxiaType="select" class="pro-condition-scope">
								</select>
								<span  class="norInfo" style="display: none;" >9折请输入90,8折请输入80,以此类推</span>
							</div>
							<div class="ui-block-line setting-step setting-normalstep">
								<table class="pt10" cellspacing="0" cellpadding="0">
									<thead>
										<tr>
											<th style="width: 86%"><spring:message code='promotion.setting.step.item' /></th>
											<th style="width: 14%"><spring:message code='promotion.setting.number' /></th>
										</tr>
									</thead>
									<tbody>
									<c:if test="${promotion.conditionNormal.conditionType eq 'Step' || promotion.conditionNormal.conditionType eq 'NormalStep' }">
										<c:forEach items="${promotion.conditionComplexList }" var="item" varStatus="status">
											<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'
													data_number="${item.number }">
												<td>${item.conditionName }</td>
												<td><input loxiaType="input" class="pro-setting-amount validateNumber" mandatory="true" value="${promotion.settingComplexList[status.index].stepNumber }" /></td>
											</tr>
											</c:forEach>
									</c:if>
									</tbody>
								</table>
							</div>
                            </div>
                            <!-- 礼品 -->
                            <div id="tag-change-in-gift" class="tag-change-in">
								<div class="ui-block-line setting-step-gift setting-normalstep">
									<table id="" class="pt10 gitf_tab" cellspacing="0" cellpadding="0">
										<thead>
											<tr>
												<th style="width: 40%"><spring:message code='promotion.setting.step.item' /></th>
												<th style="width: 20%">礼品</th>
												<th style="width: 10%">用户选择</th>
												<th style="width: 30%">礼品样数</th>
											</tr>
										</thead>
										<tbody>
										<c:if test="${promotion.conditionNormal.conditionType eq 'Step' || promotion.conditionNormal.conditionType eq 'NormalStep' }">
											<c:forEach items="${promotion.conditionComplexList }" var="item" varStatus="status">
												<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>' 
													data_number="${item.number}">
													<td>${item.conditionName}</td>
													<td>
														<select loxiaType="select" class="pro-condition-scope gift_item"  itemFlag="item${status.index}" data="<c:out value='${promotion.settingComplexList[status.index].settingExpression}' default='' />">
														</select>
													</td>
													<td>
														<input type="checkbox" class="user-select user-select-step"  userselect="userselect${status.index}" />
													</td>
													<td>
														<div class="user-selected-stepuserselect${status.index}">推送礼品样数</div>
														<div class="not-selected-stepuserselect${status.index}">最多礼品样数</div>
														<input type="text" class="gift_count item${status.index}"  loxiaType="number" mandatory="true" />
													</td>
												</tr>
											</c:forEach>
										</c:if>
										</tbody>
									</table>
								</div>
                            </div>
                        </div>
                    </div>
           		 </div>
				<%------------------------------------- 选购设置 -------------------------------------%>
				<div class="ui-block-line setting-choice">
					<table class="pt10" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th style="width: 73%">选购优惠项</th>
								<th style="width: 27%"><spring:message code='promotion.setting.number' /></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${promotion.conditionNormal.conditionType eq 'Choice' }">
							<c:set var="index" value="0" scope="page" />
							<c:forEach items="${promotion.conditionComplexList }" var="item">
							<%-- <c:if test="${item.choiceMark eq 'addtprd' }"> --%>
								<tr class='<c:if test="${index%2==0 }">even</c:if><c:if test="${index%2!=0 }">odd</c:if>'
										data="${item.conditionExpress }" data_type="${item.choiceMark }" data_scope="${item.scope }">
									<td>${item.conditionName }</td>
									<td>
										<select class="pro-setting-scope-type ui-loxia-default ui-corner-all" aria-disabled="false">
											<%-- <option data_unit="rmb" value="3" <c:if test="${promotion.settingComplexList[index].choiceScope eq 'scporddisc' }">selected="selected"</c:if>>商品范围整单优惠</option>
											<option data_unit="discount" value="4" <c:if test="${promotion.settingComplexList[index].choiceScope eq 'scpordrate' }">selected="selected"</c:if>>商品范围整单折扣</option>
											<option data_unit="rmb" value="5" <c:if test="${promotion.settingComplexList[index].choiceScope eq 'scpprddisc' }">selected="selected"</c:if>>商品范围单品优惠</option>
											<option data_unit="discount" value="6" <c:if test="${promotion.settingComplexList[index].choiceScope eq 'scpprdrate' }">selected="selected"</c:if>>商品范围单品折扣</option> --%>
											<option data_unit="rmb" value="7" <c:if test="${promotion.settingComplexList[index].choiceScope eq 'scppcsdisc' }">selected="selected"</c:if>>商品范围单件优惠</option>
											<%-- <option data_unit="discount" value="8" <c:if test="${promotion.settingComplexList[index].choiceScope eq 'scppcsrate' }">selected="selected"</c:if>>商品范围单件折扣</option> --%>
										</select>
										<input loxiaType="input" class="pro-setting-amount validateNumber" mandatory="true" value="${promotion.settingComplexList[index].stepNumber }" />
									</td>
								</tr>
								<c:set var="index" value="${index + 1 }" scope="page" />
							<%-- </c:if>	 --%>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
				
				<div class="ui-block-line right">
					<input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save" title="<spring:message code='btn.save'/>" />
					<input type="button" value="取消" class="button cancel" onclick="" title="取消" />
				</div>
			</div>
			<%------------------------------------- 促销优惠输入模式 结束 -------------------------------------%>
		</div>
		<div style="margin-top: 10px"></div>
		<%------------------------------------- 活动优惠设置 结束 -------------------------------------%>
	</div>
	<div class="ui-block-line right">
		<input class="button black btn-return" type="button" title="<spring:message code='btn.return'/>" value="<spring:message code='btn.return'/>">
	</div>
</div>

<%------------------------------------- 倍增帮助层 -------------------------------------%>
<div id="div-double-help" style="z-index: 1000">
“倍增”表示优惠是否能够自动累加，例如：<br />
“整单金额大于等于100元，整单免运费5元【倍增】”表示<br />
1. 当整单金额为100元时，免运费5元。<br />
2. 当整单金额为200元时，免运费10元。<br />
3. 当整单金额为300元时，免运费15元。<br />
</div>
<!-- 礼品选择备注 -->
<a href="javascript:void(0)" id="user-select-help-js" class="user-select-help"  style="display:none;"><img src="../images/main/qs.png" style="width: 20px;" ></a>
<div id="div-user-select-help" style="z-index: 1000">
<label>
用户选择打上√:用户可以选择的最多礼品样数<br/>
用户选择未打上√:直接推送礼品样数<br/>
</label>
</div>
</body>
</html>
