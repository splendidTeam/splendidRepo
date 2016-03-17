<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<textarea id="editor1" name="${param.contentName}" rows="20" cols="120">
${param.content}
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
		var editor = CKEDITOR.replace( 'editor1',
			{
				toolbar : 'Full' ,
				filebrowserImageUploadUrl:'/img/upload.json'
			});
		<c:if test="${param.imageUpload==1}">
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
		</c:if>
</script>
