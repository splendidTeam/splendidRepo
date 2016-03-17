
//国际化
$j.extend(loxia.regional['zh-CN'], {
	"ITEM_CATEGORY_TIPS":"友情提示",
	"ITEM_CATEGORY_CHOOSE_LEAF":"请选择叶子节点来查看商品！",
	"LABEL_ITEM_CODE":"商品编码",
	"LABEL_ITEM_TITLE":"商品名称",
	"LABEL_PRODUCT_INDUSTRY":"所属行业",
	"LABEL_ITEM_CATEGORY":"所属分类",
	"LABEL_ITEM_LIFECYCLE":"状态",
	"LABEL_ITEM_CREATETIME":"创建时间",
	"LABEL_ITEM_MODIFYTIME":"修改时间",
	"LABEL_ITEM_LISTTIME":"最近上架时间",
	"ITEM_CATEGORY_CHECK_ERROR":"错误信息",
	"ITEM_CATEGORY_OPERATE_INFO":"提示信息",
	"ITEM_CATEGORY_OPERATE_TIP_NOSEL":"未选中商品或分类",
	"ITEM_CATEGORY_SELECTED_EXIST_DEFAULT_CATEGORY":"该分类是商品的默认分类,该商品不可以脱离该分类",
	"ITEM_CATEGORY_OPERATE_TIP_NOSEL_CANCLE":"未选中商品或未选定仅一个分类",
	"ITEM_CATEGORY_OPERATE_TIP_NOSEL_ITEM_CANCLE":"未选中商品",
	"ITEM_CATEGORY_OPERATE_TIP_NO_ONECATEGORY_CANCLE":"请选择一个分类进行脱离",
	"ITEM_CATEGORY_OPERATE_TIP_NOSEL_ONECATEGORY_CANCLE":"只能选择一个分类进行脱离",
	"ITEM_CATEGORY_OPERATE_TIP_SEL_ITEM_NOCATEGROY":"所选商品存在无分类商品，无法脱离",
	"ITEM_CATEGORY_OPERATE_TIP_UNBIND_NONEED":"所选商品中，没有一条是属于所选分类的，无需脱离",
    "ITEM_CONFIRM_BIND":"确认加入分类",
    "ITEM_CONFIRM_BIND_SUCCESS":"加入成功",
    "ITEM_CONFIRM_BIND_SEL_CATEGORY_TO_ITEM":"确定要将选定的分类加入到选定的商品中么？",
    "ITEM_CONFIRM_UNBIND":"确认脱离分类",
    "ITEM_CONFIRM_UNBIND_SUCCESS":"脱离成功",
    "ITEM_CONFIRM_UNBIND_SEL_ITEM_FROM_CATEGORY":"确定要将选定的商品脱离选定的分类么？",
	"NO_CATEGORY":"无",
	"RELATE_CATEGORY_NO_CATEGORY_DATA":"暂无关联行业维护数据",
	"ITEM_CATEGORY_LIST_SUFFIX_TITLE":"分类商品列表",
	"ITEM_CATEGORY_LIST_DEFAULT_TITLE":"已分类商品",
	"CATEGORY_FIND":" 共找到",
	"CATEGORY_RESULT":"个结果",
	"LABEL_ITEM_DEFCATEGORY":"默认分类",
	"ITEM_CATEGORY_OPERATE_TIP_ONLY_ONE_DEFCATEGORY":"只能选择一个分类进行设置",
	"ITEM_CONFIRM_SETDEFAULT":"确认设置默认分类",
	"ITEM_CONFIRM_DEFAULT_SEL_CATEGORY_TO_CATEGORY":"确定要将选定的分类绑定并设置为选定商品的默认分类么？",
	"ITEM_CONFIRM_SETDEFAULT_SUCCESS":"设置成功",
  });

//常量 url,setting等

//已分类商品列表url
var itemCtListUrl = base + '/item/itemCtList.json';

//未分类商品列表url
var itemNoctListUrl = base + '/item/itemNoctList.json';

//加入分组
var bindItemUrl = base +'/item/bindItemCategory.json';

//脱离分组
var unBindItemUrl = base +'/item/unBindItemCategory.json';

//设置默认分类
var setDefaultCategoryUrl = base +'/item/setDefaultCategoryUrl.json';

var validateUnBindSelUrl = base +'/item/validateUnBindByItemIdsAndCategoryId.json';

var findItemByDefaultCategoryIdUrl = base + '/item/findItemByDefaultCategoryId.json'
//根据分类id查询商品列表
//var itemListByCategoryId = base + '/category/findItemListByCategoryId.json';


