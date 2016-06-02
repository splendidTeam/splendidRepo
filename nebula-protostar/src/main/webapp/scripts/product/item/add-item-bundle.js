$j.extend(loxia.regional['zh-CN'],{
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
	"TABLE_TITLE_STYLE_CODE":"款号"
});

var findItemInfoListJsonUrl = base + "/item/itemList.json";

$j(document).ready(function(){
	$j(':radio[name=type]').bind('change', function(){
		var currVal = $j(this).val();
		
		if(currVal == 'product') {
			$j('#selectProList_product').show();
			$j('#selectProList_style').hide();
		} else {
			$j('#selectProList_product').hide();
			$j('#selectProList_style').show();
		}
	});
	
	// 点击添加主卖品
	$j("#set_main_element").on("click",function(){
		$j('.select-pro-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});
		$j('#bundle_dialog_title').html(nps.i18n("BUNDLE_DIALOG_TITLE_MAIN"));
	});	
	
	// 点击添加捆绑成员
	$j("#add_bundle_element").on("click",function(){
		$j('.select-pro-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});	
		$j('#bundle_dialog_title').html(nps.i18n("BUNDLE_DIALOG_TITLE_ELEMENT"));
	});	
	
	// 点击删除bundle成员
	$j(".dialog_close").on("click", function(){
		
	});
	
	// 点击搜索
	$j("#search_button").on("click", function(){
		var currVal = $j(':radio[name=type]:checked').val();
		if(currVal == 'product') {
			findProduct();
		} else {
			
		}
	});
	
	$j("#selectProList_product").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "mainItemDialogSearchForm",
		cols : [ {
			label : "",
			width : "3%",
			template : "radioTemplate"
		}, {
			name : "code",
			label : nps.i18n("TABLE_TITLE_ITEM_CODE"),
			width : "10%"
		}, {
			name : "title",
			label : nps.i18n("TABLE_TITLE_ITEM_TITLE"),
			width : "25%"
		}, {
			name : "categoryNames",
			label : nps.i18n("TABLE_TITLE_ITEM_CATEGROY"),
			width : "30%"
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
	};
});

function findProduct(){
	$j("#selectProList_product").data().uiLoxiasimpletable.options.currentPage = 1;
	$j("#selectProList_product").loxiasimpletable("refresh");
}

function radioTemplate(data, args, idx) {
	return "<input type='radio' value='" + loxia.getObject("id", data) + "' />";
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
	} 
}