/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'], {
	"LABEL_COUPON_CODE" : "商品id",
	"LABEL_COUPON_NAME" : "筛选色 名称",
	"LABEL_COUPON_STATE" : "筛选色 色值",
	"LABEL_COUPON_LISTPRICE" : "商品色 名称",
	"LABEL_COUPON_SALEPRICE" : "商品色 色值",
	"LABEL_COUPON_CREATETIME" : "创建时间",
	"LABEL_COUPON_DELECT" : "操作",
	"ITEM_CATEGORY_OPERATE_INFO" : "提示信息",
	"LBAEL_ITEM_OPERATE_SUCCESS" : "删除成功",
	"ITEM_CATEGORY_CHECK_ERROR" : "错误信息",
});


/*
 * ------------------------------------------------- URL
 * -------------------------------------------------
 */
var IMPORT_URL = base + "/itemColor/itemColorValueRefAnalysis.json";
var LIST_URL = base+'/itemColor/itemColorValueRefList.json'; 
var REMOVE_URL = base + '/itemColor/removeItemColorValueById.json';
/*
 * ------------------------------------------------- 全局常量
 * -------------------------------------------------
 */

/*
 * ------------------------------------------------- 全局变量
 * -------------------------------------------------
 */

/*
 * ------------------------------------------------- ready
 * -------------------------------------------------
 */
$j(document).ready(function() {
					loxia.init({
						debug : true,
						region : 'zh-CN'
					});
					nps.init();
					$j("#table1").loxiasimpletable(
									{
										page : true,
										size : 12,
										nodatamessage : '<span>'
												+ nps.i18n("NO_DATA")
												+ '</span>',
										form : "searchForm",
										cols : [
												{
													name : "id",
													label : nps
															.i18n("LABEL_COUPON_CODE"),
													width : "5%"
												},
												{
													name : "filterColor",
													label : nps
															.i18n("LABEL_COUPON_NAME"),
													width : "5%"
												},
												{
													name : "filterColorValue",
													label : nps
															.i18n("LABEL_COUPON_STATE"),
													width : "5%",
												},
												{
													name : "itemColor",
													label : nps
															.i18n("LABEL_COUPON_LISTPRICE"),
													width : "5%"
												},

												{
													name : "itemtColorValue",
													label : nps
															.i18n("LABEL_COUPON_SALEPRICE"),
													width : "5%",
													 
												} 
												,
												{
													name : "createTime",
													label : nps
															.i18n("LABEL_COUPON_CREATETIME"),
													width : "5%",
													formatter : "formatDate",
												} ,
												{
													label : nps
															.i18n("LABEL_COUPON_DELECT"),
													width : "5%",
													type : "oplist",
													template : "drawEditItem",
												} 
												],
										dataurl : LIST_URL
									});
					refreshData();

					// 筛选数据
					$j(".func-button.search")
							.click(
									function() {
										$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
										refreshData();
									});
					// 导入
					$j(".import").click(function() {
						window.location.href = IMPORT_URL;
					});

				});

/*
 * ------------------------------------------------- draw
 * -------------------------------------------------
 */
// 操作中的启用禁用函数
//操作函数
function drawEditItem(data) {
	var itemId = loxia.getObject("id", data);
	var result = "<a href='javascript:void(0);' val='" + loxia.getObject("id", data)
			+ "' class='func-button delete' onclick='sortDel(" + itemId + ")'>" + nps.i18n("OPERATOR_DELETE") + "</a>";
	return result;
}

function sortDel(id) {
	simplePost(REMOVE_URL, id);
}

function simplePost(url, id) {
	var json = {
			"id" : id
		};
		var _d = nps.syncXhr(url, json, {
			type : "POST"
		});
		if (_d==1) {
			nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_SUCCESS"));
			refreshData();
		} else
			nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), _d.exception.message);
}

/*
 * ------------------------------------------------- util
 * -------------------------------------------------
 */
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

// 获取日期格式
function formatDate(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
				+ date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}
