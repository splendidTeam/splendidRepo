function sortNumber(a,b){
	return a - b;
}

//销售价
function selectSalePrices(){
	var selectedVal = $j("#salePrice").find("option:selected").text();
	$j("#salePrice").empty();
	var priceArray = [];
	$j(".extensionTable").find("input[id=salePrices]").each(function(i,n){
		priceArray[i] = n.value;
	});
	var uniqueArray = unique(priceArray);
	$j("#salePrice").prepend("<option value='0' selected>请选择</option>");
	$j(uniqueArray).each(function(i,n){
		var isSelected ="";
		if(n!="请选择" && parseInt(n)==parseInt(selectedVal)){
			isSelected = "selected";
		}
		$j("#salePrice").append("<option value='"+n+"' "+isSelected+">"+n+"</option>");
	});
}

//吊牌价
function selectListPrices(){
	var selectedVal = $j("#listPrice").find("option:selected").text();
	$j("#listPrice").empty();
	var priceArray = [];
	$j(".extensionTable").find("input[id=listPrices]").each(function(i,n){
		priceArray[i] = n.value;
	});
	var uniqueArray = unique(priceArray);
	$j("#listPrice").prepend("<option value='0' selected>请选择</option>");
	$j(uniqueArray).each(function(i,n){
		var isSelected ="";
		if(n!="请选择" && parseInt(n)==parseInt(selectedVal)){
			isSelected = "selected";
		}
		$j("#listPrice").append("<option value='"+n+"' "+isSelected+">"+n+"</option>");
	});
}

//去重
function unique(data){
	data = data || [];
        var a = {};
	for (var i=0; i<data.length; i++) {
		var v = data[i];
		if (typeof(a[v]) == 'undefined'){
			a[v] = 1;
		}
	};
	data.length=0;
	for (var i in a){
		data[data.length] = i;
	}
	return data;
}

$j(document).ready(function(){
	// 添加商品价格表单验证方法
	var priceValidator = new FormValidator('', 30, function(){
		
		// 验证商品的吊牌价、销售价设置是否与sku的吊牌价、销售价一致
		// 由于获取sku价格的必要条件是销售属性extentionCode验证通过，
		// 所以价格的表单验证放到销售属性的验证之后进行
		//价格校验
		var salePrice = $j("#salePrice").val();
		var salePriceArray = new Array();
		var salePriceIndex = 0;
//		var originalSalePriceArray = new Array();
		
		$j(".extensionTable").find(".dynamicInputNameSalePrices").each(function(i,n){
			var salePrice = parseFloat(n.value);
//			originalSalePriceArray[i] = salePrice;
			if(!isNaN(salePrice)){
				salePriceArray[salePriceIndex++] = salePrice;
			} else {
				return nps.i18n("PLEASE_INPUT_SALEPRICES");
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

			$j(".extensionTable").find(".dynamicInputNameListPrices").each(function(i,n){
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
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(priceValidator);
});