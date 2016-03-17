$j.extend(loxia.regional['zh-CN'],{
    "PWD_NOT_MATCH":"密码不匹配",
});

function _setStateForPwd(){
	loxia.byId($j("input[name=confirmNewPwd]")).state(true);
	loxia.byId($j("input[name=newPwd]")).state(true);
}
function check1(data) {
	if(data == $j("input[name=confirmNewPwd]").val()) {
		_setStateForPwd();
		return loxia.SUCCESS;
	} else {
		return nps.i18n("PWD_NOT_MATCH");
	}
}
function check2(data) {
	if(data == $j("input[name=newPwd]").val()) {
		_setStateForPwd();
		return loxia.SUCCESS;
	} else {
		return nps.i18n("PWD_NOT_MATCH");
	}
}
$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    nps.iframeblock.css({"height": parseInt($j(window).height())-150});
    $j(window).resize(function(){
        nps.iframeblock.css({"height": parseInt($j(window).height())-150});
    });


	
	$j(".button.orange.updateuser").on("click",function(){
		$j(".table-if").attr("src","/updateUser.htm");

	});
	
	$j(".head-dia-cur.head-end").click(function(){
		window.location.href='/logout.htm';
	});

	$j("#update-pwd").click(function(){
		$j(".head-cust-inform>div>div").removeClass("block");
		$j(".update-pwd").addClass("block");
		$j("input[name=newPwd]").val("");
		$j("input[name=oldPwd]").val("");
		$j("input[name=confirmNewPwd]").val("");
	});
	var lang = $j.cookie("i18n_lang_key");
	if(typeof(lang)=='undefined'){
		lang = "zh_cn";
	}
	var value = $j(".i18n-langs").find("option[value='"+lang+"']").text();
	$j(".lang-setting").html(value);
	//选择语言
	$j(".lang-setting").click(function(){
		var lang = $j.cookie("i18n_lang_key");
		if(typeof(lang)=='undefined'){
			lang = "zh_cn";
		}
		var dialog =$j("#dialog-lang-select");
		dialog.find("option[value='"+lang+"']").attr("checked","checked");
		dialog.dialogff({type:'open',close:'in',width:'300px',height:'170px'});
	});
	$j(".lang-btn-ok").click(function(){
		var lang = $j(".i18n-langs").val();
	  	nps.asyncXhrPost(base+"/i18n/switchI18nLang.json", {"lang":lang},{successHandler:function(data, textStatus){
				if(data.isSuccess){
					$j("#dialog-lang-select").dialogff({type:'close'});
					window.location.reload();
				}else{
					nps.info(nps.i18n("INFO_TITLE_DATA"),"设置失败");
				}
		 }});
	});
});
