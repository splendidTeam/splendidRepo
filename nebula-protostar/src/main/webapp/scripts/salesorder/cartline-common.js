var validedStr = '<span class="bc-num-addmini mr5 miniQuantity">-</span><input type="text" class="default-text quantityNo" id="SHOPPINECART_ID_REPLACE" value="ITEM_QUANTITY" skuId = "SKU_ID" skuCode = "SKU_CODE" limited="LIMITED_FLAG" limitedQuantity="LIMITED_QUANTITY" /><span class="bc-num-addmini ml5 addQuantity">+</span>';
var inValidStr = '<p class="red">该商品已下架</p>';

//判断是购物车还是订单行显示
var srcType = true;

/**
 * 
 * @param lines
 * @param type
 */
function getCartLine(lines, type){
	
	srcType = type;
	
	var itemLinesHtml = "";
	/*
	 * 获取lineGroup对应的促销类型
	 */
	var linePromotionTypeMap = getLinePromotionType(lines);
	//gift suit normalPromotion normal
	
	/*
	 *赠品行(整单)
	 */ 
	itemLinesHtml = getAllOrderGiftHtml(lines) + itemLinesHtml;
	
	/*
	 * 普通行
	 */
	itemLinesHtml += generateShopCartLinesHtml(linePromotionTypeMap['normal'],'normal');
	
	/*
	 * 套餐
	 */
	itemLinesHtml += generateShopCartLinesHtml(linePromotionTypeMap['suit'],'suit');
	/*
	 * 
	 * 普通促销
	 */
	itemLinesHtml += generateShopCartLinesHtml(linePromotionTypeMap['normalPromotion'],'normalPromotion');
	/*
	 * 赠品行(商品行)
	 */
	itemLinesHtml += generateShopCartLinesHtml(linePromotionTypeMap['gift'],'gift');
	groupGiftMap = generateGiftLinesGroupMap(linePromotionTypeMap['gift']);
	var resultMap = {};
	resultMap['groupGiftMap'] = groupGiftMap;
	resultMap['itemLinesHtml'] = itemLinesHtml;
	return resultMap;
}

/**
 * 生成赠品行和lineGroup的map
 * 
 * @return
 */
function generateGiftLinesGroupMap(lines){
	var retMap = {};
	if(isNotNullOrEmpty(lines)){
		for(var i=0;i<lines.length;i++){
			var line = lines[i];
			var linesArr = retMap[line.lineGroup];
			if(!linesArr){
				linesArr = new Array(0);
			}
			linesArr.push(line);
			retMap[line.lineGroup] = linesArr;
		}
	}
	return retMap;
}


/**
 * 生成相应的html
 * @param pType: gift suit  normalPromotion normal
 * @return
 */
function generateShopCartLinesHtml(lines,pType){
	var itemLinesHtml = "";
	var isValidStr = "";
	
	if('gift' == pType) {
		//赠品行
		var giftCartMap = groupCartLineByLineGroup(lines);
		for(var key in giftCartMap) {
			itemLinesHtml += getGiftCartLine(giftCartMap[key],validedStr,inValidStr);
		}
		
	} else if('suit' == pType) { 
		// 套装(暂不考虑)
		var giftCartMap = groupCartLineByLineGroup(lines);
		for(var key in giftCartMap) {
			itemLinesHtml += getSuitCartLine(giftCartMap[key], validedStr, inValidStr);
		}
		
	} else if('normalPromotion' == pType) { 
		//正常活动
		var giftCartMap = groupCartLineByLineGroup(lines);
		for(var key in giftCartMap) {
			itemLinesHtml += getNormalPromotionCartLine(giftCartMap[key], validedStr, inValidStr);
		}
	} else if('normal' == pType) { 
		// 正常行
		for(var i =0; i<lines.length;i++){
			var line = lines[i];
			isValidStr = validSku(line, validedStr, inValidStr);
			var curLineHtml = "";
		    curLineHtml = getNormalCartLine(line,isValidStr);
			itemLinesHtml += curLineHtml;
		}
	}
	
	return itemLinesHtml;
}

/**
 * 生成正常活动的商品行
 * @param lines
 * @param validedStr
 * @param inValidStr
 */
