$j.extend(loxia.regional['zh-CN'],{ 
	"NAVIGATION_FIND":" 共找到",
	"NAVIGATION_RESULT":"个结果",
	"NAVIGATION_MESSAGE":"提示信息",
	"NAVIGATION_EXIST":"该导航已经存在",
	"NAVIGATION_EDIT_SUCCESS":"修改成功",
	"NAVIGATION_CHILD_SUCCRSS":"添加成功",
	"NAVIGATION_SORT_SUCCRSS":"排序成功",
	"NAVIGATION_SELECT":"请先选择一个导航",
	"NAVIGATION_DELETE_SUCCRSS":"删除成功",
	"NAVIGATION_EDIT_NAME":"请输入要修改的名称",
	"NAVIGATION_EDIT_PARAM":"请选择分类",
	"NAVIGATION_EDIT_ROOT":"根节点无法进行修改",
	"NAVIGATION_NAME":"请输入你要添加导航的名称",
	"NAVIGATION_SORT_ONLY_ONE":"该导航没有同级节点",
	"NAVIGATION_SORT_ERROR":"请在同级导航节点内拖拽",
	"NAVIGATION_DELETE_ROOT":"根节点无法进行删除",
	"NAVIGATION_INSERT_PLACE":"请选择要插入的位置",
	"NAVIGATION_CHILD_EXIST":"子节点中已存在该导航",
	"NAVIGATION_HAVE_CHILD":"该导航有子节点无法删除",
	"NAVIGATION_ADMINISTRATOR":"操作失败请联系管理员",
	"NAVIGATION_DELETE_CONFIRM":"您确认要删除该导航？",
	"NAVIGATION_DISABLE_CHILD":"无效节点不允许添加子节点",
	"NAVIGATION_DISABLE_FATHER":"禁用该导航会同时禁用其子导航，确认禁用吗？",
	"NAVIGATION_URL_NODE_HAS_CHILD_CATEGORY":"子节点中存在类型为分类类型的节点，需要把分类类型子节点改为URL类型才能继续修改操作"
});

/********************************************** URL **********************************************/
var SAVE_OR_UPDATE_URL = base + "/i18n/base/saveOrUpdateNavigation.json";
var REMOVE_URL = base + "/base/removeNavigation.json";
var SORT_URL = base + "/base/sortNavigation.json";
var TREE_URL = base + "/base/navigationTree.json";

/********************************************** 全局变量 **********************************************/
var key,lastValue = "", nodeList = [], fontCss = {}, curParameterInput = null;
var NAVI_TREE = null;	//导航树
var CATE_TREE = null;	//分类树

//增加导航json
//var addJsonStr=null;
//
//
////更新导航json
//var updateJsonStr=null;
//
////分类与属性弹出框所属，增加(add)或更新(update)
//var dialogType="";

//所有属性
//var dynamicPropertyCommand="${dynamicPropertyCommand}";

