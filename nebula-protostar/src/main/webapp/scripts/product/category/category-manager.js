$j.extend(loxia.regional['zh-CN'], {
	  "ALERT_SELECT_CATEGORY_EMPTY" : "请先选择一个分类",
	  "CONFIRM_DELETE" : "确认删除",
	  "CONFIRM_DELETE_AGAIN" : "再次确认删除",
	  "CONFIRM_DELETE_CATEGORY" : "确定要删除选定的 {0} 分类吗？",
	  "CONFIRM_DELETE_CATEGORY_ITEM" : "确定要删除选定的 {0} 分类及该分类与商品的关联关系吗？",
	  "INFO_DELETE_CATEGORY" : "删除 {0} 分类成功",
	  "INFO_UPDATE_CATEGORY" : "更新 {0} 分类成功",
	  "INFO_ADD_LEAFCATEGORY" : "添加子分类 {0} 成功",
	  "INFO_INSERT_SIBLINGCATEGORY" : "插入同级分类 {0} 成功",
	  "INFO_CATEGORY_CODE_EMPTY" : "分类编码不能为空",
	  "INFO_CATEGORY_NAME_EMPTY" : "分类名称不能为空",
	  "INFO_CATEGORY_SEARCH_RESULT" : "共找到 {0} 个结果",
	  "ERROR_CATEGORY_UPDATECATEGORY" : "修改分类失败"
	  ,
  });

var findItemCategoryListByCatgoryIdUrl = base + '/product/category/findItemCategoryListByCatgoryId.json';

/**
 * 封装 一下 分类树 自有属性
 * 
 * @type
 */
var treeCategory = {

	/**
	 * 树元素id
	 * 
	 * @type String
	 */
	ztreeElementId : "categoryTree",

	/**
	 * 搜索框 选择器
	 * 
	 * @type String
	 */
	ztreeSearchElementSelector : "#key",

	/**
	 * 高亮的node list
	 * 
	 * @type
	 */
	highlightNodeList : [],

	/**
	 * 高亮节点
	 * 
	 * @param zTree
	 *         zTree
	 * 
	 * @param flag
	 *         true代表高亮,false 代表取消高亮
	 */
	highlightNodes : function(zTree, flag) {
		for (var i = 0, l = this.highlightNodeList.length; i < l; i++) {
			var node = this.highlightNodeList[i];
			node.highlight = flag;
			zTree.updateNode(node);
		}
	}
};

