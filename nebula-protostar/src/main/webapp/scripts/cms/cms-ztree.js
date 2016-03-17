//--CONSTANTS-------------------------------------------------------
//CMS模块用于标识的CSS class
var CSS_CLASS = {
	//CMS Template 中的区域编辑方式
	MODE_IMGARTICLE: "cms-imgarticle-edit",
	//图文模式中的列表元素
	MODE_LIST: "cms-area-list-element",	
		
	//可编辑项
	EDIT_HREF: "cms-area-href",
	EDIT_IMG: "cms-area-img",
	EDIT_TITLE: "cms-area-title",
	EDIT_DESC: "cms-area-desc",
	EDIT_COORDS: "cms-area-coords",
		
	//--编辑项对应的form控件 START---------------------------
	INPUT_TITLE: "title",
	INPUT_HREF: "href",
	INPUT_IMG_URL: "img-url",
	INPUT_IMG_ALT: "img-alt",
	INPUT_COORDS: "coords",
	
	TEXTAREA_DESC: "desc",
	
	DIV_IMG: "img",
	//--编辑项对应的form控件 END---------------------------
		
	//用于设置编辑项的按钮
	BUTTON_HREF: "href",
	BUTTON_REMOVE_HREF: "rehref",
	BUTTON_COORDS: "coords",
	BUTTON_REMOVE_COORDS: "recoords",
	BUTTON_IMG: "img",
	BUTTON_REMOVE_IMG: "reimg",
	BUTTON_TITLE: "title",
	BUTTON_REMOVE_TITLE: "retitle",
	BUTTON_DESC: "desc",
	BUTTON_REMOVE_DESC: "redesc"
};

//--JS Object-----------------------------------------------------------------------------------
/**
 * 与dom树操作相关的方法
 */
