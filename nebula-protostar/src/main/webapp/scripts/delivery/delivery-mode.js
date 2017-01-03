$j.extend(loxia.regional["zh-CN"], {
    ALERT_SELECT_CATEGORY_EMPTY: "请先选择一个地区",
    CONFIRM_DELETE: "确认删除",
    CONFIRM_DELETE_AGAIN: "再次确认删除",
    CONFIRM_DELETE_CATEGORY: "确定要删除选定的 {0} 地区的物流信息吗？",
    CONFIRM_DELETE_CATEGORY_ITEM: "确定要删除选定的 {0} 地区物流信息吗？",
    CONFIRM_DELETE_AREA: "确定要删除选定的 {0} 地区吗？",
    INFO_INSERT_SIBLINGCATEGORY: "插入同级分类 {0} 成功",
    INFO_DELETE_CATEGORY: "删除 {0} 地区物流信息成功",
    INFO_DELETE_AREA: "删除 {0} 地区信息成功",
    INFO_UPDATE_CATEGORY: "更新 {0} 地区物流信息成功",
    INFO_COMMONDELIVERY_TIME_EMPTY: "普通配送时间不能为空",
    INFO_FIRSTDELIVERY_TIME_EMPTY: "当日达配送时间不能为空",
    INFO_SECONDDELIVERY_TIME_EMPTY: "次日达配送时间不能为空",
    INFO_LOGISTICS_EMPTY:"物流编号和公司不能为空",
    INFO_COMMONDELIVERY_TIME_UNEMPTY:"普通配送时间不必填写",
    INFO_SECONDDELIVERY_TIME_UNEMPTY:"次日达配送时间不必填写",
    INFO_FIRSTDELIVERY_TIME_UNEMPTY:"当日达配送时间不必填写",
    INFO_CATEGORY_CODE_EMPTY: "地区编码不能为空",
    INFO_CATEGORY_NAME_EMPTY: "地区名称不能为空",
    INFO_ADD_LEAFCATEGORY: "添加子地区 {0} 成功"
    
});
var s=base+"/logistics/areaDeliverMode/findDeliveryAreaModeByAreaId.json";
var area=base+"/logistics/areaDeliverMode/findDeliveryAreaById.json";
function deleteCategory(c, b,z) {
	var  a = base + "/logistics/areaDeliverMode/deleteDeliveryAreaModeById.json";
    nps.asyncXhrPost(a, {
        id: z
    },
    {
        successHandler: function(f, h) {
            var e = c.getNextNode();
            var g = c.getPreNode();
            var d = c.getParentNode();
            if (null != e) {
                $j("#" + e.tId + "_span").click()
            } else {
                if (null != g) {
                    $j("#" + g.tId + "_span").click()
                } else {
                    $j("#" + d.tId + "_span").click()
                }
            }
            nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_DELETE_CATEGORY", [c.name]))
        }
    })
}

function deleteArea(c, b) {
	var  a = base + "/logistics/areaDeliver/deleteDeliveryAreaById.json";
    nps.asyncXhrPost(a, {
        id: c.id
    },
    {
        successHandler: function(f, h) {
            var e = c.getNextNode();
            var g = c.getPreNode();
            var d = c.getParentNode();
            b.removeNode(c, true);
            if (null != e) {
                $j("#" + e.tId + "_span").click()
            } else {
                if (null != g) {
                    $j("#" + g.tId + "_span").click()
                } else {
                    $j("#" + d.tId + "_span").click()
                }
            }
            nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_DELETE_AREA", [c.area]))
        }
    })
}

function fnPromptDelArea(d, a,z) {
    var b = {
        areaId: d.id
    };
    var c = nps.syncXhrPost(area, b);
    if (c != "" && c != null) {
        nps.confirm(nps.i18n("CONFIRM_DELETE_AGAIN"), nps.i18n("CONFIRM_DELETE_AREA", [d.name]), function() {
        	deleteArea(d, a,z)
        });
    } else {
    	nps.info(nps.i18n("INFO_TITLE_DATA"), "选择的地区已经不存在了");
    }
}

