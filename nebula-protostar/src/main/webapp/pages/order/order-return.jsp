<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/salesorder/order-return.js?${version_js}"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js?${version_js}"></script>


</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/calc.png">退换货审核
			<input type="hidden" id="errorMsg" value="${errorMsg}" />
		</div>
	   <div class="ui-block">
	   	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/users.png"><spring:message code="order.list"/>
	    <!--     <input type="button" value="手工退换货" title="手工下单"  class="button orange manualCreate" />  -->
    </div>
	    <form id="searchForm" action="/saleOrder/returnOrderList.json" >
		    <div class="ui-block-content ui-block-content-lb">
		        <table>
		            <tr>
		                <td><label><spring:message code="return.list.code"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_returnCode" loxiaType="input" mandatory="false" placeholder="<spring:message code="return.list.code"/>"></input>
		                    </span>
		                </td>
		                <td><label><spring:message code="order.code"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_oderCode" loxiaType="input" mandatory="false" placeholder="<spring:message code="order.code"/>"></input>
		                    </span>
		                </td>
		             
		                <td><label><spring:message code="order.return.logisticsCode"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_LogCode" loxiaType="input" mandatory="false" placeholder="<spring:message code="order.return.logisticsCode"/>"></input>
		                    </span>
		                </td>
		        	</tr>
		        	<tr>
						 
		                 <td><label><spring:message code="order.return.logisticsName"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_logisticsName" loxiaType="input" mandatory="false" placeholder="<spring:message code="order.return.logisticsName"/>"></input>
		                    </span>
		                </td>
		                  <td><label><spring:message code="user.list.filter.createtime"/></label></td>
			            <td>
			               <span><input type="text" id="startDate" name="q_date_startDate"  value="" loxiaType="date" mandatory="false" /></span>
			            </td>
			            <td><label>——</label></td>
			            <td>
			                <span><input type="text" id="endDate" name="q_date_endDate"  value="" loxiaType="date" mandatory="false"/></span>
			            </td>
		        	</tr>
			        <tr>
			        <td><label><spring:message code="return.status"/></label></td>
		                <td>
							<span>
							   <select loxiatype="select" name="q_long_status" id="status" >
							           <option value="">不限</option>
						               <option value=1>待审核</option>
						               <option value=2>拒绝</option>
						               <option value=3>退回中</option>
						              <!--  <option value=4>已退回</option> -->
						               <option value=5>同意退换货</option>
						                <option value=6>已完成</option>						               
						               
						        </select>
							</span>
						</td>
		                </td>
						 <td><label><spring:message code="return.login.name"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_loginName" loxiaType="input" mandatory="false" placeholder="<spring:message code="return.login.name"/>"></input>
		                    </span>
		                </td>
						
						</tr>
		        </table>
		        <div class="button-line1">
                	<a href="javascript:void(0);" class="func-button search"><span><spring:message code ='btn.search'/></span></a>
                	 <a href="javascript:void(0);" class="func-button exportVip"
							title="导出">导出</a>
						<tr>
							<td><label class="red">导出退货单(最多只能导出一个月数据)</label></td>
						</tr>
            	</div> 	
		    </div>
		  </form>
	    </div>
    <div class="ui-block">
   	 	<div class="border-grey" id="table1" caption="<spring:message code="orderReturn.list"/>"></div>
    </div>
    
    
      <!--   退货审核 -->

      <div id="active-dialog" class="proto-dialog">
    <h5><spring:message code="order.return.manage"/></h5>
    	<div class="proto-dialog-content">
		 </div>
			<div class="ui-block-line">
				<form id="exaimCode" >
					<div class="ui-block-content ui-block-content-lb">
						<table>
							<tr>
								<td><label>退换货编号:</label></td>
								<td><span id="searchkeytext">
								 <input type="text" name="q_sl_returnCode" id="returnCode" loxiaType="input" style="border-style:none" mandatory="false"  readonly="readonly"></label>
								</span>
							</tr>
								<tr>
								<td><label>退换货类型:</label></td>
								<td><span id="searchkeytext">
								 <input type="text" name="q_sl_returnType" id="returnType" loxiaType="input" style="border-style:none" mandatory="false"  readonly="readonly"></label>
								</span>
							</tr>
							<tr>
								<td><label>*OMS退换货编号:</label></td>
								<td><span id="searchkeytext">
								<input type="text" name="q_sl_omsCode" id="omsCode" loxiaType="input" mandatory="false" ></input>
							</tr>
							
							<tr>
								<td><label>*退货地址:</label></td>
								<td><span>	
									<textarea rows="10px"  style="width: 330px; height: 50px;resize:none;"  name="q_sl_returnAddress" id="returnAddress" maxlength="100" class="ui-loxia-default ui-corner-all" ></textarea>
								</span></td>
							</tr>
							
							<tr>
								<td><label>*审核备注:</label></td>
								<td><span>	
									<textarea rows="10px"  style="width: 330px; height: 100px;resize:none;"  name="q_sl_remark" id="remark" maxlength="100" class="ui-loxia-default ui-corner-all" ></textarea>
								</span></td>
							</tr>
							
						</table>
						<div class="button-line" style="text-align: center;">
						     <input type="button" value="<spring:message code='return.confirm'/>" class="button orange submit" title="<spring:message code='return.confirm'/>" 
						     id="activeOkBtn" />
						     <input type="button" value="<spring:message code='return.cancel'/>" class="button submit" title="<spring:message code='return.cancel'/>" 
						     id="active-dialog-cancel-btn" />
						</div>
					</div>
				</form>
			</div>
    
    
</div>  
    
    
<!--    弹框 拒绝退款 -->
    <div id="refuse-dialog" class="proto-dialog">
    <h5><spring:message code="order.return.manage"/></h5>
    	<div class="proto-dialog-content">
		 </div>
			<div class="ui-block-line">
				<form id="exaimCode" >
					<div class="ui-block-content ui-block-content-lb">
						<table>
							<tr>
								<td><label>退换货单编号:</label></td>
								<td><span id="searchkeytext"> <input
										name="q_sl_orderCode" id="orderCode" loxiaType="input" style="border-style:none"
										mandatory="false"  ></label>
								</span>
							</tr>
							<tr>
								<td><label>*备注:</label></td>
								<td><span>	
									<textarea style="width: 330px; height: 110px;resize:none;" name="q_sl_memo" id="memo" maxlength="100" class="ui-loxia-default ui-corner-all" ></textarea>
								</span></td>
							</tr>
							
						</table>
						<div class="button-line" style="text-align: center;">
						     <input type="button" value="<spring:message code='return.confirm'/>" class="button orange submit" title="<spring:message code='return.confirm'/>"
						      id="activeCancleBtn" />
						     <input type="button" value="<spring:message code='return.cancel'/>" class="button submit" title="<spring:message code='return.cancel'/>"
						      id="refuse-dialog-cancel-btn" />
						</div>
					</div>
				</form>
			</div>
</div>

</body>
</html>
