/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":			"提示信息",
	"LABLE_TITLE":				"标题",
	"LABLE_DESCRIPTION":		"内容",
	"LABLE_CREATE_TIME":		"创建时间",
	"LABLE_MODIFY_TIME":		"修改时间",
	"LABLE_STATUS":				"状态",
	"LABLE_OPERATION":			"操作",
	"LABLE_UPDATE":				"修改",
	"LABLE_ENABLE": 			"启用",
	"LABLE_DISABLE": 			"禁用",
	"LABLE_PREVIEW": 			"预览",
	"INFO_SUCCESS":				"操作成功！",
	"INFO_FAILURE":				"操作失败："
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var LIST_URL = base + "/station/getStationTempletListData.json";	// 数据
var EDIT_URL = base + "/station/editStationTemplet.htm";	// 添加
var UPDATE_URL = base +"/station/updateStationTemplet.htm"; //修改
var ENABLE_OR_DISABLE_URL= base +"/promotion/enableOrDisablePromotionInfo.json";//启用禁用
/* ------------------------------------------------- 全局常量 ------------------------------------------------- */
/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	// 列表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols : [{
					name : "title",
					label : nps.i18n("LABLE_TITLE"),
					width : "15%",
				},{
					name : "message",
					label : nps.i18n("LABLE_DESCRIPTION"),
					width : "30%",
				},{
					name:"createTime",
					label : nps.i18n("LABLE_CREATE_TIME"),
					width : "15%",
					formatter:"formatDate"
				},{
					name:"modifyTime",
					label : nps.i18n("LABLE_MODIFY_TIME"),
					width : "15%",
					formatter:"formatDate"
				},{
					name:"type",
					label : nps.i18n("LABLE_OPERATION"),
					width : "15%",
					template : "drawOperation"
				}  ],
		form:"searchForm",
		dataurl : LIST_URL
	});
	refreshData();
	
	// 新增
	$j(".add").click(function() {
		location.href = EDIT_URL;
	});
	
	// 搜索
    $j(".func-button.search").click(function(){
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
    });
    
    // 启用
    $j("#table1").on("click", ".enable", function() {
    	var id = $j(this).attr("val");
    	var data = nps.syncXhrPost(ENABLE_OR_DISABLE_URL, {type:1,id: id, isEnable: true});
		if (data.isSuccess) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
			refreshData();
		} else {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILURE") + data.description);
		};
    });

    // 禁用
    $j("#table1").on("click", ".disable", function() {
    	var id = $j(this).attr("val");
    	var data = nps.syncXhrPost(ENABLE_OR_DISABLE_URL, {type:0,id: id, isEnable: false});
		if (data.isSuccess) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
			refreshData();
		} else {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILURE") + data.description);
		}
    });
    
    //修改
    $j("#table1").on("click", ".update", function() { 
    	var id = $j(this).attr("val");
    	location="/update/getStationTemplet.json?id="+id;
    });
});

/* ------------------------------------------------- util ------------------------------------------------- */
function drawCheckbox(data, args, idx){
	return "<input type='checkbox' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}
// 生成操作按钮
function drawOperation(data, args, idx) {  
	var result="";
	var state=loxia.getObject("type", data);
	if(state==0){
		result+="<a style='color:red' href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button enable'>"+nps.i18n("LABLE_ENABLE")+"</a>";
	}else{
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button disable'>"+nps.i18n("LABLE_DISABLE")+"</a>";
	}
	result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button update'>"+nps.i18n("LABLE_UPDATE")+"</a>";
	
	return result;
}

// 格式化日期
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"."+(date.getMonth()+1)+"."+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}

//空字符串
function isBlankString(str) {
    return str == null || str == undefined || $j.trim(str).length == 0;
}

// 刷新表格
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}