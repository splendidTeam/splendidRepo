<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>


<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript">
var zNodes =
[
	{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
	<c:if test="${industry.isShow}">
		{
			id:${industry.id}, 
			pId:${industry.pId},
			name: "${industry.indu_name}",
			open:${industry.open}
			<c:if test="${industry.noCheck}">
				,nocheck:true
			</c:if>
		}
		<c:if test="${!status.last}">,</c:if>
		</c:if>
	</c:forEach>
];
     	
</script>
<script type="text/javascript" src="${base}/scripts/product/shop/shop-manager.js"></script>
</head>
<body>
<div class="content-box width-percent100">

    <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="shop.add.shopmanager"/></div>
    <div class="ui-block">
        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tr>
                    <td><label><spring:message code="shop.add.shopcode"/></label></td>
                    <td><span>${orgInfo.code}</span></td>
                    <td><label><spring:message code="shop.add.shopname"/></label></td>
                    <td><span>${orgInfo.name}</span></td>
                </tr>
            </table>
        </div>
    </div>
    <div class="ui-tag-change">
        <ul class="tag-change-ul">
            <li id="updateShop" ><spring:message code="shop.property.updateshop"/></li>
            <li class="selected" id="updateProp"><spring:message code="shop.update.setshopproperty"/></li>
        </ul>

        <div class="tag-change-content">
            <div class="tag-change-in" ></div>
            <div class="tag-change-in block" >
                <div class="ui-block ui-block-fleft w240">
                    <div class="ui-block-content ui-block-content-lb">
                        <div class="tree-control">
                            <input type="text" id="key" loxiatype="input" placeholder="<spring:message code='shop.property.keyword'/>" />
                        </div>
                        <ul id="tree" class="ztree"></ul>

                    </div>
                </div>
                <div class="ui-block ml240" style="padding-left: 10px;">
                    <div class="ui-block-content" style="padding-top: 0;">
                        <div class="border-grey ui-loxia-simple-table" id="table1" caption="_"></div>
                    </div>
                    <div class="button-line">
                        <input type="hidden" value="${shop.organizationid}" class="orgId">
			            <input type="hidden" value="${shop.shopname }" class="shopName">
			            <input type="hidden" value="${shop.shopid}" class="shopId">
			            <!-- 暂时先取消 -->
                        <input style="display:none" type="button" title="<spring:message code='btn.add'/>" value="<spring:message code='btn.add'/>" class="button orange addProperty">
                         <input type="button"title="<spring:message code="btn.return"/>"  value="<spring:message code="btn.return"/>" class="button goBackBtn"  />  
                    </div>
                </div>
            </div>
        </div>
</div>
</body>
</html>
