var propertyArray  = new Array();
var propertyNameArray  = new Array();
var mustCheckArray  = new Array();
var itemProperties = 0;
//是否存在颜色属性
var isExistColorProp = false;
var dynamicPropertyCommandListJsonStr="";
//用于提交 表单时候，包含 有序的propertyId 用
var propertyIdArray = new Array();
var propertyIdArrayIndex = 0;

var clickFlag = false;
var spChangedFlag = false;

var validateSkuCodesUrl = base +'/item/validateSkuCode.json';

//属性数量,全局范围，分组切换使用
var num=0;

//选取其他
function doOther(selOp,proId){
	var len = selOp.length-1;
	var sel =selOp.selectedIndex;
	if(len==sel){
		$j("#pv_"+proId).attr("type","text"); 
	}else{
		$j("#pv_"+proId).attr("type","hidden");  
		$j("#pv_"+proId).val("");  
	}
}

//绘制编码table
list="";
function itTable(array,i,array1,str,properties,propertiesName) {	
	if (array[i+1]!=null) {
		for (var k = 0 ; k < array1[0].length; k++) {
			str += "<td style='width:150px'>"+ array1[0][k] + "</td>";
			//json
			properties+="{'id':'"+array1[1][k]+"','pId':'"+array1[3][k]+"'},";
			propertiesName+="{'id':'"+array1[1][k]+"','pId':'"+array1[3][k]+"','value':'"+array1[0][k]+"','pName':'"+array1[2][k]+"'},";
			if (k > 0) {
				str = str.replace("<td style='width:150px'>"+array1[0][k-1]+"</td>","");
				//json
				properties = properties.replace("{'id':'"+array1[1][k-1]+"','pId':'"+array1[3][k-1]+"'},","");
				
			}
			itTable(array,i+1,array[i+1],str,properties,propertiesName);
		}
	}else {
		for (var j = 0 ; j < array1[0].length; j++) {
			var t1 = document.getElementById("extensionTable");
			var tr1 = t1.insertRow(-1);
			tr1.innerHTML = str+"<td style='width:150px'>"+array1[0][j]+
			"</td><td style='width:150px'><input type='text'  loxiaType='input'  name='codes' value=''/></td>" +
			"<td style='width:150px'><input type='text' loxiaType='number' decimal='2' id='salePrices' name='salePrices' value=''/></td>" +
			"<td style='width:150px'><input type='text'  loxiaType='number' decimal='2' id='listPrices' name='listPrices' value=''/></td>";
			//json
			var p ="'properties':'["+properties+"{'id':'"+array1[1][j]+"','pId':'"+array1[3][j]+"'}]'";
			var pn ="'propertiesName':'["+propertiesName+"{'id':'"+array1[1][j]+"','pId':'"+array1[3][j]+"','value':'"+array1[0][j]+"','pName':'"+array1[2][j]+"'}]'";
			var list1 ="{'itemId':'',"+p+","+pn+",'outid':''},";
			list+=list1;
			
		}
	}
}

