<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/freight/shipping/shipping-temeplate-list.js"></script>

</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/cur_yen.png">  
           运费模板
    <input type="button"   value="批量删除" class="button butch delete"  title="批量删除"/>
    <input type="button" value="新增" class="button orange addtemplate" title="新增"/>
    <input type="button" value="全部生效" class="button orange enabletemplate" title="全部生效"/>
    </div>  
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table > 
        <tr> 
            <td><label>模板名称</label></td>
            <td><input type="text" id="name" placeHolder="模板名称" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>  
                  
		   <td><label>计价类型</label></td>
             <td> <select loxiaType="select" mandatory="false" id="calculationType" name="q_sl_calculationtype">
                   <option value="">不限</option>
					<option value="unit">计件</option>
					<option value="weight">计重</option>
					<option value="base">基础</option> 
                </select> 
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
   	 	<div class="table-border0 border-grey" id="table1" caption="运费模板列表"></div>   
    </div> 
    <div class="button-line">
    <input type="button" value="全部生效" class="button orange enabletemplate" title="全部生效"/>
    <input type="button" value="新增" class="button orange addtemplate" title="新增"/>
     <input type="button"   value="批量删除" class="button butch delete"  title="批量删除"/>
    </div>
</div> 		  

</body>
</html>
