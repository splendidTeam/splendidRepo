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

	}
};


var key,log,jsonArray, className = "dark",lastValue = "", nodeList = [], fontCss = {};

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

function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}

$j(document).ready(function(){
	/**
	 * 返回按钮
	 */
	$j(".return_button-new").click(function() {
		window.location.href="/property/nebulaPropertyList.htm?keepfilter=true";
	});
	
});

$j(window).load(function(){
	$j(window.top.document).find(".button-cancel").click(function(e){
		if($j(".nyyy").length==1){
			if($j(".list-all li.selected").prev("li").length>0){
				var beforeli=$j(".list-all li.selected").prev("li");
				$j(".no-sign").remove();
				beforeli.click();
			}
			else{
				var nextli=$j(".list-all li.selected").next("li");
				$j(".no-sign").remove();
				nextli.click();
			}
		}
		
        $j(".no-sign").removeClass("nyyy");
    });
});