<%-- <%@include file="/pages/commons/common.jsp" %> --%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="url" uri="http://www.baozun.cn/url"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
	
<link rel="stylesheet" type="text/css" href="${base}/libs/spice/css/spice.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/libs/spice/css/spice.icon.min.css" />
      <link rel="stylesheet" type="text/css" href="${base}/libs/spice/js/cloudzoom/css/cloudzoom.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/libs/spice/js/tinyscrollbar/css/tinyscrollbar.min.css" />
   <link rel="stylesheet" type="text/css" href="${base}/css/common.css" />
   <link rel="stylesheet" type="text/css" href="${base}/css/myaccount-return.css">
<script type="text/javascript" src="${base}/libs/jquery/jquery.js"></script>
<script type="text/javascript" src="${base}/libs/iscroll/iscroll.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/jquery.spice.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/jquery.spice.tools.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/kvScroll/jquery.kvScroll.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/dropdown/jquery.dropdown.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/placeholder/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/cloudzoom/jquery.cloudzoom.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/tinyscrollbar/jquery.tinyscrollbar.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/citySelect/json/area.zh.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/citySelect/jquery.citySelect.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/ymdSelect/jquery.ymdSelect.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/lazyLoad/jquery.lazyLoad.min.js"></script>
<script type="text/javascript" src="${base}/libs/velocity/velocity.min.js"></script>
<script type="text/javascript" src="${base}/libs/spice/js/masonry/jquery.masonry.min.js"></script>

