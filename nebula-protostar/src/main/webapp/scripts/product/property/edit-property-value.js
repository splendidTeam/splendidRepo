$j.extend(loxia.regional['zh-CN'],{
	"PV_PLEASE_SELECT_GROUP":"请选择属性值组！",
	"PROPERTY_VALUE_NEEDS_INPUT":"需要输入属性值！",
	"PROPERTY_VALUE_NAME":"属性值名"
});


function refreshData(){
	$j("#table2").loxiasimpletable("refresh");
}


function drawCheckbox(data, args, idx){
	return "<input type='checkbox' name='id' value='" + loxia.getObject("id", data)+"'/>";
}


function drawProValue(data, args, idx){
//	var target = loxia.getObject("value",data);
	console.log(data)
	var html="<div class='ui-block-line propertyValueTabelTd' style='padding-top:0px;' target='"+JSON.stringify(data)+"'>";
	for ( var j = 0; j < i18nLangs.length; j++) {
		var i18nLang = i18nLangs[j];
		var key = i18nLang.key;
		html += "<input  readonly='value' value="+ data.value.langValues[key] +"  style='width: 100px'  class='name' loxiaType='input' /><span>"+i18nLang.value +"</span>";
		if(j!=i18nLangs.length){
			html += '<br>';
		}
	}
	html += '</div>';
	return html;
}


var propertyValuePages = base + '/i18n/property/findPropertyValueByPage.json';

$j(document).ready(function(){
	var _propertyId = $j('#propertyId').val();
	var _selectedGroupId = $j('.selectedGroupId').find("option:selected").val();
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	/**
	 * 切换属性值组的时候动态给GroupID赋值
	 */
	$j('.selectedGroupId').change(function(){
		var groupid = $j(this).val();
		$j('#groupId').val(groupid);
		_selectedGroupId = groupid;
		alert(_selectedGroupId)
		refreshData();
	});
	
	/**
	 * 添加属性值组
	 */
	$j('.addPropertyValueGroup').click(function(data){
		var propertyId = $j('#propertyId').val();
		var selectedGroupId = $j('.selectedGroupId').find("option:selected").val();
		console.log(propertyId, selectedGroupId);
		window.location.href = base + "/i18n/property/showAddOrUpdateGroup.htm?propertyId="+propertyId +"&groupId="+selectedGroupId +"&type=0";
	});
	

	/**
	 * 修改属性值组
	 */
	$j('.updatePropertyValueGroup').click(function(data){
		var propertyId = $j('#propertyId').val();
		var selectedGroupId = $j('.selectedGroupId').find("option:selected").val();
		
		if(null == selectedGroupId || "" == selectedGroupId || undefined == selectedGroupId){
			$j('.selectedGroupId').focus();
			nps.error(nps.i18n("ERROR_INFO"), nps.i18n("PV_PLEASE_SELECT_GROUP"));
			return;
		}
		
		window.location.href = base + "/i18n/property/showAddOrUpdateGroup.htm?propertyId="+propertyId +"&groupId="+selectedGroupId +"&type=1";
	});
	
	
	/**
	 * 保存属性值
	 */
	$j('.savePropertyValue').click(function(){
		
		for ( var j = 0; j < i18nLangs.length; j++) {
			var i18nLang = i18nLangs[j];
			var key = i18nLang.key;
			$j("input[id='input3'][lang='"+key+"']").each(function(indexi,datai){
	    		var input = $j(this).val();
	    		if(input == "" || undefined == input){
	    			$j(this).addClass("ui-loxia-error");
	    			nps.error(nps.i18n("ERROR_INFO"), nps.i18n("PROPERTY_VALUE_NEEDS_INPUT"));
	    		}
	    	});
	    	
    	}
		
		nps.submitForm('propertyForm',{mode:'async',
			successHandler : function(data){
				if(data.isSuccess){
//					nps.info(nps.i18n("info"),nps.i18n("successful-operation"));	
					refreshData();
				}
		   }});
	});

	/**
	 * 【属性值列表】中的属性值点击事件
	 */
	$j('.propertyValueTabelTd').live('click',function(){
		
		//tdData : {"value":{"defaultValue":"简体中文","values":["简体中文","english"],"langValues":{"en_UK":"english","zh_CN":"简体中文"},"langs":["zh_CN","en_UK"]},
		//		"id":17,"version":1460449323185,"modifyTime":1460449317569,"createTime":1460449317569,"sortNo":1,"propertyId":1,"thumb":null}
		
		var tdData =  jQuery.parseJSON($j(this).attr('target'));
//		console.log(tdData);
		$j("input[name='propertyValues.id']").val(tdData.id);
		$j("input[name='propertyValues.propertyId']").val(tdData.propertyId);
		$j("input[name='propertyValues.sortNo']").val(tdData.sortNo);
		
		$j('.propertyValueInput').each(function(index,data){
			var targetElement = $j(this);
			var _lang = targetElement.find("input[name='propertyValues.value.langs["+ index+"]']").val();
			console.log(tdData.value.langValues[_lang]);
			targetElement.find("input[id='input3']").val(tdData.value.langValues[_lang]);
		});
		
	});
	
	
	$j("#table2").loxiasimpletable({
		page : true,
		size : 1500000,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckbox"
		}, {
			name : "value",
			label : nps.i18n("PROPERTY_VALUE_NAME"),
			width : "10%",
			template : "drawProValue"
			
		}],
		dataurl : propertyValuePages +"?propertyId=" + _propertyId +"&proValueGroupId=" + _selectedGroupId 
	});
	refreshData();
	
	
	
	
});