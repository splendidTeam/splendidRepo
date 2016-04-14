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
	"SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS":"通用属性名称不能为空!",
	"SYSTEM_PROPERTY_ISEXIT_INITEM":"有商品已关联此属性！",
	"SYSTEM_PROPERTY_DELETE_ERROR":"删除属性失败！",
	"SYSTEM_INDUSTRY_PROPERTY_RELATION_ERROR":"行业属性关联失败",
	"SYSTEM_PROPERTY_SORT_FAIL":"更新排序失败"
});

var setting = {
		
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		view: {
			showIcon:false,
			fontCss: getFontCss
		},
		edit: {
			enable: false,
			showRenameBtn: false
		},
		data: {
			keep: {
				parent:false,
				leaf:false
			},
			key: {
				title: "name"
			},
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: zTreeOnClick
		}
	};

var industryId;
var jsonArray2;
$j(document).ready(function(){
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	var booleanFlag = true;
	for(var i = 0;i<nodes.length;i++){
		if(nodes[i].isParent){//checked:true
			nodes[i].nocheck = true;
		}else{
			if(booleanFlag){
				nodes[i].checked = true;
				//onCheck(event, treeId, nodes[i]);
				firstNodeId=nodes[i].id;
				firstNodeName=nodes[i].name;
				booleanFlag = false;
			}
		}
		treeObj.refresh();
	}
	
	$j(".ui-block-content-lb .button-line1 .buttonSort").click(function(){
		var lilength = $j(".list-all.ui-sortable li").length;
		var l = jsonArray2.length;
		if(l>0){
			if(lilength==l){
				var ain=1;
				$j(".list-all li").each(function(){
					var scnum=parseInt($j(this).attr("scn"));
					jsonArray2[scnum].num=ain;
					ain++;
				});	
				var str ="" ;
				for(var i=0;i<jsonArray2.length;i++){
					jsonArray2[i].sortNo = jsonArray2[i].num;
					str += "propertyId"+newarray[i].id+"sortNo"+newarray[i].num+",";
				}
				var json ={"ids":str,"industryId":industryId};
				var backWarnEntity = loxia.syncXhr(base+'/industry/updateIndustryPropertySortBylist.json',json,{type: "GET"});
				if(backWarnEntity.isSuccess){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_SUCCRSS"));
					
				}else{
					if(backWarnEntity.errorCode == 2){
						nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_FAIL"));
					}else{
						nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_ERROR"));
					}
				}
			}
		}
	});
	getProperty();
});

function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}

function getProperty(){
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.getCheckedNodes(true);
	if (nodes.length>0) {
		industryId =nodes[0].id;
		var name = nodes[0].name;
		
		var json={"industryId":industryId};
  		var backWarnEntity = loxia.syncXhr(base+'/i18n/industry/propertyListByIndustryid.json', json,{type: "GET"});
  		if(backWarnEntity.isSuccess){
  			$j("#industryId").val(industryId);
			$j("#industryPropertyDiv").html(name+nps.i18n("SYSTEM_PROPERTY_INDUSTY_LIST"));
			jsonArray2 = backWarnEntity.description.selected;
			var jsonArray1 = backWarnEntity.description.enableSelect;
			showIndustyProperty(jsonArray2);
			showEnableSelectProperty(jsonArray1);
  		 }else{
  			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_ERROR"));
  		 }
	}else{
		  nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SELECT_INDUSTRY"));
	}
}

function showIndustyProperty(array1) {
	$j.each(array1,function(i,val){
		array1[i].state = parseInt(array1[i].lifecycle);
		array1[i].num = parseInt(array1[i].sortNo);
		if(i18nOnOff){
			var name = array1[i].name;
			if(name!=null){
				array1[i].text = name.defaultValue;
			}else{
				array1[i].text = "";
			}
		}else{
			var name = array1[i].name;
			if(name!=null){
				array1[i].text = name.value;
			}else{
				array1[i].text = "";
			}
			
		}
		array1[i].type = formatEditingType(array1[i].editingType);
	});
	
	$j("#property-list li").remove();
	
	$j("#property-list").dragarraylist({
		newarray:array1
		, add:false
		, callBack:function(o){
		},deleteli:function(o){
		    deleteIndustryProperty(o);
		}
	});
}
function showEnableSelectProperty(array2){
	$j('#enableSelectProperty').empty();
	$j.each(array2,function(i,val){
		array2[i].state = parseInt(array2[i].lifecycle);
		array2[i].num = parseInt(array2[i].sortNo);
		if(i18nOnOff){
			var name = array2[i].name;
			if(name!=null){
				array2[i].text = name.defaultValue;
			}else{
				array2[i].text = "";
			}
		}else{
			var name = array2[i].name;
			if(name!=null){
				array2[i].text = name.value;
			}else{
				array2[i].text = "";
			}
			
		}
		array2[i].type = formatEditingType(array2[i].editingType);
		$j('#enableSelectProperty').append('<div><input name="propertyList" class="enableSelectPropertyList" type="checkbox" value='+array2[i].id +" />" + array2[i].text);
	});
	$j(".enableSelectPropertyList").change(function(){
		if($j(".enableSelectPropertyList").is(':checked')){
			var propertyId =  $j('input[name="propertyList"]:checked').val();
			var json={"propertyId":propertyId,"industryId":industryId};
	  		var backWarnEntity = loxia.syncXhr(base+'/industry/saveIndustryProperty.json', json,{type: "GET"});
	  		if(backWarnEntity.isSuccess){
	  			getProperty();
	  		}else{
	  			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_INDUSTRY_PROPERTY_RELATION_ERROR"));
	  		}
		}
	});
}

function deleteIndustryProperty(o){
	if(o && o.id!=""){
		var propertyId = o.id;
		var json={"propertyId":propertyId,"industryId":industryId};
		var backWarnEntity = loxia.syncXhr(base+'/industry/deleteIndustryProperty.json', json,{type: "GET"});
		if(backWarnEntity.isSuccess){
  			getProperty();
  		}else{
  			if(backWarnEntity.errorCode == 1){
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_ISEXIT_INITEM"));
  			}else{
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DELETE_ERROR"));
  			}
  		}
	}
}

function zTreeOnClick(event, treeId, treeNode) {
	getProperty();
}
