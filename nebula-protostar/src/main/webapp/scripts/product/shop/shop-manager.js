$j.extend(loxia.regional['zh-CN'],{
	"SHOP_MANAGER_TIPS":"友情提示：",
	"SHOP_MANAGER_CHOOSE_LEAF":"请选择叶子节点来查看属性！",
	"SHOP_MANAGER_PROPERY_MAINTAIN":"行业属性维护",
	"SHOP_MANAGER_NO_INDUSTRY_DATA":"暂无行业属性维护数据",
	"SHOP_MANAGER_CONFIRM_DELETE_LEAF":"确认删除 节点 -- " ,
	"SHOP_MANAGER_MA":" 吗？",
	"SHOP_MANAGER_SYSTEM_P":"系统属性",
	"SHOP_MANAGER_SHOP_P":"店铺属性",
	"SHOP_MANAGER_SINGLE_INPUT":"单行输入",
	"SHOP_MANAGER_SINGLE_CHOOSE_INPUT":"可输入单选",
	"SHOP_MANAGER_SINGLE":"单选",
	"SHOP_MANAGER_MULTIPLE_CHOOSE":"多选",
	"SHOP_MANAGER_TEXT":"文本",
	"SHOP_MANAGER_NUMBER":"数值",
	"SHOP_MANAGER_DATE":"日期",
	"SHOP_MANAGER_TIME":"日期时间",
	"SHOP_MANAGER_TRUE":"是",
	"SHOP_MANAGER_FALSE":"否",
	"SHOP_MANAGER_NO_USE":"不可用",
	"SHOP_MANAGER_USE":"可用",
	"SHOP_MANAGER_PROPERTY_NAME":"属性名称",
	"SHOP_MANAGER_EDIT_TYPE":"编辑类型",
	"SHOP_MANAGER_VALUE_TYPE":"值类型",
	"SHOP_MANAGER_ISSALE":"是否销售",
	"SHOP_MANAGER_ISCOLOR":"是否颜色",
	"SHOP_MANAGER_NO":"序号",
	"SHOP_MANAGER_STATUS":"状态",
	"SHOP_MANAGER_ISSYSTEM_P":"是否系统属性",
	"SHOP_MANAGER_CREATETIME":"创建时间",
	"SHOP_MANAGER_OPERATE":"操作",
	"SHOP_MANAGER_CHOOSE_INDUSTRY_ADD":"请选择要添加到那个行业下面？",
	"SHOP_MANAGER_MAKESURE_ENABLE_P":"确认启用属性",
	"SHOP_MANAGER_SURE_ENABLE_PRO":"确认启用属性吗？",
	"SHOP_MANAGER_TIPS_":"提示:",
	"SHOP_MANAGER_ENABLE_P_SUC":"启用属性成功！",
	"SHOP_MANAGER_ENABLE_P_FAIL":"启用属性失败！",
	"SHOP_MANAGER_MAKESURE_DISABLE_P":"确认禁用属性",
	"SHOP_MANAGER_SURE_DISABLE_PRO":"确认禁用属性吗？",
	"SHOP_MANAGER_DISABLE_P_SUC":"禁用属性成功！",
	"SHOP_MANAGER_DISABLE_P_FAIL":"禁用属性失败！",
	"SHOP_MANAGER_MAKESURE_DELETE_P":"确认删除属性",
	"SHOP_MANAGER_SURE_DELETE_PRO":"确认删除属性吗？",
	"SHOP_MANAGER_DELETE_P_SUC":"删除属性成功！",
	"SHOP_MANAGER_DELETE_P_FAIL":"删除属性失败！",
	"SHOP_MANAGER_MODIFY":"修改",
	"SHOP_MANAGER_DISABLE":"禁用",
	"SHOP_MANAGER_ENABLE":"启用",
	"SHOP_MANAGER_REMOVE":"删除",
	"SHOP_MANAGER_SET_PRO_VALUE":"设置属性可选值",
	"SHOP_MANAGER_NO_DATA_FOUND":"未找到数据",
});

var setting = {
		
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		view: {
			showIcon:false,
			fontCss: getFontCss
		},
		edit: {
			enable: false,
			showRenameBtn: false
		},
		data: {
			keep: {
				parent:false,
				leaf:false
			},
			key: {
				title: "name"
			},
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: onClickIndustry,
			onCheck: onCheck,
			beforeRemove:beforeRemove
		}
	};

var findProperyListUrl = base+'/shop/propertyList.json';

var enableOrDisablePropertyUrl = base +'/shop/enableOrDisableProperty.json';

var tempUrl = '';

