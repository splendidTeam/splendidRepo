$j.extend(loxia.regional['zh-CN'],{
    "NPS_FORM_CHECK_INFO":"系统提示",
    "CSFILTER_ADD_SUCCESS":"自定义筛选器 {0} 已经成功创建",
    "CSFILTER_ADD_FAILURE":"自定义筛选器{0} 创建失败",
    "CSFILTER_UPDATE_SUCCESS":"自定义筛选器 {0} 已更新",
    "CSFILTER_UPDATE_FAILURE":"自定义筛选器 {0} 更新失败",
    "CSFILTER_TEST_ERRORMSG":"测试未通过:{0}",
    "CSFILTER_ITEM_CODE":"商品编码",
    "CSFILTER_ITEM_TITLE":"商品名称",
    "CSFILTER_ITEM_SALEPRICE":"销售价格(元)",
    "CSFILTER_TYPE":"类型",
    "CSFILTER_STATUS":"状态",
    "CSFILTER_MEMBER_CODE":"会员编号",
    "CSFILTER_MEMBER_NICKNAME":"昵称",
    "CSFILTER_MEMBER_LOGO_NAME":"登陆名",
    "CSFILTER_MEMBER_SEX":"性别",
    "CSFILTER_REGEXP_PROMPT":"必须为字母(a~z或A~Z)开头",
    "ITEM_TYPE_MAIN":"主商品",
    "ITEM_TYPE_GIFT":"非卖品",
    "SEX_MAN":"男",
    "SEX_WOMAN":"女"
});

var backUrl = base +"/rule/csfilterclseslist.htm";

var findCustomFilterClsUrl = base +"/rule/findCustomFilterCls.json";

var resultForTestCustomFilterOnMember = base +"/rule/resultForTestCustomFilterOnMember.json";

var resultForTestCustomFilterOnItem = base +"/rule/resultForTestCustomFilterOnItem.json";

var resultForTestCustomFilter = base +"/rule/validateCustomizeFilter.json";

var enableOrDisableCustomizeFilterByIdsUrl = base+'/rule/enableOrDisableCustomizeFilterByIds.json';

var STATE_ENABLED =1;
var STATE_DISABLED =0;


function checkServiceName(val, obj){
	// 以字母开始,可以包含数字.
	var regExp = /^([a-z]|[A-Z])([0-9]|[a-z]|[A-Z])*/;
	if(!regExp.test(val)){
		return nps.i18n("CSFILTER_REGEXP_PROMPT");
	}
	return loxia.SUCCESS;
}

function itemTypeTemplate(data){
	var type = loxia.getObject("type", data);
	if(type == null || type == 1){
		return nps.i18n("ITEM_TYPE_MAIN");
	}
	else if(type == 0){
		return nps.i18n("ITEM_TYPE_GIFT");
	}
	return "";
}

function sexTemplate(data){
	var sex = loxia.getObject("sex", data);
	if(sex == 1){
		return nps.i18n("SEX_WOMAN");
	}
	else if(sex == 0){
		return nps.i18n("SEX_MAN");
	}
	else{
		return "";
	}
}

// 商品状态
$j.ui.loxiasimpletable().typepainter.threeState = {
	getContent : function(data) {
		if (data == 0 || data == 2) {
			return "<span class='ui-pyesno ui-pyesno-no' title='下架'></span>";
		} else if (data == 1) {
			return "<span class='ui-pyesno ui-pyesno-yes' title='上架'></span>";
		} else if (data == 3) {
			return "<span class='ui-pyesno ui-pyesno-wait' title='新建'></span>";
		}
	},
	postHandle : function(context) {
		// do nothing
	}
};


