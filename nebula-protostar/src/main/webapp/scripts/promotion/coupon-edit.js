/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'], {
	"INFO_TITLE_DATA" : "提示信息",
	"INFO_SUCCESS" : "保存成功！",
	"INFO_FAILURE" : "保存失败！"
});
/* ------------------------------------------------- URL ------------------------------------------------- */
var SAVE_URL = base + "/coupon/createOrupdate.json";
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	// readonly="readonly" 
	var optype = $j("#optype").val();
	if(optype=='view'){
		$j("input").attr("readonly","readonly");
		$j("select").attr("readonly","readonly");
	}
	
	$j("#discount").keyup(function(){
		var type = $j("#type").val();
		if(type==1){
			if(this.value.indexOf('.')>-1){
				var v=  this.value.substring(this.value.indexOf('.')+1,this.value.length);
				if(v.length>=2){
					this.value = this.value.substring(0,this.value.indexOf('.')+3);
				}
			}else{
		        this.value = this.value.replace(/[^\d.]/g, '');
			}
		}
    }).bind("paste",function(){  //CTR+V事件处理     
        this.val(this.val().replace(/[^\d.]/g,''));     
    });
	  //保存
    $j(".saveForm").click(function(){
    	var type = $j("#type").val();
    	var discount = $j("#discount").val();
    	if(type == 2){
    		if(discount < 0 || discount > 100 ){
    			 nps.info("提示信息","折扣范围只能是0-100");
    			return;
    		}
    	}
    	if(discount < 0 ){
			 nps.info("提示信息","优惠金额必须大于0");
			return;
		}
    	var countreg=/^[0-9]+([.]{1}[0-9]{1,2})?$/;
    	if(countreg.test(discount)==false){
    		 nps.info("提示信息","优惠金额必须是整数或小数精度为2位");
    		return;
    	}
    	var count = $j("#count").val();
    	if(count<1){
    		nps.info("提示信息","使用次数最小为1");
    		return;
    	}
    	 nps.submitForm('couponForm',{mode: 'async', 
				successHandler : function(data){
					
				var optype = $j("#optype").val();
				if(data.isSuccess){
					if(optype=="new"){
						 nps.info("提示信息",nps.i18n("优惠劵新增成功"));
						 $j("#couponForm")[0].reset();;
					}else{
						nps.info(nps.i18n("提示信息"),nps.i18n("优惠劵修改成功"));
					}
				}else{
					if(optype=="new"){
						 nps.info("提示信息",nps.i18n("优惠劵新增失败"));
						 $j("#couponForm")[0].reset();
					}else{
						nps.info(nps.i18n("提示信息"),nps.i18n("优惠劵修改失败"));
					}
				}
		}});
    });
    
  //返回
    $j(".return").click(function(){
    	window.location.href="/coupon/coupon.htm?keepfilter=true";
    });
    
	
});
