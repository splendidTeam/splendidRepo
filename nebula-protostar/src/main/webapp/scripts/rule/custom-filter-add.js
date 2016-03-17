$j.extend(loxia.regional['zh-CN'],{
    "NPS_FORM_CHECK_INFO":"系统提示",
    "CSFILTER_ADD_SUCCESS":"自定义筛选器 {0} 已经成功创建",
    "CSFILTER_ADD_FAILURE":"自定义筛选器{0} 创建失败",
    "CSFILTER_UPDATE_SUCCESS":"自定义筛选器 {0} 已更新",
    "CSFILTER_UPDATE_FAILURE":"自定义筛选器 {0} 更新失败",
    "CSFILTER_REGEXP_PROMPT":"必须为字母(a~z或A~Z)开头",
    	
});

var backUrl = base +"/rule/csfilterclseslist.htm";


function checkServiceName(val, obj){
	// 以字母开始,可以包含数字.
	var regExp = /^([a-z]|[A-Z])([0-9]|[a-z]|[A-Z])*/;
	if(!regExp.test(val)){
		return nps.i18n("CSFILTER_REGEXP_PROMPT");
	}
	return loxia.SUCCESS;
}



$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    var scopeType = $j("#scopeType").val();
    if(scopeType!=1){
    	$j("#shopSelect").parent().parent().hide();
    }else{
    	$j("#shopSelect").parent().parent().show();
    }
	
    $j("[name=scopeType]").change(function(){
        var scopeType = $j(this).val();
        if(scopeType!=1){
        	$j("#shopSelect").parent().parent().hide();
        }else{
        	$j("#shopSelect").parent().parent().show();
        }
    });
    
    //保存
    $j(".submit").click(function(){
    	
    	var scopeName =$j.trim($j("#scopeName").val());
    	
    	 nps.submitForm('csFilterForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess){
					
					return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("CSFILTER_ADD_SUCCESS",[scopeName]));
				}else{
					return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("CSFILTER_ADD_FAILURE",[scopeName]));
				}
		}});
    });
    
  //返回
    $j(".return").click(function(){
    	window.location.href=backUrl+"?keepfilter=true";
    });
    
});

