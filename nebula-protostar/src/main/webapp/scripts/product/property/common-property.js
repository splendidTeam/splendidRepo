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
	"SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS":"通用属性名称不能为空!"
});

var findPropertyListByIndustryIdUrl = base + '/property/propertyListByIndustryid.json';
//行业中颜色属性的属性id
var colorPropPropertyId = 0;
//行业中销售属性的属性id集合
var salePropPIds = new Array();
/**
 * 左侧拖拽条单击时触发setValue方法
 * @param o (callback回调的json数据)
 * @returns {String}
 */
function setValue(o){
	loxia.byId($j("input[name=name]")).state(null);
	if(o){
		if(o.state==0){
			$j(".button-line .button.orange.add:eq(0)").css("display","inline-block");
			$j(".button-line .button.orange.add:not(:eq(0))").css("display","none");	
		}else{
			$j(".button-line .button.orange.add:lt(2)").css("display","none");
			$j(".button-line .button.orange.add:gt(1)").css("display","inline-block");
		}
		$j("input[name='name']").val(o.name);
		$j("#editingType").attr("value",o.editingType);
		$j("#valueType").attr("value",o.valueType);
		$j("#isSaleProp").attr("value",o.isSaleProp);
		$j("#isColorProp").attr("value",o.isColorProp);
		$j("#required").attr("value",o.required);
		$j("#searchable").attr("value",o.searchable);
		$j("#hasThumb").attr("value",o.hasThumb);
		$j("#propertyId").val(o.id);
		$j("#sortNo").val(o.num);
		$j("input[name='groupName']").val(o.groupName);
	}else{
		if( $j(".ui-sortable li").hasClass('selected')){
			$j(".button-line .button.orange.add:eq(1)").css("display","inline-block");
			$j(".button-line .button.orange.add:not(:eq(1))").css("display","none");
		}else{
			$j(".button-line .button.orange.add").css("display","none");
		}
		$j("#propertyId").val("");
		refreshRight();
	}
	setSelectCondition($j('#editingType'));
}

function setMutlLangValue(o){
	loxia.byId($j("input[name=name]")).state(null);
	if(o){
		if(o.state==0){
			$j(".button-line .button.orange.add:eq(0)").css("display","inline-block");
			$j(".button-line .button.orange.add:not(:eq(0))").css("display","none");	
		}else{
			$j(".button-line .button.orange.add:lt(2)").css("display","none");
			$j(".button-line .button.orange.add:gt(1)").css("display","inline-block");
		}
		$j("#editingType").attr("value",o.editingType);
		$j("#valueType").attr("value",o.valueType);
		$j("#isSaleProp").attr("value",o.isSaleProp);
		$j("#isColorProp").attr("value",o.isColorProp);
		$j("#required").attr("value",o.required);
		$j("#searchable").attr("value",o.searchable);
		$j("#hasThumb").attr("value",o.hasThumb);
		$j("#propertyId").val(o.id);
		$j("#sortNo").val(o.num);
		$j("#commonPropertyId").val(o.commonPropertyId);
		setindestys(o.industrylist);
		$j("#commonname").text(o.commonName);
		$j("#commonnameinput").val(o.commonName);
		$j("#commonnameinput").removeClass("ui-loxia-error");
		$j(".error-information").hide();
//		editotherdata();
		var name = o.name;
		var groupName = o.groupName;
		if(i18nOnOff){
			if(name!=null){
				$j(".mutl-lang-name").each(function(i,dom){
		    		var me = $j(this);
		    		var values = name.values;
		    		var langs = name.langs;
		    		me.val(values[i]);
		    		me.attr(langs[i]);
		    	});
			}else{
				$j(".mutl-lang-name").each(function(i,dom){
		    		var me = $j(this);
		    		me.val("");
		    	});
			}
			if(groupName!=null){
				$j(".mutl-lang-groupName").each(function(i,dom){
		    		var me = $j(this);
		    		var values = groupName.values;
		    		var langs = groupName.langs;
		    		me.val(values[i]);
		    		me.attr(langs[i]);
		    	});
			}else{
				$j(".mutl-lang-groupName").each(function(i,dom){
		    		var me = $j(this);
		    		me.val("");
		    	});
			}
		}else{
			$j("input[name='name']").val(o.name.value);
			$j("input[name='groupName']").val(o.groupName.value);
		}
	
	}else{
		if( $j(".ui-sortable li").hasClass('selected')){
			$j(".button-line .button.orange.add:eq(1)").css("display","inline-block");
			$j(".button-line .button.orange.add:not(:eq(1))").css("display","none");
		}else{
			$j(".button-line .button.orange.add").css("display","none");
		}
		$j("#propertyId").val("");
		refreshRight();
	}
	setSelectCondition($j('#editingType'));
}

