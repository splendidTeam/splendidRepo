<%-- <%@include file="/pages/commons/common.jsp"%> --%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- <%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%> --%>
<script type="text/javascript" src="${base }/scripts/ckeditor/ckeditor.js"></script>
<textarea id="editor1" name="content" rows="20" cols="120">

</textarea>
<style type="text/css">

	.cke_button_myDialogCmd .cke_icon
	{
		display: none !important;
	}

	.cke_button_myDialogCmd .cke_label
	{
		display: inline !important;
	}
	
</style>

 <script type="text/javascript">
 $j(document).ready(function() {
		loxia.init({
			debug : true,
			region : 'zh-CN'
		});
		nps.init();
 });
		var editor = CKEDITOR.replace( 'editor1',
			{
				toolbar : [['Source','-','NewPage','-','Templates'],
			           ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
			           ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
			           ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'HiddenField'],
			           '/',
			           ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
			           ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
			           ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
			           ['Link','Unlink','Anchor'],
			           ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
			           '/',
			           ['Styles','Format','Font','FontSize'],
			           ['TextColor','BGColor'],
			           ['ShowBlocks','-','About']] ,
				filebrowserImageUploadUrl:'/img/upload.json'
			});
/* 		<c:if test="${param.imageUpload==1}">
		editor.on( 'pluginsLoaded', function( ev )
			{
				if ( !CKEDITOR.dialog.exists( 'myDialog' ) )
				{
					CKEDITOR.dialog.add( 'myDialog', function( editor )
							{
								return {
									title : '图片上传',
									minWidth : 450,
									minHeight : 200,
									contents : [
										{
											id : 'tab1',
											label : 'First Tab',
											title : 'First Tab',
											elements :
											[
												{
													type:'vbox',
													height:'250px',
													children:[
																{	type:'html',
																	style:'width:95%;',
																	html:'<iframe id ="uploadIfr" frameborder="0" name="uploadIfr" width="300" height="100" src="/common/upload.jsp"></iframe>'
																}
															]
												}
											]
										}
									]
								};
							} );
				}
				editor.addCommand( 'myDialogCmd', new CKEDITOR.dialogCommand( 'myDialog' ) );
				editor.ui.addButton( 'MyButton',
					{
						label : '图片上传',
						title : '图片上传',
						command : 'myDialogCmd'
					} );
			});
		</c:if> */
</script>

