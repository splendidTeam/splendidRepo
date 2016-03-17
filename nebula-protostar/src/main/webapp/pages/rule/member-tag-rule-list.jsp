<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/rule/member-tag-rule-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">人群筛选管理
		<input type="button" value="<spring:message code="promotion.editorList.create"/>" class="button orange addcustomgroup" title="<spring:message code="promotion.editorList.create"/>"/>
    </div>
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table > 
        <tr> 
           <td><label>筛选器名称</label></td>
           <td><input type="text" id="groupName" placeHolder="筛选器名称" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>  
             
		   <td><label>筛选器类型</label></td>
             <td> 
             	<opt:select name="q_int_type" loxiaType="select"  expression="chooseOption.MEMBER_FILTER_TYPE" nullOption="role.list.label.unlimit"  />
             </td>

        </tr> 
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="人群筛选列表"></div>   
    </div>
     
</div>
<%-------------------------------------------------------- 查看浮层 --------------------------------------------------------%>
<div id="view-block" class="proto-dialog">
	<h5>筛选器</h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block-line p5">
 	  		<label>筛选器类型</label>
 	  		<opt:select cssClass="txt-type" loxiaType="select"  expression="chooseOption.MEMBER_FILTER_TYPE" />
	 	  	<!-- <input class="txt-type" loxiaType="input" disabled="disabled" value="会员" /> -->
		</div>
		<div class="ui-block-line p5">
			<label>筛选器名称</label>
			<div class="wl-right">
				<input class="txt-name" loxiaType="input" value="XXX" disabled="disabled" />
			</div>
		</div>	
		<div class="ui-block">
			 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
			 	<div class="include-list" style="float: left; width: 55%; margin-right: 20px;">
				<div class="bold p5">包含列表</div>
				
			 	  <table class="pt10" cellspacing="0" cellpadding="0">
			 	  		 <thead>
							<tr>
								<th>名称</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
			 	  </table>
			 </div>
			 <div class="exclude-list" style="float: right; width: 40%;">
				<div class="bold p5">排除列表</div>
		 	  <table class="pt10" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th>名称</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 	  </table>
		 	  </div>
			 </div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>
				  
</html>