function setindestys(industryList){
	var industrys="";
	$j.each(industryList,function(i,n){
		industrys+=(n.name+" , ");
	});
	industrys=industrys.substring(0, industrys.length-2);
//	industrys=industrys.substring(0, industrys.length-1);
	$j("#Industrys").text(industrys);
}
function beforeValue(){
//	$j("#useTypediv").attr("showstatus",true);
	nps.confirm(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NO_SAVE"), function(){
		
		var sortNo = $j(".list-all.ui-sortable li").length;
		var name =  $j("input[name='name']").val();
		var industryId = $j("#industryId").val();
		var groupName = $j('#groupName').val();
		
		if(name!=""&& industryId!="" && sortNo!=""){	
			
			//颜色属性0到1个
			var isColorProp = $j('#isColorProp').val();
			if(isColorProp == 'true'){
				var isExistColorProp = isColorPropOnlyOne();
				if(propertyId != colorPropPropertyId){
					if(isExistColorProp){
						nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_COLOR_PROPERTY_EXIST"));
						return;
					}
				}
			}
			
			//销售属性0到2个
//			var isSaleProp = $j('#isSaleProp').val();
//			if(isSaleProp == 'true'){
//				var isSalePropCount = havaIsSalePropCount();
//				for(var i in salePropPIds){
//					if(salePropPIds[i] != propertyId){
//						if(isSalePropCount > 2){
//							nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_SALE_PROPERTY_EXIST"));
//							return;
//						}
//					}
//				}
//			}

			if(!validatePropertyName(industryId,name)){
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
				return false;
			}
			
			var json={"id": -1, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1, "groupName":groupName };
			var backWarnEntity = loxia.syncXhr(base+'/property/saveProperty.Json', json,{type: "GET"});
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description;
  				property.state = parseInt(property.lifecycle);
  				property.num = parseInt(property.sortNo);
  				property.text = property.name;
  				property.type = formatEditingType(parseInt($j("#editingType").attr("value")));
  				newarray.push(property);
  				
  				$j("#property-list li,.new-list-add").remove();
  				$j("#property-list").dragarraylist({
  					newarray:newarray
  					, add:true
  					, callBack:function(o){
  						setMutlLangValue(o);
  					},deleteli:function(o){
  					     deleteValue(o);
  					},beforecallBack:function(o){
  						beforeValue(o);
  					}
  				});
  				
  				$j(".ui-sortable #"+property.id).click();
  				
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_INSERT_SUCCRSS"));
  			}
		}else{
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
		
	});
}

/**
 * 左侧拖拽条删除时触发setValue方法
 * @param o (callback回调的json数据)
 * @returns {String}
 */
function deleteValue(o){
	if(o && o.id!=""){
	
		var propertyId = o.id;
		var json={"propertyId": propertyId ,"state":2};
	
		nps.asyncXhrPost(base+'/property/enableOrDisableproperty.json', json, {
		   successHandler : function(data, textStatus) {
  				if (data.success!=0) {
  					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DELETE_SUCCRSS"));	
  					for(var i=0;i<newarray.length;i++){
  						if(newarray[i].id==propertyId){	
							newarray.splice(i,1);
							break;
						}
					}	
					
					$j("#property-list li,.new-list-add").remove();
					
					$j("#property-list").dragarraylist({
						newarray:newarray
						, add:true
						, callBack:function(o){
							setMutlLangValue(o);
						},deleteli:function(o){
			 			    deleteValue(o);
						},beforecallBack:function(o){
						beforeValue(o);
						}
					});
					
			
					var liid = $j("#idvalue").val();
					if(liid){
						$j(".ui-sortable #"+liid).click();
					}
  				} else {
  					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DELETE_SUCCRSS"));
  				}
  			}
		});
    }
    	 
    $j("#property-list li,.new-list-add").remove();
							
	$j("#property-list").dragarraylist({
		newarray:newarray
		, add:true
		, callBack:function(o){
			setMutlLangValue(o);
		},deleteli:function(o){
		    deleteValue(o);
		},beforecallBack:function(o){
			beforeValue(o);
	}
	});
	
	var liid = $j("#idvalue").val();
	if(liid){
		$j(".ui-sortable #"+liid).click();
	}
	window.location.reload(); 	
}

/**
 * 刷新右侧填选框的方法
 */
function refreshRight(){
	$j("input[name='name']").val("");
	$j("#editingType").attr("value",1);
	$j("#valueType").attr("value",1);
	$j("#isSaleProp").attr("value","false");
	$j("#isColorProp").attr("value","false");
	$j("#required").attr("value","false");
	$j("#searchable").attr("value","true");
	$j("#hasThumb").attr("value","false");
	$j("#propertyId").val("");
	$j("#sortNo").val("");
	$j("#gropName").val("");
}

function validatePropertyName(industryId,name){
	var json={"industryId":industryId,"name":name};
	var result = loxia.syncXhr(base+'/shop/validatePropertyName.json', json,{type: "GET"});
	if(result.isSuccess!=undefined && result.isSuccess== true){
		return true;
	}else{
		$j("input[name='name']").focus();
		return false;
	}
}

