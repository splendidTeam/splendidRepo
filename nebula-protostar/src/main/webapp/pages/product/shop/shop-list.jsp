<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/product/shop/shop-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="shop.manage.title"/>
         <input type="button" value="<spring:message code ='btn.all.delete'/>" class="button deleteallshop" title="<spring:message code ='btn.all.delete'/>"/>
	     <input type="button" value="<spring:message code ='btn.add'/>" class="button orange addshop" title="<spring:message code ='btn.add'/>" />
    </div>
    <form id="searchForm">
    <div class="ui-block">
        <div class="ui-block-content ui-block-content-lb">
            <table>
                <tr>
                    <td><label><spring:message code="shop.list.name"/>　</label></td>
                    <td>
                        <span id="searchkeytext"><input type="text" name="q_sl_name" loxiaType="input" mandatory="false" placeholder="<spring:message code='shop.list.name'/>"></input></span>
                    </td>
                    <td><label><spring:message code="shop.list.code"/>　</label></td>
                    <td>
                        <span id="searchkeytext"><input type="text" name="q_sl_code" loxiaType="input" mandatory="false" placeholder="<spring:message code='shop.list.code'/>"></input></span>
                    </td>
                    <td><label><spring:message code="shop.list.status"/>　</label></td>
                    <td>
                <span id="searchkeytext">
                    <opt:select name="q_int_lifecycle" otherProperties="loxiaType=\"select\" " expression="chooseOption.IS_AVAILABLE" nullOption="role.list.label.unlimit"/>
				</span>
                    </td>
                </tr>
            </table>
            <div class="button-line1">
                <a href="javascript:void(0);" class="func-button search"><span><spring:message code ='btn.search'/></span></a>
            </div>
        </div>
    </div>
    </form>
    <div class="ui-block">
   	 	<div class="border-grey" id="table1"  caption="<spring:message code="shop.list.title"/>"></div>
    </div>

	 <div class="button-line">
	     <input type="button" value="<spring:message code ='btn.add'/>" class="button orange addshop" title="<spring:message code ='btn.add'/>" />
         <input type="button" value="<spring:message code ='btn.all.delete'/>" class="button deleteallshop" title="<spring:message code ='btn.all.delete'/>"/>
    </div>

</div>
</body>
</html>