//预售信息保存地址
var updateItemProsaleInfoUrl = base + '/item/updateItemProsaleInfo.json';
// 时间设置
$j(function() {
	$j("input[timeinput='true']").datetimepicker({
		format : "dd MM yyyy - hh:ii",
		autoclose : true,
		todayBtn : true
	});
	$j("input.button.return").click(function() {
		history.go(-1);
	});
	$j("input[myattr='submitbtn']").click(function() {
		tosubmit($j(this));
	});
	$j("input[myattr='editbtn']").click(function() {
		$j(this).parent().parent().find("input[myattr='editinventory']").css({
			"visibility" : "visible"
		});
	});
	$j(".normalCheckBoxCls").change(function() {
		var status = $j(this).val();
		changetd(status);
	});
		$j("input[myattr='earnest']").change(function() {
			var salePriceobj=$j(this).parent().parent().find("span[myattr='salePrice']");
			var earnest=$j(this).val();
			if($j.trim(earnest)!=""){
				var balance=parseFloat(salePriceobj.text())-parseFloat(earnest);
				if(balance<=0){
					showerrorinfo($j(this),"定金不能大于或等于销售价!");
				}else{
					$j(this).parent().find("input[myattr='balance']").val(balance);
					$j(".error-information").hide();
					$j("input.ui-loxia-error").removeClass("ui-loxia-error");
				}
			}
		});	
	changetd(paymentMethod);

});
function tosubmit(obj) {
	if (tovalidate() == false) {
		return;
	}
	if(obj.attr("operationtype")=="enable"||obj.attr("operationtype")=="save"){//
		$j("#lifecyclehidden").val("1");
	}else if(obj.attr("operationtype")=="disable"){
		$j("#lifecyclehidden").val("3");
	}
	$j.post(updateItemProsaleInfoUrl, $j("#itemProsaleInfoForm").serialize(),
			function(data) {
		if(data.isSuccess){
			nps.info("提示信息", '修改预售信息成功!');
		}else{
			nps.info("提示信息", data.description+'');
		}
		window.location.reload();
	});
	return;

}
/**
 * 自动显示和隐藏定金问款或全款列
 */
function changetd(status) {
	if (status == "0") {//全款
		$j("#endtimeStrtr").css({
			"visibility" : "hidden"
		});
		$j("td[myattr='tdfullMoneytd']").show();
		$j("td[myattr='tdfullearnestbalancetd']").hide();
	} else if(status == "1"){//定金加尾款
		$j("#endtimeStrtr").css({
			"visibility" : "visible"
		});
		$j("td[myattr='tdfullMoneytd']").hide();
		$j("td[myattr='tdfullearnestbalancetd']").show();
	}

}
/**
 * 验证参数
 * 
 * @returns {Boolean} true:验证通过
 */
function tovalidate() {
	$j(".error-information").hide();
	$j("input.ui-loxia-error").removeClass("ui-loxia-error");
	//*******验证开始******************
	var activityName=$j.trim($j("#activityName").val());
	if(activityName==""){
		showerrorinfo($j("#activityName"),"活动名称不能为空");
		return false;
	}
	var activityStartTimeStr=$j("#activityStartTimeStr").val();
	var activityEndTimeStr=$j("#activityEndTimeStr").val();
	var deliveryTimeStr=$j("#deliveryTimeStr").val();
	var endtimeStr=$j("#endtimeStr").val();
	var paymentMethod=$j(".normalCheckBoxCls:checked").val();
	
	if(activityEndTimeStr<=activityStartTimeStr){
		showerrorinfo($j("#activityEndTimeStr"),"结束时间必须大于开始时间!");
		return false;
	}
	if(deliveryTimeStr<=activityEndTimeStr){
		showerrorinfo($j("#deliveryTimeStr"),"预计发货时间必须大于结束时间!");
		return false;
	}
	if(endtimeStr<=activityStartTimeStr&&paymentMethod==1){
		showerrorinfo($j("#endtimeStr"),"尾款截止支付日期必须大于开始时间!");
		return false;
	}
	var returnstatus=true;
	$j.each($j("#extensionTable tr"),function(i,n){
		if(i>0){
			var editinventoryobj=$j(n).find("input[myattr='editinventory']");
			var fullMoneyobj=$j(n).find("input[myattr='fullMoney']");
			var earnestobj=$j(n).find("input[myattr='earnest']");
			if(validatinventory(editinventoryobj.val(),editinventoryobj)==false){//库存增量
				showerrorinfo(fullMoneyobj,"库存增量值格式不正确!");
				returnstatus=false;
				return false;
			}
			if(paymentMethod==0){//全款
				if(validatemoney(fullMoneyobj.val())==false){
					showerrorinfo(fullMoneyobj,"全款金额格式不正确!");
					returnstatus=false;
					return false;
				}else{
					
				}
			}else{//定金加尾款
				if(validatemoney(earnestobj.val())==false){
					showerrorinfo(earnestobj,"定金金额格式不正确!");
					returnstatus=false;
					return false;
				}else if(validaterange(earnestobj)==false){
					showerrorinfo(earnestobj,"定金金额不能超出售价!");
					returnstatus=false;
					return false;
				}
			}
		}
	});
	if(returnstatus==false){
		return false;
	}
	return true
}
function showerrorinfo(obj,msg){
	obj.addClass("ui-loxia-error");
	$j("#errormsg").text(msg);
	$j(".error-information").show();
}
/**
 * 验证库存
 * @param amount
 * @returns
 */
function validatinventory(amount,obj){
	if(amount==null||$j.trim(amount)==""){
		return true;
	}else{
		if(amount.substring(0, 1)=="-"){
			amount=amount.substring(1,amount.length);
		}else if(amount.substring(0, 1)=="+"){
			amount=amount.substring(1,amount.length);
			obj.val(amount);
		}
		return boo=/^-?\d+$/.test(amount) ;//整数
	}
}
/**
 * 验证定金金额
 * @param amount
 * @returns
 */
function validatemoney(amount){
		return boo=/^\+?[1-9][0-9]*$/.test(amount) ;//验证非零的正整数
}
/**
 * 验证定金没有超出该售价 true:通过
 */
function validaterange(earnestobj){
	var salePriceobj=earnestobj.parent().parent().find("span[myattr='salePrice']");
	var earnest=earnestobj.val();
		var balance=parseFloat(salePriceobj.text())-parseFloat(earnest);
		if(balance<=0){
			return false;
		}else{
			return true;
		}
}