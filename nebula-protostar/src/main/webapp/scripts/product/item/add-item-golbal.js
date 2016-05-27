$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"SYSTEM_ITEM_SELECT_INDUSTRY":"请选择商品所在行业",
	"ITEM_UPDATE_CODE_ENBLE":"商品编码不可用",
	"ITEM_UPDATE_CODE_ABLE":"商品编码可用",
	"ITEM_CODE_VALID_FAIL":itemCodeValidMsg,
	"INDUSTRY_FIND":" 共找到",
	"PLEASE_SELECT":" 请选择",
	"INDUSTRY_RESULT":"个结果",
	"ADDITEM_FAIL":"新增商品失败",
	"MERCHANT_CODING":"商家编码",
	"MERCHANT_SALEPRICE":"销售价",
	"MERCHANT_LISTPRICE":"吊牌价",
	"OTHERS":"其他",
	"MUST_SELECT":"为必选项",
	"SELECT_COLOR_PROPERTY":"请选择颜色属性",
	"WRITE_ALL_INFO":"请将信息填写完整",
	"DELETE_THIS_CATEGORY":"删除此分类",
	"SET_THIS_DEFCATEGORY":"设为默认",
	"THIS_BE_DEFCATEGORY":"默认",
	"PLEASE_SELECT_DEFAULT_CATEGORY":"请为商品选择默认分类",
	"NOT_REPEATEDLY_RELEVANCE_CATEGORY":"同一商品不可以多次关联同一分类",
	"IMAGE_SELECT_COLOR_PROPERTY":"颜色属性",
	"NOT_DATA_FORMAT":"数据格式不正确",
	"SALEPRICE_OUT_OF_RANGE":"销售价超出sku设置的销售价区间",
	"PLEASE_INPUT_SALEPRICES":"请输入sku销售价格",
	"LISTPRICE_OUT_OF_RANGE":"吊牌价" +
			"超出sku设置的吊牌价区间",
	"PLEASE_INPUT_LISTPRICE":"请输入sku吊牌价格",
	"MERCHANT_CODING_EQUAL":"商家编码相同",
    "PLEASE_SET_CODE":"请进行编码设置",
    "SKU_CODE_REPEAT":"以下商家编码已经存在：",
    "CUSTOM_PROPERTY_SAME":"填写的自定义属性相同",
    "SALES_PROPERTY_CHANGED":"销售属性已经更新，但是没有重新进行编码设置",
    "PLEASE_INPUT_ONE_SKU_CODE":"请输入至少一个sku编码",
    "PLEASE_SET_DEF_CATEGORY":"请设定默认分类",
    "PLEASE_SELECT_PROPERTY_GROUP":"请选择属性分组"
});

var mustCheckArray  = new Array();
var validateSkuCodesUrl = base +'/item/validateSkuCode.json';

var key,lastValue = "", nodeList = [], fontCss = {};
function onCheck(event, treeId, treeNode){
	
}

function onClick(event, treeId, treeNode){
	var treeObj = $j.fn.zTree.getZTreeObj("industrytreeDemo");
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

function beforeRemove(event, treeId, treeNode){
	
}

function onRemove(event, treeId, treeNode){
	
}

//*******************************默认分类
function defaultOnClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	
	hideMenu("defaultMenuContent");
	return false;
}

function defaultOnCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
	nodes = zTree.getCheckedNodes(true),
	v = "",
	categoryHtml = "";
	var defaultHtml = $j("#chooseDefaultCategory").html();
	var id = $j('#chooseDefaultCategory').children('div').attr('class');
	$j("#chooseDefaultCategory").html("");
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		var inode =$j("."+nodes[i].id+"");
		if(inode.length==0){
			categoryHtml = "<div class="+nodes[i].id +">"+nodes[i].name + 
			"<input type='hidden' name='defaultCategoryId'  value='"+nodes[i].id+"' />"+
			"<a href='javascript:void(0);'id="+nodes[i].id+" style='float:right;margin-right: 760px;text-decoration: underline;color:#F8A721' onclick='delDefaultCategroy(this.id)'>"+nps.i18n("DELETE_THIS_CATEGORY")+"</a><br/></div>";
			$j("#chooseDefaultCategory").append(categoryHtml);
		}else{
			$j("#chooseDefaultCategory").html(defaultHtml);
			zTree.checkNode(treeNode, !treeNode.checked, null, false);
			
			var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
			nodes = zTree.getCheckedNodes(false);
			for (var i=0, l=nodes.length; i<l; i++) {
				if(id==nodes[i].id){
					nodes[i].checked= true;
				}
			}
			zTree.refresh();
			
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_REPEATEDLY_RELEVANCE_CATEGORY"));
			return;
		}
		
	}
}

