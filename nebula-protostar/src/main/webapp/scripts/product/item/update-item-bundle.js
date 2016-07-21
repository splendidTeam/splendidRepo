$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"ADDITEM_FAIL":"新增商品失败",
	"BUNDLE_DIALOG_TITLE_MAIN":"选择主卖品",
	"BUNDLE_DIALOG_TITLE_ELEMENT":"选择成员",
	"NO_DATA":"无数据",
	"TABLE_TITLE_ITEM_CODE":"商品编码",
	"TABLE_TITLE_ITEM_TITLE":"商品名称",
	"TABLE_TITLE_ITEM_CATEGROY":"商品分类",
	"TABLE_TITLE_ITEM_TYPE":"商品种类",
	"TABLE_TITLE_ITEM_PRICE":"商品价格",
	"TABLE_TITLE_ITEM_INVENTORY":"库存",
	"TABLE_TITLE_ITEM_STATUS":"状态",
	"TABLE_TITLE_STYLE_CODE":"款号",
	"SELECT-PRODUCT":"请选择商品",
	"SELECT-PRODUCT-EXIST":"您选中的商品已存在",
	"MAIN-PRODUCT-NOT-EXIST":"主卖品未设置",
	"MEMBER-PRODUCT-NOT-EXIST":"成员商品未设置",
	"ITEM_TYPE_SIMPLE" : "普通商品",
	"ITEM_TYPE_BUNDLE" : "捆绑商品",
	"ITEM_TYPE_GROUP" : "组商品",
	"ITEM_TYPE_VIRTUAL" : "虚拟商品",
	"ITEM_TYPE_UNKNOWN" : "未知类型",
	"PRICE-IS-NULL" : "现销售价不能为空",
	"CHECK-MEMBER-SKU" : "每个商品必须有一个sku被选中",
	"CHECKED-PRICE-IS-NULL" : "被选中的sku,'现销售价'不能为空",
	"REFRESH-TABLE" : "请点击‘刷新’更新表格数据"
});

var findItemInfoListJsonUrl = base + "/item/itemList.json";
var findStyleInfoListJsonUrl = base + "/item/styleList.json";
//设置返回的是主卖品还是成员商品
var selectStoreyType = '';

var elements = new Array();
var mainElement = null;
var isRefresh = true;

