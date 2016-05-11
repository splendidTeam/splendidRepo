jQuery.extend({
 
    createUploadIframe: function(id, uri)
    {
           //create frame
            var frameId = 'jUploadFrame' + id;
            var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
           if(window.ActiveXObject)
           {
                if(typeof uri== 'boolean'){
                  iframeHtml += ' src="' + 'javascript:false' + '"';
 
                }
                else if(typeof uri== 'string'){
                  iframeHtml += ' src="' + uri + '"';
 
                }
           }
           iframeHtml += ' />';
           jQuery(iframeHtml).appendTo(document.body);
 
            return jQuery('#' + frameId).get(0);       
    },
    createUploadForm: function(id, fileElementId, data,role,model)
    {
       //create form
       var fileInputName='fileToUpload';
       var formId = 'jUploadForm' + id;
       var fileId = 'jUploadFile' + id;
       var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');  
       if(data)
       {
           for(var i in data)
           {
              jQuery('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
           }         
       }     
       var oldElement = jQuery('#' + fileElementId);
       var oldParent=oldElement.parent();
       var newElement = jQuery(oldElement).clone(true);
      
       oldParent.append(newElement);
       
       jQuery(oldElement).attr('id', fileId);
       jQuery(oldElement).attr('name', fileInputName);
       jQuery(oldElement).before(newElement);
       jQuery(oldElement).appendTo(form);
 
       jQuery('<input type="hidden" name="role" value="' + role + '" />').appendTo(form);
       
       jQuery('<input type="hidden" name="model" value="' + model + '" />').appendTo(form);
      
       //set attributes
       jQuery(form).css('position', 'absolute');
       jQuery(form).css('top', '-1200px');
       jQuery(form).css('left', '-1200px');
       jQuery(form).appendTo('body');    
       return form;
    },
    
    successProcess:function(url,fileElementId){
    	
    	 var element = jQuery('#' + fileElementId);
    	
    	 var hName=element.attr("hName");
    	 
    	 var imgName=element.attr("imgName");
    	
    	 jQuery("input[name='"+hName+"']").val(url);
    	 
    	 jQuery("img[name='"+imgName+"']").attr("src",url);
    	 
    	 element.attr("value","");
    	 
    	 //找到form，并扣掉当前form中的应上传成功的图片数
    	 var formId=element.attr("formId");
    	 if(formId!=undefined){
			 var imgCount=jQuery("#"+formId).attr("imgCurCount");
	    	 var intImgCount=parseInt(imgCount);
	    	 jQuery("#"+formId).attr("imgCurCount",--intImgCount);
	    	 
	    	 //再提交一次form
	    	 try{
	    		 jQuery("#"+formId).submit();
	    	 }
	    	 catch(e){
	    		 alert(e);
	    	 }
    	 }
    	 
    	 
    },
    handleError: function( s, xhr, status, e ) 	{
    	// If a local callback was specified, fire it
    			if ( s.error ) {
    				s.error.call( s.context || s, xhr, status, e );
    			}

    			// Fire the global callback
    			if ( s.global ) {
    				(s.context ? jQuery(s.context) : jQuery.event).trigger( "ajaxError", [xhr, s, e] );
    			}
    		},
    callFunction:function(fn,args){
    	fn.apply(this, args);  
    },
    ajaxFileUpload: function(s) {
        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout     
        s = jQuery.extend({}, jQuery.ajaxSettings, s);
        var id = new Date().getTime();       
       var form = jQuery.createUploadForm(id, s.fileElementId, (typeof(s.data)=='undefined'?false:s.data),s.role,s.model);
       var io = jQuery.createUploadIframe(id, s.secureuri);
       var frameId = 'jUploadFrame' + id;
       var formId = 'jUploadForm' + id;      
        // Watch for a new set of requests
        if ( s.global && ! jQuery.active++ )
       {
           jQuery.event.trigger( "ajaxStart" );
       }           
        var requestDone = false;
        // Create the request object
        var xml = {} ; 
        if ( s.global )
            jQuery.event.trigger("ajaxSend", [xml, s]);
        // Wait for a response to come back
        var uploadCallback = function(isTimeout)
       {         
           var io = document.getElementById(frameId);
            try
           {            
              if(io.contentWindow)
              {
                   xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
                   xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;
                   
              }else if(io.contentDocument)
              {
                   xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
                  xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
              }                   
            }catch(e)
           {
              jQuery.handleError(s, xml, null, e);
           }
            if ( xml || isTimeout == "timeout")
           {            
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    // Make sure that the request was successful or notmodified
                    if ( status != "error" )
                  {
                        // process the data (runs the xml through httpData regardless of callback)
                        var data = jQuery.uploadHttpData( xml, s.dataType );   
                        // If a local callback was specified, fire it and pass it the data
                        if ( s.success ){
                            s.success( data, status );
                        }
                        else{
                        	//console.log(data.url);
                        	jQuery.successProcess(data.url,s.fileElementId);
                        }
                        
                        if(s.complete){
            				jQuery.callFunction(eval(s.complete),[]);
            			}
                        
                        //自定义的callback
                        if(s.callback){
                        	 var element = jQuery('#' + s.fileElementId);
                        	 var hName=element.attr("hName");
                        	jQuery.callFunction(eval(s.callback),[data,hName]);
                        }
                        
                       
                        
                        // Fire the global callback
                        if( s.global )
                            jQuery.event.trigger( "ajaxSuccess", [xml, s] );
                    } else
                        jQuery.handleError(s, xml, status);
                } catch(e)
              {
                    status = "error";
                    jQuery.handleError(s, xml, status, e);
                }
 
                // The request was completed
                if( s.global )
                    jQuery.event.trigger( "ajaxComplete", [xml, s] );
 
                // Handle the global AJAX counter
                if ( s.global && ! --jQuery.active )
                    jQuery.event.trigger( "ajaxStop" );
 
                // Process result
                if ( s.complete )
                    s.complete(xml, status);
 
                jQuery(io).unbind();
 
                setTimeout(function()
                                {   try
                                   {
                                       jQuery(io).remove();
                                       jQuery(form).remove();  
                                      
                                   } catch(e)
                                   {
                                       jQuery.handleError(s, xml, null, e);
                                   }                              
 
                                }, 100);
 
                xml = null;
 
            }
        }
        // Timeout checker
        if ( s.timeout > 0 )
       {
            setTimeout(function(){
                // Check to see if the request is still happening
                if( !requestDone ) uploadCallback( "timeout" );
            }, s.timeout);
        }
        try
       {
 
           var form = jQuery('#' + formId);
           jQuery(form).attr('action', s.url);
           jQuery(form).attr('method', 'POST');
           jQuery(form).attr('target', frameId);
            if(form.encoding)
           {
              jQuery(form).attr('encoding', 'multipart/form-data');              
            }
            else
           {  
              jQuery(form).attr('enctype', 'multipart/form-data');       
            }       
            jQuery(form).submit();
 
        } catch(e)
       {         
            jQuery.handleError(s, xml, null, e);
        }
      
       jQuery('#' + frameId).load(uploadCallback );
        return {abort: function () {}}; 
 
    },
 
    uploadHttpData: function( r, type ) {
        var data = !type;
        data = type == "XML" || data ? r.responseXML : r.responseText;
        
        // If the type is "script", eval it in global context
        if ( type == "script" )
            jQuery.globalEval( data );
        // Get the JavaScript object, if JSON is used.
        if ( type == "json" )
        	if(data != ''){
        		data = (data.toString()).replace(/<[^>].*?>/g,"");
        		data=$j.parseJSON(data);
        	}
        // evaluate scripts within html
        if ( type == "html" )
            jQuery("<div>").html(data).evalScripts();
        
       
        return data;
    },
    
    makeUploadSetting:function(element){
		
		var id=element.attr("id");
    	
		var formId=element.attr("formId")?element.attr("formId"):"";
		
    	var setting={
				 url:  element.attr("url")!=undefined? element.attr("url")+'?_csrf='+$j("meta[name='_csrf']").attr("content"):'/demo/upload.json?_csrf='+$j("meta[name='_csrf']").attr("content"),  
		         secureuri: false,  
		         fileElementId: id,
		         formId: formId,
		         dataType: 'json',  
		         role: element.attr("role"),
		         model: element.attr("model"),
		         beforeSend: element.attr("beforeSend"),  
		         complete: element.attr("complete"),  
		         callback:element.attr("callback"),
		         error: function(data, status, e) {  
		        	 
		             alert("setting:"+e);  
		         } 
			};
    	return setting;
    },
    
    initUploadImg: function( element ) {
    	
    	var id = new Date().getTime()+"-"+Math.floor((Math.random()*100))%100;
    	
		element.attr("id",id);
		
		//再次加载该组件时, 判断type=file元素有没有初始化
		var hname = element.parent().find('input[name="'+element.attr("hName")+'"]').val();
		if(hname == undefined){
			element.parent().append('<input type="hidden" name="'+element.attr("hName")+'" value="'+element.attr("hValue")+'"/>');
		}
		
		if(element.attr("imgName")!=undefined&&element.attr("imgName")!=""){
		
			element.parent().append('<img name="'+element.attr("imgName")+'" src="'+element.attr("imgValue")+'"/>');
		}
		
		//如果有formId属性，则会在提交时才会上传图片
		if(element.attr("formId")!=undefined&&element.attr("formId")!=""){
			var formId=element.attr("formId");
			//form是否已绑定，为了不让事件重复绑定
			var imgEleCount=0;
			var imgCurCount=jQuery("#"+formId).attr("imgCurCount");
			if(imgCurCount!=undefined){
				imgEleCount=parseInt(imgCurCount);
			} 
			imgEleCount++;
			
			jQuery("#"+formId).attr("imgCurCount",imgEleCount);
			jQuery("#"+formId).attr("imgCount",imgEleCount);
			var formBind=jQuery("#"+formId).attr("formBind");
			
			if(true){
				jQuery("#"+formId).submit(function(){
					
					//总共有多少图片需要上传
					var imgcount=jQuery("#"+formId).attr("imgCount");
					//当前还剩多少图片未上传成功
					var imgCurCount=jQuery("#"+formId).attr("imgCurCount");
					var intImgCurCount=parseInt(imgCurCount);
					
					//不相等表示已经开始上传图片
					if(imgcount!=imgCurCount){
					
						//如果图片都上传成功了，则提交
						if(intImgCurCount<=0) {
							return true;
						}
						//表示没有上传成功
						else{
							return false;
						}
					}
					
					var setting=jQuery.makeUploadSetting(element);
						
					jQuery.ajaxFileUpload  
			        (  
			        		setting
			        );
					
					return false;
				});
				
				jQuery("#"+formId).attr("formBind","true");
			}
		}
    }
});

jQuery(document).ready(function() {
	//图片上传控件的数量
	
	
	
	jQuery(".imgUploadComponet").each(function(i,n){
		
		jQuery.initUploadImg(jQuery(this));
	});

	jQuery(".imgUploadComponet").on("change",function(){
		
		var element= jQuery(this);
		
		var setting=jQuery.makeUploadSetting(element);
		
		//如果没有formId才会绑定上传事件
		if(setting.formId==""){
			
			
			if(setting.beforeSend){
				jQuery.callFunction(eval(setting.beforeSend),[]);
			}
			jQuery.ajaxFileUpload  
	        (  
	        		setting
	        );
		}
		
	});
	
});