function DomTreeManager(){
	
	var zNodeId = 0; //用于生成zNode Id生成
	
	/**
	 * 清除CSS class所标识的设置项的内容
	 * 
	 * @param jDom 当前选中的dom
	 * @returns
	 */
	function cleanZnodeValues(jDom){
		if(jDom.hasClass(CSS_CLASS.EDIT_TITLE)){
			jDom.html("");
		}
		if(jDom.hasClass(CSS_CLASS.EDIT_HREF)){
			jDom.attr("href", "#");
		}
		if(jDom.hasClass(CSS_CLASS.EDIT_IMG)){
			jDom.attr("src", "#");
		}
		if(jDom.hasClass(CSS_CLASS.EDIT_DESC)){
			jDom.html("");
		}
		if(jDom.hasClass(CSS_CLASS.EDIT_COORDS)){
			jDom.attr("coords", "0,0,0,0");
		}
		
		jDom.children().each(function(index, element){
			cleanZnodeValues($j(element));
		});
		
	}
	this.cleanZnodeValues = cleanZnodeValues;

	/**
	 * 遍历dom对象并格式化为一个JSON数组，供zTree渲染使用
	 * 
	 * 形如：[{"id":1,"pId":0,"name":"<DIV>"},
	 *     {"id":2,"pId":1,"name":"<DIV>"}]
	 * @param jDom 当前选中的dom
	 * @returns {Array}
	 */
	function buildZnodes(jDom){
		//递归调用时会传入父节点的zNode id；不传时，取0，表示当前dom为root节点
		var pId = arguments.length == 2 ? arguments[1] : 0;
		var cId = ++zNodeId;
		
		var zNode = {id: cId, pId: pId, name:"<"+jDom.get(0).tagName+">", editable: isDomEditable(jDom)};
		//建立zNode与jDom的映射关系
		zDom[cId] = jDom;
		var result = [zNode];
		jDom.children().each(function(index, element){
			var zNodes = buildZnodes($j(element), cId);
			result = result.concat(zNodes);
		});
		
		return result;
	}
	this.buildZnodes = buildZnodes;
	
	/**
	 * 找到第一个可编辑的dom元素
	 * 
	 * 可编辑的zNode被标记为 editable=true
	 * @param zTree
	 * @returns
	 */
	function findFirstEditableDom(zTree){
		var nodes = zTree.getNodesByParam("editable", true, null);
		if(nodes.length > 0){
			return nodes[0];
		}
		return null;
	}
	this.findFirstEditableDom = findFirstEditableDom;
	
	/**
	 * 获得（图文编辑模式中）当前被选中的zNode节点
	 * @returns
	 */
	function getSelectedZnode(zTree){
		var nodes = zTree.getSelectedNodes();
		return nodes[0];
	}
	this.getSelectedZnode = getSelectedZnode;

	/**
	 * 检查dom tree中当前选中元素是否已经有了该操作指示类
	 * 
	 * @param clazz 表示该dom可以进行该类编辑
	 * @returns
	 */
	function checkDomOperation(clazz, zTree){
		var jDom = zDom[getSelectedZnode(zTree).id];
		return jDom.hasClass(clazz);
	}
	this.checkDomOperation = checkDomOperation;
	
	/**
	 * 该dom是否包含相应的操作项（用CSS class标记）
	 * 
	 * @param jDom
	 * @returns {Boolean}
	 */
	function hasCssClass(jDom){
		//标识该dom的可操作项
		var cssClasses = domConfigManager.getCssClass(jDom);
		var hasCssClass = false;
		$j(cssClasses).each(function(index, cssClass){
			if(jDom.hasClass(cssClass)){
				hasCssClass = true;
			}
		});
		
		return hasCssClass;
	}
	this.hasCssClass = hasCssClass;

	/**
	 * 获得zNode的字体，即 domOperation.fon
	 * @param node zNode对象
	 * @returns
	 */
	function getZnodeFont(node) {
		var jDom = zDom[node.id];
		return domConfigManager.getFont(jDom);
	}
	this.getZnodeFont = getZnodeFont;

	/**
	 * 获得zNode的字体，即 zNode对应的jDom具有domOperation.fon配置的CSS class
	 * @param node zNode对象
	 * @param withCssClass zNode对应的jDom是否拥有cms-area-xxx class
	 * @returns
	 */
	function getWithCssClassZnodeFont(node, withCssClass) {
		var jDom = zDom[node.id];
		
		//如果需要带有class却又没有带
		if(withCssClass && !hasCssClass(jDom)){
			return {};
		} else {
			return domConfigManager.getFont(jDom);
		}
	}
	this.getWithCssClassZnodeFont = getWithCssClassZnodeFont;
	
	/**
	 * 判断该dom是否有编辑项
	 * 
	 * 包含css class = "cms-area-*"可编辑
	 * @param jDom
	 * @returns {Boolean}
	 */
	function isDomEditable(jDom){
		//是否在CMS Instance页
		var isCmsInstanceEdit = $j("#pageInstanceForm").size() > 0;
		
		//查找该tag的可编辑配置
		var cssClasses = domConfigManager.getCssClass(jDom);
		if(cssClasses.length == 0 || (isCmsInstanceEdit && !hasCssClass(jDom))){
			return false;
		} else {
			return true;
		}
	}
	this.isDomEditable = isDomEditable;

	/**
	 * 定位该dom，返回定位数组
	 * 
	 * [{index: 0, tagName: "IMG"}, {index: 3, tagName: "DIV"}, {index: 0, tagName: "BODY"}]
	 * 表示<body>下的第4个儿子（是一个<div>）下的<img>
	 * @param jDom
	 */
	function locate(jDom){
		
		var stack =[{
			index: jDom.prevAll().size(), 
			tagName: jDom.get(0).tagName
		}];
		
		var reachEditWay = false;
		jDom.parents().each(function(i, element){
			var jElement = $j(element); 
			//如果到达列表边界，就不再向上迭代dom
			if(jElement.hasClass(CSS_CLASS.MODE_LIST)){
				reachEditWay = true;
			} else if(reachEditWay){
				return;
			}
			
			stack.push({
				index: jElement.prevAll().size(), 
				tagName: jElement.get(0).tagName
			});
		});
		
		console.log("dom定位：" + JSON.stringify(stack));
		return stack;
	}
	
	/**
	 * 在列表中复制(或删除)相同位置dom的CSS class
	 * 
	 * @param jEditWay 模板编辑区域，标有".cms-imgarticle-edit"
	 * @param jDom 当前正在编辑的dom
	 * @param cssClass
	 * @param isRemoveClass 是否删除class
	 */
	function copyCssClassSetting(jEditWay, jDom, cssClass, isRemoveClass){
		var domLocations = locate(jDom);
		
		//操作每个列表项 jEditWay.children()
		jEditWay.children().each(function(i, listArea){
			console.log("CMS Template 复制列表设置开始...");
			var cloneLocations = domLocations.slice(0);
			var target = $j(listArea);
			console.log("<"+target.get(0).tagName+">");
			
			//列表项的dom结构是否符合dom定位信息
			var sameStructure = true;
			//当前层dom的定位信息
			var location = cloneLocations.pop();
			var indent = "";
			
			while(sameStructure && location){
				
				if(target.size() == 0 || target.get(0).tagName != location.tagName){
					sameStructure = false;
				}
				
				//进入下一层dom
				location = cloneLocations.pop();
				if(location){
					target = target.children().eq(location.index);
					indent+="--";
					console.log(indent+"<"+target.get(0).tagName+">");
				}
			}
			
			if(sameStructure){
				if(isRemoveClass){
					target.removeClass(cssClass);
				} else {
					target.addClass(cssClass);
				}
				console.log("复制列表设置成功： " + cssClass);
			} else {
				console.warn("dom结构不一致，复制列表设置失败.");
			}
		});
	}
	this.copyCssClassSetting = copyCssClassSetting;
}