function getNormalPromotionCartLine(lines, validedStr, inValidStr){
	var isFirst = true;
	var curLineHtml = '';
	var addArr = new Array();
	addArr.push('<tr class="buy-send">');
	addArr.push('<td colspan="5">');
	addArr.push('<span class="send-pro">PROMOTION_LOGO_MARK</span>');
	addArr.push('<span class="send-pro-word">PROMOTION_TITLE</span>');
	addArr.push('</td>');
	addArr.push('</tr>');
	
	var addTrHtml = addArr.join("");
	var itemPromHtml = '';
	for(var i=0; i<lines.length; i++){
		var line = lines[i];
		// caption line
		if(line.captionLine){
			// 商品参加的促销活动
			var promotionList = line.promotionList; 
			if(isFirst && promotionList && promotionList.length > 0){
				for(var j in promotionList){
					promLineHtml = addTrHtml;
					var promotion = promotionList[j];
					promLineHtml = promLineHtml.replace('PROMOTION_LOGO_MARK', promotionLogoMarkMap[promotion.logo]);
					promLineHtml = promLineHtml.replace('PROMOTION_TITLE', promotion.promotionName);
					itemPromHtml += promLineHtml;
				}
				isFirst = false;
			}
			continue;
		}
		
		curLineHtml += getNormalCartLine(line, validSku(line, validedStr, inValidStr));
	}
	curLineHtml = itemPromHtml + curLineHtml;
	return curLineHtml;
}
/**
 * 根据订单行lineGroup进行分组
 * 
 * @return
 */
function groupCartLineByLineGroup(lines) {
	var retMap = {};
	for(var i=0;i<lines.length;i++){
		var line = lines[i];
		var lineGroup = line.lineGroup;
		var addArr = null;
		if(retMap[lineGroup]){
			addArr = retMap[lineGroup];
		} else {
			addArr = new Array();
		}
		addArr.push(line);
		retMap[lineGroup] = addArr;
	}
	return retMap;
}

/*
 * 获取购物车行的促销类型
 * key: gift suit normalPromotion normal
 * val: lines
 */
function getLinePromotionType(lines){
	//var retPromotionTypeMap = {};
	var proTypeMap = {};
	var giftLines = new Array();
	var suitLines = new Array();
	var normalProLine = new Array();
	var normalLine = new Array();
	var typeTemp = {};
	if(isNotNullOrEmpty(lines)){
		var lineGroupMap = {};
		
		for(var i in lines){
			var line = lines[i];
			var lineGroup = line.lineGroup;
			
			if(line.suitLine){
				//套装
				suitLines = typeTemp['suit'];
				suitLines.push(line);
				continue;
			}
			
			if(lineGroup && lineGroup != 0){
				var lineArray = lineGroupMap[lineGroup];
				if(!lineArray){
					lineArray = new Array();
				}
				lineArray.push(line);
				lineGroupMap[lineGroup] = lineArray;
			}else{
				// 常规行
				normalLine.push(line);
			}
		}
		
		
		for(var key in lineGroupMap){
			var isGift = false;
			var lineArray = lineGroupMap[key];
			for(var i in lineArray){
				if(lineArray[i].gift){
					isGift = true;
					break;
				}
			}
			
			if(isGift){
				giftLines = giftLines.concat(lineArray);
			}else{
				normalProLine = normalProLine.concat(lineArray);
			}
		}
	}
	proTypeMap['gift'] = giftLines;
	proTypeMap['suit'] = suitLines;
	proTypeMap['normalPromotion'] = normalProLine;
	proTypeMap['normal'] = normalLine;
	
	return proTypeMap;
}

/*
 * 生成整单赠品行
 */
