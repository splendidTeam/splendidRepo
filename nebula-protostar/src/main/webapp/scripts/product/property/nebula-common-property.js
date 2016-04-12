$j.extend(loxia.regional['zh-CN'],{
 	"VALUE_EDIT_DH":"单行输入",
    "VALUE_EDIT_KSRDX":"可输入单选",
    "VALUE_EDIT_DANX":"单选",
    "VALUE_EDIT_DX":"多选",   
    "VALUE_EDIT_ZDYDX":"自定义多选",   
	"SYSTEM_PROPERTY_MESSAGE":"提示信息",
	"SYSTEM_PROPERTY_DELETE_SUCCRSS":"删除成功",
	"SYSTEM_PROPERTY_DISABLE":"禁用属性不能设置属性可选值",
	"SYSTEM_PROPERTY_SELECT":"请选择一条属性",
	"SYSTEM_PROPERTY_INDUSTY_LIST":"行业属性列表",
	"SYSTEM_PROPERTY_SELECT_INDUSTRY":"请选择要修改的节点",
	"SYSTEM_PROPERTY_EXIST":"该属性已经存在",
	"SYSTEM_PROPERTY_SUCCESS":"操作成功",
	"SYSTEM_PROPERTY_NAME":"请填写属性名称",
	"SYSTEM_PROPERTY_DISABLE_SUCCRSS":"禁用成功",
	"SYSTEM_PROPERTY_INSERT_SUCCRSS":"新增成功",
	"SYSTEM_PROPERTY_SORT_SUCCRSS":"更新排序成功",
	"SYSTEM_PROPERTY_ERROR":"系统异常",
	"SYSTEM_PROPERTY_SORT_ERROR":"当前有新增并且未定义的属性",
	"SYSTEM_PROPERTY_NO_SORT":"当前没有属性",
	"SYSTEM_PROPERTY_NO_SAVE":"未定义属性是否保存",
	"SYSTEM_UNKNOW_PROPERYY":"未定义属性不能设置属性可选值",
	"SYSTEM_COLOR_PROPERTY_EXIST":"一个行业中最多只能有一个颜色属性",
	"SYSTEM_SALE_PROPERTY_EXIST":"一个行业中最多只能有二个销售属性",
	"SYSTEM_PROPERTY_VALUE_EXIT":"属性值已经存在!",
	"SYSTEM_PROPERTY_RELATION_REPEAT":"不能重复关联同一个通用属性!",
	"SYSTEM_PROPERTY_NAME_EXIT":"属性名称已存在!",
	"SYSTEM_PROPERTY_CHECK_ERROR":"验证异常!",
	"SYSTEM_PROPERTY_REPEAT":"属性名称不能重复,且不能重复关联同一个通用属性!",
	"SYSTEM_PROPERTY_COMMONPROPERTYNAME_REPEAT":"通用属性名称已存在!",
	"SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS":"属性名称不能为空!"
});

var findPropertyListByIndustryIdUrl = base + '/property/nebulaFindPropertyList.json';
//行业中颜色属性的属性id
var colorPropPropertyId = 0;
//行业中销售属性的属性id集合
var salePropPIds = new Array();


function validatePropertyName(propertyId,name){
	var json={"propertyId":propertyId,"name":name};
	var result = loxia.syncXhr(base+'/shop/nebulaValidatePropertyName.json', json,{type: "GET"});
	if(result.isSuccess!=undefined && result.isSuccess== true){
		return true;
	}else{
		$j("input[name='name']").focus();
		return false;
	}
}

function validatePropertyNameMutlLang(propertyId,name,lang){
	var json={"propertyId":propertyId,"name":name,"lang":lang};
	var result = loxia.syncXhr(base+'/shop/nebulaValidatePropertyName.json', json,{type: "GET"});
	if(result.isSuccess!=undefined && result.isSuccess== true){
		return true;
	}else{
		$j("input[name='name']").focus();
		return false;
	}
}

/**
 * 编辑类型格式
 * @param val
 * @returns {String}
 */
function formatEditingType(val) {
	if (val == 1) {
		return nps.i18n("VALUE_EDIT_DH");
	} else if (val == 2) {
		return nps.i18n("VALUE_EDIT_KSRDX");
	} else if (val == 3) {
		return nps.i18n("VALUE_EDIT_DANX");
	} else if(val == 4){
		return nps.i18n("VALUE_EDIT_DX");
	}else {
		return nps.i18n("VALUE_EDIT_ZDYDX");
	}
}


/**
 * 编辑类型为多选、多选输入时，才可以设置 是否销售属性(不能选时默认为否)，是否颜色属性(不能选时默认为否)、是否配图(不能选时默认为否)
 */
function setSelectCondition(obj){
	var _value = obj.val();
	var $isSaleProp = $j('#isSaleProp');
	var $isColorProp = $j('#isColorProp');
	var $hasThumb = $j('#hasThumb');
	var $valueType = $j('#valueType');
	var $required = $j('#required');
	if(_value == 2 || _value == 3){
		$required.attr('value', 'true').attr('disabled', 'disabled');
	}else{
		$required.removeAttr('disabled');
	}
	
	if(!(_value == 4 || _value == 5)){
		$isSaleProp.attr('value', 'false').attr('disabled', 'disabled');
		$isColorProp.attr('value', 'false').attr('disabled', 'disabled');
		$hasThumb.attr('value', 'false').attr('disabled', 'disabled');
	}else{
		$isSaleProp.removeAttr('disabled');
		$hasThumb.removeAttr('disabled');
		var isSaleProp = $isSaleProp.val();
		if(isSaleProp == 'true'){
			$isColorProp.removeAttr('disabled');
			$required.attr('value', 'true').attr('disabled', 'disabled');
		}else{
			$isColorProp.attr('value', 'false').attr('disabled', 'disabled');
			$required.removeAttr('disabled');
		}
	}
	
	//当编辑类型为单行输入(1)或自定义多选(5)时 "设置属性可选值"按钮隐藏
	if(_value == 1 || _value == 5){
		$j('#propertyValue_button').hide();
	}else{
		$j('#propertyValue_button').show();
	}
	
	if(_value == 3 || _value == 4 || _value == 5){
		$valueType.attr('value', '1').attr('disabled', 'disabled');
	}else{
		$valueType.removeAttr('disabled');
	}

}

