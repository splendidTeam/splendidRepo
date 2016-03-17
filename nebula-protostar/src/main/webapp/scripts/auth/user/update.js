
$j.extend(loxia.regional['zh-CN'],{
	"USER_OPERATOR_MODIFY":"修改",
	"USER_OPERATOR_DELETE":"删除",
    "USER_PWD_NOT_MATCH":"密码输入不一致",
    "USER_EMAIL_NOT_VALID":"邮箱不符合规则",
    "USER_MOBILE_NOT_VALID":"手机不符合规则",
    "USER_ORG_NOT_SELECT":"请选择所属组织",
    "USER_UNAME_EXISTS":"用户名已存在",
    "USER_NOTSEL_ROLE":"请选择角色",
    "USER_NOTSEL_ORG":"请选择组织",
    "USER_ROLE_EXISTS":"角色重复",
    "USER_TLABEL_ROLE":"角色",
    "USER_TLABEL_ORG":"对应组织",
    "USER_TLABEL_ORGTYPE":"组织类型",
    "USER_TLABEL_MODIFY":"修改对应组织",
    "USER_TLABEL_DELETE":"删除",
    "USER_TIP_INFO":"操作提示",
    "USER_TIP_SAVED_USER_ROLE":"已成功保存用户角色",
    "USER_TIP_REMOVED_USER_ROLE":"已成功移除所选用户角色",
    "USER_CONFIRM_DELETE_USER_ROLE":"确认删除所选的用户角色?",
    "USER_CONFIRM_DELETE":"确认删除",
    "USER_FORM_CHECK_ERROR":"错误信息",
    "USER_OPERATE_SUCCESS": "操作成功",
    "USER_OPERATE_FAILURE": "操作失败",
    "USER_TIP_NO_USERROLE": "当前暂未分配用户角色",
    "USER_ORG_ROLE":"组织类型与角色不一致",
    "USER_NOT_ROLE":"没有角色",
    "USER_NOT_ORGANIZATION":"没有组织"
});
//用户列表页
var userListUrl='/user/list.htm?keepfilter=true';
//验证用户名重复
var userValidNameUrl='/user/validate-login.json';
//保存用户角色
var saveUserRoleUrl='/user/save-user-role.json';
//删除用户角色
var removeUserRoleUrl='/user/remove-user-role.json';
//验证组织与角色是否一致
var userValidOrgRole = base + '/user/validate-org-role.json'
//开始序号
var curUrId=urData.length;
/**
 * 验证是否重复
 * 返回true表示重复
 */
function checkUserNameExists(){

	var userName=$j("input[name='userName']").val();
	  if( userName!= ''){
		var result= nps.syncXhr(userValidNameUrl, {'name':userName},{type: "GET"});
		if(result.result!=undefined&& result.result=='success'){
			return true;
		}
	  }
	  return false;
}

function userFormValidate(form){
	   
   

    //修改过的用户名,才会检查用户名称是否已存在
    if($j("input[name='oldUserName']").val()!=$j("input[name='userName']").val()&&checkUserNameExists()){
        
   	 return nps.i18n("USER_UNAME_EXISTS");
       
   }
    
    if($j("input[name='password']").val() != $j("input[name='passwordAgain']").val()){
    	 return nps.i18n("USER_PWD_NOT_MATCH");
        
    }

    var emailreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
   
    if(!emailreg.test($j("input[name='email']").val()))
     {
                 
         return nps.i18n("USER_EMAIL_NOT_VALID");
                
     }

    var mobilereg=/^((1[0-9]{2}))+\d{8}$/;
    if(!mobilereg.test($j("input[name='mobile']").val()))
    {
                
        return nps.i18n("USER_MOBILE_NOT_VALID");
               
    }
    
    return loxia.SUCCESS;  
}



function drawDeleteButton(data, args, idx){
	if(pageStatue=='readonly'){
		return "&nbsp;";
	}
	else
		return "<a href='javascript:void(0);' val='"+loxia.getObject("urId", data)+"' class='func-button delete'>"+nps.i18n("USER_OPERATOR_DELETE")+"</a>";
}

