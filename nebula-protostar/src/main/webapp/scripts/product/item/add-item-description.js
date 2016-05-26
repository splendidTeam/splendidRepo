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
});