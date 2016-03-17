
$j.extend(loxia.regional['zh-CN'],{
	"SHOP_ADDPROPERTY_NAME":"属性名重复",
	"SHOP_ADDPROPERTY_UPDATE_FAIL":"属性修改失败",
});

var validatePropertyNameUrl  =base+ '/shop/validatePropertyName.json';

function checkPropertyNameRepeat(){

	var name = $j("input[name='name']").val();
	var industryId= $j("input[name='industryId']").val();
	  if( name!= ''){

		var result= nps.syncXhr(validatePropertyNameUrl, {"name":name,"industryId":industryId},{type: "GET"});
		if(result.isSuccess!=undefined && result.isSuccess== true){
			return true;
		}
	  }
	  return false;
}

function propertyFormValidate(form){
	var propertyNameO = $j("input[name='propertyName']").val();
	var nameNew = $j("input[name='name']").val();
	if(propertyNameO.trim() != nameNew.trim() ){
		if(!checkPropertyNameRepeat()){
			$j("input[name='name']").addClass("ui-loxia-error");
		   	 return nps.i18n("SHOP_ADDPROPERTY_NAME");
		   }
	}
	 
	 return loxia.SUCCESS; 
}
$j(document).ready(function(){
	$j(".button.orange.submit,.button.orange.save").click(function(){
		nps.submitForm("propertyForm",{mode:'async',successHandler:function(data){
			if(data.isSuccess == true){
				window.location.href=base+"/shop/shopPropertymanager.htm?shopId=" + $j("input[name='shopId']").val();
			}else{
				return nps.info(nps.i18n("SHOP_ADDPROPERTY_UPDATE_FAIL"));
			}
		}});
	});
	


	//颜色属性必须配图
	$j("select[name='isColorProp']").change(function(){
		$j("select[name='hasThumb']").val($j("select[name='isColorProp']").val());
	});
	
	$j("select[name='hasThumb']").change(function(){
		$j("select[name='isColorProp']").val($j("select[name='hasThumb']").val());
	});
	
	 $j(".button.goback").click(function(){
         history.go(-1);
     });  
	
});