$j(document).ready(function(){
	 //编码设置
	$j(".func-button.extension").on("click",function(){
		spChangedFlag = false;
		var allSaleInfo = $j(".saleInfo");
		var flag = false;
		if(allSaleInfo.length>=2){
			var customerPropSize =$j(".saleInfo").eq(0).find(".customerSelect").length;
			for(var idx =0;idx < customerPropSize;idx++){
				var next = 0;
				allSaleInfo.each(function(i,dom){
					var me = $j(this);	
					var val = me.find(".customerSelect").eq(idx).val();
					if(i>0){
						if(val==""){
							flag = true;
						}
					}
					if(typeof(val)!="undefined" && val != null){
						var len = val.split("||").length;
						me.find(".propertyValueInputs").eq(idx).val(val);
						if(val!="||" && i>0){
							if(len != next){
								flag = true;
							}
						}else{
							next = len;
						}
					}
					
				});
				if(flag) break;
			}
		}
		var saleInfo = $j(this).parents(".saleInfo");
		if(flag){
			saleInfo.find(".extensionTable").html("多语言属性设置数量不一致");
			return;
		}
		showSkuTable(false,saleInfo);
	});
	
	$j(".spCkb").each(function(){
		var curCheckBox = $j(this);
		
		curCheckBox.change(function(){
			spChangedFlag = true;
			clickFlag = false;
		});
		
	});
	
	$j(".spTa").each(function(){
		var ta = $j(this);
		
		ta.bind("change",function(){
			spChangedFlag = true;
			clickFlag = false;
		});
	});
	
	$j(".spCkb").click(function(){
		if($j(this).attr("propertyid")=='2'){
			if($j(this).is(':checked')){
				selectcheck(itemcolorrefcheckURL);
			}else{
				$j("input[name=filtratecolor]").attr("checked",false);
				selectcheck(itemcolorrefcheckURL);
			}
		}
	});
});