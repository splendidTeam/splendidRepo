/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'], {
	"NO_DATA" : "无数据",
	"INFO_TITLE_DATA" : "提示信息",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
	"LABEL_PROMOTION_PROMOTIONNAME" : "促销名称",
	"LABEL_PROMOTION_STARTTIME" : "开始时间",
	"LABEL_PROMOTION_ENDTIME" : "结束时间",
	"LABEL_PROMOTION_AUDIENCEVALUE" : "受益人群",
	"LABEL_PROMOTION_TYPE" : "状态",
	"LABEL_PROMOTION_EFFECTMARK" : "参与计算",
	"LABEL_PROMOTION_GROUP" : "优惠项",
	"LABEL_PROMOTION_UPDATETIME" : "更新时间",
	"LABEL_PROMOTION_UPDATE" : "更新人员",
	"LABEL_MEMBER_OPERATE" : "操作",
	"LABEL_PROMOTION_SCOPE" : "商品范围",
	"LABEL_LIFECYCLE_0" : "待启用",
	"LABEL_LIFECYCLE_1" : "已启用",
	"LABEL_LIFECYCLE_2" : "已生效",
	"LABEL_LIFECYCLE_3" : "已取消",
	"LABEL_PROMOTION_LOGOTYPE" : "角标",
	"TO_UP" : "上移",
	"TO_DOWN" : "下移",
	"INFO_INCOMPLETE_INFO" : "请完整填写优先级信息！",
	"INFO_CRUCIAL_CALCULATION" : "至少需要有2个促销参与计算！",
	"INFO_SUCCESS" : "保存成功！",
	"INFO_FAILURE" : "保存失败！",
	"INFO_EARLY_DATE":"结束时间必须晚于起始时间",
	"LABEL_PROMOTION_SELECT":"选择",
	"LABEL_PROMOTION_PRIORITY":"编号",
	"INFO_GROUP_INFO" : "请完整填写分组信息！",
	"INFO_GROUP_LENGTH_INFO" : "至少选择两个活动参加分组！",
	"INFO_GROUP_NAME_EXIST_INFO" : "分组名称已存在！",
	"INFO_SAVE_SUCCESS_INFO" : "分组信息保存成功！",
	"UNGROUP_SURE":"解组之后不可恢复，确认解组？",
	"UNGROUP_SUCCESS":"解组成功",
	"UNGROUP_FAILURE":"解组失败",
	"INFO_ADJUST_NAME_EXIST_INFO":"优先级名称已存在"
	
});
/* ------------------------------------------------- URL ------------------------------------------------- */
var LIST_URL = base + "/promotion/promotionListForPriority.json";
var SAVE_URL = base + "/promotion/savePriority.json";

var promotionpriorityadjustUrl = base + "/promotion/promotionpriorityadjust.htm";

/** 可以分组的促销活动 */
var findCanGroupPromotionListUrl = base + '/promotion/findCanGroupPromotionList.json';
var savePriorityAdjustGroupUrl = base + '/promotion/savePriorityAdjustGroup.json';
var checkExistGroupNameUrl = base + '/promotion/checkGroupName.json';
var ungroupUrl = base+'/promotion/ungroup.json';

var editPriorityUrl = base+"/promotion/editPriority.htm";

