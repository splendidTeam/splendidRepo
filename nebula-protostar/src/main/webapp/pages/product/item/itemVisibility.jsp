<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/itemVisibility.js"></script>
<style type="text/css">
.interval{
 height: 10px;
}

</style>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png"><spring:message code="item.visibility.msg"/>
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addItemVisibility" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
       			<td><label><spring:message code="item.visibility.mem.filter"/></label></td>
				<td>
					<input name="q_sl_memFilterName" type="text" loxiaType="input" ></input>
				</td>
       			<td><label><spring:message code="item.filter.title.create"/></label></td>
				<td>
					<input name="q_sl_itemFilterName" type="text" loxiaType="input" ></input>
				</td>
        </tr> 
    </table>
    	<div class="button-line1">
    		<a href="javascript:void(0);" class="func-button reset" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code="btn.reset"/></a>
        	<a href="javascript:void(0);" class="func-button search visibilisty" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="tableList" caption="<spring:message code="item.visibility.list"/>"></div>   
    </div>
    <div class="ui-title1">
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addItemVisibility" title="<spring:message code="btn.add" />"/>
	</div>
</div>
<div id="dialog-visibility" class="proto-dialog" wid="">
		<h5  id="wc_title" ><spring:message code="item.visibility.item.visibility"/>-<spring:message code="product.property.button.insert"/></h5>
		<div class="proto-dialog-content p10">
		 <div class="ui-block">
		  <div class="ui-block-line mt5"  >
			<label><spring:message code="item.visibility.mem.filter"/></label>
			<input  class="mem-name ismandatory" placeHolder="<spring:message code="item.visibility.mem.filter"/>" memid="" readonly="readonly"  loxiaType="input" mandatory="true"  />
			<a href="javascript:void(0);" class="func-button select1" ><spring:message code="sys.warning.select"/></a>
		  </div>
		  <div class="ui-block-line mt5"  >
			<label><spring:message code="item.filter.title.create"/></label>
			<input  class="item-name ismandatory" placeHolder="<spring:message code="item.filter.title.create"/>" itemid=""  readonly="readonly" loxiaType="input" mandatory="true"  />
			<a href="javascript:void(0);" class="func-button select2" ><spring:message code="sys.warning.select"/></a>
		  </div>
		</div>
        </div>
        <div  class="proto-dialog-button-line">
          	 <input type="button" id="confirm" value="<spring:message code="btn.confirm"/>" class="button orange">  
             <input type="button" id="cancel" value="<spring:message code="item.searchCodition.canel"/>" class="button delete">
        </div>
 </div>
 <!-- <spring:message code="item.visibility.mem.filter"/> -->
 <div id="dialog-mem-select" class="proto-dialog">
	<h5><spring:message code="item.visibility.mem.filter"/>-<spring:message code="sys.warning.select"/></h5>
	<div class="proto-dialog-content p10">
		<form id="mem_searchForm">
			<div class="ui-block">
	    	<div class="ui-block-content ui-block-content-lb">
	             <table>
	                <tr>
	               	 	<td>
	                    <label><spring:message code="member.filter.name"/></label>
	                    </td>
	                    <td>
	                    	<input loxiatype="input" name="q_sl_name" placeholder=""/>
	                    	<input name="q_int_lifecycle" type="hidden" value="1" loxiaType="number" />
	                    </td>
	                </tr>
	             </table>
        		 <div class="button-line1">
                 	<a href="javascript:void(0);" class="func-button search mem"><span><spring:message code="product.property.lable.search"/></span></a></div>
			    </div>
			 </div>
		</form>
	    <div class="ui-block">
	   	 	<div class="table-border0 border-grey" id="mem_tbl" caption="<spring:message code="item.visibility.mem.list"/>"></div>   
	    </div>
	</div>
</div>

<!-- <spring:message code="item.filter.title.create"/> -->
 <div id="dialog-item-select" class="proto-dialog">
	<h5><spring:message code="item.filter.title.create"/>-<spring:message code="sys.warning.select"/></h5>
	<div class="proto-dialog-content p10">
		<form id="item_searchForm">
			    <div class="ui-block">
	    	<div class="ui-block-content ui-block-content-lb">
	             <table>
	                <tr>
	               	 	<td>
	                    <label><spring:message code="member.filter.name"/></label>
	                    </td>
	                    <td>
	                    	<input loxiatype="input" name="q_sl_name" placeholder=""/>
	                    	<input name="q_int_lifecycle" type="hidden" value="1" loxiaType="number" />
	                    </td>
	                </tr>
	             </table>
        		 <div class="button-line1">
                 	<a href="javascript:void(0);" class="func-button search item"><span><spring:message code="product.property.lable.search"/></span></a></div>
			    </div>
			 </div>
		</form>
	    <div class="ui-block">
	   	 	<div class="table-border0 border-grey" id="item_tbl" caption="<spring:message code="item.visibility.item.list"/>"></div>   
	    </div>
	</div>
</div>
</body>
</html>
