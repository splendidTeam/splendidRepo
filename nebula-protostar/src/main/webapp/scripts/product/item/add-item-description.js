var editors =[];

$j(document).ready(function() {
	if (i18nOnOff) {
		var i18nSize = i18nLangs.length;
		editors = [];
		for (var i = 0; i < i18nSize; i++) {
			var editor = CKEDITOR.replace('editor' + i, {
				toolbar : 'Full',
				filebrowserImageUploadUrl : '/img/upload.json'
			});
			editors.push(editor);
		}
	} else {
		editors = [];
		var editor = CKEDITOR.replace('editor', {
			toolbar : 'Full',
			filebrowserImageUploadUrl : '/img/upload.json'
		});
		editors.push(editor);
	}
	
	// 添加商品描述表单验证方法
	var baseInfoValidator = new FormValidator('', 100, function(){
		
		// 填充商品描述字段隐藏域
		if(i18nOnOff){
			for ( var i = 0; i < editors.length; i++) {
				var editor = editors[i];
				var content=editor.getData();
				$j("textarea[name='itemCommand.description.values["+i+"]']").val(content);
			}
		}else{
			for ( var i = 0; i < editors.length; i++) {
				var editor = editors[i];
				var content=editor.getData();
				$j("textarea[name='itemCommand.description.value']").val(content);
			}
		}
    	
    	return loxia.SUCCESS;
    });
    formValidateList.push(baseInfoValidator);
});