//国际化
$j.extend(loxia.regional['zh-CN'], {
	"GROUP_FORM_INFO":"提示信息",
	"GROUP_TABLE_GROUP_NAME":"分组名",
	"GROUP_TABLE_GROUP_TYPE":"类型",
	"GROUP_TABLE_OPERATE":"操作",
	"GROUP_CONFIRM_DELETE":"确认删除",
	"GROUP_CONFIRM_DELETE_SEL_GROUP":"确定要删除选定的分组么？",
	"GROUP_FORM_CHECK_ERROR":"错误信息",
	"OPERATE_ERROR":"操作错误",
	"PLEASE_SEL_GROUP_FIRST":"请先选择要删除的分组",
	"GROUP_FORM_DEL_MEMBER_RELATION_GROUP":"您当前删除的分组因为已经和部分会员进行关联" +
			"，此操作需要删除此分组及与会员的关联关系，所以不可逆转，且耗费较长时间，您确认要操作?",
	
	"GROUP_ADD_GROUP_NAME_NOTNULL":"分组名不能为空",
	"GROUP_ADD_GROUP_TYPE_NOTNULL":"类型不能为空",
	

	"GROUP_FORM_DEL_SUCCESS":"删除成功!",
	"GROUP_FORM_ADD_SUCCESS":"增加成功!",
	"GROUP_FORM_BIND_SUCCESS":"加入成功!",
	"GROUP_FORM_UNBIND_SUCCESS":"脱离成功!",
	
	"MEMBER_FORM_DEFAULT_TITLE":"会员列表",
	
	"LABEL_MEMBER_USERPIC":"会员头像",
    "LABEL_MEMBER_LOGINNAME":"会员名",
    "LABEL_MEMBER_NICKNAME":"昵称",
    "LABEL_MEMBER_SOURCE":"来源",
    "LABEL_MEMBER_TYPE":"类型",
    "LABEL_MEMBER_GROUP":"分组",
    "LABEL_MEMBER_LIFECYCLE":"状态",
    "LABEL_MEMBER_REGISTERTIME":"创建时间",
    "LABEL_MEMBER_LATESTTIME":"最近登录时间",
    "LABEL_MEMBER_OPERATE":"操作",
    "NO_CATEGORY":"无",
    
    "MEMBER_GROUP_CHECK_ERROR":"错误信息",
    "MEMBER_GROUP_BIND_OPERATE_TIP_NOSEL":"未选中会员或分组",
    "MEMBER_GROUP_CONFIRM_BIND":"确认加入分类",
    "MEMBER_GROUP_CONFIRM_BIND_SEL_GROUP_TO_MEMBER":"确认要将选择的会员加入所选定的分类么？",
    "MEMBER_GROUP_CONFIRM_UNBIND":"确认解除绑定",
    "MEMBER_GROUP_CONFIRM_UNBIND_SEL_MEMBER_FROM_GROUP":"确认要将选择的会员脱离所选定的分类么？",
    "MEMBER_GROUP_UNBIND_OPERATE_TIP_NOSEL":"未选中会员或未选定仅一个分组",
    "MEMBER_GROUP_UNBIND_OPERATE_TIP_NOSEL_MEMBER":"未选中会员",
    "MEMBER_GROUP_UNBIND_OPERATE_TIP_NOSEL_ONEGROUP":"只能选择一个会员脱离",
    "MEMBER_GROUP_UNBIND_OPERATE_TIP_SEL_NOGROUP":"所选会员存在无分类会员，无法脱离",
    "MEMBER_GROUP_OPERATE_TIP_UNBIND_NONEED":"所选会员中，没有一条是属于所选分组的，无需脱离",
    "MEMBER_GROUP_LIST_DEFAULT_TITLE":"会员列表",
    "GROUP_NAME_EXISTS":"分组名重复"
    
  });

//常量url\setting等

//会员分组列表
var groupListUrl = base +'/group/groupList.json';

//逻辑删除会员分组
var removeGroupUrl = base +'/group/removeGroup.json';

//新增会员分组 
var addGroupUrl = base +'/group/saveGroup.Json';

//会员列表
var memberListUrl = base+'/member/memberList.json';

//绑定分类
var bindUrl = base+'/group/bindMemberGroup.json';

//绑定分类
var unBindUrl = base+'/group/unBindMemberGroup.json';

