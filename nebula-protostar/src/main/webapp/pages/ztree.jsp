<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>


<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<SCRIPT type="text/javascript">
	var $j = jQuery.noConflict();
		<!--
		var setting = {
			
			check: {
				enable: false
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
				onClick: onClick,
				beforeRemove: beforeRemove,
				onRemove: onRemove,
				onNodeCreated: onNodeCreated,
				asyncSuccess: zTreeOnAsyncSuccess,//异步加载成功的fun 
				asyncError: zTreeOnAsyncError, //加载错误的fun 
			},
			async: { 
				enable: true, 
				url:"/ztree/queryZNode.json", 
				autoParam:["id", "name"], 
				otherParam:{"otherParam":"zTreeAsyncTest"}, 
				// dataType: "text",//默认text 
				 type:"get",//默认post 
				dataFilter: filter //异步返回后经过Filter 
				} 
				
		};
		
		function filter(treeId, parentNode, childNodes) { 
			if (!childNodes) return null; 
			for (var i=0, l=childNodes.length; i<l; i++) { 
			childNodes[i].name = childNodes[i].name.replace('',''); 
			} 
			return childNodes; 
		} 
		
		function zTreeOnAsyncError(event, treeId, treeNode){ 
			alert("异步加载失败!"); 
		} 
		
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){ 

		} 

		//var zNodes =[{ id:0,pid:-1, name:"ROOT",state:"0",sort:1, open:true,root:"true"}];
		 var zNodes =[
			{ id:0, name:"ROOT",state:"0",sort:1, open:true,root:"true",isParent:true},
			{ id:1, pId:0, name:"鞋",state:"0",sort:1, open:true,root:"true",isParent:true},
		
			{ id:2, pId:0, name:"服装",state:"0",sort:2, open:true,root:"true",isParent:true},
			
			{ id:3, pId:0, name:"配件",state:"0",sort:3, open:true,root:"true",isParent:true}
			
		]; 
		var log, className = "dark";
		
		
		function beforeRemove(treeId, treeNode,e) {
			className = (className === "dark" ? "":"dark");	
			if(confirm("确认删除 节点 -- " + treeNode.name + " 吗？"))
   		{
    	removeNode(treeNode,null);
   		}
		}
		
		
		
		
		function onRemove(e, treeId, treeNode) {
		}
		function onClick(event, treeId, treeNode)  {
			 //alert(treeNode.id + ", " + treeNode.name);
				$j("#tree_fid").val(treeNode.name);
				$j("#tree_state").val(treeNode.state);
				$j("#tree_sort").val(treeNode.sort);
				$j("#tree_fid").attr("treeId",treeId);
				$j("#tree_fid").data("treeNode",treeNode);
				$j("#tree_fid").data("event",event);
				if(treeNode.id==0){
					$j(".percent70-content-right").css("display","none");
				}else{
					$j(".percent70-content-right").css("display","block");
				}
		}
		function onNodeCreated(e, treeId, treeNode) {
				var zTree = $j.fn.zTree.getZTreeObj("tree");
				var node = zTree.getNodeByParam("id", treeId, null);
				zTree.moveNode(treeNode,  node  , "prev");
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
		function getFontCss(treeId, treeNode) {
			return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
		}
	
		var key;
		
		var newCount = 1;
		function addRoot(e) {
			var zTree = $j.fn.zTree.getZTreeObj("tree"),
			isParent = e.data.isParent,
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			if (treeNode&& treeNode.getParentNode()!=null) {
				treeNode=treeNode.getParentNode();
				var new_id = treeNode.id *10  +  treeNode.children.length+1;
				treeNode = zTree.addNodes(treeNode, {id:new_id, pId:treeNode.id, isParent:false, name:$j("#add_name").val()});
				onNodeCreated(e,new_id,nodes[0]);
			} else {
				treeNode = zTree.addNodes(null, {id:(100 + newCount), pId:0, isParent:false, name:$j("#add_name").val()});
				
			}
			if (treeNode) {
				zTree.editName(treeNode[0]);
			} else {
				alert("叶子节点被锁定，无法增加子节点");
			}
		};

		//添加子节点
		function add(e) {
			var zTree = $j.fn.zTree.getZTreeObj("tree"),
			isParent = e.data.isParent,
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			if (treeNode) {
				treeNode = zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, isParent:isParent, name:$j("#add_name").val()});
				
				
			} else {
				treeNode = zTree.addNodes(null, {id:(100 + newCount), pId:0, isParent:isParent, name:$j("#add_name").val()});
			}
			if (treeNode) {
				zTree.editName(treeNode[0]);
			} else {
				alert("叶子节点被锁定，无法增加子节点");
			}
		};
		$j(document).ready(function(){
			loxia.init({
                debug : true,
                region : 'zh-CN'
            });
            nps.init();
			$j.fn.zTree.init($j("#tree"), setting, zNodes);
			key = $j("#key");
			key.bind("focus", focusKey)
			.bind("blur", blurKey)
			.bind("propertychange", searchNode)
			.bind("input", searchNode);
			$j("#addParent").bind("click", {isParent:true}, addRoot);
			$j("#addLeaf").bind("click", {isParent:false}, add);
			
			//修改节点名称
			$j("#save_father_Name").click(function(){
				var zTree = $j.fn.zTree.getZTreeObj("tree");
				var nodes = zTree.getSelectedNodes();
				var sortNode ;
				if (nodes.length>0) {
					nodes[0].name = $j("#tree_fid").val();
					nodes[0].state = $j("#tree_state").val();
					if(nodes[0].sort == $j("#tree_sort").val()){
						sortNode = 0;
					}else{
						nodes[0].sort = $j("#tree_sort").val()
						sortNode = 1;
					}
					zTree.updateNode(nodes[0]);
					if(sortNode==1){
						var nlist ;
						if(nodes[0].getParentNode()==null){
							nlist = zTree.getNodesByParam("root", "true", null);
						}else{
							nlist = nodes[0].getParentNode().children;
						}
						for( var i=0; i<nlist.length; i++) {
							if(parseInt(nodes[0].sort)<parseInt(nlist[i].sort)){
								zTree.moveNode(nlist[i],nodes[0] , "prev");
								break;
							}
							if(i==nlist.length-1){
								zTree.moveNode(nlist[i],nodes[0], "next");
							}
						}	
					}
				}
				//$j(".curSelectedNode span:eq(1)").html($j("#tree_fid").val());
			});	

			//上移
			$j("#up_element").click(function(){
				var zTree = $j.fn.zTree.getZTreeObj("tree");
				var nodes = zTree.getSelectedNodes();

				if (nodes.length > 0) {
					var node = nodes[0].getPreNode();
				}
				if(node!=null){
					zTree.moveNode(node,nodes[0] , "prev");
					var up_value = nodes[0].sort
					var down_value  = node.sort
					node.sort = up_value;
					nodes[0].sort = down_value;
					zTree.updateNode(nodes[0]);
					zTree.updateNode(node);
					$j("#tree_sort").val(down_value);
				} 
			});	

			//下移
			$j("#down_element").click(function(){
				var zTree = $j.fn.zTree.getZTreeObj("tree");
				var nodes = zTree.getSelectedNodes();

				if (nodes.length > 0) {
					var node = nodes[0].getNextNode();
				}
				if(node!=null){
					zTree.moveNode(node,nodes[0] , "next");
					var up_value = nodes[0].sort
					var down_value  = node.sort
					node.sort = up_value;
					nodes[0].sort = down_value;
					zTree.updateNode(nodes[0]);
					zTree.updateNode(node);
					$j("#tree_sort").val(down_value);
				} 
			});				
			
			//删除节点
			$j("#remove_element").click(function(){
				 	//beforeRemove($j("#tree_fid").attr("treeId"),$j("#tree_fid").data("treeNode"),$j("#tree_fid").data("event"));
				 	var treeObj = $j.fn.zTree.getZTreeObj("tree");
					var nodes = treeObj.getSelectedNodes();
					if (nodes && nodes.length>0  && nodes[0].id!=0 && !nodes[0].isParent) {
						treeObj.removeNode(nodes[0]);
					}
			});	
		});
		

		//-->
	</SCRIPT>
