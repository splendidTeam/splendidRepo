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
	src="${base}/scripts/product/item/item-category.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript">	

var zNodes = [
            	{id:0, pId:-1, name:"ROOT", 
             		code:"ROOT", sortNo:1, 
             		open:true, lifecycle:1},
                 <c:forEach var="category" items="${categoryList}" varStatus="status">
                 	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
                 		code:"${category.code}", sortNo:${category.sortNo}, 
                 		open:false, lifecycle:${category.lifecycle}}
                 	<c:if test="${!status.last}">,</c:if>
                 </c:forEach>
            ];


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
		<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/tag.png"><spring:message code="itemcategory.manage"/>
		                <input type="button" value="<spring:message code='itemcategory.unbind'/>" title="<spring:message code='itemcategory.unbind'/>"  class="button orange removesort" />
                        <input type="button" value="<spring:message code='itemcategory.bind'/>" title="<spring:message code='itemcategory.bind'/>"  class="button orange addsort" />
                        <input type="button" value="<spring:message code='itemcategory.setDefault'/>" title="<spring:message code='itemcategory.setDefault'/>"  class="button setDefault" />
						
		</div>
		<div class="ui-block">
		<form action="/category/itemCtList.json" id="searchForm">
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
						 
						<td><label><spring:message code="itemcategory.list.label.status"/></label></td>
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
			            
			            <td><label><spring:message code="itemcategory.listtime"/></label></td>
			            <td><input type="text" id="listStartDate" name="q_date_listStartDate" loxiaType="date" mandatory="false"></input>
			            </td>
			            
			            <td><label>——</label></td>
			            <td>
			            	<input type="text" name="q_date_listEndDate" id="listEndDate" loxiaType="date" mandatory="false"></input>
			            </td>
                        
                    </tr>
                </table>
                <div class="button-line1">
                	<input type="hidden" id="categoryId" name="q_long_categoryId" value="" />
        			<a href="javascript:void(0);" class="func-button search" title="<spring:message code='btn.search'/>"><spring:message code="btn.search"/></a>
        		</div>
            </div>
            </form>
        </div>
        <div class="ui-block ui-block-fleft w240">
            <div class="ui-block-content ui-block-content-lb">
                <div class="tree-control">
                    <input type="text" id="key" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
                    <div><span id="search_result"></span></div>
                </div>
                <ul id="tree" class="ztree"></ul>

            </div>
        </div>
        <div class="ui-block ml240">
            <div class="ui-block-content" style="padding-top:0">
                
                    <div class="ui-tag-change">
                        <ul class="tag-change-ul">
                            <li class="memberbase"><spring:message code="itemcategory.classified.list"/></li>
                            <li class="memberbase"><spring:message code="itemcategory.noclassified.list"/></li>
                        </ul>
                        <div class="tag-change-content">
                            <div class="tag-change-in">
                                <div class="border-grey ui-loxia-simple-table" id="table1" caption="<spring:message code='itemcategory.classified.list'/>"></div>
                            </div>
                            <div class="tag-change-in">
                                <div class="border-grey ui-loxia-simple-table" id="table2" caption="<spring:message code='itemcategory.noclassified.list'/>" ></div>
                            </div>
                        </div>
                    </div>
                    <div class="button-line">
                    	<input type="button" value="<spring:message code='itemcategory.setDefault'/>" title="<spring:message code='itemcategory.setDefault'/>"  class="button setDefault" />
                        <input type="button" value="<spring:message code='itemcategory.bind'/>" title="<spring:message code='itemcategory.bind'/>"  class="button orange addsort" />
						<input type="button" value="<spring:message code='itemcategory.unbind'/>" title="<spring:message code='itemcategory.unbind'/>"  class="button orange removesort"/>
                    </div>
                
            </div>
        </div>
	</div>
	<div id="categoryContent" class="menuContent" style="z-index:999;display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	   <ul id="categoryDemo" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
    </div>
    <div id="industryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="industryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>
</body>
</html>
