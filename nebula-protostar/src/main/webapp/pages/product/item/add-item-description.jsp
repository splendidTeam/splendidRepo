<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript" src="${base}/scripts/product/item/add-item-description.js"></script>

<script type="text/javascript">
		
		<c:if test="${param.imageUpload==1}">
		editor1.on( 'pluginsLoaded', function(ev)
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
				editor1.addCommand( 'myDialogCmd', new CKEDITOR.dialogCommand( 'myDialog' ) );
				editor1.ui.addButton( 'MyButton',
					{
						label : '图片上传',
						title : '图片上传',
						command : 'myDialogCmd'
					} );
			});
		</c:if>
</script>

<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.add.description" />
</div>

<div class="ui-block-content border-grey">

	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style=""><spring:message code="item.update.sketch" /></label>
				<textarea rows="10px"
					name="itemCommand.sketch.values[${status.index}]" loxiaType="input"
					style="width: 600px;">${ sketch }</textarea>
				<input class="i18n-lang" type="text"
					name="itemCommand.sketch.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>

		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style=""><spring:message
						code="item.update.lable.description" /></label>
				<div style="float: left; margin-left: 0px">
					<textarea id="editor${status.index}"
						name="itemCommand.description.values[${status.index}]" rows="20"
						cols="120">
			${description}
		 </textarea>
					<input class="i18n-lang" type="text"
						name="itemCommand.description.langs[${status.index}]"
						value="${i18nLang.key}" />
				</div>
				<span style="display: block; float: left;">${i18nLang.value}</span>
			</div>
		</c:forEach>
	</c:if>

	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label style=""><spring:message code="item.update.sketch" /></label>
			<div>
				<textarea rows="10px" name="itemCommand.sketch.value"
					loxiaType="input" style="width: 600px;">${ sketch }</textarea>
			</div>
		</div>

		<div class="ui-block-line ">
			<label style=""><spring:message
					code="item.update.lable.description" /></label>
			<div>
				<textarea id="editor" name="itemCommand.description.value" rows="20"
					cols="120">
			${description}
		  </textarea>
			</div>
		</div>
	</c:if>
</div>