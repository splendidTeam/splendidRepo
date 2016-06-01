$j.extend(loxia.regional['zh-CN'],{
	"BUNDLE_DIALOG_TITLE_MAIN":"选择主卖品",
	"BUNDLE_DIALOG_TITLE_ELEMENT":"选择成员",
	"NO_DATA":"无数据",
	"TABLE_TITLE_ITEM":"商品",
	"TABLE_TITLE_ITEM_CATEGROY":"商品分类",
	"TABLE_TITLE_ITEM_TYPE":"商品种类",
	"TABLE_TITLE_ITEM_PRICE":"商品价格",
	"TABLE_TITLE_ITEM_STATUS":"商品状态",
	"TABLE_TITLE_STYLE_CODE":"款号"
});

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
});

function findProduct(){
	$j("#selectProList_product").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "mainItemDialogSearchForm",
		cols : [ {
			name : "item.id",
			label : "",
			width : "5%",
			template : "radioTemplate",
		}, {
			name : "item.name",
			label : nps.i18n("TABLE_TITLE_ITEM"),
			width : "30%"
		}, {
			name : "item.categorys",
			label : nps.i18n("TABLE_TITLE_ITEM_CATEGROY"),
			width : "30%"
		}, {
			name : "item.type",
			label : nps.i18n("TABLE_TITLE_ITEM_TYPE"),
			width : "10%"
		}, {
			name : "item.salesPrice",
			label : nps.i18n("TABLE_TITLE_ITEM_PRICE"),
			width : "10%"
		}, {
			name : "item.status",
			label : nps.i18n("TABLE_TITLE_ITEM_STATUS"),
			width : "10%"
		}],
		dataurl : findItemInfoListJsonUrl
	});
}