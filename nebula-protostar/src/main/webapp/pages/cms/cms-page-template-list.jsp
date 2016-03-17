<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript">
var customBaseUrl = '<c:out value="${ customBaseUrl }" escapeXml="" default=""></c:out>';
	
</script>
<script type="text/javascript" src="${base }/scripts/cms/cms-page-template-list.js"></script>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>

</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
          页面模板管理
    <input type="button" id="deleteAll" value="批量删除" class="button orange butch delete" title="批量删除"/>
    <input type="button" id="createTemplate" value="新建模板" class="button orange addTemplate" title="新建模板"/>
    
    </div>  

    
    <form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table > 
        <tr> 
            
		   <td><label>模板名称</label></td>
             <td> <input type="text" placeHolder="模板名称" name="q_sl_nameForLike" loxiaType="input" mandatory="false"></input>
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
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="页面模板 列表"></div>   
    </div>
    
    <div class="button-line">
    <input type="button" id="createTemplate" value="新建模板" class="button orange addTemplate" title="新建模板"/>
     <input type="button" id="deleteAll" value="批量删除" class="button orange butch delete" title="批量删除"/>
    
    </div>
</div> 		  

</body>
</html>
