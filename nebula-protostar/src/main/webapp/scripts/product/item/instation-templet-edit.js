$j.extend(loxia.regional['zh-CN'],{
    "NPS_FORM_CHECK_INFO":"系统提示",
    "INFO_SUCCESS":"保存成功！",
    "INFO_FAILURE":"保存失败！"
});

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    //保存
    $j(".saveForm").click(function(){
    	 nps.submitForm('roleForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess){
					nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("INFO_SUCCESS"));
				}else{
					nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("INFO_FAILURE"));
				}
		}});
    });
    
	// 返回
	$j(".return").click(function() {
		window.location.href = "/instation/message/template";
	});
    
});

