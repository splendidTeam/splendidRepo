//------------------------国际化--------------------------------
$j.extend(loxia.regional['zh-CN'],{
	"INFO":"提示信息",
	"CHOOSEOPTION_GROUP_GROUPCODE_EXISTS":"分组编码重复",
	"CHOOSEOPTION_GROUP_DUPLICATED_OPTIONLABEL":"选项名重复",
	"CHOOSEOPTION_GROUP_DUPLICATED_OPTIONVALUE":"选项值重复",
	"SUCCESSFUL-SAVE":"保存成功",
	"CHOOSEOPTION_GROUP_CHECK_ERROR":"错误信息"
});
//-----------------------常量声明-------------------------------

//选项组列表页
var optionGroupListUrl = base +'/option/optionGroupList.htm?keepfilter=true';

//验证选项组编码是否重复
var validateOptionGroupCodeUrl = base +'/option/validateOptionGroupCode.json';



//-------------------------函数---------------------------------


//-----------------------load page------------------------------
$j(document).ready(function(){
	
	/**
	 * 提交表单
	 */
    $j(".button.orange.saveoptgroup").click(function(val){
    	var flag=true;
    	$j("input[id='input3']").each(function(indexi,datai){
    		$j("input[id='input3']").each(function(indexj,dataj){
    			if(indexi!=indexj&&datai.value==dataj.value){
    				$j(this).addClass("ui-loxia-error");
        			nps.info(nps.i18n("INFO"),nps.i18n("CHOOSEOPTION_GROUP_DUPLICATED_OPTIONLABEL"));
        			flag=false;
        		}
        	});
    	});
    	$j("input[id='input2']").each(function(indexi,datai){
    		$j("input[id='input2']").each(function(indexj,dataj){
    			if(indexi!=indexj&&datai.value==dataj.value){
    				$j(this).addClass("ui-loxia-error");
        			nps.info(nps.i18n("INFO"),nps.i18n("CHOOSEOPTION_GROUP_DUPLICATED_OPTIONVALUE"));
        			flag=false;
        		}
        	});
    	});
    	
    	if(flag){
    		//验证选项编码是否重复
    		var groupCode=$j("input[name='groupCode']").val();
    		groupCode =groupCode.trim();
			if( groupCode!= ''){
			var result= nps.syncXhr(validateOptionGroupCodeUrl, {'groupCode':groupCode},{type: "GET"});
			
			//编码不存在
			if(result!=undefined&&result.isSuccess==true){
				nps.submitForm('createForm',{mode:'async',
					successHandler : function(data){
						if(data.isSuccess)
						{
							nps.info(nps.i18n("INFO"),nps.i18n("SUCCESSFUL-SAVE"));
							window.location.href = optionGroupListUrl;
						}
				   }});
		    }
		    else
		        nps.info(nps.i18n("CHOOSEOPTION_GROUP_CHECK_ERROR"),nps.i18n("CHOOSEOPTION_GROUP_GROUPCODE_EXISTS"));
			 }
			
    	}	
    });
	
	
	//返回
	$j(".button.return").click(function(){
		
		window.location.href = optionGroupListUrl;
		
	 });
	
});

