$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	"PROPERT_CONFIRM_DISABLE":"确定要禁用该属性么？"
});

$j(document).ready(function(){
	
	//禁用按钮
	$j(".button-line .button.orange.add:eq(2)").click(function(){	
		var propertyId = $j("#propertyId").val();
		if(propertyId){
			nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DISABLE"),function(){
				var json={"propertyId":propertyId,"state":0};
			  	nps.asyncXhrGet(base+'/property/nebulaEnableOrDisableproperty.json', json,{successHandler:function(data, textStatus){
			  		var backWarnEntity = data;
			  		if(backWarnEntity.isSuccess){
				  		//禁用成功
				  		nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DISABLE_SUCCRSS"));
				  		$j(".button-line .button.orange.add:eq(2)").hide();
				  	}
			  	}}); 
			});
		}
	});	
	
	
	//设置属性按钮
	$j("#propertyValue_button").click(function() {
		if($j("#propertyId").val()==""){
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_UNKNOW_PROPERYY"));	
		}else{
			window.location.href=base+"/i18n/property/editPropertyValue.htm?propertyId="+$j("#propertyId").val();;
		}
	});
	
	//修改按钮
	$j(".button-line .button.orange.add:eq(1)").click(function(){
		var lifecycle=$j("#lifecycle").val();
		commonUpdateProperty(lifecycle);
	});	
	//修改并启用按钮
	$j(".button-line .button.orange.add:eq(0)").click(function(){
		//将lifecycle设为1
		var lifecycle=1;
		commonUpdateProperty(lifecycle);
	});
});

/**
 * @param lifecycle 生命周期
 */
function commonUpdateProperty(lifecycle){
	if(lifecycle==null||lifecycle==''||lifecycle==undefined){
		lifecycle=$j("#lifecycle").val();
	}
	
	var propertyId=$j("#propertyId").val();
	
	//确认是否修改
	nps.confirm("确认信息","确定修改属性信息？",function(){
		//验证名称不能为空
		var name=$j("#name").val();
		if($j.trim(name)==""){
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS"));
			return;
		}else{
			$j("#name").removeClass("ui-loxia-error");
			$j(".error-information").hide();
		}
		
		if(i18nOnOff){
			var flag=false;
			$j("input[name='name']").each(function(i,dom){
				if($j.trim($j(dom).val())==""){
					flag=true;
					return false;
				}
			});
			
			if(flag){
				nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_COMMONPROPERTYNAME_EMPTYSTATUS"));
				return;
			}
		}
		
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
		
		
		if(name!=""){
			var originalName =null;
			var defualt = "";
			var json={};
			if(i18nOnOff){
				var multlangs = '{"property.id":'+propertyId;
		    	multlangs += ',"property.editingType":"' +  $j("#editingType").attr("value")+'"';
		    	multlangs += ',"property.isSaleProp":"' + $j("#isSaleProp").attr("value")+'"';
		    	multlangs += ',"property.valueType":"' + $j("#valueType").attr("value")+'"';
		    	multlangs += ',"property.isColorProp":"' + $j("#isColorProp").attr("value")+'"';
		    	multlangs += ',"property.required":"' + $j("#required").attr("value")+'"';
		    	multlangs += ',"property.searchable":"' + $j("#searchable").attr("value")+'"';
		    	multlangs += ',"property.lifecycle":"' + lifecycle+'"';
		    	var exit = false;
		    	var validate = false;
		    	$j(".mutl-lang-name").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if (null == val || "" == val) {
		    			validate = true;
		  		    }
					if(!validatePropertyNameMutlLang(propertyId,val,lang)){
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
				var name =  $j("#name").val();
				var groupName = $j("#groupName").val();
				
				if (null == name || "" == name) {
					nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		    		return;
	  		    }
				
				defualt = name;				
				if(!validatePropertyName(propertyId,name)){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
					return false;
				}
				json={
					    "property.id": propertyId,
					    "property.name.value" : name,
				        "property.name.lang" : "zh_cn",
					    "property.editingType": $j("#editingType").attr("value"),
					    "property.valueType": $j("#valueType").attr("value"),
					    "property.isSaleProp": $j("#isSaleProp").attr("value"),
					    "property.isColorProp": $j("#isColorProp").attr("value"),
					    "property.required": $j("#required").attr("value"),
					    "property.searchable": $j("#searchable").attr("value"),
					    "property.lifecycle": lifecycle,
					    "property.groupName.value" : groupName,
				        "property.groupName.lang" : "zh_cn",
				};
			}
			
			var backWarnEntity = loxia.syncXhr(base+'/i18n/property/nebulaSaveProperty.Json', json,{type: "POST"});
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description;
  				
  				var editingType=$j("#editingType").attr("value");
  				if(editingType==1||editingType==5){
  					window.location.href=base+"/property/nebulaPropertyList.htm";
  				}else{
  					window.location.href=base+"/i18n/property/editPropertyValue.htm?propertyId="+property.id;
  				}
  			}
		}else{
			nps.error(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}
	});
}
