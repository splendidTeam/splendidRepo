$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的权限么？",
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
	"CONFIRM_EN":"确定要启用权限?",
	"CONFIRM_DIS":"确定要禁用权限?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的权限",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空",
	"TO_CANCEL":"禁用",
	"fname":"功能名称",
	"furl":"功能utl",
	"oper":"操作",
	"add-s":"新增成功",
	"update-s":"修改成功",
	"exportsql":"导出sql"
});

// 列表
var urlList = base+'/auth/page.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/auth/enableOrDisableById.json";
//删除
var removeURl = base+ "/auth/removePrivilegeByIds.json";
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
	var exportsql = "<a href='javascript:void(0);' val='"+id+"' class='func-button exportsql'>"+nps.i18n("exportsql")+"</a>";
	return update+enable+del+exportsql;
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
		var json={"id":val,"state":activeMark}; 
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
function drawType(data, args, idx){
	var orgTypeId=loxia.getObject("orgTypeId", data);
	return $j(".org-type").find("option[value='"+orgTypeId+"']").text()+"<span orgTypeId='"+orgTypeId+"' ></span>";
}
function fdrawEditor(data){
	var id=loxia.getObject("id", data);
	var update="<a href='javascript:void(0);' val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	return update+del;
}
function validateInput(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j(cls).each(function(){
		var val =$j(this).val();
		if($j(this).hasClass("ui-loxia-error")){
			result = false;
		}
		if(val==null || $j.trim(val)==""){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
	 });
	return result; 
}//
var num = 0;
function getUrls(){
	 var trs = $j("#table1").find("tbody").find("tr");
	 var urls =[];
	 for ( var i = 0; i < trs.length; i++) {
		 var spans = trs.eq(i).find("span");
		 var id = trs.eq(i).find(".editor").attr("val");
		 var url = {
				 id:id,
				 "url":spans.eq(1).html(),
				 "description":spans.eq(0).html()
		 	 };
		 urls.push(url);
	}
	return urls;
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
				name : "acl",
				label : "权限编码",
				width : "10%"
			},
			{
				name : "name",
				label : "权限名称",
				width : "10%"
			},
			{
				name : "groupName",
				label : "分组名称",
				width : "10%"
			},
			{
				name : "orgTypeId",
				label : "组织类型",
				width : "5%",
				template:"drawType" 
			},
			{
				name : "description",
				label : "描述",
				width : "10%"
			},
			{
				name : "lifecycle",
				label : "状态",
				width : "5%",
				type:'yesno'
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
	
	$j("#table1").loxiasimpletable({
			cols : [{
					name : "description",
					label : nps.i18n("fname"),
					width : "20%"
				}, {
					name : "url",
					label : nps.i18n("furl"),
					width : "20%"
				},{
					label :nps.i18n("oper"),
					width : "15%", 			 
					template:"fdrawEditor" 
				}]
	});
	$j("#table1").loxiasimpletable("refresh",[]);
	 
	 $j(".gname-select").change(function(){
		 var me = $j(this);
		 var p = me.parent();
		 p.find("input").val(me.val());
		 p.find("input").removeClass("ui-loxia-error");
	 });
	 
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
	 $j(".addPrivilege").on("click",function(){
		 var dialog = $j("#dialog-auth-dialog");
		 dialog.find(".function-edit").hide();
		 dialog.attr("parentid","");
		 dialog.dialogff({type:'open',close:'in',width:'600px',height:'270px'});
	 });
	 //修改
	 $j("#tableList").on("click",".editor",function(){
		 var me = $j(this);
		 var id = me.attr("val");
		 var dialog = $j("#dialog-auth-dialog");
		 dialog.find(".function-edit").show();
		 var tr = me.parent().parent().find("span");
		 var detail  =dialog.find(".edit");
		 detail.find(".name").val(tr.eq(1).html());
		 detail.find(".acl").val(tr.eq(0).html());
		 detail.find(".gname").val(tr.eq(2).html());
		 detail.find(".desc").val(tr.eq(4).html());
		 var orgTypeId = tr.eq(3).attr("orgTypeId");
		 detail.find(".orgtype").find("option[value='"+orgTypeId+"']").attr("selected","selected");
		 //查询对应的url信息
		 var data = nps.syncXhrPost(base+"/menu/getPrivilegeUrlPid.json", {'pid': id});
		 $j("#table1").loxiasimpletable("refresh",data);
		 dialog.attr("parentid",id);
		 dialog.dialogff({type:'open',close:'in',width:'600px',height:'610px'});
	 });
	 $j("#dialog-auth-dialog .btn-ok").on("click",function(){
		 var dialog = $j("#dialog-auth-dialog");
		 var id = dialog.attr("parentid");
		 var detail  =dialog.find(".edit");
		 var orgTypeId= detail.find(".orgtype").val();
		 var name = detail.find(".name").val();
		 var acl = detail.find(".acl").val();
		 var groupName = detail.find(".gname").val();
		 var description = detail.find(".desc").val();
		 var json = {
				 "orgTypeId":orgTypeId,
				 "name":name,
				 "acl":acl,
				 "groupName":groupName,
				 "description":description,
		 };
		 if(id != null && id!=""){
			 json.id = id;
		 }
		 //获取功能信息
		var urls = getUrls();
		if(urls.length > 0){
			json.urls = urls;
		}
		json = JSON.stringify(json);
		$j.ajax({ 
			type: "POST", 
			url: base+"/auth/editPrivilege.json", 
			contentType: "application/json; charset=utf-8", 
			data: json, 
			dataType: "json", 
			success: function (data) { 
				if (data.isSuccess) {
					var menu = data.description;
					json.id = menu.id;
					if(id != null && id!=""){
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-s"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("add-s"));
					}
					refreshData();
					dialog.dialogff({type:'close'});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				}
			}, 
			error: function (message) { 
				nps.info(nps.i18n("INFO_TITLE_DATA"),message);
			} 
		}); 
		
	 });
	 
	 //功能新增
	 $j(".addUrl").on("click",function(){
		 var dialog = $j("#dialog-function-dialog");
		 dialog.attr("parentid",$j("#dialog-auth-dialog").attr("parentid"));
		 dialog.find(".desc").val(null);
		 dialog.find(".url").val(null);
		 dialog.dialogff({type:'open',close:'in',width:'500px',height:'230px'});
	 });
	 
	//确定按钮
	 $j("#dialog-function-dialog .btn-ok").on("click",function(){
		 var  dialog = $j("#dialog-function-dialog");
		 var parentId = dialog.attr("parentid");
		 var fid = dialog.attr("fid");
		 if(validateInput("#dialog-function-dialog .input_add")==false){
			 return;
		 }
		 var url =  dialog.find(".url").val();
		 if(url.substring(0,1)!="/"){
				nps.info(nps.i18n('INFO_TITLE_DATA'), "url必须以/开头");
				return;
		 }
		 var description =  $j("#dialog-function-dialog").find(".desc").val();
		 var urls = getUrls();
		 var json = {
				 id:++num,
				 "url":url,
				 "description":description
		 	};
		 if(fid == null || fid==""){
			 for ( var i = 0; i < urls.length; i++) {
				var url1 = urls[i].url;
				if(url1==url){
					nps.info(nps.i18n('INFO_TITLE_DATA'), "url已经添加");
					return;
				}
			}
			 urls.push(json);
		 }else{
			 for ( var i = 0; i < urls.length; i++) {
				var id = urls[i].id;
				if(id==fid){
					urls[i].url = url;
					urls[i].description = description;
				}
			}
		 }
		 nps.asyncXhrPost(base+"/auth/existUrl.json", {"url":url,"parentId":parentId},{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					$j("#table1").loxiasimpletable("refresh",urls);
					dialog.dialogff({type:'close'});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				}
		 }});
		 
	 });
	 //功能修改
	 $j("#table1").on("click",".editor",function(){
		 var me = $j(this);
		 var tr = me.parent().parent().find("span");
		 var dialog = $j("#dialog-function-dialog");
		 var edit = dialog;
		 edit.attr("fid",me.attr("val"));
		 edit.find(".desc").val(tr.eq(0).html());
		 edit.find(".url").val(tr.eq(1).html());
		 dialog.attr("parentid",$j("#dialog-auth-dialog").attr("parentid"));
		 dialog.dialogff({type:'open',close:'in',width:'500px',height:'230px'});
	 });
     //功能删除
	 $j("#table1").on("click",".delete",function(){
		 var me=$j(this);
		 me.parents("tr").remove();
	 });
	 
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
	 //导sql
	 $j(".exportsql").on("click",function(){
		 var id  = $j(this).attr("val");
		 var data = nps.syncXhrPost(base+"/auth/generateMenuSql.json", {'id':id});
		 $j("#dialog-sql-dialog").find(".sql").val(data);
		 $j("#dialog-sql-dialog").dialogff({type:'open',close:'in',width:'1000px',height:'410px'});
	 });
}); 