function getAllOrderGiftHtml(lines){
	var allOrderGiftlines = new Array();
	var captionLineArr = new Array();
	var lineGroupMap = {};
	for(var i in lines){
		var line = lines[i];
		if(line.giftType == '0' && line.gift){
			lineGroupMap[line.lineGroup]=1;
			allOrderGiftlines.push(line);
		}else if(line.captionLine){
			captionLineArr.push(line);
		}
	}
	var allOrderGiftlineHtml = '';
	if(allOrderGiftlines && allOrderGiftlines.length > 0){
		// find caption line
		for(var key in lineGroupMap){
			var lineGroup = key;
			for(var i in captionLineArr){
				var captionLines = captionLineArr[i];
				if(captionLines.lineGroup == lineGroup && captionLines.captionLine){
					allOrderGiftlines.push(captionLines);
				}
			}
		}
		allOrderGiftlineHtml = getAllOrderGiftCartLine(allOrderGiftlines, validedStr, inValidStr);
	}
	return allOrderGiftlineHtml;
}

/*
 * 生成基于行的里面的购物车行(整单礼品)
 * 
 * @return
 */
function getAllOrderGiftCartLine(lines,validedStr,inValidStr){
	var currCartLineHtml = '';
	var groupMap = generateGiftLinesGroupMap(lines);
	
	for(var key in groupMap){
		var tmpLines = groupMap[key];
		var addArr = new Array();
		var mainLines = new Array();
		var giftLines = new Array();
		var captionLine = '';
		for(var i=0;i<tmpLines.length;i++){
			var line = tmpLines[i];
			//var giftType = line.giftType;
			if(line.captionLine){
				captionLine = line;
				continue;
			}
			if(line.gift){
				giftLines.push(line);
			} else {
				mainLines.push(line);
			}
		}
		
		var giftMap = groupGiftLinesByPromotionIds(giftLines);
		// 礼品显示类型，0不需要用户选择，1需要用户选择
		
		//赠品促销 主赠品显示
		var lineGroup = '';
		var giftPromotionArr = new Array();
		var giftHtmlMap = {};
		for(var key in giftMap){
			var hasGift = false;
			addArr.push('<tr class="buy-send-pro">');
			addArr.push('<td colspan="5">');
			for(var i in giftMap[key]){
				var line= giftLines[i];
				lineGroup = line.lineGroup;
				var giftChoiceTypeTmp = line.giftChoiceType;
				if(giftChoiceTypeTmp == '1'){
					giftPromotionArr.push(line.promotionIds);
				}
				if(line.settlementState == '1' || giftChoiceTypeTmp == '0'){// 是否选中的赠品 或用户不可以参加选择的赠品
					addArr.push('<div class="common-line pt5">');
					addArr.push('<span class="fLeft">[赠品] '+line.itemName+' '+getSalesPropShow(line.skuPropertys)+'</span>');
					addArr.push('<span class="ml10">×'+line.quantity+'</span>');
					if(!line.valid){
						if(line.validType == 2){
							
							// 显示库存数
							if(line.stock > 0){
								addArr.push('<span class="ml10 red">库存仅存'+line.stock+'件</span>');
							}
							else{
								addArr.push('<span class="ml10 red">库存不足</span>');
							}
						}
						else if(line.validType == 1){
							addArr.push('<span class="ml10 red">该商品已下架</span>');
						}else{
							addArr.push('<span class="ml10 red">该商品已下架</span>');
						}
					}
					
					if(giftChoiceTypeTmp != '0' && !srcType){
						addArr.push('<a class="fRight removeGift" line="'+line.id+'" href="javascript:void(0);">删除</a>');
					}
					addArr.push('</div>');
					hasGift = true;
				}
			}
			addArr.push('</td>');
			addArr.push('</tr>');
			addGiftHtml = addArr.join('');
			if(!hasGift){
				addGiftHtml = '';
			}
			giftHtmlMap[key] = addGiftHtml;
		}
		//赠品促销 主商品显示
		currCartLineHtml = currCartLineHtml + getGiftCartMainLine(mainLines, giftHtmlMap, lineGroup, captionLine, giftPromotionArr);
	}
	
	return currCartLineHtml;
}

/**
 * 生成赠品的主商品行
 */
