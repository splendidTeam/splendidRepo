$j.extend(loxia.regional['zh-CN'],{
    "USER_PWD_NOT_MATCH":"密码输入不一致",
    "USER_EMAIL_NOT_VALID":"邮箱不符合规则",
    "USER_MOBILE_NOT_VALID":"手机不符合规则",
    "USER_ORG_NOT_SELECT":"请选择所属组织",
    "USER_UNAME_EXISTS":"用户名已存在",
    "USER_FORM_CHECK_ERROR":"错误信息",
    "USER_OPERATE_SUCCESS": "操作成功",
    "USER_OPERATE_FAILURE": "操作失败",
    	
});


//用户列表页
var userListUrl='/user/list.htm?keepfilter=true';
//验证用户名重复
var userValidNameUrl='/user/validate-login.json';

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
   
    console.log("xxx");

    if(checkUserNameExists()){
        
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


$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();

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

    /**
     * 重置按钮
     */
    $j(".button.reset").click(function(){
    	$j("input[name='userName']").val("");
    	$j("input[name='password']").val("");
    	$j("input[name='passwordAgain']").val("");
    	$j("input[name='email']").val("");
    	$j("input[name='realName']").val("");
    	$j("input[name='mobile']").val("");
    	$j("select[name='orgId']").val("1");
    	
    	
    	loxia.byId($j("input")).state(true);
    	loxia.byId($j("select")).state(true);
    });
});