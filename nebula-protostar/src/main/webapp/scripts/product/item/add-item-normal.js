////*******************************默认分类
//function defaultOnClick1(e, treeId, treeNode) {
//	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree");
//	zTree.checkNode(treeNode, !treeNode.checked, null, true);
//	
//	hideMenu("defaultMenuContent");
//	return false;
//}
//
//function defaultOnCheck1(e, treeId, treeNode) {
//	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
//	nodes = zTree.getCheckedNodes(true),
//	v = "",
//	categoryHtml = "";
//	var defaultHtml = $j("#chooseDefaultCategory").html();
//	var id = $j('#chooseDefaultCategory').children('div').attr('class');
//	$j("#chooseDefaultCategory").html("");
//	for (var i=0, l=nodes.length; i<l; i++) {
//		v += nodes[i].name + ",";
//		var inode =$j("."+nodes[i].id+"");
//		if(inode.length==0){
//			categoryHtml = "<div class="+nodes[i].id +">"+nodes[i].name + 
//			"<input type='hidden' name='defaultCategoryId'  value='"+nodes[i].id+"' />"+
//			"<a href='javascript:void(0);'id="+nodes[i].id+" style='float:right;margin-right: 760px;text-decoration: underline;color:#F8A721' onclick='delDefaultCategroy(this.id)'>"+nps.i18n("DELETE_THIS_CATEGORY")+"</a><br/></div>";
//			$j("#chooseDefaultCategory").append(categoryHtml);
//		}else{
//			$j("#chooseDefaultCategory").html(defaultHtml);
//			zTree.checkNode(treeNode, !treeNode.checked, null, false);
//			
//			var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
//			nodes = zTree.getCheckedNodes(false);
//			for (var i=0, l=nodes.length; i<l; i++) {
//				if(id==nodes[i].id){
//					nodes[i].checked= true;
//				}
//			}
//			zTree.refresh();
//			
//			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_REPEATEDLY_RELEVANCE_CATEGORY"));
//			return;
//		}
//		
//	}
//}
//
////删除默认分类
//function delDefaultCategroy(id){
//	$j("."+id+"").remove();
//	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
//	nodes = zTree.getCheckedNodes(true);
//	for (var i=0, l=nodes.length; i<l; i++) {
//		if(id==nodes[i].id){
//			nodes[i].checked= false;
//		}
//	}
//	zTree.refresh();
//}
