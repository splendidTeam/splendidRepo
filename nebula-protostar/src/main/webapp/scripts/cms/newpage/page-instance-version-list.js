$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"未找到数据",
	"PROMPT_INFO":"提示信息",
	"PAGE_INSTANCE_VERSION_NAME":"页面版本名称",
	"PAGE_INSTANCE_URL":"页面url地址",
	"ISPUBLISHED":"状态",
	"OPERATE":"操作",
	"CONFIRM_DELETE_PAGE":"确定删除该页面吗",
	"CONFIRM_PUBLISH_PAGE":"确定要发布该页面吗",
	"CONFIRM_CANCEL_PUBLISH_PAGE":"确定要取消发布该页面吗",
	"DELETE_SUCCESS":"删除成功",
	"DELETE_FAILURE":"删除失败",
	"PUBLISH_SUCCESS":"发布成功",
	"PUBLISH_FAILURE":"发布失败",
	"PAGE_URL_EXISTS":"页面url({0})已存在",
	"UNPUBLISH_SUCCESS":"取消发布成功",
	"UNPUBLISH_FAILURE":"取消发布失败",
	"SELECT_DELETE_PAGE_INSTANCE":"请选择要删除的页面实例",
	"OVER_PUBLISH_INSTANCE_SCOPE":"超出了发布实例时间范围",
	"PAGE_INSTANCE_NOT_PUBLISHED":"该页面实例没有发布",
	"OPERATOR_MODIFY":"修改",
	"OPERATOR_DELETE":"删除",
	"PAGE_PUBLISH":"发布",
	"PAGE_REPUBLISH":"再次发布",
	"PAGE_CANCEL":"取消发布",
	"OPERATOR_COPY":"复制",
	"CONFIRM_COPY_PAGE":"确定复制页面版本吗",
	"COPY_SUCCESS":"复制成功"
	
});

var findPageInstanceVersionListByInstanceIdUrl = base+'/page/findPageInstanceVersionListByInstanceId.json';
/** 去新增页面 */
var newPageUrl = base + '/page/toNewAddVersionPage.htm';
/** 修改页面实例 */
var updatePageInstanceVersionUrl = base + '/page/updatePageInstanceVersion.htm';
/** 删除页面实例(单个) */
var removePageInstanceByIdUrl = base + '/page/removePageInstanceVersionById.json';
/** 删除页面实例(批量) */
var removePageInstanceByIdsUrl = base + '/page/removePageInstanceVersionByIds.json';
/** 实例版本管理 **/
var instanceVersionByIdUrl = base + '/page/findPageInstanceVersionListByInstanceId'; 

var publishPageInstanceVersionUrl = base + '/page/publishPageInstanceVersion.json';

var cancelPublishPageInstanceVersion = base + '/page/cancelPublishPageInstanceVersion.json';

var pagePageInstanceListUrl = base + '/page/findPageInstanceListByTemplateId.htm';
/**检测发布实例时间**/
var checkPublishTimeUrl = base + '/page/checkPublishTime.json';

var copyPageInstanceVersionUrl = base + '/page/copyPageInstanceVersion.json';

/** 刷新数据 */
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

/** loxiasimpletable.drawcheckbox */
function drawCheckbox(data){
	return '<input type="checkbox" value="'+loxia.getObject("id", data)+'"/>';
}
///** loxiasimpletable.operateTemplate */
//function operateTemplate(data){
//	var isPublished = loxia.getObject("published", data);
//	var id = loxia.getObject("id", data);
//	var stime = formatDate(loxia.getObject("startTime", data));
//	var etime = formatDate(loxia.getObject("endTime", data));
//	var url = loxia.getObject("url", data);
//	var instanceid = loxia.getObject("instanceId", data);
//	var html = '';
//	if(isPublished){
//		html += '<a href="javascript:void(0);" class="func-button publish" url="'+url+'" instanceid="'+instanceid+'" stime="'+stime+'" etime="'+etime+'" value="'+id+'" title="再次发布"><span>再次发布</span></a>';
//		html += '<a href="javascript:void(0);" class="func-button unpublish" value="'+id+'" title="取消发布"><span>取消发布</span></a>';
//	}else {
//		html += '<a href="javascript:void(0);" class="func-button publish"  url="'+url+'" instanceid="'+instanceid+'"  stime="'+stime+'" etime="'+etime+'" value="'+id+'" title="发布"><span>发布</span></a>';
//	}
//	html += '<a href="javascript:void(0);" class="func-button update" value="'+id+'" title="修改"><span>修改</span></a>';
//	html += '<a href="javascript:void(0);" class="func-button deleteOne" value="'+id+'" title="删除"><span>删除</span></a>';
//	
//	return html;
//}