// *****************************************************************
var setting = {
	edit : {
		enable : true,
		showRemoveBtn : false,
		showRenameBtn : false
	},
	view : {
		showLine : true,
		showIcon : false,
		nameIsHTML : true,
		selectedMulti : false,
		// 设置树 节点样式
		fontCss: getFontCss
	},
	data : {
		keep: {
			parent:false,
			leaf:false
		},
		key: {
			title: "name"
		},
		simpleData : {
			enable : true
		}
	},
	callback : {
		/**
		 * 树 点击事件
		 * 
		 * @param event
		 *         标准的 js event 对象
		 * @param treeId
		 *         对应 zTree 的 treeId，便于用户操控
		 * @param treeNode
		 *         被点击的节点 JSON 数据对象
		 * @param clickFlag
		 *         节点被点击后的选中操作类型 详细建 ztree api
		 */
		onClick : function(event, treeId, treeNode, clickFlag) {
			nps.error();
			var code = treeNode.code;
			var isRoot = ("root" == code);

			// 保存,删除,插入同级分类 按钮 如果 选择的是 root 那么 不可用
			$j("#insertSibling,#save_node,#remove_element").attr("disabled", isRoot ? "disabled" : false);

			if (isRoot) {
				// 选中ROOT 设置为空
				$j("#tree_code").val("");
				$j("#tree_name_zh_cn").val("");
			} else {
				$j("#tree_code").val(treeNode.code);
				//$j("#tree_name_zh_cn").val(treeNode.name);
				var id = treeNode.id;
				if(id==0){
					return;
				}
				var data = nps.syncXhrPost(base+"/i18n/category/findCategoryLangByCatgoryId.json", {'categoryId': id});
				if(data == null){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),"选择数据已经不存在了");
					 return;
				}
				if(i18nOnOff){
					$j(".cate-update .mutl-lang").each(function(i,dom){
			    		var me = $j(this);
			    		var lang = me.attr("lang");
			    		if(data.name!=null){
			    			var values = data.name.values;
				    		var langs = data.name.langs;
				    		var num = 0;
				    		for (var i = 0; i < langs.length; i++) {
				    			var l = langs[i];
				    			if(l == null){
				    				num = i;
				    				break;
				    			}
				    			if(l==lang){
				    				num = i;
				    				break;
				    			}
							}
				    		me.val(values[num]);
			    		}else{
			    			me.val(null);
			    		}
		    			
				    });
				}else{
					$j("#tree_name_zh_cn").val(data.name.value);
				}
			}
		},
		/**
		 * 是否允许折叠操作
		 * 
		 * @param treeId
		 * @param treeNode
		 * @returns
		 */
		beforeCollapse : function(treeId, selectNode) {
			nps.error();
			var code = selectNode.code;

			var isRoot = ("root" == code);
			var isCanCollapse = !isRoot;// 根目录 不允许折叠

			// console.dir(selectNode);
			// console.log("selectNode isCanCollapse:" + isCanCollapse);
			return isCanCollapse;
		},

		// ****************************************拖拽**********************************************************
		/**
		 * 拖拽之前判断 是否可以拖拽
		 * 
		 * @param treeId
		 * @param treeNodes
		 * @returns
		 */
		beforeDrag : function(treeId, treeNodes) {
			var selectNode = treeNodes[0];
			var isCanDrag = selectNode.drag;

			// console.dir(selectNode);
			// console.log("selectNode isCanDrag:" + isCanDrag);
			return isCanDrag;
		},

		/**
		 * 拖拽到 折叠节点 还没放下之前, 那个折叠节点是否展开
		 * 
		 * @param treeId
		 * @param treeNodes
		 * @returns {Boolean}
		 */
		beforeDragOpen : function(treeId, treeNodes) {
			var isBeforeDragOpen = false;
			console.log("beforeDragOpen:" + isBeforeDragOpen);
			return isBeforeDragOpen;
		},

		/**
		 * 用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
		 * 
		 * @param treeId
		 * @param treeNodes
		 *         被拖拽的节点 JSON 数据集合
		 * @param targetNode
		 *         treeNodes 被拖拽放开的目标节点 JSON 数据对象。 如果拖拽成为根节点，则 targetNode = null
		 * @param moveType
		 *         指定移动到目标节点的相对位置 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
		 * @returns {Boolean}
		 */
		beforeDrop : function(treeId, treeNodes, targetNode, moveType) {
			nps.error();
			console.log("beforeDrop");
			var selectNode = treeNodes[0];

			console.log(selectNode);
			console.log(targetNode);
			console.log("moveType:" + moveType);

			if (targetNode == null) {
				return false;
			} else {
				// 是否脱离 Root
				// 如果 treeNode 是根节点，则 parentTId = null
				var isBreakAwayRoot = (null == targetNode.parentTId && moveType != "inner");
				console.log("isBreakAwayRoot:" + isBreakAwayRoot);

				if (isBreakAwayRoot) {
					return false;
				} else {
					// ③发送 ajax 请求,remove 分类
					var url = base + "/product/category/dropCategory.json";

					var data = nps.syncXhrPost(url, {
						  selectCategoryId : selectNode.id,
						  targetCategoryId : targetNode.id,
						  moveType : moveType
					  });

					console.log(data);

					if (data.exception) {
						// with exception
						if (data.exception.statusCode == 1) {
							nps.error(nps.i18n("NPS_OPERATE_FAILURE"), nps.i18n("NPS_SYSTEM_ERROR"));
						} else {
							nps.error(nps.i18n("NPS_OPERATE_FAILURE"), data.exception.message);
						}
					} else {
						var backWarnEntity = data;
						if (backWarnEntity.isSuccess) {
							// ③调用 ztree api 修改节点
							var category = backWarnEntity.description;

							selectNode.sortNo = category.sortNo;
							selectNode.pId = category.parentId;

							var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
							zTree.updateNode(selectNode);

							// 由于拖拽 目标节点附近的 节点 只是 sortNo 属性变更 不影响 静态页面前端的显示 ,不需要Ajax返回 页面变更

							// ④给个提示
							// nps.info(nps.i18n("INFO_UPDATE_CATEGORY", [tree_name]));

							return true;
						} else {
							nps.error(nps.i18n("ERROR_INFO"), backWarnEntity.description);
						}
					}

				}
				return false;
			}
		},

		/**
		 * 用于捕获节点拖拽操作结束的事件回调函数
		 * 
		 * @param event
		 * @param treeId
		 * @param treeNodes
		 * @param targetNode
		 * @param moveType
		 *         指定移动到目标节点的相对位置 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
		 */
		onDrop : function(event, treeId, treeNodes, targetNode, moveType) {

			console.log("onDrop");
			var selectNode = treeNodes[0];

			console.log(selectNode);
			console.log(targetNode);
			console.log("moveType:" + moveType);
		}
	}
};

