$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
    "PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的属性值么？",
	"PV_PLEASE_SELECT_GROUP":"请选择属性值组！",
	"PROPERTY_VALUE_NEEDS_INPUT":"需要输入属性值！",
	"PROPERTY_VALUE_NAME":"属性值名",
	"GROUP_TABLE_OPERATE":"操作",
    "INFO_DELETE_SUCCESS":"删除记录成功!",
	"INFO_TITLE_DATA":"系统提示",
	"ERROR_NO_FILE":"请先选择要上传的文件",
	"IMP_SUCCESS":"导入成功"
});


var SWF_URL = base + '/scripts/uploadify3/uploadify.swf';
var UPLOAD_URL = base + '/sku/propertyValueUpload.json;jsessionid=';



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
		html += "<input  readonly='true' value='"+ data.value.langValues[key] +"'  style='width: 150px'  class='ui-loxia-default' loxiaType='input' /><span>"+i18nLang.value +"</span>";
		if(j!=i18nLangs.length){
			html += '<br>';
		}
	}
	html += '</div>';
	return html;
}

function editGroup(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button deletePV' onClick='deletePropertyValueById("+loxia.getObject("id", data)+")'>删除</a>";
}


function refreshPropertyValueSection(){
	$j('.propertyValueInput').each(function(index,data){
		var targetElement = $j(this);
		targetElement.find("input[id='input3']").val("");
	});
	
	$j("input[name='propertyValues.id']").val('');
	$j("input[name='propertyValues.sortNo']").val('');
}

function deletePropertyValueById(id){
	nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
//		var checkbox=$j("input[name='id']");
//		var data=""; 
//		  $j.each(checkbox, function(i,val){   
//			  if(val.checked){
//				  data=data+$j(this).val()+",";
//			  }
//		 }); 
//		  if(data!=""){
//			  data=data.substr(0,data.length-1);
//		  }  
			var json={"pvIds":id}; 
		  	 nps.asyncXhrPost(deletePropertyValuesByIds, json,{successHandler:function(data, textStatus){
				var backWarnEntity = data;
  				if (backWarnEntity.isSuccess) {
  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
  					refreshData();
  				} else {
  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
  				}
			 }});
	});
}

var propertyValuePages = base + '/i18n/property/findPropertyValueByPage.json';

var findAllPropertyValueByPropertyId = base + '/i18n/property/findAllPropertyValueByPropertyId.json';

var updatePropertyValueSortNoById = base + '/i18n/property/updatePropertyValueSortNoById.json';

var deletePropertyValuesByIds = base + '/i18n/property/deletePropertyValuesByIds.json';

