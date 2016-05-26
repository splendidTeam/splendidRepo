$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"ITEM_UPDATE_CODE_ENBLE":"商品编码不可用,为您恢复到原始的店铺编码",
	"ITEM_UPDATE_CODE_ABLE":"商品编码可用",
	"ITEM_CODE_VALID_FAIL":itemCodeValidMsg,
	"UPDATEITEM_FAIL":"修改商品失败",
	"UPDATEITEM_SUCCESS":"修改商品成功",
	"MERCHANT_CODING":"商家编码",
	"MERCHANT_SALEPRICE":"销售价",
	"MERCHANT_LISTPRICE":"吊牌价",
	"CODING_TIPS":"您需要选择所有的属性，才能组合成完整的规格信息!",
	"DELETE_THIS_CATEGORY":"删除此分类",
	"SET_THIS_DEFCATEGORY":"设为默认",
	"THIS_BE_DEFCATEGORY":"默认",
	"MUST_SELECT":"为必选项",
	"NOT_DATA_FORMAT":"数据格式不正确",
	"SELECT_COLOR_PROPERTY":"请选择颜色属性",
	"WRITE_ALL_INFO":"请将信息填写完整",
	"PLEASE_SELECT_PROPERTY":"请选择销售属性",
	"IMAGE_SELECT_COLOR_PROPERTY":"颜色属性",
	"PLEASE_SELECT_DEFAULT_CATEGORY":"请为商品选择默认分类",
	"NOT_REPEATEDLY_RELEVANCE_CATEGORY":"同一商品不可以多次关联同一分类",
	"SALEPRICE_OUT_OF_RANGE":"销售价超出sku设置的销售价区间",
	"PLEASE_INPUT_SALEPRICES":"请输入sku销售价格",
	"LISTPRICE_OUT_OF_RANGE":"挂牌价超出sku设置的吊牌价区间",
	"PLEASE_INPUT_LISTPRICE":"请输入sku吊牌价格",
	"MERCHANT_CODING_EQUAL":"商家编码相同",
	"SKU_CODE_REPEAT":"以下商家编码已经存在：",
	"CUSTOM_PROPERTY_SAME":"填写的自定义属性相同",
	"SALES_PROPERTY_CHANGED":"销售属性已经更新，但是没有重新进行编码设置",
	"PLEASE_INPUT_ONE_SKU_CODE":"请输入至少一个sku编码",
	"PLEASE_SET_DEF_CATEGORY":"请设定默认分类"

});
var propertyArray  = new Array();
var propertyNameArray  = new Array();
var mustCheckArray  = new Array();
var oldval="";

var itemProperties = 0;

var existSaleProp = true;
var spChangedFlag = false;

var itemId = 0;

//延迟初始化分类树
var categoryTreeInited = false;


//url

var findItemImageByItemPropAndItemIdUrl = base + '/item/findItemImageByItemPropAndItemId.json';
var updateItemInfoLastSelectPropertyIdByItemIdUrl = base + '/item/findItemImageByItemPropOrItemIdUrl.json';
var updateItemInfoLastSelectPropertyValueIdByItemIdUrl = base + '/item/updateItemInfoLSPVIdByItemId.json';
var manageImagUrl= base +'/i18n/itemImage/toAddItemImage.htm?itemId=';
var validateUpdateSkuCodesUrl = base +'/item/validateUpdateSkuCodes.json';


var categorySetting = {
		check: {
			enable: true
		},
		view: {
			dblClickExpand: false,
			showIcon:false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: onClick1,
			onCheck: onCheck1,
		}
	};