/**
 * 只允许0到1个颜色属性
 */
function isColorPropOnlyOne(){
	var isExistColorProp = false;
	var result = loxia.syncXhr(findPropertyListByIndustryIdUrl, {}, {type:'GET'});
	if(result.isSuccess){
		var propertyList = result.description;
		for(var i in propertyList){
			if(propertyList[i].isColorProp){
				colorPropPropertyId = propertyList[i].id;
				isExistColorProp = true;
			}
		}
	}
	return isExistColorProp;
}

/**
 * 只允许有0到2个销售属性
 */
function havaIsSalePropCount(){
	var result = loxia.syncXhr(findPropertyListByIndustryIdUrl, {}, {type:'GET'});
	var count = 0;
	if(result.isSuccess){
		var propertyList = result.description;
		for(var i in propertyList){
			if(propertyList[i].isSaleProp){
				salePropPIds[count] = propertyList[i].id;
				count++;
			}
		}
	}
	return count;
}


$j(document).ready(function(){
	
	//编辑类型的选择
	$j('#editingType').on('change', function(){
		setSelectCondition($j(this));
	});
	
	/**
	 * 返回按钮
	 */
	$j(".return_button").click(function() {
		window.location.href="/property/nebulaPropertyList.htm";
	});
	
	//颜色属性必须配图
	$j("#isColorProp").change(function(){		
		if( $j("#isColorProp").attr("value")=="true"){
			$j("#hasThumb").attr("value","true");
			$j("#isSaleProp").attr("value","true");
		}
	});

	
	$j("#isSaleProp").change(function(){
		var $isSaleProp = $j('#isSaleProp');
		var $isColorProp = $j('#isColorProp');
		var $required = $j('#required');
		var isSaleProp = $isSaleProp.val();
		//多选和自定义多选时, 销售属性为是时, 颜色属性可选 , 否则为否
		//多选和自定义多选时, 销售属性为是时, 必填可选 , 否则为否
		if(isSaleProp == 'true'){
			$isColorProp.removeAttr('disabled');
			$required.attr('value', 'true').attr('disabled', 'disabled');
		}else{
			$isColorProp.attr('value', 'false').attr('disabled', 'disabled');
			$required.removeAttr('disabled');
		}
	});
});
var usertype=false;//默认不需要验证通用属性名是否重复


function setempty(){
	refreshRight();
	$j("#commonname").text("");
	$j("#commonname").hide("");
	$j("#commonnameinput").show("");
//	$j("#commonnameselect").show("");
	$j("#Industrys").text("");
	$j("#commonPropertyId").val("");
	//$j("#useTypediv").hide();
//	$j("#useTypediv").attr("showstatus",false);
	$j("input[name='groupName']").val("");
}
function selectvalue(obj){
	usertype=false;
	
	var selecttext=$j(obj).val();
	$j("#commonnameinput").val(selecttext);
	$j("#commonname").text(selecttext);
	$j("#proname").val(selecttext);
	
	var selectoption=$j(obj).find("option:selected");
	var editingType=selectoption.attr("editingType");
	var valueType=selectoption.attr("valueType");
	var isSaleProp=selectoption.attr("isSaleProp");
	var isColorProp=selectoption.attr("isColorProp");
	var industrylist=selectoption.attr("industrylist");
	var commonPropertyId=selectoption.attr("commonPropertyId");
		 
	$j("#editingType").attr("value",editingType);
	$j("#valueType").attr("value", valueType);
	$j("#isSaleProp").attr("value",isSaleProp);
	$j("#isColorProp").attr("value",isColorProp);
	$j("#Industrys").text(industrylist);
	$j("#commonPropertyId").val(commonPropertyId);
	setSelectCondition($j('#editingType'));
	
	$j("#commonnameinput").removeClass("ui-loxia-error");
	$j(".error-information").hide();
}
function inputchange(obj){
	if($j(obj).val()=="choose"){
//		changeconmonname();
		$j("#commonnameselect").css({ "visibility": ""});
	}else{
		$j("#commonnameselect").css({ "visibility": "hidden"});
	}
	$j("#commonnameinput").val("");
	$j("#commonname").text("");
	$j("#Industrys").text("");
	$j("#proname").val("");
	$j("#commonPropertyId").val("");
	$j("input[name='groupName']").val("");
	$j("#commonnameselect").val("");
}
function conmonnamechange(){
	if($j.trim($j("#commonnameinput").val())==""){
		nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS"));
		$j("#commonnameinput").addClass("ui-loxia-error");
		$j("#commonnameinput").focus();
		return false;
	}else{
		$j("#commonnameinput").removeClass("ui-loxia-error");
		$j(".error-information").hide();
	}
	usertype=true;
	validateconmonname();
	var selecttext=$j("#commonnameinput").val();
	$j("#proname").val(selecttext);
	$j("#commonnameselect").val("");
}

function  editotherdata(){
}




