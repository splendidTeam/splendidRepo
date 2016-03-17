/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'], {
	"NO_DATA" : "无数据",
	"INFO_TITLE_DATA" : "提示信息",
	"LABEL_NAME" : "调整名称",
	"LABEL_START_TIME" : "开始时间",
	"LABEL_END_TIME" : "结束时间",
	"LABEL_UPDATE_TIME" : "更新时间",
	"LABEL_UPDATE_USER" : "更新人员",
	"LABEL_STATUS" : "状态",
	"LABEL_OPERATION" : "操作", 
	"TO_VIEW" : "查看",
	"TO_UPDATE" : "修改",
	"TO_ENABLE" : "启用", 
	"TO_DISABLE" : "禁用", 
	"USER_CONFIRM_ENABLE_PROMTION" : "确认启用吗？",
	"USER_CONFIRM_DISABLE_PROMTION" : "确认禁止吗？", 
	"INFO_ENABLE_FAIL" : "启用失败：不能启用多个优先级",
	"INFO_DISABLE_FAIL" : "禁用失败：",
	"INFO_ENABLE_SUCCESS" : "启用成功！",
	"INFO_DISABLE_SUCCESS" : "禁用成功！",
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var LIST_URL = base + '/promotion/priorityAdjustList.json'; 
var ENABLE_URL = base + '/promotion/enablePriority.json'; 
var DISABLE_URL = base + '/promotion/disablePriority.json'; 
var EDIT_URL = base + '/promotion/editPriority.htm'; 
var VIEW_URL = base + '/promotion/viewPriority.htm'; 
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
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
		{
			name : "adjustName",
			label : nps.i18n("LABEL_NAME"),
			width : "10%" 
			
		}, {
			name : "lastUpdateTime",
			label : nps.i18n("LABEL_UPDATE_TIME"),
			width : "10%" , 
			sort:["pa.lastupdatetime asc","pa.lastupdatetime desc"],
			formatter:"formatDate"
		}, {
			name : "realname",
			label : nps.i18n("LABEL_UPDATE_USER"),
			width : "10%"  
		}, {
			name : "status",
			label : nps.i18n("LABEL_STATUS"),
			width : "10%",
			template : "drawStatus"
		},  {
			name : "operation",
			label : nps.i18n("LABEL_OPERATION"),
			width : "10%", 			 
			template : "drawEditor"
		} ],
		dataurl : LIST_URL
	});
	refreshData();
	
	// 搜索
	$j(".btn-search").click(function() {
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});  
	
	// 启用
	$j("#table1").on("click", ".btn-enable", function() {
		var id = $j(this).attr("data");
        nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("USER_CONFIRM_ENABLE_PROMTION"), function(){
            var data = nps.syncXhrPost(ENABLE_URL, {id: id});
        	if (data.isSuccess) {
        		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_ENABLE_SUCCESS"));
        		refreshData();
        	} else {   
        		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_ENABLE_FAIL"));
        	}
            	
        });
    });
	
	// 禁用
	$j("#table1").on("click", ".btn-disable", function() {
		var id = $j(this).attr("data");
        nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("USER_CONFIRM_DISABLE_PROMTION"), function(){
            var data = nps.syncXhrPost(DISABLE_URL, {id: id});
        	if (data.isSuccess) {
        		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_DISABLE_SUCCESS"));
        		refreshData();
        	} else {   
        		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_DISABLE_FAIL"));
        	}
            	
        });
    });
	
	// 创建
	$j(".btn-create").click(function(){
        window.location.href = EDIT_URL;
    });

	// 编辑
	$j("#table1").on("click", ".btn-update", function() {
		var id = $j(this).attr("data");
		window.location.href = EDIT_URL + "?id=" + id;
    });
	
	// 查看
	$j("#table1").on("click", ".btn-view", function() {
		var id = $j(this).attr("data");
		window.location.href = VIEW_URL + "?id=" + id;
    });
});

/* ------------------------------------------------- util ------------------------------------------------- */
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

//空字符串
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
function drawStatus(data, args, idx){
	var status = loxia.getObject("activeMark", data);
	switch (status) {
	case 0:
		return "待启用";
	case 1:
		return "已启用";
	case 3:
		return "已禁用";
	}
}
   
function drawEditor(data, args, idx){
	var id = loxia.getObject("id", data);
	var status = loxia.getObject("activeMark", data);
	
	var result="<a href='javascript:void(0);' data='"+id+"' class='func-button btn-view'>"+nps.i18n("TO_VIEW")+"</a>"; 
	if (status == 0) {
		result += "<a href='javascript:void(0);' data='"+id+"' class='func-button btn-enable'>"+nps.i18n("TO_ENABLE")+"</a>"
				+ "<a href='javascript:void(0);' data='"+id+"' class='func-button btn-update'>"+nps.i18n("TO_UPDATE")+"</a>";
	} else if (status == 1) {
		result += "<a href='javascript:void(0);' data='"+id+"' class='func-button btn-disable'>"+nps.i18n("TO_DISABLE")+"</a>";
	}
	
	return result;
}