function fnPromptDelCategory(d, a,z) {
    var b = {
        areaId: d.id
    };
    var c = nps.syncXhrPost(s, b);
    if (c != "" && c != null) {
        nps.confirm(nps.i18n("CONFIRM_DELETE_AGAIN"), nps.i18n("CONFIRM_DELETE_CATEGORY_ITEM", [d.name]), function() {
            deleteCategory(d, a,z)
        });
    } else {
    	nps.info(nps.i18n("INFO_TITLE_DATA"), "选择的物流信息已经不存在了");
    }
}
//空字符
function isNull(val) {

	if (val == null || val.trim() == '') {
		return true;
	}
	return false;
}
//获取日期时间
function formatDateTime(date) {  
    if (!date) return;  
    var format = "yyyy-MM-dd HH:mm:ss";  
    switch(typeof date) {  
        case "string":  
            date = new Date(date.replace(/-/, "/"));  
            break;  
        case "number":  
            date = new Date(date);  
            break;  
    }   
    if (!date instanceof Date) return;  
    var dict = {  
        "yyyy": date.getFullYear(),  
        "M": date.getMonth() + 1,  
        "d": date.getDate(),  
        "H": date.getHours(),  
        "m": date.getMinutes(),  
        "s": date.getSeconds(),  
        "MM": ("" + (date.getMonth() + 101)).substr(1),  
        "dd": ("" + (date.getDate() + 100)).substr(1),  
        "HH": ("" + (date.getHours() + 100)).substr(1),  
        "mm": ("" + (date.getMinutes() + 100)).substr(1),  
        "ss": ("" + (date.getSeconds() + 100)).substr(1)  
    };      
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {  
        return dict[arguments[0]];  
    });                  
}