/** loxiasimpletable.lifecycleTemplate */
function lifecycleTemplate(data){
	var isPublished = loxia.getObject("published", data);
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
/**
 * 检察页面实例url是否可用
 */
function checkPageInstanceUrl(url,pageId,tmpId){
	$j('#isSaved').val(false);
	var json = {'url':url, 'pageId':pageId,'tmpId':tmpId};
	var data = loxia.syncXhr(base+'/page/checkPublishPageInstanceUrl.json', json, {type:'post'});
	if(data != '' && data != null){
		nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PAGE_URL_EXISTS', url));
		return loxia.ERROR;
	}
	return loxia.SUCCESS;
};

/**
 * 检测发布实例版本的时间是否在实例发布时间之内
 */
function checkPublishTime(versionId, stime, etime){
	var json = {'versionId':versionId, 'startTime':stime,'endTime':etime};
	var data = loxia.syncXhr(checkPublishTimeUrl, json, {type:'post'});
	if(!data.isSuccess){
		if(data.errorCode == 1){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PAGE_INSTANCE_NOT_PUBLISHED'));
		}else{
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('OVER_PUBLISH_INSTANCE_SCOPE'));
		}
		return loxia.ERROR;
	}
	return loxia.SUCCESS;
}

function supportType(type){
	var html="";
	if(type==0){
		html="综合";
	}
	if(type==1){
		html="pc";
	}
	if(type==2){
		html="mobile";
	}
	return html;
};

function drawEditOperatorItem(data) {

	var versionId = loxia.getObject("id", data);
	var result = "";
		result = [ {
			label : nps.i18n("OPERATOR_MODIFY"),
			type : "href",
			content : updatePageInstanceVersionUrl+'?versionId='+versionId
		}, {
			label : nps.i18n("OPERATOR_DELETE"),
			type : "jsfunc",
			content : "fnDeletePageVersion"
		}, {
			label : nps.i18n("OPERATOR_COPY"),
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
			label : nps.i18n("PAGE_REPUBLISH"),
			type : "jsfunc",
			content : "fnPageVersionPublish"
		}, {
			label : nps.i18n("PAGE_CANCEL"),
			type : "jsfunc",
			content : "fnPageVersionCancel"
		}];
	}else{
		result = [ {
			label : nps.i18n("PAGE_PUBLISH"),
			type : "jsfunc",
			content : "fnPageVersionPublish"
		}, {
			label : nps.i18n("PAGE_CANCEL"),
			type : "jsfunc",
			content : "fnPageVersionCancel"
		}];
	}
	return result;

}

/** 发布按钮 */
function fnPageVersionPublish(data, args, caller){
	var versionId = data.id;
	var dialog = $j(".cms-publish-dialog").attr("wid",versionId);
	dialog.find(".starttime").val(sformatDate(data));
	dialog.find(".endtime").val(eformatDate(data));
	dialog.dialogff({type:'open',close:'in',width:'450px', height:'350px'});

};

/** 取消发布 */
function fnPageVersionCancel(data, args, caller){
	var versionId = data.id;
	var json = {'versionId':versionId};
	nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_CANCEL_PUBLISH_PAGE'), function(){
		var data = loxia.syncXhr(cancelPublishPageInstanceVersion, json, {type:'post'});
		if(data.isSuccess){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('UNPUBLISH_SUCCESS'));
			refreshData();
		}else{
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('UNPUBLISH_FAILURE'));
		}
	});
};