//fenlei
var setting = {

		check : {
			enable: true,
		},
		view : {
			showIcon : false,
			fontCss : getFontCss
		},
		edit : {
			enable : false,
			showRenameBtn : false
		},
		data : {
			keep : {
				parent : false,
				leaf : false
			},
			key : {
				title : "name"
			},
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : onClick
		}
	};

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




//函数
function onClick(event, treeId, treeNode) {
	
	var tempTreeId="";
	var tempTreeName="";
	
	if(treeNode.isParent||treeNode.id ==0){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
		
	}else{

		tempTreeId=treeNode.id;
		tempTreeName=treeNode.name;
		$j("#categoryId").val(tempTreeId);
		
		//emptySearch();

		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();

		showItemList(tempTreeName);
	}
	
}	
/*function emptySearch(){
	
	$j("#code").val("");
	$j("#title").val("");
	$j("#lifecycle").val("");
	$j("#industryId").val("");
	$j("#createStartDate").val("");
	$j("#createEndDate").val("");
	$j("#listStartDate").val("");
	$j("#listEndDate").val("");
}*/
/*function onCheck(event, treeId, treeNode)  {
	if(!(treeNode.isParent || treeNode.id==0)){
		treeNode.checked = treeNode.checked?false:true;
	}
}*/

function showItemList(name){
	
	var title="";
	if(null == name||'' == name){
		
		title = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
			
	}else{
		title="【"+name+"】"+nps.i18n("ITEM_CATEGORY_LIST_SUFFIX_TITLE");
			
	}
	$j("#table1 .ui-loxia-table-title").html(title);
	$j("#table1").attr("caption",title);
	$j("#table1").data().uiLoxiasimpletable.options.title=title;
}
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

//样式？重构
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {
		color : "#333",
		"background-color" : "yellow"
	} : {
		color : "#333",
		"font-weight" : "normal",
		"background-color" : ""
	};
}

function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}
function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
}
var lastValue = "", nodeList = [], fontCss = {};
function searchNode(e) {
	
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	var value = $j.trim(key.get(0).value);
	if(value==""){
		$j("#search_result").html("");
		updateNodes(false);
	}
		
		if (key.hasClass("empty")) {
			value = "";
		}
		if (lastValue === value) return;
		lastValue = value;
		if (value === "") return;
		updateNodes(false);
		
	nodeList = zTree.getNodesByParamFuzzy("name", value);
	
	$j("#search_result").html(nps.i18n("CATEGORY_FIND")+ nodeList.length+ nps.i18n("CATEGORY_RESULT"));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
 		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key").focus();
}
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId' isDefault="+ loxia.getObject("isDefault", data)+"  value='" + loxia.getObject("id", data)+"'/>";
}
function formatCategoryNames(data, args, idx){
	var propertyNameArray =loxia.getObject("categoryNames", data);
	
	var categoryNames =propertyNameArray;
	
	if(propertyNameArray==null||propertyNameArray==''){
		categoryNames= nps.i18n("NO_CATEGORY");
	}
	var hiddenCategoryNameInput ="<input type='hidden' id='itemCategoryName_"+loxia.getObject("id", data)+"' value='"+propertyNameArray+"' />";
	categoryNames +=hiddenCategoryNameInput;
	
	return categoryNames;
}

