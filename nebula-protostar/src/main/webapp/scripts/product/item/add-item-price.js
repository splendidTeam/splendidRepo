function sortNumber(a, b) {
	return a - b;
}

//销售价
function selectSalePrices(){
	var selectedVal = $j("#salePrice").find("option:selected").text();
	//alert(selectedVal);
	$j("#salePrice").empty();
	var priceArray = [];
	$j("#extensionTable").find("input[id=salePrices]").each(function(i,n){
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
	//alert(selectedVal);
	$j("#listPrice").empty();
	var priceArray = [];
	$j("#extensionTable").find("input[id=listPrices]").each(function(i,n){
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