$j(document).ready(function(){
	
	refreshItemData([]);
	
	/*==================================     bundle扩展信息    ==========================*/
	/*==================================     弹出层    ==========================*/
	//点击'设置主卖品'弹出对应弹层
	$j("#selectPro").hover(function(){
		$j(this).attr("style","text-decoration: underline");
	},function(){
		$j(this).attr("style","");
	});
	$j("#selectPro").on("click",function(){
		//返回的是主卖品
		selectStoreyType = 1;
		$j('.select-pro-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});	
		$j('#bundle_dialog_title').html(nps.i18n("BUNDLE_DIALOG_TITLE_MAIN"));
	});	
	
	//点击'+新成员'弹出对应弹层
	$j("#selectStyle").hover(function(){
		$j(this).attr("style","text-decoration: underline");
	},function(){
		$j(this).attr("style","");
	});
	$j("#selectStyle").on("click",function(){
		//返回的是成员商品
		selectStoreyType = 2;
		$j('.select-pro-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});	
		$j('#bundle_dialog_title').html(nps.i18n("BUNDLE_DIALOG_TITLE_ELEMENT"));
	});	
	
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
	
	// 点击搜索
	$j("#search_button").click(function() {
		var _type = $j(':input[name="selectType"]:checked').val();
		if(_type == "product") {
			refreshItemData(findItemInfoListJsonUrl);
		} else if(_type == "style") {
			refreshStyleData(findStyleInfoListJsonUrl);
		}
	});
	
	// 添加bundle扩展信息表单验证方法
	var bundleExtInfoValidator = new FormValidator('', 30, function(){
		
		// 校验主卖品是否存在
		if($j(".setMainProduct").find(".validate-code").text() == null || $j(".setMainProduct").find(".validate-code").text() == ""){
			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("MAIN-PRODUCT-NOT-EXIST"));
		}
		// 校验成员是否存在至少一个
		if($j(".setMemberProduct").find(".validate-code").text() == null || $j(".setMemberProduct").find(".validate-code").text() == ""){
			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("MEMBER-PRODUCT-NOT-EXIST"));
		}
		
		//校验是否点击刷新
    	if(!isRefresh){
    		return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("REFRESH-TABLE"));
    	}
		// 一口价时    只校验‘商品表’每个价格是否有值
		// 定制时     校验‘sku表’中某个成员里面是否有至少一个sku参与 并且选中的价格是否有值
    	if($j("input[name='priceType']:checked").val() == 2){
    		$j('.fix-price').each(function(){
    			if($j(this).val() == null || $j(this).val() == ""){
    				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("PRICE-IS-NULL"));
    			}
    		});
    	}else if($j("input[name='priceType']:checked").val() == 3 || $j("input[name='priceType']:checked").val() == 1){
    		var flag = true;
    		$j('.tr-product').each(function(){
    			var _flag = false;
    			var _data = $j(this).attr("data-product")
    			$j('.tr-sku').each(function(){
    				if($j(this).attr("data-sku") == _data && $j(this).find('.check-sku').is(':checked')){
    					_flag = true;
    				}	
    			});
    			
    			if(!_flag){
    				flag = false;
    			}
    		});
    		
    		if(!flag){
    			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("CHECK-MEMBER-SKU"));
    		}
    		
    		
    		$j('.check-sku:checked').each(function(){
    			if($j(this).parent().next().next().find('.sku-price').val() == null || $j(this).parent().next().next().find('.sku-price').val() == ""){
    				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("CHECKED-PRICE-IS-NULL"));
    			}
    		});
    	}
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(bundleExtInfoValidator);
    
    
	//dialog-close  给关闭图标绑定点击事件
	bindClose();
	
	$j("#addMainProduct").on("click",function(){
		var hasRepeat = false;
		var element = $j(':input[name="bundle_element"]:checked');
		//校验是否有重复选择商品
		$j(".validate-code").each(function(){
			if( element.parent().next().html() == $j(this).html() ){
				hasRepeat = true;
			}
		});
		//判断是否选择了商品
		if(element.val() != null || element.val() != ""){
			if(!hasRepeat){
				//关闭弹出层
				$j(".proto-dialog .dialog-close").click();
				//如果类型_type是商品则显示编码和名称、如果类型是款只显示款号
				var _type = $j(':input[name="selectType"]:checked').val();
				if(selectStoreyType == 1){
					if(_type == "product") {
						$j("#selectPro").before('<li class="main-pro"><a class="showpic"><img src="'+$j("#baseImageUrl").val()+element.attr("data-src") +'"><span class="dialog-close">X</span></a><p class="title p10 validate-code" data-type="product">'+element.parent().next().html()+'</p><p class="sub-title">'+element.parent().next().next().html()+'</p></li>');
						$j("#selectPro").hide();
					} else if(_type == "style") {
						$j("#selectPro").before('<li class="main-pro"><a class="showpic"><img src=""><span class="dialog-close">X</span></a><p class="title p10 validate-code" data-type="style">'+element.parent().next().text()+'</p></li>');
						$j("#selectPro").hide();
					}
				}else if(selectStoreyType == 2){
					if(_type == "product") {
						$j(".setMemberProduct").append('<li class="main-pro remove"><a class="showpic"><img src="'+$j("#baseImageUrl").val()+element.attr("data-src") +'"><span class="dialog-close">X</span></a><p class="title p10 validate-code" data-type="product">'+element.parent().next().text()+'</p><p class="sub-title">'+element.parent().next().next().text()+'</p></li>');
					} else if(_type == "style") {
						$j(".setMemberProduct").append('<li class="main-pro remove"><a class="showpic"><img src=""><span class="dialog-close">X</span></a><p class="title p10 validate-code" data-type="style">'+element.parent().next().text()+'</p></li>');
					}
				}
				bindClose();
			}else{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SELECT-PRODUCT-EXIST"));
			}
		}else{
			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SELECT-PRODUCT"));
		}
		isRefresh = false;
		$j(".setMemberProduct").sortable();
		$j(".remove").disableSelection();
	});
	
	 $j(".setMemberProduct").sortable({
		  beforeStop: function( event, ui ) {
			  isRefresh = false;
		  }
	});  
	
	//成员商品下的刷新按钮
	 $j("#refresh-table").hover(function(){
			$j(this).attr("style","text-decoration: underline");
		},function(){
			$j(this).attr("style","");
		});
	$j('#refresh-table').click(function() {
		if($j("input[name='priceType']:checked").val() == 1){
			loadBundleElements(false, true);
		}else if($j("input[name='priceType']:checked").val() == 2){
			loadBundleElements(false, false);
		}else if($j("input[name='priceType']:checked").val() == 3){
			loadBundleElements(true);
		}
	});
	/*==================================     弹出层    ==========================*/
	
	
	
	
	
	/*==================================     价格设置    ==========================*/
	$j("input[name='priceType']").bind('change', function(){
		var currVal = $j(this).val();
		
		if(currVal == '1') {//按捆绑商品总价
			$j('.product-table').hide();
			$j('.product-table > tbody').html('');
			loadBundleElements(false, true);
		} else if(currVal == '2'){//一口价
			loadBundleElements(false, false);
		} else if(currVal == '3'){//定制
			$j('.product-table').hide();
			$j('.product-table > tbody').html('');
			loadBundleElements(true);
		}
	});
	
	$j("input[name='selectType']").bind('change', function(){
		var currVal = $j(this).val();
		if(currVal == 'product') {
			refreshItemData([]);
		} else {
			refreshStyleData([]);
		}
	});
	/*==================================     价格设置    ==========================*/
	/*==================================     bundle扩展信息    ==========================*/
});


