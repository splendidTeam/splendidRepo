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
	"MAIN-PRODUCT-NOT-EXIST":"主卖品不存在",
	"MEMBER-PRODUCT-NOT-EXIST":"成员商品不存在"
});

var findItemInfoListJsonUrl = base + "/item/itemList.json";
var findStyleInfoListJsonUrl = base + "/item/styleList.json";
//设置返回的是主卖品还是成员商品
var selectStoreyType = '';

var mainEelment = null;
var elements = new Array();
var bundleElement = null;

$j(document).ready(function(){
	
	/*==================================     bundle扩展信息    ==========================*/
	/*==================================     弹出层    ==========================*/
	//点击'设置主卖品'弹出对应弹层
	$j("#selectPro").on("click",function(){
		//返回的是主卖品
		selectStoreyType = 1;
		$j('.select-pro-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});	
		$j('#bundle_dialog_title').html(nps.i18n("BUNDLE_DIALOG_TITLE_MAIN"));
	});	
	
	//点击'+新成员'弹出对应弹层
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
			refreshItemData();
		} else if(_type == "style") {
			refreshStyleData();
		}
	});
	
	// 添加bundle扩展信息表单验证方法
	var bundleExtInfoValidator = new FormValidator('', 30, function(){
		
		// TODO 校验主卖品是否存在
		if($j(".setMainProduct").find(".validate-code").text() == null){
			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("MAIN-PRODUCT-NOT-EXIST"));
		}
		// TODO 校验成员是否存在至少一个
		if($j(".setMemberProduct").find(".validate-code").text() == null){
			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("MEMBER-PRODUCT-NOT-EXIST"));
		}
		// TODO 校验成员是否重复（包括主卖品）
		
		// TODO 校验某个成员里面是否有至少一个sku参与
		
		// TODO 按三种价格模型分别校验价格设置
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(bundleExtInfoValidator);
    
    
	//dialog-close  给关闭图标绑定点击事件
	bindClose(selectStoreyType);
	
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
		if(element.val() != null){
			if(!hasRepeat){
				//关闭弹出层
				$j(".proto-dialog .dialog-close").click();
				//如果类型_type是商品则显示编码和名称、如果类型是款只显示款号
				var _type = $j(':input[name="selectType"]:checked').val();
				if(selectStoreyType == 1){
					if(_type == "product") {
						$j("#selectPro").before('<li class="main-pro"><a class="showpic"><img src="'+$j("#baseImageUrl").val()+element.attr("data-src") +'"><span class="dialog-close">X</span></a><p class="title p10 validate-code">'+element.parent().next().html()+'</p><p class="sub-title">'+element.parent().next().next().html()+'</p></li>');
						$j("#selectPro").hide();refreshItemData();
						mainEelment = {
								isMainElement : true,
								sort : 1,
								styleCode : '',
								itemCode : element.parent().next().text()
							};
					} else if(_type == "style") {
						$j("#selectPro").before('<li class="main-pro"><a class="showpic"><img src=""><span class="dialog-close">X</span></a><p class="title p10 validate-code">'+element.parent().next().html()+'</p></li>');
						$j("#selectPro").hide();
						mainEelment = {
								isMainElement : true,
								sort : 1,
								styleCode : element.parent().next().text(),
								itemCode : ''
							};
					}
				}else if(selectStoreyType == 2){
					if(_type == "product") {
						$j("#selectStyle").before('<li class="main-pro"><a class="showpic"><img src="'+$j("#baseImageUrl").val()+element.attr("data-src") +'"><span class="dialog-close">X</span></a><p class="title p10 validate-code">'+element.parent().next().html()+'</p><p class="sub-title">'+element.parent().next().next().html()+'</p></li>');
						bundleElement = {
								isMainElement : false,
								sort : $j(".setMemberProduct li").length,
								styleCode : '',
								itemCode : element.parent().next().text()
							};
						elements.push(bundleElement);
					} else if(_type == "style") {
						$j("#selectStyle").before('<li class="main-pro"><a class="showpic"><img src=""><span class="dialog-close">X</span></a><p class="title p10 validate-code">'+element.parent().next().html()+'</p></li>');
						bundleElement = {
								isMainElement : false,
								sort : $j(".setMemberProduct li").length,
								styleCode : element.parent().next().text(),
								itemCode : ''
							};
						elements.push(bundleElement);
					}
				}
				bindClose(selectStoreyType);
			}else{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SELECT-PRODUCT-EXIST"));
			}
		}else{
			return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SELECT-PRODUCT"));
		}
	});
	
	
	/*==================================     弹出层    ==========================*/
	
	
	
	
	
	/*==================================     价格设置    ==========================*/
	$j("input[name='setPrice']").bind('change', function(){
		var currVal = $j(this).val();
		
		if(currVal == 'subtotal') {//按捆绑商品总价
			$j('.product-table').hide();
			$j('.product-table > tbody').html('');
			$j('.sku-table').hide();
			$j('.sku-table > tbody').html('');
			$j('.sku-table').find("input[name='setPrice']").attr("readonly","readonly");
		} else if(currVal == 'fix'){//一口价
			$j('.product-table').show();
			$j('.sku-table').hide();
			$j('.sku-table > tbody').html('');
			loadBundleElement();
		} else if(currVal == 'custom'){//定制
			$j('.product-table').hide();
			$j('.product-table > tbody').html('');
			$j('.sku-table').show();
			$j('.sku-table').find("input[name='setPrice']").removeAttr("readonly");
			loadBundleSku();
		}
	});
	/*==================================     价格设置    ==========================*/
	/*==================================     bundle扩展信息    ==========================*/
	
	
	//保存商品
	$j(".button.orange.submit").click(function(){
	   nps.submitForm('itemForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true)
			{
				window.location.href=base+"/item/itemList.htm";
			}else
			{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("ADDITEM_FAIL"));
			}
	   }});
    });
	
	//上一步
    $j(".button.back").on("click",function(){
    	window.location.href=base+"/item/createItemChoose.htm";
    });
	
});


