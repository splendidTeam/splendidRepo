$j.extend(loxia.regional['zh-CN'],{
	"LABEL_RETURN_CODE":"退换货单编号",
	"LABEL_LOGIN_CODE":"登录名",
	"LABEL_RETURN_OMSCODE":"OMS退货单编号",
	"LABEL_EXAM_STATUS":"退货申请状态",
	"LABEL_ORDER_CODE":"订单编号",
	"LABEL_LOGISTICS_CODE":"物流单号",
	"LABEL_LOGISTICS_NAME":"物流公司",
	"LABEL_RETURN_CREATETIME":"创建时间",
	"LABEL_ORDER_REMARK":"备注",
	"LABEL_RETURNMONEY_STATUS":"操作",
	"LABEL_APPROVER":"审批人",
	"LABEL_APPROVETIME":"审批时间",
	"LABEL_OPERATE":"操作",
	"OPERATOR_TOREFUND":"退换货处理中",
	"OPERATOR_WAIT":"审核通过",
	"OPERATOR_UPPASS":"拒绝退换货",
	"OPERATOR_REFUNDED":"已完成",
	"OPERATOR_REFUSED":"拒绝退换货",
	"OPERATOR_UNRETURN":"拒绝退换货",
	"OPERATOR_TOSEND":"待发货",
	"OPERATOR_SENDED":"退回中",
	"OPERATOR_AGREE":"同意退换货",
	"OPERATOR_FINISH":"已完成",
	"OPERATOR_CANALE":"取消",
	"INFO_CONFIRM_MANAGING":"确认同意退换货吗",
	"INFO_CONFIRM_RDFUNDED":"确定将退换货状态改为已完成吗",
	"INFO_CONFIRM_REFUSED":"确认拒绝退换货吗",
	"PROPERT_OPERATOR_TIP" : "属性提示信息",
	"INFO_TITLE_DATA" : "提示信息",
	"RETURN_MANAGING":"退换货正在处理中",
	"RETURN_RDFUNDED":"退换货已完成",
	"RETURN_AGREE":"退换货申请已通过",
	"RETURN_DISAGREE":"退换货申请未通过",
	"RETURN_REFUSED":"退换货已拒绝",
	"OPERATE_FAILED":"操作失败",
	"UPDATEITEM_SUCCESS" : "操作成功",
	"UPDATEITEM_FAIL" : "操作失败",
	"NULL_DESCRIPTIONORSTATUS" : "审核备注不能为空!",
	"NULL_OMSRETURNCODE" : "OMS退货单不能为空!",
	"INFO_TITLE_DATA" : "提示信息",
	"OPERATE_EXAIM":"待审核",
	"RETURN_UNPOST":"不能处理未发货的退货单",
	"RETURN_UNEXAIM":"请先审核退货单",
	"RETURN_REDUSE":"当前退货单已拒绝！",
	"RETURN_FINISH":"当前退货申请已完成！",
	"INFO_TITLE_DATA" : "提示信息",
	"STARTTIME_NULL" : "导出时，开始日期不能为空！",
	"ENDTIME_NULL" : "导出时，结束日期不能为空！",
	"TIME_NULL" : "导出时，开始日期和结束日期不能为空！",
	"STARTTIME_ENDTIME_ERROR" : "导出时，开始日期不能大于结束日期！",
	"DATA_NULL":"在所选日期范围内，没有任何VIP数据导出!",
	"EXPORT_DATE_LIMIT" : "导出时，最多只能导出一个月的数据！",
	"OMS_CONFIRM":"oms退货入库",
	"NULL_RETURNADDRESS":"退货地址不能为空",
	"RETURN_TYPE":"退换货类型",
	"RETURN":"退货",
	"EXCHANGE":"换货"
	
});

var orderReturnUrl = base +'/saleOrder/returnOrderList.json';

var returnDetailUrl = base +'/saleOrder/returnDetail.htm';
   

