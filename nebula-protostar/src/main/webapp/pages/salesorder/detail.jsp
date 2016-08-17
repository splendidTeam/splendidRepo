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
<script type="text/javascript" src="${base}/scripts/salesorder/list.js"></script>
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
							<input type="hidden" value="${ orderCommand.salesOrderCommand.id}" id="orderId">
						    <div class="ui-block-line ui-block-line-37">
								<label> <spring:message code="order.detail.orderCode"/></label>
								<label style="width:600px;">${orderCommand.salesOrderCommand.code }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="order.detail.OMSOrderCode"/></label>
						        <label style="width:600px;">${orderCommand.salesOrderCommand.omsCode }</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="order.detail.memberName"/></label>
						        <label> ${orderCommand.memberName }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="order.detail.guestIdentify"/></label>
						      <label style="width: 200px;">  ${orderCommand.salesOrderCommand.guestIdentify }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="order.detail.productTotalQuantity"/></label>
						       	<label>  ${orderCommand.salesOrderCommand.quantity }件</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.orderAmount"/></label>
						      	<label>  ￥ ${orderCommand.salesOrderCommand.total+orderCommand.salesOrderCommand.actualFreight }<spring:message code="order.detail.unit"/></label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.totalAmount"/></label>
						      	<label>  ￥ ${orderCommand.salesOrderCommand.total }<spring:message code="order.detail.unit"/></label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.discount"/></label>
						      	<label>  ￥ ${orderCommand.salesOrderCommand.discount }<spring:message code="order.detail.unit"/></label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.logisticStatus"/></label>
						      	<label>   ${orderCommand.logisticsInfo }
						      			
						      	</label>
						    </div>
								    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.financialStatus"/></label>
						      	<label> 
						      	${orderCommand.financialStatusInfo }  
						    					      	
						      	</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="order.detail.payType"/></label>
						      	<label>   
									<%-- 						      		
									<c:forEach items="${ orderCommand.payTypeMap}" var="map">
									 		<c:if test="${map.key==orderCommand.salesOrderCommand.payment }">
									 			${ map.value}
									 		</c:if>
									 </c:forEach> 
									 --%>
									 <c:if test="${orderCommand.salesOrderCommand.payment == '1'}">
							 			 货到付款
							 		 </c:if>
									 <c:if test="${orderCommand.salesOrderCommand.payment == '3'}">
							 			 网银在线
							 		 </c:if>
									 <c:if test="${orderCommand.salesOrderCommand.payment == '6'}">
							 			 支付宝
							 		 </c:if>
						      	</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.orderSource"/></label>
						      	<label>   ${orderCommand.orderSource }</label>
						    </div>
						      
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.IP"/></label>
						      	<label>   ${orderCommand.salesOrderCommand.ip }</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.freight"/></label>
						      	<label>  
						      		<div><spring:message code="order.detail.payableFreight"/>: ${orderCommand.salesOrderCommand.payableFreight }<spring:message code="order.detail.unit"/></div></br>
						      		<div><spring:message code="order.detail.paidFreight"/>: ${orderCommand.salesOrderCommand.actualFreight }<spring:message code="order.detail.unit"/></div>
						      	</label>
						    </div>
						     
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.logisticsInfo"/></label>
						      	<label> <div> ${orderCommand.salesOrderCommand.logisticsProviderName }</div>
						      			</br>
						      		<div> ${orderCommand.salesOrderCommand.transCode }</div>
						      	</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="order.detail.receiptInfo"/></label>
						      	<label style="width:600px;">
						      		<div><spring:message code="order.detail.receiptCode"/> : ${orderCommand.salesOrderCommand.receiptCode }</div></br>
						      		<div><spring:message code="order.detail.receiptType"/> : ${orderCommand.receiptType }</div></br>
						      		<div><spring:message code="order.detail.receiptTitle"/> : ${orderCommand.salesOrderCommand.receiptTitle }</div></br>
						      		<div><spring:message code="order.detail.receiptContent"/> : ${orderCommand.salesOrderCommand.receiptContent }</div>
						      	</label>
						    </div>
						    
						      <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.remark"/></label>
						      	<label>  ${orderCommand.salesOrderCommand.remark }</label>
						    </div>
						      <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="user.list.filter.createtime"/></label>
						      	<label style="width:600px;"> 						      	
						      		<fmt:formatDate value="${orderCommand.salesOrderCommand.createTime}" type="both"/>
						      	</label>
						    </div>
						    
						    
		 	</div>
		 </div>
	
	</div>
	<!-- 订单摘要end -->
	
	<!-- 商品清单begin -->
	<div class="ui-block-title1" style="color:black;background:white;" ><spring:message code="order.detail.productList"/></div>
	<div class="ui-tag-change">
		 <div class="tag-change-content">
		 	<div  class="border-grey ui-loxia-simple-table" caption="<spring:message code='order.detail.productList'/>">	 	
		 			<table cellspacing="0" cellpadding="0" >
		 				<tr>
			 				<th><spring:message code="order.detail.UPC"/></th>
			 				<th><spring:message code="order.detail.prodcutImage"/></th>			 				
			 				<th><spring:message code="order.detail.productName"/></th>
			 				<th><spring:message code="order.detail.productProperty"/></th>
			 				<th><spring:message code="order.detail.productQuantity"/></th>
			 				<th><spring:message code="order.detail.suggestPrice"/></th>
			 				<th><spring:message code="order.detail.originalPrice"/></th>
			 				<th><spring:message code="order.detail.nowPrice"/></th>
			 				<th><spring:message code="order.detail.discount"/></th>	
			 				<th><spring:message code="order.detail.lineTotalAmount"/></th> 
			 				<th><spring:message code="order.detail.lineType"/></th>
			 				<th><spring:message code="order.detail.evaluationStatus"/></th>
		 				</tr>
		 				<c:if test="${!empty orderCommand.salesOrderCommand.orderLines }">
			 				<c:forEach items="${orderCommand.salesOrderCommand.orderLines }" var="orderLine" varStatus="index">
			 						<tr class="${index.count%2==0?'even':'odd' }" >
						 				<td>${orderLine.extentionCode }</td>
						 				<td>
						 					<a href="${frontendBaseUrl}/item/${orderLine.productCode}" target="_blank">
							 					<img alt="" src="<url:img size="${ smallSize }" imgUrl="${ customBaseUrl }${ orderLine.itemPic }" />">
									 		</a>
									 	</td>
						 				<td><a href="${frontendBaseUrl}/item/${orderLine.productCode}" target="_blank">${orderLine.itemName }</a></td>			 				
						 				<td>${orderLine.saleProperty }</td>
						 				<td>${orderLine.count }</td>
						 				<td>${orderLine.MSRP }</td>
						 				<td>${orderLine.MSRP }</td>
						 				<td>${orderLine.salePrice }</td>
						 				<td>${orderLine.discount }</td> 
						 				<td>${orderLine.subtotal }</td>
						 				<td>
						 					<c:forEach items="${ orderCommand.orderLineTypeMap}" var="lineTypemap">
									 			<c:if test="${lineTypemap.key==orderLine.type }">
									 				${ lineTypemap.value}
									 			</c:if>
									 		</c:forEach>
						 				</td>
						 				<td> 
							 				<c:forEach items="${ orderCommand.orderLineEvaluationMap}" var="evaluationMap">
									 			<c:if test="${evaluationMap.key==orderLine.evaluationStatus }">
									 				${ evaluationMap.value}
									 			</c:if>
									 		</c:forEach>
							 				
						 				</td>
					 				</tr>
			 				</c:forEach>
		 				</c:if>
		 				<c:if test="${empty orderCommand.salesOrderCommand.orderLines }">
		 					<tr>
			 					<td colspan="12" style="text-align: center;color:red;"><spring:message code="order.detail.noProduct"/></td>
			 				</tr>
		 				</c:if>
		 			</table>			
						   
		 	</div>
		 </div>
	
	</div>	
	
	<!-- 商品清单end -->
		
	<!-- tab开始 -->
	<div class="ui-tag-change">
               <ul class="tag-change-ul">
                 
                   <li class="receivePersoninfo"><spring:message code="order.detail.consigneeInfo"/></li>
                   <li class="paymentDetail"><spring:message code="order.detail.paymentDetail"/></li>
                   <!--  li class="paymentNo">支付流水</li>-->
                   <li class="orderStatusUpdateLog"><spring:message code="order.detail.orderStatusUpdateLog"/></li>
                   <li class="orderHistory"><spring:message code="order.detail.orderHistory"/></li>
                   <li class="promotionOrder"><spring:message code="order.detail.promotionOrder"/></li>
                   <li class="logisticsHistoryInfo"><spring:message code="order.detail.logisticsHistoryInfo"/></li>
               </ul>
              
               <div class="tag-change-content">
               
                    <!-- 收货信息begin -->
                    <div class="tag-change-in"  >
							<div class="ui-block div-scroll-bar" style="">
						    <div class="ui-block-title1"><spring:message code="order.detail.consigneeInfo"/></div>
						    						    				    
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="order.detail.consigneeName"/></label>
						        <label> ${orderCommand.salesOrderCommand.name }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="order.detail.address"/></label>
						      <label>   ${orderCommand.salesOrderCommand.address }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="order.detail.mobile"/></label>
						       	<label>  ${orderCommand.salesOrderCommand.mobile }</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.telephone"/></label>
						      	<label>  ${orderCommand.salesOrderCommand.tel }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.email"/></label>
						      	<label>  ${orderCommand.salesOrderCommand.email }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.appointReceive"/></label>
						      	<label style="width:600px;">		
						      				<!-- 如果指定了类型为双休日或者工作日就不需要指定时间段和日期了 -->
						      				<!--  div>时间段 : 8:00-20:00</div></br>
						      				<div>日期 : 2013/11/20</div></br>-->
						      				<div>						      			
						      				<c:if test="${!empty orderCommand.salesOrderCommand.appointType }">
						      					<spring:message code="order.detail.appointType"/> : ${orderCommand.appointReceive }
						      				</c:if>
						      				<c:if test="${empty orderCommand.salesOrderCommand.appointType }">
						      					<c:if test="${!empty orderCommand.salesOrderCommand.appointTime }">
						      						<spring:message code="order.detail.appointTime"/> :  ${ orderCommand.appointReceive}
						      					</c:if>
						      					<c:if test="${empty orderCommand.salesOrderCommand.appointTime }">
						      						<spring:message code="order.detail.appointTimeQuantum"/>:  ${ orderCommand.appointReceive}
						      					</c:if>
						      				 </c:if>
						      				 
						      				</div>
						      	</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.country"/></label>
						      	<label>   ${orderCommand.salesOrderCommand.country }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.province"/></label>
						      	<label>  ${orderCommand.salesOrderCommand.province }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.city"/></label>
						      	<label>   ${orderCommand.salesOrderCommand.city }</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="order.detail.area"/></label>
						      	<label>  ${orderCommand.salesOrderCommand.area }</label>
						    </div>

							</div>
                    </div>
                    <!-- 收货信息end -->
                    
                    <!-- 支付详细开始  -->
                     <div class="tag-change-in"  >
							<div class="ui-block div-scroll-bar" style="">
						    <div class="ui-block-title1"><spring:message code="order.detail.paymentDetail"/></div>
						    
							<div id="paymentInfo"  class="border-grey  ui-loxia-simple-table div-tab">	
								<div style="width:100%">
						 			<table  cellspacing="0" cellpadding="0">
						 				<tr>
						 					<th><spring:message code="order.detail.index"/></th>
							 				<th><spring:message code="order.detail.payValue"/></th>
							 				<th><spring:message code="order.detail.payAmount"/></th>			 				
							 				<th><spring:message code="order.detail.paidType"/></th>
							 				<th><spring:message code="order.detail.paidDetail"/></th>
							 				<th><spring:message code="order.detail.sysPayNo"/></th>
							 				<th><spring:message code="order.detail.otherPayNo"/></th>
							 				<th><spring:message code="order.detail.otherPayNumber"/></th>
						 				</tr>
						 				<c:if test="${!empty orderCommand.salesOrderCommand.payInfo}">
						 					<c:forEach items="${orderCommand.salesOrderCommand.payInfo}" var="payInfo" varStatus="index">						 					
						 						<tr class="paymentLine ${index.count%2==0?'even':'odd' }">
								 					<td>${ index.count}<input type="hidden" value="${payInfo.id }"></td>
									 				<td>${payInfo.payNumerical }</td>
									 				<td>${payInfo.payMoney }</td>
									 				<td>
									 					<%-- 	
									 					<c:forEach items="${ orderCommand.payTypeMap}" var="map">
									 						<c:if test="${map.key==payInfo.payType }">
									 						${ map.value}
									 						</c:if>
									 					</c:forEach> 
									 					--%>
									 					<c:if test="${orderCommand.salesOrderCommand.payment == '1'}">
												 			 货到付款
												 		 </c:if>
														 <c:if test="${orderCommand.salesOrderCommand.payment == '3'}">
												 			 网银在线
												 		 </c:if>
														 <c:if test="${orderCommand.salesOrderCommand.payment == '6'}">
												 			 支付宝
												 		 </c:if>
									 				</td>			 				
									 				<td>${payInfo.payInfo }</td>
									 				<td>${payInfo.subOrdinate }</td>	
									 				<td>${payInfo.thirdPayNo }</td>
									 				<td>${payInfo.thirdPayAccount }</td>
								 				</tr>
						 					
						 					</c:forEach>
						 				</c:if>
						 				
						 					<c:if test="${empty orderCommand.salesOrderCommand.payInfo }">
						 						<tr>
								 					<td colspan="7" style="text-align: center;color:red;"><spring:message code="order.detail.NotFoundData"/></td>
								 				</tr>
						 				</c:if>
						 			</table>	
					 			</div>		
									   
					 		</div>
						</div>	
                    </div>
                    
                   <!-- 支付详细end -->
                   
                   
                    <!-- 支付流水begin -->
                     <!--  div class="tag-change-in"  >
							<div class="ui-block" style="">
						    <div class="ui-block-title1">支付流水</div>
						    
						    
						    <div  class="border-grey  ui-loxia-simple-table div-tab">	
						    	<div style="width:50%">
						 			<table  cellspacing="0" cellpadding="0">
						 				<tr>
						 					<th>序号</th>
							 				<th>支付流水号</th>
							 				<th>支付详细ID</th>			 				
							 				<th>创建时间</th>
						 				</tr>
						 				<tr>
						 					<td>1</td>
							 				<td>xxxy11dadasdz01</td>
							 				<td>222</td>
							 				<td>2013-11-20</td>			 				
						 				</tr>
						 				<tr>
						 					<td>2</td>
							 				<td>xxxy11dadasdz02</td>
							 				<td>3333</td>
							 				<td>2013-11-21</td>
							 			</tr>
						 			</table>
					 			</div>			
									   
					 		</div>
						
						</div>
                    </div>
                    -->
                   <!-- 支付流水end -->
                   
                   
                   <!-- 订单状态变更日志begin -->
                     <div class="tag-change-in"  >
							<div class="ui-block div-scroll-bar" style="">
						    <div class="ui-block-title1"><spring:message code="order.detail.orderStatusUpdateLog"/></div>
						    
						    
						    <div  class="border-grey  ui-loxia-simple-table div-tab">	
						    	<div style="width:100%">
						 			<table  cellspacing="0" cellpadding="0">
						 				<tr>
						 					<th><spring:message code="order.detail.index"/></th>
							 				<th><spring:message code="order.detail.updateBeforeStatus"/></th>
							 				<th><spring:message code="order.detail.updateAfterStatus"/></th>			 				
							 				<th><spring:message code="order.detail.time"/></th>
							 				<th><spring:message code="order.detail.operatorName"/></th>
						 				</tr>
						 				
						 				
						 				<c:if test="${!empty orderCommand.orderStatusLogs }">
						 					<c:forEach items="${orderCommand.orderStatusLogs }" var="orderStatusLog" varStatus="index">
								 				<tr class="${index.count%2==0?'even':'odd' }">
								 					<td>${index.count }</td>
								 					<td>
									 					<c:forEach items="${ orderCommand.statusMap}" var="map">
									 						<c:if test="${map.key==orderStatusLog.beforeStatus }">
									 						${ map.value}
									 						</c:if>
									 					</c:forEach>
								 					</td>
								 					<td>
									 					<c:forEach items="${ orderCommand.statusMap}" var="map">
									 						<c:if test="${map.key==orderStatusLog.afterStatus }">
									 						${ map.value}
									 						</c:if>
									 					</c:forEach>
								 					</td>
								 					
									 				<td><fmt:formatDate value="${orderStatusLog.createTime}" type="both"/></td>
									 				<td>${orderStatusLog.operatorName }</td>
									 					 				
								 				</tr>
							 				</c:forEach>
						 				</c:if>
						 				<c:if test="${empty orderCommand.orderStatusLogs }">
						 						<tr>
								 					<td colspan="5" style="text-align: center;color:red;"><spring:message code="order.detail.NotFoundData"/></td>
								 				</tr>
						 				</c:if>
						 				
						 			</table>
					 			</div>			
									   
					 		</div>
					 		
						</div>
                    </div>
                    
                   <!-- 订单状态变更日志end -->
                   
                   <!-- 订单历史记录begin -->
                     <div class="tag-change-in"  >
							<div class="ui-block div-scroll-bar" style="">
						    <div class="ui-block-title1"><spring:message code="order.detail.orderHistory"/></div>
						    
						    
						    <div id="table1" class="border-grey  ui-loxia-simple-table div-tab" >	
						    	<div style="width:50%">
						 			<table  cellspacing="0" cellpadding="0">
						 				<tr>
						 					<th><spring:message code="order.detail.index"/></th>
							 				<th><spring:message code="order.detail.time"/></th>
							 				<th><spring:message code="order.detail.operatorName"/></th>			 				
							 				<th><spring:message code="order.detail.dataObj"/></th>
						 				</tr>
						 				<c:if test="${!empty orderCommand.orderLogs }">
						 					<c:forEach items="${orderCommand.orderLogs }" var="orderLog" varStatus="index">
								 				<tr class="${index.count%2==0?'even':'odd' }">
								 					<td>${index.count }</td>
									 				<td><fmt:formatDate value="${orderLog.createTime}" type="both"/></td>
									 				<td>${orderLog.operatorName }</td>
									 				<td>${orderLog.content}</td>			 				
								 				</tr>
							 				</c:forEach>
						 				</c:if>
						 				<c:if test="${empty orderCommand.orderLogs }">
						 					<tr>
								 				<td colspan="4" style="text-align: center;color:red;"><spring:message code="order.detail.NotFoundData"/></td>
								 			</tr>
						 				</c:if>
						 			</table>			
								</div>		   
					 		</div>
					 		
					 		
					 	</div>
                    </div>
                    
                   <!-- 订单历史记录end -->
                   
                   
                   <!-- 订单促销begin -->
                     <div class="tag-change-in"  >
							<div class="ui-block div-scroll-bar" style="">
						    <div class="ui-block-title1"><spring:message code="order.detail.promotionOrder"/></div> 
							    <div id="table1" class="border-grey  ui-loxia-simple-table div-tab" >	
						    	<div style="width:50%">
						 			<table  cellspacing="0" cellpadding="0">
						 				<tr> 
							 			<th><spring:message code="order.detail.promotionCode"/></th>
								        <th><spring:message code="order.detail.promotionType"/></th>
								        <th><spring:message code="order.detail.promotionAmonut"/></th>
								        <th><spring:message code="order.detail.promotionCoupone"/></th>
								        <th><spring:message code="order.detail.promotionDescription"/></th>
						 				</tr>
						 				<c:if test="${ !empty orderCommand.salesOrderCommand && !empty  orderCommand.salesOrderCommand.orderPromotions}">
						 					 <c:forEach items="${orderCommand.salesOrderCommand.orderPromotions }" var="opromotion">
										       	<tr class="${index.count%2==0?'even':'odd' }">
										        <td>${opromotion.promotionNo }</td>
										        <td>${opromotion.promotionType }</td>
										        <td>${opromotion.discountAmount }</td>
										        <td>${opromotion.coupon }</td>
										        <td>${opromotion.describe }</td> 
										        </tr>
										      </c:forEach> 
						 				</c:if>
						 				<c:if test="${ empty orderCommand.salesOrderCommand || empty  orderCommand.salesOrderCommand.orderPromotions}">
						 					<tr>
								 				<td colspan="5" style="text-align: center;color:red;"><spring:message code="order.detail.NotFoundData"/></td>
								 			</tr>
						 				</c:if>
						 			</table>			
								</div>		   
					 		</div> 			
						</div>
                    </div>
                    
                   <!-- 订单促销end -->
                   
                   
                   <!-- 订单物流信息begin -->
                     <div class="tag-change-in"  >
							<div class="ui-block div-scroll-bar" style="" id="logisticsInfo">
						    	
						    	
								<!-- 动态加载 -->
							</div>
                    </div>
                    
                   <!-- 订单物流信息end -->
                    
               </div>
          </div>
	
</div>


          
          
   <!-- 支付流水详细隐藏开始 -->
	<div id="payment-dialog" class="proto-dialog">
		<h5>支付流水</h5>
		  
		 <div class="proto-dialog-content">
		 	<div class="ui-block"> 
		 	    <div class="ui-block-content payment-dialogInfo" id="payment-dialogInfo">
		 	   
		 	    
		 	         <div  class="border-grey  ui-loxia-simple-table div-tab" >	
						    	<div style="width:100%">
						 			<table  cellspacing="0" cellpadding="0" id="payment-dialogInfo-table">
						 				<tr>
						 					<th><spring:message code="order.detail.index"/></th>
							 				<th><spring:message code="order.detail.PayNo"/></th>
							 				<th><spring:message code="order.detail.paidDetailID"/></th>			 				
							 				<th><spring:message code="user.list.filter.createtime"/></th>
						 				</tr>
						 			</table>
					 			</div>
					 </div>
					 
		 	  </div>
	
		 	  
		 </div>
		 
		</div>
		
		
		<div class="proto-dialog-button-line">
			<input type="button" value="<spring:message code="btn.close"/>" class="button orange close"/>
		</div>
		 
	</div>
	<!-- 支付流水详细数结束 -->


</body>
</html>
