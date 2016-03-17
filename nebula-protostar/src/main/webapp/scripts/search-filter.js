(function($){

    $.extend(loxia.regional['zh-CN'],{
        "NPS_BUTTON_OK":"确定",
        "NPS_BUTTON_CANCEL":"取消",
        "NPS_OPERATE_SUCCESS": "操作成功",
        "NPS_OPERATE_FAILURE": "操作失败",
        "NPS_FORM_CHECK_ERROR":"错误信息",

        "NPS_SYSTEM_ERROR": "系统出错，请联系系统管理员",
        "NPS_SYSTEM_CONNECTION_ERROR":"连接系统出错，出错信息为{0}，请重试或联系系统管理员",
        "NPS_OPERATE_FORM_SUBMIT_SUCCESS": "表单{0}成功提交"
    });

    if(typeof this["searchFilter"] === "undefined"){
        this.searchFilter = {
        	  formId:'',
        	  ajaxUrl:'/common/saveSearchFilter.json',
        	  searchButtonClass:'',
        	  clearFilterUrl:'/common/clearSearchFilter.json',
        	  readFilterUrl:'/common/readSearchFilter.json',
        	  customSetFilter:'',					//自定义设置的方法
        	  isReadFilterParam:'keepfilter',
 //           popup: false,
            //prop for main window
//            loadingpane: undefined,
//            infoblock: undefined,

            //prop for frame window
            errorblock: undefined,

            init: function(settings){

                $.extend(this,settings);
                
                $("#"+this.formId).append(
                		"<input type='hidden' name='filterKeyName' value='"+this.makeKey()+"'/>"
                );
                
                //绑定搜索的事情，点击搜索后会保存搜索条件
                $(this.searchButtonClass).click(function(){
                	searchFilter.submitFilterParam();
                });
                
                //需要读取数据
                if(this.isReadFilterCondition()){
                	var data=this.readFilterParam();
                	if(this.customSetFilter!=null&&this.customSetFilter!=''){
                		
                		 loxia.hitch(window,this.customSetFilter)(data);
                	}
                	else{
                		this.setFilterToForm(data);
                	}
                }
                //清除条件
                else{
                	
                	this.clearFilterParam();
                }
                
            },
            /**
             * 将session保存的条件放置到表单中
             * @param data
             */
            setFilterToForm:function(data){
            	
            	 for ( var p in data ){ 
            		  // 方法 
					  if ( typeof ( data [ p ]) == " function " ){
						 // obj [ p ]() ; 
					  }
					  //属性
					  else { 
						  // p 为属性名称，obj[p]为对应属性的值 
						//  props += p + " = " + obj [ p ] + " /t " ;
						  $("input[name='"+p+"']").val(data[p]);
						  
						  $("select[name='"+p+"']").val(data[p]);
						 
					  } 
				  } 
            	
            },
            /**
             * 在url地址中，通过参数名来读取参数值
             * @param name
             * @returns
             */
            queryUrlParamValue:function(name){
            	
            	 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            	 var r = window.location.search.substr(1).match(reg);
            	 if (r != null) 
            		 return unescape(r[2]);
            	  
            	  return null;
            },
            
            //是否读取filter condition
            //if true:will read by session
            //if false:will reset session
            isReadFilterCondition:function(){
            	var b=this.queryUrlParamValue(this.isReadFilterParam);
            	if(b){
            		return true;
            	}
            	
            	return false;
            },

            /**
             * use pathname make key 
             * @returns
             */
            makeKey: function(){
            	var pathname=window.location.pathname;
                return pathname;
            },
            
            validateForm: function(form){
                var rtn = loxia.validateForm(form);
                if(rtn.length ==0){
                  
                    return true;
                }else{
                   
                    return false;
                }
            },
            
            submitFilterParam: function(){
            	
            	var f = loxia._getForm(this.formId);
            	
            	var c = this.validateForm(f);
                if(c){
                	
                    loxia.asyncXhr(this.ajaxUrl,f,{
                        type: "POST"
                    });
                }
            },
            
            clearFilterParam:function(){
            	
            	loxia.asyncXhr(this.clearFilterUrl,{'filterKeyName':this.makeKey()},{
                    type: "POST"
                });
            },
            
            readFilterParam:function(){
            	
            	return loxia.syncXhr(this.readFilterUrl,{'filterKeyName':this.makeKey()},{
                    type: "POST"
                });
            },
          
           
        };
    }

})(jQuery);