<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>

<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>

<script type="text/javascript"
	src="${base}/scripts/product/item/item-tag.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>

<script type="text/javascript">

var  industry_ZNodes = [
                        <c:forEach var="industry" items="${industrylist}" varStatus="status">
                        	{id:${industry.id}, pId:${null eq industry.parentId?0:industry.parentId},lable:"${industry.id}", name: "${industry.name}",lifecycle:"${industry.lifecycle}",open:${null eq category.parentId?true:false}}
                        	<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                   ];
</script>


</head>
<body>

	<div class="content-box">
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/tag.png"><spring:message code="item.tag.manage"/> <input
				type="button" value="<spring:message code="item.tag.unbind"/>" class="button unbind" title="<spring:message code="item.tag.unbind"/>" /> <input
				type="button" value="<spring:message code="item.tag.bind"/>" class="button orange bind" title="<spring:message code="item.tag.bind"/>" />
		</div>

		<div class="ui-block">
			<form action="" id="searchForm">
			<div class="ui-block-content ui-block-content-lb">
				<table>
					<tr>
                        <td><label><spring:message code="item.code"/>　</label></td>
                        <td><span id="searchkeytext"> <input type="text"
								loxiaType="input" mandatory="false" id="code"
								name="q_sl_code" placeholder="<spring:message code='item.code'/>"></input>
						</span></td>
                       <td><label><spring:message code="item.name"/>　</label></td>
                        <td><span id="searchkeytext"><input type="text"
								loxiaType="input" mandatory="false" id="title"
								name="q_sl_title" placeholder="<spring:message code='item.name'/>">
								</span></input>
						</td>
						 
						<td><label><spring:message code="role.list.label.status"/></label></td>
						<td> <span id="searchkeytext"> <opt:select name="q_int_lifecycle" loxiaType="select" id="lifecycle" expression="chooseOption.ITEM_STATUS" nullOption="itemcategory.label.unlimit"  />
						</span> </td>
						
						
                        <td><label><spring:message
									code="product.property.lable.industry" /></label></td>
									
						<td><input type="hidden"
								id="industryId" name="q_long_industryId" mandatory="true" /> <input 
								type="text" loxiaType="input" id="industryName"
								mandatory="false" placeholder="<spring:message code='itemcategory.industry'/>"/>
						</td>
                    </tr>

					<tr>
                       <td><label><spring:message code="itemcategory.list.filter.createtime"/></label></td>
			            <td><input type="text" id="createStartDate" name="q_date_createStartDate" loxiaType="date" mandatory="false"></input>
			            </td>
			            
			            <td><label>——</label></td>
			            <td>
			            	<input type="text" name="q_date_createEndDate" id="createEndDate" loxiaType="date" mandatory="false"></input>
			            </td>
			            
			            <td><label><spring:message code="item.tag.modifytime"/></label></td>
			            <td><input type="text" id="modifyTimeStart" name="q_date_modifyTimeStart" loxiaType="date" mandatory="false"></input>
			            </td>
			            
			            <td><label>——</label></td>
			            <td>
			            	<input type="text" name="q_date_modifyTimeEnd" id="modifyTimeEnd" loxiaType="date" mandatory="false"></input>
			            </td>
                        
                    </tr>
				</table>
				<div class="button-line1">
					<input type="hidden" id="tagId" name="q_long_tagId" value="" />
					<a href="javascript:void(0);" class="func-button search" title="<spring:message code="user.list.filter.btn"/>"><spring:message code="user.list.filter.btn"/></a>
				</div>
			</div>
			</form>
		</div>

		<div class="ui-block ui-block-fleft w240">

			<div class="ui-block-content ui-block-content-lb w240">
				<form action="" id="searchTagTypeForm">
					<table>
						<tr>
							<td><input type="text" id="tagName" name="q_sl_tagName"
								loxiaType="input" mandatory="false" style="width: 140px;margin-right: 10px;"
								placeHolder="<spring:message code="item.tag.inputkey"/>"></input></td>
							<td>
								<opt:select name="q_int_tagType" loxiaType="select" id="tagType" expression="chooseOption.TAG_TYPE" nullOption="itemcategory.label.unlimit"  />
							</td>
						</tr>
					</table>
					<div class="button-line1">
						<a href="javascript:void(0);" class="func-button searchTagType"
							title="<spring:message code="user.list.filter.btn"/>"><span><spring:message code="user.list.filter.btn"/></span></a> 
						<a href="javascript:void(0);" class="func-button addTag" 
							title="<spring:message code="btn.add"/>"><span><spring:message code="btn.add"/></span></a>
					</div>

				</form>

				<div class="clear-line height10"></div>
			</div>

			<div class="ui-block">
				<div id="tagListTable" class="border-grey" caption="<spring:message code="item.tag.name"/>"></div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button deleteMultyTag"><span><spring:message code="btn.all.delete"/></span></a>
				</div>
			</div>
		</div>

		<div class="ui-block ml240">
			<div class="ui-block-content" style="padding-top: 0">
				<div class="ui-tag-change">
					<ul class="tag-change-ul">
						<li class="memberbase"><spring:message code="item.tag.alltag"/></li>
						<li class="memberbase"><spring:message code="item.tag.usetag"/></li>
						<li class="memberbase"><spring:message code="item.tag.nousetag"/></li>
					</ul>
					<div class="tag-change-content">
						<div class="tag-change-in">
							<div class="border-grey ui-loxia-simple-table" id="allTagsTable"
								caption="<spring:message code="item.tag.alltag"/>"></div>
						</div>
						<div class="tag-change-in">
							<div class="border-grey ui-loxia-simple-table" id="useTagsTable"
								caption="<spring:message code="item.tag.usetag"/>"></div>
						</div>
						<div class="tag-change-in">
							<div class="border-grey ui-loxia-simple-table"
								id="unUseTagsTable" caption="<spring:message code="item.tag.nousetag"/>"></div>
						</div>
					</div>
				</div>
				<div class="button-line">
					<input type="button" value="<spring:message code="item.tag.bind"/>" class="button orange bind"
						title="<spring:message code="item.tag.bind"/>" /> <input type="button" value="<spring:message code="item.tag.unbind"/>"
						class="button unbind" title="<spring:message code="item.tag.unbind"/>" />
				</div>

			</div>
		</div>
		
		<div id="categoryContent" class="menuContent"
			style="z-index: 999; display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
			<ul id="categoryTree" class="ztree"
				style="margin-top: 0; width: 180px; height: 100%;"></ul>
		</div>
		
		<div id="industryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
			<ul id="industryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
		</div>

	</div>
</body>
</html>
