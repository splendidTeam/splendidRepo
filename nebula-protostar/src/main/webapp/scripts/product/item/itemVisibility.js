$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的商品可见性么？",
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
	"CONFIRM_EN":"确定要启用商品可见性?",
	"CONFIRM_DIS":"确定要禁用商品可见性?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的商品可见性",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空",
	"update-remove-not-exit":"修改的数据不存在或已经被人删除",
	"memFilterName":"会员筛选器",
	"itemFilterName":"商品筛选器",
	"name":"筛选器名称",
	"text":"表达式名称"
});

// 列表
var urlList = base+'/itemVisibility/page.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/itemVisibility/enableOrDisableByIds.json";
//删除
var removeURl = base+ "/itemVisibility/removeByIds.json";
function drawEditor(data){
	var id=loxia.getObject("id", data);
	var update="<a href='javascript:void(0);'  val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	return update+del;
}


// 刷新数据
function refreshData() {
	$j("#tableList").loxiasimpletable("refresh");
}

function refreshDataMem() {
	$j("#mem_tbl").loxiasimpletable("refresh");
}
function refreshDataItem() {
	$j("#item_tbl").loxiasimpletable("refresh");
}

/**
 * checkbox
 */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}
function drawRadio(data, args, idx) {
	return "<input type='radio' class='mem-radio-ok' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}
function drawRadioItem(data, args, idx) {
	return "<input type='radio' class='item-radio-ok' name='id' value='"
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
						nps.info(nps.i18n("INFO_TITLE_DATA"),  nps.i18n("EN_SUCCESS"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DIS_SUCCESS"));
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
				name : "memFilterName",
				label : nps.i18n("memFilterName"),
				width : "15%"
			},
			{
				name : "itemFilterName",
				label : nps.i18n("itemFilterName"),
				width : "15%"
			},
			{
				label :nps.i18n("drawEditor"),
				width : "10%", 			 
				template:"drawEditor" 
			}],
		dataurl : urlList
	});
	//会员筛选器列表
	$j("#mem_tbl").loxiasimpletable({
		page : true,
		size : 5,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"mem_searchForm",
		cols : [{
				label : "",
				width : "5%",
				template : "drawRadio"
			},{
				name : "name",
				label : nps.i18n("name"),
				width : "5%"
			}, {
				name : "text",
				label : nps.i18n("text"),
				width : "10%"
			}],
		dataurl : base+'/member/memberCustomgroupList.json'
	});
	
	//商品筛选器列表
	$j("#item_tbl").loxiasimpletable({
		page : true,
		size : 5,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"item_searchForm",
		cols : [{
				label : "",
				width : "5%",
				template : "drawRadioItem"
			},{
				name : "name",
				label :  nps.i18n("name"),
				width : "5%"
			}, {
				name : "text",
				label :  nps.i18n("text"),
				width : "10%"
			}],
		dataurl : base+'/product/customProductComboList.json'
	});
	//加载数据
	refreshData();
	refreshDataMem();
	refreshDataItem();
	
	// 筛选数据
	$j(".func-button.search.visibilisty").click(function(){
		 $j("#tableList").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});  
	$j(".func-button.search.mem").click(function(){
		 $j("#mem_tbl").data().uiLoxiasimpletable.options.currentPage=1;
		 $j("#mem_tbl").loxiasimpletable("refresh");
	});
	$j(".func-button.search.item").click(function(){
		 $j("#item_tbl").data().uiLoxiasimpletable.options.currentPage=1;
		 $j("#item_tbl").loxiasimpletable("refresh");
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
	 $j(".addItemVisibility").on("click",function(){
		 var dialog = $j("#dialog-visibility");
		 dialog.find("#wc_title").html("商品可见性-新增");
		 dialog.attr("wid",null);
		 dialog.find(".mem-name").attr("memid",null).val(null);
		 dialog.find(".item-name").attr("itemid",null).val(null);
		 dialog.dialogff({type:'open',close:'in',width:'400px',height:'250px'});
	 });
	 //修改 editor
	 $j("#tableList").on("click",".editor",function(){
		 var me = $j(this);
		 var dialog = $j("#dialog-visibility");
		 var id = me.attr("val");
		 dialog.attr("wid",id);
		 dialog.find("#wc_title").html("商品可见性-修改");
		 var data = nps.syncXhrPost(base+"/itemVisibility/findByid.json", {'id': id});
		 if(data==null){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-remove-not-exit"));
			 return;
		 }
		 dialog.find(".mem-name").attr("memid",data.memFilterId).val(data.memFilterName);
		 dialog.find(".item-name").attr("itemid",data.itemFilterId).val(data.itemFilterName);
		 dialog.dialogff({type:'open',close:'in',width:'400px',height:'250px'});
	 });
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
	 
	 //会员筛选器选择 
	 $j("#dialog-visibility").on("click",'.select1',function(){
		 $j("#dialog-mem-select").dialogff({type:'open',close:'in',width:'600px',height:'600px'});
		 $j("#dialog-mem-select .proto-dialog-content").css("height",null);
	 });
	 
	 //商品筛选器选择 
	 $j("#dialog-visibility").on("click",'.select2',function(){
		 $j("#dialog-item-select").dialogff({type:'open',close:'in',width:'600px',height:'600px'});
		 $j("#dialog-item-select .proto-dialog-content").css("height",null);
	 });
	//会员确定按钮
	$j("body").on("click",'.mem-radio-ok',function(){
		var me = $j(this);
		var data=me.parent().parent().find('td').eq(1).text();; 
		var id =$j(this).attr("value");
		if(data==null || data==""){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("select-email"));
			return;
		}
		$j("#dialog-visibility .mem-name").val(data);
		$j("#dialog-visibility .mem-name").attr("memid",id);
		$j("#dialog-visibility .mem-name").removeClass("ui-loxia-error");
		$j("#dialog-mem-select").dialogff({type:'close'});
			
		});
	//商品确定按钮
	$j("body").on("click",'.item-radio-ok',function(){
		var me = $j(this);
		var data=me.parent().parent().find('td').eq(1).text();; 
		var id =$j(this).attr("value");
		if(data==null || data==""){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("select-email"));
			return;
		}
		$j("#dialog-visibility .item-name").val(data);
		$j("#dialog-visibility .item-name").attr("itemid",id);
		$j("#dialog-visibility .item-name").removeClass("ui-loxia-error");
		$j("#dialog-item-select").dialogff({type:'close'});
		
	});
	
	 //保存
	 $j("#dialog-visibility").on("click",'#confirm',function(){
		 var me =$j(this);
		 var parent = me.parent().parent();
		 var memFilterId = parent.find(".mem-name").attr("memid");
		 var itemFilterId = parent.find(".item-name").attr("itemid");
		 if(validateInput("ismandatory")==false){
			 return;
		 }
		 var id = parent.attr("wid");
		 var json = {"memFilterId":memFilterId,"itemFilterId":itemFilterId};
		 if(id != null && id != ""){
			 json.id = id;
		 }
		 me.attr("disabled",true); 
		 nps.asyncXhrPost(base+"/itemVisibility/save.json", json,{successHandler:function(data, textStatus){
			 	me.attr("disabled",false); 
				if (data.isSuccess) {
					 if(id != null && id != ""){
						 nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_EDIT_SUCCESS")+
						 		"如果希望列表页生效最新的设置，还需要刷新solr");
					 }else{
						 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ADD_SUCCESS")+
								 "如果希望列表页生效最新的设置，还需要刷新solr");
					 }
					 parent.dialogff({type:'close'});
					refreshData();
				} else {
					 if(id != null && id != ""){
						 nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
					 }else{
						 nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
					 }
				}
				
		}});
	 });

	 //取消
	 $j("#dialog-visibility").on("click",".delete",function(){
		 $j("#dialog-visibility").dialogff({type:'close'});
	 });
}); 