var key,lastValue = "", nodeList = [], fontCss = {};

/**
 * 将搜索到的节点字体置为黄色
 */
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}
//------------搜索选择器--------------
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
 * 搜索节点方法
 * 1.搜索到含有相应关键字的节点 
 * 2.展开该段节点 
 * 3.将字置为黄色
 */
function searchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree");
	var value = $j.trim(key.get(0).value);

    if(value==""){
		$j("#search_result").html("");
		updateCategoryNodes(false);
	}
		if (key.hasClass("empty")) {
			value = "";
		}
		if (lastValue === value) return;
		lastValue = value;
		if (value === "") return;
		updateCategoryNodes(false);
		
	nodeList = zTree.getNodesByParamFuzzy("name", value);
	
	$j("#search_result").html(nps.i18n("INFO_CATEGORY_SEARCH_RESULT", [nodeList.length]));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
 		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateCategoryNodes(true);
	$j("#key").focus();
    
	
}

/**
 * 将搜索到的节点展开的方法
 */
function updateCategoryNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}


function deleteCategory(selectTreeNode, zTree){
	var url = base + "/product/category/removeCategory.json";
	nps.asyncXhrPost(url, {
		id : selectTreeNode.id
	}, {
		successHandler : function(data, textStatus) {
			var nextNode = selectTreeNode.getNextNode();
			var preNode = selectTreeNode.getPreNode();
			var parentNode = selectTreeNode.getParentNode();
			
			// 5调用 ztree api 删除节点
			zTree.removeNode(selectTreeNode, true);
			
			// 6下一个节点/前一个节点/父节点 智能获得焦点
			// 先下一个节点 点击
			if (null != nextNode) {
				$j("#" + nextNode.tId + "_span").click();
			}
			// 没有下个节点 就前一个节点
			else if (null != preNode) {
				$j("#" + preNode.tId + "_span").click();
			}
			// 还没有就 父节点
			else {
				$j("#" + parentNode.tId + "_span").click();
			}
			// 7给个提示
			nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_DELETE_CATEGORY", [selectTreeNode.name]));
		}
	});
}

function fnPromptDelCategory(selectTreeNode, zTree){
	var json = {id : selectTreeNode.id};
	var data = nps.syncXhrPost(findItemCategoryListByCatgoryIdUrl, json);
	if(data != '' && data != null){
		// 分类下有挂商品
		// 4发送 ajax 请求,remove 分类
		nps.confirm(nps.i18n("CONFIRM_DELETE_AGAIN"), nps.i18n("CONFIRM_DELETE_CATEGORY_ITEM", [selectTreeNode.name]), function() {
			deleteCategory(selectTreeNode, zTree);
		});
	}else{
		// 4发送 ajax 请求,remove 分类
		deleteCategory(selectTreeNode, zTree);
	}
}

//------------------------------------


