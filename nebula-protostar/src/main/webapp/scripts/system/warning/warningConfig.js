$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的告警么？",
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
	"CONFIRM_EN":"确定要启用告警?",
	"CONFIRM_DIS":"确定要禁用告警?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的告警",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空",
	"code":"编码",
	"name":"名称",
	"email":"邮件",
	"sms":"短信",
	"l":"低",
	"m":"中",
	"h":"高",
	"time":"时间",
	"name":"名称",
	"receivers":"收件人",
	"type":"类型",
	"level":"级别",
	"lifecycle":"状态",
	"update-remove-not-exit":"修改的数据不存在或已经被人删除",
	"select-email":"请选择邮件模板数据",
	"select-sms":"请选择短信模板数据",
	"count":"次数",
	"EMAIL_REPEAT":"存在相同的收件人:",
	"warning-edit":"告警设置-修改",
	"warning-add":"告警设置-新增"
});

// 列表
var urlList = base+'/warningConfig/page.json'; 
// 启用或禁用
var ACTIVATION_URL = base + "/warningConfig/enableOrDisableByIds.json";
//删除
var removeURl = base+ "/warningConfig/removeByIds.json";
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
function drawRadio(data, args, idx) {
	return "<input type='radio' name='id' value='"
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
		var display = $j(this).parent().parent().css("display");
		if(display !="none" && $j(this).hasClass("ui-loxia-error")){
			result = false;
		}
		if(display !="none" && (val==null || val=="")){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
	 });
	return result; 
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
function type(data){
	var type = loxia.getObject("type", data);	
	var result = "";
	if(type == 1){
		result = nps.i18n("email");
	}else{
		result = nps.i18n("sms");
	}
 	return result; 
} 
function level(data){
	var type = loxia.getObject("level", data);	
	var result = "";
	if(type == 1){
		result = nps.i18n("l");
	}else if(type == 2){
		result = nps.i18n("m");
	}else{
		result = nps.i18n("h");
	}
 	return result; 
} 
function timeParam(data){
	var timeParam = loxia.getObject("timeParam", data);
	var type = timeParam.substring(timeParam.length-1,timeParam.length);
	var prefix = timeParam.substring(0,timeParam.length-1);  
	var result = prefix;
	if(type == 'y'){
		result =result+ "年";
	}else if(type == 'M'){
		result = result+"月";
	}else if(type == 'd'){
		result = result+"天";
	}else if(type == 'h'){
		result = result+"小时";
	}else if(type == 'm'){
		result = result+"分钟";
	}else{
		result =result+ "秒";
	}
 	return result; 
} 