var removePropertyUrl = base+'/shop/removeProperty.json';

var modifyPropertyUrl = base + '/shop/updateProperty.htm';

var addPropertyUrl = base + '/shop/createProperty.htm';

var tempIndustryName = '';
var tempIndustryId = '';

/**
 * 点击某一行业的name的时候可以让radio选中效果，然后后台加载选中行业的所有属性
 * @param event
 * @param treeId
 * @param treeNode
 * @return
 */
function onClickIndustry(event, treeId, treeNode) {
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(!nodes[i].isParent){
			nodes[i].checked = false;
			treeObj.refresh();
		}
	}
		treeNode.checked = true;
		treeObj.refresh();
		onCheck(event, treeId, treeNode);
}		
function onCheck(event, treeId, treeNode)  {
	//如果选中是父节点 或者 ROOT节点的话就不让加载属性，并给予提示：
	if(treeNode.isParent || treeNode.id==0){
		nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("SHOP_MANAGER_CHOOSE_LEAF"));
	}else{
		showPropertyList(treeNode.id,treeNode.name);
	}
}

function showPropertyList(industryId,name){

	 
	 tempIndustryName = name;
	 tempIndustryId = industryId;
	 
	 var shopId = $j(".shopId").val();
	 var industryId = industryId;
	 
	 refreshData(industryId, shopId);
	 //设置临时的 table加载的url。 为之后的 启用/禁用/删除行业的属性使用。
	 tempUrl = findProperyListUrl+"?industryId="+industryId+"&shopId="+shopId;
	 
		if(null == name){
			$j("#listTitle").html(nps.i18n("SHOP_MANAGER_NO_INDUSTRY_DATA"));
		}else{
			//$j("#listTitle").html("【"+name+"】"+nps.i18n("SHOP_MANAGER_PROPERY_MAINTAIN"));
			
			var title="【"+name+"】"+nps.i18n("SHOP_MANAGER_PROPERY_MAINTAIN");
			
			$j("#table1 .ui-loxia-table-title").html(title);
			$j("#table1").attr("caption",title);
			$j("#table1").data().uiLoxiasimpletable.options.title=title;
		}
}

function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}
function beforeRemove(treeId, treeNode,e) {
	className = (className === "dark" ? "":"dark");	
	if(confirm(nps.i18n("SHOP_MANAGER_CONFIRM_DELETE_LEAF") + treeNode.name + nps.i18n("SHOP_MANAGER_MA")))
	{
         removeNode(treeNode,null);
	}
}




function onRemove(e, treeId, treeNode) {
}
function onClick(event, treeId, treeNode)  {
	 // alert(treeNode.id + ", " + treeNode.name);
		$j("#tree_fid").val(treeNode.name);
		$j("#tree_state").val(treeNode.state);
		$j("#tree_sort").val(treeNode.sort);
		$j("#tree_fid").attr("treeId",treeId);
		$j("#tree_fid").data("treeNode",treeNode);
		$j("#tree_fid").data("event",event);
		if(treeNode.id==0 && treeNode.isParent){
			$j(".percent70-content-right").css("display","none");
		}else{
			$j(".percent70-content-right").css("display","block");
		}
}

function onNodeCreated(e, treeId, treeNode) {
		if(treeId!="tree"){
		var zTree = $j.fn.zTree.getZTreeObj("tree");
		var node = zTree.getNodeByParam("id", treeId, null);
		zTree.moveNode(treeNode,  node  , "prev");
		return false;
		}
}
function formatType(val){
	if(val == true) return nps.i18n("SHOP_MANAGER_SYSTEM_P");
	else return nps.i18n("SHOP_MANAGER_SHOP_P");
}

function editingTypeFormat(val){
	if(val == 1){ 
		return nps.i18n("SHOP_MANAGER_SINGLE_INPUT");
	}else if(val == 2){
		return nps.i18n("SHOP_MANAGER_SINGLE_CHOOSE_INPUT");
	}else if( val == 4){
		return nps.i18n("SHOP_MANAGER_SINGLE");
	}else{
		return nps.i18n("SHOP_MANAGER_MULTIPLE_CHOOSE");
	}
}

function valueTypeFormat(val){
	if(val == 1){  
		return nps.i18n("SHOP_MANAGER_TEXT");
	}else if(val == 2){
		return nps.i18n("SHOP_MANAGER_NUMBER");
	}else if( val == 4){
		return nps.i18n("SHOP_MANAGER_DATE");
	}else{
		return nps.i18n("SHOP_MANAGER_TIME");
	}
}

