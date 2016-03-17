$j.extend(loxia.regional['zh-CN'],{
    "NPS_FORM_CHECK_INFO":"操作提示",
    "SYS_SETOPTION":"设置",
    "SYS_MODIFYOPTION":"修改",
    "SYS_DISABLEOPTION":"禁用",
    "SYS_ENABLEOPTION":"启用",
    "SYS__OPERATOR_TIP":"确认操作",
    "INFO_STOP_SUCCESS":"禁用成功",
    "INFO_START_SUCCESS":"启用成功",
    "INFO_UPDATE_SUCCESS":"修改成功",
    "SYS_OPTION_MESSAGE":"提示信息",
    "OPTION_UPDATE_ENABLE":"修改失败",
    "OPTION_UPDATE_ABLE":"修改成功",
    "OPTION_VALUE_EXSITED":"选项值存在",
    "OPTION_SORTNO_FAILURE":"排序号不能为字符串",
    "INFO_UPDATE_SUCCESS":"保存成功",
    "INFO_TITLE_DATA":"提示",
    "SYSTEM_OPTION_NOT_OPERATION":"系统级选项不可操作",
    "OPTION_NAME":"选项名称",
    "OPTION_VALUE":"选项值",
    "OPTION_STATUS":"状态",
    "IS_SYSTEM_OPTION":"是否系统选项",
    "OPTION_OPERATION":"操作"
});
//跳转到通用选项页面，动态获取数据
var optionListUrl=base+'/option/chooseOptionList.json';
//启用禁用选项名称
var enableOrDisableOptionUrl = base+'/option/enableOrDisableOption.json';
//跳转到新增页面
var updateOptionUrl= base+'/option/updateOption.htm';
var optionGroupListUrl = base +'/option/optionGroupList.htm?keepfilter=true';
//验证选项值
var validateOptionValueUrl=base+'/option/validateOptionValue.json';
//设置选项值
var setOptionUrl=base+'/option/option-list.htm';
//查询选项信息
var findChooseOptionByGroupCodeUrl = base + '/option/findChooseOptionByGroupCode.json';

//全局变量
var oldval="";
var isSystem = false;

//操作显示 禁用 修改
function editOptionList(data, args, idx){ 
	var groupId=loxia.getObject("id", data);
	var result="";
	var state=loxia.getObject("lifecycle", data);
	if(isSystem){
		return nps.i18n("SYSTEM_OPTION_NOT_OPERATION");
	}
	if(state==1){
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button update'>"+nps.i18n("SYS_MODIFYOPTION")+"</a>"+
		"<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button enable' onclick='enableOrDisable("+groupId+",0)'>"+nps.i18n("SYS_DISABLEOPTION")+"</a>";
		
	}else{
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button update'>"+nps.i18n("SYS_MODIFYOPTION")+"</a>"+
		"<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button enable' onclick='enableOrDisable("+groupId+",1)'>"+nps.i18n("SYS_ENABLEOPTION")+"</a>";
	}
	
	return result;
}

//验证跳转
function validateParam(json){
	 var option = loxia.syncXhr(validateOptionValueUrl, json,{type: "GET"});
	if(option.isSuccess == true){
		return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("OPTION_VALUE_EXSITED"));
	}
}

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
 
}


//根据ID 禁用启用option
function enableOrDisable(val,state){
	var info="";
	//0==禁用， 1=启用
	if(state!=1){
		info=nps.i18n("SYS_DISABLEOPTION");
	}else{
		info=nps.i18n("SYS_ENABLEOPTION");
	}
	nps.confirm(nps.i18n("SYS__OPERATOR_TIP"),info,function(){
		var json={"groupId":val,"state":state};
	  	nps.asyncXhrGet(enableOrDisableOptionUrl, json,{successHandler:function(data, textStatus){
	  		var backWarnEntity = data;
	  		if(backWarnEntity.isSuccess){
		  		if(state!=1){
		  			//禁用成功
		  			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STOP_SUCCESS"));
		  		}else{
		  			//启用成功
		  			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_START_SUCCESS"));
		  		}
		  	} 
	  		refreshData();
	  	}}); 
	});
}

