$j.extend(loxia.regional['zh-CN'],{
	"PV_PLEASE_SELECT_GROUP":"请选择属性值组！",
	"PROPERTY_VALUE_NEEDS_INPUT":"需要输入属性值！",
	"PROPERTY_VALUE_NAME":"属性值名",
	"GROUP_TABLE_OPERATE":"操作",
	"INFO_TITLE_DATA":"系统提示"
});


function refreshData(){
	$j("#table2").loxiasimpletable("refresh");
}


function drawCheckbox(data, args, idx){
	return "<input type='checkbox' name='id' value='" + loxia.getObject("id", data)+"'/>";
}


function drawProValue(data, args, idx){
//	var target = loxia.getObject("value",data);
//	console.log(data)
	var html="<div class='ui-block-line propertyValueTabelTd' style='padding-top:0px;' target='"+JSON.stringify(data)+"'>";
	for ( var j = 0; j < i18nLangs.length; j++) {
		var i18nLang = i18nLangs[j];
		var key = i18nLang.key;
		html += "<input  readonly='value' value="+ data.value.langValues[key] +"  style='width: 150px'  class='name' loxiaType='input' /><span>"+i18nLang.value +"</span>";
		if(j!=i18nLangs.length){
			html += '<br>';
		}
	}
	html += '</div>';
	return html;
}

function editGroup(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button editPropertyValue' val='"+loxia.getObject("id", data)+"'>修改</a>" +
			"<a href='javascript:void(0);' class='func-button saveEditPropertyValue' style='display:none;' val='"+loxia.getObject("id", data)+"'>保存</a>";
}


function refreshPropertyValueSection(){
	$j('.propertyValueInput').each(function(index,data){
		var targetElement = $j(this);
		targetElement.find("input[id='input3']").val("");
	});
	
	$j("input[name='propertyValues.id']").val('');
	$j("input[name='propertyValues.sortNo']").val('');
}

var propertyValuePages = base + '/i18n/property/findPropertyValueByPage.json';

var findAllPropertyValueByPropertyId = base + '/i18n/property/findAllPropertyValueByPropertyId.json';

$j(document).ready(function(){
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
	 * 当选中了要修改的属性值之后又要新增属性值，点击此处
	 */
	$j('.refreshPropertyValue').click(function(){
		refreshPropertyValueSection();
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
//	    			$j(this).addClass("ui-loxia-error");
	    			nps.error(nps.i18n("ERROR_INFO"), nps.i18n("PROPERTY_VALUE_NEEDS_INPUT"));
	    		}
	    	});
    	}
		
		nps.submitForm('propertyForm',{mode:'async',
			successHandler : function(data){
				if(data.isSuccess){
					nps.info(nps.i18n("INFO_TITLE_DATA"),"操作成功！");
					refreshData();
				}
		   }});
		
		if($j(this).hasClass('continue')){
			refreshPropertyValueSection();
		}
	});

	/**
	 * 【属性值列表】中的属性值点击事件
	 */
	$j('.propertyValueTabelTd').live('click',function(){
		nps.error();
		//tdData : {"value":{"defaultValue":"简体中文","values":["简体中文","english"],"langValues":{"en_UK":"english","zh_CN":"简体中文"},"langs":["zh_CN","en_UK"]},
		//		"id":17,"version":1460449323185,"modifyTime":1460449317569,"createTime":1460449317569,"sortNo":1,"propertyId":1,"thumb":null}
		var tdData =  jQuery.parseJSON($j(this).attr('target'));
		$j("input[name='propertyValues.id']").val(tdData.id);
		$j("input[name='propertyValues.propertyId']").val(tdData.propertyId);
		$j("input[name='propertyValues.sortNo']").val(tdData.sortNo);
		
		$j('.propertyValueInput').each(function(index,data){
			var targetElement = $j(this);
			var _lang = targetElement.find("input[name='propertyValues.value.langs["+ index+"]']").val();
			console.log(tdData.value.langValues[_lang]);
			targetElement.find("input[id='input3']").val(tdData.value.langValues[_lang]);
			targetElement.find("input[id='input3']").removeClass('ui-loxia-error');
		});
//		
	});
	
	
	$j('.propertyValueSort').click(function(){
		var propertyId = $j("#propertyId").val();
		var json = {
			'propertyId':propertyId
		};
		var data = nps.syncXhr(findAllPropertyValueByPropertyId, json,{type: "POST"});
		if(data.isSuccess){
			var propertyValues = data.description;
			console.log(propertyValues)
			console.log(propertyValues.length)
			var html = "";
			for(var i = 0; i<propertyValues.length; i++){
				
				var targetData = propertyValues[i];
				html +="<div class='ui-block-line' style='padding:5px 0 5px 0;' propertyId='"+targetData.propertyId+"' sortNo='"+ targetData.sortNo+"'>";
				for ( var j = 0; j < i18nLangs.length; j++) {
					var i18nLang = i18nLangs[j];
					var key = i18nLang.key;
					html += "<input  readonly='value' value="+ targetData.value.langValues[key] +"  style='width: 150px'  class='name' loxiaType='input' /><span>"+i18nLang.value +"</span>";
					if(j!=i18nLangs.length){
						html += '<br>';
					}
				}
				html += '</div>';
			}
			
			$j(".proto-dialog-content").html(html);
		}else{
			
		}
		$j("#detail-dialog").dialogff({type:'open',close:'in',width:'800px',height:'630px'});
	});
	
//	var sortTable1Height = jQuery('#detail-dialog').height();
//	jQuery('#sortable').height(sortTable1Height);
	
	$j("#sortable").sortable();
	$j("#sortable").disableSelection();
	
	
	
	$j("#table2").loxiasimpletable({
		page : true,
		size : 8,
		form:"searchPropertyValueForm",
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols : [  {
			name : "value",
			label : nps.i18n("PROPERTY_VALUE_NAME"),
			width : "80%",
			template : "drawProValue"
			
		}
/*		,{
			 label:nps.i18n("GROUP_TABLE_OPERATE"),
			 width:"20%",
			 template:"editGroup"
		 }*/
		],
		dataurl : propertyValuePages
	});
	refreshData();
	
	
	
	
});