var updateRefundStatus=base+'/saleOrder/updateRefund.json';

var exaimUrl = base+"/saleOrder/returnExaim.json";

var manualCreateSalesOrderUrl = base + "/saleOrder/createReturnOrder.htm";

var orderCode=null;

function drawOrder(data, args, idx){
	
	var tempUrl =returnDetailUrl+"?id="+loxia.getObject("id", data);
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('" +tempUrl+
			"');\">"+loxia.getObject("returnApplicationCode", data)+"</a>";
}


//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh"); 
}

function checkDate() {
	var startDate = $j("#startDate").val();
	var endDate=$j("#endDate").val();
	if ((startDate == ""||startDate==null) &&( endDate == ""||endDate==null)) {
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("TIME_NULL"));
		return false;
	}
	if (startDate == "") {
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("STARTTIME_NULL"));
		return false;
	}

	if (endDate == "") {
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("ENDTIME_NULL"));
		return false;
	}

	startDate = startDate.replace(/-/g, "/");
	var startTime = new Date(startDate);

	endDate = endDate.replace(/-/g, "/");
	var endTime= new Date(endDate);

	// 判断开始日期不能大于结束日期
	if (startTime.getTime() > endTime.getTime()) {
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps
				.i18n("STARTTIME_ENDTIME_ERROR"));
		return false;
	}

	// 导出时，需要判断日期范围是否在一个月内的
	if (startTime.setMonth(startTime.getMonth() + 1) < endTime.getTime()) {
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("EXPORT_DATE_LIMIT"));
		return false;
	}
	return true;

	
}

//获取日期格式
//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}

//退换货处理函数
function updateRefund(val, state,arg) {
	var info = "";
		if(arg==3){
			info = nps.i18n("RETURN_UNPOST");
		}
		if(arg==1){
			info = nps.i18n("RETURN_UNEXAIM");
		}
		//退换货状态为3时允许处理退换货
		if(arg==4||arg==3){
			if (state ==5) {
				info = nps.i18n("INFO_CONFIRM_MANAGING");
			} if(state==2) {
				info = nps.i18n("INFO_CONFIRM_REFUSED");
			}
		}
	if(arg==2){
	return "<a style='cursor:pointer;' class='goto_aduit_dialog' data-returncode="+returnCode+" >"+nps.i18n("RETURN_REDUSE")+"</a>";
		info = nps.i18n("RETURN_REDUSE");
	}
	

	if(arg==5){
		if(state==6){
			info = nps.i18n("INFO_CONFIRM_RDFUNDED");
		}
	}
	
	nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"), info, function() {
		var json = {
			"returnOrderCode" : val,
			"state" : state
		};
		nps.asyncXhrGet(updateRefundStatus, json, {
			successHandler : function(data, textStatus) {
				var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					if (state == 5) {
						// 同意退换货
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("RETURN_AGREE"));
						refreshData();
						return;
					} 
					if (state == 2) {
						// 退换货拒绝
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("RETURN_DISAGREE"));
						refreshData();
						return;
					} 
					if (state == 6) {
						// 退换货已完成
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("RETURN_RDFUNDED"));
						refreshData();
						return;
					} 
				}else{
					if(backWarnEntity.description!=null && backWarnEntity.description!=undefined ){
						nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
						return;
					}else{
				    	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("OPERATE_FAILED"));
				    	return;
					}
				}
			}
		});
	});
	
}


//同意退换货
function fnAGREE(data, args, caller) {
	updateRefund(data.returnApplicationCode, 4,data.status);
}

//已完成
function fnFINISH(data, args, caller) {
	updateRefund(data.returnApplicationCode, 5,data.status);
}

//同意退换货
function fnPASS(data, args, caller) {
	var omsCode=data.omsCode;
	var orderCode=data.returnApplicationCode;
	$j("#remark").val('');
	$j("#returnCode").val(orderCode);
	$j("#omsCode").val(omsCode);
	$j("#returnType").val(data.type==1?'退货':'换货');
	$j("#active-dialog").dialogff({type:'open',close:'in',width:'500px',height:'400px'});
}

