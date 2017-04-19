$j.extend(loxia.regional['zh-CN'], {
	"SYSTEM_ITEM_MESSAGE" : "提示信息",
	"SYSTEM_ITEM_SELECT_INDUSTRY" : "请选择商品所在行业",
	"ITEM_UPDATE_CODE_ENBLE" : "商品编码不可用",
	"ITEM_UPDATE_CODE_ABLE" : "商品编码可用",
	"INDUSTRY_FIND" : " 共找到",
	"PLEASE_SELECT" : " 请选择",
	"INDUSTRY_RESULT" : "个结果",
	"ADDITEM_FAIL" : "新增商品失败",
	"MERCHANT_CODING" : "商家编码",
	"MERCHANT_GROUPCODE" : "组合商品编码",
	"MERCHANT_SALEPRICE" : "销售价",
	"MERCHANT_LISTPRICE" : "吊牌价",
	"OTHERS" : "其他",
	"MUST_SELECT" : "为必选项",
	"SELECT_COLOR_PROPERTY" : "请选择颜色属性",
	"WRITE_ALL_INFO" : "请将信息填写完整",
	"DELETE_THIS_CATEGORY" : "删除此分类",
	"SET_THIS_DEFCATEGORY" : "设为默认",
	"THIS_BE_DEFCATEGORY" : "默认",
	"PLEASE_SELECT_DEFAULT_CATEGORY" : "请为商品选择默认分类",
	"NOT_REPEATEDLY_RELEVANCE_CATEGORY" : "同一商品不可以多次关联同一分类",
	"IMAGE_SELECT_COLOR_PROPERTY" : "颜色属性",
	"NOT_DATA_FORMAT" : "数据格式不正确",
	"SALEPRICE_OUT_OF_RANGE" : "销售价超出sku设置的销售价区间",
	"PLEASE_INPUT_SALEPRICES" : "请输入sku销售价格",
	"LISTPRICE_OUT_OF_RANGE" : "吊牌价" + "超出sku设置的吊牌价区间",
	"PLEASE_INPUT_LISTPRICE" : "请输入sku吊牌价格",
	"MERCHANT_CODING_EQUAL" : "商家编码相同",
	"PLEASE_SET_CODE" : "请进行编码设置",
	"SKU_CODE_REPEAT" : "以下商家编码已经存在：",
	"CUSTOM_PROPERTY_SAME" : "填写的自定义属性相同",
	"SALES_PROPERTY_CHANGED" : "销售属性已经更新，但是没有重新进行编码设置",
	"PLEASE_INPUT_ONE_SKU_CODE" : "请输入至少一个sku编码",
	"PLEASE_SET_DEF_CATEGORY" : "请设定默认分类",
	"PLEASE_SELECT_PROPERTY_GROUP" : "请选择属性分组"
});

/**
 * only use for save item
 * @param form
 * @param args
 * @param param
 */
// function saveItem(form,args,param){
//	
// var f = loxia._getForm(form),
// mode = args.mode||"sync",
// lock = (args.lock == undefined || args.lock)?true:false;
//
// if(lock){
// loxia.lockPage();
// }
//	
// var result=$j.extend({}, f,param);
//	
// var c = this.validateForm(result);
// if(c){
// nps.asyncXhr($(result).attr("action"),result,args)
//	    
// }else{
// loxia.unlockPage();
// }
//	
// }
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

	// 初始化id为colorPropertyContent的div内部的所有Loxia组件
	loxia.initContext($j(".ui-block "));

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

	$j(".normalCheckBoxCls").each(function() {

		var curCheckBox = $j(this);
		curCheckBox.change(function() {
			drawNoSalePropEditing4Type(num);
		});

	});

	// 保存商品
	$j(".button.orange.submit").click(function() {
		nps.submitForm('itemForm', {
			mode : 'async',
			successHandler : function(data) {
				if (data.isSuccess == true) {
					window.location.href = base + "/item/itemList.htm";
				} else {
					return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("ADDITEM_FAIL"));
				}
			}
		});
	});

	$j("body").bind("formvalidatefailed", function(args) {
		// TODO 这里定义的事件不能被顺利触发，详见loxia.submitForm()
		$j.each(loxia.byCss("input:enabled,select:enabled,textarea:enabled", args[1]), function(index, widget) {
			if (!widget.state()) {
				$j(widget).parents(".ui-block-content").show();
			}
		});
	});
});
