<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>

<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>

<script type="text/javascript"
	src="${base}/scripts/member/group/member-group.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/users.png">
		<spring:message code="member.group.manage"/>
	  
        <input type="button" value="<spring:message code='member.group.unbind'/>" class="button orange unbind" title="<spring:message code='member.group.unbind'/>"/>
		<input type="button" value="<spring:message code='member.group.bind'/>" class="button orange bind" title="<spring:message code='member.group.bind'/>"/>
    
    </div>
    
    <div class="ui-block">
    <form action="/member/memberList.json" id="searchMemberForm">
    <div class="ui-block-content ui-block-content-lb">
     <table >
        <tr>
            <td><label><spring:message code="member.group.membername"/></label></td>
            <td>
                <span><input type="text" loxiaType="input" mandatory="false" placeholder="<spring:message code='member.group.loginname'/>" id="loginName" name="q_sl_loginName"></input></span>
            </td>
            
            <td><label><spring:message code="member.group.registertime"/></label></td>
            <td>
               <span><input type="text" id="startTime" name="q_date_startTime"  value="" loxiaType="date" mandatory="false"  /></span>
            </td>
            <td><label>——</label></td>
            <td>
                <span><input type="text" id="endTime" name="q_date_endTime"  value="" loxiaType="date" mandatory="false"/></span>
            </td>
            
        </tr>   
         
         <tr>
         	<td><label><spring:message code="member.group.source"/></label></td>
            <td>
				<span>
					<opt:select name="q_long_Source" id="Source" loxiaType="select" expression="chooseOption.MEMBER_SOURCE" nullOption="member.group.label.unlimit"/>
				</span>
			</td>
         
           
            
            <td><label><spring:message code="member.group.type"/></label></td>
            
             <td>
					<span>
					<opt:select name="q_long_Type" id="Type" loxiaType="select" expression="chooseOption.MEMBER_TYPE" nullOption="member.group.label.unlimit"/>
					</span>
			</td>
            
			<td><label><spring:message code="member.group.isbind"/></label></td>
            
             <td>
					<span>
					<opt:select name="q_long_isAddGroup" id="isAddGroup" loxiaType="select" expression="chooseOption.MEMBER_ISBIND" nullOption="member.group.label.unlimit"/>
					</span>
			</td>

            
         </tr>   
        
         
    </table>
     	<div class="button-line1">
	     	<input type="hidden" id="groupId" name="q_long_groupId" value="" />
	        <a href="javascript:void(0);" class="func-button searchMember" title="<spring:message code='btn.search'/>"><span><spring:message code="btn.search"/></span></a>
        </div>
    </div>
    </form>
    </div>


   

		<div class="ui-block ui-block-fleft w240">
		  <div class="ui-block-content ui-block-content-lb">
		    <form action="" id="searchGroupForm">
			    <table >
			        <tr>
			        	<td width="20%"><label><spring:message code='member.group.name'/></label></td>
			            <td>
			                <span ><input type="text" style="width:160px" id="groupName" name="q_sl_groupName" loxiaType="input" mandatory="false" style="width: 180px;" placeHolder="<spring:message code='member.group.inputgroupname'/>"></input></span>
			            </td>  
			            
			        </tr>
			        <tr>
			            <td>
			            	<label><spring:message code='member.group.type'/></label>
			            </td>
			            <td>
			                <span ><opt:select id="type" name="q_long_type" loxiaType="select" expression="chooseOption.MEMBER_GROUPTYPE" nullOption="member.group.label.unlimit"/></input></span>
			            </td>
			        </tr>
			    </table>
			    <div class="button-line1">
			    	 <a href="javascript:void(0);" class="func-button searchGroup" title="<spring:message code='btn.search'/>"><span><spring:message code="btn.search"/></span></a>
			         <a href="javascript:void(0);" class="func-button addGroup"   title="<spring:message code='btn.add'/>"><span><spring:message code="btn.add"/></span></a> 
			    </div>
			    
		    </form>
		    
		    	
			    
		    	<div class="clear-line height10"></div>
		   
		   	 	
		   	 	
		
		    </div>
		    <div class="ui-block"> 
		    	
		   		<div class="border-grey" id="table2" caption="<spring:message code='member.group.membergroup.list'/>"></div>
		   		
		   		<div class="button-line1">
		    	 <a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='user.list.filter.btn'/>"><span><spring:message code="btn.all.delete"/></span></a>
		    	</div> 
		   	</div>  
		   	
		   	 
			
		</div>

		<div class="ui-block ml240">
		    <div class="ui-block-content" style="padding-top:0;padding-right:0;">
			
			    <div class="ui-block">
			    	
			   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code='member.group.member.list'/>"></div>   
			    </div>
		
		    </div>
	   	</div>
 
 
     <div class="button-line">

        <input type="button" value="<spring:message code='member.group.bind'/>" class="button orange bind" title="<spring:message code='member.group.bind'/>"/>
		<input type="button" value="<spring:message code='member.group.unbind'/>" class="button orange unbind" title="<spring:message code='member.group.unbind'/>"/>

         
    </div>
 

</div>



</body>
</html>
