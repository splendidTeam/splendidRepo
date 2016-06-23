$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	 "LABEL_MEMBER_USERPIC":"会员头像",
	    "LABEL_MEMBER_LOGINNAME":"会员名",
	    "LABEL_MEMBER_NICKNAME":"昵称",
	    "LABEL_MEMBER_SOURCE":"来源",
	    "LABEL_MEMBER_TYPE":"类型",
	    "LABEL_MEMBER_GROUP":"分组",
	    "LABEL_MEMBER_LIFECYCLE":"状态",
	    "LABEL_MEMBER_REGISTERTIME":"创建时间",
	    "LABEL_MEMBER_LATESTTIME":"最近登录时间",
	    "LABEL_MEMBER_OPERATE":"操作",
	    "TO_DISABLE":"禁用",
	    "TO_ENABLE":"启用",
	    "CONFIRM_DISABLE_MEMBER":"确认禁用会员",
	    "INFO_CONFIRM_DISABLE_MEMBER":"确认禁用会员吗",
	    "INFO_CONFIRM_ENABLE_MEMBER":"确认启用会员吗",
	    "INFO_STOP_SUCCESS":"禁用成功!",
	    "INFO_START_SUCCESS":"启用成功!",
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_STOP_FAIL":"禁用失败!",
	    "NO_CATEGORY":"无",
	    "VALUE_SOURCE_1":"QQ登录",
	    "VALUE_SOURCE_2":"自注册",
	    "VALUE_SOURCE_3":"微博登录",
	    "USER_CONFIRM_DISABLE_USER":"确认禁用会员",
	    "USER_CONFIRM_ENABLE_USER":"确认启用会员",
	    "USER_INFO_CONFIRM":"确认信息",
	    "NO_DATA":"无数据",
	    "USER_TIP_NOSEL":"请选择您要禁用的会员",
	    "USER_TIP_ENABLE_NOSEL":"请选择您要启用的会员",
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"禁用成功"
});

//商品属性列表
var memberListUrl = base+'/member/memberList.json';

// 启用或禁用

var enableOrDisableMemberUrl = base+'/member/enableOrDisableMemberByIds.json';

// 跳转到会员详情页面
var memberViewUrl = base+'/member/view.htm';

//获取id
function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
}

//操作中的启用禁用函数
function drawEditMember(data, args, idx){  
	var result="";
	var state=loxia.getObject("lifecycle", data);
	if(state==0){
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	}else{
		result+="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button disable'>"+nps.i18n("TO_DISABLE")+"</a>";
	}

	return result;
}


//点击图片跳转到会员详情页面
function drawIcon(data, args, idx){
	var user_pic=loxia.getObject("userPic",data);
	
	var result="";
	var memberId=loxia.getObject("id", data);
	var tempDataUrl = memberViewUrl+'?memberId='+memberId;
	
	var imgResult="";
	if(user_pic==null||user_pic==''){
		imgResult="<img src='"+base+"/images/main/portrait.gif' width=60 height=60>";
	}
	result ="<a style='cursor:pointer;' onclick=\"loxia.openPage('"+tempDataUrl+"');\">"+imgResult+"</a>"; 
		
	return result;
}

//点击登录名跳转到会员详情页面
function drawName(data, args, idx){
	
	var memberId=loxia.getObject("id", data);
	var tempDataUrl = memberViewUrl+'?memberId='+memberId;
	var loginName =loxia.getObject("loginName", data);
	if(loginName == null){
		loginName ='';
	}
	
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('"+tempDataUrl+"');\">"+loginName+"</a>";
}


//获取分组名
function formatGroupName(data, args, idx){
	var propertyNameArray=loxia.getObject("groupName", data);
	
	if(propertyNameArray==null||propertyNameArray==''){
		return nps.i18n("NO_CATEGORY");
	}
	return propertyNameArray;
}


/**
 * 字符长度大于8 就截取掉用...  代替后面的字符
 * @param data
 * @param args
 * @param idx
 * @returns
 */
function formatNameLength(data, args, idx){
	var propertyName=loxia.getObject("name", data);
	if(propertyName.length>8){
		propertyName=propertyName.substring(0,8);
		propertyName+="...";
	}
	return  propertyName;
}

//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
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


