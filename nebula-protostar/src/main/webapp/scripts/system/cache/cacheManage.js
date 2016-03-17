$j.extend(loxia.regional['zh-CN'],{
    "CACHE_CLEAR":"清除缓存",
    "CACHE_ITEM":"缓存项",
    "CACHE_SCREENSHOT":"截图",
    "CACHE_DESC":"描述",
    "CACHE_OPERATION":"操作"
});

//表格数据
var optionListUrl='/system/cacheList.json';

//复选框
function checkboxs(data, args, idx){
	return "<input type='checkbox' name='id' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

//缓存项截图
function drawImg(data, args, idx){
	var imgurl=loxia.getObject("img", data);
	var html="<table cellspacing='0' cellpadding='0' border='0'><tr><td>";
	html += "<a href='"+imgurl+"' target='_blank'>";
	html += "<img src='"+imgurl+"' width='100' height='50'/>";
	html += "</a>";
	html += "</td></tr></table>";
	return html;
}

//清除缓存操作
function clear(data, args, idx){ 
	var str="";
	var key = loxia.getObject("key", data);
	str+="<a href='javascript:void(0);' class='func-button delete'  onclick='deleteCache(\""+key+"\")'>"+nps.i18n("CACHE_CLEAR")+"</a>";
	return str;
}

//删除缓存
function deleteCache(key){
	if(key == ''){
		nps.info("提示信息","要清除的缓存项 key不存在！");
		return;
	}
	nps.confirm("提示信息", "确认清除缓存？", function() {
    	var json={"key":key};
	    var _d = nps.asyncXhrPost("/system/removeCacheItem.json", json,{
			successHandler : function(data, textStatus) {
	            nps.info("提示信息","清除缓存成功！");
			}
		});
	});
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	$j("#table1").loxiasimpletable({
		page: false,
		size:10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[//{label:"<input type='checkbox'/>",width:"5%", template:"checkboxs"},
		{
			name:"name",
			label:nps.i18n("CACHE_ITEM"),
			width:"20%"},
		{
			name:"img",
			label:nps.i18n("CACHE_SCREENSHOT"),
			width:"30%",
			template:"drawImg"},
		{
			name:"desc",
			label:nps.i18n("CACHE_DESC"),
			width:"30%"},
		{
			label:nps.i18n("CACHE_OPERATION"),
			width:"15%",
			template:"clear"}
		],
		dataurl : optionListUrl
	});
    refreshData();
    
    //筛选列表
    $j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	 });
	
});
