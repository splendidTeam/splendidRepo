var propertyArray = new Array();
var propertyNameArray = new Array();
var mustCheckArray = new Array();

var itemProperties = 0;

var existSaleProp = true;
var spChangedFlag = false;

var itemcolorrefcheckURL = "/itemColor/itemcolorcheck.json";
var saveitemcolorrefURL = "/itemColor/savecolorvalue.htm";
var manageImagUrl = base + '/i18n/itemImage/toAddItemImage.htm?itemId=';
var validateUpdateSkuCodesUrl = base + '/item/validateUpdateSkuCodes.json';
var validateGroupCodesCodesUrl = base + '/item/validateGroupCodes.json';

function saveitemcolorref() {
	var selestrefre = selestref();
	var json = {
		'itemcolor' : selestrefre,
		'code' : $j("#code").val()
	};
	// 提交表單
	$j.ajax({
		url : saveitemcolorrefURL,
		data : json,
		type : "post",
		success : function(data) {
		}
	});
}

// 选取其他
function doOther(selOp, proId) {
	var len = selOp.length - 1;
	var sel = selOp.selectedIndex;
	if (len == sel) {
		$j("#pv_" + proId).attr("type", "text");
	} else {
		$j("#pv_" + proId).attr("type", "hidden");
		$j("#pv_" + proId).val("");
	}
}

function getDynamicInputName(key, array1, array2) {
	var fullKey = "";
	if (array1 != null) {
		fullKey += getKeyFromArray(array1);
		fullKey += "_";
	}

	if (array2 != null) {

		fullKey += getKeyFromArray(array2);
		fullKey += "_";
	}

	fullKey += key;

	return fullKey;
}

function getKeyFromArray(array) {
	// [0] 是pvname [1] 是 pvid 对于有pvid的 传pvid 对于没有pvId 的 传pvName
	var arraykey = "";
	if (array[1] != null || array[1] != undefined) {// 多选
		arraykey = array[1];
	} else {// 自定义多选 有pvName 而没pvId
		arraykey = array[0];
	}
	return arraykey;
}

function getSkuInfoByProperyValueArray(pArray) {
	// -----------------start by sunchenbin-----------------------------
	// 这里判断是否验证销售属性必填，这一项是在系统参数里面配置的 by sunchenbin
	if (salesOfPropertyIsNotRequired != "" && "1" == salesOfPropertyIsNotRequired) {
		// 这里是转为多销售属性时，销售属性非必填所使用的
		var currentSelectedPropertyIds = getSelectedPropertyIds();
		if ((initSelectedPropertyIds != "" && initSelectedPropertyIds != undefined)
				&& currentSelectedPropertyIds != initSelectedPropertyIds) {
			return null;
		}
	}
	// -------------------end by sunchenbin---------------------------
	var itemPropertyIdArray = new Array();
	var index = 0;
	for (var i = 0; i < pArray.length; i++) {
		var propertyId = pArray[i][3];
		var propertyValueId = pArray[i][1];
		var propertyValue = pArray[i][0];
		var itemProperty = findItemPropertyByProvertyIdAndValue(propertyId, propertyValueId, propertyValue);

		if (itemProperty != null) {
			var itemPropertyId = itemProperty.id;
			itemPropertyIdArray[index] = itemPropertyId;
			index++;
		}
	}
	itemPropertyIdArray = itemPropertyIdArray.sort();

	for (var k = 0; k < skuList.length; k++) {
		var sku = skuList[k];
		var skuPropertiesArray = JSON.parse(sku.properties);
		skuPropertiesArray = skuPropertiesArray.sort();

		if (skuPropertiesArray.toString() == itemPropertyIdArray.toString()) {
			return sku;
		}
	}

	return null;
}

function findItemPropertyByProvertyIdAndValue(propertyId, propertyValueId, propertyValue) {
	for (var i = 0; i < itemPropertiesStr.length; i++) {
		var itemProperty = itemPropertiesStr[i];
		if (propertyValueId != undefined && propertyValueId != null) {// 多选
			if (itemProperty.propertyId == propertyId && propertyValueId == itemProperty.propertyValueId) {
				return itemProperty;
			}

		} else {// 自定义多选
			if (i18nOnOff) {
				if (!!itemProperty.propertyValue) {
					if (itemProperty.propertyId == propertyId
							&& propertyValue == itemProperty.propertyValue.defaultValue) {
						return itemProperty;
					}
				}

			} else {
				if (!!itemProperty.propertyValue) {
					if (itemProperty.propertyId == propertyId && propertyValue == itemProperty.propertyValue.value) {
						return itemProperty;
					}
				}
			}

		}
	}
	return null;
}