function drawEditButton(data, args, idx){

	if(pageStatue=='readonly'){
		return "&nbsp;";
	}
	
	var orgTypeId=loxia.getObject("orgTypeId", data);
	if(orgTypeId==2){
		 var curOrgId=$j("#orgSelect").val();
		//当前用户所属系统组织,可以进行修改
		 if(curOrgId=="1"){
		
			 return "<a href='javascript:void(0);' val='"+loxia.getObject("urId", data)+"' class='func-button edit'>"+nps.i18n("USER_OPERATOR_MODIFY")+"</a>";
		 }
		 //当前用户所属店铺组织,不可以进行修改
		 else{
			 return "&nbsp;";
		 }
	}
	else{
		return "&nbsp;";
	}
}

function drawOrgList(data, args, idx){
	var urId=loxia.getObject("urId", data);
	
	var html=showOrgList(urId);
	
	return "<div id='orgListValue_"+urId+"' >"+html+"</div>";
}
/**
 * 显示表格中的组织
 * 如果超出十个,就只显示10个组织，外加一个查看按钮
 * 十个组织以下，则全部显示
 * @param urId
 * @param addDiv 表示需要生成外层的div  <div id='orgListValue_"+urId+"' >
 * @return
 */
function showOrgList(urId){
	var urObject=userRoleList.findByUrId(parseInt(urId));
	var orgArray=urObject.orgs.split(",");
	var html='';
	if(orgArray.length>1){
		
		for(var i=0;i<orgArray.length && i<1;i++){	//只显示前十个再加一个更多按钮
			html+='<span class="children-org">'+orgArray[i]+'</span>';
		}
		html+='<span class="children-org"><a class="func-button more" val="'+urId+'" href="javascript:void(0);">更多</a></span>'
			
	}
	else{
		
		for(var i in orgArray){
			html+='<span class="children-org">'+orgArray[i]+'</span>';
		}
		
	}

	return html;
}

/**
 * 用于查看模式禁用所有
 * @return
 */
function disableEdit(){
	if(pageStatue=='readonly'){
		
		$j(".button-line").hide();
		$j(".addrole").hide();
		$j("input").attr("disabled","disabled");
		$j("select").attr("disabled","disabled");
	}
}

/**
 * 获取所属组织
 * @return
 */
function querySelOrg(){

	return $j("#orgSelect").val();
}

/**
 * 选中组织类型
 * @return
 */
function chooseFilterOrgType(sel, obj){
	var val=sel.split("-")[0];
	selOrgType(val);
    return loxia.SUCCESS;
}


/**
 * 选中组织类型以后同时联动修改:
 * 角色列表及组织列表
 */
function selOrgType(val){
	var roleHtml='';
	var orghtml='';
    
	if(val == '1'){

		for(var o in sysRoleList){
			roleHtml+='<option value="'+sysRoleList[o].id+'-'+sysRoleList[o].name+'">'+sysRoleList[o].name+'</option>';
		}

		for(var o in sysOrgList){
			orghtml+='<div class="children-store"><input class="chk-role" checked="checked" type="checkbox" name="userOrgAdd" value="'+sysOrgList[o].id+'" nvalue="'+sysOrgList[o].name+'">'+sysOrgList[o].name+'</div>';
		}
		
		$j("#span-allchk-role").hide();
        
    }else {
    	for(var o in shopRoleList){
    		roleHtml+='<option value="'+shopRoleList[o].id+'-'+shopRoleList[o].name+'">'+shopRoleList[o].name+'</option>';
		}

    	for(var o in shopOrgList){
    		
        	if(querySelOrg()=='1'){
        		//console.log("进入了");	
				orghtml+='<div class="children-store"><input class="chk-role" type="checkbox" name="userOrgAdd" value="'+shopOrgList[o].id+'" nvalue="'+shopOrgList[o].name+'">'+shopOrgList[o].name+'</div>';
        	}
        	else if(parseInt(val)==shopOrgList[o].id){
        		orghtml+='<div class="children-store"><input class="chk-role" checked="checked" type="checkbox" name="userOrgAdd" value="'+shopOrgList[o].id+'" nvalue="'+shopOrgList[o].name+'">'+shopOrgList[o].name+'</div>';
        	}
		}
    	
    	$j("#span-allchk-role").show();
       
    }
    $j("#roleSelect").html(roleHtml);
    $j("#addUserRoleDiv").html(orghtml);
}