/*
 *给关闭图标绑定点击事件 
 */
function bindClose(){
	$j(".setMainProduct .dialog-close").bind("click",function(){
		$j("#selectPro").show();
		isRefresh = false;
	})
	$j(".setMemberProduct .dialog-close").bind("click",function(){
		isRefresh = false;
	})
	
	//关闭图标
	$j(".dialog-close").bind("click",function(){
		$j(this).closest("li.main-pro").remove();
	})
}

//刷新商品表格数据
function refreshItemData(dataUrl){
	$j("#selectProList_product").loxiasimpletable({
		page : true,
		size : 5,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "mainItemDialogSearchForm",
		cols : [ {
			label : "",
			width : "3%",
			template : "radioTemplate"
		}, {
			name : "code",
			label : nps.i18n("TABLE_TITLE_ITEM_CODE"),
			width : "10%",
			rowspan : true
		}, {
			name : "title",
			label : nps.i18n("TABLE_TITLE_ITEM_TITLE"),
			width : "25%"
		}, {
			name : "categoryNames",
			label : nps.i18n("TABLE_TITLE_ITEM_CATEGROY"),
			width : "30%",
			template : "formatCategoryNames"
		}, {
			name : "itemType",
			label : nps.i18n("TABLE_TITLE_ITEM_TYPE"),
			width : "10%",
			template : "itemTypeTemplate"
		}, {
			name : "salePrice",
			label : nps.i18n("TABLE_TITLE_ITEM_PRICE"),
			width : "10%"
		}, {
			name : "inventory",
			label : nps.i18n("TABLE_TITLE_ITEM_INVENTORY"),
			width : "5%"
		}, {
			name : "lifecycle",
			label : nps.i18n("TABLE_TITLE_ITEM_STATUS"),
			width : "15%",
			type : "threeState"
		}],
		dataurl : dataUrl
	});
	
	// 商品状态
	$j.ui.loxiasimpletable().typepainter.threeState = {
		getContent : function(data) {
			if (data == 0) {
				return "<span class='ui-pyesno ui-pyesno-no' title='下架'></span>";
			} else if (data == 1) {
				return "<span class='ui-pyesno ui-pyesno-yes' title='上架'></span>";
			} else if (data == 3) {
				return "<span class='ui-pyesno ui-pyesno-wait' title='新建'></span>";
			}
		},
		postHandle : function(context) {
			// do nothing
		}
	}
	
	$j("#selectProList_product").loxiasimpletable("refresh");
}

