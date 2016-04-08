$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
    "PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的属性么？",
    "PROPERT_STATE_ENABLE":"有效",
    "PROPERT_STATE_DISABLE":"无效",
    "PROPERT_CONFIRM_ENABLE":"确定要启用该属性么？",
    "PROPERT_CONFIRM_DISABLE":"确定要禁用该属性么？",
    "NO_DATA":"未找到数据",
    "VALUE_NO_EFFECT":"无效",
    "VALUE_EFFECTIVE":"有效",
    "VALUE_INDUSTRY_PROPERTY":"行业属性",
    "VALUE_SHOP_PROPERTY":"店铺自定义属性",
    "VALUE_EDIT_DH":"单行输入",
    "VALUE_EDIT_KSRDX":"可输入单选",
    "VALUE_EDIT_ZDYDX":"自定义多选", 
    "VALUE_EDIT_DANX":"单选",
    "VALUE_EDIT_DX":"多选",
    "VALUE_TYPE_WB":"文本",
    "VALUE_TYPE_SZ":"数值",
    "VALUE_TYPE_RQ":"日期",
    "VALUE_TYPE_RQSJ":"日期时间",
    "VALUE_ISSELECT_NO":"否",
    "VALUE_ISSELECT_YES":"是",
    "PROPERTY_CLICK_EDIT":"修改",
    "PROPERTY_CLICK_START":"启用",
    "PROPERTY_CLICK_STOP":"禁用",
    "PROPERTY_CLICK_DELETE":"删除",
    "PROPERTY_CLICK_SETVALUE":"设置属性可选值",
    "INFO_DELETE_SUCCESS":"删除记录成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_STOP_SUCCESS":"禁用成功!",
    "INFO_TITLE_DATA":"提示信息",
    "INFO_STOP_FAIL":"禁用失败!",
    "LABEL_PROPERTY_NAME":"属性名称",
    "LABEL_PROPERTY_EDITTYPE":"编辑类型",
    "LABEL_PROPERTY_VALUETYPE":"值类型",
    "LABEL_PROPERTY_SALEPROP":"销售属性",
    "LABEL_PROPERTY_COLORPROP":"颜色属性",
    "LABEL_PROPERTY_REQUIRED":"必输属性",
    "LABEL_PROPERTY_SEARCHABLE":"是否检索",
    "LABEL_PROPERTY_HASTHUMB":"是否配图",
    "LABEL_PROPERTY_SORTNO":"顺序",
    "LABEL_PROPERTY_INDUSTRYNAME":"所属行业",
    "LABEL_PROPERTY_LIFECYCLE":"状态",
    "LABEL_PROPERTY_OPERATE":"操作",
    "LABEL_PROPERTY_GROUP":"所属分组"
});


$j(document).ready(function(){
	
	$j('.sumbmit').click(function(){
		var groupId = $j('.groupId').val();
		var groupName = $j('.groupName').val();
		
		
		
	});
	
	
	
	
	$j('.boundPropertyValue checkbox').live('on',function(){
		alert($j(this).text())
	});
	
	$j('.freePropertyValue checkbox').live('on',function(){
		alert($j(this).text())
	});
	
	
	
});