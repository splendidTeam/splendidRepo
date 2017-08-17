
$j.extend(loxia.regional['zh-CN'],{
	"OPERATE_FAILED":"操作失败",

});



var orderLineId;
var  account;
var branch;
var accountname;
var  memo;
var bank;
 
$j(document).ready(function() {


	
	$j(".search").live("click", function(){
		loadOrderData(1);
	});	
	
	$j(".search-change").live("click", function(){
		loadOrderData(2);
	});	
	
	

		
		$j(".event-cause li").on("click",function(){

			var _this = $j(this),
				_this_p = _this.parents(".goods-border-b-150"),
				causetext = _this.children("a").text();
				
			$j('.mob-dropdown .event-cause a span:eq(0)',_this_p).text(causetext);
			$j('.unpay-767 .event-cause a span:eq(0)',_this_p).text(causetext);

		})
		
		$j(".event-num li").on("click",function(){

			var _this = $j(this),
				_this_p = _this.parents(".goods-border-b-150"),
				causetext = _this.children("a").text();
				
			$j('.mob-dropdown .event-num a span:eq(0)',_this_p).text(causetext);
			$j('.w20 .event-num a span:eq(0)',_this_p).text(causetext);
		   returnNum($j(this));
		})

});


function dialogunpay(){
	$j('body').on('click','.return-dialogunpay', function(event) {
		var isAllowSubmit=true;
		
		//用于判断是否有选中的订单行
		var isHasChecked=false;
		$j('.goods-border-b-150').each(function(){
			if(($(this).find('[id^=checkBox]').attr('class'))=='float-left unpay-w45 checked is-checked'){
				isHasChecked=true;
				if($(this).find('[id^=reason]').eq(0).text()=="请选择退货原因"){
					isAllowSubmit=false;
				}
				returnReasonErrorMsg($(this).find('[id^=reason]').eq(0),false);
				returnReasonErrorMsg($(this).find('[id^=reason]').eq(1),false);
				$(this).find(".reason").val($(this).find('[id^=reason]').eq(0).text());
				$(this).find(".reason").attr("name","reasonChecked");
				$(this).find(".selectNum").attr("name","sumChecked");
				$(this).find(".orderLineId").attr("name","lineIdChecked");
			}
			
		});
		if(!isHasChecked){
			returnSubmitErrorMsg($j(this),false,false);
			isAllowSubmit=false;
		}else{
			returnSubmitErrorMsg($j(this),false,true);
		}
		
		      memo=$j("#returnexplain").val();
		     
		    $j(".memo").text(memo);
		    if(!isAllowSubmit){
		    	return;
		    }
			   returnEachCheck();
		     
	});
	
}

var orderListUrl = base + "/order/findOrder.json";
function loadOrderData(type){
	var code=$j('.code').val();
		var json = {
			"code" : code,
			"type" :  type
			};
		nps.asyncXhrGet(orderListUrl, json, {
			successHandler : function(data, textStatus) {
				$j(".showReturn").html(data);
				initClick();
				initCheck();
			}
		});
}
	