/********************************************** ready **********************************************/
$j(document).ready(function(){
	$j("#toItemSort").css('visibility','hidden');
	
	
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	$j.fn.zTree.init($j("#categoryDemo"), categorySetting, category_ZNodes);
	
	key = $j("#key");
	key.bind("focus", focusKey)
		.bind("blur", blurKey)
		.bind("propertychange", searchNode)
		.bind("input", searchNode);

	$j("#tree_1_span").click();
	
	NAVI_TREE = $j.fn.zTree.getZTreeObj("tree");
	

	
	
	
	CATE_TREE = $j.fn.zTree.getZTreeObj("categoryDemo");
	
	NAVI_TREE.expandAll(true)
	
	var allNodes = NAVI_TREE.transformToArray(NAVI_TREE.getNodes());
	for(var i=0; i<allNodes.length; i++){
		if(allNodes[i].state==0){
			$j("#"+allNodes[i].tId+"_span").addClass('light-grey');
		}
	}

	var treeCategoryNodes = CATE_TREE.transformToArray(CATE_TREE.getNodes());
	for(var i = 0;i<treeCategoryNodes.length;i++){
		if(treeCategoryNodes[i].id == 0){
			treeCategoryNodes[i].nocheck = true;
			CATE_TREE.refresh();
			break;
		}
	}
	
	/**
	 * 修改节点名称
	 * 1.验证修改名称是否为空
	 * 2.验证是否重名
	 * 3.如果当前节点是无效节点，则需要将子节点也置为无效
	 */
	$j("#save_father_Name").click(function(){
		
		var name = $j.trim($j("#tree_name_zh_cn").val());
		//类型  1：URL类型; 2：分类类型
		var type = $j("#update-type").val();
		var url = $j.trim($j("#update-url").val());
		var param = $j("#update-parameter").data("category") +  $j("#update-parameter").data("property");
		var status = $j("#update-status").val();
		var isNewWin = $j("#update-newWindow").prop("checked");
		
		if(!name) {
			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EDIT_NAME"));
			$j("#tree_name_zh_cn").focus();
			return false;
		}

		var nodes = NAVI_TREE.getSelectedNodes();
		if (type == "2") {
			if(!param) {
				nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EDIT_PARAM"));
				$j("#update-parameter").addClass("ui-loxia-error");
				return false;
			}
		} else {
			param = "";
			if (nodes.length>0) {
				var childenNodes = nodes[0].children;
				if (childenNodes != null && childenNodes.length > 0) {
					var allN = NAVI_TREE.transformToArray(nodes[0].children);
					for (var i = 0; i < allN.length; i++) {
						if (allN[i].diy_type == 2) {
							nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_URL_NODE_HAS_CHILD_CATEGORY"));
							$j("#update-type").addClass("ui-loxia-error");
							return false;
						}
					}
				}
			}
		}
		
		if (nodes.length>0) {	
			if(nodes[0].id!=0){
			
			if(nodes[0].name!= name){	
				if(!validateNavigationName(name,nodes[0].pId)){
					nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EXIST"));
					return ;
				}
			}
			//国际化
			
			var json ={};
			var ids ="";
			if(status==0 && nodes[0].children!=null && nodes[0].children.length>0 ){
				var allN = NAVI_TREE.transformToArray(nodes[0].children);
				for(var i=0; i<allN.length; i++){
					ids += allN[i].id+",";
				}
			}
			if(i18nOnOff){
				var validate = false;
			    $j(".nav-update .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		if (! validateNavigationName(val, nodes[0].id)) {
		    			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EXIST"));
		    			return ;
		    		}
		    		if (null == val || "" == val) {
		    			validate = true;
		    			$j(this).focus();
		  		    }
		    		
		    	});
			    if(validate){
			    	nps.error(nps.i18n("NAVIGATION_MESSAGE"), "导航名不能为空");
	  			    return;
			    }
			    
		    	var multlangs = '{"navigationCommand.id":"'+ nodes[0].id+'"';
		    	multlangs += ',"navigationCommand.lifecycle":"'+ status+'"';
		    	multlangs += ',"navigationCommand.type":"' + type+'"';
		    	multlangs += ',"navigationCommand.url":"' + url+'"';
		    	multlangs += ',"navigationCommand.param":"' + param+'"';
		    	multlangs += ',"navigationCommand.sort":"' + nodes[0].diy_sort+'"';
		    	multlangs += ',"navigationCommand.isNewWin":"' + isNewWin+'"';
		    	multlangs += ',"navigationCommand.ids":"' + ids+'"';
			    multlangs += ',"naviStr":"'+ $j("#update-parameter").data("json") +'"';
			    
		    	$j(".nav-update .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.name.langs['+i+']":"'+lang+'"';
		    	});
			    
		    	//seo标题
		    	$j(".nav-update .seoTitle").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.seoTitle.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.seoTitle.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	
		    	//seo关键字
		    	$j(".nav-update .seoKeyWords").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.seoKeyWords.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.seoKeyWords.langs['+i+']":"'+lang+'"';
		    	});		    	
		    	
		    	//seo描述
		    	$j(".nav-update .seoDescription").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.seoDescription.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.seoDescription.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	//扩展
		    	$j(".nav-update .seoExtntion").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.extention.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.extention.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	multlangs+="}";
		    	
		    	json = eval('('+multlangs+')');
		    	
		    	
			}else{
				json={
						"navigationCommand.id":nodes[0].id,
						"navigationCommand.lifecycle":status,
						"navigationCommand.name.value":name,
						"navigationCommand.type":type,
						"navigationCommand.url":url,
						"navigationCommand.param":param,
						"navigationCommand.sort":nodes[0].diy_sort,
						"navigationCommand.isNewWin":isNewWin,
						"navigationCommand.ids":ids
					};
			}			
			if(ids!=''){				
				nps.confirm(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_DISABLE_FATHER"), function(){
					nps.asyncXhrPost(SAVE_OR_UPDATE_URL, json, {
						   successHandler : function(rs, textStatus) {
							   var navi = rs.model;
							   if (rs.isSuccess){
								   nps.asyncXhrPost(TREE_URL, null, {
									   successHandler : function(rs) {
										   NAVI_TREE.destroy();
										   $j.fn.zTree.init($j("#tree"), setting, eval(rs.tree));
										   NAVI_TREE = $j.fn.zTree.getZTreeObj("tree");
										   NAVI_TREE.expandAll(true);
										   var allNodes = NAVI_TREE.transformToArray(NAVI_TREE.getNodes());
										   for(var i=0; i<allNodes.length; i++){
											   if(allNodes[i].state==0){
												   $j("#"+allNodes[i].tId+"_span").addClass('light-grey');
											   }
										   }
										   var sltNode = NAVI_TREE.getNodeByParam("id", navi.id, null);
										   $j("#"+sltNode.tId+"_span").click();
										   lastValue = "";
										   searchNode(null);
					   					}
								   });
								   nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EDIT_SUCCESS"));
							   }else{
								   nps.info(nps.i18n("NAVIGATION_MESSAGE"),rs.errMsg);
							   }
						   }
				  	});
				});
			} else {
				nps.asyncXhrPost(SAVE_OR_UPDATE_URL, json, {
					successHandler : function(rs, textStatus) {
						var navi = rs.model;
						if (rs.isSuccess){
							nps.asyncXhrPost(TREE_URL, null, {
								successHandler : function(rs, textStatus) {
									NAVI_TREE.destroy();
									$j.fn.zTree.init($j("#tree"), setting, eval(rs.tree));
									NAVI_TREE = $j.fn.zTree.getZTreeObj("tree");
									NAVI_TREE.expandAll(true);
									
									var allNodes = NAVI_TREE.transformToArray(NAVI_TREE.getNodes());
									for(var i=0; i<allNodes.length; i++){
										if(allNodes[i].state==0){
											$j("#"+allNodes[i].tId+"_span").addClass('light-grey');
										}
									}
									var sltNode = NAVI_TREE.getNodeByParam("id", navi.id, null);
									$j("#"+sltNode.tId+"_span").click();
									lastValue = "";
									searchNode(null);
				   				}
				   			});
							nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EDIT_SUCCESS"));
		  		 		}else{
		  		 			nps.info(nps.i18n("NAVIGATION_MESSAGE"),rs.errMsg);
		  		 		}
					}
				});
			}
		} else {
			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EDIT_ROOT"));
		}
	} else {
		nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_SELECT"));
	}
});	
	
	/**
	 * 删除节点
	 * 1.根节点不允许删除
	 * 2.如果该节点下含有子节点也不允许删除
	 */
	$j("#remove_element").click(function(){
		var nodes = NAVI_TREE.getSelectedNodes();
		if (nodes && nodes.length>0) {
			if(nodes[0].id!=0){
				if(!nodes[0].isParent){
				nps.confirm(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_DELETE_CONFIRM"), function(){
	
				var json={"id":nodes[0].id};
				nps.asyncXhrPost(REMOVE_URL, json, {
					   successHandler : function(data, textStatus) {	
						var backWarnEntity = data;
						if(backWarnEntity.isSuccess){
							var sltId = 0;
		  					if(nodes[0].getPreNode()!=null){
			  					sltId = nodes[0].getPreNode().id;
			  				}else if(nodes[0].getNextNode()!=null){
			  					sltId = nodes[0].getNextNode().id;
			  				}else{
			  					sltId = nodes[0].getParentNode().id;
			  				}
							nps.asyncXhrPost(TREE_URL, null, {
					   					successHandler : function(rs, textStatus) {
											NAVI_TREE.destroy();
											$j.fn.zTree.init($j("#tree"), setting, eval(rs.tree));
											NAVI_TREE = $j.fn.zTree.getZTreeObj("tree");
											NAVI_TREE.expandAll(true)
											
											var allNodes = NAVI_TREE.transformToArray(NAVI_TREE.getNodes());
											for(var i=0; i<allNodes.length; i++){
												if(allNodes[i].state==0){
													$j("#"+allNodes[i].tId+"_span").addClass('light-grey');
												}
											}
											var sltNode = NAVI_TREE.getNodeByParam("id", sltId, null);
											$j("#"+sltNode.tId+"_span").click();
											lastValue = "";
											searchNode(null);
					   					}
					   			});
			  				nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_DELETE_SUCCRSS"));
							}else{
					  			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_ADMINISTRATOR"));
					  		}
				  		}
				  	 });
					});
					}else{
						nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_HAVE_CHILD"));
					}
			}else{
				nps.info(nps.i18n("NAVIGATION_MESSAGE"), nps.i18n("NAVIGATION_DELETE_ROOT"));	
			}
		}else{
			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_SELECT"));
		}
	});	
	
	/**
	 * 添加子节点
	 * 1.验证导航名是否为空,为空则返回并将光标定位在输入框
	 * 2.验证是否重名
	 * 3.验证该节点是否无效节点，如果无效则不允许添加子节点
	 * 4.验证当前是否选中要添加的节点位置
	 */
	$j("#addLeaf").bind("click", {isParent:false}, function(e){
		var name = $j.trim($j("#add_name_zh_cn").val());
		var type = $j("#add-type").val();
		var url = $j.trim($j("#add-url").val());
		var param = $j("#add-parameter").data("category") +  $j("#add-parameter").data("property");
		var isNewWin = $j("#add-newWindow").prop("checked");
		if (type == "2") {
			if(!param) {
				nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EDIT_PARAM"));
				$j("#add-parameter").addClass("ui-loxia-error");
				return false;
			}
		} else {
			param = "";
		}
		
		var zTree = $j.fn.zTree.getZTreeObj("tree"),
		isParent = e.data.isParent,
		nodes = zTree.getSelectedNodes(),
		treeNode = nodes[0];
		
		

		if (treeNode.state == 0) {
			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_DISABLE_CHILD"));
			return false;
		}
		
		var sort = 1;	//排序号
		if ( treeNode.children && treeNode.children.length > 0) {
			$j.each(treeNode.children, function(i, c) {
				if (parseInt(c.diy_sort) > sort) {
					sort = parseInt(c.diy_sort);
				} 
			});
			sort ++;
		}
		
		if (treeNode) {
			
			//国际化
			var json ={};
			if(i18nOnOff){
				var validate = false;
			    $j(".nav-add .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		if (! validateNavigationName(val, treeNode.id)) {
		    			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EXIST"));
		    			return ;
		    		}
		    		if (null == val || "" == val) {
		    			validate = true;
		    			$j(this).focus();
		  		    }
		    		
		    	});
			    if(validate){
			    	nps.error(nps.i18n("NAVIGATION_MESSAGE"), "导航名不能为空");
	  			    return;
			    }
		    	var multlangs = '{"navigationCommand.parentId":"'+ treeNode.id+'"';
		    	multlangs += ',"navigationCommand.id":-1';
		    	multlangs += ',"navigationCommand.type":"' + type+'"';
		    	multlangs += ',"navigationCommand.url":"' + url+'"';
		    	multlangs += ',"navigationCommand.param":"' + param+'"';
		    	multlangs += ',"navigationCommand.sort":"' + sort+'"';
		    	multlangs += ',"navigationCommand.isNewWin":"' + isNewWin+'"';
			    multlangs += ',"naviStr":"'+ $j("#add-parameter").data("json") +'"';
		    	$j(".nav-add .mutl-lang").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.name.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.name.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	
		    	
		    	
		    	//seo标题
		    	$j(".nav-add .seoTitle").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.seoTitle.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.seoTitle.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	
		    	//seo关键字
		    	$j(".nav-add .seoKeyWords").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.seoKeyWords.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.seoKeyWords.langs['+i+']":"'+lang+'"';
		    	});		    	
		    	
		    	//seo描述
		    	$j(".nav-add .seoDescription").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.seoDescription.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.seoDescription.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	//扩展
		    	$j(".nav-add .seoExtntion").each(function(i,dom){
		    		var me = $j(this);
		    		var val = me.val();
		    		var lang = me.attr("lang");
		    		if(defaultlang==lang){
		    			defualt = val;
		    		}
		    		multlangs+=',"navigationCommand.extention.values['+i+']":"'+val+'"';
		    		multlangs+=',"navigationCommand.extention.langs['+i+']":"'+lang+'"';
		    	});
		    	
		    	
		    	multlangs+="}";
		    	json = eval('('+multlangs+')');
				
			}else{
				if(name == "" || name == null){
					nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_NAME"));
					$j("#add_name_zh_cn").focus();
					return false;
				}
				if (! validateNavigationName(name, treeNode.id)) {
					nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_EXIST"));
					return ;
				}
				json={
						"navigationCommand.parentId": treeNode.id,
						"navigationCommand.name.value": name,
						"navigationCommand.id": -1,
						"navigationCommand.type": type,
						"navigationCommand.url": url,
						"navigationCommand.param": param,
						"navigationCommand.sort": sort,
						"navigationCommand.isNewWin": isNewWin
				};
			}
			
			
			nps.asyncXhrPost(SAVE_OR_UPDATE_URL, json, {
				successHandler : function(rs, textStatus) {
					var navi = rs.model;
	  				if (rs.isSuccess) {
	  					nps.asyncXhrPost(TREE_URL, null, {
	  						successHandler : function(rs) {
	  							NAVI_TREE.destroy();
								$j.fn.zTree.init($j("#tree"), setting, eval(rs.tree));
								NAVI_TREE = $j.fn.zTree.getZTreeObj("tree");
								NAVI_TREE.expandAll(true)
								
								var allNodes = NAVI_TREE.transformToArray(NAVI_TREE.getNodes());
								for(var i=0; i<allNodes.length; i++){
									if(allNodes[i].state==0){
										$j("#"+allNodes[i].tId+"_span").addClass('light-grey');
									}
								}
								var sltNode = NAVI_TREE.getNodeByParam("id", navi.id, null).getParentNode();
								$j("#"+sltNode.tId+"_span").click();
								lastValue = "";
								searchNode(null);
	  						}
					   	});
					   	$j("#key").focus();
					   	reset("add");
	  					nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_CHILD_SUCCRSS"));
	  				}else{
	  					nps.info(nps.i18n("NAVIGATION_MESSAGE"),rs.errMsg);
	  				}
		  		}
		  	});
		} else {
			nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_INSERT_PLACE"));
		}
	});

	$j(".select-category").click(function() {
		curParameterInput = $j(this).siblings("input").eq(0);
		
//		alert($j("#update-parameter").data("property"));
		
		var categoryIds = curParameterInput.data("category");
		var propertyIds = curParameterInput.data("property");
		
		CATE_TREE.checkAllNodes(false);  //取消所有选中效果
		if(categoryIds){
			var ids = (categoryIds+"").split(",");
			if(ids.length>0){
				for(var i=0;i<ids.length;i++){
					var node = CATE_TREE.getNodeByParam("id", ids[i], null);
					if (node) {
						CATE_TREE.checkNode(node, true);
					}
				}
			}
		}
		
		
		
		
		//属性勾选
		$j("#propertiesDiv input").attr("checked", false);  //全部取消勾选
		if(propertyIds){
			var propertyArray=propertyIds.split(",");
			for(var i=0;i<propertyArray.length;i++){
				$j("#propertiesDiv input[value='"+propertyArray[i] +"']").attr("checked", true);
			}
		}

		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#categoryMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

		$j("body").bind("mousedown", categoryOnBodyDown);
	});
	
	
	$j("#update-type,#add-type").change(function(){
		var type = $j(this).val();
		if (type == "1") {
			$j(this).parent().siblings(".navi-param").hide();
			$j("#toItemSort").css('visibility','hidden');
			
		} else {
			$j(this).parent().siblings(".navi-param").show();
			$j("#toItemSort").css('visibility','visible');
		}
	});
	
	//关闭选择属性的弹层
	$j("#selectCategoryBtn").click(function(){
		dealNavigationParam();
		categoryHideMenu();
		
	});
	
	$j(".propertySelectEvent").click(function(){
		createShowParamLabel();
		dealNavigationParam();
	});
	
	
	
	var navigationId = $j("#navigationId").val();
	var node = null;
	if(navigationId){
		 node = NAVI_TREE.getNodeByParam("id",navigationId);
	}else{
		//默认选中节点ID为0的
		 node = NAVI_TREE.getNodeByParam("id",0);
		 navigationId = "0";
	}
	if(node){
		NAVI_TREE.selectNode(node);
//		NAVI_TREE.checkNode(node,true,true);
		NAVI_TREE.setting.callback.onClick(null, NAVI_TREE.setting.treeId, node);//调用事件  
	}
	
	
	
});

