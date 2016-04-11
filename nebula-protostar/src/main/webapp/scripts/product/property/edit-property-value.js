$j.extend(loxia.regional['zh-CN'],{

});

$j(document).ready(function(){
	
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
		var selectedGroupId = $j('.selectedGroupId').find("option:selected").val();
		window.location.href = base + "/i18n/property/showAddOrUpdateGroup.htm?groupId="+selectedGroupId +"&type=1";
	});
	

	
	
	
	
	

	
	
	
	
	
	
});