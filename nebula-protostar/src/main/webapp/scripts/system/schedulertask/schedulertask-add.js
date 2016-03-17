$j.extend(loxia.regional['zh-CN'],{ 
	 	"CODE":"任务的编码，英文大写，以_分隔",
	 	"DESCRIPTION":"任务的描述",
	 	"BEAN_NAME":"对应将执行的Spring容器的BeanName",
	 	"METHOD_NAME":"将执行的方法名，必须是无参方法",
	 	"TIME_EXP":"<p>例：0 0 12 ? * WED 表示每个星期三的12点执行,这里没有使用“年”这项</p>" +
	 			"<p>字段名（项）	必须	值范围	特殊字符</p>" +
	 			"<p>秒	是	0-59	, - * /</p>" +
	 			"<p>分	是	0-59	, - * /</p>" +
	 			"<p>时	是	0-23	, - * /</p>" +
	 			"<p>月的某天	是	1-31	, - * ? / L W</p>" +
	 			"<p>月	是	1-12 or JAN-DEC	, - * /\n" +
	 			"<p>星期的某天	是	1-7 or SUN-SAT	, - * ? / L #</p>" +
	 			"<p>年	否	empty, 1970-2099	, - * /</p>",
	 	"EXP_ERROR":"时间表达式校验有误",
	 	"BEAN_NAME_NO_CONTAIN_EMPTYSTR":"不能包含空字符串",
	 	"CLASS_NAME_MUST_BE_ALPHABET_NUMBER":"必须以字母开头且只包含字母或数字",
	 	"LIMIT_MAX_LENGTH":"长度不可超过50"
	 	//"ITEM_CATEGORY_CHOOSE_LEAF":"必须选择叶子节点",
	 	//"ITEM_CATEGORY_CHOOSE_NOT_ROOT":"ROOT节点不能被选择"
});

var conditionUrl=base+"/system/schedulerTask/manager.htm";

function checkInput(sel, obj){
	if(obj.val()==""){	
    	var errorMessage = nps.i18n("EMPTY");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
    return loxia.SUCCESS;
}

function checkTimeExp(sel, obj){
	var len=sel!=null?sel.trim().split(" ").length:0;
	if(len!=6&&len!=7){	
    	var errorMessage = nps.i18n("EXP_ERROR");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
    return loxia.SUCCESS;
}

function checkBeanName(sel, obj){
	var checkStr = sel.trim();
	if(checkStr==""){	
    	var errorMessage = nps.i18n("INVALID_EMPTY_DATA");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
	if(checkStr.length >50){
		var errorMessage = nps.i18n("LIMIT_MAX_LENGTH");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
	}
	var  reg =/^[a-zA-Z][a-zA-Z0-9]*$/;
	if(!reg.test(checkStr)){
		var errorMessage = nps.i18n("CLASS_NAME_MUST_BE_ALPHABET_NUMBER");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
	}
	return loxia.SUCCESS;
}

function checkMethodName(sel, obj){
	var checkStr = sel.trim();
	if(checkStr==""){	
    	var errorMessage = nps.i18n("INVALID_EMPTY_DATA");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
	if(checkStr.length >50){
		var errorMessage = nps.i18n("LIMIT_MAX_LENGTH");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
	}
	var  reg =/^[a-zA-Z][a-zA-Z0-9]*$/;
	if(!reg.test(checkStr)){
		var errorMessage = nps.i18n("CLASS_NAME_MUST_BE_ALPHABET_NUMBER");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
	}
	return loxia.SUCCESS;
}




//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}


$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	$j("#submitButton").click(function(){
		nps.submitForm('submitForm1',{mode:'sync'});
	});

	$j("#canel").click(function(){
		window.location.href=conditionUrl+"?keepfilter=true";
	});	
	
	$j(".help").on('click',function(){
		$j("#dialog-help").dialogff({type:'open',close:'in',width:'400px',height:'250px'});
		var thisData=$j(this).attr("data-val");
		$j("#dialog-text").html(nps.i18n(thisData));
	});
	
	$j(".btn-ok").on('click',function(){
		$j("#dialog-help").dialogff({type:'close'});
	});
});
