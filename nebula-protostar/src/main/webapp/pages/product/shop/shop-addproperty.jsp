<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base}/scripts/product/shop/shop-addproperty.js"></script>
</head>
<body>
<div class="content-box width-percent100">
    
   <form name="propertyForm" action="/shop/saveProperty.json" method="post">
       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="shop.property.manager"/></div>
       <div class="ui-block">
          <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
               <table>
                   <tr>
                       <td><label><spring:message code="shop.update.shopcode"/></label></td>
                       <td><span>${shopCode }</span></td>
                       <td><label><spring:message code="shop.update.shopname"/></label></td>
                       <td><span>${shopName}</span></td>
                       <td><label><spring:message code="shop.update.industryname"/></label></td>
                       <td><span>${industryName}</span></td>
                   </tr>
               </table>
           </div>
           <div class="ui-block">
           
           
           <div class="ui-block-title1"><spring:message code="shop.property.addproperty"/></div>
           <div class="ui-block-content border-grey">
           
               <div class="ui-block-line">
                   <label><spring:message code="shop.property.name"/>　</label>
                   <div>
                      <input type="text" name="name" loxiaType="input" value="" mandatory="true" selectonfocus="true"size="50"  placeHolder="<spring:message code='shop.property.name'/>"/>
		              <input type="hidden" name="industryId" value="${industryId }" />
		              <input type="hidden" name="shopId" value="${shopId }" />
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="shop.property.edit.type"/>　</label>
                   <div>
                      <opt:select name="editingType" expression="chooseOption.EDITING_TYPE" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>


               <div class="ui-block-line">
                   <label><spring:message code="shop.property.valuetype"/>　</label>
                   <div>
                      <opt:select name="valueType" expression="chooseOption.VALUE_TYPE" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="shop.property.issale"/>　</label>
                   <div>
                     <opt:select name="isSaleProp" expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="shop.property.iscolor"/>　</label>
                   <div>
                      <opt:select name="isColorProp" expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>
               <div class="ui-block-line">
                   <label><spring:message code="shop.property.isoutput.necessary"/>　</label>
                   <div>
                      <opt:select name="required" expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="shop.property.issearch"/>　</label>
                   <div>
                     <opt:select name="searchable" expression="chooseOption.TRUE_OR_FALSE" defaultValue="true" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="shop.property.isthumb"/>　</label>
                   <div>
                       <opt:select name="hasThumb" expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="shop.property.sort"/>　</label>
                   <div>
                       <input  name="sortNo" type="text" value="${sortNo }" loxiaType="number" min="0"value="" mandatory="true"/>
                   </div>
               </div>

               <div class="ui-block-line">
                   <label><spring:message code="user.list.filter.state"/>　</label>
                   <div>
                      <opt:select name="lifecycle" expression="chooseOption.IS_AVAILABLE" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
                   </div>
               </div>
           </div>
           </div>
       </div>
      </form>
       <div class="button-line">
         <input type="button" title="<spring:message code='btn.save'/>" value="<spring:message code='btn.save'/>" class="button orange submit" />
         <input type="button" title="<spring:message code='btn.return'/>" value="<spring:message code='btn.return'/>" class="button goback" />
       </div>
  
    

</div>
</body>
</html>
