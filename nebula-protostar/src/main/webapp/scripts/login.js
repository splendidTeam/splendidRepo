if(top != this) {
	window.parent.location.href = window.location.href;
}
$j.extend(loxia.regional['zh-CN'],{
	"USER_DISABLED":"该账号已被禁用！",
	"LOGIN_FAILURE":"账号或密码不正确，请重新登录！",
});
$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
	nps.init();
	
	$j(".login").css({"margin-top":((parseInt($j(window).height())-parseInt($j(".login").height()))/2)-50});
	$j(window).resize(function(){$j(".login").css({"margin-top":((parseInt($j(window).height())-parseInt($j(".login").height()))/2)-50});});

	$j(".reset").click(function(){
		$j("input[name=j_username]").val("");
		$j("input[name=j_password]").val("");
		loxia.byId($j("input[name=j_username]")).state(null);
		loxia.byId($j("input[name=j_password]")).state(null);
	});

	$j(".orange.submit").click(function(){
		loxia.submitForm("login-form");
	});
	
	switch($j("#type").val()){
		case "1": nps.info(nps.i18n("LOGIN_FAILURE"));break;
		case "2": nps.info(nps.i18n("USER_DISABLED"));break;
		default:break;
	}
});