var treeCategory = {
    ztreeElementId: "categoryTree",
    ztreeSearchElementSelector: "#key",
    highlightNodeList: [],
    highlightNodes: function(c, b) {
        for (var d = 0, a = this.highlightNodeList.length; d < a; d++) {
            var e = this.highlightNodeList[d];
            e.highlight = b;
            c.updateNode(e)
        }
    }
};
var setting = {
    edit: {
        enable: true,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    view: {
        showLine: true,
        showIcon: false,
        nameIsHTML: true,
        selectedMulti: false,
        fontCss: getFontCss
    },
    data: {
        keep: {
            parent: false,
            leaf: false
        },
        key: {
            title: "name"
        },
        simpleData: {
            enable: true
        }
    },
    callback: {
        onClick: function(d, g, f, c) {
            nps.error();
            var b = f.code;
            var a = ("ROOT" == b);
            $j("#insertSibling,#save_node,#remove_element").attr("disabled", a ? "disabled" : false);
            if (a) {
                $j("#tree_code").val("");
                $j("#tree_name_zh_cn").val("")
            } else {
                $j("#tree_code").val(f.code);
                $j("#tree_name_zh_cn").val(f.name);
                var h = f.id;
                if (h == 0) {
                    return
                }
                var e = nps.syncXhrPost(base + "/logistics/areaDeliverMode/findDeliveryAreaModeByAreaId.json", {
                    areaId: h
                });
                if (e == null) {
                    nps.info(nps.i18n("INFO_TITLE_DATA"), "选择数据已经不存在了");
                    return
                }
                if (i18nOnOff) {
                	$j("#modeId").val(e.id);
                	$j("#areaId").val(f.id);
                    $j("#logisticsCode").val(e.logisticsCode);
                    $j("#logisticsCompany").val(e.logisticsCompany);
                    $j("#commonDelivery").val(e.commonDelivery=='Y'?'true':'false');
                    $j("#commomStartTime").val(e.commonDeliveryStartTime);
                    $j("#commomEndTime").val(e.commonDeliveryEndTime);
                    $j("#firstDayDelivery").val(e.firstDayDelivery=='Y'?'true':'false');
                    $j("#firstStartTime").val(e.firstDeliveryStartTime);
                    $j("#firstEndTime").val(e.firstDeliveryEndTime);
                    $j("#secondDayDelivery").val(e.secondDayDelivery=='Y'?'true':'false');
                    $j("#secondStartTime").val(e.secondDeliveryStartTime);
                    $j("#secondEndTime").val(e.secondDeliveryEndTime);
                    $j("#thirdDayDelivery").val(e.thirdDayDelivery=='Y'?'true':'false');
                    $j("#thirdDeliveryStartTime").val(e.thirdDeliveryStartTime);
                    $j("#thirdDeliveryEndTime").val(e.thirdDeliveryEndTime);
                    $j("#remark").val(e.remark);
                    $j("#createTime").val(formatDateTime(e.createTime));
                    $j("#modifyTime").val(formatDateTime(e.modifyTime));
                    $j("#version").val(formatDateTime(e.version));
                    $j("#supportcod").val(e.support_COD=='Y'?'true':'false');
                } 
            }
        },

    }
};
var key, lastValue = "",
    nodeList = [],
    fontCss = {};

function getFontCss(b, a) {
    return (!!a.highlight) ? {
        color: "#333",
        "background-color": "yellow"
    } : {
        color: "#333",
        "font-weight": "normal",
        "background-color": ""
    }
}

function focusKey(a) {
    if (key.hasClass("empty")) {
        key.removeClass("empty")
    }
}

function blurKey(a) {
    if (key.get(0).value === "") {
        key.addClass("empty")
    }
}

function searchNode(c) {
    var a = $j.fn.zTree.getZTreeObj("categoryTree");
    var b = $j.trim(key.get(0).value);
    if (b == "") {
        $j("#search_result").html("");
        updateCategoryNodes(false)
    }
    if (key.hasClass("empty")) {
        b = ""
    }
    if (lastValue === b) {
        return
    }
    lastValue = b;
    if (b === "") {
        return
    }
    updateCategoryNodes(false);
    nodeList = a.getNodesByParamFuzzy("name", b);
    $j("#search_result").html(nps.i18n("INFO_CATEGORY_SEARCH_RESULT", [nodeList.length]));
    if (nodeList.length > 0) {
        $j.each(nodeList, function(d, e) {
            a.expandNode(e.getParentNode(), true, true, true)
        })
    }
    updateCategoryNodes(true);
    $j("#key").focus()
}

function updateCategoryNodes(b) {
    var c = $j.fn.zTree.getZTreeObj("categoryTree");
    for (var d = 0, a = nodeList.length; d < a; d++) {
        nodeList[d].highlight = b;
        c.updateNode(nodeList[d])
    }
}

$j(function() {
    $j.fn.zTree.init($j("#" + treeCategory.ztreeElementId), setting, zNodes);
    $j("#" + treeCategory.ztreeElementId + "_1_span").click();
    key = $j("#key");
    key.bind("focus", focusKey).bind("blur", blurKey).bind("propertychange", searchNode).bind("input", searchNode);
    $j("#delete_node").click(function() {
        nps.error();
        var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
        var selectedNodes = zTree.getSelectedNodes();
        if (selectedNodes.length == 0) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
            return
        }
        var selectTreeNode = selectedNodes[0];
        var id= selectTreeNode.id;
        nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_CATEGORY", [selectTreeNode.name]),
        function() {
            setTimeout(function() {
            	fnPromptDelCategory(selectTreeNode, zTree,id);
            },
            500)
        })
    });
    
    $j("#remove_area").click(function() {
        nps.error();
        var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
        var selectedNodes = zTree.getSelectedNodes();
        if (selectedNodes.length == 0) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
            return
        }
        var selectTreeNode = selectedNodes[0];
        nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_AREA", [selectTreeNode.name]),
        function() {
            setTimeout(function() {
            	fnPromptDelArea(selectTreeNode, zTree)
            },
            500)
        })
    });
    
    
   $j("#save_area").click(function(){
       nps.error();
       var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
       var selectedNodes = zTree.getSelectedNodes();
       if (selectedNodes.length == 0) {
           nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
           $j("#tree_code").focus();
           return
       }
       var selectTreeNode = selectedNodes[0];
       var tree_code = $j.trim($j("#tree_code").val());
       var id= selectTreeNode.id;
       var tree_name_zh_cn = $j("#tree_name_zh_cn").val();
       if (null == tree_code || "" == tree_code) {
           nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_CODE_EMPTY"));
           $j("#tree_code").focus();
           return
       }
       var url = base + "/logistics/areaDeliverMode/updateDelivery.json";
       var data = {};
       var defualt = "";
       if (i18nOnOff) {
    	   var validate = false;
           $j(".cate-update .mutl-lang").each(function(i, dom) {
               var me = $j(this);
               var val = me.val();
               if (null == val || "" == val) {
                   validate = true
               }
           });
           var multlangs = '{"id":"' + id + '"';
           multlangs += ',"area":"' + tree_name_zh_cn + '"';
           multlangs += ',"code":"' + tree_code + '"';
           multlangs += "}";
           data = eval("(" + multlangs + ")")
       } else {
           if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
               nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
               $j("#tree_name_zh_cn").focus();
               return
           }
           data = {
           		 "id":id,
           		 "area":tree_name_zh_cn,
                    "code":tree_code
           }
       }
       nps.asyncXhrPost(url, data, {
           successHandler: function(data, textStatus) {
               selectTreeNode.name = tree_name_zh_cn;
               selectTreeNode.code = tree_code;
               zTree.updateNode(selectTreeNode);
               if (!i18nOnOff) {
                   defualt = tree_name_zh_cn
               }
               nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_UPDATE_CATEGORY", [defualt]))
           }
       })
   });
    
    $j("#save_node").click(function() {
        nps.error();
        var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
        var selectedNodes = zTree.getSelectedNodes();
        if (selectedNodes.length == 0) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
            return
        }
        var id= $j("#modeId").val();
        var areaId= $j("#areaId").val();
        var logisticsCode= $j("#logisticsCode").val();
        var logisticsCompany=$j("#logisticsCompany").val();
        var commonDelivery=$j("#commonDelivery").val()=='true'?'Y':'N';
        var commomStartTime= $j("#commomStartTime").val();
        var commomEndTime=$j("#commomEndTime").val();
        var firstDayDelivery= $j("#firstDayDelivery").val()=='true'?'Y':'N';
        var firstStartTime=  $j("#firstStartTime").val();
        var firstEndTime= $j("#firstEndTime").val();
        var secondDayDelivery=$j("#secondDayDelivery").val()=='true'?'Y':'N';
        var secondStartTime=$j("#secondStartTime").val();
        var secondEndTime=$j("#secondEndTime").val();
        var thirdDayDelivery=$j("#thirdDayDelivery").val()=='true'?'Y':'N';
        var thirdDeliveryStartTime=$j("#thirdDeliveryStartTime").val();
        var thirdDeliveryEndTime=$j("#thirdDeliveryEndTime").val();
        var supportcod= $j("#supportcod").val()=='true'?'Y':'N';
        var remark= $j("#remark").val();
        //如果物流选择为否，则不需要填写相应的时间
        if((commonDelivery!='N')&&(isNull(commomStartTime)||isNull(commomEndTime))){
       	 nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_COMMONDELIVERY_TIME_UNEMPTY"));
            return
       }
       if((firstDayDelivery!='N')&&(isNull(firstStartTime)||isNull(firstEndTime))){
    	   nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_FIRSTDELIVERY_TIME_UNEMPTY"));
           return
       }
       if((secondDayDelivery!='N')&&(isNull(secondStartTime)||isNull(secondEndTime))){
    	   nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_SECONDDELIVERY_TIME_UNEMPTY"));
           return
       }
       if((thirdDayDelivery!='N')&&(isNull(thirdDeliveryStartTime)||isNull(thirdDeliveryEndTime))){
    	   nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_FIRSTDELIVERY_TIME_EMPTY"));
           return
       }
       if((logisticsCode==""||isNull(logisticsCode)||isNull(logisticsCompany))){
    	   nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_LOGISTICS_EMPTY"));
           return
       }
       
        var url = base + "/logistics/areaDeliverMode/updateDeliveryAreaModeByAreaId.json";
        var data = {};
        if (i18nOnOff) {
        	var validate = false;
            $j(".cate-add .mutl-lang").each(function(i, dom) {
                var me = $j(this);
                var val = me.val();
                if (null == val || "" == val) {
                    validate = true
                }
            });
            if (validate) {
                nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
                return
            }
            var multlangs = '{"id":"' + id + '"';
            multlangs += ',"areaId":"' + areaId + '"';
            multlangs += ',"logisticsCode":"' + logisticsCode + '"';
            multlangs += ',"logisticsCompany":"' + logisticsCompany + '"';
            multlangs += ',"commonDelivery":"' + commonDelivery + '"';
            multlangs += ',"commonDeliveryStartTime":"' + commomStartTime + '"';
            multlangs += ',"commonDeliveryEndTime":"' + commomEndTime + '"';
            multlangs += ',"firstDayDelivery":"' + firstDayDelivery + '"';
            multlangs += ',"firstDeliveryStartTime":"' + firstStartTime + '"';
            multlangs += ',"firstDeliveryEndTime":"' + firstEndTime + '"';
            multlangs += ',"secondDayDelivery":"' + secondDayDelivery + '"';
            multlangs += ',"secondDeliveryStartTime":"' + secondStartTime + '"';
            multlangs += ',"secondDeliveryEndTime":"' + secondEndTime + '"';
            multlangs += ',"thirdDayDelivery":"' + thirdDayDelivery + '"';
            multlangs += ',"thirdDeliveryStartTime":"' + thirdDeliveryStartTime + '"';
            multlangs += ',"thirdDeliveryEndTime":"' + thirdDeliveryEndTime + '"';
            multlangs += ',"remark":"' + remark + '"';
            multlangs +=',"support_COD":"' + supportcod + '"';
            multlangs += "}";
            data = eval("(" + multlangs + ")")
        } else {
            if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
                nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
                $j("#tree_name_zh_cn").focus();
                return
            }
            data = {
            		 "id":id,
            		 "areaId":areaId,
                     "logisticsCode":logisticsCode,
                     "logisticsCompany":logisticsCompany ,
                      "commonDelivery":commonDelivery ,
                     "commonDeliveryStartTime":commomStartTime ,
                     "commonDeliveryEndTime":commomEndTime,
                      "firstDayDelivery":firstDayDelivery ,
                      "firstStartTime":firstStartTime ,
                      "firstEndTime":firstEndTime ,
                      "secondDayDelivery": secondDayDelivery ,
                      "secondDeliveryStartTime": secondStartTime ,
                      "secondDeliveryEndTime": secondEndTime,
                      "thirdDayDelivery": thirdDayDelivery ,
                      "thirdDeliveryStartTime": thirdDeliveryStartTime ,
                      "thirdDeliveryEndTime": thirdDeliveryEndTime,
                      "support_COD": supportcod,
                      "remark":  remark 
            }
        }
        nps.asyncXhrPost(url, data, {
            successHandler: function(data, textStatus) {
            	 nps.info(nps.i18n("INFO_TITLE_DATA"), "保存成功");
            }
        });
        
        var e = nps.syncXhrPost(base + "/logistics/areaDeliverMode/findDeliveryAreaModeByAreaId.json", {
            areaId: areaId
        });
    	$j("#modeId").val(e.id);
    });
    
    
    $j("#addLeaf").click(function() {
        nps.error();
        var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
        var selectedNodes = zTree.getSelectedNodes();
        if (selectedNodes.length == 0) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
            return
        }
        var selectTreeNode = selectedNodes[0];
        var code = $j.trim($j("#add_code").val());
        var selectTreeNodeId = selectTreeNode.id;
        var parentNode = selectTreeNode.getParentNode();
        var tree_name_zh_cn = $j.trim($j("#add_name_zh_cn").val());
        if (null == code || "" == code) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_CODE_EMPTY"));
            $j("#add_code").focus();
            return
        }
        var data = {};
        var defualt = "";
        if (i18nOnOff) {
            var validate = false;
            $j(".area-insert .mutl-lang ").each(function(i, dom) {
                var me = $j(this);
                var val = me.val();
                if (null == val || "" == val) {
                    validate = true
                }
            });
            if (validate) {
                nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
                return
            }
            var multlangs = '{"id":"' + selectTreeNodeId + '"';
            multlangs += ',"area":"' + tree_name_zh_cn + '"';
            multlangs += ',"code":"' + code + '"';
            multlangs += ',"parentId":"' + parentNode.id + '"';
            multlangs += "}";
            data = eval("(" + multlangs + ")")
        } else {
            if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
                nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
                $j("#add_name_zh_cn").focus();
                return
            }
            data = {
            		"id":selectTreeNodeId,
           		 	"area":tree_name_zh_cn,
                    "code":code,
                    "parentId":parentNode.id
            }
        }
        var url = base + "/logistics/areaDeliverMode/insertLeafDelivery.json";
        nps.asyncXhrPost(url, data, {
            successHandler: function(data, textStatus) {
                var category = data;
                if (i18nOnOff) {
                    zTree.addNodes(selectTreeNode, {
                        id: category.id,
                        pId: selectTreeNodeId,
                        code: code,
                        name: category.area,
                        sortNo: category.sortNo
                    })
                } else {
                    defualt = $j.trim($j("#add_name_zh_cn").val());
                    zTree.addNodes(selectTreeNode, {
                        id: category.id,
                        pId: selectTreeNodeId,
                        code: code,
                        name: category.area,
                        sortNo: category.sortNo
                    })
                }
                nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_ADD_LEAFCATEGORY", [category.area]))
            }
        })
    });
    $j("#insertSibling").click(function() {
        nps.error();
        var zTree = $j.fn.zTree.getZTreeObj(treeCategory.ztreeElementId);
        var selectedNodes = zTree.getSelectedNodes();
        if (selectedNodes.length == 0) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("ALERT_SELECT_CATEGORY_EMPTY"));
            return
        }
        var selectTreeNode = selectedNodes[0];
        var code = $j.trim($j("#add_code").val());
        var tree_name_zh_cn = $j.trim($j("#add_name_zh_cn").val());
        if ("" == code || null == code) {
            nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_CODE_EMPTY"));
            $j("#tree_code").focus();
            return
        }
        var selectTreeNodeId = selectTreeNode.id;
        var parentNode = selectTreeNode.getParentNode();
        var data = {};
        if (i18nOnOff) {
        	var validate = false;
            $j(".area-insert .mutl-lang").each(function(i, dom) {
                var me = $j(this);
                var val = me.val();
                if (null == val || "" == val) {
                    validate = true
                }
            });
            if (validate) {
                nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
                return
            }
            var multlangs = '{"id":"' + selectTreeNodeId + '"';
            multlangs += ',"area":"' + tree_name_zh_cn + '"';
            multlangs += ',"code":"' + code + '"';
            multlangs += ',"parentId":"' + parentNode.id + '"';
            multlangs += "}";
            data = eval("(" + multlangs + ")")
        } else {
            if (null == tree_name_zh_cn || "" == tree_name_zh_cn) {
                nps.error(nps.i18n("ERROR_INFO"), nps.i18n("INFO_CATEGORY_NAME_EMPTY"));
                $j("#tree_name_zh_cn").focus();
                return
            }
            data = {
            		 "id":selectTreeNodeId,
            		 "area":tree_name_zh_cn,
                     "code":code,
                     "parentId":parentNode.id
            }
        }
        var url = base + "/logistics/areaDeliverMode/insertSiblingDelivery.json";
        nps.asyncXhrPost(url, data, {
            successHandler: function(data, textStatus) {
                var category = data;
                if(null==data){
                	nps.info(nps.i18n("INFO_TITLE_DATA"), "插入失败！地区已存在，请检查插入的地区编号！");
                	return;
                }
                var newNodes = zTree.addNodes(parentNode, {
                    id: category.id,
                    parentId: parentNode.id,
                    code: code,
                    name: category.area,
                    sortNo: category.sortNo
                });
                zTree.moveNode(selectTreeNode, newNodes[0], "prev");
                nps.info(nps.i18n("INFO_TITLE"), nps.i18n("INFO_INSERT_SIBLINGCATEGORY", [category.area]))
            }
        })
    })
    
});