//刷新款号表格数据
function refreshStyleData(dataUrl){
	$j("#selectProList_product").loxiasimpletable({
		page : true,
		size : 5,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "mainItemDialogSearchForm",
		cols : [ {
			label : "",
			width : "3%",
			template : "radioTemplate1"
		}, {
			name : "style",
			label : nps.i18n("TABLE_TITLE_STYLE_CODE"),
			width : "10%",
			rowspan : true
		}, {
			name : "items",
			label : nps.i18n("TABLE_TITLE_ITEM_CODE"),
			width : "25%",
			template : "itemCodeTemplate"
		}],
		dataurl : dataUrl
	});
	
	$j("#selectProList_product").loxiasimpletable("refresh");
}

function radioTemplate(data, args, idx) {
	// bundle成员只能是普通商品
	var _type = loxia.getObject("type", data);
	var _itemImageList = loxia.getObject("itemImageList", data);
	var _itemImage = null;
	if( _itemImageList != null ){
		for(var i = 0; i < _itemImageList.length; i++) {
			var imgType = _itemImageList[i].type;
			if( imgType == "1" ){
				_itemImage = _itemImageList[i].picUrl;
				break;
			}
		}
	}
	return "<input type='radio' data-src='"+ _itemImage +"' name='bundle_element' value='" + loxia.getObject("id", data) + "' " + (_type == 1 ? "" : "disabled=disabled") + "/>";
}

function radioTemplate1(data, args, idx) {
	return "<input type='radio' data-src='' name='bundle_element' value='" + loxia.getObject("id", data) + "' />";
}

function itemCodeTemplate(data, args, idx) {
	var result = new Array();
	var itemArray = loxia.getObject("items", data);
	if(itemArray) {
		for(var i = 0; i < itemArray.length; i++) {
			result.push(itemArray[i].code);
		}
	}
	
	return result;
}

function formatCategoryNames(data, args, idx) {
	var propertyNameArray = null;
	
	if(categoryDisplayMode == "code") {
		propertyNameArray = loxia.getObject("categoryCodes", data);
	} else {//defaul display name
		propertyNameArray = loxia.getObject("categoryNames", data);
	}

	if (propertyNameArray == null || propertyNameArray == '') {
		return nps.i18n("NO_CATEGORY");
	}
	
	var name = loxia.getObject("defCategory", data);
   
	if(name != null && name.trim() != ''){
		propertyNameArray.remove(name);
		propertyNameArray += "&nbsp;<font  color='red'>"+name+"</font>";
	}
   
	return propertyNameArray;
}

function itemTypeTemplate(data, args, idx) {
	var _type = loxia.getObject("itemType", data);
	switch(_type) {
		case 1 : return nps.i18n("ITEM_TYPE_SIMPLE");
		case 3 : return nps.i18n("ITEM_TYPE_BUNDLE"); 
		case 5 : return nps.i18n("ITEM_TYPE_GROUP"); 
		case 7 : return nps.i18n("ITEM_TYPE_VIRTUAL");
		default: return nps.i18n("ITEM_TYPE_UNKNOWN");
	} 
}

Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
}

/**
 * 装填“一口价”表格数据
 */