$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    var scopeType = $j("#scopeType").val();
    if(scopeType!=1){
    	$j("#shopSelect").parent().parent().hide();
    }else{
    	$j("#shopSelect").parent().parent().show();
    }
    $j("[name=scopeType]").change(function(){
        var scopeType = $j(this).val();
        if(scopeType!=1){
        	$j("#shopSelect").parent().parent().hide();
        }else{
        	$j("#shopSelect").parent().parent().show();
        }
    });
    
    //保存
    $j(".submit").click(function(){
    	
    	var scopeName =$j.trim($j("#scopeName").val());
    	
    	 nps.submitForm('csFilterForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess){
					return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("CSFILTER_UPDATE_SUCCESS",[scopeName]));
				}else{
					return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("CSFILTER_UPDATE_FAILURE",[scopeName]));
				}
		}});
    });
    
  //返回
    $j(".return").click(function(){
    	window.location.href=backUrl+"?keepfilter=true";
    });
    
  //测试结果
	$j(".func-button.search1").click(function(){
		
		var id =$j("#fiterId").val();
		
		var curLifecycle =$j("#curLifecycle").val();
		
		//-------------------------测试------------------------start--
		
		var enable_json={"ids":id,"state":STATE_ENABLED};
		var disable_json={"ids":id,"state":STATE_DISABLED};
		//---启用--
		nps.syncXhr(enableOrDisableCustomizeFilterByIdsUrl, enable_json,{type: "GET"});
		
		var testRlt =loxia.syncXhr(resultForTestCustomFilter, {"id":id},{type:"GET"});
		
		if(!testRlt.isSuccess){
			return nps.info(nps.i18n("NPS_FORM_CHECK_INFO"),nps.i18n("CSFILTER_TEST_ERRORMSG",[testRlt.description]));
		}
		
		
		//-------------------------测试------------------------end--
		
		//根据id在controller获取该记录是商品类型还是会员，从而判断测试结果集类型
		
		var obj =loxia.syncXhr(findCustomFilterClsUrl, {"id":id},{type:"GET"});
		
		if(obj.scopeType == 1){
			$j("#table1").loxiasimpletable({
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				cols : [
				{
					name : "id",
					label : "ID",
					width : "15%" 
				},{
					name : "code",
					label : nps.i18n("CSFILTER_ITEM_CODE"),
					width : "20%" 
				},{
					name : "title",
					label : nps.i18n("CSFILTER_ITEM_TITLE"),
					width : "25%" ,
				},{
					name : "salePrice",
					label : nps.i18n("CSFILTER_ITEM_SALEPRICE"),
					width : "15%" ,
				},{
					name : "type",
					label : nps.i18n("CSFILTER_TYPE"),
					width : "15%",
					template : "itemTypeTemplate"
				},{
					name : "lifecycle",
					label : nps.i18n("CSFILTER_STATUS"),
					width : "10%",
					type : "threeState"
				}],
				dataurl : resultForTestCustomFilterOnItem+"?id="+id
			});
			$j("#table1").loxiasimpletable("refresh");
			
		}else if(obj.scopeType == 2){
			$j("#table1").loxiasimpletable({
				page:true,
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				cols : [
				{
					name : "id",
					label : nps.i18n("CSFILTER_MEMBER_CODE"),
					width : "10%" 
				},{
					name : "nickName",
					label : nps.i18n("CSFILTER_MEMBER_NICKNAME"),
					width : "30%" ,
				},{
					name : "loginName",
					label : nps.i18n("CSFILTER_MEMBER_LOGO_NAME"),
					width : "30%" 
				},{
					name : "sex",
					label : nps.i18n("CSFILTER_MEMBER_SEX"),
					width : "30%" ,
					template:"sexTemplate"
				}],
				dataurl : resultForTestCustomFilterOnMember+"?id="+id
			});
			$j("#table1").loxiasimpletable("refresh");
		}else if(obj.scopeType == 3){
			nps.info(nps.i18n("NPS_FORM_CHECK_INFO"), "测试通过,自定义服务正常!");
		}
		//---禁用--
		//测试逻辑:对当前为启用的数据进行测试，无需修改状态；禁用的数据测试时，先启用再禁用
		if(curLifecycle != STATE_ENABLED){
			nps.syncXhr(enableOrDisableCustomizeFilterByIdsUrl, disable_json,{type: "GET"});
		}
	}); 
});
