$j.extend(loxia.regional['zh-CN'], {
	"SYSTEM_ITEM_MESSAGE" : "提示信息",
	"UPDATEITEM_FAIL" : "修改商品库存失败",
	"UPDATEITEM_SUCCESS" : "修改商品库存成功",
	"MERCHANT_CODING" : "商家编码",
	"MERCHANT_GROUPCODE" : "组合商品编码",
	"MERCHANT_SALEPRICE" : "销售价",
	"MERCHANT_LISTPRICE" : "吊牌价",
	"CODING_TIPS" : "您需要选择所有的属性，才能组合成完整的规格信息!",
	"NOT_DATA_FORMAT" : "数据格式不正确",
	"STORE_MANAGER" : "库存管理",// add by hr 20140417
	"STORE_NUM" : "库存量"
});
var propertyArray = new Array();
var propertyNameArray = new Array();
var mustCheckArray = new Array();
var oldval = "";
var itemProperties = 0;
var itemId = 0;
// 绘制编码table
list = "";

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
			if (itemProperty.propertyId == propertyId && propertyValue == itemProperty.propertyValue) {
				return itemProperty;
			}
		}
	}
	return null;
}
function showSkuTable() {
	clickFlag = true;
	// console.log(skuList);
	$j("#exten").css("display", "block");
	$j("#extensionTable").html("");

	// 拿到 销售属性, 根据销售属性 确定 列
	var salesProperty = new Array();

	var salesPropertyIndex = 0;
	for (var i = 0; i < dynamicPropertyCommandListJsonStr.length; i++) {
		var dynamicPropertyCommand = dynamicPropertyCommandListJsonStr[i];
		// console.log(dynamicPropertyCommand);
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
	var sameNameFlag = false;

	for (var i = 0; i < salesProperty.length; i++) {
		var saleProperty = salesProperty[i];
		var propertyId = saleProperty.property.id;
		var selectedArray = new Array();
		var pInputArray = new Array();
		var index = 0;

		$j("input[propertyId='" + propertyId + "']").each(function() {

			if ($j(this).attr("checked")) {
				var propertyName = $j(this).attr("propertyName");
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

			$j("input[pid='" + propertyId + "']").each(function(index) {
				var selectdStr = "";
				for (var i = 0; i < pInputArray.length; i++) {
					selectdStr += pInputArray[i];
					selectdStr += ",";
				}
				selectdStr = selectdStr.substring(0, selectdStr.length - 1);
				$j(this).val(selectdStr);
			});
		}

		$j("textarea[propertyid='" + propertyId + "'][editingtype='5']").each(function() {
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

					$j("input[pid='" + propertyId + "']").each(function(index) {
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
	if (sameNameFlag) {
		clickFlag = false;
		html = nps.i18n("CUSTOM_PROPERTY_SAME");
		$j("#extensionTable").html(html);
		return;
	}
	if (propertyValueInputArray.length != propertyNameArray.length) {// 如果用户未把每个属性都填上值，则提示用户
		html = nps.i18n("CODING_TIPS");
		$j("#extensionTable").html(html);
	} else {// 画表格
		drawTableContent(propertyValueArray, propertyNameArray, propertyValueInputArray);
	}
	$j("#extensionTable").find("[loxiaType]").each(function(i, n) {
		// console.log("sss");
		loxia.initLoxiaWidget(this);
	});
}
function drawTableContent(propertyValueArray, propertyNameArray, propertyInputValueArray) {

	var skuInfoList = new Array();
	var tableHeader = "<tr>";
	for (var j = 0; j < propertyNameArray.length; j++) {// 动态生成 销售属性的列
		tableHeader += "<td style='width:150px'>" + propertyNameArray[j] + "</td>";
	}
	tableHeader += ("<td style='width:150px'>" + nps.i18n("MERCHANT_CODING") + "</td>" + "<td style='width:150px'>"
			+ nps.i18n("MERCHANT_SALEPRICE") + "</td>" + "<td style='width:150px'>" + nps.i18n("MERCHANT_LISTPRICE")
			+ "</td>" + "<td style='width:150px'>" + nps.i18n("STORE_NUM") + "</td>" + "</tr>");

	var tableContent = "";
	if (propertyValueArray.length > 0) {
		if (propertyValueArray.length == 1) {// 销售属性只有1个
			for (var i = 0; i < propertyValueArray[0].length; i++) {
				var availab = 0;
				var dynamicStr = "<td style='width:150px'>" + propertyValueArray[0][i][0] + "</td>";
				var codesName = getDynamicInputName("code", propertyValueArray[0][i], null);
				var salePriceName = getDynamicInputName("salePrice", propertyValueArray[0][i], null);
				var listPriceName = getDynamicInputName("listPrice", propertyValueArray[0][i], null);
				var idName = getDynamicInputName("id", propertyValueArray[0][i], null);
				var tmpArray = new Array();
				tmpArray[0] = propertyValueArray[0][i];
				var sku = getSkuInfoByProperyValueArray(tmpArray);
				var proHtml = "<input type='hidden' name='idNameToReplace' value = 'idValueTOReplace' /><td style='width:150px'>CODE_VALUE<input type='hidden' name='extentionCode' value = 'extentionCode_VALUE' /></td>"
						+ "<td style='width:150px'>salePrices_value</td>"
						+ "<td style='width:150px'>listPrices_value</td>"
						+ "<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='availableQty' mandatory='true' name='availableQty' decimal='0' min='0' loxiaType='number' value='availableQty_value'/></td>";

				proHtml = proHtml.replace('codesNameToReplace', codesName);
				proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
				proHtml = proHtml.replace('listPriceToReplace', listPriceName);
				proHtml = proHtml.replace('idNameToReplace', idName);

				var salePrice = $j("#salePrice").val();
				var listPrice = $j("#listPrice").val();
				var code = "";
				var skuId = null;
				if (sku != null) {
					salePrice = ((sku.salePrice == null || sku.salePrice == "") ? salePrice : sku.salePrice);
					listPrice = ((sku.listPrice == null || sku.listPrice == "") ? listPrice : sku.listPrice);
					code = (sku.outid == null || sku.outid == "") ? code : sku.outid;
					skuId = (sku.id == null || sku.id == "") ? skuId : sku.id;
				}
				for (var m = 0; m < storeNumStr.length; m++) {
					if (code == storeNumStr[m].extentionCode) {
						availab = storeNumStr[m].availableQty;
						break;
					}
				}

				if (!isNotNullOrEmpty(listPrice)) {
					listPrice = "";
				}
				proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
				proHtml = proHtml.replace('CODE_VALUE', code);
				proHtml = proHtml.replace('salePrices_value', salePrice);
				proHtml = proHtml.replace('listPrices_value', listPrice);
				proHtml = proHtml.replace('availableQty_value', availab);
				proHtml = proHtml.replace('extentionCode_VALUE', code);
				tableContent += ("<tr>" + dynamicStr + proHtml + "</tr>");
			}

		} else {
			// 多个销售属性的展示
			tableContent = buildTable(null, propertyValueArray, 0);
		}
		$j("#jsonSku").val(JSON.stringify(skuInfoList));
	} else {
		var availab = 0;
		var sku = skuList[0];
		var proHtml = "<input type='hidden' name='skuId' value = 'idValueTOReplace' /><td style='width:150px'>CODE_VALUE<input type='hidden' name='extentionCode' value = 'extentionCode_VALUE' /></td>"
				+ "<td style='width:150px'>salePrices_value</td>"
				+ "<td style='width:150px'>listPrices_value</td>"
				+ "<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='availableQty' name='availableQty' decimal='0' min='0' loxiaType='number' value='availableQty_value'/></td>";

		var salePrice = $j("#salePrice").val();
		var listPrice = $j("#listPrice").val();
		var code = "";
		var skuId = null;
		if (sku != null) {
			salePrice = ((sku.salePrice == null || sku.salePrice == "") ? salePrice : sku.salePrice);
			listPrice = ((sku.listPrice == null || sku.listPrice == "") ? listPrice : sku.listPrice);
			code = (sku.outid == null || sku.outid == "") ? code : sku.outid;
			skuId = (sku.id == null || sku.id == "") ? skuId : sku.id;
		}
		for (var m = 0; m < storeNumStr.length; m++) {
			if (code == storeNumStr[m].extentionCode) {
				availab = storeNumStr[m].availableQty;
				break;
			}
		}

		if (!isNotNullOrEmpty(listPrice)) {
			listPrice = "";
		}
		proHtml = proHtml.replace('CODE_VALUE', code);
		proHtml = proHtml.replace('salePrices_value', salePrice);
		proHtml = proHtml.replace('listPrices_value', listPrice);
		proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
		proHtml = proHtml.replace('availableQty_value', availab);
		proHtml = proHtml.replace('extentionCode_VALUE', code);
		tableContent += ("<tr>" + proHtml + "</tr>");
		list = "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}
	var html = tableHeader + tableContent;
	$j("#extensionTable").html(html);
}

// --------------------------------多个销售属性循环start-------------------------------
function buildTable(table, data, dataRowIndex) {
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
				var availab = 0;
				htmlLine += "<td style='width:150px'>" + table[i][j][0] + "</td> ";
				arrays.push(table[i][j]);
			}

			var idName = getMoreDynamicInputName("id", arrays);
			var codesName = getMoreDynamicInputName("code", arrays);
			var salePriceName = getMoreDynamicInputName("salePrice", arrays);
			var listPriceName = getMoreDynamicInputName("listPrice", arrays);
			var tmpArray = clone(arrays);

			var sku = getSkuInfoByProperyValueArray(tmpArray);

			var proHtml = "<input type='hidden' name='idNameToReplace' value = 'idValueTOReplace' /><td style='width:150px'>CODE_VALUE<input type='hidden' name='extentionCode' value = 'extentionCode_VALUE'/></td>"
					+ "<td style='width:150px'>salePrices_value</td>"
					+ "<td style='width:150px'>listPrices_value</td>"
					+ "<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='availableQty' mandatory='true' name='availableQty' decimal='0' min='0' loxiaType='number' value='availableQty_value'/></td>";

			proHtml = proHtml.replace('idNameToReplace', idName);
			proHtml = proHtml.replace('codesNameToReplace', codesName);
			proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
			proHtml = proHtml.replace('listPriceToReplace', listPriceName);
			var salePrice = $j("#salePrice").val();
			var listPrice = $j("#listPrice").val();
			var code = "";
			var skuId = null;
			if (sku != null) {
				salePrice = ((sku.salePrice == null || sku.salePrice == "") ? salePrice : sku.salePrice);
				listPrice = ((sku.listPrice == null || sku.listPrice == "") ? listPrice : sku.listPrice);
				code = (sku.outid == null || sku.outid == "") ? code : sku.outid;
				skuId = (sku.id == null || sku.id == "") ? skuId : sku.id;
			}

			for (var m = 0; m < storeNumStr.length; m++) {
				if (code == storeNumStr[m].extentionCode) {
					availab = storeNumStr[m].availableQty;
					break;
				}
			}

			if (!isNotNullOrEmpty(listPrice)) {
				listPrice = "";
			}

			proHtml = proHtml.replace('CODE_VALUE', code);
			proHtml = proHtml.replace('salePrices_value', salePrice);
			proHtml = proHtml.replace('listPrices_value', listPrice);
			proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
			proHtml = proHtml.replace('availableQty_value', availab);
			proHtml = proHtml.replace('extentionCode_VALUE', code);

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
	return buildTable(result, data, ++dataRowIndex);
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
			if (itemProperty.propertyId == propertyId && propertyValue == itemProperty.propertyValue) {
				return itemProperty;
			}
		}
	}
	return null;
}

/**
 * 判断字符是不是空
 * @param str
 */
function isNotNullOrEmpty(str) {
	if (str != undefined && str != null && str != "" && str != "undefined") {
		return true;
	} else {
		return false;
	}
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
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
	showSkuTable();
	// 保存商品库存量
	$j(".button.orange.submit").click(function() {
		nps.submitForm('itemForm', {
			mode : 'async',
			successHandler : function(data) {
				if (data.isSuccess == true) {
					nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("UPDATEITEM_SUCCESS"));
					window.location.href = window.location.href;
					return;
				} else {
					return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("UPDATEITEM_FAIL"));
				}
			}
		});

	});
	// 返回
	$j(".button.return").on("click", function() {
		window.location.href = base + "/item/itemList.htm";
	});

	loxia.initContext($j(".ui-block "));

});
