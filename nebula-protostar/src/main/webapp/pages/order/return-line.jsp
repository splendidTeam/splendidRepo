<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="order.detail"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/salesorder/return-line.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>

<style type="text/css">
	.div-tab table tr th{
		height:40px;
		background:#FBF7EE;
		color:black;
		}
		
	#paymentInfo table .paymentLine {
		cursor:pointer;
	}	
	/*滚动条样式*/
	.div-scroll-bar{
		overflow-x:auto; 
		height: 350px;
	}
	.odd {
    	background-color: #F1F9FF;
	}
	.even{
		background-color:#FFFFFF;
	}

</style>

<script type="text/javascript" src="${base}/scripts/salesorder/detail.js"></script>

</head>

<body>

<div class="content-box width-percent100">
   
   <!-- 订单摘要begin -->
	<div class="ui-title1">
	<img src="../images/wmi/blacks/32x32/user.png">
	<spring:message code="order.detail"/></div>
	<div class="ui-tag-change">
		 <div class="tag-change-content" style="width: 100%;">
		 	<div  style="float:left;margin-left:10px;">
							<input type="hidden" value="${SoReturnApplication.id}" id="returnCode">
						    <div class="ui-block-line ui-block-line-37">
								<label> <spring:message code="return.list.code"/></label>
								<label >${SoReturnApplication.platformOMSCode }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="return.type"/></label>
						        	 <c:if test="${SoReturnApplication.type == 1}">
							 			 退货申请
							 		 </c:if>
									 <c:if test="${SoReturnApplication.type == 2}">
							 			换货申请
							 		 </c:if>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="order.code"/></label>
						        <label> ${SoReturnApplication.soOrderCode }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="refund.type"/></label>
							 		   <c:if test="${SoReturnApplication.refundType == 1}">
							 			COD
							 		 </c:if>
							 		   <c:if test="${SoReturnApplication.refundType == 6}">
							 			支付宝
							 		 </c:if>
							 		 <c:if test="${SoReturnApplication.refundType ==320}">
							 			银联
							 		 </c:if>
							 		 <c:if test="${SoReturnApplication.refundType == 4}">
							 			微信
							 		 </c:if>
						    </div>
						  
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.refund.account"/></label>
						      	<label>  ${SoReturnApplication.refundAccount }</label>
						    </div>						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.return.rsneeded_returninvoice"/></label>
						      	<label>   ${SoReturnApplication.isNeededReturnInvoice }
						      	</label>
						    </div>
								    						    
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="return.status"/></label>
						      	<label>   
									 <c:if test="${SoReturnApplication.status == 0}">
							 			待审核
							 		 </c:if>
									 <c:if test="${SoReturnApplication.status == 1}">
							 			 拒绝退货
							 		 </c:if>
									 <c:if test="${SoReturnApplication.status == 2}">
							 			 待发货
							 		 </c:if>
							 		  <c:if test="${SoReturnApplication.status == 3}">
							 			 已发货
							 		 </c:if>
							 		  <c:if test="${SoReturnApplication.status == 4}">
							 			 同意退款
							 		 </c:if>
							 		  <c:if test="${SoReturnApplication.status == 5}">
							 			 已完成
							 		 </c:if>
							 		  <c:if test="${SoReturnApplication.status == 6}">
							 			 取消
							 		 </c:if>
						      	</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.refund.status"/></label>
						      	<label>  
						      		<c:if test="${SoReturnApplication.refundStatus == 0}">
							 			 待处理
							 		 </c:if>
									 <c:if test="${SoReturnApplication.refundStatus == 1}">
							 			 进行中
							 		 </c:if>
									 <c:if test="${SoReturnApplication.refundStatus == 2}">
							 			 成功
							 		 </c:if>
							 		  <c:if test="${SoReturnApplication.refundStatus == 3}">
							 			 失败
							 		 </c:if>
						      	
						      	</label>
						    </div>
						      
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="order.return.price"/></label>
						      	<label style="width:600px;"><div> ￥${SoReturnApplication.returnPrice }</div>
						      	</label>
						    </div>
						    
						      <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.refund.account"/></label>
						      	<label>  ${SoReturnApplication.refundPayee }</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.refund.bank"/></label>
						      	<label>  ${SoReturnApplication.refundBank }</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.refund.bankbranch"/></label>
						      	<label>  ${SoReturnApplication.refundAccountBank }</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.return.logisticsCode"/></label>
						      	<label>  ${SoReturnApplication.transCode }</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.return.logisticsName"/></label>
						      	<label>  ${SoReturnApplication.transName }</label>
						 	 </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.platforOmsCode"/></label>
						      	<label>  ${SoReturnApplication.platformOMSCode }</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.returnAddress"/></label>
						      	<label>  ${SoReturnApplication.returnAddress }</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.approval.description"/></label>
						      	<label>  ${SoReturnApplication.approvalDescription }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.return.approver"/></label>
						      	<label>  
						      		<div>${SoReturnApplication.approver }</div>
						      	</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.approve.time"/></label>
						        <fmt:formatDate value="${SoReturnApplication.approveTime}" type="both"/>
						      	</label>
						    </div>
						    	
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="user.list.filter.createtime"/></label>
						      	<label> 						      	
						      		<fmt:formatDate value="${SoReturnApplication.createTime}" type="both"/>
						      	</label>
						    </div>					    				    
		 	</div>
		 </div>
	
	</div>
	<!-- 订单摘要end -->
	
	<!-- 退货商品清单begin -->
	<div class="ui-block-title1" style="color:black;background:white;" >退货<spring:message code="order.returnLine"/></div>
	<div class="ui-tag-change">
		 <div class="tag-change-content">
		 	<div  class="border-grey ui-loxia-simple-table" caption="<spring:message code='order.returnLine'/>">	 	
		 			<table cellspacing="0" cellpadding="0" >
		 				<tr>
			 				<th><spring:message code="order.ExtentionCode"/></th>
			 				<th><spring:message code="order.productName"/></th>
			 				<th><spring:message code="order.return.qty"/></th>			 				
			 				<th><spring:message code="order.reutrnLineId"/></th>
			 				<th><spring:message code="order.return.reason"/></th>
			 				<th><spring:message code="user.list.filter.createtime"/></th>
			 				<th><spring:message code="order.return.memo"/></th>
		 				</tr>
		 				<c:if test="${!empty SoReturnLine }">
			 				<c:forEach items="${SoReturnLine}" var="SoReturnLine" varStatus="index">
			 						<tr class="${index.count%2==0?'even':'odd' }" >
						 				<td>${SoReturnLine.rtnExtentionCode}</td>
						 				<td><a href="${frontendBaseUrl}/${SoReturnLine.itemCode}/item" target="_blank">${SoReturnLine.productName}</td>
						 				<td>${SoReturnLine.qty}</td>
						 				<td>${SoReturnLine.soLineId}</td>
						 				<td> <c:if test="${SoReturnLine.returnReason =='R001A'}">
									 			我改变主意了
									 		 </c:if>
									 		  <c:if test="${SoReturnLine.returnReason =='R002A'}">
									 			商品质量有问题
									 		 </c:if>
									 		  <c:if test="${SoReturnLine.returnReason == 'R003A'}">
									 			商品包装破损
									 		 </c:if>
									 		   <c:if test="${SoReturnLine.returnReason == 'R004A'}">
									 			尺码不合适
									 		 </c:if>
									 		   <c:if test="${SoReturnLine.returnReason == 'R005A'}">
									 			颜色/款式与商品描述不符
									 		 </c:if>
							 		</td>
							 				 
						 				<td><fmt:formatDate value="${SoReturnLine.createTime }" type="both"/></td>
						 				<td>${SoReturnLine.memo }</td>
					 				</tr>
			 				</c:forEach>
		 				</c:if>
		 				<c:if test="${empty SoReturnLine}">
		 					<tr>
			 					<td colspan="12" style="text-align: center;color:red;"><spring:message code="order.return.line.notexist"/></td>
			 				</tr>
		 				</c:if>
		 			</table>			
						   
		 	</div>
		 </div>
	
	</div>	
	
	<!-- 商品清单end -->
		
</div>
 


</body>
</html>