function formatDefCategoryNames(data, args, idx){
	var name =loxia.getObject("defCategory", data);
	
	if(name==null||name==''){
		return nps.i18n("NO_CATEGORY");
	}
	return name;
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

//刷新表格数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
	
	$j("#table2").loxiasimpletable("refresh");
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	//分组树
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(nodes[i].isParent){
			nodes[i].nocheck = true;
			treeObj.refresh();
		}
	}
	
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
	//商品列表
	
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox'  />",
			width : "3%",
			template:"drawCheckbox"
		}, {
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "10%",
			sort:["tpit.code asc","tpit.code desc"]
		}, {
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			sort:["tpifo.title asc","tpifo.title desc"]
		}, {
			name : "industryName",
			label : nps.i18n("LABEL_PRODUCT_INDUSTRY"),
			width : "10%",
			sort:["tpid.name asc","tpid.name desc"]
		}, {
			name : "categoryNames",
			label : nps.i18n("LABEL_ITEM_CATEGORY"),
			width : "20%",
			template : "formatCategoryNames"
		}, {
			name : "defCategory",
			label : nps.i18n("LABEL_ITEM_DEFCATEGORY"),
			width : "10%",
			template : "formatDefCategoryNames"
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_ITEM_LIFECYCLE"),
			width : "5%",
			type:"threeState"
		}, {
			name:"createTime",
			label : nps.i18n("LABEL_ITEM_CREATETIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["tpit.create_time asc","tpit.create_time desc"]
		} ,{
			name:"modifyTime",
			label : nps.i18n("LABEL_ITEM_MODIFYTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["tpit.modify_time asc","tpit.modify_time desc"]
		} ,{
			name:"listTime",
			label : nps.i18n("LABEL_ITEM_LISTTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["tpit.list_time asc","tpit.list_time desc"]
		} ],
		dataurl : itemCtListUrl
	});
	
	$j("#table2").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox'  />",
			width : "3%",
			template:"drawCheckbox"
		}, {
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "10%",
			sort:["tpit.code asc","tpit.code desc"]
		}, {
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			sort:["tpifo.title asc","tpifo.title desc"]
		}, {
			name : "industryName",
			label : nps.i18n("LABEL_PRODUCT_INDUSTRY"),
			width : "10%",
			sort:["tpid.name asc","tpid.name desc"]
		}, {
			name : "categoryNames",
			label : nps.i18n("LABEL_ITEM_CATEGORY"),
			width : "20%",
			template : "formatCategoryNames"
		}, {
			name : "defCategory",
			label : nps.i18n("LABEL_ITEM_DEFCATEGORY"),
			width : "10%",
			template : "formatDefCategoryNames"
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_ITEM_LIFECYCLE"),
			width : "5%",
			type:"threeState"
		},   {
			name:"createTime",
			label : nps.i18n("LABEL_ITEM_CREATETIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["tpit.create_time asc","tpit.create_time desc"]
		} ,{
			name:"modifyTime",
			label : nps.i18n("LABEL_ITEM_MODIFYTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["tpit.modify_time asc","tpit.modify_time desc"]
		} ,{
			name:"listTime",
			label : nps.i18n("LABEL_ITEM_LISTTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["tpit.list_time asc","tpit.list_time desc"]
		} ],
		dataurl : itemNoctListUrl
	});
	refreshData();
	
	// 筛选列表
	$j(".func-button.search").click(function() {
		//置空分类条件
		$j("#categoryId").val("");
		var defaultTitle = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
		$j("#table1 .ui-loxia-table-title").html(defaultTitle);
		$j("#table1").attr("caption",defaultTitle);
		$j("#table1").data().uiLoxiasimpletable.options.title=defaultTitle;
		
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		$j("#table2").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
	
	//分组树筛选
	
	key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	
	//加入分类
	
	$j(".button.orange.addsort").on("click", function(){
       
		var itemIds="";
		
		var categoryIds="";
		
		//check未选中
		
    	$j(".checkId:checked").each(function(i,n){
        	if(i!=0){
        		itemIds+=",";
            	}
        	itemIds+=$j(this).val();
        });
    	
    	var checkedNum=0;
    	
    	for(var i = 0 ; i < nodes.length ; i++){
    		if(nodes[i].checked){
    			checkedNum++;
    			categoryIds+=nodes[i].id+",";
    		}
    	}
    	if(checkedNum>0){
    		categoryIds = categoryIds.substring(0, categoryIds.length-1);
    	}
    	
    	if(checkedNum==0||itemIds==""){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NOSEL"));
    		return ;
    	}

    	
    	nps.confirm(nps.i18n("ITEM_CONFIRM_BIND"),nps.i18n("ITEM_CONFIRM_BIND_SEL_CATEGORY_TO_ITEM"), function(){

        	
            var json={"itemIds":itemIds,"categoryIds":categoryIds};
        	var _d = nps.syncXhr(bindItemUrl, json,{type: "GET"});
        	if(_d.isSuccess){
        		nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"),nps.i18n("ITEM_CONFIRM_BIND_SUCCESS"));
            	refreshData();
        	}
        	else
            	nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),_d.exception.message);
        });
    	
    });
	
	//脱离分类:只能是多个或者一个商品从一个分类下解除
	$j(".button.orange.removesort").on("click", function(){
	       
		var itemIds="";
		
		var categoryId="";
		
		var isDefault = false;
		//check未选中
		
    	$j(".checkId:checked").each(function(i,n){
        	if(i!=0){
        		itemIds+=",";
            	}
        	itemIds+=$j(this).val();
        });
    	
    	var checkedNum=0;
    	
    	for(var i = 0 ; i < nodes.length ; i++){
    		if(nodes[i].checked){
    			checkedNum++;
    			categoryId+=nodes[i].id+",";
    		}
    	}
    	if(checkedNum>0){
    		categoryId = categoryId.substring(0, categoryId.length-1);
    	}
    	
    	if(itemIds==""){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NOSEL_ITEM_CANCLE"));
    		return ;
    	}
    	if(checkedNum == 0){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NO_ONECATEGORY_CANCLE"));
    		return ;
    	}else if(checkedNum!=1){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NOSEL_ONECATEGORY_CANCLE"));
    		return ;
    	}
    	
    	//选择无分类商品给出提示
    	var isEmptyCategoryNamesFlag = false;
		var tempCategoryName ="";
    	$j(".checkId:checked").each(function(i,n){
        	
        	tempCategoryName =$j("#itemCategoryName_"+$j(this).val()).val();
        	if(tempCategoryName==null||tempCategoryName==''||tempCategoryName=="null"){
        		isEmptyCategoryNamesFlag =true;
        	}
        	
        });
    	
    	if(isEmptyCategoryNamesFlag){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_SEL_ITEM_NOCATEGROY"));
    		return ;
    	}
    	
    	//如果分类是商品的默认分类时, 不可以脱离分类  此限制取消
    	
    	//1, 通过categoryId查询该分类为默认分类的商品集合(itemList)
//    	var _json = {"categoryId":categoryId};
//    	var _result = nps.syncXhr(findItemByDefaultCategoryIdUrl, _json, {type:"GET"})
//    	//2, 遍历itemIds与itemList判断是否有一致的
//    	if(_result != ''){
//    		var itemIdArray = itemIds.split(',');
//    		for(var i=0; i<_result.length; i++){
//    			for(var j=0; j<itemIdArray.length;j++){
//    				if(itemIdArray[j] == _result[i].itemId){
//    					isDefault = true;
//    				}
//    			}
//    		}
//    	}
//    	if(isDefault){
//    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_SELECTED_EXIST_DEFAULT_CATEGORY"));
//    		return ;
//    	}
    	
        var json={"itemIds":itemIds,"categoryId":categoryId};
    	
        var result = nps.syncXhr(validateUnBindSelUrl, json,{type: "GET"});
        
        if(result.isSuccess){
        	nps.confirm(nps.i18n("ITEM_CONFIRM_UNBIND"),nps.i18n("ITEM_CONFIRM_UNBIND_SEL_ITEM_FROM_CATEGORY"), function(){
            	var _d = nps.syncXhr(unBindItemUrl, json,{type: "GET"});
            	if(_d.isSuccess){
            		nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"),nps.i18n("ITEM_CONFIRM_UNBIND_SUCCESS"));
                	refreshData();
            	}
            	else
                	nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),_d.exception.message);
            });
        }else{
        	//所选商品中，没有一个是属于所选分类的
        	nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_UNBIND_NONEED"));
        	return ;
        }        
    });
	
	//设置默认分类
	$j('.button.setDefault').on('click',function(){
		//1.设置前校验
		var itemIds="";
		
		var categoryId="";
		
		//check未选中
		
    	$j(".checkId:checked").each(function(i,n){
        	if(i!=0){
        		itemIds+=",";
            	}
        	itemIds+=$j(this).val();
        });
    	
    	var checkedNum=0;
    	
    	for(var i = 0 ; i < nodes.length ; i++){
    		if(nodes[i].checked){
    			checkedNum++;
    			categoryId+=nodes[i].id+",";
    		}
    	}
    	if(checkedNum>0){
    		categoryId = categoryId.substring(0, categoryId.length-1);
    	}
    	
    	if(itemIds==""){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NOSEL_ITEM_CANCLE"));
    		return ;
    	}
    	if(checkedNum == 0){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NO_ONECATEGORY_CANCLE"));
    		return ;
    	}else if(checkedNum!=1){
    		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),nps.i18n("ITEM_CATEGORY_OPERATE_TIP_ONLY_ONE_DEFCATEGORY"));
    		return ;
    	}
    	//2.设置
    	var json={"itemIds":itemIds,"categoryId":categoryId};
    	
    	nps.confirm(nps.i18n("ITEM_CONFIRM_UNBIND"),nps.i18n("ITEM_CONFIRM_DEFAULT_SEL_CATEGORY_TO_CATEGORY"), function(){
        	var _d = nps.syncXhr(setDefaultCategoryUrl, json,{type: "GET"});
        	if(_d.isSuccess){
        		nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"),nps.i18n("ITEM_CONFIRM_SETDEFAULT_SUCCESS"));
            	refreshData();
        	}
        	else
            	nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),_d.exception.message);
        });
		
	});
	
});