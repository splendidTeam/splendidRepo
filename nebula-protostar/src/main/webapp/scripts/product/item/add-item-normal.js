var editors =[];
//表单验证
function itemFormValidate(form){
	
	if(!clickFlag){
		return nps.i18n("PLEASE_SET_CODE");
	}
	if(i18nOnOff){
		for ( var i = 0; i < editors.length; i++) {
			var editor = editors[i];
			var content=editor.getData();
			$j("textarea[name='itemCommand.description.values["+i+"]']").val(content);
		}
	}else{
		for ( var i = 0; i < editors.length; i++) {
			var editor = editors[i];
			var content=editor.getData();
			$j("textarea[name='itemCommand.description.value']").val(content);
		}
	}
	var code = $j("#code").val();
  // 正则验证
  if(pdValidCode != null && pdValidCode.length > 0){
  	var re =new RegExp(pdValidCode);
		if(!re.test(code)){
				$j("#code").val("");
				$j("#loxiaTip-r").show();
				$j(".codetip").html(nps.i18n("ITEM_CODE_VALID_FAIL",[code]));
				$j("#code").addClass("ui-loxia-error");
				return nps.i18n("ITEM_CODE_VALID_FAIL",[code]);
			}else{
				$j("#loxiaTip-r").show();
				$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
				setTimeout(function(){ 
					$j("#loxiaTip-r").hide();
				},2000);
			}
		}
  	//判断复选框必选
	var att ="";
	$j.each(mustCheckArray,function(j,val){
		var a=0;
		$j("#notSalepropertySpace").find("[mustCheck='"+mustCheckArray[j]+"']").each(function(i,n){
		    if(this.checked){
		    	a+=1;
		    }
	    });
		if(a==0){
			att+="【"+mustCheckArray[j]+"】,";
		}
	});
	if(att!=""){
	     nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),att.substring(0,att.length-1)+nps.i18n("MUST_SELECT"));
	     return;
	}
	
	//如果选了分类，则必须设置默认分类
	var cateLen =$j("input[name='categoriesIds']").length;
	if(cateLen >0){
		var defCategroyId =$j("#defCategroyId").val();
		if(defCategroyId ==null ||defCategroyId ==''){
			return nps.i18n("PLEASE_SET_DEF_CATEGORY");
		}
	}
	
	var salePrice = $j("#salePrice").val();
	
	   var salePriceArray = new Array();
	   var salePriceIndex = 0;
	   var originalSalePriceArray = new Array();
	   $j("#extensionTable").find(".dynamicInputNameSalePrices").each(function(i,n){
		   var salePrice = parseFloat(n.value);
		   originalSalePriceArray[i] = salePrice;
		   if(!isNaN(salePrice)){
			   salePriceArray[salePriceIndex++] = salePrice;
		   }
		});
	   salePriceArray = salePriceArray.sort(sortNumber);

	   if(salePriceArray.length>0){
		   if(!(salePrice>=salePriceArray[0]&&salePrice<=salePriceArray[salePriceArray.length-1])){
			   
			   return nps.i18n("SALEPRICE_OUT_OF_RANGE");
		   }
	   }
	   
	   
	   var listPrice = $j("#listPrice").val();
	   if(listPrice!=null&&listPrice!=""){
		   var listPriceArray = new Array();
		   var listPriceIndex = 0;
		  
		   $j("#extensionTable").find(".dynamicInputNameListPrices").each(function(i,n){
			   var listPrice =  parseFloat(n.value);
			   if(!isNaN(listPrice)){
				   listPriceArray[listPriceIndex++] = listPrice;   
			   }
			});
		   listPriceArray = listPriceArray.sort(sortNumber);
	   
		   if(listPriceArray.length>0){
			   if(!(listPrice>=listPriceArray[0]&&listPrice<=listPriceArray[listPriceArray.length-1])){
				   
				   return nps.i18n("LISTPRICE_OUT_OF_RANGE");
			   }
		   }
	   }
	   
	   //验证 商家编码是否相同
	   var skuCodesArray = new Array();
	   
	   // 验证是否至少填写了一个sku编码 PLEASE_INPUT_ONE_SKU_CODE
	   var atLeastOneCode = false;
	   
	   $j("#extensionTable").find(".dynamicInputNameSkuCode").each(function(i,n){
		   skuCodesArray[i] = $j(this).val();
		});
	   
	   var validateArrayStr="";
		   for(var i=0;i<skuCodesArray.length;i++){
			   var curCode = skuCodesArray[i];
			   
			   if(curCode!=null&&curCode!=""){
				   atLeastOneCode = true;
				   var curSp =  originalSalePriceArray[i];
					  if(isNaN(curSp)){
						  return nps.i18n("PLEASE_INPUT_SALEPRICES");
					  }
			   }
			   for(var j=0;j<skuCodesArray.length;j++){
				   if(i!=j&&curCode!=""&&curCode==skuCodesArray[j]){
					   return nps.i18n("MERCHANT_CODING_EQUAL");
				   }
			   }
			   
		   }
		   
	   
	   if(!atLeastOneCode){
		   return nps.i18n("PLEASE_INPUT_ONE_SKU_CODE");
	   }
	   
	   if(spChangedFlag){
		   return nps.i18n("SALES_PROPERTY_CHANGED");
	   }
	   
	   if(skuCodesArray.length>0){
		 //向服务器提交验证 TODO
		   
		   for(var i=0;i<skuCodesArray.length;i++){
			   validateArrayStr+=skuCodesArray[i];
			   if(i!=skuCodesArray.length-1){
				   validateArrayStr+=",";
			   }
		   }
		   var skuCodeArrayJsonStr = JSON.stringify(skuCodesArray);
		   var validateJson = {"skuCodes":validateArrayStr};
		   console.log(validateJson);
		  var data = nps.syncXhr(validateSkuCodesUrl,validateJson);
		  
		  if (data.isSuccess==false) {
			  return nps.i18n("SKU_CODE_REPEAT")+data.description;
		 }

	   }
  
  return loxia.SUCCESS;  
}