var validateUnBindSelUrl = base +'/group/validateUnBindByMemberIdsAndGroupId.json';



//--函数
//table1
function drawCheckboxTable1(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='member-checkId'  value='" + loxia.getObject("id", data)+"'/>";
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
function formatGroupName(data, args, idx){
	var propertyNameArray=loxia.getObject("groupName", data);
	
	var groupNames =propertyNameArray;
	
	if(propertyNameArray==null||propertyNameArray==''){
		groupNames= nps.i18n("NO_CATEGORY");
	}
	var hiddenGroupNameInput ="<input type='hidden' id='memberGroupName_"+loxia.getObject("id", data)+"' value='"+propertyNameArray+"' />";
	groupNames +=hiddenGroupNameInput;
	return groupNames;
}

//table2

function drawCheckboxTable2(data, args, idx){
	return "<input type='checkbox' class='group-checkId' name='id' value='" + loxia.getObject("id", data)+"'/>";
}

function editGroup(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button deleteSingleGroup' val='"+loxia.getObject("id", data)+"'>删除</a>";
}

function groupNameTemplate(data, args, idx){
	return "<a href='javascript:void(0);' class='func-button showmember' val='"+loxia.getObject("id", data)+"'>"+loxia.getObject("name",data)+"</a>";
}

function groupTypeTemplate(data, args, idx){
	var type=loxia.getObject('type',data);
	var name=type;
	if(type==1){
		name="常规";
	}else if(type==2){
		name="等级";
	}else{
		return "";
	}
	return name;
}


function unBindMember(memberIds,groupId){
	var json={"memberIds":memberIds,"groupId":groupId};
	var _d = nps.syncXhr(unBindUrl, json,{type: "GET"});
	if(_d.isSuccess){
		
		nps.info(nps.i18n("GROUP_FORM_INFO"),nps.i18n("GROUP_FORM_UNBIND_SUCCESS"));
    	refreshData();
	}
	else
    	nps.info(nps.i18n("MEMBER_GROUP_CHECK_ERROR"),_d.exception.message);
}

function removeGroup(groupId){
	var json={"ids":groupId};
 	var result = nps.syncXhr(removeGroupUrl, json,{type: "GET"});
 	if(result.isSuccess){
 		
 		nps.info(nps.i18n("GROUP_FORM_INFO"),nps.i18n("GROUP_FORM_DEL_SUCCESS"));
     	refreshData();
 	}
 	else
     	nps.info(nps.i18n("GROUP_FORM_CHECK_ERROR"),_d.exception.message);
}




function refreshData(){

	$j("#table1").loxiasimpletable("refresh");


	$j("#table2").loxiasimpletable("refresh");
    


}




$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    searchFilter.init({formId: 'searchMemberForm', searchButtonClass: '.func-button.searchMember'});
    
    searchFilter.init({formId: 'searchGroupForm', searchButtonClass: '.func-button.searchGroup'});
    
    $j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchMemberForm",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckboxTable1"
		},
		{
			name : "loginName",
			label : nps.i18n("LABEL_MEMBER_LOGINNAME"),
			width : "10%",
			sort:["a.login_name asc","a.login_name desc"]
		}, {
			name : "nickName",
			label : nps.i18n("LABEL_MEMBER_NICKNAME"),
			width : "10%",
			sort:["b.nickname asc","b.nickname desc"]
			
		}, {
			name : "sourceName",
			label : nps.i18n("LABEL_MEMBER_SOURCE"),
			width : "5%"
		}, {
			name : "typeName",
			label : nps.i18n("LABEL_MEMBER_TYPE"),
			width : "5%",
			
		}, {
			name : "groupName",
			label : nps.i18n("LABEL_MEMBER_GROUP"),
			width : "15%",
			template:"formatGroupName"
			
		},  
		   {
			name : "lifecycle",
			label : nps.i18n("LABEL_MEMBER_LIFECYCLE"),
			width : "5%",
			type:"yesno"
		}, {
			name : "registerTime",
			label : nps.i18n("LABEL_MEMBER_REGISTERTIME"),
			width : "10%",
			formatter:"formatDate",
			sort: ["c.register_time asc","c.register_time desc"]
		}, {
			name : "loginTime",
			label : nps.i18n("LABEL_MEMBER_LATESTTIME"),
			width : "10%",
			formatter:"formatDate",
			sort: ["c.login_time asc","c.login_time desc"]
		} ],
		dataurl : memberListUrl
	});


	$j("#table2").loxiasimpletable({
		form:"searchGroupForm",
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols:
		[
		 {
			 label:"<input type='checkbox'/>",
			 witdh:"5%", template:"drawCheckboxTable2"
		 },
		 {
			 name:"name",
			 label:nps.i18n("GROUP_TABLE_GROUP_NAME"),
			 width:"50%",
			 template:"groupNameTemplate",
			 sort:["mgr.name asc","mgr.name desc"]
		 },
		 {
			 name:"type",
			 label:nps.i18n("GROUP_TABLE_GROUP_TYPE"),
			 width:"25%",
			 template:"groupTypeTemplate"
		 },
		 {
			 label:nps.i18n("GROUP_TABLE_OPERATE"),
			 width:"25%",
			 template:"editGroup"
		 }
		],
		dataurl : groupListUrl
	});
	
	
    $j("#table2").on("click",".func-button.showmember",function(){
    	
    	var table1_title = "【"+$j(this).text()+"】"+nps.i18n("MEMBER_FORM_DEFAULT_TITLE");
    	$j("#table1 .ui-loxia-table-title").html(table1_title);
    	
    	//筛选
    	var curObject = $j(this);
    	
    	var groupId = curObject.attr("val");
    	$j("#groupId").val(groupId);
    	
    	//置其他条件为空
    	$j("#loginName").val("");
    	$j("#startTime").val("");
    	$j("#endTime").val("");
    	$j("#Source").val("");
    	$j("#Type").val("");
    	$j("#isAddGroup").val("");
    	
    	//刷新
    	$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
    	$j("#table1").attr("caption",table1_title);
    	$j("#table1").data().uiLoxiasimpletable.options.title=table1_title;
		refreshData();
    	
    });
    
    //单项删除
    $j("#table2").on("click",".func-button.deleteSingleGroup",function(){
    	var curObject=$j(this);
    	var groupId = curObject.attr("val");
		
		var _relationGroupIdJson ={"q_long_groupId":groupId,"page.size":15,"page.start":1};
		
		var _relationMemListWithpageJson = nps.syncXhr(memberListUrl, _relationGroupIdJson,{type: "GET"});
		
		var relationItems =_relationMemListWithpageJson.items;
		
		var relationMemberCount =relationItems.length;
		
		var relatMemberIds = "";
		
		if(relationMemberCount>0){
			
			for(var idx =0;idx <relationMemberCount;idx++){
				if(idx!=0){
					relatMemberIds+=",";
				}
				relatMemberIds+=relationItems[idx].id;
			}
			
			nps.confirm(nps.i18n("GROUP_CONFIRM_DELETE"),nps.i18n("GROUP_FORM_DEL_MEMBER_RELATION_GROUP"), function(){
    			//脱离分类
				unBindMember(relatMemberIds,groupId);
				
				//删除分组
				removeGroup(groupId);
			});
			
		}else{
			nps.confirm(nps.i18n("GROUP_CONFIRM_DELETE"),nps.i18n("GROUP_CONFIRM_DELETE_SEL_GROUP"), function(){
				removeGroup(groupId);
			});
		}
    });

	refreshData();
    //批量删除按钮
    $j(".func-button.deleteMultyGroup").click(function(){
    	
    	var data="";
    	
    	var tempGroupId ="";
    	var _tempRelationGroupIdJson ="";
    	var _tempRelationMemListWithpageJson ="";
    	var tempRelationItems ="";
    	//当前id所关联的member数
    	var tempRelationMemberCount =0;
    	
    	var relationMemberCount =0;
    	
    	var relationGroupId ="";
    	
    	var isHaveRelativeMember =false;

    	$j(".group-checkId:checked").each(function(i,n){
    		
    		tempGroupId = $j(this).val();
    		
    		_tempRelationGroupIdJson ={"q_long_groupId":tempGroupId,"page.size":15,"page.start":1};
    		
    		_tempRelationMemListWithpageJson = nps.syncXhr(memberListUrl, _tempRelationGroupIdJson,{type: "GET"});
    		
    		tempRelationItems =_tempRelationMemListWithpageJson.items;
    		
    		tempRelationMemberCount =tempRelationItems.length;
    		if(tempRelationMemberCount>0){
    			
    			if(relationMemberCount!=0){
    				relationGroupId+=",";
    			}
    			relationGroupId +=tempGroupId;
    			relationMemberCount++;
    			isHaveRelativeMember =true;
    		}
    		
        	if(i!=0){
            	data+=",";
            	}
        	data+=tempGroupId;
        });
    	
    	if(data==null||data==''){
    		nps.info(nps.i18n("OPERATE_ERROR"),nps.i18n("PLEASE_SEL_GROUP_FIRST"));
    		return ;
    	}
    	
    	if(isHaveRelativeMember){
    		nps.confirm(nps.i18n("GROUP_CONFIRM_DELETE"),nps.i18n("GROUP_FORM_DEL_MEMBER_RELATION_GROUP"), function(){
    			//脱离分类
    			
    			var groupArray =relationGroupId.split(",");
    			
    			var _tempConfirmRelationGroupIdJson ="";
    	    	var _tempConfirmRelationMemListWithpageJson ="";
    	    	var tempConfirmRelationItems ="";
    	    	//当前id所关联的member数
    	    	var tempConfirmRelationMemberCount =0;
    	    	var tempConfirmRelationMemberIds="";
    			
    			//将关联选定分组的会员脱离
    			for(var tempIdx=0;tempIdx<groupArray.length;tempIdx++){
    				_tempConfirmRelationGroupIdJson ={"q_long_groupId":groupArray[tempIdx],"page.size":15,"page.start":1};
    				
    				_tempConfirmRelationMemListWithpageJson = nps.syncXhr(memberListUrl, _tempConfirmRelationGroupIdJson,{type: "GET"});
    				
    				tempConfirmRelationItems =_tempConfirmRelationMemListWithpageJson.items;
    				
    				tempConfirmRelationMemberCount =tempConfirmRelationItems.length;
    				
    				tempConfirmRelationMemberIds = "";
    				
    				for(var idx =0;idx <tempConfirmRelationMemberCount;idx++){
    					if(idx!=0){
    						tempConfirmRelationMemberIds+=",";
    					}
    					tempConfirmRelationMemberIds+=tempConfirmRelationItems[idx].id;
    				}
    				unBindMember(tempConfirmRelationMemberIds,groupArray[tempIdx]);
    			}
    			
				
				//删除分组
				removeGroup(data);
			});
    	}else{
			nps.confirm(nps.i18n("GROUP_CONFIRM_DELETE"),nps.i18n("GROUP_CONFIRM_DELETE_SEL_GROUP"), function(){
				removeGroup(data);
			});
		}

    });
    
    //搜索会员
    $j(".func-button.searchMember").on("click",function(){
    	$j("#groupId").val("");
    	
    	var defaultTitle = nps.i18n("MEMBER_GROUP_LIST_DEFAULT_TITLE");
		$j("#table1 .ui-loxia-table-title").html(defaultTitle);
		$j("#table1").attr("caption",defaultTitle);
		$j("#table1").data().uiLoxiasimpletable.options.title=defaultTitle;
    	
    	refreshData();
    });
    
    //搜索分组
    
    $j(".func-button.searchGroup").on("click",function(){
    	refreshData();
    });
    
    //新增分组
    $j(".func-button.addGroup").on("click",function(){
    	var groupName = $j("#groupName").val();
    	var groupType = $j("#type").val();
    	
    	groupName = (groupName==null)?null:groupName.trim();
    	
    	if(groupName==null||groupName==''){
    		nps.info(nps.i18n("OPERATE_ERROR"),nps.i18n("GROUP_ADD_GROUP_NAME_NOTNULL"));
    		return ;
    	}
    	
    	if(groupType==""){
    		nps.info(nps.i18n("OPERATE_ERROR"),nps.i18n("GROUP_ADD_GROUP_TYPE_NOTNULL"));
    		return ;
    	}
    	
    	var json={"groupName":groupName,"groupType":groupType};
    	var result = nps.syncXhr(addGroupUrl, json,{type: "GET"});
    	$j("#groupName").val("");
    	if(result!=undefined&&result.isSuccess){
    		
    		nps.info(nps.i18n("GROUP_FORM_INFO"),nps.i18n("GROUP_FORM_ADD_SUCCESS"));
        	refreshData();
    	}
    	else
        	nps.info(nps.i18n("GROUP_FORM_CHECK_ERROR"),nps.i18n("GROUP_NAME_EXISTS"));
    });
    
    //加入分类
    $j(".button.orange.bind").on("click", function(){
        
		var memberIds="";
		
		var groupIds="";
		
		//check未选中
		
    	$j(".member-checkId:checked").each(function(i,n){
        	if(i!=0){
        		memberIds+=",";
            	}
        	memberIds+=$j(this).val();
        });
    	
    	$j(".group-checkId:checked").each(function(i,n){
        	if(i!=0){
        		groupIds+=",";
            	}
        	groupIds+=$j(this).val();
        });
    	
    	
    	if(memberIds==""||groupIds==""){
    		nps.info(nps.i18n("MEMBER_GROUP_CHECK_ERROR"),nps.i18n("MEMBER_GROUP_BIND_OPERATE_TIP_NOSEL"));
    		return ;
    	}

    	
    	nps.confirm(nps.i18n("MEMBER_GROUP_CONFIRM_BIND"),nps.i18n("MEMBER_GROUP_CONFIRM_BIND_SEL_GROUP_TO_MEMBER"), function(){

        	
            var json={"memberIds":memberIds,"groupIds":groupIds};
        	var _d = nps.syncXhr(bindUrl, json,{type: "GET"});
        	if(_d.isSuccess){
        		nps.info(nps.i18n("GROUP_FORM_INFO"),nps.i18n("GROUP_FORM_BIND_SUCCESS"));
            	refreshData();
            	
        	}
        	else
            	nps.info(nps.i18n("MEMBER_GROUP_CHECK_ERROR"),_d.exception.message);
        });
    	
    });
    
	//脱离分类:只能是多个或者一个商品从一个分类下解除
	$j(".button.orange.unbind").on("click", function(){
	       
		var memberIds="";
		
		var groupId="";
		
		//check未选中
		
		var member_checked_num = 0;
		var group_checked_num = 0;
		
    	$j(".member-checkId:checked").each(function(i,n){
        	if(i!=0){
        		memberIds+=",";
            	}
        	memberIds+=$j(this).val();
        	member_checked_num++;
        });
    	
    	$j(".group-checkId:checked").each(function(i,n){
        	if(i!=0){
        		groupId+=",";
            	}
        	groupId+=$j(this).val();
        	group_checked_num++;
        });
    	
    	if(member_checked_num==0){
    		nps.info(nps.i18n("MEMBER_GROUP_CHECK_ERROR"),nps.i18n("MEMBER_GROUP_UNBIND_OPERATE_TIP_NOSEL_MEMBER"));
    		return ;
    	}
    	if(group_checked_num!=1){
    		nps.info(nps.i18n("MEMBER_GROUP_CHECK_ERROR"),nps.i18n("MEMBER_GROUP_UNBIND_OPERATE_TIP_NOSEL_ONEGROUP"));
    		return ;
    	}
    	
    	//选择无分类商品给出提示
    	var isEmptyGroupNamesFlag = false;
		var tempGroupName ="";
    	$j(".member-checkId:checked").each(function(i,n){
    		
        	tempGroupName =$j("#memberGroupName_"+$j(this).val()).val();
        	if(tempGroupName==null||tempGroupName==''){
        		isEmptyGroupNamesFlag =true;
        	}
        	
        });
    	
    	if(isEmptyGroupNamesFlag){
    		nps.info(nps.i18n("MEMBER_GROUP_CHECK_ERROR"),nps.i18n("MEMBER_GROUP_UNBIND_OPERATE_TIP_SEL_NOGROUP"));
    		return ;
    	}
    	
    	var json={"memberIds":memberIds,"groupId":groupId};
    	var result = nps.syncXhr(validateUnBindSelUrl, json,{type: "GET"});
    	
    	if(result.isSuccess){
    		nps.confirm(nps.i18n("MEMBER_GROUP_CONFIRM_UNBIND"),nps.i18n("MEMBER_GROUP_CONFIRM_UNBIND_SEL_MEMBER_FROM_GROUP"), function(){

        		unBindMember(memberIds,groupId);
            });
    	}else{
    		nps.info(nps.i18n("GROUP_FORM_INFO"),nps.i18n("MEMBER_GROUP_OPERATE_TIP_UNBIND_NONEED"));
    	}

    });

});