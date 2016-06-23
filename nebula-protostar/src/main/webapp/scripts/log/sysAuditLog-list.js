$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的SysAuditLog么？",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_DELETE_SUCCESS":"删除记录成功!",
	"INFO_ADD_SUCCESS":"添加成功!",
	"INFO_EDIT_SUCCESS":"修改成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_CANCLE_START_SUCCESS":"取消启用成功!",
    "INFO_CANCEL_START_FAIL":"取消启用失败!",
	"TO_CANCEL":"禁用",
	"YES":"有效",
	"NO":"无效",
	"CONFIRM_EN":"确定要启用SysAuditLog?",
	"CONFIRM_DIS":"确定要禁用SysAuditLog?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的SysAuditLog",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空"
});

// 列表
var urlList = base+'/backlog/sysAuditLog/page.json';

// 刷新数据
function refreshData() {
	$j("#tableList").loxiasimpletable("refresh");
}


$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	// 通过loxiasimpletable动态获取数据
	$j("#tableList").loxiasimpletable({
		page : true,
		size : 20,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
			{
				name : "uri",
				label : "请求功能",
				width : "10%"
			},
			{
				name : "parametersLabel",
				label : "请求参数",
				width : "10%"
			},
			{
				name : "method",
				label : "请求类型",
				width : "10%"
			},
			{
				name : "responseCode",
				label : "请求结果",
				width : "10%"
			},
			{
				name : "exception",
				label : "异常",
				width : "10%"
			},
			{
				name : "operatorLabel",
				label : "操作人",
				width : "10%"
			},
			{
				name : "ip",
				label : "请求来源",
				width : "10%"
			},
			{
				name : "createTime",
				label : "操作时间",
				width : "10%",
				formatter : "formatDate"
			}],
		dataurl : urlList
	});
	//加载数据
	refreshData();
	// 筛选数据
	$j(".func-button.search").click(function(){
		 $j("#tableList").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
}); 

//获取日期
function formatDate(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}


