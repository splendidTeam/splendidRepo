
//属性列表页点击修改后,下一个商品属性管理页面,行业属性列表中该属性默认选中
function defaultSelectedProperty(){
	var propertyId = $j('input[name="propertyId"]').val();
	if(!$j(".ui-sortable #"+propertyId).hasClass('selected')){
		$j(".ui-sortable #"+propertyId).addClass('selected');
	}
	var obj = {};
	for(var i=0; i<jsonArray.length;i++){
		var id = jsonArray[i].id;
		if(id == propertyId){
			obj = jsonArray[i];
			break;
		}
	}
	//设置右边值
	setMutlLangValue(obj);
}

$j(document).ready(function(){
			var industryId =$j("#industryId").val();
			var json={"industryId":industryId};
	  		var backWarnEntity = loxia.syncXhr(base+'/i18n/property/propertyListByIndustryid.json', json,{type: "GET"});
	  		if(backWarnEntity.isSuccess){
	  			$j("#industryId").val(industryId);
	  			$j("#industrySelDiv").css("display","none");
				$j("#propertySelDiv").css("display","block");
				//$j("#industryPropertyDiv").html(name+nps.i18n("SYSTEM_PROPERTY_INDUSTY_LIST"));
				jsonArray = backWarnEntity.description;
				
				$j.each(jsonArray,function(i,val){
					jsonArray[i].state = parseInt(jsonArray[i].lifecycle);
					jsonArray[i].num = parseInt(jsonArray[i].sortNo);
					if(i18nOnOff){
						var name = jsonArray[i].name;
						if(name!=null){
							jsonArray[i].text = name.defaultValue;
						}else{
							jsonArray[i].text = "";
						}
					}else{
						var name = jsonArray[i].name;
						if(name!=null){
							jsonArray[i].text = name.value;
						}else{
							jsonArray[i].text = "";
						}
						
					}
					jsonArray[i].type = formatEditingType(jsonArray[i].editingType);
				});
				
				$j("#property-list").dragarraylist({
					newarray:jsonArray
					, add:true
					, callBack:function(o){
						setMutlLangValue(o);
					},deleteli:function(o){
					     deleteValue(o);
					},beforecallBack:function(o){
  						beforeValue(o);
  					}
				});					
	  		 }
	  		
	  		defaultSelectedProperty();
$j("#proname").blur( function () {
	validatename();
	});
});
$j(window).load(function(){
	$j(window.top.document).find(".button-cancel").click(function(e){
		if($j(".nyyy").length==1){
			if($j(".list-all li.selected").prev("li").length>0){
				var beforeli=$j(".list-all li.selected").prev("li");
				$j(".no-sign").remove();
				beforeli.click();
			}
			else{
				var nextli=$j(".list-all li.selected").next("li");
				$j(".no-sign").remove();
				nextli.click();
			}
		}
		
        $j(".no-sign").removeClass("nyyy");
    });
});