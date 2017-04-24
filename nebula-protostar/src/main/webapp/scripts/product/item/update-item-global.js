$j.extend(loxia.regional['zh-CN'], {
	"SYSTEM_ITEM_MESSAGE" : "提示信息",
	"ITEM_UPDATE_CODE_ABLE" : "商品编码可用",
	"UPDATEITEM_FAIL" : "修改商品失败",
	"UPDATEITEM_SUCCESS" : "修改商品成功",
	"MERCHANT_CODING" : "商家编码",
	"MERCHANT_SALEPRICE" : "销售价",
	"MERCHANT_LISTPRICE" : "吊牌价",
	"MERCHANT_GROUPCODE" : "组合商品编码",
	"CODING_TIPS" : "您需要选择所有的属性，才能组合成完整的规格信息!",
	"DELETE_THIS_CATEGORY" : "删除此分类",
	"SET_THIS_DEFCATEGORY" : "设为默认",
	"THIS_BE_DEFCATEGORY" : "默认",
	"MUST_SELECT" : "为必选项",
	"NOT_DATA_FORMAT" : "数据格式不正确",
	"SELECT_COLOR_PROPERTY" : "请选择颜色属性",
	"WRITE_ALL_INFO" : "请将信息填写完整",
	"PLEASE_SELECT_PROPERTY" : "请选择销售属性",
	"IMAGE_SELECT_COLOR_PROPERTY" : "颜色属性",
	"PLEASE_SELECT_DEFAULT_CATEGORY" : "请为商品选择默认分类",
	"NOT_REPEATEDLY_RELEVANCE_CATEGORY" : "同一商品不可以多次关联同一分类",
	"SALEPRICE_OUT_OF_RANGE" : "销售价超出sku设置的销售价区间",
	"PLEASE_INPUT_SALEPRICES" : "请输入sku销售价格",
	"LISTPRICE_OUT_OF_RANGE" : "挂牌价超出sku设置的吊牌价区间",
	"PLEASE_INPUT_LISTPRICE" : "请输入sku吊牌价格",
	"MERCHANT_CODING_EQUAL" : "商家编码相同",
	"ITEM_UPDATE_CODE_ENBLE" : "商品编码不可用,为您恢复到原始的店铺编码",
	"SKU_CODE_REPEAT" : "以下商家编码已经存在：",
	"CUSTOM_PROPERTY_SAME" : "填写的自定义属性相同",
	"SALES_PROPERTY_CHANGED" : "销售属性已经更新，但是没有重新进行编码设置",
	"PLEASE_INPUT_ONE_SKU_CODE" : "请输入至少一个sku编码",
	"GROUP_CODE_FORMAT_ERROR" : "组合商品编码格式以:分割",
	"GROUP_CODE_FORMAT_EQUAL" : "同一个输入框的组合商品编码不能相同",
	"PLEASE_SET_DEF_CATEGORY" : "请设定默认分类"
});
var oldval = "";
var itemId = 0;

function FormValidator(name, sortNo, validate) {
	this.name = name;
	this.sortNo = sortNo;
	this.validate = validate;
}

var formValidateList = [];

// 表单验证
function itemFormValidate(form) {
	formValidateList.sort(function(a, b) {
		return a.sortNo - b.sortNo;
	});

	for (var i = 0; i < formValidateList.length; i++) {
		if (formValidateList[i] instanceof FormValidator) {
			var result = formValidateList[i].validate(form);
			if (result != loxia.SUCCESS) {
				return result;
			}
		} else {

		}
	}

	return loxia.SUCCESS;
}

$j(document).ready(function() {
	/* toggle div */
	$j(".ui-block-title").on("click", function() {
		$j(this).next(".ui-block-content").slideToggle();
	})

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	$j.extend(loxia.regional['zh-CN'], {
		"ITEM_CODE_VALID_FAIL" : itemCodeValidMsg
	});

	oldval = $j("#code").val();
	itemId = $j("#itemid").val();

	// 保存商品
	$j(".button.orange.submit").click(function() {
		nps.submitForm('itemForm', {
			mode : 'async',
			successHandler : function(data) {
				if (data.isSuccess == true) {
					try {
						saveitemcolorref();
					} catch (e) {
						// do nothing
					}
					nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("UPDATEITEM_SUCCESS"));
					window.location.href = window.location.href;
					return;
				} else {
					return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("UPDATEITEM_FAIL"));
				}
			}
		});
	});

	// 图片管理
	$j(".button.orange.imageManage").click(function() {
		window.location.href = manageImagUrl + itemId;
	});

	// 返回
	$j(".button.return").on("click", function() {
		window.location.href = base + "/item/itemList.htm";
	});

	loxia.initContext($j(".ui-block "));

	$j("body").bind("formvalidatefailed", function(args) {
		// TODO 这里定义的事件不能被顺利触发，详见loxia.submitForm()
		$j.each(loxia.byCss("input:enabled,select:enabled,textarea:enabled", args[1]), function(index, widget) {
			if (!widget.state()) {
				$j(widget).parents(".ui-block-content").show();
			}
		});
	});
});