//拒绝退换货
function fnREFUSED(data, args, caller){
	var omsCode=data.omsCode;
	var orderCode=data.returnApplicationCode;
	$j("#orderCode").val(orderCode);
	$j("#omsReturnCode").val(omsCode);
	$j("#returnType").val(data.type==1?'退货':'换货');
	$j("#memo").val('');
	$j("#refuse-dialog").dialogff({type:'open',close:'in',width:'500px',height:'350px'});

}

function returnType(data){
	var status=loxia.getObject("type", data);
	if(status==2){
		return nps.i18n("RETURN");
	}
	if(status==3){
		return nps.i18n("EXCHANGE");
	}
}


function refund(data){
	var result = "";
	var returnStatus = loxia.getObject("status", data);
	var type= loxia.getObject("type", data);
		//returnStatus:3  或者2 ，提供同意退换货，和拒绝退换货按钮
		if ((returnStatus == 4||returnStatus==3)) {
			result = [ {
				label : nps.i18n("OPERATOR_AGREE"),
				type : "jsfunc",
				content : "fnAGREE"
			},{
				label : nps.i18n("OPERATOR_REFUSED"),
				type : "jsfunc",
				content : "fnREFUSED"
			}];
		} 
	
	//returnStatus:4  同意退换货，并提供已完成按钮
	if (returnStatus == 5) {
		result = [ {
			label : nps.i18n("OPERATOR_FINISH"),
			type : "jsfunc",
			content : "fnFINISH"
		}];
	} 
	if (returnStatus == 1) {
		result = [ {
			label : nps.i18n("OPERATOR_WAIT"),
			type : "jsfunc",
			content : "fnPASS"
		},{
			label : nps.i18n("OPERATOR_UPPASS"),
			type : "jsfunc",
			content : "fnREFUSED"
		}];
	} 
	return result;
}


function status(data, args, idx){
	var status=loxia.getObject("status", data);
	if(status==1){
		return nps.i18n("OPERATE_EXAIM");
	}
	if(status==2){
		return nps.i18n("OPERATOR_UNRETURN");
	}
	if(status==4||status==3){
		return nps.i18n("OPERATOR_SENDED");
	}
	if(status==5){
		return nps.i18n("OPERATOR_AGREE");
	}
	if(status==6){
		return nps.i18n("OPERATOR_FINISH");
	}
	if(status==7){
		return nps.i18n("OMS_CONFIRM");
	}
}

