
$j.extend(loxia.regional['zh-CN'],{
    "USER_SEARCH_ORG_NOSELECT":"未选择组织",
    "USER_SEARCH_DATA_NULL":"开始或结束时间为空",
    "USER_OPERATOR_MODIFY":"修改",
    "USER_OPERATOR_DELETE":"删除",
    "USER_CONFIRM_DELETE":"确认删除",
    "USER_CONFIRM_DELETE_SEL_USER":"确定要删除选定的用户么？",
    "USER_TLABEL_USER_NAME":"用户名",
    "USER_TLABEL_REAL_NAME":"真实姓名",
    "USER_TLABEL_MOBILE":"手机",
    "USER_TLABEL_ORGNAME":"所属组织机构",
    "USER_TLABEL_EMAIL":"邮箱",
    "USER_TLABEL_CREATE_TIME":"创建时间",
    "USER_TLABEL_RESET_PASSWD":"重置密码",
    "USER_TLABEL_STATE":"状态",
    "USER_TIP_VIEW_USER":"查看用户",
    "USER_FORM_CHECK_ERROR":"错误信息",
    "USER_OPERATE_SUCCESS": "操作成功",
    "USER_OPERATE_FAILURE": "操作失败",
    "USER_OPERATOR_TIP":"操作提示",
    "USER_SEARCH_NOT_INPUT_USERNAME":"请输入用户名称",
    "USER_SEARCH_NOT_INPUT_REALNAME":"请输入真实名称",
    "USER_CONFIRM_ENABLE":"确认启用",
    "USER_CONFIRM_ENABLE_SEL_USER":"确定要启用选定的用户么？",
    "USER_CONFIRM_DISABLE":"确认禁用",
    "USER_CONFIRM_DISABLE_SEL_USER":"确定要禁用选定的用户么？",
    "USER_OPERATOR_ENABLE":"启用",
    "USER_OPERATOR_DISABLE":"禁用",
    "USER_STATE_ENABLE":"有效",
    "USER_STATE_DISABLE":"无效",
    "USER_TIP_NOSEL":"未选中",
    "USER_ENABLE_SUCCESS":"启用成功",
    "USER_DISABLE_SUCCESS":"禁用成功",
    "USER_FORM_CHECK_INFO":"操作提示"
});


//用户列表
var userListUrl='/user/list.json';

//用户查看
var userViewUrl='/user/view.htm';
//修改页面
var modifyUserUrl='/user/to-update.htm';
//新增页面
var addUserUrl='/user/to-create.htm';
//重置密码
var resetPasswdUserUrl='/user/to-reset-passwd.htm';
//启用或禁用
var enableOrDisableUserUrl='/user/enable-or-disable.json';

function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}



function drawEditUser(data, args, idx){  
	var result="";
	var state=loxia.getObject("lifecycle", data);
	result+="<a href='javascript:void(0);' class='func-button modify' val='"+loxia.getObject("id", data)+"'>"+nps.i18n("USER_OPERATOR_MODIFY")+"</a>";
	result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button resetpasswd'>"+nps.i18n("USER_TLABEL_RESET_PASSWD")+"</a>";
	
	if(state==0){
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button enable'>"+nps.i18n("USER_OPERATOR_ENABLE")+"</a>";
	}else{
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button disable'>"+nps.i18n("USER_OPERATOR_DISABLE")+"</a>";
	}
	return result;
}

