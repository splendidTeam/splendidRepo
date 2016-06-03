$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"未找到数据",
	"PROMPT_INFO":"提示信息",
	"PAGE_INSTANCE_CODE":"页面编码",
	"PAGE_INSTANCE_NAME":"页面名称",
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
	"OPERATOR_MODIFY":"修改",
	"OPERATOR_DELETE":"删除",
	"PAGE_PUBLISH":"发布",
	"PAGE_REPUBLISH":"再次发布",
	"PAGE_CANCEL":"取消发布",
	"VERSION_MANAGE":"版本管理"
});

var findPagfeInstanceListByTemplateIdUrl = base+'/page/findPageInstanceListByTemplateId.json';
/** 去新增页面 */
var newPageUrl = base + '/page/toNewAddPage.htm';
/** 修改页面实例 */
var updatePageInstanceUrl = base + '/page/updatePageInstance.htm';
/** 删除页面实例(单个) */
var removePageInstanceByIdUrl = base + '/page/removePageInstanceById.json';
/** 删除页面实例(批量) */
var removePageInstanceByIdsUrl = base + '/page/removePageInstanceByIds.json';
/** 实例版本管理 **/
var instanceVersionByIdUrl = base + '/page/findPageInstanceVersionListByInstanceId.htm'; 

var publishPageInstanceUrl = base + '/page/publishPageInstance.json';

var cancelPublishPageInstanceUrl = base + '/page/cancelPublishPageInstance.json';

var pageTemplateListUrl = base + '/newcms/pageTemplateList.htm';




/** 刷新数据 */
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

/** loxiasimpletable.drawcheckbox */
function drawCheckbox(data){
	return '<input type="checkbox" value="'+loxia.getObject("id", data)+'"/>';
}
/** loxiasimpletable.operateTemplate */
function operateTemplate(data){
	var id = loxia.getObject("id", data);
	var html = '';
	html += '<a href="javascript:void(0);" class="func-button versionmanage" value="'+id+'" title="版本管理"><span>版本管理</span></a>';
	return html;
}

/** loxiasimpletable.lifecycleTemplate */
function lifecycleTemplate(data){
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
}
function formatDate(obj){
	if(obj==null||obj==''){
		return "";
	}
	else{
		var date=new Date(obj);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}
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

	var pageId = loxia.getObject("id", data);
	var result = "";
		result = [ {
			label : nps.i18n("OPERATOR_MODIFY"),
			type : "href",
			content : updatePageInstanceUrl+'?pageId='+pageId
		}, {
			label : nps.i18n("OPERATOR_DELETE"),
			type : "jsfunc",
			content : "fnDeletePage"
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
			content : "fnPagePublish"
		}, {
			label : nps.i18n("PAGE_CANCEL"),
			type : "jsfunc",
			content : "fnPageCancel"
		}];
	}else{
		result = [ {
			label : nps.i18n("PAGE_PUBLISH"),
			type : "jsfunc",
			content : "fnPagePublish"
		}, {
			label : nps.i18n("PAGE_CANCEL"),
			type : "jsfunc",
			content : "fnPageCancel"
		}];
	}
	return result;

}

/** 删除(单个) */
function fnDeletePage(data, args, caller){
	var json = {"pageId":data.id};
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

/** 发布按钮 */
function fnPagePublish(data, args, caller){
//	alert(data.id);
//	alert(data.startTime);
//	var me = $j(this);
//	var pageId = me.attr('value');
	var dialog = $j(".cms-publish-dialog").attr("wid",data.id);
	dialog.attr("url",data.url);
	dialog.attr("tmpid",data.templateId);
	dialog.find(".starttime").val(sformatDate(data));
	dialog.find(".endtime").val(eformatDate(data));
	dialog.dialogff({type:'open',close:'in',width:'450px', height:'350px'});

};

/** 取消发布 */
function fnPageCancel(data, args, caller){
	var pageId = data.id;
	var json = {'pageId':pageId};
	nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_CANCEL_PUBLISH_PAGE'), function(){
		var data = loxia.syncXhr(cancelPublishPageInstanceUrl, json, {type:'post'});
		if(data.isSuccess){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('UNPUBLISH_SUCCESS'));
			refreshData();
		}else{
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('UNPUBLISH_FAILURE'));
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
		}, {
			name : "code",
			label : nps.i18n("PAGE_INSTANCE_CODE"),
			width : "10%",
			sort : [ "code asc", "code desc" ]
		}, {
			name : "name",
			label : nps.i18n("PAGE_INSTANCE_NAME"),
			width : "25%",
			sort : [ "name asc", "name desc" ]
		}, {
			name : "url",
			label : nps.i18n("PAGE_INSTANCE_URL"),
			width : "15%",
			sort : [ "url asc", "url desc" ]
		}, {
			name : "startTime",
			label : "开始时间",
			width : "10%",
			template : "sformatDate",
			sort : [ "startTime asc", "startTime desc" ]
		}, {
			name : "endTime",
			label : "结束时间",
			width : "10%",
			template : "eformatDate",
			sort : [ "startTime asc", "startTime desc" ]
		}, {
			name : "ispublished",
			label : nps.i18n("ISPUBLISHED"),
			width : "10%",
			sort : [ "lifecycle asc", "lifecycle desc" ],
			template : "lifecycleTemplate"

		}, {
			label : nps.i18n("OPERATE"),
			width : "5%",
			template : "operateTemplate"
		},{
			width : "5%",
			type : "oplist",
			oplist : drawEditOperatorItem
		},{
			width : "5%",
			type : "oplist",
			oplist : drawEditPublishItem
		}],
		dataurl : findPagfeInstanceListByTemplateIdUrl
	});
	refreshData();
	
	/** 搜索 */
	$j('.func-button.search').click(function(){
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
	
	/** 新增页面 */
	$j('.add').click(function(){
		window.location.href = newPageUrl+'?templateId='+$j('#templateId').val();
	});
	

	/** 实例版本管理 */
	$j('#table1').on('click','.versionmanage', function(){
		var pageId = $j(this).attr('value');
		window.location.href = instanceVersionByIdUrl+'?instanceId='+pageId;
	});
	
	/** 删除(批量) */
	$j('.butch.delete').click(function(){
		var pageIds = new Array();
		var checkboxs = $j('#table1').find('tbody').find('input:checkbox:checked');
		$j.each(checkboxs, function(index, obj){
			pageIds[index] = obj.value;
		});
		
		if(pageIds == ''){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('SELECT_DELETE_PAGE_INSTANCE'));
			return;
		}
		
		var json = {'pageIds':pageIds};
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
		var pageId = dialog.attr('wid');
		var url = dialog.attr('url');
		var tmpid = dialog.attr('tmpid');
		if(checkPageInstanceUrl(url,pageId,tmpid) == loxia.ERROR){
			return;
		}
		var stime = dialog.find(".starttime").val();
		var sdate;
		
		var etime=dialog.find(".endtime").val();
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
		var json = {'pageId':pageId,"startTime":stime,"endTime":etime};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_PUBLISH_PAGE'), function(){
			var data = loxia.syncXhr(publishPageInstanceUrl, json, {type:'post'});
			if(data.isSuccess){
				$j('.cms-publish-dialog').dialogff({type : 'close'});
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PUBLISH_SUCCESS'));
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
	
	
	/** 返回 */
	$j('.return').click(function(){
		window.location.href = pageTemplateListUrl;
	});
});
