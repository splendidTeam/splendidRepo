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
var userListUrl='/user/list.htm';


function userFormValidate(form){
   
    console.log("xxx");

    if($j("input[name='password']").val()!=''&&$j("input[name='password']").val() != $j("input[name='passwordAgain']").val()){
    	 return nps.i18n("USER_PWD_NOT_MATCH");
        
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
    	nps.submitForm('userForm',{mode:'async',successHandler: function(){
			  nps.info(nps.i18n('OPERATOR_TIP'),nps.i18n('OPERATE_SUCCESS'));
		}});
      });



});