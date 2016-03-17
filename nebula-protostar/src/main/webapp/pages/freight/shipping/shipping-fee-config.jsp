<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="item.update.manage"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/freight/shipping/shipping-fee-config.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript">
	var templateId = '<c:out value="${shippingTemplate.id }" escapeXml="" default=""></c:out>';

</script>

</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cur_yen.png">运费表
	 	 <input type="button" value="返回" class="button return-back" />
         <input type="button" value="导入运费" class="button orange" 
         onclick="javascript:location.href='/freight/openShippingFeeConfig.htm?templateId=${shippingTemplate.id }'" />
    </div>
 
  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tr>
                    <td><label>模板名称</label></td>
                    <td><span>${shippingTemplate.name }</span></td>
                    <td><label>计价类型</label></td>
                    <td><span>
                    <c:choose> 
           			 <c:when test="${shippingTemplate.calculationType eq 'base'}">
					基础
           			 </c:when>
           			 <c:when test="${shippingTemplate.calculationType eq 'unit'}">
					计件
           			 </c:when>
           			 <c:when test="${shippingTemplate.calculationType eq 'weight'}">
					计重
           			 </c:when>
           			 <c:otherwise>
					基础
           			 </c:otherwise>
           			 </c:choose> 
                    </span></td>
   
                </tr>
            </table>
        </div>
    </div>
 
    <div class="ui-block">
    <input type="hidden" value="${shippingTemplate.id }" id="distributionModeId"/>
    <div id="table1" class="border-grey" caption="运费表" ></div>   
    </div>
    
    <div class="button-line">
         <input type="button" value="导入运费" class="button orange" 
         onclick="javascript:location.href='/freight/openShippingFeeConfig.htm?templateId=${shippingTemplate.id }'" />
    <input type="button" value="返回" class="button return-back" />
    </div>

</div>

</body>
</html>
