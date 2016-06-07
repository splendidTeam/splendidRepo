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
	"TABLE_TITLE_STYLE_CODE":"款号"
});

var findItemInfoListJsonUrl = base + "/item/itemList.json";
//设置返回的是主卖品还是成员商品
var selectStoreyType = '';

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
	}
	
	refreshData();
	
	// 点击搜索
	$j("#search_button").click(function() {
		$j("#selectProList_product").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
	
	// 添加bundle扩展信息表单验证方法
	var baseInfoValidator = new FormValidator('', 30, function(){
		
		// TODO 校验主卖品是否存在
		
		// TODO 校验成员是否存在至少一个
		
		// TODO 校验成员是否重复（包括主卖品）
		
		// TODO 校验某个成员里面是否有至少一个sku参与
		
		// TODO 按三种价格模型分别校验价格设置
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(baseInfoValidator);
    
    
	//dialog-close  给关闭图标绑定点击事件
	bindClose(selectStoreyType);
	
	$j("#addMainProduct").on("click",function(){
		//关闭弹出层
		$j(".proto-dialog .dialog-close").click();
		if(selectStoreyType == 1){
			$j("#selectPro").before('<li class="main-pro"><a class="showpic"><img src=""><span class="dialog-close">X</span></a><p class="title p10">ABCD1234</p><p class="sub-title">超级舒适运动跑鞋</p></li>');
			$j("#selectPro").hide();
		}else if(selectStoreyType == 2){
			$j("#selectStyle").before('<li class="main-pro"><a class="showpic"><img src=""><span class="dialog-close">X</span></a><p class="title p10">ABCD1234</p><p class="sub-title">超级舒适运动跑鞋</p></li>');
		}
		bindClose(selectStoreyType);
		
	});
	
	
	/*==================================     弹出层    ==========================*/
	/*==================================     bundle扩展信息    ==========================*/
	
	
	
	
	
	/*==================================     价格设置    ==========================*/
	$j("input[name='setPrice']").bind('change', function(){
		var currVal = $j(this).val();
		
		if(currVal == 'subtotal') {//按捆绑商品总价
			$j('.product-table').hide();
			$j('.sku-table').find("input[name='setPrice']").attr("readonly","readonly");
		} else if(currVal == 'fix'){//一口价
			$j('.product-table').show();
			$j('.sku-table').find("input[name='setPrice']").attr("readonly","readonly");
		} else if(currVal == 'custom'){//定制
			$j('.sku-table').find("input[name='setPrice']").removeAttr("readonly");
			$j('.product-table').hide();
		}
	});
	/*==================================     价格设置    ==========================*/
	
	
	
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


//刷新表格数据
function refreshData(){
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