//**********************category input  start********************
function onClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	categoryHtml = "";
	$j("#chooseCategory").html("");
	var defCategroyId =$j("#defCategroyId").val();
	
	var innerDefHtml ="";
	
	
	for (var i=0, l=nodes.length; i<l; i++) {
		if(defCategroyId !='' &&defCategroyId==nodes[i].id){
			innerDefHtml =nps.i18n("THIS_BE_DEFCATEGORY");
		}else{
			innerDefHtml ="<a href='javascript:void(0);'id="+nodes[i].id+" onclick='setCategroyDef(this.id)' style='color:#F8A721;text-decoration: underline;'>"+nps.i18n("SET_THIS_DEFCATEGORY")+"</a>";
		}
		
		categoryHtml = "<div class="+nodes[i].id +">"+nodes[i].name + 
		"<input type='hidden' name='categoriesIds'  value='"+nodes[i].id+"' /><span style='float:right;margin-right: 1000px;'>"+innerDefHtml+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
		"<a href='javascript:void(0);'id="+nodes[i].id+" style='color:#F8A721;text-decoration: underline;' onclick='delCategroy(this.id)'>"+nps.i18n("DELETE_THIS_CATEGORY")+"</a></span><br/></div>";
		$j("#chooseCategory").append(categoryHtml);
		
	}
	
	
	
}
function hideMenu(id) {
	$j("#"+id).fadeOut("fast");
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "menuContent" || $j(event.target).parents("#menuContent").length>0)) {
		hideMenu("menuContent");
	}
}
//*******************************默认分类

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
/******************treeDemo 展示end****************************/
//删除分类
function delCategroy(id){
	$j("."+id+"").remove();
	
	if(!categoryTreeInited) {
		initCategeoryTree();
		categoryTreeInited = true;
	}
	
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodes[i].checked= false;
		}
	}
	
	if(id==$j("#defCategroyId").val()){
		$j("#defCategroyId").val('');
	}
	zTree.refresh();
}

//设为默认分类
function setCategroyDef(id){
	$j("#defCategroyId").val(id);
	
	if(!categoryTreeInited) {
		initCategeoryTree();
		categoryTreeInited = true;
	}
	
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	var nodeObj=null;
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodeObj =nodes[i];
		}
	}
	onCheck1(null,null,nodeObj);
}

list="";


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
		var propertyId = pArray[i][3];
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
			if(i18nOnOff){
				if(!!itemProperty.propertyValue){
					if(itemProperty.propertyId==propertyId&&propertyValue==itemProperty.propertyValue.defaultValue){
						return itemProperty;
					}
				}
				
			}else{
				if(!!itemProperty.propertyValue){
					if(itemProperty.propertyId==propertyId&&propertyValue==itemProperty.propertyValue.value){
						return itemProperty;
					}
				}
			}
			
		}
	}
	return null;
}