/********************************************** zTree配置与事件 **********************************************/
//导航树
var setting = {
	
	check: {
		enable: false
	},
	view: {
		showIcon:false,
		fontCss: getFontCss
	},
	edit: {
		enable: true,
		showRemoveBtn: false,
		showRenameBtn: false,
		drag: {
			inner: false,
			isCopy: false
		}
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
		onClick: onClick,
		beforeDrag: beforeDrag,
		beforeDrop: beforeDrop,
		onDrop: onDrop
	}
};

//分类树
var categorySetting = {
	check: {
		enable: true,
//		chkStyle: "radio",    由单选改成复选
//		radioType: "all"
		chkboxType: { "Y":"", "N":""}  //父子节点选中与取消选择级联情况：不相关
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
		onClick: categoryClick,
		onCheck: categoryCheck
	}
};

/**
* 导航树点击事件
* @param {} event
* @param {} treeId
* @param {} treeNode
*/
function onClick(event, treeId, treeNode)  {
	
	//取消输入框的错误提示
	loxia.byId($j("#tree_name_zh_cn")).state(null);
	loxia.byId($j("#update-parameter")).state(null);
	
	var isRoot = (treeNode.id == 0);
	
	if (isRoot) {
		reset("update");
	} else {
		
		var id = treeNode.id;
		if(id==0){
			$j("#toItemSort").css('visibility','hidden');
			return;
		}
		var data = nps.syncXhrPost(base+"/i18n/navigation/findNavigationLangByNavigationId.json", {'navigationId': id});
		if(data == null){
			 nps.info(nps.i18n("NAVIGATION_MESSAGE"),"选择数据已经不存在了");
			 return;
		}
		if(i18nOnOff){
			
			//名称
			$j(".nav-update .mutl-lang").each(function(i,dom){
	    		var me = $j(this);
	    		var lang = me.attr("lang");
	    		if(data.name!=null){
	    			var values = data.name.values;
		    		var langs = data.name.langs;
		    		var num = 0;
		    		for (var i = 0; i < langs.length; i++) {
		    			if(langs[i]==lang){
		    				num = i;
		    				break;
		    			}
					}
		    		me.val(values[num]);
	    		}else{
	    			me.val(null);
	    		}
		    });
			
			
			//seo标题
			$j(".nav-update .seoTitle").each(function(i,dom){
	    		var me = $j(this);
	    		var lang = me.attr("lang");
	    		if(data.seoTitle!=null){
	    			var values = data.seoTitle.values;
		    		var langs = data.seoTitle.langs;
		    		var num = 0;
		    		for (var i = 0; i < langs.length; i++) {
		    			if(langs[i]==lang){
		    				num = i;
		    				break;
		    			}
					}
		    		me.val(values[num]);
	    		}else{
	    			me.val(null);
	    		}
		    });
			
			//seoKeyWords
			$j(".nav-update .seoKeyWords").each(function(i,dom){
	    		var me = $j(this);
	    		var lang = me.attr("lang");
	    		if(data.seoKeyWords!=null){
	    			var values = data.seoKeyWords.values;
		    		var langs = data.seoKeyWords.langs;
		    		var num = 0;
		    		for (var i = 0; i < langs.length; i++) {
		    			if(langs[i]==lang){
		    				num = i;
		    				break;
		    			}
					}
		    		me.val(values[num]);
	    		}else{
	    			me.val(null);
	    		}
		    });
			
			//seoDescription
			$j(".nav-update .seoDescription").each(function(i,dom){
	    		var me = $j(this);
	    		var lang = me.attr("lang");
	    		if(data.seoDescription!=null){
	    			var values = data.seoDescription.values;
		    		var langs = data.seoDescription.langs;
		    		var num = 0;
		    		for (var i = 0; i < langs.length; i++) {
		    			if(langs[i]==lang){
		    				num = i;
		    				break;
		    			}
					}
		    		me.val(values[num]);
	    		}else{
	    			me.val(null);
	    		}
		    });
			//extention
			$j(".nav-update .seoExtntion").each(function(i,dom){
	    		var me = $j(this);
	    		var lang = me.attr("lang");
	    		if(data.extention!=null){
	    			var values = data.extention.values;
		    		var langs = data.extention.langs;
		    		var num = 0;
		    		for (var i = 0; i < langs.length; i++) {
		    			if(langs[i]==lang){
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
			$j("#tree_name_zh_cn").val(treeNode.name);
		}
		
		//全部取消选中
		CATE_TREE.checkAllNodes(false);
		
		//默认当前分类与属性信息指向修改导航
		curParameterInput = $j("#update-parameter");
		
		//分类与属性设置
		if(data.facetParameterList && data.facetParameterList.length>0){
			//页面显示 “参数”显示
//			var paramLabel="";
			
			 //勾选的属性ID拼接串
			var propertIds = "";
			
			//勾选的分类ID拼接串
			var categoryIds = "";  
			for(var i=0;i<data.facetParameterList.length;i++){
				var facetParameter = data.facetParameterList[i];
				
				//分类
				if(facetParameter.facetType=="CATEGORY"){
					for(var j=0;j<facetParameter.values.length;j++){
						var info = facetParameter.values[j].split("-")
						var id=info[info.length-1];
						var node = CATE_TREE.getNodeByParam("id", id, null);
						if (node) {
							CATE_TREE.checkNode(node, true, false,false);
							categoryIds +=  facetParameter.values[j] +","
						}
					}
				}
				//属性
				if(facetParameter.facetType=="PROPERTY"){
					for(var j=0;j<facetParameter.values.length;j++){
							propertIds  +=  facetParameter.values[j] +","
							$j("#propertiesDiv input[value='"+facetParameter.values[j]+"']").attr("checked", true);
					}
				}

			}
			
			
			//重置隐藏属性值
			if(propertIds){
				curParameterInput.data("property",propertIds.substring(0,propertIds.length-1));
			}else{
				curParameterInput.data("property","");
			}
			
			//重置隐藏分类值
			if(categoryIds){
				curParameterInput.data("category",categoryIds.substring(0,categoryIds.length-1));
			}else{
				curParameterInput.data("category","");
			}
			
			createShowParamLabel();
			
			//触发选中分类与属性
			$j("#selectCategoryBtn").click();
			
		}else{
			//无分类与属性信息，置空
			curParameterInput.val("");
			curParameterInput.data("category", "");
			curParameterInput.data("property", "");
		}
		
		
		if (isParentNavigationURLType(treeNode)) {	// 父节点是URL类型，不能更改类型为分类类型的节点，或添加分类类型的子节点
			$j("#update-type").val(1);
			$j("#update-type option[value='2']").remove();
			$j("#update-type").parent().siblings(".navi-param").hide();
			$j("#add-type").val(1);
			$j("#add-type option[value='2']").remove();
			$j("#add-type").parent().siblings(".navi-param").hide();
		} else {
			if (treeNode.diy_type == 1) {	// 当前节点是URL类型，不能添加分类类型的子节点
				$j("#add-type").val(1);
				$j("#add-type option[value='2']").remove();
				$j("#add-type").parent().siblings(".navi-param").hide();
			} else {
				//新增导航初始化
				var addFlag = false;
				
				$j("#add-type option").each(function(){
					if ($j(this).val() == 2) {
						addFlag = true;
					}
				});
				
				if (!addFlag) {
					$j("#add-type").prepend("<option value='2'>分类类型</option>");
				}
				
				if ($j("#add-type").find("option:selected").val() == 2) {
					$j("#add-type").parent().siblings(".navi-param").show();
				}
				
			}
			
			//修改导航初始化
			var updateFlag = false;
			
			$j("#update-type option").each(function(){
				if ($j(this).val() == 2) {
					updateFlag = true;
				}
			});
			
			if (!updateFlag) {
				$j("#update-type").prepend("<option value='2'>分类类型</option>");
			}
			
			if ($j("#update-type").find("option:selected").val() == 2) {
				$j("#update-type").parent().siblings(".navi-param").show();
				//重置排序链接
				$j("#toItemSort").attr("href","/navigation/itemSort.htm?navigationId="+treeNode.id).css('visibility','visible');
				
			}else{
				$j("#toItemSort").css('visibility','hidden');
			}
			
			$j("#update-type").val(treeNode.diy_type);
			
			$j("#update-type").change();	//触发事件，是否显示 “参数” 输入框
		}
		
		//页面“URL”显示
		$j("#update-url").val(treeNode.diy_url);
		
		//页面“状态”显示
		$j("#update-status").val(treeNode.state);
		
		//页面“新窗口”显示
		$j("#update-newWindow").prop("checked", treeNode.diy_isNewWin);
		
	}
}

/**
 * 拖拽前事件
* @param treeId
* @param treeNodes
* @returns {Boolean}
 */
function beforeDrag(treeId, treeNodes) {
	$j("#"+treeNodes[0].tId+"_span").click();
	
	if (treeNodes.length != 1) {	//不允许多节点同时拖拽
		return false;
	}
	
	var treeNode = treeNodes[0];	//被拖拽的节点
	var allSiblings = NAVI_TREE.getNodesByParam("pId", treeNode.pId, null);	//被拖拽节点的所有兄弟节点（包括其本身）
	
	if (allSiblings.length === 1) {
		nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_SORT_ONLY_ONE"));
	}
	
	return allSiblings.length > 1;	//只有被拖拽节点有兄弟节点时，才允许拖拽
}

/**
 * 安放前事件
* @param treeId
* @param treeNodes
* @param targetNode
* @param moveType
* @returns {Boolean}
 */
function beforeDrop(treeId, treeNodes, targetNode, moveType) {
	var treeNode = treeNodes[0];	//被拖拽的节点
	var allSiblings = NAVI_TREE.getNodesByParam("pId", treeNode.pId, null);	//被拖拽节点的所有兄弟节点（包括其本身）
	var siblings = new Array();	//被拖拽节点的兄弟节点
	
	$j.each(allSiblings, function(i, n) {
		if (n.id != treeNode.id) {
			siblings.push(n);
		}
	});
	
	var isSibling = false;	//“目标节点”是否是“被拖拽节点”的兄弟节点 

	$j.each(siblings, function(i, n){
		if (targetNode.pId == n.pId) {
			isSibling = true;
			return;
		}
	});
	
	if (! isSibling) {
		nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_SORT_ERROR"));
	}

	return isSibling;
}

/**
 * 安放下事件
* @param event
* @param treeId
* @param treeNodes
* @param targetNode
* @param moveType
 */
function onDrop(event, treeId, treeNodes, targetNode, moveType) {
	var treeNode = treeNodes[0];	//被拖拽的节点
	var allSiblings = NAVI_TREE.getNodesByParam("pId", treeNode.pId, null);	//被拖拽节点的所有兄弟节点（包括其本身）
	var ids = "";	//排序后的节点id
	$j.each(allSiblings, function(i, n) {
		ids += n.id + ((i == allSiblings.length - 1) ? "" : ",");
	});
	
	nps.asyncXhrPost(SORT_URL, {"ids": ids}, {
		successHandler : function(rs) {
			if (rs.isSuccess) {
				nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_SORT_SUCCRSS"));
			} else {
				nps.info(nps.i18n("NAVIGATION_MESSAGE"),nps.i18n("NAVIGATION_ADMINISTRATOR"));
			}
		}
	});
}

//分类点击函数 获得树结构
function categoryClick(e, treeId, treeNode) {
	CATE_TREE.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

//分类节点点击事件
function categoryCheck(e, treeId, treeNode) {
//	nodes = CATE_TREE.getCheckedNodes(true);
//	if (nodes.length > 0) {
//		var nodeIds = "";
////		var nodeNames  = "";
//		for(var i=0;i<nodes.length;i++){
//			nodeIds = nodeIds +  nodes[i].id +",";
////			nodeNames += nodes[i].name +",";
//		}
//		//去掉最后 一个,
//		nodeIds = nodeIds.substring(0,nodeIds.length-1);
////		nodeNames = nodeNames.substring(0,nodeNames.length-1);
//		
//		curParameterInput.data("category", nodeIds);
////		curParameterInput.val(nodeNames);
//		curParameterInput.blur();
//		curParameterInput.removeClass("ui-loxia-error");
//	} else {
//		curParameterInput.val("");
//		curParameterInput.data("category", "");
//	}
	
	createShowParamLabel();
	dealNavigationParam();
	//categoryHideMenu();
}

//鼠标移动隐藏事件
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}

function categoryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "categoryName" || event.target.id == "categoryMenuContent" || $j(event.target).parents("#categoryMenuContent").length>0)) {
		categoryHideMenu();
	}
}

/********************************************** 导航树搜索 **********************************************/
function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}

function blurKey(e) {
	if (key.val() == "") {
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
	var value = $j.trim(key.val());
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
		
	nodeList = NAVI_TREE.getNodesByParamFuzzy("name", value);
	
	$j("#search_result").html(nps.i18n("NAVIGATION_FIND")+ nodeList.length+ nps.i18n("NAVIGATION_RESULT"));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
 		 NAVI_TREE.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key").focus();
}

/**
 * 将搜索到的节点展开的方法
 */
function updateNodes(highlight) {
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		NAVI_TREE.updateNode(nodeList[i]);
	}
}

/**
 * 将搜索到的节点字体置为黄色
 */
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}

