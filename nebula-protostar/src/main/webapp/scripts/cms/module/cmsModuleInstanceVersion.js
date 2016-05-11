$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"发布",
	"TO_DEL":"删除",
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
	"OVER_PUBLISH_INSTANCE_SCOPE":"超出了发布实例时间范围",
	"PAGE_INSTANCE_NOT_PUBLISHED":"页面实例没有发布",
	"PAGE_VERSION_NOT_SAVE":"页面实例版本没有保存",
	"CONFIRM_PUBLISH_PAGE":"确定要发布该页面吗",
	"CONFIRM_CANCEL_PUBLISH_PAGE":"确定要取消发布该页面吗",
	"CONFIRM_RETURN":"是否离开该页面",
	"MODULE_REPUBLISH":"再次发布",
	"TO_COPY":"复制",
	"CONFIRM_COPY_PAGE":"确定复制页面版本吗",
	"COPY_SUCCESS":"复制成功"
});

// 列表
var urlList = base+'/cmsModuleInstanceVersion/page.json'; 
// 启用或禁用
var PUBLISH_URL = base + "/module/publishModuleInstanceVersion.json";
//取消发布
var cancelPublishModuleInstanceVersion = base + "/module/cancelPublishPageInstanceVersion.json";
//删除
var removeURl = base+ "/cmsModuleInstanceVersion/removeByIds.json";

/** 去新增页面 */
var newPageUrl = base + '/cmsModuleInstanceVersion/addCmsModuleInstanceVersion.htm';

var copyModuleInstanceVersionUrl=  base + '/module/copyModuleInstanceVersion.json';

//function drawEditor(data){
//	var id=loxia.getObject("id", data);
//	var isPublished=loxia.getObject("isPublished",data);
//	var stime = formatDate(loxia.getObject("startTime", data));
//	var etime = formatDate(loxia.getObject("endTime", data));
//	var url = loxia.getObject("url", data);
//	var instanceid = loxia.getObject("instanceId", data);
//	var update="<a href='javascript:void(0);' val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
//	var enable ;
//	if(isPublished==false || isPublished=="false"){
//		enable="<a href='javascript:void(0);' url='"+url+"' instanceid='"+instanceid+"' stime='"+stime+"' etime='"+etime+"' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
//	}else{
//		enable="<a href='javascript:void(0);' url='"+url+"' instanceid='"+instanceid+"' stime='"+stime+"' etime='"+etime+"' val='"+id+"' class='func-button enable'>再次发布</a>";
//		enable=enable+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
//	}
//    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
//	return enable+update+del;
//}


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
	var isPublished = loxia.getObject("isPublished", data);
	if (isPublished) {
		var startTime = loxia.getObject("startTime", data);
		var endTime = loxia.getObject("endTime", data);
		var ctime = new Date().getTime();
		if(startTime== null && endTime==null){
			return '<span>已发布</span>';
		}
		if(startTime== null && endTime != null && ctime<endTime){
			return '<span>已发布</span>';
		}else if(startTime== null && endTime != null && ctime > endTime){
			return '<span>已结束</span>';
		}
		if(startTime != null && endTime == null && ctime >= startTime){
			return '<span>已发布</span>';
		}
		
		if(startTime != null && endTime != null && ctime >= startTime){
			if(ctime > startTime && ctime<endTime ){
				return '<span>已发布</span>';
			}
			if(ctime>endTime){
				return '<span>已结束</span>';
			}
			if(ctime< startTime){
				return '<span>发布中</span>';
			}
		}
	
		return '<span>发布中</span>';
	} else {
		return '<span>未发布</span>';
	}

}

