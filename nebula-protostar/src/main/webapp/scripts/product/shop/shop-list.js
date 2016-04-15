$j.extend(loxia.regional['zh-CN'],{
    "SHOP_NAME":"店铺名称",
    "SHOP_CODE":"店铺编码",
    "DESCRIPTION":"描述",
    "STATUS":"状态",
    "TO_ENABLE":"启用",
    "TO_DISABLE":"禁用",
    "CREATE_TIME":"创建时间",
    "OPERATE":"操作",
    "USER_CONFIRM_REMOVE":"确认删除",
    "SET_PROPERTY":"设置店铺自定义属性",
    "CONFIRM_REMOVE_SHOP": "确定要删除选定的店铺么？",
    "SHOP_STATE_ENABLE":"有效",
    "SHOP_STATE_DISABLE":"无效",
    "USER_FORM_CHECK_ERROR":"错误信息",
    "NPS_FORM_CHECK_INFO":"操作提示",
    "CONFIRM_ENABLE_SHOP":"确认启用店铺",
    "INFO_CONFIRM_ENABLE_SHOP":"确认启用店铺吗？",
    "INFO":"提示:",
    "ENABLE_SHOP_SECCESS":"启用店铺成功！",
    "ENABLE_SHOP_FAIL":"启用店铺失败！",
    "CONFIRM_DISABLE_SHOP":"确认禁用店铺",
    "INFO_CONFIRM_DISABLE_SHOP":"确认禁用店铺吗？",
    "DISABLE_SHOP_SECCESS":"禁用店铺成功！",
    "DISABLE_SHOP_FAIL":"禁用店铺失败！",
    "SHOP_DELETE_SUCCESS":"删除成功 ！",
});

var searchRoleUrl = base + '/shop/shopList.json';

var removeShopUrl = base + '/shop/removeShop.json';

var setShopPropertyUrl = base + '/shop/shopPropertymanager.htm';

function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("organizationid", data)+"'/>";
}


function drawEditShop(data){
	
//	 var list = [{label:nps.i18n("OPERATOR_MODIFY"), type:"href", content:base+'/shop/updateShop.htm?shopid='+data.shopid},
//                 {label:nps.i18n("TO_DISABLE"), type:"jsfunc", content:"disableShop"},
//                 {label:nps.i18n("OPERATOR_DELETE"), type:"jsfunc", content:"removeshop"},
//                 {label:nps.i18n("SET_PROPERTY"), type:"href", content:setShopPropertyUrl+"?shopId="+data.shopid}];
//             if(data.lifecycle == 0){
//                 list[1].label = nps.i18n("TO_ENABLE");
//                 list[1].content = "enableShop";
//             }
	
	var list = [{label:nps.i18n("OPERATOR_MODIFY"), type:"href", content:base+'/shop/updateShop.htm?shopid='+data.shopid},
                {label:nps.i18n("TO_DISABLE"), type:"jsfunc", content:"disableShop"},
                {label:nps.i18n("OPERATOR_DELETE"), type:"jsfunc", content:"removeshop"}];
            if(data.lifecycle == 0){
                list[1].label = nps.i18n("TO_ENABLE");
                list[1].content = "enableShop";
            }
	
	
      return list;
	
}
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}
function stateFormatter(val){
	if(val==null||val==0){
		return nps.i18n("SHOP_STATE_DISABLE");
	}
	else if(val==1){
		return nps.i18n("SHOP_STATE_ENABLE");
	}
}


