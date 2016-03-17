/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_COUPON_CODENAME":"优惠券名称",
	    "LABEL_COUPON_CODE":"券码", 
	    "LABEL_COUPON_STARTTIME":"开始时间",	   
	    "LABEL_COUPON_ENDTIME":"结束时间",
	    "LABEL_COUPON_STATE":"状态",
	    "LABEL_COUPON_UPDATTIME":"创建时间",
	    "LABEL_COUPON_UPDATEMENBER":"创建人员",
	    "LABEL_COUPON_ACTION":"操作",
	    "TO_DELETE":"删除",
	    "NO_DATA":"无数据",
	    "INFO_TITLE_DATA":"提示信息", 
	    "INFO_REMOVE_SUCCESS":"删除成功", 
	    "INFO_REMOVE_FAILURE":"删除失败：", 
	    "INFO_CONFIRM_REMOVE":"确认删除吗？", 
	    "limitTimes":"剩余次数"
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var IMPORT_URL = base + "/promotion/couponCodeImport.htm";
var LIST_URL = base+'/promotion/couponCodeList.json'; 
var REMOVE_URL = base + "/promotion/removeCouponCode.json";
/* ------------------------------------------------- 全局常量 ------------------------------------------------- */

/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
  
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();  
	


	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "searchForm",
		cols : [ {
			name : "couponName",
			label : nps.i18n("LABEL_COUPON_CODENAME"),
			width : "11%"
		}, {
			name : "couponCode",
			label : nps.i18n("LABEL_COUPON_CODE"),
			width : "10%"
		}, {
			name : "startTime",
			label : nps.i18n("LABEL_COUPON_STARTTIME"),
			width : "10%",
			formatter : "formatDate",
			sort : [ "cc.start_time asc", "cc.start_time desc" ]
		}, {
			name : "endTime",
			label : nps.i18n("LABEL_COUPON_ENDTIME"),
			width : "10%",
			formatter : "formatDate",
			sort : [ "cc.end_time asc", "cc.end_time desc" ]
		}, {
			name : "limitTimes",
			label : nps.i18n("limitTimes"),
			width : "10%"
		}, {
			name : "isused",
			label : nps.i18n("LABEL_COUPON_STATE"),
			width : "6%",
			template : "drawState"
		}, {
			name : "createTime",
			label : nps.i18n("LABEL_COUPON_UPDATTIME"),
			width : "10%",
			formatter : "formatDate",
			sort : [ "cc.create_time asc", "cc.create_time desc" ]
		}, {
			name : "createName",
			label : nps.i18n("LABEL_COUPON_UPDATEMENBER"),
			width : "10%"
		}, {
			name : "操作",
			label : nps.i18n("LABEL_COUPON_ACTION"),
			width : "9%",
			template : "drawOperation"
		} ],
		dataurl : LIST_URL
	});
	refreshData();

	// 筛选数据
	$j(".func-button.search").click(function() {
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});  
	
	//‘删除’
	$j("#table1").on("click", ".remove", function() {
		var id = $j(this).attr("data");
		nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CONFIRM_REMOVE"), function() {
			var data = nps.syncXhrPost(REMOVE_URL, {id: id});
			if (data.isSuccess) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REMOVE_SUCCESS"));
				refreshData();
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REMOVE_FAILURE") + data.description);
			}
		});
	});

    //导入
    $j(".import").click(function() {
	   window.location.href = IMPORT_URL;
    });
   
});

/* ------------------------------------------------- draw ------------------------------------------------- */
//优惠券状态
function drawState(data, args, idx){
	var isUsed = loxia.getObject("isused", data);
	var endTime = loxia.getObject("endTime", data);
	return generateCouponState(isUsed, endTime);
}

//操作中的启用禁用函数
function drawOperation(data, args, idx){  
	var isUsed = loxia.getObject("isused", data);
	return isUsed == 0 ? "<a href='javascript:void(0);' data='"
			+ loxia.getObject("id", data) + "' class='func-button remove'>"
			+ nps.i18n("TO_DELETE") + "</a>" : "";
}

/* ------------------------------------------------- util ------------------------------------------------- */
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

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

/**
 * 生成优惠券状态字符串
 * @param isUsed
 * @param endTime
 * @returns
 */
function generateCouponState(isUsed, endTime) {
	if (isUsed == 1) {
		return "已使用";
	} else {
		return endTime < new Date() ? "已过期" : "未使用";
	}
}

 