function formatDate(obj){
	if(obj==null||obj==''){
		return "";
	}
	else{
		var date=new Date(obj);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
};

function parseDate(str){     
	   if(typeof str == 'string'){     
	     var results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);  
	     if(results && results.length>3){  
	        return  new Date(parseInt(results[1],10),(parseInt(results[2],10) -1),parseInt(results[3],10));      
	     }  
	     results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);     
	     if(results && results.length>6)     
	       return new Date(parseInt(results[1],10),parseInt(results[2],10) -1,parseInt(results[3],10),parseInt(results[4],10),parseInt(results[5],10),parseInt(results[6],10));      
	     results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2})\.(\d{1,9}) *$/);     
	     if(results && results.length>7)     
	       return new Date(parseInt(results[1],10),parseInt(results[2],10) -1,parseInt(results[3],10),parseInt(results[4],10),parseInt(results[5],10),parseInt(results[6],10),parseInt(results[7],10));      
	   }     
	   return null;     
};
//获取日期格式
function sformatDate(data){
	var obj = loxia.getObject("startTime", data);
	if(obj==null||obj==''){
		return "";
	}
	else{
		var date=new Date(obj);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

//获取日期格式
function eformatDate(data){
	var obj = loxia.getObject("endTime", data);
	if(obj==null||obj==''){
		return "";
	}
	else{
		var date=new Date(obj);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
};

/** 发布按钮 */
function fnModuleVersionPublish(data, args, caller){
	var versionId = data.id;
	var dialog = $j(".cms-publish-dialog").attr("wid",versionId);
	dialog.find(".starttime").val(sformatDate(data.stime));
	dialog.find(".endtime").val(eformatDate(data.etime));
	dialog.dialogff({type:'open',close:'in',width:'450px', height:'350px'});

}
/** 取消发布 */
function fnModuleVersionCancel(data, args, caller){
	var versionId = data.id;
	var json = {'versionId':versionId};
	nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_CANCEL_PUBLISH_PAGE'), function(){
		var data = loxia.syncXhr(cancelPublishModuleInstanceVersion, json, {type:'post'});
		if(data.isSuccess){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('INFO_CANCLE_START_SUCCESS'));
			refreshData();
		}else{
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('INFO_CANCEL_START_FAIL'));
		}
	});
}

function fnDeleteModuleVersion(data, args, caller){
	confirmDelete(data.id);
}

function drawEditOperatorItem(data) {

	var versionId = loxia.getObject("id", data);
	var result = "";
		result = [ {
			label : nps.i18n("TO_UPDATE"),
			type : "href",
			content : base+"/cmsModuleInstanceVersion/updateModuleInstanceVersion.htm?versionId="+versionId
		}, {
			label : nps.i18n("TO_DEL"),
			type : "jsfunc",
			content : "fnDeleteModuleVersion"
		}, {
			label : nps.i18n("TO_COPY"),
			type : "jsfunc",
			content : "fnCopyPageVersion"
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
			content : "fnModuleVersionPublish"
		}, {
			label : nps.i18n("TO_CANCEL"),
			type : "jsfunc",
			content : "fnModuleVersionCancel"
		}];
	}else{
		result = [ {
			label : nps.i18n("TO_ENABLE"),
			type : "jsfunc",
			content : "fnModuleVersionPublish"
		}, {
			label : nps.i18n("TO_CANCEL"),
			type : "jsfunc",
			content : "fnModuleVersionCancel"
		}];
	}
	return result;

}