// 启用店铺
function enableShop(data,args,caller){

	var curObject=$j(caller);
	 nps.confirm(nps.i18n("CONFIRM_ENABLE_SHOP"),nps.i18n("INFO_CONFIRM_ENABLE_SHOP"), function(){

		   nps.asyncXhrPost(base+"/shop/enableOrDisableShop.json", {"ids":data.organizationid,"type":1}, {
			   successHandler : function(data, textStatus) {
	  				var backWarnEntity = data;
	  				if (backWarnEntity.isSuccess) {
	  					nps.info(nps.i18n("INFO"),nps.i18n("ENABLE_SHOP_SECCESS"));
	  					refreshData();
	  				} else {
	  					nps.info(nps.i18n("ENABLE_SHOP_FAIL"),backWarnEntity.description);
	  				}
	  			}
	  		});
	 });

}
// 禁用店铺
function disableShop(data,args,caller){

	var curObject=$j(caller);
	 nps.confirm(nps.i18n("CONFIRM_DISABLE_SHOP"),nps.i18n("INFO_CONFIRM_DISABLE_SHOP"), function(){
  	
      nps.asyncXhrPost(base+"/shop/enableOrDisableShop.json", {"ids":data.organizationid,"type":0}, {
    	  successHandler : function(data, textStatus) {
				var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					nps.info(nps.i18n("INFO"),nps.i18n("DISABLE_SHOP_SECCESS"));
					refreshData();
				} else {
					nps.info(nps.i18n("DISABLE_SHOP_FAIL"),backWarnEntity.description);
				}
			}
		});
      
  });

}

//修改店铺
function modifyShop(data,args,caller){
	window.location.href=base+'/shop/updateShop.htm?shopid='+data.id;
}
//设置店铺自定义属性
function setshopproperty(data,args,caller){

	
	window.location.href=setShopPropertyUrl+"?shopId="+data.id;

}

//删除单行
function removeshop(data,args,caller){

     var curObject=$j(caller);
     nps.confirm(nps.i18n("USER_CONFIRM_REMOVE"),nps.i18n("CONFIRM_REMOVE_SHOP"), function(){
         var json={"ids":data.organizationid};
     	var _d = nps.syncXhr(removeShopUrl, json,{type: "GET"});
     	if(_d.isSuccess){
    		nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("SHOP_DELETE_SUCCESS"));
    		refreshData();
    	}
    	else{
        	nps.info(nps.i18n("NPS_FORM_CHECK_ERROR"),nps.i18n("NPS_OPERATE_FAILURE"));
    	}
     });
}



$j(document).ready(function(){
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
    $j("#table1").loxiasimpletable({
    	nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[
			{label:"<input type='checkbox'/>",width:"5%", template:"drawCheckbox"},
			{name:"shopname",label:nps.i18n("SHOP_NAME"),width:"18%",sort: ["t.name asc","t.name desc"]},
			{name:"shopcode",label:nps.i18n("SHOP_CODE"),width:"12%",sort: ["t.code asc","t.code desc"]},
			{name:"description",label:nps.i18n("DESCRIPTION"),width:"35%"},
			{name:"lifecycle",label:nps.i18n("STATUS"),width:"4%",type:"yesno"},
			{name:"createTime",label:nps.i18n("CREATE_TIME"),width:"12%",formatter:"formatDate",sort: ["s.create_time asc","s.create_time desc"]},
			{label:nps.i18n("OPERATE"),width:"15%", type:"oplist", oplist:drawEditShop}
		],
		dataurl :searchRoleUrl
	});
    refreshData();
    

    
    //批量删除按钮
    $j(".deleteallshop").click(function(){
        nps.confirm(nps.i18n("USER_CONFIRM_REMOVE"),nps.i18n("CONFIRM_REMOVE_SHOP"), function(){
        	var data="";
        	$j(".checkId:checked").each(function(i,n){
            	if(i!=0){
                	data+=",";
                	}
            	data+=$j(this).val();
            });

            var json={"ids":data};
        	var _d = loxia.syncXhr(base+'/shop/removeShop.json', json,{type: "GET"});
        	if(_d.isSuccess){
        		nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("SHOP_DELETE_SUCCESS"));
        		refreshData();
        	}
        	else{
            	nps.info(nps.i18n("NPS_FORM_CHECK_ERROR"),nps.i18n("NPS_OPERATE_FAILURE"));
        	}
        });
    });
    

    //查询
    $j(".func-button.search").click(function(){
    	$j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		refreshData();
    });
   
    //新增店铺
	 $j(".button.addshop").click(function(){
		 window.location.href=base+"/shop/createShop.htm";
	 });
});

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
 
}