function booleanFormat(val){
	if(val == true){  
		return nps.i18n("SHOP_MANAGER_TRUE");
	}else {
		return nps.i18n("SHOP_MANAGER_FALSE");
	}
}

function stateFormatter(val){
	if(val==null||val==0){
		return nps.i18n("SHOP_MANAGER_NO_USE");
	}
	else if(val==1){
		return nps.i18n("SHOP_MANAGER_USE");
	}
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
//删除属性
function deleteProp(data,args,caller){
	var curObject=$j(caller);
	nps.confirm(nps.i18n("SHOP_MANAGER_MAKESURE_DELETE_P"),nps.i18n("SHOP_MANAGER_SURE_DELETE_PRO"), function(){
		
		
		nps.asyncXhrPost(removePropertyUrl, {"id":data.id,"shopId":$j(".shopId").val(),"industryId":tempIndustryId}, {
			success : function(data, textStatus) {
			var backWarnEntity = data;
			if (backWarnEntity.isSuccess) {
				nps.info(nps.i18n("SHOP_MANAGER_TIPS_"),nps.i18n("SHOP_MANAGER_DELETE_P_SUC"));
				refreshData();
			} else {
				nps.info(nps.i18n("SHOP_MANAGER_DELETE_P_FAIL"),nps.i18n("SHOP_MANAGER_DELETE_P_FAIL"));
			}
		}
		});
		
	});
}

//启用属性
function enableProp(data,args,caller){


	var curObject=$j(caller);
	 nps.confirm(nps.i18n("SHOP_MANAGER_MAKESURE_ENABLE_P"),nps.i18n("SHOP_MANAGER_SURE_ENABLE_PRO"), function(){

		

		   nps.asyncXhrPost(enableOrDisablePropertyUrl, {"id":data.id,"type":1}, {
	  			success : function(data, textStatus) {
	  				var backWarnEntity = data;
	  				if (backWarnEntity.isSuccess) {
	  					nps.info(nps.i18n("SHOP_MANAGER_TIPS_"),nps.i18n("SHOP_MANAGER_ENABLE_P_SUC"));
	  					refreshData();
	  				} else {
	  					nps.info(nps.i18n("SHOP_MANAGER_ENABLE_P_FAIL"),backWarnEntity.description);
	  				}
	  			}
	  		});
	 });
}
// 禁用属性
function disableProp(data,args,caller){
	var curObject=$j(caller);
	 nps.confirm(nps.i18n("SHOP_MANAGER_MAKESURE_DISABLE_P"),nps.i18n("SHOP_MANAGER_SURE_DISABLE_PRO"), function(){

  
  	
      nps.asyncXhrPost(enableOrDisablePropertyUrl, {"id":data.id,"type":0}, {
			success : function(data, textStatus) {
				var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					nps.info(nps.i18n("SHOP_MANAGER_TIPS_"),nps.i18n("SHOP_MANAGER_DISABLE_P_SUC"));
					refreshData();
				} else {
					nps.info(nps.i18n("SHOP_MANAGER_DISABLE_P_FAIL"),backWarnEntity.description);
				}
			}
		});
      
  });
}





function editUser(data){
	 
	 var list =null;
	 
	 if(!data.isSystem){
	 
		 list=[{label:nps.i18n("SHOP_MANAGER_MODIFY"), type:"href", content:modifyPropertyUrl+"?id="+data.id+"&shopName="+$j(".shopName").val()+"&industryName="+tempIndustryName+"&shopId="+$j(".shopId").val()},
	             {label:nps.i18n("SHOP_MANAGER_DISABLE"), type:"jsfunc", content:"disableProp"},
	             {label:nps.i18n("SHOP_MANAGER_REMOVE"), type:"jsfunc", content:"deleteProp"},
	             {label:nps.i18n("SHOP_MANAGER_SET_PRO_VALUE"), type:"href", content:base+'/shop/propertyValueList.json?propertyId=${{id}}'}];
	         if(data.lifecycle == 0){
	             list[1].label = nps.i18n("SHOP_MANAGER_ENABLE");
	             list[1].content = "enableProp";
	         }
	 }
	 return list;
	 
}