//通过用户所属组织来决定用户可选的组织类型
function orgTypeSelInit(){
	var html='';
	for(var o in orgTypeList){
		 if(querySelOrg()==1){
			 html+='<option value="'+orgTypeList[o].id+'-'+orgTypeList[o].name+'">'+orgTypeList[o].name+'</option>';
		 }else{
			 if(orgTypeList[o].id==2){
				 html='<option value="'+orgTypeList[o].id+'-'+orgTypeList[o].name+'">'+orgTypeList[o].name+'</option>';
			 }
		 }
	}
    $j("#orgTypeSelect").html(html);
    selOrgType(querySelOrg());
//    var orgTypeId = $j('#orgTypeSelect').val();
//    if(orgTypeId != ''){
//    	selOrgType(orgTypeId.split('-')[0]);
//    }else{
//    	selOrgType(querySelOrg());
//    }
}


function refreshUserRole(){
	
	var urList=userRoleList.findAll();
	//用户角色显示表格中没有一条数据
	if(urList.length==0){
		$j("#noUserRoleTable").html("&nbsp;&nbsp;"+nps.i18n("USER_TIP_NO_USERROLE"));
		
		$j("#noUserRoleTable").show();
		$j("#userRoleTable").hide();
	}else{
		$j("#noUserRoleTable").hide();
		$j("#userRoleTable").show();
		$j("#userRoleTable").loxiasimpletable("refresh",userRoleList.findAll());
	}

	
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();

    //初始化用户角色关系
    userRoleList.init(urData,orgTypeList,sysRoleList,shopRoleList);

    orgTypeSelInit();

    /**
     * 添加用户角色关系
     * 
     * 会做基本的检测
     */
    $j(".func-button.addrole").click(function(){
        	var ortTypeSel=$j("#orgTypeSelect").val();
        	var roleSel=$j("#roleSelect").val();
        	if(roleSel == null){
        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_NOT_ROLE"));
        		return;
        	}
        	
        	if(ortTypeSel == null){
        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_NOT_ORGANIZATION"));
        		return;
        	}
        	
			var orgTypeId=ortTypeSel.split("-")[0];
			var orgTypeName=ortTypeSel.split("-")[1];
			var roleId=roleSel.split("-")[0];
			var roleName=roleSel.split("-")[1];
			
			var orgList=new Array();
			var orgNameList=new Array();
			 $j("input[name='userOrgAdd']:checked").each(function(i,n){
				 orgList[i]=$j(this).val();
				 orgNameList[i]=$j(this).attr("nvalue");
			 });
			//console.log(roleId);
			if(roleId==''){
				nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_NOTSEL_ROLE"));
				return ;
			}
			if(orgList.length==0){
				
				nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_NOTSEL_ORG"));
				return ;
			}
			
			//组织类型与角色是否一致
			var isOrgRoleAccordance = nps.syncXhr(userValidOrgRole, {'orgTypeId':parseInt(orgTypeId),'orgs':orgList.join(',')},{type: "GET"});
			if(isOrgRoleAccordance){
				nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_ORG_ROLE"));
				return ;
			}

			var object=userRoleList.findByRoleId(parseInt(roleId));
			if(object!=null){
				
				nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_ROLE_EXISTS"));
				return ;
			}
			var userId=$j("input[name='userId']").val();
			var result= nps.syncXhr(saveUserRoleUrl, {'userId':parseInt(userId),'roleId':parseInt(roleId),'orgs':orgList.join(',')},{type: "GET"});

			if(result.exception!=undefined ){
				nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),result.exception.message);
				return ;
			}
			
			
			userRoleList.addElement(curUrId++,parseInt(orgTypeId),parseInt(roleId),orgList,orgNameList)
			
			
			refreshUserRole();
     });

    
    $j("#userRoleTable").loxiasimpletable({
    	cols:[{name:"role",label:nps.i18n("USER_TLABEL_ROLE"),width:"20%"},
    	      {name:"orgType",label:nps.i18n("USER_TLABEL_ORGTYPE"),width:"20%"},
    	      {name:"orgs",label:nps.i18n("USER_TLABEL_ORG"),width:"40%", align:"left",template:"drawOrgList"},
    	      {label:nps.i18n("USER_TLABEL_MODIFY"),width:"10%", align:"left",template:"drawEditButton"},
    	      {label:nps.i18n("USER_TLABEL_DELETE"),width:"10%", align:"left",template:"drawDeleteButton"}]
    	});

    refreshUserRole();

    /**
     * 提交表单
     */
    $j(".button.orange.submit").click(function(){
    	nps.submitForm('userForm',{mode:'sync'})
      });

    /**
     * 返回按钮
     */
    $j(".button.return").click(function(){
     	location.href=userListUrl; 
    });


    
    //修改角色对应组织
	 $j("#userRoleTable").on("click",".func-button.edit",function(){
		 var urId=$j(this).attr("val");
		 var urObject=userRoleList.findByUrId(parseInt(urId));

		 var html='<div style="overflow-y:scroll;border:1px solid;height:100px;">';
		 var orgNameArray=urObject.orgs.split(",");

		 //当前用户的组织
		 var curOrgId=$j("#orgSelect").val();
		 
		 //如果是系统组织
		 if(curOrgId=='1'){
			 //重新绘制编辑框中的checkbox
			 for(var j=0;j<shopOrgList.length;j++){
	
				 var eleHtml='';
				 //遍历已选中的数据
			 	 for(var i=0;i<urObject.orgIds.length;i++){
	
					if(shopOrgList[j].id== urObject.orgIds[i]){
			 			eleHtml+='<div class="children-store"><input checked="checked" type="checkbox" name="userOrg_'+urObject.urId+'" value="'+urObject.orgIds[i]+'" nvalue="'+orgNameArray[i]+'">'+orgNameArray[i]+'</div>';
						break;
					}
	  			
			 	  }
			 	  if(eleHtml==''){
	
			 		 eleHtml+='<div class="children-store"><input type="checkbox" name="userOrg_'+urObject.urId+'" value="'+shopOrgList[j].id+'" nvalue="'+shopOrgList[j].name+'">'+shopOrgList[j].name+'</div>';
				 }
	
			 	html+=eleHtml;
			 }
		 }
		 //所属店铺组织,只显示某个店铺
		 else{
			 var eleHtml='';
			 
			 for(var j=0;j<shopOrgList.length;j++){
				 if(shopOrgList[j].id== parseInt( curOrgId)){
					 eleHtml='<div class="children-store"><input checked="checked" type="checkbox" name="userOrg_'+shopOrgList[j].id+'" value="'+shopOrgList[j].id+'" nvalue="'+shopOrgList[j].name+'">'+shopOrgList[j].name+'</div>';
				 }
			
			 }
			 
			 html+=eleHtml;
		 }
		 
			html+='</div>';
			html+='<div><a style="line-height:30px;" class="func-button orgok" href="javascript:void(0)" val='+urId+'>'+nps.i18n("LABEL_OK")+'</a>&nbsp;<a style="line-height:30px;" class="func-button orgcancel" href="javascript:void(0)" val='+urId+'>'+nps.i18n("LABEL_CANCEL")+'</a></div>';
			$j("#orgListValue_"+urId).html(html);
 
			//确定修改
			 $j(".func-button.orgok").on("click",function(){
				 var urId=$j(this).attr("val");
				 var urObject=userRoleList.findByUrId(parseInt(urId));
				 //遍历选中的角色组织对应关系
				 var orgIds=new Array();
				 var orgNames=new Array();
				 $j("input[name='userOrg_"+urId+"']:checked").each(function(i,n){
					 orgIds[i]=$j(this).val();
					 orgNames[i]=$j(this).attr("nvalue");
				 });
				if(orgIds.length==0){
					nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("USER_NOTSEL_ROLE"));
					return ;
				}


				var userId=$j("input[name='userId']").val();
				var result= nps.syncXhr(saveUserRoleUrl, {'userId':parseInt(userId),'roleId':parseInt(urObject.roleId),'orgs':orgIds.join(',')},{type: "GET"});

				if(result.exception!=undefined ){
					nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),result.exception.message);
					return ;
				}
				
				 userRoleList.modifyElement(urId,urObject.orgTypeId,urObject.roleId,orgIds,orgNames)
				 nps.info(nps.i18n("USER_TIP_INFO"),nps.i18n("USER_TIP_SAVED_USER_ROLE")); 
				 var html=showOrgList(urId);
				 $j("#orgListValue_"+urId).html(html);
				 
			 });
			 //取消修改
			 $j(".func-button.orgcancel").on("click",function(){
				 var urId=$j(this).attr("val");
				 var html=showOrgList(urId);
				 $j("#orgListValue_"+urId).html(html);
			 });
					
	 });
	 
	 
	//表格中组织一栏点击更多
	 $j("#userRoleTable").on("click",".func-button.more",function(){
		 var urId=$j(this).attr("val");
		 var urObject=userRoleList.findByUrId(parseInt(urId));
		 var orgArray=urObject.orgs.split(",");
		 var html='';
			for(var i in orgArray){
				html+='<span class="children-org">'+orgArray[i]+'</span>';
			}
			html+='<span class="children-org"><a class="func-button packup" val="'+urId+'" href="javascript:void(0);">收起</a></span>'
			$j("#orgListValue_"+urId).html(html);		
	 });
	 
	 //表格中组织一栏点击收起
	 $j("#userRoleTable").on("click",".func-button.packup",function(){
		 var urId=$j(this).attr("val");
		
		 var html=showOrgList(urId);
			
		$j("#orgListValue_"+urId).html(html);		
	 });


	//删除角色对应组织
	$j("#userRoleTable").on("click",".func-button.delete",function(){
		 var urId=$j(this).attr("val");
		nps.confirm(nps.i18n("USER_CONFIRM_DELETE"),nps.i18n("USER_CONFIRM_DELETE_USER_ROLE"), function(){
			
			 var urObject=userRoleList.findByUrId(parseInt(urId));

			var userId=$j("input[name='userId']").val();
			var result= nps.syncXhr(removeUserRoleUrl, {'userId':parseInt(userId),'roleId':parseInt(urObject.roleId),'orgs':urObject.orgIds.join(',')},{type: "GET"});

			if(result.exception!=undefined ){
				nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),result.exception.message);
				return ;
			}
			
			userRoleList.deleteElement(parseInt(urId));
			nps.info(nps.i18n("USER_TIP_INFO"),nps.i18n("USER_TIP_REMOVED_USER_ROLE")); 
			refreshUserRole();
		});
		
	 });
	
	/**
	 * 新增角色全选按钮
	 */
	$j(".func-button.allchk-role").on("click",function(){
		
		var checked=$j(this).attr("checked");
		
		if(checked=='checked'){//全选按钮选中
			
			$j(".chk-role").each(function(i,n){
				$j(this).attr("checked",true);
			});
		}
		else{
			
			$j(".chk-role").each(function(i,n){
				$j(this).attr("checked",false);
			});
		}
	})
	
	disableEdit();
});

