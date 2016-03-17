$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的国际化语言么？",
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
	"CONFIRM_EN":"确定要启用国际化语言?",
	"CONFIRM_DIS":"确定要禁用国际化语言?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的国际化语言",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空"
});

// 列表
var urlList = base+'/i18nLang/page.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/i18nLang/enableOrDisableByIds.json";
//删除
var removeURl = base+ "/i18nLang/removeByIds.json";
function drawEditor(data){
	var id=loxia.getObject("id", data);
	var lifecycle=loxia.getObject("lifecycle",data);
	var update="<a href='javascript:void(0);' val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
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
		$j(this).removeClass("ui-loxia-error");
	 });
	$j("#dialog-i18n-win .sort").val(null);
	return result; 
};
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
};
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
				name : "key",
				label : "编码",
				width : "10%"
			},
			{
				name : "value",
				label : "名称",
				width : "10%"
			},
			{
				name : "tokenizer",
				label : "分词器",
				width : "5%"
			},
			{
				name : "defaultlang",
				label : "默认语言",
				width : "10%",
				type:"yesno"
			},
			{
				name : "lifecycle",
				label : "状态",
				width : "10%",
				type:"yesno"
			},
			{
				name : "sort",
				label : "排序",
				width : "10%"
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
	 $j(".addI18nLang").on("click",function(){
		 $j("#dialog-i18n-win").attr("wid","");
		 setEmpty("input_add");
		 $j("#confirm_warning").removeAttr("disabled");
		 $j(".proto-dialog-content .key").attr("readonly",false);
		 $j("#dialog-i18n-win").dialogff({type:'open',close:'in',width:'330px',height:'290px'});
	 });
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
	 
	//保存操作
	 $j("#confirm_warning").on("click",function(){
		 if(validateInput("input_add")==false){
			 return;
		 }
		 var me = $j(this);
		 var id = $j("#dialog-i18n-win").attr("wid");;
		 var key = $j("#dialog-i18n-win .key").val();
		 var value = $j("#dialog-i18n-win .value").val();
		 var tokenizer = $j("#dialog-i18n-win .tokenizer").val();
		 var defaultlang = $j("#dialog-i18n-win .defaultlang").val();
		 var sort =$j("#dialog-i18n-win .sort").val();
		 //ui-loxia-error
		 if($j("#dialog-i18n-win .sort").hasClass('ui-loxia-error')){
			 return ;
		 }
		 var json = {
				 'key':key,
				 'value':value,
				 'tokenizer':tokenizer,
				 'defaultlang':defaultlang,
				 'sort':sort
		 };
		 if(id!=null && id!=""){
			 json.id = id;
		 }
		 me.attr("disabled",true); 
		 nps.asyncXhrPost(base+"/i18nLang/save.json", json,{successHandler:function(data, textStatus){
			 me.removeAttr("disabled"); 
			if (data.isSuccess) {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_EDIT_SUCCESS"));
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ADD_SUCCESS"));
				 }
				 $j("#dialog-i18n-win").dialogff({type : 'close'});
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
	 
	 //修改
	 $j("#tableList").on("click",".func-button.editor",function(){
		 var me=$j(this);
		 var id = me.attr("val");
		 $j("#dialog-i18n-win").attr("wid",id);
		 var data = nps.syncXhrPost(base+"/i18nLang/findByid.json", {'id': id});
		 if(data==null){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-remove-not-exit"));
			 return;
		 }
		 $j(".proto-dialog-content .key").val(data.key);
		 $j(".proto-dialog-content .value").val(data.value);
		 $j(".proto-dialog-content .tokenizer").val(data.tokenizer);
		 $j(".proto-dialog-content .defaultlang").find("option[value='"+data.defaultlang+"']").attr("selected","selected");
		 $j(".proto-dialog-content .sort").val(data.sort);
		 
		 $j("#confirm_warning").removeAttr("disabled");
		 $j(".proto-dialog-content .key").attr("readonly",true);
		// $j("#wc_title").text(nps.i18n("meta-edit"));
		 $j("#dialog-i18n-win").dialogff({type:'open',close:'in',width:'330px',height:'290px'});
	 });
}); 