function showSkuTable(isLoadInit,saleInfo){
	clickFlag = true;
	if(i18nOnOff){
		if(typeof(saleInfo)=='undefined'){
			$j(".saleInfo").each(function(i,dom){
				var me = $j(this);
				var lang = me.attr("lang");
				if(lang == defaultlang){
					saleInfo = me;
				}
			});
		}
		var lang = saleInfo.attr("lang");
		if(lang != defaultlang){
			return;
		}
	}else{
		if(typeof(saleInfo)=='undefined'){
			saleInfo = $j(".saleInfo");
		}
	}
	saleInfo.find(".exten").css("display","block");
	saleInfo.find(".extensionTable").html("");
	
	//拿到  销售属性,  根据销售属性 确定 列
	var salesProperty = new Array();
	
	var salesPropertyIndex = 0;
	for(var i=0;i<dynamicPropertyCommandListJsonStr.length;i++){
		var dynamicPropertyCommand = dynamicPropertyCommandListJsonStr[i];
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
			
			saleInfo.find("input[pid='"+propertyId+"']").each(function(index){
				var selectdStr="";
				for(var i=0;i<pInputArray.length;i++){
					selectdStr+=pInputArray[i];
					selectdStr+=",";
				}
				selectdStr=selectdStr.substring(0, selectdStr.length-1);
				$j(this).val(selectdStr);
				
			});
		}
		
		saleInfo.find("textarea[propertyId='"+propertyId+"'][editingtype='5']").each(function(){
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
								
							}
							
							if(inputValueArray.length>0){
								
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
		saleInfo.find(".extensionTable").html(html);
		
		return;
	}
	if(propertyValueInputArray.length!=propertyNameArray.length){//如果用户未把每个属性都填上值，则提示用户
		html =  nps.i18n("CODING_TIPS");
		saleInfo.find(".extensionTable").html(html);
	}else{// 画表格
		drawTableContent(propertyValueArray,propertyNameArray,propertyValueInputArray,isLoadInit,saleInfo);
	}
	
	saleInfo.find(".extensionTable").find("[loxiaType]").each(function(i,n){
		loxia.initLoxiaWidget(this);
	});
	
}
function drawTableContent(propertyValueArray,propertyNameArray,propertyInputValueArray,isLoadInit,saleInfo){

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
		
		/**
		 * add by hr 2014/07/11
		 * 现在nebula中的商品最多只有两个销售属性: 
		 * 1, 不存在销售属性: item与sku是一一对应的
		 * 2, 存在一个销售属性: item与sku是一对多的关系
		 * 3, 存在两个销售属性: item与sku是一对多的关系
		 */
		if(propertyValueArray.length==1){// 销售属性只有1个
			for(var i = 0;i<propertyValueArray[0].length;i++){ 
				var dynamicStr = "<td style='width:150px'>"+propertyValueArray[0][i][0]+"</td>";
				var codesName = getDynamicInputName("code",propertyValueArray[0][i],null);
				var salePriceName = getDynamicInputName("salePrice",propertyValueArray[0][i],null);
				var listPriceName = getDynamicInputName("listPrice",propertyValueArray[0][i],null);
				var idName = getDynamicInputName("id",propertyValueArray[0][i],null);
				
				var tmpArray = new Array();
				tmpArray[0]=propertyValueArray[0][i];
				
				var sku = getSkuInfoByProperyValueArray(tmpArray);
				
				var proHtml="<input type='hidden' name='idNameToReplace' value = 'idValueTOReplace' /><td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode'  name='codesNameToReplace' loxiaType='input' skuId='idValueTOReplace'  value='CODE_VALUE'/></td>" +
				"<td style='width:150px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices' name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
				"<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

				proHtml = proHtml.replace('codesNameToReplace', codesName);
				proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
				proHtml = proHtml.replace('listPriceToReplace', listPriceName);
				proHtml = proHtml.replace('idNameToReplace', idName);
				
				var salePrice = $j("#salePrice").val();
				var listPrice = $j("#listPrice").val();
				var code = "";
				var skuId = null;
				if(sku!=null){
					salePrice = ((sku.salePrice==null||sku.salePrice=="")?salePrice:sku.salePrice);
					listPrice = ((sku.listPrice==null||sku.listPrice=="")?listPrice:sku.listPrice);
					code = (sku.outid==null||sku.outid=="")?code:sku.outid;
					skuId = (sku.id==null||sku.id=="")?skuId:sku.id;
				}
				
				if(isLoadInit){//如果是初始化的时候，就不显示没有skuId的情况。
					if(skuId==null){
						continue;
					}
				}
				
				proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
				proHtml = proHtml.replace('CODE_VALUE', code);
				proHtml = proHtml.replace('salePrices_value', salePrice);
				proHtml = proHtml.replace('listPrices_value', listPrice);

				tableContent += ("<tr>"+dynamicStr+proHtml+"</tr>");
			}
			
			
		}else{
			//多个销售属性的展示
			tableContent = bulidTable(null, propertyValueArray, 0,isLoadInit);
			
		}
		
		
		$j("#jsonSku").val(JSON.stringify(skuInfoList));
	}else{
		var sku = skuList[0];
		
		var proHtml="<input type='hidden' name='skuId' value = 'idValueTOReplace' /><td style='width:150px'><input type='text' mandatory='true' class = 'dynamicInputNameSkuCode' name='skuCode' loxiaType='input' skuId='idValueTOReplace'  value='CODE_VALUE'/></td>" +
		"<td style='width:150px'><input type='text' id='salePrices' class = 'dynamicInputNameSalePrices' mandatory='true' name='skuSalePrice' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
		"<td style='width:150px'><input type='text' id='listPrices' class = 'dynamicInputNameListPrices' name='skuListPrice' decimal='2' loxiaType='number' value='listPrices_value'/></td>";
		
		var salePrice = $j("#salePrice").val();
		var listPrice = $j("#listPrice").val();
		var code = "";
		var skuId = null;
		if(sku!=null){
			salePrice = ((sku.salePrice==null||sku.salePrice=="")?salePrice:sku.salePrice);
			listPrice = ((sku.listPrice==null||sku.listPrice=="")?listPrice:sku.listPrice);
			code = (sku.outid==null||sku.outid=="")?code:sku.outid;
			skuId = (sku.id==null||sku.id=="")?skuId:sku.id;
		}
		
		proHtml = proHtml.replace('CODE_VALUE', code);
		proHtml = proHtml.replace('salePrices_value', salePrice);
		proHtml = proHtml.replace('listPrices_value', listPrice);
		proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
			
		tableContent += ("<tr>"+proHtml+"</tr>");
		list=   "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}
	
	
	var html =  tableHeader+tableContent;
	saleInfo.find(".extensionTable").html(html);
	
}

//--------------------------------多个销售属性循环start-------------------------------
function bulidTable(table, data, dataRowIndex,isLoadInit){
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
			
			var idName = getMoreDynamicInputName("id",arrays);
			var codesName = getMoreDynamicInputName("code",arrays);
			var salePriceName = getMoreDynamicInputName("salePrice",arrays);
			var listPriceName = getMoreDynamicInputName("listPrice",arrays);
			var tmpArray = clone(arrays);
			
			var sku = getSkuInfoByProperyValueArray(tmpArray);
			
			var proHtml="<input type='hidden' name='idNameToReplace' value = 'idValueTOReplace' /><td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode'  skuId='idValueTOReplace' name='codesNameToReplace' loxiaType='input'  value='CODE_VALUE'/></td>" +
			"<td style='width:150px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices' name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
			"<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

			proHtml = proHtml.replace('idNameToReplace', idName);
			proHtml = proHtml.replace('codesNameToReplace', codesName);
			proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
			proHtml = proHtml.replace('listPriceToReplace', listPriceName);
			
			var salePrice = $j("#salePrice").val();
			var listPrice = $j("#listPrice").val();
			var code = "";
			var skuId = null;
			if(sku!=null){
				salePrice = ((sku.salePrice==null||sku.salePrice=="")?salePrice:sku.salePrice);
				listPrice = ((sku.listPrice==null||sku.listPrice=="")?listPrice:sku.listPrice);
				code = (sku.outid==null||sku.outid=="")?code:sku.outid;
				skuId = (sku.id==null||sku.id=="")?skuId:sku.id;
			}
			
			if(isLoadInit){//如果是初始化的时候，就不显示没有skuId的情况。
				if(skuId==null){
					continue;
				}
			}
			
			proHtml = proHtml.replace('CODE_VALUE', code);
			proHtml = proHtml.replace('salePrices_value', salePrice);
			proHtml = proHtml.replace('listPrices_value', listPrice);
			proHtml = proHtml.replace(/idValueTOReplace/g, skuId);
			
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
	return bulidTable(result, data, ++dataRowIndex,isLoadInit);
}
//--------------------------------多个销售属性循环end-------------------------------

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
		var propertyId = pArray[i][3];
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
			if(i18nOnOff){
				if(!!itemProperty.propertyValue){
					if(itemProperty.propertyId==propertyId&&propertyValue==itemProperty.propertyValue.defaultValue){
						return itemProperty;
					}
				}
				
			}else{
				if(!!itemProperty.propertyValue){
					if(itemProperty.propertyId==propertyId&&propertyValue==itemProperty.propertyValue.value){
						return itemProperty;
					}
				}
			}
		}
	}
	return null;
}


