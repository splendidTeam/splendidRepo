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
<script type="text/javascript" src="${base}/scripts/system/warning/warningConfig.js"></script>
<style type="text/css">
.t_width{
	width: 80px;
}
.s_width{
	width: 200px;
}
#dialog-item-select label {
	margin-right: 5px;
	margin-bottom: 20px;
}
.input_add{
 width: 200px
}
.interval{
 height: 10px;
}
#tableList span{
 word-break: break-all;
}
</style>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png"><spring:message code="sys.warning.setting"/>
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addWarningConfig" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
      		<td><label><spring:message code="sys.warning.type"/></label></td>
			<td>
				<opt:select  name="q_int_type" loxiaType="select" expression="chooseOption.WARNING_TYPE" nullOption="member.group.label.unlimit" />
			</td>
      		<td><label><spring:message code="sys.warning.level"/></label></td>
			<td>
				<opt:select  name="q_int_level" loxiaType="select" expression="chooseOption.WARNING_LEVEL" nullOption="member.group.label.unlimit" />
			</td>
			<td><label><spring:message code="sys.warning.time"/></label></td>
			<td>
				<opt:select  name="q_sl_timeParam" loxiaType="select" expression="chooseOption.WARNING_TIME" nullOption="member.group.label.unlimit" />
			</td>
        </tr> 
        <tr>
			<td><label><spring:message code="member.group.name"/></label></td>
			<td>
				<input  name="q_sl_name"  type="text" loxiaType="input" >
			</td>
			<td><label><spring:message code="product.property.lable.code"/></label></td>
			<td>
				<input  name="q_string_code"  type="text" loxiaType="input" >
			</td>
        </tr> 
    </table>
    	<div class="button-line1">
    		<a href="javascript:void(0);" class="func-button reset" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code="btn.reset"/></a>
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="tableList" caption="<spring:message code="sys.warning.list"/>"></div>   
    </div>
 	<div class="ui-title1">
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addWarningConfig" title="<spring:message code="btn.add" />"/>
	</div>