/** 复制 **/
function fnCopyPageVersion(data, args, caller){
	var versionId = data.id;
	var dialog = $j(".cms-copy-dialog").attr("wid",versionId);
	dialog.dialogff({type:'open',close:'in',width:'450px', height:'250px'});
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
			}, {
				name : "name",
				label : "模块版本名称",
				width : "35%"
			}, {
				name : "startTime",
				label : "开始时间",
				width : "15%",
				template : "sformatDate",
				sort : [ "start_time asc", "start_time desc" ]
			}, {
				name : "endTime",
				label : "结束时间",
				width : "15%",
				template : "eformatDate",
				sort : [ "end_time asc", "end_time desc" ]
			}, {
				name : "isPublished",
				label : "状态",
				width : "10%",
				template:"isPublished" 	
			}, {
				label :nps.i18n("drawEditor"),
				width : "10%", 			 
				type : "oplist",
				oplist : drawEditOperatorItem
			}, {
				width : "10%", 			 
				type : "oplist",
				oplist : drawEditPublishItem
			}
			],
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
		 window.location.href =base+"/cmsModuleInstanceVersion/updateModuleInstanceVersion.htm?versionId="+id;
	 });
	//执行批量删除
	$j(".button.butch.delete").click(function() {
		confirmDelete(null);
	});

     //删除
	 $j("#tableList").on("click",".func-button.delete",function(){
		 var curObject=$j(this);
		 confirmDelete(curObject.attr("val"));
	 });
	 
	 /** 新增 */
	$j('.add').click(function(){
		window.location.href = newPageUrl+'?moduleId='+$j('#instanceId').val();
	});
	 /** 返回 */
	$j('.return').click(function(){
		var templateId = $j("#templateId").val();
		window.location.href = base + "/cmsModuleInstance/list.htm?templateId="+templateId;
	});
	

	
	/** 发布 */
	$j('.cms-publish-dialog').on('click', '.confrim', function(){
		var dialog = $j(this).parent().parent();
		var versionId = dialog.attr('wid');
		var stime = dialog.find(".starttime").val();
		var etime=dialog.find(".endtime").val();
		if(stime == "" || stime == undefined || etime == "" || etime == undefined){
			nps.info(nps.i18n('PROMPT_INFO'), "发布时间和发布结束时间为必填项");
			return;
		}
		var sdate;
		
		
		if(etime!=""){
			var date=parseDate(etime);
			if((date.getTime()-new Date().getTime()) <= 0){
				nps.info(nps.i18n('PROMPT_INFO'), "结束时间应大于当前时间");
				return;
			}
		}
		if(etime!="" && stime!=""){
			var edate=parseDate(etime);
			sdate=parseDate(stime);
			if((edate.getTime()-sdate.getTime()) <= 0){
				nps.info(nps.i18n('PROMPT_INFO'), "结束时间应大于开始时间");
				return;
			}
		}

		var json = {'versionId':versionId,"startTime":stime,"endTime":etime};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_PUBLISH_PAGE'), function(){
			var data = loxia.syncXhr(PUBLISH_URL, json, {type:'post'});
			if(data.isSuccess){
				$j('.cms-publish-dialog').dialogff({type : 'close'});
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('INFO_START_SUCCESS'));
				refreshData();
			}else{
				nps.info(nps.i18n('PROMPT_INFO'),data.description);
			}
		});
	});
	
	/** 复制版本 */
	$j('.cms-copy-dialog').on('click', '.confrim', function(){
		var dialog = $j(this).parent().parent();
		var versionId = dialog.attr('wid');
		var copyname = dialog.find(".copyname").val();
		if(versionId == undefined || versionId == ""){
			nps.info(nps.i18n('PROMPT_INFO'), "页面版本不存在");
			return;
		}	
		if(copyname == undefined || copyname == ""){
			nps.info(nps.i18n('PROMPT_INFO'), "请输入复制页面版本的名称");
			return;
		}
	
		var json = {'versionId':versionId,"name":copyname};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_COPY_PAGE'), function(){
			var data = loxia.syncXhr(copyModuleInstanceVersionUrl, json, {type:'post'});
			if(data.isSuccess){
				$j('.cms-copy-dialog').dialogff({type : 'close'});
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('COPY_SUCCESS'));
				refreshData();
			}else{
				nps.info(nps.i18n('PROMPT_INFO'),data.description);
			}
		});
	});
	
	/** 关闭 */
	$j('.cms-copy-dialog').on('click', '.close', function(){
		$j('.dialog-close').click();
	});
	
	/** 关闭 */
	$j('.cms-publish-dialog').on('click', '.close', function(){
		$j('.dialog-close').click();
	});

	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
	 
}); 


