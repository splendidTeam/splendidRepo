$j.extend(loxia.regional['zh-CN'],{
    "NPS_FORM_CHECK_INFO":"系统提示",
    "ROLE_ADD_SUCCESS":"角色 {0} 已经成功创建",
    "ROLE_ADD_FAILURE":"角色 {0} 创建失败",
    "ROLE_UPDATE_SUCCESS":"角色 {0} 已更新",
    "ROLE_UPDATE_FAILURE":"角色 {0} 更新失败",
    	
});

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    var smarkTypeId;
	
    $j("[name=orgTypeId]").change(function(){
        var orgTypeId = $j(this).val();
        $j("#pri_"+smarkTypeId).find("input").attr("checked", false);
        smarkTypeId=orgTypeId;
        $j(".priDiv").each(function(i,n){
        	$j(this).hide();
        });
        $j("#pri_"+orgTypeId).show();
    });
    var orgTypeId = $j("[name=orgTypeId]").val();
    smarkTypeId=orgTypeId;
    $j(".priDiv").each(function(i,n){
    	$j(".priDiv").each(function(i,n){
        	$j(this).hide();
        });
        $j("#pri_"+orgTypeId).show();
    });
    
    //保存
    $j(".saveForm").click(function(){
    	 var isUpdate = $j("#isUpdate").val();
    	 var roleName = $j("#roleName").val();
    	 nps.submitForm('roleForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess){
					if(isUpdate=="1")
						return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_UPDATE_SUCCESS",[roleName]));
					return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_ADD_SUCCESS",[roleName]));
				}else{
					if(isUpdate=="1")
						return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_UPDATE_FAILURE",[roleName]));
					return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_ADD_FAILURE",[roleName]));
				}
		}});
    });
    
  //返回
    $j(".return").click(function(){
    	window.location.href="/role/list.htm?keepfilter=true";
    });
    
});

