$j.extend(loxia.regional['zh-CN'], {
		"TAG_TABLE_TAG_NAME":"名称",
		"TAG_TABLE_TYPE_NAME":"类型",
		"TAG_TABLE_OPERATE":"操作",
		
		"LABEL_ITEM_CODE":"商品编码",
		"LABEL_ITEM_TITLE":"商品名称",
		"LABEL_PRODUCT_INDUSTRY":"所属行业",
		"LABEL_ITEM_TAGNAME":"所属标签",
		"LABEL_ITEM_LIFECYCLE":"状态",
		"LABEL_ITEM_CREATETIME":"创建时间",
		"LABEL_ITEM_MODIFYTIME":"修改时间",
		"LABEL_ITEM_LISTTIME":"最近上架时间",
		"NO_CATEGORY":"无",
		
		"ITEM_TAG_CHECK_ERROR":"错误信息",
		"ITEM_TAG_OPERATE_INFO":"提示信息",
		
		"ITEM_TAG_OPERATE_TIP_NOSEL":"未选中商品或标签",
		"ITEM_TAG_OPERATE_TIP_NOSEL_UNBIND":"未选中商品或未选定仅一个标签",
		"ITEM_TAG_OPERATE_TIP_NOSEL_ITEM":"未选中商品",
		"ITEM_TAG_OPERATE_TIP_NOSEL_ONETAG":"只能选择一个标签脱离",
		
		"ITEM_TAG_OPERATE_TIP_SEL_ITEM_NOTAG":"所选商品存在无标签商品，无法脱离",
		"ITEM_TAG_OPERATE_TIP_UNBIND_NONEED":"所选商品中，没有一条是属于所选标签的，无需脱离",
	    "ITEM_TAG_CONFIRM_BIND":"确认加入标签",
	    "ITEM_TAG_CONFIRM_BIND_SUCCESS":"加入成功",
	    "ITEM_TAG_CONFIRM_BIND_SEL_TAG_TO_ITEM":"确定要将选定的标签加入到选定的商品中么？",
	    "ITEM_TAG_CONFIRM_UNBIND":"确认脱离标签",
	    "ITEM_TAG_CONFIRM_UNBIND_SUCCESS":"脱离成功",
	    "ITEM_TAG_CONFIRM_UNBIND_SEL_ITEM_FROM_TAG":"确定要将选定的商品脱离选定的标签么？",
	    "ITEM_TAG_TAGED_TITLE":"已打标签",
	    	
	    "ITEM_TAG_NAME_TYPE_NOTNULL":"请确认已填写标签名或选择类型",
	    "ITEM_TAG_NAME_EXISTS":"标签名已存在",
	    "ITEM_TAG_ADD_SUCCESSFUL":"新增成功",
	    "ITEM_TAG_LABLE_DET_TAGTYPE":"删除",
	    "ITEM_TAG_DEL_SUCCESSFUL":"删除成功",
	    "ITEM_TAG_CONFIRM_DELETE":"确认删除",
	    "ITEM_TAG_DEL_ITEM_RELATION_TAG":"您当前删除的标签因为已经和部分商品进行关联" +
		"，此操作需要删除此标签及与商品的关联关系，所以不可逆转，且耗费较长时间，您确认要操作?",
		"ITEM_TAG_CONFIRM_DELETE_SEL_TAG":"确定要删除选定的标签么？",
		"PLEASE_SEL_TAG_FIRST":"请先选择要删除的分组"
	    	
	});

//常量url\setting等
var tagListJson = base +'/product/tagList.json';

//全部商品列表url
var itemListJson = base +'/item/itemList.json';

//已标签商品列表url
var itemTagListUrl = base + '/item/itemTagList.json';

//未标签商品列表url
var itemNoTagListUrl = base + '/item/itemNoTagList.json';

//加入标签
var bindItemTagUrl = base +'/item/bindItemTag.json';

//脱离标签
var unBindItemTagUrl = base +'/item/unBindItemTag.json';

//校验脱离
var validateUnBindSelUrl = base +'/item/validateUnBindByItemIdsAndTagId.json';

//保存标签
var addTagUrl = base +'/product/saveTag.Json';

//删除标签
var removeTagUrl = base +'/product/removeTagByIds.json';

