$j.extend(loxia.regional['zh-CN'],{
    "CMS_MANAGER_TIPS":"友情提示",
	"CMS_MANAGER_MSG":"请先保存基本信息",
    "CMS_MANAGER_SAVE_MSG":"保存基本信息时未选择模版路径，不能编辑其它信息"
});


$j(document).ready(
		function() {
		$j(this).find(".tag-change-ul").find("li").unbind();
		$j(".ui-tag-change").each(function(){
			
			if(!$j(this).find(".tag-change-ul").find("li").hasClass("selected")){
				$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
			}
			
			$j(this).find(".tag-change-ul").find("li").on("click",function(){
				
                var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
                if(!isClick){
                	//不可选
                	if(thisindex == 0)
                		return ;
                	return  nps.info(nps.i18n("CMS_MANAGER_TIPS"),nps.i18n("CMS_MANAGER_MSG"));
                }else if(isClick && tempPath.length == 0){
                	//不可选
	                if(thisindex!=0){
	                	return  nps.info(nps.i18n("CMS_MANAGER_TIPS"),nps.i18n("CMS_MANAGER_SAVE_MSG"));
	                }
                }else if(isClick && tempPath.length > 0){
                	//可选
                	$j(this).addClass("selected").siblings("li").removeClass("selected");
	                $j(".tag-change-in").eq(thisindex).addClass("block").siblings(".tag-change-in").removeClass("block");
                }
	           
			});
		});
});