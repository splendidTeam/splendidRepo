::	
::		一键式 clean install nebula-helix
::
::step1 基于nebula parent目录 svn更新
::
::step2 双击执行这个脚本,会自动 install parent 项目, 
::		以及 nebula-utilities,nebula-repo,nebula-helix
::		不需要自己再 install project-by-project了

::--by feilong 2016-05-07

mode con cols=150 lines=5600

@echo off
rem 批量install nebula-helix
title install nebula-helix

echo 
echo create by feilong
echo 2016-05-07

call "%~dp0/batchInstall.bat" "nebula-utilities,nebula-repo,nebula-helix"


pause