function getGiftCartMainLine(lines, giftHtmlMap, lineGroup, captionLine, giftPromotionArr){
	var addArr = new Array();
	addArr.push('<tr class="buy-send">');
	addArr.push('<td colspan="5">');
	addArr.push('<span class="send-pro">CATPIOTNLINE_LOGO_MARK</span>');// 
	addArr.push('<span class="send-pro-word">CATPIOTNLINE_TITLE</span>');
	addArr.push('GIFT_CHOICE_TYPE_HTML_REPLACE');
	addArr.push('</td>');
	addArr.push('</tr>');
	var addTr = addArr.join('');
	
	// 购物车订单行显示区别
	var giftChoiceTypeHtml = "";
	if(!srcType){
		giftChoiceTypeHtml  = '<a href="javascript:void(0);" class="wic-right-button ml30 chooseGift" data="GIFT_LINEGROUP" promotionIds="PROMOTION_IDS_REPLACE">赠品选择<span>&nbsp;</span></a>';
	}
	
	// 选择赠品行
	var promGiftHtml = '';
	if(captionLine){
		var promotionList = captionLine.promotionList;
		for(var i in promotionList){
			var promIsGift = false;
			var promotion = promotionList[i];
			var tmpCaptionLineHtml = addTr;
			tmpCaptionLineHtml = tmpCaptionLineHtml.replace('CATPIOTNLINE_LOGO_MARK', promotionLogoMarkMap[promotion.logo]);
			tmpCaptionLineHtml = tmpCaptionLineHtml.replace('CATPIOTNLINE_TITLE', promotion.promotionName);
			for(var j in giftPromotionArr){
				if(giftPromotionArr[j] == promotion.promotionId){
					promIsGift = true;
					break;
				}
			}
			
			if(promIsGift){
				tmpCaptionLineHtml = tmpCaptionLineHtml.replace('GIFT_CHOICE_TYPE_HTML_REPLACE', giftChoiceTypeHtml);
				tmpCaptionLineHtml = tmpCaptionLineHtml.replace('GIFT_LINEGROUP', lineGroup);
				tmpCaptionLineHtml = tmpCaptionLineHtml.replace('PROMOTION_IDS_REPLACE', promotion.promotionId);
			}else{
				tmpCaptionLineHtml = tmpCaptionLineHtml.replace('GIFT_CHOICE_TYPE_HTML_REPLACE', '');
			}
			tmpCaptionLineHtml += giftHtmlMap[promotion.promotionId];
			promGiftHtml += tmpCaptionLineHtml;
		}
	}
	
	var curLineHtml = "";
	for(var i=0;i<lines.length;i++){
		var line = lines[i];
		curLineHtml += getNormalCartLine(line, validSku(line, validedStr, inValidStr));
	}
//	curLineHtml = promGiftHtml + addGiftHtml + curLineHtml;
	curLineHtml = promGiftHtml + curLineHtml;
	return curLineHtml;
}

/*
 * 获取普通的购物车行
 */