function validatePropertyNameMutlLang(industryId,name,lang){
	var json={"propertyId":industryId,"name":name,"lang":lang};
	var result = loxia.syncXhr(base+'/i18n/shop/validatePropertyName.json', json,{type: "GET"});
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

//修改商品属性值时, 同时jsonArray中商品属性值
function updateJsonArray(){
	var propertyId = $j('input[name="propertyId"]').val();
	//var obj;
	for(var i=0; i<jsonArray.length;i++){
		var id = jsonArray[i].id;
		if(id == propertyId){
			newarray[i].editingType = $j("#editingType").attr("value");
			newarray[i].editingType =  $j("#editingType").attr("value");
			newarray[i].valueType = $j("#valueType").attr("value");
			newarray[i].isSaleProp =$j("#isSaleProp").attr("value");
			newarray[i].isColorProp =$j("#isColorProp").attr("value");
			newarray[i].required =$j("#required").attr("value");
			newarray[i].searchable =$j("#searchable").attr("value");
			newarray[i].type = formatEditingType(parseInt($j("#editingType").attr("value")))  ;
			newarray[i].state =1;
			newarray[i].lifecycle=1;
			
			if(i18nOnOff){
				$j(".mutl-lang-name").each(function(j,dom){
					var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang == lang){
		    			newarray[i].name.defaultValue = val;
		    			newarray[i].text = val;
		    			newarray[i].title= val;
		    		}
		    		newarray[i].name.values[j] = val;
	    			newarray[i].name.langs[j] = lang;
				});
				$j(".mutl-lang-groupName").each(function(k,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang == lang){
		    			newarray[i].groupName.defaultValue = val;
		    		}
		    		newarray[i].groupName.values[k] = val;
		    		newarray[i].groupName.langs[k] = lang;
				});
			} else {
				var name = $j('input[name="name"]').val();
				newarray[i].name = name;
				newarray[i].text = name;
				newarray[i].title= name;
				newarray[i].groupName=$j('input[name="groupName"]').val();
			}
			
			break;
		}
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
	var industryId = $j('#industryId').val();
	var json = {'industryId': industryId};
	var isExistColorProp = false;
	var result = loxia.syncXhr(findPropertyListByIndustryIdUrl, json, {type:'GET'});
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
	var industryId = $j('#industryId').val();
	var json = {'industryId': industryId};
	var result = loxia.syncXhr(findPropertyListByIndustryIdUrl, json, {type:'GET'});
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
	
	/**
	 * 返回按钮
	 */
	$j(".return_button").click(function() {
		window.location.href="/property/propertyList.htm?keepfilter=true";
	});
	
	/**
	 * 设置属性按钮
	 */
	$j("#propertyValue_button").click(function() {
		if( $j(".ui-sortable li").hasClass('selected')){
			if($j("#propertyId").val()==""){
				nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_UNKNOW_PROPERYY"));	
			}else if($j(".ui-sortable li").hasClass('no-element selected')){
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DISABLE"));	
			}else{
				window.location.href=base+"/i18n/property/propertyValueList.json?propertyId="+$j("#propertyId").val();;
			}
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SELECT"));
		}
	});
	
	/**
	 * 下一步按钮
	 */
	$j(".button.orange.comfirm").click(function(){		
		var zTree = $j.fn.zTree.getZTreeObj("tree");
		var nodes = zTree.getCheckedNodes(true);
		if (nodes.length>0) {
			var industryId =nodes[0].id;
			var name = nodes[0].name;
			
			var json={"industryId":industryId};
	  		var backWarnEntity = loxia.syncXhr(base+'/i18n/property/propertyListByIndustryid.json', json,{type: "GET"});
	  		if(backWarnEntity.isSuccess){
	  			$j("#industryId").val(industryId);
	  			$j("#industrySelDiv").css("display","none");
				$j("#propertySelDiv").css("display","block");
				$j("#industryPropertyDiv").html(name+nps.i18n("SYSTEM_PROPERTY_INDUSTY_LIST"));
				jsonArray = backWarnEntity.description  ;
				
				$j.each(jsonArray,function(i,val){
					jsonArray[i].state = parseInt(jsonArray[i].lifecycle);
					jsonArray[i].num = parseInt(jsonArray[i].sortNo);
					if(i18nOnOff){
						var name = jsonArray[i].name;
						if(name!=null){
							jsonArray[i].text = name.defaultValue;
						}else{
							jsonArray[i].text = "";
						}
					}else{
						var name = jsonArray[i].name;
						if(name!=null){
							jsonArray[i].text = name.value;
						}else{
							jsonArray[i].text = "";
						}
						
					}
					jsonArray[i].type = formatEditingType(jsonArray[i].editingType);
				});
				
				$j("#property-list").dragarraylist({
					newarray:jsonArray
					, add:true
					, callBack:function(o){
						setMutlLangValue(o);
					},deleteli:function(o){
					     deleteValue(o);
					},beforecallBack:function(o){
  						beforeValue(o);
  					}
				});
				$j(".new-list-add").click();
				//$j("ul#property-list>li:last").click();
				ajaxgetcommonpropertydate(industryId);
	  		 }
		}else{
			  nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SELECT_INDUSTRY"));
		}
	});	
	/**
	 * 修改按钮
	 */
	$j(".button-line .button.orange.add:eq(2)").click(function(){	
		var commonname=$j("#commonname").text();
		var commonnameinput=$j("#commonnameinput").val();
		if($j.trim(commonnameinput)==""){
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS"));
			$j("#commonnameinput").addClass("ui-loxia-error");
			$j("#commonnameinput").focus();
			return;
		}else{
			$j("#commonnameinput").removeClass("ui-loxia-error");
			$j(".error-information").hide();
		}
		if(submitvalidate()==false){
			return;
		}
		if(commonname==commonnameinput||$j("input[name='typeradio']:checked").val()=="add"){
			dowithdata();
		}else{
			nps.confirm("确认信息","确定将通用属性名'"+commonname+"'改为'"+commonnameinput+"'",function(){
				dowithdata();
				
			});
		}
		
	});	
	
	/**
	 * ajax获取通用属性表信息
	 */
	function ajaxgetcommonpropertydate(industryId){
		var json={"industryId":industryId};
  		var result = loxia.syncXhr(base+'/i18n/property/commonpropertyListByIndustryid.json', json,{type: "GET"});
  		if(result!=null){
  			$j.each(result,function (i,n){
  				var cloneselect=$j("#commonnameselectcolone option").clone();
  				cloneselect.text(n.name);
  				cloneselect.attr("value",n.name);
  				cloneselect.attr("editingType",n.editingType);
  				cloneselect.attr("valueType",n.valueType);
  				cloneselect.attr("isSaleProp",n.isSaleProp);
  				cloneselect.attr("isColorProp",n.isColorProp);
  				cloneselect.attr("industrylist",n.industrylist);
  				cloneselect.attr("commonPropertyId",n.id);
  				$j("#commonnameselect").append(cloneselect[0].outerHTML);
  			});
  		}
	}
	function dowithdata(){
		var propertyId = $j("#propertyId").val();
		var industryId = $j("#industryId").val();
		var sortNo = $j("#sortNo").val();
		//颜色属性0到1个
		var isColorProp = $j('#isColorProp').val();
		if(isColorProp == 'true'){
			var isExistColorProp = isColorPropOnlyOne();
			if(propertyId != colorPropPropertyId){
				if(isExistColorProp){
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_COLOR_PROPERTY_EXIST"));
					return;
				}
			}
		}
		
		//销售属性0到2个
//		var isSaleProp = $j('#isSaleProp').val();
//		if(isSaleProp == 'true'){
//			var isSalePropCount = havaIsSalePropCount();
//			for(var i in salePropPIds){
//				if(salePropPIds[i] != propertyId){
//					if(isSalePropCount > 2){
//						nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_SALE_PROPERTY_EXIST"));
//						return;
//					}
//				}
//			}
//		}
		
		if(propertyId!="" && industryId!="" && sortNo!=""){	
			var originalName =null;
			
			//var json={"id": propertyId, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1,"groupName":group};
			var json={};
			var defualt = "";
			if(i18nOnOff){
				var multlangs = '{"property.id":"'+ propertyId+'"';
				multlangs += ',"property.industryId":"' + industryId+'"';
				multlangs += ',"property.editingType":"' +  $j("#editingType").attr("value")+'"';
				multlangs += ',"property.isSaleProp":"' + $j("#isSaleProp").attr("value")+'"';
				multlangs += ',"property.valueType":"' + $j("#valueType").attr("value")+'"';
				multlangs += ',"property.isColorProp":"' + $j("#isColorProp").attr("value")+'"';
				multlangs += ',"property.required":"' + $j("#required").attr("value")+'"';
				multlangs += ',"property.searchable":"' + $j("#searchable").attr("value")+'"';
				multlangs += ',"property.hasThumb":"' + $j("#hasThumb").attr("value")+'"';
				multlangs += ',"property.sortNo":"' + sortNo+'"';
				multlangs += ',"property.lifecycle":"' + 1+'"';
				multlangs += ',"property.commonPropertyId":"' + $j("#commonPropertyId").attr("value")+'"';
				multlangs += ',"property.commonName":"' + $j("#commonnameinput").attr("value")+'"';
				multlangs += ',"property.usertype":"' + $j("input[name='typeradio']:checked").val()+'"';
				
				var exit = false;
				var validate = false;
				$j(".mutl-lang-name").each(function(i,dom){
					var me = $j(this);
					var val = me.val();
					var lang = me.attr("lang");
					if (null == val || "" == val) {
						validate = true;
					}
					for(var j=0;j<newarray.length;j++){
						if(newarray[j].id==propertyId){
							var name1 = newarray[j].name;
							if(name1==null){
								originalName= null;
							}else{
								if(typeof(name1) == "undefined"){
									originalName= null;
								}else{
									originalName = name1.defaultValue;
								}
							}
							break;
						}
					}
					if(originalName!=null && originalName!=val){
						if(!validatePropertyNameMutlLang(industryId,val,lang)){
							exit = true;
						}
					}
					if(defaultlang==lang){
						defualt = val;
					}
					multlangs+=',"property.name.values['+i+']":"'+val+'"';
					multlangs+=',"property.name.langs['+i+']":"'+lang+'"';
				});
				if(validate){
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
					return;
				}
				if(exit){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
					return;
				}
				$j(".mutl-lang-groupName").each(function(i,dom){
					var me = $j(this);
					var val = me.val();
					var lang = me.attr("lang");
					multlangs+=',"property.groupName.values['+i+']":"'+val+'"';
					multlangs+=',"property.groupName.langs['+i+']":"'+lang+'"';
				});
				
				multlangs+="}";
				json = eval('('+multlangs+')');
				
			}else{
				var name =  $j("input[name='name']").val();
				var group = $j('input[name="groupName"]').val();
				if (null == name || "" == name) {
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
					return;
				}
				defualt = name;
				for(var i=0;i<newarray.length;i++){
					if(newarray[i].id==propertyId){	
						originalName = newarray[i].name.value;
						break;
					}
				}	
				if(originalName!=name){
					if(!validatePropertyName(industryId,name)){
						nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
						return false;
					}
				}
				json = {
						"property.id": propertyId,
						"property.industryId": industryId,
						"property.name.value" : name,
						"property.name.lang" : "zh_cn",
						"property.editingType": $j("#editingType").attr("value"),
						"property.valueType": $j("#valueType").attr("value"),
						"property.isSaleProp": $j("#isSaleProp").attr("value"),
						"property.isColorProp": $j("#isColorProp").attr("value"),
						"property.required": $j("#required").attr("value"),
						"property.searchable": $j("#searchable").attr("value"),
						"property.hasThumb": $j("#hasThumb").attr("value"),
						"property.commonPropertyId": $j("#commonPropertyId").attr("value"),
						"property.sortNo": sortNo,
						"property.lifecycle": 1,
						"property.groupName.value" : group,
						"property.commonName" : $j("#commonnameinput").attr("value"),
						"property.usertype":$j("input[name='typeradio']:checked").val()+""
				};
			}
			
			var backWarnEntity = loxia.syncXhr(base+'/i18n/property/saveProperty.Json', json,{type: "POST"});
			if(backWarnEntity.isSuccess){
				//var property = backWarnEntity.description
				$j(".ui-sortable .selected .text").html(defualt);
				$j(".ui-sortable .selected .type").html(formatEditingType(parseInt($j("#editingType").attr("value"))));
				$j(".ui-sortable .selected .text").attr("title",defualt);
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SUCCESS"));
				
				updateJsonArray();
				
				$j("#property-list li,.new-list-add").remove();
				
				$j("#property-list").dragarraylist({
					newarray:newarray
					, add:true
					, callBack:function(o){
						setMutlLangValue(o);
					},deleteli:function(o){
						deleteValue(o);
					},beforecallBack:function(o){
						beforeValue(o);
					}
				});
				if($j(".ui-sortable #"+propertyId)){
					$j(".ui-sortable #"+propertyId).click();
				}
			}
		}else{
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
		window.location.reload();
	}
	/**
	 * 修改并启用按钮
	 */
	$j(".button-line .button.orange.add:eq(0)").click(function(){	
		if(submitvalidate()==false){
			return;
			}
		var propertyId = $j("#propertyId").val();
		var industryId = $j("#industryId").val();
		var sortNo = $j("#sortNo").val();
		var isColorProp = $j('#isColorProp').val();
		if(isColorProp == 'true'){
			var isExistColorProp = isColorPropOnlyOne();
			if(propertyId != colorPropPropertyId){
				if(isExistColorProp){
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_COLOR_PROPERTY_EXIST"));
					return;
				}
			}
		}
		
		//销售属性0到2个
//		var isSaleProp = $j('#isSaleProp').val();
//		if(isSaleProp == 'true'){
//			var isSalePropCount = havaIsSalePropCount();
//			for(var i in salePropPIds){
//				if(salePropPIds[i] != propertyId){
//					if(isSalePropCount > 2){
//						nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_SALE_PROPERTY_EXIST"));
//						return;
//					}
//				}
//			}
//		}
		
		if(propertyId!="" && industryId!="" && sortNo!=""){		
			var originalName = null;
			//var json={"id": propertyId, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),
			//"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":
			//$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),
			//"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1, "groupName":groupName };
			var json={};
			var defualt = "";
			if(i18nOnOff){
				var multlangs = '{"property.id":"'+ propertyId+'"';
		    	multlangs += ',"property.industryId":"' + industryId+'"';
		    	multlangs += ',"property.editingType":"' +  $j("#editingType").attr("value")+'"';
		    	multlangs += ',"property.isSaleProp":"' + $j("#isSaleProp").attr("value")+'"';
		    	multlangs += ',"property.valueType":"' + $j("#valueType").attr("value")+'"';
		    	multlangs += ',"property.isColorProp":"' + $j("#isColorProp").attr("value")+'"';
		    	multlangs += ',"property.required":"' + $j("#required").attr("value")+'"';
		    	multlangs += ',"property.searchable":"' + $j("#searchable").attr("value")+'"';
		    	multlangs += ',"property.hasThumb":"' + $j("#hasThumb").attr("value")+'"';
		    	multlangs += ',"property.sortNo":"' + sortNo+'"';
		    	multlangs += ',"property.lifecycle":"' + 1+'"';
		    	multlangs += ',"property.commonPropertyId":"' + $j("#commonPropertyId").attr("value")+'"';
				multlangs += ',"property.commonName":"' + $j("#commonnameinput").attr("value")+'"';
				multlangs += ',"property.usertype":"' + $j("input[name='typeradio']:checked").val()+'"';
		    	var exit = false;
		    	var validate = false;
		    	$j(".mutl-lang-name").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if (null == val || "" == val) {
		    			validate = true;
		  		    }
		    		for(var j=0;j<newarray.length;j++){
						if(newarray[j].id==propertyId){
							var name1 = newarray[j].name;
							if(name1==null){
								originalName= null;
							}else{
								if(typeof(name1) == "undefined"){
									originalName= null;
								}else{
									originalName = name1.defaultValue;
								}
							}
							break;
						}
					}
					if(originalName!=null && originalName!=val){
						if(!validatePropertyNameMutlLang(industryId,val,lang)){
							exit = true;
						}
					}
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"property.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"property.name.langs['+i+']":"'+lang+'"';
		    	});
		    	if(validate){
		    		nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		    		return;
		    	}
		    	if(exit){
		    		nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
		    		return;
		    	}
		    	$j(".mutl-lang-groupName").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"property.groupName.values['+i+']":"'+val+'"';
		    		multlangs+=',"property.groupName.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	multlangs+="}";
		    	json = eval('('+multlangs+')');
				
			}else{
				var name =  $j("input[name='name']").val();
				var groupName = $j('#groupName').val();
				if (null == name || "" == name) {
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		    		return;
	  		    }
				defualt = name;
				for(var i=0;i<newarray.length;i++){
					if(newarray[i].id==propertyId){	
						originalName = newarray[i].name.value;
						break;
					}
				}	
				if(originalName!=name){
					if(!validatePropertyName(industryId,name)){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
							return false;
					}
				}
				json = {
					    "property.id": propertyId,
					    "property.industryId": industryId,
					    "property.name.value" : name,
				        "property.name.lang" : "zh_cn",
					    "property.editingType": $j("#editingType").attr("value"),
					    "property.valueType": $j("#valueType").attr("value"),
					    "property.isSaleProp": $j("#isSaleProp").attr("value"),
					    "property.isColorProp": $j("#isColorProp").attr("value"),
					    "property.required": $j("#required").attr("value"),
					    "property.searchable": $j("#searchable").attr("value"),
					    "property.hasThumb": $j("#hasThumb").attr("value"),
					    "property.commonPropertyId": $j("#commonPropertyId").attr("value"),
					    "property.sortNo": sortNo,
					    "property.lifecycle": 1,
					    "property.groupName.value" : groupName,
				        "property.groupName.lang" : "zh_cn",
				        "property.commonName" : $j("#commonnameinput").attr("value"),
				        "property.usertype":$j("input[name='typeradio']:checked").val()+""
				        
					};
			}
			var backWarnEntity = loxia.syncXhr(base+'/i18n/property/saveProperty.Json', json,{type: "GET"});
  			
  			if(backWarnEntity.isSuccess){
  				//var property = backWarnEntity.description
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SUCCESS"));
  				
  				updateJsonArray();
  				
  				$j("#property-list li,.new-list-add").remove();
  				
  				$j("#property-list").dragarraylist({
  					newarray:newarray
  					, add:true
  					, callBack:function(o){
  						setMutlLangValue(o);
  					},deleteli:function(o){
  					     deleteValue(o);
  					},beforecallBack:function(o){
  						beforeValue(o);
  					}
  				});
  				if($j(".ui-sortable #"+propertyId)){
  					$j(".ui-sortable #"+propertyId).click();
  				}
  			}
		}else{
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
		
		window.location.reload();
	});		
	
	/**
	 * 禁用按钮
	 */	
	$j(".button-line .button.orange.add:eq(3)").click(function(){	
		var propertyId = $j("#propertyId").val();
		if(propertyId){
		var json={"propertyId": propertyId ,"state":0};
  		var _d = loxia.syncXhr(base+'/property/enableOrDisableproperty.json', json,{type: "GET"});
  		if (_d.success!=0) {
  			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DISABLE_SUCCRSS"))	;
  			for(var i=0;i<newarray.length;i++){
				if(newarray[i].id==propertyId){	
					newarray[i].state =0;
					$j("#property-list li,.new-list-add").remove();
					break;
				}
			}

  			$j("#property-list").dragarraylist({
				newarray:newarray
				, add:true
				, callBack:function(o){
					setMutlLangValue(o);
				},deleteli:function(o){
				     deleteValue(o);
				},beforecallBack:function(o){
  					beforeValue(o);
  				}
			});
  			if($j(".ui-sortable #"+propertyId)){
  			$j(".ui-sortable #"+propertyId).click();
  			}
  		 }
		}
	});	
		
	/**
	 * 新增并且启用按钮
	 */
	$j(".button-line .button.orange.add:eq(1)").click(function(){	
		var commonnameinput=$j("#commonnameinput").val();
		if($j.trim(commonnameinput)==""){
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS"));
			return;
		}else{
			$j("#commonnameinput").removeClass("ui-loxia-error");
			$j(".error-information").hide();
		}
		if(submitvalidate()==false){
			return;
		}else{
			$j(".error-information").hide();
		}
		var sortNo = $j(".list-all.ui-sortable li").length;
		var industryId = $j("#industryId").val();
		var isColorProp = $j('#isColorProp').val();
		if(isColorProp == 'true'){
			var isExistColorProp = isColorPropOnlyOne();
			if(propertyId != colorPropPropertyId){
				if(isExistColorProp){
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_COLOR_PROPERTY_EXIST"));
					return;
				}
			}
		}
		//销售属性0到2个
//		var isSaleProp = $j('#isSaleProp').val();
//		if(isSaleProp == 'true'){
//			var isSalePropCount = havaIsSalePropCount();
//			for(var i in salePropPIds){
//				if(salePropPIds[i] != propertyId){
//					if(isSalePropCount > 2){
//						nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_SALE_PROPERTY_EXIST"));
//						return;
//					}
//				}
//			}
//		}
		
		
		if(name!=""&& industryId!="" && sortNo!=""){	
			var originalName =null;
			var defualt = "";
			var json={};
			if(i18nOnOff){
				var multlangs = '{"property.id":"-1"';
		    	multlangs += ',"property.industryId":"' + industryId+'"';
		    	multlangs += ',"property.editingType":"' +  $j("#editingType").attr("value")+'"';
		    	multlangs += ',"property.isSaleProp":"' + $j("#isSaleProp").attr("value")+'"';
		    	multlangs += ',"property.valueType":"' + $j("#valueType").attr("value")+'"';
		    	multlangs += ',"property.isColorProp":"' + $j("#isColorProp").attr("value")+'"';
		    	multlangs += ',"property.required":"' + $j("#required").attr("value")+'"';
		    	multlangs += ',"property.searchable":"' + $j("#searchable").attr("value")+'"';
		    	multlangs += ',"property.hasThumb":"' + $j("#hasThumb").attr("value")+'"';
		    	multlangs += ',"property.sortNo":"' + sortNo+'"';
		    	multlangs += ',"property.lifecycle":"' + 1+'"';
		    	multlangs += ',"property.commonPropertyId":"' + $j("#commonPropertyId").attr("value")+'"';
				multlangs += ',"property.commonName":"' + $j("#commonnameinput").attr("value")+'"';
				multlangs += ',"property.usertype":"' + $j("input[name='typeradio']:checked").val()+'"';
		    	var exit = false;
		    	var validate = false;
		    	$j(".mutl-lang-name").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if (null == val || "" == val) {
		    			validate = true;
		  		    }
					if(!validatePropertyNameMutlLang(industryId,val,lang)){
						exit = true;
					}
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"property.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"property.name.langs['+i+']":"'+lang+'"';
		    	});
		    	if(validate){
		    		nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		    		return;
		    	}
		    	if(exit){
		    		nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
		    		return;
		    	}
		    	$j(".mutl-lang-groupName").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		multlangs+=',"property.groupName.values['+i+']":"'+val+'"';
		    		multlangs+=',"property.groupName.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	multlangs+="}";
		    	json = eval('('+multlangs+')');
				
			}else{
				var name =  $j("input[name='name']").val();
				var groupName = $j("#groupName").val();
				
				if (null == name || "" == name) {
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		    		return;
	  		    }
				
				defualt = name;
				
				if(!validatePropertyName(industryId,name)){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
					return false;
				}
				json={
					    "property.id": -1,
					    "property.industryId": industryId,
					    "property.name.value" : name,
				        "property.name.lang" : "zh_cn",
					    "property.editingType": $j("#editingType").attr("value"),
					    "property.valueType": $j("#valueType").attr("value"),
					    "property.isSaleProp": $j("#isSaleProp").attr("value"),
					    "property.isColorProp": $j("#isColorProp").attr("value"),
					    "property.required": $j("#required").attr("value"),
					    "property.searchable": $j("#searchable").attr("value"),
					    "property.hasThumb": $j("#hasThumb").attr("value"),
					    "property.commonPropertyId": $j("#commonPropertyId").attr("value"),
					    "property.sortNo": sortNo,
					    "property.lifecycle": 1,
					    "property.groupName.value" : groupName,
				        "property.groupName.lang" : "zh_cn",
				        "property.commonName" : $j("#commonnameinput").attr("value"),
				        "property.usertype":$j("input[name='typeradio']:checked").val()+""
			    	   
				};
			}
			var backWarnEntity = loxia.syncXhr(base+'/i18n/property/saveProperty.Json', json,{type: "GET"});
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description;
  				property.state = parseInt(property.lifecycle);
  				property.num = parseInt(property.sortNo);
  				property.text = defualt;
  				property.type = formatEditingType(parseInt($j("#editingType").attr("value")));
  				newarray.push(property);
  				
  				$j("#property-list li,.new-list-add").remove();
  				$j("#property-list").dragarraylist({
  					newarray:newarray
  					, add:true
  					, callBack:function(o){
  						setMutlLangValue(o);
  					},deleteli:function(o){
  					     deleteValue(o);
  					},beforecallBack:function(o){
  						beforeValue(o);
  					}
  				});
//  				if($j(".ui-sortable #"+property.id)){
//  						$j(".ui-sortable #"+property.id).click();
//  				}

  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_INSERT_SUCCRSS"));
  			}
		}else{
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
		window.location.reload();
	});
	
	/**
	 * 排序按钮
	 * 当前有未定义的属性时不允许排序
	 */
	$j(".ui-block-content-lb .button-line1 .button").click(function(){
		var lilength = $j(".list-all.ui-sortable li").length;
		var l = newarray.length;
		if(l>0){
			if(lilength==l){
				savelistarry();
			var str ="" ;
			for(var i=0;i<newarray.length;i++){
				newarray[i].sortNo = newarray[i].num;
				str += "propertyId"+newarray[i].id+"sortNo"+newarray[i].num+",";
			}
			var json ={"ids":str};
			var backWarnEntity = loxia.syncXhr(base+'/property/updatePropertyBylist.json',json,{type: "GET"});
			if(backWarnEntity.isSuccess){
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_SUCCRSS"));		
			}else{
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_ERROR"));				
			}
			}else{
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_ERROR"));	
			}
		}else if(lilength==1 && l==0){
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_ERROR"));
		}else{
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NO_SORT"));	
		}
	});	
	
	
	$j('#editingType').on('change', function(){
		setSelectCondition($j(this));
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
/**
 * 验证属性名称是否重复(true:不重复,验证通过)
 */
function validatename(){
	var industryId = $j("#industryId").val();
	var propertyId=$j("#propertyId").val();
	var proname=$j("#proname").val();
	var commonPropertyId=$j("#commonPropertyId").val();
	if(tovalidatePropertyName(industryId,commonPropertyId,propertyId,proname)==false){
		return false;
	}else{
		return true;
	}
}
/**
 * 验证属性是否重复(属性名称不能重复,其关联的公共属性也不能重复)
 * @param industryId
 * @param name
 * @returns {Boolean}
 */
function tovalidatePropertyName(industryId,commonPropertyId,propertyId,proname){
	var json={"industryId":industryId,"commonPropertyId":commonPropertyId,"propertyId":propertyId,"proname":proname};
	var result = loxia.syncXhr(base+'/property/validatePropertyData.json', json,{type: "GET"});
	if(result!=undefined && result!=null){
		if(result.repeatStatus==true){
			if(result.repeatType==0){
				nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_RELATION_REPEAT"));
			}else{
				nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME_EXIT"));
			}
			return false;
		}else{
			$j(".error-information").hide();
			return true;
		}
	}else{
		nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_CHECK_ERROR"));
		return false;
	}
}
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
/**
 * 验证通用属性名是否重复(true:验证通过)
 */
function validateconmonname(){
	var commonPropertyName=$j("#commonnameinput").val();
	var json={"commonPropertyName":commonPropertyName};
	var result = loxia.syncXhr(base+'/property/validatecommonPropertyName.json', json,{type: "GET"});
	if(result==false){
		nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_COMMONPROPERTYNAME_REPEAT"));
		$j("#commonnameinput").addClass("ui-loxia-error");
		$j("#commonnameinput").focus();
		return false;
	}else{
		$j("#commonnameinput").removeClass("ui-loxia-error");
		$j(".error-information").hide();
		return true;
	}
}
function submitvalidate(){
	if(usertype==true){
		return validatename()&&validateconmonname();
	}else{
		return validatename();
	}
	
}
function  editotherdata(){
}
//nps.info("qq","11");
//nps.confirm("qq","11",function(){
//	alert((11));
//});