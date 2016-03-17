var categorySetting = {
	check : {
		enable : true,
		chkboxType : {
			"Y" : "",
			"N" : ""
		}
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
	},
};

function onClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj(treeId);
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj(treeId), nodes = zTree.getCheckedNodes(true);
	var sv = "";
	var dv = "";

	for (i in nodes) {
		sv += nodes[i].name + ",";
		dv += nodes[i].id + ",";
	}
	if (sv.length > 0) sv = sv.substring(0, sv.length - 1);
	if (dv.length > 0) dv = dv.substring(0, dv.length - 1);
	var iObj = zTree.setting.inputele;
	$j(iObj).val(sv);
	$j(iObj).attr("dv", dv);
	$j(iObj).parent().change();
}


$j(document).ready(function() {
	
});