</HEAD>

<BODY>
	<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="../images/wmi/blacks/32x32/wrench_plus_2.png">行业管理
		</div>
		<div class="ui-block ui-block-fleft w240">
			<div class="ui-block-content ui-block-content-lb">
				<div class="tree-control">
					<input type="text" id="key" loxiatype="input"
						placeholder="输入关键字快速定位" />
				</div>
				<ul id="tree" class="ztree"></ul>

			</div>
		</div>
		<div class="ui-block ml240" style="padding-left: 10px;">
			<div class="ui-block-title1">您当前选中的行业</div>
			<div class="ui-block-content border-grey"
				style="margin-bottom: 10px;">
				<div class="ui-block-line">
					<label>名称</label>
					<div>
						<input type="text" id="tree_fid" loxiatype="input"
							mandatory="true" placeholder="商品名称" />
					</div>
				</div>
				<div class="ui-block-line">
					<label>状态</label>
					<div>
						<span><select id="tree_state"><option value="1">有效</option>
								<option value="0">无效</option></select>
					</div>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button persist"
						id="save_father_Name"><span>保存</span></a> <a
						href="javascript:void(0);" class="func-button delete"
						id="remove_element"><span>删除</span></a>
				</div>
			</div>
			<div class="ui-block-title1">添加新行业</div>
			<div class="ui-block-content border-grey">
				<div class="ui-block-line">
					<label>名称</label>
					<div>
						<input type="text" id="add_name" loxiatype="input"
							mandatory="true" placeholder="商品名称" />
					</div>
				</div>
				
				<div class="button-line1">
					<a id="addLeaf" href="javascript:void(0);" class="func-button insertLeaf"><span>添加子行业</span></a>
				</div>
			</div>
		</div>
	</div>
</BODY>

</html>