$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的模块么？",
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
	"CONFIRM_EN":"确定要启用模块?",
	"CONFIRM_DIS":"确定要禁用模块?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的模块",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空",
	"TO_MANAGER":"管理模块"
});

// 列表
var urlList = base+'/cmsModuleTemplate/page.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/cmsModuleTemplate/enableOrDisableByIds.json";
//删除
var removeURl = base+ "/cmsModuleTemplate/removeByIds.json";

var editUrl = base+ "/cmsModuleTemplate/edit.htm";
var toManagerUrl=base+"/cmsModuleInstance/list.htm";
function drawEditor(data){
	var result="";  
	var id=loxia.getObject("id", data);
	var tomanager="<a href='"+ toManagerUrl+"?templateId="+id+"' class='func-button '>"+nps.i18n("TO_MANAGER")+"</a>";
	var tomodify ="<a href='"+ editUrl+"?id="+id+"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	var todelete ="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	var editTemp ="<a href='javascript:void(0);' val='"+id+"' class='func-button editTemp'>编辑模块</a>";
	result +=tomanager+editTemp + tomodify + todelete;
	return result;
}

// 刷新数据
function refreshData() {
	$j("#tableList").loxiasimpletable("refresh");
}

/**
 * checkbox
 */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}
/**
 * 有效或无效
 */
function updateActive(val,activeMark){
	var msg = "";
	if(activeMark== 1){
		msg = nps.i18n("CONFIRM_EN");
	}else{
		msg = nps.i18n("CONFIRM_DIS");
	}
	nps.confirm(nps.i18n("INFO_TITLE_DATA"),msg,function(){
		var json={"ids":val,"state":activeMark}; 
	  	nps.asyncXhrPost(ACTIVATION_URL, json,{successHandler:function(data, textStatus){
			var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					if(activeMark==1){
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("DIS_SUCCESS"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("EN_SUCCESS"));
					}
					refreshData();
				} else {
					if(activeMark==1){
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DIS_FAIL"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("EN_FAIL"));
					}
				}
		 }});
	  	
	});
}
/**
 * 批量删除
 */
function confirmDelete(id){
	nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
		var json ;
		if(id != null){
			 json={"ids":id}; 
		}else{
			var checkbox=$j("input[name='id']");
			var data=""; 
			  $j.each(checkbox, function(i,val){   
				  if(val.checked){
					  data=data+$j(this).val()+",";
				  }
			 }); 
			  if(data!=""){
				  data=data.substr(0,data.length-1);
			  }  
			  if(data == ""){
				  nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("SELECT_EMAIL"));
				  return;
			  }
			 json={"ids":data}; 
			 
		}
	  	 nps.asyncXhrPost(removeURl, json,{successHandler:function(data, textStatus){
			var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
					refreshData();
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
				}
		 }});
	});
}
//模板截图
function drawImg(data, args, idx){
	var imgurl=loxia.getObject("img", data);
	var html= "<a href='"+customBaseUrl+imgurl+"' target='_blank'>";
	html += "<img src='"+customBaseUrl+imgurl+"' width=30 height=30/>";
	html += "</a>";
	return html;
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
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [{
				label : "<input type='checkbox'  />",
				width : "5%",
				template : "drawCheckbox"
			},
			{
				name : "img",
				label : "模块截图",
				width : "10%",
				template:"drawImg"
			},
			{
				name : "name",
				label : "模块名称",
				width : "10%"
			},
			{
				label :nps.i18n("drawEditor"),
				width : "15%", 			 
				template:"drawEditor" 
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
	//执行批量删除
	$j(".button.delete.batch").click(function() {
		confirmDelete(null);
	});
     //启用
	$j("#tableList").on("click",".func-button.enable",function(){
		  var curObject=$j(this);
		  updateActive(curObject.attr("val"),1);
	 });
     //禁用
	 $j("#tableList").on("click",".func-button.disable",function(){
		 var curObject=$j(this);
		 updateActive(curObject.attr("val"),0);
	 });
     //删除
	 $j("#tableList").on("click",".func-button.delete",function(){
		 var curObject=$j(this);
		 confirmDelete(curObject.attr("val"));
	 });
	 
	 $j("#tableList").on("click",".func-button.editTemp",function(){
		window.location.href=base +"/module/editCmsTemplate.htm?id="+$j(this).attr("val");
	 });
	
	 //新建
	 $j(".addCmsModuleTemplate").on("click",function(){
		 window.location.href=editUrl;
	 });
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
}); 


