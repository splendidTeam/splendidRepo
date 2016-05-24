/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
    config.uiColor = '#27A2C5';
	config.language	= 'zh-cn' ;
	config.width = '600px'; // 高度   
	config.height = '300px'; // 高度   
	
	//保留i标签
	config.protectedSource.push(/<i[^>]><\/i>/g);
	CKEDITOR.dtd.$removeEmpty['i'] = false;
	//删除自动添加的p标签
	config.shiftEnterMode = CKEDITOR.ENTER_P;
	config.enterMode = CKEDITOR.ENTER_BR;

	config.allowedContent = true;
};
