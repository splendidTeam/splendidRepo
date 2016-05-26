$j.extend(loxia.regional['zh-CN'],{
	"ITEM_UPDATE_CODE_ENBLE":"商品编码不可用,为您恢复到原始的店铺编码"
});


var validateItemCodeUrl = base + '/item/validateItemCode.json';

$j(document).ready(function(){
	//************************************************附加分类
    $j("#category").click(function() {
    	if(!categoryTreeInited) {
    		initCategeoryTree();
    		categoryTreeInited = true;
    	}
		var cityObj = $j(this);
		var cityOffsetleft = $j(this).offset().left;
		var cityOffsettop = $j(this).offset().top;
		$j("#menuContent").css({left:cityOffsetleft, top:cityOffsettop+ cityObj.outerHeight()}).stop(true,true).slideDown("fast");

		$j("body").bind("mousedown", onBodyDown);
	});
    
    
    //检查商品编码是否具有唯一性
	$j("#code").bind("blur",function(){
		 var code = $j("#code").val();
		 if(code.trim()=="")return;
		// 正则验证
		 if(pdValidCode != null && pdValidCode.length > 0){
			 	var re =new RegExp(pdValidCode);
				if(!re.test(code)){
					$j("#code").val(oldval);
	 				$j("#loxiaTip-r").show();
					$j(".codetip").html(nps.i18n("ITEM_CODE_VALID_FAIL",[code]));
					$j("#code").addClass("ui-loxia-error");
	 				 return;
				}else{
					$j("#loxiaTip-r").show();
	 				$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
	 				setTimeout(function(){ 
	 					$j("#loxiaTip-r").hide();
	 				},2000);
				}
			}
		 if(oldval==$j("#code").val()){
			 return ;
		 }else{
 			var json={"code":code};
 		  	var _d = loxia.syncXhr(validateItemCodeUrl, json,{type: "GET"});
 			if(_d.isSuccess == false){
 				 $j("#code").val(oldval);
 				 $j("#loxiaTip-r").show();
 				 $j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ENBLE"));
 				 
 				 
 				 setTimeout(function(){ 
 					$j("#loxiaTip-r").hide();
 				 },4000); 
 				 return;
 			}else if(_d.isSuccess == true){
 				 $j("#loxiaTip-r").show();
 				 $j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
 			     setTimeout(function(){ 
 					$j("#loxiaTip-r").hide();
 				 },2000); 
 				 return;
 			}
 		}
	 });
	$j("#code").bind("focus",function(){
	   $j("#loxiaTip-r").hide();
	 });
});