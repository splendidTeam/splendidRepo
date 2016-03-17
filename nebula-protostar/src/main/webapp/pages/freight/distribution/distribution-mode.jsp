<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base }/scripts/freight/distribution/distribution-mode.js"></script>
</head>
<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/twitter_2.png">物流方式
	 	 <input type="button"   value="批量删除" class="button butch delete"  title="批量删除"/>
         <input type="button" value="新增" class="button orange add-distribution" title="新增"/>
    </div>
 
    <div class="ui-block">
    <div id="table1" class="border-grey" caption="物流方式列表" ></div>   
    </div>
    
      <div class="button-line">
         <input type="button" value="新增" class="button orange add-distribution" title="新增"/>
      <input type="button"   value="批量删除" class="button butch delete"  title="批量删除"/>
      </div>

</div>

</body>
</html>
