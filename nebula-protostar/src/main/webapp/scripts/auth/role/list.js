$j.extend(loxia.regional['zh-CN'],{
    "ROLE_NAME":"角色名称",
    "DEPT_TYPE":"机构类型",
    "STATUS":"状态",
    "OPERATE":"操作",
    "CONFIRM_DELETE":"确认删除",
    "CONFIRM_DELETE_USER": "确定要删除选定的用户么？",
    "ROLE_STATE_ENABLE":"有效",
    "ROLE_STATE_DISABLE":"无效",
    "NPS_FORM_CHECK_ERROR":"错误提示",
    "NPS_FORM_CHECK_INFO":"系统提示",
    "ROLE_TIP_NOSEL":"未选中",
    "ROLE_DELETE_SUCCESS":"删除成功"
    	
});


var searchRoleUrl='/role/list.json';

function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
}
function drawOperate(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button modify' val='"+loxia.getObject("id", data)+"'>"+nps.i18n("OPERATOR_MODIFY")+"</a><a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button delete'>"+nps.i18n("OPERATOR_DELETE")+"</a>";
}
function drawName(data, args, idx){
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('add-role.html','aaa',null,[0,1]);\">"+loxia.getObject("name", data)+"</a>";
}

/*function stateFormatter(val){
	if(val==null||val==0){
		return nps.i18n("ROLE_STATE_DISABLE");
	}
	else if(val==1){
		return nps.i18n("ROLE_STATE_ENABLE");
	}
}*/

$j(document).ready(function(){
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});

    $j("#table1").loxiasimpletable({
		page: true,
		size:10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[
			{label:"<input type='checkbox'/>",witdh:"5%", template:"drawCheckbox"},
			{name:"name",label:nps.i18n("ROLE_NAME"),width:"35%",sort: ["r.name asc","r.name desc"]},
			{name:"orgTypeName",label:nps.i18n("DEPT_TYPE"),width:"30%"},
			{name:"lifecycle",label:nps.i18n("STATUS"),width:"15%",type:"yesno"},
			{label:nps.i18n("OPERATE"),width:"15%", template:"drawOperate"}
		],
		dataurl : searchRoleUrl
	});
    refreshData();
	
  	//makeTable("/role/list.json");
	
  	$j("#table1").on("click",".func-button.modify",function(){
    	
       window.location.href = "/role/update.htm?roleId="+$j(this).attr("val");
    });
  	
  	$j("#table1").on("click",".func-button.delete",function(){
  	   	 
  		var curObject=$j(this);
        nps.confirm(nps.i18n("CONFIRM_DELETE"),nps.i18n("CONFIRM_DELETE_USER"), function(){

            var json={"ids":curObject.attr("val")};
        	var _d = loxia.syncXhr('/role/delete.json', json,{type: "GET"});
        	if(_d=='success'){
            	nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_DELETE_SUCCESS"));
            	refreshData();
        	}
        	else{
            	nps.info(nps.i18n("NPS_FORM_CHECK_ERROR"),nps.i18n("NPS_OPERATE_FAILURE"));
        	}
        });
    });
  //批量删除按钮
    $j(".deleteRole").click(function(){
    	
    	var data="";
    	$j(".checkId:checked").each(function(i,n){
        	if(i!=0){
            	data+=",";
            	}
        	data+=$j(this).val();
        });

        var json={"ids":data};
        
        if(data==''){
        	nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_TIP_NOSEL"));
        	return ;
        }
    	
        nps.confirm(nps.i18n("CONFIRM_DELETE"),nps.i18n("CONFIRM_DELETE_USER"), function(){
        	
            
        	var _d = loxia.syncXhr('/role/delete.json', json,{type: "GET"});
        	
        	if(_d=='success'){
        		nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("ROLE_DELETE_SUCCESS"));
        		refreshData();
        	}
        	else{
            	nps.info(nps.i18n("NPS_FORM_CHECK_ERROR"),nps.i18n("NPS_OPERATE_FAILURE"));
        	}
        });
    });
	//添加
    $j(".addRole").click(function(){
        window.location.href="/role/create.htm";
    });

    //查询
    $j(".func-button.search").click(function(){
    	$j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		refreshData();
    });
});
//刷新表格数据
function refreshData(){

	$j("#table1").loxiasimpletable("refresh");
 
}

