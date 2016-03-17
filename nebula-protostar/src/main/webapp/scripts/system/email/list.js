$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的邮件模板么？",
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
	"CONFIRM_EN":"确定要启用邮件模板?",
	"CONFIRM_DIS":"确定要禁用邮件模板?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的邮件模板",
	"code":"编码",
	"name":"名称",
	"desc":"描述",
	"type":"类型",
	"subject":"主题",
	"body":"正文",
	"lifecycle":"状态",
	"createTime":"创建时间",
	"modifyTime":"修改时间",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空"
});

// 列表
var urlList = base+'/email/pagination.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/email/enOrDisable.json";
var editURl = base+ "/email/edit.htm";
function drawEditor(data){
	var id=loxia.getObject("id", data);
	//lifecycle
	var activeMark=loxia.getObject("lifecycle",data);
	var update="<a href='"+ editURl+"?id="+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
	var enable ;
	if(activeMark == 0){
		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	}else{
		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
	}
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	
	return update+enable+del;
}


// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

// 获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		if(val.createTime==null||val.createTime==''){
			return "&nbsp;";
		}
		var date=new Date(val.createTime);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

function lifecycle(data){
	var type = loxia.getObject("lifecycle", data);	
	var result = "";
	if(type == 1){
		result = nps.i18n("YES");
	}else{
		result = nps.i18n("NO");
	}
 	return result; 
} 

function type(data){
	var type = loxia.getObject("type", data);	
	var result = "";
	if(type == 1){
		result = "html";
	}else{
		result = "text";
	}
 	return result; 
} 

/**
 * checkbox
 */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}
/**
 * 有效或无效优惠劵
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
//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 
/**
 * 批量删除优惠劵
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
	  	 nps.asyncXhrPost(base+"/email/remove.json", json,{successHandler:function(data, textStatus){
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
function validateInput(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j("."+cls).each(function(){
		var val =$j(this).val();
		if(val==null || val==""){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
	 });
	return result; 
}

function removeCls(cls){
	if(cls==null || cls==""){
		return; 
	}
	$j("."+cls).each(function(){
		$j(this).removeClass("ui-loxia-error");
	 });
}

function setEmpty(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j("."+cls).each(function(){
		$j(this).val(null);
	 });
	return result; 
}

function getBody(data){
	var body = loxia.getObject("body", data);
	body="<xmp>"+body+"</xmp>";
 	return body; 
} 

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	// 通过loxiasimpletable动态获取数据
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [{
				label : "<input type='checkbox'  />",
				width : "5%",
				template : "drawCheckbox"
			},{
				name : "code",
				label : nps.i18n("code"),
				width : "5%"
			}, {
				name : "name",
				label : nps.i18n("name"),
				width : "10%"
			}, {
				name : "description",
				label : nps.i18n("desc"),
				width : "10%" ,
			}, {
				name : "type",
				label : nps.i18n("type"),
				width : "5%" , 
				template: "type"
			}, {
				name : "subject",
				label : nps.i18n("subject"),
				width : "10%" 
			}, {
				name : "lifecycle",
				label : nps.i18n("lifecycle"),
				type:'yesno',
				width : "5%" 
			}, {
				name : "createTime",
				label : nps.i18n("createTime"),
				formatter:"formatDate",
				width : "10%" 
			}, {
				name : "modifyTime",
				formatter:"formatDate",
				label : nps.i18n("modifyTime"),
				width : "10%" 
			},{
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
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});  
	//执行批量删除
	$j(".button.delete.batch").click(function() {
		confirmDelete(null);
	});
     //启用
	$j("#table1").on("click",".func-button.enable",function(){
		  var curObject=$j(this);
		  updateActive(curObject.attr("val"),1);
	 });
     //禁用
	 $j("#table1").on("click",".func-button.disable",function(){
		 var curObject=$j(this);
		 updateActive(curObject.attr("val"),0);
	 });
     //删除
	 $j("#table1").on("click",".func-button.delete",function(){
		 var curObject=$j(this);
		 confirmDelete(curObject.attr("val"));
	 });
	 //新建
	 $j(".addItemSortScore").on("click",function(){
		 window.location.href=base+"/email/edit.htm";
	 });
}); 