var checkAdjustNameUrl = base+"/promotion/checkAdjustName.json";
/* ------------------------------------------------- 全局常量 ------------------------------------------------- */
/** 0单享排他组，共享排他组**/
var exclusiveGroupType = 0;
/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
 
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	$j("#table1").loxiasimpletable();

	// 上移
	$j("#table1").on("click", ".btn-up", function() {
		var tbody = $j("#table1 tbody");
		var tr = $j(this).parent().parent();
		//tr.siblings().removeClass("highlight");
		var prev = tr.prev();
		if (prev.length > 0) {
			tr.remove();
			prev.before(tr);
			//colourTbody(tbody);
		}
		//tr.addClass("highlight");
	});  

	// 下移
	$j("#table1").on("click", ".btn-down", function() {
		var tbody = $j("#table1 tbody");
		var tr = $j(this).parent().parent();
		//tr.siblings().removeClass("highlight");
		var next = tr.next();
		if (next.length > 0) {
			tr.remove();
			next.after(tr);
			//colourTbody(tbody);
		}
		//tr.addClass("highlight");
	}); 
	
	$j("#table1").on("click",".btn-ungroup",function(){
		 var groupName = $j(this).attr("groupName");
		 var adjustId = $j("#priority-id").val();
		 nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("UNGROUP_SURE"), function(){
	            var data = nps.syncXhrPost(ungroupUrl, {"adjustId": adjustId,"groupName":groupName});
	        	if (data.isSuccess) {
	        		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("UNGROUP_SUCCESS"));
	        		refreshData();
	        	} else {   
	        		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("UNGROUP_FAILURE"));
	        	}
	            	
	        });
	});
	
	// 保存
	$j(".btn-save").click(function() {
		var txts = $j("#div-info :text").blur();
		if (txts.filter(".ui-loxia-error").length > 0) {	// 输入框验证
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_INCOMPLETE_INFO"));
			return;
		}
		
		
		var id = $j("#priority-id").val();
		
		if(id==""){
			// 名称是否已存在
			var data = {"name":$j("#name").val().trim()};
			var res = loxia.syncXhrPost(checkAdjustNameUrl, data);
			if(!res){
				$j('#name').addClass('ui-loxia-error');
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ADJUST_NAME_EXIST_INFO"));
				return;
			}
		}
		
		var name = $j.trim(txts[0].value);

		var priorityDetailList = [];
		$j("#table1 #parameters").each(function(i, dom) {
			
			var detail = {};
			var promotionId = $j(dom).attr("promotionId");
			var groupName = $j(dom).attr("groupName");
			
			if(groupName==""){
				detail.exclusiveMark = $j("#pid_"+promotionId).prop("checked") ? 1 : 0;
			}else{
				detail.exclusiveMark = 0;
			}
			detail.promotionId = promotionId;
			
			priorityDetailList[i] = detail;
		});
		
		var priorityDetailListString = JSON.stringify(priorityDetailList);
		var json = {
				id: id,
				adjustName: name,
				priorityDetailListString: priorityDetailListString
		};
		nps.asyncXhrPost(SAVE_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
//				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
//				$j("#priority-id").val(data.description);
				location.href="/promotion/promotionpriorityadjust.htm";
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
			}
		}});
		
	});
	
	// date-picker位置错误bug
//	$j('.date-picker').focus(function(){
//		$j("#ui-datepicker-div").css({top:parseInt($j(this).offset().top)+parseInt($j(this).outerHeight())});
//	});
	
//	$j('.tbl_priority_datas tbody').find("tr").each(function(i, dom) {
//		var td = $j(dom).find("td:eq(6)");
//		var  logotype = $j.trim(td.text());
//		//td.text(drawSettingtype(logotype));
//	});
//	$j('.tbl_priority_datas tbody').find("tr").each(function(i, dom) {
//		var td = $j(dom).find("td:eq(5)");
//		var  status = $j.trim(td.text());
//		td.text(drawStatusjsp(status));
//	});

	/** *********************************************dialog******************************************************** */
	/** 创建新组 */
	$j('.btn-create').click(function() {
		// 加载Dialog数据
		refreshDialogData();
		exclusiveGroupType = 0;
		var dialogGroupNameObj = $j('#dialogGroupName');
		dialogGroupNameObj.val("");
		if(dialogGroupNameObj.hasClass('ui-loxia-error')){
			dialogGroupNameObj.removeClass('ui-loxia-error');
		}
		// 弹出Dialog
		$j('.promotion-exclusive-dialog').dialogff({
			type : 'open',
			close : 'in',
			width : '900px',
			height : '500px'
		});
	});
	/** 创建新组共享 */
	$j('.btn-create-share').click(function() {
		// 加载Dialog数据
		refreshDialogData();
		exclusiveGroupType = 1;
		var dialogGroupNameObj = $j('#dialogGroupName');
		dialogGroupNameObj.val("");
		if(dialogGroupNameObj.hasClass('ui-loxia-error')){
			dialogGroupNameObj.removeClass('ui-loxia-error');
		}
		// 弹出Dialog
		$j('.promotion-exclusive-dialog').dialogff({
			type : 'open',
			close : 'in',
			width : '900px',
			height : '500px'
		});
	});
	$j("#dialog-table").loxiasimpletable({
		page : false,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		cols : [ {
			name : "priority",
			label : nps.i18n("LABEL_PROMOTION_PRIORITY"),
			width : 100
		}, {
			name : "promotionName",
			label : nps.i18n("LABEL_PROMOTION_PROMOTIONNAME"),
			width : 200
		}, {
			name : "promotionStartTime",
			label : nps.i18n("LABEL_PROMOTION_STARTTIME"),
			width : 200,
			formatter:"formatData"
		}, {
			name : "promotionEndTime",
			label : nps.i18n("LABEL_PROMOTION_ENDTIME"),
			width : 200,
			formatter:"formatData"
		}, {
			name : "audienceName",
			label : nps.i18n("LABEL_PROMOTION_AUDIENCEVALUE"),
			width : 200
		}, {
			name : "scopeName",
			label : nps.i18n("LABEL_PROMOTION_SCOPE"),
			width : 200
		}, {
			label : nps.i18n("LABEL_PROMOTION_SELECT"),
			template: "selectGroupTemplate",
			width : 100
		} ],
		dataurl : findCanGroupPromotionListUrl+"?adjustId="+$j('#priority-id').val()
	});
	
	/** 确定按钮 */
	$j('.dialog-confirm').click(function(){
		var groupName = $j('#dialogGroupName').val();
		if(!groupName){
			$j('#dialogGroupName').addClass('ui-loxia-error');
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_GROUP_INFO"));
			return;
		}
		// 只有选择两个或两个以上的活动才可以参加分组
		var selectedPriorityAdjustArr = $j('.canGroupPromotion:checked');
		if(selectedPriorityAdjustArr.length < 2){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_GROUP_LENGTH_INFO"));
			return;
		}
		// 分组名称是否已存在
		var data = {"groupName":groupName};
		var isExists = loxia.syncXhrPost(checkExistGroupNameUrl, data);
		if(isExists){
			$j('#dialogGroupName').addClass('ui-loxia-error');
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_GROUP_NAME_EXIST_INFO"));
			return;
		}
		
		var promotionIds = new Array();
		$j.each(selectedPriorityAdjustArr, function(index, object){
			promotionIds.push(object.value);
		});
		
		// 保存分组数据 
		var json = {"groupName":groupName, "promotionIds":promotionIds, "adjustId":$j('#priority-id').val(),"excGroupType":exclusiveGroupType};
		var resultData = loxia.syncXhrPost(savePriorityAdjustGroupUrl, json);
		if(resultData.isSuccess){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SAVE_SUCCESS_INFO"));
		}
		
		/** 关闭Dialog */
		$j('.dialog-close').click();
		refreshData();
	});
	
	/** 取消按钮 */
	$j('.dialog-cencal').click(function() {
		$j('.dialog-close').click();
	});
	
	/** 返回 */
	$j('.return').click(function(){
		window.location.href = promotionpriorityadjustUrl;
	});
	
	/** *********************************************dialog******************************************************** */

});