//删除默认分类
function delDefaultCategroy(id){
	$j("."+id+"").remove();
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
	nodes = zTree.getCheckedNodes(true);
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodes[i].checked= false;
		}
	}
	zTree.refresh();
}

function onBodyDownDefault(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "defaultMenuContent" || $j(event.target).parents("#defaultMenuContent").length>0)) {
		hideMenu("defaultMenuContent");
	}
}
/**********************industrytreeDemo 展示  end********************/

/**
* 搜索节点方法
* 1.搜索到含有相应关键字的节点 
* 2.展开该段节点 
* 3.将字置为黄色
*/
function searchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
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
	
	$j("#search_result").html(nps.i18n("INDUSTRY_FIND")+ nodeList.length+ nps.i18n("INDUSTRY_RESULT"));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key").focus();
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
/**
* 将搜索到的节点字体置为黄色
*/
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}
/**
* 将搜索到的节点展开的方法
*/
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}
/**********************industrytreeDemo 搜索  end********************/

/**
 * only use for save item
 * @param form
 * @param args
 * @param param
 */
function saveItem(form,args,param){
	
    var f = loxia._getForm(form),
    mode = args.mode||"sync",
    lock = (args.lock == undefined || args.lock)?true:false;

	if(lock){
	    loxia.lockPage();
	}
	
	var result=$j.extend({}, f,param);
	
	var c = this.validateForm(result);
	if(c){
	        nps.asyncXhr($(result).attr("action"),result,args)
	    
	}else{
	    loxia.unlockPage();
	}
	
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    $j.fn.zTree.init($j("#industrytreeDemo"), setting, zNodes);
    
    key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	
	var treeObj = $j.fn.zTree.getZTreeObj("industrytreeDemo");
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
	
	//下一步
	$j(".button.orange.next").on("click",function(){
		$j("#secondBtnLineDiv").show();
		$j("#firstBtnLineDiv").hide();
		
		$j("#first").css("display","none");
		$j("#second").css("display","block");
		
		var ztree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
		var nodes_ = ztree.getCheckedNodes(true);
		num = 0;
		if (nodes_.length>0) {
			var industryId =nodes_[0].id;
			var name = nodes_[0].name;
			$j("#chooseIndustry").html(name);
  			$j("#industryId").val(industryId);
  			
			// TODO 这里根据编辑的商品类型不同，调用不同的初使化方法
			try{
				initProperties(industryId);
			} catch(e) {
				// do nothing
			}
		} else {
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SYSTEM_ITEM_SELECT_INDUSTRY"));
		}
		
		//初始化id为colorPropertyContent的div内部的所有Loxia组件
		loxia.initContext($j(".ui-block "));
		
		$j(".spCkb").each(function(){
			var curCheckBox = $j(this);
			
			curCheckBox.change(function(){
				spChangedFlag = true;
				clickFlag = false;
			});
			
		});
		
		$j(".spTa").each(function(){
			var ta = $j(this);
			
			ta.bind("change",function(){
				spChangedFlag = true;
				clickFlag = false;
			});
		});
		
		$j(".normalCheckBoxCls").each(function(){
			
			var curCheckBox = $j(this);
			curCheckBox.change(function(){
				drawNoSalePropEditing4Type(num);				
			});
			
		});
	});
	
	//上一步
    $j(".button.back").on("click",function(){
    	window.location.href=base+"/item/createItem.htm";
    });
    //返回
	$j(".button.return").on("click",function(){
		window.location.href=base+"/item/itemList.htm";
	});
	
	//保存商品
	$j(".button.orange.submit").click(function(){
	   nps.submitForm('itemForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true)
			{
				window.location.href=base+"/item/itemList.htm";
			}else
			{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("ADDITEM_FAIL"));
			}
	   }});
    });
});