function drawCheckbox(data, args, idx){
	return "<input type='checkbox' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}

function drawName(data, args, idx){
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('"+userViewUrl+"?userId="+loxia.getObject("id", data)+"',null,null,[1200,450]);\">"+loxia.getObject("userName", data)+"</a>";
}


function chooseFilterType(val, obj){

    $j("#searchKey").attr("name",val);
    return loxia.SUCCESS;
}
//刷新表格数据
function refreshData(){

	$j("#table1").loxiasimpletable("refresh");
 
}

function userSetFilter(data){
	alert("custom");
	 for ( var p in data ){ 
		  // 方法 
		  if ( typeof ( data [ p ]) == " function " ){
			 // obj [ p ]() ; 
		  }
		  //属性
		  else { 
			  // p 为属性名称，obj[p]为对应属性的值 
			//  props += p + " = " + obj [ p ] + " /t " ;
			  $j("input[name='"+p+"']").val(data[p]);
			  
			  $j("select[name='"+p+"']").val(data[p]);
			 
		  } 
	  } 
	
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
    //searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search',customSetFilter:'userSetFilter'});

    //init table with current search conditions
    //$j("table1").loxiasimpletable();
    //register event
    //$j(".modify table1").on("click", function(){});
    //click event for search
    //$j("table1").loxiasimpletable("dataurl",newurl);
    //$j("table1").loxiasimpletable("refresh");
    //set dataurl option for table and trigger refresh
	
    //生成表格
	$j("#table1").loxiasimpletable({
		page: true,
		size:10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[
		{label:"<input type='checkbox'/>",width:"5%", template:"drawCheckbox"},
		{name:"userName",label:nps.i18n("USER_TLABEL_USER_NAME"),width:"15%",template:"drawName",sort:["us.user_name asc","us.user_name desc"]},
		{name:"realName",label:nps.i18n("USER_TLABEL_REAL_NAME"),width:"10%"},
		{name:"mobile",label:nps.i18n("USER_TLABEL_MOBILE"),width:"10%"},
		{name:"email",label:nps.i18n("USER_TLABEL_EMAIL"),width:"19%"},
		{name:"orgName",label:nps.i18n("USER_TLABEL_ORGNAME"),width:"10%"},
		
		{name:"createTime",label:nps.i18n("USER_TLABEL_CREATE_TIME"),width:"12%",formatter:"formatDate" ,sort: ["us.create_time asc","us.create_time desc"]},
		{name:"lifecycle",label:nps.i18n("USER_TLABEL_STATE"),width:"4%",type:"yesno"},
		
		{label:"操作",width:"15%", template:"drawEditUser"}
		],
		dataurl : userListUrl
	});
	

	$j("#table1").on("click",".func-button.modify",function(){
   	 
    	window.location.href=modifyUserUrl+"?userId="+$j(this).attr("val");
    });
	
	$j("#table1").on("click",".func-button.resetpasswd",function(){
	   	 
    	window.location.href=resetPasswdUserUrl+"?userId="+$j(this).attr("val");
    });

 //启用单行
   $j("#table1").on("click",".func-button.enable",function(){
        var curObject=$j(this);
        nps.confirm(nps.i18n("USER_CONFIRM_ENABLE"),nps.i18n("USER_CONFIRM_ENABLE_SEL_USER"), function(){

            var json={"ids":curObject.attr("val"),"state":1};

        	var _d = nps.syncXhr(enableOrDisableUserUrl, json,{type: "GET"});
        	if(_d.result=='success'){
        		nps.info(nps.i18n("USER_FORM_CHECK_INFO"),nps.i18n("USER_ENABLE_SUCCESS"));
        		refreshData();
        	}
        	else
            	nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
        });
    });
   
 //禁用单行
   $j("#table1").on("click",".func-button.disable",function(){
        var curObject=$j(this);
        nps.confirm(nps.i18n("USER_CONFIRM_DISABLE"),nps.i18n("USER_CONFIRM_DISABLE_SEL_USER"), function(){

            var json={"ids":curObject.attr("val"),"state":0};

        	var _d = nps.syncXhr(enableOrDisableUserUrl, json,{type: "GET"});
        	if(_d.result=='success'){
        		nps.info(nps.i18n("USER_FORM_CHECK_INFO"),nps.i18n("USER_DISABLE_SUCCESS"));
        		refreshData();
        	}
        	else
            	nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
        });
    });

    refreshData();

  //批量启用按钮
    $j(".button.enable").click(function(){
    	var data="";

    	$j(".chkcid:checked").each(function(i,n){
        	if(i!=0){
            	data+=",";
            	}
        	data+=$j(this).val();
        });
    	if(data==""){
    		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_TIP_NOSEL"));
    		return ;
    	}
        nps.confirm(nps.i18n("USER_CONFIRM_ENABLE"),nps.i18n("USER_CONFIRM_ENABLE_SEL_USER"), function(){

        	
            var json={"ids":data,"state":1};
        	var _d = nps.syncXhr(enableOrDisableUserUrl, json,{type: "GET"});
        	if(_d.result=='success'){
        		nps.info(nps.i18n("USER_FORM_CHECK_INFO"),nps.i18n("USER_ENABLE_SUCCESS"));
        		refreshData();
        	}
        	else
            	nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
        });
    });
    
    //批量禁用按钮
    $j(".button.disable").click(function(){
    	
    	var data="";

    	$j(".chkcid:checked").each(function(i,n){
        	if(i!=0){
            	data+=",";
            	}
        	data+=$j(this).val();
        });
    	if(data==""){
    		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_TIP_NOSEL"));
    		return ;
    	}
    	
        nps.confirm(nps.i18n("USER_CONFIRM_DISABLE"),nps.i18n("USER_CONFIRM_DISABLE_SEL_USER"), function(){

        	
            var json={"ids":data,"state":0};
        	var _d = nps.syncXhr(enableOrDisableUserUrl, json,{type: "GET"});
        	if(_d.result=='success'){
        		nps.info(nps.i18n("USER_FORM_CHECK_INFO"),nps.i18n("USER_DISABLE_SUCCESS"));
        		refreshData();
        	}
        	else
            	nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
        });
    });

    $j(".button.adduser").click(function(){
        window.location.href=addUserUrl;
    });


    //筛选列表
    $j(".func-button.search").click(function(){
    
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	 });

	
});