/**
 * 对dom可配置项的设定
 * 
 * @param impl 提供一个JSON作为功能实现
 */
function DomEditionConfig(impl){
	
	var obj = {
		//描述该对象用于对设定何种 html tag。形如："EDIT_A"，该实例用于配置A标签的操作项
		name: undefined,
		//判断实例是否适合该dom。返回boolean
		isMatch: function(jDom){return undefined;},
		//计算该dom有效的操作项。返回数组，形如：[CSS_CLASS.EDIT_HREF, CSS_CLASS.EDIT_TITLE]
		processConfig: function(jDom){return undefined;}
	};
	
	(function init(){
		$j.extend(obj, impl);
	})();
	
	function getName(){
		return obj.name;
	}
	this.getName = getName;
	
	this.isMatch = obj.isMatch;
	
	this.processConfig = obj.processConfig;
}

/**
 * dom配置项管理器
 * 
 * @param domEditionConfigList dom配置项的设定（数组）
 */
function DomConfigManager(domEditionConfigList){
	
	var domEditionConfigs = [];
	
	(function init(){
		domEditionConfigs = domEditionConfigList;
	})();
	
	/**
	 * 渲染dom tree是对该dom使用何种字体 
	 * 
	 * @param jDom
	 */
	function getFont(jDom){
		if(getCssClass(jDom).length == 0){
			return {};
		} else {
			return {'color':'blue', 'font-weight':'bold'};
		}
	}
	this.getFont = getFont;
	
	/**
	 * 收集该dom可应用的配置项
	 * 
	 * @param jDom
	 * @returns 配置项对应的CSS class数组，形如：[CSS_CLASS.EDIT_HREF, CSS_CLASS.EDIT_TITLE]
	 */
	function getCssClass(jDom){
		var result = [];
		
		$j(domEditionConfigs).each(function(i, config){
			if(!config.isMatch(jDom)){
				return;
			}
			
			var cssClasses = config.processConfig(jDom);
			console.log("<"+jDom.get(0).tagName+">匹配到CSS class: "+ cssClasses);
			result = result.concat(config.processConfig(jDom));
		});
		
		console.log("合并后的CSS class为 "+ result);
		return result;
	}
	this.getCssClass = getCssClass;
}

//--JS Instance------------------------------------------------------

var zDom = new Object(); //全局zNode与jDom的映射，如：zDome[111]=xxxJdom

var aEditionConfig = new DomEditionConfig({
	name: "EDIT_A",
	isMatch: function(jDom){
		return jDom.get(0).tagName == "A";
	},
	processConfig: function(jDom){
		//纯文本超链接
		if(jDom.children().size() == 0){
			return [CSS_CLASS.EDIT_HREF, CSS_CLASS.EDIT_TITLE];
		} else {
			return [CSS_CLASS.EDIT_HREF];
		}
	}
});

var imgEditionConfig = new DomEditionConfig({
	name: "EDIT_IMG",
	isMatch: function(jDom){
		return jDom.get(0).tagName == "IMG";
	},
	processConfig: function(jDom){
		return [CSS_CLASS.EDIT_IMG];
	}
});

var descEditionConfig = new DomEditionConfig({
	name: "EDIT_DESC",
	isMatch: function(jDom){
		return true;
	},
	processConfig: function(jDom){
		if(jDom.children().size() == 0 
				&& ["DIV", "SPAN", "STRONG", "LABEL", "LI", "P", 
				    "H1", "H2", "H3", "H4", "H5", "H6" ].indexOf(jDom.get(0).tagName) != -1){
			return [CSS_CLASS.EDIT_DESC];
		} else {
			return [];
		}
	}
});

var urlEditionConfig = new DomEditionConfig({
	name: "EDIT_HREF",
	isMatch: function(jDom){
		return jDom.attr("href") != undefined;
	},
	processConfig: function(jDom){
		return [CSS_CLASS.EDIT_HREF];
	}
});

var areaEditionConfig = new DomEditionConfig({
	name: "EDIT_AREA",
	isMatch: function(jDom){
		return jDom.get(0).tagName == "AREA";
	},
	processConfig: function(jDom){
		return [CSS_CLASS.EDIT_HREF, CSS_CLASS.EDIT_COORDS];
	}
});

var domConfigManager = new DomConfigManager([aEditionConfig, imgEditionConfig, descEditionConfig, urlEditionConfig, areaEditionConfig]);

var domTreeManager = new DomTreeManager();