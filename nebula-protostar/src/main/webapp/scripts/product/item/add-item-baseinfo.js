var validateItemCodeUrl = base + '/item/validateItemCode.json';

// 商品分类树设置
var categorySetting = {
	check : {
		enable : true
	},
	view : {
		dblClickExpand : false,
		showIcon : false
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : onClick1,
		onCheck : onCheck1,
	}
};

function onClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"), nodes = zTree
			.getCheckedNodes(true), v = "", categoryHtml = "";
	var defaultHtml = $j("#chooseCategory").html();
	$j("#chooseCategory").html("");
	var defCategroyId = $j("#defaultCategoryId").val();

	var innerDefHtml = "";

	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
		var inode = $j("." + nodes[i].id + "");
		if (inode.length == 0) {

			if (defCategroyId != '' && defCategroyId == nodes[i].id) {
				innerDefHtml = nps.i18n("THIS_BE_DEFCATEGORY");
			} else {
				innerDefHtml = "<a href='javascript:void(0);'id="
						+ nodes[i].id
						+ " onclick='setCategroyDef(this.id)' style='color:#F8A721;text-decoration: underline;'>"
						+ nps.i18n("SET_THIS_DEFCATEGORY") + "</a>";
			}

			categoryHtml = "<div class="
					+ nodes[i].id
					+ ">"
					+ nodes[i].name
					+ "<input type='hidden' name='categoriesIds'  value='"
					+ nodes[i].id
					+ "' /><span style='float:right;margin-right: 1000px;'>"
					+ innerDefHtml
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "<a href='javascript:void(0);'id="
					+ nodes[i].id
					+ " style='color:#F8A721;text-decoration: underline;' onclick='delCategroy(this.id)'>"
					+ nps.i18n("DELETE_THIS_CATEGORY")
					+ "</a></span><br/></div>";
			$j("#chooseCategory").append(categoryHtml);
		} else {
			$j("#chooseCategory").html(defaultHtml);
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps
					.i18n("NOT_REPEATEDLY_RELEVANCE_CATEGORY"));
			zTree.checkNode(treeNode, !treeNode.checked, null, false);
			return;
		}

	}
}

function hideMenu(id) {
	$j("#"+id).fadeOut("fast");
	//$j("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "menuContent" || $j(event.target).parents("#menuContent").length>0)) {
		hideMenu('menuContent');
	}
}

//删除分类
function delCategroy(id){
	$j("."+id+"").remove();
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodes[i].checked= false;
		}
	}
	
	if(id==$j("#defaultCategoryId").val()){
		$j("#defaultCategoryId").val('');
	}
	zTree.refresh();
}

//设为默认分类
function setCategroyDef(id){
	$j("#defaultCategoryId").val(id);
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	var nodeObj=null;
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodeObj =nodes[i];
		}
	}
	onCheck1(null,null,nodeObj);
}

$j(document).ready(function(){
	//检查商品编码是否具有唯一性
	$j("#code").bind("blur",function() {
		var code = $j("#code").val();
		if(code.trim()=="")return;
		// 正则验证
		if(pdValidCode != null && pdValidCode.length > 0){
			var re =new RegExp(pdValidCode);
			if(!re.test(code)){
	 			$j("#code").val("");
	 	 		$j("#loxiaTip-r").show();
	 			$j(".codetip").html(nps.i18n("ITEM_CODE_VALID_FAIL",[code]));
	 			$j("#code").addClass("ui-loxia-error");
	 			return;
			} else {
	 			$j("#loxiaTip-r").show();
	 	 		$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
	 	 		setTimeout(function(){ 
	 	 			$j("#loxiaTip-r").hide();
	 	 		},2000);
			}
		}
 			
		var json={"code":code};
	 	var _d = loxia.syncXhr(validateItemCodeUrl, json,{type: "GET"});
	 	if(_d.isSuccess == false){
	 		$j("#code").val("");
	 		$j("#loxiaTip-r").show();
			$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ENBLE"));
			$j("#code").addClass("ui-loxia-error");
			return;
		} else if(_d.isSuccess == true) {
			$j("#loxiaTip-r").show();
	 		$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
	 		setTimeout(function(){ 
	 			$j("#loxiaTip-r").hide();
	 		},2000); 
	 		return;
		}
	});
	
	$j("#code").bind("focus",function(){
		$j("#loxiaTip-r").hide();
	});
   
	//商品分类下拉树
	$j.fn.zTree.init($j("#treeDemo"), categorySetting, categoryzNodes);
	//让商品分类下拉树只有叶子节点可选
	var _treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
	//1.将所有的节点转换为简单 Array 格式
	var _nodes = _treeObj.transformToArray(_treeObj.getNodes());
	for(var i = 0;i<_nodes.length;i++){
		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
		if(_nodes[i].isParent || _nodes[i].id == 0){
			_nodes[i].nocheck = true;
		}
		_treeObj.refresh();
	}
    $j("#category").click(function() {
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top+ cityObj.outerHeight() + "px"}).slideDown("fast");

		$j("body").bind("mousedown", onBodyDown);
	});
    
    // 添加商品基本信息表单验证方法
    var baseInfoValidator = new FormValidator('', 10, function(){
    	var code = $j("#code").val();
    	
    	// 正则验证
    	if(pdValidCode != null && pdValidCode.length > 0){
    		var re = new RegExp(pdValidCode);
    		if(!re.test(code)){
    			$j("#code").val("");
    			$j("#loxiaTip-r").show();
    			$j(".codetip").html(nps.i18n("ITEM_CODE_VALID_FAIL",[code]));
    			$j("#code").addClass("ui-loxia-error");
    			return nps.i18n("ITEM_CODE_VALID_FAIL",[code]);
    		}else{
    			$j("#loxiaTip-r").show();
    			$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
    			setTimeout(function(){ 
    				$j("#loxiaTip-r").hide();
    			},2000);
    		}
    	}
    	
    	//如果选了分类，则必须设置默认分类
    	var cateLen =$j("input[name='categoriesIds']").length;
    	if(cateLen > 0){
    		var defCategroyId =$j("#defaultCategoryId").val();
    		if(defCategroyId == null || defCategroyId == ''){
    			return nps.i18n("PLEASE_SET_DEF_CATEGORY");
    		}
    	}
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(baseInfoValidator);
});