// *****************************************************************
$j(function() {
	  // 加载树
	  $j.fn.zTree.init($j("#" + treeCategory.ztreeElementId), setting, zNodes);

	  // 跟节点 默认点击
	  $j("#" + treeCategory.ztreeElementId + "_1_span").click();

	  //搜索选择器
	  key = $j("#key");
	  key.bind("focus", focusKey)
		.bind("blur", blurKey)
		.bind("propertychange", searchNode)
		.bind("input", searchNode);

	  // **************************删除****************************************
	  $j("#remove_element").click(function() {
		    nps.error();
		    var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
		    var selectedNodes = zTree.getSelectedNodes();

		    // 1提示必须要选择了 节点
		    if (selectedNodes.length == 0) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
			    return;
		    }

		    var selectTreeNode = selectedNodes[0];
		    //2 该分类下有无挂商品

		    // 3提示确认下
		    nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_CATEGORY", [selectTreeNode.name]), function() {
		    	setTimeout(function(){fnPromptDelCategory(selectTreeNode, zTree);}, 500);
		    });
	    });

	  // ***************************保存***********************************************
	  $j("#save_node").click(function() {
		    nps.error();
		    var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
		    var selectedNodes = zTree.getSelectedNodes();

		    // ①提示必须要选择了 节点
		    if (selectedNodes.length == 0) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
			    return;
		    }

		    var selectTreeNode = selectedNodes[0];

		    var tree_code = $j.trim($j("#tree_code").val());
		    var tree_name_zh_cn = $j("#tree_name_zh_cn").val();
		    // ②校验下 非空
		    if (null == tree_code || "" == tree_code) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_CODE_EMPTY"));
			    $j("#tree_code").focus();
			    return;
		    }
		    
		    var selectTreeNodeId = selectTreeNode.id;
		    // ③发送 ajax 请求,update 分类
		    var url = base + "/i18n/product/category/updateCategory.json";
		    var data = {};
		    var defualt = "";
		    if(i18nOnOff){
	    	    var validate = false;
			    $j(".cate-update .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		if (null == val || "" == val) {
		    			validate = true;
		  		    }
		    	});
			    if(validate){
			    	nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
	  			    return;
			    }
		    	var multlangs = '{"categoryCommand.id":"'+ selectTreeNodeId+'"';
		    	multlangs += ',"categoryCommand.code":"' + tree_code+'"';
		    	$j(".cate-update .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"categoryCommand.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"categoryCommand.name.langs['+i+']":"'+lang+'"';
		    	});
		    	multlangs+="}";
		    	data = eval('('+multlangs+')');
		    }else{
		    	 if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
					    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
					    $j("#tree_name_zh_cn").focus();
					    return;
				    }
		    	data = {
					      "categoryCommand.id" : selectTreeNodeId,
					      "categoryCommand.code" : tree_code,
				    	  "categoryCommand.name.value" : tree_name_zh_cn
				      };
		    }
		    nps.asyncXhrPost(url, data, {
			      successHandler : function(data, textStatus) {

				      // ④调用 ztree api 修改节点
				      selectTreeNode.name = tree_name_zh_cn;
				      selectTreeNode.code = tree_code;

				      zTree.updateNode(selectTreeNode);
				      if(!i18nOnOff){
				    	  defualt = tree_name_zh_cn;
				      }
				      // ⑤给个提示
				      nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_UPDATE_CATEGORY", [defualt]));
			      }
		      });
	    });

	  // ***************************添加子分类*********************************************
	  $j("#addLeaf").click(function() {
		    nps.error();
		    var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
		    var selectedNodes = zTree.getSelectedNodes();

		    // ①提示必须要选择了 节点
		    if (selectedNodes.length == 0) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
			    return;
		    }
		    var selectTreeNode = selectedNodes[0];
		    var code = $j.trim($j("#add_code").val());
		   
		    // 选中节点的id
		    var selectTreeNodeId = selectTreeNode.id;
		    // ②校验下 非空
		    if (null == code || "" == code) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_CODE_EMPTY"));
			    $j("#add_code").focus();
			    return;
		    }
		 
		    var data = {};
		    var defualt = "";
		    if(i18nOnOff){
	    	    var validate = false;
			    $j(".cate-add .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		if (null == val || "" == val) {
		    			validate = true;
		  		    }
		    	});
			    if(validate){
			    	nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
	  			    return;
			    }
		    	var multlangs = '{"categoryCommand.parentId":"'+ selectTreeNodeId+'"';
		    	multlangs += ',"categoryCommand.code":"' + code+'"';
		    	$j(".cate-add .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"categoryCommand.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"categoryCommand.name.langs['+i+']":"'+lang+'"';
		    	});
		    	multlangs+="}";
		    	data = eval('('+multlangs+')');
		    }else{
		    	 var tree_name_zh_cn = $j.trim($j("#add_name_zh_cn").val());
		    	 if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
					    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
					    $j("#add_name_zh_cn").focus();
					    return;
				  }
		    	 
		    	data = {
		    			 "categoryCommand.parentId": selectTreeNodeId,
					      "categoryCommand.code" : code,
				    	  "categoryCommand.name.value" : tree_name_zh_cn
				      };
		    }

		    // ③发送 ajax 请求,addLeafCategory
		    var url = base + "/i18n/product/category/addLeafCategory.json";
		    nps.asyncXhrPost(url, data, {
			      successHandler : function(data, textStatus) {
				      var category = data;
				      // ④调用 ztree api 在一个父节点 下面 ,添加一个叶子节点
				      if(i18nOnOff){
				    	  zTree.addNodes(selectTreeNode, {
						        id : category.id, // 取到插入的叶子节点的id
						        pId : selectTreeNodeId,
						        code : code,
						        name : defualt,
						        sortNo : category.sortNo
					        });
				      }else{
				    	  defualt = $j.trim($j("#add_name_zh_cn").val());
				    	  zTree.addNodes(selectTreeNode, {
						        id : category.id, // 取到插入的叶子节点的id
						        pId : selectTreeNodeId,
						        code : code,
						        name : defualt,
						        sortNo : category.sortNo
					        });
				      }
				      // ⑤给个提示
				      nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_ADD_LEAFCATEGORY", [defualt]));
			      }
		      });
	    });

	  // ***************************插入同级分类*********************************************
	  $j("#insertSibling").click(function() {
		    nps.error();
		    var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
		    var selectedNodes = zTree.getSelectedNodes();
		    // ①提示必须要选择了 节点
		    if (selectedNodes.length == 0) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
			    return;
		    }
            
		    var selectTreeNode = selectedNodes[0];
		    var code = $j.trim($j("#add_code").val());
		    var tree_name_zh_cn = $j.trim($j("#add_name_zh_cn").val());
		    // ②校验下 非空
		    if ("" == code || null == code) {
			    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_CODE_EMPTY"));
			    $j("#add_code").focus();
			    return;
		    }
		    // 选中节点的id
		    var selectTreeNodeId = selectTreeNode.id;
		    // 被选中节点的父节点
		    var parentNode = selectTreeNode.getParentNode();
		    var data = {};
		    var defualt = "";
		    if(i18nOnOff){
	    	    var validate = false;
			    $j(".cate-add .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		if (null == val || "" == val) {
		    			validate = true;
		  		    }
		    	});
			    if(validate){
			    	nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
	  			    return;
			    }
		    	var multlangs = '{"selectCategoryId":"'+ selectTreeNodeId+'"';
		    	multlangs += ',"categoryCommand.code":"' + code+'"';
		    	$j(".cate-add .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"categoryCommand.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"categoryCommand.name.langs['+i+']":"'+lang+'"';
		    	});
		    	multlangs+="}";
		    	data = eval('('+multlangs+')');
		    }else{
		    	if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
				    nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
				    $j("#add_name_zh_cn").focus();
				    return;
			    }   
		    	data = {
		    			  "selectCategoryId": selectTreeNodeId,
					      "categoryCommand.code" : code,
				    	  "categoryCommand.name.value" : tree_name_zh_cn
				      };
		    }

		    // ③发送 ajax 请求,插入同级分类
		    var url = base + "/i18n/product/category/insertSiblingCategory.json";
		    nps.asyncXhrPost(url, data, {
			      successHandler : function(data, textStatus) {
				      var category = data;
				      // ④调用 ztree api 在被选中节点的父节点 下面,添加一个子节点
				      // 注意 这个api 返回的是 Array
				      if(!i18nOnOff){
				    	  defualt = tree_name_zh_cn;
				      }
				      var newNodes = zTree.addNodes(parentNode, {
					        id : category.id, // 取到插入的节点的id
					        pId : parentNode.id,
					        code : code,
					        name : defualt,
					        sortNo : category.sortNo
				        });

				      // ⑤将新插入的节点 移动到 选择节点的 前面
				      zTree.moveNode(selectTreeNode, newNodes[0], "prev");
				      // ⑥给个提示
				      nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_INSERT_SIBLINGCATEGORY", [defualt]));
			      }
		      });
	    });
	  
  });