function receivers(data){
	var receivers = loxia.getObject("receivers", data);
	var result="";
	if(receivers!=null){
		var arr = receivers.split(",");
		for ( var i = 0; i < arr.length; i++) {
			result = result + arr[i]+"<br>";
		}
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
				label : nps.i18n("code"),
				width : "10%"
			},
			{
				name : "name",
				label :nps.i18n("name"),
				width : "10%"
			},
			{
				name : "timeParam",
				label : nps.i18n("time"),
				width : "5%",
				template:'timeParam'
				
			},
			{
				label : nps.i18n("receivers"),
				width : "30%",
				name:'receivers',
				template:'receivers'
			},
			{
				name : "count",
				label : nps.i18n("count"),
				width : "5%"
			},
			{
				name : "type",
				label : nps.i18n("type"),
				width : "5%",
				template:"type" 
			},
			{
				name : "level",
				label :  nps.i18n("level"),
				width : "5%",
				template:"level" 
			},
			{
				name : "lifecycle",
				label : nps.i18n("lifecycle"),
				width : "5%",
				type:'yesno'
			},
			{
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
	 //修改
	 $j("#tableList").on("click",".func-button.editor",function(){
		 var curObject=$j(this);
		 var id = curObject.attr("val");
		 $j("#dialog-item-select").attr("wid",id);
		 var data = nps.syncXhrPost(base+"/warningConfig/findById.json", {'id': id});
		 if(data==null){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-remove-not-exit"));
			 return;
		 }
		 $j(".proto-dialog-content .code").val(data.code);
		 $j(".proto-dialog-content .code").attr("readonly",true);
		 $j(".proto-dialog-content .name").val(data.name);
		 $j(".proto-dialog-content .desc").val(data.warningDesc);
		 var timeParam = data.timeParam;
		 var suffix = timeParam.substring(timeParam.length-1,timeParam.length);
		 var prefix = timeParam.substring(0,timeParam.length-1);  
		 $j(".proto-dialog-content .time_suffix option[value='"+suffix+"']").attr("selected",true);
		 $j(".proto-dialog-content .time_prefix").val(prefix);
		 $j(".proto-dialog-content .tmpCode").val(data.templateCode);
		 $j(".proto-dialog-content .type option[value='"+data.type+"']").attr("selected",true);
		 $j(".proto-dialog-content .level option[value='"+data.level+"']").attr("selected",true);
		 $j(".proto-dialog-content .count").val(data.count);
		 //处理收件人
		 //先删除
		 $j(".edit_tbl .tr_receiver").each(function(i,dom){
			 var dis = $j(dom).css("display");
			 if(dis != "none"){
				 $j(dom).remove();
			 }
		 });
		 var  res = data.receivers;
		 if(res!=null){
			 var arr = res.split(",");
			 if(arr.length != 0){
				 $j(".tr_receiver_pre .email").val(arr[arr.length-1]);
				 for ( var i = 0; i < arr.length-1; i++) {
					 var tr = $j(".tr_receiver").eq(0).clone(true).show();
					 tr.find(".email").val(arr[i]);
					 $j(".tr_receiver_pre").after(tr);
				}
			 }
		 }
		 $j("#confirm_warning").attr("disabled",false); 
		 $j("#wc_title").text(nps.i18n("warning-edit"));
		 $j("#dialog-item-select").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
	 });
	 //新建
	 $j(".addWarningConfig").on("click",function(){
		 $j("#dialog-item-select").attr("wid","");
		 setEmpty("input_add");
		 $j(".desc").val(null);
		 $j(".edit_tbl .tr_receiver").each(function(i,dom){
			 var dis = $j(dom).css("display");
			 if(dis != "none"){
				 $j(dom).remove();
			 }
		 });
		 $j("#wc_title").text(nps.i18n("warning-add"));
		 $j("#confirm_warning").attr("disabled",false); 
		 $j(".proto-dialog-content .code").attr("readonly",false);
		 $j("#dialog-item-select").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
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
		 var id = $j("#dialog-item-select").attr("wid");;
		 var code = $j(".proto-dialog-content .code").val();
		 var name = $j(".proto-dialog-content .name").val();
		 var desc = $j(".proto-dialog-content .desc").val();
		 var time_suffix = $j(".proto-dialog-content .time_suffix").val();
		 var time_prefix = $j(".proto-dialog-content .time_prefix").val();
		 var tmpCode = $j(".proto-dialog-content .tmpCode").val();
		 var type = $j(".proto-dialog-content .type").val();
		 var level = $j(".proto-dialog-content .level").val();
		 var count = $j(".proto-dialog-content .count").val();
		 var receiver="";
		 var ischeck = true;
		 var email="";
		 $j(".proto-dialog-content .receivers").each(function(i,dom){
			 if($j(dom).val()!=null && $j(dom).val()!=""){
				 email = $j(dom).val();
				 if(receiver.indexOf(email)>-1){
					 ischeck = false;
				 }
				 receiver = receiver+email+",";
			 }
			
		 });
		 if(ischeck==false){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("EMAIL_REPEAT")+email);
			 return;
		 }
		 var emails = receiver.substring(0,receiver.lastIndexOf(","));
		 var json = {
				 'code':code,
				 'name':name,
				 'warningDesc':desc,
				 'timeParam':time_prefix+time_suffix,
				 'templateCode':tmpCode,
				 'type':type,
				 'level':level,
				 'receivers':emails,
				 'count':count
		 };
		 if(id!=null && id!=""){
			 json.id = id;
		 }
		 me.attr("disabled",true); 
		 nps.asyncXhrPost(base+"/warningConfig/save.json", json,{successHandler:function(data, textStatus){
			 me.removeAttr("disabled"); 
			if (data.isSuccess) {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_EDIT_SUCCESS"));
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ADD_SUCCESS"));
					 setEmpty("input_add");
					 $j("#dialog-item-select").dialogff({type : 'close'});
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
	 //添加
	 $j(".edit_tbl").on("click",'.add',function(){
		 var tr=null;
		 $j(".tr_receiver ").each(function(i,dom){
			 var display = $j(dom).css("display");
			 if(display =="none"){
				 tr= $j(dom);
			 }
		 });
		 $j(".tr_receiver_pre").after(tr.clone(true).show());
	 });
	 //删除
	 $j(".edit_tbl").on("click",'.del',function(){
		 var tr = $j(this).parent().parent();
		 tr.remove();
	 });
	 $j(".edit_tbl .email").unbind("blur");
	 //验证邮箱
	 $j(".edit_tbl").on("blur",'.email',function(){
		 var email = $j(this).parent().parent().find(".receivers");
		 var emailStr=email.val();
		 if(emailStr==null || emailStr==""){
			 email.addClass("ui-loxia-error");
			 return;
		 }
		 var regx = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		 if(regx.test(emailStr)==false){
			 email.addClass("ui-loxia-error");
		 }else{
			 email.removeClass("ui-loxia-error");
		 }
	 });
	
	 //邮件编码选择 
	 $j(".edit_tbl").on("click",'.select',function(){
		 var type = $j(".proto-dialog-content .type").val();
		 if(type==1){
			 $j("#dialog-email-select").dialogff({type:'open',close:'in',width:'500px',height:'600px'});
		 }else{
			 $j("#dialog-sms-select").dialogff({type:'open',close:'in',width:'500px',height:'600px'});
		 }
		 
	 });
	 $j("#email_tbl").loxiasimpletable({
			page : true,
			size : 10,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			form:"email_searchForm",
			cols : [{
					label : "",
					width : "5%",
					template : "drawRadio"
				},{
					name : "code",
					label : nps.i18n("code"),
					width : "5%"
				}, {
					name : "name",
					label : nps.i18n("name"),
					width : "10%"
				}],
			dataurl : base+'/email/pagination.json'
	 });
	 $j("#email_tbl").loxiasimpletable("refresh");
	//告警取消按钮
	 $j("#cancel").click(function(){
		$j("#dialog-item-select").dialogff({type:'close'});
	});
	 
	//邮件筛选
	$j(".func-button.search.email").click(function(){
		 $j("#email_tbl").data().uiLoxiasimpletable.options.currentPage=1;
		 $j("#email_tbl").loxiasimpletable("refresh");
	});
	
	//短信列表
	$j("#sms_tbl").loxiasimpletable({
			page : true,
			size : 10,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			form:"sms_searchForm",
			cols : [{
					label : "",
					width : "5%",
					template : "drawRadio"
				},{
					name : "code",
					label : nps.i18n("code"),
					width : "5%"
				}, {
					name : "name",
					label : nps.i18n("name"),
					width : "10%"
				}]
			//dataurl : base+'/email/pagination.json'
	});
	 $j("#sms_tbl").loxiasimpletable("refresh");
	//短信筛选
	$j(".func-button.search.sms").click(function(){
		 $j("#sms_tbl").data().uiLoxiasimpletable.options.currentPage=1;
		 $j("#sms_tbl").loxiasimpletable("refresh");
	});


	$j(".edit_tbl").on("change",'.type',function(){
		$j(".proto-dialog-content .tmpCode").val(null);
	});
	//邮件确定按钮
	$j("#dialog-email-select").on("click",'.btn-ok',function(){
		 var data=""; 
		 var radio=$j("#dialog-email-select input[name='id']");
		 $j.each(radio, function(i,val){   
			  if(val.checked){
				  data=$j(this).parent().parent().find('td').eq(1).text();
			  }
		});
		if(data==null || data==""){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("select-email"));
			return;
		}
		$j(".proto-dialog-content .tmpCode").val(data);
		$j(".proto-dialog-content .tmpCode").removeClass("ui-loxia-error");
		$j("#dialog-email-select").dialogff({type:'close'});
		
	});
	
	//短信确定按钮
	$j("#dialog-sms-select").on("click",'.btn-ok',function(){
		 var data=""; 
		 var radio=$j("#dialog-sms-select input[name='id']");
		 $j.each(radio, function(i,val){   
			  if(val.checked){
				  data=$j(this).parent().parent().find('td').eq(1).text();
			  }
		});
		if(data==null || data==""){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("select-sms"));
			return;
		}
		$j(".proto-dialog-content .tmpCode").val(data);
		$j(".proto-dialog-content .tmpCode").removeClass("ui-loxia-error");
		$j("#dialog-sms-select").dialogff({type:'close'});
		
	});
	
}); 