</div>

 	<!-- 弹出编辑框 -->
   <div id="dialog-item-select" class="proto-dialog" wid="">
		<h5  id="wc_title" ><spring:message code="sys.warning.manager"/>-</h5>
		<div class="proto-dialog-content p10">
			<table style="width: 700px" class="edit_tbl">
				<tr>
	       			<td><label><spring:message code="product.property.lable.code"/></label></td>
					<td>
						<input class="input_add code"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
	       			<td><label><spring:message code="member.group.name"/></label></td>
					<td>
						<input class="input_add name"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr>
		        <tr class="interval"></tr>
		        <tr>
	       			<td><label><spring:message code="cms.page.add.descriptioninfo"/></label></td>
					<td colspan="3">
						<textarea class="desc"  style="margin: 0px; width: 545px; height:60px;resize: none;"  loxiaType="input" ></textarea>
					</td>
		        </tr> 
		        <tr class="interval"></tr>
		        <tr>
	       			<td><label><spring:message code="order.detail.time"/></label></td>
					<td>
						<opt:select cssClass="t_width time_suffix" loxiaType="select" expression="chooseOption.WARNING_TIME" />
						<input style="width: 110px;margin-left: 9px"  class="input_add time_prefix"  type="text" loxiaType="number"  mandatory="true"  >
					</td>
					<td><label><spring:message code="sys.warning.level"/></label></td>
					<td>
						<opt:select  name="level" cssClass="level s_width" loxiaType="select" expression="chooseOption.WARNING_LEVEL"  />
					</td>
		        </tr>
		        <tr class="interval"></tr>
		        <tr>
	       			<td><label><spring:message code="sys.warning.type"/></label></td>
					<td>
						<opt:select  name="type" cssClass="type s_width" loxiaType="select" expression="chooseOption.WARNING_TYPE" />
					</td>
	       			<td><label><spring:message code="sys.warning.code"/></label></td>
					<td>
						<input  class="input_add tmpCode"  style="width: 160px" readonly="readonly" type="text" loxiaType="input"  mandatory="true"  >
						<a href="javascript:void(0);" class="func-button select" ><spring:message code="sys.warning.select"/></a>
					</td>
		        </tr> 
		        <tr class="interval"></tr>
		        <tr>
	       			<td><label><spring:message code="sys.warning.count"/></label></td>
					<td>
						<input  class="input_add count"  type="text" loxiaType="number"  mandatory="true"  >
					</td>
		        </tr> 
		        <tr class="interval"></tr>
		        <tr class="tr_receiver_pre">
	       			<td><label><spring:message code="sys.warning.receiver"/></label></td>
					<td colspan="3">
						<input  class="input_add receivers email" type="text" loxiaType="input" >
						<a href="javascript:void(0);" class="func-button add" ><spring:message code="btn.add2"/></a>
					</td>
		        </tr> 
		        <tr class="tr_receiver" style="display: none;">
	       			<td><label></label></td>
					<td colspan="3">
						<input  class="input_add receivers email"  type="text" loxiaType="input" >
						<a href="javascript:void(0);" class="func-button del" ><spring:message code="btn.delete"/></a>
					</td>
		        </tr> 
    		</table>
		</div>
		<div  class="proto-dialog-button-line">
          	 <input type="button" id="confirm_warning" value="<spring:message code="btn.confirm"/>" class="button orange">  
             <input type="button" id="cancel" value="<spring:message code="item.searchCodition.canel"/>" class="button delete">
        </div>
     </div>
     
     <div id="dialog-email-select" class="proto-dialog">
		<h5><spring:message code="sys.warning.email.select"/></h5>
		<div class="proto-dialog-content p10">
			<form id="email_searchForm">
				    <div class="ui-block">
				    	<div class="ui-block-content ui-block-content-lb">
				             <table>
				                <tr>
				               	 	<td>
				                    <label><spring:message code="member.group.name"/></label>
				                    </td>
				                    <td>
				                    	<input loxiatype="input" name="q_sl_name" placeholder=""/>
				                    	<input name="q_int_lifecycle" type="hidden" value="1" loxiaType="number" />
				                    </td>
				                    <td>
				                    <label><spring:message code="product.property.lable.code"/></label>
				                    </td>
				                    <td>
				                    	<input loxiatype="input" name="q_string_code" placeholder=""/>
				                    </td>
				                </tr>
				             </table>
			        		 <div class="button-line1">
			                 <a href="javascript:void(0);"
			                   class="func-button search email"><span><spring:message code="product.property.lable.search"/></span></a>
			       `		 </div>
				    </div>
				 </div>
			</form>
		    <div class="ui-block">
		   	 	<div class="table-border0 border-grey" id="email_tbl" caption="<spring:message code="sys.warning.email.list"/>"></div>   
		    </div>
		</div>
        <div class="proto-dialog-button-line center">
				<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
        </div>
	</div>
	    <div id="dialog-sms-select" class="proto-dialog">
		<h5><spring:message code="sys.warning.sms.select"/></h5>
		<div class="proto-dialog-content p10">
			<form id="sms_searchForm">
				    <div class="ui-block">
				    	<div class="ui-block-content ui-block-content-lb">
				             <table>
				                <tr>
				               	 	<td>
				                    <label><spring:message code="member.group.name"/></label>
				                    </td>
				                    <td>
				                    	<input loxiatype="input" name="q_sl_name" placeholder=""/>
				                    </td>
				                    <td>
				                    <label><spring:message code="product.property.lable.code"/></label>
				                    </td>
				                    <td>
				                    	<input loxiatype="input" name="q_string_code" placeholder=""/>
				                    </td>
				                </tr>
				             </table>
			        		 <div class="button-line1">
			                 <a href="javascript:void(0);"
			                   class="func-button search sms"><span><spring:message code="product.property.lable.search"/></span></a>
			       `		 </div>
				    </div>
				 </div>
			</form>
		    <div class="ui-block">
		   	 	<div class="table-border0 border-grey" id="sms_tbl" caption="<spring:message code="sys.warning.sms.lsit"/>"></div>   
		    </div>
		</div>
        <div class="proto-dialog-button-line center">
				<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
        </div>
	</div>
</body>
</html>