function initCheck(){
	
	$j("body").on('click', '.unpay-dialog-submit .sure', function() {
		returnEachCheck();
	});
	
	
	$j("#returnexplain").keypress(function(){
		if($j(this).val().length>100){
			returnExplainErrorMsg($j(this), false)
			$j(this).val($j(this).val().substr(0,100));
		}
	});
	
	
}

 
function returnEachCheck(){
	var selectNum="";
	var orderLineId="";
	var reason="";
	$j(".goods-border-b-150").each(function(){
		//判断是否被选中
		if($j(this).find('[id^=checkBox]').hasClass('is-checked')){
			
			selectNum+=$j.trim($j(this).find('[id^=selectNum]').eq(0).text()+",");
			 orderLineId+=$j.trim($j(this).find('input[name="lineIdChecked"]').val()+",");
			 reason+=$j.trim($j(this).find('[name=reasonChecked]').eq(0).val()+",");
			 
			 $j(this).find('.selectNum').val(selectNum);
			 $j(this).find('.orderLineId').val(orderLineId);
			 $j(this).find('.orderCode').val($j('input[name="orderCode"]').val());
			 $j(this).find('.orderId').val($j('input[name="orderId"]').val());
			 $j(this).find('.reason').val(reason);
			 
		}
		
	});
	$j(".lineIdSelected").val(orderLineId.substring(0, orderLineId.lastIndexOf(',')));
	$j(".sumSelected").val(selectNum.substring(0, selectNum.lastIndexOf(',')));
	$j(".reasonSelected").val(reason.substring(0, reason.lastIndexOf(',')));
	
	
	
	var lineIdSelected=$j(".lineIdSelected").val();
	var sumSelected=$j(".sumSelected").val();
	var reasonSelected=$j('.reasonSelected').val();
	var orderId=$j('[name=orderId]').val();
	var orderCode=$j('[name=orderCode]').val();
	
	var url = base +'/order/returnCommit';
	var data = nps.syncXhrGet(url, {
		lineIdSelected:lineIdSelected,
		sumSelected:sumSelected,
		reasonSelected:reasonSelected,
		orderId:orderId,
		orderCode:orderCode,
		memo:memo,
		bank:bank,
		branch:branch,
		account:account,
		userName:accountname,
		lineIdSelected:lineIdSelected
    });
	if (data.isSuccess) {
		showInfo("操作成功！");
		//刷新数据
		window.setTimeout("window.location.href = base + '/saleOrder/orderReturn.htm'",2500);
		
	} else {
			showInfo(data.description);
		window.setTimeout("window.location.href = base + '/saleOrder/orderReturn.htm'",2500);
	}
	
	return false;
}


function showInfo(msg){
	var msgDialog = $.spice.dialog({
	    title: msg
	    , dialogClass: 'dialog-common dialog-time-info'
	    , autoClose: 1500
	});
	msgDialog.show();
}



/* 退货用函数 */
//退货说明
function returnExplainErrorMsg($jobj, isfoucs) {
	if ($jobj.val().length>100) {
		validateLoginInputFlag = false;
		return setValidInRegister($jobj, false, "已达上限", isfoucs);
	} else {
		return setValidInRegister($jobj, true, "");
	}
}

function notNullErrorMsg($jobj, isfoucs,value){
	if ($jobj.val()==null||$jobj.val().trim()=="") {
		validateLoginInputFlag = false;
		return setValidInRegister($jobj, false, "请输入" + value, isfoucs);
	} else {
		return setValidInRegister($jobj, true, "");
	}
	
}

function initClick(){
/*	commonSelect();*/

	dialogunpay();
	checkBox();
	selectAll();
	/*returnProduct();*/
	
}
/*
function returnProduct(){
	$j('[name=returnProduct]').on("click", function(){
		$j('[name=orderId]').val($j(this).attr('orderId'));
		$j('#returnDetaill').attr("action",_contextPath+"/myAccountOrder/returnOrder");
		    $j("#returnDetaill").submit();       
	});
}*/

function selectAll(){
	$j(".nobuy-all").on("click",function(){
		
		var returnSum=0;
		var returnCountNum=0;
		
	 	if($j("#allselect").hasClass('is-checked')){
			$j("#allselect").removeClass('is-checked');
			$j(".checked").removeClass("is-checked");
			$j(".returnCountNum").text(0);
		}else{
			$j("#allselect").addClass('is-checked');
			$j(".checked").addClass("is-checked");
			/*$j('[id^=selectNum]').text($j('[id^=selectNum]').eq(0).text());*/
			$j('.goods-border-b-150').each(function(){
				if(($j(this).find('[id^=checkBox]').attr('class'))=='float-left unpay-w45 checked is-checked'){
					returnSum+=	parseFloat($j(this).find(".returnMoney").html().trim().substring(1).replace(",",""));
					returnCountNum+=parseInt(($j(this).find('[id^=selectNum]').eq(0).text()));
				}
				
			});
			$j(".returnCountNum").text(returnCountNum);
		}
	 		$j(".returntotal").html('¥'+formatMoney(returnSum));
		});
}


