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
<script type="text/javascript" src="${base}/scripts/product/scope/product-scope-selector.js"></script>
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

var category_ZNodes = [
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
var  pdp_base_url = "${pdp_base_url}";
</script>
</head>
<body>
<div class="content-box" style="float: left; width: 100%">
	<div class="ui-block">
		 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
		 	  <div class="ui-loxia-nav"><span class="ui-loxia-table-title"><spring:message code="item.filter.title.create"/></span></div>
		 	  <div class="ui-block-line p5">
		 	  	<label><spring:message code="member.filter.type"/> </label>
					<div class="wl-right">
						<opt:select expression="chooseOption.ITEM_FILTER_TYPE" cssClass="slt-parent-type"  loxiaType="select"/>
						<%-- 
						<select loxiaType="select" style="width: 160px;" class="slt-parent-type" >
							<option value="101"><spring:message code="item.filter.item.list"/></option>
							<option value="102"><spring:message code="item.list.catagory"/></option>
							<option value="104"><spring:message code="member.filter.type.combo"/></option>
						</select> 
						--%>
					</div>
					</div>
					 <div class="ui-block-line p5">
					<label><spring:message code="member.filter.name"/> </label>
					<div class="wl-right">
						<input id="scope-name" type="text" loxiaType="input" value="" mandatory="true" size="50" />
					</div>
					
				</div>	
			<div id="item-select-lable" >
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
		 	<table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
	 			<thead>
					<tr>
						<th><spring:message code="item.name"/></th>
						<th style="width: 20%;"><spring:message code="item.filter.item.price"/></th>
						<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
		 	</table>
		 	<div class="ui-block-line p5 pt15 right">
				<select class="slt-type" style="display: none;"><option value="1"></option></select>
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
				<input class="txt-result" type="hidden" disabled="disabled" loxiaType="input" value="" size="50" />
			</div>
</div>
				

<%-- <spring:message code="item.filter.category"/> --%>
<div id="category-select-lable" style="display:none">
<%-- <spring:message code="item.filter.include.list"/> --%>
				<div class="slt-type-inc" style="float: left; width: 55%; margin-right: 20px;">
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
		 	  <table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="item.filter.category.name"/></th>
							<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15 right">
			    <select class="slt-type" style="display: none;"><option value="2"></option></select>
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			</div>
				</div>
				<%-- <spring:message code="item.filter.except.list"/> --%>
				<div class="slt-type-exc" style="float: right; width: 42%;">
				<div class="bold p5"><spring:message code="member.filter.excludeList"/></div>
		 	  <table id="tbl-result-exclude" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="item.name"/></th>
							<th style="width: 20%;"><spring:message code="item.filter.item.price"/></th>
							<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 	  </table>
		 	   <div class="ui-block-line p5 pt15 right">
				<select id="slt-exclude-type" class="slt-type" style="float: left" loxiaType="select">
		 	   		<option value="1">商品</option>
		 	   		<option value="2">分类</option>
		 	   	</select>
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			</div>
		 	  </div>
</div>
<%--自定义筛选器 --%>
<div id="custom-select-lable" style="display:none">
	<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
	<table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<th>自定义筛选器名称</th>
				<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
			</tr>
		</thead>
		<tbody>
			
		</tbody>
 	</table>
 	<div class="ui-block-line p5 pt15 right">
	 	<select loxiaType="select" style="display: none;" class="slt-type">
			<option value="4"><spring:message code="member.filter.type.combo"/></option>
		</select>    
		<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
		<div id="group-content" class="p5 ml0"></div>
	</div>
</div>

<%--<spring:message code="item.filter.combo.list"/> --%>
<div id="combo-select-lable" style="display:none">
<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
		 	  <table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="member.filter.name"/></th>
							<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15 right">
		 	  <select loxiaType="select" style="display: none;" class="slt-type">
					<option value="4"><spring:message code="member.filter.type.combo"/></option>
				</select>    
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
				<div id="group-content" class="p5 ml0">
				</div>
			</div>
</div>
		 	  
		 	  <div class="p10 right clear">
				   <input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save"   title="<spring:message code='btn.save'/>" />
				     <input type="button" value="<spring:message code='btn.return'/>" class="button black btn-return"   title="<spring:message code='btn.return'/>" />
			  </div>
		 </div>
	
	</div>
</div>

<%-------------------------------------------------------- <spring:message code="item.filter.selector"/> --------------------------------------------------------%>

<%-- 商品选择器 --%>
<div id="dialog-item-select" class="proto-dialog">
	<h5><spring:message code="product.item.scope.select"/></h5>
	<div class="proto-dialog-content p10">
	<form id="searchForm">
				    <div class="ui-block">
				    	<div class="ui-block-content ui-block-content-lb">
				             <table>
				                <tr>
				               	 	<td>
				                    <label>商品名称</label>
				                    </td>
				                    <td>
				                    	<input loxiatype="input" name="q_sl_title" placeholder=""/>
				                    </td>
				                    <td>
				                    <label><spring:message code="item.code"/></label>
				                    </td>
				                    <td>
				                    	<input loxiatype="input" name="q_string_code" placeholder=""/>
				                    </td>
				                       
				                    <td><label><spring:message code="item.filter.brand"/></label>
				                    	</td>
				                    <td>
				                    	<input loxiatype="input" placeholder=""/>
				                    	</td>
				                    	<!-- 
				                    	<td><label><spring:message code="item.filter.shop.name"/></label>
				                    	</td>
				                    <td>
				                    	<select loxiaType="select" class="slt-shop-id" style="width: 100px;" name="q_long_orgid" id="orgid">
				                    	<c:if test="${!empty organizationList }">
										<c:forEach items="${organizationList }" var="org">
											<option  value="${org.id }">${org.name }</option>
										</c:forEach>
										</c:if>
									
										</select>
				                    	</td>
				                    	 -->
				                </tr>
				                <tr>
				                	<td>
				                    <label>是否主卖品</label>
				                    </td>
				                    <td>
				                    	<select loxiaType="select" mandatory="false" id="couponType"name="q_long_type">
				                    		<option value="2">全部</option>
				                    		<option value="1">主卖品</option>
				                    		<option value="0">赠品</option>
				                    	</select>
				                    </td>
				                    <td><label><spring:message code="item.filter.item.price"/></label>
				                    	</td>
				                    <td>
				                    	<input loxiatype="input" name="q_long_minPrice" placeholder=""/>
				                        </td>
				                    <td align="center"><label>——</label></td>
				                         <td>
				                    <input loxiatype="input" name="q_long_maxPrice" placeholder=""/>
				                        </td>
	                     <!-- 
				                        <td>
				                        </td>	
	                     <td>
				                        <label><spring:message code="item.list.catagory"/></label>
	                     <input type="hidden"
								id="categoryId" name="q_long_categoryId" mandatory="true" /> <input 
								type="text" loxiaType="input" id="categoryName"
								mandatory="false" placeholder="<spring:message code='item.list.catagory'/>"/>
						</td>
	                      -->
				                        
				                    </tr>
				                </table>
				        <div class="button-line1">
				                <a href="javascript:void(0);"
				                   class="func-button search"><span><spring:message code="product.property.lable.search"/></span></a>
				                   
				        </div>
				    </div>
				 </div>
			</form>
			<div class="table-border0 border-grey" id="table1" caption="<spring:message code='item.list.itemList'/>"></div>  
		</div>
         <div class="proto-dialog-button-line center">
				<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
         </div>
</div>
<%-- 商品分类选择器 --%>
<div id="dialog-category-select-left" class="proto-dialog">
	<h5><spring:message code="navigation.list.category"/></h5>
	 <div class="proto-dialog-content p10">
			 <div>
			 	<div id="select-all-item-id"><span class="select-all"><input id="select-all-item" type="checkbox" name="chk-group" value="0" /><spring:message code="item.filter.all.item"/></span></div>
			 </div>
                <div class="tree-control">
                    <input type="text" id="key-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
                    <div><span id="search_result_left"></span></div>
                <ul id="tree-left" class="ztree"></ul>
                </div>

      </div>
      <div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>

<%-- 自定义筛选器选择器 --%>
<div id="dialog-custom-select" class="proto-dialog">
    <h5>自定义筛选器选择</h5>
    <div class="proto-dialog-content p10">
    	<div class="ui-block">
			<label style="margin-left: 5px;"><spring:message code="member.filter.name"/>：</label>
			<input class="txt-name" loxiaType="input" />
		</div>
		<div class="ui-block right">
			<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
		</div>
		<div class="ui-block combo-list">
			
		</div>
	</div>
    <div class="proto-dialog-button-line center">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
     </div>
</div>
<%-- 商品组合选择器 --%>
<div id="dialog-combo-select" class="proto-dialog">
    <h5> <spring:message code="item.filter.item.category"/></h5>
    <div class="proto-dialog-content p10">
    	<div class="ui-block">
			<label><spring:message code="member.filter.type"/>：</label>
			<select loxiaType="select" style="width: 100px;" class="slt-custom-type" >
				<option value="0"><spring:message code="member.group.label.unlimit"/></option>
				<option value="1"><spring:message code="item.filter.item.list"/></option>
				<option value="2"><spring:message code="item.list.catagory"/></option>
				<option value="3">自定义筛选器</option>
			</select>
			<label style="margin-left: 5px;"><spring:message code="member.filter.name"/>：</label>
	 	 	<input class="txt-name" loxiaType="input" />
		</div>
		<div class="ui-block right">
			<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
		</div>
		<div class="ui-block combo-list">
			
		</div>
	</div>
    <div class="proto-dialog-button-line center">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
     </div>
</div>

 <div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>
</body>
</html>