/** 复制 **/
function fnCopyPageVersion(data, args, caller){
	var versionId = data.id;
	var dialog = $j(".cms-copy-dialog").attr("wid",versionId);
	dialog.dialogff({type:'open',close:'in',width:'450px', height:'250px'});
}

/** 删除(单个) */
function fnDeletePageVersion(data, args, caller){
	var versionId = data.id;
	var json = {"versionId":versionId};
	nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_DELETE_PAGE'),function(){
		var data = loxia.syncXhr(removePageInstanceByIdUrl, json, {type:'post'});
		if(data.isSuccess){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('DELETE_SUCCESS'));
			refreshData();
		}else{
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('DELETE_FAILURE'));	
		}
	});
};

$j(document).ready(function() {
	
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "searchForm",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckbox"
		},{
			name : "name",
			label : nps.i18n("PAGE_INSTANCE_VERSION_NAME"),
			width : "35%",
			sort : [ "name asc", "name desc" ]
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
			name : "ispublished",
			label : nps.i18n("ISPUBLISHED"),
			width : "10%",
			sort : [ "lifecycle asc", "lifecycle desc" ],
			template : "lifecycleTemplate"

		}, {
			label : nps.i18n("OPERATE"),
			width : "10%",
			type : "oplist",
			oplist : drawEditOperatorItem
		}, {
			width : "10%",
			type : "oplist",
			oplist : drawEditPublishItem
		}],
		dataurl : findPageInstanceVersionListByInstanceIdUrl
	});
	refreshData();
	
	/** 搜索 */
	$j('.func-button.search').click(function(){
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
	
	/** 新增实例版本页面 */
	$j('.addversion').click(function(){
		window.location.href = newPageUrl+'?instanceId='+$j('#instanceId').val();
	});
	
	
	/** 删除(批量) */
	$j('.butch.delete').click(function(){
		var versionIds = new Array();
		var checkboxs = $j('#table1').find('tbody').find('input:checkbox:checked');
		$j.each(checkboxs, function(index, obj){
			versionIds[index] = obj.value;
		});
		
		if(versionIds == ''){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('SELECT_DELETE_PAGE_INSTANCE'));
			return;
		}
		
		var json = {'versionIds':versionIds};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_DELETE_PAGE'),function(){
			var data = loxia.syncXhr(removePageInstanceByIdsUrl, json, {type:'post'});
			if(data.isSuccess){
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('DELETE_SUCCESS'));
				refreshData();
			}else{
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('DELETE_FAILURE'));	
			}
		});
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
		
		if(checkPublishTime(versionId, stime,etime) == loxia.ERROR){
			return; 
		}
		var json = {'versionId':versionId,"startTime":stime,"endTime":etime};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_PUBLISH_PAGE'), function(){
			var data = loxia.syncXhr(publishPageInstanceVersionUrl, json, {type:'post'});
			if(data.isSuccess){
				$j('.cms-publish-dialog').dialogff({type : 'close'});
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PUBLISH_SUCCESS'));
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
			var data = loxia.syncXhr(copyPageInstanceVersionUrl, json, {type:'post'});
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
	$j('.cms-publish-dialog').on('click', '.close', function(){
		$j('.dialog-close').click();
	});
	
	/** 关闭 */
	$j('.cms-copy-dialog').on('click', '.close', function(){
		$j('.dialog-close').click();
	});
	

	/** 取消发布 */
	$j('#table1').on('click', '.unpublish', function(){
		var versionId = $j(this).attr('value');
		var json = {'versionId':versionId};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_PUBLISH_PAGE'	), function(){
			var data = loxia.syncXhr(cancelPublishPageInstanceVersion, json, {type:'post'});
			if(data.isSuccess){
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('UNPUBLISH_SUCCESS'));
				refreshData();
			}else{
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('UNPUBLISH_FAILURE'));
			}
		});
	});
	
	/** 返回 */
	$j('.return').click(function(){
		window.location.href = pagePageInstanceListUrl+'?templateId='+$j("#templeteId").val();
	});
});
