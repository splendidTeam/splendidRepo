$j(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
 // 刷新数据
    function refreshData() {
    	$j(".border-grey.logs").loxiasimpletable("refresh");
    }
    // 动态获取数据库商品信息表
	$j(".border-grey.logs").loxiasimpletable({
		page : true,
		size : 20,
		nodatamessage :'<span>无数据</span>',
		form : "searchForm",
		cols : [
				{name:"code",label:"任务编码",width:"10%"},
				{name:"description",label:"任务描述",width:"10%"},
				{name:"beanName",label:"Bean Name",width:"10%"},
				{name:"methodName",label:"方法名称",width:"15%"},
				{name:"beginTime",label:"开始时间",formatter:"formatDate",sort : [ "log.begin_time asc", "log.begin_time desc" ],width:"10%"},
				{name:"endTime",label:"结束时间",formatter:"formatDate",sort : [ "log.end_time asc", "log.end_time desc" ],width:"10%"},
				{name:"executionTime",label:"执行时长(ms)",sort : [ "log.execution_time asc", "log.execution_time desc" ],width:"10%"}
				],
		dataurl : base + "/backlog/schedulerLog/list.json"
	});
	refreshData();
	// 筛选数据
	$j(".func-button.search").click(function(){
		 $j(".border-grey.logs").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	//重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
	 $j("input[name='q_long_min'],input[name='q_long_max']").bind("keyup",function(){
		 !/^[\d]+$/.test($j(this).val())?$j(this).val($j(this).val().replace(/[^\d]/g,"")):undefined;
	 })
})
//获取日期
function formatDate(val) {
	if (val == null || val == '' || val == '-28800000') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds()+"."+date.getMilliseconds();
	}
}