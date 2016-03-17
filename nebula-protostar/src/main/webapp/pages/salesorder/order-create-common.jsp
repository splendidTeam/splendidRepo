<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<div style="margin-top: 10px"></div>

<div class="ui-block-title1" style="background: #fff; color: #000;">
	收货人信息<span style="color: red">（*为必填项）</span>
</div>
<!--  收货人信息 开始-->
<div class="ui-block-content border-grey">
	<div class="sbox-wrap">
		<div id="editAddressArea">
			<div class="ui-block-line ">
				<label> <em>*</em>收货人：
				</label>
				<div>
					<input type="text" loxiaType="input" name="name" id="name" value=""
						mandatory="true" placeholder="" />
				</div>
			</div>
			<div class="ui-block-line ">
				<label><em>*</em>选择所在地：</label>
				<div>
					<select id="provience" name="provinceId"></select> 
					<select id="city" name="cityId"></select> 
					<select id="area" name="areaId" onchange="refreshOrderData()"></select>
				</div>
			</div>
			<div class="ui-block-line ">
				<label> <em>*</em>街道地址：
				</label>
				<div style="padding: 0px;">
					<textarea type="text" id="address" loxiaType="input"
						mandatory="true" name="address" rows="2" style="width: 490px;"
						class="ui-loxia-default ui-corner-all"></textarea>
				</div>
			</div>
			<div class="ui-block-line ">
				<label> <em>*</em>手机号码：
				</label>
				<div>
					<input type="text" id="mobile" name="mobile" maxlength="11" loxiaType="input" mandatory="true"
						checkmaster="checkMobile" placeholder="" />
				</div>
			</div>
			
			<div class="ui-block-line ">
				<label> 固定电话：
				</label>
				<div>
					<input type="text" id="tel" name="tel" loxiaType="input" checkmaster="checkTel" placeholder="" />
				</div>
			</div>
			<div class="ui-block-line ">
				<label><em>*</em>邮箱：</label>
				<div>
					<input type="text" id="email" name="email" loxiaType="input" value="" mandatory="true"
						checkmaster="checkEmail" 　placeholder="" /> 用来接收订单提醒邮件，便于您及时了解订单状态
				</div>
			</div>
			<div class="ui-block-line ">
				<label> 邮编：
				</label>
				<div>
					<input type="text" id="postcode" name="postcode" maxlength="6" loxiaType="input"
						value="" checkmaster="checkPostCode"   placeholder="" />
				</div>
			</div>
		</div>
	</div>



</div>
<!--  收货人信息 结束-->

<div style="margin-top: 10px"></div>
<!--  支付及配送方式 开始-->
<div class="ui-block-title1" style="background: #fff; color: #000;">支付及配送方式</div>

<div class="ui-block-content border-grey">


	<div class="ui-block-line ">
		<label style="">支付方式</label>
	</div>
	<div class="sbox-wrap">
		 <dl class="bank">
                   <dt class="pt20 bold">平台支付：</dt>
                   <dd><input type="radio" class="inform-check" value="支付宝支付" name="paymentStr" checked="checked"  /><img src="/images/bank/alipay.gif" /></dd>
                   
                   <dt class="pt20 bold">网银支付：</dt>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中国工商银行" name="paymentStr" /><img src="/images/bank/ICBC_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_招商银行" name="paymentStr" /><img src="/images/bank/CMB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中国建设银行" name="paymentStr" /><img src="/images/bank/CCB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中国农业银行" name="paymentStr" /><img src="/images/bank/ABC_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_北京农商银行" name="paymentStr" /><img src="/images/bank/BJRCB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中国银行" name="paymentStr" /><img src="/images/bank/BOC_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中国光大银行" name="paymentStr" /><img src="/images/bank/CEB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_兴业银行" name="paymentStr" /><img src="/images/bank/CIB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中信银行" name="paymentStr" /><img src="/images/bank/CITIC_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_中国民生银行" name="paymentStr" /><img src="/images/bank/CMBC_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_交通银行" name="paymentStr" /><img src="/images/bank/COMM_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_富滇银行" name="paymentStr" /><img src="/images/bank/FDB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_广东发展银行" name="paymentStr" /><img src="/images/bank/GDB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_杭州银行" name="paymentStr" /><img src="/images/bank/HZCB_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_宁波银行" name="paymentStr" /><img src="/images/bank/NBBANK_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_上海银行" name="paymentStr" /><img src="/images/bank/SHBANK_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_平安银行" name="paymentStr" /><img src="/images/bank/SPABANK_OUT.gif" /></dd>
                   <dd><input type="radio" class="inform-check" value="支付宝支付_上海浦东发展银行" name="paymentStr" /><img src="/images/bank/SPDB_OUT.gif" /></dd>
                   
                   <dt class="pt20 bold">信用卡支付：</dt>
                   <dd><input type="radio" class="inform-check" value="支付宝国内信用卡支付" name="paymentStr" /><img src="/images/bank/CREDIT_CARD.gif" /></dd>
                   
                   <dt class="pt20 bold">货到付款：</dt>
                   <dd><input type="radio" class="inform-check" value="货到付款" name="paymentStr" /><img src="/images/bank/cod.gif" /></dd>
               </dl>
	</div>

	<div class="ui-block-line ">
		<label style="">配送方式</label>
	</div>
	<div class="sbox-wrap">
		<c:forEach var="distributionModeChooseOption" items="${distributionModeList}"
			varStatus="status">
			<div class="ui-block-line ">
				<input type="radio" name="distributionModeId" id="distributionModeId"
					value="${distributionModeChooseOption.id }"
					<c:if test="${status.count==1 }">checked="checked"</c:if> />
					${distributionModeChooseOption.name }
			</div>
		</c:forEach>
	</div>


	<div class="ui-block-line ">
		<label style="">送货时间</label>
	</div>
	<div class="sbox-wrap">
		<c:forEach var="appointTimeChooseOption" items="${appointTimeList}"
			varStatus="status">
			<div class="ui-block-line ">
				<input type="radio" name="appointType"
					value="${appointTimeChooseOption.optionLabel }"
					<c:if test="${status.count==1 }">checked="checked"</c:if> />
				${fn:replace(appointTimeChooseOption.optionLabel, "&&", "&nbsp;　　　&nbsp;")}
			</div>
		</c:forEach>
		指定送货时间 <input type="input" name="appointTime" loxiaType="date"
			showTime="true" min="next" />
	</div>