//行业下拉列表
var industrySetting = {
		treeNodeKey : "id",
        treeNodeParentKey : "parentId",
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},  
		view: {
			dblClickExpand: false,
			showIcon:false,
			fontCss:function(treeId,treeNode){
				if(treeNode.lifecycle==0){
					return {color:"#666"};
				}else{
					return {color:"#000"};
				}
			}
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: industryonClick,
			onCheck: industryonCheck
		}
	};

//----------------函数----------------------------


//行业
function industryonClick(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("industryDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
function industryonCheck(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("industryDemo"),
	nodes = zTree.getCheckedNodes(true),
	v = "";
	id="";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		id+= nodes[i].id + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);
	if (id.length > 0 ) id = id.substring(0, id.length-1); 
	var cityObj = $j("#industryName"); 
	cityObj.attr("value", v);
	$j("#industryId").attr("value",id);
	industryHideMenu();
}

function industryHideMenu() {
	$j("#industryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", industryOnBodyDown);
}
function industryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industryName" || event.target.id == "industryMenuContent" || $j(event.target).parents("#industryMenuContent").length>0)) {
		industryHideMenu();
	}
}

//刷新table
function refreshData(){
	$j("#tagListTable").loxiasimpletable("refresh");
	$j("#allTagsTable").loxiasimpletable("refresh");
	$j("#useTagsTable").loxiasimpletable("refresh");
	$j("#unUseTagsTable").loxiasimpletable("refresh");
}

function formatTagNames(data, args, idx){
	var propertyNameArray =loxia.getObject("tagNames", data);
	
	var tagNames =propertyNameArray;
	
	if(propertyNameArray==null||propertyNameArray==''){
		tagNames= nps.i18n("NO_CATEGORY");
	}
	var hiddenTagNameInput ="<input type='hidden' id='itemTagName_"+loxia.getObject("id", data)+"' value='"+propertyNameArray+"' />";
	tagNames +=hiddenTagNameInput;
	
	return tagNames;
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

//脱离标签
function unBindTag(itemIds,tagId){
	var json={"itemIds":itemIds,"tagId":tagId};
	var _d = nps.syncXhr(unBindItemTagUrl, json,{type: "GET"});
	if(_d.isSuccess){
		
		nps.info(nps.i18n("ITEM_TAG_OPERATE_INFO"),nps.i18n("ITEM_TAG_CONFIRM_UNBIND_SUCCESS"));
    	refreshData();
	}
	else
    	nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),_d.exception.message);
}

function onCategoryClick(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCategoryCheck(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree"), nodes = zTree
			.getCheckedNodes(true), v = "";
	for ( var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	var categoryObj = $j("#category");
	categoryObj.attr("value", v);
	hideMenu();
}

//隐藏行业树菜单
function hideMenu() {
	$j("#categoryContent").fadeOut("fast");
	$j("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "category"
			|| event.target.id == "categoryContent" || $j(event.target)
			.parents("#categoryContent").length > 0)) {
		hideMenu1();
	}
}

//清除搜索的条件
function cleanSearchCondition() {

}

//设置左下选中标签之后，被选中的标签变为默认的
function setUseTagsTableSelected() {
	//选中memberbase 的第一个元素，添加selected样式。同时它的兄弟移除selected样式
	$j(".memberbase").first().next().addClass("selected").siblings(".memberbase").removeClass("selected");
	$j(".tag-change-in").first().next().addClass("block").siblings(
			".tag-change-in").removeClass("block");
}

//设置全部tag未默认
function setAllTagsTableSelected() {
	$j(".memberbase").first().addClass("selected").siblings(".memberbase").removeClass("selected");
	$j(".tag-change-in").first().addClass("block").siblings(
			".tag-change-in").removeClass("block");
}

//生成选择标签 左下角的标签使用
function drawSelectTagName(data, args, idx) {
	return "<a href='javascript:void(0);' class='func-button selectTagType' val='"
			+ loxia.getObject("id", data)
			+ "'>"
			+ loxia.getObject("name", data) + "</a>";
}

//左边的 标签列表选中按钮
function drawTagTypeCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' class='tagTypeCheckId' value='"
			+ loxia.getObject("id", data) + "'/>";
}