var exportUrl =base+"/salesorder/return/exportReturn.htm";
$j(document).ready(function() {
	var errorMsg = $j("#errorMsg").val();
	if (errorMsg != undefined && errorMsg != "") {
		nps.info(nps.i18n("INFO_TITLE_DATA"), errorMsg);
		$j("#errorMsg").val("");
	}

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	
    $j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form : "searchForm",
		cols : [ {
			name:"returnApplicationCode",
			label:nps.i18n("LABEL_RETURN_CODE"),
			width:"8%",
			template:"drawOrder"
		},{
			name:"omsCode",
			label:nps.i18n("LABEL_RETURN_OMSCODE"),
			width:"8%",
		},	{
			name:"soOrderCode",
			label:nps.i18n("LABEL_ORDER_CODE"),
			width:"8%"
		},
		 {
			name:"accountName",
			label:nps.i18n("LABEL_LOGIN_CODE"),
			width:"10%",
		},{
			name:"transName",
			label:nps.i18n("LABEL_LOGISTICS_NAME"),
			width:"5%"
		},{
			name:"transCode",
			label:nps.i18n("LABEL_LOGISTICS_CODE"),
			width:"8%"
		},{
			name:"createTime",
			label:nps.i18n("LABEL_RETURN_CREATETIME"),
			width:"8%",
			formatter:"formatDate",
			sort: ["application.create_time asc","application.create_time desc"]
		},{
			name:"approver",
			label:nps.i18n("LABEL_APPROVER"),
			width:"5%"
		},{
			name:"approveTime",
			label:nps.i18n("LABEL_APPROVETIME"),
			formatter:"formatDate",
			width:"8%",
			sort: ["application.approve_time asc","application.approve_time desc"]
		} ,{
			label:nps.i18n("LABEL_EXAM_STATUS"),
			width:"5%",
			template : "status"
		},{
			label:nps.i18n("RETURN_TYPE"),
			width:"6%",
			template : returnType		
		},{
			label:nps.i18n("LABEL_RETURNMONEY_STATUS"),
			width:"6%",
			type : "oplist",
			oplist : refund		
		}
		
		],
		dataurl : orderReturnUrl
	});

	refreshData();
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	});
	 
	 //取消按钮
	 $j("#active-dialog-cancel-btn").click(function() {
		 $j("#active-dialog").dialogff({type:'close'});
	 });
	 
	 $j("#refuse-dialog-cancel-btn").click(function() {
		 $j("#refuse-dialog").dialogff({type:'close'});
	 });
	 
	// 审核通过 
	$j("#activeOkBtn").click(function() {
		var remark = $j("#remark").val();
		remark= remark.replace(/\n/g,'<br/>');  
		var returnorderCode = $j("#returnCode").val();
		var platformOMSCode=$j("#omsCode").val();
		var returnAddress=$j("#returnAddress").val();
		var omsCode=$j("#omsCode").val();
			if (remark == ""||remark==null) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NULL_DESCRIPTIONORSTATUS"));
				return;
			} else if(returnAddress.trim()==""||returnAddress==null){
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NULL_RETURNADDRESS"));
				return;
			}else if(omsCode.trim()==""||omsCode==null){
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NULL_OMSRETURNCODE"));
				return;
			}
			else {
				var json = {
					"status" : 3,
					"description" : remark,
					"orderCode" : returnorderCode,
					"omsCode": platformOMSCode,
					"returnAddress":returnAddress
				};
				var _d = loxia.syncXhr(exaimUrl, json, {
					type : "POST"
				});
				if (_d.isSuccess == true) {
					nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("UPDATEITEM_SUCCESS"));				
					refreshData();
					$j("#active-dialog").dialogff({type:'close'});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("UPDATEITEM_FAIL"));
					$j("#active-dialog").dialogff({type:'close'});
				}
			}
			 $j("#remark").val("");
			 $j("#omsCode").val("");
		});
          
	    // 审核退回
		$j("#activeCancleBtn").click(function() {
			var description =$j("#memo").val();
			description= description.replace(/\n/g,'<br/>');  
			var returnorderCode = $j("#orderCode").val();
			var platformOMSCode=null;
			if (description ==""||description==null) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NULL_DESCRIPTIONORSTATUS"));
				return;
			} 
			else {
				var json = {
					"status" : 2,
					"description" : description,
					"orderCode" : returnorderCode,
					"omsCode": platformOMSCode,
					"returnAddress":""
				};
				var _d = loxia.syncXhr(exaimUrl, json, {
					type : "POST"
				});
				if (_d.isSuccess == true) {
					nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("UPDATEITEM_SUCCESS"));				
					refreshData();
					$j("#refuse-dialog").dialogff({type:'close'});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("UPDATEITEM_FAIL"));
				}
			}
			
		});
		
		//退货信息导出
		$j(".exportVip").on("click", function() {
			if (checkDate()) {
				$j("#searchForm").attr("action", exportUrl);
				$j("#searchForm").attr("method", "post");
				$j("#searchForm").submit();
			}

		});
	    
	    $j(".button.orange.manualCreate").click(function() {
	        window.location.href = manualCreateSalesOrderUrl;
	    })
			
	 
});