function refreshData(industryId, shopId){
	
	var dataUrl="";
	if(industryId==undefined){
		dataUrl=tempUrl;
	}else{
		dataUrl=findProperyListUrl+"?industryId="+industryId+"&shopId="+shopId;  
	}
	
	$j("#table1").data().uiLoxiasimpletable.options.dataurl=dataUrl;
	 $j("#table1").loxiasimpletable("refresh");
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
		
		if (key.hasClass("empty")) {
			value = "";
		}
		if (lastValue === value) return;
		lastValue = value;
		if (value === "") return;
		updateNodes(false);
		
	nodeList = zTree.getNodesByParamFuzzy("name", value);

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


$j(document).ready(function(){
	
	var firstNodeId=null;
	var firstNodeName=null;
	
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	//1.将所有的节点转换为简单 Array 格式
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	var booleanFlag = true;
	for(var i = 0;i<nodes.length;i++){
		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
		if(nodes[i].isParent || nodes[i].id == 0){
			nodes[i].nocheck = true;
		}else{
			if(booleanFlag){
				//3.第一个叶子节点的 radio为checked，然后把booleanFlag设置为false
				nodes[i].checked = true;
				var node = nodes[i].getParentNode();
				console.log(node.name);
				if(null != node){
					node.open = true;
					firstNodeId=nodes[i].id;
					firstNodeName=nodes[i].name;
					booleanFlag = false;
				}
				//onCheck(event, treeId, nodes[i]);
				
			}
		}
		treeObj.refresh();
	}
	

	
	key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	
	
	
	// **************************
	$j("#table1").loxiasimpletable({
		nodatamessage:'<span>'+nps.i18n("SHOP_MANAGER_NO_DATA_FOUND")+'</span>',
		cols:
		[
		
			{name:"name",label:nps.i18n("SHOP_MANAGER_PROPERTY_NAME"),width:"25%",sort:["p.name asc","p.name desc"]},
			{name:"editingType",label:nps.i18n("SHOP_MANAGER_EDIT_TYPE"),width:"10%",formatter:"editingTypeFormat"},
			{name:"valueType",label:nps.i18n("SHOP_MANAGER_VALUE_TYPE"),width:"10%",formatter:"valueTypeFormat"},
			{name:"isSaleProp",label:nps.i18n("SHOP_MANAGER_ISSALE"),width:"7%",type:"yesno"},
			{name:"isColorProp",label:nps.i18n("SHOP_MANAGER_ISCOLOR"),width:"7%",type:"yesno"},
			
			{name:"lifecycle",label:nps.i18n("SHOP_MANAGER_STATUS"),width:"7%",type:"yesno"},
			{name:"isSystem",label:nps.i18n("SHOP_MANAGER_ISSYSTEM_P"),width:"10%", formatter:"formatType",sort:"p.is_system"},
			{name:"createTime",label:nps.i18n("SHOP_MANAGER_CREATETIME"),width:"14%",formatter:"formatDate",sort:"p.create_time"},
			{label:nps.i18n("SHOP_MANAGER_OPERATE"),width:"10%",type : "oplist",
                oplist: editUser}
		]
		
	});
	var shopName = $j(".shopName").val();
	//修改属性
	$j("#table1").on("click",".func-button.modify",function(){
		
		window.location.href=modifyPropertyUrl+"?id="+$j(this).attr("val")+"&shopName="+shopName+"&industryName="+tempIndustryName+"&shopId="+$j(".shopId").val();
	});
	
	 // 新增
    $j(".button.orange.addProperty").click(function(){
    	//判断是否有点选叶子节点
    	var treeObj = $j.fn.zTree.getZTreeObj("tree");
//    	var nodes = treeObj.transformToArray(treeObj.getNodes());
    	var nodes = treeObj.getCheckedNodes();
    	if(nodes.length == 0){
    		nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("SHOP_MANAGER_CHOOSE_INDUSTRY_ADD"));
    	}else{
    		window.location.href=addPropertyUrl+"?industryId="+tempIndustryId+"&shopName="+shopName+"&industryName="+tempIndustryName+"&shopId="+$j(".shopId").val();
    	}
    	
    });
    

    //标签跳转
/*	$j("#updateProp").click(function(){
		//var shopId = $j("#shopId").val();
		window.location.href=base+"/shop/shopPropertymanager.htm?shopId="+$j(".shopId").val();
	});*/
	$j("#updateShop").click(function(){
		//var orgId = $j("#up_orgId").val();
		window.location.href=base+"/shop/updateShop.htm?shopid="+$j(".shopId").val();
	});
	$j(".ui-tag-change").each(function(){
		$j(this).find(".tag-change-ul").find("li").eq(0).removeClass("selected");
	});
	showPropertyList(firstNodeId,firstNodeName);
   
    /*var blnClicked=false;
    $j(".button.chk.radio_false_full").each(function(i,n){

		if($j(this).css("display")!="none"&&!blnClicked){
			
			$j(this).click();
			
			blnClicked=true;
		}
	});*/
	$j(".button.goBackBtn").click(function(){
		 window.location.href=base+"/shop/shopList.htm?keepfilter=true";
	});
   
});
