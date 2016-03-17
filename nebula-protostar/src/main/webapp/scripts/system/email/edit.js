$j.extend(loxia.regional['zh-CN'],{
	"INFO_TITLE_DATA":"提示信息",
	"INFO_ADD_SUCCESS":"添加成功!",
	"INFO_EDIT_SUCCESS":"修改成功!"
});
function validateInput(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j("."+cls).each(function(){
		var val =$j(this).val();
		if(val==null || val==""){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
	 });
	return result; 
}
 
function setEmpty(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j("."+cls).each(function(){
		$j(this).val(null);
	 });
	return result; 
}
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	//确定按钮
	$j(".saveForm").on("click",function(){
		 if(validateInput("input_add")==false){
			 return;
		 }
		 $j(".saveForm").attr('disabled',"true");
		 var id = $j("#emailId").val(); 
		 var code = $j(".email-content .code").val();
		 var name = $j(".email-content .name").val();
		 var description = $j(".email-content .description").val();
		 var type = $j(".email-content #type").val();
		 var subject = $j(".email-content .subject").val();
		 var body = $j(".email-content .body").val();
		 var json;
		 if(id==null || id==""){
			 json = {
					 'code':code,
					 'name':name,
					 'description':description,
					 'type':type,
					 'subject':subject,
					 'body':body
			      };
		 }else{
			 json = {
					 'id':id,
					 'name':name,
					 'description':description,
					 'type':type,
					 'subject':subject,
					 'body':body
			      }; 
		 }
		 //保存操作
		 nps.asyncXhrPost(base+"/email/addOrEdit.json", json,{successHandler:function(data, textStatus){
			 $j(".saveForm").removeAttr("disabled"); 
			if (data.isSuccess) {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_EDIT_SUCCESS"));
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ADD_SUCCESS"));
					 setEmpty("input_add");
					 $j(".email-content .description").val("");
				 }
			} else {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				 }
			}
		}});
		 
	 });
	 
	//返回
    $j(".return").click(function(){
    	window.location.href="/email/list.htm?keepfilter=true";
    });
 
}); 




