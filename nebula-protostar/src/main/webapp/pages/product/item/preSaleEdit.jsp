<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript">
	var paymentMethod='${itemPresalseInfoCommand.paymentMethod==null?'':itemPresalseInfoCommand.paymentMethod}';
</script>
<script type="text/javascript" src="${base}/scripts/product/item/item-presaleInfo.js"></script>
</head>
<body>
	<div class="content-box width-percent100">
	   <form:form id="itemProsaleInfoForm" modelAttribute="itemPresalseInfoCommand">
	   <input type="hidden" name="itemId" value="${itemPresalseInfoCommand.itemId }"/>
	   <input type="hidden" name="itemCode" value="${itemPresalseInfoCommand.itemCode }"/>
	   <input type="hidden" name="id" value="${itemPresalseInfoCommand.id }"/>
	    <input type="hidden" name="lifecycle" value="${itemPresalseInfoCommand.lifecycle==null?1:itemPresalseInfoCommand.lifecycle }" id="lifecyclehidden"/>
			<div class="ui-title1"><img src="/images/wmi/blacks/32x32/cube.png">预售信息管理</div>
			<div id="">
				<div class="ui-block"> 
			        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
				    	<table>
				        	<tbody><tr>
				                <td><label>商品编码</label></td>
				                <td><span>${itemPresalseInfoCommand.itemCode }</span></td>
				                <td><label>商品名称</label></td>
				                <td><span>${itemPresalseInfoCommand.itemName }</span></td>
				            </tr>
				        </tbody></table>
			        </div>
			    </div>
		        <div class="ui-block ">	
				   <div class="ui-block-title1">预售活动信息</div>		   
				</div>
		        <div class="ui-block-title1" style="background:#fff;color:#000;">预售基本信息</div>
		        <div id="exten" class="ui-block-content border-grey">
		          	<div id="extension">
						<table id="" class="" style="padding: 5px;">
							<tbody>
								<tr class="ui-block-line ">
									<td style="width: 150px">活动名称</td>
									<td style="width: 150px">
										<input type="text" name="activityName" id="activityName" class="dynamicInputNameListPrices ui-loxia-default ui-corner-all ui-loxia-highlight" 
										value="<c:out value="${itemPresalseInfoCommand.activityName }"></c:out>"/>
									</td>
								</tr>
								<tr class="ui-block-line ">
									<td style="width: 150px">开始时间</td>
									<td style="width: 150px">
										<input type="text" readonly="readonly" name="activityStartTimeStr" id="activityStartTimeStr" timeinput="true" class="dynamicInputNameListPrices ui-loxia-default ui-corner-all ui-loxia-highlight" 
										value="<fmt:formatDate value="${itemPresalseInfoCommand.activityStartTime }" pattern="yyyy-MM-dd HH:mm" />"></input>
									</td>
								</tr>
								<tr class="ui-block-line ">
									<td style="width: 150px">结束时间</td>
									<td style="width: 150px">
										<input type="text" readonly="readonly" name="activityEndTimeStr" id="activityEndTimeStr" timeinput="true" class="dynamicInputNameListPrices ui-loxia-default ui-corner-all ui-loxia-highlight" 
										value="<fmt:formatDate value="${itemPresalseInfoCommand.activityEndTime }" pattern="yyyy-MM-dd HH:mm" />"></input>
									</td>
								</tr>
								<tr class="ui-block-line ">
									<td style="width: 150px">预计发货时间</td>
									<td style="width: 150px">
										<input type="text" readonly="readonly" name="deliveryTimeStr" id="deliveryTimeStr" timeinput="true" class="dynamicInputNameListPrices ui-loxia-default ui-corner-all ui-loxia-highlight" 
										value="<fmt:formatDate value="${itemPresalseInfoCommand.deliveryTime }" pattern="yyyy-MM-dd HH:mm" />"></input>
									</td>
								</tr>
								<tr class="ui-block-line ">
									<td style="width: 150px">付款方式</td>
									<td style="width: 150px">
										 <div class="priDiv">
									          	 <span class="children-store  normalCheckBoxSpan">
									          	 	<input name="paymentMethod" type="radio" class="normalCheckBoxCls" 
									          	 	<c:if test="${itemPresalseInfoCommand.paymentMethod==0||itemPresalseInfoCommand.paymentMethod==null}">checked="checked"</c:if>
									          	 	 value="0"/>全款
									          	 </span> 
								         </div>
								          <div class="priDiv">
									          	<span class="children-store  normalCheckBoxSpan">
									          	 	<input name="paymentMethod" type="radio" class="normalCheckBoxCls"
									          	 	<c:if test="${itemPresalseInfoCommand.paymentMethod==1}">checked="checked"</c:if>
									          	 	 value="1">定金+尾款
									          	</span> 
								         </div>
									</td>
								</tr>
								<tr class="ui-block-line " id="endtimeStrtr" style="visibility:hidden;">
									<td style="width: 150px">尾款截止支付日期</td>
									<td style="width: 150px">
										<input type="text" readonly="readonly" name="endtimeStr" id="endtimeStr" timeinput="true" class="dynamicInputNameListPrices ui-loxia-default ui-corner-all ui-loxia-highlight" 
										value="<fmt:formatDate value="${itemPresalseInfoCommand.endtime }" pattern="yyyy-MM-dd HH:mm" />"></input>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
		        </div>
		        <div style="margin-top: 20px"></div>
				<div class="ui-block ">	
				   <div class="ui-block-title1">SKU信息</div>		   
				</div>
			    <div class="ui-block-title1" style="background:#fff;color:#000;">SKU价格库存信息</div>
		        <div id="exten" class="ui-block-line">
		          	<div id="extension">
						<table id="extensionTable" class="border-grey"
							style="padding: 5px;">
							<tbody>
								<tr>
									<td style="width: 150px">商家编码</td>
									<td style="width: 150px">销售属性</td>
									<td style="width: 150px">吊牌价</td>
									<td style="width: 150px">销售价</td>
									<td style="width: 100px;text-align: center;">当前库存</td>
									<td style="width: 150px"></td>
									<td style="width: 100px">操作</td>
									<td style="width: 150px">付款金额(单位:元)</td>
								</tr>
								<c:forEach items="${itemPresalseInfoCommand.itemPresalseSkuInfoCommandList }" var="itemPresalseSkuInfo" varStatus="vs">
									<input type="hidden" name="itemPresalseSkuInfoCommandList[${vs.index }].extentionCode" value="${itemPresalseSkuInfo.extentionCode }"/>
									<input type="hidden" name="itemPresalseSkuInfoCommandList[${vs.index }].id" value="${itemPresalseSkuInfo.id }"/>
									<tr>
										<td >${itemPresalseSkuInfo.extentionCode }</td>
										<td >${itemPresalseSkuInfo.saleProperty }</td>
										<td >${itemPresalseSkuInfo.listPrice }</td>
										<td ><span myattr="salePrice">${itemPresalseSkuInfo.salePrice }</span></td>
										<td style="text-align: center;">${itemPresalseSkuInfo.currentInventory }
										</td>
										<td >
											<input type="text" myattr="editinventory" class="ui-loxia-default ui-loxia-highlight" name="itemPresalseSkuInfoCommandList[${vs.index }].inventoryIncrement" style="visibility: hidden;" placeholder="请填写整数或不填">
										</td>
										<td >
											<input value="增量修改" class="button orange" type="button" myattr="editbtn"/>
										</td>
										<td myattr="tdfullMoneytd">
											<span>全款 &nbsp; &nbsp;</span></span><input type="text" readonly="readonly" myattr="fullMoney" value="<fmt:formatNumber value="${itemPresalseSkuInfo.salePrice }" pattern="####0"/>" name="itemPresalseSkuInfoCommandList[${vs.index }].fullMoney" class="ui-loxia-default ui-loxia-highlight">
										</td>
										<td myattr="tdfullearnestbalancetd" style="display: none;">
											<span>定金 &nbsp; &nbsp;</span><input type="text" myattr="earnest" value="<fmt:formatNumber value="${itemPresalseSkuInfo.earnest }" pattern="####0"/>" name="itemPresalseSkuInfoCommandList[${vs.index }].earnest" class="ui-loxia-default ui-loxia-highlight">
											<span>尾款 &nbsp; &nbsp;</span><input type="text" myattr="balance" readonly="readonly" value="<fmt:formatNumber value="${itemPresalseSkuInfo.balance }" pattern="####0"/>" name="itemPresalseSkuInfoCommandList[${vs.index }].balance" class="ui-loxia-default ui-loxia-highlight">
										</td>
									</tr>
								</c:forEach>
								
							</tbody>
						</table>
					</div>
		        </div>
		  </div>
		  
		 <div class="button-line">
		  	<c:if test="${itemPresalseInfoCommand.lifecycle==null}">
		         	<input type="button" value="保存并启用" class="button orange submit" title="保存并启用" myattr="submitbtn" operationtype="save">
			</c:if>
			<c:if test="${itemPresalseInfoCommand.lifecycle==3}">
			  		<input type="button" value="保存并启用" class="button orange submit" title="保存并启用" myattr="submitbtn" operationtype="save">
		         	<input type="button" value="启用" class="button orange submit" title="启用" myattr="submitbtn" operationtype="enable">
			</c:if>
			<c:if test="${itemPresalseInfoCommand.lifecycle==1}">
			 		<input type="button" value="保存" class="button orange submit" title="保存" myattr="submitbtn" operationtype="save">
			        <input type="button" value="禁用" class="button orange submit" title="禁用" myattr="submitbtn" operationtype="disable">
			</c:if>
		         <input type="button" value="返回" class="button return" title="返回">        
		</div>
		 <div style="margin-top: 20px"></div>
	  </form:form>   
	</div>
<div class="error-information" style="display:none;"><h5>错误信息</h5><p>提交数据时发现错误：<br><span id="errormsg"></span></p></div>
</body>
</html>