//loxia获取通用选项数据列表
$j(document).ready(function(){
	
	//判断该分组是否是系统级的
	var json={"groupCode":$j("#groupCode").val()};
	var result = loxia.syncXhr(findChooseOptionByGroupCodeUrl, json, { type: "POST"});
	for(var i=0; i<result.length; i++){
		if(result[i].isSystem){
			isSystem = true;
			break;
		}
	}
	
	if(isSystem){
		//隐藏"新增"按钮
		$j('.addchooseop').remove();
	}
	
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    oldval = $j("#optionValue").val();
    searchFilter.init({formId:'searchForm', searchButtonClass:'.func-button.search'});
	$j("#table1").loxiasimpletable({
		page: true,
		size:10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[
		{name:"optionLabel",label:nps.i18n("OPTION_NAME"),width:"20%",sort:["option_label asc","option_label desc"]},
		{name:"optionValue",label:nps.i18n("OPTION_VALUE"),width:"20%",sort:["option_value asc","option_value desc"]},
		{name:"lifecycle",label:nps.i18n("OPTION_STATUS"),width:"20%",type:"yesno",sort:["lifecycle asc","lifecycle desc"]},
		{name:"isSystem",label:nps.i18n("IS_SYSTEM_OPTION"),width:"20%",sort:["is_system  asc","is_system desc"]},
		{label:nps.i18n("OPTION_OPERATION"),width:"20%", template:"editOptionList"}
		],
		dataurl : optionListUrl
	});
    refreshData();
    
   //修改通用选项信息
  $j("#table1").on("click",".func-button.update",function(){
	   	
    	 var groupId=$j(this).attr("val");
	   	 window.location.href=updateOptionUrl+"?groupId=" + groupId;
    });

  //筛选列表
    $j(".func-button.search").click(function(){
	    
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
    });
   
    //保存
   
    $j(".button.orange.saveopt").click(function(){
    	nps.submitForm('updateForm',{mode:'async',
			successHandler : function(data){
				if(data.isSuccess){
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
				}
		   }});
    
      });
    
    //验证
	$j("#optionValue").bind("blur",function()
			 {
				 var groupCode = $j("#groupCode").val();
				 var json={"groupCode":groupCode,"optionValue":$j("#optionValue").val()};
				 if(oldval == undefined){
					 //新增
					 validateParam(json);
				 }else{
					 //修改
					 if(oldval==$j("#optionValue").val()){
						 return ;
					 }else{
						 validateParam(json);
			 		}
				 }
				 
	  });
	//验证sortNo不能为字符串
	$j("#sortNo").bind("blur",function(){
		 var sortNo  = $j("#sortNo").val();
		 var regNumber = /^(\+|-)?\d+$/;
			//表达式判断
		    if( !(regNumber.test(sortNo)) || sortNo <0){  
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("OPTION_SORTNO_FAILURE"));
			}
			
			
	});
	//新增
	$j(".button.orange.addchooseop").on("click", function(){
		
		var groupCode=$j("#groupCode").val();
		var groupDesc = $j("#groupDesc").val();
        window.location.href = base +"/option/createOption.htm?groupCode="+groupCode  + "&groupDesc=" + groupDesc;
    });
    
  //返回到通用选项页面

    $j(".button.orange.return").on("click",function(){
		window.location.href=optionGroupListUrl;
		
	});
    
  //返回到通用选项修改页面
    $j(".button.orange.returnUpdate").on("click",function(){
    	var groupCode = $j("#groupCode").val();
    	var groupDesc = $j("#groupDesc").val();
    	window.location.href=setOptionUrl + "?groupCode=" + groupCode  + "&groupDesc=" + groupDesc;
    });
	
});


