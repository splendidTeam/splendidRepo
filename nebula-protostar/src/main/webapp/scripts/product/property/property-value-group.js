$j.extend(loxia.regional['zh-CN'],{
    "LABEL_PROPERTY_GROUP":"所属分组",
    "INFO_PROPERTY_VALUE_GROUP_NAME_EMPTY":"请填写属性值组名称！",
    "SYS_ERROR":"系统异常！",
    "INFO_TITLE_DATA":"系统提示"
});

var editPropertyValue= base + '/i18n/property/editPropertyValue.htm';

var saveOrUpdatePropertyValueGroup = base + '/i18n/property/saveOrUpdatePropertyValueGroup.json';

$j(document).ready(function(){
	
	/**
	 * 保存按钮事件
	 */
	$j('.submit').click(function(){
		 nps.error();
		var propertyId = $j('#propertyId').val();
		var groupId = $j('#groupId').val();
		var groupName = $j('#groupName').val();
		console.log(groupName)
	    if (null == groupName || "" == groupName) {
		    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_PROPERTY_VALUE_GROUP_NAME_EMPTY"));
		    $j("#groupName").focus();
		    return;
	    }
		
		var proValIds = "";
		var length = $j('.boundPropertyValue .children-store').length;
		$j('.boundPropertyValue .children-store').each(function(index,element){
			var target = $j(this);
			if(index +1 == length){
				proValIds += target.find('input').val();
			}else{
				proValIds += target.find('input').val() + ",";
			}
		});
		
		console.log(proValIds);
		
		var json = {
				'propertyId':propertyId,
				'groupId':groupId,
				'groupName':groupName,
				'propertyValueIds':proValIds
		};
		
		var data = nps.syncXhrPost(saveOrUpdatePropertyValueGroup,json);
		
		if(data.isSuccess){
			nps.info(nps.i18n("INFO_TITLE_DATA"),"操作成功！");
		}else{
			nps.error(nps.i18n("ERROR_INFO"),nps.i18n("SYS_ERROR"));
		}
	});
	
	/**
	 * 返回
	 */
	$j(".return").click(function(){
		var propertyId = $j('#propertyId').val();
		window.location.href = editPropertyValue + "?propertyId=" + propertyId;
	});
	
	
	
	
	/**
	 * 移除已经加入到属性值组中的属性值
	 */
	$j('.boundPropertyValue .children-store').live('click',function(){
		var target = $j(this);
		var propertValueId = target.find('input').val();
		var propertyValue = target.text();
		$j('.freePropertyValue').append("<span class='children-store'><input type='checkbox' value='"+ propertValueId +"' />"+ propertyValue +"</span>");
		target.remove();
	});
	
	/**
	 * 把属性值加入到属性值组中
	 */
	$j('.freePropertyValue .children-store').live('click',function(){
		var target = $j(this);
		var propertValueId = target.find('input').val();
		var propertyValue = target.text();
		$j('.boundPropertyValue').append("<span class='children-store'><input type='checkbox' checked='checked' value='"+ propertValueId +"' />"+ propertyValue +"</span>");
		target.remove();
	});
	
	
	
});