function sortNumber(a,b){
	return a - b;
}

var editors =[];
//验证表单
function itemFormValidate(form){
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
				$j("#code").val(oldval);
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
	
	
	//价格校验
	 var salePrice = $j("#salePrice").val();
	   var salePriceArray = new Array();
	   var salePriceIndex = 0;
	   var originalSalePriceArray = new Array();
	   $j(".extensionTable").find(".dynamicInputNameSalePrices").each(function(i,n){
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
	   
	   // 验证skuCode 是否重复
	   var skuCodesArray = new Array();
	   var skuCodeValidateInfoArray = new Array();
	   $j(".extensionTable").find(".dynamicInputNameSkuCode").each(function(i,n){
		   skuCodesArray[i] = $j(this).val();
		   var skuId = $j(this).attr("skuId");
		   var skuInfo = new Object();
		   if(skuId==null||skuId=="null"){
			   skuId =-1;
		   }
		   skuInfo.id = skuId;
		   skuInfo.code = $j(this).val();
		   skuCodeValidateInfoArray[i]=skuInfo;
		});
	   
	   if(spChangedFlag){
		   return nps.i18n("SALES_PROPERTY_CHANGED");
	   }
	   
	   // 验证是否至少填写了一个sku编码 PLEASE_INPUT_ONE_SKU_CODE
	   var atLeastOneCode = false;
	   
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
	   
	   if(skuCodeValidateInfoArray.length>0){
		   var validateJsonStr = JSON.stringify(skuCodeValidateInfoArray);
		   var json = {"skuInfos":validateJsonStr};
		  var data = nps.syncXhr(validateUpdateSkuCodesUrl,json);
		  
		  if (data.isSuccess==false) {
			  return nps.i18n("SKU_CODE_REPEAT")+data.description;
		 }
	   }
	   
	   if(!atLeastOneCode){
		   return nps.i18n("PLEASE_INPUT_ONE_SKU_CODE");
	   }
	   
	   

    return loxia.SUCCESS;  
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


//除去textarea最后的一个"||"
function removeTextareaLastChar(){
	var $textarea = $j('.priDiv textarea');
	$j.each($textarea, function(i, object){
		var text = $j(this).text();
		if(text != '' && text != undefined && text != null){
			$j(this).text(text.substring(0, text.length-2));
		}
	});
}

function loadColorProp(sel, obj){
	var editingType = obj.attr('editingType');
	var isColorProp = obj.attr('iscolorprop');
	if(isColorProp == 'true'){
		var $colorPropertySelect = $j('.colorPropertySelect');
		if(sel != ''){
			var colors = sel.split('||');
			var str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
			str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
			for(var i in colors){
				str += '<option value="#'+colors[i]+'">'+colors[i]+'</option>';
			}
			str += '</select>';
			$colorPropertySelect.html(str);
			//初始化Loxia组件
			loxia.initContext($j(".colorPropertySelect"));
		}
	}
	//如果最后的是'||', 就除掉
	var lastTwoChar = sel.substring(sel.length-2);
	var propertyValues = '';
	if('||' == lastTwoChar){
		propertyValues = sel.substring(0, sel.length-2);
		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_DATA_FORMAT"));
		obj.focus();
		return;
	}
	var firstTwoChar = propertyValues.substring(0,2);
	if('||' == firstTwoChar){
		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_DATA_FORMAT"));
		obj.focus();
		return;
	}
	return loxia.SUCCESS;
}


function initCategeoryTree() {
	//商品分类下拉树
	$j.fn.zTree.init($j("#treeDemo"), categorySetting, categoryzNodes);
	//让商品分类下拉树只有叶子节点可选
	var _treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
	//节点全部展开
	//_treeObj.expandAll(true); 
	//1.将所有的节点转换为简单 Array 格式
	var _nodes = _treeObj.transformToArray(_treeObj.getNodes());
	for(var i = 0;i<_nodes.length;i++){
		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
		if(_nodes[i].isParent || _nodes[i].id == 0){
			_nodes[i].nocheck = true;
		}
		_treeObj.refresh();
	}
}

// obj 下拉框dom对象,type 属性类型(1:销售属性，2:普通属性) 两种属性生成HTML不同故做区分
function changeProGroup(obj,type){
	
	var propertyId=$j(obj).attr('propertyId');
	var json={proGroupId:$j(obj).val(),propertyId:propertyId};
	var backWarnEntity = loxia.syncXhr(base+'/item/findProGroupInfo.json', json,{type: "GET"});
	var html = ""
	//
	if(backWarnEntity.isSuccess){
  		var propertyValueArray = backWarnEntity.description.propertyValueList;	
  		var property = backWarnEntity.description.property
  		var picUrl = "";
		if(propertyValueArray!=null && propertyValueArray.length>0){
			if(type==1){
				$j.each(propertyValueArray,function(j){
					if(property.isColorProp && property.hasThumb ){
						picUrl ="<img src='"+baseUrl+"/images/1.png'>";
					}
					html+= "<div class='priDiv'><span class='children-store'>" +
							"<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' " ;
					//属性值分组中后已选择的属性选中效果
					for(var i = 0 ;i<itemPropertiesStr.length;i++){
						var itemProperty = itemPropertiesStr[i];
						if(itemProperty.propertyId==propertyId && itemProperty.propertyValueId==propertyValueArray[j].id){
							html+= "checked='checked'";
							break;
						}
					}
					
					html+=		" pvId='"+propertyValueArray[j].id+"' propertyId='"+property.id+"' " +
							"pvValue='"+propertyValueArray[j].value+"' propertyName='"+property.name+ "'" +
							"value='"+propertyValueArray[j].id+"'>"+picUrl+propertyValueArray[j].value+"</input></span> </div>";
					
			     });
			}else if(type==2){
				$j.each(propertyValueArray,function(j){
					html+= " <div class='priDiv'><span class='children-store'>" +
						   " <input type='checkbox' class='normalCheckBoxCls' name='filtratecolor'" ;
					//属性值分组中后已选择的属性选中效果
					for(var i = 0 ;i<itemPropertiesStr.length;i++){
						var itemProperty = itemPropertiesStr[i];
						if(itemProperty.propertyId==propertyId && itemProperty.propertyValueId==propertyValueArray[j].id){
							html+= " checked='checked'";
							break;
						}
					}
					html+=	" pid='"+property.id+"' tname='" +propertyValueArray[j].value +
							" 'mustcheck='"+property.name+ "'" +
							" value='"+propertyValueArray[j].id+"'>"+propertyValueArray[j].value+"</input></span> </div>";
					
			     });
			}
			$j(obj).next().html(html);
			//重新渲染
			var curSize = $j("#notSalePropSize").val();
			$j(".normalCheckBoxCls").each(function(){
				var curCheckBox = $j(this);
				//初始化选中的值
				drawNoSalePropEditing4Type(curSize);
				curCheckBox.change(function(){
				drawNoSalePropEditing4Type(curSize);
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






$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    oldval = $j("#code").val();
    itemId = $j("#itemid").val();

    
    
   //清空填充属性名称Array及属性idArray
   propertyArray.splice(0,propertyArray.length);
   var propertyIdsStr =$j("#propertyIdArray").val();
   propertyArray =eval("("+propertyIdsStr+")");
   
   propertyNameArray.splice(0,propertyNameArray.length);
   var propertyNamesStr =$j("#propertyNameArray").val();
   propertyNameArray =eval("("+propertyNamesStr+")");
   
   mustCheckArray.splice(0,mustCheckArray.length);
   var mustCheckArrayStr =$j("#mustCheckArray").val();
   mustCheckArray =eval("("+mustCheckArrayStr+")");
   //自动编码
   showSkuTable(true);
    
  
    //保存商品
	$j(".button.orange.submit").click(function(){

	   nps.submitForm('itemForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true)
			{
				saveitemcolorref();
				nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("UPDATEITEM_SUCCESS"));
				window.location.href=window.location.href;
				return;
			}else
			{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("UPDATEITEM_FAIL"));
			}
	   }});
	   
    });
    //返回
	$j(".button.return").on("click",function(){
		window.location.href=base+"/item/itemList.htm";
	});

	
	loxia.initContext($j(".ui-block "));
	
	
	
	$j(".button.orange.imageManage").click(function(){
		window.location.href= manageImagUrl+itemId;
	});
	
	function PropEditing4Typeinit(){
		var curSize = $j("#notSalePropSize").val();
		$j(".normalCheckBoxCls").each(function(){
			var curCheckBox = $j(this);
			//初始化选中的值
			drawNoSalePropEditing4Type(curSize);
			curCheckBox.change(function(){
			drawNoSalePropEditing4Type(curSize);
			});
		});
	}
	PropEditing4Typeinit();
	


	
	if(i18nOnOff){
		var i18nSize = i18nLangs.length;
		editors=[];
		for ( var i = 0; i < i18nSize; i++) {
		  var editor = CKEDITOR.replace( 'editor'+i,
				{
					toolbar : 'Full' ,
					filebrowserImageUploadUrl:'/img/upload.json'
				});
		  editors.push(editor);
		}
	}else{
		 editors=[];
		 var editor = CKEDITOR.replace( 'editor',
					{
						toolbar : 'Full' ,
						filebrowserImageUploadUrl:'/img/upload.json'
					});
		 editors.push(editor);
	}
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
	
	var itemcolorrefcheckURL="/itemColor/itemcolorcheck.json";
	var saveitemcolorrefURL="/itemColor/savecolorvalue.htm";
	
	function saveitemcolorref(){
		var selestrefre=selestref();
	    var json = {
	           'itemcolor' : selestrefre,
	           'code': $j("#code").val()  
	     };
		// 提交表單
		$j.ajax({
			url:saveitemcolorrefURL, 
			data:json,
			type: "post",
			success:function(data) { 
			}
		});
	}
	
	
	//商品颜色已选择值
	function selestref(){
		var selestrefret="";  
        $j("input[name=propertyValueIds]").each(function() {  
            if ($j(this).attr("checked")) {  
            	selestrefret +=$j(this).attr("pvvalue")+",";  
            }  
        });  
        return selestrefret;
	}
	
	
	function  selectcheck(URL){
		var selestrefre=selestref();
	    var json = {
	           'itemcolor' : selestrefre
	     };
		// 提交表單
		$j.ajax({
			url:URL, 
			data:json,
			type: "post",
			success:function(data) { 
				var dataObj = $j.parseJSON(data);
				$j("input[name=filtratecolor]").each(function() {  
		            	var filtratecolor =$j(this).attr("tname"); 
		            	for(var i=0;i<dataObj.length;i++){
		            		if(filtratecolor == dataObj[i]){
			            		$j(this).attr("checked",true);
			            		drawNoSalePropEditing4Type(null);
			            	}
		            	}
		        }); 
			}
		});
	}
	selectcheck(itemcolorrefcheckURL);
});