<script type="text/javascript" src="${base}/js/common.js"></script>
<script type="text/javascript" src="${base}/scripts/salesorder/create-return.js"></script>
<%-- <script type="text/javascript" src="${base}/js/return/return-order.js"></script> --%>
<%-- <script type="text/javascript" src="${base}/js/myAccount/myaccount-return.js"></script> --%>
<body>
	<div class="ui-block-title1" style="background: #fff; color: #000;">
					订单信息
	</div>
	<form id="returnDetaill"  action="${page_base}/myAccountOrder/returnDetail" method="post">
		<input type="hidden"  name="orderId" value="${salesOrder.id}" />
		<input type="hidden" name="isTrack" value="isNotTrack"/>
	</form>
			
					<!-- 内容部分 -->
			<section class="container">
				<!-- 模块一 -->
				
			<form id="returnOrderForm" action="${page_base}/myAccountOrder/returnCommit" method="get" >
				<article class="article article-con-box">
					<div class="article myaccountcon-smallbox float-clearfix">
						<div class="myaccountcon-right">
							<div class="return">
								<div class="return-title float-clearfix">
									<h3 class="float-left">我的订单</h3>
									<p class="float-right"><a href="javascript:history.go(-1);"><i class="icon order-towards-left"></i> 返回订单详情</a></p>
								</div>
								<div class="return-main">
									<div class="return-main-title float-clearfix">
										<p class="float-right show-again-on" >订单号: <span>${salesOrder.code }</span></p>
									</div>
									<div class="return-main-title-s">请选择您要退货的商品以及退货数量</div>
									<div class="goods-detail online-1020">
										<span class="show-again-all nobuy-all" style="left: 40px;top: 0px;">全选</span>
										<div class="goods-detail-title float-clearfix">
											<div class="float-left w105 checked" id="allselect">
												<i class="icon icon-checkbox15 nobuy-all"></i>	
											</div>
											<div class="float-left w28-a">商品</div>
											<div class="float-left w20">购买数量</div>
											<div class="float-left w20">退货数量</div>
											<div class="float-left w15">价格</div>
										</div>
										<div class="goods-detail-main float-clearfix">
										<c:forEach items="${soReturnLineVo}" var="lines">
											<c:if test="${lines.orderLineCommand.count-lines.count>=1}">
											<div class="goods-border-b-150">
												<div class="float-left unpay-w45 checked" id="checkBox-${lines.orderLineCommand.id}">
												 <input type="hidden" value="${lines.orderLineCommand.id}" class="orderLineId" name="orderLineId"/>
			      								 <input type="hidden" value="<fmt:formatNumber type="number" value="${lines.orderLineCommand.subtotal/lines.orderLineCommand.count}" pattern="#,##0.00"/> " class="unitPrice"/>
			      								 <input type="hidden"  class="selectNum" name="sum"/>
			      								 <input type="hidden" class="reason" name="reason"/>
													<div class="introduce">
														<h3><a href="${page_base}/${lines.orderLineCommand.productCode}/item.htm">${lines.orderLineCommand.itemName }</a></h3>
														<p>颜色: ${lines.orderLineCommand.skuPropertys[1].value}</p>
														<p>尺码: ${lines.orderLineCommand.skuPropertys[0].value}</p>
														<p class="display-none-1">购买数量：${lines.orderLineCommand.count }
														<span style="display: inline-block; float: right;color:#000;margin-right:20px;" ><span class="returnMoney">
															￥<fmt:formatNumber type="number" value="${lines.orderLineCommand.subtotal/lines.orderLineCommand.count}" pattern="#,##0.00"/>
														</span></span></p>

														<div class="shopcart-quantity  w100 unpay-767">
															<div class="dropdown events-dropdown event-cause">
																<a class="btn">
																	<span style="font-size:12px;" id="reason-${lines.orderLineCommand.id}">请选择退货原因</span>
																		<span>
																		<i class="icon icon-caret"></i>
																	</span>
																</a>
																<div class="sub-menu tinyscrollbar events-tinyscrollbar">
																	<div class="viewport">
																        <ul class="overview event-overview">
																			<li>
																				<a>我改变主意了</a>
																			</li>
																			<li>
																				<a>商品质量有问题</a>
																			</li>
																			<li>
																				<a>商品包装破损</a>
																			</li>
																			<li>
																				<a>尺码不合适</a>
																			</li>
																			<li>
																				<a>颜色/款式与商品描述不符</a>
																			</li>
																		</ul>
																    </div>
																    <!-- 这里是滚动条部分 -->
																    <div class="scrollbar">
																        <div class="track">
																            <div class="thumb">
																                <div class="end"></div>
																            </div>
																        </div>
																    </div>
																</div>
															</div>
															<span class="msg-block">请选择退货原因</span>
														</div>
													
													
													</div>
													<i class="icon icon-checkbox15 event-icon-checkbox15"></i>
													<div class="goods-pic">
														<a href="${page_base}/${lines.orderLineCommand.productCode}/item.htm"><img src="<url:img size='221X298' imgUrl='${lines.orderLineCommand.itemPic}'/>"width="55" height="74"></a>
													</div>
												</div>
												
												
												<div class="float-left w20 show-stop">
													<p class="display-none">购买数量：</p>${lines.orderLineCommand.count }
												</div>
												<div class="float-left w20 show-stop">
													<p class="display-none">退货数量：</p>
													<c:if test="${lines.orderLineCommand.count-lines.count>1}">
													<div class="shopcart-quantity unpay-767-1 shopcart-quantity-center">
														<div class="dropdown events-dropdown event-num">
															<a class="btn">
																<span id="selectNum-${lines.orderLineCommand.id}">1</span>
																<span>
																	<i class="icon icon-caret"></i>
																</span>
															</a>
															<div class="sub-menu tinyscrollbar events-tinyscrollbar">
																<div class="viewport">
															        <ul class="overview event-overview">
																		<c:forEach var="i" begin="1" end="${lines.orderLineCommand.count-lines.count}">
																		<li class="returnNum">
																			<a >${i}</a>
																		</li>
																	</c:forEach>
																		
																	</ul>
															    </div>
															    <!-- 这里是滚动条部分 -->
															    <div class="scrollbar">
															        <div class="track">
															            <div class="thumb">
															                <div class="end"></div>
															            </div>
															        </div>
															    </div>
															</div>
														</div>
													</div>
													</c:if>
														<c:if test="${lines.orderLineCommand.count-lines.count==1}">
														<span id="selectNum-${lines.orderLineCommand.id}">1</span>	
														</c:if>
												</div>
													<!-- 如果可退的商品数量大于1，显示选择退货数量的下拉框 -->
												<c:if test="${lines.orderLineCommand.count-lines.count>=1 }">
												<div class="shopcart-quantity w100 unpay-767 mob-dropdown">
													<div class="dropdown events-dropdown event-num">
														 <a class="btn">
															<span style="font-size:12px;" id="selectNum-${lines.orderLineCommand.id}">请选择退货数量</span>
															<span>
																<i class="icon icon-caret"></i>
															</span>
														</a>
														<div class="sub-menu tinyscrollbar events-tinyscrollbar">
															<div class="viewport">
														        <ul class="overview event-overview">
																	<c:forEach var="i" begin="1" end="${lines.orderLineCommand.count-lines.count}">
																		<span><li class="returnNum">
																			<a >${i}</a>
																			<i class="icon icon-right"></i>
																		</li></span>
																	</c:forEach>
																</ul>
														    </div>
														    <!-- 这里是滚动条部分 -->
														    <div class="scrollbar">
														        <div class="track">
														            <div class="thumb">
														                <div class="end"></div>
														            </div>
														        </div>
														    </div>
														</div> 
													
													</div>
												</div>
												</c:if>
												<div class="shopcart-quantity w100 unpay-767 mob-dropdown">
													<div class="dropdown events-dropdown event-cause">
														<a class="btn">
															<span style="font-size:12px;" id="reason-${lines.orderLineCommand.id}">请选择退货原因</span>
																<span>
																		<i class="icon icon-caret"></i>
																</span>
														</a>
														<div class="sub-menu tinyscrollbar events-tinyscrollbar">
															<div class="viewport">
														        <ul class="overview event-overview">
																			<li>
																				<a>我改变主意了</a>
																			</li>
																			<li>
																				<a>商品质量有问题</a>
																			</li>
																			<li>
																				<a>商品包装破损</a>
																			</li>
																			<li>
																				<a>尺码不合适</a>
																			</li>
																			<li>
																				<a>颜色/款式与商品描述不符</a>
																			</li>
																</ul>
														    </div>
														    <!-- 这里是滚动条部分 -->
														    <div class="scrollbar">
														        <div class="track">
														            <div class="thumb">
														                <div class="end"></div>
														            </div>
														        </div>
														    </div>
														</div>
													</div>
													<span class="msg-block">请选择退货原因</span>
												</div>
												
												<div class="float-left w15 show-stop show-767-unpay" ><span class="returnMoney">
													¥<fmt:formatNumber type="number" value="${lines.orderLineCommand.subtotal/lines.orderLineCommand.count}" pattern="#,##0.00"/>
												</span></div>
											
												
											</div>
												</c:if>
												<c:if test="${(lines.orderLineCommand.count-lines.count)==0}">
												<div class="goods-border-b-999">
												<div class="float-left unpay-w45">
													<div class="introduce">
														<h3><a href="${page_base}/${lines.orderLineCommand.productCode}/item.htm">${lines.orderLineCommand.itemName }</a></h3>
														<p>颜色: ${lines.orderLineCommand.skuPropertys[1].value}</p>
														<p>尺码: ${lines.orderLineCommand.skuPropertys[0].value}</p>
														<p class="display-none-1">购买数量：${lines.orderLineCommand.count }
													</div>
													<i class="icon icon-checkbox15"></i>
													<div class="goods-pic">
														<a href="${page_base}/${lines.orderLineCommand.productCode}/item.htm"><img src="<url:img size='221X298' imgUrl='${lines.orderLineCommand.itemPic}'/>"width="55" height="74"></a>
													</div>
												</div>
												<div class="float-left w20 show-stop">
													<p class="display-none">购买数量：</p>${lines.orderLineCommand.count }
												</div>
												<div class="w55">
														*该商品已全部退完，如有问题请联系客服
												</div>
											</div>
												</c:if>
											</c:forEach>
											
										</div>
