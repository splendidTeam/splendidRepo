<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="chooseoption.group.manage"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/system/option/choose-option-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>



<body>

<div class="content-box">

	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/compass.png"><spring:message code="chooseoption.group.manage"/>

			 <input type="button" value="<spring:message code='btn.return'/>" class="button orange return" title="<spring:message code='btn.return'/>"/>
	         <input type="button" value="<spring:message code='btn.add'/>"    class="button orange addchooseop" title="<spring:message code='btn.add'/>"/>
    </div>
        <div class="ui-block">
        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tr>
                    <td><label><spring:message code="chooseoption.group.code"/></label></td>
                    <td><span>${groupCode}</span></td>
                    <td><label><spring:message code="chooseoption.group.desc"/></label></td>
                    <td><span>${groupDesc}</span></td>
                </tr>
            </table>
        </div>
    </div>
    <form id="searchForm">
	<div class="ui-block">
	    <div class="ui-block-content ui-block-content-lb">
			    <table>
			        <tr>
						<td><label><spring:message code="choose.option.list.name"/></label></td>
			              <td>
		                     <span id="searchkeytext"><input type="text" name="q_sl_optionLabel" loxiaType="input" mandatory="false" placeholder="<spring:message code='choose.option.list.name'/>"></input></span>
		                  </td>
			
						<td><label><spring:message code="role.list.label.status"/></label></td>
                        <td>
			                <span>
			                 <opt:select name="q_int_lifecycle" id="lifecycle" loxiaType="select" expression="chooseOption.IS_AVAILABLE" nullOption="role.list.label.unlimit"/>
			                </span>
			            </td>
			        </tr>
	   			 </table>
	   			 <div class="button-line1">
	   			 	<input type="hidden" name="groupCode" id="groupCode" value="${groupCode}">
	   			 	<input type="hidden" id="groupDesc" name="groupDesc" value="${groupDesc}">
			        <a href="javascript:void(0);" class="func-button search"><span><spring:message code="user.list.filter.btn"/></span></a>
			    </div>
	   	</div>
	 </div>
	</form>
	<div class="ui-block">
		 <div class="border-grey" id="table1" caption="<spring:message code='choose.option.list.optionList'/>"></div>
	</div>

	<div class="button-line">
        <input type="button" value="<spring:message code='btn.add'/>" class="button orange addchooseop" title="<spring:message code='btn.add'/>"/>
        <input type="button" value="<spring:message code='btn.return'/>" class="button orange return" title="<spring:message code='btn.return'/>"/>
    </div>

</div>
</body>
</html>