//通过loxiasimpletable动态获取数据
$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckbox"
		}, {
			name : "userPic",
			label : nps.i18n("LABEL_MEMBER_USERPIC"),
			width : "10%",
			template : "drawIcon"
			
		}, {
			name : "loginName",
			label : nps.i18n("LABEL_MEMBER_LOGINNAME"),
			width : "10%",
			template:"drawName",
			sort:["a.login_name asc","a.login_name desc"]
		   
		}, {
			name : "nickName",
			label : nps.i18n("LABEL_MEMBER_NICKNAME"),
			width : "10%",
			sort:["b.nickname asc","b.nickname desc"]
		}, {
			name : "sourceName",
			label : nps.i18n("LABEL_MEMBER_SOURCE"),
			width : "5%",
			sort:["a.source asc","a.source desc"]
		}, {
			name : "typeName",
			label : nps.i18n("LABEL_MEMBER_TYPE"),
			width : "5%",
			sort:["a.type asc","a.type desc"]
			
			
		}, {
			name : "groupName",
			label : nps.i18n("LABEL_MEMBER_GROUP"),
			width : "5%",
			template:"formatGroupName"
			
		},  
		   {
			name : "lifecycle",
			label : nps.i18n("LABEL_MEMBER_LIFECYCLE"),
			width : "5%",
			type:"yesno"
		}, {
			name : "registerTime",
			label : nps.i18n("LABEL_MEMBER_REGISTERTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["c.register_time asc","c.register_time desc"]
		}, {
			name : "loginTime",
			label : nps.i18n("LABEL_MEMBER_LATESTTIME"),
			width : "10%",
			formatter:"formatDate",
			sort: ["c.login_time asc","c.login_time desc"]
		}, {
			label : nps.i18n("LABEL_MEMBER_OPERATE"),
			width : "10%",
			template:"drawEditMember"
		} ],
		dataurl : memberListUrl
	});
	refreshData();
	
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   });
	 
	 
	//启用单行
	   $j("#table1").on("click",".func-button.enable",function(){
	        var curObject=$j(this);
	        nps.confirm(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_CONFIRM_ENABLE_USER"), function(){

	            var json={"ids":curObject.attr("val"),"state":1};

	        	var _d = nps.syncXhr(enableOrDisableMemberUrl, json,{type: "GET"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS"));
	        		refreshData();
	        	}
	            	
	        	else{
	        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
	        	}
	            	
	        });
	    });
	   
	 //禁用单行
	   $j("#table1").on("click",".func-button.disable",function(){
	        var curObject=$j(this);
	        nps.confirm(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_CONFIRM_DISABLE_USER"), function(){

	            var json={"ids":curObject.attr("val"),"state":0};

	        	var _d = nps.syncXhr(enableOrDisableMemberUrl, json,{type: "GET"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS"));
	        		refreshData();
	        	}
	        	else{
	        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
	        	}
	            	
	        });
	    });

	    refreshData();
	 
	 //批量启用
	 $j(".button.batchEnable").click(function(){
	    	var data="";

	    	$j(".checkId:checked").each(function(i,n){
	        	if(i!=0){
	            	data+=",";
	            	}
	        	data+=$j(this).val();
	        });
	    	if(data==""){
	    		nps.info(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_TIP_ENABLE_NOSEL"));
	    		return ;
	    	}
	        nps.confirm(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_CONFIRM_ENABLE_USER"), function(){

	        	
	            var json={"ids":data,"state":1};
	        	var _d = nps.syncXhr(enableOrDisableMemberUrl, json,{type: "GET"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS"));
	        		refreshData();
	        	}
	            	
	        	else{
	        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
	        	}
	            	
	        });
	        refreshData();
	    });
	 //批量禁用
	 $j(".button.batchDisable").click(function(){
	    	var data="";

	    	$j(".checkId:checked").each(function(i,n){
	        	if(i!=0){
	            	data+=",";
	            	}
	        	data+=$j(this).val();
	        });
	    	if(data==""){
	    		nps.info(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_TIP_NOSEL"));
	    		return ;
	    	}
	        nps.confirm(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_CONFIRM_DISABLE_USER"), function(){

	        	
	            var json={"ids":data,"state":0};
	        	var _d = nps.syncXhr(enableOrDisableMemberUrl, json,{type: "GET"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS"));
	        		refreshData();
	        	}
	            	
	        	else{
	        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.exception.message);
	        	}
	            	
	        });
	        refreshData();
	    });

	 

});




