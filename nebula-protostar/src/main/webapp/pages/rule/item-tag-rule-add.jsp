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
<script type="text/javascript" src="${base}/scripts/rule/item-tag-rule-add.js"></script>

</head>
<body>
<div class="content-box" style="float: left; width: 100%">
	<div class="ui-block">
		 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
		 	  <div class="ui-loxia-nav"><span class="ui-loxia-table-title"><spring:message code="item.filter.combiner"/></span></div>
		 	  <div class="ui-block-line p5">
					<label><spring:message code="member.filter.comboName"/></label>
					<div class="wl-right">
						<input id="scope-name" type="text" loxiaType="input" value="" mandatory="true" size="50" />
					</div>
				</div>	
				<%-- 包含表 --%>
				<div>
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
		 	  <table id="tbl-result-include" class="pt10 tbl-result" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="item.filter.combination.type"/></th>
							<th style="width: 20%;"><spring:message code="member.filter.operation"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 	  </table>
		 	  <div class="ui-block-line p5 pt15">
				
				 <label class="slt-type"><spring:message code="item.filter.item.list"/></label>
				 <div>
				<input class="txt-result" type="text" disabled="disabled" loxiaType="input" value="" size="50" />
				<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code='btn.select'/>" ><spring:message code='btn.select'/></a>
				<a href="javascript:void(0);" class="func-button btn-add-left" title="<spring:message code='btn.add2'/>" ><spring:message code='btn.add2'/></a>
				 </div>
			</div>
				</div>
		 	  
		 	  <div class="p10 right clear">
				   <input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save"   title="<spring:message code='btn.save'/>" />
			  </div>
		 </div>
	
	</div>
</div>

<%-------------------------------------------------------- 选择框 --------------------------------------------------------%>

<div id="dialog-item-select" class="proto-dialog">
	<h5><spring:message code="product.item.scope.select"/></h5>
	<form id="searchForm">
				    <div class="ui-block">
				    	<div class="ui-block-content ui-block-content-lb">
				             <table>
				                <tr>
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
				                    <td><label><spring:message code="item.code"/>价格</label>
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
				                   class="func-button search"><span><spring:message code="btn.search"/></span></a>
				                   
				        </div>
				    </div>
				 </div>
		<div class="table-border0 border-grey" id="table1" caption="<spring:message code='item.list.itemList'/>"></div>  
           			 <div class="ui-block-content center">
						<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
                	<input type="hidden" id="categoryId" name="q_sl_categoryId" value="" />
            		</div>
			</form>
		
	
</div>

</body>
</html>
