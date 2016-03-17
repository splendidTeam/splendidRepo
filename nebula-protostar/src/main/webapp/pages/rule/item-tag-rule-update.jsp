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
<script type="text/javascript" src="${base}/scripts/rule/item-tag-rule-update.js"></script>
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
var  pdp_base_url = "${pdp_base_url}";   

</script>
</head>
<body>
<div class="content-box" style="float: left; width: 100%">
	<div class="ui-block">
		 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
		 	   <div class="ui-loxia-nav"><span class="ui-loxia-table-title"><spring:message code="item.filter.title.create"/>-<spring:message code="shop.update.update"/></span></div>
		 	  <div class="ui-block-line p5">
		 	  	<label><spring:message code="member.filter.type"/> </label>
					<div class="wl-right">
			 	  	 	<opt:select expression="chooseOption.ITEM_FILTER_TYPE" otherProperties="disabled=disabled" cssClass="slt-parent-type" defaultValue="${ customCombo.type }"  loxiaType="select"/>
						<input id="scope-parent-type" type="hidden" value="${customCombo.type}" />
					</div>
					</div>
					<div class="ui-block-line p5">
					<label><spring:message code="member.filter.name"/> </label>
					<div class="wl-right">
						<input id="scope-name" type="text" loxiaType="input" value="${customCombo.name}"  disabled="disabled" mandatory="true" size="50" />
        				<input id="scope-id" type="hidden" value="${customCombo.id}" />
					</div>
					
				</div>	
<c:if test="${!empty customCombo &&  customCombo.type==1 }">
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
						<!-- 
						-->
						<c:if test="${!empty details && !empty details.itemList}">
							<c:forEach items="${details.itemList }" var="item">
							
								<tr><td tip='<spring:message code="item.filter.item.list"/>：[${item.code }] ${item.title }' data='${item.id }'>
								<spring:message code="item.filter.item.list"/>： 
									<c:set var="baseurl" value="${pdp_base_url}"/>
									<c:set var="baseurllength" value="${fn:length(baseurl)}"/>
									<c:set var="bycode" value="${fn:endsWith(baseurl,'code')}"/>
									<c:if test="${bycode == true}">
										<c:set var="urlBycode" value="${fn:substring(baseurl, 0, baseurllength-4)}" />
										<c:set var="code" value="${item.code}" />
										<c:set var="urlcode" value="${fn:replace(urlBycode, '(@)',code)}" />
										<a href="${urlcode}" target="_blank" class="func-button" >[${item.code }] ${item.title}</a>
									</c:if>
									<c:if test="${bycode == false}">
										<c:set var="urlByitemId" value="${fn:substring(baseurl, 0, baseurllength-6)}" />
										<c:set var="id" value="${item.id}" />
										<c:set var="urlitemId" value="${fn:replace(urlByitemId, '(@)',id)}" />
										<a href="${urlitemId}" target="_blank" class="func-button" >[${item.code }] ${item.title}</a>
									</c:if>
								</td>
							<td>${item.salePrice }</td>
							<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
							</tr>
					
							</c:forEach>
						</c:if>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15 right">
				<select class="slt-type" style="display: none;"><option value="1"></option></select>
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
				<input class="txt-result" type="hidden" disabled="disabled" loxiaType="input" value="" size="50" />
			</div>
</div>
				</c:if>
				
				
<c:if test="${!empty customCombo &&  customCombo.type==2 }">
<!-- <spring:message code="item.filter.category"/> -->
<div id="category-select-lable" >
<%-- <spring:message code="item.filter.include.list"/> --%>
				<div id="result-include" class="slt-type-inc" style="float: left; width: 55%; margin-right: 20px;">
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
		 	  <table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="item.filter.category.name"/></th>
							<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
						</tr>
						
					</thead>
					<tbody>
					<c:if test="${!empty details && !empty details.categoryList }">
							<c:forEach items="${details.categoryList }" var="cate">
							
							<c:if test="${cate.id=='0' }">
								<tr><td tip='<spring:message code="item.filter.all.item"/>' data='0'><spring:message code="item.filter.all.item"/></td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
								</tr>
							
						</c:if>
							<c:if test="${cate.id !='0' }">
									<tr><td tip='<spring:message code="item.list.catagory"/>：${cate.name }' data='${cate.id }'><spring:message code="item.list.catagory"/>：${cate.name }</td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
								</tr>
								
							</c:if>
							</c:forEach>
						</c:if>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15 right">
			    <select class="slt-type" style="display: none;"><option value="2"></option></select>
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			</div>
				</div>
				<%-- <spring:message code="item.filter.except.list"/> --%>
				<div id="result-exclude" class="slt-type-exc" style="float: right; width: 42%;">
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
					<c:if test="${!empty details && !empty details.excCategoryList }">
							<c:forEach items="${details.excCategoryList }" var="exccate">
							
							<c:if test="${exccate.id !='0' }">
									<tr><td data_isCategory='true' tip='<spring:message code="item.list.catagory"/>：${exccate.name }' data='${exccate.id }'><spring:message code="item.list.catagory"/>：${exccate.name }</td>
									<td></td>
								<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
								</tr>
								
							</c:if>
							</c:forEach>
						</c:if>
					<c:if test="${!empty details.itemList }">
							<c:forEach items="${details.itemList }" var="item">
							
								<tr><td tip='<spring:message code="item.filter.item.list"/>：[${item.code }] ${item.title }' data='${item.id }'><spring:message code="item.filter.item.list"/>：[${item.code }] ${item.title }</td>
							<td>${item.salePrice }</td>
							<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
							</tr>
					
							</c:forEach>
						</c:if>
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
				</c:if>
				
