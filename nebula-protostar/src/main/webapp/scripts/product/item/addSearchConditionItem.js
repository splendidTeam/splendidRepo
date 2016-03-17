$j.extend(loxia.regional['zh-CN'],{ 
	 	"MAXMIN":"最大值不可以小于最小值"
});
var settingConditionUrl="/item/itemSearchCondition/managerSetting.htm";



function checkSelect(sel, obj){
	if(obj.val()==""){	
    	var errorMessage = nps.i18n("EMPTY");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
    return loxia.SUCCESS;
}
function minCheck(sel, obj){
	if(obj.val()==""){	
    	var errorMessage = nps.i18n("EMPTY");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }else{

    	if($j("#areaMax").val()!=""){
    		if(parseInt($j("#areaMin").val())>parseInt($j("#areaMax").val())){
	    		obj.state(false,nps.i18n("MAXMIN"));
	    		return loxia.FAILURE;
    		}
    	}
    }
    return loxia.SUCCESS;
}
function maxCheck(sel, obj){
	if(obj.val()==""){	
    	var errorMessage = nps.i18n("EMPTY");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }else{
    	if(parseInt($j("#areaMax").val())<parseInt($j("#areaMin").val())){
    		obj.state(false,nps.i18n("MAXMIN"));
    		return loxia.FAILURE;
		}
    }
    return loxia.SUCCESS;
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
		window.location.href=settingConditionUrl+"?pid="+$j("#pid").val()+"&keepfilter=true";
	});
	
	$j("#propertyValueId").change(function(){
		if($j(this).val()!=""){
			$j("#name").val($j(this).find("option:selected").text());
		}
	});
			
});