</div>
<!--  支付及配送方式 结束-->

<div style="margin-top: 10px"></div>
<!--  发票信息 开始-->
<div class="ui-block-title1" style="background: #fff; color: #000;">发票信息</div>

<div class="ui-block-content border-grey">

	<div class="ui-block-line ">
		<label style="">类型和抬头</label>
	</div>
	<div class="sbox-wrap">
		<div class="ui-block-line ">
			<label style="">发票类型：</label>
			<c:forEach var="receiptTypeChooseOption" items="${receiptTypeList}"
				varStatus="status">
				<input type="radio" name="receiptType"
					value="${receiptTypeChooseOption.optionValue }"
					<c:if test="${status.count==1 }">checked="checked"</c:if> />
	                     ${fn:replace(receiptTypeChooseOption.optionLabel, "&&", "&nbsp;　　　&nbsp;")} 
			        </c:forEach>
		</div>
		<div class="ui-block-line ">
			<label style="">发票抬头：</label>
			<c:forEach var="receiptTitleChooseOption" items="${receiptTitleList}"
				varStatus="status">
				<input type="radio" name="receiptTitleOpt"
					value="${receiptTitleChooseOption.optionValue }"
					<c:if test="${status.count==1 }">checked="checked"</c:if> />
	                     ${fn:replace(receiptTitleChooseOption.optionLabel, "&&", "&nbsp;　　　&nbsp;")} 
			         </c:forEach>
			<input type="input" name="receiptTitle" id="receiptTitle"
				loxiaType="input" value="" placeholder="" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label style="">发票内容</label>
	</div>
	<div class="sbox-wrap">
		<div class="ui-block-line ">
			<c:forEach var="receiptContentChooseOption"
				items="${receiptContentList}" varStatus="status">
				<input type="radio" name="receiptContent"
					value="${receiptContentChooseOption.optionLabel }"
					<c:if test="${status.count==1 }">checked="checked"</c:if> />
	                    ${fn:replace(receiptContentChooseOption.optionLabel, "&&", "&nbsp;　　　&nbsp;")} 
		         </c:forEach>
		</div>
	</div>

</div>
<!--  发票信息 结束-->

<div style="margin-top: 10px"></div>
<!--  商品清单 开始-->
<div class="ui-block-title1" style="background: #fff; color: #000;">商品清单</div>
<div class="ui-tag-change">
	<div class="tag-change-content">
		<div class="border-grey ui-loxia-simple-table" caption="商品列表">
			<table id="productTable" width="100%" cellspacing="0" cellpadding="0">
				<tr id="orderLineTitle">
					<th width="15%">&nbsp;</th>
					<th width="25%" class="left">商品名称</th>
					<th width="15%">交易单价</th>
					<th width="15%">订购数量</th>
					<th width="15%">金额小计</th>
					<th width="15%">操作</th>
				</tr>
			</table>


		</div>
		<div class="order-summary"></div>
		<a id="cleanShoppingcart" href="javascript:void(0)"
			class="func-button ml5">清空购物车</a> <a id="addShoppingcart"
			href="javascript:void(0)" class="func-button ml5">添加商品</a>
	</div>

</div>
