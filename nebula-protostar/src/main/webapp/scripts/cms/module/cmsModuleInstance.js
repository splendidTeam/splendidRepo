$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"发布",
	"TO_DEL":"删除",
	"TO_VERSION":"版本管理",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的模块实例么？",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_DELETE_SUCCESS":"删除记录成功!",
	"INFO_ADD_SUCCESS":"添加成功!",
	"INFO_EDIT_SUCCESS":"修改成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_CANCLE_START_SUCCESS":"取消启用成功!",
    "INFO_CANCEL_START_FAIL":"取消启用失败!",
	"TO_CANCEL":"取消发布",
	"YES":"有效",
	"NO":"无效",
	"CONFIRM_EN":"确定要发布模块实例?",
	"CONFIRM_DIS":"确定要取消发布实例模块?",
	"EN_SUCCESS":"发布成功",
	"EN_FAIL":"发布失败",
	"DIS_FAIL":"取消发布失败",
	"DIS_SUCCESS":"取消发布成功",
	"SELECT_EMAIL":"请选择需要删除的模块实例",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空",
	"MODULE_PUBLISH":"发布",
	"MODULE_REPUBLISH":"再次发布",
});

// 列表
var urlList = base+'/cmsModuleInstance/page.json'; 
// 启用或禁用
var PUBLISH_URL = base + "/module/publishModuleInstance.json";
//删除
var removeURl = base+ "/cmsModuleInstance/removeByIds.json";
//模块版本管理
moduleVersionByIdUrl = base + "/cmsModuleInstanceVersion/list.htm";
/** 去新增页面 */
var newPageUrl = base + '/cmsModuleInstance/addCmsModuleInstance.htm';
function drawEditor(data){
	var id=loxia.getObject("id", data);
//	var isPublished=loxia.getObject("isPublished",data);
//	var update="<a href='javascript:void(0);' val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
//	var enable ;
//	if(isPublished==false || isPublished=="false"){
//		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
//	}else{
//		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>再次发布</a>";
//		enable=enable+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
//	}
//    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
    var manage = "<a href='javascript:void(0);' val='"+id+"' class='func-button versionmanage'>"+nps.i18n("TO_VERSION")+"</a>";
	return manage;
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
function updateActive(id,activeMark){
	var msg = "";
	if(activeMark== 1){
		msg = nps.i18n("CONFIRM_EN");
	}else{
		msg = nps.i18n("CONFIRM_DIS");
	}
	nps.confirm(nps.i18n("INFO_TITLE_DATA"),msg,function(){
		var json={"moduleId":id,"status":activeMark}; 
	  	nps.asyncXhrPost(PUBLISH_URL, json,{successHandler:function(data, textStatus){
			var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					if(activeMark==0){
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("DIS_SUCCESS"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("EN_SUCCESS"));
					}
					refreshData();
				} else {
					if(activeMark==0){
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
function isPublished(data){
	var isPublished=loxia.getObject("isPublished", data);
	if(isPublished==true || isPublished=="true"){
		return "已发布";
	}else{
		return "未发布";
	}
}

function fnModulePublish(data, args, caller){
	 updateActive(data.id,1);
}

function fnModuleCancel(data, args, caller){
	updateActive(data.id,0); 
};

//禁用
$j("#tableList").on("click",".func-button.disable",function(){
	 var curObject=$j(this);
	 updateActive(curObject.attr("val"),0);
});
//删除单行
function fnDeleteModule(data, args, caller){
	confirmDelete(data.id);
}

function drawEditOperatorItem(data) {

	var moduleId = loxia.getObject("id", data);
	var result = "";
		result = [ {
			label : nps.i18n("TO_UPDATE"),
			type : "href",
			content : base+"/cmsModuleInstance/updateModuleInstance.htm?moduleId="+moduleId
		}, {
			label : nps.i18n("TO_DEL"),
			type : "jsfunc",
			content : "fnDeleteModule"
		}];

	return result;

}

function drawEditPublishItem(data) {
	var isPublished = loxia.getObject("isPublished", data);
	var result = "";

	//var state = loxia.getObject("lifecycle", data);
	if(isPublished){
		result = [ {
			label : nps.i18n("MODULE_REPUBLISH"),
			type : "jsfunc",
			content : "fnModulePublish"
		}, {
			label : nps.i18n("TO_CANCEL"),
			type : "jsfunc",
			content : "fnModuleCancel"
		}];
	}else{
		result = [ {
			label : nps.i18n("TO_ENABLE"),
			type : "jsfunc",
			content : "fnModulePublish"
		}, {
			label : nps.i18n("TO_CANCEL"),
			type : "jsfunc",
			content : "fnModuleCancel"
		}];
	}
	return result;

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
				name : "code",
				label : "模块编码",
				width : "20%"
			},
			{
				name : "name",
				label : "模块名称",
				width : "50%"
			},
			{
				name : "isPublished",
				label : "状态",
				width : "10%",
				template:"isPublished" 	
			},
			{
				label :nps.i18n("drawEditor"),
				width : "5%", 			 
				template:"drawEditor" 
			},{
				width : "5%",
				type : "oplist",
				oplist : drawEditOperatorItem
			},{
				width : "5%",
				type : "oplist",
				oplist : drawEditPublishItem
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
	
	 $j("#tableList").on("click",".editor",function(){
		 var id = $j(this).attr("val");
		 window.location.href =base+"/cmsModuleInstance/updateModuleInstance.htm?moduleId="+id;
	 });
	//执行批量删除
	$j(".button.butch.delete").click(function() {
		confirmDelete(null);
	});
//     //启用
//	$j("#tableList").on("click",".func-button.enable",function(){
//		  var curObject=$j(this);
//		  updateActive(curObject.attr("val"),1);
//	 });
//     //禁用
//	 $j("#tableList").on("click",".func-button.disable",function(){
//		 var curObject=$j(this);
//		 updateActive(curObject.attr("val"),0);
//	 });
//     //删除
//	 $j("#tableList").on("click",".func-button.delete",function(){
//		 var curObject=$j(this);
//		 confirmDelete(curObject.attr("val"));
//	 });
	 //版本管理
	 $j("#tableList").on("click",".func-button.versionmanage",function(){
		 var moduleId=$j(this).attr('val');
		 window.location.href = moduleVersionByIdUrl+'?moduleId='+moduleId;
	 });
	 
	 /** 新增 */
	$j('.add').click(function(){
		window.location.href = newPageUrl+'?templateId='+$j('#templateId').val();
	});
	 /** 返回 */
	$j('.return').click(function(){
		window.location.href = base + "/newcms/moduleTemplateList.htm";
	});
	
	
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
}); 