function checkBox(){
	$j('[id^=checkBox]').find("i").on("click",function(){
		var totalReturnNum=parseInt($j(".returnCountNum").text());
		$j(this).parent().parent().find('[id^=selectNum]').text($j(this).parent().parent().find('[id^=selectNum]').eq(0).text())
		 var total=parseFloat($(".returntotal").html().trim().substring(1).replace(",",""));
		var isAll=true;
		if($(this).parent().attr('class')=='float-left unpay-w45 checked is-checked'){
			$j("#allselect").removeClass("is-checked");
			isAll=false;
			total-=parseFloat($j(this).parent().find(".returnMoney").html().trim().substring(1).replace(",",""));
			totalReturnNum-=parseInt($j(this).parent().parent().find('[id^=selectNum]').eq(0).text());
		}else{
			totalReturnNum+=parseInt($j(this).parent().parent().find('[id^=selectNum]').eq(0).text());
			$j(this).parent().parent().siblings().each(function(){
				if(!$j(this).find('[id^=checkBox]').hasClass('is-checked')){
					isAll=false;
				}
			});
			if(isAll){
				$j("#allselect").addClass("is-checked");
				total+=parseFloat($j(this).parent().find(".returnMoney").html().trim().substring(1).replace(",",""));
			}else{
				$j("#allselect").removeClass("is-checked");
				total+=parseFloat($j(this).parent().find(".returnMoney").html().trim().substring(1).replace(",",""));
			}
		}
	

		$(".returntotal").html('¥'+formatMoney(total));	
		$(".returnCountNum").text(totalReturnNum);
	});
}










function returnFloat(value){
	 var value=Math.round(parseFloat(value)*100)/100;
	 var xsd=value.toString().split(".");
	 if(xsd.length==1){
	 value=value.toString()+".00";
	 return value;
	 }
	 if(xsd.length>1){
	 if(xsd[1].length<2){
	 value=value.toString()+"0";
	 }
	 return value;
	 }
	}






//设置错误信息
function setValidInRegister(obj, flag, message, isfocus) {
	if (!flag) {
		$(obj).parent().removeClass("is-success");
		$(obj).parent().addClass("is-error");
		$(obj).next("span").show();
		if (isfocus)
			$(obj).focus();
		$(obj).siblings("span").html(message);
		return false;
	}
	$(obj).parent().removeClass("is-error");
	$(obj).parent().addClass("is-success");
	$(obj).siblings("span").find("span").html("");
	$(obj).next("span").hide();
	return true;

}

//确认退货错误提示
function returnSubmitErrorMsg($jobj, isfoucs,flag) {
	if (flag==false) {
		validateLoginInputFlag = false;
		return setValidInRegister($jobj, false, "请先选择您要退货的商品", isfoucs);
	} else {
		return setValidInRegister($jobj, true, "");
	}
}

//退货原因
function returnReasonErrorMsg($jobj, isfoucs) {
	if ($jobj.eq(0).text()=="请选择退货原因") {
		validateLoginInputFlag = false;
		return setValidInRegister($jobj.parent().parent(), false, "请选择退货原因", isfoucs);
	} else {
		return setValidInRegister($jobj.parent().parent(), true, "");
	}
}

//设置银行输入框错误信息
//设置错误信息
function setValidInBank(obj, flag, message, isfocus) {
	if (!flag) {
		$(obj).parent().removeClass("is-success");
		$(obj).parent().addClass("is-error");
		$(obj).next("span").show();
		if (isfocus)
			$(obj).focus();
		$(obj).siblings("span").html(message);
		return false;
	}
	$(obj).parent().removeClass("is-error");
	$(obj).parent().addClass("is-success");
	$(obj).siblings("span").html("");
	$(obj).next("span").hide();
	return true;

}




/**
 * 格式化金额
 * */
function formatMoney(s) {
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
	t = "";
	for (var i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	return t.split("").reverse().join("") + "." + r;
}

function returnNum(arg) {
	var selectedCount = parseInt(arg.find('a').text());
	var unitPrice = arg.parent().parent().parent().parent().parent().parent().parent().find(".unitPrice").val();
	arg.parent().parent().parent().parent().parent().parent().parent().find(".returnMoney").html('¥'+formatMoney(selectedCount * unitPrice.replace(",","")));
	
	
	/*计算总共的退款金额、退货数量*/
	var returnSum=0;
	//每次点击重新计算退货数量
	var returnNum=0;
	$j('.goods-border-b-150').each(function(){
		if(($j(this).find('[id^=checkBox]').attr('class'))=='float-left unpay-w45 checked is-checked'){
			returnNum+=parseInt($j(this).find('[id^=selectNum]').eq(0).text());
			returnSum+=	parseFloat($j(this).find(".returnMoney").html().trim().substring(1).replace(",",""));
		}
		
	});
	$j(".returntotal").html('¥'+formatMoney(returnSum));
	$j(".returnCountNum").text(returnNum);	

}