/*
 *给关闭图标绑定点击事件 
 */
function bindClose(selectStoreyType){
	if(selectStoreyType == 1){
		$j(".setMainProduct .dialog-close").bind("click",function(){
			$j("#selectPro").show();
		})
	}
	
	//关闭图标
	$j(".dialog-close").bind("click",function(){
		$j(this).closest("li.main-pro").remove();
	})
}


//刷新商品表格数据
function refreshItemData(){
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
			name : "type",
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
		dataurl : findItemInfoListJsonUrl
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
function refreshStyleData(){
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
		dataurl : findStyleInfoListJsonUrl
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
	var _type = loxia.getObject("type", data);
	switch(_type) {
		case 1 : return "普通商品";
		case 3 : return "捆绑商品"; 
		case 5 : return "搭配商品"; 
		case 7 : return "虚拟商品";
		default: return "未知";
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
function loadBundleElement(){
	if(fillForm()) {
		var f = loxia._getForm("bundle_element_form");
		var data = nps.syncXhr("/item/loadBundleElement.json", f);
		// TODO 绘制表格
		
		var _html = '';
		$j(data).each(function(idx, d){
			debugger;
			if($(this).styleCode) { // 同款商品
				var bundleItems = $(this).bundleItemViewCommands;
				html += '<tr>';
				html += '<td rowspan="' + bundleitems.length + '">' + idx + 1 + '</td>';
				$j(bundleItems).each(function(i, bivc){
					html += '<td>' + $(this).itemCode + '</td>';
					html += '<td>' + $(this).salesPrice + '</td>';
					if(i == 0) {
						html += '<td rowspan="' + bundleitems.length + '><input type="text" name="bundleElement.salesPrice" /></td>';
					}
					html += '</tr>';
					if(i < bundleItems.length - 1) {
						html += '<tr>';
					}
				});
				html += '</tr>';
			} else { // 单一商品
				html += '<tr>';
				html += '<td>' + idx + 1 + '</td>';
				html += '<td>' + $(this).itemCode + '</td>';
				html += '<td>' + $(this).bundleItemViewCommands[0].salesPrice + '</td>';
				html += '<td><input type="text" name="bundleElement.salesPrice" /></td>';
				html += '</tr>';
			}
		}); 
		$j('#product-table > tbody').html(_html);
	}
}

/**
 * 装填“定制”表格数据
 */
function loadBundleSku(){
	if(fillForm()) {
		var f = loxia._getForm("bundle_element_form");
		var data = nps.syncXhr("/item/loadBundleSku.json", f);
		// TODO 绘制表格
		
		var _html = '';
		var _a = new Array();
		$j(data).each(function(idx, element){
			_html += '<tr>';
			_html += '<td colsapn="##' + idx + '##">' + idx + 1 + '</td>';
			var bundleItems = $(this).bundleItemViewCommands;
			var rowspan = 0;
			$j($(bundleItems)).each(function(m, item){
				_html += '<td>商品</td>';
				_html += '<td>' + $(this).itemCode + '</td>';
				_html += '<td></td>';
				_html += '<td></td>';
				_html += '</tr>';
				rowspan ++;
				$j(this.bundleSkuViewCommands).each(function(m, sku){
					
				});
			});
		}); 
		
		for(var i = 0; i < _a.length; i++) {
			_html.replace('##' + i + '##', _a[i]);
		}
		
		$j('#sku-table > tbody').html(_html);
	}
}

function fillForm(){
	$j('#bundle_element_form').html('');
	
	var _html = '';
	var _e = new Array();
	
	// 校验主卖品
	if(mainEelment) {
		_e.push(mainEelment)
	} else {
		return false;
	}
	
	// 校验捆绑成员
	if(elements && elements.length > 0) {
		_e.push(elements)
	} else {
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