//编码
function produceExtension(){
	$j("#exten").css("display","block");
	
	if(propertyArray.length>0){ //如果有 销售属性
		//用于保存所选择的的属性可选值
		var propertyValueArray=new Array();  
		//用于保存属性
		var propertyNamesArray = new Array();
		$j.each(propertyArray,function(i,val){
			propertyValueArray[i]=new Array(); 
			propertyValueArray[i][0]=new Array(); 
			propertyValueArray[i][1]=new Array(); 
			propertyValueArray[i][2]=new Array(); 
			propertyValueArray[i][3]=new Array(); 
			$j("input[propertyId='"+propertyArray[i]+"']").each(function(index){
				if($j(this).attr("checked")){
					propertyNamesArray[i]=propertyNameArray[i];
					propertyValueArray[i][0].push($j(this).attr("pvValue"));
					propertyValueArray[i][1].push($j(this).attr("pvId"));
					propertyValueArray[i][2].push($j(this).attr("propertyName"));
					propertyValueArray[i][3].push($j(this).attr("propertyId"));
				}
		
			});
			$j("textarea[propertyid='"+propertyArray[i]+"'][editingtype='5']").each(function(){
				if($j(this).val()!=""){
					var content = $j(this).val();
					var valueArray = content.split("||");
					for(var k=0;k<valueArray.length;k++){
						propertyNamesArray[i]=propertyNameArray[i];
						propertyValueArray[i][0].push(valueArray[k]);
						propertyValueArray[i][3].push($j(this).attr("propertyId"));
					}
					
				}
				
			});
		});
	    //去除空值
		for(var i=0;i<propertyValueArray.length;i++){
			if (typeof propertyValueArray[i] != "undefined")
			{
				if(propertyValueArray[i][0].length==0){
					propertyValueArray.splice(i, 1);
					i=i-1;
				}
			}
			
		}
		 //去除空值
		for(var j=0;j<propertyNamesArray.length;j++){
			if (typeof propertyNamesArray[j] == "undefined")
			{
					propertyNamesArray.splice(j, 1);
					j=j-1;
			}
			
		}
		
		var html = "";
		if(propertyValueArray.length !=propertyArray.length){
			html =  nps.i18n("CODING_TIPS");
			$j("#extensionTable").html(html);
			$j("#jsonSku").val("");
		}else{
			htmlA="";
			for(var j=0;j<propertyNamesArray.length;j++){
				htmlA+="<td style='width:150px'>"+propertyNamesArray[j]+"</td>";
			}
			html = "<tr>"+htmlA +
					"<td style='width:150px'>"+nps.i18n("MERCHANT_CODING")+"</td>" +
					"<td style='width:150px'>"+nps.i18n("MERCHANT_SALEPRICE")+"</td>" +
					"<td style='width:150px'>"+nps.i18n("MERCHANT_LISTPRICE")+"</td>" +
					"</tr>";
			$j("#extensionTable").html(html);
			list="";
			itTable(propertyValueArray,0,propertyValueArray[0],"","","");
			list="["+list.substring(0, list.length-1)+"]";
			$j("#jsonSku").val(list);
			
		}
	}else{// 如果没有销售属性
		html = "<tr>" +
		"<td style='width:150px'>"+nps.i18n("MERCHANT_CODING")+"</td>" +
		"<td style='width:150px'>"+nps.i18n("MERCHANT_SALEPRICE")+"</td>" +
		"<td style='width:150px'>"+nps.i18n("MERCHANT_LISTPRICE")+"</td>" +
		"</tr>";
		 html += "<td style='width:150px'><input type='text' mandatory='true' loxiaType='input'  name='codes' value=''/></td><td style='width:150px'><input type='text' mandatory='true' loxiaType='number' decimal='2' id='salePrices' name='salePrices' value=''/></td><td style='width:150px'><input type='text'  loxiaType='number' decimal='2' id='listPrices' name='listPrices' value=''/></td>";
		$j("#extensionTable").html(html);
		
		list=   "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}
	
	$j("#extensionTable").find("[loxiaType]").each(function(i,n){
		loxia.initLoxiaWidget(this);
	});
	return loxia.SUCCESS; 
}

function showSkuTable(saleInfo){
	clickFlag = true;
//	showSth();
	saleInfo.find("#exten").css("display","block");
	saleInfo.find("#extensionTable").html("");
	//拿到  销售属性,  根据销售属性 确定 列
	var salesProperty = new Array();
	
	var salesPropertyIndex = 0;
	for(var i=0;i<dynamicPropertyCommandListJsonStr.length;i++){
		var dynamicPropertyCommand = dynamicPropertyCommandListJsonStr[i];
		console.log(dynamicPropertyCommand);
		//编辑属性为4 或者5 的是 销售属性
		if(dynamicPropertyCommand!=null&&(dynamicPropertyCommand.property.editingType == 4 ||dynamicPropertyCommand.property.editingType == 5)&&dynamicPropertyCommand.property.isSaleProp==true){
			salesProperty[salesPropertyIndex] = dynamicPropertyCommand;
			salesPropertyIndex++;
		}
	}
	
	var propertyIdArray = new Array();
	var propertyValueArray = new Array();
	var propertyValueInputArray = new Array();
	
	var propertyValueArrayIndex = 0;
	var propertyNameArrayIndex = 0;
	
	var sameNameFlag = false;
	for(var i = 0;i<salesProperty.length;i++){
		var saleProperty = salesProperty[i];
		var propertyId = saleProperty.property.id;
		
//		propertyValueArray[propertyValueArrayIndex]=new Array();//第一层
		
		var curPropertyName = null;
		
		var selectedArray = new Array();
		
		var pInputArray = new Array();
		
		var index = 0;
		
		$j("input[propertyId='"+propertyId+"']").each(function(){
			
			if($j(this).attr("checked")){
				var pvValue = $j(this).attr("pvValue");
				var pvId = $j(this).attr("pvId");
				var propertyName = $j(this).attr("propertyName");
				var propertyId = $j(this).attr("propertyId");
				
				var pArray = new Array();//最里边
				pArray[0]=$j(this).attr("pvValue");
				pArray[1]=$j(this).attr("pvId");
				pArray[2]=$j(this).attr("propertyName");
				pArray[3]=$j(this).attr("propertyId");
				
				pInputArray[index] = pArray[1];
				
				
				selectedArray[index]=pArray;
				index++;
				curPropertyName = propertyName;
			}
			
		});
		
		if(selectedArray.length>0){
			propertyIdArray[propertyValueArrayIndex] = propertyId;
			propertyValueArray[propertyValueArrayIndex] = selectedArray;
			
			propertyValueInputArray[propertyValueArrayIndex] = pInputArray;
			propertyValueArrayIndex++;
			
			$j("input[pid='"+propertyId+"']").each(function(index){
				var selectdStr="";
				for(var i=0;i<pInputArray.length;i++){
					selectdStr+=pInputArray[i];
					selectdStr+=",";
				}
				selectdStr=selectdStr.substring(0, selectdStr.length-1);
				$j(this).val(selectdStr);
				
			});
		}
		
		saleInfo.find("textarea[propertyid='"+propertyId+"'][editingtype='5']").each(function(){
			if($j(this).val()!=""){
				var index = 0;
				var content = $j(this).val();
				var valueArray = content.split("||");
				var inputValueArray = new Array();
				for(var k=0;k<valueArray.length;k++){
					var pArray =new Array();
					
					pArray[0]=valueArray[k];
					pArray[2]=saleProperty.property.name;
					pArray[3]=$j(this).attr("propertyId");
					inputValueArray[index]=pArray;
					
					index++;
					
					curPropertyName = pArray[2];
				}
				
				if(inputValueArray.length>0){
					propertyIdArray[propertyValueArrayIndex] = propertyId;
					propertyValueArray[propertyValueArrayIndex] = inputValueArray;
					propertyValueInputArray[propertyValueArrayIndex] = valueArray;
					propertyValueArrayIndex++;
					
					saleInfo.find("input[pid='"+propertyId+"']").each(function(index){
						var selectdStr="";
						for(var i=0;i<pInputArray.length;i++){
							selectdStr+=pInputArray[i];
							selectdStr+=",";
						}
						selectdStr.substring(0, selectdStr.length-1);
						
						for(var i=0;i<valueArray.length;i++){
							for(var j=0;j<valueArray.length;j++){
								if(i!=j){
									if(valueArray[i]==valueArray[j]){
										sameNameFlag = true;
										break;
									}
								}
							}
						}
						
						$j(this).val(content);
						
					});
					
				}
				
			}
			
		});
		
		//其他语言(仅editType=5)的hidden propertyValueInputs input设置
		if(i18nOnOff){
			$j(".saleInfo").each(function(i,dom){
				var me = $j(this);
				var lang = me.attr("lang");
				if(lang != defaultlang){
					me.find("textarea[propertyId='"+propertyId+"'][editingtype='5']").each(function(){
						if($j(this).val()!=""){
							var index = 0;
							var content = $j(this).val();
							var valueArray = content.split("||");
							var inputValueArray = new Array();
							for(var k=0;k<valueArray.length;k++){
								var pArray =new Array();
								
								pArray[0]=valueArray[k];
								pArray[2]=saleProperty.property.name;
								pArray[3]=$j(this).attr("propertyId");
								inputValueArray[index]=pArray;
								
								index++;
								
								//curPropertyName = pArray[2];
							}
							
							if(inputValueArray.length>0){
								/*propertyIdArray[propertyValueArrayIndex] = propertyId;
								propertyValueArray[propertyValueArrayIndex] = inputValueArray;
								propertyValueInputArray[propertyValueArrayIndex] = valueArray;
								propertyValueArrayIndex++;*/
								
								me.find("input[pid='"+propertyId+"']").each(function(index){
									var selectdStr="";
									for(var i=0;i<pInputArray.length;i++){
										selectdStr+=pInputArray[i];
										selectdStr+=",";
									}
									selectdStr.substring(0, selectdStr.length-1);
									
									for(var i=0;i<valueArray.length;i++){
										for(var j=0;j<valueArray.length;j++){
											if(i!=j){
												if(valueArray[i]==valueArray[j]){
													
													sameNameFlag = true;
													break;
												}
											}
										}
									}
									
									$j(this).val(content);
									
								});
								
							}
							
						}
						
					});
				}
			});
		}
		
		
	}
	
	if(sameNameFlag){
		clickFlag = false;
		html=nps.i18n("CUSTOM_PROPERTY_SAME");
		saleInfo.find("#extensionTable").html(html);
		
		return;
	}
	if(propertyValueInputArray.length!=propertyNameArray.length){//如果用户未把每个属性都填上值，则提示用户
		html =  nps.i18n("CODING_TIPS");
		saleInfo.find("#extensionTable").html(html);
	}else{// 画表格
		drawTableContent(propertyValueArray,propertyNameArray,propertyValueInputArray,saleInfo);
	}
	
	saleInfo.find("#extensionTable").find("[loxiaType]").each(function(i,n){
		loxia.initLoxiaWidget(this);
	});
	
}

function drawTableContent(propertyValueArray,propertyNameArray,propertyInputValueArray,saleInfo){

	var skuInfoList = new Array();
	var skuInfoListIndex = 0;
	var tableHeader="<tr>";
	for(var j=0;j<propertyNameArray.length;j++){//动态生成 销售属性的列
		tableHeader+="<td style='width:150px'>"+propertyNameArray[j]+"</td>";
	}
	
	tableHeader+= (
			"<td style='width:150px'>"+nps.i18n("MERCHANT_CODING")+"</td>" +
			"<td style='width:150px'>"+nps.i18n("MERCHANT_SALEPRICE")+"</td>" +
			"<td style='width:150px'>"+nps.i18n("MERCHANT_LISTPRICE")+"</td>" +
			"</tr>");
	
	var tableContent="";
	if(propertyValueArray.length>0){
		
		if(propertyValueArray.length==1){// 销售属性只有1个
			for(var i = 0;i<propertyValueArray[0].length;i++){ 
				var dynamicStr = "<td style='width:150px'>"+propertyValueArray[0][i][0]+"</td>";
				var codesName = getDynamicInputName("code",propertyValueArray[0][i],null);
				var salePriceName = getDynamicInputName("salePrice",propertyValueArray[0][i],null);
				var listPriceName = getDynamicInputName("listPrice",propertyValueArray[0][i],null);
				
				var proHtml="<td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode'  name='codesNameToReplace' loxiaType='input'  value='CODE_VALUE'/></td>" +
				"<td style='width:150px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices'  name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
				"<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

				proHtml = proHtml.replace('codesNameToReplace', codesName);
				proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
				proHtml = proHtml.replace('listPriceToReplace', listPriceName);
				
				var salePrice = $j("#salePrice").val();
				var listPrice = $j("#listPrice").val();
				proHtml = proHtml.replace('CODE_VALUE', "");
				proHtml = proHtml.replace('salePrices_value', salePrice);
				proHtml = proHtml.replace('listPrices_value', listPrice);

				tableContent += ("<tr>"+dynamicStr+proHtml+"</tr>");
			}
			
			
		}else{
			//多个销售属性的展示
			tableContent = buildTable(null, propertyValueArray, 0);
			
		}
		
		
		$j("#jsonSku").val(JSON.stringify(skuInfoList));
	}else{
		
		var proHtml="<td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode' mandatory='true' name='skuCode' loxiaType='input'  value='CODE_VALUE'/></td>" +
		"<td style='width:150px'><input type='text' id='salePrices' class = 'dynamicInputNameSalePrices' mandatory='true' name='skuSalePrice' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
		"<td style='width:150px'><input type='text' id='listPrices' class = 'dynamicInputNameListPrices' name='skuListPrice' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

		proHtml = proHtml.replace('CODE_VALUE', "");
		proHtml = proHtml.replace('salePrices_value', "");
		proHtml = proHtml.replace('listPrices_value', "");
			
		tableContent += ("<tr>"+proHtml+"</tr>");
		list=   "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}
	
	
	var html =  tableHeader+tableContent;
	saleInfo.find("#extensionTable").html(html);
	
}

//--------------------------------多个销售属性循环start-------------------------------
function buildTable(table, data, dataRowIndex){
	var result = new Array();
	if (table == null){
		table = result;
	}
	
	if (dataRowIndex >= data.length){
		var htmlStr = "";

		for(var i = 0;i<table.length;i++){
			var htmlLine = "";
			var arrays = new Array();//所有属性的数组
			for(var j=0;j<table[i].length;j++){
				htmlLine +="<td style='width:150px'>"+table[i][j][0]+"</td> ";
				arrays.push(table[i][j]);
			}
			
			var codesName = getMoreDynamicInputName("code",arrays);
			var salePriceName = getMoreDynamicInputName("salePrice",arrays);
			var listPriceName = getMoreDynamicInputName("listPrice",arrays);
			var tmpArray = clone(arrays);
			
			var proHtml="<td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode' name='codesNameToReplace' loxiaType='input'  value='CODE_VALUE'/></td>" +
			"<td style='width:150px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices'  name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
			"<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

			proHtml = proHtml.replace('codesNameToReplace', codesName);
			proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
			proHtml = proHtml.replace('listPriceToReplace', listPriceName);
			
			var salePrice = $j("#salePrice").val();
			var listPrice = $j("#listPrice").val();
			var code = "";
			
			proHtml = proHtml.replace('CODE_VALUE', code);
			proHtml = proHtml.replace('salePrices_value', salePrice);
			proHtml = proHtml.replace('listPrices_value', listPrice);
			
			if(htmlLine.length!=0){
				htmlStr += "<tr>"+htmlLine+proHtml+"</tr>";
			}
			
		}
		return htmlStr;
	}
	// 组织第i个"销售属性"
	var dataRow = data[dataRowIndex];
	var dynamicStr ="";
	var proHtml="";
	// 组织第1个"销售属性"时
	if (table.length == 0){
		for (var i = 0; i < dataRow.length; i++) {
			var targetRow = new Array();
			
			targetRow.push(dataRow[i]);
			result.push(targetRow);
		};
	}else{
		//循环遍历某一个销售属性dataRow的全部值
		for ( var i = 0; i < dataRow.length; i++) {
			for ( var j = 0; j < table.length; j++) {
				var subResult = clone(table[j]);
				subResult.push(dataRow[i]);
				result.push(subResult);
			};
		}
	}
	// 递归组织下一行数据
	return buildTable(result, data, ++dataRowIndex);
}

//获取动态输入框中的值    多个销售属性
function getMoreDynamicInputName(key,array){
	var fullKey = "";
	for (var i = 0; i < array.length; i++) {
		if(array[i]!=null){
			fullKey += getKeyFromArray(array[i]);
			fullKey+="_";
		}
	}
	fullKey += key;
	return fullKey;
}

//js克隆
function clone(obj){  
    var o;  
    switch(typeof obj){  
    case 'undefined': break;  
    case 'string'   : o = obj + '';break;  
    case 'number'   : o = obj - 0;break;  
    case 'boolean'  : o = obj;break;  
    case 'object'   :  
        if(obj === null){  
            o = null;  
        }else{  
            if(obj instanceof Array){  
                o = [];  
                for(var i = 0, len = obj.length; i < len; i++){  
                    o.push(clone(obj[i]));  
                }  
            }else{  
                o = {};  
                for(var k in obj){  
                    o[k] = clone(obj[k]);  
                }  
            }  
        }  
        break;  
    default:          
        o = obj;break;  
    }  
    return o;     
}

function getDynamicInputName(key,array1,array2){
	var fullKey = "";
	if(array1!=null){
		fullKey += getKeyFromArray(array1);
		fullKey+="_";
	}
	
	if(array2!=null){
		
		fullKey+=getKeyFromArray(array2);
		fullKey+="_";
	}
	
	fullKey+=key;
	
	return fullKey;
}

function getKeyFromArray(array){
	//[0] 是pvname  [1] 是 pvid   对于有pvid的 传pvid 对于没有pvId 的 传pvName
	var arraykey="";
	if(array[1]!=null||array[1]!=undefined){//多选
		arraykey = array[1];
	}else{//自定义多选 有pvName 而没pvId
		arraykey = array[0];
	}
	return arraykey;
}
function getSkuStr(pArray){
	var sku = new Object();
	sku.itemId="";
	sku.properties="[]";
	sku.propertiesName = new Array();
	sku.outid ="";
	for(var i=0;i<pArray.length;i++){
		var propertyId = pArray[i][3]
		var pName = pArray[i][2];
		var propertyValueId = pArray[i][1];
		var propertyValue = pArray[i][0];
		
		var propertyName = new Object();
		
		propertyName.id =propertyValueId;
		propertyName.pId = propertyId;
		propertyName.value = propertyValue;
		propertyName.pName = pName;
		
		sku.propertiesName[i]=propertyName;
	}
	sku.propertiesName = JSON.stringify(sku.propertiesName);
	return sku;
}
function getSkuInfoByProperyValueArray(pArray){
	var itemPropertyIdArray = new Array();
	var index = 0;
	for(var i=0;i<pArray.length;i++){
		var propertyId = pArray[i][3];
		var propertyValueId = pArray[i][1];
		var propertyValue = pArray[i][0];
		var itemProperty = findItemPropertyByProvertyIdAndValue(propertyId,propertyValueId,propertyValue);
		
		if(itemProperty!=null){
			var itemPropertyId = itemProperty.id;
			itemPropertyIdArray[index] = itemPropertyId;
			index++;
		}
	}
	itemPropertyIdArray=itemPropertyIdArray.sort();
	
	for(var k=0;k<skuList.length;k++){
		var sku = skuList[k];
		var skuPropertiesArray = JSON.parse(sku.properties);
		skuPropertiesArray = skuPropertiesArray.sort();
		
		if(skuPropertiesArray.toString()==itemPropertyIdArray.toString()){
			return sku;
		}
	}
	
	return null;
}

function findItemPropertyByProvertyIdAndValue(propertyId,propertyValueId,propertyValue){
	
	for(var i = 0 ;i<itemPropertiesStr.length;i++){
		var itemProperty = itemPropertiesStr[i];
		if(propertyValueId!=undefined&&propertyValueId!=null){//多选
			if(itemProperty.propertyId==propertyId&&propertyValueId==itemProperty.propertyValueId){
				return itemProperty;
			}
			
		}else{// 自定义多选
			if(itemProperty.propertyId==propertyId&&propertyValue==itemProperty.propertyValue){
				return itemProperty;
			}
		}
	}
	return null;
}

//obj 下拉框dom对象,type 属性类型(1:销售属性，2:普通属性) 两种属性生成HTML不同故做区分
function changeProGroup(obj, type) {
	// 获取分组下属性列表请求参数
	var json = {
		proGroupId : $j(obj).val(),
		propertyId : $j(obj).attr('propertyId')
	};
	var backWarnEntity = loxia.syncXhr(base + '/item/findProGroupInfo.json',
			json, {
				type : "GET"
			});
	var html = ""
	//
	if (backWarnEntity.isSuccess) {
		var propertyValueArray = backWarnEntity.description.propertyValueList;
		var property = backWarnEntity.description.property
		var picUrl = "";
		if (propertyValueArray != null && propertyValueArray.length > 0) {

			if (type == 1) {
				$j
						.each(
								propertyValueArray,
								function(j) {
									html += "<div class='priDiv'><span class='children-store'>"
											+ "<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' "
											+ "pvId='"
											+ propertyValueArray[j].id
											+ "' propertyId='"
											+ property.id
											+ "' "
											+ "pvValue='"
											+ propertyValueArray[j].value
											+ "' propertyName='"
											+ property.name
											+ "'"
											+ "value='"
											+ propertyValueArray[j].id
											+ "'>"
											+ picUrl
											+ propertyValueArray[j].value
											+ "</input></span> </div>";

								});
			} else if (type == 2) {
				$j
						.each(
								propertyValueArray,
								function(j, val) {
									html += "<span class='children-store normalCheckBoxSpan'><input type='checkbox' class = 'normalCheckBoxCls' pid="
											+ property.id
											+ " mustCheck='"
											+ property.name
											+ "' name='' "
											+ "value='"
											+ propertyValueArray[j].id
											+ "'>"
											+ propertyValueArray[j].value
											+ "</input></span>";
								});

			}

			$j(obj).next().html(html);
			$j(".normalCheckBoxCls").each(function() {
				var curCheckBox = $j(this);
				curCheckBox.change(function() {
					drawNoSalePropEditing4Type(num);
				});
			});

		}
	}
}

function drawNoSalePropEditing4Type(curSize){
	if(curSize==undefined ||curSize ==null){
		curSize =0;
	}
	var tempNum =curSize;
	//去除所有
	$j(".hidBoxSpan").find("div[class='repNormalCheckBoxCls']").siblings().remove();
	
	$j(".hidBoxSpan").find("div[class='repNormalCheckBoxCls']").each(function(){
		var pid=$j(this).attr("pid");
		var pvid=$j(this).attr("pvid");
		var isCheck =false;
		$j(".normalCheckBoxCls").each(function(){
			if(pid ==$j(this).attr("pid") &&pvid==$j(this).attr("value")){
				if('checked' == $j(this).attr("checked")){
					isCheck =true ;
				}
				return ;
			}
		});
		if(isCheck){
			var parent =$j(this).parent();
			var ipropertiesHtml = "<input type='hidden' value='propertyId_toReplace' name='iProperties.propertyId'/>"+
			"<input type='hidden' value='propertyValueId_toRepalce' name='iProperties.propertyValueId'/>"+
			"<input type='hidden' value='' name='iProperties.id'><input type='hidden' value='' name='iProperties.propertyDisplayValue'><input type='hidden' value='' name='iProperties.createTime'><input type='hidden' value='' name='iProperties.modifyTime'><input type='hidden' value='' name='iProperties.version'><input type='hidden' value='' name='iProperties.itemId'><input type='hidden' value='' name='iProperties.picUrl'>";

			if(i18nOnOff){
			 	   for ( var j = 0; j < i18nLangs.length; j++) {
							var i18nLang = i18nLangs[j];
							ipropertiesHtml = ipropertiesHtml+ "<input type='hidden' name='iProperties.propertyValue.values["+tempNum+"-"+j+"]' value=''/>";
							ipropertiesHtml = ipropertiesHtml+ "<input type='hidden' name='iProperties.propertyValue.langs["+tempNum+"-"+j+"]' value='"+i18nLang.key+"'/>";
			 	   }
			    }else{
			    	ipropertiesHtml = ipropertiesHtml +"<input type='hidden'name='iProperties.propertyValue.value["+tempNum+"]' value=''/>";
			    }
			//}
			ipropertiesHtml=ipropertiesHtml.replace("propertyId_toReplace", pid);
			ipropertiesHtml=ipropertiesHtml.replace("propertyValueId_toRepalce", pvid);
			parent.append(ipropertiesHtml);
			tempNum ++;
		}
	});
}

function initProperties(industryId){
	var json={"industryId":industryId};
	var _data = loxia.syncXhr(base+'/item/findDynamicPropertisJson.json', json,{type: "GET"});
	dynamicPropertyCommandListJsonStr=_data.description;
	dynamicPropertyCommandListJsonStr = JSON.parse(dynamicPropertyCommandListJsonStr);
	
	var backWarnEntity = loxia.syncXhr(base+'/item/findDynamicPropertis.json', json,{type: "GET"});
	if(backWarnEntity.isSuccess){
		//清空
		propertyArray.splice(0,propertyArray.length);
		propertyNameArray.splice(0,propertyNameArray.length);
		jsonArray = backWarnEntity.description;
		var SalepropertySpaceHtmlArr= [];
		var notSalepropertySpaceHtml="";
		var propSelectHtml="<option value=''>请选择</option>";
		
		//处理销售属性
		if(i18nOnOff){
			for ( var j = 0; j < i18nLangs.length; j++) {
				var i18nLang = i18nLangs[j];
				var SalepropertySpaceHtml="";
				$j.each(jsonArray,function(i,val){
				if(jsonArray[i].property.isSaleProp){
					propertyIdArray[propertyIdArrayIndex] = jsonArray[i].property.id;
					propertyIdArrayIndex++;
					var html2 ="";
					if(jsonArray[i].property.editingType == 4 && i18nLang.key == defaultlang){
						jsonArray2 = jsonArray[i].propertyValueList;
						
						//属性值分组列表
						jsonProGroupArray = jsonArray[i].propertyValueGroupList;
						
						var html1="";
						var picUrl="";
						
						
						// 属性分组相关HMTL
						var html3 ="";  
						
						//如果选项值为空 不显示该多选框
						if(jsonArray2!=null&&jsonArray2.length>0){
							//销售属性
							propertyArray.push(jsonArray[i].property.id);
							propertyNameArray.push(jsonArray[i].property.name);
							$j.each(jsonArray2,function(j,val){
								if(jsonArray[i].property.isColorProp && jsonArray[i].property.hasThumb ){
									picUrl ="<img src='"+baseUrl+"/images/1.png'>";
								}
								html1+= "<div class='priDiv'><span class='children-store'>" +
										"<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' " +
										"pvId='"+jsonArray2[j].id+"' propertyId='"+jsonArray[i].property.id+"' " +
										"pvValue='"+jsonArray2[j].value+"' propertyName='"+jsonArray[i].property.name+ "'" +
										"value='"+jsonArray2[j].id+"'>"+picUrl+jsonArray2[j].value+"</input></span> </div>";
								
						     });
							
							//属性值分组处理
							if(jsonProGroupArray != null && jsonProGroupArray.length>0){
								 html3 = '<br><select  onchange="changeProGroup(this,1)" loxiaType="select"  propertyId="'+jsonArray[i].property.id+'">' ;
								 html3 += '<option value="">'+nps.i18n("PLEASE_SELECT_PROPERTY_GROUP")+'</option>';
								 $j.each(jsonProGroupArray, function (i) {
								      html3 += '<option value="'+jsonProGroupArray[i].id + '">'+  jsonProGroupArray[i].name + '</option>';
								 });
								 html3 += "</select>"
							}
							
							html2= "<div class='ui-block-line '><label style=''>"+jsonArray[i].property.name+"</label><div >"+
								"<input type='hidden' value='"+ jsonArray[i].property.id +"'  name = 'propertyIds' />"+
								"<input type='hidden' value=''  name='propertyValueInputIds' pid='"+jsonArray[i].property.id+"' />"+
								html3+
						         "<div class='wl-right-auto width-percent50 mt10'>"+
								          html1+
						        " </div>"+
						        " </div>"+
							    " </div>";
						}
					  }else if(jsonArray[i].property.editingType == 5){
						propertyArray.push(jsonArray[i].property.id);
						var add= true;
						for ( var m = 0; m < propertyNameArray.length; m++) {
						   var str = propertyNameArray[m];
						   if(str==jsonArray[i].property.name){
							   add = false;
						   }
						}
						if(add){
							propertyNameArray.push(jsonArray[i].property.name);
						}
						html2 = '<div class="ui-block-line "><label>'+jsonArray[i].property.name+'</label>';
						html2+= "<input type='hidden' name = 'propertyIds' value='"+ jsonArray[i].property.id +"' />";
						html2+="<input type='hidden' value='' class='propertyValueInputs' name='propertyValueInputs' pid='"+jsonArray[i].property.id+"' />";
						html2+="<input type='hidden' value='"+i18nLang.key+"'  name='propertyValueInputs'/>";
						html2 += '<div class="priDiv">';
						if(jsonArray[i].property.isColorProp){
							html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="true" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
						}else{
							html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="false" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
							
						}
						html2 += '<span style="margin: 17px 0 0 10px; position: absolute;">'+i18nLang.value+'&nbsp;&nbsp;属性名之间请以"||"分隔</span>'
						+'</div></div>';
					}
					SalepropertySpaceHtml= SalepropertySpaceHtml+html2;
					//是否存在颜色属性
					if(jsonArray[i].property.isColorProp){
						isExistColorProp = true;
						var propertyValueListJson = '';
						//editingType为多选
						var str = "";
						if(jsonArray[i].property.editingType == 4){
							propertyValueListJson = jsonArray[i].propertyValueList;
							if(propertyValueListJson != '' && propertyValueListJson.length > 0){
								str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
								str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
								$j.each(propertyValueListJson, function(index, propertyValue){
									str += '<option value="'+propertyValue.id+'#">'+propertyValue.value+'</option>';
								});
								str += '</select>';
							}
						}else if(jsonArray[i].property.editingType == 5){
							str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
							str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
							str += '<option value="">'+nps.i18n('PLEASE_SELECT')+'</option>';
							str += '</select>';
						}
						$j('.colorPropertySelect').html(str);
						$j('#colorPropertyContent').empty();
						$j('#colorPropertyContent').next('.ui-block-line').show();
					}
					 }
					
				});
				
				var SalepropertySpaceHtmlObj= {};
				SalepropertySpaceHtmlObj.html = SalepropertySpaceHtml;
				SalepropertySpaceHtmlObj.lang = i18nLang.key;
				SalepropertySpaceHtmlArr.push(SalepropertySpaceHtmlObj);
			}
		}else{  //  i18nOn
			var SalepropertySpaceHtml="";
			$j.each(jsonArray,function(i,val){
			   if(jsonArray[i].property.isSaleProp){
				propertyIdArray[propertyIdArrayIndex] = jsonArray[i].property.id;
				propertyIdArrayIndex++;
				var html2 ="";
				
				// 属性分组相关HMTL
				var html3 ="";  
				if(jsonArray[i].property.editingType == 4){
					
					//属性值列表
					jsonArray2 = jsonArray[i].propertyValueList;
					
					//属性值分组列表
					jsonProGroupArray = jsonArray[i].propertyValueGroupList;
					var html1="";
					var picUrl="";
					//如果选项值为空 不显示该多选框
					if(jsonArray2!=null&&jsonArray2.length>0){
						//销售属性
						propertyArray.push(jsonArray[i].property.id);
						propertyNameArray.push(jsonArray[i].property.name);
						//propSelectHtml+='<option value="'+jsonArray[i].property.name+'" }>'+jsonArray[i].property.name+'</option>';
						
						//属性值分组处理
						if(jsonProGroupArray != null && jsonProGroupArray.length>0){
							 html3 = '<br><select  onchange="changeProGroup(this,1)" loxiaType="select"  propertyId="'+jsonArray[i].property.id+'">' ;
							 html3 += '<option value="">'+nps.i18n("PLEASE_SELECT_PROPERTY_GROUP")+'</option>';
							 $j.each(jsonProGroupArray, function (i) {
							      html3 += '<option value="'+jsonProGroupArray[i].id + '">'+  jsonProGroupArray[i].name + '</option>';
							 });
							 html3 += "</select>"
						}
						
						
						//属性值处理
						$j.each(jsonArray2,function(j,val){
							if(jsonArray[i].property.isColorProp && jsonArray[i].property.hasThumb ){
								picUrl ="<img src='"+baseUrl+"/images/1.png'>";
							}
							html1+= "<div class='priDiv'><span class='children-store'>" +
									"<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' " +
									"pvId='"+jsonArray2[j].id+"' propertyId='"+jsonArray[i].property.id+"' " +
									"pvValue='"+jsonArray2[j].value+"' propertyName='"+jsonArray[i].property.name+ "'" +
									"value='"+jsonArray2[j].id+"'>"+picUrl+jsonArray2[j].value+"</input></span> </div>";
							
					     });
						
						
						
						html2= "<div class='ui-block-line '><label style=''>"+jsonArray[i].property.name+"</label><div >"+
							"<input type='hidden' value='"+ jsonArray[i].property.id +"'  name = 'propertyIds' />"+
							"<input type='hidden' value='' name='propertyValueInputIds' pid='"+jsonArray[i].property.id+"' />"+
							html3+"<div class='wl-right-auto width-percent50 mt10'>"+ html1+
					        " </div>"+
					        " </div>"+
						    " </div>";
					}
				  }else if(jsonArray[i].property.editingType == 5){
					  
					propertyArray.push(jsonArray[i].property.id);
					propertyNameArray.push(jsonArray[i].property.name);
					
					html2 = '<div class="ui-block-line "><label>'+jsonArray[i].property.name+'</label>';
					html2+= "<input type='hidden' name = 'propertyIds' value='"+ jsonArray[i].property.id +"' />";
					html2+="<input type='hidden' value='' name='propertyValueInputs' pid='"+jsonArray[i].property.id+"' />";
					html2 += '<div class="priDiv">';
					if(jsonArray[i].property.isColorProp){
						html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="true" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
					}else{
						html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="false" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
						
					}
						html2 += '<span style="margin: 17px 0 0 10px; position: absolute;">属性名之间请以"||"分隔</span>'
						+'</div></div>';
				}
				SalepropertySpaceHtml=SalepropertySpaceHtml+html2;
				
				var SalepropertySpaceHtmlObj= {};
				SalepropertySpaceHtmlObj.html = SalepropertySpaceHtml;
				SalepropertySpaceHtmlArr.push(SalepropertySpaceHtmlObj);
				//是否存在颜色属性
				if(jsonArray[i].property.isColorProp){
					isExistColorProp = true;
					var propertyValueListJson = '';
					//editingType为多选
					var str = "";
					if(jsonArray[i].property.editingType == 4){
						propertyValueListJson = jsonArray[i].propertyValueList;
						if(propertyValueListJson != '' && propertyValueListJson.length > 0){
							str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
							str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
							$j.each(propertyValueListJson, function(index, propertyValue){
								str += '<option value="'+propertyValue.id+'#">'+propertyValue.value+'</option>';
							});
							str += '</select>';
						}
					}else if(jsonArray[i].property.editingType == 5){
						str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
						str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
						str += '<option value="">'+nps.i18n('PLEASE_SELECT')+'</option>';
						str += '</select>';
					}
					$j('.colorPropertySelect').html(str);
					$j('#colorPropertyContent').empty();
					$j('#colorPropertyContent').next('.ui-block-line').show();
				}
			}
		  });
		}
		
		//非销售属性
		$j.each(jsonArray,function(i,val){
			if(!jsonArray[i].property.isSaleProp){
				//编辑类型 ：1 单行输入2可输入单选3单选4多选
				var html3="";
				jsonArray2 = jsonArray[i].propertyValueList  ;
				var isIncrease =true ;
				if(jsonArray[i].property.editingType==1){
					var required="";
					if(jsonArray[i].property.required){
						required="mandatory='true'";
					}							
					var inputHtml ="";
					if(i18nOnOff){
						if(jsonArray[i].property.valueType==1){
							for ( var j = 0; j < i18nLangs.length; j++) {
								var i18nLang = i18nLangs[j];
								inputHtml=inputHtml+"<input type=\"text\" name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" style='width: 600px' loxiaType=\"input\" "+required+" />";
								inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
								'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
							}
						}
						if(jsonArray[i].property.valueType==2){
							for ( var j = 0; j < i18nLangs.length; j++) {
								var i18nLang = i18nLangs[j];
								inputHtml=inputHtml+"<input type=\"text\" name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" loxiaType=\"number\" "+required+" />";
								inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
								'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
							}
						}
						if(jsonArray[i].property.valueType==3){
							for ( var j = 0; j < i18nLangs.length; j++) {
								var i18nLang = i18nLangs[j];
								inputHtml=inputHtml+"<input type=\"text\"   name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" loxiaType=\"date\" "+required+" />";
								inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
								'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
							}	
						}
						if(jsonArray[i].property.valueType==4){
							for ( var j = 0; j < i18nLangs.length; j++) {
								var i18nLang = i18nLangs[j];
								inputHtml=inputHtml+"<input type=\"text\" showtime=\"true\"   name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" loxiaType=\"date\" "+required+" />";
								inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
								'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
							}
						}
					}else{
						if(jsonArray[i].property.valueType==1){
							inputHtml="<input type=\"text\" name=\"iProperties.propertyValue.value["+num+"]\" style='width: 600px' loxiaType=\"input\" "+required+" />";
						}
						if(jsonArray[i].property.valueType==2){
							inputHtml="<input type=\"text\" name=\"iProperties.propertyValue.value["+num+"]\" loxiaType=\"number\" "+required+" />";
						}
						if(jsonArray[i].property.valueType==3){
							inputHtml="<input type=\"text\"   name=\"iProperties.propertyValue.value["+num+"]\" loxiaType=\"date\" "+required+" />";
						}
						if(jsonArray[i].property.valueType==4){
							inputHtml="<input type=\"text\" showtime=\"true\"   name=\"iProperties.propertyValue.value["+num+"]\" loxiaType=\"date\" "+required+" />";
						}
						
					}
				
					html3="<label >"+jsonArray[i].property.name+"</label>"+
                      "<div >" +inputHtml+
                       "<input type='hidden' name='iProperties.propertyId' value='"+jsonArray[i].property.id+"'/>" +
                       "<input type='hidden' name='iProperties.id' value=''/>" +
                       "<input type='hidden' name='iProperties.propertyDisplayValue' value=''/>" + 
                       "<input type='hidden' name='iProperties.createTime' value=''/>" + 
                       "<input type='hidden' name='iProperties.modifyTime' value=''/>" + 
                       "<input type='hidden' name='iProperties.version' value=''/>" + 
                       "<input type='hidden' name='iProperties.itemId' value=''/>" + 
                       "<input type='hidden' name='iProperties.propertyValueId' value=''/>" + 
                       "<input type='hidden' name='iProperties.picUrl' value=''/>" + 
                       "</div>";
				}else if(jsonArray[i].property.editingType==2){
					html3="<label >"+jsonArray[i].property.name+"</label><div ><select style='width:160px;height:25px' onchange='doOther(this,"+jsonArray[i].property.id+")' name='iProperties.propertyValueId'>";
                      $j.each(jsonArray2,function(j,val){
							html3+= "<option value ='"+jsonArray2[j].id+"' >"+jsonArray2[j].value+"</option>";
					     });
                      html3+=" <option value ='' >"+nps.i18n("OTHERS")+"</option></select>" +
                       "<input type='hidden' name='iProperties.propertyId' value='"+jsonArray[i].property.id+"'/>" +
                       "<input type='hidden' name='iProperties.id' value=''/>" +
                       "<input type='hidden' name='iProperties.propertyDisplayValue' value=''/>" + 
                       "<input type='hidden' name='iProperties.createTime' value=''/>" + 
                       "<input type='hidden' name='iProperties.modifyTime' value=''/>" + 
                       "<input type='hidden' name='iProperties.version' value=''/>" + 
                       "<input type='hidden' name='iProperties.itemId' value=''/>" + 
                       "<input type='hidden' name='iProperties.picUrl' value=''/>" ;
                      if(i18nOnOff){
                    	  for ( var j = 0; j < i18nLangs.length; j++) {
								var i18nLang = i18nLangs[j];
								html3 = html3 +	
			                       "&nbsp;<input type='hidden' id='pv_"+jsonArray[i].property.id+"'  name='iProperties.propertyValue.values["+num+"-"+j+"]' value=''/>";
		                    	html3= html3+'<input class="i18n-lang"  type="text" '+
									'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/>';
                    	  }
                    	  html3 = html3 +"</div>";
                      }else{
                    	  html3 = html3 +	
	                       "&nbsp;<input type='hidden' id='pv_"+jsonArray[i].property.id
	                       +"'  name='iProperties.propertyValue.value["+num+"]' value=''/>" + 
	                      "</div>";
                      }
                       
				}else if(jsonArray[i].property.editingType==3){
					html3="<label >"+jsonArray[i].property.name+"</label><div ><select style='width:160px;height:25px' name='iProperties.propertyValueId'>";
                      $j.each(jsonArray2,function(j,val){
							html3+= "<option value ='"+jsonArray2[j].id+"'>"+jsonArray2[j].value+"</option>";
					     });
                      html3+="</select>" +
                       "<input type='hidden' name='iProperties.propertyId' value='"+jsonArray[i].property.id+"'/>" +
                       "<input type='hidden' name='iProperties.id' value=''/>" +
                       "<input type='hidden' name='iProperties.propertyDisplayValue' value=''/>" + 
                       "<input type='hidden' name='iProperties.createTime' value=''/>" + 
                       "<input type='hidden' name='iProperties.modifyTime' value=''/>" + 
                       "<input type='hidden' name='iProperties.version' value=''/>" + 
                       "<input type='hidden' name='iProperties.itemId' value=''/>" + 
                       "<input type='hidden' name='iProperties.picUrl' value=''/>" ;
                       if(i18nOnOff){
                    	   for ( var j = 0; j < i18nLangs.length; j++) {
								var i18nLang = i18nLangs[j];
							    html3 = html3+ "<input type='hidden' name='iProperties.propertyValue.values["+num+"-"+j+"]' value=''/></div>";
	                    	    html3= html3+'<input class="i18n-lang"  type="text" '+
								'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/>';
                    	   }
                       }else{
                    	   html3 = html3+ "<input type='hidden' name='iProperties.propertyValue.value["+num+"]' value=''/></div>";
                       }
				}else if(jsonArray[i].property.editingType==4){
					isIncrease =false ;
					if(jsonArray2!=null&&jsonArray2.length>0){
						html3="<label >"+jsonArray[i].property.name+"</label>";
						if(jsonArray[i].property.required){
						    mustCheckArray.push(jsonArray[i].property.name);
						}
						
						//属性值分组列表
						jsonProGroupArray = jsonArray[i].propertyValueGroupList;
						
						//属性值分组处理
						if(jsonProGroupArray != null && jsonProGroupArray.length>0){
							html3 += '<br><select  onchange="changeProGroup(this,2)" loxiaType="select"  propertyId="'+jsonArray[i].property.id+'">' ;
							html3 += '<option value="">'+nps.i18n("PLEASE_SELECT_PROPERTY_GROUP")+'</option>';
							 $j.each(jsonProGroupArray, function (i) {
								 html3 += '<option value="'+jsonProGroupArray[i].id + '">'+  jsonProGroupArray[i].name + '</option>';
							 });
							 html3 += "</select>"
						}
						
						html3 += "<div>"
						
						$j.each(jsonArray2,function(j,val){
							html3+= "<span class='children-store normalCheckBoxSpan'><input type='checkbox' class = 'normalCheckBoxCls' pid="+jsonArray[i].property.id+" mustCheck='"+jsonArray[i].property.name+"' name='' " +
									"value='"+jsonArray2[j].id+"'>"+jsonArray2[j].value+"</input></span>";
					     });
						
						html3 += "</div>"

						
					}
				}else if(jsonArray[i].property.editingType==5){
					html3 = '<label>'+jsonArray[i].property.name+'</label>';
					html3 += '<div class="priDiv">';
					if(i18nOnOff){
						for ( var j = 0; j < i18nLangs.length; j++) {
							var i18nLang = i18nLangs[j];
							html3 = html3+'<textarea loxiaType="input" name="iProperties.propertyValue.values['+num+'-'+j+']"  style="width: 600px; height: 45px" ></textarea>';
							html3= html3+'<input class="i18n-lang"  type="text" name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/>';
							html3 = html3+'<span style="margin: 17px 0 0 10px; position: absolute;">'+i18nLang.value+'&nbsp;&nbsp;属性名之间请以"||"分隔</span><br>';
						}  
					}else{
						html3 = html3+'<textarea loxiaType="input" name="iProperties.propertyValue"  style="width: 600px; height: 45px" ></textarea>';
						html3 = html3+'<span style="margin: 17px 0 0 10px; position: absolute;">属性名之间请以"||"分隔</span>';
					}
                    html3 = html3+'<input type="hidden" name="iProperties.propertyId" value="'+jsonArray[i].property.id+'"/>'
						+'<input type="hidden" name="iProperties.id" value=""/>'
						+'<input type="hidden" name="iProperties.propertyDisplayValue" value=""/>'
						+'<input type="hidden" name="iProperties.createTime" value=""/>'
						+'<input type="hidden" name="iProperties.modifyTime" value=""/>'
						+'<input type="hidden" name="iProperties.version" value=""/>'
						+'<input type="hidden" name="iProperties.itemId" value=""/>'
						+'<input type="hidden" name="iProperties.picUrl" value=""/>'
						+'<input type="hidden" name="iProperties.propertyValueId" value=""/></div>';
                    
				}
				
				var html4="";
				if(html3!=""){
					html4="<div class='ui-block-line '>"+html3+"</div>";
				}
				notSalepropertySpaceHtml=notSalepropertySpaceHtml+html4;
				
				if(isIncrease){
					num++;
				}
				
			}
		});
		
		var rhtml3 ="";
		
		$j.each(jsonArray,function(i,val){
			if(!jsonArray[i].property.isSaleProp){
				jsonArray2 = jsonArray[i].propertyValueList  ;
				if(jsonArray[i].property.editingType==4){
					if(jsonArray2!=null&&jsonArray2.length>0){
						$j.each(jsonArray2,function(j,val){
							rhtml3+= "<span class='hidBoxSpan'><div type='hidden' class = 'repNormalCheckBoxCls' pid="+jsonArray[i].property.id+" pvid='"+jsonArray2[j].id+"'/></span>";
					     });
					}
				}
			}
		});
		
		notSalepropertySpaceHtml +=rhtml3 ;
		
		//销售属性
		for ( var n = 0; n < SalepropertySpaceHtmlArr.length; n++) {
			var a = SalepropertySpaceHtmlArr[n];
			if(i18nOnOff){
				$j(".saleInfo[lang='"+a.lang+"']").find("#propertySpace").html(a.html);
			}else{
				$j(".saleInfo").find("#propertySpace").html(a.html);
			}
		}
		
		if(i18nOnOff){
			$j(".saleInfo").each(function(i,obj){
				var lang =$j(this).attr("lang");
				if(lang!=defaultlang){
					$j(this).find("#extensionTable").remove();
				}
			});
		}
		
		$j("#notSalepropertySpace").html(notSalepropertySpaceHtml);
		$j("#propSelect").html(propSelectHtml);
		$j("#notSalepropertySpace").find("[loxiaType]").each(function(i,n){
			loxia.initLoxiaWidget(this);
		});
	
	}else{
		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SYSTEM_ITEM_SELECT_INDUSTRY"));
	}
}

$j(document).ready(function(){
	initProperties($j('#industryId').val());
	
	//编码设置
	$j(".func-button.extension").on("click",function(){
//		produceExtension();
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
			saleInfo.find("#exten").show();
			saleInfo.find(".extensionTable").html("多语言属性设置数量不一致");
			return;
		}
		showSkuTable(saleInfo);
	});
	
	if(i18nOnOff){
		$j(".saleInfo").each(function(){
			var me = $j(this);
			var lang = me.attr("lang");
			if(lang != defaultlang){
				me.find(".exten").remove();
				me.find(".func-button.extension").parent().parent().remove();
			}
		});
	}
	
	// 添加商品属性表单验证方法
	var baseInfoValidator = new FormValidator('', 30, function(){
		
		//判断非销售属性复选框必选
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
		
		// 销售属性extentionCode验证
		if(!clickFlag){
			return nps.i18n("PLEASE_SET_CODE");
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
				return nps.i18n("SKU_CODE_REPEAT") + data.description;
			}

		}
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(baseInfoValidator);
});