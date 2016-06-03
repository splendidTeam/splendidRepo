$j(document).ready(function() {
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
	// 动态获取数据库商品信息表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 20,
		nodatamessage : '<span>无数据</span>',
		form : "searchForm",
		cols : [
				{name:"code",label:"商品编码",width:"10%"},
				{name:"title",label:"商品名称",width:"20%"},
				{name:"pushTime",label:"上架时间",formatter:"formatDate",width:"10%"},
				{name:"pushOperatorName",label:"上架操作人",width:"10%"},
				{name:"soldOutTime",label:"下架时间",formatter:"formatDate",width:"10%"},
				{name:"soldOutOperatorName",label:"下架操作人",width:"10%"},
				{name:"activeTime",label:"在架时长",width:"10%",formatter:"formatDay"}
				],
		dataurl : base + "/backlog/itemOperateLogList.json"
	});
	refreshData();
	
	
	// 筛选
	$j(".func-button.search").click(function() {
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
});


//刷新表格数据
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

//获取日期
function formatDate(val) {
	if (val == null || val == '' || val == '-28800000') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}


//获取天数
function formatDay(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var days = Math.floor(val/86400); 
		var hours = Math.floor((val%86400)/3600); 
		var minutes = Math.floor(((val%86400)%3600)/60);  
		var seconds = Math.floor(((val%86400)%3600)%60);
		return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
	}
}