<!-- ------------------------------------------------------------------------------------------------------------ -->	
										<div class="goods-detail-footer float-clearfix">
											<div class="float-left">
												<p>共 <span class="returnCountNum" >0</span> 件退货商品 <span class="ml15" >退款总计</span></p>
											</div>
											<div class="float-right">
												<p class="mr20"><span class="returntotal">¥0.00</span></p>
											</div>
										</div>
									</div>
									<div class="form-textarea">
									    <textarea placeholder="退货说明(非必填)" id="returnexplain" name="memo" maxLength="101"></textarea>
									    <span class="msg-block">
											<span>报错文案</span>
										</span>
									    <span class="zsts">不超过100个字</span>
									</div>
									<%-- <c:if test="${isCod}">
										<div class="unpay-zhuanyou">
											<h3>请您填写您的银行帐户信息，我们将会退款至您所填写的帐户</h3>
											<ul class="float-clearfix">
												<li class="float-left">
													<p>银行名称</p>
													<div class="shopcart-quantity " >
															<div class="dropdown events-dropdown ">
																<a class="btn">
																	<span class="returnbank">银行名称</span>
																	<span>
																		<i class="icon icon-caret"></i>
																	</span>
																</a>
																<div class="sub-menu tinyscrollbar events-tinyscrollbar">
																	<div class="viewport">
																        <ul class="overview">
																			<li>
																				<a>中国工商银行</a>
																			</li>
																			<li>
																				<a>中国农业银行</a>
																			</li>
																			<li>
																				<a>中国银行</a>
																			</li>
																			<li>
																				<a>中国建设银行</a>
																			</li>
																			<li>
																				<a>交通银行</a>
																			</li>
																			<li>
																				<a>中信银行</a>
																			</li>
																			
																			<li>
																				<a>中国光大银行</a>
																			</li>
																			<li>
																				<a>华夏银行</a>
																			</li>
																			<li>
																				<a>中国民生银行</a>
																			</li>
																			<li>
																				<a>广发银行股份有限公司</a>
																			</li>
																			<li>
																				<a>平安银行</a>
																			</li>
																			<li>
																				<a>招商银行</a>
																			</li>
																			<li>
																				<a>兴业银行</a>
																			</li>
																			<li>
																				<a>上海浦东发展银行</a>
																			</li>
																		</ul>
																    </div>
																    <!-- 这里是滚动条部分 -->
																    <div class="scrollbar">
																        <div class="track">
																            <div class="thumb">
																                <div class="end"></div>
																            </div>
																        </div>
																    </div>
																</div>
															</div>
															<span class="msg-block"></span>
													</div>
												</li>
												<li class="float-left">
													<p>支行名称</p>
													<input type="text" class="form-input p1" id="returnbranch" placeholder="支行名称"/>
													<span class="msg-block"></span>
												</li>
												<li class="float-left" style="clear: both" >
													<p>银行卡号</p>
													<input type="text" class="form-input p1" id="returnaccount"  placeholder="银行卡号"/>
													<span class="msg-block"></span>
												</li>
												<li class="float-left">
													<p>账户姓名</p>
													<input type="text" class="form-input p1" id="returnaccountname" placeholder="账户姓名"/>
													<span class="msg-block"></span>
												</li>
											</ul>
										</div>
									</c:if> --%>
									
									<div class="unpay-submit">
										<div class="submit-sure return-dialogunpay" >确认退货</div>
										<div class="submit-nosure" ><a href="javascript:history.go(-1);">取消退货</a></div>
										<span class="msg-block">请先选择您要退货的商品</span>
									</div>
								</div>
							</div>
						</div>
							<input type="hidden" value="${salesOrder.code}" class="orderCode" name="orderCode"/>
							<input type="hidden" value="${salesOrder.id}" class="orderId" name="orderId"/>
							<input type="hidden" class="accountname" name="userName"/>
							<input type="hidden" class="branch" name="branch"/>
							<input type="hidden" class="account" name="account"/>
							<input type="hidden" class="bank" name="bank"/>
							<input type="hidden" class="lineIdSelected" name="lineIdSelected"/>
							<input type="hidden" class="sumSelected" name="sumSelected"/>
							<input type="hidden" class="reasonSelected" name="reasonSelected"/>
							<!--判断是否是cod，因为cod普通退货单公用一个js，在js里有少许的区别，因此做一个判断  -->
							<input type="hidden" value="${isCod}"  class="isCod"/>
					</div>
				</article>
				</form>
				</section>
				</body>
		