/********************************************** 依赖方法 **********************************************/
/**
 * 重置输入框，并显示“参数”输入框
 * @param {} mode	该值为 "add" 或 "update"，分别代表新增或更新
 */
function reset(mode) {
	//$j("#" + mode + "-name").val("");
	$j("#tree_name_zh_cn").val("");
	$j("#" + mode + "-type").val(2);	//类型默认为 分类类型
	$j("#" + mode + "-url").val("");
	$j("#" + mode + "-parameter").val("");
	$j("#" + mode + "-newWindow").prop("checked", false);

	if (mode === "update") {	//修改模块中 可以设置状态
		$j("#" + mode + "-status").val(1);
		$j(".navi-param:eq(0)").show();
	} else {
		$j(".navi-param:eq(1)").show();
	}
}

/**
* 验证导航是否重名
*/
function validateNavigationName(name,pId){
	var nodes = NAVI_TREE.getNodesByParam("pId", pId, null);	//所有已选中节点的子节点
	for (var i = 0; i < nodes.length; i++) {
		if (nodes[i].name == name) {
			return false;
		}
	}
	return true;
}

/**
* 判断祖先导航类型是否是分类类型(true:URL类型， false:分类类型)
*/
function isParentNavigationURLType(treeNode){
	if (treeNode.id == 0) {
		return false;
	}
	
	var parentNode = treeNode.getParentNode();
	
	if (parentNode.diy_type == 1) {
		return true;
	} else {
		if (parentNode.id != 0) {
			isParentNavigationURLType(parentNode);
		}
		return false;
	}
}

