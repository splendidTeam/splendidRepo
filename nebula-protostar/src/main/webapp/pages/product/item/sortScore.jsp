<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base }/scripts/product/item/itemSortScore.js"></script>
<script type="text/javascript">

var zNodes = [
            	{id:0, pId:-1, name:"ROOT", 
             		code:"ROOT", sortNo:1, 
             		open:true, lifecycle:1,nocheck:true},
                 <c:forEach var="category" items="${categoryList}" varStatus="status">
                 	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
                 		code:"${category.code}", sortNo:${category.sortNo}, 
                 		open:false, lifecycle:${category.lifecycle}}
                 	<c:if test="${!status.last}">,</c:if>
                 </c:forEach>
            ];
//行业树
var industrZNodes =[           
         	{ id:0, name:"ROOT",state:"1", open:true,root:"true",nocheck:true},
         	<c:forEach var="industry" items="${industryList}" varStatus="status">
         	{id:${industry.id}, pId:${null eq industry.parentId?0:industry.parentId}, name: "${industry.name}", state:"${industry.lifecycle}",open:${0 eq industry.parentId?false:false}}<c:if test="${!status.last}">,</c:if>
         	</c:forEach>
         	
         ];

</script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">默认排序引擎管理
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addItemSortScore" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label>排序类型</label></td>
			<td>
			<opt:select id="isused" name="q_int_type" loxiaType="select" expression="chooseOption.ITEM_SORT_SCORE" nullOption="member.group.label.unlimit" />
			</td>
			 <td><label>计算类型</label></td>
			<td>
			<opt:select id="isused" name="q_int_operator" loxiaType="select" expression="chooseOption.ITEM_SORT_OPER" nullOption="member.group.label.unlimit" />
			</td>
            <td><label>状态</label></td>
			<td>
			<opt:select id="isused" name="q_int_lifecycle" loxiaType="select"  expression="chooseOption.IS_AVAILABLE" nullOption="member.group.label.unlimit" />
			</td>
            <td></td> 
            <td></td>
        </tr> 
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="table1" caption="默认排序引擎管理列表"></div>   
    </div>
    
    <div id="dialog-item-select" class="proto-dialog">
		<h5  id="sort_title" >默认排序引擎管理-新增</h5>
		<div class="proto-dialog-content p10">
		   <div id="ui-tag-change-id" class="ui-tag-change"  optId="" >
                 <ul class="tag-change-ul">
                     <li class="memberbase category" type="1">分类</li>
                     <li class="memberbase property" type="2" >属性</li>
                     <li class="memberbase other" type ="3">其他</li>
                 </ul>
                 <div class="tag-change-content">
                 	<!--分类  -->
                     <div id="tag-change-in-category" class="tag-change-in">
                     	<span style="font-weight: bold;">分类</span>
                  		<div class="tree-control" style="margin-left: 30px">
		                    <input type="text" id="key-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
		                    <div><span id="search_result_left"></span></div>
		               		 <ul id="tree-left" class="ztree"></ul>
		                </div>
		                <div style="margin-top: 10px"><span style="font-weight: bold;">权重</span><input style="margin-left: 10px" type="text" id="cate_score" loxiaType="number" mandatory="true"> </div>
                     </div>
                     <!-- 属性 -->
                     <div id="tag-change-in-property" class="tag-change-in">
                     	<div style="margin-top: 10px">
                     		<span style="font-weight: bold;">行业</span>
                     	<div class="tree-control" style="margin-left: 30px">
		                    <input type="text" id="hkey-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
		                    <div><span id="hsearch_result_left"></span></div>
		               		 <ul id="industr-tree-left" class="ztree"></ul>
		                </div>
                     	</div>
						<div style="margin-top: 10px">
                     		<span style="font-weight: bold;margin-right: 10px">属性</span><opt:select id="per_select" loxiaType="select" />
                     		<opt:select id="per_select_oper" loxiaType="select" expression="chooseOption.ITEM_SORT_OPER"  />
                     		<input style="margin-left: 10px" type="text" id="per_param" loxiaType="number" mandatory="true">
                     	</div>
						<div style="margin-top: 10px"><span style="font-weight: bold;">权重</span><input style="margin-left: 10px" type="text" id="per_score" loxiaType="number" mandatory="true"> </div>
                     </div>
                      <!-- 其他 -->
                     <div id="tag-change-in-other" class="tag-change-in">
                     	<div style="margin-top: 10px">
                     		<span style="font-weight: bold;margin-right: 10px">类型</span><opt:select id="other_select" loxiaType="select" expression="chooseOption.ITEM_SORT_SCORE" />
                     		<opt:select id="other_select_oper" loxiaType="select" expression="chooseOption.ITEM_SORT_OPER"  />
                     		<input style="margin-left: 10px" type="text" id="param" loxiaType="number" mandatory="true">
                     	</div>
						  <div style="margin-top: 10px"><span style="font-weight: bold;">权重</span><input style="margin-left: 10px" type="text" id="other_score" loxiaType="number" mandatory="true"> </div>
                     </div>
                 </div>
            </div>
           
		</div>
		 <div  class="proto-dialog-button-line">
          	  <input type="button" id="confirm_sort" value="确定" class="button orange">  
             <input type="button" value="取消" id="cancel" 	class="button delete">
        </div>
     </div>
</div>

</body>
</html>