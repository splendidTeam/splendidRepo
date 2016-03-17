/**
 * 
 * @param cmsTreeSetting
 *            cmsTree的配置，json格式
 * @returns
 */
function CmsTree(cmsTreeSetting){
	//ztree异步请求Url
	this.asyncUrl = base + "/cms/findNodeForTree.json";
	//请求方式
	this.type = 'get';
	
	this.autoParams=["path"];

	//是否支持选择
	this.checkEnable = false;
	
	//选择的类型 radio 或者 checkbox
	this.chkStyle = null;
	
	//checkBox 样式
	this.chkboxType = null;
	
	//radio 样式
	this.radioType = null;

	//树的divId 在jsp中定义的  必需
	this.treeId = cmsTreeSetting.treeId;
	
	//根节点的显示名称  必需
	this.rootName = cmsTreeSetting.rootName;
	//根节点的实际路径  必需
	this.rootPath = cmsTreeSetting.rootPath;
	//根节点的类型 非必需
	this.rootNodeType = cmsTreeSetting.rootNodeType;

	//点击树节点时候的响应事件， 必需
	this.onClickHandler = cmsTreeSetting.onClickHandler;
	
	var key=null;
//	ztree=null;
	var treeId=this.treeId;
	var lastValue = "", nodeList = [], fontCss = {};
	
	if(null!=cmsTreeSetting.searchKeyId){
		this.searchKeyId=cmsTreeSetting.searchKeyId;
	}
	
	if(null!=cmsTreeSetting.searchResultId){
		this.searchResultId=cmsTreeSetting.searchResultId;
	}
	
	var searchResultId=cmsTreeSetting.searchResultId;
	
	if (null != cmsTreeSetting.asyncUrl) {
		this.asyncUrl = cmsTreeSetting.asyncUrl;
	}
	if (null != cmsTreeSetting.type) {
		this.type = cmsTreeSetting.type;
	}

	if (null != cmsTreeSetting.checkEnable) {
		this.checkEnable = cmsTreeSetting.checkEnable;
		if ("checkbox" == cmsTreeSetting.chkStyle) {
			this.chkStyle = cmsTreeSetting.chkStyle;
			this.chkboxType = cmsTreeSetting.chkType;
		}

		if ("radio" == cmsTreeSetting.chkStyle) {
			this.chkStyle = cmsTreeSetting.chkStyle;
			this.radioType = cmsTreeSetting.chkType;
		}
	}

	this.otherParams = {};
	if (null != cmsTreeSetting.nodeType) {
		this.otherParams.nodeType = cmsTreeSetting.nodeType;
	}

	if (null != cmsTreeSetting.selectType) {
		this.otherParams.selectType = cmsTreeSetting.selectType;
	}

	//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
	if (null != cmsTreeSetting.nodeLevel) {
		this.otherParams.nodeLevel = cmsTreeSetting.nodeLevel;
	}
	
	if( null != cmsTreeSetting.excludePath){
		this.otherParams.excludePath=cmsTreeSetting.excludePath;
	}

	this.getFontCss = function(treeId, treeNode) {
		return (!!treeNode.highlight) ? {
			color : "#333",
			"background-color" : "yellow"
		} : {
			color : "#333",
			"font-weight" : "normal",
			"background-color" : ""
		};
	}

	this.setting = {

		check : {
			enable : this.checkEnable,
			chkStyle : this.chkStyle,
			chkboxType : this.chkboxType,
			radioType : this.radioType
		},
		view : {
			showIcon : false,
			fontCss : this.getFontCss
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
			onClick : this.onClickHandler,// 自定义 必须
		},
		async : {
			enable : true,
			url : this.asyncUrl, // 自定义
			contentType : "application/x-www-form-urlencoded",
			autoParam : this.autoParams, // 自定义 必须
			otherParam : this.otherParams,
			dataType : "text",// 默认text
			type : this.type,// 默认post
		}
	};

	this.getInit = function() {
		var zrootNode = [ {
			"name" : this.rootName,
			id : this.rootPath,
			path : this.rootPath,
			root : true,
			isParent : true,
			type : this.rootNodeType, 
			nocheck : true
		} ];
		$j.fn.zTree.init($j("#" + this.treeId), this.setting, zrootNode);
		var rootNode = this.getTree().getNodeByParam("id", this.rootPath, null);
		this.getTree().reAsyncChildNodes(rootNode, "refresh", false);
		ztree=this.getTree();
		this.initSearch();
	};
	
	this.getRootNode = function(){
		var rootNode = this.getTree().getNodeByParam("id", this.rootPath, null);
		return rootNode;
	}

	this.initSearch = function() {
		if(null==this.searchKeyId){
			return ;
		}
		key = $j("#"+this.searchKeyId);
		key.bind("focus", this.focusKey).bind("blur", this.blurKey).bind(
				"propertychange", this.searchNode).bind("input",
				this.searchNode);
	};

	this.focusKey = function(e) {
		if (key.hasClass("empty")) {
			key.removeClass("empty");
		}
	};

	this.blurKey = function(e) {
		if (key.get(0).value === "") {
			key.addClass("empty");
		}
	};

	this.searchNode = function(e) {
		var zTree = getTree();
		var value = $j.trim(key.get(0).value);
		if (value == "") {
			$j("#"+searchResultId).html("");
			updateNodes(false);
		}
		if (key.hasClass("empty")) {
			value = "";
		}
		if (lastValue === value)
			return;
		lastValue = value;
		if (value === "")
			return;
		updateNodes(false);

		nodeList = zTree.getNodesByParamFuzzy("name", value);

		$j("#"+searchResultId).html(
				nps.i18n("SEARCH_RESULT", [ nodeList.length ]));

		if (nodeList.length > 0) {
			$j.each(nodeList, function(i, node) {
//				zTree.expandNode(node, true, false, false);
				expandNode(node);
			});
		}
		updateNodes(true);
		$j("#"+this.searchKeyId).focus();
	};
	
	function expandNode(node){
		var zTree = getTree();
		if(null==node.getParentNode()){
			return;
		}else{
			expandNode(node.getParentNode())
			zTree.expandNode(node, true, false, false);
		}
	}
	
	function updateNodes(highlight) {
		var zTree = getTree();
		for( var i=0, l=nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;
			zTree.updateNode(nodeList[i]);
		}
	};
	
	this.getTree = function() {
		return $j.fn.zTree.getZTreeObj(this.treeId);
	};
	
	function getTree(){
		return $j.fn.zTree.getZTreeObj(treeId);
	}

}