//处理显示页面"参数"
function createShowParamLabel(){
	var param="";
	//分类
	var nodes = CATE_TREE.getCheckedNodes(true);
	if (nodes.length > 0) {
		var nodeIds = "";
		for(var i=0;i<nodes.length;i++){
			nodeIds = nodeIds +  nodes[i].id +",";
			param += nodes[i].name +",";
		}
		nodeIds = nodeIds.substring(0,nodeIds.length-1);
		curParameterInput.data("category", nodeIds);
		curParameterInput.blur();
		curParameterInput.removeClass("ui-loxia-error");
	}else {
		curParameterInput.val("");
		curParameterInput.data("category", "");
	}

	//属性
	
	$j("div#propertiesDiv > div").each(function(){
		var $checkInfo=$j(this).find('input:checkbox:checked');
		if($checkInfo.size()>0){
			for(var j=0;j<$checkInfo.size();j++){
				param += $checkInfo.eq(j).next().html() +",";
			}
		}
	});
//	alert(param);
	if(param){
		curParameterInput.val(param.substring(0,param.length-1));
	}
}

//处理分类类型导航参数
function dealNavigationParam(){
	//存储选择的分类与属性
	var facetParameters = new Array();
	
	//分类
	var arryInfo=new Object();
	var keys= "";
	var nodes = CATE_TREE.getCheckedNodes(true);
	if (nodes.length > 0) {
		for(var i=0;i<nodes.length;i++){
			var parentTId=nodes[i].parentTId;
			var tId = nodes[i].tId;
			if(keys.indexOf(parentTId)<0){
				keys = keys  + parentTId + ","
				arryInfo[parentTId] = tId;
			}else{
				arryInfo[parentTId] = arryInfo[parentTId] + "," + tId;
			}
		}
		
		//取消末尾,
		keys = keys.substring(0,keys.length-1);
		var arrayKeys = keys.split(",");
	
		for(var i=0;i<arrayKeys.length;i++){
			var parentTId = arrayKeys[i];
			if(parentTId!=""){
				var facetParameter = new Object();
				facetParameter.facetType="CATEGORY";
				//分类类型的FacetParameter.name统一固定 category_tree
				facetParameter.name="category_tree";
				var tIds = arryInfo[parentTId];
				var arraytIds = tIds.split(",");
				
				var values = new Array();
				
				for(var j=0;j<arraytIds.length;j++){
					
					var tid = arraytIds[j];
					if(tid!= ""){
						var node = CATE_TREE.getNodeByTId(tid)
						if(node){
							//结点树ID的倒序连接
							var value = node.id+"";
						
							var parentNode = node.getParentNode();
							while(parentNode!=null && parentNode.id!='0'){
								value  = value + "-" + parentNode.id;
								parentNode = parentNode.getParentNode()
							}
							var info = value.split("-");
							for(var k=0;k<info.length;k++){
								if(k==0){
									value = info[info.length-1]
								}else{
									value  = value +"-"+ info[info.length-1-k]
								}
							}
							values.push(value);
						}
					}
				}
				facetParameter.values=values;
				facetParameters.push(facetParameter);
			}
		}
	}
	
	//属性
	var propertyIds ="";
	$j("div#propertiesDiv > div").each(function(){
		var $checkInfo=$j(this).find('input:checkbox:checked');
		if($checkInfo.size()>0){
			var facetParameter = new Object();
			facetParameter.name="dynamic_forsearch_"+$checkInfo.eq(0).attr("lang");
			facetParameter.facetType="PROPERTY";
			var values = new Array();
			for(var j=0;j<$checkInfo.size();j++){
				values.push($checkInfo.eq(j).val());
				propertyIds += $checkInfo.eq(j).val()+","
			}
			
			facetParameter.values=values;
			
			facetParameters.push(facetParameter);
		}
	});
	if(propertyIds){
		curParameterInput.data("property", propertyIds.substring(0,propertyIds.length-1));
	}else{
		curParameterInput.data("property", "");
	}
	
	//构建转递到服务器的数据
	if(facetParameters.length>0){
		var jsonstr="";
		for(var j=0;j<facetParameters.length;j++){
			jsonstr += facetParameters[j].name+",";
			jsonstr += facetParameters[j].facetType+",";
			
			var value="";
			for(var k=0;k<facetParameters[j].values.length;k++){
				if(k==facetParameters[j].values.length-1){
					value += facetParameters[j].values[k];
				}else{
					value += facetParameters[j].values[k]+"_";
				}
			}
			jsonstr += value;
			jsonstr += ";";
		}
		curParameterInput.data("json", jsonstr);
	}
	
	$j("#propertiesDiv input").attr("checked", false);  //全部取消勾选
}