function fillProductTable(data){
	var _html = '';
	var number = 0;
	$j(data).each(function(idx, element){
		number++;
		if(element.styleCode) { // 同款商品
			var bundleItems = $j(element.bundleItemViewCommands);
			_html += '<tr class="'+(number%2==0?"odd":"even")+'">';
			_html += '<td rowspan="' + bundleItems.length + '">' + (idx + 1) + '</td>';
			$j(bundleItems).each(function(i, item){
				_html += '<td>' + item.itemCode + '</td>';
				_html += '<td>' + item.salesPrice + '</td>';
				if(i == 0) {
					_html += '<td rowspan="' + bundleItems.length + '"><input type="text" class="fix-price" name="bundleElementViewCommands[' + idx + '].salesPrice" value="' + item.salesPrice + '" /></td>';
				}
				_html += '</tr>';
				if(i < bundleItems.length - 1) {
					_html += '<tr>';
				}
			});
			_html += '</tr>';
		} else { // 单一商品
			_html += '<tr class="'+(number%2==0?"odd":"even")+'">';
			_html += '<td>' + (idx + 1) + '</td>';
			_html += '<td>' + element.itemCode + '</td>';
			_html += '<td>' + element.bundleItemViewCommands[0].salesPrice + '</td>';
			_html += '<td><input type="text" class="fix-price" name="bundleElementViewCommands[' + idx + '].salesPrice" value="' + element.bundleItemViewCommands[0].salesPrice + '" /></td>';
			_html += '</tr>';
		}
	}); 
	$j('.product-table > tbody').html(_html);
	$j('.product-table').show();
	isRefresh = true;
}

/**
 * 装填“定制”表格数据
 */
function loadBundleElements(editable, showDefaultValue, elements){
	var data = null;
	if(elements) {
		data = elements
	} else {
		getElements();
		if(fillForm()) {
			var f = loxia._getForm("bundle_element_form");
			loxia.lockPage();
			data = nps.syncXhr("/item/loadBundleElements.json", f);
			loxia.unlockPage();
		}
	}
	
	// 如果是一口价，装填“一口价”表格数据
	if($j("input[name='priceType']:checked").val() == 2){
		fillProductTable(data);
	}
	
	var _html = '';
	var _a = new Array();
	var num = 0;
	$j(data).each(function(idx, element){
		_html += '<tr class="tr-product" data-product="' + idx + '">';
		_html += '<td rowspan="##' + idx + '##">' + (idx + 1) + '</td>'
			+ '<input type="hidden" name="bundleElementViewCommands[' + idx + '].isMainElement" value="' + element.isMainElement + '" />'
			+ '<input type="hidden" name="bundleElementViewCommands[' + idx + '].sort" value="' + element.sort + '" />'
			+ '<input type="hidden" name="bundleElementViewCommands[' + idx + '].styleCode" value="' + element.styleCode + '" />'
			+ '<input type="hidden" name="bundleElementViewCommands[' + idx + '].itemCode" value="' + element.itemCode + '" />';
		var bundleItems = element.bundleItemViewCommands;
		var rowspan = 0;
		$j(bundleItems).each(function(m, item){
			_html += '<td>商品</td>';
			_html += '<td>' + item.itemCode + '<input type="hidden" name="bundleElementViewCommands[' + idx + '].bundleItemViewCommands[' + m + '].itemId" value="' + item.itemId + '" /></td>';
			_html += '<td></td>';
			_html += '<td></td>';
			_html += '</tr>';
			rowspan ++;
			var bundleSkus = item.bundleSkuViewCommands;
			$j(bundleSkus).each(function(n, sku){
				rowspan ++;
				_html += '<tr class="tr-sku odd" data-sku="' + idx + '">';
				_html += '<td>' + sku.property + '</td>';
				_html += '<td><input class="check-sku" type="checkbox" name="bundleElementViewCommands[' + idx + '].bundleItemViewCommands[' + m + '].bundleSkuViewCommands[' + n + '].isParticipation" ' + (sku.isParticipation ? 'checked="checked"' : '') + ' /></td>';
				_html += '<td>' + sku.originalSalesPrice + '</td>';
				_html += '<td><input class="sku-price" type="text" name="bundleElementViewCommands[' + idx + '].bundleItemViewCommands[' + m + '].bundleSkuViewCommands[' + n + '].salesPrice" ' + (editable ? 'value="' + (sku.salesPrice == null ? sku.originalSalesPrice : sku.salesPrice) + '"' : (showDefaultValue ? 'value="' + sku.originalSalesPrice + '" readonly=readonly' : 'readonly=readonly')) + ' />'
					+ '<input type="hidden" name="bundleElementViewCommands[' + idx + '].bundleItemViewCommands[' + m + '].bundleSkuViewCommands[' + n + '].skuId" value="' + sku.skuId + '" /></td>';
				_html += '</tr>';
			});
		});
		_a.push(rowspan);
	}); 
	
	for(var i = 0; i < _a.length; i++) {
		_html = _html.replace('##' + i + '##', _a[i]);
	}
	
	$j('.sku-table > tbody').html(_html);
	isRefresh = true;
}