//选择标签的checkbox
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' class='tagCheckId' value='"
			+ loxia.getObject("id", data) + "'/>";
}

//table-删除标签
function drawDelTagType(data, args, idx) {
	return "<a href='javascript:void(0);' class='func-button delTagType' val='"
			+ loxia.getObject("id", data) + "'>"+nps.i18n("ITEM_TAG_LABLE_DET_TAGTYPE")+"</a>";
}

/*function delTag() {
	var curObj = $j(this);
	alert("要删除的标签的id " + curObj.attr("val"));
}*/

//检测字符串是否为空
function checkStrIsNotNull(str) {
	if (str != null && str.trim() != "") {
		return true;
	} else {
		return false;
	}
}

//func-删除标签
function removeTag(tagId){
	var json={"ids":tagId};
 	var result = nps.syncXhr(removeTagUrl, json,{type: "GET"});
 	if(result.isSuccess){
 		
 		nps.info(nps.i18n("ITEM_TAG_OPERATE_INFO"),nps.i18n("ITEM_TAG_DEL_SUCCESSFUL"));
     	refreshData();
 	}
 	else
     	nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),_d.exception.message);
}

$j(document).ready(
		function() {
			loxia.init({
				debug : true,
				region : 'zh-CN'
			});
			nps.init();
			
			searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
			
			searchFilter.init({formId: 'searchTagTypeForm', searchButtonClass: '.func-button.searchTagType'});
			
			$j("#tagType").css("width","69px");
			$j("#tagType").css("margin-right","5px");
			$j("#tagType").css("aria-disabled","false");
			//行业树
			$j.fn.zTree.init($j("#industryDemo"), industrySetting, industry_ZNodes);
			
			
			var treeObjIndustry = $j.fn.zTree.getZTreeObj("industryDemo");
			var treeIndustryNodes = treeObjIndustry.transformToArray(treeObjIndustry.getNodes());
			for(var i = 0;i<treeIndustryNodes.length;i++){
				if(treeIndustryNodes[i].isParent){
					treeIndustryNodes[i].nocheck = true;
					treeObjIndustry.refresh();
				}
			}
			
			var q_long_industryId = $j("input[name='q_long_industryId']").val().trim();
			
			
			if(q_long_industryId!=null&&q_long_industryId!=''){
				
				var industry_TreeObj=$j.fn.zTree.getZTreeObj("industryDemo");
				
				var industry_Nodes=industry_TreeObj.transformToArray(industry_TreeObj.getNodes());
				
				for(var i = 0 ; i < industry_Nodes.length ; i++){
					if(industry_Nodes[i].id==q_long_industryId){
						
						industry_Nodes[i].checked = true;
						
						$j("#industryName").val(industry_Nodes[i].name);
						
						break;
					}
				}
				industry_TreeObj.refresh();
			}
			
			$j("#industryName").click(function() {
				var cityObj = $j(this);
				var cityOffset = $j(this).offset();
				$j("#industryMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

				$j("body").bind("mousedown", industryOnBodyDown);
			});

			$j(".button.orange.bind").click(function() {
				var itemIds="";
				
				var tagIds="";
				
				//check未选中
				
		    	$j(".tagCheckId:checked").each(function(i,n){
		        	if(i!=0){
		        		itemIds+=",";
		            	}
		        	itemIds+=$j(this).val();
		        });
		    	
		    	
		    	$j(".tagTypeCheckId:checked").each(function(i,n){
		        	if(i!=0){
		        		tagIds+=",";
		            	}
		        	tagIds+=$j(this).val();
		        });
		    	
		    	if(tagIds==""||itemIds==""){
		    		nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("ITEM_TAG_OPERATE_TIP_NOSEL"));
		    		return ;
		    	}

		    	
		    	nps.confirm(nps.i18n("ITEM_TAG_CONFIRM_BIND"),nps.i18n("ITEM_TAG_CONFIRM_BIND_SEL_TAG_TO_ITEM"), function(){

		        	
		            var json={"itemIds":itemIds,"tagIds":tagIds};
		        	var _d = nps.syncXhr(bindItemTagUrl, json,{type: "GET"});
		        	if(_d.isSuccess){
		        		nps.info(nps.i18n("ITEM_TAG_OPERATE_INFO"),nps.i18n("ITEM_TAG_CONFIRM_BIND_SUCCESS"));
		            	refreshData();
		        	}
		        	else
		            	nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),_d.exception.message);
		        });
		    	
			});

			$j(".button.unbind").click(function() {
				var itemIds="";
				
				var tagId="";
				
				//check未选中
				
				var item_checked_num = 0;
				var tag_checked_num = 0;
				
		    	$j(".tagCheckId:checked").each(function(i,n){
		        	if(i!=0){
		        		itemIds+=",";
		            	}
		        	itemIds+=$j(this).val();
		        	item_checked_num++;
		        });
		    	
		    	$j(".tagTypeCheckId:checked").each(function(i,n){
		        	if(i!=0){
		        		tagId+=",";
		            	}
		        	tagId+=$j(this).val();
		        	tag_checked_num++;
		        });
		    	
		    	if(item_checked_num==0){
		    		nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("ITEM_TAG_OPERATE_TIP_NOSEL_ITEM"));
		    		return ;
		    	}
		    	
		    	if(tag_checked_num!=1){
		    		nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("ITEM_TAG_OPERATE_TIP_NOSEL_ONETAG"));
		    		return ;
		    	}
		    	
		    	
		    	//选择无标签商品给出提示
		    	var isEmptyTagNamesFlag = false;
				var tempTagName ="";
		    	$j(".tagCheckId:checked").each(function(i,n){
		    		
		    		tempTagName =$j("#itemTagName_"+$j(this).val()).val();
		        	if(tempTagName==null||tempTagName==''){
		        		isEmptyTagNamesFlag =true;
		        	}
		        	
		        });
		    	
		    	if(isEmptyTagNamesFlag){
		    		nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("ITEM_TAG_OPERATE_TIP_SEL_ITEM_NOTAG"));
		    		return ;
		    	}
		    	
		    	var json={"itemIds":itemIds,"tagId":tagId};
		    	var result = nps.syncXhr(validateUnBindSelUrl, json,{type: "GET"});
		    	
		    	if(result.isSuccess){
		    		nps.confirm(nps.i18n("ITEM_TAG_CONFIRM_UNBIND"),nps.i18n("ITEM_TAG_CONFIRM_UNBIND_SEL_ITEM_FROM_TAG"), function(){

		        		unBindTag(itemIds,tagId);
		            });
		    	}else{
		    		nps.info(nps.i18n("ITEM_TAG_OPERATE_INFO"),nps.i18n("ITEM_TAG_OPERATE_TIP_UNBIND_NONEED"));
		    	}
			});
			//商品状态
		    $j.ui.loxiasimpletable().typepainter.threeState= {
		        getContent: function(data){
		        	if(data==0){
		        		return "<span class='ui-pyesno ui-pyesno-no' title='下架'></span>";
		        	}
		        	else if(data==1){
		        		return "<span class='ui-pyesno ui-pyesno-yes' title='上架'></span>";
		        	}
					else if(data==3){
						return "<span class='ui-pyesno ui-pyesno-wait' title='新建'></span>";
		        	}
		            
		        },
		        postHandle: function(context){
		            //do nothing
		        }
		    };
			//左下标签表格 
			$j("#tagListTable").loxiasimpletable({
				form:"searchTagTypeForm",
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				cols : [ {
					label : "<input type='checkbox'/>",
					witdh : "20%",
					template : "drawTagTypeCheckbox"
				}, {
					name:"tagName",
					label:nps.i18n("TAG_TABLE_TAG_NAME"),
					width : "25%",
					template : "drawSelectTagName",
					sort:["name asc","name desc"]
				}, {
					name : "typeName",
					label:nps.i18n("TAG_TABLE_TYPE_NAME"),
					width : "25%",
					sort:["type asc","type desc"]
				}, {
					label:nps.i18n("TAG_TABLE_OPERATE"),
					width : "30%",
					template : "drawDelTagType"
				} ],
				dataurl : tagListJson

			});

			//全部标签 表格
			$j("#allTagsTable").loxiasimpletable({
				page : true,
				size : 15,
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				form:"searchForm",
				cols : [ {
					label : "<input type='checkbox'  />",
					width : "4%",
					template:"drawCheckbox"
				}, {
					name : "code",
					label : nps.i18n("LABEL_ITEM_CODE"),
					width : "14%",
					sort:["tpit.code asc","tpit.code desc"]
				}, {
					name : "title",
					label : nps.i18n("LABEL_ITEM_TITLE"),
					width : "14%",
					sort:["tpifo.title asc","tpifo.title desc"]
				}, {
					name : "industryName",
					label : nps.i18n("LABEL_PRODUCT_INDUSTRY"),
					width : "14%",
					sort:["tpid.name asc","tpid.name desc"]
				}, {
					name : "tagNames",
					label : nps.i18n("LABEL_ITEM_TAGNAME"),
					width : "18%",
					template : "formatTagNames"
				}, {
					name : "lifecycle",
					label : nps.i18n("LABEL_ITEM_LIFECYCLE"),
					width : "8%",
					type:"threeState"
				}, {
					name:"createTime",
					label : nps.i18n("LABEL_ITEM_CREATETIME"),
					width : "14%",
					formatter:"formatDate",
					sort:["tpit.create_time asc","tpit.create_time desc"]
				},{
					name:"modifyTime",
					label : nps.i18n("LABEL_ITEM_MODIFYTIME"),
					width : "14%",
					formatter:"formatDate",
					sort:["tpit.modify_time asc","tpit.modify_time desc"]
				} ],
				dataurl : itemListJson
			});

			//已打标签 表格
			$j("#useTagsTable").loxiasimpletable({
				page : true,
				size : 15,
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				form:"searchForm",
				cols : [ {
					label : "<input type='checkbox'  />",
					width : "4%",
					template:"drawCheckbox"
				}, {
					name : "code",
					label : nps.i18n("LABEL_ITEM_CODE"),
					width : "14%",
					sort:["tpit.code asc","tpit.code desc"]
				}, {
					name : "title",
					label : nps.i18n("LABEL_ITEM_TITLE"),
					width : "14%",
					sort:["tpifo.title asc","tpifo.title desc"]
				}, {
					name : "industryName",
					label : nps.i18n("LABEL_PRODUCT_INDUSTRY"),
					width : "14%",
					sort:["tpid.name asc","tpid.name desc"]
				}, {
					name : "tagNames",
					label : nps.i18n("LABEL_ITEM_TAGNAME"),
					width : "18%",
					template : "formatTagNames"
				}, {
					name : "lifecycle",
					label : nps.i18n("LABEL_ITEM_LIFECYCLE"),
					width : "8%",
					type:"threeState"
				}, {
					name:"createTime",
					label : nps.i18n("LABEL_ITEM_CREATETIME"),
					width : "14%",
					formatter:"formatDate",
					sort:["tpit.create_time asc","tpit.create_time desc"]
				},{
					name:"modifyTime",
					label : nps.i18n("LABEL_ITEM_MODIFYTIME"),
					width : "14%",
					formatter:"formatDate",
					sort:["tpit.modify_time asc","tpit.modify_time desc"]
				} ],
				dataurl : itemTagListUrl

			});

			////未打标签 表格
			$j("#unUseTagsTable").loxiasimpletable({
				page : true,
				size : 15,
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				form:"searchForm",
				cols : [ {
					label : "<input type='checkbox'  />",
					width : "4%",
					template:"drawCheckbox"
				}, {
					name : "code",
					label : nps.i18n("LABEL_ITEM_CODE"),
					width : "14%",
					sort:["tpit.code asc","tpit.code desc"]
				}, {
					name : "title",
					label : nps.i18n("LABEL_ITEM_TITLE"),
					width : "14%",
					sort:["tpifo.title asc","tpifo.title desc"]
				}, {
					name : "industryName",
					label : nps.i18n("LABEL_PRODUCT_INDUSTRY"),
					width : "14%",
					sort:["tpid.name asc","tpid.name desc"]
				}, {
					name : "tagNames",
					label : nps.i18n("LABEL_ITEM_TAGNAME"),
					width : "18%",
					template : "formatTagNames"
				}, {
					name : "lifecycle",
					label : nps.i18n("LABEL_ITEM_LIFECYCLE"),
					width : "8%",
					type:"threeState"
				}, {
					name:"createTime",
					label : nps.i18n("LABEL_ITEM_CREATETIME"),
					width : "14%",
					formatter:"formatDate",
					sort:["tpit.create_time asc","tpit.create_time desc"]
				},{
					name:"modifyTime",
					label : nps.i18n("LABEL_ITEM_MODIFYTIME"),
					width : "14%",
					formatter:"formatDate",
					sort:["tpit.modify_time asc","tpit.modify_time desc"]
				} ],
				dataurl : itemNoTagListUrl

			});
			refreshData();
			
			$j(".func-button.search").click(function() {
				//置空标签条件
				$j("#tagId").val("");
				var defaultTitle = nps.i18n("ITEM_TAG_TAGED_TITLE");
				
				setAllTagsTableSelected();
				
				$j("#useTagsTable .ui-loxia-table-title").html(defaultTitle);
				$j("#useTagsTable").attr("caption",defaultTitle);
				$j("#useTagsTable").data().uiLoxiasimpletable.options.title=defaultTitle;
				
				$j("#useTagsTable").data().uiLoxiasimpletable.options.currentPage = 1;
				refreshData();
				
				
			});
			
			//搜索左下标签表
		    
		    $j(".func-button.searchTagType").on("click",function(){
		    	refreshData();
		    });
		    
		    //新增标签
		    $j(".func-button.addTag").on("click",function(){
		    	var tagName = $j("#tagName").val();
		    	
		    	var tagType = $j("#tagType").val();
		    	
		    	if(!checkStrIsNotNull(tagName)||!checkStrIsNotNull(tagType)){
		    		nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("ITEM_TAG_NAME_TYPE_NOTNULL"));
		    		return ;
		    	}
		    	
		    	var json ={"tagName":tagName,"tagType":tagType};
		    	
		    	var result = nps.syncXhr(addTagUrl, json,{type: "GET"});
		    	
		    	$j("#tagName").val("");
		    	
		    	//将tagType 无限selected
		    	$j("#tagType").val('');
		    	
		    	if(result!=undefined&&result.isSuccess){
		    		
		    		nps.info(nps.i18n("ITEM_TAG_OPERATE_INFO"),nps.i18n("ITEM_TAG_ADD_SUCCESSFUL"));
		        	refreshData();
		    	}
		    	else
		        	nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("ITEM_TAG_NAME_EXISTS"));
		    });
		    

		    
			$j("#tagListTable").on("click", ".func-button.delTagType",
					function() {
						var curObject=$j(this);
				    	var tagId = curObject.attr("val");
						
						var _relationtagIdJson ={"q_long_tagId":tagId,"page.size":15,"page.start":1};
						
						var _relationItemListWithpageJson = nps.syncXhr(itemTagListUrl, _relationtagIdJson,{type: "GET"});
						
						var relationItems =_relationItemListWithpageJson.items;
						
						var relationMemberCount =relationItems.length;
						
						var relatItemIds = "";
						
						if(relationMemberCount>0){
							
							for(var idx =0;idx <relationMemberCount;idx++){
								if(idx!=0){
									relatItemIds+=",";
								}
								relatItemIds+=relationItems[idx].id;
							}
							
							nps.confirm(nps.i18n("ITEM_TAG_CONFIRM_DELETE"),nps.i18n("ITEM_TAG_DEL_ITEM_RELATION_TAG"), function(){
				    			//脱离分类
								unBindTag(relatItemIds,tagId);
								
								//删除分组
								removeTag(tagId);
							});
							
						}else{
							nps.confirm(nps.i18n("ITEM_TAG_CONFIRM_DELETE"),nps.i18n("ITEM_TAG_CONFIRM_DELETE_SEL_TAG"), function(){
								removeTag(tagId);
							});
						}
					});

			$j("#tagListTable").on("click",".func-button.selectTagType",
							function() {
								var tableTitle = "【" + $j(this).text()
										+ "】" + "标签";
								
								//筛选
						    	var curObject = $j(this);
						    	
						    	var tagId = curObject.attr("val");
						    	$j("#tagId").val(tagId);

						    	$j("#useTagsTable .ui-loxia-table-title")
								.html(tableTitle);
						    	
						    	$j("#useTagsTable").attr("caption",tableTitle);
								$j("#useTagsTable").data().uiLoxiasimpletable.options.title=tableTitle;
								refreshData();
								setUseTagsTableSelected();
						    	
							});

			/*$j("#allTagsTable").on("click", ".func-button.delTag", delTag);
			$j("#useTagsTable").on("click", ".func-button.delTag", delTag);
			$j("#unUseTagsTable")
					.on("click", ".func-button.delTag", delTag);*/

			

			//批量删除标签
			$j(".func-button.deleteMultyTag").click(function() {
				var data="";
		    	
		    	var tempTagId ="";
		    	var _tempRelationTagIdJson ="";
		    	var _tempRelationItemListWithpageJson ="";
		    	var tempRelationItems ="";
		    	//当前id所关联的member数
		    	var tempRelationTagCount =0;
		    	
		    	var relationTagCount =0;
		    	
		    	var relationTagId ="";
		    	
		    	var isHaveRelativeMember =false;

		    	$j(".tagTypeCheckId:checked").each(function(i,n){
		    		
		    		tempTagId = $j(this).val();
		    		
		    		_tempRelationTagIdJson ={"q_long_tagId":tempTagId,"page.size":15,"page.start":1};
		    		
		    		_tempRelationItemListWithpageJson = nps.syncXhr(itemTagListUrl, _tempRelationTagIdJson,{type: "GET"});
		    		
		    		tempRelationItems =_tempRelationItemListWithpageJson.items;
		    		
		    		tempRelationTagCount =tempRelationItems.length;
		    		if(tempRelationTagCount>0){
		    			
		    			if(relationTagCount!=0){
		    				relationTagId+=",";
		    			}
		    			relationTagId +=tempTagId;
		    			relationTagCount++;
		    			isHaveRelativeMember =true;
		    		}
		    		
		        	if(i!=0){
		            	data+=",";
		            	}
		        	data+=tempTagId;
		        });
		    	
		    	if(data==null||data==''){
		    		nps.info(nps.i18n("ITEM_TAG_CHECK_ERROR"),nps.i18n("PLEASE_SEL_TAG_FIRST"));
		    		return ;
		    	}
		    	
		    	if(isHaveRelativeMember){
		    		nps.confirm(nps.i18n("ITEM_TAG_CONFIRM_DELETE"),nps.i18n("ITEM_TAG_DEL_ITEM_RELATION_TAG"), function(){
		    			//脱离分类
		    			
		    			var TagArray =relationTagId.split(",");
		    			
		    			var _tempConfirmRelationTagIdJson ="";
		    	    	var _tempConfirmRelationTagListWithpageJson ="";
		    	    	var tempConfirmRelationItems ="";
		    	    	//当前id所关联的member数
		    	    	var tempConfirmRelationItemCount =0;
		    	    	var tempConfirmRelationItemIds="";
		    			
		    			//将关联选定分组的会员脱离
		    			for(var tempIdx=0;tempIdx<TagArray.length;tempIdx++){
		    				_tempConfirmRelationTagIdJson ={"q_long_tagId":TagArray[tempIdx],"page.size":15,"page.start":1};
		    				
		    				_tempConfirmRelationTagListWithpageJson = nps.syncXhr(itemTagListUrl, _tempConfirmRelationTagIdJson,{type: "GET"});
		    				
		    				tempConfirmRelationItems =_tempConfirmRelationTagListWithpageJson.items;
		    				
		    				tempConfirmRelationItemCount =tempConfirmRelationItems.length;
		    				
		    				tempConfirmRelationItemIds = "";
		    				
		    				for(var idx =0;idx <tempConfirmRelationItemCount;idx++){
		    					if(idx!=0){
		    						tempConfirmRelationItemIds+=",";
		    					}
		    					tempConfirmRelationItemIds+=tempConfirmRelationItems[idx].id;
		    				}
		    				unBindTag(tempConfirmRelationItemIds,TagArray[tempIdx]);
		    			}
		    			
						
						//删除分组
		    			removeTag(data);
					});
		    	}else{
					nps.confirm(nps.i18n("ITEM_TAG_CONFIRM_DELETE"),nps.i18n("ITEM_TAG_CONFIRM_DELETE_SEL_TAG"), function(){
						removeTag(data);
					});
				}

			});

		});