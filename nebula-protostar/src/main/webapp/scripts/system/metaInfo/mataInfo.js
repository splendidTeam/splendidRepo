$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的系统参数么？",
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
	"CONFIRM_EN":"确定要启用系统参数?",
	"CONFIRM_DIS":"确定要禁用系统参数?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的系统参数",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空",
	"update-remove-not-exit":"修改的数据不存在或已经被人删除",
	"meta-add":"系统参数-新增",
	"meta-edit":"系统参数-修改",
	"confirm_active_meta":"确定要生效系统参数",
	"active_success":"生效成功",
	"code":"参数",
	"describe":"参数说明",
	"value":"参数值",
	"status":"状态"
});

// 列表
var urlList = base+'/mataInfo/page.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/mataInfo/enableOrDisableByIds.json";
//删除
var removeURl = base+ "/mataInfo/removeByIds.json";
function drawEditor(data){
	var id=loxia.getObject("id", data);
	var lifecycle=loxia.getObject("lifecycle",data);
	var update="<a href='javascript:void(0);' val='"+id+"'  class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
	var enable ;
	if(lifecycle == 0){
		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	}else{
		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
	}
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	
	return update+enable+del;
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
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("EN_SUCCESS"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("DIS_SUCCESS"));
					}
					refreshData();
				} else {
					if(activeMark==1){
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("EN_FAIL"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DIS_FAIL"));
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
function validateInput(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j("."+cls).each(function(){
		var val =$j(this).val();
		if($j(this).hasClass("ui-loxia-error")){
			result = false;
		}
		if(val==null || val==""){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
	 });
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
				label : nps.i18n("code"),
				width : "15%"
			},
			{
				name : "describe",
				label : nps.i18n("describe"),
				width : "25%"
			},
			{
				name : "value",
				label : nps.i18n("value"),
				width : "40%"
			},
			{
				name : "lifecycle",
				label : nps.i18n("status"),
				width : "5%",
				type : "yesno"
			},
			{
				label :nps.i18n("drawEditor"),
				width : "10%", 			 
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
	 //新建
	 $j(".addMataInfo").on("click",function(){
		 $j("#dialog-meta-select").attr("wid","");
		 setEmpty("input_add");
		 $j("#wc_title").text(nps.i18n("meta-add"));
		 $j("#confirm_warning").attr("disabled",false); 
		 $j(".proto-dialog-content .code").attr("readonly",false);
		 $j("#dialog-meta-select").dialogff({type:'open',close:'in',width:'350px',height:'250px'});
	 });
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
	 //修改
	 $j("#tableList").on("click",".func-button.editor",function(){
		 var me=$j(this);
		 var id = me.attr("val");
		 $j("#dialog-meta-select").attr("wid",id);
		 var data = nps.syncXhrPost(base+"/mataInfo/findById.json", {'id': id});
		 if(data==null){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-remove-not-exit"));
			 return;
		 }
		 $j(".proto-dialog-content .code").val(data.code);
		 $j(".proto-dialog-content .value").val(data.value);
		 $j(".proto-dialog-content .describe").val(data.describe);//参数说明
		 $j("#confirm_warning").attr("disabled",false); 
		 $j(".proto-dialog-content .code").attr("readonly",true);
		 $j("#wc_title").text(nps.i18n("meta-edit"));
		 $j("#dialog-meta-select").dialogff({type:'open',close:'in',width:'350px',height:'250px'});
	 });
	 //保存操作
	 $j("#confirm_warning").on("click",function(){
		 if(validateInput("input_add")==false){
			 return;
		 }
		 var me = $j(this);
		 var id = $j("#dialog-meta-select").attr("wid");;
		 var code = $j(".proto-dialog-content .code").val();
		 var value = $j(".proto-dialog-content .value").val();
		 var describe = $j(".proto-dialog-content .describe").val();//参数说明
		 
		 var json = {
				 'code':code,
				 'value':value,
				 'describe':describe
		 };
		 if(id!=null && id!=""){
			 json.id = id;
		 }
		 me.attr("disabled",true); 
		 nps.asyncXhrPost(base+"/mataInfo/save.json", json,{successHandler:function(data, textStatus){
			 me.removeAttr("disabled"); 
			if (data.isSuccess) {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_EDIT_SUCCESS"));
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ADD_SUCCESS"));
					 setEmpty("input_add");
					 $j("#dialog-meta-select").dialogff({type : 'close'});
				 }
				 refreshData();
			} else {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				 }
			}
			
		}});
	 });
	//告警取消按钮
	 $j("#cancel").click(function(){
		$j("#dialog-meta-select").dialogff({type:'close'});
	 });
	 $j(".mataInfoActive").click(function(){
		 nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("confirm_active_meta"),function(){
			 var json={};
			 nps.asyncXhrPost(base+"/mataInfo/activeMetaInfo.json",json,{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("active_success"));
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				}
				
			}});
		 });
		
	 });
	 
}); 