/** 刷新dialog数据 */
function refreshDialogData() {
	$j("#dialog-table").loxiasimpletable("refresh");
}

function selectGroupTemplate(data){
	var promotionId = data.promotionId;
	return '<input type="checkbox" class="canGroupPromotion" value='+promotionId+' />';
}

function formatData(val){
	return new Date(val).format("yyyy-MM-dd hh:mm:ss");
}

/* ------------------------------------------------- util ------------------------------------------------- */
// 为表格体上色
function colourTbody(tbody) {
	tbody.find("tr").each(function(i, dom) {
		var cls = (i%2==0) ? "even" : "odd";
		$j(dom).removeClass("even odd").addClass(cls);
	});
}

// 刷新数据
function refreshData() {
	location.href=editPriorityUrl+"?id="+$j("#priority-id").val();
}

// 比较日期大小
function compareDate(data1, data2) {
	return Date.parse(data1.replace(/-/g,"/")) > Date.parse(data2.replace(/-/g,"/"));
}

// 空字符串
function isBlank(str) {
	return str == null || $j.trim(str).length == 0;
}

// 获取日期格式
function formatDate(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
				+ date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}
/* ------------------------------------------------- draw ------------------------------------------------- */
function drawSettingtypejs(data){
	var result="无"; 
	var type = loxia.getObject("logo", data);
	if(type !=null){
		if(type==1) 
			result="减";
		if(type==2) 
			result="折";
		if(type==3) 
			result="免运费";
		if(type==4) 
			result="优惠价"; 
		if(type==5) 
			result="赠品"; 
	}
 	return result; 
}
function drawSettingtype(type){
	var result="无"; 
	if(type !=null){
		if(type==1) 
			result="减";
		if(type==2) 
			result="折";
		if(type==3) 
			result="免运费";
		if(type==4) 
			result="优惠价"; 
		if(type==5) 
			result="赠品"; 
	}
 	return result; 
}
function drawStatus(data, args, idx) {
	var lifecycle = loxia.getObject("lifecycle", data);
	return nps.i18n("LABEL_LIFECYCLE_" + lifecycle);
}
function drawStatusjsp(status) {
	return nps.i18n("LABEL_LIFECYCLE_" + status);
}

function drawCheckbox(data, args, idx){
	var id = loxia.getObject("promotionId", data);
	return "<input type='checkbox' name='isCalc' disabled='disabled' checked='checked' data='" + id + "' />";
}

function drawEditor(data, args, idx){
	var up = "<a href='javascript:void(0);' class='func-button btn-up'>" + nps.i18n("TO_UP") + "</a>";
	var down = "<a href='javascript:void(0);' class='func-button btn-down'>" + nps.i18n("TO_DOWN") + "</a>";
	return up + down;
}