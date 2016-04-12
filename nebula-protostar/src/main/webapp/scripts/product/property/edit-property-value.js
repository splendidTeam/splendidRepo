$j.extend(loxia.regional['zh-CN'],{
	"PROPERTY_VALUE_NEEDS_INPUT":"需要输入属性值！"
});


function refreshData(){


	$j("#table2").loxiasimpletable("refresh",[
					{groupname:"分组1"},
					{groupname:"分组"},
					{groupname:"分组"},
					{groupname:"分组3"},
					{groupname:"分组4"},
					{groupname:"分组5"},
					{groupname:"分组6"},
					{groupname:"分组7"}
				]);
    


}


function drawCheckbox(data, args, idx){
	return "<input type='checkbox' name='id' value='" + loxia.getObject("name", data)+"'/>";
}

function groupNameTemplate(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button showmember'>"+loxia.getObject("groupname",data)+"</a>";
}

function editGroup(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button deleteGroup'>删除</a>";
}

$j(document).ready(function(){
	for ( var j = 0; j < i18nLangs.length; j++) {
		var i18nLang = i18nLangs[j];
		var key = i18nLang.key;
		console.log(key);
	}
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
		window.location.href = base + "/i18n/property/showAddOrUpdateGroup.htm?propertyId="+propertyId +"&groupId="+selectedGroupId +"&type=1";
	});
	
	
	/**
	 * 
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
				}
		   }});
	});

	
	$j("#table2").loxiasimpletable({	
		cols:
		[
		{label:"<input type='checkbox'/>",witdh:"5%", template:"drawCheckbox"},
		{name:"groupname",label:"名称",width:"70%",template:"groupNameTemplate"},
		{label:"操作",width:"30%", template:"editGroup"}
		],
		dataurl : {
			currentPage:1, totalPages: 10, count: 99, size: 10, sortStr:"date desc", firstPage: true, lastPage: false,
			items: [
					{groupname:"1分组1"},
					{groupname:"1分组"},
					{groupname:"1分组"},
					{groupname:"44444"},
					{groupname:"1分组4"},
					{groupname:"1分组5"},
					{groupname:"1分组6"},
					{groupname:"1分组7"},
					{groupname:"1分组8"},
					{groupname:"1分组9"}		
			]
			}
	});
	refreshData();
	
	

	
	
	
	
	
	
});