function fillForm(){
	$j('#bundle_element_form').html('');
	
	var _html = '';
	var _e = new Array();
	
	// 校验主卖品
	if(mainElement ) {
		_e.push(mainElement )
	} else {
		nps.info(nps.i18n('MAIN-PRODUCT-NOT-EXIST'));
		return false;
	}
	
	// 校验捆绑成员
	if(elements && elements.length > 0) {
		_e = _e.concat(elements);
	} else {
		nps.info(nps.i18n('MEMBER-PRODUCT-NOT-EXIST'));
		return false;
	}
	
	for(var i = 0; i < _e.length; i++) {
		_html += '<input type="hidden" name="bundleElements.isMainElement" value="' + _e[i].isMainElement + '" />';
		_html += '<input type="hidden" name="bundleElements.sort" value="' + _e[i].sort + '" />';
		_html += '<input type="hidden" name="bundleElements.styleCode" value="' + _e[i].styleCode + '" />';
		_html += '<input type="hidden" name="bundleElements.itemCode" value="' + _e[i].itemCode + '" />';
	}
	$j('#bundle_element_form').html(_html);
	
	return true;
}


//获取主商品和成员商品的对象
function getElements(){
	
	mainElement = null;
	elements = new Array();
    bundleElement = null;
	
	//主商品
	var mainProductCode = $j(".setMainProduct").find(".validate-code");
	if(mainProductCode.text() != null){
		if(mainProductCode.attr("data-type") == "product"){
			mainElement = {
					isMainElement : true,
					sort : 1,
					styleCode : '',
					itemCode : mainProductCode.text()
				};
		}else if(mainProductCode.attr("data-type") == "style"){
			mainElement = {
					isMainElement : true,
					sort : 1,
					styleCode : mainProductCode.text(),
					itemCode : ''
				};
		}
	}
	
	//成员商品
	var memberProductCode = null;
	var _sort = 1;
	$j(".setMemberProduct").find(".validate-code").each(function(){
		if($j(this).text() != null){
			_sort++;
			if($j(this).attr("data-type") == "product"){
				bundleElement = {
						isMainElement : false,
						sort : _sort,
						styleCode : '',
						itemCode : $j(this).text()
					};
				elements.push(bundleElement);
			}else if($j(this).attr("data-type") == "style"){
				bundleElement = {
						isMainElement : false,
						sort : _sort,
						styleCode : $j(this).text(),
						itemCode : ''
					};
				elements.push(bundleElement);
			}
		}
	});
}

function initBundleElement(priceType, elements){
	switch(priceType) {
	case 1 : loadBundleElements(false, true, elements);break;
	case 2 : loadBundleElements(false, false, elements);break;
	case 3 : loadBundleElements(true, false, elements);break;
	}
}

