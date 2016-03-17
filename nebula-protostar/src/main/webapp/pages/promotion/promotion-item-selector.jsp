<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/>nbdgdyj</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>

<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>

<script type="text/javascript"
	src="${base}/scripts/promotion/item-selector.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript">	

var zNodes = [
     <c:forEach var="category" items="${categoryList}" varStatus="status">
     	<c:set var="isRoot" value="${null eq category.parentId}"></c:set>
     	{id:${category.id}, pId:${isRoot?0:category.parentId}, name:"${category.name}", 
     		code:"${category.code}", sortNo:${category.sortNo}, 
     		open:${isRoot?true:false}, lifecycle:${category.lifecycle}}
     	<c:if test="${!status.last}">,</c:if>
     </c:forEach>
];
</script>

</head>
<body>
	<div class="content-box">
		
		
        <div class="ui-block ui-block-fleft w240">
            <div class="ui-block-content">
               
                <ul id="tree" class="ztree"></ul>

            </div>
        </div>
        <div class="ui-block ml240">
            <div class="ui-block-content" style="padding-top:0">
                
                    <div class="ui-tag-change">
                        <div class="tag-change-content">
                            <div class="tag-change-in">
                                <div class="border-grey ui-loxia-simple-table" id="table1" caption="<spring:message code='itemcategory.classified.list'/>"></div>
                            </div>
                           
                        </div>
                    </div>
                    <div class="button-line">
                    <form action="/category/itemCtList.json" id="searchForm">
                    
                      <table>
                <tr>
                        
                        <td><span id="searchkeytext"> <input type="text"
								loxiaType="input" mandatory="false" id="code"
								name="q_sl_code" placeholder="<spring:message code='item.code'/>"></input>
						</span></td>
                        <td><span id="searchkeytext"><input type="text"
								loxiaType="input" mandatory="false" id="title"
								name="q_sl_title" placeholder="<spring:message code='item.name'/>">
								</span></input>
						</td>
						 
						<td>
						 <input type="button" value="查询商品"  class="func-button search" />
						
						</td>
						
                        <td>
                        <input type="button" value="选择目录"  class="func-button selectCategorys"/>
                        </td>
									
						<td>
						<input type="button" value="选择商品"  class="func-button selectItems" />
						</td>
                    </tr>
                    
                </table>
           			 <div class="ui-block-content">
                	<input type="hidden" id="categoryId" name="q_sl_categoryId" value="" />
            		</div>
           			 </form>
                    </div>
                
            </div>
        </div>
	</div>
	<div id="categoryContent" class="menuContent" style="z-index:999;display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	   <ul id="categoryDemo" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
    </div>
    
</body>
</html>
