<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="No-cache" />
<meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />

<style type="text/css">
	textarea.margin {margin: 100px 10px 10px 10px}
</style>

<script type="text/javascript">
	$j(function() {

		loxia.init({
			debug : true,
			region : 'zh-CN'
		});

		nps.init();
	});
</script>
<title><spring:message code="cms.component.add.viewtemplate"/></title>
</head>
<body>
	<center>
		<textarea style="width:950px;height:450px" class="margin">
			${result}
		</textarea>
	</center>
</body>
</html>
