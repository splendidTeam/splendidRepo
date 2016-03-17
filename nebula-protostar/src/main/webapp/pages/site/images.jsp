<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/ckfinder/ckfinder.js?${version_js}"></script>

<%-- 
<script type="text/javascript" src="${base}/scripts/cms/module/cmsModuleTemplate.js"></script>
<script type="text/javascript">
	var customBaseUrl= "${customBaseUrl}"; 
</script>
--%>

</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">图片管理</div>
</div>

<%-- begin section --%>
<div class="section">
	<div id="ckfinder" style="width:100%;height:830px;"></div>
	<script type="text/javascript">
	//<![CDATA[
	(function()
	{
		var config = {};
		var get = CKFinder.tools.getUrlParam;
		var getBool = function( v )
		{
			var t = get( v );

			if ( t === null )
				return null;

			return t == '0' ? false : true;
		};

		var tmp;
		if ( tmp = get( 'configId' ) )
		{
			var win = window.opener || window;
			try
			{
				while ( ( !win.CKFinder || !win.CKFinder._.instanceConfig[ tmp ] ) && win != window.top )
					win = win.parent;

				if ( win.CKFinder._.instanceConfig[ tmp ] )
					config = CKFINDER.tools.extend( {}, win.CKFinder._.instanceConfig[ tmp ] );
			}
			catch(e) {}
		}

		if ( tmp = get( 'basePath' ) )
			CKFINDER.basePath = tmp;

		if ( tmp = get( 'startupPath' ) || get( 'start' ) )
			config.startupPath = decodeURIComponent( tmp );

		config.id = get( 'id' ) || '';

		if ( ( tmp = getBool( 'rlf' ) ) !== null )
			config.rememberLastFolder = tmp;

		if ( ( tmp = getBool( 'dts' ) ) !== null )
			config.disableThumbnailSelection = tmp;

		if ( tmp = get( 'data' ) )
			config.selectActionData = tmp;

		if ( tmp = get( 'tdata' ) )
			config.selectThumbnailActionData = tmp;

		if ( tmp = get( 'type' ) )
			config.resourceType = tmp;

		if ( tmp = get( 'skin' ) )
			config.skin = tmp;

		if ( tmp = get( 'langCode' ) )
			config.language = tmp;

		if ( typeof( config.selectActionFunction ) == 'undefined' )
		{
			// Try to get desired "File Select" action from the URL.
			var action;
			if ( tmp = get( 'CKEditor' ) )
			{
				if ( tmp.length )
					action = 'ckeditor';
			}
			if ( !action )
				action = get( 'action' );

			var parentWindow = ( window.parent == window ) ? window.opener : window.parent;
			switch ( action )
			{
				case 'js':
					var actionFunction = get( 'func' );
					if ( actionFunction && actionFunction.length > 0 )
						config.selectActionFunction = parentWindow[ actionFunction ];

					actionFunction = get( 'thumbFunc' );
					if ( actionFunction && actionFunction.length > 0 )
						config.selectThumbnailActionFunction = parentWindow[ actionFunction ];
					break ;

				case 'ckeditor':
					var funcNum = get( 'CKEditorFuncNum' );
					if ( parentWindow['CKEDITOR'] )
					{
						config.selectActionFunction = function( fileUrl, data )
						{
							parentWindow['CKEDITOR'].tools.callFunction( funcNum, fileUrl, data );
						};

						config.selectThumbnailActionFunction = config.selectActionFunction;
					}
					break;

				default:
					if ( parentWindow && parentWindow['FCK'] && parentWindow['SetUrl'] )
					{
						action = 'fckeditor' ;
						config.selectActionFunction = parentWindow['SetUrl'];

						if ( !config.disableThumbnailSelection )
							config.selectThumbnailActionFunction = parentWindow['SetUrl'];
					}
					else
						action = null ;
			}
			config.action = action;
		}

		// Always use 100% width and height when nested using this middle page.
		config.width = config.height = '100%';

		var ckfinder = new CKFinder( config );
		ckfinder.replace( 'ckfinder', config );
	})();
	//]]>
	</script>
</div>
<%-- end section --%>

<div id="showImage"></div>

</body>
</html>