function getNormalCartLine(line,isValidStr){
	
	// 购物车订单行显示区别
	var qtyHtml ="";
	if(srcType){
		qtyHtml ='IS_VALID_STR'+	
		'<p class="pt5"><a href="javascript:void(0);" class="addFavorites" itemId="ITEM_ID">加入收藏夹</a></p>'+  
		' <p class="pt5"><a href="javascript:void(0);" skuCode="SKU_CODE" id="SHOPPINECART_ID_REPLACE" class="delCartLine">删除</a></p>';
	}else{
		var isValid = line.valid;
		if(isValid){
			qtyHtml ="ITEM_QUANTITY";
		} else {
			qtyHtml ="IS_VALID_STR";
		}
	}
	
	
	var singleItemLineHtml = 
		'<tr>'+
		'<td><a href="ITEM_URL"><img src="ITEM_IMG" width="75" height="75"/></a></td>'+ 
		'<td class="left">'+
		'<p><a href="ITEM_URL">ITEM_NAME</a></p>'+	 
		'<p class="pt5">ITEM_PROPERTIES_VAL</p>'+	 
		'</td>'+
		'<td><span class="bold">￥ITEM_SALE_PRICE</span></td>'+
		'<td>'+
		qtyHtml+
		'</td>'+
		'<td><span class="bold red">￥ITEM_LINE_TOTAL</span></td>'+
		'<td><a id="removeShoppingCartLine" extentionCode="'+line.extentionCode+'"  href="javascript:void(0)"  class="func-button ml5">删除</a></td>'+
		'</tr>';
	var curLineHtml =singleItemLineHtml;
//	var code = line.productCode;	
//	var itemUrl =getItemUrlByCode(code);
	
	var itemImg = customBaseUrl+processImgUrl(line.itemPic,smallSize);
	
	var itemUrl = frontendBaseUrl+pdpPrefix.replace(repStr,line.productCode);
	
	var itemName = line.itemName;
	var itemSalePrice = line.salePrice;
	var itemListPrice = line.listPrice;
	var itemQuantity = line.quantity;
	var itemLineTotal = line.subTotalAmt;
	var itemId = line.itemId;
//var itemImg = processImgUrl(line.itemPic,SMALL_IMG_SIZE);
//var code = line.productCode;
	var limitedMark = line.limitMark;
	var limitedQuantity = line.limit;
	var skuId = line.skuId;
	var skuCode = line.extentionCode;
	var propertyArr =line.skuPropertys;
	if(!isNotNullOrEmpty(limitedMark)){
		limitedMark = DEFAULT_LIMIT_FLAG;
	}
	var propertiesStr = "";
	if(isNotNullOrEmpty(propertyArr)){
		var temp = "";
		for(var k =0;k<propertyArr.length;k++){
			var propertyName = propertyArr[k].pName;
			var propertyValue = propertyArr[k].value;
			temp +=propertyName +" : "+propertyValue;
			if(k<propertyArr.length-1){
				temp +=", ";
			}
		}
		
		propertiesStr = temp;
	}
	
	curLineHtml=curLineHtml.replace("IS_VALID_STR", isValidStr);
	curLineHtml=curLineHtml.replace(/ITEM_ID/g, itemId);
	curLineHtml=curLineHtml.replace(/ITEM_URL/g, itemUrl);
	curLineHtml=curLineHtml.replace("ITEM_NAME", itemName);
	curLineHtml=curLineHtml.replace("ITEM_IMG", itemImg);
	curLineHtml=curLineHtml.replace("ITEM_SALE_PRICE", itemSalePrice);
	curLineHtml=curLineHtml.replace("ITEM_LIST_PRICE", itemListPrice);
	curLineHtml=curLineHtml.replace("ITEM_QUANTITY", itemQuantity);
	curLineHtml=curLineHtml.replace("ITEM_LINE_TOTAL", itemLineTotal);
	curLineHtml=curLineHtml.replace("LIMITED_FLAG", limitedMark);
	curLineHtml=curLineHtml.replace("LIMITED_FLAG", limitedMark);
	curLineHtml=curLineHtml.replace("LIMITED_FLAG", limitedMark);
	curLineHtml=curLineHtml.replace(/SKU_ID/g, skuId);
	curLineHtml=curLineHtml.replace(/SKU_CODE/g, skuCode);
	curLineHtml=curLineHtml.replace('ITEM_PROPERTIES_VAL', propertiesStr);
	curLineHtml=curLineHtml.replace(/SHOPPINECART_ID_REPLACE/g, line.id);
	
	//如果限购
	if(limitedMark!=NON_LIMIT_FLAG){
		curLineHtml=curLineHtml.replace("LIMITED_QUANTITY", limitedQuantity);
	}else{
		curLineHtml=curLineHtml.replace("LIMITED_QUANTITY", -1);
	}
	return curLineHtml;
}

/**
 * 生成套装的商品行(暂不考虑)
 */
function getSuitCartLine(lines, validedStr, inValidStr){
	
	return "";
}
/*
 * 生成基于行的里面的购物车行
 * 
 * @return
 */