function showSkuTable(isLoadInit, saleInfo) {
	// -----------------start by sunchenbin----------------------
	// 这里判断是否验证销售属性必填，这一项是在系统参数里面配置的 by sunchenbin
	if (salesOfPropertyIsNotRequired != "" && "1" == salesOfPropertyIsNotRequired) {
		removeNoSelectInputName();
	}
	// -----------------end by sunchenbin----------------------
	clickFlag = true;
	if (i18nOnOff) {
		if (typeof (saleInfo) == 'undefined') {
			$j(".saleInfo").each(function(i, dom) {
				var me = $j(this);
				var lang = me.attr("lang");
				if (lang == defaultlang) {
					saleInfo = me;
				}
			});
		}
		var lang = saleInfo.attr("lang");
		if (lang != defaultlang) {
			return;
		}
	} else {
		if (typeof (saleInfo) == 'undefined') {
			saleInfo = $j(".saleInfo");
		}
	}
	var min_width=$j(window).width()-20;
	saleInfo.find(".exten").css({'display': 'block', 'overflow-x':'auto', 'width':min_width});
	saleInfo.find(".extensionTable").html("");

	// 拿到 销售属性, 根据销售属性 确定 列
	var salesProperty = new Array();

	var salesPropertyIndex = 0;
	for (var i = 0; i < dynamicPropertyCommandListJsonStr.length; i++) {
		var dynamicPropertyCommand = dynamicPropertyCommandListJsonStr[i];
		// 编辑属性为4 或者5 的是 销售属性
		if (dynamicPropertyCommand != null
				&& (dynamicPropertyCommand.property.editingType == 4 || dynamicPropertyCommand.property.editingType == 5)
				&& dynamicPropertyCommand.property.isSaleProp == true) {
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
	for (var i = 0; i < salesProperty.length; i++) {
		var saleProperty = salesProperty[i];
		var propertyId = saleProperty.property.id;

		var curPropertyName = null;

		var selectedArray = new Array();

		var pInputArray = new Array();

		var index = 0;

		$j("input[propertyId='" + propertyId + "']").each(function() {

			if ($j(this).attr("checked")) {
				var pvValue = $j(this).attr("pvValue");
				var pvId = $j(this).attr("pvId");
				var propertyName = $j(this).attr("propertyName");
				var propertyId = $j(this).attr("propertyId");

				var pArray = new Array();// 最里边
				pArray[0] = $j(this).attr("pvValue");
				pArray[1] = $j(this).attr("pvId");
				pArray[2] = $j(this).attr("propertyName");
				pArray[3] = $j(this).attr("propertyId");

				pInputArray[index] = pArray[1];

				selectedArray[index] = pArray;
				index++;
				curPropertyName = propertyName;
			}

		});

		if (selectedArray.length > 0) {
			propertyIdArray[propertyValueArrayIndex] = propertyId;
			propertyValueArray[propertyValueArrayIndex] = selectedArray;

			propertyValueInputArray[propertyValueArrayIndex] = pInputArray;
			propertyValueArrayIndex++;

			saleInfo.find("input[pid='" + propertyId + "']").each(function(index) {
				var selectdStr = "";
				for (var i = 0; i < pInputArray.length; i++) {
					selectdStr += pInputArray[i];
					selectdStr += ",";
				}
				selectdStr = selectdStr.substring(0, selectdStr.length - 1);
				$j(this).val(selectdStr);

			});
		}

		saleInfo.find("textarea[propertyId='" + propertyId + "'][editingtype='5']").each(function() {
			if ($j(this).val() != "") {
				var index = 0;
				var content = $j(this).val();
				var valueArray = content.split("||");
				var inputValueArray = new Array();
				for (var k = 0; k < valueArray.length; k++) {
					var pArray = new Array();

					pArray[0] = valueArray[k];
					pArray[2] = saleProperty.property.name;
					pArray[3] = $j(this).attr("propertyId");
					inputValueArray[index] = pArray;

					index++;

					curPropertyName = pArray[2];
				}

				if (inputValueArray.length > 0) {
					propertyIdArray[propertyValueArrayIndex] = propertyId;
					propertyValueArray[propertyValueArrayIndex] = inputValueArray;
					propertyValueInputArray[propertyValueArrayIndex] = valueArray;
					propertyValueArrayIndex++;

					saleInfo.find("input[pid='" + propertyId + "']").each(function(index) {
						var selectdStr = "";
						for (var i = 0; i < pInputArray.length; i++) {
							selectdStr += pInputArray[i];
							selectdStr += ",";
						}
						selectdStr.substring(0, selectdStr.length - 1);

						for (var i = 0; i < valueArray.length; i++) {
							for (var j = 0; j < valueArray.length; j++) {
								if (i != j) {
									if (valueArray[i] == valueArray[j]) {

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

		// 其他语言(仅editType=5)的hidden propertyValueInputs input设置
		if (i18nOnOff) {
			$j(".saleInfo").each(function(i, dom) {
				var me = $j(this);
				var lang = me.attr("lang");
				if (lang != defaultlang) {
					me.find("textarea[propertyId='" + propertyId + "'][editingtype='5']").each(function() {
						if ($j(this).val() != "") {
							var index = 0;
							var content = $j(this).val();
							var valueArray = content.split("||");
							var inputValueArray = new Array();
							for (var k = 0; k < valueArray.length; k++) {
								var pArray = new Array();

								pArray[0] = valueArray[k];
								pArray[2] = saleProperty.property.name;
								pArray[3] = $j(this).attr("propertyId");
								inputValueArray[index] = pArray;

								index++;

							}

							if (inputValueArray.length > 0) {

								me.find("input[pid='" + propertyId + "']").each(function(index) {
									var selectdStr = "";
									for (var i = 0; i < pInputArray.length; i++) {
										selectdStr += pInputArray[i];
										selectdStr += ",";
									}
									selectdStr.substring(0, selectdStr.length - 1);

									for (var i = 0; i < valueArray.length; i++) {
										for (var j = 0; j < valueArray.length; j++) {
											if (i != j) {
												if (valueArray[i] == valueArray[j]) {

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

	if (sameNameFlag) {
		clickFlag = false;
		html = nps.i18n("CUSTOM_PROPERTY_SAME");
		saleInfo.find(".extensionTable").html(html);

		return;
	}

	// 这里判断是否验证销售属性必填，这一项是在系统参数里面配置的 by sunchenbin
	if (salesOfPropertyIsNotRequired != "" && "1" == salesOfPropertyIsNotRequired) {
		drawTableContent(propertyValueArray, propertyNameArray, propertyValueInputArray, isLoadInit, saleInfo);
	} else {
		if (propertyValueInputArray.length != propertyNameArray.length) {// 如果用户未把每个属性都填上值，则提示用户
			html = nps.i18n("CODING_TIPS");
			saleInfo.find(".extensionTable").html(html);
		} else {// 画表格
			drawTableContent(propertyValueArray, propertyNameArray, propertyValueInputArray, isLoadInit, saleInfo);
		}
	}

	saleInfo.find(".extensionTable").find("[loxiaType]").each(function(i, n) {
		loxia.initLoxiaWidget(this);
	});

}

function drawTableContent(propertyValueArray, propertyNameArray, propertyInputValueArray, isLoadInit, saleInfo) {
	// -----------------start by sunchenbin----------------------
	// 这里判断是否验证销售属性必填，这一项是在系统参数里面配置的 by sunchenbin
	if (salesOfPropertyIsNotRequired != "" && "1" == salesOfPropertyIsNotRequired) {
		// 仅作为销售属性非必填时，此时table的列标题是不确定的，所以这里动态加载
		var propertyNameArrayNew = new Array();
		$j.each(propertyValueArray, function(i) {
			$j.each(propertyValueArray[i], function(j) {
				propertyNameArrayNew[i] = ((propertyValueArray[i])[0])[2];
			});
		});
		propertyNameArray = propertyNameArrayNew;
	}
	// --------------------end by sunchenbin--------------------

	var skuInfoList = new Array();
	var skuInfoListIndex = 0;
	var tableHeader = "<tr>";
	for (var j = 0; j < propertyNameArray.length; j++) {// 动态生成 销售属性的列
		tableHeader += "<td style='width:120px'>" + propertyNameArray[j] + "</td>";
	}

	tableHeader += "<td style='width:120px'>" + nps.i18n("MERCHANT_CODING") + "</td>" + "<td style='width:120px'>"
			+ nps.i18n("MERCHANT_SALEPRICE") + "</td>" + "<td style='width:120px'>" + nps.i18n("MERCHANT_LISTPRICE")
			+ "</td>" + "<td style='width:120px'>" + nps.i18n("MERCHANT_GROUPCODE") + "</td>";
	if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
		tableHeader +="<td style='width:120px'>"+ nps.i18n("MERCHANT_WEIGHT") + "</td>"+"<td style='width:120px'>" + nps.i18n("MERCHANT_VOLUME") + "</td>"
	}
	tableHeader+="</tr>";
	var tableContent = "";
	if (propertyValueArray.length > 0) {

		/**
		 * add by hr 2014/07/11
		 * 现在nebula中的商品最多只有两个销售属性: 
		 * 1, 不存在销售属性: item与sku是一一对应的
		 * 2, 存在一个销售属性: item与sku是一对多的关系
		 * 3, 存在两个销售属性: item与sku是一对多的关系
		 */
		if (propertyValueArray.length == 1) {// 销售属性只有1个
			for (var i = 0; i < propertyValueArray[0].length; i++) {
				var dynamicStr = "<td style='width:120px'>" + propertyValueArray[0][i][0] + "</td>";
				var codesName = getDynamicInputName("code", propertyValueArray[0][i], null);
				var salePriceName = getDynamicInputName("salePrice", propertyValueArray[0][i], null);
				var listPriceName = getDynamicInputName("listPrice", propertyValueArray[0][i], null);
				var idName = getDynamicInputName("id", propertyValueArray[0][i], null);
				var groupCode = getDynamicInputName("groupCode", propertyValueArray[0][i], null);
				
				var weight = getDynamicInputName("weight", propertyValueArray[0][i], null);
				var volue = getDynamicInputName("volue", propertyValueArray[0][i], null);

				var tmpArray = new Array();
				tmpArray[0] = propertyValueArray[0][i];
				var sku = getSkuInfoByProperyValueArray(tmpArray);

				var proHtml = "<input type='hidden' name='idNameToReplace' value = 'idValueTOReplace' />"
						+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameSkuCode'  name='codesNameToReplace' loxiaType='input' skuId='idValueTOReplace'  value='CODE_VALUE'/></td>"
						+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices' name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>"
						+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>"
						+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameGroupCode'  name='groupCodeNameToReplace' loxiaType='input'  value='groupCode_value'/></td>";
				
				if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
					proHtml+="<td style='width:120px'><input type='text' class = 'dynamicInputNameWeight'  name='weightNameToReplace' decimal='2' loxiaType='number'  value='weight_value'/></td>"
						+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameVolue'  name='volueNameToReplace' decimal='2' loxiaType='number'  value='volue_value'/></td>";
					proHtml = proHtml.replace('weightNameToReplace', weight);
					proHtml = proHtml.replace('volueNameToReplace', volue);
				}
				proHtml = proHtml.replace('codesNameToReplace', codesName);
				proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
				proHtml = proHtml.replace('listPriceToReplace', listPriceName);
				proHtml = proHtml.replace('idNameToReplace', idName);
				proHtml = proHtml.replace('groupCodeNameToReplace', groupCode);
				

				var salePrice = $j("#salePrice").val();
				var listPrice = $j("#listPrice").val();
				var code = "";
				var groupCode = "";
				var volue ="";
				var weight = "";
				var skuId = null;
				if (sku != null) {
					salePrice = ((sku.salePrice == null || sku.salePrice == "") ? salePrice : sku.salePrice);
					listPrice = ((sku.listPrice == null || sku.listPrice == "") ? listPrice : sku.listPrice);
					code = (sku.outid == null || sku.outid == "") ? code : sku.outid;
					groupCode = (sku.groupCode == null || sku.groupCode == ""||sku.groupCode==undefined) ? groupCode : sku.groupCode;
					weight = (sku.weight == null || sku.weight == ""||sku.weight ==undefined) ? weight : sku.weight;
					volue = (sku.volume == null || sku.volume == ""||sku.volume ==undefined) ? volue : sku.volume;
					skuId = (sku.id == null || sku.id == "") ? skuId : sku.id;
				}

				if (isLoadInit) {// 如果是初始化的时候，就不显示没有skuId的情况。
					if (skuId == null) {
						continue;
					}
				}

				proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
				proHtml = proHtml.replace('CODE_VALUE', code);
				proHtml = proHtml.replace('salePrices_value', salePrice);
				proHtml = proHtml.replace('listPrices_value', listPrice);
				proHtml = proHtml.replace('groupCode_value', groupCode);
				if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
					proHtml = proHtml.replace('weight_value', weight);
					proHtml = proHtml.replace('volue_value', volue);
				}

				tableContent += ("<tr>" + dynamicStr + proHtml + "</tr>");
			}

		} else {
			// 多个销售属性的展示
			tableContent = bulidTable(null, propertyValueArray, 0, isLoadInit);

		}

		$j("#jsonSku").val(JSON.stringify(skuInfoList));
	} else {
		var sku = skuList[0];

		var proHtml = "<input type='hidden' name='skuId' value = 'idValueTOReplace' />"
			+ "<td style='width:150px'><input type='text' mandatory='true' class = 'dynamicInputNameSkuCode' name='skuCode' loxiaType='input' skuId='idValueTOReplace'  value='CODE_VALUE'/></td>"
			+ "<td style='width:150px'><input type='text' id='salePrices' class = 'dynamicInputNameSalePrices' mandatory='true' name='skuSalePrice' decimal='2' loxiaType='number' value='salePrices_value'/></td>"
			+ "<td style='width:150px'><input type='text' id='listPrices' class = 'dynamicInputNameListPrices' name='skuListPrice' decimal='2' loxiaType='number' value='listPrices_value'/></td>"
			+ "<td style='width:150px'><input type='text' class = 'dynamicInputNameGroupCode'  name='groupCode' loxiaType='input'  value='groupCode_value'/></td>";
	
		if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
			proHtml+="<td style='width:120px'><input type='text' class = 'dynamicInputNameWeight'  name='weightNameToReplace' decimal='2' loxiaType='number'  value='weight_value'/></td>"
				+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameVolue'  name='volueNameToReplace' decimal='2' loxiaType='number'  value='volue_value'/></td>";
		}
		var salePrice = $j("#salePrice").val();
		var listPrice = $j("#listPrice").val();
		var groupCode = "";
		var weight = "";
		var volue ="";
		var weight = "";
		var skuId = null;
		if (sku != null) {
			salePrice = ((sku.salePrice == null || sku.salePrice == "") ? salePrice : sku.salePrice);
			listPrice = ((sku.listPrice == null || sku.listPrice == "") ? listPrice : sku.listPrice);
			code = (sku.outid == null || sku.outid == "") ? code : sku.outid;
			groupCode = (sku.groupCode == null || sku.groupCode == "") ? groupCode : sku.groupCode;
			weight = (sku.weight == null || sku.weight == ""||sku.weight ==undefined) ? weight : sku.weight;
			volue = (sku.volume == null || sku.volume == ""||sku.volume ==undefined) ? volue : sku.volume;
			skuId = (sku.id == null || sku.id == "") ? skuId : sku.id;
		}

		proHtml = proHtml.replace('CODE_VALUE', code);
		proHtml = proHtml.replace('salePrices_value', salePrice);
		proHtml = proHtml.replace('listPrices_value', listPrice);
		proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
		proHtml = proHtml.replace('groupCode_value', groupCode);
		if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
			proHtml = proHtml.replace('weight_value', weight);
			proHtml = proHtml.replace('volue_value', volue);
		}

		tableContent += ("<tr>" + proHtml + "</tr>");
		list = "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}

	var html = tableHeader + tableContent;
	saleInfo.find(".extensionTable").html(html);

}

// --------------------------------多个销售属性循环start-------------------------------
function bulidTable(table, data, dataRowIndex, isLoadInit) {
	var result = new Array();
	if (table == null) {
		table = result;
	}

	if (dataRowIndex >= data.length) {
		var htmlStr = "";

		for (var i = 0; i < table.length; i++) {
			var htmlLine = "";
			var arrays = new Array();// 所有属性的数组
			for (var j = 0; j < table[i].length; j++) {
				htmlLine += "<td style='width:120px'>" + table[i][j][0] + "</td> ";
				arrays.push(table[i][j]);
			}

			var idName = getMoreDynamicInputName("id", arrays);
			var codesName = getMoreDynamicInputName("code", arrays);
			var salePriceName = getMoreDynamicInputName("salePrice", arrays);
			var listPriceName = getMoreDynamicInputName("listPrice", arrays);
			var groupCodeName = getMoreDynamicInputName("groupCode", arrays);
			var weightName = getMoreDynamicInputName("weight", arrays);
			var volueName = getMoreDynamicInputName("volue", arrays);
			
			
			var tmpArray = clone(arrays);
			var sku = getSkuInfoByProperyValueArray(tmpArray);

			var proHtml = "<input type='hidden' name='idNameToReplace' value = 'idValueTOReplace' />"
					+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameSkuCode'  skuId='idValueTOReplace' name='codesNameToReplace' loxiaType='input'  value='CODE_VALUE'/></td>"
					+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices' name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>"
					+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>"
					+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameGroupCode'  name='groupCodeNameToReplace' loxiaType='input'  value='groupCode_value'/></td>";
			
			if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
				proHtml+="<td style='width:120px'><input type='text' class = 'dynamicInputNameWeight'  name='weightNameToReplace' decimal='2' loxiaType='number'  value='weight_value'/></td>"
					+ "<td style='width:120px'><input type='text' class = 'dynamicInputNameVolue'  name='volueNameToReplace' decimal='2' loxiaType='number'  value='volue_value'/></td>";
			}
			proHtml = proHtml.replace('idNameToReplace', idName);
			proHtml = proHtml.replace('codesNameToReplace', codesName);
			proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
			proHtml = proHtml.replace('listPriceToReplace', listPriceName);
			proHtml = proHtml.replace('groupCodeNameToReplace', groupCodeName);
			if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
				proHtml = proHtml.replace('weightNameToReplace', weightName);
				proHtml = proHtml.replace('volueNameToReplace', volueName);
			}
			var salePrice = $j("#salePrice").val();
			var listPrice = $j("#listPrice").val();
			var code = "";
			var groupCode = "";
			var volue ="";
			var weight = "";
			var skuId = null;
			if (sku != null) {
				salePrice = ((sku.salePrice == null || sku.salePrice == "") ? salePrice : sku.salePrice);
				listPrice = ((sku.listPrice == null || sku.listPrice == "") ? listPrice : sku.listPrice);
				code = (sku.outid == null || sku.outid == "") ? code : sku.outid;
				groupCode = (sku.groupCode == null || sku.groupCode == ""||sku.groupCode ==undefined) ? groupCode : sku.groupCode;
				weight = (sku.weight == null || sku.weight == ""||sku.weight ==undefined) ? weight : sku.weight;
				volue = (sku.volume == null || sku.volume == ""||sku.volume ==undefined) ? volue : sku.volume;
				skuId = (sku.id == null || sku.id == "") ? skuId : sku.id;
			}

			if (isLoadInit) {// 如果是初始化的时候，就不显示没有skuId的情况。
				if (skuId == null) {
					continue;
				}
			}

			proHtml = proHtml.replace('CODE_VALUE', code);
			proHtml = proHtml.replace('salePrices_value', salePrice);
			proHtml = proHtml.replace('listPrices_value', listPrice);
			proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
			proHtml = proHtml.replace('groupCode_value', groupCode);
			if (shippingPropertyIsNotRequired != "" && "1" == shippingPropertyIsNotRequired) {
				proHtml = proHtml.replace('weight_value', weight);
				proHtml = proHtml.replace('volue_value', volue);
			}
			
			if (htmlLine.length != 0) {
				htmlStr += "<tr>" + htmlLine + proHtml + "</tr>";
			}

		}
		return htmlStr;
	}
	// 组织第i个"销售属性"
	var dataRow = data[dataRowIndex];
	var dynamicStr = "";
	var proHtml = "";
	// 组织第1个"销售属性"时
	if (table.length == 0) {
		for (var i = 0; i < dataRow.length; i++) {
			var targetRow = new Array();

			targetRow.push(dataRow[i]);
			result.push(targetRow);
		}
		;
	} else {
		// 循环遍历某一个销售属性dataRow的全部值
		for (var i = 0; i < dataRow.length; i++) {
			for (var j = 0; j < table.length; j++) {
				var subResult = clone(table[j]);
				subResult.push(dataRow[i]);
				result.push(subResult);
			}
			;
		}
	}
	// 递归组织下一行数据
	return bulidTable(result, data, ++dataRowIndex, isLoadInit);
}
// --------------------------------多个销售属性循环end-------------------------------

// 获取动态输入框中的值 多个销售属性
function getMoreDynamicInputName(key, array) {
	var fullKey = "";
	for (var i = 0; i < array.length; i++) {
		if (array[i] != null) {
			fullKey += getKeyFromArray(array[i]);
			fullKey += "_";
		}
	}
	fullKey += key;
	return fullKey;
}

// js克隆
function clone(obj) {
	var o;
	switch (typeof obj) {
	case 'undefined':
		break;
	case 'string':
		o = obj + '';
		break;
	case 'number':
		o = obj - 0;
		break;
	case 'boolean':
		o = obj;
		break;
	case 'object':
		if (obj === null) {
			o = null;
		} else {
			if (obj instanceof Array) {
				o = [];
				for (var i = 0, len = obj.length; i < len; i++) {
					o.push(clone(obj[i]));
				}
			} else {
				o = {};
				for ( var k in obj) {
					o[k] = clone(obj[k]);
				}
			}
		}
		break;
	default:
		o = obj;
		break;
	}
	return o;
}

// obj 下拉框dom对象,type 属性类型(1:销售属性，2:普通属性) 两种属性生成HTML不同故做区分
function changeProGroup(obj, type) {

	var propertyId = $j(obj).attr('propertyId');
	var json = {
		proGroupId : $j(obj).val(),
		propertyId : propertyId
	};
	var backWarnEntity = loxia.syncXhr(base + '/item/findProGroupInfo.json', json, {
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
				$j.each(propertyValueArray, function(j) {
					if (property.isColorProp && property.hasThumb) {
						picUrl = "<img src='" + baseUrl + "/images/1.png'>";
					}
					html += "<div class='priDiv'><span class='children-store'>"
							+ "<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' ";
					// 属性值分组中后已选择的属性选中效果
					for (var i = 0; i < itemPropertiesStr.length; i++) {
						var itemProperty = itemPropertiesStr[i];
						if (itemProperty.propertyId == propertyId
								&& itemProperty.propertyValueId == propertyValueArray[j].id) {
							html += "checked='checked'";
							break;
						}
					}

					html += " pvId='" + propertyValueArray[j].id + "' propertyId='" + property.id + "' " + "pvValue='"
							+ propertyValueArray[j].value + "' propertyName='" + property.name + "'" + "value='"
							+ propertyValueArray[j].id + "'>" + picUrl + propertyValueArray[j].value
							+ "</input></span> </div>";

				});
			} else if (type == 2) {
				$j.each(propertyValueArray, function(j) {
					html += " <div class='priDiv'><span class='children-store'>"
							+ " <input type='checkbox' class='normalCheckBoxCls' name='filtratecolor'";
					// 属性值分组中后已选择的属性选中效果
					for (var i = 0; i < itemPropertiesStr.length; i++) {
						var itemProperty = itemPropertiesStr[i];
						if (itemProperty.propertyId == propertyId
								&& itemProperty.propertyValueId == propertyValueArray[j].id) {
							html += " checked='checked'";
							break;
						}
					}
					html += " pid='" + property.id + "' tname='" + propertyValueArray[j].value + " 'mustcheck='"
							+ property.name + "'" + " value='" + propertyValueArray[j].id + "'>"
							+ propertyValueArray[j].value + "</input></span> </div>";

				});
			}
			$j(obj).next().html(html);
			// 重新渲染
			var curSize = $j("#notSalePropSize").val();
			$j(".normalCheckBoxCls").each(function() {
				var curCheckBox = $j(this);
				// 初始化选中的值
				drawNoSalePropEditing4Type(curSize);
				curCheckBox.change(function() {
					drawNoSalePropEditing4Type(curSize);
				});
			});

		}
	}
}

function drawNoSalePropEditing4Type(curSize) {
	if (curSize == undefined || curSize == null) {
		curSize = 0;
	}
	var tempNum = curSize;
	// 去除所有
	$j(".hidBoxSpan").find("div[class='repNormalCheckBoxCls']").siblings().remove();

	$j(".hidBoxSpan")
			.find("div[class='repNormalCheckBoxCls']")
			.each(
					function() {
						var pid = $j(this).attr("pid");
						var pvid = $j(this).attr("pvid");
						var isCheck = false;
						$j(".normalCheckBoxCls").each(function() {
							if (pid == $j(this).attr("pid") && pvid == $j(this).attr("value")) {
								if ('checked' == $j(this).attr("checked")) {
									isCheck = true;
								}
								return;
							}
						});
						if (isCheck) {
							var parent = $j(this).parent();
							var ipropertiesHtml = "<input type='hidden' value='propertyId_toReplace' name='iProperties.propertyId'/>"
									+ "<input type='hidden' value='propertyValueId_toRepalce' name='iProperties.propertyValueId'/>"
									+ "<input type='hidden' value='' name='iProperties.id'><input type='hidden' value='' name='iProperties.propertyDisplayValue'><input type='hidden' value='' name='iProperties.createTime'><input type='hidden' value='' name='iProperties.modifyTime'><input type='hidden' value='' name='iProperties.version'><input type='hidden' value='' name='iProperties.itemId'><input type='hidden' value='' name='iProperties.picUrl'>";

							if (i18nOnOff) {
								for (var j = 0; j < i18nLangs.length; j++) {
									var i18nLang = i18nLangs[j];
									ipropertiesHtml = ipropertiesHtml
											+ "<input type='hidden' name='iProperties.propertyValue.values[" + tempNum
											+ "-" + j + "]' value=''/>";
									ipropertiesHtml = ipropertiesHtml
											+ "<input type='hidden' name='iProperties.propertyValue.langs[" + tempNum
											+ "-" + j + "]' value='" + i18nLang.key + "'/>";
								}
							} else {
								ipropertiesHtml = ipropertiesHtml
										+ "<input type='hidden'name='iProperties.propertyValue.value[" + tempNum
										+ "]' value=''/>";
							}
							// }
							ipropertiesHtml = ipropertiesHtml.replace("propertyId_toReplace", pid);
							ipropertiesHtml = ipropertiesHtml.replace("propertyValueId_toRepalce", pvid);
							parent.append(ipropertiesHtml);
							tempNum++;
						}
					});
}

function PropEditing4Typeinit() {
	var curSize = $j("#notSalePropSize").val();
	$j(".normalCheckBoxCls").each(function() {
		var curCheckBox = $j(this);
		// 初始化选中的值
		drawNoSalePropEditing4Type(curSize);
		curCheckBox.change(function() {
			drawNoSalePropEditing4Type(curSize);
		});
	});
}

// 商品颜色已选择值
function selestref() {
	var selestrefret = "";
	$j("input[name=propertyValueIds]").each(function() {
		if ($j(this).attr("checked")) {
			selestrefret += $j(this).attr("pvvalue") + ",";
		}
	});
	return selestrefret;
}

function selectcheck(URL) {
	var selestrefre = selestref();
	var json = {
		'itemcolor' : selestrefre
	};
	// 提交表單
	$j.ajax({
		url : URL,
		data : json,
		type : "post",
		success : function(data) {
			var dataObj = $j.parseJSON(data);
			$j("input[name=filtratecolor]").each(function() {
				var filtratecolor = $j(this).attr("tname");
				for (var i = 0; i < dataObj.length; i++) {
					if (filtratecolor == dataObj[i]) {
						$j(this).attr("checked", true);
						drawNoSalePropEditing4Type(null);
					}
				}
			});
		}
	});
}

var initSelectedPropertyIds = "";
$j(document).ready(function() {
	// 清空填充属性名称Array及属性idArray
	propertyArray.splice(0, propertyArray.length);
	var propertyIdsStr = $j("#propertyIdArray").val();
	propertyArray = eval("(" + propertyIdsStr + ")");

	propertyNameArray.splice(0, propertyNameArray.length);
	var propertyNamesStr = $j("#propertyNameArray").val();
	propertyNameArray = eval("(" + propertyNamesStr + ")");

	mustCheckArray.splice(0, mustCheckArray.length);
	var mustCheckArrayStr = $j("#mustCheckArray").val();
	mustCheckArray = eval("(" + mustCheckArrayStr + ")");
	// 自动编码
	showSkuTable(true);
	// -----------------start by sunchenbin----------------------
	initSelectedPropertyIds = getSelectedPropertyIds();
	// -----------------end by sunchenbin----------------------
	// 编码设置
	$j(".func-button.extension").on("click", function() {
		spChangedFlag = false;
		var allSaleInfo = $j(".saleInfo");
		var flag = false;
		if (allSaleInfo.length >= 2) {
			var customerPropSize = $j(".saleInfo").eq(0).find(".customerSelect").length;
			for (var idx = 0; idx < customerPropSize; idx++) {
				var next = 0;
				allSaleInfo.each(function(i, dom) {
					var me = $j(this);
					var val = me.find(".customerSelect").eq(idx).val();
					if (i > 0) {
						if (val == "") {
							flag = true;
						}
					}
					if (typeof (val) != "undefined" && val != null) {
						var len = val.split("||").length;
						me.find(".propertyValueInputs").eq(idx).val(val);
						if (val != "||" && i > 0) {
							if (len != next) {
								flag = true;
							}
						} else {
							next = len;
						}
					}

				});
				if (flag)
					break;
			}
		}
		var saleInfo = $j(this).parents(".saleInfo");
		if (flag) {
			saleInfo.find(".extensionTable").html("多语言属性设置数量不一致");
			return;
		}
		showSkuTable(false, saleInfo);
	});

	$j(".spCkb").each(function() {
		var curCheckBox = $j(this);

		curCheckBox.change(function() {
			spChangedFlag = true;
			clickFlag = false;
		});

	});

	$j(".spTa").each(function() {
		var ta = $j(this);

		ta.bind("change", function() {
			spChangedFlag = true;
			clickFlag = false;
		});
	});

	$j(".spCkb").click(function() {
		if ($j(this).attr("propertyid") == '2') {
			if ($j(this).is(':checked')) {
				selectcheck(itemcolorrefcheckURL);
			} else {
				$j("input[name=filtratecolor]").attr("checked", false);
				selectcheck(itemcolorrefcheckURL);
			}
		}
	});

	PropEditing4Typeinit();
	selectcheck(itemcolorrefcheckURL);

	// 添加商品属性表单验证方法
	var propertyValidator = new FormValidator('', 30, function() {

		// 判断复选框必选
		var att = "";
		$j.each(mustCheckArray, function(j, val) {
			var a = 0;
			$j("#notSalepropertySpace").find("[mustCheck='" + mustCheckArray[j] + "']").each(function(i, n) {
				if (this.checked) {
					a += 1;
				}
			});
			if (a == 0) {
				att += "【" + mustCheckArray[j] + "】,";
			}
		});
		if (att != "") {
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), att.substring(0, att.length - 1) + nps.i18n("MUST_SELECT"));
			return;
		}

		// 验证skuCode 是否重复
		var skuCodesArray = new Array();
		var skuIdArray = new Array();
		$j(".extensionTable").find(".dynamicInputNameSkuCode").each(function(i, n) {
			skuCodesArray[i] = $j(this).val();
			var skuId = $j(this).attr("skuId");
			if (skuId == null || skuId == "null") {
				skuIdArray[i] = -1;
			} else {
				skuIdArray[i] = skuId;
			}
		});

		if (spChangedFlag) {
			return nps.i18n("SALES_PROPERTY_CHANGED");
		}
		// 验证groupCodes开始==============
		var groupCodesArray = new Array();
		$j(".extensionTable").find(".dynamicInputNameGroupCode").each(function(i, n) {
			groupCodesArray[i] = $j(this).val();
		});

		for (var i = 0; i < groupCodesArray.length; i++) {
			var codeStrs = groupCodesArray[i];
			if (codeStrs != null && codeStrs != "") {
				if (codeStrs.indexOf(":") == -1) {
					return nps.i18n("GROUP_CODE_FORMAT_ERROR");
				}

				// groupCodes同一行的每个编码不可重复 但是不同行可以重复
				var strs = new Array(); // 定义一数组
				strs = codeStrs.split(":"); // 字符分割
				for (var j = 0; j < strs.length; j++) {
					curCode = strs[j];
					for (var k = 0; k < strs.length; k++) {
						if (k != j && curCode != "" && curCode == strs[k]) {
							return nps.i18n("GROUP_CODE_FORMAT_EQUAL");
							// return "同一个输入框的组合商品编码不能相同";
						}
					}
				}
			}
		}

		if (groupCodesArray.length > 0) {
			var json = {
				"gpCodes" : groupCodesArray
			};
			var data = loxia.syncXhr(validateGroupCodesCodesUrl, json, {
				type : 'post'
			});
			if (data.isSuccess == false) {
				return data.description;
			}
		}
		// 验证groupCodes结束==============

		// 验证是否至少填写了一个sku编码 PLEASE_INPUT_ONE_SKU_CODE
		var atLeastOneCode = false;

		for (var i = 0; i < skuCodesArray.length; i++) {
			var curCode = skuCodesArray[i];
			if (curCode != null && curCode != "") {
				atLeastOneCode = true;
			}
			for (var j = 0; j < skuCodesArray.length; j++) {
				if (i != j && curCode != "" && curCode == skuCodesArray[j]) {
					return nps.i18n("MERCHANT_CODING_EQUAL");
				}
			}

		}

		if (skuIdArray.length > 0) {
			var json = {
				"skuIds" : skuIdArray,
				"skuCodes" : skuCodesArray
			};
			var data = loxia.syncXhr(validateUpdateSkuCodesUrl, json, {
				type : 'post'
			});
			if (data.isSuccess == false) {
				return nps.i18n("SKU_CODE_REPEAT") + data.description;
			}
		}

		if (!atLeastOneCode) {
			return nps.i18n("PLEASE_INPUT_ONE_SKU_CODE");
		}

		return loxia.SUCCESS;
	});
	formValidateList.push(propertyValidator);
});

/**
 * 避免没有选的销售属性的propertyIds和propertyValueInputIds被提交，用于销售属性非必选时使用的
 * @author sunchenbin
 */
function removeNoSelectInputName() {
	$j("#propertySpace .ui-block-line").each(function() {
		// 判断当前这一条销售属性是否有任何一个值被选中过，如果有是true，没有是false
		var flag = $j(this).find("input[name='propertyValueIds']").is(':checked');
		// false的情况,删除input的name属性，让其进行form表单提交的时候不将该属性提交到后台
		if (!flag) {
			$j(this).find("input[name='propertyIds']").attr("name", "propertyIds_remove");
			$j(this).find("input[name='propertyValueInputIds']").attr("name", "propertyValueInputIds_remove");
		} else {
			// true的情况,将input的name属性加上，让其进行form表单提交的时候可以讲该属性提交到后台
			$j(this).find("input[name='propertyIds_remove']").attr("name", "propertyIds");
			$j(this).find("input[name='propertyValueInputIds_remove']").attr("name", "propertyValueInputIds");
		}
	});
}

/**
 * 获取当前已选过至少一个的销售属性的propertyId
 * @author sunchenbin
 * @returns 1,2,3,4,5,6
 */
function getSelectedPropertyIds() {
	var propertyIds = [];
	$j("#propertySpace .ui-block-line ").find("input[name='propertyIds']").each(function(i) {
		propertyIds[i] = $j(this).val();
	});
	return propertyIds.toString();
}