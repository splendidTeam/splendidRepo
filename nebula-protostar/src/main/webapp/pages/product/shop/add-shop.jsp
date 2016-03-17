<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/shop/add-shop.js"></script>
<script type="text/javascript">
var propertys=${propertyZNodes};
var zNodes =[           
	{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
		{
			id:${industry.id}, 
			pId:${industry.pId},
			name: "${industry.indu_name}",
			open:${industry.open}
			<c:if test="${industry.noCheck eq true}">
				,nocheck:true
			</c:if>
		}
		<c:if test="${!status.last}">,</c:if>
	</c:forEach>
 ];
 	
</script>

</head>
<body>
<div class="content-box width-percent100">
   
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="shop.add.shopmanager"/></div>
             <form id="shopForm" name="userForm" action="/shop/saveShop.json" method="post">
                <div class="ui-block">
                    <div class="ui-block-title1"><spring:message code="shop.add.addshop"/></div>
                    <div class="ui-block-content border-grey">
                        <div class="ui-block-line">
                            <label><spring:message code="shop.add.shopname"/></label>
                            <div>
                                <input name="shopname"  id="shopname" type="text" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='shop.add.shopname'/>"/>
                            </div>
                        </div>

                        <div class="ui-block-line">
                            <label><spring:message code="shop.add.shopcode"/></label>
                            <div>
                                <input name="shopcode" class="fLeft"  id="shopcode"  type="text"  loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='shop.add.shopcode'/>"/>
                            </div>
                             <div id="loxiaTip-r" class="loxiaTip-r" style="display:none">
							 	  <div class="arrow"></div>
							      <div class="inner ui-corner-all" style="padding: .3em .7em; width: auto;"></div>
						     </div>
                        </div>

                        <div class="ui-block-line">
                            <label><spring:message code="shop.add.shopdescription"/></label>
                            <div>
                                <textarea name="description" cols="50" size="30" style="border:2px solid #EEE"></textarea>
                            </div>
                        </div>

                        <div class="ui-block-line">
                            <label><spring:message code="user.list.filter.state"/></label>
                            <div>
                                 <opt:select name="lifecycle" cssClass="ui-loxia-default" expression="chooseOption.IS_AVAILABLE" defaultValue="${roleCommand.lifecycle }"/>
                            </div>
                        </div>

                        <div class="ui-block-line">
                            <label><spring:message code="shop.add.onindustry"/></label>
                            <div>
                                <ul id="treeDemo" class="ztree" style="margin-top:0; width:180px;"></ul>
                            </div>
                        </div>
                    </div>
                </div>
                <input type="hidden" id="industrys" name="industrys"/>
				<input type="hidden" name="shopid" id="shopid"/>	
				<input type="hidden" name="organizationid" id="organizationid">
                </form>
    <div class="button-line">
        <input type="button" title="<spring:message code='btn.save'/>" value="<spring:message code="btn.save"/>" class="button orange submit"/>
        <input type="button" title="<spring:message code='btn.return'/>" value="<spring:message code="btn.return"/>" class="button goBackBtn goBack" />
        <input type="button" title="<spring:message code='shop.add.saveandsetshopproperty'/>" value="<spring:message code="shop.add.saveandsetshopproperty"/>"  class="button configShopProperty submit"/>
    </div>
    
<div id="dialog" class="proto-dialog">
	<h5>选择行业属性</h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block combo-list" id="props" >
		</div>
    </div>
    <div class="proto-dialog-button-line">
		<label><input id="checkAll" type="checkBox">全选</label>
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
		<input type="button" value="取消" class="button goBackBtn canel" /> 
	</div>
</div>
</div>

</body>
</html>