function getGiftCartLine(lines,validedStr,inValidStr){
	var mainLines = new Array();
	var giftLines = new Array();
	var captionLine = '';
	for(var i=0;i<lines.length;i++){
		var line = lines[i];
		var giftType = line.giftType;
		if(line.gift && giftType == '0' && !line.captionLine){
			return;
		}else if(line.captionLine){
			captionLine = line;
			continue;
		}
		if(line.gift){
			giftLines.push(line);
		} else {
			mainLines.push(line);
		}
	}
	
	var giftMap = groupGiftLinesByPromotionIds(giftLines);
	

	//是否基于购物车行的
	//var baseLine = false;
	// 礼品显示类型，0不需要用户选择，1需要用户选择
	//var giftChoiceType = '0';
	
	//赠品促销 主赠品显示
	var lineGroup = '';
	// 是否在赠品行
	var giftPromotionArr = new Array();
	var giftHtmlMap = {};
	for(var key in giftMap){
		var hasGift = false;
		var addArr = new Array();
		addArr.push('<tr class="buy-send-pro">');
		addArr.push('<td colspan="5">');
		for(var i in giftMap[key]){
			var line= giftMap[key][i];
			lineGroup = line.lineGroup;
			var giftChoiceTypeTmp = line.giftChoiceType;
			if(giftChoiceTypeTmp == '1'){
				giftPromotionArr.push(line.promotionIds);
			}
			if(line.settlementState == '1' || giftChoiceTypeTmp == '0'){// 是否选中的赠品 或用户不可以参加选择的赠品
				addArr.push('<div class="common-line pt5">');
				addArr.push('<span class="fLeft">[赠品] '+line.itemName+' '+getSalesPropShow(line.skuPropertys)+'</span>');
				addArr.push('<span class="ml10">×'+line.quantity+'</span>');
				
				if(!line.valid){
					if(line.validType == 2){
						// 显示库存数
						if(line.stock > 0){
							addArr.push('<span class="ml10 red">库存仅存'+line.stock+'件</span>');
						}else{
							addArr.push('<span class="ml10 red">库存不足</span>');
						}
					}
					else if(line.validType == 1){
						addArr.push('<span class="ml10 red">该商品已下架</span>');
					}else{
						addArr.push('<span class="ml10 red">该商品已下架</span>');
					}
				}
				
				// 购物车订单行显示区别
				if(giftChoiceTypeTmp != '0' && !srcType){
					addArr.push('<a class="fRight removeGift" line="'+line.id+'" href="javascript:void(0);">删除</a>');
				}
				addArr.push('</div>');
				hasGift = true;
			}
		}
		addArr.push('</td>');
		addArr.push('</tr>');
		if(!hasGift){
			giftHtmlMap[key] = '';
		}else{
			giftHtmlMap[key] = addArr.join('');
		}
	}
	
	//赠品促销 主商品显示
	//var currCartLineHtml = getGiftCartMainLine(mainLines, addGiftHtml, lineGroup, captionLine, giftMap, giftPromotionArr);
	var currCartLineHtml = getGiftCartMainLine(mainLines, giftHtmlMap, lineGroup, captionLine, giftPromotionArr);
	return currCartLineHtml;
}

/**
 * 通过promotionIds给赠品行分组
 * @param giftLines
 */
function groupGiftLinesByPromotionIds(giftLines){
	var giftMap = {};
	var groupGiftArr = null;
	for(var i in giftLines){
		var giftLine = giftLines[i];
		groupGiftArr = giftMap[giftLine.promotionIds];
		if(!groupGiftArr){
			groupGiftArr = new Array;
		}
		groupGiftArr.push(giftLine);
		giftMap[giftLine.promotionIds] = groupGiftArr;
	}
	return giftMap;
}

/**
 * 验证sku是否已下架
 * @param line
 */
function validSku(line, validedStr, inValidStr){
	var isValid = line.valid;
	var validType = line.validType;
	if(isValid){// 可用
		return validedStr;
	} else { // 不可用:下架(item下架, sku下架)或 没有库存
		hasInvalidSku = true;
		if(validType == 1 || validType == 3){// 下架
			inValidStr = '<p class="red">该商品已下架</p>';
		}else if(validType == 2){// 没有库存
			inValidStr = '<p class="red">库存不足</p>';
		}
		return inValidStr;
	}
}

/**
 * 获取销售属性的显示
 * 
 * @return
 */
function getSalesPropShow(skuProps){
    if(!skuProps){
    	return "";
    }
    var arr = new Array();
    for(var i=0;i<skuProps.length;i++){
    	var prop = skuProps[i];
    	arr.push(prop.pName+":"+prop.value);
    }
	return arr.join(",");
}