<c:if test="${!empty customCombo &&  customCombo.type==3 }">
<%--自定义筛选器 --%>
	<div id="custom-select-lable" >
		<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
		<table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th><spring:message code="member.filter.name"/></th>
					<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
				</tr>
			</thead>
			<tbody>
			<c:if test="${!empty details }">
					<c:forEach items="${details.customizeFilterClassList}" var="customFilterClass">
						<tr><td tip='自定义筛选：${customFilterClass.scopeName }' data='${customFilterClass.id }'>自定义筛选：${customFilterClass.scopeName }</td>
					<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
					</tr>
				
					</c:forEach>
				</c:if>
			</tbody>
		</table>
		<div class="ui-block-line p5 pt15 right">
			<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
			<div id="group-content" class="p5 ml0">
			</div>
		</div>
	</div>
</c:if>
				
<c:if test="${!empty customCombo &&  customCombo.type==4 }">
<%--<spring:message code="item.filter.combo.list"/> --%>
<div id="combo-select-lable" >
<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
		 	  <table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="member.filter.name"/></th>
							<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${!empty detailsList }">
							<c:forEach items="${detailsList }" var="zitem">
							
								<tr><td tip='<spring:message code="member.filter.type.combo"/>：${zitem.name }' data='${zitem.id }'><spring:message code="member.filter.type.combo"/>：${zitem.name }</td>
							<td><a href='javascript:void(0);' class='func-button btn-remove' title='<spring:message code="btn.delete"/>'><spring:message code="btn.delete"/></a></td>
							</tr>
						
							</c:forEach>
						</c:if>
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
				</c:if>
		 	  
		 	  <div class="p10 right clear">
				   <input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save"   title="<spring:message code='btn.save'/>" />
				   <input type="button" value="<spring:message code='btn.return'/>" class="button black btn-return"   title="<spring:message code='btn.return'/>" />
			  </div>
		 </div>
	
	</div>
</div>

<%-------------------------------------------------------- <spring:message code="item.filter.selector"/> --------------------------------------------------------%>


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
				                    <td><label><spring:message code="item.code"/></label>
				                    	</td>
				                    <td>
				                    	<input loxiatype="input" name="q_string_code" placeholder=""/>
				                    	</td>
				                       
				                    <td><label><spring:message code="item.filter.brand"/></label>
				                    	</td>
				                    <td>
				                    	<input loxiatype="input" placeholder=""/>
				                    	</td>
				                    
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

<%--自定义筛选器选择--%>
<div id="dialog-custom-select" class="proto-dialog">
    <h5>自定义筛选器选择</h5>
    <div class="proto-dialog-content p10">
    	<div class="ui-block" style="margin-bottom: 20px;">
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

<div id="dialog-combo-select" class="proto-dialog">
    <h5> <spring:message code="item.filter.item.category"/></h5>
    <div class="proto-dialog-content p10">
    	<div class="ui-block" style="margin-bottom: 20px;">
			<label><spring:message code="member.filter.type"/>：</label>
			<select loxiaType="select" style="width: 100px;" class="slt-custom-type" >
				<option value="1"><spring:message code="item.filter.item.list"/></option>
				<option value="2"><spring:message code="item.list.catagory"/></option>
				<option value="3">自定义筛选器</option>
			</select>
		</div>
		<div class="ui-block">
		<c:if test="${!empty comboList }">
			<c:forEach items="${comboList }" var="rule">
				<span class="children-store"><label>
				<input type="checkbox" name="chk-group" data_type="${rule.type }" value="${rule.id }" />${rule.name }
				</label>
				</span>
			</c:forEach>
			</c:if>
		</div>
	</div>
    <div class="ui-block-content center">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
     </div>
</div>
</body>
</html>