$j(document).ready(function(){
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	
	$j('.deleteSelected').click(function(){
		nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
			var checkbox=$j("input[name='id']");
			var data=""; 
			  $j.each(checkbox, function(i,val){   
				  if(val.checked){
					  data=data+$j(this).val()+",";
				  }
			 }); 
			  if(data!=""){
				  data=data.substr(0,data.length-1);
			  }  
				var json={"pvIds":data}; 
			  	 nps.asyncXhrPost(deletePropertyValuesByIds, json,{successHandler:function(data, textStatus){
					var backWarnEntity = data;
	  				if (backWarnEntity.isSuccess) {
	  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
	  					refreshData();
	  				} else {
	  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
	  				}
				 }});
		});
	});
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
		nps.error();
		var flag = true;
		for ( var j = 0; j < i18nLangs.length; j++) {
			var i18nLang = i18nLangs[j];
			var key = i18nLang.key;
			$j("input[id='input3'][lang='"+key+"']").each(function(indexi,datai){
	    		var input = $j(this).val();
	    		if(input == "" || undefined == input || null == input){
	    			$j(this).addClass('ui-loxia-error');
	    			nps.error(nps.i18n("ERROR_INFO"), nps.i18n("PROPERTY_VALUE_NEEDS_INPUT"));
	    			flag = false;
	    		}
	    	});
    	}
		if(!flag){
			return ;
		}
		nps.submitForm('propertyForm',{mode:'async',
			successHandler : function(data){
				if(data.isSuccess){
					nps.info(nps.i18n("INFO_TITLE_DATA"),"操作成功！");
					refreshData();
				}else{
					console.log(data.description)
					
					for(var key in data.description){  
					    console.log("属性：" + key + ",值："+ data.description[key]); 
					    
//					    <input id="input3" lang="${i18nLang.key}"
					    $j("input[id='input3'][lang='"+key+"']").addClass('ui-loxia-error');
					}  
					nps.info(nps.i18n("INFO_TITLE_DATA"),"属性值不能重复！");
					return ;
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
	});
	
	/**
	 * 属性值排序，渲染
	 */
	$j('.propertyValueSort').click(function(){
		var propertyId = $j("#propertyId").val();
		var json = {
			'propertyId':propertyId
		};
		var data = nps.syncXhr(findAllPropertyValueByPropertyId, json,{type: "POST"});
		if(data.isSuccess){
			var propertyValues = data.description;
			var html = "";
			for(var i = 0; i<propertyValues.length; i++){
				var targetData = propertyValues[i];
				html +="<div class='propertyValues' style='border:1px solid #ccc;padding:1px;' proValId='"+targetData.id+"' sortNo='"+ targetData.sortNo+"' >";
				for ( var j = 0; j < i18nLangs.length; j++) {
					var i18nLang = i18nLangs[j];
					var key = i18nLang.key;
					html += "<div class='ui-loxia-text'>"+ targetData.value.langValues[key] +"</div><span>"+i18nLang.value +"</span><br>";
				}
				html += '</div>';
			}
			console.log(html)
			$j(".container").html(html);
		}else{
			
		}
		$j("#detail-dialog").dialogff({type:'open',close:'in',width:'800px',height:'650px'});
		$j(".container").shapeshift();
	});
	
	
	$j(".copycancel").on("click",function(){
		$j("#detail-dialog").dialogff({type:'close'});
	});
	
	/**
	 * 属性值排序，提交
	 */
	$j(".copyok").on("click",function(){
		
		var length = $j('.p10 .propertyValues').length;
		var result = "";
		$j('.p10 .propertyValues').each(function(index,data){
			var pvId = $j(this).attr('proValId');
			var sortNo = $j(this).attr('sortNo');
			var newSortNo = index + 1;
			result += pvId + "," + sortNo + "," + newSortNo;
			if(length > index +1){
				result += "-";
			}
		});
		console.log(result);
		
		var data = nps.syncXhr(updatePropertyValueSortNoById, {'result':result},{type: "POST"});
		if(data.isSuccess){
			$j("#detail-dialog").dialogff({type:'close'});
			nps.info(nps.i18n("INFO_TITLE_DATA"),"操作成功！");
			refreshData();
		}else{
			nps.error(nps.i18n("ERROR_INFO"), "操作失败！");
		}
	});
	
	
	$j("#table2").loxiasimpletable({
		page : true,
		size : 8,
		form:"searchPropertyValueForm",
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols : 
		  [{
			 label:"<input type='checkbox'/>",
			 witdh:"10%", template:"drawCheckbox"
		 }, {
			name : "value",
			label : nps.i18n("PROPERTY_VALUE_NAME"),
			width : "70%",
			template : "drawProValue"
			
		}
		,{
			 label:nps.i18n("GROUP_TABLE_OPERATE"),
			 width:"20%",
			 template:"editGroup"
		 }
		],
		dataurl : propertyValuePages
	});
	refreshData();
	
	//下载属性值模板
	$j("#downLoadTmplOfPropertyValue").click(function(){
		var propertyId = $j("#propertyId").val();
		location.href="/product/tplt_property_value_import.xls?propertyId="+propertyId;
	});
	
	
	//解决uploadify对于火狐浏览器的丢失session
	UPLOAD_URL += $j("#session-id").val();	
	var tokenValue = $j("meta[name='_csrf']").attr("content");
	if(typeof(tokenValue) != "undefined") {
		UPLOAD_URL += "?_csrf="+tokenValue;
	}
	
	
	//导入属性值
	$j("#propertyValue-upload").uploadify({
		'swf'        		: SWF_URL, 
		'uploader'          : UPLOAD_URL,
		'button_image_url'	: base + '/scripts/uploadify3/none.png',
		'formData'			: {'propertyId':$j("#propertyId").val()},
		'auto'				: false,
		'buttonText'		: '浏  览',
		'fileTypeDesc'		: '支持文件：',
		'fileTypeExts'		: '*.xlsx',
		'multi'				: false,
		'queueSizeLimit'	: 1,
		'successTimeout'  	: 1200,	//响应时间（秒），为大数据量预留足够时间    
		'removeTimeout'		: 1,
		'onSelectError'		: function(){
			alert("每次只能上传一个文件！");
		},
		'onUploadSuccess' 	: function(file, data, response) {
			var result = eval('('+data+')');
			if(result.isSuccess){
				$j("#errorTip").hide();
				nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("IMP_SUCCESS"));
			}else{
				$j(".showError").html(result.description);
				$j("#errorTip").show();	
			}			
        }
        
	});	
	
	// ‘确认’按钮
	$j("#btn-ok").click(function() {
		if (! checkImport('#propertyValue-upload-queue')) return;
		$j(".showError").html("正在处理数据，请稍后...");
		$j("#errorTip").show();
		
		$j('#propertyValue-upload').uploadify('upload');
	});

	//‘取消’按钮
	$j("#btn-cancel").click(function() {
		$j("#errorTip").hide();
		$j('#propertyValue-upload').uploadify("cancel");
	});
});

//----------------------------------------------------------------------------------------------------
//如果点击‘导入文件’时选择文件为空，则提示
function checkImport(id) {
  if ($j.trim($j(id).html()) == "") {
  	nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("ERROR_NO_FILE"));
      return false;
  }
  return true;
}
