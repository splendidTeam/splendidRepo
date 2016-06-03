$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_SELECT_ALL":"全选",
	    "LABEL_TEMPLATE_PIC":"模板截图", 
	    "LABEL_TEMPLATE_NAME":"模板名称",
	    "LABEL_IS_PUBLIC":"公共头尾",
	    "LABEL_TEMPLATE_OPERATE":"操作",	   
	    "TO_MANAGER":"管理页面",
	    "TO_UPDATE":"修改",
	    "DELETE":"删除",
	    "ITEM_TIP_NOSEL_DEL" : "请选择要删除的模板",
	    "TEMPLATE_DELETE_CHECK_INFO" : "所选模板包含页面实例，请先删除页面实例",
	    "ITEM_INFO_CONFIRM" : "提示",
	    "INFO_TITLE_DATA":"提示信息",
	    "TEMPLATE_INFO_CONFIRM":"确认信息",
	    "CONFIRM_DELETE" : "确认删除吗",
	    "NPS_DELETE_SUCCESS" : "删除成功",
		"NPS_DELETE_FAILURE" : "删除失败",
		"EDIT_TEMPLATE":"编辑模板"
	    
});

//页面模板查询
var pageTemplateListUrl = base+'/cms/pageTemplateAllList.json'; 
//新建模板url
var createTemplateUrl = base+'/cms/createtemplate.htm';
//管理页面url
var toManagerUrl = base+'/page/findPageInstanceListByTemplateId.htm';
//修改模板页面url
var toUpdateUrl = base+'/cms/updatetemplate.htm';

//检查是否有管理页面
var checkInstanceUrl = '/cms/checkInstance.htm';
//删除模板页面url
var toDeleteUrl = base+'/cms/deletetemplate.htm';
//批量删除
var butchDeleteUrl = base + '/cms/butchremove.json';
//查看html
var viewHtmlUrl = '/cms/viewhtml.htm';
//更新缓存的数据到数据库
var megerCacheUrl = '/cms/megerCacheUrl.json';

function checkboxs(data, args, idx){
	return "<input type='checkbox' name='id' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
}
function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	var tomanager="<a href='"+ toManagerUrl+"?templateId="+id+"' class='func-button '>"+nps.i18n("TO_MANAGER")+"</a>";
	result +=tomanager;
	return result;
} 

function drawCheckbox(data, args, idx){
	var result=""; 
	var state=loxia.getObject("useCommonHeader", data);
	if(state){
		return "<input name='limitValue' type='checkbox' class='limitValue' checked='checked' disabled='disabled'/>";
	}
	return "<input name='limitValue' type='checkbox' class='limitValue' disabled='disabled'/>";
	 
}
//模板截图
function drawImg(data, args, idx){
	var imgurl=loxia.getObject("img", data);
	var html="<table cellspacing='0' cellpadding='0' border='0'><tr><td>";
	html += "<a href='"+customBaseUrl+imgurl+"' target='_blank'>";
	html += "<img src='"+customBaseUrl+imgurl+"' width='30' height='30'/>";
	html += "</a>";
	html += "</td></tr></table>";
	return html;
}

//模板名称
function nameToHtml(data, args, idx){
	var id=loxia.getObject("id", data);
	var name=loxia.getObject("name", data);
	var html="<a onclick=\"loxia.openPage('"+viewHtmlUrl+"?id="+id
		+"');\" style='cursor:pointer;'>"+name+"</a>";
	return html;
}
function supportType(data, args, idx){
	var type= loxia.getObject("supportType", data);
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
}

//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

function drawEditTemplateItem(data) {
	var id = loxia.getObject("id", data);
	var result = "";
	//var state = loxia.getObject("lifecycle", data);
		result = [ {
			label : nps.i18n("EDIT_TEMPLATE"),
			type : "href",
			content : base+"/cms/editCmsTemplate.htm?id="+id
		}, {
			label : nps.i18n("TO_UPDATE"),
			type : "href",
			content : toUpdateUrl+"?id="+id
		}, {
			label : nps.i18n("DELETE"),
			type : "jsfunc",
			content : "fnDeleteTemplate"
		}];
	return result;

}

//删除单行
function fnDeleteTemplate(data, args, caller){
        //var curObject=$j(this);
        var json = {
        		"ids" : data.id
        };
        var data = nps.syncXhrPost(checkInstanceUrl, json);
        if (! data.isSuccess) {
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("TEMPLATE_DELETE_CHECK_INFO"));
        	return;
        }
        nps.confirm(nps.i18n("TEMPLATE_INFO_CONFIRM"),nps.i18n("CONFIRM_DELETE"), function(){
            
        	var _d = nps.syncXhr(toDeleteUrl, json,{type: "GET"});
        	if(_d.isSuccess){
        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DELETE_SUCCESS")); 
        		  refreshData(); 
        	}
        	else{
        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DELETE_FAILURE"));
        	}
            	
        });
}

$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});

	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
		{
			label:"<input type='checkbox'/>"+nps.i18n("LABEL_SELECT_ALL"),
			width:"5%", 
			template:"checkboxs"
		},{
			name : "img",
			label : nps.i18n("LABEL_TEMPLATE_PIC"),
			width : "15%" ,
			template:"drawImg"
			
		},{
			name : "name",
			label : nps.i18n("LABEL_TEMPLATE_NAME"),
			width : "40%",
			template:"nameToHtml"
		},{
			name : "supportType",
			label : "支持类型",
			width : "10%",
			template:"supportType"
		}, {
			name : "useCommonHeader",
			label : nps.i18n("LABEL_IS_PUBLIC"),
			width : "10%" ,
			template:"drawCheckbox"
		},{
			name : nps.i18n("LABEL_TEMPLATE_OPERATE"),
			label : nps.i18n("LABEL_TEMPLATE_OPERATE"),
			width : "10%", 			 
			template : "drawEditor"
		},{
			width : "10%", 			 
			type : "oplist",
			oplist : drawEditTemplateItem
		}],
		dataurl : pageTemplateListUrl
	});
	
	refreshData();
	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	
	// 批量逻辑删除
	$j(".button.butch.delete").click(function() {
		var data = "";
		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				data += ",";
			}
			data += $j(this).val();

		});
		if (data == "") {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("ITEM_TIP_NOSEL_DEL"));
			return;
		}
		
		var json = {
				"ids" : data
		};
		
		var ischeck = nps.syncXhrPost(checkInstanceUrl, json);
		if (! ischeck.isSuccess) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("TEMPLATE_DELETE_CHECK_INFO"));
			return;
		}
		nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE"), function() {
			var _d = loxia.syncXhr(butchDeleteUrl, json, {
				type : "GET"
			});
			if (_d == 'success') {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NPS_DELETE_SUCCESS"));
				refreshData();
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NPS_DELETE_FAILURE"));
			}
		});
	});
	
	
	//新建的返回
	 $j(".button.orange.addTemplate").click(function(){
	        window.location.href=createTemplateUrl;
	 });
	 
});