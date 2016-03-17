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
	    //"LABEL_MEMBER_OPERATE":"操作",
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
	   // "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"禁用成功",
	    "INFO_SUCCESS":"请选择发送站内信模板类型",
	    "USER_TYPE":"请选择用户的发送方式",
	    "CHECK_USER":"请选择用户",
	    "INFO_SUCCESS_TY":"操作成功!",
	    "INFO_FAIL":"操作成功!",
	    "INFO_NULL":"请输入文本框的值!",
	    "INFO_CONFIRM":"有重复的数据是否提交",
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
	
	
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('"+tempDataUrl+"');\">"+loxia.getObject("loginName", data)+"</a>";
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

function checktype(type){
	if(type==1){
		$j("#tip").css("display","inline-block");
	}else{
		$j("#tip").css("display","none");
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
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox' /> 全选",
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
		}],
		dataurl : memberListUrl
	});
	refreshData();
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   });
	    refreshData();
	    
	   var URL="/instation/saveInstationSendMessage.json";
	    
	    //发送按钮
	  $j(".confirmsendtext").click(function() {
		if ($j("#text").val()==null ||$j("#text").val()=="") {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_NULL"));
			return;
		} else if ($j("input[type='radio']:checked").val() == null) {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("USER_TYPE"));
			return;
		}else if(  $j(".messagethp").find("option:selected").val() == 0){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_SUCCESS"));
			return;
		}
		//得到选择的用户发送类型
		var type = $j("input[type='radio']:checked").val();
		
		//得到文本框的值
		var textvalue = $j("#text").val();
		
		//选择的站内信的模板ID
		var tmpid = $j(".messagethp").find("option:selected").val();
		
		checktype(textvalue, type, function(ret){
			if(ret==""){
		      var JSON={type:type,textvalue:textvalue,tmpid:tmpid};
		  	  ajaxjosn(URL, JSON);
		    }else{
		    	nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("以下数据格式不对</br>"+ret));
		    	return;	
		    }
		});
		
	});

	$j(".confirmsenduserid").click(function() {
		if ($j("input:checkbox[name='chedkId']:checked").length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("CHECK_USER"));
			return;
		} else if ($j(".usersetptype").find("option:selected").val() == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_SUCCESS"));
			return;
		}
		//得到选择的用户发送id
		var userid = "";
		$j("input:checkbox[name='chedkId']:checked").each(function() { 
			userid += $j(this).val() + ","; // 每一个被选中项的值
		});
		//选择的站内信的模板ID
		var messangID = $j(".usersetptype").find("option:selected").val();

        var JSON={type:1,textvalue:userid,tmpid:messangID};
		
		ajaxjosn(URL, JSON);
		
	});
	
	function ajaxjosn(URL,JSON){
		$j.ajax({
			url:URL, 
			data:JSON,
			type: "post",
			success:function(data) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n(data));
			}
		});
	}
	
	//判断用户输入的值是否符合和类型
	function  checktype(textvalue,type, fn){
		var ter="";
		var array= textvalue.split(",");
		var reg =/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		if(findSame(array)){
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CONFIRM"), function(){
				$j(array).each(function(index){
					//ID
					if(type=='1'){
						if(isNaN(this)){
							ter+=this+",";
						}
					}
					//邮箱
					if(type=='2'){
						if(!reg.test(this)){
							ter+=this+",";
						}
					}
				});
				fn( ter.substring(0, ter.length-1) );
				
			});
		}else{
			$j(array).each(function(index){
				//ID
				if(type=='1'){
					if(isNaN(this)){
						ter+=this+",";
					}
				}
				//邮箱
				if(type=='2'){
					if(!reg.test(this)){
						ter+=this+",";
					}
				}
			});
			fn( ter.substring(0, ter.length-1) );
		}
	}
	
	//包含相同元素，相同值为
	function findSame(arr) {
	    arr.sort();
	    for (var i = 0; i < arr.length - 1; i++) {
	        if (arr[i] == arr[i + 1]) {
	            return true;
	        }
	    };
	    return false;
	}
});




