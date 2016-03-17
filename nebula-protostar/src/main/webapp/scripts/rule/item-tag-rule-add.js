/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"LABEL_ITEM_CODE":"商品编码",
	"LABEL_ITEM_TITLE":"商品名称",
	"LABEL_ITEM_SALE_PRICE":"标价",
	"LABEL_ITEM_LIST_PRICE":"贴牌价",
	"EXIST_MESSAGE":"已包含此项，不必重复添加",
	"INFO_NO_NAME":"请输入组合名称",
	"INFO_NO_DATA":"包含列表不能为空",
	"INFO_SUCCESS":"创建成功",
	"INFO_REPEATED_NAME":"组合名称已被使用，请更换其他名称",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
	"TEXT_REMOVE":"删除",
	"TEXT_INCLUDE":"包含",
	"TEXT_EXCLUDE":"排除",
	"TEXT_STAFF":"全体",
	"TEXT_ITEM":"商品",
	"TEXT_CATEGORY":"分类"
});

/* ------------------------------------------------- URL ------------------------------------------------- */

/* ------------------------------------------------- 全局常量 ------------------------------------------------- */

/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
var SEARCH_RESULT_INPUT = null;	//包含列表与排除列表的查询结果输入框

/* ------------------------------------------------- ready ------------------------------------------------- */
//已分类商品列表url
var itemCtListUrl = base + '/product/scope/itemSelect.json';
//保存自定义组合
var saveCategoryUrl = base + "/product/scope/saveCategory.json";


/**-------------------------------right----------------------------------------*/
//table1
function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
}


function getTitle(data, args, idx){
	var title =loxia.getObject("title", data);
	
	var name =title;
	
	if(title==null||title==''){
		name= nps.i18n("NO_CATEGORY");
	}	
	var hiddenNameInput ="<input type='hidden' id='title_"+loxia.getObject("id", data)+"' value='"+title+"' />";
	name +=hiddenNameInput;
	return name;
}
function getSalePrice(data, args, idx){
	var title =loxia.getObject("salePrice", data);
	
	var name =title;
	
	if(title==null||title==''){
		name= nps.i18n("NO_CATEGORY");
	}	
	var hiddenNameInput ="<input type='hidden' id='salePrice_"+loxia.getObject("id", data)+"' value='"+title+"' />";
	name +=hiddenNameInput;
	return name;
}

function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
    
}


/**-----------------------------------------------------------------------*/
$j(document).ready(function() {

	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();  
    
    
	//商品列表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox'  />",
			width : "3%",
			template:"drawCheckbox"
		},{
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "10%",
		},
		{
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			template:"getTitle",
		}, {
			name : "salePrice",
			label : nps.i18n("LABEL_ITEM_SALE_PRICE"),
			width : "10%",
			template:"getSalePrice",
		}, {
			name : "listPrice",
			label : nps.i18n("LABEL_ITEM_LIST_PRICE"),
			width : "12%",
		} ],
		dataurl : itemCtListUrl
	});
	
	
	refreshData();
	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	
	//添加包含项
	$j(".btn-add-left").on("click",function(){		
		var names = $j(this).siblings(".txt-result").val();
		if ($j.trim(names).length == 0 && val != 0) return;
		var ids = $j(this).siblings(".txt-result").data("ids");
		var html = "<tr><td tip='";
		html += nps.i18n("TEXT_ITEM")+"：" + names + "' data='" + ids + "' dname='"+names+"'>"+ nps.i18n("TEXT_ITEM")+"：" + names;
		html += "</td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
		$j(".tbl-result tbody").append(html);
		$j(this).siblings(".txt-result").val('');
		$j(this).siblings(".txt-result").removeData("ids");
	});
	
	//删除按钮
	$j(".tbl-result tbody").on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
	});
		
	
	$j(".slt-type").change();	//隐藏查询按钮
	
	//‘查询’按钮点击事件
	$j(".btn-search").click(function() {
		SEARCH_RESULT_INPUT = $j(this).siblings(".txt-result");
		$j("#dialog-item-select").dialogff({type:'open',close:'in',width:'1000px',height:'700px'});
	});
	
	//商品选择器‘确定’按钮点击事件
	$j("#dialog-item-select .btn-ok").click(function() {
		var itemid="",description="";
    	$j(".checkId:checked").each(function(i,n){
        	if("" !=description){
        		description +=",";
        	}
        	if(""!= itemid){
        		itemid +=",";
        	}
        	itemid += $j(this).val()
        	description +=$j("#title_"+$j(this).val()).val();
        });
    	SEARCH_RESULT_INPUT.val(description);
		SEARCH_RESULT_INPUT.data("ids", itemid);
    	$j("#dialog-item-select").dialogff({type : 'close'});
	});
	
	
	//保存按钮
	$j(".btn-save").click(function() {
		var name = $j.trim($j("#scope-name").val());
		var ids = "";
		var dname = "";
		$j('#tbl-result-include tbody tr td:even').each(function(i,e) {		
			if(""!= ids){
				ids+=","
			}
			ids+= $j(e).attr("data");
			
			if("" != dname){
				dname+=",";
			}
			dname+= $j(e).attr("dname");
			});
		
		if (name.length == 0) {
			$j("#scope-name").blur().focus();
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_NAME"));
			return;
		}
		if (ids.length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_DATA"));
			return;
		}
		var json = {
				'type' : 1,
				'name' : name,
				'expression' : ids,
				'text':"【包含】"+dname
					
		};
		
		// 提交表单
		nps.asyncXhrPost(saveCategoryUrl, json, {
			   successHandler : function(data, textStatus) {
				   
				   if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
						$j("#custom-block tbody").empty();
						$j("#txt-name").val("");
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
					}
			   }
		});
	});
});